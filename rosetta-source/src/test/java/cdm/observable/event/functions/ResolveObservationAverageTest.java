package cdm.observable.event.functions;

import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.UnitType;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.event.Observation;
import javax.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResolveObservationAverageTest extends AbstractFunctionTest {

    @Inject
    private ResolveObservationAverage func;

    @Test
    void shouldCalculateAverage() {
        List<Observation> observations = Arrays.asList(
                getObservation(37.0),
                getObservation(37.2),
                getObservation(37.4),
                getObservation(37.6),
                getObservation(37.8));
        Price averagePrice = func.evaluate(observations);

        assertEquals(BigDecimal.valueOf(37.4), averagePrice.getValue());
        assertEquals("USD", averagePrice.getUnit().getCurrency().getValue());
        assertEquals(FinancialUnitEnum.SHARE, averagePrice.getPerUnitOf().getFinancialUnit());
        assertEquals(PriceTypeEnum.ASSET_PRICE, averagePrice.getPriceType());
    }

    private Observation.ObservationBuilder getObservation(double amount) {
        return Observation.builder()
                .setObservedValue(Price.builder()
                        .setValue(BigDecimal.valueOf(amount))
                        .setUnit(UnitType.builder().setCurrencyValue("USD"))
                        .setPerUnitOf(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE))
                       .setPriceType(PriceTypeEnum.ASSET_PRICE));
    }
}