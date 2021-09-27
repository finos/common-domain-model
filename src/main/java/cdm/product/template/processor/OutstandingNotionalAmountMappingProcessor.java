package cdm.product.template.processor;

import cdm.base.math.MeasureBase;
import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import cdm.product.common.settlement.PriceQuantity;
import cdm.product.template.TradeLot;
import com.regnosys.rosetta.common.translation.*;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

@SuppressWarnings("unused") // used in generated code
public class OutstandingNotionalAmountMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(OutstandingNotionalAmountMappingProcessor.class);
	private static final String REMOVE = "REMOVE";

	public OutstandingNotionalAmountMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> tradeLots, RosettaModelObjectBuilder tradableProduct) {
		// Update outstanding notional amount on all quantities with matching currency
		Set<Path> synonymPaths = getMappings().stream()
				.map(Mapping::getXmlPath)
				.filter(synonymPath::nameStartMatches)
				.map(Path::getParent)
				.collect(Collectors.toSet());
		synonymPaths.forEach(p -> mapTradeLots(p, tradeLots));

		// Remove out-of-date mappings
		getMappings().removeAll(getMappings().stream()
				.filter(m -> REMOVE.equals(m.getError()))
				.collect(Collectors.toList()));
	}

	private void mapTradeLots(Path synonymPath, List<? extends RosettaModelObjectBuilder> tradeLots) {
		AtomicInteger index = new AtomicInteger(0);
		emptyIfNull(tradeLots).forEach(tradeLot -> mapTradeLot(synonymPath,
				PathUtils.toPath(getModelPath().getParent()).addElement("tradeLot", index.getAndIncrement()),
				(TradeLot.TradeLotBuilder) tradeLot));
	}

	private void mapTradeLot(Path synonymPath, Path tradeLotPath, TradeLot.TradeLotBuilder tradeLot) {
		AtomicInteger index = new AtomicInteger(0);
		emptyIfNull(tradeLot.getPriceQuantity()).forEach(priceQuantity ->
				mapPriceQuantity(synonymPath, tradeLotPath.addElement("priceQuantity", index.getAndIncrement()), priceQuantity));
	}

	private void mapPriceQuantity(Path synonymPath, Path priceQuantityPath, PriceQuantity.PriceQuantityBuilder priceQuantity) {
		AtomicInteger index = new AtomicInteger(0);
		emptyIfNull(priceQuantity.getQuantity()).forEach(quantity -> mapQuantity(synonymPath,
				priceQuantityPath.addElement("quantity", index.getAndIncrement()).addElement("value"),
				quantity.getValue()));
	}

	private void mapQuantity(Path synonymPath, Path modelPath, Quantity.QuantityBuilder quantity) {
		Path amountSynonymPath = synonymPath.addElement("amount");
		Optional<Mapping> outstandingNotionalAmount = MappingProcessorUtils.getNonNullMapping(getMappings(), amountSynonymPath);
		Path currencySynonymPath = synonymPath.addElement("currency");
		Optional<Mapping> outstandingNotionalCurrency = MappingProcessorUtils.getNonNullMapping(getMappings(), currencySynonymPath);

		if (outstandingNotionalCurrency.isPresent() && outstandingNotionalAmount.isPresent()) {
			String currencyValue = (String) outstandingNotionalCurrency.get().getXmlValue();
			String existingCurrencyValue = Optional.ofNullable(quantity)
					.map(MeasureBase.MeasureBaseBuilder::getUnitOfAmount)
					.map(UnitType.UnitTypeBuilder::getCurrency)
					.map(FieldWithMetaString::getValue)
					.orElse(null);
			if (currencyValue.equals(existingCurrencyValue)) {
				String amountValue = (String) outstandingNotionalAmount.get().getXmlValue();
				quantity.setAmount(new BigDecimal(amountValue));

				// Update mappings
				updateMappings(modelPath.addElement("amount"), amountSynonymPath, amountValue);
				updateMappings(modelPath.addElement("unitOfAmount").addElement("currency").addElement("value"), currencySynonymPath, currencyValue);
			}
		}
	}

	private void updateMappings(Path modelPath, Path newSynonymPath, String newValue) {
		// mark any existing mappings for deletion
		getEmptyMappings(newSynonymPath).forEach(m -> m.setError(REMOVE));
		// add new mappings
		getMapping(modelPath)
				// old synonym path, e.g., from the previously set value (that has been overwritten)
				.map(Mapping::getXmlPath)
				// find all mappings based on the old synonym path
				.map(oldSynonymPath -> getMappings(oldSynonymPath, ".quantityChange.after.trade.")).orElse(Collections.emptyList())
				.forEach(oldMapping -> {
					// add new mapping (there should be 2; one for the priceQuantity and one for reference in the product)
					Object modelValue = oldMapping.getRosettaValue() instanceof Reference ? oldMapping.getRosettaValue() : newValue;
					Mapping newMapping = new Mapping(newSynonymPath, newValue, oldMapping.getRosettaPath(), modelValue, null, true, true, false);
					getMappings().add(newMapping);
					LOGGER.debug("Added {}", newMapping);
					// mark old mapping for removal
					oldMapping.setError(REMOVE);
				});
	}

	private Optional<Mapping> getMapping(Path modelPath) {
		return getMappings().stream()
				.filter(m -> m.getRosettaPath() != null)
				.filter(m -> modelPath.nameIndexMatches(m.getRosettaPath()))
				.filter(m -> m.getXmlValue() != null)
				.findFirst();
	}

	private List<Mapping> getMappings(Path synonymPath, String containsSubPath) {
		return getMappings().stream()
				.filter(m -> synonymPath.nameIndexMatches(m.getXmlPath()))
				.filter(m -> m.getRosettaPath() != null)
				.filter(m -> m.getRosettaPath().toString().contains(containsSubPath))
				.collect(Collectors.toList());
	}

	private List<Mapping> getEmptyMappings(Path synonymPath) {
		return getMappings().stream()
				.filter(p -> synonymPath.nameIndexMatches(p.getXmlPath()))
				.filter(m -> m.getRosettaPath() == null || m.getError() != null)
				.collect(Collectors.toList());
	}
}