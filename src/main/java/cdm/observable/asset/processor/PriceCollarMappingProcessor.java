package cdm.observable.asset.processor;

import cdm.base.math.UnitType;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceQuantity;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.metafields.FieldWithMetaPrice;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.Key;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static cdm.base.math.UnitType.UnitTypeBuilder;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.subPath;

/**
 * Mapper required due to mapping issues with multiple prices.
 * <p>
 * Fix synonym mapping logic and remove these mappers.
 */
@SuppressWarnings("unused")
public class PriceCollarMappingProcessor extends MappingProcessor {

	public PriceCollarMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		subPath("capFloorStream", synonymPath).ifPresent(subPath -> {
			Optional<Mapping> floorRateMapping = getNonNullMapping(getModelPath(), subPath, "floorRateSchedule", "initialValue");
			Optional<Mapping> capRateMapping = getNonNullMapping(getModelPath(), subPath, "capRateSchedule", "initialValue");
			if (floorRateMapping.isPresent() && capRateMapping.isPresent()) {
				UnitTypeBuilder unitType = getUnitTypeNotionalCurrency(subPath);
				BigDecimal floorRate = new BigDecimal(String.valueOf(floorRateMapping.get().getXmlValue()));
				((PriceQuantity.PriceQuantityBuilder) parent)
						.addPriceBuilder(getPrice(floorRate, unitType, unitType, PriceTypeEnum.FLOOR_RATE));
				updateMapping(floorRateMapping.get());
			}
		});
	}

	private void updateMapping(Mapping mapping) {
		// update price index, e.g. floorRate and capRate were previously mapped to the same field so the price index
		// must be incremented otherwise any references will break
		mapping.setRosettaPath(incrementPricePathElementIndex(mapping));
		// clear errors
		mapping.setError(null);
		mapping.setCondition(true);
	}

	@NotNull
	private Path incrementPricePathElementIndex(Mapping mapping) {
		// find price path element that needs updating
		Path.PathElement pricePathElement = subPath("price", mapping.getRosettaPath())
				.map(Path::getLastElement)
				.orElse(null);
		// build new path
		return new Path(mapping.getRosettaPath().getElements().stream()
						.map(pathElement -> {
							Optional<Integer> index = pathElement.equals(pricePathElement) ?
									Optional.of(pathElement.forceGetIndex() + 1) :
									pathElement.getIndex();
							return new Path.PathElement(pathElement.getPathName(), index, pathElement.getMetas());
						})
						.collect(Collectors.toList()));
	}

	private FieldWithMetaPrice.FieldWithMetaPriceBuilder getPrice(BigDecimal price, UnitTypeBuilder unitOfAmount, UnitTypeBuilder perUnitOfAmount, PriceTypeEnum priceType) {
		FieldWithMetaPrice.FieldWithMetaPriceBuilder priceBuilder = FieldWithMetaPrice.builder()
				.setValueBuilder(Price.builder()
						.setAmountBuilder(price)
						.setPriceType(priceType)
						.setUnitOfAmountBuilder(unitOfAmount)
						.setPerUnitOfAmountBuilder(perUnitOfAmount));
		priceBuilder.getOrCreateMeta().getOrCreateKeys().addKey(new Key.KeyBuilder().setScope("DOCUMENT"));
		return priceBuilder;
	}

	private UnitTypeBuilder getUnitTypeNotionalCurrency(Path startsWithPath) {
		return UnitType.builder()
				.setCurrencyBuilder(FieldWithMetaString.builder()
						.setValue(getStringValue(startsWithPath, "notionalStepSchedule", "currency"))
						.setMeta(MetaFields.builder()
								.setScheme(getStringValue(startsWithPath, "notionalStepSchedule", "currency", "currencyScheme"))
								.build()));
	}

	private Optional<Mapping> getNonNullMapping(RosettaPath modelPath, Path startsWith, String... endsWith) {
		return getMappings().stream()
				.filter(m -> startsWith.fullStartMatches(m.getXmlPath()))
				.filter(m -> m.getXmlPath().endsWith(endsWith))
				.filter(m -> Optional.ofNullable(modelPath).map(PathUtils::toPath).map(p -> p.fullStartMatches(m.getRosettaPath())).orElse(true))
				.filter(m -> m.getXmlValue() != null)
				.findFirst();
	}

	private String getStringValue(Path startsWith, String... endsWith) {
		return getNonNullMapping(null, startsWith, endsWith)
				.map(Mapping::getXmlValue)
				.map(String::valueOf)
				.orElse(null);
	}
}
