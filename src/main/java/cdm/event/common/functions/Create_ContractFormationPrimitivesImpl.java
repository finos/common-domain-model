package cdm.event.common.functions;

import cdm.event.common.PrimitiveEvent;
import cdm.event.common.TradeState;
import com.google.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class Create_ContractFormationPrimitivesImpl extends Create_ContractFormationPrimitives {

	@Inject
	@SuppressWarnings("unused")
	private Create_ContractFormationPrimitive func;

	@Override
	protected List<PrimitiveEvent.PrimitiveEventBuilder> doEvaluate(List<? extends TradeState> tradeStates) {
		return emptyIfNull(tradeStates).stream()
				.map(tradeState -> func.evaluate(tradeState.getTrade(), null))
				.map(contractFormationPrimitive -> PrimitiveEvent.builder().setContractFormation(contractFormationPrimitive))
				.collect(Collectors.toList());
	}
}