package cdm.ingest.fpml.confirmation.pricequantity.functions;

import cdm.observable.asset.BasketConstituent;
import cdm.observable.asset.metafields.FieldWithMetaBasketConstituent;
import com.rosetta.model.lib.meta.Key;
import com.rosetta.model.metafields.MetaFields;

public class CreateBasketConstituentWithLocationImpl extends CreateBasketConstituentWithLocation{
    @Override
    protected FieldWithMetaBasketConstituent.FieldWithMetaBasketConstituentBuilder doEvaluate(BasketConstituent basketConstituent, String keyValue) {
        return FieldWithMetaBasketConstituent.builder()
                .setValue(basketConstituent)
                .setMeta(MetaFields.builder()
                        .addKey(Key.builder()
                                .setKeyValue(keyValue)
                                .setScope("DOCUMENT")));
    }
}
