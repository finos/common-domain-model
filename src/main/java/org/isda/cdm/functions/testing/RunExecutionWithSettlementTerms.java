package org.isda.cdm.functions.testing;

import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.RelatedPartyReference;
import cdm.event.common.BusinessEvent;
import cdm.event.common.Trade;
import cdm.event.common.TradeState;
import cdm.event.common.functions.Create_Execution;
import cdm.product.common.settlement.SettlementTerms;
import cdm.product.common.settlement.functions.CashflowSettlementTerms;
import cdm.product.template.*;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.metafields.FieldWithMetaDate;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.isda.cdm.functions.testing.FunctionUtils.guard;

public class RunExecutionWithSettlementTerms implements ExecutableFunction<TradeState, BusinessEvent> {

    @Inject
    Create_Execution execute;

    @Inject
    CashflowSettlementTerms cashflowSettlementTerms;


    @Override
    public BusinessEvent execute(TradeState input) {
        List<SettlementTerms> settlementTerm = getSettlementTerm(input);
        if (!settlementTerm.isEmpty()) {
            input = clearCashPayout(input);
        }

        return execute.evaluate(input.getTrade().getTradableProduct().getProduct(),
                guard(input.getTrade().getTradableProduct().getQuantityNotation()),
                guard(input.getTrade().getTradableProduct().getPriceNotation()),
                guard(input.getTrade().getTradableProduct().getCounterparties()),
                guard(input.getTrade().getTradableProduct().getRelatedParties()),
                guard(input.getTrade().getParty()),
                guard(input.getTrade().getPartyRole()),
                settlementTerm,
                null,
                Optional.ofNullable(input.getTrade().getTradeDate()).map(FieldWithMetaDate::getValue).orElse(null),
                guard(input.getTrade().getTradeIdentifier()));
    }

    private TradeState clearCashPayout(TradeState input) {
        TradeState.TradeStateBuilder contractBuilder = input.toBuilder();
        Optional.of(contractBuilder)
                .map(TradeState.TradeStateBuilder::getTrade)
                .map(Trade.TradeBuilder::getTradableProduct)
                .map(TradableProduct.TradableProductBuilder::getProduct)
                .map(Product.ProductBuilder::getContractualProduct)
                .map(ContractualProduct.ContractualProductBuilder::getEconomicTerms)
                .map(EconomicTerms.EconomicTermsBuilder::getPayout)
                .ifPresent(Payout.PayoutBuilder::clearCashflow);
        return contractBuilder.build();
    }

    private List<SettlementTerms> getSettlementTerm(TradeState input) {
    	List<Counterparty> counterparties = Optional.ofNullable(input)
		        .map(TradeState::getTrade)
		        .map(Trade::getTradableProduct)
		        .map(TradableProduct::getCounterparties)
		        .orElse(Collections.emptyList());
        List<RelatedPartyReference> relatedParties = Optional.ofNullable(input)
                .map(TradeState::getTrade)
                .map(Trade::getTradableProduct)
                .map(TradableProduct::getRelatedParties)
                .orElse(Collections.emptyList());
        return Optional.ofNullable(input)
                .map(TradeState::getTrade)
                .map(Trade::getTradableProduct)
                .map(TradableProduct::getProduct)
                .map(Product::getContractualProduct)
                .map(ContractualProduct::getEconomicTerms)
                .map(EconomicTerms::getPayout)
                .map(Payout::getCashflow)
                .map(cashflows -> cashflows.stream()
                        .map(cashflow -> cashflowSettlementTerms.evaluate(cashflow, counterparties, relatedParties))
                        .collect(Collectors.toList())
                )
                .orElse(Collections.emptyList());
    }

    @Override
    public Class<TradeState> getInputType() {
        return TradeState.class;
    }

    @Override
    public Class<BusinessEvent> getOutputType() {
        return BusinessEvent.class;
    }

}
