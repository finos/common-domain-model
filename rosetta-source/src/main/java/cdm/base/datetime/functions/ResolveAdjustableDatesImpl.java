package cdm.base.datetime.functions;

import cdm.base.datetime.AdjustableRelativeOrPeriodicDates;
import cdm.product.common.schedule.functions.AdjustableDateUtils;
import com.rosetta.model.lib.records.Date;

import java.util.List;

public class ResolveAdjustableDatesImpl extends ResolveAdjustableDates {

    @Override
    protected List<Date> doEvaluate(AdjustableRelativeOrPeriodicDates adjustableRelativeOrPeriodicDates) {
        return AdjustableDateUtils.adjustDates(adjustableRelativeOrPeriodicDates);
    }
}
