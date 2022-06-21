package cdm.base.math.processor;

import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.UnitType;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMappedValue;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMapping;

@SuppressWarnings("unused")
public class OpenUnitsMappingProcessor extends MappingProcessor {

	public OpenUnitsMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public <T> void mapBasic(Path openUnitPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		getNonNullMapping(getMappings(), openUnitPath)
				.flatMap(x -> getNonNullMappedValue(getMappings(), openUnitPath.getParent().addElement("equity")))
				.ifPresent(x -> ((UnitType.UnitTypeBuilder) parent).setFinancialUnit(FinancialUnitEnum.SHARE));
		getNonNullMapping(getMappings(), openUnitPath)
				.flatMap(x -> getNonNullMappedValue(getMappings(), openUnitPath.getParent().addElement("index")))
				.ifPresent(x -> ((UnitType.UnitTypeBuilder) parent).setFinancialUnit(FinancialUnitEnum.INDEX_UNIT));
	}
}
