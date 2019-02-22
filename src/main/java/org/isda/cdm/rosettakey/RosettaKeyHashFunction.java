package org.isda.cdm.rosettakey;

import com.rosetta.model.lib.HashFunction;
import com.rosetta.model.lib.RosettaKey;

/**
 * Combines a {@link NonNullHashCollector} and {@link HashFunction} so that we have an easy way of asking a
 * {@link RosettaKey} model object for its hash value. The {@link NonNullHashCollector} ignores black values such that
 *  * the hashcode can be used as a close proxy to equivalence.
 */
public class RosettaKeyHashFunction extends NonNullHashCollector implements HashFunction<RosettaKey, Integer> {

    @Override
    public Integer hash(RosettaKey modelObject) {
        return modelObject.computeRosettaKeyHash(this);
    }

}
