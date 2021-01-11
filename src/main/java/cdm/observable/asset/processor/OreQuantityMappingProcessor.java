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

import static cdm.base.math.metafields.FieldWithMetaQuantity.FieldWithMetaQuantityBuilder;
import static com.rosetta.util.CollectionUtils.emptyIfNull;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.startsWithPath;

@SuppressWarnings("unused")
public class OreQuantityMappingProcessor extends MappingProcessor {

	public OreQuantityMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		getValueAndUpdateMappings(startsWithPath("LegData", synonymPath).addElement("Currency"))
				.ifPresent(currency -> emptyIfNull((List<FieldWithMetaQuantityBuilder>) builders).stream()
						.map(FieldWithMetaQuantityBuilder::getValue)
						.filter(Objects::nonNull)
						.forEach(quantityBuilder ->
								quantityBuilder.setUnitOfAmountBuilder(UnitType.builder()
										.setCurrencyBuilder(FieldWithMetaString.builder()
												.setValue(currency)))));
	}
}
