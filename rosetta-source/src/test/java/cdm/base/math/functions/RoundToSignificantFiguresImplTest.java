package cdm.base.math.functions;

import cdm.base.math.RoundingDirectionEnum;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoundToSignificantFiguresImplTest {

    @Test
    void shouldRoundToNearest5SignificantFigures() {
        RoundToSignificantFiguresImpl func = new RoundToSignificantFiguresImpl();
        BigDecimal result = func.doEvaluate(BigDecimal.valueOf(1023.123456789), 5, RoundingDirectionEnum.NEAREST);
        assertEquals("1023.1", result.toPlainString());
    }

    @Test
    void shouldRoundUpTo5SignificantFigures() {
        RoundToSignificantFiguresImpl func = new RoundToSignificantFiguresImpl();
        BigDecimal result = func.doEvaluate(BigDecimal.valueOf(1023.123456789), 5, RoundingDirectionEnum.UP);
        assertEquals("1023.2", result.toPlainString());
    }

    @Test
    void shouldRoundDownTo5SignificantFigures() {
        RoundToSignificantFiguresImpl func = new RoundToSignificantFiguresImpl();
        BigDecimal result = func.doEvaluate(BigDecimal.valueOf(1023.123456789), 5, RoundingDirectionEnum.DOWN);
        assertEquals("1023.1", result.toPlainString());
    }

    @Test
    void shouldRoundToNearest1SignificantFigures() {
        RoundToSignificantFiguresImpl func = new RoundToSignificantFiguresImpl();
        BigDecimal result = func.doEvaluate(BigDecimal.valueOf(1023.123456789), 1, RoundingDirectionEnum.NEAREST);
        assertEquals("1000", result.toPlainString());
    }

    @Test
    void shouldRoundToNearest7SignificantFigures() {
        RoundToSignificantFiguresImpl func = new RoundToSignificantFiguresImpl();
        BigDecimal result = func.doEvaluate(BigDecimal.valueOf(1023.1), 7, RoundingDirectionEnum.NEAREST);
        assertEquals("1023.1", result.toPlainString());
    }
}