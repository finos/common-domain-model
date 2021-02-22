package cdm.event.common;

import cdm.base.math.MeasureBase;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.event.common.functions.FilterCashTransfers;
import cdm.observable.asset.PriceQuantity;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterCashTransfersImpl extends FilterCashTransfers {

    @Override
    protected Transfers.TransfersBuilder doEvaluate(List<? extends Transfer> transfers) {
        List<Transfer> cashTransfers = transfers.stream().filter(this::hasCurrency).collect(Collectors.toList());
        return !cashTransfers.isEmpty() ? Transfers.builder().addTransfers(cashTransfers) : null;
    }

    private boolean hasCurrency(Transfer transfer) {
        return Optional.ofNullable(transfer).map(Transfer::getPriceQuantity)
                .map(PriceQuantity::getQuantity).map(Collection::stream)
                .map(this::getCurrency)
                .isPresent();
    }

    @NotNull
    private Stream<FieldWithMetaString> getCurrency(Stream<? extends FieldWithMetaQuantity> s) {
        return s.map(FieldWithMetaQuantity::getValue).map(MeasureBase::getUnitOfAmount).map(UnitType::getCurrency);
    }
}
