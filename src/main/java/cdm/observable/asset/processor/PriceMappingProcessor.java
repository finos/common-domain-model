package cdm.observable.asset.processor;

import cdm.base.math.UnitType;
import cdm.observable.asset.Price;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMappedValue;

public class PriceMappingProcessor extends MappingProcessor {

	public PriceMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, T instance, RosettaModelObjectBuilder parent) {
		Price.PriceBuilder priceBuilder = (Price.PriceBuilder) parent;
		PriceTypeHelper.getPriceTypeEnum(synonymPath).ifPresent(priceBuilder::setPriceType);
		System.out.println("**** " + synonymPath);
		//priceBuilder.setUnitOfAmount()

//		if (synonymPath.toString().contains("swapStream")) {
//			getUnitType(synonymPath).ifPresent(priceBuilder::setUnitOfAmount);
//			getUnitType(synonymPath).ifPresent(priceBuilder::setPerUnitOfAmount);
//		}
	}

	private Optional<UnitType> getUnitType(Path synonymPath) {
		return getCurrency(synonymPath).map(c -> UnitType.builder().setCurrency(c).build());
	}

	private Optional<FieldWithMetaString> getCurrency(Path synonymPath) {
		Path notionalCurrencyPath = synonymPath.getParent().getParent().addElement("notionalSchedule").addElement("notionalStepSchedule").addElement("currency");
		FieldWithMetaString.FieldWithMetaStringBuilder currencyBuilder = FieldWithMetaString.builder();
		getNonNullMappedValue(notionalCurrencyPath, getMappings()).ifPresent(currencyBuilder::setValue);
		getNonNullMappedValue(notionalCurrencyPath.addElement("currencyScheme"), getMappings())
				.ifPresent(scheme -> currencyBuilder.getOrCreateMeta().setScheme(scheme));
		return currencyBuilder.hasData() ? Optional.of(currencyBuilder.build()) : Optional.empty();
	}
}
