package cdm.event.common.functions;

import cdm.event.common.Transfer;
import cdm.event.common.Transfers;
import cdm.observable.asset.Observable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class FilterSecurityTransfersImpl extends FilterSecurityTransfers {

    @Override
    protected Transfers.TransfersBuilder doEvaluate(List<? extends Transfer> transfers) {
        List<Transfer> cashTransfers = emptyIfNull(transfers).stream()
                .filter(this::hasSecurity)
                .collect(Collectors.toList());
        return !cashTransfers.isEmpty() ? Transfers.builder().addTransfers(cashTransfers) : null;
    }

    private boolean hasSecurity(Transfer transfer) {
        return !Optional.ofNullable(transfer)
                .map(Transfer::getObservable)
                .map(Observable::getProductIdentifier)
                .orElse(Collections.emptyList())
                .isEmpty();
    }
}
