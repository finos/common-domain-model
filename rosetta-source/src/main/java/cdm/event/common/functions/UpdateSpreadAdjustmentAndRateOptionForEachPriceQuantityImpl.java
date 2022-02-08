package cdm.event.common.functions;

import cdm.event.common.TradeState;
import cdm.product.common.settlement.PriceQuantity;

import javax.inject.Inject;
import java.util.List;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class UpdateSpreadAdjustmentAndRateOptionForEachPriceQuantityImpl extends UpdateSpreadAdjustmentAndRateOptionForEachPriceQuantity {

	@Inject
	private UpdateSpreadAdjustmentAndRateOption func;

	@Override
	protected TradeState.TradeStateBuilder doEvaluate(TradeState tradeState, List<? extends PriceQuantity> instructionPriceQuantity) {
		if (tradeState == null)
			return null;

		for(PriceQuantity i : emptyIfNull(instructionPriceQuantity)) {
			tradeState = func.evaluate(tradeState, i);
		}
		return tradeState.toBuilder();
	}
}
