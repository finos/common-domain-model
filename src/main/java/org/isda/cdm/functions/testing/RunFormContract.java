package org.isda.cdm.functions.testing;

import com.regnosys.rosetta.common.testing.ExecutableFunction;
import org.isda.cdm.BusinessEvent;
import org.isda.cdm.Contract;
import org.isda.cdm.functions.Execute;
import org.isda.cdm.functions.FormContract;

import javax.inject.Inject;

import static org.isda.cdm.functions.testing.FunctionUtils.guard;

public class RunFormContract implements ExecutableFunction<Contract, BusinessEvent> {

    @Inject
    Execute execute;

    @Inject
    FormContract formContract;


    @Override
    public BusinessEvent execute(Contract contract) {
        BusinessEvent executeBusinessEvent = execute.evaluate(contract.getTradableProduct().getProduct(),
                guard(contract.getTradableProduct().getQuantityNotation()),
                guard(contract.getTradableProduct().getPriceNotation()),
                guard(contract.getParty()),
                guard(contract.getPartyRole()));

        return formContract.evaluate(executeBusinessEvent, null);
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
