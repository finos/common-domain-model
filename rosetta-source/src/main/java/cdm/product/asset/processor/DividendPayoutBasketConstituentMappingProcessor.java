package cdm.product.asset.processor;

import cdm.observable.asset.metafields.ReferenceWithMetaBasketConstituent;
import cdm.product.asset.DividendPayoutRatio;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMappingForModelPath;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.subPath;

/**
 * FpML mapper.
 * <p>
 * Removes mappings that have been incorrectly mapped into dividendPayoutRatio.basketConstituent.
 */
@SuppressWarnings("unused")
public class DividendPayoutBasketConstituentMappingProcessor extends MappingProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DividendPayoutBasketConstituentMappingProcessor.class);

    public DividendPayoutBasketConstituentMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    /**
     * If this is mapped under dividendPayoutRatio.basketConstituent, then we need to check if there's any
     * incorrectly mapped data that we need to remove.
     */
    @Override
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
        if (isBasketConstituentSynonymPath(synonymPath)) {
            List<DividendPayoutRatio.DividendPayoutRatioBuilder> dividendPayoutRatioBuilders =
                    (List<DividendPayoutRatio.DividendPayoutRatioBuilder>) builders;
            List<ReferenceWithMetaBasketConstituent.ReferenceWithMetaBasketConstituentBuilder> basketConstituentBuilders = dividendPayoutRatioBuilders.stream()
                    .map(DividendPayoutRatio.DividendPayoutRatioBuilder::getBasketConstituent)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if (!basketConstituentBuilders.isEmpty()) {
                List<Mapping> basketConstituentSynonymPathMappings = getBasketConstituentSynonymPathMappings();
                // 2 scenarios:
                // - dividendPayoutRatio is not set, so remove any dividendPayoutRatio.basketConstituents
                // or
                // - dividendPayoutRatio is set, but the underlier is not a basketConstituent, so remove any dividendPayoutRatio.basketConstituents
                if (!isDividendPayoutRatioSet()) {
                    // remove data
                    //dividendPayoutRatioBuilders.forEach(b -> b.setBasketConstituent(null));
                    // remove all dividendPayoutRatio mappings
                    removeAllDividendPayoutRatioMappings(basketConstituentSynonymPathMappings);
                } else if (basketConstituentSynonymPathMappings.isEmpty()) {
                    // remove data
                    //dividendPayoutRatioBuilders.forEach(b -> b.setBasketConstituent(null));
                    // remove all dividendPayoutRatio.basketConstituent mappings
                    removeAllDividendPayoutRatioBasketConstituentMappings();
                }
            }
        }
    }

    /**
     * Does the synonym path contain element basketConstituent?
     */
    private boolean isBasketConstituentSynonymPath(Path synonymPath) {
        return synonymPath.endsWith("basketConstituent");
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

    private List<Mapping> getBasketConstituentSynonymPathMappings() {
        return getMappings().stream()
                .filter(m -> m.getRosettaValue() != null)
                .filter(m -> m.getXmlPath().toString().contains("basketConstituent"))
                .collect(Collectors.toList());
    }

    private void removeAllDividendPayoutRatioMappings(List<Mapping> basketConstituentSynonymPathMappings) {
        List<Mapping> mappingsToRemove = basketConstituentSynonymPathMappings.stream()
                .filter(m -> m.getRosettaPath() != null)
                .filter(m -> m.getRosettaPath().toString().contains("dividendPayoutRatio"))
                .collect(Collectors.toList());
        getMappings().removeAll(mappingsToRemove);
    }

    private void removeAllDividendPayoutRatioBasketConstituentMappings() {
        List<Mapping> mappingsToRemove = getMappings().stream()
                .filter(m -> m.getRosettaPath() != null)
                .filter(m -> m.getRosettaPath().toString().contains("dividendPayoutRatio"))
                .filter(m -> m.getRosettaPath().toString().contains("basketConstituent"))
                .collect(Collectors.toList());
        getMappings().removeAll(mappingsToRemove);
    }
}
