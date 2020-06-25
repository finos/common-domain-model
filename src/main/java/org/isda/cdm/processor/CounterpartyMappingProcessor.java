package org.isda.cdm.processor;

import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyEnum;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.TradableProduct;
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
public class CounterpartyMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CounterpartyMappingProcessor.class);
	private static final String COUNTERPARTY_MAP = "counterpartyMap";
	private static final List<CounterpartyEnum> COUNTERPARTIES = Arrays.asList(CounterpartyEnum.PARTY_1, CounterpartyEnum.PARTY_2);
	private final Map<String, CounterpartyEnum> externalReferenceToCounterpartyMap;

	public CounterpartyMappingProcessor(RosettaPath rosettaPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(rosettaPath, synonymPaths, mappingContext);
		this.externalReferenceToCounterpartyMap = (Map<String, CounterpartyEnum>)
				getParams().computeIfAbsent(COUNTERPARTY_MAP, (key) -> new HashMap<String, CounterpartyEnum>());
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		TradableProduct.TradableProductBuilder tradableProductBuilder = (TradableProduct.TradableProductBuilder) parent;
		tradableProductBuilder.clearCounterparties();
		COUNTERPARTIES.forEach(c -> getCounterparty(c).ifPresent(tradableProductBuilder::addCounterparties));
	}

	private Optional<Counterparty> getCounterparty(CounterpartyEnum counterpartyEnum) {
		return getExternalReference(counterpartyEnum)
				.map(externalRef -> Counterparty.builder()
						.setCounterparty(counterpartyEnum)
						.setPartyBuilder(ReferenceWithMetaParty.builder().setExternalReference(externalRef))
						.build());
	}

	private Optional<String> getExternalReference(CounterpartyEnum counterparty) {
		return externalReferenceToCounterpartyMap.entrySet()
				.stream()
				.filter((entry) -> entry.getValue().equals(counterparty))
				.findFirst()
				.map(Map.Entry::getKey);
	}
}
