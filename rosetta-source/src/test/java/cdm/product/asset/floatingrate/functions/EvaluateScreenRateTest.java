package cdm.product.asset.floatingrate.functions;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.observable.asset.FloatingRateIndex;
import cdm.observable.asset.fro.functions.IndexValueObservation;
import cdm.product.asset.FloatingRate;
import cdm.product.asset.floatingrate.FloatingRateSettingDetails;
import cdm.product.common.schedule.CalculationPeriodBase;
import cdm.product.common.schedule.ResetDates;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static cdm.observable.asset.calculatedrate.functions.CalculatedRateTestHelper.period;
import static cdm.product.asset.floatingrate.functions.FloatingRateTestHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EvaluateScreenRateTest extends AbstractFunctionTest {

    @Inject
    private EvaluateScreenRate func;

    @Override
    protected void bindTestingMocks(Binder binder) {
        binder.bind(IndexValueObservation.class).toInstance(initIndexData(initFro()));
    }

    @Test
    void shouldEvaluateRate() {
        FloatingRateIndex fro = initFro();
        FloatingRate rate = initFloatingRate(fro);
        ResetDates resetDates = initResetDates(BusinessCenterEnum.GBLO, 3, 2, false);

        CalculationPeriodBase dec2020 = period(Date.of(2020, 12, 10), Date.of(2021, 3, 10));
        FloatingRateSettingDetails result = func.evaluate(rate, resetDates, dec2020);
        check(result, 0.01, Date.of(2021, 3, 8));
    }

    private void check(FloatingRateSettingDetails result, double finalRate, Date fixingDate) {
        assertEquals(BigDecimal.valueOf(finalRate), result.getFloatingRate());
        assertEquals(fixingDate, result.getObservationDate());
    }
}
