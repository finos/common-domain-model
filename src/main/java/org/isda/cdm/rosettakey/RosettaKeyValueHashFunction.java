package org.isda.cdm.rosettakey;

import com.rosetta.model.lib.HashFunction;
import com.rosetta.model.lib.RosettaKeyValue;

/**
 * Combines a {@link com.rosetta.model.lib.HashHelper} and {@link HashFunction} so that we have an easy way of asking a
 * {@link RosettaKeyValue} model object for its hash value.
 */
public class RosettaKeyValueHashFunction extends IntegerHashHelper implements HashFunction<RosettaKeyValue, Integer> {

    @Override
    public Integer hash(RosettaKeyValue modelObject) {
        return modelObject.computeRosettaKeyValueHash(this);
    }

}
