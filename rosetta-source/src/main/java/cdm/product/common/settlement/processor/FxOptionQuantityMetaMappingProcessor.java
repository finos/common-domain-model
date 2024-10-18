package cdm.product.common.settlement.processor;

import cdm.base.staticdata.asset.common.processor.FxMetaHelper;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

import static cdm.base.math.metafields.ReferenceWithMetaNonNegativeQuantitySchedule.ReferenceWithMetaNonNegativeQuantityScheduleBuilder;

@SuppressWarnings("unused") // used in generated code
public class FxOptionQuantityMetaMappingProcessor extends MappingProcessor {

    private final FxMetaHelper helper;

    public FxOptionQuantityMetaMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
        this.helper = new FxMetaHelper(context.getMappings());
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        helper.getStrikeQuoteBasisCurrencySynonymPath(synonymPath)
                .map(currencyPath -> currencyPath.getParent().addElement("amount"))
                .ifPresent(amountPath -> {
                    ReferenceWithMetaNonNegativeQuantityScheduleBuilder quantityScheduleBuilder = (ReferenceWithMetaNonNegativeQuantityScheduleBuilder) builder;
                    Reference.ReferenceBuilder referenceBuilder = quantityScheduleBuilder.getOrCreateReference();
                    addMapping(amountPath, referenceBuilder);
                });
    }

    private void addMapping(Path currencySynonymPath, Reference.ReferenceBuilder reference) {
        // create new mapping to make the reference work
        Path referencePath = PathUtils.toPath(getModelPath()).addElement("reference");
        getMappings().add(createSuccessMapping(currencySynonymPath, referencePath, reference));
    }

    private Mapping createSuccessMapping(Path xmlPath, Path modelPath, Reference.ReferenceBuilder reference) {
        return new Mapping(xmlPath, null, modelPath, reference, null, true, true, false);
    }
}
