package cdm.base.math.functions;

import cdm.base.math.ArithmeticOp;
import cdm.base.math.Vector;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VectorOperationImpl extends VectorOperation {

    @Override
    protected Vector.VectorBuilder doEvaluate(ArithmeticOp arithmeticOp, Vector left, Vector right) {
        List<BigDecimal> res = doEval(arithmeticOp, left.getValues(), right.getValues());
        Vector.VectorBuilder ret = Vector.builder();
        ret.setValues(res);
        return ret;
    }


    protected List<BigDecimal> doEval(ArithmeticOp arithmeticOp, List<? extends BigDecimal> left, List<? extends BigDecimal> right) {
        ArithmeticOpImpl eval = new ArithmeticOpImpl(arithmeticOp);

        int num = Math.max(left.size(), right.size());
        List<BigDecimal> result = new ArrayList<>(num);

        for (int i = 0; i < num; i++) {
            BigDecimal lhs = i < left.size() ? left.get(i) : new BigDecimal("0.0");
            BigDecimal rhs = i < right.size() ? right.get(i) : new BigDecimal("0.0");
            BigDecimal val = eval.apply(lhs, rhs);
            result.add(val);
        }
        return result;
    }
}


