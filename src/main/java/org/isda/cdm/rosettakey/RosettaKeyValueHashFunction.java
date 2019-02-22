package org.isda.cdm.rosettakey;

import com.rosetta.model.lib.HashFunction;
import com.rosetta.model.lib.RosettaKeyValue;

/**
 * Combines a {@link NonNullHashCollector} and {@link HashFunction} so that we have an easy way of asking a
 * {@link RosettaKeyValue} object for its hash value. The {@link NonNullHashCollector} ignores black values such that
 * the hashcode can be used as a close proxy to equivalence.
 */
public class RosettaKeyValueHashFunction extends NonNullHashCollector implements HashFunction<RosettaKeyValue, Integer> {

    @Override
    public Integer hash(RosettaKeyValue modelObject) {
        return modelObject.computeRosettaKeyValueHash(this);
    }

}
