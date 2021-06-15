package cdm.product.asset.functions;

import cdm.base.datetime.*;
import cdm.observable.asset.FloatingRateOption;
import cdm.product.asset.FloatingRate;
import cdm.product.asset.FloatingRateSettingDetails;
import cdm.product.common.schedule.CalculationPeriodBase;
import cdm.product.common.schedule.ResetDates;
import cdm.product.common.schedule.ResetFrequency;
import cdm.product.common.schedule.ResetRelativeToEnum;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EvaluateTermRateTest extends AbstractFunctionTest {

    @Inject
    private EvaluateTermRate func;

    @Test
    void shouldEvaluateRate() {
        FloatingRateOption fro = IndexValueObservationImplTest.initFro();
        FloatingRate rate = GetFloatingRateConditionParametersTest.initFloatingRate(fro);
        ResetDates resetDates = initResetDates(BusinessCenterEnum.GBLO, 3, 2, false);

        IndexValueObservationImplTest.initIndexData(fro);

        CalculationPeriodBase dec2020 = period(date(2020, 12, 10), date(2021, 3, 10));
        FloatingRateSettingDetails result = func.evaluate(rate, resetDates, dec2020);
        check(result, 0.01, date(2021, 3, 8));
    }

    public CalculationPeriodBase period (Date start, Date end) { return GetFloatingRateConditionParametersTest.period(start, end);}
    public Date date (int yy, int mm, int dd) { return GetFloatingRateConditionParametersTest.date(yy, mm, dd); }


    public static ResetDates initResetDates(BusinessCenterEnum bc, int freq, int offsetDays, boolean inAdvance) {
        BusinessCenters myBC = BusinessCenters.builder()
                .addBusinessCenterValue(bc).build();
        return ResetDates.builder()
                .setFixingDates(RelativeDateOffset.builder()
                        .setBusinessCenters(myBC)
                        .setPeriod(PeriodEnum.D)
                        .setPeriodMultiplier(-offsetDays)
                        .build())
                .setResetFrequency(ResetFrequency.builder()
                        .setPeriod(PeriodExtendedEnum.M)
                        .setPeriodMultiplier(freq)
                        .build())
                .setResetDatesAdjustments(BusinessDayAdjustments.builder()
                        .setBusinessCenters(myBC)
                        .build())
                .setResetRelativeTo(inAdvance? ResetRelativeToEnum.CALCULATION_PERIOD_START_DATE : ResetRelativeToEnum.CALCULATION_PERIOD_END_DATE)
                .build();
    }

    void check(FloatingRateSettingDetails result, double finalRate, Date fixingDate) {
        assertEquals(BigDecimal.valueOf(finalRate), result.getFloatingRate());
        assertEquals(fixingDate, result.getObservationDate());
    }
}
