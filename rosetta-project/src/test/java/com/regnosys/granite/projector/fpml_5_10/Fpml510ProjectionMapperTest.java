package com.regnosys.granite.projector.fpml_5_10;

import cdm.event.common.TradeState;
import com.google.common.io.Resources;
import com.google.inject.Module;
import com.google.inject.*;
import com.google.inject.util.Modules;
import com.regnosys.ingest.test.framework.ingestor.IngestionReport;
import com.regnosys.ingest.test.framework.ingestor.IngestionTestUtil;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionFactory;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionService;
import com.regnosys.ingest.test.framework.ingestor.synonym.MappingResult;
import com.regnosys.rosetta.RosettaRuntimeModule;
import com.regnosys.rosetta.RosettaStandaloneSetup;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.util.UrlUtils;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.process.PostProcessStep;
import org.eclipse.xtext.common.TerminalsStandaloneSetup;
import org.fpml.fpml_5.confirmation.DataDocument;
import org.fpml.fpml_5.confirmation.Document;
import org.fpml.fpml_5.confirmation.RequestClearing;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.regnosys.granite.projector.fpml_5_10.ProjectionMappingReport.ProjectionMappingReportBuilder;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
class Fpml510ProjectionMapperTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(Fpml510ProjectionMapperTest.class);

	private static final String INSTANCE_NAME = "target/FpML_5_10";
	private static final String RATES_DIR = "cdm-sample-files/fpml-5-10/products/rates/";
	private static final String LCH_DIR = "available-samples/lch-samples/";

	private static final List<Expectations> FPML_DOCUMENT_FILES = List.of(
		new Expectations(RATES_DIR + "EUR-Long-Final-Stub-uti.xml", DataDocument.class,true, 13),
		new Expectations(RATES_DIR + "EUR-OIS-uti.xml", DataDocument.class, true, 4),
		new Expectations(RATES_DIR + "EUR-Vanilla-party-roles-versioned.xml", DataDocument.class, true, 4),
		new Expectations(RATES_DIR + "EUR-Vanilla-uti.xml", DataDocument.class, true, 0),
		new Expectations(RATES_DIR + "EUR-vanilla-reconciliation-partyA-uti.xml", DataDocument.class, true, 0),
		new Expectations(RATES_DIR + "EUR-vanilla-reconciliation-partyB-uti.xml", DataDocument.class, true, 0),
		new Expectations(RATES_DIR + "EUR-variable-notional-uti.xml", DataDocument.class, true, 0),
		new Expectations(RATES_DIR + "GBP-OIS-uti.xml", DataDocument.class, true, 0),
		new Expectations(RATES_DIR + "GBP-VNS-uti.xml", DataDocument.class, true, 0),
		new Expectations(RATES_DIR + "GBP-Vanilla-person-roles-uti.xml", DataDocument.class, true, 2),
		new Expectations(RATES_DIR + "GBP-Vanilla-uti.xml", DataDocument.class, true, 0),
		new Expectations(RATES_DIR + "NDS-CNY-uti.xml", DataDocument.class, true, 1),
		new Expectations(RATES_DIR + "NDS-INR-uti.xml", DataDocument.class, true, 0),
		new Expectations(RATES_DIR + "NDS-KRW-uti.xml", DataDocument.class, true, 0),
		new Expectations(RATES_DIR + "USD-Long-Final-Stub-uti.xml", DataDocument.class, true, 2),
		new Expectations(RATES_DIR + "USD-OIS-uti.xml", DataDocument.class, true, 6),
		new Expectations(RATES_DIR + "USD-VNS-uti.xml", DataDocument.class, true, 0),
		new Expectations(RATES_DIR + "USD-Vanilla-uti.xml", DataDocument.class, true, 0),
		new Expectations(RATES_DIR + "ird-ex01-vanilla-swap-versioned.xml", DataDocument.class, true, 0),
		new Expectations(RATES_DIR + "ird-ex02-stub-amort-swap-versioned.xml", DataDocument.class, true, 11),
		new Expectations(RATES_DIR + "ird-ex03-compound-swap-versioned.xml", DataDocument.class, true, 2),
		new Expectations(RATES_DIR + "ird-ex04-arrears-stepup-fee-swap-usi-uti.xml", DataDocument.class, true, 0),
		new Expectations(RATES_DIR + "ird-ex05-long-stub-swap-uti.xml", DataDocument.class, true, 15),
		new Expectations(RATES_DIR + "ird-ex07-ois-swap-uti.xml", DataDocument.class, true, 0),
		new Expectations(RATES_DIR + "ird-ex09-euro-swaption-explicit-physical-exercise.xml", DataDocument.class, true, 0),
		new Expectations(RATES_DIR + "ird-ex09-euro-swaption-explicit-versioned.xml", DataDocument.class, true, 0),
		new Expectations(RATES_DIR + "ird-ex10-euro-swaption-relative-usi.xml", DataDocument.class, true, 0),
		new Expectations(RATES_DIR + "ird-ex12-euro-swaption-straddle-cash.xml", DataDocument.class, true, 0),
		new Expectations(RATES_DIR + "ird-ex12-euro-swaption-straddle-cash-other-party.xml", DataDocument.class, true, 0),
		new Expectations(RATES_DIR + "ird-ex32-zero-coupon-swap-account-versioned.xml", DataDocument.class, true, 13),
		new Expectations(RATES_DIR + "ird-ex33-BRL-CDI-swap-versioned.xml", DataDocument.class, true, 2),
		new Expectations(RATES_DIR + "swap-with-other-party-payment.xml", DataDocument.class, true, 6),
		new Expectations("swaption-cash-exercise.xml", DataDocument.class, true, 0),
		new Expectations("swaption-physical-exercise.xml", DataDocument.class, true, 0),
		// LCH
		new Expectations(LCH_DIR + "ClearLink-requestClearingSample.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingFRATRADE001.xml", RequestClearing.class, false, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingFRATRADE002.xml", RequestClearing.class, false, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingINFTRADE00001.xml", RequestClearing.class, false, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingINFTRADE00002.xml", RequestClearing.class, false, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingINFTRADE00003.xml", RequestClearing.class, false, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE001.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE005.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE011.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE016.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE020.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE022.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE023.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE027.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE030.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE031.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE032.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE055.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE056.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE057.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE058.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE059.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE060.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE061.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE062.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE063.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE064.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE065.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE066.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE067.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE068.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE069.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE070.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE071.xml", RequestClearing.class, true, 1),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE072.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE073.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE074.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE075.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE076.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE077.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE078.xml", RequestClearing.class, false, 1),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE079.xml", RequestClearing.class, false, 1),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE080.xml", RequestClearing.class, false, 1),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE082.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE083.xml", RequestClearing.class, true, 0),
		new Expectations(LCH_DIR + "ClearLink-requestClearingTESTTRADE084.xml", RequestClearing.class, true, 11)
	);

	private static IngestionService ingestionService;

	private Fpml510ProjectionMapper fpmlMapper;

	static Injector injector;
	@Inject
	Provider<Fpml510ProjectionMapper> fpml510ProjectionMapperProvider;

	@BeforeAll
	static void globalSetUp() {
		CdmRuntimeModule runtimeModule = new CdmRuntimeModule();
		injector = Guice.createInjector(runtimeModule);
		initialiseIngestionFactory(runtimeModule);
		ingestionService = IngestionFactory
				.getInstance(INSTANCE_NAME)
				.getService("FpML_5_Confirmation_To_TradeState");
	}

	@BeforeEach
	void setUp() {
		injector.injectMembers(this);
		fpmlMapper = fpml510ProjectionMapperProvider.get();
	}

	@ParameterizedTest(name = "{1}")
	@MethodSource("fpmlDocumentFiles")
	<T extends Document> void shouldIngestContractAndBuildFpmlDocument(URL fpmlUrl, String name, Class<T> fpmlDocument, Expectations expectations) throws JAXBException, IOException, URISyntaxException {
		LOGGER.debug("---------------------- Running Test for file: " + name + " ----------------------");
		IngestionReport<TradeState> ingestionReport = ingestionService.ingestValidateAndPostProcess(TradeState.class, UrlUtils.openURL(fpmlUrl));
		TradeState tradeState = ingestionReport.getRosettaModelInstance();
		assertNotNull(tradeState);
		LOGGER.debug(RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(tradeState));

		T document;
		try {
			document = fpmlMapper.getDocument(ingestionReport.getRosettaModelInstance(), fpmlDocument);
			assertNotNull(document);
		} catch (UnsupportedOperationException e) {
			if (!expectations.pass) {
				LOGGER.warn("Expected error " + e.getMessage());
				return;
			}
			throw e;
		}

		String projectedFpml = Fpml510Marshaller.marshal(document);
		assertNotNull(projectedFpml);
		LOGGER.debug(projectedFpml);

		Path originalXmlPath = Path.of(fpmlUrl.toURI());
		String originalXml = Files.readString(originalXmlPath);

		Set<RosettaPath> expectedUnmappedXmlPaths = ingestionReport.getMappingReport().getFailures().stream()
			.map(MappingResult::getExternalPath)
			.collect(Collectors.toSet());
		Set<RosettaPath> ignoredXmlPaths = ingestionReport.getMappingReport().getExcludedPaths().stream()
			.map(RosettaPath::valueOf)
			.collect(Collectors.toSet());
		ProjectionMappingReportBuilder xmlMappingBuilder =
			new ProjectionMappingReportBuilder(expectedUnmappedXmlPaths, ignoredXmlPaths, originalXml, projectedFpml);
		ProjectionMappingReport mappingReport = xmlMappingBuilder.buildMappings();

		List<MappingResult> mappingFails = mappingReport.getMappingResults().stream()
			.filter(x -> !x.isSuccess())
			.collect(Collectors.toList());
		mappingFails.stream()
			.map(x -> "Failed to map: " + x.getExternalPath().toString() + " ---> " + x.getValue())
			.forEach(LOGGER::info);

		// useful to write out the projection xml so we can diff
		// Files.write(originalXmlPath.getParent().resolve(originalXmlPath.getFileName().toString().replace(".xml", "-projected.xml")), projectedFpml.getBytes());		assertThat(mappingFails, Matchers.hasSize(expectations.mappingFailures));

	}

	@SuppressWarnings("unused") //used by the junit parameterized test
	private static List<Arguments> fpmlDocumentFiles() {
		return getArguments(FPML_DOCUMENT_FILES);
	}

	private static List<Arguments> getArguments(List<Expectations> fpmlDataDocumentFiles) {
		return fpmlDataDocumentFiles.stream()
			.map(expectations -> Arguments.of(
				Resources.getResource(expectations.fileName),
				Paths.get(expectations.fileName).getFileName().toString(),
				expectations.fpmlDocument,
				expectations))
			.collect(Collectors.toList());
	}

	private static void initialiseIngestionFactory(Module moduleRuntimeModule) {
		IngestionFactory.init(INSTANCE_NAME,
				Fpml510ProjectionMapperTest.class.getClassLoader(),
				setupRuntimeModules(moduleRuntimeModule),
				IngestionTestUtil.getPostProcessors(injector).toArray(new PostProcessStep[0]));
	}

	private static Module setupRuntimeModules(Module modelRuntimeModule) {
		TerminalsStandaloneSetup.doSetup();
		Module combinedModules = Modules.combine(new Module[]{modelRuntimeModule, new RosettaRuntimeModule()});
		injector = Guice.createInjector(new Module[]{combinedModules});
		(new RosettaStandaloneSetup()).register(injector);
		return combinedModules;
	}

	static class Expectations {
		private final String fileName;
		private final Class<? extends Document> fpmlDocument;
		private final boolean pass;
		private final int mappingFailures;

		public Expectations(String fileName, Class<? extends Document> fpmlDocument, boolean pass, int mappingFailures) {
			this.fileName = fileName;
			this.fpmlDocument = fpmlDocument;
			this.pass = pass;
			this.mappingFailures = mappingFailures;
		}
	}
}
