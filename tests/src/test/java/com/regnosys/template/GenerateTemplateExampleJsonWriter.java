package com.regnosys.template;

import cdm.base.staticdata.asset.common.Security;
import cdm.event.common.TradeState;
import cdm.product.asset.InterestRatePayout;
import cdm.product.template.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.io.Resources;
import com.google.inject.Guice;
import jakarta.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.regnosys.ingest.test.framework.ingestor.IngestionReport;
import com.regnosys.ingest.test.framework.ingestor.IngestionTestUtil;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionFactory;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionService;
import com.regnosys.rosetta.RosettaRuntimeModule;
import com.regnosys.rosetta.RosettaStandaloneSetup;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.regnosys.rosetta.common.hashing.ReKeyProcessStep;
import com.regnosys.rosetta.common.postprocess.qualify.QualifyProcessorStep;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.util.UrlUtils;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.metafields.MetaAndTemplateFields;
import org.eclipse.xtext.common.TerminalsStandaloneSetup;
import org.finos.cdm.CdmRuntimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Generates sample json for com.regnosys.cdm.example.template.TemplateExample.
 */
public class GenerateTemplateExampleJsonWriter {
	private static final String INSTANCE_NAME = "target/FpML_5_10";

	@Inject RosettaTypeValidator validator;
	@Inject QualifyProcessorStep qualifyProcessorStep;

	private static final Logger LOGGER = LoggerFactory.getLogger(GenerateTemplateExampleJsonWriter.class);
	private static final String SAMPLE_PATH = "cdm-sample-files/fpml-5-10/products/equity/eqs-ex01-single-underlyer-execution-long-form.xml";
	private Injector injector;

	public static void main(String[] args) throws IOException {
		new GenerateTemplateExampleJsonWriter().init(args);
	}

	public void init(String[] args) throws IOException {
		// Guice Injection
		Module runtimeModule = new CdmRuntimeModule();
		injector = Guice.createInjector(runtimeModule);
		initialiseIngestionFactory(runtimeModule);
		injector.injectMembers(this);

		String outputPath = Arrays.stream(args).findFirst().orElse("target/template/");
		LOGGER.info("Output path {}", outputPath);

		IngestionService ingestionService = IngestionFactory
				.getInstance(INSTANCE_NAME)
				.getService("FpML_5_Confirmation_To_TradeState");

		IngestionReport<TradeState> ingest = ingestionService.ingestValidateAndPostProcess(TradeState.class, UrlUtils.openURL(Resources.getResource(SAMPLE_PATH)));
		generateTemplateExamples(ingest.getRosettaModelInstance(), outputPath);
	}

	private void initialiseIngestionFactory(Module moduleRuntimeModule) {
		IngestionFactory.init(INSTANCE_NAME,
				GenerateTemplateExampleJsonWriter.class.getClassLoader(),
				setupRuntimeModules(moduleRuntimeModule),
				IngestionTestUtil.getPostProcessors(injector).toArray(new PostProcessStep[0]));
	}

	private Module setupRuntimeModules(Module modelRuntimeModule) {
		TerminalsStandaloneSetup.doSetup();
		Module combinedModules = Modules.combine(modelRuntimeModule, new RosettaRuntimeModule());
		injector = Guice.createInjector(combinedModules);
		(new RosettaStandaloneSetup()).register(injector);
		return combinedModules;
	}

	private void generateTemplateExamples(TradeState tradeState, String outFolder) throws IOException {
		ContractualProduct contractualProductTemplate = getContractualProductTemplate(tradeState);
		writeFileToDisk(Paths.get(outFolder), "contractual-product-template.json", contractualProductTemplate);

		TradeState unmergedContract = getUnmergedContract(tradeState, contractualProductTemplate.getMeta().getGlobalKey());
		writeFileToDisk(Paths.get(outFolder), "trade-state-unmerged.json", unmergedContract);

		TradeState mergedContract = getMergedContract(tradeState, contractualProductTemplate.getMeta().getGlobalKey());
		writeFileToDisk(Paths.get(outFolder), "trade-state-merged.json", mergedContract);
	}

	private ContractualProduct getContractualProductTemplate(TradeState inputTradeState) {
		ContractualProduct.ContractualProductBuilder templateBuilder = inputTradeState.toBuilder().getTrade().getTradableProduct().getProduct().getContractualProduct();

		PerformancePayout.PerformancePayoutBuilder performancePayoutBuilder = templateBuilder.getEconomicTerms().getPayout().getPerformancePayout().get(0);
		performancePayoutBuilder
				.setValuationDates(null)
				.setPaymentDates(null)
				.setReturnTerms(null);
		performancePayoutBuilder.getUnderlier().getSecurity().setProductIdentifier(null);

		InterestRatePayout.InterestRatePayoutBuilder interestRatePayoutBuilder = templateBuilder.getEconomicTerms().getPayout().getInterestRatePayout().get(0);
		interestRatePayoutBuilder
			.setCalculationPeriodDates(null)
			.setPaymentDates(null);

		reKeyPostProcess(ContractualProduct.class, templateBuilder.prune());

		return templateBuilder.build();
	}

	private TradeState getMergedContract(TradeState inputContract, String templateGlobalReference) {
		TradeState.TradeStateBuilder mergedContractBuilder = inputContract.toBuilder();
		mergedContractBuilder.getTrade().getTradableProduct().getProduct().getContractualProduct().getMeta().setTemplateGlobalReference(templateGlobalReference);

		reKeyPostProcess(TradeState.class, mergedContractBuilder.prune());

		return mergedContractBuilder.build();
	}

	private TradeState getUnmergedContract(TradeState inputContract, String templateGlobalReference) {
		ContractualProduct contractualProductInstance = getContractualProduct(inputContract, templateGlobalReference);

		TradeState.TradeStateBuilder unmergedContractBuilder = inputContract.toBuilder();
		unmergedContractBuilder.getTrade().getTradableProduct().getProduct().setContractualProduct(contractualProductInstance);

		reKeyPostProcess(TradeState.class, unmergedContractBuilder.prune());

		return unmergedContractBuilder.build();
	}

	private ContractualProduct getContractualProduct(TradeState inputContract, String templateGlobalReference) {
		ContractualProduct contractualProduct = inputContract.getTrade().getTradableProduct().getProduct().getContractualProduct();
		PerformancePayout performancePayout = contractualProduct.getEconomicTerms().getPayout().getPerformancePayout().get(0);
		InterestRatePayout interestRatePayout = contractualProduct.getEconomicTerms().getPayout().getInterestRatePayout().get(0);

		ContractualProduct.ContractualProductBuilder contractualProductBuilder = ContractualProduct.builder()
			.setMeta(MetaAndTemplateFields.builder().setTemplateGlobalReference(templateGlobalReference))
			.setEconomicTerms(EconomicTerms.builder()
				.setPayout(Payout.builder()
					.addPerformancePayout(PerformancePayout.builder()
						.setValuationDates(performancePayout.getValuationDates())
						.setPaymentDates(performancePayout.getPaymentDates())
						.setReturnTerms(performancePayout.getReturnTerms())
						.setUnderlier(Product.builder()
								.setSecurity(Security.builder()
									.addProductIdentifier(performancePayout.getUnderlier().getSecurity().getProductIdentifier()))))
					.addInterestRatePayout(InterestRatePayout.builder()
						.setCalculationPeriodDates(interestRatePayout.getCalculationPeriodDates())
						.setPaymentDates(interestRatePayout.getPaymentDates()))));

		reKeyPostProcess(ContractualProduct.class, contractualProductBuilder.prune());

		return contractualProductBuilder.build();
	}

	private static void writeFileToDisk(Path folder, String filename, RosettaModelObject object) throws IOException {
		Path path = folder.resolve(filename);
		Files.createDirectories(path.getParent());
		LOGGER.info("Writing path {}", path);

		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write(toJson(object));
		}
	}

	private static String toJson(Object object) throws JsonProcessingException {
		return RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
	}

	private void reKeyPostProcess(Class<? extends RosettaModelObject> clazz, RosettaModelObjectBuilder builder) {
		GlobalKeyProcessStep globalKeyProcessStep = new GlobalKeyProcessStep(NonNullHashCollector::new);
		Arrays.asList(globalKeyProcessStep, new ReKeyProcessStep(globalKeyProcessStep))
			.forEach(p -> p.runProcessStep(clazz, builder));

	}
}

