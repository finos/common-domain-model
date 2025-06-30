package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.ControlAgreement;
import cdm.legaldocumentation.csa.ControlAgreementElections;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.updateMappings;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.PARTIES;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.toCounterpartyRoleEnum;

/**
 * CreateiQ mapping processor.
 */
@SuppressWarnings("unused")
public class ControlAgreementMappingProcessor extends MappingProcessor {

	public ControlAgreementMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		ControlAgreement.ControlAgreementBuilder controlAgreementBuilder = (ControlAgreement.ControlAgreementBuilder) builder;
		PARTIES.forEach(party -> getControlAgreementElection(synonymPath, party).ifPresent(controlAgreementBuilder::addPartyElection));
	}

	private Optional<ControlAgreementElections> getControlAgreementElection(Path synonymPath, String party) {
		ControlAgreementElections.ControlAgreementElectionsBuilder controlAgreementElections = ControlAgreementElections.builder();

		setValueAndUpdateMappings(synonymPath.addElement(party + "_" + synonymPath.getLastElement().getPathName()),
				(value) -> {
					controlAgreementElections.setParty(toCounterpartyRoleEnum(party));
					yesNoToBoolean(value).ifPresent(controlAgreementElections::setControlAgreementAsCsd);
				});

		setValueAndUpdateMappings(String.format("answers.partyA.inconsistency_with_the_control_agreement.%s_inconsistency_with_the_control_agreement", party),
				(value) -> applicableToBoolean(value).ifPresent(applicable -> {
					controlAgreementElections.setConsistencyWithControlAgreement(applicable);
					// Update parent mappings (not sure why this is necessary)
					updateMappings(Path.parse("answers.partyA.inconsistency_with_the_control_agreement"), getMappings(), getModelPath());
				}));

		setValueAndUpdateMappings(String.format("answers.partyA.relationship_with_the_control_agreement.%s_control_agreement_relationship", party),
				(value) -> applicableToBoolean(value).ifPresent(controlAgreementElections::setRelationshipWithControlAgreement));

		return controlAgreementElections.hasData() ? Optional.of(controlAgreementElections.build()) : Optional.empty();
	}

	private Optional<Boolean> yesNoToBoolean(String yesNo) {
		if ("yes".equals(yesNo)) {
			return Optional.of(true);
		} else if ("no".equals(yesNo)) {
			return Optional.of(false);
		} else {
			return Optional.empty();
		}
	}

	private Optional<Boolean> applicableToBoolean(String applicable) {
		if ("applicable".equals(applicable)) {
			return Optional.of(true);
		} else if ("not_applicable".equals(applicable)) {
			return Optional.of(false);
		} else {
			return Optional.empty();
		}
	}
}