package cdm.product.asset.calculation.functions;

import cdm.base.math.*;
import cdm.base.math.metafields.ReferenceWithMetaQuantity;
import cdm.observable.asset.Money;
import cdm.product.asset.InterestRatePayout;
import cdm.product.common.schedule.CalculationPeriod;
import cdm.product.common.schedule.CalculationPeriodBase;
import cdm.product.common.settlement.ResolvablePayoutQuantity;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.DateImpl;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LookupNotionalAmountTest extends AbstractFunctionTest {

    @Inject
    private LookupNotionalAmount func;

    @Test
    void shouldLookupValue() {

        InterestRatePayout interestRatePayout = InterestRatePayout.builder()
                .setPayoutQuantity(initNotionalSchedule())
                .build();


        CalculationPeriodBase dec1_2020 = CalculationPeriod.builder()
                .setAdjustedStartDate(DateImpl.of(2020, 12,10))
                .setAdjustedEndDate(DateImpl.of(2021, 3,10)).build();
        CalculationPeriodBase mar1 = CalculationPeriod.builder()
                .setAdjustedStartDate(DateImpl.of(2021, 3,10))
                .setAdjustedEndDate(DateImpl.of(2021, 6,10)).build();
        CalculationPeriodBase jun1 = CalculationPeriod.builder()
                .setAdjustedStartDate(DateImpl.of(2021, 6,10))
                .setAdjustedEndDate(DateImpl.of(2021, 9,10)).build();
        CalculationPeriodBase sep1 = CalculationPeriod.builder()
                .setAdjustedStartDate(DateImpl.of(2021, 9,10))
                .setAdjustedEndDate(DateImpl.of(2021, 12,10)).build();
        CalculationPeriodBase dec1 = CalculationPeriod.builder()
                .setAdjustedStartDate(DateImpl.of(2021, 12,10))
                .setAdjustedEndDate(DateImpl.of(2022, 3,10)).build();

        Money ninemillion = Money.builder()
                .setMultiplierUnit(UnitType.builder().setCurrency(FieldWithMetaString.builder().setValue("USD")))
                .setMultiplier(BigDecimal.valueOf(9_000_000));
        Money tenmillion = Money.builder()
                .setMultiplierUnit(UnitType.builder().setCurrency(FieldWithMetaString.builder().setValue("USD")))
                .setMultiplier(BigDecimal.valueOf(10_000_000));
        Money elevenmillion = Money.builder()
                .setMultiplierUnit(UnitType.builder().setCurrency(FieldWithMetaString.builder().setValue("USD")))
                .setMultiplier(BigDecimal.valueOf(11_000_000));
        Money twelvemillion = Money.builder()
                .setMultiplierUnit(UnitType.builder().setCurrency(FieldWithMetaString.builder().setValue("USD")))
                .setMultiplier(BigDecimal.valueOf(12_000_000));
        Money thirteenmillion = Money.builder()
                .setMultiplierUnit(UnitType.builder().setCurrency(FieldWithMetaString.builder().setValue("USD")))
                .setMultiplier(BigDecimal.valueOf(13_000_000));


        check(thirteenmillion, func.evaluate(interestRatePayout, dec1));
        check(elevenmillion, func.evaluate(interestRatePayout, jun1));
        check(ninemillion, func.evaluate(interestRatePayout, dec1_2020));
        check(tenmillion, func.evaluate(interestRatePayout, mar1));
        check(twelvemillion, func.evaluate(interestRatePayout, sep1));
 //       check(0.0101, func.evaluate(interestRatePayout, DateImpl.of(2021, 04, 01)));
 //       check(0.0101, func.evaluate(interestRatePayout, DateImpl.of(2021, 03,01)));
 //       check(0.0102, func.evaluate(interestRatePayout, DateImpl.of(2021, 06,01)));
 //       check(0.0103, func.evaluate(interestRatePayout, DateImpl.of(2021, 9,01)));
 //       check(0.0103, func.evaluate(interestRatePayout, DateImpl.of(2021, 11,30)));
 //       check(0.0104, func.evaluate(interestRatePayout, DateImpl.of(2021, 12,1)));
 //       check(0.0104, func.evaluate(interestRatePayout, DateImpl.of(2021, 12,31)));


    }

    public static ResolvablePayoutQuantity initNotionalSchedule () {
        return ResolvablePayoutQuantity.builder()
                        .setQuantitySchedule(NonNegativeQuantitySchedule.builder()
                                .setInitialQuantity(ReferenceWithMetaQuantity.builder()
                                        .setValue(Quantity.builder()
                                                .setMultiplier(BigDecimal.valueOf(9_000_000))
                                                .setMultiplierUnit(UnitType.builder()
                                                        .setCurrency(FieldWithMetaString.builder()
                                                                .setValue("USD").build())
                                                        .build())
                                                .build())
                                        .build())
                                .setStepSchedule(NonNegativeStepSchedule.builder()
                                        .addStep(NonNegativeStep.builder()
                                                .setStepDate(DateImpl.of(2021,3, 10))
                                                .setStepValue(BigDecimal.valueOf(10_000_000))
                                                .build())
                                        .addStep(NonNegativeStep.builder()
                                                .setStepDate(DateImpl.of(2021,6, 10))
                                                .setStepValue(BigDecimal.valueOf(11_000_000))
                                                .build())
                                        .addStep(NonNegativeStep.builder()
                                                .setStepDate(DateImpl.of(2021,9, 10))
                                                .setStepValue(BigDecimal.valueOf(12_000_000))
                                                .build())
                                        .addStep(NonNegativeStep.builder()
                                                .setStepDate(DateImpl.of(2021,12, 10))
                                                .setStepValue(BigDecimal.valueOf(13_000_000))
                                                .build())
                                        .build())
                                .build())
                        .build();

    }

    void check (Money expected, Money actual) {
        String exp = expected.getMultiplierUnit().getCurrency().getValue();
        String act = actual.getMultiplierUnit().getCurrency().getValue();
        assertEquals(exp, act);
        BigDecimal e = expected.getMultiplier();
        BigDecimal a = actual.getMultiplier();
        assertEquals(e, a);
    }

}
