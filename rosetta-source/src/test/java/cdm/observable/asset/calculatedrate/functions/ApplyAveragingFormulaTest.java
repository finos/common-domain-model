package cdm.observable.asset.calculatedrate.functions;

import cdm.observable.asset.calculatedrate.CalculatedRateDetails;
import javax.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static cdm.observable.asset.calculatedrate.functions.CalculatedRateTestHelper.vector;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplyAveragingFormulaTest extends AbstractFunctionTest {

	@Inject
	private ApplyAveragingFormula func;

	@Test
	void shouldCalculateAverage() {
		double[] weights = GenerateWeightsTest.expectedWeights;
		double[] rates = new double[weights.length];
		for (int i = 0; i < rates.length; i++)
			rates[i] = 0.01 + i * 0.0001;

		double sum = 0.0;
		double totalWeight = 0.0;
		for (int i = 0; i < rates.length; i++)
			sum += rates[i] * weights[i];
		for (int i = 0; i < rates.length; i++)
			totalWeight += weights[i];
		double avg = sum / totalWeight;

		List<BigDecimal> weightVect = vector(weights);
		List<BigDecimal> rateVect = vector(rates);

		CalculatedRateDetails results = func.evaluate(rateVect, weightVect);
		assertEquals(sum, results.getAggregateValue().doubleValue(), 0.000001);
		assertEquals(totalWeight, results.getAggregateWeight().doubleValue(), 0.000001);
		assertEquals(avg, results.getCalculatedRate().doubleValue(), 0.000001);
	}
}
