package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.ControlAgreement;
import org.isda.cdm.ControlAgreementElections;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.MappingProcessorUtils.updateMappings;
import static org.isda.cdm.processor.RegimeMappingHelper.PARTIES;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class ControlAgreementMappingProcessor extends MappingProcessor {

	public ControlAgreementMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
	}

	@Override
	public void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		getSynonymValues().forEach(v -> {
			ControlAgreement.ControlAgreementBuilder controlAgreementBuilder = (ControlAgreement.ControlAgreementBuilder) builder;
			PARTIES.forEach(party -> getControlAgreementElection(v, party).ifPresent(controlAgreementBuilder::addPartyElection));
		});
	}

	private Optional<ControlAgreementElections> getControlAgreementElection(String synonymValue, String party) {
		ControlAgreementElections.ControlAgreementElectionsBuilder controlAgreementElections = ControlAgreementElections.builder();

		setValueFromMappings(String.format("answers.partyA.%s.%s_%s", synonymValue, party, synonymValue),
				(value) -> {
					controlAgreementElections.setParty(party);
					yesNoToBoolean(value).ifPresent(controlAgreementElections::setControlAgreementAsCsd);
				});

		setValueFromMappings(String.format("answers.partyA.inconsistency_with_the_control_agreement.%s_inconsistency_with_the_control_agreement", party),
				(value) -> applicableToBoolean(value).ifPresent(applicable -> {
					controlAgreementElections.setConsistencyWithControlAgreement(applicable);
					// Update parent mappings (not sure why this is necessary)
					updateMappings(Path.parse(String.format("answers.partyA.inconsistency_with_the_control_agreement", party)), getMappings(), getPath());
				}));

		setValueFromMappings(String.format("answers.partyA.relationship_with_the_control_agreement.%s_control_agreement_relationship", party),
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