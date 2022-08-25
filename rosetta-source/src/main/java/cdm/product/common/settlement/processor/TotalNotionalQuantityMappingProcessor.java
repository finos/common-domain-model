package cdm.product.common.settlement.processor;

import cdm.base.math.CapacityUnitEnum;
import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.product.common.settlement.PriceQuantity;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.MappingProcessorUtils;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cdm.product.common.settlement.processor.PriceQuantityHelper.incrementPathElementIndex;
import static cdm.product.common.settlement.processor.PriceQuantityHelper.toReferencableQuantityBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMappedValue;
import static com.regnosys.rosetta.common.util.PathUtils.toPath;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class TotalNotionalQuantityMappingProcessor extends MappingProcessor {

	public TotalNotionalQuantityMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		List<FieldWithMetaQuantity.FieldWithMetaQuantityBuilder> quantityBuilders = filterEmpty(builders);

		getTotalNotionalQuantity(synonymPath, quantityBuilders.size()).ifPresent(totalNotionalQuantity -> {
			// add to quantity list
			quantityBuilders.add(toReferencableQuantityBuilder(totalNotionalQuantity));
			// set Quantity list on PriceQuantity
			((PriceQuantity.PriceQuantityBuilder) parent).setQuantity(quantityBuilders);
		});
	}

	@NotNull
	private List<FieldWithMetaQuantity.FieldWithMetaQuantityBuilder> filterEmpty(List<? extends RosettaModelObjectBuilder> builders) {
		return (List<FieldWithMetaQuantity.FieldWithMetaQuantityBuilder>) builders.stream()
				.filter(RosettaModelObjectBuilder::hasData)
				.collect(Collectors.toList());
	}

	private Optional<Quantity.QuantityBuilder> getTotalNotionalQuantity(Path synonymPath, int index) {
		Quantity.QuantityBuilder quantity = Quantity.builder();

		Path mappedModelPath = toPath(getModelPath()).addElement("amount");

		MappingProcessorUtils.setValueAndUpdateMappings(synonymPath,
				(xmlValue) -> quantity
						.setAmount(new BigDecimal(xmlValue))
						.setUnitOfAmount(UnitType.builder().setCapacityUnit(getCapacityUnitEnum(synonymPath))),
				getMappings(),
				PathUtils.toRosettaPath(mappedModelPath));

		return quantity.hasData() ? Optional.of(quantity) : Optional.empty();
	}

	@Nullable
	private CapacityUnitEnum getCapacityUnitEnum(Path baseSynonymPath) {
		Path quantityUnitPath = baseSynonymPath.getParent().addElement("notionalQuantity").addElement("quantityUnit");
		return getNonNullMappedValue(quantityUnitPath, getMappings())
				.flatMap(xmlValue -> getSynonymToEnumMap().getEnumValueOptional(CapacityUnitEnum.class, xmlValue))
				.orElse(null);
	}
}
