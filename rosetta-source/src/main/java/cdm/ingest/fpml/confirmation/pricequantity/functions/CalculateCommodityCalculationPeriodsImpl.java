package cdm.ingest.fpml.confirmation.pricequantity.functions;

import cdm.ingest.fpml.confirmation.product.commodityoption.functions.CalculateCommodityCalculationPeriods;
import com.rosetta.model.lib.records.Date;
import fpml.consolidated.com.CommodityCalculationPeriodsSchedule;
import fpml.consolidated.fpmlenum.PeriodExtendedEnum;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CalculateCommodityCalculationPeriodsImpl extends CalculateCommodityCalculationPeriods {
    @Override
    protected List<Date> doEvaluate(CommodityCalculationPeriodsSchedule fpmlCommodityCalculationPeriodsSchedule, Date effectiveDate, Date terminationDate) {
        if (fpmlCommodityCalculationPeriodsSchedule == null) {
            return new ArrayList<>();
        }

        PeriodExtendedEnum period = fpmlCommodityCalculationPeriodsSchedule.getPeriod();
        Integer periodMultiplier = fpmlCommodityCalculationPeriodsSchedule.getPeriodMultiplier();

        if (period == null || periodMultiplier == null) {
            return new ArrayList<>();
        }

        boolean balanceOfFirstPeriod = Optional.ofNullable(fpmlCommodityCalculationPeriodsSchedule.getBalanceOfFirstPeriod()).orElse(false);

        LocalDate currentDate = effectiveDate.toLocalDate();

        List<Date> periodDates = new ArrayList<>();
        periodDates.add(Date.of(currentDate));

        while (currentDate.isBefore(terminationDate.toLocalDate())) {
            currentDate = getNextPeriodStartDate(currentDate, periodMultiplier, period, balanceOfFirstPeriod);
            periodDates.add(Date.of(currentDate));
        }
        return periodDates;
    }

    private LocalDate getNextPeriodStartDate(LocalDate date, int periodMultiplier, PeriodExtendedEnum period, boolean balanceOfFirstPeriod) {
        switch (period) {
            case M:
                return balanceOfFirstPeriod
                        ? date.plusMonths(periodMultiplier).withDayOfMonth(1)
                        : date.plusMonths(periodMultiplier);
            case D:
                return date.plusDays(periodMultiplier);
            case W:
                return date.plusWeeks(periodMultiplier);
            case Y:
                return date.plusYears(periodMultiplier);
            default:
                return date;
        }
    }
}
