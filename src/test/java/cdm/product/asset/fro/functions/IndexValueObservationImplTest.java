package cdm.product.asset.fro.functions;

import cdm.base.datetime.Period;
import cdm.base.datetime.PeriodEnum;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.base.staticdata.asset.rates.metafields.FieldWithMetaFloatingRateIndexEnum;
import cdm.observable.asset.FloatingRateOption;
import cdm.product.asset.fro.functions.IndexValueObservationImpl;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

public class IndexValueObservationImplTest extends AbstractFunctionTest {

    @Inject
    private IndexValueObservation func;

    @Test
    void shouldGetValue() {
        FloatingRateOption fro = initFro();

        initIndexData(fro);

        assertEquals(BigDecimal.valueOf(0.033), func.evaluate(DateImpl.of(2021,7,31),fro));
        assertEquals(BigDecimal.valueOf(0.0329), func.evaluate(DateImpl.of(2021,7,30),fro));
        assertEquals(BigDecimal.valueOf(0.01), func.evaluate(DateImpl.of(2021,8,1),fro));
        assertEquals(BigDecimal.valueOf(0.01), func.evaluate(DateImpl.of(2021,1,1),fro));
        assertEquals(BigDecimal.valueOf(0.02), func.evaluate(DateImpl.of(2021,6,1),fro));
        assertEquals(BigDecimal.valueOf(0.03), func.evaluate(DateImpl.of(2021,7,1),fro));

    }

    public static void initIndexData(FloatingRateOption fro) {
        IndexValueObservationImpl ivo = IndexValueObservationImpl.getInstance();
        ivo.setDefaultValue(0.01);
        ivo.setValue(fro, DateImpl.of(2021,6,1), 0.02);
        ivo.setValues(fro, DateImpl.of(2021,7,1), 31,0.03, 0.0001);
    }

    public static FloatingRateOption initFro() {
        return FloatingRateOption.builder()
                .setFloatingRateIndex(FieldWithMetaFloatingRateIndexEnum.builder()
                        .setValue(FloatingRateIndexEnum.EUR_EURIBOR_ACT_365)
                        .build())
                .setIndexTenor(Period.builder()
                        .setPeriod(PeriodEnum.M)
                        .setPeriodMultiplier(3).build())
                .build();
    }
}
