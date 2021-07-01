package cdm.event.common.functions;

import cdm.event.common.DecreasedTrade;
import cdm.event.common.PrimitiveEvent;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Create_DecreasedTradeQuantityChangePrimitivesImpl extends Create_DecreasedTradeQuantityChangePrimitives {

    @Inject
    Create_DecreasedTradeQuantityChangePrimitive create_decreasedTradeQuantityChangePrimitive;

    @Override
    protected List<PrimitiveEvent.PrimitiveEventBuilder> doEvaluate(List<? extends DecreasedTrade> decreases) {
        return Optional.ofNullable(decreases)
                .map(dl -> dl.stream()
                        .map(create_decreasedTradeQuantityChangePrimitive::evaluate)
                        .map(PrimitiveEvent.builder()::setQuantityChange)
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }
}
