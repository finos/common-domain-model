package com.regnosys.cdm.example.functions;

import cdm.base.staticdata.asset.common.ProductIdTypeEnum;
import cdm.base.staticdata.asset.common.ProductIdentifier;
import cdm.base.staticdata.asset.common.Security;
import cdm.base.staticdata.asset.common.metafields.ReferenceWithMetaProductIdentifier;
import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.TradeState;
import cdm.event.position.*;
import cdm.event.position.functions.EvaluatePortfolioState;
import cdm.product.template.Product;
import com.regnosys.cdm.example.AbstractExampleTest;
import com.regnosys.cdm.example.TestObjectsFactory;
import com.regnosys.cdm.example.functions.impls.EvaluatePortfolioStateImpl;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static com.regnosys.cdm.example.TestObjectsFactory.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
public class EvaluatePortfolioStateTest extends AbstractExampleTest {

	private static final ZonedDateTime DATE_TIME = ZonedDateTime.of(LocalDateTime.of(2019, 8, 30, 11, 00), ZoneId.of("Europe/London"));
	private final BigDecimal TOLERANCE = BigDecimal.valueOf(0.001);

	private EvaluatePortfolioState func;

	@BeforeEach
	public void setUp() {
		super.setUp();
		func = new EvaluatePortfolioStateImpl(getTrades());
		getInjector().injectMembers(func); // FIXME Workaround for stateful function EvaluatePortfolioStateImpl
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
		assertEquals(BigDecimal.valueOf(91000000), getQuantityAmount(p1));
		assertEquals(BigDecimal.valueOf(-8648032500.0).setScale(2), p1.getCashBalance().getAmount().setScale(2));

		Position p2 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.SETTLED);
		assertNotNull(p2);
		assertThat(BigDecimal.valueOf(138000000), closeTo(getQuantityAmount(p2), TOLERANCE));
		assertEquals(BigDecimal.valueOf(-13123950000.0).setScale(2), p2.getCashBalance().getAmount().setScale(2));

		Position p3 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.EXECUTED);
		assertNotNull(p3);
		assertThat(BigDecimal.valueOf(-21000000), closeTo(getQuantityAmount(p3), TOLERANCE));
		assertEquals(BigDecimal.valueOf(1997001750.0).setScale(2), p3.getCashBalance().getAmount().setScale(2));

		Position p4 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.SETTLED);
		assertNotNull(p4);
		assertEquals(BigDecimal.valueOf(410000000), getQuantityAmount(p4));
		assertEquals(BigDecimal.valueOf(-38982750000.0).setScale(2), p4.getCashBalance().getAmount().setScale(2));
	}

	private BigDecimal getQuantityAmount(Position p) {
		return p.getPositionComponent().get(0).getQuantity().get(0).getValue().getAmount();
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
		assertEquals(BigDecimal.valueOf(80000000), getQuantityAmount(p1));
		assertEquals(BigDecimal.valueOf(-7602400000.0).setScale(2), p1.getCashBalance().getAmount().setScale(2));

		Position p2 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.SETTLED);
		assertNotNull(p2);
		assertThat(BigDecimal.valueOf(-2000000), closeTo(getQuantityAmount(p2), TOLERANCE));
		assertEquals(BigDecimal.valueOf(190100000.0).setScale(2), p2.getCashBalance().getAmount().setScale(2));

		Position p3 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.EXECUTED);
		assertNotNull(p3);
		assertThat(BigDecimal.valueOf(-7500000), closeTo(getQuantityAmount(p3), TOLERANCE));
		assertEquals(BigDecimal.valueOf(713212500.0).setScale(2), p3.getCashBalance().getAmount().setScale(2));

		Position p4 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.SETTLED);
		assertNotNull(p4);
		assertEquals(BigDecimal.valueOf(35000000), getQuantityAmount(p4));
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
		assertEquals(BigDecimal.valueOf(91000000), getQuantityAmount(p1));
		assertEquals(BigDecimal.valueOf(-8648032500.0).setScale(2), p1.getCashBalance().getAmount().setScale(2));

		Position p2 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.EXECUTED);
		assertNotNull(p2);
		assertThat(BigDecimal.valueOf(-21000000.0), closeTo(getQuantityAmount(p2), TOLERANCE));
		assertThat(BigDecimal.valueOf(1997001750.00), closeTo(p2.getCashBalance().getAmount(), TOLERANCE));

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
		assertEquals(BigDecimal.valueOf(80000000), getQuantityAmount(p1));
		assertEquals(BigDecimal.valueOf(-7602400000.0).setScale(2), p1.getCashBalance().getAmount().setScale(2));

		Position p2 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.EXECUTED);
		assertNotNull(p2);
		assertThat(BigDecimal.valueOf(-7500000), closeTo(getQuantityAmount(p2), TOLERANCE));
		assertEquals(BigDecimal.valueOf(713212500.0).setScale(2), p2.getCashBalance().getAmount().setScale(2));
	}

	@Test
	void shouldEvaluateTotalPositionForDateAndProduct() {
		Portfolio input = Portfolio.builder()
				.setAggregationParameters(AggregationParameters.builder()
						.setDateTime(DATE_TIME)
						.setTotalPosition(true)
						.addProduct(getProduct(CUSIP_US1234567891, ProductIdTypeEnum.CUSIP))
						.build())
				.build();
		PortfolioState portfolioState = func.evaluate(input);

		assertNotNull(portfolioState);
		assertNotNull(portfolioState.getPositions());
		assertEquals(2, portfolioState.getPositions().size());

		Position p1 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.EXECUTED);
		assertNotNull(p1);
		assertEquals(BigDecimal.valueOf(91000000), getQuantityAmount(p1));
		assertEquals(BigDecimal.valueOf(-8648032500.0).setScale(2), p1.getCashBalance().getAmount().setScale(2));

		Position p2 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.SETTLED);
		assertNotNull(p2);
		assertThat(BigDecimal.valueOf(138000000), closeTo(getQuantityAmount(p2), TOLERANCE));
		assertEquals(BigDecimal.valueOf(-13123950000.0).setScale(2), p2.getCashBalance().getAmount().setScale(2));
	}

	@Test
	void shouldEvaluateDailyPositionForDateAndProduct() {
		Portfolio input = Portfolio.builder()
				.setAggregationParameters(AggregationParameters.builder()
						.setDateTime(DATE_TIME)
						.setTotalPosition(false)
						.addProduct(getProduct(CUSIP_US1234567891, ProductIdTypeEnum.CUSIP))
						.build())
				.build();
		PortfolioState portfolioState = func.evaluate(input);

		assertNotNull(portfolioState);
		assertNotNull(portfolioState.getPositions());
		assertEquals(2, portfolioState.getPositions().size());

		Position p1 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.EXECUTED);
		assertNotNull(p1);
		assertEquals(BigDecimal.valueOf(80000000), getQuantityAmount(p1));
		assertEquals(BigDecimal.valueOf(-7602400000.0).setScale(2), p1.getCashBalance().getAmount().setScale(2));

		Position p2 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.SETTLED);
		assertNotNull(p2);
		assertThat(BigDecimal.valueOf(-2000000), closeTo(getQuantityAmount(p2), TOLERANCE));
		assertEquals(BigDecimal.valueOf(190100000.0).setScale(2), p2.getCashBalance().getAmount().setScale(2));
	}

	private Product getProduct(String productId, ProductIdTypeEnum source) {
		return Product.builder().setSecurity(Security.builder()
				.addProductIdentifierValue(ProductIdentifier.builder()
					.setIdentifier(FieldWithMetaString.builder().setValue(productId).build())
					.setSource(source)))
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
		assertEquals(BigDecimal.valueOf(80000000), getQuantityAmount(p1));
		assertEquals(BigDecimal.valueOf(-7602400000.0).setScale(2), p1.getCashBalance().getAmount().setScale(2));

		Position p2 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.SETTLED);
		assertNotNull(p2);
		assertEquals(BigDecimal.valueOf(140000000), getQuantityAmount(p2));
		assertEquals(BigDecimal.valueOf(-13314050000.0).setScale(2), p2.getCashBalance().getAmount().setScale(2));

		Position p3 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.EXECUTED);
		assertNotNull(p3);
		assertEquals(BigDecimal.valueOf(-13500000), getQuantityAmount(p3));
		assertEquals(BigDecimal.valueOf(1283789250.0).setScale(2), p3.getCashBalance().getAmount().setScale(2));

		Position p4 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.SETTLED);
		assertNotNull(p4);
		assertEquals(BigDecimal.valueOf(125000000), getQuantityAmount(p4));
		assertEquals(BigDecimal.valueOf(-11883125000.0).setScale(2), p4.getCashBalance().getAmount().setScale(2));
	}

	private Position getPosition(PortfolioState portfolioState, String productId, PositionStatusEnum positionStatus) {
		return portfolioState.getPositions()
				.stream()
				// TODO fix position status
				.filter(p -> isProductId(p, productId) /*&& p.getPositionStatus() == positionStatus*/)
				.findFirst()
				.orElse(null);
	}

	private boolean isProductId(Position p, String productId) {
		return p.getTradeReference().getValue().getTrade().getTradableProduct().getProduct().getSecurity().getProductIdentifier().stream()
				.map(ReferenceWithMetaProductIdentifier::getValue)
				.map(ProductIdentifier::getIdentifier)
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

	private List<TradeState> getTrades() {
		TestObjectsFactory factory = new TestObjectsFactory();
		return Arrays.asList(
				factory.getTradeState(1, LocalDate.of(2019, 8, 26), CUSIP_US1234567891,
						150000000, 95.0975, 94.785, CURRENCY_USD,
						LocalDate.of(2019, 8, 28), true,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME,  null),
						factory.getParty(COUNTERPARTY_BROKER_A_ID, COUNTERPARTY_BROKER_A_NAME,  null)
				),
				factory.getTradeState(2, LocalDate.of(2019, 8, 26), CUSIP_DH9105730505,
						250000000, 95.095, 94.78, CURRENCY_USD,
						LocalDate.of(2019, 8, 28), true,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME,  null),
						factory.getParty(COUNTERPARTY_BROKER_B_ID, COUNTERPARTY_BROKER_B_NAME,  null)
				),
				factory.getTradeState(3, LocalDate.of(2019, 8, 27), CUSIP_US1234567891,
						10000000, 95.0575, 94.77, CURRENCY_USD,
						LocalDate.of(2019, 8, 29), false,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME,  null),
						factory.getParty(COUNTERPARTY_BROKER_A_ID, COUNTERPARTY_BROKER_A_NAME,  null)
				),
				factory.getTradeState(4, LocalDate.of(2019, 8, 27), CUSIP_DH9105730505,
						125000000, 95.065, 94.73, CURRENCY_USD,
						LocalDate.of(2019, 8, 29), true,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME,  null),
						factory.getParty(COUNTERPARTY_BROKER_A_ID, COUNTERPARTY_BROKER_A_NAME,  null)
				),
				factory.getTradeState(5, LocalDate.of(2019, 8, 28), CUSIP_US1234567891,
						2000000, 95.05, 94.79, CURRENCY_USD,
						LocalDate.of(2019, 8, 30), false,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME,  null),
						factory.getParty(COUNTERPARTY_BROKER_B_ID, COUNTERPARTY_BROKER_B_NAME,  null)
				),
				factory.getTradeState(6, LocalDate.of(2019, 8, 28), CUSIP_DH9105730505,
						35000000, 95.025, 94.65, CURRENCY_USD,
						LocalDate.of(2019, 8, 30), true,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME,  null),
						factory.getParty(COUNTERPARTY_BROKER_B_ID, COUNTERPARTY_BROKER_B_NAME,  null)
				),
				factory.getTradeState(7, LocalDate.of(2019, 8, 29), CUSIP_US1234567891,
						11000000, 95.0575, 94.63, CURRENCY_USD,
						LocalDate.of(2019, 9, 2), true,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME,  null),
						factory.getParty(COUNTERPARTY_BROKER_B_ID, COUNTERPARTY_BROKER_B_NAME,  null)
				),
				factory.getTradeState(8, LocalDate.of(2019, 8, 29), CUSIP_DH9105730505,
						13500000, 95.0955, 94.685, CURRENCY_USD,
						LocalDate.of(2019, 9, 2), false,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME,  null),
						factory.getParty(COUNTERPARTY_BROKER_A_ID, COUNTERPARTY_BROKER_A_NAME,  null)
				),
				factory.getTradeState(9, LocalDate.of(2019, 8, 30), CUSIP_US1234567891,
						80000000, 95.03, 94.355, CURRENCY_USD,
						LocalDate.of(2019, 9, 3), true,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME,  null),
						factory.getParty(COUNTERPARTY_BROKER_A_ID, COUNTERPARTY_BROKER_A_NAME,  null)
				),
				factory.getTradeState(10, LocalDate.of(2019, 8, 30), CUSIP_DH9105730505,
						7500000, 95.095, 94.555, CURRENCY_USD,
						LocalDate.of(2019, 9, 3), false,
						factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
						factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME, null),
						factory.getParty(COUNTERPARTY_BROKER_B_ID, COUNTERPARTY_BROKER_B_NAME,  null)
				));
	}
}
