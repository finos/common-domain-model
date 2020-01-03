package org.isda.cdm.functions;

import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.GlobalKey;
import com.rosetta.model.lib.meta.MetaFieldsI;
import org.isda.cdm.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResolvePayoutQuantityImplTest extends AbstractFunctionTest {

	private static final String PRODUCTS_DIR = "result-json-files/products/";
	private static final String RATES_DIR = PRODUCTS_DIR + "rates/";
	private static final String EQUITY_DIR = PRODUCTS_DIR + "equity/";
	private static final String REPO_DIR = PRODUCTS_DIR + "repo/";

	@Inject
	private ResolvePayoutQuantity resolveFunc;

	@Test
	void shouldResolveQuantityForFixFloatVanilla() throws IOException {
		Contract contract = getContract(RATES_DIR + "GBP-Vanilla-uti.json");

		PayoutBase fixedLegPayout = getPayout(getInterestRatePayouts(contract), "fixedLeg1");
		ResolvablePayoutQuantity resolvedFixedLegQuantity = resolveFunc.evaluate(fixedLegPayout.getPayoutQuantity(), contract);
		assertEquals(BigDecimal.valueOf(4352000), getResolvedQuantity(resolvedFixedLegQuantity));


		PayoutBase floatingLegPayout = getPayout(getInterestRatePayouts(contract), "floatingLeg2");
		ResolvablePayoutQuantity resolvedFloatingLegQuantity = resolveFunc.evaluate(floatingLegPayout.getPayoutQuantity(), contract);
		assertEquals(BigDecimal.valueOf(4352000), getResolvedQuantity(resolvedFloatingLegQuantity));
	}

	@Test
	void shouldResolveQuantityForFra() throws IOException {
		Contract contract = getContract(RATES_DIR + "ird-ex08-fra-no-discounting.json");

		PayoutBase fixedLegPayout = getPayout(getInterestRatePayouts(contract), 0);
		ResolvablePayoutQuantity resolvedFixedLegQuantity = resolveFunc.evaluate(fixedLegPayout.getPayoutQuantity(), contract);
		assertEquals(BigDecimal.valueOf(25000000), getResolvedQuantity(resolvedFixedLegQuantity));


		PayoutBase floatingLegPayout = getPayout(getInterestRatePayouts(contract), 1);
		ResolvablePayoutQuantity resolvedFloatingLegQuantity = resolveFunc.evaluate(floatingLegPayout.getPayoutQuantity(), contract);
		assertEquals(BigDecimal.valueOf(25000000), getResolvedQuantity(resolvedFloatingLegQuantity));
	}

	@Test
	void shouldResolveQuantityForEquitySwap() throws IOException {
		Contract contract = getContract(EQUITY_DIR + "eqs-ex01-single-underlyer-execution-long-form.json");

		PayoutBase equityPayout = getPayout(getEquityPayouts(contract));
		ResolvablePayoutQuantity resolvedEquityQuantity = resolveFunc.evaluate(equityPayout.getPayoutQuantity(), contract);
		assertEquals(BigDecimal.valueOf(28469376), getResolvedQuantity(resolvedEquityQuantity));

		// TODO check with Leo - underlier product identifier in executionQuantity not used
		PayoutBase interestRatePayout = getPayout(getInterestRatePayouts(contract));
		ResolvablePayoutQuantity resolvedInterestRateQuantity = resolveFunc.evaluate(interestRatePayout.getPayoutQuantity(), contract);
		assertEquals(BigDecimal.valueOf(28469376), getResolvedQuantity(resolvedInterestRateQuantity));
	}

	@Disabled
	@Test
	void shouldResolveQuantityForResettingXccySwaps() {
		// TODO ird-ex25-fxnotional-swap-usi-uti.json
	}

	@Test
	void shouldResolveQuantityForRepo() throws IOException {
		Contract contract = getContract(REPO_DIR + "repo-ex01-repo-fixed-rate.json");

		PayoutBase fixedLegPayout = getPayout(getInterestRatePayouts(contract), 0);
		ResolvablePayoutQuantity resolvedFixedLegQuantity = resolveFunc.evaluate(fixedLegPayout.getPayoutQuantity(), contract);
		assertEquals(BigDecimal.valueOf(1292748), getResolvedQuantity(resolvedFixedLegQuantity));
	}

	@Disabled
	@Test
	void shouldResolveQuantityForBrlCdiSwap() {
		// TODO ird-ex33-BRL-CDI-swap-versioned.json
	}

	// TODO index options

	private BigDecimal getResolvedQuantity(ResolvablePayoutQuantity resolvedEquityQuantity) {
		return resolvedEquityQuantity.getQuantitySchedule().getQuantity().getAmount().setScale(0, RoundingMode.HALF_UP);
	}

	private Contract getContract(String resourceName) throws IOException {
		URL url = Resources.getResource(resourceName);
		String json = Resources.toString(url, Charset.defaultCharset());
		return RosettaObjectMapper.getNewRosettaObjectMapper().readValue(json, Contract.class);
	}

	private PayoutBase getPayout(List<? extends PayoutBase> payouts) {
		Iterator<? extends PayoutBase> iterator = payouts.iterator();
		if (!iterator.hasNext())
			new IllegalArgumentException("No Payout found");
		return iterator.next();
	}

	private PayoutBase getPayout(List<? extends PayoutBase> payouts, int index) {
		if (payouts.size() <= index)
			new IllegalArgumentException("No Payout found with index " + index);
		return payouts.get(index);
	}

	private PayoutBase getPayout(List<? extends PayoutBase> payouts, String payoutExternalKey) {
		return payouts.stream()
				.filter(p -> GlobalKey.class.isInstance(p))
				.filter(p -> externalKeyMatches((GlobalKey) p, payoutExternalKey))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No Payout found with external key " + payoutExternalKey));
	}

	private List<InterestRatePayout> getInterestRatePayouts(Contract contract) {
		return getPayouts(contract)
				.map(Payout::getInterestRatePayout)
				.orElseThrow(() -> new IllegalArgumentException("Contract does not contain InterestRatePayout"));
	}

	private List<EquityPayout> getEquityPayouts(Contract contract) {
		return getPayouts(contract)
				.map(Payout::getEquityPayout)
				.orElseThrow(() -> new IllegalArgumentException("Contract does not contain EquityPayout"));
	}

	private Optional<Payout> getPayouts(Contract contract) {
		return Optional.ofNullable(contract)
				.map(Contract::getContractualProduct)
				.map(ContractualProduct::getEconomicTerms)
				.map(EconomicTerms::getPayout);
	}

	private boolean externalKeyMatches(GlobalKey o, String key) {
		return Optional.ofNullable(o)
				.map(GlobalKey::getMeta)
				.map(MetaFieldsI::getExternalKey)
				.map(key::equals)
				.orElse(false);
	}
}