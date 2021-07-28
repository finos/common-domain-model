package cdm.base.datetime.functions;

import cdm.base.datetime.DateGroup;
import com.rosetta.model.lib.records.Date;

import java.util.ArrayList;
import java.util.List;

public class PopOffDateListImpl extends PopOffDateList {

    @Override
    protected DateGroup.DateGroupBuilder doEvaluate(DateGroup dateList) {
        if (dateList == null) return DateGroup.builder();

        List<Date> res = doEval(dateList.getDates());
        return DateGroup.builder().setDates(res);
    }

    protected List<Date> doEval(List<? extends Date> dateList) {
        int len = dateList == null ? 0 : dateList.size();
        if (len < 2) return new ArrayList<>(0);
        List<Date> result = new ArrayList<>(len-1);
        for (int i = 0; i < len-1; i++) result.add(dateList.get(i));
        return result;
    }


}
