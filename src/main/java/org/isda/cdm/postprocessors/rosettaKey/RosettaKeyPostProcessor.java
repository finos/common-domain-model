package org.isda.cdm.postprocessors.rosettaKey;

import com.regnosys.rosetta.common.ingestor.postprocess.PostProcessor;
import com.regnosys.rosetta.common.ingestor.postprocess.PostProcessorReport;
import com.rosetta.model.lib.RosettaModelObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Takes a RosettaModelObject and
 */
public class RosettaKeyPostProcessor implements PostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RosettaKeyPostProcessor.class);

    @Override
    public <T extends RosettaModelObject> T process(Class<T> rosettaType, T instance) {
        return instance;
    }

    @Override
    public Optional<PostProcessorReport> getReport() {
        return Optional.empty();
    }

}
