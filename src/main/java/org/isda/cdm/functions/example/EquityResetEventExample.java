package org.isda.cdm.functions.example;

import com.google.common.collect.ClassToInstanceMap;
import com.rosetta.model.lib.functions.RosettaFunction;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.*;
import org.isda.cdm.calculation.EquityCashSettlementAmount;
import org.isda.cdm.calculation.EquityNotionalAmount;
import org.isda.cdm.calculation.RateOfReturn;
import org.isda.cdm.functions.CalculationPeriod;
import org.isda.cdm.functions.*;
import org.isda.cdm.functions.example.services.identification.IdentifierService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.google.common.collect.Iterables.getOnlyElement;

public class EquityResetEventExample extends EquityResetEvent {

    private final IdentifierService identifierService;
    private final LocalDate businessDate;

    EquityResetEventExample(ClassToInstanceMap<RosettaFunction> classRegistry, IdentifierService identifierService, LocalDate businessDate) {
        super(classRegistry);
        this.identifierService = identifierService;
        this.businessDate = businessDate;
    }

    @Override
    protected Event doEvaluate(Contract contract, Event observation) {

        EquityPayout equityPayout = getOnlyElement(contract.getContractualProduct().getEconomicTerms().getPayout().getEquityPayout());

        CalculationPeriod calculationPeriod = new CalculationPeriod() {
            @Override
            public CalculationResult execute(CalculationPeriodDates calculationPeriodDates) {
                LocalDate startDate = calculationPeriodDates.getEffectiveDate().getAdjustableDate().getUnadjustedDate();


                return new CalculationResult()
                        .setStartDate(new DateImpl(calculationPeriodDates.getEffectiveDate().getAdjustableDate().getUnadjustedDate()));
            }
        };

        RateOfReturn rateOfReturnCalc = new RateOfReturn(calculationPeriod,
                (equityValuation, date) -> new ResolvePrice.CalculationResult().setPrice(new BigDecimal("0.1")));

        EquityNotionalAmount equityNotionalAmountCalc = new EquityNotionalAmount(calculationPeriod,
                _equityPayout -> new ResolveInitialPrice.CalculationResult().setPrice(new BigDecimal("100.00")));

        EquityCashSettlementAmount settlementAmount = new EquityCashSettlementAmount(
                new AbsImpl(),
                (_equityPayout) -> new ResolveRateOfReturn.CalculationResult().setRate(rateOfReturnCalc.calculate(_equityPayout).getRateOfReturn()),
                (_equityPayout) -> new ResolveNotionalAmount.CalculationResult().setNotional(equityNotionalAmountCalc.calculate(_equityPayout).getEquityNotionalAmount())
        );

        BigDecimal equityCashSettlementAmount = settlementAmount.calculate(equityPayout).getEquityCashSettlementAmount();

        Identifier id = identifierService.nextVersion(contract.getContractIdentifier().get(0).getIssuer().getValue(), Event.class.getSimpleName());

//        EquityReset equityReset = classRegistry.getInstance(EquityReset.class);
//        equityReset.evaluate()

        return Event.builder()
                .addEventIdentifier(id)
                .setPrimitiveBuilder(PrimitiveEvent.builder()
                    .addResetBuilder(ResetPrimitive.builder()
                        .setResetValue(equityCashSettlementAmount)
                        .setDate(businessDate)))
                .build();
    }
}
