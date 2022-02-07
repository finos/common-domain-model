package cdm.base.datetime.functions;

import cdm.base.datetime.DayOfWeekEnum;
import com.rosetta.model.lib.records.Date;

import java.time.LocalDate;

public class DayOfWeekImpl extends DayOfWeek{
    @Override
    protected DayOfWeekEnum doEvaluate(Date date) {
        if (date == null) return null;

        LocalDate localDate = date.toLocalDate();
        java.time.DayOfWeek dow = localDate.getDayOfWeek();
        DayOfWeekEnum result = null;
        int dayNum = dow.getValue();
        switch (dayNum) {
            case 1: result = DayOfWeekEnum.MON; break;
            case 2: result = DayOfWeekEnum.TUE; break;
            case 3: result = DayOfWeekEnum.WED; break;
            case 4: result = DayOfWeekEnum.THU; break;
            case 5: result = DayOfWeekEnum.FRI; break;
            case 6: result = DayOfWeekEnum.SAT; break;
            case 7: result = DayOfWeekEnum.SUN; break;
        }
        return result;
    }
}
