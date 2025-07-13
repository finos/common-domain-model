package cdm.ingest.fpml.confirmation.pricequantity.functions;

import cdm.observable.asset.Observable;
import cdm.observable.asset.metafields.FieldWithMetaObservable;
import com.rosetta.model.lib.meta.Key;
import com.rosetta.model.metafields.MetaFields;

public class CreateObservableWithLocationImpl extends CreateObservableWithLocation {

    @Override
    protected FieldWithMetaObservable.FieldWithMetaObservableBuilder doEvaluate(Observable observable, String keyValue) {
        return FieldWithMetaObservable.builder()
                .setValue(observable)
                .setMeta(MetaFields.builder()
                        .addKey(Key.builder()
                                .setKeyValue(keyValue)
                                .setScope("DOCUMENT")));
    }
}
