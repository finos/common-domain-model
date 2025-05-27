package com.regnosys.cdm.example;

import cdm.product.template.ContractualProduct;
import cdm.product.template.EconomicTerms;
import cdm.product.template.Payout;
import cdm.product.template.meta.EconomicTermsMeta;
import jakarta.inject.Inject;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationHandlerProvider;
import com.rosetta.model.lib.qualify.QualifyFunctionFactory;
import com.rosetta.model.lib.qualify.QualifyResult;
import com.rosetta.model.lib.qualify.QualifyResultsExtractor;
import org.isda.cdm.qualify.EconomicTermsQualificationHandler;

import java.math.BigDecimal;

import static com.regnosys.cdm.example.InterestRatePayoutCreation.getFixedRatePayout;
import static com.regnosys.cdm.example.InterestRatePayoutCreation.getFloatingRatePayout;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Qualification extends AbstractExample {

    @Inject
    private QualifyFunctionFactory.Default qualifyFunctionFactory;

    @Inject
    private QualificationHandlerProvider qualificationHandlerProvider;

    @Override
    public void example() {
        // Build an ContractualProduct containing an EconomicTerms with two InterestRatePayouts (e.g. an IRS).
        //
        var contractualProduct = ContractualProduct.builder()
                .setEconomicTerms(EconomicTerms.builder()
                        .setPayout(Payout.builder()
                                .addInterestRatePayout(getFixedRatePayout(BigDecimal.valueOf(0.05)))
                                .addInterestRatePayout(getFloatingRatePayout())))
                .build();



        // Extract the list of qualification function applicable to the EconomicTerms object
        //
        var qualifyFunctions = new EconomicTermsMeta().getQualifyFunctions(qualifyFunctionFactory);

        // Use the QualifyResultsExtractor helper to easily make use of qualification results
        //
        var qualificationResult = new QualifyResultsExtractor<>(qualifyFunctions, contractualProduct.getEconomicTerms())
                .getOnlySuccessResult()
                .map(QualifyResult::getName)
                .orElse("Failed to qualify");

        System.out.println(qualificationResult);
        assertThat(qualificationResult, is("InterestRate_IRSwap_FixedFloat"));


        // Stamp the qualification value in the correct location using the qualification handler
        //
        EconomicTermsQualificationHandler qualificationHandler = new EconomicTermsQualificationHandler();
        var contractualProductBuilder = contractualProduct.toBuilder();
        qualificationHandler.setQualifier(contractualProductBuilder, qualificationResult);
        assertThat(qualificationHandler.getQualifier(contractualProductBuilder), is("InterestRate_IRSwap_FixedFloat"));
    }

    public static void main(String[] args) {
        new Qualification().run();
    }
}
