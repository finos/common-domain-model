package cdm.product.common.settlement.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.filterMappings;

public class RelativeNotionalAmountMappingProcessor extends MappingProcessor {

    public RelativeNotionalAmountMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    /*
     * This mapper updates the PriceQuantity->quantity reference for Equity Swaps that contain a relative notional amount.
     *
     * `Payout->interestRatePayout->payoutQuantity->quantitySchedule` now references the
     * existing `PriceQuantity->quantity`. The `PriceQuantity->quantity` is now referenced by both
     * `EquityPayout` and `InterestRatePayout`.
     */
    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        // find the href mapping that contains a reference object used to build the PQ references
        getNotionalAmountHrefMapping(synonymPath)
                .ifPresent(hrefMapping ->
                        // find the synonym path that is associated with the id that corresponds to the href
                        getNotionalAmountIdSynonymPath(hrefMapping)
                                .ifPresent(notionalIdPath -> {
                                    // add new reference mapping with correct synonym path and value
                                    getMappings().add(new Mapping(convertPath(notionalIdPath),
                                            hrefMapping.getXmlValue(),
                                            hrefMapping.getRosettaPath(),
                                            hrefMapping.getRosettaValue(),
                                            null, false, true, false));
                                    // remove previous reference mapping that's now been replaced
                                    getMappings().remove(hrefMapping);
                                }));
    }


    private Optional<Mapping> getNotionalAmountHrefMapping(Path synonymPath) {
        return filterMappings(getMappings(), synonymPath).stream()
                .filter(m -> m.getRosettaValue() instanceof Reference.ReferenceBuilder)
                .findFirst();
    }


    private Optional<Path> getNotionalAmountIdSynonymPath(Mapping relativeNotionalAmountHrefMapping) {
        return getMappings().stream()
                .filter(mapping -> mapping.getXmlPath().endsWith(Path.parse("notionalAmount.id")))
                .filter(m -> m.getXmlValue().equals(relativeNotionalAmountHrefMapping.getXmlValue()))
                .map(Mapping::getXmlPath)
                .distinct()
                .findFirst();
    }


    private Path convertPath(Path idPath) {
        return idPath.getParent().addElement("amount");
    }
}
