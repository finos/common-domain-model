package cdm.product.asset.functions;

import cdm.observable.asset.FloatingRateOption;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexValueObservationImpl extends IndexValueObservation {

    static IndexValueObservationImpl singleton = new IndexValueObservationImpl();
    BigDecimal defaultValue = null;
    Map<String, Map<Date,BigDecimal>> indexData = new HashMap<>();


    public static IndexValueObservationImpl getInstance() {
        return singleton;
    }

    @Override
    protected BigDecimal doEvaluate(Date observationDate, FloatingRateOption floatingRateOption) {
        IndexValueObservationImpl iv = getInstance();
        return iv.getValue(observationDate, floatingRateOption);
    }

    public BigDecimal getValue(Date observationDate, FloatingRateOption floatingRateOption) {
        Map<Date,BigDecimal>valsForIndex = indexData.get(getKey(floatingRateOption));
        if (valsForIndex == null) return defaultValue;
        BigDecimal val = valsForIndex.get(observationDate);
        return val == null ? defaultValue : val;
    }

    public List<BigDecimal> getValues(List<Date> observationDates, FloatingRateOption floatingRateOption) {
        Map<Date,BigDecimal>valsForIndex = indexData.get(getKey(floatingRateOption));
        if (valsForIndex == null) return null;
        List<BigDecimal> vals = new ArrayList<>(observationDates.size());
        for (Date observationDate : observationDates) {
            BigDecimal val = valsForIndex.get(observationDate);
            val =  val == null ? defaultValue : val;
            vals.add(val);
        }
        return vals;
    }

    public void setDefaultValue(double val) {
        defaultValue = BigDecimal.valueOf(val);
    }

    public void setValues(FloatingRateOption fro, Date startingDate, int numDays, double value, double increment) {
        LocalDate start = startingDate.toLocalDate();
        for(int i = 0; i < numDays; i++) {
            LocalDate dt = start.plusDays(i);
            Date date = DateImpl.of(dt);
            double val = value + increment * i;
            setValue(fro, date, val);

        }
    }
    public void setValue(FloatingRateOption fro, Date date, double value) {
        Map<Date, BigDecimal> valueMap = indexData.computeIfAbsent(getKey(fro), k -> new HashMap<>());
        valueMap.put(date, BigDecimal.valueOf(value));
    }

    String getKey(FloatingRateOption fro) {
        return fro.getFloatingRateIndex().getValue().toString() + ":" + fro.getIndexTenor().getPeriodMultiplier().toString() + fro.getIndexTenor().getPeriod().toString();
    }

}
