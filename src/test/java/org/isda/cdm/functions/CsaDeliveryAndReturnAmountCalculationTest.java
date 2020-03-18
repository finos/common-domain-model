package org.isda.cdm.functions;

import com.google.inject.Inject;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import org.isda.cdm.CollateralRounding;
import org.isda.cdm.MarginApproachEnum;
import org.isda.cdm.Money;
import org.isda.cdm.PostedCreditSupportItem;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsaDeliveryAndReturnAmountCalculationTest extends AbstractFunctionTest {

	@Inject private DeliveryAmount deliveryAmountCalc;
	@Inject private ReturnAmount returnAmountCalc;

	private static final Logger LOGGER = LoggerFactory.getLogger(CsaDeliveryAndReturnAmountCalculationTest.class);
	private static final String BASE_CURRENCY_USD = "EUR";
	private static final String CURRENCY_SCHEME = "http://www.fpml.org/coding-scheme/external/iso4217";
	private static final BigDecimal ROUND_TO_NEAREST = BigDecimal.valueOf(0.5);
	private static final double EPSILON = 1e-10;

	@Test
	void testMarginCallSpreadsheetExample() {
		// Day 1 - Do first trade with 10 of exposure
		assertDeliveryAndReturnAmount("Day 1", 0, 0, 0, 0, 0, 0);
		// Day 2 - Calculate new portfolio including yesterday's trading
		assertDeliveryAndReturnAmount("Day 2", 10, 0, 0, 0, 10, 0);
		// Day 3 - Do offsetting trade
		assertDeliveryAndReturnAmount("Day 3", 10, 0, 10, 0, 0, 0);
		// Day 4 - Day 2 delivery amount settles
		assertDeliveryAndReturnAmount("Day 4", 0, 10, 0, 0, 0, 10);
		// Day 5
		assertDeliveryAndReturnAmount("Day 5", 0, 10, 0, 10, 0, 0);
		// Day 6 - Day 4 return amount settles. New trade of 20.
		assertDeliveryAndReturnAmount("Day 6", 0, 0, 0, 0, 0, 0);
		// Day 7
		assertDeliveryAndReturnAmount("Day 7", 20, 0, 0, 0, 20, 0);
		// Day 8 - Take off 15 of risk
		assertDeliveryAndReturnAmount("Day 8", 20, 0, 20, 0, 0, 0);
		// Day 9 - Yesterday's risk reduction gives 15 return amount
		assertDeliveryAndReturnAmount("Day 9", 5, 20, 0, 0, 0, 15);
		// Day 10 - Collateral unit price collapses 90%.
		// Yesterday's unsettled return amount is greater than current collateral value, giving a negative adjusted value.
		// However the Return Amount calc is still zero.
		assertDeliveryAndReturnAmount("Day 10", 15, 2, 0, 1.5, 14.5, 0);
		// Day 11
		assertDeliveryAndReturnAmount("Day 11", 15, 0.5, 14.5, 0, 0, 0);
		// Day 12 - Risk reduced to zero
		assertDeliveryAndReturnAmount("Day 12", 15, 15, 0, 0, 0, 0);
		// Day 13 - Return amount of 15
		assertDeliveryAndReturnAmount("Day 13", 0, 15, 0, 0, 0, 15);
		// Day 14 - Collateral units recover to previous price before return amount settles.
		// Yesterday's comparatively small return amount is compared to today's large collateral value, generating additional return amount in excess
		// of current collateral balance.
		assertDeliveryAndReturnAmount("Day 14", 0, 150, 0, 150, 0, 0);
		// Day 15
		assertDeliveryAndReturnAmount("Day 15", 0, 0, 0, 0, 0, 0);
		// Day 16
		assertDeliveryAndReturnAmount("Day 16", 0, 0, 0, 0, 0, 0);
		// Day 17
		assertDeliveryAndReturnAmount("Day 17", 0, 0, 0, 0, 0, 0);
		// Day 18
		assertDeliveryAndReturnAmount("Day 18", 0, 0, 0, 0, 0, 0);
	}

	/**
	 * @param message - assert message
	 * @param marginAmount - column D
	 * @param postedCreditSupportAmount - column H
	 * @param priorDeliveryAmountAdjustment - column R
	 * @param priorReturnAmountAdjustment - column S
	 * @param expectedDeliveryAmount - column J
	 * @param expectedReturnAmount - column K
	 */
	private void assertDeliveryAndReturnAmount(
			String message,
			double marginAmount,
			double postedCreditSupportAmount,
			double priorDeliveryAmountAdjustment,
			double priorReturnAmountAdjustment,
			double expectedDeliveryAmount,
			double expectedReturnAmount) {
		LOGGER.debug(message);
		assertEquals(expectedDeliveryAmount,
				deliveryAmount(marginAmount, postedCreditSupportAmount, priorDeliveryAmountAdjustment, priorReturnAmountAdjustment), EPSILON, message + " deliveryAmount");
		assertEquals(expectedReturnAmount,
				returnAmount(marginAmount, postedCreditSupportAmount, priorDeliveryAmountAdjustment, priorReturnAmountAdjustment), EPSILON, message + " returnAmount");
	}

	private double deliveryAmount(double marginAmount, double postedCreditSupportAmount, double priorDeliveryAmountAdjustment, double priorReturnAmountAdjustment) {
		List<PostedCreditSupportItem> postedCreditSupportItems = Collections.singletonList(PostedCreditSupportItem.builder()
				.setCashOrSecurityValue(getMoney(postedCreditSupportAmount))
				.setValuationPercentage(BigDecimal.valueOf(100))
				.setFxHaircutPercentage(BigDecimal.valueOf(0))
				.setDisputedCashOrSecurityValue(getMoney(0)) // add null check
				.build());
		double disputedPostedCreditSupportAmount = 0;
		double threshold = 0;
		double minimumTransferAmount = 0;
		CollateralRounding rounding = CollateralRounding.builder()
				.setDeliveryAmount(ROUND_TO_NEAREST)
				.setReturnAmount(ROUND_TO_NEAREST)
				.build();
		double disputedDeliveryAmount = 0;
		Money deliveryAmountMoney = deliveryAmountCalc.evaluate(postedCreditSupportItems,
				getMoney(priorDeliveryAmountAdjustment),
				getMoney(priorReturnAmountAdjustment),
				getMoney(disputedPostedCreditSupportAmount),
				getMoney(marginAmount),
				getMoney(threshold),
				MarginApproachEnum.ALLOCATED,
				null, // absent if MarginApproachEnum == ALLOCATED
				getMoney(minimumTransferAmount),
				rounding,
				getMoney(disputedDeliveryAmount),
				BASE_CURRENCY_USD);
		return deliveryAmountMoney.getAmount().doubleValue();
	}

	private double returnAmount(double marginAmount, double postedCreditSupportAmount, double priorDeliveryAmountAdjustment, double priorReturnAmountAdjustment) {
		List<PostedCreditSupportItem> postedCreditSupportItems = Collections.singletonList(PostedCreditSupportItem.builder()
				.setCashOrSecurityValue(getMoney(postedCreditSupportAmount))
				.setValuationPercentage(BigDecimal.valueOf(100))
				.setFxHaircutPercentage(BigDecimal.valueOf(0))
				.setDisputedCashOrSecurityValue(getMoney(0)) // add null check
				.build());
		double disputedPostedCreditSupportAmount = 0;
		double threshold = 0;
		double minimumTransferAmount = 0;
		CollateralRounding rounding = CollateralRounding.builder()
				.setDeliveryAmount(ROUND_TO_NEAREST)
				.setReturnAmount(ROUND_TO_NEAREST)
				.build();
		double disputedReturnAmount = 0;
		Money returnAmountMoney = returnAmountCalc.evaluate(postedCreditSupportItems,
				getMoney(priorDeliveryAmountAdjustment),
				getMoney(priorReturnAmountAdjustment),
				getMoney(disputedPostedCreditSupportAmount),
				getMoney(marginAmount),
				getMoney(threshold),
				MarginApproachEnum.ALLOCATED,
				null, // absent if MarginApproachEnum == ALLOCATED
				getMoney(minimumTransferAmount),
				rounding,
				getMoney(disputedReturnAmount),
				BASE_CURRENCY_USD);
		return returnAmountMoney.getAmount().doubleValue();
	}

	private Money getMoney(double amount) {
		return Money.builder()
				.setAmount(BigDecimal.valueOf(amount))
				.setCurrency(FieldWithMetaString.builder()
						.setValue(BASE_CURRENCY_USD)
						.setMetaBuilder(MetaFields.builder()
								.setScheme(CURRENCY_SCHEME))
						.build())
				.build();
	}
}
