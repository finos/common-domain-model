package org.isda.cdm.functions;

import org.isda.cdm.*;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.ReferenceWithMetaParty;
import org.isda.cdm.util.TestObjectsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.isda.cdm.util.TestObjectsFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EvaluatePortfolioStateTest {

	private static final ZonedDateTime DATE_TIME = ZonedDateTime.of(LocalDateTime.of(2019, 8, 30, 11, 00), ZoneId.of("Europe/London"));

	private EvaluatePortfolioState func;

	@BeforeEach
	void setUp() {
		func = new EvaluatePortfolioStateImpl(getExecutions());
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
		assertEquals(BigDecimal.valueOf(-8648032500.0).setScale(2), p1.getCashBalance().getAmount().setScale(2));

		Position p2 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.SETTLED);
		assertNotNull(p2);
		assertEquals(BigDecimal.valueOf(138000000), p2.getQuantity().getAmount());
		assertEquals(BigDecimal.valueOf(-13123950000.0).setScale(2), p2.getCashBalance().getAmount().setScale(2));

		Position p3 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.EXECUTED);
		assertNotNull(p3);
		assertEquals(BigDecimal.valueOf(-21000000), p3.getQuantity().getAmount());
		assertEquals(BigDecimal.valueOf(1997001750.0).setScale(2), p3.getCashBalance().getAmount().setScale(2));

		Position p4 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.SETTLED);
		assertNotNull(p4);
		assertEquals(BigDecimal.valueOf(410000000), p4.getQuantity().getAmount());
		assertEquals(BigDecimal.valueOf(-38982750000.0).setScale(2), p4.getCashBalance().getAmount().setScale(2));
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
		assertEquals(BigDecimal.valueOf(-7602400000.0).setScale(2), p1.getCashBalance().getAmount().setScale(2));

		Position p2 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.SETTLED);
		assertNotNull(p2);
		assertEquals(BigDecimal.valueOf(-2000000), p2.getQuantity().getAmount());
		assertEquals(BigDecimal.valueOf(190100000.0).setScale(2), p2.getCashBalance().getAmount().setScale(2));

		Position p3 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.EXECUTED);
		assertNotNull(p3);
		assertEquals(BigDecimal.valueOf(-7500000), p3.getQuantity().getAmount());
		assertEquals(BigDecimal.valueOf(713212500.0).setScale(2), p3.getCashBalance().getAmount().setScale(2));

		Position p4 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.SETTLED);
		assertNotNull(p4);
		assertEquals(BigDecimal.valueOf(35000000), p4.getQuantity().getAmount());
		assertEquals(BigDecimal.valueOf(-3325875000.0).setScale(2), p4.getCashBalance().getAmount().setScale(2));
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
		assertEquals(BigDecimal.valueOf(-8648032500.0).setScale(2), p1.getCashBalance().getAmount().setScale(2));

		Position p2 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.EXECUTED);
		assertNotNull(p2);
		assertEquals(BigDecimal.valueOf(-21000000), p2.getQuantity().getAmount());
		assertEquals(BigDecimal.valueOf(1997001750.00).setScale(2), p2.getCashBalance().getAmount().setScale(2));

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
		assertEquals(BigDecimal.valueOf(-7602400000.0).setScale(2), p1.getCashBalance().getAmount().setScale(2));

		Position p2 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.EXECUTED);
		assertNotNull(p2);
		assertEquals(BigDecimal.valueOf(-7500000), p2.getQuantity().getAmount());
		assertEquals(BigDecimal.valueOf(713212500.0).setScale(2), p2.getCashBalance().getAmount().setScale(2));
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
		assertEquals(BigDecimal.valueOf(-8648032500.0).setScale(2), p1.getCashBalance().getAmount().setScale(2));

		Position p2 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.SETTLED);
		assertNotNull(p2);
		assertEquals(BigDecimal.valueOf(138000000), p2.getQuantity().getAmount());
		assertEquals(BigDecimal.valueOf(-13123950000.0).setScale(2), p2.getCashBalance().getAmount().setScale(2));
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
		assertEquals(BigDecimal.valueOf(-7602400000.0).setScale(2), p1.getCashBalance().getAmount().setScale(2));

		Position p2 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.SETTLED);
		assertNotNull(p2);
		assertEquals(BigDecimal.valueOf(-2000000), p2.getQuantity().getAmount());
		assertEquals(BigDecimal.valueOf(190100000.0).setScale(2), p2.getCashBalance().getAmount().setScale(2));
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
						.addParty(toReferenceWithMetaParty(COUNTERPARTY_BROKER_A_NAME))
						.build())
				.build();
		PortfolioState portfolioState = func.evaluate(input);

		assertNotNull(portfolioState);
		assertNotNull(portfolioState.getPositions());
		assertEquals(4, portfolioState.getPositions().size());

		Position p1 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.EXECUTED);
		assertNotNull(p1);
		assertEquals(BigDecimal.valueOf(80000000), p1.getQuantity().getAmount());
		assertEquals(BigDecimal.valueOf(-7602400000.0).setScale(2), p1.getCashBalance().getAmount().setScale(2));

		Position p2 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.SETTLED);
		assertNotNull(p2);
		assertEquals(BigDecimal.valueOf(140000000), p2.getQuantity().getAmount());
		assertEquals(BigDecimal.valueOf(-13314050000.0).setScale(2), p2.getCashBalance().getAmount().setScale(2));

		Position p3 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.EXECUTED);
		assertNotNull(p3);
		assertEquals(BigDecimal.valueOf(-13500000), p3.getQuantity().getAmount());
		assertEquals(BigDecimal.valueOf(1283789250.0).setScale(2), p3.getCashBalance().getAmount().setScale(2));

		Position p4 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.SETTLED);
		assertNotNull(p4);
		assertEquals(BigDecimal.valueOf(125000000), p4.getQuantity().getAmount());
		assertEquals(BigDecimal.valueOf(-11883125000.0).setScale(2), p4.getCashBalance().getAmount().setScale(2));
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

	private List<Execution> getExecutions() {
		TestObjectsFactory factory = new TestObjectsFactory();
		return Arrays.asList(
				factory.getExecution(1, LocalDate.of(2019, 8, 26), CUSIP_US1234567891,
						factory.getQuantity(150000000), factory.getPrice(95.0975, 94.785, CURRENCY_USD),
						LocalDate.of(2019, 8, 28), true,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME,  null),
						factory.getParty(COUNTERPARTY_BROKER_A_ID, COUNTERPARTY_BROKER_A_NAME,  null)
				),
				factory.getExecution(2, LocalDate.of(2019, 8, 26), CUSIP_DH9105730505,
						factory.getQuantity(250000000), factory.getPrice(95.095, 94.78, CURRENCY_USD),
						LocalDate.of(2019, 8, 28), true,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME,  null),
						factory.getParty(COUNTERPARTY_BROKER_B_ID, COUNTERPARTY_BROKER_B_NAME,  null)
				),
				factory.getExecution(3, LocalDate.of(2019, 8, 27), CUSIP_US1234567891,
						factory.getQuantity(10000000), factory.getPrice(95.0575, 94.77, CURRENCY_USD),
						LocalDate.of(2019, 8, 29), false,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME,  null),
						factory.getParty(COUNTERPARTY_BROKER_A_ID, COUNTERPARTY_BROKER_A_NAME,  null)
				),
				factory.getExecution(4, LocalDate.of(2019, 8, 27), CUSIP_DH9105730505,
						factory.getQuantity(125000000), factory.getPrice(95.065, 94.73, CURRENCY_USD),
						LocalDate.of(2019, 8, 29), true,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME,  null),
						factory.getParty(COUNTERPARTY_BROKER_A_ID, COUNTERPARTY_BROKER_A_NAME,  null)
				),
				factory.getExecution(5, LocalDate.of(2019, 8, 28), CUSIP_US1234567891,
						factory.getQuantity(2000000), factory.getPrice(95.05, 94.79, CURRENCY_USD),
						LocalDate.of(2019, 8, 30), false,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME,  null),
						factory.getParty(COUNTERPARTY_BROKER_B_ID, COUNTERPARTY_BROKER_B_NAME,  null)
				),
				factory.getExecution(6, LocalDate.of(2019, 8, 28), CUSIP_DH9105730505,
						factory.getQuantity(35000000), factory.getPrice(95.025, 94.65, CURRENCY_USD),
						LocalDate.of(2019, 8, 30), true,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME,  null),
						factory.getParty(COUNTERPARTY_BROKER_B_ID, COUNTERPARTY_BROKER_B_NAME,  null)
				),
				factory.getExecution(7, LocalDate.of(2019, 8, 29), CUSIP_US1234567891,
						factory.getQuantity(11000000), factory.getPrice(95.0575, 94.63, CURRENCY_USD),
						LocalDate.of(2019, 9, 2), true,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME,  null),
						factory.getParty(COUNTERPARTY_BROKER_B_ID, COUNTERPARTY_BROKER_B_NAME,  null)
				),
				factory.getExecution(8, LocalDate.of(2019, 8, 29), CUSIP_DH9105730505,
						factory.getQuantity(13500000), factory.getPrice(95.0955, 94.685, CURRENCY_USD),
						LocalDate.of(2019, 9, 2), false,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME,  null),
						factory.getParty(COUNTERPARTY_BROKER_A_ID, COUNTERPARTY_BROKER_A_NAME,  null)
				),
				factory.getExecution(9, LocalDate.of(2019, 8, 30), CUSIP_US1234567891,
						factory.getQuantity(80000000), factory.getPrice(95.03, 94.355, CURRENCY_USD),
						LocalDate.of(2019, 9, 3), true,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME,  null),
						factory.getParty(COUNTERPARTY_BROKER_A_ID, COUNTERPARTY_BROKER_A_NAME,  null)
				),
				factory.getExecution(10, LocalDate.of(2019, 8, 30), CUSIP_DH9105730505,
						factory.getQuantity(7500000), factory.getPrice(95.095, 94.555, CURRENCY_USD),
						LocalDate.of(2019, 9, 3), false,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME, null),
						factory.getParty(COUNTERPARTY_BROKER_B_ID, COUNTERPARTY_BROKER_B_NAME,  null)
				));
	}
}
