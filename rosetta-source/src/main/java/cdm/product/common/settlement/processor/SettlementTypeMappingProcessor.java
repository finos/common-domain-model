package cdm.product.common.settlement.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static cdm.product.common.settlement.SettlementTerms.SettlementTermsBuilder;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class SettlementTypeMappingProcessor extends MappingProcessor {

    private final SettlementTypeHelper settlementTypeHelper;

    public SettlementTypeMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
        this.settlementTypeHelper = new SettlementTypeHelper(context.getMappings());
    }

    @Override
    public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
        settlementTypeHelper.setSettlementType(synonymPath, (SettlementTermsBuilder) parent);
    }
}
