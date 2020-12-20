package cdm.event.common;

import cdm.event.common.functions.TransfersForDate;
import com.rosetta.model.lib.records.Date;

import java.util.List;
import java.util.stream.Collectors;

public class TransfersForDateImpl extends TransfersForDate {
    @Override
    protected Transfers.TransfersBuilder doEvaluate(List<Transfer> transfers, Date date) {
        List<Transfer> transfersOnDate = transfers.stream()
                .filter(transfer -> transfer.getSettlementDate().getAdjustedDate().getValue().equals(date))
                .collect(Collectors.toList());
        return !transfersOnDate.isEmpty() ? Transfers.builder().addTransfers(transfersOnDate) : null;
    }
}
