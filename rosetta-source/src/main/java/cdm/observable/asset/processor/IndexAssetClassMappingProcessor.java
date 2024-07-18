package cdm.observable.asset.processor;

import cdm.base.staticdata.asset.common.AssetClassEnum;
import cdm.observable.asset.CreditIndex;
import cdm.observable.asset.EquityIndex;
import cdm.observable.asset.FloatingRateIndex;
import cdm.observable.asset.ForeignExchangeRate;
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
        if (parent instanceof CreditIndex) {
            CreditIndex.CreditIndexBuilder builder = (CreditIndex.CreditIndexBuilder) parent;
            builder.setAssetClass(AssetClassEnum.CREDIT);
        } else if (parent instanceof EquityIndex) {
            EquityIndex.EquityIndexBuilder builder = (EquityIndex.EquityIndexBuilder) parent;
            builder.setAssetClass(AssetClassEnum.EQUITY);
        } else if (parent instanceof FloatingRateIndex) {
            FloatingRateIndex.FloatingRateIndexBuilder builder = (FloatingRateIndex.FloatingRateIndexBuilder) parent;
            builder.setAssetClass(AssetClassEnum.INTEREST_RATE);
        } else if (parent instanceof ForeignExchangeRate) {
            ForeignExchangeRate.ForeignExchangeRateBuilder builder = (ForeignExchangeRate.ForeignExchangeRateBuilder) parent;
            builder.setAssetClass(AssetClassEnum.FOREIGN_EXCHANGE);
        }
    }
}
