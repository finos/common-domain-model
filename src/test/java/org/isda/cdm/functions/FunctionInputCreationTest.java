package org.isda.cdm.functions;

import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.Quantity;
import cdm.base.math.QuantityChangeDirectionEnum;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.PartyRole;
import cdm.base.staticdata.party.PartyRoleEnum;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.*;
import cdm.event.workflow.WorkflowStep;
import cdm.product.asset.InterestRatePayout;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.settlement.PriceQuantity;
import cdm.product.template.Payout;
import cdm.product.template.TradeLot;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.records.DateImpl;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import org.isda.cdm.functions.testing.FunctionUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import util.ResourcesUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionInputCreationTest {

    private static final ObjectMapper STRICT_MAPPER = RosettaObjectMapper.getNewRosettaObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
            .setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));

    @Test
    void validateCreateTerminationWorkflowFuncInputJson() throws IOException {
        RunCreateTerminationWorkflowInput actual = new RunCreateTerminationWorkflowInput(
                createInterestRateSwapTerminationTradeState(),
                TerminationInstruction.builder()
                        .addTerminatedPriceQuantity(PriceQuantity.builder()
                                .addQuantity(FieldWithMetaQuantity.builder()
                                        .setValue(Quantity.builder()
                                                .setAmount(BigDecimal.valueOf(10000))
                                                .setUnitOfAmount(UnitType.builder().setCurrency(FieldWithMetaString.builder()
                                                        .setValue("USD")
                                                        .setMeta(MetaFields.builder()
                                                                .setScheme("http://www.fpml.org/coding-scheme/external/iso4217")))))))
                        .setTerminationDate(DateImpl.of(2019, 12, 12))
                        .build());

        assertEquals(readResource("/cdm-sample-files/functions/termination-workflow-func-input.json"),
                STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(actual),
                "The input JSON for termination-workflow-func-input.json has been updated (probably due to a model change). Update the input file");
    }

    @Test
    void validateCreatePartialTerminationWorkflowFuncInputJson() throws IOException {
        RunCreateTerminationWorkflowInput actual = new RunCreateTerminationWorkflowInput(
                createInterestRateSwapTerminationTradeState(),
                TerminationInstruction.builder()
                        .addTerminatedPriceQuantity(PriceQuantity.builder()
                                .addQuantity(FieldWithMetaQuantity.builder()
                                        .setValue(Quantity.builder()
                                                .setAmount(BigDecimal.valueOf(7000))
                                                .setUnitOfAmount(UnitType.builder().setCurrency(FieldWithMetaString.builder()
                                                        .setValue("USD")
                                                        .setMeta(MetaFields.builder()
                                                                .setScheme("http://www.fpml.org/coding-scheme/external/iso4217")))))))
                        .setTerminationDate(DateImpl.of(2019, 12, 12))
                        .build());

        assertEquals(readResource("/cdm-sample-files/functions/partial-termination-workflow-func-input.json"),
                STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(actual),
                "The input JSON for partial-termination-workflow-func-input.json has been updated (probably due to a model change). Update the input file");
    }

    @Test
    void validateCreateFullTerminationEquitySwapFuncInputJson() throws IOException {
        RunCreateBusinessEventInput actual = new RunCreateBusinessEventInput(
                getFullTerminationEquitySwapInstruction(),
                InstructionFunctionEnum.QUANTITY_CHANGE,
                new DateImpl(11, 11, 2021)
        );

        assertEquals(readResource("/cdm-sample-files/functions/full-termination-equity-swap-func-input.json"),
                STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(actual),
                "The input JSON for full-termination-equity-swap-func-input.json has been updated (probably due to a model change). Update the input file");
    }

    private List<? extends Instruction> getFullTerminationEquitySwapInstruction() throws IOException {
        Instruction.InstructionBuilder instructionBuilder = Instruction.builder();

        QuantityChangeInstruction.QuantityChangeInstructionBuilder quantityChangeBuilder =
                instructionBuilder.getOrCreatePrimitiveInstruction(0)
                .getOrCreateQuantityChange();

        quantityChangeBuilder.setDirection(QuantityChangeDirectionEnum.DECREASE);

        PriceQuantity.PriceQuantityBuilder changeBuilder = quantityChangeBuilder
                .getOrCreateChange(0);

        changeBuilder.getOrCreateQuantity(0)
                .setValue(Quantity.builder()
                        .setAmount(BigDecimal.valueOf(760400))
                        .setUnitOfAmount(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE).build())
                        .build());

        changeBuilder.getOrCreateQuantity(1)
                .setValue(Quantity.builder()
                        .setAmount(BigDecimal.valueOf(28469376))
                        .setUnitOfAmount(UnitType.builder().setCurrencyValue("USD").build())
                        .build());

        instructionBuilder
                .setBefore(createEquitySwapTerminationTradeState().build());

        return Lists.newArrayList(instructionBuilder.build());
    }

    private TradeState.TradeStateBuilder createEquitySwapTerminationTradeState() throws IOException {
        TradeState.TradeStateBuilder tradeStateBuilder = createGenericSwapTerminationTradeState();

        Payout.PayoutBuilder payout = tradeStateBuilder.getTrade()
                .getTradableProduct()
                .getProduct()
                .getContractualProduct()
                .getEconomicTerms()
                .getPayout();

        payout.getInterestRatePayout()
                .stream()
                .filter(irPayout -> irPayout.getRateSpecification().getFloatingRate() != null)
                .findFirst()
                .ifPresent(floatingLeg -> {
                    CalculationPeriodDates.CalculationPeriodDatesBuilder calculationPeriodDates = floatingLeg.getCalculationPeriodDates();
                    calculationPeriodDates.getEffectiveDate().getAdjustableDate().setUnadjustedDate(DateImpl.of(2014, 4, 3));
                    calculationPeriodDates.getTerminationDate().getAdjustableDate().setUnadjustedDate(DateImpl.of(2025, 4, 1));
                });

        //TODO: make an irs payout and aan equity payout

        return tradeStateBuilder;
    }

    @Test
    void validateCreateAllocationWorkflowInputJason() throws IOException {
        TradeState.TradeStateBuilder tradeStateBuilder = createInterestRateSwapTerminationTradeState();

        List<? extends InterestRatePayout.InterestRatePayoutBuilder> interestRatePayoutBuilders = tradeStateBuilder
                .getTrade()
                .getTradableProduct()
                .getProduct()
                .getContractualProduct()
                .getEconomicTerms()
                .getPayout()
                .getInterestRatePayout();

        interestRatePayoutBuilders.get(0)
                .getCalculationPeriodDates()
                .getTerminationDate()
                .getAdjustableDate()
                .setUnadjustedDate(DateImpl.of(2028, 4, 1));

        interestRatePayoutBuilders.get(1)
                .getCalculationPeriodDates()
                .getTerminationDate()
                .getAdjustableDate()
                .setUnadjustedDate(DateImpl.of(2028, 4, 1));

        tradeStateBuilder
                .getTrade()
                .getParty().get(0)
                .getPartyId().get(0)
                .setValue("LEI1RPT001");

        tradeStateBuilder
                .getTrade()
                .getParty().get(1)
                .getPartyId().get(0)
                .setValue("LEIFUNDMGR");

        tradeStateBuilder
                .getTrade()
                .getTradeIdentifier().get(0)
                .getAssignedIdentifier().get(0)
                .getIdentifier()
                .setValue("LEI1RPT001PREAA");

        ExecutionInstruction executionInstruction = FunctionUtils.createExecutionInstructionFromTradeState(tradeStateBuilder.build());

        assertEquals(readResource("/cdm-sample-files/functions/allocation-workflow-func-input.json"),
                STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(executionInstruction),
                "The input JSON for allocation-workflow-func-input.json has been updated (probably due to a model change). Update the input file");
    }


    /**
     * Use record-ex01-vanilla-swap.json sample and modify it to look exactly like CFTC example 3 (used in regs termination example)
     */
    @NotNull
    private TradeState.TradeStateBuilder createInterestRateSwapTerminationTradeState() throws IOException {
        TradeState.TradeStateBuilder tradeStateBuilder = createGenericSwapTerminationTradeState();
        // effective and termination date
        List<? extends InterestRatePayout.InterestRatePayoutBuilder> interestRatePayouts = tradeStateBuilder.getTrade()
                .getTradableProduct()
                .getProduct()
                .getContractualProduct()
                .getEconomicTerms()
                .getPayout()
                .getInterestRatePayout();
        interestRatePayouts.stream()
                .filter(payout -> payout.getRateSpecification().getFloatingRate() != null)
                .findFirst()
                .ifPresent(floatingLeg -> {
                    CalculationPeriodDates.CalculationPeriodDatesBuilder calculationPeriodDates = floatingLeg.getCalculationPeriodDates();
                    calculationPeriodDates.getEffectiveDate().getAdjustableDate().setUnadjustedDate(DateImpl.of(2014,4, 3));
                    calculationPeriodDates.getTerminationDate().getAdjustableDate().setUnadjustedDate(DateImpl.of(2025,4, 1));
                });
        interestRatePayouts.stream()
                .filter(payout -> payout.getRateSpecification().getFixedRate() != null)
                .findFirst()
                .ifPresent(fixedLeg -> {
                    CalculationPeriodDates.CalculationPeriodDatesBuilder calculationPeriodDates = fixedLeg.getCalculationPeriodDates();
                    calculationPeriodDates.getEffectiveDate().getAdjustableDate().setUnadjustedDate(DateImpl.of(2018,4, 3));
                    calculationPeriodDates.getTerminationDate().getAdjustableDate().setUnadjustedDate(DateImpl.of(2025,4, 1));
                });

        return tradeStateBuilder;
    }

    @NotNull
    private TradeState.TradeStateBuilder createGenericSwapTerminationTradeState() throws IOException {
        WorkflowStep workflowStep = ResourcesUtils.getObject(WorkflowStep.class, "result-json-files/fpml-5-10/record-keeping/record-ex01-vanilla-swap.json");
        // parties
        List<Party> parties = workflowStep.getParty().stream()
                .filter(p -> p.getName().getValue().equals("Bank X") || p.getName().getValue().equals("Bank Y"))
                .collect(Collectors.toList());
        TradeState tradeState = workflowStep.getBusinessEvent().getPrimitives().get(0).getContractFormation().getAfter();
        TradeState.TradeStateBuilder tradeStateBuilder = tradeState.toBuilder();
        tradeStateBuilder.getTrade().setParty(parties);
        // partyId
        updatePartyId(tradeStateBuilder, "Bank X", "LEI1RPT0001");
        updatePartyId(tradeStateBuilder, "Bank Y", "LEI2CP0002");
        // party role
        List<? extends PartyRole> partyRoles = tradeState.getTrade().getPartyRole();
        PartyRole partyRole = partyRoles.get(0);
        PartyRole.PartyRoleBuilder reportingPartyRole = partyRole.toBuilder()
                .setPartyReference(ReferenceWithMetaParty.builder()
                        .setGlobalReference(partyRole.getOwnershipPartyReference().getGlobalReference())
                        .setExternalReference(partyRole.getOwnershipPartyReference().getExternalReference()))
                .setRole(PartyRoleEnum.REPORTING_PARTY);
        tradeStateBuilder.getTrade().addPartyRole(reportingPartyRole);

        // quantity
        tradeStateBuilder.getTrade().getTradableProduct().getTradeLot().stream().map(TradeLot.TradeLotBuilder::getPriceQuantity).flatMap(Collection::stream).map(
                PriceQuantity.PriceQuantityBuilder::getQuantity).flatMap(Collection::stream).map(FieldWithMetaQuantity.FieldWithMetaQuantityBuilder::getValue).forEach(quantity -> {
            quantity.setAmount(new BigDecimal(10000));
        });
        // trade id
        tradeStateBuilder.getTrade().getTradeIdentifier().get(0).getAssignedIdentifier().get(0).setIdentifierValue("LEI1RPT0001KKKK");
        // trade date
        tradeStateBuilder.getTrade().setTradeDateValue(DateImpl.of(2018, 4, 1));

        return tradeStateBuilder;
    }

    private void updatePartyId(TradeState.TradeStateBuilder tradeStateBuilder, String partyName, String partyId) {
        tradeStateBuilder.getTrade().getParty().stream()
                .filter(p -> p.getName().getValue().equals(partyName))
                .findFirst().ifPresent(bankXParty -> bankXParty.setPartyId(Arrays.asList(FieldWithMetaString.builder()
                        .setValue(partyId)
                        .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/iso17442")))));
    }

    private static String readResource(String inputJson) throws IOException {
        //noinspection UnstableApiUsage
        return Resources
                .toString(Objects
                        .requireNonNull(FunctionInputCreationTest.class.getResource(inputJson)), Charset
                        .defaultCharset());
    }
}
