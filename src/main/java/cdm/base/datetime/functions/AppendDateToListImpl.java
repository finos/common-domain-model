package cdm.base.datetime.functions;

import cdm.base.datetime.DateGroup;
import com.rosetta.model.lib.records.Date;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AppendDateToListImpl extends  AppendDateToList{

    @Override
    protected DateGroup.DateGroupBuilder doEvaluate(DateGroup dateList, Date newDate) {
        if (dateList == null && newDate==null) return DateGroup.builder();      // if provided nothing return an empty date group
        if (dateList == null) return DateGroup.builder().addDates(newDate);     // if empty list return the date
        if (newDate == null) return DateGroup.builder().setDates(dateList.getDates());  // if missing date return the list

        List<Date> res = doEval(dateList.getDates(), newDate);
        DateGroup.DateGroupBuilder ret = DateGroup.builder().setDates(res);
        return ret;
    }

    protected List<Date> doEval(List<? extends Date> dateList, Date newDate) {
        List<Date> result = new ArrayList<>(dateList.size()+1);
        result.addAll(dateList);
        result.add(newDate);
        return result;
    }


}
