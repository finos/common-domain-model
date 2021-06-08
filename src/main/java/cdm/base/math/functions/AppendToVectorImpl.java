package cdm.base.math.functions;

import cdm.base.math.Vector;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AppendToVectorImpl extends AppendToVector {

    @Override
    protected Vector.VectorBuilder doEvaluate(Vector vector, BigDecimal value) {
        List<? extends BigDecimal> orig = vector == null ? null : vector.getValues();
        List<BigDecimal> result = new ArrayList<>(orig == null ? 1 : orig.size()+1);
        if (orig != null) result.addAll(orig);
        if (value != null) result.add(value);
        Vector.VectorBuilder builder = Vector.builder();
        builder.addValues(result);
        return builder;
    }
}
