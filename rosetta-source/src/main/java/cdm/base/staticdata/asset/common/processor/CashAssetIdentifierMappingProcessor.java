package cdm.base.staticdata.asset.common.processor;

import cdm.base.staticdata.asset.common.AssetIdTypeEnum;
import cdm.base.staticdata.asset.common.AssetIdentifier;
import cdm.base.staticdata.asset.common.Cash;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.MappingProcessorUtils;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

@SuppressWarnings("unused")
public class CashAssetIdentifierMappingProcessor extends MappingProcessor {

    private final FxMetaHelper helper;

    public CashAssetIdentifierMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
        this.helper = new FxMetaHelper(context.getMappings());
    }

    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        helper.getCurrencySynonymPath(synonymPath)
                .ifPresent(currencyPath -> {
                    AssetIdentifier.AssetIdentifierBuilder assetIdentifierBuilder = AssetIdentifier.builder();

                    RosettaPath modelPath = getModelPath().newSubPath("value").newSubPath("identifier", 0).newSubPath("identifier");
                    
                    MappingProcessorUtils.setValueAndUpdateMappings(currencyPath,
                            xmlValue ->
                                    assetIdentifierBuilder
                                            .setIdentifierValue(xmlValue)
                                            .setIdentifierType(AssetIdTypeEnum.CURRENCY_CODE),
                            helper.getNonReferenceMappings(),
                            modelPath);

                    if (assetIdentifierBuilder.hasData()) {
                        Cash.CashBuilder cashBuilder = (Cash.CashBuilder) builder;
                        cashBuilder.addIdentifier(assetIdentifierBuilder.build());
                    }
                });
    }
}

