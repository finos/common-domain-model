package cdm.base.math.functions;

import cdm.base.math.Vector;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VectorGrowthOperationImpl extends VectorGrowthOperation {

    // create a list of values based on a supplied list of growth factors. Each returned value will be the prior value
    // times the corresponding growth factor
    @Override
    protected Vector.VectorBuilder doEvaluate(BigDecimal baseValue, Vector factors) {
        if (baseValue==null || factors == null) return Vector.builder();

        List<BigDecimal> res = doEval(baseValue, factors.getValues());
        Vector.VectorBuilder ret = Vector.builder();
        ret.setValues(res);
        return ret;
    }

    private List<BigDecimal> doEval(BigDecimal baseValue, List<? extends BigDecimal> factor) {
        List<BigDecimal> result = new ArrayList<>(factor.size() + 1);
        BigDecimal value = baseValue;
        // initialize the list with the base value
        result.add(baseValue);
        // do the multiplications
        for (BigDecimal fact : factor) {
            value = value.multiply(fact);
            result.add(value);
        }
        return result;
    }
}


