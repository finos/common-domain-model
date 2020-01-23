package org.isda.cdm.functions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import org.isda.cdm.Contract;
import org.junit.jupiter.api.Test;

import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;

public class EquityPriceQuantityTriangulationTest extends AbstractFunctionTest {

	private static final String EQUITY_DIR = "result-json-files/products/equity/";
	
	@Inject
	private EquityPriceQuantityTriangulation func;
	
	@Test
	void shouldTriangulateEquityPriceNotionalAndNoOfUnitsAndReturnSuccess() throws IOException {
		Contract contract = getContract(EQUITY_DIR + "eqs-ex01-single-underlyer-execution-long-form.json");
		
		boolean success = func.evaluate(contract.getContractualPrice(), contract.getContractualQuantity());
		
		assertTrue(success);
	}
	
	@Test
	void shouldReturnSuccessNotApplicableBecauseNoOfUnitsNotDefined() throws IOException {
		Contract contract = getContract(EQUITY_DIR + "eqs-ex10-short-form-interestLeg-driving-schedule-dates.json");
		
		boolean success = func.evaluate(contract.getContractualPrice(), contract.getContractualQuantity());
		
		assertTrue(success);
	}
	
	private Contract getContract(String resourceName) throws IOException {
		URL url = Resources.getResource(resourceName);
		String json = Resources.toString(url, Charset.defaultCharset());
		return RosettaObjectMapper.getNewRosettaObjectMapper().readValue(json, Contract.class);
	}
}
