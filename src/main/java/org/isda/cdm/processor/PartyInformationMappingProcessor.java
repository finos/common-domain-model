package org.isda.cdm.processor;

import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.Party.PartyBuilder;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.MetaFields;
import org.isda.cdm.LegalAgreement.LegalAgreementBuilder;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.MappingProcessorUtils.*;

/**
 * ISDA Create mapping processor.
 * <p>
 * Sets LegalAgreement.partyInformation and LegalAgreement.contractualParty
 */
@SuppressWarnings("unused")
public class PartyInformationMappingProcessor extends MappingProcessor {

	public PartyInformationMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
	}

	@Override
	protected void map(List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		LegalAgreementBuilder legalAgreementBuilder = (LegalAgreementBuilder) parent;
		updatePartyInformation(legalAgreementBuilder, "partyA");
		updatePartyInformation(legalAgreementBuilder, "partyB");
	}

	private void updatePartyInformation(LegalAgreementBuilder legalAgreementBuilder, String party) {
		getPartyInformation(party).ifPresent(
				partyInfo -> legalAgreementBuilder
						.addPartyInformation(partyInfo)
						.addContractualParty(ReferenceWithMetaParty.builder()
								.setExternalReference(party)
								.build()));
	}

	private Optional<Party> getPartyInformation(String party) {
		PartyBuilder partyBuilder = Party.builder();

		setValueFromMappings(String.format("answers.partyA.parties.%s_name", party),
				(value) -> {
					partyBuilder.setNameRef(value);
					partyBuilder.setMetaBuilder(MetaFields.builder().setExternalKey(party));
				});

		setValueFromMappings(String.format("%s.entity.id", party),
				(value) -> partyBuilder.addPartyId(toFieldWithMetaString(value)));

		// clean up mappings
		findMappings(getMappings(), Path.parse("answers.partyA.parties")).forEach(m -> updateMapping(m, getPath()));

		return partyBuilder.hasData() ? Optional.of(partyBuilder.build()) : Optional.empty();
	}
}