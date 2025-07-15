package cdm.ingest.fpml.confirmation.pricequantity.functions;

import cdm.observable.asset.PriceSchedule;
import cdm.observable.asset.metafields.FieldWithMetaPriceSchedule;
import com.rosetta.model.lib.meta.Key;
import com.rosetta.model.metafields.MetaFields;

public class CreatePriceWithLocationImpl extends CreatePriceWithLocation {

    @Override
    protected FieldWithMetaPriceSchedule.FieldWithMetaPriceScheduleBuilder doEvaluate(PriceSchedule price, String keyValue) {
        return FieldWithMetaPriceSchedule.builder()
                .setValue(price)
                .setMeta(MetaFields.builder()
                        .addKey(Key.builder()
                                .setKeyValue(keyValue)
                                .setScope("DOCUMENT")));
    }
}
