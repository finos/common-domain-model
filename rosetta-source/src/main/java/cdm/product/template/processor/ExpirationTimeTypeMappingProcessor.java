package cdm.product.template.processor;

import cdm.product.template.ExerciseTerms;
import cdm.product.template.ExpirationTimeTypeEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused") // unused in generated code
public class ExpirationTimeTypeMappingProcessor extends MappingProcessor {
    
    public ExpirationTimeTypeMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        ExerciseTerms.ExerciseTermsBuilder exerciseTermsBuilder = (ExerciseTerms.ExerciseTermsBuilder) parent;
        if (isExpirationTimeSet(exerciseTermsBuilder)) {
            exerciseTermsBuilder.setExpirationTimeType(ExpirationTimeTypeEnum.SPECIFIC_TIME);
        }
    }

    private boolean isExpirationTimeSet(ExerciseTerms.ExerciseTermsBuilder exerciseTermsBuilder) {
        return Optional.ofNullable(exerciseTermsBuilder.getExpirationTime())
                .map(RosettaModelObjectBuilder::hasData)
                .orElse(false);
    }
}
