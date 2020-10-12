package cdm.legalagreement.contract.processor;

import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyEnum;
import cdm.base.staticdata.party.RelatedPartyEnum;
import cdm.base.staticdata.party.RelatedPartyReference;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.product.template.TradableProduct.TradableProductBuilder;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cdm.base.staticdata.party.RelatedPartyReference.RelatedPartyReferenceBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndUpdateMappings;

/**
 * Helper class for FpML mapper processors.
 * <p>
 * Handles Counterparty and RelatedParties.  A new instance is created for each TradableProduct.
 */
public class PartyMappingHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(PartyMappingHelper.class);

	static final String PARTY_MAPPING_HELPER_KEY = "PARTY_MAPPING_HELPER";
	public static final RosettaPath PRODUCT_SUB_PATH = RosettaPath.valueOf("tradableProduct").newSubPath("product");

	private final Map<String, CounterpartyEnum> partyExternalReferenceToCounterpartyEnumMap;
	private final List<Mapping> mappings;
	private final CompletableFuture<Map<String, CounterpartyEnum>> bothCounterpartiesCollected = new CompletableFuture<>();
	private final CompletableFuture<TradableProductBuilder> tradableProductBuilderSupplied = new CompletableFuture<>();
	private final Function<String, Optional<String>> translator;
	private final ExecutorService executor;
	private final List<CompletableFuture<?>> invokedTasks;

	PartyMappingHelper(MappingContext context, Function<String, Optional<String>> translator) {
		this.mappings = context.getMappings();
		this.executor = context.getExecutor();
		this.invokedTasks = context.getInvokedTasks();
		this.translator = translator;
		this.partyExternalReferenceToCounterpartyEnumMap = new LinkedHashMap<>();
	}

	/**
	 * Get an instance of this party mapping helper from the MappingContext params.
	 */
	@NotNull
	public synchronized static Optional<PartyMappingHelper> getInstance(MappingContext mappingContext) {
		return Optional.ofNullable((PartyMappingHelper) mappingContext.getMappingParams().get(PARTY_MAPPING_HELPER_KEY));
	}

	/**
	 * Maps party external reference to CounterpartyEnum, then sets on the given builder.
	 */
	public void setCounterpartyEnum(RosettaPath modelPath, Path synonymPath, Consumer<CounterpartyEnum> setter) {
		if (modelPath.containsPath(PRODUCT_SUB_PATH)) {
			setValueAndOptionallyUpdateMappings(
					synonymPath.addElement("href"), // synonym path to party external reference
					(partyExternalReference) -> {
						// Map externalRef to CounterpartyEnm and update builder object
						Optional<CounterpartyEnum> counterpartyEnum =
								getOrCreateCounterpartyEnum(translatePartyExternalReference(partyExternalReference));
						counterpartyEnum.ifPresent(setter);
						return counterpartyEnum.isPresent(); // return true to update synonym mapping stats to success
					},
					mappings,
					modelPath);
		}
	}

	/**
	 * Apply party external reference translation if translator provided
	 */
	public String translatePartyExternalReference(String partyExternalReference) {
		return Optional.ofNullable(translator)
				.flatMap(t -> t.apply(partyExternalReference))
				.orElse(partyExternalReference);
	}

	/**
	 * Provides a TradableProductBuilder instance so the counterparties and relatedParties can be added.
	 */
	void supplyTradableProductBuilder(TradableProductBuilder tradableProductBuilder) {
		// complete future so any updates can happen
		tradableProductBuilderSupplied.complete(tradableProductBuilder);
	}

	/**
	 * Waits until the partyExternalReference to CounterpartyEnum map is ready, then adds both Counterparty instances to TradableProductBuilder.
	 */
	void addCounterparties() {
		// when the tradableProductBuilder has been supplied and both counterparties have been collected, update tradableProduct.counterparties
		invokedTasks.add(tradableProductBuilderSupplied
				.thenAcceptBothAsync(bothCounterpartiesCollected,
						(tradableProductBuilder, map) -> {
							LOGGER.info("Setting TradableProduct.counterparties");
							tradableProductBuilder.clearCounterparties()
									.addCounterparties(map.entrySet().stream()
											.map(extRefCounterpartyEntry -> Counterparty.builder()
													.setCounterparty(extRefCounterpartyEntry.getValue())
													.setPartyReferenceBuilder(
															ReferenceWithMetaParty.builder().setExternalReference(extRefCounterpartyEntry.getKey()))
													.build())
											.collect(Collectors.toList()));
						}, executor));
	}

	public CompletableFuture<Map<String, CounterpartyEnum>> getBothCounterpartiesCollectedFuture() {
		return bothCounterpartiesCollected;
	}

	/**
	 * Looks up externalReference in the map and returns the corresponding CounterpartyEnum.
	 */
	private Optional<CounterpartyEnum> getOrCreateCounterpartyEnum(String externalReference) {
		synchronized (partyExternalReferenceToCounterpartyEnumMap) {
			Optional<CounterpartyEnum> counterpartyEnum = Optional.ofNullable(partyExternalReferenceToCounterpartyEnumMap.computeIfAbsent(
					externalReference,
					(key) -> {
						if (partyExternalReferenceToCounterpartyEnumMap.isEmpty()) {
							LOGGER.info("Adding CounterpartyEnum.PARTY_1 for {}", externalReference);
							return CounterpartyEnum.PARTY_1;
						} else if (partyExternalReferenceToCounterpartyEnumMap.size() == 1) {
							LOGGER.info("Adding CounterpartyEnum.PARTY_2 for {}", externalReference);
							return CounterpartyEnum.PARTY_2;
						} else {
							return null;
						}
					}));
			// If both counterparties have been added to the map, then complete the future
			if (!bothCounterpartiesCollected.isDone() && partyExternalReferenceToCounterpartyEnumMap.size() == 2) {
				LOGGER.debug("Both counterparties collected");
				bothCounterpartiesCollected.complete(partyExternalReferenceToCounterpartyEnumMap);
			}
			return counterpartyEnum;
		}
	}

	/**
	 * Wait until both counterparties have been computed then determine if this party is a counterparty or related party and update accordingly.
	 */
	public void setCounterpartyOrRelatedParty(RosettaPath modelPath,
			Path synonymPath,
			Consumer<CounterpartyEnum> counterpartySetter,
			Consumer<RelatedPartyEnum> relatedPartySetter,
			RelatedPartyEnum relatedPartyEnum) {

		invokedTasks.add(getBothCounterpartiesCollectedFuture()
				.thenAcceptAsync(map ->
								setCounterpartyOrRelatedParty(modelPath,
										synonymPath,
										counterpartySetter,
										relatedPartySetter,
										relatedPartyEnum,
										(ref) -> Optional.ofNullable(map.get(ref))),
						executor));
	}

	/**
	 * Cashflow payout is problematic because it is defined inside the Product yet can contain either a counterparty or related party.
	 * Model should be refactored so cashflow payout only allows counterparties, and any 3rd party payments are defined outside the Product (e.g. SettlementTerms).
	 * <p>
	 * If both counterparties have not yet been computed, get or create counterparty based on the party reference.
	 * If both counterparties have already been computed, then add as a related party.
	 */
	public void computeCashflowCounterpartyOrRelatedParty(RosettaPath modelPath,
			Path synonymPath,
			Consumer<CounterpartyEnum> counterpartySetter,
			Consumer<RelatedPartyEnum> relatedPartySetter,
			RelatedPartyEnum relatedPartyEnum) {

		setCounterpartyOrRelatedParty(modelPath,
				synonymPath,
				counterpartySetter,
				relatedPartySetter,
				relatedPartyEnum,
				this::getOrCreateCounterpartyEnum);
	}

	private void setCounterpartyOrRelatedParty(RosettaPath modelPath,
			Path synonymPath,
			Consumer<CounterpartyEnum> counterpartySetter,
			Consumer<RelatedPartyEnum> relatedPartySetter,
			RelatedPartyEnum relatedPartyEnum,
			Function<String, Optional<CounterpartyEnum>> partyToCounterpartyFunc) {

		if (modelPath.containsPath(PRODUCT_SUB_PATH)) {
			setValueAndUpdateMappings(synonymPath,
					partyExternalReference -> {
						Optional<CounterpartyEnum> counterparty = partyToCounterpartyFunc.apply(translatePartyExternalReference(partyExternalReference));
						if (counterparty.isPresent()) {
							counterpartySetter.accept(counterparty.get());
						} else {
							LOGGER.info("Adding {} for {}", relatedPartyEnum, partyExternalReference);
							relatedPartySetter.accept(relatedPartyEnum);
							addRelatedParties(partyExternalReference, relatedPartyEnum);
						}
					},
					mappings,
					modelPath);
		}
	}

	/**
	 * Add RelatedPartyEnum and associated partyExternalReference to tradableProduct.relatedParties.
	 */
	public void addRelatedParties(String partyExternalReference, RelatedPartyEnum relatedPartyEnum) {
		invokedTasks.add(tradableProductBuilderSupplied
				.thenAcceptAsync(tradableProductBuilder -> {
							synchronized (tradableProductBuilder) {
								LOGGER.info("Adding {} as {} to TradableProduct.relatedParties", partyExternalReference, relatedPartyEnum);
								List<RelatedPartyReferenceBuilder> relatedParties = Optional.ofNullable(tradableProductBuilder.getRelatedParties())
										.orElse(new ArrayList<>());
								Optional<RelatedPartyReferenceBuilder> relatedPartyReference = relatedParties.stream()
										.filter(r -> relatedPartyEnum == r.getRelatedParty())
										.findFirst();
								if (relatedPartyReference.isPresent()) {
									// Update existing entry
									relatedPartyReference.get()
											.addPartyReference(ReferenceWithMetaParty.builder().setExternalReference(partyExternalReference).build());
								} else {
									// Add new entry
									relatedParties.add(RelatedPartyReference.builder()
											.setRelatedParty(relatedPartyEnum)
											.addPartyReferenceBuilder(ReferenceWithMetaParty.builder().setExternalReference(partyExternalReference)));
								}
								// Clear related parties, and re-add sorted list so we don't get diffs on the list order on each ingestion
								tradableProductBuilder
										.clearRelatedParties()
										.addRelatedParties(relatedParties.stream()
												.map(RelatedPartyReferenceBuilder::build)
												.sorted(Comparator.comparing(RelatedPartyReference::getRelatedParty))
												.collect(Collectors.toList()));
							}
						},
						executor));
	}
}
