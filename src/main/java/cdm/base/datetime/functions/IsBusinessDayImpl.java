package cdm.base.datetime.functions;

import cdm.base.datetime.BusinessCenters;
import com.rosetta.model.lib.records.Date;

public class IsBusinessDayImpl extends IsBusinessDay{
    @Override
    protected Boolean doEvaluate(Date date, BusinessCenters businessCenters) {
        return Boolean.FALSE;
    }
}
