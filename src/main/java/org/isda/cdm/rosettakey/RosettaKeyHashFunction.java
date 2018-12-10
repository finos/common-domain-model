package org.isda.cdm.rosettakey;

import com.rosetta.model.lib.HashFunction;
import com.rosetta.model.lib.RosettaKey;

/**
 * Combines a {@link com.rosetta.model.lib.HashHelper} and {@link HashFunction} so that we have an easy way of asking a
 * {@link RosettaKey} model object for its hash value.
 */
public class RosettaKeyHashFunction extends IntegerHashHelper implements HashFunction<RosettaKey, Integer> {

    @Override
    public Integer hash(RosettaKey modelObject) {
        return modelObject.computeRosettaKeyHash(this);
    }

}
