package org.isda.cdm.functions;

import com.google.common.collect.MoreCollectors;
import com.regnosys.rosetta.common.hashing.*;
import com.rosetta.model.lib.process.PostProcessStep;
import org.isda.cdm.*;
import org.isda.cdm.PortfolioState.PortfolioStateBuilder;
import org.isda.cdm.metafields.FieldWithMetaDate;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.ReferenceWithMetaParty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EvaluatePortfolioStateImpl extends EvaluatePortfolioState {

	private final List<Execution> executions;
	private final List<PostProcessStep> postProcessors;
	
	// FIXME functions should not have state
	public EvaluatePortfolioStateImpl(List<Execution> executions) {
		this.executions = executions;
		RosettaKeyProcessStep rosettaKeyProcessStep = new RosettaKeyProcessStep(NonNullHashCollector::new);
		this.postProcessors = Arrays.asList(rosettaKeyProcessStep, // Calculates rosetta keys
				new RosettaKeyValueProcessStep(RosettaKeyValueHashFunction::new), // Calculates rosetta key values
				new ReKeyProcessStep(rosettaKeyProcessStep)); // Uses external key/references to populate global reference (which refer to keys generated in earlier steps)
	}

	@Override
	protected PortfolioStateBuilder doEvaluate(Portfolio input) {
		AggregationParameters params = input.getAggregationParameters();
		// For this example ignore time, only used date
		LocalDate date = params.getDateTime().toLocalDate();
		boolean totalPosition = Optional.ofNullable(params.getTotalPosition()).orElse(false);

		// Filter executions and collect into set to avoid any duplicates
		Set<Execution> filteredExecution = executions.stream()
													 .filter(e -> filterByDate(e, date, totalPosition))
													 .filter(e -> filterByPositionStatus(e, date, params.getPositionStatus()))
													 .filter(e -> filterByProducts(e, params.getProduct()))
													 .filter(e -> filterByParty(e, params.getParty()))
													 // TODO filter by other aggregration parameters
													 .collect(Collectors.toSet());
		// Build position -> aggregated position map
		Map<Position, BigDecimal> positionQuantity = filteredExecution.stream()
																	  .collect(Collectors.groupingBy(
																			  e -> toPosition(e, date),
																			  Collectors.reducing(BigDecimal.ZERO, this::getAggregationQuantity,
																					  BigDecimal::add)));

		// Build position -> aggregated cash balance map
		Map<Position, BigDecimal> positionCashBalance = filteredExecution.stream()
				.collect(Collectors.groupingBy(
						e -> toPosition(e, date),
						Collectors.reducing(BigDecimal.ZERO, this::getAggregationSettlementAmount,
								BigDecimal::add)));

		// Update Position with aggregated quantity
		Set<Position> aggregatedPositions = positionQuantity.keySet().stream()
				.map(p -> p.toBuilder()
						.setQuantityBuilder(Quantity.builder().setAmount(positionQuantity.get(p)))
						.setCashBalanceBuilder(Money.builder().setAmount(positionCashBalance.get(p))) // TODO add currency
						.build())
				.collect(Collectors.toSet());



		PortfolioStateBuilder portfolioStateBuilder = PortfolioState.builder();
		aggregatedPositions.forEach(portfolioStateBuilder::addPositions);

		// Update keys / references
		postProcessors.forEach(postProcessStep -> postProcessStep.runProcessStep(PortfolioState.class, portfolioStateBuilder));

		return portfolioStateBuilder;
	}

	/**
	 * @param execution
	 * @param date          find executions traded or settled, on or before (depending on totalPosition) this date
	 * @param totalPosition true if aggregating all executions on or before date, false if aggregating executions for that date only
	 * @return true if execution matches filter params
	 */
	private boolean filterByDate(Execution execution, LocalDate date, boolean totalPosition) {
		// matching on executions on tradeDate or settlementDate across a date range could cause
		// duplicates, however the filtered executions are added to a set.
		return filterByTradeDate(execution, date, totalPosition) || filterBySettlementDate(execution, date, totalPosition);
	}

	/**
	 * @return true if execution was traded on or before given date
	 */
	private boolean filterByTradeDate(Execution execution, LocalDate date, boolean totalPosition) {
		return Optional.ofNullable(execution.getTradeDate())
					   .map(FieldWithMetaDate::getValue)
					   .map(tradeDate -> matches(date, tradeDate.toLocalDate(), totalPosition))
					   .orElseThrow(() -> new RuntimeException(String.format("Trade date not set on execution [%s]", execution)));
	}

	/**
	 * @return true if execution was settled on or before given date
	 */
	private boolean filterBySettlementDate(Execution execution, LocalDate date, boolean totalPosition) {
		return Optional.ofNullable(execution.getSettlementTerms())
					   .map(SettlementTerms::getSettlementDate)
					   .map(AdjustableOrRelativeDate::getAdjustableDate)
					   .map(AdjustableDate::getAdjustedDate)
					   .map(FieldWithMetaDate::getValue)
					   .map(settlementDate -> matches(date, settlementDate.toLocalDate(), totalPosition))
					   .orElse(true); // SettlementDate not set on execution
	}

	private boolean matches(LocalDate dateToFind, LocalDate tradeDate, boolean totalPosition) {
		return totalPosition ?
				dateToFind.compareTo(tradeDate) >= 0 : // for total position match dates on for before the given date
				dateToFind.compareTo(tradeDate) == 0; // for daily position match dates only on given date
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
	 * @return true if execution product matches given products
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
							 .anyMatch(idsToFind ->
									 getIdentifiersAsString(idsToFind).stream().anyMatch(identifiers::contains)
											 && source == idsToFind.getSource());
	}

	private Set<String> getIdentifiersAsString(ProductIdentifier productIdentifier) {
		return productIdentifier.getIdentifier().stream().map(FieldWithMetaString::getValue).collect(Collectors.toSet());
	}

	/**
	 * @return true if execution parties contains given partyId
	 */
	private boolean filterByParty(Execution execution, List<ReferenceWithMetaParty> partyToFind) {
		if (partyToFind == null || partyToFind.isEmpty()) {
			return true;
		}

		List<String> partyIdToFind = partyToFind.stream()
												.map(ReferenceWithMetaParty::getValue)
												.map(Party::getPartyId)
												.flatMap(List::stream)
												.map(FieldWithMetaString::getValue)
												.collect(Collectors.toList());

		return execution.getParty().stream()
						.map(ReferenceWithMetaParty::getValue)
						.map(Party::getPartyId)
						.flatMap(List::stream)
						.map(FieldWithMetaString::getValue)
						.anyMatch(partyIdToFind::contains);
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
		} else if (Optional.ofNullable(execution.getSettlementTerms())
						   .map(SettlementTerms::getSettlementDate)
						   .map(AdjustableOrRelativeDate::getAdjustableDate)
						   .map(AdjustableDate::getAdjustedDate)
						   .map(FieldWithMetaDate::getValue)
						   .map(settlementDate -> date.compareTo(settlementDate.toLocalDate()) >= 0)
						   .orElse(false)) {
			return PositionStatusEnum.SETTLED;
		} else if (Optional.ofNullable(execution.getTradeDate())
						   .map(FieldWithMetaDate::getValue)
						   .map(tradeDate -> date.compareTo(tradeDate.toLocalDate()) >= 0)
						   .orElse(false)) {
			return PositionStatusEnum.EXECUTED;
		} else {
			throw new RuntimeException(String.format("Unable to determine PositionStatus on date [%s] for execution [%s]", date, execution));
		}
	}

	/**
	 * @return execution quantity, as positive for buy, and negative for sell
	 */
	private BigDecimal getAggregationQuantity(Execution e) {
		PartyRoleEnum buyOrSell = getExecutingEntityBuyOrSell(e);
		BigDecimal quantity = e.getQuantity().getAmount();
		return buyOrSell == PartyRoleEnum.SELLER ?
				quantity.negate() : // if selling, reduce position
				quantity; // if buying, increase position
	}

	/**
	 * @return execution cash balance, as negative for buy, and positive for sell
	 */
	private BigDecimal getAggregationSettlementAmount(Execution e) {
		PartyRoleEnum buyOrSell = getExecutingEntityBuyOrSell(e);
		BigDecimal settlementAmount = e.getSettlementTerms().getSettlementAmount().getAmount();
		return buyOrSell == PartyRoleEnum.SELLER ?
				settlementAmount : // if selling, increase cash
				settlementAmount.negate(); // if buying, reduce cash
	}

	/**
	 * @return returns Buy/Sell direction of executing entity
	 */
	private PartyRoleEnum getExecutingEntityBuyOrSell(Execution e) {
		String partyReference = e.getPartyRole().stream()
								 .filter(r -> r.getRole() == PartyRoleEnum.EXECUTING_ENTITY)
								 .map(r -> r.getPartyReference().getGlobalReference())
								 .collect(MoreCollectors.onlyElement());
		return e.getPartyRole().stream()
				.filter(r -> partyReference.equals(r.getPartyReference().getGlobalReference()))
				.map(PartyRole::getRole)
				.filter(r -> r == PartyRoleEnum.BUYER || r == PartyRoleEnum.SELLER)
				.collect(MoreCollectors.onlyElement());
	}
}
