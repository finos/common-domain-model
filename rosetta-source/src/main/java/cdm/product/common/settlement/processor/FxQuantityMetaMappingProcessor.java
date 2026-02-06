package cdm.product.common.settlement.processor;

import cdm.base.math.metafields.ReferenceWithMetaNonNegativeQuantitySchedule;
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

@SuppressWarnings("unused")
public class FxQuantityMetaMappingProcessor extends MappingProcessor {

    private final FxMetaHelper fxHelper;

    public FxQuantityMetaMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
        this.fxHelper = new FxMetaHelper(context.getMappings());
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        fxHelper.getExchangedCurrencyPath(synonymPath).ifPresent(exchangedCurrencyPath -> {
            ReferenceWithMetaNonNegativeQuantitySchedule.ReferenceWithMetaNonNegativeQuantityScheduleBuilder quantityScheduleBuilder = (ReferenceWithMetaNonNegativeQuantitySchedule.ReferenceWithMetaNonNegativeQuantityScheduleBuilder) builder;
            Reference.ReferenceBuilder referenceBuilder = quantityScheduleBuilder.getOrCreateReference();
            Path amountPath = exchangedCurrencyPath.addElement("paymentAmount").addElement("currency");
            addMapping(amountPath, referenceBuilder);
        });
    }

    private void addMapping(Path synonymPath, Reference.ReferenceBuilder reference) {
        // create new mapping to make the reference work
        Path referencePath = PathUtils.toPath(getModelPath()).addElement("reference");
        getMappings().add(createSuccessMapping(synonymPath, referencePath, reference));
    }

    private Mapping createSuccessMapping(Path xmlPath, Path modelPath, Reference.ReferenceBuilder reference) {
        return new Mapping(xmlPath, null, modelPath, reference, null, true, true, false);
    }
}
