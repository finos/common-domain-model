package cdm.base.math.functions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VectorGrowthOperationImpl extends VectorGrowthOperation{
    @Override
    protected List<BigDecimal> doEvaluate(BigDecimal baseValue, List<? extends BigDecimal> factor) {
        List<BigDecimal> result = new ArrayList<>(factor.size() + 1);
        BigDecimal value = baseValue;
        result.add(baseValue);
        for (BigDecimal fact : factor) {
            value = value.multiply(fact);
            result.add(value);
        }
        return result;
    }
}
