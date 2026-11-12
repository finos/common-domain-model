package cdm.event.instructioncomposition.reset.functions;

import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.event.common.Reset;
import cdm.event.common.ResetInstruction;
import cdm.observable.asset.FloatingRateIndex;
import cdm.observable.asset.InterestRateIndex;
import cdm.observable.asset.Observable;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.metafields.FieldWithMetaInterestRateIndex;
import cdm.observable.event.Observation;
import com.rosetta.model.lib.records.Date;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Create_ResetInstructionTest extends AbstractFunctionTest {

    @Inject
    private Create_ResetInstruction createResetInstruction;

    @Test
    @DisplayName("Builds a ResetInstruction with a single Reset carrying the supplied reset date, priced reset value and one observation")
    void shouldBuildResetInstructionWithResetDateValueAndObservation() {
        BigDecimal resetValue = new BigDecimal("0.05");
        String currency = "USD";
        Date resetDate = Date.of(2024, 3, 20);
        Observable observable = buildObservable();

        ResetInstruction result = createResetInstruction.evaluate(resetValue, currency, resetDate, observable);

        assertEquals(1, result.getReset().size(), "exactly one Reset must be produced");

        Reset reset = result.getReset().get(0);
        assertEquals(resetDate, reset.getResetDate(), "resetDate must be set and match the supplied date");
        assertFinalResetValue(reset.getResetValue(), resetValue, currency);

        assertEquals(1, reset.getObservations().size(), "exactly one Observation must be recorded");
        assertObservation(reset.getObservations().get(0).getValue(), resetValue, currency, resetDate, observable);
    }

    // -------- Builder helpers -----------
    private static Observable buildObservable() {
        return Observable.builder()
                .setIndex(cdm.observable.asset.Index.builder()
                        .setInterestRateIndex(FieldWithMetaInterestRateIndex.builder()
                                .setValue(InterestRateIndex.builder()
                                        .setFloatingRateIndex(FloatingRateIndex.builder()
                                                .setFloatingRateIndexValue(FloatingRateIndexEnum.USD_SOFR)))
                                .setMeta(null)))
                .build();
    }

    // -------- Assertion helpers -----------
    private static void assertFinalResetValue(Price finalResetValue, BigDecimal expectedValue, String expectedCurrency) {
        assertPrice(finalResetValue, expectedValue, expectedCurrency);
    }

    private static void assertObservation(Observation observation, BigDecimal expectedValue, String expectedCurrency, Date expectedObservationDate, Observable expectedObservable) {
        assertPrice(observation.getObservedValue(), expectedValue, expectedCurrency);

        assertEquals(expectedObservationDate, observation.getObservationIdentifier().getObservationDate(),
                "observationIdentifier observationDate must equal the supplied resetDate");
        assertEquals(expectedObservable, observation.getObservationIdentifier().getObservable(),
                "observationIdentifier observable must be passed through from the supplied Observable");
    }

    private static void assertPrice(Price price, BigDecimal expectedValue, String expectedCurrency) {
        assertEquals(0, expectedValue.compareTo(price.getValue()),
                "Price value must equal the supplied value");
        assertEquals(PriceTypeEnum.INTEREST_RATE, price.getPriceType(),
                "Price must be an InterestRate price type");
        assertEquals(expectedCurrency, price.getUnit().getCurrency().getValue(),
                "Price unit currency must equal the supplied currency");
        assertEquals(expectedCurrency, price.getPerUnitOf().getCurrency().getValue(),
                "Price perUnitOf currency must equal the supplied currency");
    }
}
