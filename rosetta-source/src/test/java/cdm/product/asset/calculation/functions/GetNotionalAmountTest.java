package cdm.product.asset.calculation.functions;

import cdm.base.math.DatedValue;
import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.UnitType;
import cdm.observable.asset.Money;
import cdm.product.asset.InterestRatePayout;
import cdm.product.common.schedule.CalculationPeriod;
import cdm.product.common.schedule.CalculationPeriodBase;
import cdm.product.common.settlement.ResolvablePriceQuantity;
import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetNotionalAmountTest extends AbstractFunctionTest {

    @Inject
    private GetNotionalAmount func;

    @Test
    void shouldLookupValue() {
        InterestRatePayout interestRatePayout = InterestRatePayout.builder()
                .setPriceQuantity(initNotionalSchedule())
                .build();

        CalculationPeriodBase dec1_2020 = CalculationPeriod.builder()
                .setAdjustedStartDate(Date.of(2020, 12, 10))
                .setAdjustedEndDate(Date.of(2021, 3, 10)).build();
        CalculationPeriodBase mar1 = CalculationPeriod.builder()
                .setAdjustedStartDate(Date.of(2021, 3, 10))
                .setAdjustedEndDate(Date.of(2021, 6, 10)).build();
        CalculationPeriodBase jun1 = CalculationPeriod.builder()
                .setAdjustedStartDate(Date.of(2021, 6, 10))
                .setAdjustedEndDate(Date.of(2021, 9, 10)).build();
        CalculationPeriodBase sep1 = CalculationPeriod.builder()
                .setAdjustedStartDate(Date.of(2021, 9, 10))
                .setAdjustedEndDate(Date.of(2021, 12, 10)).build();
        CalculationPeriodBase dec1 = CalculationPeriod.builder()
                .setAdjustedStartDate(Date.of(2021, 12, 10))
                .setAdjustedEndDate(Date.of(2022, 3, 10)).build();

        Money nineMillion = Money.builder()
                .setUnit(UnitType.builder().setCurrencyValue("USD"))
                .setValue(BigDecimal.valueOf(9_000_000));
        Money tenMillion = Money.builder()
                .setUnit(UnitType.builder().setCurrencyValue("USD"))
                .setValue(BigDecimal.valueOf(10_000_000));
        Money elevenMillion = Money.builder()
                .setUnit(UnitType.builder().setCurrencyValue("USD"))
                .setValue(BigDecimal.valueOf(11_000_000));
        Money twelveMillion = Money.builder()
                .setUnit(UnitType.builder().setCurrencyValue("USD"))
                .setValue(BigDecimal.valueOf(12_000_000));
        Money thirteenMillion = Money.builder()
                .setUnit(UnitType.builder().setCurrencyValue("USD"))
                .setValue(BigDecimal.valueOf(13_000_000));

        assertEquals(thirteenMillion, func.evaluate(interestRatePayout, dec1));
        assertEquals(elevenMillion, func.evaluate(interestRatePayout, jun1));
        assertEquals(nineMillion, func.evaluate(interestRatePayout, dec1_2020));
        assertEquals(tenMillion, func.evaluate(interestRatePayout, mar1));
        assertEquals(twelveMillion, func.evaluate(interestRatePayout, sep1));
    }

    public static ResolvablePriceQuantity initNotionalSchedule() {
        return ResolvablePriceQuantity.builder()
                .setQuantityScheduleValue(NonNegativeQuantitySchedule.builder()
                        .setValue(BigDecimal.valueOf(9_000_000))
                        .setUnit(UnitType.builder().setCurrencyValue("USD"))
                        .addDatedValue(DatedValue.builder()
                                .setDate(Date.of(2021, 3, 10))
                                .setValue(BigDecimal.valueOf(10_000_000)))
                        .addDatedValue(DatedValue.builder()
                                .setDate(Date.of(2021, 6, 10))
                                .setValue(BigDecimal.valueOf(11_000_000)))
                        .addDatedValue(DatedValue.builder()
                                .setDate(Date.of(2021, 9, 10))
                                .setValue(BigDecimal.valueOf(12_000_000)))
                        .addDatedValue(DatedValue.builder()
                                .setDate(Date.of(2021, 12, 10))
                                .setValue(BigDecimal.valueOf(13_000_000))))
                .build();

    }
}
