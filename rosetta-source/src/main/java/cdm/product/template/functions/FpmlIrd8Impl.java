package cdm.product.template.functions;

import cdm.base.staticdata.party.Account;
import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.Trade;
import cdm.product.template.TradableProduct;

import java.util.List;
import java.util.Optional;

/**
 * FpML validation rule ird-8:
 *
 * If the same party is specified as the payer and receiver, then different accounts must be specified.
 */
public class FpmlIrd8Impl extends FpmlIrd8 {

	@Override
	protected Boolean doEvaluate(Trade trade, List<? extends Account> accounts) {
		if ( trade.getCounterparty() == null || trade.getCounterparty().size() != 2)
			return false;

		Optional<ReferenceWithMetaParty> party1 = Optional.ofNullable(trade.getCounterparty().get(0)).map(Counterparty::getPartyReference);
		Optional<ReferenceWithMetaParty> party2 = Optional.ofNullable(trade.getCounterparty().get(1)).map(Counterparty::getPartyReference);

		if (!party1.isPresent() || !party2.isPresent())
			return false;

		if (!party1.equals(party2))
			return true;

		// Same party on each side of trade, so accounts must different
		return accounts.stream()
				.filter(a -> party1.get().equals(a.getPartyReference()))
				.distinct()
				.count() == 2;
	}
}
