package org.isda.cdm.functions;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Iterables;
import org.isda.cdm.*;
import org.isda.cdm.metafields.FieldWithMetaString;

public class CalculatePositionImpl extends CalculatePosition {

	private final List<Execution> executions;

	public CalculatePositionImpl(List<Execution> executions) {
		this.executions = executions;
	}
	
	@Override
	protected List<Position> doEvaluate(AggregationParameters aggregationParameters) {
		List<org.isda.cdm.Position> positions = executions.stream().map(e -> toPosition(e)).collect(Collectors.toList());
		//.setQuantityBuilder()
//	);
		return positions;
	}

	private org.isda.cdm.Position toPosition(Execution execution) {
		ProductIdentifier productIdentifier = execution.getProduct().getSecurity().getBond().getProductIdentifier();
		String identifier = Iterables.getOnlyElement(productIdentifier.getIdentifier()).getValue();
		ProductIdSourceEnum source = productIdentifier.getSource();
		Quantity quantity = execution.getQuantity();
		return org.isda.cdm.Position.builder()
				.setProductBuilder(Product.builder()
					.setSecurityBuilder(Security.builder()
						.setBondBuilder(Bond.builder()
							.setProductIdentifierBuilder(ProductIdentifier.builder()
								.addIdentifier(FieldWithMetaString.builder().setValue(identifier).build())
								.setSource(source)))))
				.setQuantity(quantity)
				.build();
	}

	private Execution createExecution() {
		ProductIdSourceEnum cusip = ProductIdSourceEnum.CUSIP;
		String productId = "";
		return Execution.builder()
						//.addIdentifier()
						.setProductBuilder(Product.builder()
							.setSecurityBuilder(Security.builder()
								.setBondBuilder(Bond.builder()
									.setProductIdentifierBuilder(ProductIdentifier.builder()
										.addIdentifier(FieldWithMetaString.builder().setValue(productId).build())
									.setSource(cusip)))))
						.setPriceBuilder(Price.builder()
							.setNetPrice(newActualPrice(100.1, "USD"))
							.setCleanNetPrice(newActualPrice(100.05, "USD"))
							.setAccruedInterest(BigDecimal.valueOf(123456)))
						.setQuantityBuilder(Quantity.builder().setAmount(BigDecimal.valueOf(100000)))
						//.addParty()
						//.addPartyRole()
						.setClosedStateBuilder(ClosedState.builder())
						.setSettlementTermsBuilder(SettlementTerms.builder())
						.setExecutionType(ExecutionTypeEnum.ELECTRONIC)
						.build();
	}

	private ActualPrice newActualPrice(double amount, String currency) {
		return ActualPrice.builder()
						  .setAmount(BigDecimal.valueOf(amount))
						  .setCurrency(FieldWithMetaString.builder().setValue(currency).build())
						  .build();
	}
}
