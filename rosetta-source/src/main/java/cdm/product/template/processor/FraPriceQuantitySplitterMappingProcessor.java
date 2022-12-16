package cdm.product.template.processor;

import cdm.product.common.settlement.processor.PriceQuantityHelper;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static cdm.product.common.settlement.PriceQuantity.PriceQuantityBuilder;
import static cdm.product.template.processor.FraHelper.getDummyFloatingLegPath;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.filterMappings;
import static com.rosetta.model.lib.meta.Reference.ReferenceBuilder;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * FpML FRAs are represented as fra xml element, but in the CDM FRAs are represented with a fixed and a floating leg.
 *
 * FpML synonyms map the input FpML onto a single PriceQuantity, then this mapper splits it into a fixed and floating PriceQuantity instances.
 */
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

		getNonReferenceMapping(synonymPath.addElement("notional").addElement("amount"))
				.ifPresent(this::updateFloatingLegQuantity);

		getNonReferenceMapping(synonymPath.addElement("floatingRateIndex"))
				.ifPresent(this::updateRateOption);
	}

	@NotNull
	private Optional<Mapping> getNonReferenceMapping(Path synonymPath) {
		return filterMappings(getContext().getMappings(), synonymPath).stream()
				.filter(m -> !(m.getRosettaValue() instanceof ReferenceBuilder))
				.filter(m -> Arrays.stream(m.getRosettaPath().getPathNames()).anyMatch("tradeLot"::equals))
				.filter(m -> m.getXmlValue() != null)
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

