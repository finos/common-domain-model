package cdm.base.datetime.functions;

import com.rosetta.model.lib.records.Date;

import java.time.LocalDate;
import java.time.chrono.IsoChronology;

public class LeapYearDateDifferenceImpl extends LeapYearDateDifference {
    @Override
    protected Integer doEvaluate(Date firstDate, Date secondDate) {
        if (firstDate == null || secondDate == null) return null;

        LocalDate startDate = firstDate.toLocalDate();
        LocalDate endDate = secondDate.toLocalDate();

        int daysThatAreInLeapYear = 0;
        boolean reversed = endDate.isBefore(startDate);
        int multiplier = reversed? -1 :1 ;
        if (reversed) {
            LocalDate temp = endDate;
            endDate = startDate;
            startDate = temp;
        }
        for (LocalDate myDate = startDate; myDate.isBefore(endDate); myDate = myDate.plusDays(1)) {
            if (IsoChronology.INSTANCE.isLeapYear(myDate.getYear())) {
                daysThatAreInLeapYear++;
            }
        }
        return multiplier * daysThatAreInLeapYear;
    }

}
