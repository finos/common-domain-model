package cdm.product.template.processor;

import cdm.base.staticdata.asset.common.processor.FxMetaHelper;
import cdm.observable.asset.metafields.ReferenceWithMetaObservable;
import cdm.product.template.Underlier;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

@SuppressWarnings("unused") // used in generated code
public class AssetCashMetaMappingProcessor extends MappingProcessor {

    private final FxMetaHelper helper;
    
    public AssetCashMetaMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
        this.helper = new FxMetaHelper(context.getMappings());
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        helper.getCurrencySynonymPath(synonymPath)
                .ifPresent(currencyPath -> setReference(currencyPath, builder));
    }

    private void setReference(Path currencyPath, RosettaModelObjectBuilder builder) {
        if (builder instanceof Underlier.UnderlierBuilder) {
            Reference.ReferenceBuilder reference = ((Underlier.UnderlierBuilder) builder).getOrCreateObservable().getOrCreateReference();
            addMapping(currencyPath, "Observable", reference);
        } else if (builder instanceof ReferenceWithMetaObservable.ReferenceWithMetaObservableBuilder) {
            Reference.ReferenceBuilder reference = ((ReferenceWithMetaObservable.ReferenceWithMetaObservableBuilder) builder).getOrCreateReference();
            addMapping(currencyPath, null, reference);
        }
    }

    private void addMapping(Path currencySynonymPath, String modelPathElement, Reference.ReferenceBuilder reference) {
        // create new mapping to make the reference work
        RosettaPath modelPath = modelPathElement != null ? getModelPath().newSubPath(modelPathElement) : getModelPath();
        Path referencePath = PathUtils.toPath(modelPath).addElement("reference");
        getMappings().add(createSuccessMapping(currencySynonymPath, referencePath, reference));
    }

    private Mapping createSuccessMapping(Path xmlPath, Path modelPath, Reference.ReferenceBuilder reference) {
        return new Mapping(xmlPath, null, modelPath, reference, null, true, true, false);
    }
}
