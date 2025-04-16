package cdm.legaldocumentation.common.processor;

import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.Party.PartyBuilder;
import cdm.base.staticdata.party.PartyIdentifier;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.legaldocumentation.common.LegalAgreement.LegalAgreementBuilder;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.MetaFields;

import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.updateMappings;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.PARTIES;

/**
 * CreateiQ mapping processor.
 * <p>
 * Sets LegalAgreement.contractualParty
 */
@SuppressWarnings("unused")
public class ContractualPartyMappingProcessor extends MappingProcessor {

	public ContractualPartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		LegalAgreementBuilder legalAgreementBuilder = (LegalAgreementBuilder) parent;
		PARTIES.forEach(party ->
				getContractualParty(party).ifPresent(
						partyInfo -> legalAgreementBuilder
								.addContractualParty(ReferenceWithMetaParty.builder()
										.setValue(partyInfo)
										.build())));
	}

	private Optional<Party> getContractualParty(String party) {
		PartyBuilder partyBuilder = Party.builder();

		setValueAndUpdateMappings(String.format("answers.partyA.parties.%s_name", party),
				(value) -> {
					partyBuilder.setNameValue(value);
					partyBuilder.setMeta(MetaFields.builder().setExternalKey(party));
				});

		setValueAndUpdateMappings(String.format("%s.entity.id", party),
				(value) -> partyBuilder.addPartyId(PartyIdentifier.builder().setIdentifierValue(value).build()));

		// clean up mappings
		updateMappings(Path.parse("answers.partyA.parties"), getMappings(), getModelPath());

		return partyBuilder.hasData() ? Optional.of(partyBuilder.build()) : Optional.empty();
	}
}