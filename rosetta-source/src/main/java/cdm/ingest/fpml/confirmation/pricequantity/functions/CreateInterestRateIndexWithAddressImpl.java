package cdm.ingest.fpml.confirmation.pricequantity.functions;

import cdm.observable.asset.metafields.ReferenceWithMetaInterestRateIndex;
import com.rosetta.model.lib.meta.Reference;

public class CreateInterestRateIndexWithAddressImpl extends CreateInterestRateIndexWithAddress {

    @Override
    protected ReferenceWithMetaInterestRateIndex.ReferenceWithMetaInterestRateIndexBuilder doEvaluate(String keyValue) {
        return ReferenceWithMetaInterestRateIndex.builder()
                .setReference(Reference.builder()
                        .setReference(keyValue)
                        .setScope("DOCUMENT"));
    }
}
