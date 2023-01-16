package cdm.base.staticdata.asset.common.processor;

import cdm.base.staticdata.asset.common.TaxonomySourceEnum;
import cdm.base.staticdata.asset.common.TaxonomyValue;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.MappingProcessorUtils;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

import static cdm.base.staticdata.asset.common.ProductTaxonomy.ProductTaxonomyBuilder;

@SuppressWarnings("unused")
public class TaxonomySourceMappingProcessor extends MappingProcessor {

    public TaxonomySourceMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        MappingProcessorUtils.getNonNullMappingForModelPath(getMappings(), PathUtils.toPath(getModelPath().getParent()))
                .map(m -> m.getXmlPath())
                .ifPresent(xmlPath -> {
                    ProductTaxonomyBuilder productTaxonomyBuilder = (ProductTaxonomyBuilder) parent;
                    TaxonomyValue.TaxonomyValueBuilder taxonomyValueBuilder = (TaxonomyValue.TaxonomyValueBuilder) builder;

                    updateSchemeAndSource(xmlPath, productTaxonomyBuilder, taxonomyValueBuilder);

                    // If unset, set to OTHER
                    if (productTaxonomyBuilder.getSource() == null) {
                        productTaxonomyBuilder.setSource(TaxonomySourceEnum.OTHER);
                    }
                });
    }

    protected void updateSchemeAndSource(Path xmlPath, ProductTaxonomyBuilder productTaxonomyBuilder, TaxonomyValue.TaxonomyValueBuilder taxonomyValueBuilder) {
        setValueAndUpdateMappings(xmlPath.addElement("productTypeScheme"),
                xmlValue -> {
                    // Update scheme
                    taxonomyValueBuilder.getOrCreateName().getOrCreateMeta().setScheme(xmlValue);
                    // Update taxonomySource
                    productTaxonomyBuilder.setSource(getTaxonomySourceEnum(xmlValue));
                });

        setValueAndUpdateMappings(xmlPath,
                xmlValue -> {
                    // Update name
                    taxonomyValueBuilder.setNameValue(xmlValue);
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