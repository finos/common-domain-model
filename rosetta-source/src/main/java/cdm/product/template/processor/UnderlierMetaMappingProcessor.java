package cdm.product.template.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

@SuppressWarnings("unused") // used in generated code
public class UnderlierMetaMappingProcessor extends MappingProcessor {

    public UnderlierMetaMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        // clean up duplicate mappings caused by references
        getMappings().stream()
                .filter(m -> m.getRosettaValue() instanceof Reference.ReferenceBuilder)
                .filter(m -> m.getError() != null)
                .forEach(m -> {
                    m.setDuplicate(false);
                    m.setError(null);
                });
    }
}
