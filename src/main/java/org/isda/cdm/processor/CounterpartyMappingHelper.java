package org.isda.cdm.processor;

import cdm.base.staticdata.party.BuyerSeller;
import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyEnum;
import cdm.base.staticdata.party.PayerReceiver;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cdm.base.staticdata.party.PayerReceiver.PayerReceiverBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;
import static com.regnosys.rosetta.common.util.StringExtensions.toFirstUpper;
import static org.isda.cdm.TradableProduct.TradableProductBuilder;

/**
 * Helper class for FpML mapper processors.
 *
 * Collects party references and if inside the product definition assigns a CounterpartyEnum value.
 */
public class CounterpartyMappingHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(CounterpartyMappingHelper.class);

	public static final String COUNTERPARTY_MAPPING_HELPER_KEY = "COUNTERPARTY_MAPPING_HELPER";
	public static final RosettaPath PRODUCT_SUB_PATH = RosettaPath.valueOf("tradableProduct").newSubPath("product");

	private final Map<String, CounterpartyEnum> partyExternalReferenceToCounterpartyEnumMap;
	private final List<Mapping> mappings;
	private final CompletableFuture<Void> bothCounterpartiesCollected = new CompletableFuture<>();

	CounterpartyMappingHelper(List<Mapping> mappings) {
		this.mappings = mappings;
		this.partyExternalReferenceToCounterpartyEnumMap = new LinkedHashMap<>();
	}

	/**
	 * Get an instance of this counterparty mapping helper from the MappingContext params.
	 */
	@NotNull
	public synchronized static Optional<CounterpartyMappingHelper> getInstance(MappingContext mappingContext) {
		return Optional.ofNullable((CounterpartyMappingHelper) mappingContext.getMappingParams().get(COUNTERPARTY_MAPPING_HELPER_KEY));
	}

	/**
	 * Maps party external reference to CounterpartyEnum, then sets on the given builder.
	 */
	public void setCounterpartyEnum(RosettaModelObjectBuilder builder,
			RosettaPath modelPath,
			Path synonymPath,
			Function<String, Optional<String>> externalRefTranslator) {
		if (modelPath.containsPath(PRODUCT_SUB_PATH)) {
			setValueAndOptionallyUpdateMappings(
					synonymPath.addElement("href"), // synonym path to party external reference
					(externalRef) -> Optional.of(externalRef)
							// apply additional external reference translation function if provided
							.map(x -> Optional.ofNullable(externalRefTranslator).flatMap(f -> f.apply(x)).orElse(x))
							// translate to counterparty enum
							.flatMap(this::getOrCreateCounterpartyEnum)
							// determine counterparty enum and add to builder
							.filter(cpty -> setCounterpartyEnumReflectively(builder, modelPath.getElement().getPath(), cpty))
							.isPresent(),
					mappings,
					modelPath);
		}
	}

	/**
	 * Waits until the partyExternalReference to CounterpartyEnum map is ready, sets CounterpartyEnum based on the partyReferences, and
	 * then runs the thenConsumer.  Used by other mappers that depend on the party payer / receiver / buyer / seller, so those mappers can
	 * run only once the party data is stable.
	 */
	void setCounterpartyEnumThen(PayerReceiverBuilder payerReceiverBuilder, Consumer<PayerReceiverBuilder> thenConsumer) {
		bothCounterpartiesCollected
				.thenRun(() -> CompletableFuture.supplyAsync(() -> payerReceiverBuilder)
						.thenApply(builder -> {
							// Update payer from payerPartyReference
							Optional.ofNullable(builder.getPayerPartyReference())
									.map(ReferenceWithMetaParty.ReferenceWithMetaPartyBuilder::getExternalReference)
									.flatMap(this::getOrCreateCounterpartyEnum)
									.ifPresent(counterpartyEnum -> {
										builder.setPayer(counterpartyEnum);
										builder.setPayerPartyReferenceBuilder(ReferenceWithMetaParty.builder());
									});
							// Update receiver from receiverPartyReference
							Optional.ofNullable(builder.getReceiverPartyReference())
									.map(ReferenceWithMetaParty.ReferenceWithMetaPartyBuilder::getExternalReference)
									.flatMap(this::getOrCreateCounterpartyEnum)
									.ifPresent(counterpartyEnum -> {
										builder.setReceiver(counterpartyEnum);
										builder.setReceiverPartyReferenceBuilder(ReferenceWithMetaParty.builder());
									});
							return builder;
						})
						.thenAccept(thenConsumer));
	}

	/**
	 * Waits until the partyExternalReference to CounterpartyEnum map is ready, then adds both Counterparty instances to TradableProductBuilder.
	 */
	void addCounterparties(TradableProductBuilder tradableProductBuilder) {
		bothCounterpartiesCollected
				.thenRun(() -> CompletableFuture.supplyAsync(() -> tradableProductBuilder)
						.thenAccept(builder -> {
							LOGGER.info("Setting TradableProduct.counterparties");
							builder.clearCounterparties()
									.addCounterparties(partyExternalReferenceToCounterpartyEnumMap.entrySet().stream()
											.map(extRefCounterpartyEntry -> Counterparty.builder()
													.setCounterparty(extRefCounterpartyEntry.getValue())
													.setPartyBuilder(ReferenceWithMetaParty.builder()
															.setExternalReference(extRefCounterpartyEntry.getKey()))
													.build())
											.collect(Collectors.toList()));
						}));
	}

	/**
	 * Looks up externalReference in the map and returns the corresponding CounterpartyEnum.
	 */
	private Optional<CounterpartyEnum> getOrCreateCounterpartyEnum(String externalReference) {
		CounterpartyEnum counterpartyEnum = partyExternalReferenceToCounterpartyEnumMap.computeIfAbsent(
				externalReference,
				(key) -> {
					if (partyExternalReferenceToCounterpartyEnumMap.isEmpty()) {
						LOGGER.info("Adding CounterpartyEnum.PARTY_1 for {}", externalReference);
						return CounterpartyEnum.PARTY_1;
					} else if (partyExternalReferenceToCounterpartyEnumMap.size() == 1) {
						LOGGER.info("Adding CounterpartyEnum.PARTY_2 for {}", externalReference);
						return CounterpartyEnum.PARTY_2;
					} else {
						LOGGER.error("Not translating external reference {} to a CounterpartyEnum because 2 counterparties already exist", externalReference);
						return null;
					}
				});

		// If both counterparties have been added to the map, then complete the future
		if (!bothCounterpartiesCollected.isDone() && partyExternalReferenceToCounterpartyEnumMap.size() == 2) {
			bothCounterpartiesCollected.complete(null);
		}

		return Optional.ofNullable(counterpartyEnum);
	}

	private boolean setCounterpartyEnumReflectively(RosettaModelObjectBuilder builder, String attribute, CounterpartyEnum counterpartyEnum) {
		try {
			// set CounterpartyEnum
			builder.getClass()
					.getMethod("set" + toFirstUpper(attribute), CounterpartyEnum.class)
					.invoke(builder, counterpartyEnum);
			// blank out partyReference if builder is a BuyerSeller or PayerReceiver
			if (builder instanceof BuyerSeller.BuyerSellerBuilder || builder instanceof PayerReceiver.PayerReceiverBuilder) {
				builder.getClass()
						.getMethod("set" + toFirstUpper(attribute) + "PartyReference", ReferenceWithMetaParty.class)
						.invoke(builder, ReferenceWithMetaParty.builder().build());
			}
			return true;
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			LOGGER.error("Failed to set CounterpartyEnum {}", counterpartyEnum, e);
			return false;
		}
	}
}
