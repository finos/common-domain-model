package cdm.product.template.processor;

import cdm.base.staticdata.party.PayerReceiver.PayerReceiverBuilder;
import cdm.legalagreement.contract.processor.PartyMappingHelper;
import cdm.product.asset.InterestRatePayout.InterestRatePayoutBuilder;
import cdm.product.asset.RateSpecification.RateSpecificationBuilder;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;
import static com.regnosys.rosetta.common.util.PathUtils.toPath;

@SuppressWarnings("unused")
public class FRAIRPSplitterMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(FRAIRPSplitterMappingProcessor.class);

	private final ExecutorService executor;

	public FRAIRPSplitterMappingProcessor(RosettaPath path, List<Path> synonymPaths, MappingContext context) {
		super(path, synonymPaths, context);
		this.executor = context.getExecutor();
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		@SuppressWarnings("unchecked")
		List<InterestRatePayoutBuilder> irps = (List<InterestRatePayoutBuilder>) builder;
		List<InterestRatePayoutBuilder> result = new ArrayList<>();
		for (InterestRatePayoutBuilder irp : irps) {
			result.add(irp);
			RateSpecificationBuilder rateSpec = irp.getRateSpecification();
			if (rateSpec != null) {
				if (rateSpec.getFixedRate() != null && // fixedRate.hasData() doesn't work due to references
						rateSpec.getFloatingRate() != null //&&  // floatingRate.hasData() does work (the rate option is populated)
				//		(rateSpec.getFloatingRate().hasData())
				) {
					//this IRP has both fixed and floating - it needs to be split

					InterestRatePayoutBuilder newIrp = irp.build().toBuilder();

					rateSpec.setFloatingRateBuilder(null);
					newIrp.getRateSpecification().setFixedRateBuilder(null);

					List<Mapping> mappings = filterMappings(getContext().getMappings(), synonymPath.addElement("floatingRateIndex"));
					mappings.stream().filter(mapping -> toPath(getModelPath()).fullStartMatches(mapping.getRosettaPath())).findFirst().ifPresent(
							mapping -> {
								System.out.println(mapping);

								Path.PathElement interestRatePayoutPathElement = subPath("interestRatePayout", mapping.getRosettaPath())
										.map(Path::getLastElement)
										.orElse(null);
								Path path = new Path(mapping.getRosettaPath().getElements().stream()
										.map(pathElement -> {
											Optional<Integer> index = pathElement.equals(interestRatePayoutPathElement) ?
													Optional.of(pathElement.forceGetIndex() + 1) :
													pathElement.getIndex();
											return new Path.PathElement(pathElement.getPathName(), index, pathElement.getMetas());
										})
										.collect(Collectors.toList()));

								mapping.setRosettaPath(path);
								mapping.setRosettaValue(newIrp.getRateSpecification().getFloatingRate().getRateOption().getReference());
							});

					PartyMappingHelper helper = PartyMappingHelper.getInstanceOrThrow(getContext());

					addInvokedTask(helper.getBothCounterpartiesCollectedFuture()
							.thenAcceptAsync(map -> {
								LOGGER.info("Flipping payer/receiver on new FRA interest rate payout");
								PayerReceiverBuilder newBuilder = newIrp.getPayerReceiver();
								// Get party references from xml mappings rather than rosetta mapped objects to avoid race conditions
								getPartyReference("buyerPartyReference")
										.map(helper::translatePartyExternalReference)
										.map(map::get)
										.ifPresent(newBuilder::setReceiver);
								getPartyReference("sellerPartyReference")
										.map(helper::translatePartyExternalReference)
										.map(map::get)
										.ifPresent(newBuilder::setPayer);
							}, executor));
					result.add(newIrp);
				}
			}
		}
		irps.clear();
		irps.addAll(result);
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
