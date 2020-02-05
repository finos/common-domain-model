package org.isda.cdm.sequences;

import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.lib.process.PostProcessorRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SequencePostProcessRunner implements PostProcessorRunner {
    private static final Logger LOG = LoggerFactory.getLogger(SequencePostProcessRunner.class);
    private final List<PostProcessStep> postProcessors;

    public SequencePostProcessRunner(PostProcessStep... postProcessors) {
        this.postProcessors = Arrays.asList(postProcessors);
        this.postProcessors.sort(Comparator.comparingInt(PostProcessStep::getPriority));
    }

    public SequencePostProcessRunner(List<PostProcessStep> postProcessors) {
        this.postProcessors = postProcessors;
        this.postProcessors.sort(Comparator.comparingInt(PostProcessStep::getPriority));
    }

    @Override
    public <T extends RosettaModelObject> RosettaModelObjectBuilder postProcess(Class<T> rosettaType, RosettaModelObjectBuilder instance) {
        for (PostProcessStep postProcessor : postProcessors) {
            postProcessor.runProcessStep(rosettaType, instance);
        }
        return instance;
    }
}
