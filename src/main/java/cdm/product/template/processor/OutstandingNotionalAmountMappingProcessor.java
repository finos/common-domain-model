package cdm.product.template.processor;

import cdm.base.math.MeasureBase;
import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import cdm.product.common.settlement.processor.PriceQuantityHelper;
import cdm.product.template.TradeLot;
import com.regnosys.rosetta.common.translation.*;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;

@SuppressWarnings("unused") // used in generated code
public class OutstandingNotionalAmountMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(OutstandingNotionalAmountMappingProcessor.class);
	private static final String REMOVE = "REMOVE";

	private final boolean isQuantityChangeAfterTrade;
	private final boolean isContractFormationAfterTrade;

	public OutstandingNotionalAmountMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
		Path path = PathUtils.toPath(getModelPath());
		this.isQuantityChangeAfterTrade = Path.parse("WorkflowStep.businessEvent.primitives.quantityChange.after").nameStartMatches(path);
		this.isContractFormationAfterTrade = Path.parse("WorkflowStep.businessEvent.primitives.contractFormation.after").nameStartMatches(path);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> tradeLots, RosettaModelObjectBuilder tradableProduct) {
		// map of modelPath to quantityBuilder
		Map<Path, Quantity.QuantityBuilder> quantityMap =
				PriceQuantityHelper.getQuantityMap(getModelPath(), (List<? extends TradeLot.TradeLotBuilder>) tradeLots);
		// the input synonym path does not have indexes, find all synonym paths (with indexes) by searching the mappings
		Set<Path> indexedSynonymPaths = getIndexedSynonymPaths(synonymPath);

		if ((isNovation(synonymPath) && isContractFormationAfterTrade) || (isTermination(synonymPath) && isQuantityChangeAfterTrade)) {
			indexedSynonymPaths.forEach(indexedSynonymPath ->
					quantityMap.entrySet().forEach(e ->
							setQuantityAmountToOutstandingNotional(indexedSynonymPath, e.getKey(), e.getValue())));
		}

		if (isNovation(synonymPath) && isQuantityChangeAfterTrade) {
			AtomicInteger index = new AtomicInteger(0);
			indexedSynonymPaths.forEach(indexedSynonymPath ->
					quantityMap.entrySet().forEach(e ->
							setQuantityAmountToZero(indexedSynonymPath, e.getKey(), e.getValue(), index.getAndIncrement())));
		}

		// Remove out-of-date mappings
		getMappings().removeAll(getMappings().stream()
				.filter(m -> REMOVE.equals(m.getError()))
				.collect(Collectors.toList()));
	}

	@NotNull
	private Set<Path> getIndexedSynonymPaths(Path unindexedSynonymPath) {
		return getMappings().stream()
				.map(Mapping::getXmlPath)
				.filter(unindexedSynonymPath::nameStartMatches)
				.map(Path::getParent)
				.collect(Collectors.toSet());
	}

	private boolean isNovation(Path synonymPath) {
		return synonymPath.toString().contains(".novation.");
	}

	private boolean isTermination(Path synonymPath) {
		return synonymPath.toString().contains(".termination.");
	}

	private void setQuantityAmountToOutstandingNotional(Path synonymPath, Path modelPath, Quantity.QuantityBuilder quantity) {
		Path amountSynonymPath = synonymPath.addElement("amount");
		Optional<Mapping> afterNotionalAmount = MappingProcessorUtils.getNonNullMapping(getMappings(), amountSynonymPath);
		Path currencySynonymPath = synonymPath.addElement("currency");
		Optional<Mapping> afterNotionalCurrency = MappingProcessorUtils.getNonNullMapping(getMappings(), currencySynonymPath);

		if (afterNotionalCurrency.isPresent() && afterNotionalAmount.isPresent()) {
			String currencyValue = (String) afterNotionalCurrency.get().getXmlValue();
			String existingCurrencyValue = Optional.ofNullable(quantity)
					.map(MeasureBase.MeasureBaseBuilder::getUnitOfAmount)
					.map(UnitType.UnitTypeBuilder::getCurrency)
					.map(FieldWithMetaString::getValue)
					.orElse(null);
			if (currencyValue.equals(existingCurrencyValue)) {
				String amountValue = (String) afterNotionalAmount.get().getXmlValue();
				quantity.setAmount(new BigDecimal(amountValue));

				// Update mappings
				updateMappings(modelPath.addElement("amount"), amountSynonymPath, amountValue);
				updateMappings(modelPath.addElement("unitOfAmount").addElement("currency").addElement("value"), currencySynonymPath, currencyValue);
			}
		}
	}

	private void updateMappings(Path modelPath, Path newSynonymPath, String newValue) {
		// mark any existing mappings for deletion
		getEmptyMappings(getMappings(), newSynonymPath).forEach(m -> m.setError(REMOVE));
		// add new mappings
		getNonNullMappingForModelPath(getMappings(), modelPath)
				// old synonym path, e.g., from the previously set value (that has been overwritten)
				.map(Mapping::getXmlPath)
				// find all mappings based on the old synonym path and trade
				.map(oldSynonymPath ->
						filterMappings(getMappings(), oldSynonymPath, subPath("trade", modelPath).orElse(new Path())))
				.orElse(Collections.emptyList())
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

	private void setQuantityAmountToZero(Path synonymPath, Path modelPath, Quantity.QuantityBuilder quantity, int index) {
		Optional<String> existingCurrencyValue = Optional.ofNullable(quantity)
				.map(MeasureBase.MeasureBaseBuilder::getUnitOfAmount)
				.map(UnitType.UnitTypeBuilder::getCurrency)
				.map(FieldWithMetaString::getValue);
		if (existingCurrencyValue.isPresent()) {
			String amountValue = "0.0";
			quantity.setAmount(new BigDecimal(amountValue));

			// Update mappings
			updateMappings(modelPath.addElement("amount"), new Path().addElement("dummy", index), amountValue);
		}
	}
}