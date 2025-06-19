package cdm.base.datetime.functions;

import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;

import java.time.LocalDate;

public class AddDaysImpl extends AddDays {

    @Inject
    private AddDays func;

    // add a specified number of calendar days to a supplied date.
    @Override
    protected Date doEvaluate(Date inputDate, Integer numDays) {
        if (inputDate == null || numDays == null) return null;
        LocalDate date = inputDate.toLocalDate();
        LocalDate newDate = date.plusDays(numDays);
        return Date.of(newDate);
    }
}
