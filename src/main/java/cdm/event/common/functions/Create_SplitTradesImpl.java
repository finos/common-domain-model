package cdm.event.common.functions;

import cdm.event.common.AllocationBreakdown;
import cdm.event.common.TradeState;
import com.google.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class Create_SplitTradesImpl extends Create_SplitTrades {

	@Inject
	private Create_SplitTrade func;

	@Override
	protected List<TradeState.TradeStateBuilder> doEvaluate(TradeState tradeState, List<? extends AllocationBreakdown> breakdowns) {
		return emptyIfNull(breakdowns).stream()
				.map(breakdown -> func.evaluate(tradeState, breakdown))
				.map(TradeState::toBuilder)
				.collect(Collectors.toList());
	}
}