package cdm.legalagreement.contract.processor;

import cdm.base.staticdata.party.AncillaryRole;
import cdm.base.staticdata.party.AncillaryRoleEnum;
import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyEnum;
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

import static cdm.base.staticdata.party.AncillaryRole.AncillaryRoleBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndUpdateMappings;

/**
 * Helper class for FpML mapper processors.
 * <p>
 * Handles Counterparty and AncillaryRole.  A new instance is created for each TradableProduct.
 */
public class PartyMappingHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(PartyMappingHelper.class);

	static final String PARTY_MAPPING_HELPER_KEY = "PARTY_MAPPING_HELPER";
	public static final RosettaPath PRODUCT_SUB_PATH = RosettaPath.valueOf("tradableProduct").newSubPath("product");

	private final Map<String, CounterpartyEnum> partyExternalReferenceToCounterpartyEnumMap;
	private final List<Mapping> mappings;
	private final CompletableFuture<Map<String, CounterpartyEnum>> bothCounterpartiesCollected = new CompletableFuture<>();
	private final TradableProductBuilder tradableProductBuilder;
	private final Function<String, Optional<String>> translator;
	private final ExecutorService executor;
	private final List<CompletableFuture<?>> invokedTasks;

	PartyMappingHelper(MappingContext context, TradableProductBuilder tradableProductBuilder, Function<String, Optional<String>> translator) {
		this.mappings = context.getMappings();
		this.executor = context.getExecutor();
		this.invokedTasks = context.getInvokedTasks();
		this.tradableProductBuilder = tradableProductBuilder;
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
	 * Get an instance of this party mapping helper from the MappingContext params, or if not found throw exception.
	 */
	@NotNull
	public synchronized static PartyMappingHelper getInstanceOrThrow(MappingContext mappingContext) {
		return getInstance(mappingContext).orElseThrow(() -> new IllegalStateException("PartyMappingHelper not found."));
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
	 * Waits until the partyExternalReference to CounterpartyEnum map is ready, then adds both Counterparty instances to TradableProductBuilder.
	 */
	void addCounterparties() {
		// when the tradableProductBuilder has been supplied and both counterparties have been collected, update tradableProduct.counterparties
		invokedTasks.add(bothCounterpartiesCollected
				.thenAcceptAsync(map -> {
							LOGGER.info("Setting TradableProduct.counterparties");
							tradableProductBuilder.clearCounterparty()
									.addCounterparty(map.entrySet().stream()
											.map(extRefCounterpartyEntry -> Counterparty.builder()
													.setRole(extRefCounterpartyEntry.getValue())
													.setPartyReferenceBuilder(ReferenceWithMetaParty.builder().setExternalReference(extRefCounterpartyEntry.getKey()))
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
	 * Cashflow payout is problematic because it is defined inside the Product yet can contain either a counterparty or ancillary role.
	 * Model should be refactored so cashflow payout only allows counterparties, and any 3rd party payments are defined outside the
	 * Product (e.g. SettlementTerms, or some new other party payment model similar to FpML).
	 * <p>
	 * If both counterparties have not yet been computed, get or create counterparty based on the party reference.
	 * If both counterparties have already been computed, then add as an ancillary role.
	 */
	public void computeCashflowParty(RosettaPath modelPath,
			Path synonymPath,
			Consumer<CounterpartyEnum> counterpartySetter,
			Consumer<AncillaryRoleEnum> ancillaryRoleSetter,
			AncillaryRoleEnum ancillaryRoleEnum) {

		if (modelPath.containsPath(PRODUCT_SUB_PATH)) {
			setValueAndUpdateMappings(synonymPath,
					partyExternalReference -> {
						String translatedPartyRef = translatePartyExternalReference(partyExternalReference);
						Optional<CounterpartyEnum> counterparty = getOrCreateCounterpartyEnum(translatedPartyRef);
						if (counterparty.isPresent()) {
							counterpartySetter.accept(counterparty.get());
						} else {
							LOGGER.info("Adding {} for {}", ancillaryRoleEnum, translatedPartyRef);
							ancillaryRoleSetter.accept(ancillaryRoleEnum);
							// add to tradableProduct
							addAncillaryRole(translatedPartyRef, ancillaryRoleEnum);
						}
					},
					mappings,
					modelPath);
		}
	}

	/**
	 * Set ancillaryRoleEnum with given ancillaryRoleSetter and add associated partyExternalReference to tradableProduct.ancillaryRole.
	 */
	public void setAncillaryRoleEnum(RosettaPath modelPath,
			Path synonymPath,
			Consumer<AncillaryRoleEnum> ancillaryRoleSetter,
			AncillaryRoleEnum ancillaryRoleEnum) {

		if (modelPath.containsPath(PRODUCT_SUB_PATH)) {
			setValueAndUpdateMappings(synonymPath,
					partyExternalReference -> {
						String translatedPartyRef = translatePartyExternalReference(partyExternalReference);
						LOGGER.info("Adding {} for {}", ancillaryRoleEnum, translatedPartyRef);
						ancillaryRoleSetter.accept(ancillaryRoleEnum);
						// add to tradableProduct
						addAncillaryRole(translatedPartyRef, ancillaryRoleEnum);
					},
					mappings,
					modelPath);
		}
	}

	/**
	 * Add AncillaryRoleEnum and associated partyExternalReference to tradableProduct.ancillaryRole.
	 */
	public void addAncillaryRole(String partyExternalReference, AncillaryRoleEnum relatedPartyEnum) {
		synchronized (tradableProductBuilder) {
			LOGGER.info("Adding {} as {} to TradableProduct.relatedParties", partyExternalReference, relatedPartyEnum);
			List<AncillaryRoleBuilder> relatedParties = Optional.ofNullable(tradableProductBuilder.getAncillaryRole())
					.orElse(new ArrayList<>());
			Optional<AncillaryRoleBuilder> relatedPartyReference = relatedParties.stream()
					.filter(r -> relatedPartyEnum == r.getRole())
					.findFirst();
			if (relatedPartyReference.isPresent()) {
				// Update existing entry
				relatedPartyReference.get()
						.addPartyReference(ReferenceWithMetaParty.builder().setExternalReference(partyExternalReference).build());
			} else {
				// Add new entry
				relatedParties.add(AncillaryRole.builder()
						.setRole(relatedPartyEnum)
						.addPartyReferenceBuilder(ReferenceWithMetaParty.builder().setExternalReference(partyExternalReference)));
			}
			// Clear related parties, and re-add sorted list so we don't get diffs on the list order on each ingestion
			tradableProductBuilder
					.clearAncillaryRole()
					.addAncillaryRole(relatedParties.stream()
							.map(AncillaryRoleBuilder::build)
							.sorted(Comparator.comparing(AncillaryRole::getRole))
							.collect(Collectors.toList()));
		}
	}
}
