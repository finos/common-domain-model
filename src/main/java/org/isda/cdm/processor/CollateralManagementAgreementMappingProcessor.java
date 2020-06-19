package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.CollateralManagementAgreement;
import org.isda.cdm.CollateralManagementAgreementElection;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.MappingProcessorUtils.PARTIES;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class CollateralManagementAgreementMappingProcessor extends MappingProcessor {

	public CollateralManagementAgreementMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
	}

	@Override
	public void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		CollateralManagementAgreement.CollateralManagementAgreementBuilder collateralManagementAgreementBuilder =
				(CollateralManagementAgreement.CollateralManagementAgreementBuilder) builder;
		PARTIES.forEach(party -> getCollateralManagementAgreementElection(party).ifPresent(collateralManagementAgreementBuilder::addPartyElection));
	}

	private Optional<CollateralManagementAgreementElection> getCollateralManagementAgreementElection(String party) {
		CollateralManagementAgreementElection.CollateralManagementAgreementElectionBuilder electionBuilder = CollateralManagementAgreementElection.builder();

		setValueAndUpdateMappings(String.format("answers.partyA.collateral_management_agreement.%s_specify", party),
				(value) -> {
					electionBuilder.setParty(party);
					electionBuilder.setCollateralManagementAgreement(value);
				});

		return electionBuilder.hasData() ? Optional.of(electionBuilder.build()) : Optional.empty();
	}
}