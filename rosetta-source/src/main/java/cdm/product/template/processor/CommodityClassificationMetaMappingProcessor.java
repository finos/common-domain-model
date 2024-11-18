package cdm.product.template.processor;

import cdm.product.template.SettlementPayout;
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
public class CommodityClassificationMetaMappingProcessor extends MappingProcessor {

    public CommodityClassificationMetaMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        SettlementPayout.SettlementPayoutBuilder settlementPayoutBuilder =
                (SettlementPayout.SettlementPayoutBuilder) builder;
        // create reference
        Reference.ReferenceBuilder referenceBuilder =
                settlementPayoutBuilder.getOrCreateUnderlier().getOrCreateObservable().getOrCreateReference();
        // create new mapping to make the reference work
        Path commodityClassificationSynonymPath =
                synonymPath
                        .addElement("commodityClassification", 0)
                        .addElement("code", 0);
        Path modelPath = PathUtils.toPath(getModelPath());
        Path referenceModelPath = modelPath
                .addElement("underlier")
                .addElement("Observable")
                .addElement("reference");
        getMappings().add(createSuccessMapping(commodityClassificationSynonymPath, referenceModelPath, referenceBuilder));

    }

    private Mapping createSuccessMapping(Path xmlPath, Path modelPath, Reference.ReferenceBuilder reference) {
        return new Mapping(xmlPath, null, modelPath, reference, null, true, true, false);
    }
}