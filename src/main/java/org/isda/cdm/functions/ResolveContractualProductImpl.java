package org.isda.cdm.functions;

import java.util.List;

import org.isda.cdm.ContractualProduct;
import org.isda.cdm.ContractualProduct.ContractualProductBuilder;
import org.isda.cdm.ResolvablePayoutQuantity.ResolvablePayoutQuantityBuilder;

import com.google.inject.Inject;
import com.regnosys.rosetta.common.util.RosettaObjectBuilderCollectorProcessStep;

import cdm.observable.asset.QuantityNotation;

public class ResolveContractualProductImpl extends ResolveContractualProduct {

	@Inject
	private ResolvePayoutQuantity resolvePayoutQuantityFunc;

	@Override
	protected ContractualProductBuilder doEvaluate(ContractualProduct contractualProduct, List<QuantityNotation> quantityNotations) {
		ContractualProductBuilder contractualProductBuilder = contractualProduct.toBuilder();
		// Find all ResolvablePayoutQuantity instances
		List<ResolvablePayoutQuantityBuilder> resolvablePayoutQuantityBuilders = new RosettaObjectBuilderCollectorProcessStep<>(ResolvablePayoutQuantityBuilder.class)
				.runProcessStep(ContractualProduct.class, contractualProductBuilder)
				.getCollectedObjects();
		// Resolve each instance
		resolvablePayoutQuantityBuilders.forEach(b -> resolveQuantity(b, quantityNotations, contractualProduct));

		return contractualProductBuilder;
	}

	private void resolveQuantity(ResolvablePayoutQuantityBuilder builder, List<QuantityNotation> quantityNotations, ContractualProduct contractualProduct) {
		builder
			.setResolvedQuantity(resolvePayoutQuantityFunc.evaluate(builder.build(), quantityNotations, contractualProduct))
			.build();
	}

}
