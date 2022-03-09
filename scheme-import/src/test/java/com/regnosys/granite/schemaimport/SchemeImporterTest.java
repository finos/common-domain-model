package com.regnosys.granite.schemaimport;

import com.regnosys.rosetta.common.util.ClassPathUtils;
import com.regnosys.rosetta.rosetta.RosettaEnumeration;
import com.regnosys.rosetta.rosetta.RosettaExternalEnum;
import com.regnosys.rosetta.rosetta.RosettaExternalSynonymSource;
import com.regnosys.rosetta.rosetta.RosettaNamed;
import com.regnosys.rosetta.transgest.ModelLoaderImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SchemeImporterTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(SchemeImporterTest.class);
	private static final boolean WRITE_TEST_OUTPUT = false;
	public static final String FPML_SET_OF_SCHEMES_2_2_XML = "coding-schemes/fpml/set-of-schemes-2-2.xml";
	public static final String BODY = "ISDA";
	public static final String CODING_SCHEME = "FpML_Coding_Scheme";

	private SchemeImporter schemeImporter;
	private URL[] rosettaPaths;
	private ModelLoaderImpl modelLoader;


	@BeforeEach
	void setUp() {
		rosettaPaths = getRosettaPaths();
		modelLoader = new ModelLoaderImpl(rosettaPaths);
		schemeImporter = new SchemeImporter(
			new FpMLSchemeEnumReader(
				schemeUrl(),
				"coding-schemes/fpml/"
			),
			new AnnotatedRosettaEnumReader(modelLoader),
			new RosettaResourceWriter()
		);
	}

	@Test
	void checkEnumsAreValid() throws IOException {
		Map<String, String> generatedFromScheme = schemeImporter.generateRosettaEnums(BODY, CODING_SCHEME);

		if (WRITE_TEST_OUTPUT) {
			writeTestOutput(generatedFromScheme);
		}

		for (String fileName : generatedFromScheme.keySet()) {
			String contents = getContents(fileName);
			assertEquals(contents, generatedFromScheme.get(fileName));
		}
	}

	@Test
	void writeIsFalse() {
		assertThat(WRITE_TEST_OUTPUT, equalTo(false));
	}

	private void writeTestOutput(Map<String, String> rosettaExpected) throws IOException {
		// Add environment variablex TEST_WRITE_BASE_PATH to override the base write path, e.g.
		// TEST_WRITE_BASE_PATH=/Users/hugohills/code/src/github.com/REGnosys/rosetta-cdm/src/main/rosetta/
		Path basePath = Optional.ofNullable(System.getenv("TEST_WRITE_BASE_PATH"))
			.map(Paths::get)
			.filter(Files::exists)
			.orElseThrow();

		for (String fileName : rosettaExpected.keySet()) {
			Path outputPath = basePath.resolve(fileName);
			Files.write(outputPath, rosettaExpected.get(fileName).getBytes(StandardCharsets.UTF_8));
			LOGGER.info("Wrote test output to {}", outputPath.toAbsolutePath());
		}

	}

	//TODO: finish this to generate updated synonyms
	@Disabled
	@Test
	void updateSynonyms() {

		Map<String, List<RosettaEnumeration>> enums = modelLoader.rosettaElements(RosettaEnumeration.class)
			.stream().collect(Collectors.groupingBy(RosettaNamed::getName));

		List<RosettaExternalSynonymSource> rosettaExternalSynonymSources = modelLoader.rosettaElements(RosettaExternalSynonymSource.class);
		RosettaExternalSynonymSource fpML_5_10 = rosettaExternalSynonymSources.stream()
			.filter(x -> x.getName().equals("FpML_5_10"))
			.findAny().orElseThrow();

		List<RosettaExternalEnum> externalEnums = fpML_5_10.getExternalEnums();

		for (RosettaExternalEnum externalEnum : externalEnums) {
			String name = externalEnum.getTypeRef().getName();

			if (enums.containsKey(name)) {
				List<RosettaEnumeration> rosettaEnumerations = enums.get(name);
				externalEnum.getRegularValues().clear();
				for (RosettaEnumeration rosettaEnumeration : rosettaEnumerations) {

					// create RosettaExternalEnumValue

					// set the value from the rosettaEnumeration.displayname
				}
			}
		}
	}



	private String getContents(String fileName) throws IOException {
		URL rosettaPath = Arrays.stream(rosettaPaths)
			.filter(x -> getFileName(x.getFile()).equals(fileName))
			.findFirst().orElseThrow();
		String contents = new String(rosettaPath.openStream().readAllBytes());
		return RosettaResourceWriter.rewriteProjectVersion(contents);
	}

	@NotNull
	private URL schemeUrl() {
		return ClassPathUtils.loadFromClasspath(FPML_SET_OF_SCHEMES_2_2_XML, getClass().getClassLoader())
			.map(ClassPathUtils::toUrl)
			.findFirst().orElseThrow();
	}

	private String getFileName(String path) {
		return path.substring(path.lastIndexOf('/')+1);
	}


	private static URL[] getRosettaPaths() {
		return ClassPathUtils.findPathsFromClassPath(
				List.of("cdm/rosetta"),
				".*\\.rosetta",
				Optional.empty(),
				SchemeImporter.class.getClassLoader()
			).stream()
			.map(ClassPathUtils::toUrl)
			.toArray(URL[]::new);
	}
}