package org.isda.cdm.processor;

import cdm.event.common.TradeState;
import com.regnosys.rosetta.common.hashing.ReferenceResolverConfig;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.Arrays;

/**
 * Reference resolver config for CDM use-case.  All scope references are unique within a TradeState.
 */
public class CdmReferenceResolverConfig {

    public static ReferenceResolverConfig get() {
        return new ReferenceResolverConfig(
                TradeState.class,
                Arrays.asList(RosettaPath.valueOf("lineage"), RosettaPath.valueOf("eventEffect")));
    }
}

