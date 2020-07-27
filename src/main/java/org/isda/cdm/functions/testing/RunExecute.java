package org.isda.cdm.functions.testing;

import com.regnosys.rosetta.common.testing.ExecutableFunction;
import org.isda.cdm.BusinessEvent;
import org.isda.cdm.Contract;
import org.isda.cdm.functions.Create_Execution;

import javax.inject.Inject;
import java.util.Collections;

import static org.isda.cdm.functions.testing.FunctionUtils.guard;

public class RunExecute implements ExecutableFunction<Contract, BusinessEvent> {

    @Inject
    Create_Execution execute;

    @Override
    public BusinessEvent execute(Contract contract) {
        return execute.evaluate(contract.getTradableProduct().getProduct(),
                guard(contract.getTradableProduct().getQuantityNotation()),
                guard(contract.getTradableProduct().getPriceNotation()),
                guard(contract.getTradableProduct().getCounterparties()),
                guard(contract.getParty()),
                guard(contract.getPartyRole()),
                Collections.emptyList());
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
