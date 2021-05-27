package cdm.base.math.functions;

import cdm.base.math.ArithmeticOp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VectorScalarOperationImpl extends VectorScalarOperation{


    @Override
    protected List<BigDecimal> doEvaluate(ArithmeticOp arithmeticOp, List<? extends BigDecimal> left, BigDecimal right) {
        ArithmeticOpImpl eval = new ArithmeticOpImpl(arithmeticOp);
        int num = left.size();
        List<BigDecimal> result = new ArrayList<>(num);

        for (int i = 0; i < num; i++) {
            BigDecimal lhs = i < left.size() ? left.get(i) : null;
            BigDecimal val = eval.apply(lhs, right);
            result.set(i, val);
        }

        return result;

    }
}
