package cdm.observable.asset.processor;

import cdm.base.staticdata.asset.common.AssetClassEnum;
import cdm.observable.asset.EquityIndex;
import cdm.observable.asset.IndexBase;
import cdm.observable.asset.InterestRateIndex;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused") // used in generated code
public class IndexAssetClassMappingProcessor extends MappingProcessor {

    public IndexAssetClassMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
        if (parent instanceof EquityIndex) {
            setAssetClass(parent, AssetClassEnum.EQUITY);
        } else if (parent instanceof InterestRateIndex) {
            setAssetClass(parent, AssetClassEnum.INTEREST_RATE);
        } 
//        else if (parent instanceof ForeignExchangeRateIndex) {
//            ForeignExchangeRateIndex.ForeignExchangeRateIndexBuilder builder = (ForeignExchangeRateIndex.ForeignExchangeRateIndexBuilder) parent;
//            setAssetClass(builder, AssetClassEnum.FOREIGN_EXCHANGE);
//        }
    }

    private void setAssetClass(RosettaModelObjectBuilder builder, AssetClassEnum assetClass) {
        if (builder.hasData()) {
            ((IndexBase.IndexBaseBuilder) builder).setAssetClass(assetClass);
        }
    }
}
