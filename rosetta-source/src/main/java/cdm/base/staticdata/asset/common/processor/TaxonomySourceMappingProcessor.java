package cdm.base.staticdata.asset.common.processor;

import cdm.base.staticdata.asset.common.TaxonomySourceEnum;
import com.regnosys.rosetta.common.translation.*;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static cdm.base.staticdata.asset.common.ProductTaxonomy.ProductTaxonomyBuilder;

@SuppressWarnings("unused")
public class TaxonomySourceMappingProcessor extends MappingProcessor {

    public TaxonomySourceMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
    }

    @Override
    public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
        Path productTaxonomyModelPath = PathUtils.toPath(getModelPath()).getParent();
        Path taxomomyValueModelPath = productTaxonomyModelPath.addElement("value");
        Path nameModelPath = taxomomyValueModelPath.addElement("name").addElement("value");
        // Find xml path from name model path due to mapping bug where schemes on multi-cardinality basic types get
        // mapped to the wrong list item
        MappingProcessorUtils.getNonNullMappingForModelPath(getMappings(), nameModelPath)
                .map(Mapping::getXmlPath)
                .ifPresent(xmlPath -> {
                    ProductTaxonomyBuilder productTaxonomyBuilder = (ProductTaxonomyBuilder) parent;
                    updateSchemeAndSource(xmlPath, productTaxonomyBuilder);

                    // If unset, set to OTHER
                    if ( productTaxonomyBuilder.getValue() != null && productTaxonomyBuilder.getSource() == null) {
                        productTaxonomyBuilder.setSource(TaxonomySourceEnum.OTHER);
                    }
                });
    }
    protected void updateSchemeAndSource(Path xmlPath, ProductTaxonomyBuilder productTaxonomyBuilder) {
        setValueAndUpdateMappings(xmlPath.addElement("productTypeScheme"),
                xmlValue -> {
                    if ("http://www.fpml.org/coding-scheme/esma-emir-refit-crypto-asset-indicator".equals(xmlValue)) {
                        // If the value is equal to the ignored value, set productTaxonomy to null
                        productTaxonomyBuilder.setValue(null);
                    } else {
                        // Update scheme
                        productTaxonomyBuilder.getOrCreateValue().getOrCreateName().getOrCreateMeta().setScheme(xmlValue);
                        // Update taxonomySource
                        productTaxonomyBuilder.setSource(getTaxonomySourceEnum(xmlValue));
                    }
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
