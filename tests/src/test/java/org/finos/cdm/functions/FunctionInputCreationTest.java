package org.finos.cdm.functions;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.regnosys.rosetta.common.postprocess.WorkflowPostProcessor;
import com.rosetta.model.lib.process.PostProcessor;
import jakarta.inject.Inject;
import org.finos.cdm.CdmRuntimeModule;
import org.finos.cdm.functions.FunctionInputCreator.ExpectationResult;
import org.finos.cdm.util.ResourcesUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionInputCreationTest {

    private static final ObjectWriter OBJECT_WRITER = FunctionInputCreator.STRICT_MAPPER.writerWithDefaultPrettyPrinter();
    
    @Inject private FunctionInputCreator creator;
    
    @BeforeEach
    public void setUp() {
        Module module = Modules.override(new CdmRuntimeModule())
                .with(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(PostProcessor.class).to(WorkflowPostProcessor.class);
                    }
                });
        Injector injector = Guice.createInjector(module);
        injector.injectMembers(this);
    }

    @Test
    void testContractFormationIrSwapFuncInputJson() throws IOException {
        assertResult(creator.getContractFormationIrSwapFuncInputJson());
    }

    @Test
    void testExecutionIrSwapFuncInputJson() throws IOException {
        assertResult(creator.getExecutionIrSwapFuncInputJson());
    }

    @Test
    void testExecutionIrSwapWithInitialFeeFuncInputJson() throws IOException {
        assertResult(creator.getExecutionIrSwapWithInitialFeeFuncInputJson());
    }

    @Test
    void testExecutionIrSwapWithOtherPartyPaymentFuncInputJson() throws IOException {
        assertResult(creator.getExecutionIrSwapWithOtherPartyPaymentFuncInputJson());
    }

    @Test
    void testExecutionFraFuncInputJson() throws IOException {
        assertResult(creator.getExecutionFraFuncInputJson());
    }

    @Test
    void testExecutionBasisSwapFuncInputJson() throws IOException {
        assertResult(creator.getExecutionBasisSwapFuncInputJson());
    }

    @Test
    void testExecutionOisSwapFuncInputJson() throws IOException {
        assertResult(creator.getExecutionOisSwapFuncInputJson());
    }

    @Test
    void testExecutionCreditDefaultSwapFuncInputJson() throws IOException {
        assertResult(creator.getExecutionCreditDefaultSwapFuncInputJson());
    }

    @Test
    void testExecutionFxForwardFuncInputJson() throws IOException {
        assertResult(creator.getExecutionFxForwardFuncInputJson());
    }

    @Test
    void testExecutionSwaptionFuncInputJson() throws IOException {
        assertResult(creator.getExecutionSwaptionFuncInputJson());
    }

    @Test
    void testContractFormationIrSwapWithLegalAgreementFuncInputJson() throws IOException {
        assertResult(creator.getContractFormationIrSwapWithLegalAgreementFuncInputJson());
    }

    @Test
    void testContractFormationFraFuncInputJson() throws IOException {
        assertResult(creator.getContractFormationFraFuncInputJson());
    }

    @Test
    void testContractFormationBasisSwapFuncInputJson() throws IOException {
        assertResult(creator.getContractFormationBasisSwapFuncInputJson());
    }

    @Test
    void testContractFormationOisSwapFuncInputJson() throws IOException {
        assertResult(creator.getContractFormationOisSwapFuncInputJson());
    }

    @Test
    void testContractFormationSwaptionFuncInputJson() throws IOException {
        assertResult(creator.getContractFormationSwaptionFuncInputJson());
    }

    @Test
    void testContractFormationCreditDefaultSwapFuncInputJson() throws IOException {
        assertResult(creator.getContractFormationCreditDefaultSwapFuncInputJson());
    }

    @Test
    void testContractFormationFxForwardFuncInputJson() throws IOException {
        assertResult(creator.getContractFormationFxForwardFuncInputJson());
    }

    @Test
    void testFullTerminationVanillaSwapFuncInputJson() throws IOException {
        assertResult(creator.getFullTerminationVanillaSwapFuncInputJson());
    }

    @Test
    void testFullTerminationEquitySwapFuncInputJson() throws IOException {
        assertResult(creator.getFullTerminationEquitySwapFuncInputJson());
    }

    @Test
    void testPartialTerminationVanillaSwapFuncInputJson() throws IOException {
        assertResult(creator.getPartialTerminationVanillaSwapFuncInputJson());
    }

    @Test
    void testPartialTerminationEquitySwapFuncInputJson() throws IOException {
        assertResult(creator.getPartialTerminationEquitySwapFuncInputJson());
    }

    @Test
    void testIncreaseEquitySwapFuncInputJson() throws IOException {
        assertResult(creator.getIncreaseEquitySwapFuncInputJson());
    }

    @Test
    void testIncreaseEquitySwapExistingTradeLotFuncInputJson() throws IOException {
        assertResult(creator.getIncreaseEquitySwapExistingTradeLotFuncInputJson());
    }

    @Test
    void testCompressionFuncInputJson() throws IOException {
        assertResult(creator.getCompressionFuncInputJson());
    }

    @Test
    void testFullNovationFuncInputJson() throws IOException {
        assertResult(creator.getFullNovationFuncInputJson());
    }

    @Test
    void testPartialNovationFuncInputJson() throws IOException {
        assertResult(creator.getPartialNovationFuncInputJson());
    }

    @Test
    void testClearingFuncInputJson() throws IOException {
        assertResult(creator.getClearingFuncInputJson());
    }

    @Test
    void testAllocationFuncInputJson() throws IOException {
        assertResult(creator.getAllocationFuncInputJson());
    }

    @Test
    void testCreditEventFuncInputJson() throws IOException {
        assertResult(creator.getCreditEventFuncInputJson());
    }

    @Test
    void testCreditEventWithObservationFuncInputJson() throws IOException {
        assertResult(creator.getCreditEventWithObservationFuncInputJson());
    }

    @Test
    void testCorporateActionFuncInputJson() throws IOException {
        assertResult(creator.getCorporateActionFuncInputJson());
    }

    @Test
    void testCorporateActionWithObservationFuncInputJson() throws IOException {
        assertResult(creator.getCorporateActionWithObservationFuncInputJson());
    }

    @Test
    void testExerciseSwaptionFullPhysicalInputJson() throws IOException {
        assertResult(creator.getExerciseSwaptionFullPhysicalInputJson());
    }

    @Test
    void testExerciseCashSettledInputJson() throws IOException {
        assertResult(creator.getExerciseCashSettledInputJson());
    }

    @Test
    void testExercisePartialExerciseInputJson() throws IOException {
        assertResult(creator.getExercisePartialExerciseInputJson());
    }

    @Test
    void testExerciseCancellableOptionInputJson() throws IOException {
        assertResult(creator.getExerciseCancellableOptionInputJson());
    }

    @Test
    void testIndexTransitionVanillaSwapFuncInputJson() throws IOException {
        assertResult(creator.getIndexTransitionVanillaSwapFuncInputJson());
    }

    @Test
    void testIndexTransitionXccySwapFuncInputJson() throws IOException {
        assertResult(creator.getIndexTransitionXccySwapFuncInputJson());
    }

    @Test
    void testStockSplitFuncInputJson() throws IOException {
        assertResult(creator.getStockSplitFuncInputJson());
    }

    @Test
    void testCorrectionWorkflowFuncInputJson() throws IOException {
        assertResult(creator.getCorrectionWorkflowFuncInputJson());
    }

    @Test
    void testCancellationWorkflowFuncInputJson() throws IOException {
        assertResult(creator.getCancellationWorkflowFuncInputJson());
    }

    @Test
    void testBondExecutionInput() throws IOException {
        assertResult(creator.getBondExecutionInput());
    }

    @Test
    void testRepoExecutionInput() throws IOException {
        assertResult(creator.getRepoExecutionInput());
    }

    @Test
    void testRollInput() throws IOException {
        assertResult(creator.getRollInput());
    }

    @Test
    void testOnDemandRateChangeInput() throws IOException {
        assertResult(creator.getOnDemandRateChangeInput());
    }

    @Test
    void testPairOffInput() throws IOException {
        assertResult(creator.getPairOffInput());
    }

    @Test
    void testCancellationInput() throws IOException {
        assertResult(creator.getCancellationInput());
    }

    @Test
    void testOnDemandInterestPaymentEventInput() throws IOException {
        assertResult(creator.getOnDemandInterestPaymentEventInput());
    }

    @Test
    void testShapingPrimitiveInstructionTradeLots() throws IOException {
        assertResult(creator.getShapingPrimitiveInstructionTradeLots());
    }

    @Test
    void testShapingEventInput() throws IOException {
        assertResult(creator.getShapingEventInput());
    }

    @Test
    void testPartialDeliveryDeliveredPriceQuantity() throws IOException {
        assertResult(creator.getPartialDeliveryDeliveredPriceQuantity());
    }

    @Test
    void testPartialDeliveryEventInput() throws IOException {
        assertResult(creator.getPartialDeliveryEventInput());
    }

    @Test
    void testRepriceEventInput() throws IOException {
        assertResult(creator.getRepriceEventInput());
    }

    @Test
    void testAdjustmentEventInput() throws IOException {
        assertResult(creator.getAdjustmentEventInput());
    }

    @Test
    void testRepoSubstitutionCollateral() throws IOException {
        assertResult(creator.getRepoSubstitutionCollateral());
    }

    @Test
    void testRepoSubstitutionPriceQuantity() throws IOException {
        assertResult(creator.getRepoSubstitutionPriceQuantity());
    }

    @Test
    void testSubstitutionEventInput() throws IOException {
        assertResult(creator.getSubstitutionEventInput());
    }

    private static void assertResult(ExpectationResult<?> result) throws IOException {
        String expectedJson = ResourcesUtils.getJson(result.getResourceName());
        String actualJson = OBJECT_WRITER.writeValueAsString(result.getActual());
        assertEquals(expectedJson, actualJson);
    }
}
