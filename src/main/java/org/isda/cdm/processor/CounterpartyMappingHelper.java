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
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;

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

	static final String COUNTERPARTY_MAPPING_HELPER_KEY = "COUNTERPARTY_MAPPING_HELPER";
	public static final RosettaPath PRODUCT_SUB_PATH = RosettaPath.valueOf("tradableProduct").newSubPath("product");

	private final Map<String, CounterpartyEnum> partyExternalReferenceToCounterpartyEnumMap;
	private final List<Mapping> mappings;
	private final CompletableFuture<Map<String, CounterpartyEnum>> bothCounterpartiesCollected = new CompletableFuture<>();
	private final ExecutorService executor;

	CounterpartyMappingHelper(MappingContext context) {
		this.mappings = context.getMappings();
		this.executor = context.getExecutor();
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
					(externalRef) -> {
						// Map externalRef to CounterpartyEnm and update builder object
						boolean updated = Optional.of(externalRef)
								// apply additional external reference translation function if provided
								.map(x -> Optional.ofNullable(externalRefTranslator).flatMap(f -> f.apply(x)).orElse(x))
								// translate to counterparty enum
								.flatMap(this::getOrCreateCounterpartyEnum)
								// determine counterparty enum and add to builder
								.filter(cpty -> setCounterpartyEnumReflectively(builder, modelPath.getElement().getPath(), cpty))
								.isPresent();
						// If both counterparties have been added to the map, then complete the future
						if (!bothCounterpartiesCollected.isDone() && partyExternalReferenceToCounterpartyEnumMap.size() == 2) {
							bothCounterpartiesCollected.complete(partyExternalReferenceToCounterpartyEnumMap);
						}
						// Return true to update synonym mapping stats
						return updated;
					},
					mappings,
					modelPath);
		}
	}

	/**
	 * Waits until the partyExternalReference to CounterpartyEnum map is ready, then adds both Counterparty instances to TradableProductBuilder.
	 */
	void addCounterparties(TradableProductBuilder tradableProductBuilder) {
		bothCounterpartiesCollected
				.thenAcceptAsync(map -> {
					LOGGER.info("Setting TradableProduct.counterparties");
					tradableProductBuilder.clearCounterparties()
							.addCounterparties(map.entrySet().stream()
									.map(extRefCounterpartyEntry -> Counterparty.builder()
											.setCounterparty(extRefCounterpartyEntry.getValue())
											.setPartyBuilder(ReferenceWithMetaParty.builder()
													.setExternalReference(extRefCounterpartyEntry.getKey()))
											.build())
									.collect(Collectors.toList()));
				}, executor);
	}

	CompletableFuture<Map<String, CounterpartyEnum>> getBothCounterpartiesCollectedFuture() {
		return bothCounterpartiesCollected;
	}

	/**
	 * Looks up externalReference in the map and returns the corresponding CounterpartyEnum.
	 */
	private Optional<CounterpartyEnum> getOrCreateCounterpartyEnum(String externalReference) {
		return Optional.ofNullable(partyExternalReferenceToCounterpartyEnumMap.computeIfAbsent(
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
				}));
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
