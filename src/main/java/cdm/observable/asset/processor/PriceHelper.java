package cdm.observable.asset.processor;

import cdm.base.math.UnitType;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceTypeEnum;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static cdm.base.math.UnitType.UnitTypeBuilder;

public class PriceHelper {

	private final List<Mapping> mappings;

	public PriceHelper(List<Mapping> mappings) {
		this.mappings = mappings;
	}

	Path startsWithPath(String pathElement, Path path) {
		if (path.endsWith(pathElement)) {
			return path;
		}
		Path parent = path.getParent();
		if (parent != null) {
			return startsWithPath(pathElement, parent);
		}
		throw new IllegalArgumentException(String.format("Path element %s not found in path %s", pathElement, path));
	}

	Price.PriceBuilder getPrice(Optional<Mapping> price, UnitTypeBuilder unitOfAmount, UnitTypeBuilder perUnitOfAmount, PriceTypeEnum priceType) {
		return Price.builder()
				.setAmountBuilder(getBigDecimalValue(price))
				.setPriceType(priceType)
				.setUnitOfAmountBuilder(unitOfAmount)
				.setPerUnitOfAmountBuilder(perUnitOfAmount);
	}

	UnitTypeBuilder getUnitTypeNotionalCurrency(Path startsWithPath) {
		UnitTypeBuilder unitType = UnitType.builder()
				.setCurrencyBuilder(FieldWithMetaString.builder()
						.setValue(getStringValue(startsWithPath, "notionalStepSchedule", "currency"))
						.setMeta(MetaFields.builder()
								.setScheme(getStringValue(startsWithPath, "notionalStepSchedule", "currency", "currencyScheme"))
								.build()));
		return unitType;
	}

	Optional<Mapping> getNonNullMapping(Path startsWith, String... endsWith) {
		return mappings.stream()
				.filter(m -> startsWith.fullStartMatches(m.getXmlPath()))
				.filter(m -> m.getXmlPath().endsWith(endsWith))
				.filter(m -> m.getXmlValue() != null)
				.findFirst();
	}

	private BigDecimal getBigDecimalValue(Optional<Mapping> mapping) {
		return mapping.map(Mapping::getXmlValue).map(String::valueOf).map(BigDecimal::new).orElse(null);
	}

	private String getStringValue(Path startsWith, String... endsWith) {
		return getNonNullMapping(startsWith, endsWith)
				.map(Mapping::getXmlValue)
				.map(String::valueOf)
				.orElse(null);
	}
}
