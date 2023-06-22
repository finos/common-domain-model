package cdm.base.datetime.functions;

import cdm.base.datetime.AdjustableOrRelativeDate;
import cdm.product.common.schedule.functions.AdjustableDateUtils;
import com.rosetta.model.lib.records.Date;

public class ResolveAdjustableDateImpl extends ResolveAdjustableDate {

    @Override
    protected Date doEvaluate(AdjustableOrRelativeDate adjustableOrRelativeDate) {
        return AdjustableDateUtils.adjustDate(adjustableOrRelativeDate);
    }
}
