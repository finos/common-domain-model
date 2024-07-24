package com.regnosys.ingest.dtcc;

import cdm.event.workflow.WorkflowStep;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.regnosys.ingest.test.framework.ingestor.IngestionReport;
import com.regnosys.ingest.test.framework.ingestor.IngestionTest;
import com.regnosys.ingest.test.framework.ingestor.IngestionTestUtil;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionFactory;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionService;
import com.regnosys.ingest.test.framework.ingestor.synonym.MappingReport;
import com.regnosys.ingest.test.framework.ingestor.synonym.MappingResult;
import com.regnosys.ingest.test.framework.ingestor.testing.Expectation;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.Arguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DtccIngestion9ServiceTest extends IngestionTest<WorkflowStep> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DtccIngestion9ServiceTest.class);

	private static final String DTCC_9_0_FILES_DIR = "cdm-sample-files/dtcc-9-0/";

	private static ImmutableList<URL> EXPECTATION_FILES = ImmutableList.<URL>builder()
			.add(Resources.getResource(DTCC_9_0_FILES_DIR + "expectations.json"))
			.build();

	private static IngestionService dtcc9IngestionService;

	@BeforeAll
	static void setup() {
		CdmRuntimeModule runtimeModule = new CdmRuntimeModule();
		initialiseIngestionFactory(runtimeModule, IngestionTestUtil.getPostProcessors(runtimeModule));
		dtcc9IngestionService = IngestionFactory.getInstance().getDtcc9();
	}

	@Override
	protected Class<WorkflowStep> getClazz() {
		return WorkflowStep.class;
	}

	@Override
	protected IngestionService ingestionService() {
		return dtcc9IngestionService;
	}

	@SuppressWarnings("unused")//used by the junit parameterized test
	private static Stream<Arguments> fpMLFiles() {
		return readExpectationsFrom(EXPECTATION_FILES);
	}

	@SuppressWarnings("unused")
	private void toPrintExcelExport(MappingReport mappingReport) {
		LOGGER.info("\nSuccesses -----------------------------------------------------------------\n");
		mappingReport.getSuccesses().stream().map(this::toExcelExportString).forEach(LOGGER::info);

		LOGGER.info("\nFailures -----------------------------------------------------------------\n");
		mappingReport.getFailures().stream().map(this::toExcelExportString).forEach(LOGGER::info);
	}

	private String toExcelExportString(MappingResult r) {
		return r.getExternalPath() + "|" + r.getInternalPaths().entrySet().stream().map(e->e.getKey().buildPath()).collect(Collectors.joining(",\n\t\t"));
	}
}
