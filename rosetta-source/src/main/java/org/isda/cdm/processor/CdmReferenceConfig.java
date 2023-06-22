package org.isda.cdm.processor;

import cdm.event.common.TradeState;
import com.regnosys.rosetta.common.hashing.ReferenceConfig;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.Arrays;

/**
 * Reference resolver config for CDM use-case.  All scope references are unique within a TradeState.
 */
public class CdmReferenceConfig {

    public static ReferenceConfig get() {
        return new ReferenceConfig(
                TradeState.class,
                Arrays.asList(RosettaPath.valueOf("lineage")));
    }
}

