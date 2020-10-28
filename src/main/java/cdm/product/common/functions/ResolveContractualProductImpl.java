package cdm.product.common.functions;

import cdm.observable.asset.QuantityNotation;
import cdm.product.common.settlement.ResolvablePayoutQuantity.ResolvablePayoutQuantityBuilder;
import cdm.product.template.ContractualProduct;
import cdm.product.template.ContractualProduct.ContractualProductBuilder;
import com.google.inject.Inject;
import com.regnosys.rosetta.common.util.RosettaObjectBuilderCollectorProcessStep;

import java.util.List;

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
