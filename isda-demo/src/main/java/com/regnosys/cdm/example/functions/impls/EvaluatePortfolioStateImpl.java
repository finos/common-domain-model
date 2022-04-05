package com.regnosys.cdm.example.functions.impls;

import cdm.base.datetime.AdjustableDates;
import cdm.base.math.MeasureBase;
import cdm.base.math.Quantity;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.base.staticdata.asset.common.ProductIdentifier;
import cdm.base.staticdata.asset.common.Security;
import cdm.base.staticdata.asset.common.metafields.FieldWithMetaProductIdentifier;
import cdm.base.staticdata.asset.common.metafields.ReferenceWithMetaProductIdentifier;
import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.PartyRole;
import cdm.base.staticdata.party.PartyRoleEnum;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.Lineage;
import cdm.event.common.Trade;
import cdm.event.common.TradeState;
import cdm.event.position.*;
import cdm.event.position.PortfolioState.PortfolioStateBuilder;
import cdm.event.position.functions.EvaluatePortfolioState;
import cdm.event.position.metafields.ReferenceWithMetaPortfolioState;
import cdm.legalagreement.common.ClosedState;
import cdm.legalagreement.common.ClosedStateEnum;
import cdm.observable.asset.Money;
import cdm.product.common.settlement.CashSettlementTerms;
import cdm.product.common.settlement.PriceQuantity;
import cdm.product.common.settlement.SettlementDate;
import cdm.product.common.settlement.SettlementTerms;
import cdm.product.template.Product;
import cdm.product.template.TradableProduct;
import cdm.product.template.TradeLot;
import com.google.common.collect.MoreCollectors;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.regnosys.rosetta.common.hashing.ReKeyProcessStep;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaDate;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.regnosys.cdm.example.util.ProductIdentifiers.areEqual;

/**
 * Sample EvaluatePortfolioState implementation, should be used as a simple example only.
 */
public class EvaluatePortfolioStateImpl extends EvaluatePortfolioState {

	private final List<TradeState> executions;
	private final List<PostProcessStep> postProcessors;

	// FIXME functions should not have state
	public EvaluatePortfolioStateImpl(List<TradeState> tradeStates) {
		this.executions = tradeStates;
		GlobalKeyProcessStep globalKeyProcessStep = new GlobalKeyProcessStep(NonNullHashCollector::new);
		this.postProcessors = Arrays.asList(globalKeyProcessStep, // Calculates rosetta keys
				new ReKeyProcessStep(globalKeyProcessStep)); // Uses external key/references to populate global reference (which refer to keys generated in earlier steps)
	}

	@Override
	protected PortfolioStateBuilder doEvaluate(Portfolio input) {
		AggregationParameters params = input.getAggregationParameters();
		// For this example ignore time, only used date
		LocalDate date = params.getDateTime().toLocalDate();
		boolean totalPosition = Optional.ofNullable(params.getTotalPosition()).orElse(false);

		// Filter executions and collect into set to avoid any duplicates
		Set<TradeState> filteredExecution = executions.stream()
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
						.addPositionComponent(PriceQuantity.builder()
								.addQuantityValue(Quantity.builder()
										.setAmount(positionQuantity.get(p))))
						.setCashBalance(Money.builder().setAmount(positionCashBalance.get(p))) // TODO add currency
						.build())
				.collect(Collectors.toSet());



		PortfolioStateBuilder portfolioStateBuilder = PortfolioState.builder();
		aggregatedPositions.forEach(portfolioStateBuilder::addPositions);

		// add input portfolioState to output lineage
		portfolioStateBuilder.setLineage(Lineage.builder()
				.addPortfolioStateReference(ReferenceWithMetaPortfolioState.builder()
						.setValue(input.getPortfolioState())
						.build())
				.build());

		// Update keys / references
		postProcessors.forEach(postProcessStep -> postProcessStep.runProcessStep(PortfolioState.class, portfolioStateBuilder));

		return portfolioStateBuilder;
	}

	/**
	 * @param trade
	 * @param date          find executions traded or settled, on or before (depending on totalPosition) this date
	 * @param totalPosition true if aggregating all executions on or before date, false if aggregating executions for that date only
	 * @return true if trade matches filter params
	 */
	private boolean filterByDate(TradeState trade, LocalDate date, boolean totalPosition) {
		// matching on executions on tradeDate or settlementDate across a date range could cause
		// duplicates, however the filtered executions are added to a set.
		return filterByTradeDate(trade, date, totalPosition) || filterBySettlementDate(trade, date, totalPosition);
	}

	/**
	 * @return true if tradeState was traded on or before given date
	 */
	private boolean filterByTradeDate(TradeState tradeState, LocalDate date, boolean totalPosition) {
		return Optional.ofNullable(tradeState.getTrade().getTradeDate())
				.map(FieldWithMetaDate::getValue)
				.map(tradeDate -> matches(date, tradeDate.toLocalDate(), totalPosition))
				.orElseThrow(() -> new RuntimeException(String.format("TradeState date not set on tradeState [%s]", tradeState)));
	}

	/**
	 * @return true if tradeState was settled on or before given date
	 */
	private boolean filterBySettlementDate(TradeState tradeState, LocalDate date, boolean totalPosition) {

		return getSettlementDates(tradeState).stream()
				.map(settlementDate -> matches(date, settlementDate.toLocalDate(), totalPosition))
				.findAny().orElse(true);
	}

	@NotNull
	private List<Date> getSettlementDates(TradeState tradeState) {
		return getSettlementTerms(tradeState).stream()
				.map(SettlementTerms::getSettlementDate)
				.map(SettlementDate::getAdjustableDates)
				.map(AdjustableDates::getAdjustedDate)
				.flatMap(Collection::stream)
				.map(FieldWithMetaDate::getValue)
				.collect(Collectors.toList());
	}

	@NotNull
	private List<SettlementTerms> getSettlementTerms(TradeState tradeState) {
		return Optional.ofNullable(tradeState)
				.map(TradeState::getTrade)
				.map(Trade::getTradableProduct)
				.map(TradableProduct::getTradeLot)
				.orElse(Collections.emptyList()).stream()
				.map(TradeLot::getPriceQuantity)
				.flatMap(Collection::stream)
				.map(PriceQuantity::getSettlementTerms)
				.collect(Collectors.toList());
	}

	private boolean matches(LocalDate dateToFind, LocalDate tradeDate, boolean totalPosition) {
		return totalPosition ?
				dateToFind.compareTo(tradeDate) >= 0 : // for total position match dates on for before the given date
				dateToFind.compareTo(tradeDate) == 0; // for daily position match dates only on given date
	}

	/**
	 * @return true if position status matches
	 */
	private boolean filterByPositionStatus(TradeState tradeState, LocalDate date, PositionStatusEnum positionStatusToFind) {
		if (positionStatusToFind == null) {
			return true;
		}
		return Optional.ofNullable(tradeState.getTrade().getTradeDate())
				.map(FieldWithMetaDate::getValue)
				.map(tradeDate -> toPositionStatus(tradeState, date))
				.map(s -> s == positionStatusToFind)
				.orElseThrow(() -> new RuntimeException(String.format("TradeState date not set on tradeState [%s]", tradeState)));
	}

	/**
	 * @return true if tradeState product matches given products
	 */
	private boolean filterByProducts(TradeState tradeState, List<? extends Product> productsToFind) {
		if (productsToFind == null || productsToFind.isEmpty()) {
			return true;
		}

		List<? extends ProductIdentifier> productIdentifiers = Optional.ofNullable(tradeState.getTrade().getTradableProduct())
				.map(TradableProduct::getProduct)
				.map(Product::getSecurity)
				.map(Security::getProductIdentifier)
				.orElse(Collections.emptyList())
				.stream()
				.map(ReferenceWithMetaProductIdentifier::getValue)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());

		if (productIdentifiers.isEmpty()) {
			new RuntimeException(String.format("ProductIdentifier not set on tradeState [%s]", tradeState));
		}

		return productsToFind.stream()
				.map(Product::getSecurity)
				.map(Security::getProductIdentifier)
				.flatMap(Collection::stream)
				.map(ReferenceWithMetaProductIdentifier::getValue)
				.anyMatch(id -> productIdentifiers.stream().anyMatch(_id -> areEqual(id, _id)));
	}

	/**
	 * @return true if tradeState parties contains given partyId
	 */
	private boolean filterByParty(TradeState tradeState, List<? extends ReferenceWithMetaParty> partyToFind) {
		if (partyToFind == null || partyToFind.isEmpty()) {
			return true;
		}

		List<String> partyIdToFind = partyToFind.stream()
				.map(ReferenceWithMetaParty::getValue)
				.map(Party::getPartyId)
				.flatMap(List::stream)
				.map(FieldWithMetaString::getValue)
				.collect(Collectors.toList());

		return tradeState.getTrade().getParty().stream()
				.map(Party::getPartyId)
				.flatMap(List::stream)
				.map(FieldWithMetaString::getValue)
				.anyMatch(partyIdToFind::contains);
	}

	/**
	 * @return PositionBuilder with product and position status set
	 */
	private Position toPosition(TradeState tradeState, LocalDate date) {
		Position.PositionBuilder positionBuilder = Position.builder();
//		Quantity quantity = getQuantity(tradeState);
//		if (quantity.getUnit() != null) {
//			throw new IllegalArgumentException("Position aggregation not supported for quantities with units " + quantity.getUnit().name());
//		}
		positionBuilder
				//.setPositionStatus(toPositionStatus(tradeState, date))
				.setTradeReferenceValue(tradeState);
		return positionBuilder.build();
	}

	/**
	 * @return PositionStatus (i.e. CANCELLED, SETTLED, EXECUTED) for given date
	 */
	protected PositionStatusEnum toPositionStatus(TradeState tradeState, LocalDate date) {
		Optional<ClosedState> closedState = Optional.ofNullable(tradeState.getState().getClosedState());
		if (closedState.map(s -> s.getState() == ClosedStateEnum.CANCELLED).orElse(false)
				&& closedState.map(ClosedState::getActivityDate).map(d -> date.isAfter(d.toLocalDate())).orElse(false)) {
			return PositionStatusEnum.CANCELLED;
		} else if (getSettlementDates(tradeState).stream()
				.map(settlementDate -> date.compareTo(settlementDate.toLocalDate()) >= 0)
				.findAny()
				.orElse(false)) {
			return PositionStatusEnum.SETTLED;
		} else if (Optional.ofNullable(tradeState.getTrade().getTradeDate())
				.map(FieldWithMetaDate::getValue)
				.map(tradeDate -> date.compareTo(tradeDate.toLocalDate()) >= 0)
				.orElse(false)) {
			return PositionStatusEnum.EXECUTED;
		} else {
			throw new RuntimeException(String.format("Unable to determine PositionStatus on date [%s] for tradeState [%s]", date, tradeState));
		}
	}

	/**
	 * @return trade quantity corresponding to the trade product identifier
	 */
	private Quantity getQuantity(TradeState tradeState) {
		List<? extends ProductIdentifier> productIdentifiers = tradeState.getTrade().getTradableProduct()
				.getProduct()
				.getSecurity()
				.getProductIdentifier()
				.stream()
				.map(ReferenceWithMetaProductIdentifier::getValue)
				.collect(Collectors.toList());
		// find quantity for product identifier
		return tradeState.getTrade().getTradableProduct().getTradeLot()
				.stream()
				.map(TradeLot::getPriceQuantity)
				.flatMap(Collection::stream)
				.filter(pq -> productIdentifiers.stream().anyMatch(pid ->
						pq.getObservable()
								.getProductIdentifier()
								.stream()
								.map(FieldWithMetaProductIdentifier::getValue)
								.anyMatch(pqid -> areEqual(pid, pqid))))
				.map(PriceQuantity::getQuantity)
				.flatMap(Collection::stream).map(FieldWithMetaQuantity::getValue)
				.findFirst()
				.orElseThrow(() -> new RuntimeException(String.format("Unable to determine quantity for product identifier [%s]", productIdentifiers)));
	}

	/**
	 * @return trade quantity, as positive for buy, and negative for sell
	 */
	private BigDecimal getAggregationQuantity(TradeState e) {
		PartyRoleEnum buyOrSell = getExecutingEntityBuyOrSell(e);
		BigDecimal quantity = getQuantity(e).getAmount();
		return buyOrSell == PartyRoleEnum.SELLER ?
				quantity.negate() : // if selling, reduce position
				quantity; // if buying, increase position
	}

	/**
	 * @return trade cash balance, as negative for buy, and positive for sell
	 */
	private BigDecimal getAggregationSettlementAmount(TradeState tradeState) {
		PartyRoleEnum buyOrSell = getExecutingEntityBuyOrSell(tradeState);

		return getSettlementTerms(tradeState).stream().findFirst()
				.map(SettlementTerms::getCashSettlementTerms)
				.map(Collection::stream)
				.flatMap(Stream::findFirst)
				.map(CashSettlementTerms::getCashSettlementAmount)
				.map(MeasureBase::getAmount)
				.map(settlementAmount ->
						buyOrSell == PartyRoleEnum.SELLER ?
								settlementAmount : // if selling, increase cash
								settlementAmount.negate() // if buying, reduce cash);
				)
				.orElse(BigDecimal.ZERO);
	}

	/**
	 * @return returns Buy/Sell direction of executing entity
	 */
	private PartyRoleEnum getExecutingEntityBuyOrSell(TradeState tradeState) {
		String partyReference = tradeState.getTrade().getPartyRole().stream()
				.filter(r -> r.getRole() == PartyRoleEnum.EXECUTING_ENTITY)
				.map(r -> r.getPartyReference().getGlobalReference())
				.collect(MoreCollectors.onlyElement());
		return tradeState.getTrade().getPartyRole().stream()
				.filter(r -> partyReference.equals(r.getPartyReference().getGlobalReference()))
				.map(PartyRole::getRole)
				.filter(r -> r == PartyRoleEnum.BUYER || r == PartyRoleEnum.SELLER)
				.collect(MoreCollectors.onlyElement());
	}
}
