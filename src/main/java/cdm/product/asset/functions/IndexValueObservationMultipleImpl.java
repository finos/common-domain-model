package cdm.product.asset.functions;

import cdm.base.datetime.DateGroup;
import cdm.base.math.Vector;
import cdm.observable.asset.FloatingRateOption;
import com.rosetta.model.lib.records.Date;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class IndexValueObservationMultipleImpl  extends IndexValueObservationMultiple{
    @Override
    protected Vector.VectorBuilder doEvaluate(DateGroup observationDates, FloatingRateOption floatingRateOption) {
        List<? extends Date> observationDate = observationDates.getDates();
        if (observationDate==null || observationDate.size() ==0) {
            return Vector.builder();
        }
        IndexValueObservationImpl obs = IndexValueObservationImpl.getInstance();
        List<Date> dates = new ArrayList<>(observationDate.size());
        dates.addAll(observationDate);
        List<? extends BigDecimal> vals = obs.getValues(dates, floatingRateOption);
        Vector.VectorBuilder vb = Vector.builder();
        if (vals != null) vb.addValues(vals);
        return vb;
    }
}
