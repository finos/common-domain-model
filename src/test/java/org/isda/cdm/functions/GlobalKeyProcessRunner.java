package org.isda.cdm.functions;

import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.process.PostProcessor;

class GlobalKeyProcessRunner implements PostProcessor {
    @Override
    public <T extends RosettaModelObject> RosettaModelObjectBuilder postProcess(Class<T> rosettaType, RosettaModelObjectBuilder instance) {
        new GlobalKeyProcessStep(NonNullHashCollector::new).runProcessStep(rosettaType, instance);
        return instance;
    }
}
