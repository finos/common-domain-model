package cdm.event.common.functions;

import cdm.event.common.TradeState;
import cdm.observable.asset.PriceQuantity;
import cdm.product.common.settlement.PriceQuantitySettlement;

import javax.inject.Inject;
import java.util.List;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class UpdateSpreadAdjustmentAndRateOptionForEachPriceQuantityImpl extends UpdateSpreadAdjustmentAndRateOptionForEachPriceQuantity {

	@Inject
	private UpdateSpreadAdjustmentAndRateOption func;

	@Override
	protected TradeState.TradeStateBuilder doEvaluate(TradeState tradeState, List<? extends PriceQuantitySettlement> instructionPriceQuantity) {
		if (tradeState == null)
			return null;

		for(PriceQuantitySettlement i : emptyIfNull(instructionPriceQuantity)) {
			tradeState = func.evaluate(tradeState, i);
		}
		return tradeState.toBuilder();
	}
}
