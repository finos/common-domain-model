package cdm.base.datetime.functions;

import cdm.base.datetime.*;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.validation.ModelObjectValidator;

import java.util.*;

public class RetrieveBusinessCenterHolidaysImpl extends RetrieveBusinessCenterHolidays {
    protected static Map<String, DateGroup.DateGroupBuilder> cache = new HashMap<>(); // cache of combined holiday data lists for lists of business centers
    protected static Map<BusinessCenterEnum, List<Date>> holidayData = new HashMap<>(); // raw holiday lists


    protected DateGroup.DateGroupBuilder doEvaluate(BusinessCenters businessCenters) {
        String key = getKey(businessCenters);   //get a key based on the list of business centers

        // check if the combined list is cached and if so return it
        DateGroup cached = cache.get(key);
        if (cached != null) return cached.toBuilder();

        // otherwise generat the combined list, cache it, and return it
        DateGroup.DateGroupBuilder generated = generateHolidayList(businessCenters);
        cache.put(key, generated);
        return generated.toBuilder();
    }


    // generate a combined list of holidays for a list of businecss centers
    private DateGroup.DateGroupBuilder generateHolidayList(BusinessCenters bcs) {
        // retrive the list of business centers as a list
        List<? extends FieldWithMetaBusinessCenterEnum> bcl =  getBC(bcs);

        Set<Date> newHols = new TreeSet<>();    // initialize a sorted set to hold the dates
        // for each business center
        for (FieldWithMetaBusinessCenterEnum bc: bcl) {
            List<Date>  holidays = getHolidays(bc); // get the list of holidays for the bc
            if(holidays!= null) newHols.addAll(holidays);   // add it to the set of holidays to be return
        }
        // convert the sorted set to a list
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

    // create a key by concatenating the BC codes
    private String getKey(BusinessCenters bcs) {
        List<? extends FieldWithMetaBusinessCenterEnum> bcl = getBC(bcs);
        StringBuilder key = new StringBuilder();
        for (FieldWithMetaBusinessCenterEnum bc: bcl) {
            if(bc == null || bc.getValue() == null) continue;
            key.append(bc.getValue().toString());
        }
        return key.toString();
    }

    // retrieve the list of business centers to a BusinessCenters structure, dereferencing if necessary
    private List<? extends FieldWithMetaBusinessCenterEnum> getBC(BusinessCenters bc) {
        if (bc == null) return new ArrayList<>();
        ReferenceWithMetaBusinessCenters ref = bc.getBusinessCentersReference();
        if (ref != null) {
            BusinessCenters refBC = ref.getValue();
            return getBC(refBC);
        }
        return bc.getBusinessCenter();
    }
}
