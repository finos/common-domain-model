package cdm.base.staticdata.party.functions;

import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyEnum;

import java.util.List;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class ExtractCounterpartyByRoleImpl extends ExtractCounterpartyByRole {

	@Override
	protected Counterparty.CounterpartyBuilder doEvaluate(List<Counterparty> counterparties, CounterpartyEnum role) {
		List<Counterparty> counterpartiesForRole = emptyIfNull(counterparties).stream()
				.filter(cp -> cp.getRole() == role)
				.collect(Collectors.toList());

		if (counterpartiesForRole.size() > 1) {
			throw new IllegalArgumentException("Counterparties input contains more than 1 entry for role: " + role);
		} else if (counterpartiesForRole.size() == 1) {
			return counterpartiesForRole.get(0).toBuilder();
		} else {
			return Counterparty.builder();
		}
	}
}