package cdm.base.math.functions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AppendToVectorImpl extends AppendToVector {
    @Override
    protected List<BigDecimal> doEvaluate(List<? extends BigDecimal> vector, BigDecimal value) {
        List<BigDecimal> result = new ArrayList<>(vector.size()+1);
        result.addAll(vector);
        result.add(value);
        return result;
    }
}
