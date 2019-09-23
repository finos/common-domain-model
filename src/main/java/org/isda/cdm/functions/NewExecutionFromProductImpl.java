package org.isda.cdm.functions;

import org.isda.cdm.Execution;
import org.isda.cdm.ExecutionPrimitive;
import org.isda.cdm.ExecutionPrimitive.ExecutionPrimitiveBuilder;
import org.isda.cdm.ExecutionState;
import org.isda.cdm.Identifier;
import org.isda.cdm.Party;
import org.isda.cdm.Product;
import org.isda.cdm.functions.example.services.identification.IdentifierService;
import org.isda.cdm.processor.PostProcessorProvider;

import com.google.inject.Inject;

public class NewExecutionFromProductImpl extends NewExecutionFromProduct {
	
	@Inject private IdentifierService identifierService;
	@Inject private PostProcessorProvider postProcessorProvider;
	
	@Override
	protected ExecutionPrimitiveBuilder doEvaluate(Product product, Party partyA, Party partyB) {
		Identifier id = identifierService.nextType(partyA.getMeta().getExternalKey(), Execution.class.getSimpleName());

		ExecutionPrimitiveBuilder builder = ExecutionPrimitive
				.builder()
				.setAfterBuilder(ExecutionState.builder()
						.setExecutionBuilder(Execution.builder()
								.addIdentifier(id)
								.setProduct(product)));

		postProcessorProvider.getPostProcessor().forEach(step -> step.runProcessStep(ExecutionPrimitive.class, builder));
		return builder;
	}
}
