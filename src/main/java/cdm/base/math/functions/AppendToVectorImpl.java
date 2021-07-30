package cdm.base.math.functions;

import cdm.base.math.Vector;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AppendToVectorImpl extends AppendToVector {

    // append a value to a vector
    @Override
    protected Vector.VectorBuilder doEvaluate(Vector vector, BigDecimal value) {
        // get original list
        List<? extends BigDecimal> orig = vector == null ? null : vector.getValues();
        // allocate space for new list
        List<BigDecimal> result = new ArrayList<>(orig == null ? 1 : orig.size()+1);
        // add original list values and new value to the return list
        if (orig != null) result.addAll(orig);
        if (value != null) result.add(value);

        // convert to a builder for return
       return Vector.builder().addValues(result);
    }
}
