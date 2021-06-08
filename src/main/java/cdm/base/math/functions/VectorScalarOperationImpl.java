package cdm.base.math.functions;

import cdm.base.math.ArithmeticOp;
import cdm.base.math.Vector;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VectorScalarOperationImpl extends VectorScalarOperation{

    @Override
    protected Vector.VectorBuilder doEvaluate(ArithmeticOp arithmeticOp, Vector left, BigDecimal right) {
        List<BigDecimal> res = doEval(arithmeticOp, left.getValues(), right);
        Vector.VectorBuilder ret = Vector.builder();
        ret.setValues(res);
        return ret;
    }

    protected List<BigDecimal> doEval(ArithmeticOp arithmeticOp, List<? extends BigDecimal> left, BigDecimal right) {
        ArithmeticOpImpl eval = new ArithmeticOpImpl(arithmeticOp);
        int num = left.size();
        List<BigDecimal> result = new ArrayList<>(num);

        for (int i = 0; i < num; i++) {
            BigDecimal lhs = i < left.size() ? left.get(i) : null;
            BigDecimal val = eval.apply(lhs, right);
            result.add(val);
        }

        return result;

    }


}
