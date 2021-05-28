package cdm.base.datetime.functions;

import cdm.base.datetime.BusinessCenters;
import com.rosetta.model.lib.mapper.Mapper;
import com.rosetta.model.lib.records.Date;

import java.util.ArrayList;
import java.util.List;

public class GenerateDateListImpl extends GenerateDateList {


    AppendDateToList appender = new AppendDateToListImpl();

    @Override
    protected List<Date> doEvaluate(Date startDate, Date endDate, BusinessCenters businessCenters) {
        Mapper<Boolean> isActive = active(startDate, endDate, businessCenters);
        boolean act = isActive.get();
        if (!act) return List.of();

        Mapper<Boolean> isGoodDay = isGoodBusinessDay(startDate, endDate, businessCenters);
        boolean good = isGoodDay.get();

        Mapper<? extends Date> prior = priorDate(startDate, endDate, businessCenters);
        Date priorDt = prior.get();
        List<? extends Date> priorList = evaluate(startDate,  priorDt, businessCenters);

        List<? extends Date> newList = (good) ? appender.evaluate(priorList, endDate) : priorList;

        List<Date> result = new ArrayList<>(newList.size());
        result.addAll(newList);
        return result;
    }
}
