package cdm.ingest.fpml.confirmation.pricequantity.functions;

import fpml.consolidated.shared.QuotedCurrencyPair;
import java.util.Objects;
import java.util.Optional;

public class CreateKeyForQuotedCurrencyPairImpl extends CreateKeyForQuotedCurrencyPair {
    @Override
    protected String doEvaluate(String keyPrefix, QuotedCurrencyPair fpmlQuotedCurrencyPair) {
        int index = (Optional.ofNullable(keyPrefix).map(Objects::hashCode).orElse(2) +
                Optional.ofNullable(fpmlQuotedCurrencyPair).map(Objects::hashCode).orElse(3)) % 1000;
        return String.format("%s-$%d",
                Optional.ofNullable(keyPrefix).orElse("empty"),
                Math.abs(index));
    }
}
