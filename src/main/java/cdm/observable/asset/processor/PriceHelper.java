package cdm.observable.asset.processor;

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

	public static FieldWithMetaPriceBuilder toPriceBuilder(
			BigDecimal price, UnitType.UnitTypeBuilder unitOfAmount, UnitType.UnitTypeBuilder perUnitOfAmount, PriceTypeEnum priceType) {
		FieldWithMetaPriceBuilder priceBuilder = FieldWithMetaPrice.builder()
				.setValueBuilder(Price.builder()
						.setAmountBuilder(price)
						.setPriceType(priceType)
						.setUnitOfAmountBuilder(unitOfAmount)
						.setPerUnitOfAmountBuilder(perUnitOfAmount));
		priceBuilder.getOrCreateMeta().getOrCreateKeys().addKey(new Key.KeyBuilder().setScope("DOCUMENT"));
		return priceBuilder;
	}
}
