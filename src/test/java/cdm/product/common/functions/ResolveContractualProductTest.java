package cdm.product.common.functions;

import cdm.legalagreement.contract.Contract;
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
import static org.junit.jupiter.api.Assertions.*;

class ResolveContractualProductTest extends AbstractFunctionTest {

	private static final String PRODUCTS_DIR = "result-json-files/products/";
	private static final String RATES_DIR = PRODUCTS_DIR + "rates/";
	private static final String EQUITY_DIR = PRODUCTS_DIR + "equity/";
	private static final String REPO_DIR = PRODUCTS_DIR + "repo/";
	private static final String CREDIT_DIR = PRODUCTS_DIR + "credit/";

	@Inject
	private ResolveContractualProduct resolveFunc;

	@Test
	void shouldThrowExceptionForMissingQuantityNotation() throws IOException {
		TradeState tradeState = getTradeState(RATES_DIR + "GBP-Vanilla-uti.json");
		ContractualProduct contractualProduct = tradeState.getTrade().getTradableProduct().getProduct().getContractualProduct();

		try {
			resolveFunc.evaluate(contractualProduct, Collections.emptyList());
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

		ContractualProduct resolved = resolveFunc.evaluate(contractualProduct, quantityNotations);

		assertNotNull(resolved);

		ResolvablePayoutQuantity fixedLegQuantity = getPayout(getInterestRatePayouts(resolved), "fixedLeg1").getPayoutQuantity();
		assertEquals(BigDecimal.valueOf(4352000), getResolvedQuantityAmount(fixedLegQuantity));

		ResolvablePayoutQuantity floatingLegQuantity = getPayout(getInterestRatePayouts(resolved), "floatingLeg2").getPayoutQuantity();
		assertEquals(BigDecimal.valueOf(4352000), getResolvedQuantityAmount(floatingLegQuantity));
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

		ContractualProduct resolved = resolveFunc.evaluate(contractualProduct, quantityNotations);

		ResolvablePayoutQuantity fixedLegQuantity = getPayout(getInterestRatePayouts(resolved), 0).getPayoutQuantity();
		assertEquals(BigDecimal.valueOf(25000000), getResolvedQuantityAmount(fixedLegQuantity));

		ResolvablePayoutQuantity floatingLegQuantity = getPayout(getInterestRatePayouts(resolved), 1).getPayoutQuantity();
		assertEquals(BigDecimal.valueOf(25000000), getResolvedQuantityAmount(floatingLegQuantity));
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

		ContractualProduct resolved = resolveFunc.evaluate(contractualProduct, quantityNotations);

		ResolvablePayoutQuantity equityQuantity = getPayout(getEquityPayouts(resolved)).getPayoutQuantity();
		assertEquals(BigDecimal.valueOf(28469376), getResolvedQuantityAmount(equityQuantity));

		ResolvablePayoutQuantity interestRateQuantity = getPayout(getInterestRatePayouts(resolved)).getPayoutQuantity();
		assertEquals(BigDecimal.valueOf(28469376), getResolvedQuantityAmount(interestRateQuantity));
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

		ContractualProduct resolved = resolveFunc.evaluate(contractualProduct, quantityNotations);

		ResolvablePayoutQuantity fixedLegQuantity = getPayout(getInterestRatePayouts(resolved), 0).getPayoutQuantity();
		assertEquals(BigDecimal.valueOf(1000000000L), getResolvedQuantityAmount(fixedLegQuantity));
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

		ContractualProduct resolved = resolveFunc.evaluate(contractualProduct, quantityNotations);

		ResolvablePayoutQuantity fixedLegQuantity = getPayout(getInterestRatePayouts(resolved)).getPayoutQuantity();
		assertEquals(BigDecimal.valueOf(1292748), getResolvedQuantityAmount(fixedLegQuantity));
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

		ContractualProduct resolved = resolveFunc.evaluate(contractualProduct, quantityNotations);

		ResolvablePayoutQuantity optionQuantity = getPayout(getOptionPayouts(resolved)).getPayoutQuantity();
		assertEquals(BigDecimal.valueOf(10000000000L), getResolvedQuantityAmount(optionQuantity));
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

		ContractualProduct resolved = resolveFunc.evaluate(contractualProduct, quantityNotations);

		ResolvablePayoutQuantity optionQuantity = getPayout(getOptionPayouts(resolved)).getPayoutQuantity();
		assertEquals(BigDecimal.valueOf(10000000), getResolvedQuantityAmount(optionQuantity));
	}

	private BigDecimal getResolvedQuantityAmount(ResolvablePayoutQuantity resolvedQuantity) {
		return resolvedQuantity.getResolvedQuantity().getAmount().setScale(0, RoundingMode.HALF_UP);
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

	private List<InterestRatePayout> getInterestRatePayouts(ContractualProduct contractualProduct) {
		return getPayouts(contractualProduct)
				.map(Payout::getInterestRatePayout)
				.orElseThrow(() -> new IllegalArgumentException("ContractualProduct does not contain InterestRatePayout"));
	}

	private List<EquityPayout> getEquityPayouts(ContractualProduct contractualProduct) {
		return getPayouts(contractualProduct)
				.map(Payout::getEquityPayout)
				.orElseThrow(() -> new IllegalArgumentException("ContractualProduct does not contain EquityPayout"));
	}

	private List<OptionPayout> getOptionPayouts(ContractualProduct contractualProduct) {
		return getPayouts(contractualProduct)
				.map(Payout::getOptionPayout)
				.orElseThrow(() -> new IllegalArgumentException("ContractualProduct does not contain OptionPayout"));
	}

	private Optional<Payout> getPayouts(ContractualProduct contractualProduct) {
		return Optional.ofNullable(contractualProduct)
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