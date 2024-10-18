package cdm.base.math.functions;

import cdm.base.math.RoundingDirectionEnum;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RoundToPrecisionImplTest {

    @Test
    void shouldRoundToNearest5DecimalPlaces() {
        RoundToPrecisionImpl func = new RoundToPrecisionImpl();
        BigDecimal result = func.doEvaluate(BigDecimal.valueOf(1023.123456789), 5, RoundingDirectionEnum.NEAREST, false);
        assertEquals("1023.12346", result.toPlainString());
    }

    @Test
    void shouldRoundUpTo5DecimalPlaces() {
        RoundToPrecisionImpl func = new RoundToPrecisionImpl();
        BigDecimal result = func.doEvaluate(BigDecimal.valueOf(1023.123456789), 5, RoundingDirectionEnum.UP,false);
        assertEquals("1023.12346", result.toPlainString());
    }

    @Test
    void shouldRoundDownTo5DecimalPlaces() {
        RoundToPrecisionImpl func = new RoundToPrecisionImpl();
        BigDecimal result = func.doEvaluate(BigDecimal.valueOf(1023.123456789), 5, RoundingDirectionEnum.DOWN,false);
        assertEquals("1023.12345", result.toPlainString());
    }

    @Test
    void shouldRoundToNearest0DecimalPlaces() {
        RoundToPrecisionImpl func = new RoundToPrecisionImpl();
        BigDecimal result = func.doEvaluate(BigDecimal.valueOf(1023.123456789), 0, RoundingDirectionEnum.NEAREST,false);
        assertEquals("1023", result.toPlainString());
    }

    @Test
    void shouldRoundToNearest7DecimalPlaces() {
        RoundToPrecisionImpl func = new RoundToPrecisionImpl();
        BigDecimal result = func.doEvaluate(BigDecimal.valueOf(1023.1), 7, RoundingDirectionEnum.NEAREST,false);
        assertEquals("1023.1000000", result.toPlainString());
    }

    @Test
    void shouldRoundDownTo5DecimalPlacesWithTrailingZero() {
        RoundToPrecisionImpl func = new RoundToPrecisionImpl();
        BigDecimal result = func.doEvaluate(BigDecimal.valueOf(1023), 5, RoundingDirectionEnum.NEAREST,false);
        assertEquals("1023.00000", result.toPlainString());
    }

    @Test
    void shouldRoundDownTo4DecimalPlacesWithTrailingZeros() {
        RoundToPrecisionImpl func = new RoundToPrecisionImpl();
        BigDecimal result = func.doEvaluate(BigDecimal.valueOf(999999999), 4, RoundingDirectionEnum.NEAREST,false);
        assertEquals("999999999.0000", result.toPlainString());
    }

    @Test
    void shouldRoundDownTo5DecimalPlacesRemoveTrailingZero() {
        RoundToPrecisionImpl func = new RoundToPrecisionImpl();
        BigDecimal result = func.doEvaluate(BigDecimal.valueOf(1023), 5, RoundingDirectionEnum.NEAREST,true);
        assertEquals("1023", result.toPlainString());
    }

    @Test
    void shouldRoundDownTo4DecimalPlacesRemovingTrailingZeros() {
        RoundToPrecisionImpl func = new RoundToPrecisionImpl();
        BigDecimal result = func.doEvaluate(BigDecimal.valueOf(999999999), 4, RoundingDirectionEnum.NEAREST,true);
        assertEquals("999999999", result.toPlainString());
    }
}