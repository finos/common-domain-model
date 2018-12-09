package org.isda.cdm.rosettakey;

import com.rosetta.model.lib.HashFunction;
import com.rosetta.model.lib.RosettaKeyValue;

public class RosettaKeyValueHashFunction extends IntegerHashHelper implements HashFunction<RosettaKeyValue, Integer> {

    @Override
    public Integer hash(RosettaKeyValue modelObject) {
        return modelObject.computeRosettaKeyValueHash(this);
    }

}
