package cdm.base.math.processor;

import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.UnitType;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class BasketPercentageMappingProcessor extends MappingProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasketPercentageMappingProcessor.class);
    
    public BasketPercentageMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public <T> void mapBasic(Path openUnitPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
        UnitType.UnitTypeBuilder unitTypeBuilder = (UnitType.UnitTypeBuilder) parent;
        // set to WEIGHT if unset
        if (unitTypeBuilder.getFinancialUnit() == null) {
            unitTypeBuilder.setFinancialUnit(FinancialUnitEnum.WEIGHT);
        }
    }
}
