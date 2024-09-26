package cdm.observable.asset.processor;

import cdm.base.math.UnitType;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;

import java.util.List;
import java.util.Objects;

import static cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule.FieldWithMetaNonNegativeQuantityScheduleBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.subPath;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * ORE mapper to enrich the mapped quantity with currency unitOfAmount.
 */
@SuppressWarnings("unused")
public class OreQuantityMappingProcessor extends MappingProcessor {

	public OreQuantityMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		subPath("LegData", synonymPath)
				.map(subPath -> subPath.addElement("Currency"))
				.flatMap(this::getValueAndUpdateMappings)
				.ifPresent(currency -> emptyIfNull((List<FieldWithMetaNonNegativeQuantityScheduleBuilder>) builders).stream()
						.map(FieldWithMetaNonNegativeQuantityScheduleBuilder::getValue)
						.filter(Objects::nonNull)
						.forEach(quantityBuilder ->
								quantityBuilder.setUnit(UnitType.builder()
										.setCurrency(FieldWithMetaString.builder()
												.setValue(currency)))));
	}
}
