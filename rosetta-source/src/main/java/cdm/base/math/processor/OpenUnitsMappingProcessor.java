package cdm.base.math.processor;

import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.UnitType;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMappedValue;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.subPath;

@SuppressWarnings("unused")
public class OpenUnitsMappingProcessor extends MappingProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenUnitsMappingProcessor.class);
    
    public OpenUnitsMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public <T> void mapBasic(Path openUnitPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
        UnitType.UnitTypeBuilder unitTypeBuilder = (UnitType.UnitTypeBuilder) parent;

        // for single underliers
        subPath("singleUnderlyer", openUnitPath).ifPresent(singleUnderlyerPath -> {
            List<Mapping> singleUnderlyerMappings = getMappings().stream()
                    .filter(m -> singleUnderlyerPath.fullStartMatches(m.getXmlPath()))
                    .collect(Collectors.toList());
            // openUnits path:
            // /underlyer/singleUnderlyer/openUnits
            // underlier type:
            // /underlyer/singleUnderlyer/equity|bond|index
            setQuantityUnit(unitTypeBuilder, singleUnderlyerPath.addElement("equity"), FinancialUnitEnum.SHARE, singleUnderlyerMappings);
            setQuantityUnit(unitTypeBuilder, singleUnderlyerPath.addElement("bond"), FinancialUnitEnum.SHARE, singleUnderlyerMappings);
            setQuantityUnit(unitTypeBuilder, singleUnderlyerPath.addElement("index"), FinancialUnitEnum.INDEX_UNIT, singleUnderlyerMappings);
            setQuantityUnit(unitTypeBuilder, singleUnderlyerPath.addElement("commodity"), FinancialUnitEnum.WEIGHT, singleUnderlyerMappings);
        });
        // for basketConstituent
        subPath("basketConstituent", openUnitPath).ifPresent(basketConsitutentPath -> {
            List<Mapping> basketConstituentMappings = getMappings().stream()
                    .filter(m -> basketConsitutentPath.fullStartMatches(m.getXmlPath()))
                    .collect(Collectors.toList());
            LOGGER.debug("Found {} mappings in path {}", basketConstituentMappings.size(), basketConsitutentPath);
            // openUnits path:
            // /underlyer/basket/basketConstituent/constituentWeight/openUnits
            // underlier type:
            // /underlyer/basket/basketConstituent/equity|bond|index|commodity
            setQuantityUnit(unitTypeBuilder, basketConsitutentPath.addElement("equity"), FinancialUnitEnum.SHARE, basketConstituentMappings);
            setQuantityUnit(unitTypeBuilder, basketConsitutentPath.addElement("bond"), FinancialUnitEnum.SHARE, basketConstituentMappings);
            setQuantityUnit(unitTypeBuilder, basketConsitutentPath.addElement("index"), FinancialUnitEnum.INDEX_UNIT, basketConstituentMappings);
            setQuantityUnit(unitTypeBuilder, basketConsitutentPath.addElement("commodity"), FinancialUnitEnum.WEIGHT, basketConstituentMappings);
            // set to default if unset
            if (unitTypeBuilder.getFinancialUnit() == null) {
                unitTypeBuilder.setFinancialUnit(FinancialUnitEnum.WEIGHT);
            }
        });
    }

    private void setQuantityUnit(UnitType.UnitTypeBuilder unitTypeBuilder, Path synonymPath, FinancialUnitEnum financialUnit, List<Mapping> filteredMappings) {
        getNonNullMappedValue(filteredMappings, synonymPath)
                .ifPresent(x -> {
                    LOGGER.debug("Setting openUnits FinancialUnitEnum {} based on path {}", financialUnit, synonymPath);
                    unitTypeBuilder.setFinancialUnit(financialUnit);
                });
    }
}
