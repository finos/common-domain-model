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
import java.util.*;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;
import static com.regnosys.rosetta.common.util.StringExtensions.toFirstUpper;
import static org.isda.cdm.TradableProduct.TradableProductBuilder;

class CounterpartyMappingHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(CounterpartyMappingHelper.class);

	private static final RosettaPath PRODUCT_SUB_PATH = RosettaPath.valueOf("tradableProduct").newSubPath("product");

	private final Map<String, CounterpartyEnum> partyExternalReferenceToCounterpartyEnumMap;
	private final List<Mapping> mappings;

	private volatile TradableProductBuilder tradableProductBuilder;

	private CounterpartyMappingHelper(List<Mapping> mappings) {
		this.mappings = mappings;
		this.partyExternalReferenceToCounterpartyEnumMap = new LinkedHashMap<>();
	}

	/**
	 * Get or create an instance of this counterparty mapping helper.
	 */
	@NotNull
	static synchronized CounterpartyMappingHelper getOrCreateHelper(MappingContext mappingContext) {
		return (CounterpartyMappingHelper)
				mappingContext.getMappingParams()
						.computeIfAbsent("COUNTERPARTY_MAPPING_HELPER", (key) -> new CounterpartyMappingHelper(mappingContext.getMappings()));
	}

	/**
	 * Maps party external reference to CounterpartyEnum, then sets on builder, and updates counterparties list (if present)
	 */
	synchronized void setCounterpartyEnum(RosettaModelObjectBuilder builder, RosettaPath modelPath, Path synonymPath) {
		if (modelPath.containsPath(PRODUCT_SUB_PATH)) {
			setValueAndOptionallyUpdateMappings(
					synonymPath.addElement("href"), // synonym path to party external reference
					(externalRef) -> getOrCreateCounterpartyEnum(externalRef)
							// determine counterparty enum and add to builder
							.filter(cpty -> setCounterpartyEnum(builder, modelPath.getElement().getPath(), cpty))
							// add to tradableProductBuilder.counterparties if present
							.filter(cpty -> addCounterparty(cpty, externalRef))
							.isPresent(),
					mappings,
					modelPath);
		}
	}

	/**
	 * Creates Counterparty instances from the partyExternalReference to CounterpartyEnum map.
	 * Reference to tradableProductBuilder is stored in case all mappings have not be collected yet.
	 */
	synchronized void addCounterparties(TradableProductBuilder builder) {
		tradableProductBuilder = builder.clearCounterparties();
		partyExternalReferenceToCounterpartyEnumMap.entrySet()
				.forEach(entry -> addCounterparty(entry.getValue(), entry.getKey()));
	}

	private Optional<CounterpartyEnum> getOrCreateCounterpartyEnum(String externalReference) {
		return Optional.ofNullable(partyExternalReferenceToCounterpartyEnumMap.computeIfAbsent(
				externalReference,
				(key) -> {
					if (partyExternalReferenceToCounterpartyEnumMap.isEmpty()) {
						return CounterpartyEnum.PARTY_1;
					} else if (partyExternalReferenceToCounterpartyEnumMap.size() == 1) {
						return CounterpartyEnum.PARTY_2;
					} else {
						LOGGER.error("Unexpected counterparty external reference {}, 2 counterparties already exist", externalReference);
						return null;
					}
				}));
	}

	private boolean setCounterpartyEnum(RosettaModelObjectBuilder builder, String attribute, CounterpartyEnum counterpartyEnum) {
		try {
			// set CounterpartyEnum
			builder.getClass()
					.getMethod("set" + toFirstUpper(attribute), CounterpartyEnum.class)
					.invoke(builder, counterpartyEnum);
			// blank out partyReference if builder is a BuyerSeller or PayerReceiver
			if (builder instanceof BuyerSeller.BuyerSellerBuilder || builder instanceof PayerReceiver.PayerReceiverBuilder) {
				builder.getClass()
						.getMethod("set" + toFirstUpper(attribute) + "Reference", ReferenceWithMetaParty.class)
						.invoke(builder, ReferenceWithMetaParty.builder().build());
			}
			return true;
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			LOGGER.error("Failed to set CounterpartyEnum {}", counterpartyEnum, e);
			return false;
		}
	}

	private boolean addCounterparty(CounterpartyEnum counterpartyEnum, String externalReference) {
		Optional.ofNullable(tradableProductBuilder)
				.filter(builder -> Optional.ofNullable(builder.getCounterparties()).orElse(Collections.emptyList()).stream()
						.map(Counterparty.CounterpartyBuilder::getCounterparty)
						.noneMatch(counterpartyEnum::equals))
				.ifPresent(builder -> builder.addCounterparties(Counterparty.builder()
						.setCounterparty(counterpartyEnum)
						.setPartyBuilder(ReferenceWithMetaParty.builder().setExternalReference(externalReference))
						.build()));
		return true;
	}
}
