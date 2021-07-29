package cdm.base.datetime.functions;

import com.rosetta.model.lib.records.Date;

import java.time.Duration;
import java.time.LocalDate;

public class DateDifferenceImpl extends DateDifference{
    @Override
    protected Integer doEvaluate(Date firstDate, Date secondDate) {
        if (firstDate == null || secondDate == null) return null;
       LocalDate second = secondDate.toLocalDate();
       LocalDate first = firstDate.toLocalDate();
       Duration dur =  Duration.between(first.atStartOfDay(), second.atStartOfDay());
       return (int) dur.toDays();
    }
}
