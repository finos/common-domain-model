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
		UnitType.UnitTypeBuilder unitTypeBuilder = (UnitType.UnitTypeBuilder) parent;
		setPerUnitOf(unitTypeBuilder, getPath(openUnitPath, "equity"), FinancialUnitEnum.SHARE);
		setPerUnitOf(unitTypeBuilder, getPath(openUnitPath, "bond"), FinancialUnitEnum.SHARE);
		setPerUnitOf(unitTypeBuilder, getPath(openUnitPath, "index"), FinancialUnitEnum.INDEX_UNIT);
		// for basketConstituent
		setPerUnitOf(unitTypeBuilder, getBasketConstituentPath(openUnitPath.getParent(), "equity"), FinancialUnitEnum.SHARE);
		setPerUnitOf(unitTypeBuilder, getBasketConstituentPath(openUnitPath.getParent(), "bond"), FinancialUnitEnum.SHARE);
		setPerUnitOf(unitTypeBuilder, getBasketConstituentPath(openUnitPath.getParent(), "index"), FinancialUnitEnum.INDEX_UNIT);
	}

	private Path getPath(Path openUnitPath, String equity) {
		return openUnitPath.getParent().addElement(equity);
	}

	private Path getBasketConstituentPath(Path openUnitPath, String equity) {
		return openUnitPath.getParent().addElement(equity);
	}

	private void setPerUnitOf(UnitType.UnitTypeBuilder unitTypeBuilder, Path synonymPath, FinancialUnitEnum financialUnit) {
		getNonNullMappedValue(getMappings(), synonymPath)
				.ifPresent(x -> unitTypeBuilder.setFinancialUnit(financialUnit));
	}
}
