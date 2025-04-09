package cdm.legaldocumentation.csa.processor;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import cdm.legaldocumentation.csa.CalculationDateLocation;
import cdm.legaldocumentation.csa.CalculationDateLocationElection;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.*;

/**
 * CreateiQ mapping processor.
 */
@SuppressWarnings("unused")
public class CalculationDateLocationMappingProcessor extends MappingProcessor {

	public CalculationDateLocationMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		CalculationDateLocation.CalculationDateLocationBuilder calculationDateLocationBuilder = (CalculationDateLocation.CalculationDateLocationBuilder) builder;
		PARTIES.forEach(party -> getCalculationDateLocation(synonymPath, party).ifPresent(calculationDateLocationBuilder::addPartyElection));
	}

	private Optional<CalculationDateLocationElection> getCalculationDateLocation(Path synonymPath, String party) {
		CalculationDateLocationElection.CalculationDateLocationElectionBuilder calculationDateLocationElectionBuilder = CalculationDateLocationElection.builder();

		String selectLocationSynonymValue = synonymPath.endsWith("calculation_date") ?
				"_calculation_date_location" :
				"_" + synonymPath.getLastElement().getPathName();
		setValueAndUpdateMappings(synonymPath.addElement(party + selectLocationSynonymValue),
				(value) -> calculationDateLocationElectionBuilder.setParty(toCounterpartyRoleEnum(party)));

		setValueAndUpdateMappings(synonymPath.addElement(party + "_location"),
				(value) -> getSynonymToEnumMap().getEnumValueOptional(BusinessCenterEnum.class, value)
						.map(enumValue -> FieldWithMetaBusinessCenterEnum.builder().setValue(enumValue).build())
						.ifPresent(calculationDateLocationElectionBuilder::setBusinessCenter));

		setValueAndUpdateMappings(synonymPath.addElement(party + "_specify"),
				value -> calculationDateLocationElectionBuilder.setCustomLocation(removeHtml(value)));

		return calculationDateLocationElectionBuilder.hasData() ? Optional.of(calculationDateLocationElectionBuilder.build()) : Optional.empty();
	}
}