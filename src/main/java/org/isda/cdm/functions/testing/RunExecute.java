package org.isda.cdm.functions.testing;

import static org.isda.cdm.functions.testing.FunctionUtils.guard;

import java.util.Collections;
import java.util.Optional;

import javax.inject.Inject;

import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.metafields.FieldWithMetaDate;

import cdm.event.common.BusinessEvent;
import cdm.event.common.functions.Create_Execution;
import cdm.legalagreement.contract.Contract;

public class RunExecute implements ExecutableFunction<Contract, BusinessEvent> {

    @Inject
    Create_Execution execute;

    @Override
    public BusinessEvent execute(Contract contract) {
        return execute.evaluate(contract.getTradableProduct().getProduct(),
                guard(contract.getTradableProduct().getQuantityNotation()),
                guard(contract.getTradableProduct().getPriceNotation()),
                guard(contract.getTradableProduct().getCounterparty()),
                guard(contract.getTradableProduct().getAncillaryRole()),
                guard(contract.getParty()),
                guard(contract.getPartyRole()),
                Collections.emptyList(),
                null,
                Optional.ofNullable(contract.getTradeDate()).map(FieldWithMetaDate::getValue).orElse(null),
                guard(contract.getContractIdentifier()));
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
