package cdm.base.math.functions;

import java.math.BigDecimal;
import java.util.List;

public class SelectFromVectorImpl extends SelectFromVector{
    @Override
    protected BigDecimal doEvaluate(List<? extends BigDecimal> vector, Integer index) {
        return (index >=0 && index < vector.size()) ? vector.get(index) : null;
    }
}
