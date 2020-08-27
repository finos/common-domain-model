package org.isda.cdm.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import cdm.base.staticdata.party.PayerReceiver.PayerReceiverBuilder;
import cdm.product.asset.InterestRatePayout.InterestRatePayoutBuilder;
import cdm.product.asset.RateSpecification.RateSpecificationBuilder;

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

					CounterpartyMappingHelper.getInstance(getContext())
							.orElseThrow(() -> new IllegalStateException("CounterpartyMappingHelper not found."))
							.getBothCounterpartiesCollectedFuture()
							.thenAcceptAsync(map -> flipPayerReceiver(irp.getPayerReceiver(), newIrp.getPayerReceiver()), executor);

					result.add(newIrp);
				}
			}
		}
		irps.clear();
		irps.addAll(result);
	}

	@NotNull
	private void flipPayerReceiver(PayerReceiverBuilder originalBuilder, PayerReceiverBuilder newBuilder) {
		LOGGER.info("Flipping payer/receiver on new FRA interest rate payout");
		newBuilder.setPayer(originalBuilder.getReceiver());
		newBuilder.setPayerPartyReferenceBuilder(originalBuilder.getReceiverPartyReference());

		newBuilder.setReceiver(originalBuilder.getPayer());
		newBuilder.setReceiverPartyReferenceBuilder(originalBuilder.getPayerPartyReference());
	}
}
