package cdm.observable.asset.processor;

import cdm.base.math.ArithmeticOperationEnum;
import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceComposite;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.metafields.FieldWithMetaPriceSchedule;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.meta.Key;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

import static cdm.observable.asset.metafields.FieldWithMetaPriceSchedule.FieldWithMetaPriceScheduleBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.subPath;

public class PriceQuantityHelper {

	/**
	 * If xml values have been mapped to the model instance, each Mapping will contain the same path.
	 * If fixed with a mapper, then the path must be updated accordingly.
	 */
	public static Path incrementPathElementIndex(Path mappedModelPath, String lastElement, int indexDiff) {
		// find price/quantity path element that needs updating, e.g. the path will likely be price.value or quantity.value.
		Path.PathElement pricePathElement = subPath(lastElement, mappedModelPath)
				.map(Path::getLastElement)
				.orElse(null);
		// build new path
		return new Path(mappedModelPath.getElements().stream()
				.map(pathElement -> {
					Optional<Integer> index = pathElement.equals(pricePathElement) ?
							Optional.of(pathElement.forceGetIndex() + indexDiff) :
							pathElement.getIndex();
					return new Path.PathElement(pathElement.getPathName(), index, pathElement.getMetas());
				})
				.collect(Collectors.toList()));
	}

	/**
	 * Creates a Price instance that can be referenced, e.g. the meta key is added with the DOCUMENT scope.
	 */
	public static FieldWithMetaPriceScheduleBuilder toReferencablePriceBuilder(
			BigDecimal price, UnitType unitOfAmount, UnitType perUnitOfAmount, PriceTypeEnum priceType, ArithmeticOperationEnum arithmeticOperator, PriceComposite priceComposite) {
		FieldWithMetaPriceScheduleBuilder priceBuilder = FieldWithMetaPriceSchedule.builder()
				.setValue(Price.builder()
						.setValue(price)
						.setPriceType(priceType)
						.setArithmeticOperator(arithmeticOperator)
						.setComposite(priceComposite)
						.setUnit(unitOfAmount)
						.setPerUnitOf(perUnitOfAmount));
		priceBuilder.getOrCreateMeta().addKey(Key.builder().setScope("DOCUMENT"));
		return priceBuilder;
	}

	/**
	 * Creates a Quantity instance that can be referenced, e.g. the meta key is added with the DOCUMENT scope.
	 */
	public static FieldWithMetaNonNegativeQuantitySchedule.FieldWithMetaNonNegativeQuantityScheduleBuilder toReferencableQuantityBuilder(NonNegativeQuantitySchedule quantity) {
		FieldWithMetaNonNegativeQuantitySchedule.FieldWithMetaNonNegativeQuantityScheduleBuilder quantityBuilder = FieldWithMetaNonNegativeQuantitySchedule.builder().setValue(quantity);
		quantityBuilder.getOrCreateMeta().addKey(Key.builder().setScope("DOCUMENT"));
		return quantityBuilder;
	}
}
