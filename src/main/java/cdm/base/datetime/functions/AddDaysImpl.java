package cdm.base.datetime.functions;

import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;

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
        return DateImpl.of(newDate);
    }
}
