package cdm.event.instructioncomposition.reset.functions;

import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.event.instructioncomposition.reset.CollectFloatingRateOptionInstruction;
import cdm.observable.asset.FloatingRateIndex;
import cdm.observable.asset.InterestRateIndex;
import cdm.product.asset.FloatingRateSpecification;
import cdm.product.asset.InterestRatePayout;
import cdm.product.asset.RateSpecification;
import com.rosetta.model.lib.records.Date;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Create_CollectFloatingRateOptionInstructionTest extends AbstractFunctionTest {

    @Inject
    private Create_CollectFloatingRateOptionInstruction createCollectFloatingRateOptionInstruction;

    @Test
    @DisplayName("Extracts the floating rate index and trade date from the floating leg")
    void shouldExtractFloatingRateIndexAndTradeDateFromFloatingLeg() {
        InterestRatePayout floatingLeg = buildFloatingLeg();

        CollectFloatingRateOptionInstruction result =
                createCollectFloatingRateOptionInstruction.evaluate(floatingLeg, Date.of(2024, 1, 15));

        assertEquals(FloatingRateIndexEnum.USD_SOFR, result.getFloatingRateIndex(),
                "floatingRateIndex must be extracted from the floating leg");
        assertEquals(Date.of(2024, 1, 15), result.getTradeDate(),
                "tradeDate must be set to the supplied trade date");
    }

    // -------- Helper methods -----------

    private static InterestRatePayout buildFloatingLeg() {
        return InterestRatePayout.builder()
                .setRateSpecification(RateSpecification.builder()
                        .setFloatingRateSpecification(FloatingRateSpecification.builder()
                                .setRateOptionValue(InterestRateIndex.builder()
                                        .setFloatingRateIndex(FloatingRateIndex.builder()
                                                .setFloatingRateIndexValue(FloatingRateIndexEnum.USD_SOFR)
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();
    }
}
