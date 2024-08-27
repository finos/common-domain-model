package cdm.product.template.processor;

import cdm.base.staticdata.asset.common.Commodity;
import cdm.base.staticdata.asset.common.Taxonomy;
import cdm.base.staticdata.asset.common.TaxonomyClassification;
import cdm.base.staticdata.asset.common.TaxonomySourceEnum;
import cdm.product.template.SettlementPayout;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.*;
import java.util.stream.Collectors;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.subPath;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("unused")
public class CommodityClassificationMetaMappingProcessor extends MappingProcessor {

    public CommodityClassificationMetaMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
        List<SettlementPayout.SettlementPayoutBuilder> settlementPayoutBuilders = (List<SettlementPayout.SettlementPayoutBuilder>) builders;
        if (!settlementPayoutBuilders.isEmpty()) {
            SettlementPayout.SettlementPayoutBuilder settlementPayoutBuilder = settlementPayoutBuilders.get(0);
            Commodity.CommodityBuilder commodityBuilder = settlementPayoutBuilder.getOrCreateUnderlier().getOrCreateAsset().getValue().getOrCreateCommodity();

            List<Mapping> commodityClassificationMappings = getCommodityClassificationMappings(synonymPath.addElement("commodityClassification"));
            Map<Path, List<Mapping>> groupedCommodityClassificationMappings = groupMappings("commodityClassification", commodityClassificationMappings);

            groupedCommodityClassificationMappings.values().forEach(mappings ->
                    getTaxonomy(mappings, getModelPath()).ifPresent(commodityBuilder::addTaxonomy));
        }
    }

    private Optional<Taxonomy> getTaxonomy(List<Mapping> groupedCommodityClassificationMappings, RosettaPath modelPath) {
        Taxonomy.TaxonomyBuilder taxonomyBuilder = Taxonomy.builder();

        Map<Path, List<Mapping>> groupedCodeMappings = groupMappings("code", groupedCommodityClassificationMappings);

        groupedCodeMappings.values().forEach(mappings -> {
            getTaxonomyClassification(mappings).ifPresent(taxonomyBuilder.getOrCreateValue()::addClassification);
            getTaxonomySource(mappings).ifPresent(taxonomyBuilder::setSource);
        });

        return taxonomyBuilder.hasData() ? Optional.of(taxonomyBuilder.build()) : Optional.empty();
    }

    private Optional<TaxonomyClassification> getTaxonomyClassification(List<Mapping> groupedCodeMappings) {
        TaxonomyClassification.TaxonomyClassificationBuilder taxonomyClassificationBuilder = TaxonomyClassification.builder();

        groupedCodeMappings.forEach(mapping -> {
            if (mapping.getXmlPath().endsWith("code")) {
                taxonomyClassificationBuilder.setValue((String) mapping.getXmlValue());
            }
            if (mapping.getXmlPath().endsWith("commodityClassificationScheme")) {
                getOrdinal((String) mapping.getXmlValue()).ifPresent(taxonomyClassificationBuilder::setOrdinal);
            }
        });
        
        return taxonomyClassificationBuilder.hasData() ? Optional.of(taxonomyClassificationBuilder.build()) : Optional.empty();
    }

    private Optional<Integer> getOrdinal(String scheme) {
        switch (scheme) {
            case "http://www.fpml.org/coding-scheme/isda-layer-1-commodity-classification":
            case "http://www.fpml.org/coding-scheme/esma-emir-refit-layer-1-commodity-classification":
                return Optional.of(1);
            case "http://www.fpml.org/coding-scheme/isda-layer-2-commodity-classification":
            case "http://www.fpml.org/coding-scheme/esma-emir-refit-layer-2-commodity-classification":
                return Optional.of(2);
            case "http://www.fpml.org/coding-scheme/isda-layer-3-commodity-classification":
            case "http://www.fpml.org/coding-scheme/esma-emir-refit-layer-3-commodity-classification":
                return Optional.of(3);
            default:
                return Optional.empty();
        }
    }

    private Optional<TaxonomySourceEnum> getTaxonomySource(List<Mapping> mappings) {
        if (containsScheme(mappings, "http://www.fpml.org/coding-scheme/isda-layer")) {
            return Optional.of(TaxonomySourceEnum.ISDA);
        } else if (containsScheme(mappings, "http://www.fpml.org/coding-scheme/esma-emir-refit-layer")) {
            return Optional.of(TaxonomySourceEnum.EMIR);
        } else {
            return Optional.empty();
        }
    }

    private boolean containsScheme(List<Mapping> mappings, String scheme) {
        return mappings.stream()
                .map(Mapping::getXmlValue)
                .filter(Objects::nonNull)
                .map(String.class::cast)
                .anyMatch(v -> v.contains(scheme));
    }

    private List<Mapping> getCommodityClassificationMappings(Path startsWith) {
        return getMappings().stream()
                .filter(m -> startsWith.nameStartMatches(m.getXmlPath()))
                .collect(Collectors.toList());
    }

    private Map<Path, List<Mapping>> groupMappings(String groupByPathElement, List<Mapping> filteredMappings) {
        return filteredMappings.stream()
                .filter(m -> m.getXmlValue() != null)
                .filter(m -> subPath(groupByPathElement, m.getXmlPath()).isPresent())
                // group the mappings by the groupByPathElement
                .collect(groupingBy(m -> subPath(groupByPathElement, m.getXmlPath()).get(), LinkedHashMap::new, toList()));
    }
}