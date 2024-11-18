package cdm.base.staticdata.asset.common.processor;

import cdm.base.staticdata.asset.common.ProductIdTypeEnum;
import cdm.base.staticdata.asset.common.ProductIdentifier;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.MappingProcessorUtils;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;

import java.util.List;

/**
 * Update product identifier source enum based on the instrumentIdScheme or productIdScheme.
 *
 * @see cdm.base.staticdata.asset.common.processor.AssetIdentifierTypeMappingProcessor
 */
@SuppressWarnings("unused")
public class ProductIdentifierSourceMappingProcessor extends MappingProcessor {


    public ProductIdentifierSourceMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
            MappingProcessorUtils.getNonNullMappingForModelPath(getMappings(), PathUtils.toPath(getModelPath().newSubPath("value")))
                .map(m -> m.getXmlPath())
                .ifPresent(xmlPath -> {
                    ProductIdentifier.ProductIdentifierBuilder productIdentifierBuilder = (ProductIdentifier.ProductIdentifierBuilder) parent;
                    FieldWithMetaString.FieldWithMetaStringBuilder productIdentifierValueBuilder = (FieldWithMetaString.FieldWithMetaStringBuilder) builder;

                    updateSchemeAndSource(xmlPath, productIdentifierBuilder, productIdentifierValueBuilder);

                    // If unset, set to OTHER
                    if (productIdentifierBuilder.getSource() == null) {
                        productIdentifierBuilder.setSource(ProductIdTypeEnum.OTHER);
                    }
                });
    }

    protected void updateSchemeAndSource(Path xmlPath, ProductIdentifier.ProductIdentifierBuilder productIdentifierBuilder, FieldWithMetaString.FieldWithMetaStringBuilder productIdentifierValueBuilder) {
        setValueAndUpdateMappings(xmlPath.addElement("productIdScheme"),
                xmlValue -> {
                    // Update scheme
                    productIdentifierValueBuilder.getOrCreateMeta().setScheme(xmlValue);
                    // Update Source
                    productIdentifierBuilder.setSource(getSourceEnum(xmlValue));
                });
    }

    protected ProductIdTypeEnum getSourceEnum(String scheme) {
        if (scheme.contains("CUSIP")) {
            return ProductIdTypeEnum.CUSIP;
        } else if (scheme.contains("ISIN")) {
            return ProductIdTypeEnum.ISIN;
        } else if (scheme.contains("RIC")) {
            return ProductIdTypeEnum.RIC;
        } else if (scheme.contains("Bloomberg")){
            return ProductIdTypeEnum.BBGID;
        } else if (scheme.contains("commodity-reference-price")){
            return ProductIdTypeEnum.ISDACRP;
        } else if (scheme.contains("iso4914")) {
            return ProductIdTypeEnum.UPI;
        } else {
            return ProductIdTypeEnum.OTHER;
        }
    }
}
