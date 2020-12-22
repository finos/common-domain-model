package cdm.event.common;

import cdm.event.common.functions.FilterCashTransfers;

import java.util.List;
import java.util.stream.Collectors;

public class FilterCashTransfersImpl extends FilterCashTransfers {

    @Override
    protected Transfers.TransfersBuilder doEvaluate(List<Transfer> transfers) {
        List<Transfer> cashTransfers = transfers.stream().filter(this::hasCurrency).collect(Collectors.toList());
        return !cashTransfers.isEmpty() ? Transfers.builder().addTransfers(cashTransfers) : null;
    }

    private boolean hasCurrency(Transfer transfer) {
//        return Optional.ofNullable(transfer).map(Transfer::getPriceQuantity)
//                .map(PriceQuantity::getQuantity)
//                .map(Collection::stream)
//                .map(Quantity::getUnitOfAmount)
//                .map(UnitType::getCurrency).isPresent();
        return false;
    }
}
