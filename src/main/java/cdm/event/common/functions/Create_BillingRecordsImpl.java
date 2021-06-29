package cdm.event.common.functions;

import cdm.event.common.BillingRecord;
import cdm.event.common.BillingRecordInstruction;
import cdm.event.common.Transfer;
import cdm.event.common.functions.Create_BillingRecord;
import cdm.event.common.functions.Create_BillingRecords;

import javax.inject.Inject;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Create_BillingRecordsImpl extends Create_BillingRecords {

    @Inject
    Create_BillingRecord create_billingRecord;

    @Override
    protected List<BillingRecord.BillingRecordBuilder> doEvaluate(List<? extends BillingRecordInstruction> billingRecordInstructions) {
        return Optional.ofNullable(billingRecordInstructions)
                .stream().flatMap(Collection::stream)
                .map(create_billingRecord::evaluate)
                .map(this::setAmountTo2DP)
                .collect(Collectors.toList());
    }

    private BillingRecord.BillingRecordBuilder setAmountTo2DP(BillingRecord billingRecord) {
        BillingRecord.BillingRecordBuilder billingRecordBuilder = billingRecord.toBuilder();
        Optional.ofNullable(billingRecordBuilder)
                .map(BillingRecord.BillingRecordBuilder::getRecordTransfer)
                .map(Transfer.TransferBuilder::getQuantity)
                .stream()
                .forEach(q -> q.setAmount(q.getAmount().setScale(2, RoundingMode.CEILING)));
        return billingRecordBuilder;
    }
}
