package cdm.base.datetime.functions;

import cdm.base.datetime.BusinessCenters;
import cdm.base.datetime.DateCollection;
import com.rosetta.model.lib.mapper.MapperBuilder;
import com.rosetta.model.lib.records.Date;

import java.util.List;

public class IsHolidayImpl  extends IsHoliday {

    RetrieveBusinessCenterHolidays retrieveBusinessCenterHolidays = new RetrieveBusinessCenterHolidaysImpl();
    @Override
    protected  Boolean doEvaluate(Date checkDate, BusinessCenters businessCenters) {
        List<? extends Date> hols = retrieveBusinessCenterHolidays.evaluate(businessCenters);
        //List<? extends Date> holidayDates = hols.getDatelist();
        boolean isHol = hols.contains(checkDate);

        return isHol;
    }
}

