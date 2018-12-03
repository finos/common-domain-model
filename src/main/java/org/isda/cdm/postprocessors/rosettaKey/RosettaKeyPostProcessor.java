package org.isda.cdm.postprocessors.rosettaKey;



import com.regnosys.rosetta.common.inspection.Node;
import com.regnosys.rosetta.common.inspection.PathObject;
import com.regnosys.rosetta.common.inspection.PathObjectNode;
import com.regnosys.rosetta.common.inspection.RosettaNodeInspector;
import com.rosetta.lib.postprocess.PostProcessor;
import com.rosetta.lib.postprocess.PostProcessorReport;
import com.rosetta.model.lib.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * A {@link PostProcessor} that uses calculates the hashcode of a {@link RosettaModelObject} and sets it onto the
 * instances of {@link com.rosetta.model.lib.RosettaKey}
 * @param <T> The type representing the hashcode
 */
public class RosettaKeyPostProcessor<T> implements PostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RosettaKeyPostProcessor.class);

    private final HashFunction<T> hashFunction;
    private final HashToString<T> hashToString;

    public RosettaKeyPostProcessor(HashFunction<T> hashFunction, HashToString<T> hashToString) {
        this.hashFunction = hashFunction;
        this.hashToString = hashToString;
    }

    /**
     * Takes a {@link RosettaModelObject}, searches for children that are {@link com.rosetta.model.lib.RosettaKey}s,
     * evaluates its hashcode and sets the rosettaKey attribute.
     *
     * @param <T> The concrete model object type
     * @return A new instance of the object input parameter, reflecting the effects of the post processor
     */
    @Override
    public <T extends RosettaModelObject> T process(Class<T> rosettaType, T instance) {
        RosettaModelObjectBuilder<? extends RosettaModelObject> builder = instance.toBuilder();
        new RosettaNodeInspector<PathObject<Object>>().inspect(PathObjectNode.root(builder), visitor(), true);
        return rosettaType.cast(builder.build());
    }

    private RosettaNodeInspector.Visitor<PathObject<Object>> visitor() {
        return (Node<PathObject<Object>> node) -> {
            Object builder = node.get().getObject();

            if (builder instanceof RosettaKeyBuilder) {
                if (builder instanceof RosettaModelObjectBuilder) {
                    T hash = ((RosettaModelObjectBuilder) builder).build().externalHash(hashFunction);
                    ((RosettaKeyBuilder) builder).setRosettaKey(hashToString.toString(hash));
                }
            }
        };
    }

    @Override
    public Optional<PostProcessorReport> getReport() {
        return Optional.empty();
    }

}
