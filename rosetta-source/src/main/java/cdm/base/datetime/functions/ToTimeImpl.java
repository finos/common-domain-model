package cdm.base.datetime.functions;

import java.time.LocalTime;
import java.util.Optional;

@SuppressWarnings("unused")
public class ToTimeImpl extends ToTime {

    @Override
    protected LocalTime doEvaluate(Integer hours, Integer minutes, Integer seconds) {
        int h = Optional.ofNullable(hours).orElse(0);
        int m = Optional.ofNullable(minutes).orElse(0);
        int s = Optional.ofNullable(seconds).orElse(0);
        return LocalTime.of(h, m ,s);
    }
}
