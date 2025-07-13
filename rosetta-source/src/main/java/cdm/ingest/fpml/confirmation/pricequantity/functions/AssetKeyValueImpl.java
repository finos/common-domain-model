package cdm.ingest.fpml.confirmation.pricequantity.functions;

import fpml.confirmation.Asset;

import java.util.Objects;
import java.util.Optional;

public class AssetKeyValueImpl extends AssetKeyValue {

    @Override
    protected String doEvaluate(String keyPrefix, Asset fpmlAsset) {
        int index = Optional.ofNullable(fpmlAsset).map(Objects::hashCode).orElse(2) % 1000;
        return String.format("%s-$%d",
                Optional.ofNullable(keyPrefix).orElse("empty"),
                Math.abs(index));
    }
}
