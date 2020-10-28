package cdm.product.common.functions;

import cdm.base.math.NonNegativeQuantity;
import cdm.event.common.TradeNew;
import cdm.event.common.TradeState;
import cdm.observable.asset.QuantityNotation;
import cdm.product.asset.InterestRatePayout;
import cdm.product.common.settlement.PayoutBase;
import cdm.product.common.settlement.ResolvablePayoutQuantity;
import cdm.product.template.*;
import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.GlobalKey;
import com.rosetta.model.lib.meta.GlobalKeyFields;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ResolvePayoutQuantityTest extends AbstractFunctionTest {

	private static final String PRODUCTS_DIR = "result-json-files/products/";
	private static final String RATES_DIR = PRODUCTS_DIR + "rates/";
	private static final String EQUITY_DIR = PRODUCTS_DIR + "equity/";
	private static final String REPO_DIR = PRODUCTS_DIR + "repo/";
	private static final String CREDIT_DIR = PRODUCTS_DIR + "credit/";

	@Inject
	private ResolvePayoutQuantity resolveFunc;

	@Test
	void shouldThrowExceptionForEmptyResolvablePayoutQuantity() {
		ResolvablePayoutQuantity resolvablePayoutQuantity = ResolvablePayoutQuantity.builder().build();
		ContractualProduct contractualProduct = ContractualProduct.builder().build();

		try {
			resolveFunc.evaluate(resolvablePayoutQuantity, Collections.emptyList(), contractualProduct);
			fail("Expected exception for empty ResolvablePayoutQuantity");
		} catch (RuntimeException expected) {
			assertEquals("No assetIdentifier nor quantityReference found", expected.getMessage());
		}
	}

	@Test
	void shouldThrowExceptionForMissingQuantityNotation() throws IOException {
		TradeState tradeState = getTradeState(RATES_DIR + "GBP-Vanilla-uti.json");
		ContractualProduct contractualProduct = tradeState.getTrade().getTradableProduct().getProduct().getContractualProduct();
		PayoutBase fixedLegPayout = getPayout(getInterestRatePayouts(tradeState), "fixedLeg1");

		try {
			resolveFunc.evaluate(fixedLegPayout.getPayoutQuantity(), Collections.emptyList(), contractualProduct);
			fail("Expected exception for missing QuantityNotation");
		} catch (RuntimeException expected) {
			assertThat(expected.getMessage(), startsWith("No quantity found for assetIdentifier AssetIdentifier"));
		}
	}

	/**
	 * Each interest rate payout has resolvablePayoutQuantity containing an AssetIdentifier with currency key.
	 * Look up corresponding QuantityNotation based on AssetIdentifier.
	 */
	@Test
	void shouldResolveQuantityForFixFloatVanilla() throws IOException {
		TradeState tradeState = getTradeState(RATES_DIR + "GBP-Vanilla-uti.json");
		TradableProduct tradableProduct = tradeState.getTrade().getTradableProduct();
		List<QuantityNotation> quantityNotations = tradableProduct.getQuantityNotation();
		ContractualProduct contractualProduct = tradableProduct.getProduct().getContractualProduct();

		PayoutBase fixedLegPayout = getPayout(getInterestRatePayouts(tradeState), "fixedLeg1");
		NonNegativeQuantity resolvedFixedLegQuantity = resolveFunc.evaluate(fixedLegPayout.getPayoutQuantity(), quantityNotations, contractualProduct);

		assertEquals(BigDecimal.valueOf(4352000), getResolvedQuantityAmount(resolvedFixedLegQuantity));


		PayoutBase floatingLegPayout = getPayout(getInterestRatePayouts(tradeState), "floatingLeg2");
		NonNegativeQuantity resolvedFloatingLegQuantity = resolveFunc.evaluate(floatingLegPayout.getPayoutQuantity(), quantityNotations, contractualProduct);

		assertEquals(BigDecimal.valueOf(4352000), getResolvedQuantityAmount(resolvedFloatingLegQuantity));
	}

	/**
	 * Each interest rate payout has resolvablePayoutQuantity containing an AssetIdentifier with currency key.
	 * Look up corresponding QuantityNotation based on AssetIdentifier.
	 */
	@Test
	void shouldResolveQuantityForFra() throws IOException {
		TradeState tradeState = getTradeState(RATES_DIR + "ird-ex08-fra-no-discounting.json");
		TradableProduct tradableProduct = tradeState.getTrade().getTradableProduct();
		List<QuantityNotation> quantityNotations = tradableProduct.getQuantityNotation();
		ContractualProduct contractualProduct = tradableProduct.getProduct().getContractualProduct();

		PayoutBase fixedLegPayout = getPayout(getInterestRatePayouts(tradeState), 0);
		NonNegativeQuantity resolvedFixedLegQuantity = resolveFunc.evaluate(fixedLegPayout.getPayoutQuantity(), quantityNotations, contractualProduct);

		assertEquals(BigDecimal.valueOf(25000000), getResolvedQuantityAmount(resolvedFixedLegQuantity));


		PayoutBase floatingLegPayout = getPayout(getInterestRatePayouts(tradeState), 1);
		NonNegativeQuantity resolvedFloatingLegQuantity = resolveFunc.evaluate(floatingLegPayout.getPayoutQuantity(), quantityNotations, contractualProduct);

		assertEquals(BigDecimal.valueOf(25000000), getResolvedQuantityAmount(resolvedFloatingLegQuantity));
	}

	/**
	 * The equity payout contains an AssetIdentifier with currency key.
	 * The interest rate payout contains a reference to equity payout AssetIdentifier with currency key.
	 * Look up corresponding QuantityNotation based on AssetIdentifier.
	 *
	 * The underlier quantity on the equity payout is not resolved as it can be calculated from the equity payout quantity.
	 * E.g.
	 * resolvedPayoutQuantity / equityPayout.priceReturnTerms.initialPrice.netPrice.amount = underlier quantity
	 * 28469376 / 37.44 = 760400
	 */
	@Test
	void shouldResolveQuantityForEquitySwap() throws IOException {
		TradeState tradeState = getTradeState(EQUITY_DIR + "eqs-ex01-single-underlyer-execution-long-form.json");
		TradableProduct tradableProduct = tradeState.getTrade().getTradableProduct();
		List<QuantityNotation> quantityNotations = tradableProduct.getQuantityNotation();
		ContractualProduct contractualProduct = tradableProduct.getProduct().getContractualProduct();

		PayoutBase equityPayout = getPayout(getEquityPayouts(tradeState));
		NonNegativeQuantity resolvedEquityQuantity = resolveFunc.evaluate(equityPayout.getPayoutQuantity(), quantityNotations, contractualProduct);

		assertEquals(BigDecimal.valueOf(28469376), getResolvedQuantityAmount(resolvedEquityQuantity));

		PayoutBase interestRatePayout = getPayout(getInterestRatePayouts(tradeState));
		NonNegativeQuantity resolvedInterestRateQuantity = resolveFunc.evaluate(interestRatePayout.getPayoutQuantity(), quantityNotations, contractualProduct);

		assertEquals(BigDecimal.valueOf(28469376), getResolvedQuantityAmount(resolvedInterestRateQuantity));
	}

	/**
	 * The fixed leg interest rate payout can be resolved (based on AssetIdentifier currency).
	 *
	 * TODO The floating leg interest rate payout requires the FX rate to calculate the quantity.
	 */
	@Test
	void shouldResolveQuantityForResettingXccySwaps() throws IOException {
		TradeState tradeState = getTradeState(RATES_DIR + "ird-ex25-fxnotional-swap-usi-uti.json");
		TradableProduct tradableProduct = tradeState.getTrade().getTradableProduct();
		List<QuantityNotation> quantityNotations = tradableProduct.getQuantityNotation();
		ContractualProduct contractualProduct = tradableProduct.getProduct().getContractualProduct();

		PayoutBase fixedLegPayout = getPayout(getInterestRatePayouts(tradeState), 0);
		NonNegativeQuantity resolvedFixedLegQuantity = resolveFunc.evaluate(fixedLegPayout.getPayoutQuantity(), quantityNotations, contractualProduct);

		assertEquals(BigDecimal.valueOf(1000000000L), getResolvedQuantityAmount(resolvedFixedLegQuantity));
	}

	/**
	 * The interest rate payout has resolvablePayoutQuantity containing an AssetIdentifier with currency key, which is used to look up QuantityNotation.
	 *
	 * TODO The security payout is not mapped yet (synonyms need to be added) so the quantity cannot be resolved.
	 */
	@Test
	void shouldResolveQuantityForRepo() throws IOException {
		TradeState tradeState = getTradeState(REPO_DIR + "repo-ex01-repo-fixed-rate.json");
		TradableProduct tradableProduct = tradeState.getTrade().getTradableProduct();
		List<QuantityNotation> quantityNotations = tradableProduct.getQuantityNotation();
		ContractualProduct contractualProduct = tradableProduct.getProduct().getContractualProduct();

		PayoutBase fixedLegPayout = getPayout(getInterestRatePayouts(tradeState));
		NonNegativeQuantity resolvedFixedLegQuantity = resolveFunc.evaluate(fixedLegPayout.getPayoutQuantity(), quantityNotations, contractualProduct);

		assertEquals(BigDecimal.valueOf(1292748), getResolvedQuantityAmount(resolvedFixedLegQuantity));
	}

	/**
	 * TODO Further synonym mapping work required for BRL CDI Swaps to remove the future value notional from the quantityNotation.
	 *
	 * Cannot be resolved due to multiple quantityNotations with same AssetIdentifier currency but different quantities.
	 */
	@Disabled
	@Test
	void shouldResolveQuantityForBrlCdiSwap() {
		// ird-ex33-BRL-CDI-swap-versioned.json
	}

	/**
	 * The option payout AssetIdentifier contains a currency which is looked up in the quantityNotations.
	 * The option payout underlier is not required for resolving the bond option quantity.
	 */
	@Test
	void shouldResolveQuantityForBondOption() throws IOException {
		TradeState tradeState = getTradeState(RATES_DIR + "bond-option-uti.json");
		TradableProduct tradableProduct = tradeState.getTrade().getTradableProduct();
		List<QuantityNotation> quantityNotations = tradableProduct.getQuantityNotation();
		ContractualProduct contractualProduct = tradableProduct.getProduct().getContractualProduct();

		PayoutBase optionPayout = getPayout(getOptionPayouts(tradeState));
		NonNegativeQuantity resolvedOptionQuantity = resolveFunc.evaluate(optionPayout.getPayoutQuantity(), quantityNotations, contractualProduct);

		assertEquals(BigDecimal.valueOf(10000000000L), getResolvedQuantityAmount(resolvedOptionQuantity));
	}

	/**
	 * The option payout contains a quantity reference to the underlier CDS protection terms.
	 */
	@Test
	void shouldResolveQuantityForCreditSwaption() throws IOException {
		TradeState tradeState = getTradeState(CREDIT_DIR + "cd-swaption-usi.json");
		TradableProduct tradableProduct = tradeState.getTrade().getTradableProduct();
		List<QuantityNotation> quantityNotations = tradableProduct.getQuantityNotation();
		ContractualProduct contractualProduct = tradableProduct.getProduct().getContractualProduct();

		PayoutBase optionPayout = getPayout(getOptionPayouts(tradeState));
		NonNegativeQuantity resolvedOptionQuantity = resolveFunc.evaluate(optionPayout.getPayoutQuantity(), quantityNotations, contractualProduct);

		assertEquals(BigDecimal.valueOf(10000000), getResolvedQuantityAmount(resolvedOptionQuantity));
	}

	private BigDecimal getResolvedQuantityAmount(NonNegativeQuantity resolvedQuantity) {
		return resolvedQuantity.getAmount().setScale(0, RoundingMode.HALF_UP);
	}

	private TradeState getTradeState(String resourceName) throws IOException {
		URL url = Resources.getResource(resourceName);
		String json = Resources.toString(url, Charset.defaultCharset());
		return RosettaObjectMapper.getNewRosettaObjectMapper().readValue(json, TradeState.class);
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

	private List<InterestRatePayout> getInterestRatePayouts(TradeState tradeState) {
		return getPayouts(tradeState)
				.map(Payout::getInterestRatePayout)
				.orElseThrow(() -> new IllegalArgumentException("Contract does not contain InterestRatePayout"));
	}

	private List<EquityPayout> getEquityPayouts(TradeState tradeState) {
		return getPayouts(tradeState)
				.map(Payout::getEquityPayout)
				.orElseThrow(() -> new IllegalArgumentException("Contract does not contain EquityPayout"));
	}

	private List<OptionPayout> getOptionPayouts(TradeState tradeState) {
		return getPayouts(tradeState)
				.map(Payout::getOptionPayout)
				.orElseThrow(() -> new IllegalArgumentException("Contract does not contain OptionPayout"));
	}

	private Optional<Payout> getPayouts(TradeState tradeState) {
		return Optional.ofNullable(tradeState)
				.map(TradeState::getTrade)
				.map(TradeNew::getTradableProduct)
				.map(TradableProduct::getProduct)
				.map(Product::getContractualProduct)
				.map(ContractualProduct::getEconomicTerms)
				.map(EconomicTerms::getPayout);
	}

	private boolean externalKeyMatches(GlobalKey o, String key) {
		return Optional.ofNullable(o)
				.map(GlobalKey::getMeta)
				.map(GlobalKeyFields::getExternalKey)
				.map(key::equals)
				.orElse(false);
	}
}