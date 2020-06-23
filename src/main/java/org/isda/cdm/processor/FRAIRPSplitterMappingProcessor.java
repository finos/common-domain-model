package org.isda.cdm.processor;

import cdm.base.staticdata.party.PayerReceiver.PayerReceiverBuilder;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaAccount.ReferenceWithMetaAccountBuilder;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty.ReferenceWithMetaPartyBuilder;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.InterestRatePayout.InterestRatePayoutBuilder;
import org.isda.cdm.RateSpecification.RateSpecificationBuilder;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class FRAIRPSplitterMappingProcessor extends MappingProcessor {

	public FRAIRPSplitterMappingProcessor(RosettaPath path, List<Path> synonymPaths, List<Mapping> mappings) {
		super(path, synonymPaths, mappings);
	}

	@Override
	protected void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
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
					
					PayerReceiverBuilder payerReceiver = newIrp.getPayerReceiver();
					flipPR(payerReceiver);
					result.add(newIrp);
				}
			}
		}
		irps.clear();
		irps.addAll(result);
	}

	private void flipPR(PayerReceiverBuilder payerReceiver) {
		ReferenceWithMetaPartyBuilder payerPartyReference = payerReceiver.getPayerPartyReference();
		ReferenceWithMetaAccountBuilder payerAccountReference = payerReceiver.getPayerAccountReference();
		
		payerReceiver.setPayerAccountReferenceBuilder(payerReceiver.getReceiverAccountReference());
		payerReceiver.setPayerPartyReferenceBuilder(payerReceiver.getReceiverPartyReference());
		payerReceiver.setReceiverAccountReferenceBuilder(payerAccountReference);
		payerReceiver.setReceiverPartyReferenceBuilder(payerPartyReference);
	}
}
