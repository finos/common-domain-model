package cdm.event.common.functions;

import cdm.event.common.BusinessEvent;
import cdm.event.common.PrimitiveEvent;
import cdm.event.common.TradeState;
import com.google.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

public class Create_ContractFormationPrimitivesImpl extends Create_ContractFormationPrimitives {

	@Inject
	private Create_ContractFormationPrimitive func;

	@Override
	protected BusinessEvent.BusinessEventBuilder doEvaluate(List<? extends TradeState> tradeStates) {
		return BusinessEvent.builder()
				.addPrimitives(tradeStates.stream()
						.map(tradeState -> func.evaluate(tradeState.getTrade(), null))
						.map(contractFormationPrimitive -> PrimitiveEvent.builder().setContractFormation(contractFormationPrimitive).build())
						.collect(Collectors.toList()));
	}
}