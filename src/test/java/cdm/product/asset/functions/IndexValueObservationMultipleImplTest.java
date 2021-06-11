package cdm.product.asset.functions;

import cdm.base.math.Vector;
import cdm.observable.asset.FloatingRateOption;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IndexValueObservationMultipleImplTest extends AbstractFunctionTest {

    @Inject
    private IndexValueObservationMultiple func;

    @Test
    void shouldGetValues() {
        FloatingRateOption fro = IndexValueObservationImplTest.initFro();
        IndexValueObservationImplTest.initIndexData(fro);

        List<Date> dates = List.of(
                DateImpl.of(2021,7,31),
                DateImpl.of(2021,7,30),
                DateImpl.of(2021,8,1),
                DateImpl.of(2021,1,1),
                DateImpl.of(2021,6,1),
                DateImpl.of(2021,7,1));

        List<BigDecimal> expected = List.of(
                            BigDecimal.valueOf(0.033),
                            BigDecimal.valueOf(0.0329),
                            BigDecimal.valueOf(0.01),
                            BigDecimal.valueOf(0.01),
                            BigDecimal.valueOf(0.02),
                            BigDecimal.valueOf(0.03));

        check(expected, func.evaluate(dates, fro));
    }

    private void check(List<BigDecimal> expected, Vector actual) {
        List<? extends BigDecimal> results = actual.getValues();
        assertEquals(expected.size(), results.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), results.get(i));
        }
    }
}
