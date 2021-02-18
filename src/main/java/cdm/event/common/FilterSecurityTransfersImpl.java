package cdm.event.common;

import cdm.event.common.functions.FilterSecurityTransfers;
import cdm.observable.asset.Observable;
import cdm.observable.asset.PriceQuantity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FilterSecurityTransfersImpl extends FilterSecurityTransfers {
    @Override
    protected Transfers.TransfersBuilder doEvaluate(List<Transfer> transfers) {
        List<Transfer> cashTransfers = transfers.stream().filter(this::hasSecurity).collect(Collectors.toList());
        return !cashTransfers.isEmpty() ? Transfers.builder().addTransfers(cashTransfers) : null;
    }

    private boolean hasSecurity(Transfer transfer) {
        return Optional.ofNullable(transfer).map(Transfer::getPriceQuantity)
                .map(PriceQuantity::getObservable)
                .map(Observable::getProductIdentifier)
                .isPresent();
    }
}
