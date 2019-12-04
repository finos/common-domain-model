package org.isda.cdm.functions;

import java.util.List;

import org.isda.cdm.Party;
import org.isda.cdm.Party.PartyBuilder;

public class PartyByIndexImpl extends PartyByIndex {

	@Override
	protected PartyBuilder doEvaluate(List<Party> parties, Integer index) {
		if (parties.size() > index) {
			return parties.get(index).toBuilder();
		}
		return Party.builder();
	}
}
