package cdm.product.template.processor;

import cdm.base.staticdata.asset.common.AssetClassEnum;
import cdm.base.staticdata.asset.common.AssetIdTypeEnum;
import cdm.observable.asset.FloatingRateIndex;
import cdm.observable.asset.processor.PriceQuantityHelper;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static cdm.observable.asset.PriceQuantity.PriceQuantityBuilder;
import static cdm.product.template.processor.FraHelper.getDummyFloatingLegPath;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.filterMappings;
import static com.rosetta.model.lib.meta.Reference.ReferenceBuilder;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * FpML FRAs are represented as fra xml element, but in the CDM FRAs are represented with a fixed and a floating leg.
 *
 * FpML synonyms map the input FpML onto a single PriceQuantity, then this mapper splits it into a fixed and floating PriceQuantity instances.
 */
@SuppressWarnings("unused")
public class FraPriceQuantitySplitterMappingProcessor extends MappingProcessor {

	public FraPriceQuantitySplitterMappingProcessor(RosettaPath path, List<Path> synonymPaths, MappingContext context) {
		super(path, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		List<PriceQuantityBuilder> priceQuantityList = emptyIfNull((List<PriceQuantityBuilder>) builder);
		if (priceQuantityList.size() == 1) {
			PriceQuantityBuilder fixedLegPriceQuantity = priceQuantityList.get(0);
			PriceQuantityBuilder floatingLegPriceQuantity = priceQuantityList.get(0).build().toBuilder();
			priceQuantityList.add(floatingLegPriceQuantity);

			updateFixedLeg(fixedLegPriceQuantity);
			updateFloatingLeg(synonymPath, floatingLegPriceQuantity);
		}
	}

	private PriceQuantityBuilder updateFixedLeg(PriceQuantityBuilder fixedLegPriceQuantity) {
		return fixedLegPriceQuantity.setObservable(null);
	}

	private void updateFloatingLeg(Path synonymPath, PriceQuantityBuilder floatingLegPriceQuantity) {
		floatingLegPriceQuantity.getPrice().clear();
		
		FloatingRateIndex.FloatingRateIndexBuilder floatingRateIndexBuilder = floatingLegPriceQuantity
				.getOrCreateObservable()
				.getOrCreateValue()
				.getOrCreateIndex()
				.getOrCreateInterestRateIndex()
				.getOrCreateValue()
				.getOrCreateFloatingRateIndex();
		emptyIfNull(floatingRateIndexBuilder.getIdentifier())
				.forEach(b -> b.setIdentifierType(AssetIdTypeEnum.OTHER));
		floatingRateIndexBuilder
				.setAssetClass(AssetClassEnum.INTEREST_RATE);

		getNonReferenceMapping(synonymPath.addElement("notional").addElement("amount"))
				.ifPresent(this::updateFloatingLegQuantity);

		getNonReferenceMapping(synonymPath.addElement("floatingRateIndex"))
				.ifPresent(this::updateRateOption);
	}

	private Optional<Mapping> getNonReferenceMapping(Path synonymPath) {
		return filterMappings(getMappings(), synonymPath).stream()
				.filter(m -> !(m.getRosettaValue() instanceof ReferenceBuilder))
				.filter(m -> Arrays.stream(m.getRosettaPath().getPathNames()).anyMatch("tradeLot"::equals))
				.filter(m -> m.getXmlValue() != null)
				.filter(m -> m.getRosettaValue() != null)
				.findFirst();
	}

	private void updateFloatingLegQuantity(Mapping mapping) {
		// create new dummy synonym path to differentiate it from the fixed leg path
		Path newSynonymPath = getDummyFloatingLegPath(mapping.getXmlPath());
		// update model path to the correct price quantity instance
		Path rosettaPath = PriceQuantityHelper.incrementPathElementIndex(mapping.getRosettaPath(), "priceQuantity", 1);
		// add new mapping
		getMappings().add(new Mapping(newSynonymPath,
				mapping.getXmlValue(),
				rosettaPath,
				mapping.getRosettaValue(),
				null,
				false,
				true,
				false));
	}

	private void updateRateOption(Mapping mapping) {
		// update model path to the correct price quantity instance
		Path rosettaPath = PriceQuantityHelper.incrementPathElementIndex(mapping.getRosettaPath(), "priceQuantity", 1);
		// update existing mapping
		mapping.setRosettaPath(rosettaPath);
	}
}

