package org.isda.cdm.functions;

import java.util.List;
import java.util.stream.Collectors;

import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.Party.PartyBuilder;
import cdm.base.staticdata.party.PartyRole;
import cdm.base.staticdata.party.PartyRoleEnum;
import cdm.base.staticdata.party.functions.PartyByRole;


public class PartyByRoleImpl extends PartyByRole {

	@Override
	protected PartyBuilder doEvaluate(List<PartyRole> roles, PartyRoleEnum role) {
		
		List<PartyBuilder> partyBuilders = roles.stream().filter(r -> r.getRole().equals(role))
				.map(partyRole -> partyRole.getPartyReference().getValue().toBuilder())
				.collect(Collectors.toList());
		
		if (partyBuilders.size() > 1) {
			List<String> partyIds = partyBuilders.stream().map(partyBuilder -> partyBuilder.getPartyId().toString()).collect(Collectors.toList());
			
			throw new RuntimeException("Multiple roles of the same type found: " + String.join(",", partyIds));
			
		}
		
		return partyBuilders.size() == 0 ? Party.builder() : partyBuilders.get(0);
	}

}
