package cdm.ingest.fpml.confirmation.pricequantity.functions;

import cdm.observable.asset.metafields.ReferenceWithMetaPriceSchedule;
import com.rosetta.model.lib.meta.Reference;

public class CreatePriceWithAddressImpl extends CreatePriceWithAddress {

    @Override
    protected ReferenceWithMetaPriceSchedule.ReferenceWithMetaPriceScheduleBuilder doEvaluate(String keyValue) {
        return ReferenceWithMetaPriceSchedule.builder()
                .setReference(Reference.builder()
                        .setReference(keyValue)
                        .setScope("DOCUMENT"));
    }
}
