package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.AmendmentEffectiveDate;
import cdm.legaldocumentation.csa.AmendmentEffectiveDateEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMappedValue;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;

/**
 * CreateiQ mapping processor.
 */
@SuppressWarnings("unused")
public class EffectivenessDateMappingProcessor extends MappingProcessor {
    public EffectivenessDateMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
        AmendmentEffectiveDate.AmendmentEffectiveDateBuilder amendmentEffectiveDateBuilder = (AmendmentEffectiveDate.AmendmentEffectiveDateBuilder) parent;

        Optional<String> mappedValue = getNonNullMappedValue(synonymPath, getMappings());

        if (mappedValue.orElse("").equals("other")) {
            setValueAndUpdateMappings(synonymPath, value -> {});
        } else {
            setValueAndOptionallyUpdateMappings(synonymPath, value -> {
                instance.ifPresent(i -> {
                    amendmentEffectiveDateBuilder.setSpecificDate((AmendmentEffectiveDateEnum) i);
                });
                return instance.isPresent();
            }, getMappings(), getModelPath());
        }
    }

}
