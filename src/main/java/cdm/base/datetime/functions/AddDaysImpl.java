package cdm.base.datetime.functions;

import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;

import java.time.LocalDate;

public class AddDaysImpl extends AddDays {

    @Inject
    private AddDays func;

    @Override
    protected Date doEvaluate(Date inputDate, Integer numDays) {
        LocalDate date = inputDate.toLocalDate();
        LocalDate newDate = date.plusDays(numDays);
        return new DateImpl(newDate);
    }
}
