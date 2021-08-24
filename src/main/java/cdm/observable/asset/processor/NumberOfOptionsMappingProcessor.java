package cdm.observable.asset.processor;

import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import cdm.observable.asset.PriceQuantity;
import com.regnosys.rosetta.common.translation.*;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.math.BigDecimal;
import java.util.List;

import static cdm.observable.asset.processor.PriceQuantityHelper.incrementPathElementIndex;
import static cdm.observable.asset.processor.PriceQuantityHelper.toReferencableQuantityBuilder;
import static com.regnosys.rosetta.common.util.PathUtils.toPath;

/**
 * FpML mapper:
 * - maps numberOfOptions to Quantity.amount
 * - sets Quantity.unitOfAmount to FinancialUnitEnum.Contract
 * - optionEntitlement to Quantity.multiplier
 * - sets/maps the appropriate Quantity.multiplierUnit depending on underlying product
 */
public class NumberOfOptionsMappingProcessor extends MappingProcessor {

	private static final Path EQUITY_UNDERLIER_PATH = Path.parse("underlyer.singleUnderlyer.equity.instrumentId");
	private static final Path INDEX_UNDERLIER_PATH = Path.parse("underlyer.singleUnderlyer.index.instrumentId");

	public NumberOfOptionsMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		Quantity.QuantityBuilder quantity = Quantity.builder();
		setAmountAndUnit(synonymPath, quantity, builder.size());
		setMultiplierAndUnit(synonymPath, quantity);

		if (quantity.hasData()) {
			((PriceQuantity.PriceQuantityBuilder) parent).addQuantity(toReferencableQuantityBuilder(quantity));
		}
	}

	private void setAmountAndUnit(Path synonymPath, Quantity.QuantityBuilder quantity, int index) {
		Path baseModelPath = toPath(getModelPath()).addElement("amount");
		Path mappedModelPath = incrementPathElementIndex(baseModelPath, "quantity", index);

		MappingProcessorUtils.setValueAndUpdateMappings(synonymPath,
				(xmlValue) -> quantity
						.setAmount(new BigDecimal(xmlValue))
						.setUnitOfAmount(UnitType.builder().setFinancialUnit(FinancialUnitEnum.CONTRACT)),
				getMappings(),
				PathUtils.toRosettaPath(mappedModelPath));
	}

	private void setMultiplierAndUnit(Path synonymPath, Quantity.QuantityBuilder quantity) {
		Path parentSynonymPath = synonymPath.getParent();

		setValueAndUpdateMappings(parentSynonymPath.addElement("optionEntitlement"),
				(xmlValue) -> quantity.setMultiplier(new BigDecimal(xmlValue)));

		// bond option multiplier unit
		setValueAndUpdateMappings(parentSynonymPath.addElement("entitlementCurrency"),
				(xmlValue) -> quantity.setMultiplierUnit(UnitType.builder().setCurrencyValue(xmlValue)));
		setValueAndUpdateMappings(parentSynonymPath.addElement("entitlementCurrency").addElement("currencyScheme"),
				(xmlValue) -> quantity.getOrCreateMultiplierUnit().getOrCreateCurrency().getOrCreateMeta().setScheme(xmlValue));
		// equity multiplier unit
		if (pathExists(EQUITY_UNDERLIER_PATH)) {
			quantity.setMultiplierUnit(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE));
		}
		// index multiplier unit
		if (pathExists(INDEX_UNDERLIER_PATH)) {
			quantity.setMultiplierUnit(UnitType.builder().setFinancialUnit(FinancialUnitEnum.INDEX_UNIT));
		}
	}

	private boolean pathExists(Path endsWith) {
		return getMappings().stream()
				.filter(m -> m.getXmlPath().endsWith(endsWith))
				.filter(m -> m.getXmlValue() != null)
				.findFirst()
				.isPresent();
	}
}
