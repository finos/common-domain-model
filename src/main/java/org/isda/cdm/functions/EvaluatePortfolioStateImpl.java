package org.isda.cdm.functions;

import com.regnosys.rosetta.common.hashing.*;
import com.rosetta.model.lib.process.PostProcessStep;
import org.isda.cdm.*;
import org.isda.cdm.metafields.FieldWithMetaDate;
import org.isda.cdm.metafields.FieldWithMetaString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EvaluatePortfolioStateImpl extends EvaluatePortfolioState {

	private final List<Execution> executions;
	private final List<PostProcessStep> postProcessors;

	public EvaluatePortfolioStateImpl(List<Execution> executions) {
		this.executions = executions;
		RosettaKeyProcessStep rosettaKeyProcessStep = new RosettaKeyProcessStep(() -> new NonNullHashCollector());
		this.postProcessors = Arrays.asList(rosettaKeyProcessStep,
				new RosettaKeyValueProcessStep(() -> new RosettaKeyValueHashFunction()),
				new ReKeyProcessStep(rosettaKeyProcessStep));
	}

	@Override
	protected PortfolioState doEvaluate(Portfolio input) {
		AggregationParameters params = input.getAggregationParameters();
		LocalDate date = params.getDate().toLocalDate();

		// Filter executions and build position -> aggregated position map
		Map<Position, BigDecimal> positionQuantity = executions.stream()
				.filter(e -> filterByDate(e, date))
			    .filter(e -> filterByPositionStatus(e, date, params.getPositionStatus()))
				.filter(e -> filterByProducts(e, params.getProduct()))
				// TODO filter by other aggregration parameters
				.collect(Collectors.groupingBy(
						e -> toPosition(e, date),
						Collectors.reducing(BigDecimal.ZERO, e -> e.getQuantity().getAmount(), BigDecimal::add)));

		// Update Position with aggregated quantity
		Set<Position> aggregatedPositions = positionQuantity.keySet().stream()
				.map(p -> p.toBuilder().setQuantityBuilder(Quantity.builder().setAmount(positionQuantity.get(p))).build())
				.collect(Collectors.toSet());

		PortfolioState.PortfolioStateBuilder portfolioStateBuilder = PortfolioState.builder();
		aggregatedPositions.forEach(p -> portfolioStateBuilder.addPositions(p));

		// Update keys / references
		postProcessors.forEach(postProcessStep -> postProcessStep.runProcessStep(PortfolioState.class, portfolioStateBuilder));

		return portfolioStateBuilder.build();
	}

	/**
	 * @return true if execution was traded on or before given date
	 */
	private boolean filterByDate(Execution execution, LocalDate date) {
		return Optional.ofNullable(execution.getTradeDate())
					   .map(FieldWithMetaDate::getValue)
					   .map(tradeDate -> date.compareTo(tradeDate.toLocalDate()) >= 0)
					   .orElseThrow(() -> new RuntimeException(String.format("Trade date not set on execution [%s]", execution)));
	}

	/**
	 * @return true if position status matches
	 */
	private boolean filterByPositionStatus(Execution execution, LocalDate date, PositionStatusEnum positionStatusToFind) {
		if (positionStatusToFind == null) {
			return true;
		}
		return Optional.ofNullable(execution.getTradeDate())
					   .map(FieldWithMetaDate::getValue)
					   .map(tradeDate -> toPositionStatus(execution, date))
					   .map(s -> s == positionStatusToFind)
					   .orElseThrow(() -> new RuntimeException(String.format("Trade date not set on execution [%s]", execution)));
	}

	/**
	 * @return true if execution was traded on or before given date
	 */
	private boolean filterByProducts(Execution execution, List<Product> productsToFind) {
		if (productsToFind == null || productsToFind.isEmpty()) {
			return true;
		}

		ProductIdentifier productIdentifier = Optional.ofNullable(execution.getProduct())
													  .map(Product::getSecurity)
													  .map(Security::getBond)
													  .map(Bond::getProductIdentifier)
													  .orElseThrow(() -> new RuntimeException(
															  String.format("ProductIdentifier not set on execution [%s]", execution)));
		Set<String> identifiers = getIdentifiersAsString(productIdentifier);
		ProductIdSourceEnum source = productIdentifier.getSource();

		return productsToFind.stream()
					   .map(Product::getSecurity)
					   .map(Security::getBond)
					   .map(Bond::getProductIdentifier)
					   .anyMatch(idsToFind -> getIdentifiersAsString(idsToFind).stream().anyMatch(identifiers::contains) && source == idsToFind.getSource());
	}

	private Set<String> getIdentifiersAsString(ProductIdentifier productIdentifier) {
		return productIdentifier.getIdentifier().stream().map(FieldWithMetaString::getValue).collect(Collectors.toSet());
	}

	/**
	 * @return PositionBuilder with product and position status set
	 */
	private Position toPosition(Execution execution, LocalDate date) {
		Quantity quantity = execution.getQuantity();
		if (quantity.getUnit() != null) {
			throw new IllegalArgumentException("Position aggregation not supported for quantities with units " + quantity.getUnit().name());
		}
		Position.PositionBuilder positionBuilder = Position.builder()
														   .setPositionStatus(toPositionStatus(execution, date))
														   .setProduct(execution.getProduct());
		// Update keys / references
		postProcessors.forEach(postProcessStep -> postProcessStep.runProcessStep(Position.class, positionBuilder));

		return positionBuilder.build();
	}

	/**
	 * @return PositionStatus (i.e. CANCELLED, SETTLED, EXECUTED) for given date
	 */
	private PositionStatusEnum toPositionStatus(Execution execution, LocalDate date) {
		Optional<ClosedState> closedState = Optional.ofNullable(execution.getClosedState());
		if (closedState.map(s -> s.getState() == ClosedStateEnum.CANCELLED).orElse(false)
				&& closedState.map(ClosedState::getActivityDate).map(d -> date.isAfter(d.toLocalDate())).orElse(false)) {
			return PositionStatusEnum.CANCELLED;
		}
		else if (Optional.ofNullable(execution.getSettlementTerms())
						   .map(SettlementTerms::getSettlementDate)
						   .map(AdjustableOrRelativeDate::getAdjustableDate)
						   .map(AdjustableDate::getAdjustedDate)
						   .map(FieldWithMetaDate::getValue)
						   .map(settlementDate -> date.compareTo(settlementDate.toLocalDate()) >= 0)
						   .orElse(false)) {
			return PositionStatusEnum.SETTLED;
		}
		else if (Optional.ofNullable(execution.getTradeDate())
						 .map(FieldWithMetaDate::getValue)
						 .map(tradeDate -> date.compareTo(tradeDate.toLocalDate()) >= 0)
						 .orElse(false)) {
			return PositionStatusEnum.EXECUTED;
		}
		else {
			throw new RuntimeException(String.format("Unable to determine PositionStatus on date [%s] for execution [%s]", date, execution));
		}
	}
}
