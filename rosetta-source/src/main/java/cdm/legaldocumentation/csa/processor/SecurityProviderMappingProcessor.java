package cdm.legaldocumentation.csa.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

/**
 * CreateiQ mapping processor.
 */
@SuppressWarnings("unused")
public class SecurityProviderMappingProcessor extends MappingProcessor {


    public SecurityProviderMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        System.out.println("Security Provider Mapping Processor: " + synonymPath);
    }

    @Override
    public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {

        System.out.println("Security Provider Mapping Processor 2: " + synonymPath);

    }
}
