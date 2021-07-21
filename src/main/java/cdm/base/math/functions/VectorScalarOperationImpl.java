package cdm.base.math.functions;

import cdm.base.math.ArithmeticOp;
import cdm.base.math.Vector;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VectorScalarOperationImpl extends VectorScalarOperation{

    // apply an arithmetic operation on a supplied vector and a scalar, applying the operation to each
    // element of the vector and to the scalar.
    @Override
    protected Vector.VectorBuilder doEvaluate(ArithmeticOp arithmeticOp, Vector left, BigDecimal right) {
        List<? extends BigDecimal> leftVals = left == null ? null : left.getValues();

        List<BigDecimal> res = doEval(arithmeticOp, leftVals, right);
        Vector.VectorBuilder ret = Vector.builder();
        ret.setValues(res);
        return ret;
    }

    protected List<BigDecimal> doEval(ArithmeticOp arithmeticOp, List<? extends BigDecimal> left, BigDecimal right) {
        ArithmeticOpImpl eval = new ArithmeticOpImpl(arithmeticOp);
        int num = left == null ? 0 : left.size();
        List<BigDecimal> result = new ArrayList<>(num);
        BigDecimal rightVal = right == null ? BigDecimal.valueOf(0.0)  : right;

        // go through the vector and apply the operator and the scalar to each elemnent in turn
        for (int i = 0; i < num; i++) {
            BigDecimal lhs = i < left.size() ? left.get(i) : null;
            BigDecimal val = eval.apply(lhs, rightVal);
            result.add(val);
        }

        return result;

    }


}
