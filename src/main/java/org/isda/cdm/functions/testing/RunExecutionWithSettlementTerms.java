package org.isda.cdm.functions.testing;

import static org.isda.cdm.functions.testing.FunctionUtils.guard;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import cdm.base.staticdata.party.AncillaryRole;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.metafields.FieldWithMetaDate;

import cdm.base.staticdata.party.Counterparty;
import cdm.event.common.BusinessEvent;
import cdm.event.common.functions.Create_Execution;
import cdm.legalagreement.contract.Contract;
import cdm.product.common.settlement.SettlementTerms;
import cdm.product.common.settlement.functions.CashflowSettlementTerms;
import cdm.product.template.ContractualProduct;
import cdm.product.template.EconomicTerms;
import cdm.product.template.Payout;
import cdm.product.template.Product;
import cdm.product.template.TradableProduct;

public class RunExecutionWithSettlementTerms implements ExecutableFunction<Contract, BusinessEvent> {

    @Inject
    Create_Execution execute;

    @Inject
    CashflowSettlementTerms cashflowSettlementTerms;


    @Override
    public BusinessEvent execute(Contract input) {
        List<SettlementTerms> settlementTerm = getSettlementTerm(input);
        if (!settlementTerm.isEmpty()) {
            input = clearCashPayout(input);
        }

        return execute.evaluate(input.getTradableProduct().getProduct(),
                guard(input.getTradableProduct().getQuantityNotation()),
                guard(input.getTradableProduct().getPriceNotation()),
                guard(input.getTradableProduct().getCounterparty()),
                guard(input.getTradableProduct().getAncillaryRole()),
                guard(input.getParty()),
                guard(input.getPartyRole()),
                settlementTerm,
                null,
                Optional.ofNullable(input.getTradeDate()).map(FieldWithMetaDate::getValue).orElse(null),
                guard(input.getContractIdentifier()));
    }

    private Contract clearCashPayout(Contract input) {
        Contract.ContractBuilder contractBuilder = input.toBuilder();
        Optional.of(contractBuilder)
                .map(Contract.ContractBuilder::getTradableProduct)
                .map(TradableProduct.TradableProductBuilder::getProduct)
                .map(Product.ProductBuilder::getContractualProduct)
                .map(ContractualProduct.ContractualProductBuilder::getEconomicTerms)
                .map(EconomicTerms.EconomicTermsBuilder::getPayout)
                .ifPresent(Payout.PayoutBuilder::clearCashflow);
        return contractBuilder.build();
    }

    private List<SettlementTerms> getSettlementTerm(Contract input) {
    	List<Counterparty> counterparties = Optional.ofNullable(input)
		        .map(Contract::getTradableProduct)
		        .map(TradableProduct::getCounterparty)
		        .orElse(Collections.emptyList());
        List<AncillaryRole> ancillaryRoles = Optional.ofNullable(input)
                .map(Contract::getTradableProduct)
                .map(TradableProduct::getAncillaryRole)
                .orElse(Collections.emptyList());
        return Optional.ofNullable(input)
                .map(Contract::getTradableProduct)
                .map(TradableProduct::getProduct)
                .map(Product::getContractualProduct)
                .map(ContractualProduct::getEconomicTerms)
                .map(EconomicTerms::getPayout)
                .map(Payout::getCashflow)
                .map(cashflows -> cashflows.stream()
                        .map(cashflow -> cashflowSettlementTerms.evaluate(cashflow, counterparties, ancillaryRoles))
                        .collect(Collectors.toList())
                )
                .orElse(Collections.emptyList());
    }

    @Override
    public Class<Contract> getInputType() {
        return Contract.class;
    }

    @Override
    public Class<BusinessEvent> getOutputType() {
        return BusinessEvent.class;
    }

}
