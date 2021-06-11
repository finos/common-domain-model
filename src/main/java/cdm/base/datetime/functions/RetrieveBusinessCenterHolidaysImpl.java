package cdm.base.datetime.functions;

import cdm.base.datetime.*;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.validation.ModelObjectValidator;

import java.util.*;

public class RetrieveBusinessCenterHolidaysImpl extends RetrieveBusinessCenterHolidays{
    protected static Map<String, DateGroup.DateGroupBuilder> cache = new HashMap<>();
    protected static Map<BusinessCenterEnum, List<Date>> holidayData = new HashMap<>();


    protected DateGroup.DateGroupBuilder doEvaluate(BusinessCenters businessCenters) {
        objectValidator = new NoOpValidator();
        String key = getKey(businessCenters);
        DateGroup cached = cache.get(key);
        if (cached != null) return cached.toBuilder();
        DateGroup.DateGroupBuilder generated = generateHolidayList(businessCenters);
        cache.put(key, generated);
        return generated.toBuilder();
    }


    private DateGroup.DateGroupBuilder generateHolidayList(BusinessCenters bcs) {
        List<? extends FieldWithMetaBusinessCenterEnum> bcl =  getBC(bcs);
        Set<Date> newHols = new TreeSet<>();
        for (FieldWithMetaBusinessCenterEnum bc: bcl) {
            List<Date>  holidays = getHolidays(bc);
            if(holidays!= null) newHols.addAll(holidays);
        }
        List<Date> result = new ArrayList<>(newHols.size());
        result.addAll(newHols);
        return DateGroup.builder().setDates(result);
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

    private class NoOpValidator implements ModelObjectValidator {
        @Override
        public <T extends RosettaModelObject> void validateAndFailOnErorr(Class<T> topClass, T modelObject) {
        }

        @Override
        public <T extends RosettaModelObject> void validateAndFailOnErorr(Class<T> topClass, List<? extends T> modelObjects) {
        }
    }
}
