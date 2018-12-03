package org.isda.cdm.postprocessors.rosettaKey;

import com.rosetta.model.lib.HashToString;

public class DefaultHashToString implements HashToString<Integer> {

    @Override
    public String toString(Integer hash) {
        return Integer.toHexString(hash);
    }

}
