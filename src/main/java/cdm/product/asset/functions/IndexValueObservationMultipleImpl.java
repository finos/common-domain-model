package cdm.product.asset.functions;

import cdm.base.math.Vector;
import cdm.observable.asset.FloatingRateOption;
import com.rosetta.model.lib.records.Date;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class IndexValueObservationMultipleImpl  extends IndexValueObservationMultiple{
    @Override
    protected Vector.VectorBuilder doEvaluate(List<? extends Date> observationDate, FloatingRateOption floatingRateOption) {
        if (observationDate==null || observationDate.size() ==0) {
            return Vector.builder();
        }
        IndexValueObservationImpl obs = IndexValueObservationImpl.getInstance();
        List<Date> dates = new ArrayList<>(observationDate.size());
        for (int i = 0; i < observationDate.size(); i++) {
            Date myDate = observationDate.get(i);
            dates.add(myDate);
        }
        List<? extends BigDecimal> vals = obs.getValues(dates, floatingRateOption);
        Vector.VectorBuilder vb = Vector.builder();
        if (vals != null) vb.addValues(vals);
        return vb;
    }
}
