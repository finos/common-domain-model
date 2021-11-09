package cdm.event.common.functions;

import cdm.event.common.BillingRecord;
import cdm.event.common.BillingRecordInstruction;
import cdm.event.common.TransferBase;

import javax.inject.Inject;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Create_BillingRecordsImpl extends Create_BillingRecords {

	@Inject
    Create_BillingRecord create_billingRecord;

    @Override
    protected List<BillingRecord.BillingRecordBuilder> doEvaluate(List<? extends BillingRecordInstruction> billingRecordInstructions) {
		Optional<List<? extends BillingRecordInstruction>> opt = Optional.ofNullable(billingRecordInstructions);
		return opt.map(recordInstructions -> recordInstructions
				.stream()
				.map(create_billingRecord::evaluate)
				.map(this::setAmountTo2DP)
				.collect(Collectors.toList())).orElse(Collections.emptyList());
    }

	private BillingRecord.BillingRecordBuilder setAmountTo2DP(BillingRecord billingRecord) {
		BillingRecord.BillingRecordBuilder billingRecordBuilder = billingRecord.toBuilder();
		Optional.ofNullable(billingRecordBuilder)
				.map(BillingRecord.BillingRecordBuilder::getRecordTransfer)
				.map(TransferBase.TransferBaseBuilder::getQuantity)
				.ifPresent(q -> q.setAmount(q.getAmount().setScale(2, RoundingMode.CEILING)));
		return billingRecordBuilder;
	}
}