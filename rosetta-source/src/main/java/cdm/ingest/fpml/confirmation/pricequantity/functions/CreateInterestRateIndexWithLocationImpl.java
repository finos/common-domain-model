package cdm.ingest.fpml.confirmation.pricequantity.functions;

import cdm.observable.asset.InterestRateIndex;
import cdm.observable.asset.metafields.FieldWithMetaInterestRateIndex;
import com.rosetta.model.lib.meta.Key;
import com.rosetta.model.metafields.MetaFields;

public class CreateInterestRateIndexWithLocationImpl extends CreateInterestRateIndexWithLocation {

    @Override
    protected FieldWithMetaInterestRateIndex.FieldWithMetaInterestRateIndexBuilder doEvaluate(InterestRateIndex interestRateIndex, String keyValue) {
        return FieldWithMetaInterestRateIndex.builder()
                .setValue(interestRateIndex)
                .setMeta(MetaFields.builder()
                        .addKey(Key.builder()
                                .setKeyValue(keyValue)
                                .setScope("DOCUMENT")));
    }
}
