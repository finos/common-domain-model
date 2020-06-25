package org.isda.cdm.processor;

import cdm.base.staticdata.party.BuyerSeller;
import cdm.base.staticdata.party.CounterpartyEnum;
import cdm.base.staticdata.party.PayerReceiver;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;
import static com.regnosys.rosetta.common.util.StringExtensions.toFirstUpper;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class CounterpartyEnumMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CounterpartyEnumMappingProcessor.class);
	private static final String COUNTERPARTY_MAP = "counterpartyMap";
	private final Map<String, CounterpartyEnum> externalReferenceToCounterpartyMap;
	private final boolean tradableProductSubPath;

	public CounterpartyEnumMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
		this.externalReferenceToCounterpartyMap = (Map<String, CounterpartyEnum>)
				getParams().computeIfAbsent(COUNTERPARTY_MAP, (key) -> new HashMap<String, CounterpartyEnum>());
		this.tradableProductSubPath = modelPath.containsPath(RosettaPath.valueOf("tradableProduct"));
	}

	@Override
	public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		//		Optional<String> blah = getNonNullMappedValue(filterMappings(getMappings(), synonymPath));
		//		LOGGER.info("**** {} {} {}", synonymPath, blah, PayerReceiver.PayerReceiverBuilder.class.isInstance(parent));
		//		addToList();
		if (tradableProductSubPath) {
			setValueAndOptionallyUpdateMappings(
					synonymPath.addElement("href"),
					(value) -> getCounterpartyEnum(value)
							.filter(c -> invokeBuilderSetter(parent, getModelPath().getElement().getPath(), c, value))
							.isPresent(),
					getMappings(),
					getModelPath());
		}
	}

	private boolean invokeBuilderSetter(RosettaModelObjectBuilder builder, String attribute, CounterpartyEnum counterparty, String externalRef) {
		try {
			if (builder instanceof BuyerSeller.BuyerSellerBuilder) {
				BuyerSeller.BuyerSellerBuilder buyerSeller = (BuyerSeller.BuyerSellerBuilder) builder;
				if (attribute.equals("buyer")) {
					LOGGER.info("modelPath={}, attribute={}, externalRef={}, counterparty={}, existingCounterparty={}", getModelPath(), attribute, externalRef, counterparty, buyerSeller.getBuyer());
				} else if (attribute.equals("seller")) {
					LOGGER.info("modelPath={}, attribute={}, externalRef={}, counterparty={}, existingCounterparty={}", getModelPath(), attribute, externalRef, counterparty, buyerSeller.getBuyer());
				} else {
					throw new RuntimeException("Unknown attribute! " + attribute);
				}
			}


			builder.getClass()
					.getMethod("set" + toFirstUpper(attribute), CounterpartyEnum.class)
					.invoke(builder, counterparty);
			if (builder instanceof BuyerSeller.BuyerSellerBuilder || builder instanceof PayerReceiver.PayerReceiverBuilder) {
				builder.getClass()
						.getMethod("set" + toFirstUpper(attribute) + "Reference", ReferenceWithMetaParty.class)
						.invoke(builder, ReferenceWithMetaParty.builder().build());
			}
			return true;
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			LOGGER.error("Failed to set CounterpartyEnum {} at {}", counterparty, getModelPath(), e);
			return false;
		}
	}

	private Optional<CounterpartyEnum> getCounterpartyEnum(String externalReference) {
		return Optional.ofNullable(externalReferenceToCounterpartyMap.computeIfAbsent(
				externalReference,
				(key) -> {
					if (externalReferenceToCounterpartyMap.isEmpty()) {
						return CounterpartyEnum.PARTY_1;
					} else if (externalReferenceToCounterpartyMap.size() == 1) {
						return CounterpartyEnum.PARTY_2;
					} else {
						return null;
					}
				}));
	}

	private void addToList() {
		List<RosettaPath> counterparty = (List<RosettaPath>) getParams().computeIfAbsent("counterparty", (key) -> new ArrayList<RosettaPath>());
		counterparty.add(getModelPath());
	}
}
