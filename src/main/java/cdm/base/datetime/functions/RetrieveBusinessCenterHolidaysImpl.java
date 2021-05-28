package cdm.base.datetime.functions;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.BusinessCenters;
//import cdm.base.datetime.DateCollection;
import cdm.base.datetime.DateCollection;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import com.rosetta.model.lib.records.Date;

import java.util.*;

public class RetrieveBusinessCenterHolidaysImpl extends RetrieveBusinessCenterHolidays{
    protected static Map<String, List<Date>> cache = new HashMap<>();
    protected static Map<BusinessCenterEnum, List<Date>> holidayData = new HashMap<>();

    @Override
    protected List<Date> doEvaluate(BusinessCenters businessCenters) {
        String key = getKey(businessCenters);
        List<Date> cached = cache.get(key);
        if (cached != null) return cached;
        List<Date> generated = generateHolidayList(businessCenters);
        cache.put(key, generated);
        return generated;
    }


    private List<Date> generateHolidayList(BusinessCenters bcs) {
        List<? extends FieldWithMetaBusinessCenterEnum> bcl =  getBC(bcs);
        Set<Date> newHols = new TreeSet<>();
        for (FieldWithMetaBusinessCenterEnum bc: bcl) {
            List<Date>  holidays = getHolidays(bc);
            newHols.addAll(holidays);
        }
        List<Date> result = new ArrayList<>(newHols.size());
        result.addAll(newHols);
        return result;
        //DateCollection.DateCollectionBuilder builder = DateCollection.builder();
        //builder.addDatelist(result);
        //return builder;
    }

    private List<Date> getHolidays(FieldWithMetaBusinessCenterEnum bc) {
        return holidayData.get(bc.getValue());
    }

    public void setHolidays(BusinessCenterEnum bc, List<Date> holidays) {
        holidayData.put(bc, holidays);
    }

    public void addHolidays(BusinessCenterEnum bc, List<Date> holidays) {
        List<Date> existingHolidays = holidayData.get(bc);
        existingHolidays.addAll(holidays);
    }


    private String getKey(BusinessCenters bcs) {
        List<? extends FieldWithMetaBusinessCenterEnum> bcl = getBC(bcs);
        String key = "";
        for (FieldWithMetaBusinessCenterEnum bc: bcl) {
            key += bc.getValue().toString();
        }
        return key;
    }

    private List<? extends FieldWithMetaBusinessCenterEnum> getBC(BusinessCenters bc) {
        ReferenceWithMetaBusinessCenters ref = bc.getBusinessCentersReference();
        if (ref != null) {
            BusinessCenters refBC = ref.getValue();
            return getBC(refBC);
        }
        return bc.getBusinessCenter();
    }
}
