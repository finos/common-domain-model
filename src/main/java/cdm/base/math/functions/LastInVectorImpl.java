package cdm.base.math.functions;

import cdm.base.math.Vector;
import com.rosetta.model.lib.records.Date;

import java.math.BigDecimal;
import java.util.List;

public class LastInVectorImpl extends LastInVector {

    @Override
    protected BigDecimal doEvaluate(Vector vector) {
        // get original list values if supplied
        List<BigDecimal> values = vector == null ? null : vector.getValues();

        // return last in the list if the list is not empty
        if (values !=null && values.size() > 0) {
            return values.get(values.size()-1);
        }
        return null;
    }
}
