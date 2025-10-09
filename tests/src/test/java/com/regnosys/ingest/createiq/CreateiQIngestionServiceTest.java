package com.regnosys.ingest.createiq;

import cdm.legaldocumentation.common.LegalAgreement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.regnosys.ingest.test.framework.ingestor.IngestionReport;
import com.regnosys.ingest.test.framework.ingestor.IngestionTest;
import com.regnosys.ingest.test.framework.ingestor.IngestionTestUtil;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionFactory;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionService;
import com.regnosys.ingest.test.framework.ingestor.testing.Expectation;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.testing.MappingCoverage;
import com.regnosys.rosetta.common.util.ClassPathUtils;
import com.regnosys.rosetta.common.util.MutablePair;
import com.regnosys.rosetta.common.util.Pair;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;


public class CreateiQIngestionServiceTest extends IngestionTest<LegalAgreement> {

	private static final String SAMPLE_DIR = "cdm-sample-files/createiq/";

	private static ImmutableList<String> EXPECTATION_FILES = ImmutableList.<String>builder()
		.add(SAMPLE_DIR + "test-pack/production/expectations.json")
		.add(SAMPLE_DIR + "test-pack/sandbox/expectations.json")
		.add(SAMPLE_DIR + "test-pack/development/expectations.json")
		.add(SAMPLE_DIR + "other/sandbox/expectations.json")
		.build();


	static IngestionService ingestionService;

	@BeforeAll
	static void setup() {
		CdmRuntimeModule runtimeModule = new CdmRuntimeModule();
		initialiseIngestionFactory(runtimeModule, IngestionTestUtil.getPostProcessors(runtimeModule));
		ingestionService = IngestionFactory.getInstance().getService("createiQAll");
	}

	@Override
	protected Class<LegalAgreement> getClazz() {
		return LegalAgreement.class;
	}

	@Override
	protected IngestionService ingestionService() {
		return ingestionService;
	}

	@Override
	protected void assertExpectations(Expectation expectation, IngestionReport<LegalAgreement> ingested) throws JsonProcessingException {
		super.assertExpectations(expectation,ingested);
	}


	@Test
	void mappingCoverageIsConsistent() {
		for (String file : EXPECTATION_FILES) {
			List<Expectation> expectations = readFile(Resources.getResource(file), RosettaObjectMapper.getNewRosettaObjectMapper(), new TypeReference<List<Expectation>>() {
			}).collect(Collectors.toList());
			List<MappingCoverage> coverageFromExpectations = toMappingCoverages(ingestionService.getEnvironmentName(), expectations);
			URL resource = this.getClass().getResource(file.replace("expectations.json", "coverage.json"));
			if (resource != null) {
				List<MappingCoverage> actualCoverage = readCoverageFile(Resources.getResource(file.replace("expectations.json", "coverage.json")), RosettaObjectMapper.getNewRosettaObjectMapper());
				for (MappingCoverage mappingCoverage : coverageFromExpectations) {
					assertThat(actualCoverage, hasItem(mappingCoverage));
				}
			}
		}
	}

	private static List<MappingCoverage> toMappingCoverages(String environmentName, Collection<Expectation> expectations) {
		return expectations.stream().collect(groupingBy(CreateiQIngestionServiceTest::schema, toMappingStatistic()))
			.entrySet().stream()
			.map(entry -> new MappingCoverage(environmentName, entry.getKey().getSchema(), entry.getValue()))
			.collect(Collectors.toList());
	}

	/**
	 * Builds an CreateiQSchema object from the file path of the expectations file
	 */
	private static CreateiQSchema schema(Expectation e) {
		List<Path> parts = Lists.newArrayList(ClassPathUtils.loadSingleFromClasspath(e.getFileName()).iterator());
		List<Path> pathData = parts.subList(parts.size() - 4, parts.size() - 1);
		return new CreateiQSchema(pathData.get(0).toString(), pathData.get(1).toString(), pathData.get(2).toString());
	}

	/**
	 * @return A collector to add all the external paths and mapping exceptions across expectation files and produce a mapping coverage statistic.
	 */
	private static Collector<Expectation, MutablePair<Integer, Integer>, Double> toMappingStatistic() {
		return Collector.of(
			() -> MutablePair.of(0, 0),
			(pair, expectation) -> pair.set(pair.left + expectation.getExternalPaths(), pair.right + expectation.getOutstandingMappings()),
			(pair1, pair2) -> MutablePair.of(pair1.left + pair2.left, pair1.right + pair2.right),
			pair -> pair.left == 0 ? 0.0 : 1.0 - (pair.right.doubleValue() / pair.left.doubleValue()));
	}

	private static void writeFileToDisk(Pair<String, List<MappingCoverage>> expectationFileMappingCoveragesPair) {
		String url = EXPECTATION_FILES.stream().filter(_url -> expectationFileMappingCoveragesPair.left().endsWith(_url)).findFirst().orElseThrow();
		String updatedFilename = url.replace("expectations.json", "coverage.json");
		System.out.println("Writing to disk: " + updatedFilename);
		try {
			Path path = Paths.get(updatedFilename);
			Files.createDirectories(path.getParent());
			try (BufferedWriter writer = Files.newBufferedWriter(path)) {
				List<MappingCoverage> mappingCoverages = expectationFileMappingCoveragesPair.right();
				Collections.sort(mappingCoverages);
				writer.write(toJson(mappingCoverages));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unused")//used by the junit parameterized test
	private static Stream<Arguments> fpMLFiles() {
		return readExpectationsFromString(EXPECTATION_FILES);
	}
}
