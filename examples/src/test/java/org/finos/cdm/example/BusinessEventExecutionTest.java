package org.finos.cdm.example;

import cdm.base.datetime.AdjustableOrAdjustedDate;
import cdm.base.datetime.BusinessCenterTime;
import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.QuantityChangeDirectionEnum;
import cdm.base.math.UnitType;
import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.*;
import cdm.event.workflow.EventInstruction;
import cdm.event.workflow.EventTimestamp;
import cdm.event.workflow.EventTimestampQualificationEnum;
import cdm.event.workflow.WorkflowStep;
import cdm.event.workflow.functions.Create_AcceptedWorkflowStepFromInstruction;
import cdm.legaldocumentation.common.AgreementName;
import cdm.legaldocumentation.common.LegalAgreement;
import cdm.legaldocumentation.common.LegalAgreementIdentification;
import cdm.legaldocumentation.common.LegalAgreementTypeEnum;
import cdm.legaldocumentation.master.MasterAgreementTypeEnum;
import cdm.legaldocumentation.master.metafields.FieldWithMetaMasterAgreementTypeEnum;
import cdm.observable.asset.PriceQuantity;
import cdm.product.asset.InterestRatePayout;
import cdm.product.asset.RateSpecification;
import cdm.product.common.NotionalAdjustmentEnum;
import cdm.product.template.NonTransferableProduct;
import cdm.product.template.OptionPayout;
import cdm.product.template.Payout;
import cdm.product.template.TradeLot;
import cdm.product.template.metafields.ReferenceWithMetaOptionPayout;
import cdm.product.template.metafields.ReferenceWithMetaPayout;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaDate;
import com.rosetta.model.metafields.MetaFields;
import org.finos.cdm.example.util.ResourcesUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CDMBusinessEventTestExample class provides example tests for various business events
 * related to contract management and financial transactions. This class demonstrates
 * the creation and validation of different primitive instructions, such as Contract Formation,
 * Stock Splits, and other legal and financial event instructions. Each method builds a
 * specific type of business event, ensuring that the system behaves as expected for various scenarios.
 *
 * The tests also include various constructs for instructions for trades
 * and events such as adjustments, splits, and valuations. This class serves as both
 * a documentation tool and a practical set of unit tests for ensuring correctness in business logic.
 *
 * It includes example methods for:
 * - Creating instructions for trade-related events.
 * - Verifying that trade instructions are properly processed and validated.
 */
public class BusinessEventExecutionTest extends AbstractExampleTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessEventExecutionTest.class);

    private static final ObjectMapper mapper = RosettaObjectMapper.getNewRosettaObjectMapper();

    private static final Date event_date = Date.parse("2025-01-10");
    private static final LocalTime event_time = LocalTime.of(9, 0);

    @Inject
    Create_AcceptedWorkflowStepFromInstruction createWorkflowStepFunc;

    /**
     * Contract Formation
     * The contract formation primitive function represents the transition of the trade state to a legally binding legal agreement after the trade is confirmed.
     */
    @Test
    public void mustCreateContractFormationBusinessEventAcceptedWorkflowStep() {

        // Trade to be included in contract formation.
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-rates/ird-ex01-vanilla-swap-versioned.json"); //processes/msg-new-trade-CFTC-SEC-and-canada.json
        assertNotNull(beforeTradeState, "before TradeState must not be null");

        // Creation of the contract formation instruction containing a legal agreement
        ContractFormationInstruction contractFormationInstruction = buildContractFormationPrimitiveInstruction(buildISDAMasterAgreementLegalAgreement());

        // Adding the contract formation instruction to the primitive instruction that will be included in the resulting workflow step
        PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder().setContractFormation(contractFormationInstruction).build();

        // Creation of the workflow step of a contract formation event
        mustCreateAcceptedWorkflowStepAsExpected(primitiveInstruction, beforeTradeState, EventIntentEnum.CONTRACT_FORMATION);
    }

    /**
     * Execution
     * This test verifies that a new `WorkflowStep` for an execution event is correctly created
     * when a transaction is executed. The test ensures that the resulting `WorkflowStep` contains
     * the appropriate `ExecutionInstruction` and checks the validity of its components.
     * Notice that no before trade is required as input.
     */
    @Test
    void mustCreateExecutionBusinessEventAcceptedWorkflowStep() {

        // Load a sample TradeState from a JSON file and resolve its references.
        // We will use this deserialized tradeState to extract the information of a trade in order to create the execution instruction more easily.
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-rates/ird-ex01-vanilla-swap-versioned.json");
        assertNotNull(beforeTradeState, "before TradeState must not be null");

        // Extract the product from the loaded TradeState. This represents the financial product being traded.
        NonTransferableProduct product = beforeTradeState.getTrade().getProduct();

        // Extract the list of trade lots from the loaded TradeState.
        List<? extends TradeLot> tradeLot = beforeTradeState.getTrade().getTradeLot();

        // Initialize an empty list for PriceQuantity (used later if trade lots are present).
        List<PriceQuantity> pricequantity = List.of();

        // If trade lots exist, extract and flatten their associated PriceQuantity lists.
        if (beforeTradeState.getTrade().getTradeLot() != null) {
            pricequantity = beforeTradeState.getTrade()
                    .getTradeLot() // Get the list of TradeLots.
                    .stream() // Create a stream to process the list.
                    .map(TradeLot::getPriceQuantity) // Extract PriceQuantity from each TradeLot.
                    .flatMap(List::stream) // Flatten the nested lists of PriceQuantity into a single stream.
                    .collect(Collectors.toList()); // Collect into a single list.
        }

        // Extract the list of counterparties from the loaded TradeState.
        List<? extends Counterparty> counterparty = beforeTradeState.getTrade().getCounterparty();

        // Extract the list of parties involved in the trade.
        List<? extends Party> party = beforeTradeState.getTrade().getParty();

        // Extract the trade date from the TradeState.
        FieldWithMetaDate date = beforeTradeState.getTrade().getTradeDate();

        // Extract trade identifiers from the TradeState (e.g., unique identifiers for the trade).
        List<? extends TradeIdentifier> tradeIdentifier = beforeTradeState.getTrade().getTradeIdentifier();

        // Create an ExecutionInstruction object using the extracted components of the TradeState.
        ExecutionInstruction executionInstruction = buildExecutionPrimitiveInstruction(product, tradeLot, pricequantity, counterparty, party, date, tradeIdentifier);

        // Wrap the ExecutionInstruction in a PrimitiveInstruction, which will later be used to build the WorkflowStep.
        PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder()
                .setExecution(executionInstruction)
                .build();

        // Create a WorkflowStep for the execution event. Since this is an execution, there is no "before" trade state.
        WorkflowStep ws = mustCreateAcceptedWorkflowStepAsExpected(primitiveInstruction, null, null);

        assertNotNull(ws.getBusinessEvent().getInstruction().get(0).getPrimitiveInstruction().getExecution(), "The resulting workflow step must contain the Execution instruction in the business event"
        );
    }


    /**
     * Novation
     * A full novation occurs when an entire trade or contract is transferred from one counterparty to another.
     * The original counterparty is fully replaced, and the new counterparty assumes all rights and obligations
     * of the trade.
     */
    @Test
    void mustCreateFullNovationBusinessEventAcceptedWorkflowStep() {

        // Trade to be included in Novation.
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-rates/EUR-Vanilla-party-roles-versioned.json");
        assertNotNull(beforeTradeState, "before TradeState must not be null");
        //beforeTradeState = beforeTradeState.toBuilder().setState(State.builder().setClosedState(ClosedState.builder().build()));

        //New party to replace in the first after trade for the party1
        Party party3 = beforeTradeState.getTrade().getParty().get(2);

        // Creation of the partyChange instruction that will be applied to the first after trade
        PartyChangeInstruction partyChangeInstruction = buildPartyChangePrimitiveInstruction(beforeTradeState, Counterparty.builder().setPartyReference(ReferenceWithMetaParty.builder().setValue(party3).build()).setRole(CounterpartyRoleEnum.PARTY_1).build());

        // Creation of the quantityChange instruction that will be applied to the second after trade
        QuantityChangeInstruction quantityChangeInstruction = buildQuantityChangePrimitiveInstruction(beforeTradeState, PriceQuantity.builder().setQuantityValue(List.of(NonNegativeQuantitySchedule.builder().setUnit(UnitType.builder().setCurrencyValue("EUR").build()).setValue(BigDecimal.ZERO).build())).build(), QuantityChangeDirectionEnum.INCREASE);

        // Creation of the novation instruction through a split instruction
        SplitInstruction splitInstruction = buildNovationPrimitiveInstruction( PrimitiveInstruction.builder().setPartyChange(partyChangeInstruction).build(), PrimitiveInstruction.builder().setQuantityChange(quantityChangeInstruction).build());

        // Adding the novation instruction to the primitive instruction that will be included in the resulting workflow step
        PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder().setSplit(splitInstruction).build();

        // Creation of the workflow step of a novation event
        WorkflowStep ws = mustCreateAcceptedWorkflowStepAsExpected(primitiveInstruction, beforeTradeState, EventIntentEnum.NOVATION);

        assertNotEquals(ws.getBusinessEvent().getAfter().get(0).getTrade().getCounterparty().get(0), beforeTradeState.getTrade().getCounterparty().get(0),"The counterparty with role party1 should be different than the one on the before tradeState.");
        assertEquals(ws.getBusinessEvent().getAfter().get(0).getTrade().getCounterparty().get(1), beforeTradeState.getTrade().getCounterparty().get(1), "The counterparty with role party2 should be the same as the one on the before tradeState.");
        assertEquals(BigDecimal.ZERO, ws.getBusinessEvent().getAfter().get(1).getTrade().getTradeLot().get(1).getPriceQuantity().get(0).getQuantity().get(0).getValue().getValue(), "The quantity of the trade with the same counterparties as the original must be 0.");

        //TODO: ClosedState not modeled. Expected ClosedStateEnum.NOVATED
    }

    /**
     * Partial Novation
     * A partial novation happens when only part of the trade's notional or obligations is transferred to a new
     * counterparty. The original counterparty remains in the trade but shares it with the new counterparty.
     */
    @Test
    void mustCreatePartialNovationBusinessEventAcceptedWorkflowStep() {

        // Trade to be included in Novation.
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-rates/EUR-Vanilla-party-roles-versioned.json");
        assertNotNull(beforeTradeState, "before TradeState must not be null");

        //New party to replace in the first after trade for the party1
        Party party3 = beforeTradeState.getTrade().getParty().get(2);

        // Creation of the partyChange instruction that will be applied to the first after trade
        PartyChangeInstruction partyChangeInstruction = buildPartyChangePrimitiveInstruction(beforeTradeState, Counterparty.builder().setPartyReference(ReferenceWithMetaParty.builder().setValue(party3).build()).setRole(CounterpartyRoleEnum.PARTY_1).build());

        // Creation of the quantityChange instruction that will be applied to the first after trade
        QuantityChangeInstruction quantityChangeInstruction = buildQuantityChangePrimitiveInstruction(beforeTradeState, PriceQuantity.builder().addQuantityValue(List.of(NonNegativeQuantitySchedule.builder().setUnit(UnitType.builder().setCurrencyValue("EUR").build()).setValue(beforeTradeState.getTrade().getTradeLot().get(0).getPriceQuantity().get(0).getQuantity().get(0).getValue().getValue().subtract(BigDecimal.valueOf(4000000))).build())).build(), QuantityChangeDirectionEnum.INCREASE);
        // Creation of the quantityChange instruction that will be applied to the second after trade
        QuantityChangeInstruction quantityChangeInstruction2 = buildQuantityChangePrimitiveInstruction(beforeTradeState, PriceQuantity.builder().addQuantityValue(List.of(NonNegativeQuantitySchedule.builder().setUnit(UnitType.builder().setCurrencyValue("EUR").build()).setValue(beforeTradeState.getTrade().getTradeLot().get(0).getPriceQuantity().get(1).getQuantity().get(0).getValue().getValue().subtract(BigDecimal.valueOf(6000000))).build())).build(), QuantityChangeDirectionEnum.INCREASE);

        // Creation of the novation instruction through a split instruction
        SplitInstruction splitInstruction = buildNovationPrimitiveInstruction( PrimitiveInstruction.builder().setPartyChange(partyChangeInstruction).setQuantityChange(quantityChangeInstruction).build(), PrimitiveInstruction.builder().setQuantityChange(quantityChangeInstruction2).build());

        // Adding the novation instruction to the primitive instruction that will be included in the resulting workflow step
        PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder().setSplit(splitInstruction).build();

        // Creation of the workflow step of a novation event
        WorkflowStep ws = mustCreateAcceptedWorkflowStepAsExpected(primitiveInstruction, beforeTradeState, EventIntentEnum.NOVATION);

        assertNotEquals(ws.getBusinessEvent().getAfter().get(0).getTrade().getCounterparty().get(0), beforeTradeState.getTrade().getCounterparty().get(0),"The counterparty with role party1 should be different than the one on the before tradeState.");
        assertEquals(ws.getBusinessEvent().getAfter().get(0).getTrade().getCounterparty().get(1), beforeTradeState.getTrade().getCounterparty().get(1), "The counterparty with role party2 should be the same as the one on the before tradeState.");
        assertEquals(beforeTradeState.getTrade().getTradeLot().get(0).getPriceQuantity().get(0).getQuantity().get(0).getValue().getValue(), ws.getBusinessEvent().getAfter().get(1).getTrade().getTradeLot().get(1).getPriceQuantity().get(0).getQuantity().get(0).getValue().getValue().add(ws.getBusinessEvent().getAfter().get(0).getTrade().getTradeLot().get(1).getPriceQuantity().get(0).getQuantity().get(0).getValue().getValue()), "The priceQuantity of the original trade must be equivalent to the sum of the quantities of the new trades.");

        //TODO: ClosedState not modeled. Expected ClosedStateEnum.NOVATED
    }

    /**
     * Increase
     * The intent is to Increase the quantity or notional of the contract.
     * @throws IOException
     */
    @Test
    void mustCreateIncreaseBusinessEventAcceptedWorkflowStep() {

        // Trade to be included in Increase.
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-rates/ird-ex01-vanilla-swap-versioned.json");
        assertNotNull(beforeTradeState,"before TradeState must not be null");

        // Price quantity change to be applied to the trade
        PriceQuantity change = PriceQuantity.builder().addQuantityValue(NonNegativeQuantitySchedule.builder().setUnit(UnitType.builder().setCurrencyValue("EUR").build()).setValue(beforeTradeState.getTrade().getTradeLot().get(0).getPriceQuantity().get(0).getQuantity().get(0).getValue().getValue().add(BigDecimal.valueOf(1000000))).build()).build();

        // Direction must be set to "INCREASE" in order to increase the quantity
        QuantityChangeDirectionEnum direction = QuantityChangeDirectionEnum.INCREASE;

        // Creation of the increase instruction through a quantity change instruction
        QuantityChangeInstruction increaseInstruction = buildQuantityChangePrimitiveInstruction (beforeTradeState, change, direction);

        // Adding the increase instruction to the primitive instruction that will be included in the resulting workflow step
        PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder().setQuantityChange(increaseInstruction).build();

        // Creation of the workflow step of an increase event
        WorkflowStep ws = mustCreateAcceptedWorkflowStepAsExpected(primitiveInstruction, beforeTradeState, null); //EventIntentEnum.INCREASE

        assertNull(ws.getBusinessEvent().getIntent(),"Workflow step intent must be null");
        assertEquals(1, ws.getBusinessEvent().getInstruction().size(), "The workflow step instruction contains only one instruction");
        assertNotNull(ws.getBusinessEvent().getInstruction().get(0).getPrimitiveInstruction().getQuantityChange(), "The workflow step instruction contains a quantity change");
        assertTrue( beforeTradeState.getTrade().getTradeLot().size() < ws.getBusinessEvent().getAfter().get(0).getTrade().getTradeLot().size(), "The before trade has less tradeLot than the after trade");
        assertTrue( beforeTradeState.getTrade().getTradeLot().get(0).getPriceQuantity().get(0).getQuantity().get(0).getValue().getValue().compareTo(ws.getBusinessEvent().getAfter().get(0).getTrade().getTradeLot().get(1).getPriceQuantity().get(0).getQuantity().get(0).getValue().getValue()) <  0, "The quantity of the before tradeState must be less than the quantity of the after tradeState.");
    }

    /**
     * Decrease
     * The intent is to Decrease the quantity or notional of the contract.
     */
    @Test
    void mustCreateDecreaseBusinessEventAcceptedWorkflowStep() {

        // Trade to be included in decrease.
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-rates/ird-ex01-vanilla-swap-versioned.json");
        assertNotNull(beforeTradeState, "before TradeState must not be null");

        // Price quantity change to be applied to the trade
        PriceQuantity change = PriceQuantity.builder().addQuantityValue(NonNegativeQuantitySchedule.builder().setUnit(UnitType.builder().setCurrencyValue("EUR").build()).setValue(BigDecimal.valueOf(4000000L)).build()).build();

        // Direction must be set to "DECREASE" in order to decrease the quantity
        QuantityChangeDirectionEnum direction = QuantityChangeDirectionEnum.DECREASE;

        // Creation of the decrease instruction through a quantity change instruction
        QuantityChangeInstruction decreaseInstruction = buildQuantityChangePrimitiveInstruction (beforeTradeState, change, direction);

        // Adding the decrease instruction to the primitive instruction that will be included in the resulting workflow step
        PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder().setQuantityChange(decreaseInstruction).build();

        // Creation of the workflow step of a decrease event
        WorkflowStep ws =  mustCreateAcceptedWorkflowStepAsExpected(primitiveInstruction, beforeTradeState, null);//EventIntentEnum.DECREASE

        assertNull(ws.getBusinessEvent().getIntent(), "Workflow step intent must be null");
        assertEquals(1, ws.getBusinessEvent().getInstruction().size(), "Workflow step must contain only one primitive instruction");
        assertNotNull(ws.getBusinessEvent().getInstruction().get(0).getPrimitiveInstruction().getQuantityChange(), "Workflow step must contain a quantity change instruction");

        //TODO: ClosedState not modeled
    }

    /**
     * Termination
     * The termination of a contract before its expiration, either by mutual agreement or by some specific condition.
     */
    @Test
    void mustCreateTerminationBusinessEventAcceptedWorkflowStep() {

        // Trade to be included in Full Termination.
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-rates/ird-ex01-vanilla-swap-versioned.json");
        assertNotNull(beforeTradeState, "before TradeState must not be null");

        // Price quantity change to be applied to the trade
        PriceQuantity change = PriceQuantity.builder().addQuantityValue(NonNegativeQuantitySchedule.builder().setUnit(UnitType.builder().setCurrencyValue("EUR").build()).setValue(BigDecimal.valueOf(0)).build()).build();

        // Direction must be set to "REPLACE" in order to replace the quantity with the new one
        QuantityChangeDirectionEnum direction = QuantityChangeDirectionEnum.REPLACE;

        // Creation of the termination instruction through a quantity change instruction
        QuantityChangeInstruction terminationInstruction = buildQuantityChangePrimitiveInstruction (beforeTradeState, change, direction);

        // Adding the termination instruction to the primitive instruction that will be included in the resulting workflow step
        PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder().setQuantityChange(terminationInstruction).build();

        // Creation of the workflow step of a termination event
        WorkflowStep ws =  mustCreateAcceptedWorkflowStepAsExpected(primitiveInstruction, beforeTradeState, null); // EventIntentEnum.EARLY_TERMINATION_PROVISION?

        assertNull(ws.getBusinessEvent().getIntent(), "Workflow step intent must be null");
        assertEquals(1, ws.getBusinessEvent().getInstruction().size(), "Workflow step must contain only one primitive instruction");
        assertNotNull(ws.getBusinessEvent().getInstruction().get(0).getPrimitiveInstruction().getQuantityChange(), "Workflow step must contain a quantity change instruction");

        //TODO: ClosedState not modeled. Expected ClosedStateEnum.TERMINATED
    }

    /**
     * Reset
     * Refers to the periodic adjustment of a floating reference rate (such as LIBOR or SOFR) to reflect the current prevailing rate.
     * It is commonly used in derivatives involving floating interest rates.
     * @throws IOException
     */
    @Test
    void mustCreateResetBusinessEventAcceptedWorkflowStep() {

        // Trade to be included in Reset.
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-rates/ird-ex01-vanilla-swap-versioned.json");
        assertNotNull(beforeTradeState, "before TradeState must not be null");

        // Payout to be applied to the trade.
        Payout payout = Payout.builder().setInterestRatePayout(InterestRatePayout.builder().setRateSpecification(RateSpecification.builder().build()).build()).build();
        ReferenceWithMetaPayout refPayout = ReferenceWithMetaPayout.builder().setValue(payout).build();

        // Creation of the reset instruction
        ResetInstruction resetInstruction = buildResetPrimitiveInstruction (refPayout, payout, event_date );

        // Adding the reset instruction to the primitive instruction that will be included in the resulting workflow step
        PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder().setReset(resetInstruction).build();

        // Creation of the workflow step of a reset event
        WorkflowStep ws = mustCreateAcceptedWorkflowStepAsExpected(primitiveInstruction, beforeTradeState, EventIntentEnum.NOTIONAL_RESET);

        assertEquals(1, ws.getBusinessEvent().getAfter().size(), "The business event of the resulting workflow step must contain only one after trade state");
        assertEquals(beforeTradeState.getTrade(), ws.getBusinessEvent().getAfter().get(0).getTrade(), "The before and the after trades must be the same");
        if(beforeTradeState.getResetHistory() != null )
            assertEquals(beforeTradeState.getResetHistory().size() + 1, ws.getBusinessEvent().getAfter().get(0).getResetHistory().size(), "Before reset history count + 1 must be equal to after reset history count");
        else assertEquals(1, ws.getBusinessEvent().getAfter().get(0).getResetHistory().size(), "Before reset history count + 1 must be equal to after reset history count");
    }

    /**
     * Valuation
     * This event represents the periodic calculation of the market value of a derivative contract, essential for margin and risk management.
     * The qualification of a valuation update from the fact that the only component is a valuation.
     */
    @Test
    void mustCreateValuationBusinessEventAcceptedWorkflowStep() {

        // Trade to be included in Valuation.
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-rates/ird-ex01-vanilla-swap-versioned.json");
        assertNotNull(beforeTradeState, "before TradeState must not be null");

        // Valuation to be applied to the trade
        Valuation valuation = new Valuation.ValuationBuilderImpl();

        // Creation of the valuation instruction
        ValuationInstruction valuationInstruction = buildValuationPrimitiveInstruction (valuation);

        // Adding the valuation instruction to the primitive instruction that will be included in the resulting workflow step
        PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder().setValuation(valuationInstruction).build();

        // Creation of the workflow step of a valuation event
        WorkflowStep ws = mustCreateAcceptedWorkflowStepAsExpected(primitiveInstruction, beforeTradeState, null);  // Debería tener un intent?

        assertEquals(1, ws.getBusinessEvent().getInstruction().size(), "The resulting workflow step must contain only one instruction in the business event");
        assertNotNull(ws.getBusinessEvent().getInstruction().get(0).getPrimitiveInstruction().getValuation(), "The resulting workflow step must contain the Valuation instruction in the business event");
    }

    /**
     * Exercise
     * The intent is to Exercise a contract that is made of one or several option payout legs.
     * The qualification of an exercise event from the fact that the only primitive is the exercise,
     * and the remaining quantity = 0, and the closedState of the contract is Terminated.
     */
    @Test
    void mustCreateExerciseBusinessEventAcceptedWorkflowStep() {

        // Trade to be included in Option Exercise.
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-rates/cb-option-usi.json");
        assertNotNull(beforeTradeState, "before TradeState must not be null");
        assertNotNull(beforeTradeState.getTrade().getProduct().getEconomicTerms().getPayout().stream()
                        .filter(payout -> payout.getOptionPayout() != null)
                        .findFirst().orElse(null),
                "OptionPayout must not be null"
        );

        // The date in which the option is exercised. It must be applied to the trade
        AdjustableOrAdjustedDate eventDate = new AdjustableOrAdjustedDate.AdjustableOrAdjustedDateBuilderImpl();

        // Creation of the option exercise instruction
        ExerciseInstruction exerciseInstruction = buildExercisePrimitiveInstruction(beforeTradeState, eventDate);

        // Adding the option exercise instruction to the primitive instruction that will be included in the resulting workflow step
        PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder().setExercise(exerciseInstruction).build();

        // Creation of the workflow step of an option exercise event
        WorkflowStep ws = mustCreateAcceptedWorkflowStepAsExpected(primitiveInstruction, beforeTradeState, EventIntentEnum.OPTION_EXERCISE);

        assertEquals(1,ws.getBusinessEvent().getInstruction().size(), "The resulting workflow step must contain only one instruction in the business event");
        assertNotNull(ws.getBusinessEvent().getInstruction().get(0).getPrimitiveInstruction().getExercise(), "The resulting workflow step must contain the Option Exercise instruction in the busines event");
    }

    /**
     * Transfer
     * The intent is to transfer the position to another clearing member
     */
    @Test
    void mustCreateTransferBusinessEventAcceptedWorkflowStep() {

        // Trade to be included in Transfer.
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-rates/ird-ex01-vanilla-swap-versioned.json");
        assertNotNull(beforeTradeState, "before TradeState must not be null");

        // Creation of the transfer instruction
        TransferInstruction transferInstruction = buildTransferPrimitiveInstruction ();

        // Adding the transfer instruction to the primitive instruction that will be included in the resulting workflow step
        PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder().setTransfer(transferInstruction).build();

        // Creation of the workflow step of a transfer event
        mustCreateAcceptedWorkflowStepAsExpected(primitiveInstruction, beforeTradeState, null);
    }

    /**
     * Amendment
     * The intent is to amend the terms of the contract through renegotiation.
     * It occurs when the terms of the original contract are modified, such as an extension of the maturity date, adjustment in the notional amount, or currency change.
     */
    @Test
    void mustCreateAmendmentBusinessEventAcceptedWorkflowStep() {

        // Trade to be included in Amendment.
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-rates/ird-ex01-vanilla-swap-versioned.json");
        assertNotNull(beforeTradeState, "before TradeState must not be null");

        // Creation of the amendment instruction through a terms change instruction
        TermsChangeInstruction amendmentInstruction = buildTermsChangePrimitiveInstruction (beforeTradeState);

        // Adding the amendment instruction to the primitive instruction that will be included in the resulting workflow step
        PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder().setTermsChange(amendmentInstruction).build();

        // Creation of the workflow step of an amendment event
        mustCreateAcceptedWorkflowStepAsExpected(primitiveInstruction, beforeTradeState, EventIntentEnum.CONTRACT_TERMS_AMENDMENT);
    }

    /**
     * Stock Split
     * A stock split is a corporate action in which a company increases the number of its outstanding shares by dividing its existing shares into multiple new shares.
     * The qualification of StockSplit business event based on an unchanged before/after currency amount,
     * the same adjustment ratio applied to the before/after cash price and number of units.
     */
    @Test
    void mustCreateStockSplitBusinessEventAcceptedWorkflowStep() {

        // Trade to be included in Stock Split.
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-equity/eqd-ex01-american-call-stock-long-form.json");
        assertNotNull(beforeTradeState, "before TradeState must not be null");

        // Adjustment ratio to be applied to the trade
        BigDecimal adjustmentRatio = new BigDecimal("0.23");

        // Creation of the stock split instruction
        StockSplitInstruction stockSplitInstruction = buildStockSplitPrimitiveInstruction (adjustmentRatio);

        // Adding the stock split instruction to the primitive instruction that will be included in the resulting workflow step
        PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder().setStockSplit(stockSplitInstruction).build();

        // Creation of the workflow step of a stock split event
        WorkflowStep ws = mustCreateAcceptedWorkflowStepAsExpected(primitiveInstruction, beforeTradeState, null);

        Trade beforeTrade = beforeTradeState.getTrade();
        Trade afterTrade = ws.getBusinessEvent().getAfter().get(0).getTrade();

        assertEquals(beforeTrade.getTradeLot().get(0).getPriceQuantity().get(0).getQuantity().get(0).getValue().getValue(), afterTrade.getTradeLot().get(0).getPriceQuantity().get(0).getQuantity().get(0).getValue().getValue(), "The before and the after trade must have the same quantity in te price quantity of the trade");
        assertNotEquals(beforeTrade.getTradeLot().get(0).getPriceQuantity().get(0).getPrice().get(0).getValue().getValue(),afterTrade.getTradeLot().get(0).getPriceQuantity().get(0).getPrice().get(0).getValue().getValue(), "The before and the after trade must have a different price in the price quantity of the trade");

        FinancialUnitEnum beforeFinancialUnit = beforeTrade.getTradeLot().get(0).getPriceQuantity().get(0).getPrice().get(0).getValue().getPerUnitOf().getFinancialUnit();
        FinancialUnitEnum afterFinancialUnit = ws.getBusinessEvent().getAfter().get(0).getTrade().getTradeLot().get(0).getPriceQuantity().get(0).getPrice().get(0).getValue().getPerUnitOf().getFinancialUnit();
        assertEquals(beforeFinancialUnit, FinancialUnitEnum.SHARE, "The before trade must contain a share financial unit");
        assertEquals(afterFinancialUnit, FinancialUnitEnum.SHARE, "The after trade must contain a share financial unit");
    }

    /**
     * Allocation
     * Some transactions, especially swaps, may be split between different accounts or funds after being executed.
     * The qualification of allocation event from the fact that the only primitives are split and contract formation,
     * the number of split executions and the number of contract formations are equal.
     * Note that SplitPrimitive type has a condition to check that the post-split quantities sum to the pre-split quantity.
     * Also note that it is expected that an allocation can result in a single contract.
     */
    @Test
    void mustCreateAllocationBusinessEventAcceptedWorkflowStep() {

        // Trade to be included in Allocation.
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-rates/ird-ex01-vanilla-swap-versioned.json");
        assertNotNull(beforeTradeState, "before TradeState must not be null");

        // Creation of the allocation instruction through the split instruction
        SplitInstruction allocationInstruction = buildSplitPrimitiveInstruction();

        // Adding the allocation instruction to the primitive instruction that will be included in the resulting workflow step
        PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder().setSplit(allocationInstruction).build();

        // Creation of the workflow step of an allocation event
        WorkflowStep ws = mustCreateAcceptedWorkflowStepAsExpected(primitiveInstruction, beforeTradeState, EventIntentEnum.ALLOCATION);

        //TODO: ClosedState not modeled
    }

    /**
     * Corporate Action
     * A corporate action is any event initiated by a company that impacts its shareholders or bondholders.
     * The qualification of the ocurrence of a corporate action from the fact that a corporate action is present
     * in either the observation or the observation history structures.
     */
    @Test
    void mustCreateCorporateActionBusinessEventAcceptedWorkflowStep() {

        // Trade to be included in Corporate Action.
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-rates/ird-ex01-vanilla-swap-versioned.json");
        assertNotNull(beforeTradeState, "before TradeState must not be null");
        //assertNotNull("before", beforeTradeState.getObservationHistory());

        // Observation that must be applied to the trade
        ObservationEvent observation = ObservationEvent.builder().setCorporateAction(CorporateAction.builder().setCorporateActionType(CorporateActionTypeEnum.LIQUIDATION).build()).build();

        // Creation of the corporate action instruction through the observation instruction
        ObservationInstruction corporateActionInstruction = buildObservationPrimitiveInstruction(observation);


        // Adding the corporate action instruction to the primitive instruction that will be included in the resulting workflow step
        PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder().setObservation(corporateActionInstruction).build();

        // Creation of the workflow step of a corporate action event
        WorkflowStep ws = mustCreateAcceptedWorkflowStepAsExpected(primitiveInstruction, beforeTradeState, EventIntentEnum.CORPORATE_ACTION_ADJUSTMENT);

        assertNotNull(ws.getBusinessEvent().getInstruction().get(0).getPrimitiveInstruction().getObservation().getObservationEvent().getCorporateAction(), "The resulting workflow step must contain an observation primitive instruction with a corporate action");
    }

    /**
     * Credit Event
     * A credit event is an occurrence that negatively affects the ability of a borrower (such as a company, government or financial institution)
     * to meet its debt obligations. These events are often related to payment defaults, changes in debt terms or financial restructurings.
     * The qualification of the ocurrence of a credit event determination from the fact that a credit event
     * is present in either the observation or observation history structures.
     */
    @Test
    void mustCreateCreditEventBusinessEventAcceptedWorkflowStep() {

        // Trade to be included in Credit Event.
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-rates/ird-ex01-vanilla-swap-versioned.json");
        assertNotNull(beforeTradeState, "before TradeState must not be null");

        // Observation that must be applied to the trade
        ObservationEvent observation = ObservationEvent.builder().setCreditEvent(CreditEvent.builder().setCreditEventType(CreditEventTypeEnum.BANKRUPTCY).build()).build();

        // Creation of the credit event instruction through the observation instruction
        ObservationInstruction creditEventInstruction = buildObservationPrimitiveInstruction(observation);

        // Adding the credit event instruction to the primitive instruction that will be included in the resulting workflow step
        PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder().setObservation(creditEventInstruction).build();

        // Creation of the workflow step of a credit event
        WorkflowStep ws = mustCreateAcceptedWorkflowStepAsExpected(primitiveInstruction, beforeTradeState, EventIntentEnum.CREDIT_EVENT);

        assertNotNull(ws.getBusinessEvent().getInstruction().get(0).getPrimitiveInstruction().getObservation().getObservationEvent().getCreditEvent(), "The resulting workflow step must contain a credit event in the primitive instruction of the business event");

    }

    /**
     * Compression
     * The intent is to compress multiple trades as part of a netting or compression event.
     * The qualification of a compression event from the fact that the quantityChange primitive exists,
     * and there are multiple contracts (or contract references) specified in the before state.
     */
    @Test
    void mustCreateCompressionBusinessEventAcceptedWorkflowStep() {

        // Trade to be included in the Execution instruction
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-rates/ird-ex01-vanilla-swap-versioned.json");
        assertNotNull(beforeTradeState, "before TradeState must not be null");

        // Creation of the execution instruction and adding it to the primitive instruction that will be included in the resulting workflow step
        NonTransferableProduct product = beforeTradeState.getTrade().getProduct();

        List <? extends TradeLot> tradeLot = beforeTradeState.getTrade().getTradeLot();

        List<PriceQuantity> pricequantity = List.of();

        if (beforeTradeState.getTrade().getTradeLot() != null) {
            pricequantity = beforeTradeState.getTrade()
                    .getTradeLot() // Get the list of TradeLots.
                    .stream() // Stream through the list.
                    .map(TradeLot::getPriceQuantity) // Extract PriceQuantity from each TradeLot.
                    .flatMap(List::stream) // Flatten the nested lists of PriceQuantity.
                    .collect(Collectors.toList()); // Collect into a single list.
        }

        List<? extends Counterparty> counterparty = beforeTradeState.getTrade().getCounterparty();

        List<? extends Party> party = beforeTradeState.getTrade().getParty();

        FieldWithMetaDate date = beforeTradeState.getTrade().getTradeDate();

        List<? extends TradeIdentifier> tradeIdentifier = beforeTradeState.getTrade().getTradeIdentifier();

        ExecutionInstruction executionInstruction = buildExecutionPrimitiveInstruction(product, tradeLot, pricequantity, counterparty, party, date, tradeIdentifier);
        PrimitiveInstruction p1 = PrimitiveInstruction.builder().setExecution(executionInstruction).build();

        // Trade to be included in the first quantity change instruction
        TradeState beforeTradeState2 = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-rates/EUR-OIS-uti.json");
        assertNotNull(beforeTradeState2, "before TradeState must not be null");

        // Creation of the first quantity change instruction and adding it to the primitive instruction that will be included in the resulting workflow step
        QuantityChangeInstruction quantityChangeInstruction = buildQuantityChangePrimitiveInstruction(beforeTradeState2, new PriceQuantity.PriceQuantityBuilderImpl(),QuantityChangeDirectionEnum.REPLACE);
        PrimitiveInstruction p2 = PrimitiveInstruction.builder().setQuantityChange(quantityChangeInstruction).build();

        // Trade to be included in the second quantity change instruction
        TradeState beforeTradeState3 = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "result-json-files/fpml-5-10/products/credit/cd-swaption-uti.json");
        assertNotNull(beforeTradeState3, "before TradeState must not be null");

        // Creation of the second quantity change instruction and adding it to the primitive instruction that will be included in the resulting workflow step
        QuantityChangeInstruction quantityChangeInstruction2 = buildQuantityChangePrimitiveInstruction(beforeTradeState3, new PriceQuantity.PriceQuantityBuilderImpl(),QuantityChangeDirectionEnum.INCREASE);
        PrimitiveInstruction p3 = PrimitiveInstruction.builder().setQuantityChange(quantityChangeInstruction2).build();

        // Creation of the workflow step of a compression event, containing the different instructions and its before tradeStates
        WorkflowStep ws = mustCreateCompressionAcceptedWorkflowStepAsExpected(p1, p2, p3, beforeTradeState, beforeTradeState2, beforeTradeState3, EventIntentEnum.COMPRESSION);

        int exec = 0;
        int quant = 0;

        //Go through every instruction in the business event
        for(int i = 0; i < ws.getBusinessEvent().getInstruction().size(); ++i){
            if(ws.getBusinessEvent().getInstruction().get(i).getPrimitiveInstruction().getExecution() != null)
                exec++;
            else if(ws.getBusinessEvent().getInstruction().get(i).getPrimitiveInstruction().getQuantityChange()!= null)
                quant++;
        }

        assertEquals(1, exec, "There must exist one Execution Primitive Instruction");
        assertTrue(quant > 1, "There must exist more than one Quantity Change Primitive Instructions");
    }

    /**
     * Clearing
     * Clearing is the process by which financial transactions between two parties are managed and verified, ensuring that payment and asset delivery obligations are met.
     */
    @Test
    void mustCreateClearingBusinessEventAcceptedWorkflowStep() {

        // Trade to be included in Clearing.
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-rates/ird-ex01-vanilla-swap-versioned.json");
        assertNotNull(beforeTradeState, "before TradeState must not be null");

        PartyChangeInstruction partyChangeInstruction = buildPartyChangePrimitiveInstruction(beforeTradeState, Counterparty.builder().setRole(CounterpartyRoleEnum.PARTY_1).build());
        PartyChangeInstruction partyChangeInstruction2 = buildPartyChangePrimitiveInstruction(beforeTradeState, Counterparty.builder().setRole(CounterpartyRoleEnum.PARTY_2).build()); //.setPartyReferenceValue(Party.builder().)
        QuantityChangeInstruction quantityChangeInstruction = buildQuantityChangePrimitiveInstruction(beforeTradeState, PriceQuantity.builder().build(), QuantityChangeDirectionEnum.INCREASE);

        // Creation of the clearing instruction through the split instruction
        SplitInstruction clearingInstruction = buildSplitClearingPrimitiveInstruction(PrimitiveInstruction.builder().setPartyChange(partyChangeInstruction).build(), PrimitiveInstruction.builder().setPartyChange(partyChangeInstruction2).build(), PrimitiveInstruction.builder().setQuantityChange(quantityChangeInstruction).build());

        // Adding the clearing instruction to the primitive instruction that will be included in the resulting workflow step
        PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder().setSplit(clearingInstruction).build();

        // Creation of the workflow step of a clearing event
        WorkflowStep ws = mustCreateAcceptedWorkflowStepAsExpected(primitiveInstruction, beforeTradeState, EventIntentEnum.CLEARING);

        Trade beforeTrade = beforeTradeState.getTrade();
        Trade afterTrade = ws.getBusinessEvent().getAfter().get(0).getTrade();

        //TODO: ClosedState not modeled

        //assertNotEquals(beforeTrade.getTradeIdentifier(), afterTrade.getTradeIdentifier(), "The before and after trade identifiers must be different ");

        //TODO: PartyRoleEnum must contain CLEARING_ORGANIZATION
        //assertEquals("Party Role must contain Clearing Organization", afterTrade.getPartyRole().get(0).getRole(), PartyRoleEnum.CLEARING_ORGANIZATION);

    }

    /**
     *
     * PRIMITIVE INSTRUCTION test builders
     */

    /**
     * Builds a ContractFormationInstruction with the provided legal agreement.
     *
     * @param legalAgreement The legal agreement to be associated with the contract formation instruction.
     * @return              A ContractFormationInstruction object containing the specified legal agreement.
     */
    public static ContractFormationInstruction buildContractFormationPrimitiveInstruction(LegalAgreement legalAgreement) {

        // Initialize and build the ContractFormationInstruction.
        return ContractFormationInstruction.builder()
                .addLegalAgreement(legalAgreement) // Adds the provided legal agreement to the contract formation instruction.
                .build();                        // Builds and returns the finalized ContractFormationInstruction.
    }

    /**
     * Builds an ISDA Master Agreement LegalAgreement.
     *
     * @return A LegalAgreement object representing an ISDA Master Agreement.
     */
    public static LegalAgreement buildISDAMasterAgreementLegalAgreement() {

        // Initialize and build the LegalAgreement for ISDA Master Agreement.
        return LegalAgreement.builder()
                .setLegalAgreementIdentification(LegalAgreementIdentification.builder()
                        .setAgreementName(AgreementName.builder()
                                .setAgreementType(LegalAgreementTypeEnum.MASTER_AGREEMENT) // Specifies the agreement type as MASTER_AGREEMENT.
                                .setMasterAgreementType(FieldWithMetaMasterAgreementTypeEnum.builder()
                                        .setValue(MasterAgreementTypeEnum.ISDA_MASTER)  // Specifies the master agreement type as ISDA_MASTER.
                                        .setMeta(MetaFields.builder()
                                                .setScheme("http://www.fpml.org/coding-scheme/master-agreement-type") // Sets the scheme for the master agreement type.
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build(); // Builds and returns the finalized LegalAgreement object.
    }


    /**
     * Creates an accepted WorkflowStep based on the provided parameters and validates its integrity.
     *
     * @param primitiveInstruction The instruction containing the primitive operation details used to build the proposed WorkflowStep.
     * @param tradeState           The current state of the trade, which serves as input to construct the WorkflowStep.
     * @param intent               The intended purpose or type of event being executed (e.g., trade execution, amendment, etc.).
     * @return                     An accepted WorkflowStep, which is the validated and finalized result of the given input parameters.
     */
    private WorkflowStep mustCreateAcceptedWorkflowStepAsExpected(PrimitiveInstruction primitiveInstruction, TradeState tradeState, EventIntentEnum intent) {

        // Build a proposed WorkflowStep based on the provided trade state, instruction, and event metadata.
        WorkflowStep proposedWorkflowStep = buildProposedWorkflowStep(tradeState, primitiveInstruction, event_date, event_time, intent);
        assertNotNull(proposedWorkflowStep, "Instruction WorkflowStep must not be null");

        // Evaluate the proposed WorkflowStep to generate an accepted WorkflowStep.
        WorkflowStep acceptedWorkflowStep = createWorkflowStepFunc.evaluate(proposedWorkflowStep);

        // Ensure the accepted WorkflowStep and its "after" TradeState are not null.
        assertNotNull(acceptedWorkflowStep, "Accepted WorkflowStep must not be null");
        assertNotNull(acceptedWorkflowStep.getBusinessEvent().getAfter(), "after TradeState must not be null");

        // Log the details of the accepted WorkflowStep in a human-readable JSON format for debugging purposes.
        try {
            LOGGER.debug(
                    String.format("*** ACCEPTED WorkflowStep - [Action = %s] [Rejected = %s] ***%n%s",
                            acceptedWorkflowStep.getAction(),
                            acceptedWorkflowStep.getRejected(),
                            RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(acceptedWorkflowStep)
                    )
            );
        } catch (JsonProcessingException e) {
            // Handle JSON processing exceptions and log the error stack trace for debugging.
            e.printStackTrace();
        }

        // Return the accepted WorkflowStep as the final result.
        return acceptedWorkflowStep;
    }

    /**
     * Creates an accepted WorkflowStep for a compression event using multiple trade states and instructions,
     * and validates its integrity.
     *
     * @param p1          The first primitive instruction used in the compression operation.
     * @param p2          The second primitive instruction used in the compression operation.
     * @param p3          The third primitive instruction used in the compression operation.
     * @param tradeState  The current state of the first trade involved in the compression.
     * @param tradeState2 The current state of the second trade involved in the compression.
     * @param tradeState3 The current state of the third trade involved in the compression.
     * @param intent      The intended purpose or type of event (e.g., compression, termination, etc.).
     * @return            An accepted WorkflowStep, which represents the validated and finalized result of the compression event.
     */
    private WorkflowStep mustCreateCompressionAcceptedWorkflowStepAsExpected(PrimitiveInstruction p1, PrimitiveInstruction p2, PrimitiveInstruction p3, TradeState tradeState, TradeState tradeState2, TradeState tradeState3, EventIntentEnum intent) {

        // Build a proposed WorkflowStep for the compression event using the provided trade states, instructions, and event metadata.
        WorkflowStep proposedWorkflowStep = buildProposedWorkflowStepCompression(tradeState, tradeState2, tradeState3, p1, p2, p3, event_date, event_time, intent);
        assertNotNull(proposedWorkflowStep, "Instruction WorkflowStep must not be null");

        // Evaluate the proposed WorkflowStep to generate an accepted WorkflowStep.
        WorkflowStep acceptedWorkflowStep = createWorkflowStepFunc.evaluate(proposedWorkflowStep);

        // Validate that the accepted WorkflowStep and its "after" TradeState are not null.
        assertNotNull(acceptedWorkflowStep, "Accepted WorkflowStep must not be null");
        assertNotNull(acceptedWorkflowStep.getBusinessEvent().getAfter(), "after TradeState must not be null");

        // Log the details of the accepted WorkflowStep in a human-readable JSON format for debugging purposes.
        try {
            LOGGER.debug(
                    String.format("*** ACCEPTED WorkflowStep - [Action = %s] [Rejected = %s] ***%n%s",
                            acceptedWorkflowStep.getAction(),
                            acceptedWorkflowStep.getRejected(),
                            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(acceptedWorkflowStep)
                    )
            );
        } catch (JsonProcessingException e) {
            // Handle JSON processing exceptions and log the error stack trace for debugging.
            e.printStackTrace();
        }

        // Return the accepted WorkflowStep as the final result of the compression operation.
        return acceptedWorkflowStep;
    }

    /**
     * Builds a proposed WorkflowStep based on the provided trade state, primitive instruction, and event details.
     *
     * @param before               The initial TradeState before the event occurs, representing the state to be updated.
     * @param primitiveInstruction The primitive instruction defining the specific operation (e.g., ContractFormationInstruction, SplitInstruction, etc.) to be applied.
     * @param eventDate            The date of the event, representing when the WorkflowStep is proposed to occur.
     * @param eventTime            The time of the event, used in conjunction with the date for precise event timestamping.
     * @param eventIntent          The intent or purpose of the event (e.g., creation, modification, compression) as an enumerated value.
     * @return                     A proposed WorkflowStep object containing all the specified inputs, ready for further processing.
     */
    public WorkflowStep buildProposedWorkflowStep(TradeState before, PrimitiveInstruction primitiveInstruction, Date eventDate, LocalTime eventTime, EventIntentEnum eventIntent) {

        // Create an Instruction containing the initial TradeState and the PrimitiveInstruction.
        Instruction instruction = Instruction.builder()
                .setBeforeValue(before) // Sets the TradeState to be updated.
                .setPrimitiveInstruction(primitiveInstruction); // Defines the operation to be performed.

        // Build an EventInstruction with the provided Instruction, event intent, and event date.
        EventInstruction eventInstruction = EventInstruction.builder()
                .addInstruction(instruction)
                .setIntent(eventIntent)
                .setEventDate(eventDate);

        // Create an EventTimestamp to define the exact event creation time using the provided date and time.
        EventTimestamp eventTimestamp = EventTimestamp.builder()
                .setDateTime(ZonedDateTime.of(eventDate.toLocalDate(), eventTime, ZoneOffset.UTC.normalized()))
                .setQualification(EventTimestampQualificationEnum.EVENT_CREATION_DATE_TIME);

        // Add an identifier for the event to facilitate tracking or auditing.
        Identifier eventIdentifier = Identifier.builder()
                .addAssignedIdentifier(
                        AssignedIdentifier.builder()
                                .setIdentifierValue("ExecutionExamples") // Example identifier for the event.
                );

        // Build and return the WorkflowStep with the constructed event instruction, timestamp, and identifier.
        return WorkflowStep.builder()
                .setProposedEvent(eventInstruction)
                .addTimestamp(eventTimestamp)
                .addEventIdentifier(eventIdentifier)
                .build();
    }

    /**
     * Builds a proposed WorkflowStep for a compression event, incorporating multiple trade states and primitive instructions.
     *
     * @param before    The initial TradeState for the first trade involved in the compression event.
     * @param before2   The initial TradeState for the second trade involved in the compression event.
     * @param before3   The initial TradeState for the third trade involved in the compression event.
     * @param p1        The first primitive instruction defining the action to be applied to the first trade state.
     * @param p2        The second primitive instruction defining the action to be applied to the second trade state.
     * @param p3        The third primitive instruction defining the action to be applied to the third trade state.
     * @param eventDate The date of the compression event, representing when it is proposed to occur.
     * @param eventTime The time of the compression event, used in conjunction with the date for precise timestamping.
     * @param eventIntent The intent or purpose of the compression event, as an enumerated value.
     * @return          A proposed WorkflowStep object containing the input instructions and metadata for further processing.
     */
    public WorkflowStep buildProposedWorkflowStepCompression(TradeState before, TradeState before2, TradeState before3, PrimitiveInstruction p1, PrimitiveInstruction p2, PrimitiveInstruction p3, Date eventDate, LocalTime eventTime, EventIntentEnum eventIntent) {

        // Create the first Instruction using the initial TradeState and the first PrimitiveInstruction.
        Instruction instruction = Instruction.builder()
                .setBeforeValue(before)
                .setPrimitiveInstruction(p1);

        // Create the second Instruction using the second TradeState and PrimitiveInstruction.
        Instruction instruction2 = Instruction.builder()
                .setBeforeValue(before2)
                .setPrimitiveInstruction(p2);

        // Create the third Instruction using the third TradeState and PrimitiveInstruction.
        Instruction instruction3 = Instruction.builder()
                .setBeforeValue(before3)
                .setPrimitiveInstruction(p3);

        // Build the EventInstruction containing all three Instructions, the event intent, and the event date.
        EventInstruction eventInstruction = EventInstruction.builder()
                .addInstruction(instruction)
                .addInstruction(instruction2)
                .addInstruction(instruction3)
                .setIntent(eventIntent)
                .setEventDate(eventDate);

        // Create the EventTimestamp for the compression event, combining the event date and time.
        EventTimestamp eventTimestamp = EventTimestamp.builder()
                .setDateTime(ZonedDateTime.of(eventDate.toLocalDate(), eventTime, ZoneOffset.UTC.normalized()))
                .setQualification(EventTimestampQualificationEnum.EVENT_CREATION_DATE_TIME);

        // Add an event identifier for tracking or auditing purposes.
        Identifier eventIdentifier = Identifier.builder()
                .addAssignedIdentifier(
                        AssignedIdentifier.builder()
                                .setIdentifierValue("ExecutionExamples") // Example identifier.
                );

        // Build and return the WorkflowStep containing the constructed event instruction, timestamp, and identifier.
        return WorkflowStep.builder()
                .setProposedEvent(eventInstruction)
                .addTimestamp(eventTimestamp)
                .addEventIdentifier(eventIdentifier)
                .build();
    }

    /**
     * Builds an ExecutionInstruction based on the provided TradeState.
     *
     * @return           An ExecutionInstruction object populated with data extracted from the given TradeState.
     */
    public static ExecutionInstruction buildExecutionPrimitiveInstruction( NonTransferableProduct product, List<? extends TradeLot> tradeLot, List<PriceQuantity> priceQuantity, List<? extends Counterparty> counterparty, List<? extends Party> party, FieldWithMetaDate date, List<? extends TradeIdentifier> tradeIdentifier) {

        // Initialize the ExecutionInstruction builder.
        ExecutionInstruction.ExecutionInstructionBuilder executionInstructionBuilder = ExecutionInstruction.builder();

        // Set the product information from the trade's product details.
        executionInstructionBuilder.setProduct(product);

        // Set price and quantity details if the trade has associated trade lots.
        if (tradeLot != null) {
            executionInstructionBuilder.setPriceQuantity(priceQuantity);
        }

        // Set the counterparties and parties involved in the trade.
        executionInstructionBuilder.setCounterparty(counterparty)
                .setParties(party);

        // Set the trade date and trade identifiers.
        executionInstructionBuilder.setTradeDate(date);
        executionInstructionBuilder.setTradeIdentifier(tradeIdentifier);

        // Build and return the ExecutionInstruction.
        return executionInstructionBuilder.build();
    }

    /**
     * Builds a PartyChangeInstruction to update the counterparty of a trade.
     *
     * @param tradeState The current state of the trade, containing information such as trade identifiers and existing counterparties.
     * @param newParty   The new counterparty to replace the existing one in the trade.
     * @return           A PartyChangeInstruction object containing the details of the counterparty change.
     */
    private static PartyChangeInstruction buildPartyChangePrimitiveInstruction(TradeState tradeState, Counterparty newParty) {

        // Initialize the PartyChangeInstruction builder.
        return PartyChangeInstruction.builder()
                .setTradeId(tradeState.getTrade().getTradeIdentifier()) // Set the trade identifiers from the trade state to ensure the instruction applies to the correct trade.
                .setCounterparty(newParty)                              // Set the new counterparty for the trade.
                .build();                                               // Build and return the finalized PartyChangeInstruction.
    }

    /**
     * Builds an ExerciseInstruction to execute the option within a trade.
     *
     * @param tradeState The current state of the trade, containing information such as trade identifiers.
     * @param eventDate  The date on which the exercise action occurs, either adjustable or adjusted to reflect business conventions.
     * @return           An ExerciseInstruction object representing the details required to execute the option.
     */
    public static ExerciseInstruction buildExercisePrimitiveInstruction(TradeState tradeState, AdjustableOrAdjustedDate eventDate) {

        // Create a reference to the option payout as part of the exercise process.
        ReferenceWithMetaOptionPayout option = ReferenceWithMetaOptionPayout.builder()
                .setValue(OptionPayout.builder().build()) // Build a basic OptionPayout structure.
                .build();

        // Initialize and build the ExerciseInstruction.
        return ExerciseInstruction.builder()
                .setExerciseDate(eventDate)                                                 // Set the date on which the option is exercised.
                .setExerciseTime(BusinessCenterTime.builder().build())                      // Set a placeholder exercise time, typically defined as business center time.
                .setExerciseOptionValue(OptionPayout.builder().build())                     // Define the value of the exercised option.
                .setExerciseQuantity(PrimitiveInstruction.builder().build())                // Set the quantity to be exercised, typically linked to a primitive instruction.
                .setExerciseOption(option)                                                  // Associate the exercise with the referenced option.
                .addReplacementTradeIdentifier(tradeState.getTrade().getTradeIdentifier())  // Add a replacement trade identifier from the trade state for tracking purposes.
                .build();                                                                   // Build and return the finalized ExerciseInstruction.
    }

    /**
     * Builds a ResetInstruction for a trade, specifying reset and rate record dates, and payout details.
     *
     * @param payout     A reference to the payout object associated with the reset.
     * @param payout1    The actual payout value to be set for the reset instruction.
     * @param date       The date of the reset and the rate record, applicable to this instruction.
     * @return           A ResetInstruction object containing the provided reset and payout details.
     */
    public static ResetInstruction buildResetPrimitiveInstruction(ReferenceWithMetaPayout payout, Payout payout1, Date date) {

        //TODO Review if payout1 is used
        // Initialize and build the ResetInstruction.
        return ResetInstruction.builder()
                .setResetDate(date)         // The date on which the reset occurs.
                .setRateRecordDate(date)    // The date for recording the rate associated with the reset.
                .setPayout(List.of(payout))          // Associates the reset instruction with a referenced payout.
                //.setPayoutValue(payout1)    // Sets the specific payout value for the reset.
                .build();                   // Builds and returns the finalized ResetInstruction.
    }

    /**
     * Builds a QuantityChangeInstruction for a trade, specifying the quantity change and direction.
     *
     * @param tradeState The current state of the trade, containing the trade identifier and other details.
     * @param change     The new desired quantity to be set for the trade.
     * @param direction  The direction of the quantity change (e.g., increase or decrease).
     * @return           A QuantityChangeInstruction object containing the specified quantity change and direction.
     */
    public static QuantityChangeInstruction buildQuantityChangePrimitiveInstruction(TradeState tradeState, PriceQuantity change, QuantityChangeDirectionEnum direction) {

        // Initialize and build the QuantityChangeInstruction.
        return QuantityChangeInstruction.builder()
                .addChange(change)                          // The new desired quantity to be set.
                .setDirection(direction)                    // Specifies the direction of the quantity change (e.g., increase or decrease).
                .setLotIdentifier(tradeState.getTrade().getTradeIdentifier()) // The identifier of the original trade.
                .build();                                   // Builds and returns the finalized QuantityChangeInstruction.
    }

    /**
     * Builds a ValuationInstruction for a trade, specifying the valuation amount and whether the previous valuation history should be replaced.
     *
     * @param valuation  The calculated valuation amount to be applied.
     * @return           A ValuationInstruction object containing the specified valuation details and history management.
     */
    public static ValuationInstruction buildValuationPrimitiveInstruction(Valuation valuation) {

        // Initialize and build the ValuationInstruction.
        return ValuationInstruction.builder()
                .addValuation(valuation)          // The calculated valuation amount to be included in the instruction.
                .setReplace(false)                // Specifies if the previous valuation tracks are kept (False).
                .build();                         // Builds and returns the finalized ValuationInstruction.
    }

    /**
     * Builds a TransferInstruction for a trade, initializing it with a transfer state.
     *
     * @return           A TransferInstruction object containing an initialized transfer state.
     */
    public static TransferInstruction buildTransferPrimitiveInstruction() {

        // Initialize and build the TransferInstruction.
        return TransferInstruction.builder()
                .addTransferState(TransferState.builder().build()) // Adds a newly created TransferState to the instruction.
                .build();                                         // Builds and returns the finalized TransferInstruction.
    }

    /**
     * Builds a TermsChangeInstruction for a trade, specifying the product, ancillary party, and adjustment type.
     *
     * @param tradeState The current state of the trade, which provides information about the product, ancillary party, and other trade details.
     * @return           A TermsChangeInstruction object containing the specified product, ancillary party, and adjustment type.
     */
    public static TermsChangeInstruction buildTermsChangePrimitiveInstruction(TradeState tradeState) {

        // Initialize and build the TermsChangeInstruction.
        return TermsChangeInstruction.builder()
                .setProduct(tradeState.getTrade().getProduct())          // Specifies the product to which the terms change will be applied.
                .setAncillaryParty(tradeState.getTrade().getAncillaryParty()) // Sets the ancillary party involved in the terms change.
                .setAdjustment(NotionalAdjustmentEnum.PORTFOLIO_REBALANCING) // Defines the type of adjustment, such as portfolio rebalancing.
                .build();                                              // Builds and returns the finalized TermsChangeInstruction.
    }

    /**
     * Builds a StockSplitInstruction for a trade, specifying the adjustment ratio and effective date.
     *
     * @param adjustmentRatio The ratio by which the stock split is applied, determining how the stock is adjusted.
     * @return              A StockSplitInstruction object containing the adjustment ratio and effective date for the stock split.
     */
    public static StockSplitInstruction buildStockSplitPrimitiveInstruction(BigDecimal adjustmentRatio) {

        // Initialize and build the StockSplitInstruction.
        return StockSplitInstruction.builder()
                .setAdjustmentRatio(adjustmentRatio) // Sets the adjustment ratio for the stock split (how the stock is adjusted).
                .setEffectiveDate(event_date)       // Specifies the effective date when the stock split takes place.
                .build();                           // Builds and returns the finalized StockSplitInstruction.
    }

    /**
     * Builds a SplitInstruction for a trade, specifying a breakdown of the primitive instruction.
     *
     * @return           A SplitInstruction object containing a breakdown of the primitive instruction.
     */
    public static SplitInstruction buildSplitPrimitiveInstruction() {

        // Initialize and build the SplitInstruction.
        return SplitInstruction.builder()
                //.addBreakdown(PrimitiveInstruction.builder().build()) // Adds a breakdown of the primitive instruction (empty in this case).
                .addBreakdown(PrimitiveInstruction.builder().build()) // Adds a breakdown of the primitive instruction (empty in this case).
                .build();                                             // Builds and returns the finalized SplitInstruction.
    }

    /**
     * Builds a Novation SplitInstruction for a trade, specifying a breakdown of the primitive instruction.
     *
     * @return           A SplitInstruction object containing a breakdown of the primitive instruction.
     */
    public static SplitInstruction buildNovationPrimitiveInstruction(PrimitiveInstruction partyChange, PrimitiveInstruction quantityChange) {

        // Initialize and build the SplitInstruction.
        return SplitInstruction.builder()
                .addBreakdown(partyChange) // Adds a breakdown of the primitive instruction (party change).
                .addBreakdown(quantityChange) // Adds a breakdown of the primitive instruction (quantity change).
                .build();                                             // Builds and returns the finalized SplitInstruction.
    }

    /**
     * Builds a SplitInstruction for a Clearing instruction, specifying a breakdown of each primitive instruction.
     *
     * @return           A SplitInstruction object containing a breakdown of each primitive instruction.
     */
    public static SplitInstruction buildSplitClearingPrimitiveInstruction(PrimitiveInstruction p1, PrimitiveInstruction p2, PrimitiveInstruction p3) {

        // Initialize and build the SplitInstruction.
        return SplitInstruction.builder()
                .addBreakdown(p1) // Adds a breakdown of the primitive instruction.
                .addBreakdown(p2) // Adds a breakdown of the primitive instruction.
                .addBreakdown(p3) // Adds a breakdown of the primitive instruction.
                .build();                                             // Builds and returns the finalized SplitInstruction.
    }

    /**
     * Builds an ObservationInstruction for a given observation event.
     *
     * @param observation The observation event containing the details of the event to be observed.
     * @return            An ObservationInstruction object containing the specified observation event.
     */
    public static ObservationInstruction buildObservationPrimitiveInstruction(ObservationEvent observation) {

        // Initialize and build the ObservationInstruction.
        return ObservationInstruction.builder()
                .setObservationEvent(observation) // Sets the observation event details in the instruction.
                .build();                        // Builds and returns the finalized ObservationInstruction.
    }
}