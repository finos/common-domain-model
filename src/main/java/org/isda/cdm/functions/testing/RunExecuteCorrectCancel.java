package org.isda.cdm.functions.testing;

import com.regnosys.rosetta.common.testing.ExecutableFunction;
import org.isda.cdm.*;
import org.isda.cdm.functions.Create_Execution;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.isda.cdm.functions.testing.FunctionUtils.guard;

public class RunExecuteCorrectCancel implements ExecutableFunction<Contract, Workflow> {

    @Inject
    Create_Execution execute;

    @Inject
    LineageUtils lineageUtils;

    @Override
    public Workflow execute(Contract contract) {

        List<QuantityNotation> incorrectQuantity = setQuantityAmount(contract, 99999);

        BusinessEvent evaluate = execute.evaluate(contract.getTradableProduct().getProduct(),
                guard(incorrectQuantity),
                guard(contract.getTradableProduct().getPriceNotation()),
                guard(contract.getParty()),
                guard(contract.getPartyRole()),
                Collections.emptyList());

        WorkflowStep newExecutionWorkflowStep = WorkflowStep.builder()
                .setAction(ActionEnum.NEW)
                .setBusinessEvent(evaluate)
                .build();

        BusinessEvent corrected = execute.evaluate(contract.getTradableProduct().getProduct(),
                guard(contract.getTradableProduct().getQuantityNotation()),
                guard(contract.getTradableProduct().getPriceNotation()),
                guard(contract.getParty()),
                guard(contract.getPartyRole()),
                Collections.emptyList());

        WorkflowStep correctedExecutionWorkflowStep = WorkflowStep.builder()
                .setAction(ActionEnum.CORRECT)
                .setBusinessEvent(corrected)
                .build();

        WorkflowStep cancelledExecutionWorkflowStep = WorkflowStep.builder()
                .setAction(ActionEnum.CANCEL)
                .setBusinessEvent(corrected)
                .build();

        Workflow workflow = Workflow.builder()
                .addSteps(lineageUtils.withLineage(newExecutionWorkflowStep, correctedExecutionWorkflowStep, cancelledExecutionWorkflowStep))
                .build();
        return workflow;
    }

    private List<QuantityNotation> setQuantityAmount(Contract contract, int val) {
        List<QuantityNotation.QuantityNotationBuilder> quantityNotationBuilders = contract.getTradableProduct().getQuantityNotation().stream()
                .map(QuantityNotation::toBuilder)
                .collect(Collectors.toList());
        quantityNotationBuilders.stream()
                .map(QuantityNotation.QuantityNotationBuilder::getQuantity)
                .forEach(b -> b.setAmount(BigDecimal.valueOf(val)));

        return quantityNotationBuilders.stream()
                .map(QuantityNotation.QuantityNotationBuilder::build)
                .collect(Collectors.toList());
    }

    @Override
    public Class<Contract> getInputType() {
        return Contract.class;
    }

    @Override
    public Class<Workflow> getOutputType() {
        return Workflow.class;
    }
}
