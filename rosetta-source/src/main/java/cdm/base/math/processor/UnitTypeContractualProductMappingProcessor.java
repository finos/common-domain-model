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
public class UnitTypeContractualProductMappingProcessor extends MappingProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnitTypeContractualProductMappingProcessor.class);
    
    public UnitTypeContractualProductMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
        if (!isApplicable(synonymPath)) {
            return;
        }
        UnitType.UnitTypeBuilder unitTypeBuilder = (UnitType.UnitTypeBuilder) parent;
        // set to CONTRACTUAL_PRODUCT if unset
        if (unitTypeBuilder.getFinancialUnit() == null) {
            unitTypeBuilder.setFinancialUnit(FinancialUnitEnum.CONTRACTUAL_PRODUCT);
        }
    }
    
    private boolean isApplicable(Path synonymPath) {
        return (synonymPath.toString().contains("bulletPayment") 
                && getModelPath().endsWith(RosettaPath.valueOf("perUnitOf.financialUnit")));
    }
}
