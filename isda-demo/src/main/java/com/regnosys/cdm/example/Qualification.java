package com.regnosys.cdm.example;

import cdm.product.common.ProductIdentification;
import cdm.product.template.ContractualProduct;
import cdm.product.template.EconomicTerms;
import cdm.product.template.Payout;
import cdm.product.template.meta.EconomicTermsMeta;
import com.google.inject.Inject;
import com.rosetta.model.lib.qualify.QualifyFunctionFactory;
import com.rosetta.model.lib.qualify.QualifyResult;
import com.rosetta.model.lib.qualify.QualifyResultsExtractor;

import java.math.BigDecimal;

import static com.regnosys.cdm.example.InterestRatePayoutCreation.getFixedRatePayout;
import static com.regnosys.cdm.example.InterestRatePayoutCreation.getFloatingRatePayout;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Qualification extends AbstractExample {

    @Inject
    private QualifyFunctionFactory.Default qualifyFunctionFactory;

    @Override
    public void example() {
        // Build an EconomicTerms object using two InterestRatePayouts
        //
        var economicTerms = EconomicTerms.builder()
                .setPayout(Payout.builder()
                        .addInterestRatePayout(getFixedRatePayout(BigDecimal.valueOf(0.05)))
                        .addInterestRatePayout(getFloatingRatePayout()))
                .build();


        // Extract the list of qualification function applicable to the EconomicTerms object
        //
        var qualifyFunctions = new EconomicTermsMeta().getQualifyFunctions(qualifyFunctionFactory);


        // Use the QualifyResultsExtractor helper to easily make use of qualification results
        //
        var qualificationResult = new QualifyResultsExtractor<>(qualifyFunctions, economicTerms)
                .getOnlySuccessResult()
                .map(QualifyResult::getName)
                .orElse("Failed to qualify");

        System.out.println(qualificationResult);
        assertThat(qualificationResult, is("InterestRate_IRSwap_FixedFloat"));

        // Stamp the qualification value in the correct location
        //
        ContractualProduct contractualProduct = ContractualProduct.builder()
                .setProductIdentification(ProductIdentification.builder()
                        .setProductQualifier(qualificationResult))
                .setEconomicTerms(economicTerms)
                .build();

        assertThat(contractualProduct.getProductIdentification().getProductQualifier(), is("InterestRate_IRSwap_FixedFloat"));
    }

    public static void main(String[] args) {
        new Qualification().run();
    }
}
