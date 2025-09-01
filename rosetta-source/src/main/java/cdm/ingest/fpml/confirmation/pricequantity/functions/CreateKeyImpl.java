package cdm.ingest.fpml.confirmation.pricequantity.functions;

import fpml.confirmation.Leg;

import java.util.Objects;
import java.util.Optional;

public class CreateKeyImpl extends CreateKey {

    @Override
    protected String doEvaluate(String keyPrefix, String id, Leg fpmlLeg) {
        int index = (Optional.ofNullable(keyPrefix).map(Objects::hashCode).orElse(2) +
                Optional.ofNullable(id).map(Objects::hashCode).orElse(3) +
                Optional.ofNullable(fpmlLeg).map(Objects::hashCode).orElse(4)) % 1000;
        return String.format("%s-$%d",
                Optional.ofNullable(keyPrefix).orElse("empty"),
                Math.abs(index));
    }
}
