package cdm.product.asset.floatingrate.functions;

import cdm.observable.asset.calculatedrate.CalculationMethodEnum;
import cdm.observable.asset.fro.FloatingRateIndexCalculationMethodEnum;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MapFloatingRateIndexCalculationMethodEnumToCalculationMethodEnumTest extends AbstractFunctionTest {

    @Inject
    private MapFloatingRateIndexCalculationMethodEnumToCalculationMethodEnum func;

    @Test
    void shouldMapOISCompoundToCompounding() {
        assertEquals(CalculationMethodEnum.COMPOUNDING, func.evaluate(FloatingRateIndexCalculationMethodEnum.OIS_COMPOUND));
    }

    @Test
    void shouldMapCompoundedToCompoundedIndex() {
        assertEquals(CalculationMethodEnum.COMPOUNDED_INDEX, func.evaluate(FloatingRateIndexCalculationMethodEnum.COMPOUNDED));
    }

    @ParameterizedTest
    @EnumSource(value = FloatingRateIndexCalculationMethodEnum.class, names = {"OIS_COMPOUND", "COMPOUNDED"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldThrowForUnmappedValues(FloatingRateIndexCalculationMethodEnum value) {
        assertThrows(Exception.class, () -> func.evaluate(value));
    }
}
