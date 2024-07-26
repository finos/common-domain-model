package cdm.product.template.processor;

import cdm.base.staticdata.party.PayerReceiver.PayerReceiverBuilder;
import cdm.legaldocumentation.contract.processor.PartyMappingHelper;
import cdm.product.asset.InterestRatePayout.InterestRatePayoutBuilder;
import cdm.product.common.settlement.processor.PriceQuantityHelper;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static cdm.product.template.processor.FraHelper.getDummyFloatingLegPath;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.filterMappings;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * FpML FRAs are represented as fra xml element, but in the CDM FRAs are represented with a fixed and a floating leg.
 *
 * FpML synonyms map the input FpML onto a single InterestRatePayout, then this mapper splits it into a fixed and floating InterestRatePayout.
 */
@SuppressWarnings("unused")
public class FraPayoutSplitterMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(FraPayoutSplitterMappingProcessor.class);

	public FraPayoutSplitterMappingProcessor(RosettaPath path, List<Path> synonymPaths, MappingContext context) {
		super(path, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		@SuppressWarnings("unchecked")
		List<InterestRatePayoutBuilder> interestRatePayouts = (List<InterestRatePayoutBuilder>) emptyIfNull(builder);
		if (interestRatePayouts.size() == 1 && interestRatePayouts.get(0).getRateSpecification() != null) {
			InterestRatePayoutBuilder fixedLeg = interestRatePayouts.get(0);
			InterestRatePayoutBuilder floatingLeg = interestRatePayouts.get(0).build().toBuilder();
			interestRatePayouts.add(floatingLeg);

			// update legs
			updateFixedLeg(fixedLeg);
			updateFloatingLeg(synonymPath, floatingLeg);
		}
	}

	/**
	 * Remove floating rate specification and fixing dates as these belongs on the floating leg.
	 */
	private void updateFixedLeg(InterestRatePayoutBuilder fixedLeg) {
		fixedLeg.getRateSpecification().setFloatingRateSpecification(null);
		
		if (fixedLeg.getResetDates() != null) {
			fixedLeg.getResetDates().setFixingDates(null);
		}
	}

	/**
	 * Remove fixed rate specification as it belongs to the fixed leg.
	 * Update paths in mappings to reference code still works.
	 * Flip payer/receiver parties (required as this leg was created as a copy of the fixed leg).
	 */
	private void updateFloatingLeg(Path synonymPath, InterestRatePayoutBuilder floatingLeg) {
		floatingLeg.getRateSpecification().toBuilder().setFixedRateSpecification(null);
		floatingLeg.setPaymentDates(null);
		
		getReferenceMapping(synonymPath.addElement("notional").addElement("amount"))
				.ifPresent(m -> addFloatingLegQuantityReference(m, floatingLeg));

		getReferenceMapping(synonymPath.addElement("floatingRateIndex"))
				.ifPresent(m -> updateFloatingRateIndexReference(m, floatingLeg));

		updateFloatingLegParties(floatingLeg);
	}

	private Optional<Mapping> getReferenceMapping(Path synonymPath) {
		return filterMappings(getContext().getMappings(), synonymPath).stream()
				.filter(m -> m.getRosettaValue() instanceof Reference.ReferenceBuilder)
				.findFirst();
	}

	private void addFloatingLegQuantityReference(Mapping mapping, InterestRatePayoutBuilder floatingLeg) {
		// create new dummy synonym path to differentiate it from the fixed leg path
		Path newSynonymPath = getDummyFloatingLegPath(mapping.getXmlPath());
		// create new reference object, so it can be linked to the price quantity
		Reference.ReferenceBuilder reference = Reference.builder();
		// update mapped object with new reference object
		floatingLeg.getOrCreatePriceQuantity().getOrCreateQuantitySchedule().setReference(reference);
		// add new mapping
		getMappings().add(new Mapping(newSynonymPath,
				mapping.getXmlValue(),
				mapping.getRosettaPath(),
				reference,
				null,
				false,
				true,
				false));
	}

	private void updateFloatingRateIndexReference(Mapping mapping, InterestRatePayoutBuilder floatingLeg) {
		Reference.ReferenceBuilder reference = Optional.of(floatingLeg)
				.map(b -> b.getRateSpecification())
				.map(b -> b.getFloatingRateSpecification())
				.map(b -> b.getRateOption())
				.map(b -> b.getReference())
				.orElse(null);
		mapping.setRosettaValue(reference);
		mapping.setRosettaPath(PriceQuantityHelper.incrementPathElementIndex(mapping.getRosettaPath(), "interestRatePayout", 1));
	}

	private void updateFloatingLegParties(InterestRatePayoutBuilder floatingLeg) {
		LOGGER.info("Flipping payer/receiver on new FRA interest rate payout");
		PayerReceiverBuilder newBuilder = floatingLeg.getPayerReceiver().toBuilder();

		PartyMappingHelper helper = PartyMappingHelper.getInstanceOrThrow(getContext());
		getPartyReference("buyerPartyReference")
				.ifPresent(partyReference ->
						helper.setCounterpartyRoleEnum(partyReference, newBuilder::setReceiver));
		getPartyReference("sellerPartyReference")
				.ifPresent(partyReference -> helper.setCounterpartyRoleEnum(partyReference,
						newBuilder::setPayer));
	}

	private Optional<String> getPartyReference(String endsWith) {
		return getMappings().stream()
				.filter(m -> m.getXmlPath() != null && m.getXmlValue() != null)
				.filter(m -> m.getXmlPath().endsWith("fra", endsWith, "href"))
				.findFirst()
				.map(Mapping::getXmlValue)
				.map(String.class::cast);
	}
}
