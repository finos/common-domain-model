package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.CalculationDateLocation;
import cdm.legaldocumentation.csa.CalculationDateLocationElection;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.processor.CreateiQMappingProcessorUtils;

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

		//TH Sprint 2025-12: CDM Reference Data update. Requires further review.
		setValueAndUpdateMappings(synonymPath.addElement(party + "_location"),
				value -> calculationDateLocationElectionBuilder.setBusinessCenter(CreateiQMappingProcessorUtils.toFieldWithMetaString(value)));

		setValueAndUpdateMappings(synonymPath.addElement(party + "_specify"),
				value -> calculationDateLocationElectionBuilder.setCustomLocation(removeHtml(value)));

		return calculationDateLocationElectionBuilder.hasData() ? Optional.of(calculationDateLocationElectionBuilder.build()) : Optional.empty();
	}
}