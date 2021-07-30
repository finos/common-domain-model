package cdm.product.common.settlement.processor;

import cdm.base.math.UnitType;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.metafields.FieldWithMetaPrice;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.meta.Key;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

import static cdm.observable.asset.metafields.FieldWithMetaPrice.FieldWithMetaPriceBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.subPath;

public class PriceHelper {

	/**
	 * If xml values have been mapped to the model instance, each Mapping will contain the same path.
	 * If fixed with a mapper, then the path must be updated accordingly.
	 */
	@NotNull
	public static Path incrementPricePathElementIndex(Path mappedModelPath, int indexDiff) {
		// find price path element that needs updating
		Path.PathElement pricePathElement = subPath("price", mappedModelPath)
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
	public static FieldWithMetaPriceBuilder toReferencablePriceBuilder(
			BigDecimal price, UnitType unitOfAmount, UnitType perUnitOfAmount, PriceTypeEnum priceType) {
		FieldWithMetaPriceBuilder priceBuilder = FieldWithMetaPrice.builder()
				.setValue(Price.builder()
						.setAmount(price)
						.setPriceType(priceType)
						.setUnitOfAmount(unitOfAmount)
						.setPerUnitOfAmount(perUnitOfAmount));
		priceBuilder.getOrCreateMeta().addKey(Key.builder().setScope("DOCUMENT"));
		return priceBuilder;
	}
}
