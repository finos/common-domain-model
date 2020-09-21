package org.isda.cdm.functions;

import java.math.BigDecimal;
import java.util.List;

import com.google.inject.Inject;
import com.rosetta.model.metafields.FieldWithMetaString;

import cdm.legalagreement.csa.PostedCreditSupportItem;
import cdm.legalagreement.csa.functions.PostedCreditSupportItemAmount;
import cdm.legalagreement.csa.functions.SumPostedCreditSupportItemAmounts;
import cdm.observable.asset.Money;
import cdm.observable.asset.Money.MoneyBuilder;

/**
 * For each postedCreditSupportItem call the PostedCreditSupportItemAmount func and return the summed total.
 */
public class SumPostedCreditSupportItemAmountsImpl extends SumPostedCreditSupportItemAmounts {

	@Inject private PostedCreditSupportItemAmount postedCreditSupportItemAmount;

	@Override
	protected MoneyBuilder doEvaluate(List<PostedCreditSupportItem> postedCreditSupportItems, String baseCurrency) {
		BigDecimal sum = BigDecimal.valueOf(0.0);
		for (PostedCreditSupportItem item : postedCreditSupportItems) {
			sum = sum.add(postedCreditSupportItemAmount.evaluate(item, baseCurrency).getAmount());
		}
		return Money.builder()
				.setAmount(sum)
				.setCurrency(FieldWithMetaString.builder()
						.setValue(baseCurrency)
						.build());
	}

}