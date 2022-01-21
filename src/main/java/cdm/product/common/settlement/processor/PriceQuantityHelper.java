package cdm.product.common.settlement.processor;

import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceExpression;
import cdm.observable.asset.metafields.FieldWithMetaPrice;
import cdm.product.common.settlement.PriceQuantity;
import cdm.product.template.TradeLot;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.meta.Key;
import com.rosetta.model.lib.path.RosettaPath;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static cdm.observable.asset.metafields.FieldWithMetaPrice.FieldWithMetaPriceBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.subPath;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class PriceQuantityHelper {

	/**
	 * If xml values have been mapped to the model instance, each Mapping will contain the same path.
	 * If fixed with a mapper, then the path must be updated accordingly.
	 */
	@NotNull
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
	public static FieldWithMetaPriceBuilder toReferencablePriceBuilder(
			BigDecimal price, UnitType unitOfAmount, UnitType perUnitOfAmount, PriceExpression priceExpression) {
		FieldWithMetaPriceBuilder priceBuilder = FieldWithMetaPrice.builder()
				.setValue(Price.builder()
						.setAmount(price)
						.setPriceExpression(priceExpression)
						.setUnitOfAmount(unitOfAmount)
						.setPerUnitOfAmount(perUnitOfAmount));
		priceBuilder.getOrCreateMeta().addKey(Key.builder().setScope("DOCUMENT"));
		return priceBuilder;
	}

	/**
	 * Creates a Quantity instance that can be referenced, e.g. the meta key is added with the DOCUMENT scope.
	 */
	public static FieldWithMetaQuantity.FieldWithMetaQuantityBuilder toReferencableQuantityBuilder(Quantity quantity) {
		FieldWithMetaQuantity.FieldWithMetaQuantityBuilder quantityBuilder = FieldWithMetaQuantity.builder().setValue(quantity);
		quantityBuilder.getOrCreateMeta().addKey(Key.builder().setScope("DOCUMENT"));
		return quantityBuilder;
	}

	public static Map<Path, Quantity.QuantityBuilder> getQuantityMap(RosettaPath modelPath, List<? extends TradeLot.TradeLotBuilder> tradeLots) {
		Map<Path, Quantity.QuantityBuilder> quantityMap = new HashMap<>();

		AtomicInteger tradeLotIndex = new AtomicInteger(0);
		for (TradeLot.TradeLotBuilder tradeLot : emptyIfNull(tradeLots)) {
			Path tradeLotPath = PathUtils.toPath(modelPath.getParent()).addElement("tradeLot", tradeLotIndex.getAndIncrement());

			AtomicInteger priceQuantityIndex = new AtomicInteger(0);
			for (PriceQuantity.PriceQuantityBuilder priceQuantity : emptyIfNull(tradeLot.getPriceQuantity())) {
				Path priceQuantityPath = tradeLotPath.addElement("priceQuantity", priceQuantityIndex.getAndIncrement());

				AtomicInteger quantityIndex = new AtomicInteger(0);
				for (FieldWithMetaQuantity.FieldWithMetaQuantityBuilder quantity : emptyIfNull(priceQuantity.getQuantity())) {
					Path quantityPath = priceQuantityPath.addElement("quantity", quantityIndex.getAndIncrement()).addElement("value");

					quantityMap.put(quantityPath, quantity.getValue());
				}
			}
		}
		return quantityMap;
	}
}
