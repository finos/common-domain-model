package cdm.base.staticdata.asset.common.processor;

import cdm.base.staticdata.asset.common.TaxonomySourceEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.MappingProcessorUtils;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

import static cdm.base.staticdata.asset.common.ProductTaxonomy.ProductTaxonomyBuilder;
import static com.rosetta.model.metafields.FieldWithMetaString.FieldWithMetaStringBuilder;

@SuppressWarnings("unused")
public class TaxonomySourceMappingProcessor extends MappingProcessor {

    public TaxonomySourceMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        MappingProcessorUtils.getNonNullMappingForModelPath(getMappings(), PathUtils.toPath(getModelPath().newSubPath("value")))
                .map(m -> m.getXmlPath())
                .ifPresent(xmlPath -> {
                    ProductTaxonomyBuilder productTaxonomyBuilder = (ProductTaxonomyBuilder) parent;
                    FieldWithMetaStringBuilder taxonomyValueBuilder = (FieldWithMetaStringBuilder) builder;

                    updateSchemeAndSource(xmlPath, productTaxonomyBuilder, taxonomyValueBuilder);

                    // If unset, set to OTHER
                    if (productTaxonomyBuilder.getTaxonomySource() == null) {
                        productTaxonomyBuilder.setTaxonomySource(TaxonomySourceEnum.OTHER);
                    }
                });
    }

    protected void updateSchemeAndSource(Path xmlPath, ProductTaxonomyBuilder productTaxonomyBuilder, FieldWithMetaStringBuilder taxonomyValueBuilder) {
        setValueAndUpdateMappings(xmlPath.addElement("productTypeScheme"),
                xmlValue -> {
                    // Update scheme
                    taxonomyValueBuilder.getOrCreateMeta().setScheme(xmlValue);
                    // Update taxonomySource
                    productTaxonomyBuilder.setTaxonomySource(getTaxonomySourceEnum(xmlValue));
                });
    }

    protected TaxonomySourceEnum getTaxonomySourceEnum(String scheme) {
        if (scheme.contains("www.fpml.org/coding-scheme/product-taxonomy")) {
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