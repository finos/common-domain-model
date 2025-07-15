package cdm.ingest.fpml.confirmation.pricequantity.functions;

import cdm.observable.asset.metafields.ReferenceWithMetaObservable;
import com.rosetta.model.lib.meta.Reference;

public class CreateObservableWithAddressImpl extends CreateObservableWithAddress {

    @Override
    protected ReferenceWithMetaObservable.ReferenceWithMetaObservableBuilder doEvaluate(String keyValue) {
        return ReferenceWithMetaObservable.builder()
                .setReference(Reference.builder()
                        .setReference(keyValue)
                        .setScope("DOCUMENT"));
    }
}
