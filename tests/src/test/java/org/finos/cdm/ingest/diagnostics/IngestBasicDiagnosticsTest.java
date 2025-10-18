package org.finos.cdm.ingest.diagnostics;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import fpml.confirmation.custom.ProductChoice;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.finos.cdm.ingest.diagnostics.IngestUtils.*;

public class IngestBasicDiagnosticsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngestBasicDiagnosticsTest.class);

    private static final Map<String, Map<String, TestPackAndProductDiagnostics>> TEST_PACK_AND_PRODUCT_SAMPLE_TOTALS = new HashMap<>();
    private static final Map<String, Diagnostics> PRODUCT_SAMPLE_TOTALS = new HashMap<>();
    private static final Map<String, Diagnostics> TEST_PACK_SAMPLE_TOTALS = new HashMap<>();
    private static final Map<String, Multimap<String, SampleDiagnostics>> TEST_PACK_SAMPLES = new HashMap<>();

    private static final Set<String> FPML_PRODUCT_ELEMENTS = getFpmlProductElements();

    @MethodSource("inputs")
    @ParameterizedTest(name = "{0}")
    void checkTargetOutputDiff(String testName, String testPack, Path inputXmlPath, Path ingestOutputPath, Path synonymIngestOutputPath) {
        if (!Files.exists(ingestOutputPath)) {
            return;
        }
        String inputXml = readFile(inputXmlPath);
        FPML_PRODUCT_ELEMENTS.stream()
                .filter(product -> isProduct(product, inputXml))
                .findFirst()
                .ifPresent(fpmlProduct -> {
                    int actualOutputContentLength = readFile(ingestOutputPath).length();
                    int targetOutputFileContentLength = readFile(synonymIngestOutputPath).length();
                    // add to test pack and product diagnostics
                    TEST_PACK_AND_PRODUCT_SAMPLE_TOTALS.compute(testPack, (k, fpmlProductDiagnostics) ->
                            updateTestPackDiagnostics(testPack,
                                    fpmlProduct,
                                    actualOutputContentLength,
                                    targetOutputFileContentLength,
                                    Objects.requireNonNullElseGet(fpmlProductDiagnostics, HashMap::new)));
                    // add to test pack diagnostics
                    Diagnostics testPackTotal = TEST_PACK_SAMPLE_TOTALS.getOrDefault(testPack, Diagnostics.create(testPack));
                    TEST_PACK_SAMPLE_TOTALS.put(testPack, testPackTotal.accumulate(actualOutputContentLength, targetOutputFileContentLength));
                    // add to product diagnostics
                    Diagnostics productTotal = PRODUCT_SAMPLE_TOTALS.getOrDefault(fpmlProduct, Diagnostics.create(fpmlProduct));
                    PRODUCT_SAMPLE_TOTALS.put(fpmlProduct, productTotal.accumulate(actualOutputContentLength, targetOutputFileContentLength));
                    // add to sample diagnostics
                    TEST_PACK_SAMPLES.compute(testPack, (k, sampleDiagnostics) ->
                            updateSampleDiagnostics(testPack,
                                    fpmlProduct,
                                    inputXmlPath.getFileName().toString(),
                                    actualOutputContentLength,
                                    targetOutputFileContentLength,
                                    Objects.requireNonNullElseGet(sampleDiagnostics, ArrayListMultimap::create)));

                });
    }

    private static Stream<Arguments> inputs() throws IOException {
        List<Arguments> arguments;
        try (Stream<Path> synonymIngestOutputFileStream = Files.walk(SYNONYM_INGEST_OUTPUT_BASE_PATH)) {
            arguments = synonymIngestOutputFileStream
                    .filter(IngestUtils::isFpmlTestPack)
                    .filter(IngestUtils::isJsonExt)
                    .map(synonymIngestOutputPath ->
                    {
                        Path fileName = synonymIngestOutputPath.getFileName();
                        Path relativePath = SYNONYM_INGEST_OUTPUT_BASE_PATH.relativize(synonymIngestOutputPath.getParent());
                        String testPackName = toTestPackName(relativePath.toString());
                        Path ingestOutputPath = getIngestOutputPath(testPackName, fileName);
                        if (!Files.exists(ingestOutputPath)) {
                            LOGGER.warn("No ingest output file found for {}", ingestOutputPath);
                            return null;
                        }
                        return Arguments.of(getTestName(testPackName, fileName),
                                testPackName,
                                getXmlInputPath(synonymIngestOutputPath),
                                ingestOutputPath,
                                synonymIngestOutputPath);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return arguments.stream();
    }

    @NotNull
    private static Path getXmlInputPath(Path synonymIngestOutputPath) {
        return Path.of(synonymIngestOutputPath.toString()
                .replace("result-json-files", "cdm-sample-files")
                .replace(".json", ".xml"));
    }


    @AfterAll
    static void printTotals() {
        printProductDiagnostics();
        printTestPackDiagnostics();
        printTestPackAndProductDiagnostics();
    }

    private static void printProductDiagnostics() {
        List<Diagnostics> productDiagnostics = PRODUCT_SAMPLE_TOTALS.values().stream()
                .sorted(Comparator.comparing((Diagnostics x) -> getCompleteness(x.actual, x.target))
                        .thenComparing(Diagnostics::samples).reversed())
                .collect(Collectors.toList());

        // Create StringBuilder for the markdown content
        StringBuilder markdownContent = new StringBuilder();
        markdownContent.append("# Product Diagnostics\n\n");
        markdownContent.append("| FpML Product | Samples | Completeness |\n");
        markdownContent.append("|:-----------------------------------------------|:-------:|:-------:|\n");

        // Add each diagnostic entry
        productDiagnostics.forEach(d ->
                markdownContent.append(String.format("| %s | %s | %s |\n",
                        d.group, d.samples, getFormattedCompleteness(d.actual, d.target))));

        writeDiagnosticsFile("product_diagnostics.md", markdownContent.toString());
    }

    private static void printTestPackDiagnostics() {
        List<Diagnostics> testPackDiagnostics = TEST_PACK_SAMPLE_TOTALS.values().stream()
                .sorted(Comparator.comparing((Diagnostics x) -> getCompleteness(x.actual, x.target))
                        .thenComparing(Diagnostics::samples).reversed())
                .collect(Collectors.toList());

        // Create StringBuilder for the markdown content
        StringBuilder markdownContent = new StringBuilder();
        markdownContent.append("# Test Pack Diagnostics\n\n");
        markdownContent.append("| Test Pack | Samples | Completeness |\n");
        markdownContent.append("|:-----------------------------------------------------|:-------:|:-------:|\n");

        testPackDiagnostics.forEach(d ->
                markdownContent.append(String.format("| %s | %s | %s |\n",
                        d.group, d.samples, getFormattedCompleteness(d.actual, d.target))));
        
        writeDiagnosticsFile("test_pack_diagnostics.md", markdownContent.toString());
    }

    private static void printTestPackAndProductDiagnostics() {
        List<TestPackAndProductDiagnostics> testPackAndProductDiagnostics = TEST_PACK_AND_PRODUCT_SAMPLE_TOTALS.values().stream()
                .map(Map::values).flatMap(Collection::stream)
                .sorted(Comparator.comparing(TestPackAndProductDiagnostics::testPack)
                        .thenComparing(TestPackAndProductDiagnostics::product))
                .collect(Collectors.toList());

        // Create StringBuilder for the markdown content
        StringBuilder markdownContent = new StringBuilder();
        markdownContent.append("# Test Pack / Product Diagnostics\n\n");
        markdownContent.append("| Test Pack | FpML Product | Samples | Completeness |\n");
        markdownContent.append("|:-----------------------------------------------------|:-----------------------------------------------|:-------:|:-------:|\n");
        
        testPackAndProductDiagnostics.forEach(d ->
                markdownContent.append(String.format("| %s | %s | %s | %s |\n",
                        d.testPack, d.product, d.samples, getFormattedCompleteness(d.actual, d.target))));

        writeDiagnosticsFile("test_pack_product_diagnostics.md", markdownContent.toString());
    }

    private static void writeDiagnosticsFile(String fileName, String contents) {
        Path outputDir = PROJECT_ROOT.resolve("tests/src/test/resources/diagnostics");
        Path outputFile = outputDir.resolve(fileName);

        try {
            // Create directory if it doesn't exist
            if (!Files.exists(outputDir)) {
                Files.createDirectories(outputDir);
            }
            // Write to file
            Files.writeString(outputFile, contents);
            LOGGER.info("Product diagnostics written to {}", outputFile);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write product diagnostics to markdown file", e);
        }
    }

    private static double getCompleteness(int actual, int target) {
        return (double) actual / target * 100;
    }

    private static String getFormattedCompleteness(int actual, int target) {
        DecimalFormat df = new DecimalFormat("0");
        return df.format(getCompleteness(actual, target)) + "%";
    }

    private static Set<String> getFpmlProductElements() {
        Set<String> returnTypes = new HashSet<>();
        Method[] methods = ProductChoice.class.getDeclaredMethods();
        for (Method method : methods) {
            // Only consider public methods with 
            if (Modifier.isPublic(method.getModifiers())
                    && method.getReturnType().getPackageName().equals("fpml.confirmation")) {
                String product = method.getReturnType().getSimpleName();
                returnTypes.add(StringExtensions.toFirstLower(product));
            }
        }
        return returnTypes;
    }

    private static boolean isProduct(String product, String inputXml) {
        // nasty hack
        if (product.equals("swap") && inputXml.contains("<swaption")) {
            return false;
        }
        String regex = "<" + "\\b" + Pattern.quote(product) + "\\b";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(inputXml).find();
    }


    private Map<String, TestPackAndProductDiagnostics> updateTestPackDiagnostics(String testPack,
                                                                                 String fpmlProduct,
                                                                                 int actualOutputContentLength,
                                                                                 int targetOutputFileContentLength,
                                                                                 Map<String, TestPackAndProductDiagnostics> fpmlProductDiagnostics) {
        TestPackAndProductDiagnostics currentTotal = fpmlProductDiagnostics.getOrDefault(fpmlProduct, TestPackAndProductDiagnostics.create(testPack, fpmlProduct));
        fpmlProductDiagnostics.put(fpmlProduct, currentTotal.accumulate(actualOutputContentLength, targetOutputFileContentLength));
        return fpmlProductDiagnostics;
    }

    private Multimap<String, SampleDiagnostics> updateSampleDiagnostics(String testPack,
                                                                        String fpmlProduct,
                                                                        String sample,
                                                                        int actualOutputContentLength,
                                                                        int targetOutputFileContentLength,
                                                                        Multimap<String, SampleDiagnostics> sampleDiagnostics) {
        sampleDiagnostics.put(fpmlProduct, new SampleDiagnostics(testPack, fpmlProduct, sample, actualOutputContentLength, targetOutputFileContentLength));
        return sampleDiagnostics;
    }

    private String readFile(Path file) {
        try {
            return Files.readString(file);
        } catch (IOException e) {
            LOGGER.error("Failed to read file {}", file.getFileName().toString(), e);
            return "";
        }
    }

    private static final class SampleDiagnostics {
        private final String testPack;
        private final String product;
        private final String sample;
        private final int actual;
        private final int target;

        private SampleDiagnostics(String testPack, String product, String sample, int actual, int target) {
            this.testPack = testPack;
            this.product = product;
            this.sample = sample;
            this.actual = actual;
            this.target = target;
        }

        public String testPack() {
            return testPack;
        }

        public String product() {
            return product;
        }

        public String sample() {
            return sample;
        }

        public int actual() {
            return actual;
        }

        public int target() {
            return target;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (SampleDiagnostics) obj;
            return Objects.equals(this.testPack, that.testPack) &&
                    Objects.equals(this.product, that.product) &&
                    Objects.equals(this.sample, that.sample) &&
                    this.actual == that.actual &&
                    this.target == that.target;
        }

        @Override
        public int hashCode() {
            return Objects.hash(testPack, product, sample, actual, target);
        }

        @Override
        public String toString() {
            return "SampleDiagnostics[" +
                    "testPack=" + testPack + ", " +
                    "product=" + product + ", " +
                    "sample=" + sample + ", " +
                    "actual=" + actual + ", " +
                    "target=" + target + ']';
        }
    }

    private static final class TestPackAndProductDiagnostics {
        private final String testPack;
        private final String product;
        private final int samples;
        private final int actual;
        private final int target;

        private TestPackAndProductDiagnostics(String testPack, String product, int samples, int actual, int target) {
            this.testPack = testPack;
            this.product = product;
            this.samples = samples;
            this.actual = actual;
            this.target = target;
        }

        public static TestPackAndProductDiagnostics create(String testPack, String product) {
            return new TestPackAndProductDiagnostics(testPack, product, 0, 0, 0);
        }

        public TestPackAndProductDiagnostics accumulate(int actual, int target) {
            return new TestPackAndProductDiagnostics(this.testPack, this.product, this.samples + 1, this.actual + actual, this.target + target);
        }

        public String testPack() {
            return testPack;
        }

        public String product() {
            return product;
        }

        public int samples() {
            return samples;
        }

        public int actual() {
            return actual;
        }

        public int target() {
            return target;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (TestPackAndProductDiagnostics) obj;
            return Objects.equals(this.testPack, that.testPack) &&
                    Objects.equals(this.product, that.product) &&
                    this.samples == that.samples &&
                    this.actual == that.actual &&
                    this.target == that.target;
        }

        @Override
        public int hashCode() {
            return Objects.hash(testPack, product, samples, actual, target);
        }

        @Override
        public String toString() {
            return "TestPackAndProductDiagnostics[" +
                    "testPack=" + testPack + ", " +
                    "product=" + product + ", " +
                    "samples=" + samples + ", " +
                    "actual=" + actual + ", " +
                    "target=" + target + ']';
        }
    }

    private static final class Diagnostics {
        private final String group;
        private final int samples;
        private final int actual;
        private final int target;

        private Diagnostics(String group, int samples, int actual, int target) {
            this.group = group;
            this.samples = samples;
            this.actual = actual;
            this.target = target;
        }

        public static Diagnostics create(String product) {
            return new Diagnostics(product, 0, 0, 0);
        }

        public Diagnostics accumulate(int actual, int target) {
            return new Diagnostics(this.group, this.samples + 1, this.actual + actual, this.target + target);
        }

        public String group() {
            return group;
        }

        public int samples() {
            return samples;
        }

        public int actual() {
            return actual;
        }

        public int target() {
            return target;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Diagnostics) obj;
            return Objects.equals(this.group, that.group) &&
                    this.samples == that.samples &&
                    this.actual == that.actual &&
                    this.target == that.target;
        }

        @Override
        public int hashCode() {
            return Objects.hash(group, samples, actual, target);
        }

        @Override
        public String toString() {
            return "Diagnostics[" +
                    "group=" + group + ", " +
                    "samples=" + samples + ", " +
                    "actual=" + actual + ", " +
                    "target=" + target + ']';
        }
    }
}
