package org.isda.cdm.functions;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Iterables;
import org.isda.cdm.*;
import org.isda.cdm.metafields.FieldWithMetaString;

public class CalculatePositionImpl {

	private final List<Execution> executions;

	public CalculatePositionImpl(List<Execution> executions) {
		this.executions = executions;
	}
	
//	@Override
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
				//.setProduct(getProduct(identifier, source))
				.setQuantity(quantity)
				.build();
	}



}
