package cdm.product.template.processor;

import cdm.product.template.ForwardPayout;
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
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
        List<ForwardPayout.ForwardPayoutBuilder> forwardPayoutBuilders = (List<ForwardPayout.ForwardPayoutBuilder>) builders;
        if (!forwardPayoutBuilders.isEmpty()) {
            ForwardPayout.ForwardPayoutBuilder forwardPayoutBuilder = forwardPayoutBuilders.get(0);
            // create reference
            Reference.ReferenceBuilder referenceBuilder =
                    forwardPayoutBuilder.getOrCreateUnderlier().getOrCreateCommodity().getOrCreateReference();
            // create new mapping to make the reference work
            Path commodityClassificationSynonymPath =
                    synonymPath.addElement("commodityClassification", 0).addElement("code", 0);
            getMappings().add(createSuccessMapping(commodityClassificationSynonymPath, getModelPath(), referenceBuilder));
        }
    }

    private Mapping createSuccessMapping(Path xmlPath, RosettaPath modelPath, Reference.ReferenceBuilder reference) {
        return new Mapping(xmlPath, null, PathUtils.toPath(modelPath), reference, null, true, true, false);
    }
}