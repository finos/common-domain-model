package cdm.base.staticdata.asset.common.processor;

import cdm.base.staticdata.asset.common.Security;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;

/**
 * FpML mapper.
 *
 * Removes mappings that have been incorrectly mapped into dividendPayoutRatio.basketConstituent.
 */
@SuppressWarnings("unused")
public class BasketConstituentMappingProcessor extends MappingProcessor {

    private final boolean isDividendPayoutRatioBasketConstituentModelPath;

    public BasketConstituentMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
        this.isDividendPayoutRatioBasketConstituentModelPath =
                modelPath.toIndexless().containsPath(RosettaPath.valueOf("dividendPayoutRatio.basketConstituent"));
    }

    /**
     * If this is mapped under dividendPayoutRatio.basketConstituent, then we need to check if there's any
     * incorrectly mapped data that we need to remove.
     */
    @Override
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
        if (isDividendPayoutRatioBasketConstituentModelPath
                && (!isBasketConstituentSynonymPath(synonymPath) || !isDividendPayoutRatioSet())) {
            // remove data
            removeData(parent);
            // remove mappings
            getMappings().removeAll(filterMappings(getMappings(), getModelPath()));
        }
    }

    /**
     * Blank out data, need to find a better way of doing this.
     */
    private void removeData(RosettaModelObjectBuilder builder) {
//        if (builder instanceof ProductBase.ProductBaseBuilder) {
//            ProductBase.ProductBaseBuilder productBaseBuilder = (ProductBase.ProductBaseBuilder) builder;
//            productBaseBuilder.setProductIdentifier(null);
//        }
        if (builder instanceof Security.SecurityBuilder) {
            Security.SecurityBuilder securityBuilder = (Security.SecurityBuilder) builder;
            securityBuilder.setSecurityType(null);
        }
    }

    /**
     * Does the synonym path contain element basketConstituent?
     */
    private boolean isBasketConstituentSynonymPath(Path synonymPath) {
        return synonymPath.toString().contains("basketConstituent");
    }

    /**
     * Is dividendPayoutRatio.totalRatio or dividendPayoutRatio.cashRatio or dividendPayoutRatio.nonCashRatio set?
     */
    private boolean isDividendPayoutRatioSet() {
        return subPath("dividendPayoutRatio", PathUtils.toPath(getModelPath()))
                .map(dividendPayoutRatioPath ->
                        getNonNullMappingForModelPath(getMappings(), dividendPayoutRatioPath.addElement("totalRatio")).isPresent()
                                || getNonNullMappingForModelPath(getMappings(), dividendPayoutRatioPath.addElement("cashRatio")).isPresent()
                                || getNonNullMappingForModelPath(getMappings(), dividendPayoutRatioPath.addElement("nonCashRatio")).isPresent())
                .orElse(false);
    }
}
