package org.isda.cdm.functions;

import org.isda.cdm.Contract;
import org.isda.cdm.ContractFormation;
import org.isda.cdm.ContractFormation.ContractFormationBuilder;
import org.isda.cdm.ContractualProduct;
import org.isda.cdm.Documentation;
import org.isda.cdm.ExecutionPrimitive;
import org.isda.cdm.ExecutionState;
import org.isda.cdm.Identifier;
import org.isda.cdm.LegalAgreement;
import org.isda.cdm.Party;
import org.isda.cdm.PostInceptionState;
import org.isda.cdm.functions.example.services.identification.IdentifierService;
import org.isda.cdm.metafields.ReferenceWithMetaLegalAgreement;
import org.isda.cdm.processor.PostProcessorProvider;

import com.google.inject.Inject;

public class NewContractFormationFromExecutionImpl extends NewContractFormationFromExecution {
	
	@Inject private IdentifierService identifierService;
	@Inject private PostProcessorProvider postProcessorProvider;

	@Override
	protected ContractFormationBuilder doEvaluate(ExecutionState executionState, Party partyA, Party partyB,
			LegalAgreement legalAgreement) {
		Identifier id = identifierService.nextType(partyA.getMeta().getExternalKey(), Contract.class.getSimpleName());
		ContractualProduct contractualProduct = executionState.getExecution().getProduct().getContractualProduct();

		ContractFormationBuilder builder = ContractFormation.builder().setBefore(executionState)
				.setAfterBuilder(PostInceptionState.builder().setContractBuilder(
						Contract.builder().addContractIdentifier(id).setContractualProduct(contractualProduct)));

		if (legalAgreement != null) {
			builder.getAfter().getContract().setDocumentationBuilder(
					Documentation.builder().addLegalAgreementBuilder(ReferenceWithMetaLegalAgreement.builder()
							.setValue(legalAgreement).setGlobalReference(legalAgreement.getMeta().getGlobalKey())));
		}

		postProcessorProvider.getPostProcessor()
				.forEach(step -> step.runProcessStep(ExecutionPrimitive.class, builder));

		return builder;
	}
}
