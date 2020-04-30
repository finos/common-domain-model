package org.isda.cdm.functions.testing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.lib.process.PostProcessor;
import org.isda.cdm.*;
import org.isda.cdm.functions.Create_Execute;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.isda.cdm.functions.testing.FunctionUtils.guard;

public class RunExecuteCorrectCancel implements ExecutableFunction<Contract, Workflow> {

    @Inject
    Create_Execute execute;

    @Inject
    LineageUtils lineageUtils;

    @com.google.inject.Inject
    private PostProcessor runner;

    @Override
    public Workflow execute(Contract contract) {

        List<QuantityNotation> incorrectQuantity = setQuantityAmount(contract, 99999);

        BusinessEvent originalExecution = execute.evaluate(contract.getTradableProduct().getProduct(),
                guard(incorrectQuantity),
                guard(contract.getTradableProduct().getPriceNotation()),
                guard(contract.getParty()),
                guard(contract.getPartyRole()));

        WorkflowStep newExecution = WorkflowStep.builder()
                .setAction(ActionEnum.NEW)
                .setBusinessEvent(originalExecution)
                .build();

        BusinessEvent correctedExecution = execute.evaluate(contract.getTradableProduct().getProduct(),
                guard(contract.getTradableProduct().getQuantityNotation()),
                guard(contract.getTradableProduct().getPriceNotation()),
                guard(contract.getParty()),
                guard(contract.getPartyRole()));

        WorkflowStep correctExecution = WorkflowStep.builder()
                .setAction(ActionEnum.CORRECT)
                .setBusinessEvent(correctedExecution)
                .build();

        WorkflowStep cancelExecution = WorkflowStep.builder()
                .setAction(ActionEnum.CANCEL)
                .setRejected(true)
                .setBusinessEvent(correctedExecution)
                .build();


        Workflow.WorkflowBuilder builder = Workflow.builder();


        runner.postProcess(Workflow.class, builder
                .addSteps(lineageUtils.withLineage(newExecution, correctExecution, cancelExecution))
        );
        Workflow build = builder .build();
        try {
            System.out.println(RosettaObjectMapper.getNewRosettaObjectMapper().writeValueAsString(build));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return build;
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
