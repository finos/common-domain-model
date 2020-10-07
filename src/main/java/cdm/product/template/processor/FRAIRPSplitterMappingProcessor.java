package cdm.product.template.processor;

import cdm.base.staticdata.party.CounterpartyEnum;
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
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

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
		for (InterestRatePayoutBuilder irp:irps) {
			result.add(irp);
			RateSpecificationBuilder rateSpec = irp.getRateSpecification();
			if (rateSpec!=null) {
				if (rateSpec.getFixedRate()!=null && rateSpec.getFixedRate().hasData() &&
						rateSpec.getFloatingRate()!=null && rateSpec.getFloatingRate().hasData()) {
					//this IRP has both fixed and floating - it needs to be split

					InterestRatePayoutBuilder newIrp = irp.build().toBuilder();

					rateSpec.setFloatingRateBuilder(null);
					newIrp.getRateSpecification().setFixedRateBuilder(null);

					executor.execute(() -> {
						try {
							LOGGER.debug("Waiting for both counterparties");
							PartyMappingHelper helper = PartyMappingHelper.getInstance(getContext())
									.orElseThrow(() -> new IllegalStateException("PartyMappingHelper not found."));
							Map<String, CounterpartyEnum> map = helper.getBothCounterpartiesCollectedFuture().get();
							LOGGER.info("Flipping payer/receiver on new FRA interest rate payout");
							PayerReceiverBuilder newBuilder = newIrp.getPayerReceiver();
							getPartyReference("payer")
									.map(helper::translatePartyExternalReference)
									.map(map::get)
									.ifPresent(newBuilder::setReceiver);
							getPartyReference("receiver")
									.map(helper::translatePartyExternalReference)
									.map(map::get)
									.ifPresent(newBuilder::setPayer);
						} catch (InterruptedException|ExecutionException e) {
							LOGGER.warn("FRA payer / receiver splitter thread interrupted - {}", e.getMessage());
						}
					});
					result.add(newIrp);
				}
			}
		}
		irps.clear();
		irps.addAll(result);
	}

	private Optional<String> getPartyReference(String attribute) {
		return getMappings().stream()
				.filter(m -> m.getRosettaPath() != null)
				.filter(m -> m.getRosettaPath().endsWith("interestRatePayout", "payerReceiver", attribute))
				.findFirst()
				.map(Mapping::getXmlValue)
				.map(String.class::cast);
	}
}
