package cdm.event.common.functions;

import cdm.base.datetime.AdjustableOrAdjustedOrRelativeDate;
import cdm.event.common.Transfer;
import cdm.event.common.Transfers;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaDate;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class TransfersForDateImpl extends TransfersForDate {
    @Override
    protected Transfers.TransfersBuilder doEvaluate(List<? extends Transfer> transfers, Date date) {
        if (date == null) {
            return null;
        }

        List<Transfer> transfersOnDate = emptyIfNull(transfers).stream()
                .filter(transfer -> getDate(transfer).map(date::equals).orElse(false))
                .collect(Collectors.toList());
        return !transfersOnDate.isEmpty() ? Transfers.builder().addTransfers(transfersOnDate) : null;
    }

    @NotNull
    private Optional<Date> getDate(Transfer transfer) {
        return Optional.ofNullable(transfer.getSettlementDate())
                .map(AdjustableOrAdjustedOrRelativeDate::getAdjustedDate)
                .map(FieldWithMetaDate::getValue);
    }
}
