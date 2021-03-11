package cdm.base.staticdata.party.functions;


import static com.rosetta.util.CollectionUtils.emptyIfNull;

import java.util.List;

import cdm.base.staticdata.party.AncillaryParty;
import cdm.base.staticdata.party.AncillaryRoleEnum;

public class ExtractAncillaryPartyByRoleImpl extends ExtractAncillaryPartyByRole {

	@Override
	protected AncillaryParty.AncillaryPartyBuilder doEvaluate(List<AncillaryParty> parties, AncillaryRoleEnum roleToExtract) {
		return emptyIfNull(parties).stream()
				.filter(p -> p.getRole() == roleToExtract)
				.map(AncillaryParty::toBuilder)
				.findFirst()
				.orElse(AncillaryParty.builder());
	}
}