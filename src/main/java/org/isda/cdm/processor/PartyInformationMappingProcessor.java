package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.LegalAgreement.LegalAgreementBuilder;
import org.isda.cdm.Party;
import org.isda.cdm.Party.PartyBuilder;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.MetaFields;
import org.isda.cdm.metafields.ReferenceWithMetaParty;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
		addPartyInformation(legalAgreementBuilder, Path.parse("answers.partyA.parties.partyA_name"), "partyA");
		addPartyInformation(legalAgreementBuilder, Path.parse("answers.partyA.parties.partyB_name"), "partyB");
	}

	private void addPartyInformation(LegalAgreementBuilder legalAgreementBuilder, Path inputPath, String partyId) {
		findMapping(inputPath).ifPresent(partyName -> {
			// Update model
			legalAgreementBuilder
					.addPartyInformationBuilder(getParty(partyId, partyName))
					.addContractualParty(ReferenceWithMetaParty.builder().setExternalReference(partyId).build());

			// Update mappings
			getMappings().removeIf(m -> inputPath.fullStartMatches(m.getXmlPath()));
			getMappings().add(new Mapping(inputPath, partyName, Path.parse("LegalAgreement.partyInformation.meta.id"), partyId, null, false, false));
			getMappings().add(new Mapping(inputPath, partyName, Path.parse("LegalAgreement.partyInformation.partyId"), partyId, null, false, false));
			getMappings().add(new Mapping(inputPath, partyName, Path.parse("LegalAgreement.partyInformation.name"), partyName, null, false, false));
			getMappings().add(new Mapping(inputPath, partyName, Path.parse("LegalAgreement.contractualParty.meta.externalKey"), partyName, null, false, false));
		});

	}

	private PartyBuilder getParty(String partyId, String partyName) {
		return Party.builder()
				.setMetaBuilder(MetaFields.builder().setExternalKey(partyId))
				.addPartyId(FieldWithMetaString.builder().setValue(partyId).build())
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