package cdm.event.common;

import cdm.base.math.MeasureBase;
import cdm.base.math.UnitType;
import cdm.event.common.functions.FilterCashTransfers;
import com.rosetta.model.metafields.FieldWithMetaString;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FilterCashTransfersImpl extends FilterCashTransfers {

    @Override
    protected Transfers.TransfersBuilder doEvaluate(List<? extends Transfer> transfers) {
        List<Transfer> cashTransfers = transfers.stream().filter(this::hasCurrency).collect(Collectors.toList());
        return !cashTransfers.isEmpty() ? Transfers.builder().addTransfers(cashTransfers) : null;
    }

    private boolean hasCurrency(Transfer transfer) {
        return Optional.ofNullable(transfer)
                .map(Transfer::getQuantity)
                .map(MeasureBase::getUnitOfAmount)
                .map(UnitType::getCurrency)
                .map(FieldWithMetaString::getValue)
                .isPresent();
    }
}
