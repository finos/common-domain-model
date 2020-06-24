package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.CollateralManagementAgreement;
import org.isda.cdm.CollateralManagementAgreementElection;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.CdmMappingProcessorUtils.PARTIES;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class CollateralManagementAgreementMappingProcessor extends MappingProcessor {

	public CollateralManagementAgreementMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	protected void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		CollateralManagementAgreement.CollateralManagementAgreementBuilder collateralManagementAgreementBuilder =
				(CollateralManagementAgreement.CollateralManagementAgreementBuilder) builder;
		PARTIES.forEach(party -> getCollateralManagementAgreementElection(synonymPath, party).ifPresent(collateralManagementAgreementBuilder::addPartyElection));
	}

	private Optional<CollateralManagementAgreementElection> getCollateralManagementAgreementElection(Path synonymPath, String party) {
		CollateralManagementAgreementElection.CollateralManagementAgreementElectionBuilder electionBuilder = CollateralManagementAgreementElection.builder();

		setValueAndUpdateMappings(synonymPath.addElement(party + "_specify"),
				(value) -> {
					electionBuilder.setParty(party);
					electionBuilder.setCollateralManagementAgreement(value);
				});

		return electionBuilder.hasData() ? Optional.of(electionBuilder.build()) : Optional.empty();
	}
}