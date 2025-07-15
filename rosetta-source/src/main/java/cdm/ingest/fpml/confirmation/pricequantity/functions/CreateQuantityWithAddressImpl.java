package cdm.ingest.fpml.confirmation.pricequantity.functions;

import cdm.base.math.metafields.ReferenceWithMetaNonNegativeQuantitySchedule;
import com.rosetta.model.lib.meta.Reference;

public class CreateQuantityWithAddressImpl extends CreateQuantityWithAddress {

    @Override
    protected ReferenceWithMetaNonNegativeQuantitySchedule.ReferenceWithMetaNonNegativeQuantityScheduleBuilder doEvaluate(String keyValue) {
        return ReferenceWithMetaNonNegativeQuantitySchedule.builder()
                .setReference(Reference.builder()
                        .setReference(keyValue)
                        .setScope("DOCUMENT"));
    }
}
