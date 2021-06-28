package cdm.base.math.functions;

import cdm.base.math.Vector;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VectorGrowthOperationImpl extends VectorGrowthOperation {

    @Override
    protected Vector.VectorBuilder doEvaluate(BigDecimal baseValue, Vector factors) {
        List<BigDecimal> res = doEval(baseValue, factors.getValues());
        Vector.VectorBuilder ret = Vector.builder();
        ret.setValues(res);
        return ret;
    }


    private List<BigDecimal> doEval(BigDecimal baseValue, List<? extends BigDecimal> factor) {
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


