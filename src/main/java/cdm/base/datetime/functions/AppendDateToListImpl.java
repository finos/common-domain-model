package cdm.base.datetime.functions;

import com.rosetta.model.lib.records.Date;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AppendDateToListImpl extends  AppendDateToList{

    @Override
    protected List<Date> doEvaluate(List<? extends Date> dateList, Date newDate) {
        List<Date> result = new ArrayList<>(dateList.size()+1);
        result.addAll(dateList);
        result.add(newDate);
        return result;
    }
}
