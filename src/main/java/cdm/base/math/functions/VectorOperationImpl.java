package cdm.base.math.functions;

import cdm.base.math.ArithmeticOp;
import cdm.base.math.Vector;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VectorOperationImpl extends VectorOperation {

    // apply an arithmetic operation on two supplied vectors, applying the operation pairwise.
    // If one vector is shorter than the other, padd out the shorter one with 0s.
    @Override
    protected Vector.VectorBuilder doEvaluate(ArithmeticOp arithmeticOp, Vector left, Vector right) {
        List<? extends BigDecimal> leftVals = left == null ? null : left.getValues();
        List<? extends BigDecimal> rightVals = right == null ? null : right.getValues();

        List<BigDecimal> res = doEval(arithmeticOp, leftVals, rightVals);
        Vector.VectorBuilder ret = Vector.builder();
        ret.setValues(res);
        return ret;
    }


    protected List<BigDecimal> doEval(ArithmeticOp arithmeticOp, List<? extends BigDecimal> left, List<? extends BigDecimal> right) {
        ArithmeticOpImpl eval = new ArithmeticOpImpl(arithmeticOp);

        int leftSize = left == null ? 0 : left.size();;
        int rightSize = right == null ? 0 : right.size();

        int num = Math.max(leftSize, rightSize);
        List<BigDecimal> result = new ArrayList<>(num);

        // go through the pairs of values, defaulting with 0 if past the end of the list,
        // and apply the operator to each pair
        for (int i = 0; i < num; i++) {
            BigDecimal lhs = i < leftSize? left.get(i) : new BigDecimal("0.0");  // get left value, default 0
            BigDecimal rhs = i < rightSize? right.get(i) : new BigDecimal("0.0");   // get right value, default 0
            BigDecimal val = eval.apply(lhs, rhs);      // apply the operation
            result.add(val);
        }
        return result;
    }
}


