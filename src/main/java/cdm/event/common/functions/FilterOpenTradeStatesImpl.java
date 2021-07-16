package cdm.event.common.functions;

import cdm.event.common.State;
import cdm.event.common.TradeState;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class FilterOpenTradeStatesImpl extends FilterOpenTradeStates {

	@Override
	protected List<TradeState.TradeStateBuilder> doEvaluate(List<? extends TradeState> tradeStates) {
		return emptyIfNull(tradeStates).stream()
				.filter(ts -> !Optional.ofNullable(ts).map(TradeState::getState).map(State::getClosedState).isPresent())
				.filter(ts -> !Optional.ofNullable(ts).map(TradeState::getState).map(State::getPositionState).isPresent()).filter(Objects::nonNull)
				.map(TradeState::toBuilder)
				.collect(Collectors.toList());
	}
}
