package cdm.product.common.schedule.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMapping;

public class RelativeNotionalAmountMappingProcessor extends MappingProcessor {

    public RelativeNotionalAmountMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    /*
     * This mapper updates the PriceQuantity->quantity reference for Equity Swaps that contain a relative notional amount.
     *
     * `Payout->interestRatePayout->payoutQuantity->quantitySchedule->initialQuantity` now references the
     * existing `PriceQuantity->quantity`. The `PriceQuantity->quantity` is now referenced by both
     * `EquityPayout` and `InterestRatePayout`.
     */
    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        getNonNullMapping(getMappings(), synonymPath)
                .ifPresent(hrefMapping ->
                        getNotionalAmountIdMapping(hrefMapping)
                                .flatMap(this::getNotionalAmountMapping)
                                .ifPresent(notionalMapping -> {
                                    // add new reference mapping with correct synonym path and value
                                    getMappings().add(new Mapping(notionalMapping.getXmlPath(),
                                            notionalMapping.getXmlValue(),
                                            hrefMapping.getRosettaPath(),
                                            hrefMapping.getRosettaValue(),
                                            null, false, true, false));
                                    // remove previous reference mapping that's now been replaced
                                    getMappings().remove(hrefMapping);
                                }));
    }

    @NotNull
    private Optional<Mapping> getNotionalAmountIdMapping(Mapping relativeNotionalAmountHrefMapping) {
        return getMappings().stream()
                .filter(mapping -> mapping.getXmlPath().endsWith(Path.parse("notionalAmount.id")))
                .filter(m -> m.getXmlValue().equals(relativeNotionalAmountHrefMapping.getXmlValue()))
                .findFirst();
    }

    @NotNull
    private Optional<Mapping> getNotionalAmountMapping(Mapping idMapping) {
        return getNonNullMapping(getMappings(), idMapping.getXmlPath().getParent().addElement("amount"));
    }
}
