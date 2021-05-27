package cdm.base.datetime.functions;

import com.rosetta.model.lib.records.Date;

import java.time.Duration;
import java.time.LocalDate;

public class DateDifferenceImpl extends DateDifference{
    @Override
    protected Integer doEvaluate(Date leftDate, Date rightDate) {
       LocalDate left = leftDate.toLocalDate();
       LocalDate right = rightDate.toLocalDate();
       Duration dur =  Duration.between(right.atStartOfDay(), left.atStartOfDay());
       int diff = (int) dur.toDays();
       return Integer.valueOf(diff);
    }
}
