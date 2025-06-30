package cdm.legaldocumentation.common.processor;

import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.legaldocumentation.common.AgreementTerms;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.PARTIES;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.toCounterpartyRoleEnum;

/**
 * CreateiQ mapping processor.
 * <p>
 * Sets AgreementTerms.counterparty.  Always maps "partyA" to CounterpartyRoleEnum.Party1 and "partyB" to CounterpartyRoleEnum.Party2.
 */
@SuppressWarnings("unused")
public class AgreementTermsPartyMappingProcessor extends MappingProcessor {

	public AgreementTermsPartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		AgreementTerms.AgreementTermsBuilder agreementBuilder = (AgreementTerms.AgreementTermsBuilder) parent;
		PARTIES.forEach(party -> agreementBuilder
				.addCounterparty(Counterparty.builder()
						.setRole(toCounterpartyRoleEnum(party))
						.setPartyReference(ReferenceWithMetaParty.builder().setExternalReference(party).build())));
	}
}
