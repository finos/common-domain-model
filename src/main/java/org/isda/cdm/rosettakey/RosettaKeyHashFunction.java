package org.isda.cdm.rosettakey;

import com.rosetta.model.lib.HashFunction;
import com.rosetta.model.lib.RosettaKey;

public class RosettaKeyHashFunction extends IntegerHashHelper implements HashFunction<RosettaKey, Integer> {

    @Override
    public Integer hash(RosettaKey modelObject) {
        return modelObject.computeRosettaKeyHash(this);
    }

}
