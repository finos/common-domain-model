package org.isda.cdm.processor;

import java.util.ArrayList;
import java.util.List;

import org.isda.cdm.InterestRatePayout.InterestRatePayoutBuilder;
import org.isda.cdm.PayerReceiver.PayerReceiverBuilder;
import org.isda.cdm.RateSpecification.RateSpecificationBuilder;

import com.regnosys.rosetta.common.translation.Mapping;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.ReferenceWithMetaAccount.ReferenceWithMetaAccountBuilder;
import com.rosetta.model.metafields.ReferenceWithMetaParty.ReferenceWithMetaPartyBuilder;

public class FRAIRPSplitterMappingProcessor extends MappingProcessor {

	public FRAIRPSplitterMappingProcessor(RosettaPath path, List<Mapping> mappings) {
		super(path, mappings);
	}

	@Override
	protected <R extends RosettaModelObject> void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
	}

	@Override
	protected void map(List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
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
