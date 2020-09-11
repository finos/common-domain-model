package org.isda.cdm.functions;

import cdm.base.staticdata.party.Account;
import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import org.isda.cdm.TradableProduct;

import java.util.List;
import java.util.Optional;

/**
 * FpML validation rule ird-8:
 *
 * If the same party is specified as the payer and receiver, then different accounts must be specified.
 */
public class FpmlIrd8Impl extends FpmlIrd8 {

	@Override
	protected Boolean doEvaluate(TradableProduct tradableProduct, List<Account> accounts) {
		if (tradableProduct.getCounterparties().size() != 2)
			return false;

		Optional<ReferenceWithMetaParty> party1 = Optional.ofNullable(tradableProduct.getCounterparties().get(0)).map(Counterparty::getParty);
		Optional<ReferenceWithMetaParty> party2 = Optional.ofNullable(tradableProduct.getCounterparties().get(1)).map(Counterparty::getParty);

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
