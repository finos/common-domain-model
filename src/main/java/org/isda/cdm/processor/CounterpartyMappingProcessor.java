package org.isda.cdm.processor;

import cdm.base.staticdata.party.CounterpartyEnum;
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

public class CounterpartyMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CounterpartyMappingProcessor.class);
	private static final String COUNTERPARTY_MAP = "counterpartyMap";
	private final Map<String, CounterpartyEnum> externalReferenceToCounterpartyMap;

	public CounterpartyMappingProcessor(RosettaPath rosettaPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(rosettaPath, synonymPaths, mappingContext);
		this.externalReferenceToCounterpartyMap = (Map<String, CounterpartyEnum>)
				getParams().computeIfAbsent(COUNTERPARTY_MAP, (key) -> new HashMap<String, CounterpartyEnum>());
	}

	@Override
	protected <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		//		Optional<String> blah = getNonNullMappedValue(filterMappings(getMappings(), synonymPath));
		//		LOGGER.info("**** {} {} {}", synonymPath, blah, PayerReceiver.PayerReceiverBuilder.class.isInstance(parent));
		//		addToList();
		setValueAndOptionallyUpdateMappings(
				synonymPath,
				(value) -> {
					Optional<CounterpartyEnum> counterpartyEnum = getCounterpartyEnum(value);
					counterpartyEnum.ifPresent(c -> invokeSetter(parent, getModelPath().getElement().getPath(), c));
					return counterpartyEnum.isPresent();
				},
				getMappings(),
				getModelPath());
	}

	private void invokeSetter(RosettaModelObjectBuilder parent, String attribute, CounterpartyEnum counterparty) {
		try {
			parent.getClass()
					.getMethod("set" + toFirstUpper(attribute), CounterpartyEnum.class)
					.invoke(parent, counterparty);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private Optional<CounterpartyEnum> getCounterpartyEnum(String externalReference) {
		return Optional.ofNullable(externalReferenceToCounterpartyMap.computeIfAbsent(externalReference,
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
