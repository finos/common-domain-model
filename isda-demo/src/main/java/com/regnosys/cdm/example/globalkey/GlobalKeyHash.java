package com.regnosys.cdm.example.globalkey;

import cdm.product.asset.InterestRatePayout;
import com.regnosys.cdm.example.InterestRatePayoutCreation;
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


        // Assert the globalKey has not been set
        //
        assert fixedRatePayout.getMeta().getGlobalKey() == null : "globalKey should be null";


        // Use the a HashFunction to generate a hash value for the fixedRatePayout object created above.
        //
        String hash = new SerialisingHashFunction().hash(fixedRatePayout);


        // Now, set the computed global key onto the original object. Note that toBuilder()
        // creates a new instance of the object.
        //
        InterestRatePayout withGlobalKey = fixedRatePayout.toBuilder().setMeta(MetaFields.builder().setGlobalKey(hash).build()).build();

        System.out.println("globalKey is: " + withGlobalKey.getMeta().getGlobalKey());
    }

}
