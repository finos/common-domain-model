package cdm.product.asset.floatingrate.functions;

import cdm.base.math.RateSchedule;
import cdm.base.math.Step;
import cdm.observable.asset.Price;
import cdm.observable.asset.metafields.ReferenceWithMetaPrice;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class LookupRateScheduleAmountTest extends AbstractFunctionTest {

    @Inject
    private LookupRateScheduleAmount func;

    @Test
    void shouldLookupValue() {
        RateSchedule.RateScheduleBuilder builder = RateSchedule.builder();
        ReferenceWithMetaPrice.ReferenceWithMetaPriceBuilder refPriceBuilder = ReferenceWithMetaPrice.builder();
        Price.PriceBuilder priceBuilder = Price.builder();
        priceBuilder.setAmount(BigDecimal.valueOf(0.01));
        refPriceBuilder.setValue(priceBuilder);
        builder.setInitialValue(refPriceBuilder);

        //Price.builder().setAmount(BigDecimal.valueOf(0.01)));
        Step.StepBuilder stepBuilder = Step.builder();

        stepBuilder.setStepDate(DateImpl.of(2021,03,01));
        stepBuilder.setStepValue(BigDecimal.valueOf(0.0101));
        builder.addStep(stepBuilder);

        stepBuilder = Step.builder();
        stepBuilder.setStepDate(DateImpl.of(2021,06,01));
        stepBuilder.setStepValue(BigDecimal.valueOf(0.0102));
        builder.addStep(stepBuilder);

        stepBuilder = Step.builder();
        stepBuilder.setStepDate(DateImpl.of(2021,9,01));
        stepBuilder.setStepValue(BigDecimal.valueOf(0.0103));
        builder.addStep(stepBuilder);

        stepBuilder = Step.builder();
        stepBuilder.setStepDate(DateImpl.of(2021,12,01));
        stepBuilder.setStepValue(BigDecimal.valueOf(0.0104));
        builder.addStep(stepBuilder);
        builder.build();

        check(0.01, func.evaluate(builder, DateImpl.of(2021, 01,01)));
        check(0.0101, func.evaluate(builder, DateImpl.of(2021, 04, 01)));
        check(0.0101, func.evaluate(builder, DateImpl.of(2021, 03,01)));
        check(0.0102, func.evaluate(builder, DateImpl.of(2021, 06,01)));
       check(0.0103, func.evaluate(builder, DateImpl.of(2021, 9,01)));
        check(0.0103, func.evaluate(builder, DateImpl.of(2021, 11,30)));
        check(0.0104, func.evaluate(builder, DateImpl.of(2021, 12,1)));
        check(0.0104, func.evaluate(builder, DateImpl.of(2021, 12,31)));


    }

    void check (double expected, BigDecimal actual) {
        assertEquals(BigDecimal.valueOf(expected), actual);
    }

}
