package org.isda.cdm.processor;

import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.Party.PartyBuilder;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import org.isda.cdm.LegalAgreement.LegalAgreementBuilder;

import java.util.List;

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
		addPartyInformation(legalAgreementBuilder, "partyA");
		addPartyInformation(legalAgreementBuilder, "partyB");
	}

	private void addPartyInformation(LegalAgreementBuilder legalAgreementBuilder, String partyId) {
		findMappings(getMappings(), Path.parse("answers.partyA.parties")).forEach(m -> updateMapping(m, getPath()));

		PartyBuilder partyBuilder = Party.builder().setMetaBuilder(MetaFields.builder().setExternalKey(partyId));

		List<Mapping> partyNameMappings = findMappings(getMappings(), Path.parse(String.format("answers.partyA.parties.%s_name", partyId)));
		findMappedValue(partyNameMappings)
				.ifPresent(xmlValue -> {
					partyBuilder.setNameRef(xmlValue);
					partyNameMappings.forEach(m -> updateMapping(m, getPath()));
				});

		List<Mapping> partyEntityIdMappings = findMappings(getMappings(), Path.parse(String.format("%s.entity.id", partyId)));
		findMappedValue(partyEntityIdMappings).ifPresent(xmlValue -> {
			partyBuilder.addPartyId(toFieldWithMetaString(xmlValue));
			partyNameMappings.forEach(m -> updateMapping(m, getPath()));
		});

		legalAgreementBuilder
				.addPartyInformationBuilder(partyBuilder)
				.addContractualParty(ReferenceWithMetaParty.builder().setExternalReference(partyId).build());
	}
}