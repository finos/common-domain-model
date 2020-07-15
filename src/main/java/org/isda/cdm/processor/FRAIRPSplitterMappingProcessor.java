package org.isda.cdm.processor;

import cdm.base.staticdata.party.CounterpartyEnum;
import cdm.base.staticdata.party.PayerReceiver.PayerReceiverBuilder;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.InterestRatePayout.InterestRatePayoutBuilder;
import org.isda.cdm.RateSpecification.RateSpecificationBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static cdm.base.staticdata.party.metafields.ReferenceWithMetaAccount.ReferenceWithMetaAccountBuilder;
import static cdm.base.staticdata.party.metafields.ReferenceWithMetaParty.ReferenceWithMetaPartyBuilder;

@SuppressWarnings("unused")
public class FRAIRPSplitterMappingProcessor extends MappingProcessor {

	public FRAIRPSplitterMappingProcessor(RosettaPath path, List<Path> synonymPaths, MappingContext mappingContext) {
		super(path, synonymPaths, mappingContext);
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
							.setCounterpartyEnumThen(newIrp.getPayerReceiver(), flipPayerReceiver());

					result.add(newIrp);
				}
			}
		}
		irps.clear();
		irps.addAll(result);
	}

	@NotNull
	private Consumer<PayerReceiverBuilder> flipPayerReceiver() {
		return (builder) -> {
			CounterpartyEnum payer = builder.getPayer();
			ReferenceWithMetaPartyBuilder payerPartyReference = builder.getPayerPartyReference();
			ReferenceWithMetaAccountBuilder payerAccountReference = builder.getPayerAccountReference();

			builder.setPayer(builder.getReceiver());
			builder.setPayerAccountReferenceBuilder(builder.getReceiverAccountReference());
			builder.setPayerPartyReferenceBuilder(builder.getReceiverPartyReference());

			builder.setReceiver(payer);
			builder.setReceiverAccountReferenceBuilder(payerAccountReference);
			builder.setReceiverPartyReferenceBuilder(payerPartyReference);
		};
	}
}
