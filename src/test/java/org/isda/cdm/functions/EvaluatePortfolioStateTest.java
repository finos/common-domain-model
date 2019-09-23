package org.isda.cdm.functions;

import org.isda.cdm.*;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.ReferenceWithMetaParty;
import org.isda.cdm.util.SampleExecutionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.isda.cdm.util.SampleExecutionFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EvaluatePortfolioStateTest {

	private static final ZonedDateTime DATE_TIME = ZonedDateTime.of(LocalDateTime.of(2019, 8, 30, 11, 00), ZoneId.of("Europe/London"));

	private EvaluatePortfolioState func;

	@BeforeEach
	void setUp() {
		List<Execution> executions = new SampleExecutionFactory().getExecutions();
		func = new EvaluatePortfolioStateImpl(executions);
	}

	@Test
	void shouldEvaluateTotalPositionForDate() {
		Portfolio input = Portfolio.builder()
								   .setAggregationParameters(AggregationParameters.builder()
																				  .setDateTime(DATE_TIME)
																				  .setTotalPosition(true)
																				  .build())
								   .build();
		PortfolioState portfolioState = func.evaluate(input);

		assertNotNull(portfolioState);
		assertNotNull(portfolioState.getPositions());
		assertEquals(4, portfolioState.getPositions().size());

		Position p1 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.EXECUTED);
		assertNotNull(p1);
		assertEquals(BigDecimal.valueOf(91000000), p1.getQuantity().getAmount());

		Position p2 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.SETTLED);
		assertNotNull(p2);
		assertEquals(BigDecimal.valueOf(138000000), p2.getQuantity().getAmount());

		Position p3 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.EXECUTED);
		assertNotNull(p3);
		assertEquals(BigDecimal.valueOf(-21000000), p3.getQuantity().getAmount());

		Position p4 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.SETTLED);
		assertNotNull(p4);
		assertEquals(BigDecimal.valueOf(410000000), p4.getQuantity().getAmount());
	}

	@Test
	void shouldEvaluateDailyPositionForDate() {
		Portfolio input = Portfolio.builder()
								   .setAggregationParameters(AggregationParameters.builder()
																				  .setDateTime(DATE_TIME)
																				  .setTotalPosition(false)
																				  .build())
								   .build();
		PortfolioState portfolioState = func.evaluate(input);

		assertNotNull(portfolioState);
		assertNotNull(portfolioState.getPositions());
		assertEquals(4, portfolioState.getPositions().size());

		Position p1 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.EXECUTED);
		assertNotNull(p1);
		assertEquals(BigDecimal.valueOf(80000000), p1.getQuantity().getAmount());

		Position p2 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.SETTLED);
		assertNotNull(p2);
		assertEquals(BigDecimal.valueOf(-2000000), p2.getQuantity().getAmount());

		Position p3 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.EXECUTED);
		assertNotNull(p3);
		assertEquals(BigDecimal.valueOf(-7500000), p3.getQuantity().getAmount());

		Position p4 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.SETTLED);
		assertNotNull(p4);
		assertEquals(BigDecimal.valueOf(35000000), p4.getQuantity().getAmount());
	}

	@Test
	void shouldEvaluateTotalPositionForDateAndPositionStatus() {
		Portfolio input = Portfolio.builder()
								   .setAggregationParameters(AggregationParameters.builder()
																				  .setDateTime(DATE_TIME)
																				  .setTotalPosition(true)
																				  .setPositionStatus(PositionStatusEnum.EXECUTED)
																				  .build())
								   .build();
		PortfolioState portfolioState = func.evaluate(input);

		assertNotNull(portfolioState);
		assertNotNull(portfolioState.getPositions());
		assertEquals(2, portfolioState.getPositions().size());

		Position p1 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.EXECUTED);
		assertNotNull(p1);
		assertEquals(BigDecimal.valueOf(91000000), p1.getQuantity().getAmount());

		Position p2 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.EXECUTED);
		assertNotNull(p2);
		assertEquals(BigDecimal.valueOf(-21000000), p2.getQuantity().getAmount());
	}

	@Test
	void shouldEvaluateDailyPositionForDateAndPositionStatus() {
		Portfolio input = Portfolio.builder()
								   .setAggregationParameters(AggregationParameters.builder()
																				  .setDateTime(DATE_TIME)
																				  .setTotalPosition(false)
																				  .setPositionStatus(PositionStatusEnum.EXECUTED)
																				  .build())
								   .build();
		PortfolioState portfolioState = func.evaluate(input);

		assertNotNull(portfolioState);
		assertNotNull(portfolioState.getPositions());
		assertEquals(2, portfolioState.getPositions().size());

		Position p1 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.EXECUTED);
		assertNotNull(p1);
		assertEquals(BigDecimal.valueOf(80000000), p1.getQuantity().getAmount());

		Position p2 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.EXECUTED);
		assertNotNull(p2);
		assertEquals(BigDecimal.valueOf(-7500000), p2.getQuantity().getAmount());
	}

	@Test
	void shouldEvaluateTotalPositionForDateAndProduct() {
		Portfolio input = Portfolio.builder()
								   .setAggregationParameters(AggregationParameters.builder()
																				  .setDateTime(DATE_TIME)
																				  .setTotalPosition(true)
																				  .addProduct(getProduct(CUSIP_US1234567891, ProductIdSourceEnum.CUSIP))
																				  .build())
								   .build();
		PortfolioState portfolioState = func.evaluate(input);

		assertNotNull(portfolioState);
		assertNotNull(portfolioState.getPositions());
		assertEquals(2, portfolioState.getPositions().size());

		Position p1 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.EXECUTED);
		assertNotNull(p1);
		assertEquals(BigDecimal.valueOf(91000000), p1.getQuantity().getAmount());

		Position p2 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.SETTLED);
		assertNotNull(p2);
		assertEquals(BigDecimal.valueOf(138000000), p2.getQuantity().getAmount());
	}

	@Test
	void shouldEvaluateDailyPositionForDateAndProduct() {
		Portfolio input = Portfolio.builder()
								   .setAggregationParameters(AggregationParameters.builder()
																				  .setDateTime(DATE_TIME)
																				  .setTotalPosition(false)
																				  .addProduct(getProduct(CUSIP_US1234567891, ProductIdSourceEnum.CUSIP))
																				  .build())
								   .build();
		PortfolioState portfolioState = func.evaluate(input);

		assertNotNull(portfolioState);
		assertNotNull(portfolioState.getPositions());
		assertEquals(2, portfolioState.getPositions().size());

		Position p1 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.EXECUTED);
		assertNotNull(p1);
		assertEquals(BigDecimal.valueOf(80000000), p1.getQuantity().getAmount());

		Position p2 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.SETTLED);
		assertNotNull(p2);
		assertEquals(BigDecimal.valueOf(-2000000), p2.getQuantity().getAmount());
	}

	private Product getProduct(String productId, ProductIdSourceEnum source) {
		return Product.builder().setSecurityBuilder(Security.builder()
					.setBondBuilder(Bond.builder()
						.setProductIdentifierBuilder(ProductIdentifier.builder()
							.addIdentifier(FieldWithMetaString.builder().setValue(productId).build())
							.setSource(source))))
					.build();
	}

	@Test
	void shouldEvaluateTotalPositionForParty() {
		Portfolio input = Portfolio.builder()
								   .setAggregationParameters(AggregationParameters.builder()
																				  .setDateTime(DATE_TIME)
																				  .setTotalPosition(true)
																				  .addParty(toReferenceWithMetaParty(CLIENT_A_NAME))
																				  .build())
								   .build();
		PortfolioState portfolioState = func.evaluate(input);

		assertNotNull(portfolioState);
		assertNotNull(portfolioState.getPositions());
		assertEquals(4, portfolioState.getPositions().size());

		Position p1 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.EXECUTED);
		assertNotNull(p1);
		assertEquals(BigDecimal.valueOf(80000000), p1.getQuantity().getAmount());

		Position p2 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.SETTLED);
		assertNotNull(p2);
		assertEquals(BigDecimal.valueOf(140000000), p2.getQuantity().getAmount());

		Position p3 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.EXECUTED);
		assertNotNull(p3);
		assertEquals(BigDecimal.valueOf(-13500000), p3.getQuantity().getAmount());

		Position p4 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.SETTLED);
		assertNotNull(p4);
		assertEquals(BigDecimal.valueOf(125000000), p4.getQuantity().getAmount());
	}

	private Position getPosition(PortfolioState portfolioState, String productId, PositionStatusEnum positionStatus) {
		return portfolioState.getPositions()
							 .stream()
							 .filter(p -> isProductId(p, productId) && p.getPositionStatus() == positionStatus)
							 .findFirst()
							 .orElse(null);
	}

	private boolean isProductId(Position p, String productId) {
		return p.getProduct().getSecurity().getBond().getProductIdentifier().getIdentifier().stream()
				.map(FieldWithMetaString::getValue)
				.anyMatch(id -> id.equals(productId));
	}

	private ReferenceWithMetaParty toReferenceWithMetaParty(String partyId) {
		return ReferenceWithMetaParty.builder()
									 .setValue(Party.builder()
													.addPartyId(FieldWithMetaString.builder().setValue(partyId).build())
													.build())
									 .build();
	}
}
