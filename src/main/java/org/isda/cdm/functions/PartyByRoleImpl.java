package org.isda.cdm.functions;

import java.util.List;
import java.util.stream.Stream;

import org.isda.cdm.Party.PartyBuilder;
import org.isda.cdm.Party;
import org.isda.cdm.PartyRole;
import org.isda.cdm.PartyRoleEnum;

public class PartyByRoleImpl extends PartyByRole {

	@Override
	protected PartyBuilder doEvaluate(List<PartyRole> roles, PartyRoleEnum role) {
		Stream<PartyRole> partyRoleStream = roles.stream().filter(r -> r.getRole().equals(role));
		if (partyRoleStream.count() > 1) throw new RuntimeException("Multiple roles of the same type found");
		
		return partyRoleStream
				.map(partyRole -> partyRole.getPartyReference().getValue().toBuilder())
				.findFirst().orElse(Party.builder());
		
	}

}
