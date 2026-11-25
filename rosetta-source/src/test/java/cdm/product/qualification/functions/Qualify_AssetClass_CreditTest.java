package cdm.product.qualification.functions;

import cdm.base.staticdata.asset.common.AssetClassEnum;
import cdm.base.staticdata.asset.common.Instrument;
import cdm.base.staticdata.asset.common.Loan;
import cdm.base.staticdata.asset.common.Security;
import cdm.base.staticdata.asset.common.SecurityTypeEnum;
import cdm.base.staticdata.asset.common.Asset;
import cdm.event.common.TradeState;
import cdm.observable.asset.CreditIndex;
import cdm.observable.asset.Index;
import cdm.observable.asset.Observable;
import cdm.product.asset.InterestRatePayout;
import cdm.product.template.EconomicTerms;
import cdm.product.template.Payout;
import cdm.product.template.PerformancePayout;
import cdm.product.template.Underlier;
import javax.inject.Inject;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;
import org.finos.cdm.util.ResourcesUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Qualify_AssetClass_CreditTest extends AbstractFunctionTest {

    @Inject
    Qualify_AssetClass_Credit qualifyAssetClassCredit;

    @Test
    void shouldQualifyAsAssetClassCredit() throws IOException {
        EconomicTerms economicTerms = getEconomicTerms("ingest/output/fpml-confirmation-to-trade-state/fpml-5-13-products-credit-derivatives/cd-ex01-long-asia-corp-fixreg.json");

        Boolean result = qualifyAssetClassCredit.evaluate(economicTerms);

        assertTrue(result);
    }

    @Test
    void shouldNotQualifyAsAssetClassCredit() throws IOException {
        EconomicTerms economicTerms = getEconomicTerms("ingest/output/fpml-confirmation-to-trade-state/fpml-5-13-products-fx-derivatives/fx-ex08-fx-swap.json");

        Boolean result = qualifyAssetClassCredit.evaluate(economicTerms);

        assertFalse(result);
    }

    @Test
    void shouldQualifyCreditTotalReturnSwapWithLoanUnderlier() {
        Underlier loanUnderlier = Underlier.builder()
                .setObservableValue(Observable.builder()
                        .setAsset(Asset.builder()
                                .setInstrument(Instrument.builder()
                                        .setLoan(Loan.builder().build()))))
                .build();

        EconomicTerms economicTerms = buildPerformancePlusInterestRateEconomicTerms(loanUnderlier);

        assertTrue(qualifyAssetClassCredit.evaluate(economicTerms));
    }

    @Test
    void shouldQualifyCreditTotalReturnSwapWithDebtSecurityUnderlier() {
        Underlier debtSecurityUnderlier = Underlier.builder()
                .setObservableValue(Observable.builder()
                        .setAsset(Asset.builder()
                                .setInstrument(Instrument.builder()
                                        .setSecurity(Security.builder()
                                                .setSecurityType(SecurityTypeEnum.DEBT)))))
                .build();

        EconomicTerms economicTerms = buildPerformancePlusInterestRateEconomicTerms(debtSecurityUnderlier);

        assertTrue(qualifyAssetClassCredit.evaluate(economicTerms));
    }

    @Test
    void shouldQualifyCreditTotalReturnSwapWithCreditIndexUnderlier() {
        Underlier creditIndexUnderlier = Underlier.builder()
                .setObservableValue(Observable.builder()
                        .setIndex(Index.builder()
                                .setCreditIndex(CreditIndex.builder()
                                        .setAssetClass(AssetClassEnum.CREDIT))))
                .build();

        EconomicTerms economicTerms = buildPerformancePlusInterestRateEconomicTerms(creditIndexUnderlier);

        assertTrue(qualifyAssetClassCredit.evaluate(economicTerms));
    }

    private static EconomicTerms buildPerformancePlusInterestRateEconomicTerms(Underlier performanceUnderlier) {
        return EconomicTerms.builder()
                .addPayout(Payout.builder()
                        .setPerformancePayout(PerformancePayout.builder()
                                .setUnderlier(performanceUnderlier)))
                .addPayout(Payout.builder()
                        .setInterestRatePayout(InterestRatePayout.builder()))
                .build();
    }

    private static EconomicTerms getEconomicTerms(String resourceName) throws IOException {
        TradeState tradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, resourceName);
        return tradeState.getTrade().getProduct().getEconomicTerms();
    }
}