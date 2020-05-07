package org.isda.cdm.functions.testing;

import static org.isda.cdm.functions.testing.FunctionUtils.guard;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import javax.inject.Inject;

import org.isda.cdm.BusinessEvent;
import org.isda.cdm.BusinessEvent.BusinessEventBuilder;
import org.isda.cdm.CashPrice;
import org.isda.cdm.Cashflow;
import org.isda.cdm.Contract;
import org.isda.cdm.Execution.ExecutionBuilder;
import org.isda.cdm.GoverningLawEnum;
import org.isda.cdm.LegalAgreement;
import org.isda.cdm.LegalAgreementNameEnum;
import org.isda.cdm.LegalAgreementPublisherEnum;
import org.isda.cdm.LegalAgreementType;
import org.isda.cdm.Price;
import org.isda.cdm.PriceNotation;
import org.isda.cdm.SettlementTerms;
import org.isda.cdm.functions.Create_ContractFormation;
import org.isda.cdm.functions.Create_Execution;

import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.lib.records.DateImpl;

import cdm.base.datetime.AdjustableDate;
import cdm.base.datetime.AdjustableOrRelativeDate;

public class RunFormContractWithSettlementTerms implements ExecutableFunction<Contract, BusinessEvent> {

    @Inject
    Create_Execution execute;

    @Inject
    Create_ContractFormation formContract;


    @Override
    public BusinessEvent execute(Contract contract) {

    	Cashflow cashflow = contract.getTradableProduct()
	    	.getProduct()
	    	.getContractualProduct()
	    	.getEconomicTerms()
	    	.getPayout()
	    	.getCashflow().get(0);
    	    	
    	Optional<PriceNotation> cashPricePriceNotation = contract.getTradableProduct().getPriceNotation().stream()
    	    	.filter(pn -> pn.getPrice().getCashPrice() != null)
    	    	.findAny();

    	Optional<CashPrice> cashPrice = cashPricePriceNotation.map(pn -> pn.getPrice().getCashPrice());
    	    	
    	SettlementTerms settlementTerms = SettlementTerms.builder()
    			.setAssetIdentifier(cashflow.getPayoutQuantity().getAssetIdentifier())
    			.setPayerReceiver(cashflow.getPayerReceiver())
    			.setSettlementDate(AdjustableOrRelativeDate.builder()
    					.setAdjustableDate(AdjustableDate.builder()
    							.setDateAdjustments(cashflow.getCashflowDate().getDateAdjustments())
    							.setUnadjustedDate(cashflow.getCashflowDate().getUnadjustedDate())
    							.build())
    					.build())

    			.build();
    	
    	BusinessEvent executeBusinessEvent = execute.evaluate(contract.getTradableProduct().getProduct(),
                guard(contract.getTradableProduct().getQuantityNotation()),
                guard(contract.getTradableProduct().getPriceNotation()),
                guard(contract.getParty()),
                guard(contract.getPartyRole()));
    	
    	BusinessEventBuilder builder = executeBusinessEvent.toBuilder();
		ExecutionBuilder execution = builder.getPrimitives()
			.get(0)
			.getExecution()
			.getAfter()
			.getExecution();
		
		execution.addSettlementTerms(settlementTerms);
		
		execution.getTradableProduct().getPriceNotation().stream()
		.filter(x -> x.getPrice().getCashPrice() != null)
		.forEach(x -> x.setAssetIdentifier(cashflow.getPayoutQuantity().getAssetIdentifier()));
		

        return builder.build();
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
