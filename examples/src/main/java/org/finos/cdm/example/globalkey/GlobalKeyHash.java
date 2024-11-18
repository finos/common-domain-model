package org.finos.cdm.example.globalkey;

import cdm.product.asset.InterestRatePayout;
import cdm.product.template.Payout;
import org.finos.cdm.example.InterestRatePayoutCreation;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.metafields.MetaFields;

import java.math.BigDecimal;

/**
 * A demonstration of how to set the Global Key using a {@link PostProcessStep}
 */
public class GlobalKeyHash {

    public static void main(String[] args) {

        // Create a CDM object.
        //
        InterestRatePayout fixedRatePayout = InterestRatePayoutCreation.getFixedRatePayout(BigDecimal.valueOf(0.05));
        Payout payout = Payout.builder().setInterestRatePayout(fixedRatePayout);


        // Assert the globalKey has not been set
        //
        assert payout.getMeta().getGlobalKey() == null : "globalKey should be null";


        // Use the a HashFunction to generate a hash value for the fixedRatePayout object created above.
        //
        String hash = new SerialisingHashFunction().hash(payout);


        // Now, set the computed global key onto the original object. Note that toBuilder()
        // creates a new instance of the object.
        //
        Payout withGlobalKey = payout.toBuilder().setMeta(MetaFields.builder().setGlobalKey(hash).build()).build();

        System.out.println("globalKey is: " + withGlobalKey.getMeta().getGlobalKey());
    }

}
