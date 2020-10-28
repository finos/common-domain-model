package cdm.base.staticdata.party.functions;

import cdm.base.staticdata.party.RelatedPartyEnum;
import cdm.base.staticdata.party.RelatedPartyReference;

import java.util.List;

public class ExtractRelatedPartyImpl extends ExtractRelatedParty {

	@Override
	protected RelatedPartyReference.RelatedPartyReferenceBuilder doEvaluate(List<RelatedPartyReference> relatedParties, RelatedPartyEnum relatedPartyEnum) {
		return relatedParties.stream()
				.filter(cp -> cp.getRelatedParty() == relatedPartyEnum)
				.map(RelatedPartyReference::toBuilder)
				.findFirst()
				.orElse(RelatedPartyReference.builder());
	}
}