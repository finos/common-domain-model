package cdm.ingest.fpml.confirmation.pricequantity.functions;

import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule;
import com.rosetta.model.lib.meta.Key;
import com.rosetta.model.metafields.MetaFields;

public class CreateQuantityWithLocationImpl extends CreateQuantityWithLocation {

    @Override
    protected FieldWithMetaNonNegativeQuantitySchedule.FieldWithMetaNonNegativeQuantityScheduleBuilder doEvaluate(NonNegativeQuantitySchedule quantity, String keyValue) {
        return FieldWithMetaNonNegativeQuantitySchedule.builder()
                .setValue(quantity)
                .setMeta(MetaFields.builder()
                        .addKey(Key.builder()
                                .setKeyValue(keyValue)
                                .setScope("DOCUMENT")));
    }
}
