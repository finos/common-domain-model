package com.regnosys.granite.ingestor.dtcc;

import cdm.event.workflow.WorkflowStep;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.regnosys.granite.ingestor.IngestionTest;
import com.regnosys.granite.ingestor.IngestionTestUtil;
import com.regnosys.granite.ingestor.service.IngestionFactory;
import com.regnosys.granite.ingestor.service.IngestionService;
import com.regnosys.granite.ingestor.synonym.MappingReport;
import com.regnosys.granite.ingestor.synonym.MappingResult;
import org.isda.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.Arguments;

import java.net.URL;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DtccIngestion11ServiceTest  extends IngestionTest<WorkflowStep>{

	private static final String DTCC_11_0_FILES_DIR = "cdm-sample-files/dtcc-11-0/";

	private static ImmutableList<URL> EXPECTATION_FILES = ImmutableList.<URL>builder()
			.add(Resources.getResource(DTCC_11_0_FILES_DIR + "expectations.json"))
			.build();

	private static IngestionService dtcc11IngestionService;

	@BeforeAll
	static void setup() {
		CdmRuntimeModule runtimeModule = new CdmRuntimeModule();
		initialiseIngestionFactory(runtimeModule, IngestionTestUtil.getPostProcessors(runtimeModule));
		dtcc11IngestionService = IngestionFactory.getInstance().getDtcc11();
	}
	
	@Override
	protected Class<WorkflowStep> getClazz() {
		return WorkflowStep.class;
	}

	@Override
	protected IngestionService ingestionService() {
		return dtcc11IngestionService;
	}

	@SuppressWarnings("unused")//used by the junit parameterized test
	private static Stream<Arguments> fpMLFiles() {
		return readExpectationsFrom(EXPECTATION_FILES);
	}

	@SuppressWarnings("unused")
	private void toPrintExcelExport(MappingReport mappingReport) {
		System.out.println("\nSuccesses -----------------------------------------------------------------\n");
		mappingReport.getSuccesses().stream().map(this::toExcelExportString).forEach(System.out::println);

		System.out.println("\nFailures -----------------------------------------------------------------\n");
		mappingReport.getFailures().stream().map(this::toExcelExportString).forEach(System.out::println);
	}

	private String toExcelExportString(MappingResult r) {
		return r.getExternalPath() + "|" + r.getInternalPaths().entrySet().stream().map(e->e.getKey().buildPath()).collect(Collectors.joining(",\n\t\t"));
	}
}
