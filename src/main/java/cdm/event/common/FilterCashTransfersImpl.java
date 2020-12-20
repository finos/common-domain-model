package cdm.event.common;

import cdm.event.common.functions.FilterCashTransfers;
import cdm.observable.asset.AssetIdentifier;
import cdm.observable.asset.QuantityNotation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FilterCashTransfersImpl extends FilterCashTransfers {

    @Override
    protected Transfers.TransfersBuilder doEvaluate(List<Transfer> transfers) {
        List<Transfer> cashTransfers = transfers.stream().filter(this::hasCurrency).collect(Collectors.toList());
        return !cashTransfers.isEmpty() ? Transfers.builder().addTransfers(cashTransfers) : null;
    }

    private boolean hasCurrency(Transfer transfer) {
        return Optional.ofNullable(transfer).map(Transfer::getQuantity).map(QuantityNotation::getAssetIdentifier)
                .map(AssetIdentifier::getCurrency).isPresent();
    }
}
