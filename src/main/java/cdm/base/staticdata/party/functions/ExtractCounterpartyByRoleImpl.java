package cdm.base.staticdata.party.functions;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

import java.util.List;
import java.util.stream.Collectors;

import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyRoleEnum;

public class ExtractCounterpartyByRoleImpl extends ExtractCounterpartyByRole {

	@Override
	protected Counterparty.CounterpartyBuilder doEvaluate(List<Counterparty> parties, CounterpartyRoleEnum roleToExtract) {
		List<Counterparty> counterpartiesForRole = emptyIfNull(parties).stream()
				.filter(cp -> cp.getRole() == roleToExtract)
				.collect(Collectors.toList());

		if (counterpartiesForRole.size() > 1) {
			throw new IllegalArgumentException("Counterparties input contains more than 1 entry for role: " + roleToExtract);
		} else if (counterpartiesForRole.size() == 1) {
			return counterpartiesForRole.get(0).toBuilder();
		} else {
			return Counterparty.builder();
		}
	}
}