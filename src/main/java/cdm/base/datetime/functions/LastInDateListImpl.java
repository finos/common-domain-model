package cdm.base.datetime.functions;

import com.rosetta.model.lib.records.Date;

import java.util.List;

public class LastInDateListImpl extends LastInDateList {

    @Override
    protected Date doEvaluate(List<Date> dates) {
        if (dates !=null && dates.size() > 0) {
            return dates.get(dates.size()-1);
        }
        return null;
    }
}
