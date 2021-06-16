package cdm.product.asset.functions;

import cdm.base.math.Vector;
import cdm.product.asset.CalculatedRateDetails;
import com.google.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplyCompoundingFormulaTest  extends AbstractFunctionTest {

    @Inject
    private ApplyCompoundingFormula func;

    @Test
    void shouldCalculateCompound() {

        double[] weights = GenerateWeightsTest.expectedWeights;
        double[] rates = new double[weights.length];
        for(int i = 0 ; i < rates.length; i++) rates[i] = 0.01 + i * 0.0001;

        double dcb = 1.0 / 365.0;
        double aggregate = 1.0;
        double totalWeight = 0.0;
        double[] factors = new double[weights.length];
        for(int i = 0 ; i < rates.length; i++) factors[i] = 1.0 + (rates[i] * dcb * weights[i]);
        for(int i = 0 ; i < rates.length; i++) aggregate *= factors[i];
        for(int i = 0 ; i < rates.length; i++) totalWeight +=  weights[i];
        double avg = (aggregate - 1.0) / totalWeight / dcb;

        Vector weightVect = vector(weights);
        Vector rateVect = vector(rates);

        CalculatedRateDetails results = func.evaluate(rateVect, weightVect, BigDecimal.valueOf(dcb));
        List<? extends BigDecimal> resultFactors = results.getGrowthFactor().getValues();
        check(factors, resultFactors);
        assertEquals(aggregate, results.getAggregateValue().doubleValue(), 0.000001);
        assertEquals(totalWeight, results.getAggregateWeight().doubleValue(), 0.000001);
        assertEquals(avg, results.getCalculatedRate().doubleValue(), 0.000001);
    }

    void check (double[] expected, List<? extends BigDecimal> actual) {
        for (int i = 0; i < expected.length && i < actual.size(); i++)
            assertEquals(expected[i], actual.get(i).doubleValue(), 0.000001);
        assertEquals(expected.length, actual.size());
    }

    Vector vector(double[] values) {
        List<BigDecimal> valueList = new ArrayList<>(values.length);
        for (double value : values) valueList.add(BigDecimal.valueOf(value));
        return Vector.builder().setValues(valueList).build();
    }
}
