package cdm.base.staticdata.asset.common.processor;

import cdm.base.staticdata.asset.common.AssetIdTypeEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.MappingProcessorUtils;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

import static cdm.base.staticdata.asset.common.AssetIdentifier.AssetIdentifierBuilder;

/**
 * Update asset identifier type enum based on the instrumentIdScheme or productIdScheme.
 * 
 * @see cdm.base.staticdata.asset.common.processor.ProductIdentifierSourceMappingProcessor
 */
@SuppressWarnings("unused")
public class AssetIdentifierTypeMappingProcessor extends MappingProcessor {
    
    public AssetIdentifierTypeMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public <T> void mapBasic(Path synonymPath, T instance, RosettaModelObjectBuilder parent) {
        MappingProcessorUtils.getNonNullMappingForModelPath(getMappings(), PathUtils.toPath(getModelPath()))
                .map(m -> m.getXmlPath())
                .ifPresent(xmlPath -> {
                    AssetIdentifierBuilder assetIdentifierBuilder = (AssetIdentifierBuilder) parent;
                    updateSchemeAndSource(xmlPath, assetIdentifierBuilder);

                    // If unset, set to OTHER
                    if (assetIdentifierBuilder.getIdentifierType() == null) {
                        assetIdentifierBuilder.setIdentifierType(AssetIdTypeEnum.OTHER);
                    }
                });
    }

    protected void updateSchemeAndSource(Path xmlPath, AssetIdentifierBuilder assetIdentifierBuilder) {
        setValueAndUpdateMappings(xmlPath.addElement("instrumentIdScheme"),
                xmlValue -> {
                    // Update Source
                    assetIdentifierBuilder.setIdentifierType(getSourceEnum(xmlValue));
                });
        setValueAndUpdateMappings(xmlPath.addElement("productIdScheme"),
                xmlValue -> {
                    // Update Source
                    assetIdentifierBuilder.setIdentifierType(getSourceEnum(xmlValue));
                });
        if (xmlPath.endsWith("description")) {
            assetIdentifierBuilder.setIdentifierType(AssetIdTypeEnum.NAME);
        }
    }

    protected AssetIdTypeEnum getSourceEnum(String scheme) {
        if (scheme.contains("CUSIP")) {
            return AssetIdTypeEnum.CUSIP;
        } else if (scheme.contains("ISIN")) {
            return AssetIdTypeEnum.ISIN;
        } else if (scheme.contains("RIC")) {
            return AssetIdTypeEnum.RIC;
        } else if (scheme.contains("Bloomberg")){
            return AssetIdTypeEnum.BBGID;
        } else if (scheme.contains("commodity-reference-price")){
            return AssetIdTypeEnum.ISDACRP;
        } else if (scheme.contains("iso4914")) {
            return AssetIdTypeEnum.UPI;
        } else {
            return AssetIdTypeEnum.OTHER;
        }
    }
}
