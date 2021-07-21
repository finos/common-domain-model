package cdm.base.math.functions;

import cdm.base.math.Vector;

import java.math.BigDecimal;
import java.util.List;

public class SelectFromVectorImpl extends SelectFromVector{

    // select a vlaue from a vector by index.  return null if not in range
    @Override
    protected BigDecimal doEvaluate(Vector vector, Integer index) {
        if (vector == null || index == null) return null;
        List<? extends  BigDecimal> vect = vector.getValues();
        if (vect == null) return null;
        return (index >=0 && index < vect.size()) ? vect.get(index) : null;
    }
}
