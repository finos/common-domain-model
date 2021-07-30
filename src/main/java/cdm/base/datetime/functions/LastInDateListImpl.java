package cdm.base.datetime.functions;

import cdm.base.datetime.DateGroup;
import com.rosetta.model.lib.records.Date;

import java.util.List;

public class LastInDateListImpl extends LastInDateList {

    @Override
    protected Date doEvaluate(DateGroup dateList) {
        if (dateList == null) return null;
        List<? extends Date> dates = dateList.getDates();
        if (dates !=null && dates.size() > 0) {
            return dates.get(dates.size()-1);
        }
        return null;
    }
}
