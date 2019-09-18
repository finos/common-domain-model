package org.isda.cdm.functions;

import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.*;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EvaluatePortfolioStateTest {

	private static final String EXECUTIONS_PATH = "src/main/resources/cdm-sample-files/functions/position/";

	private static final String CUSIP_US1234567891 = "US1234567891";
	private static final String CUSIP_DH9105730505 = "DH9105730505";
	public static final Date DATE = DateImpl.of(2019, 8, 29);

	private EvaluatePortfolioState func;

	@BeforeEach
	void setUp() {
		try (Stream<Path> paths = Files.walk(Paths.get(EXECUTIONS_PATH))) {
			List<Execution> executions =  paths
					.filter(Files::isRegularFile)
					.map(path -> {
						try {
							return RosettaObjectMapper.getDefaultRosettaObjectMapper().readValue(Files.readAllBytes(path), Execution.class);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					})
					.collect(Collectors.toList());
			func = new EvaluatePortfolioStateImpl(executions);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void shouldFilterOnDateOnly() {
		Portfolio input = Portfolio.builder()
								   .setAggregationParameters(AggregationParameters.builder()
																				  .setDate(DATE)
																				  .build())
								   .build();
		PortfolioState portfolioState = func.doEvaluate(input);

		assertNotNull(portfolioState);
		assertNotNull(portfolioState.getPositions());
		assertEquals(4, portfolioState.getPositions().size());

		Position p1 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.EXECUTED);
		assertNotNull(p1);
		assertEquals(p1.getQuantity().getAmount(), BigDecimal.valueOf(13000000));

		Position p2 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.SETTLED);
		assertNotNull(p2);
		assertEquals(p2.getQuantity().getAmount(), BigDecimal.valueOf(150000000));

		Position p3 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.EXECUTED);
		assertNotNull(p3);
		assertEquals(p3.getQuantity().getAmount(), BigDecimal.valueOf(170000000));

		Position p4 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.SETTLED);
		assertNotNull(p4);
		assertEquals(p4.getQuantity().getAmount(), BigDecimal.valueOf(130000000));
	}

	@Test
	void shouldFilterOnDateAndPositionStatus() {
		Portfolio input = Portfolio.builder()
								   .setAggregationParameters(AggregationParameters.builder()
																				  .setDate(DATE)
																				  .setPositionStatus(PositionStatusEnum.EXECUTED)
																				  .build())
								   .build();
		PortfolioState portfolioState = func.doEvaluate(input);

		assertNotNull(portfolioState);
		assertNotNull(portfolioState.getPositions());
		assertEquals(2, portfolioState.getPositions().size());

		Position p1 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.EXECUTED);
		assertNotNull(p1);
		assertEquals(p1.getQuantity().getAmount(), BigDecimal.valueOf(13000000));

		Position p2 = getPosition(portfolioState, CUSIP_DH9105730505, PositionStatusEnum.EXECUTED);
		assertNotNull(p2);
		assertEquals(p2.getQuantity().getAmount(), BigDecimal.valueOf(170000000));
	}

	@Test
	void shouldFilterOnDateAndProduct() {
		Portfolio input = Portfolio.builder()
								   .setAggregationParameters(AggregationParameters.builder()
																				  .setDate(DATE)
																				  .addProduct(getProduct(CUSIP_US1234567891, ProductIdSourceEnum.CUSIP))
																				  .build())
								   .build();
		PortfolioState portfolioState = func.doEvaluate(input);

		assertNotNull(portfolioState);
		assertNotNull(portfolioState.getPositions());
		assertEquals(2, portfolioState.getPositions().size());

		Position p1 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.EXECUTED);
		assertNotNull(p1);
		assertEquals(p1.getQuantity().getAmount(), BigDecimal.valueOf(13000000));

		Position p2 = getPosition(portfolioState, CUSIP_US1234567891, PositionStatusEnum.SETTLED);
		assertNotNull(p2);
		assertEquals(p2.getQuantity().getAmount(), BigDecimal.valueOf(150000000));
	}

	private Product getProduct(String productId, ProductIdSourceEnum source) {
		return Product.builder().setSecurityBuilder(Security.builder()
					.setBondBuilder(Bond.builder()
						.setProductIdentifierBuilder(ProductIdentifier.builder()
							.addIdentifier(FieldWithMetaString.builder().setValue(productId).build())
							.setSource(source))))
					.build();
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
}
