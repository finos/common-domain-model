package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.Party.PartyBuilder;

import org.isda.cdm.LegalAgreement.LegalAgreementBuilder;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.MetaFields;
import org.isda.cdm.metafields.ReferenceWithMetaParty;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * ISDA Create mapping processor.
 * <p>
 * Sets LegalAgreement.partyInformation and LegalAgreement.contractualParty
 */
@SuppressWarnings("unused")
public class PartyInformationMappingProcessor extends MappingProcessor {

	public PartyInformationMappingProcessor(RosettaPath rosettaPath, List<Mapping> mappings) {
		super(rosettaPath, mappings);
	}

	@Override
	public void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		// Do nothing
	}

	@Override
	protected void map(List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		LegalAgreementBuilder legalAgreementBuilder = (LegalAgreementBuilder) parent;
		addPartyInformation(legalAgreementBuilder, "partyA");
		addPartyInformation(legalAgreementBuilder, "partyB");
	}

	private void addPartyInformation(LegalAgreementBuilder legalAgreementBuilder, String partyId) {
		Path namePath = Path.parse("answers.partyA.parties." + partyId + "_name");
		Optional<String> nameMapping = findMapping(namePath);

		if (nameMapping.isPresent()) {
			String name = nameMapping.get();
			String id = findMapping(Path.parse(partyId + ".entity.id"))
					.orElse(findMapping(Path.parse(partyId + ".id")).orElse(partyId));
			updateModelAndMappings(legalAgreementBuilder, partyId, namePath, name, id);
		}
	}

	private void updateModelAndMappings(LegalAgreementBuilder legalAgreementBuilder, String partyId, Path namePath, String name, String id) {
		// Update model
		legalAgreementBuilder
				.addPartyInformationBuilder(getParty(partyId, id, name))
				.addContractualParty(ReferenceWithMetaParty.builder().setExternalReference(partyId).build());

		// Update mappings
		getMappings().removeIf(m -> namePath.fullStartMatches(m.getXmlPath()));
		getMappings().add(new Mapping(namePath, name, Path.parse("LegalAgreement.partyInformation.meta.id"), partyId, null, false, false));
		getMappings().add(new Mapping(namePath, name, Path.parse("LegalAgreement.partyInformation.name"), name, null, false, false));
	}

	private PartyBuilder getParty(String externalReference, String id, String partyName) {
		return Party.builder()
				.setMetaBuilder(MetaFields.builder().setExternalKey(externalReference))
				.addPartyId(FieldWithMetaString.builder().setValue(id).build())
				.setNameRef(partyName);
	}

	private Optional<String> findMapping(Path path) {
		return getMappings().stream()
				.filter(p -> path.fullStartMatches(p.getXmlPath()))
				.map(Mapping::getXmlValue)
				.filter(Objects::nonNull)
				.map(String::valueOf)
				.findFirst();
	}
}