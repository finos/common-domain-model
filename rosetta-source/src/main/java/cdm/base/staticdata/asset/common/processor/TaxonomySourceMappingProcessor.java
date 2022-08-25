package cdm.base.staticdata.asset.common.processor;

import cdm.base.staticdata.asset.common.ProductTaxonomy;
import cdm.base.staticdata.asset.common.TaxonomySourceEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.MappingProcessorUtils;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;

import java.util.List;

@SuppressWarnings("unused")
public class TaxonomySourceMappingProcessor extends MappingProcessor {

    public TaxonomySourceMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        MappingProcessorUtils.getNonNullMappingForModelPath(getMappings(), PathUtils.toPath(getModelPath()))
                .map(m -> m.getXmlPath())
                .ifPresent(xmlPath ->
                        setValueAndUpdateMappings(xmlPath.addElement("productTypeScheme"),
                                xmlValue -> {
                                    // Update scheme
                                    ((FieldWithMetaString.FieldWithMetaStringBuilder) builder).getOrCreateMeta().setScheme(xmlValue);
                                    // Update taxonomySource
                                    ((ProductTaxonomy.ProductTaxonomyBuilder) parent).setTaxonomySource(getTaxonomySourceEnum(xmlValue));
                                }));
    }

    private TaxonomySourceEnum getTaxonomySourceEnum(String scheme) {
        if (scheme.contains("product-taxonomy")) {
            return TaxonomySourceEnum.ISDA;
        } else if (scheme.contains("iso10962")) {
            return TaxonomySourceEnum.CFI;
        } else if (scheme.contains("emir-contract-type")) {
            return TaxonomySourceEnum.EMIR;
        } else {
            return TaxonomySourceEnum.OTHER;
        }
    }
}