package cdm.base.staticdata.party.functions;

import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyEnum;

import java.util.List;
import java.util.stream.Collectors;

public class ExtractCounterpartyBySideImpl extends ExtractCounterpartyBySide {

	@Override
	protected Counterparty.CounterpartyBuilder doEvaluate(List<Counterparty> counterparties, CounterpartyEnum side) {
		List<Counterparty> counterpartiesForSide = counterparties.stream()
				.filter(cp -> cp.getCounterparty() == side)
				.collect(Collectors.toList());

		if (counterpartiesForSide.size() > 1) {
			throw new IllegalArgumentException("Counterparties input contains more than 1 entry for side: " + side);
		} else if (counterpartiesForSide.size() == 1) {
			return counterpartiesForSide.get(0).toBuilder();
		} else {
			return Counterparty.builder();
		}
	}
}