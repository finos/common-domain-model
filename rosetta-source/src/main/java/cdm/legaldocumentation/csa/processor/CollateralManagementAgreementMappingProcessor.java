package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.CollateralManagementAgreement;
import cdm.legaldocumentation.csa.CollateralManagementAgreementElection;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.PARTIES;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.toCounterpartyRoleEnum;

/**
 * CreateiQ mapping processor.
 */
@SuppressWarnings("unused")
public class CollateralManagementAgreementMappingProcessor extends MappingProcessor {

	public CollateralManagementAgreementMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		CollateralManagementAgreement.CollateralManagementAgreementBuilder collateralManagementAgreementBuilder =
				(CollateralManagementAgreement.CollateralManagementAgreementBuilder) builder;
		PARTIES.forEach(party -> getCollateralManagementAgreementElection(synonymPath, party).ifPresent(collateralManagementAgreementBuilder::addPartyElection));
	}

	private Optional<CollateralManagementAgreementElection> getCollateralManagementAgreementElection(Path synonymPath, String party) {
		CollateralManagementAgreementElection.CollateralManagementAgreementElectionBuilder electionBuilder = CollateralManagementAgreementElection.builder();

		setValueAndUpdateMappings(synonymPath.addElement(party + "_specify"),
				(value) -> electionBuilder.setParty(toCounterpartyRoleEnum(party)).setCollateralManagementAgreement(value));

		return electionBuilder.hasData() ? Optional.of(electionBuilder.build()) : Optional.empty();
	}
}