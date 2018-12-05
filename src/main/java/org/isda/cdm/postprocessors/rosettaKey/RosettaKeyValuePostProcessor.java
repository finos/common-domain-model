package org.isda.cdm.postprocessors.rosettaKey;

import com.regnosys.rosetta.common.inspection.Node;
import com.regnosys.rosetta.common.inspection.PathObject;
import com.regnosys.rosetta.common.inspection.PathObjectNode;
import com.regnosys.rosetta.common.inspection.RosettaNodeInspector;
import com.rosetta.lib.postprocess.PostProcessor;
import com.rosetta.lib.postprocess.PostProcessorReport;
import com.rosetta.model.lib.*;

import java.util.Optional;

public class RosettaKeyValuePostProcessor implements PostProcessor {

    private final HashFunction<Integer> hashFunction;
    private final HashToString<Integer> hashToString;

    public RosettaKeyValuePostProcessor(HashFunction<Integer> hashFunction, HashToString<Integer> hashToString) {
        this.hashFunction = hashFunction;
        this.hashToString = hashToString;
    }

    @Override
    public <R extends RosettaModelObject> R process(Class<R> rosettaType, R instance) {
        RosettaModelObjectBuilder<? extends RosettaModelObject> builder = instance.toBuilder();
        new RosettaNodeInspector<PathObject<Object>>().inspect(PathObjectNode.root(builder), visitor(), true);
        return rosettaType.cast(builder.build());
    }

    private RosettaNodeInspector.Visitor<PathObject<Object>> visitor() {
        return (Node<PathObject<Object>> node) -> {
            Object builder = node.get().getObject();

            if (builder instanceof RosettaKeyValueBuilder && builder instanceof RosettaModelObjectBuilder) {
                RosettaModelObject modelObject = ((RosettaModelObjectBuilder) builder).build();

                if (modelObject instanceof RosettaKeyValue) {
                    Integer rosettaKeyValueHash = ((RosettaKeyValue) modelObject).computeRosettaKeyValueHash(hashFunction);
                    String rosettaKeyValue = hashToString.toString(rosettaKeyValueHash);

                    ((RosettaKeyValueBuilder) builder).setRosettaKeyValue(rosettaKeyValue);
                }
            }
        };
    }

    @Override
    public Optional<PostProcessorReport> getReport() {
        return Optional.empty();
    }
}
