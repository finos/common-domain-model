package cdm.legalagreement.csa.processor;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import cdm.legalagreement.csa.CalculationDateLocation.CalculationDateLocationBuilder;
import cdm.legalagreement.csa.CalculationDateLocationElection;
import cdm.legalagreement.csa.CalculationDateLocationElection.CalculationDateLocationElectionBuilder;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static cdm.legalagreement.csa.CalculationDateLocationElection.builder;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.getEnumValue;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.synonymToEnumValueMap;
import static org.isda.cdm.processor.IsdaCreateMappingProcessorUtils.*;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class CalculationDateLocationMappingProcessor extends MappingProcessor {

	private final Map<String, BusinessCenterEnum> synonymToBusinessCenterEnumMap;

	public CalculationDateLocationMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
		this.synonymToBusinessCenterEnumMap = synonymToEnumValueMap(BusinessCenterEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		CalculationDateLocationBuilder calculationDateLocationBuilder = (CalculationDateLocationBuilder) builder;
		PARTIES.forEach(party -> getCalculationDateLocation(synonymPath, party).ifPresent(calculationDateLocationBuilder::addPartyElection));
	}

	private Optional<CalculationDateLocationElection> getCalculationDateLocation(Path synonymPath, String party) {
		CalculationDateLocationElectionBuilder calculationDateLocationElectionBuilder = builder();

		String selectLocationSynonymValue = synonymPath.endsWith("calculation_date") ?
				"_calculation_date_location" :
				"_" + synonymPath.getLastElement().getPathName();
		setValueAndUpdateMappings(synonymPath.addElement(party + selectLocationSynonymValue),
				(value) -> calculationDateLocationElectionBuilder.setParty(toCounterpartyRoleEnum(party)));

		setValueAndUpdateMappings(synonymPath.addElement(party + "_location"),
				(value) -> getEnumValue(synonymToBusinessCenterEnumMap, value, BusinessCenterEnum.class)
						.map(enumValue -> FieldWithMetaBusinessCenterEnum.builder().setValue(enumValue).build())
						.ifPresent(calculationDateLocationElectionBuilder::setBusinessCenter));

		setValueAndUpdateMappings(synonymPath.addElement(party + "_specify"),
				calculationDateLocationElectionBuilder::setCustomLocation);

		return calculationDateLocationElectionBuilder.hasData() ? Optional.of(calculationDateLocationElectionBuilder.build()) : Optional.empty();
	}
}