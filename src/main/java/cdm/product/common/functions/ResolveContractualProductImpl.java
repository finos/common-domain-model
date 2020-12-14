package cdm.product.common.functions;

//public class ResolveContractualProductImpl extends ResolveContractualProduct {
//
//	@Inject
//	private ResolvePayoutQuantity resolvePayoutQuantityFunc;
//
//	@Override
//	protected ContractualProductBuilder doEvaluate(ContractualProduct contractualProduct, List<QuantityNotation> quantityNotations) {
//		ContractualProductBuilder contractualProductBuilder = contractualProduct.toBuilder();
//		// Find all ResolvablePayoutQuantity instances
//		List<ResolvablePayoutQuantityBuilder> resolvablePayoutQuantityBuilders = new RosettaObjectBuilderCollectorProcessStep<>(ResolvablePayoutQuantityBuilder.class)
//				.runProcessStep(ContractualProduct.class, contractualProductBuilder)
//				.getCollectedObjects();
//		// Resolve each instance
//		resolvablePayoutQuantityBuilders.forEach(b -> resolveQuantity(b, quantityNotations, contractualProduct));
//
//		return contractualProductBuilder;
//	}
//
//	private void resolveQuantity(ResolvablePayoutQuantityBuilder builder, List<QuantityNotation> quantityNotations, ContractualProduct contractualProduct) {
//		builder
//			.setResolvedQuantity(resolvePayoutQuantityFunc.evaluate(builder.build(), quantityNotations, contractualProduct))
//			.build();
//	}
//
//}
