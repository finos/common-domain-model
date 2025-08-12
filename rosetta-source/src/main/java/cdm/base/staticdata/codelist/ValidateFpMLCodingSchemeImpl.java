package cdm.base.staticdata.codelist;

import cdm.base.staticdata.codelist.functions.LoadCodeList;
import cdm.base.staticdata.codelist.functions.ValidateFpMLCodingSchemeDomain;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of FpML coding scheme validation.
 *
 * This class is responsible for:
 * - Loading CodeList JSON files from resources using LoadCodeList.
 * - Caching loaded CodeLists for efficient validation.
 * - Evaluating whether a given code exists in the relevant CodeList.
 */
public class ValidateFpMLCodingSchemeImpl extends ValidateFpMLCodingSchemeDomain {

    private static final Logger logger = LoggerFactory.getLogger(ValidateFpMLCodingSchemeImpl.class);

    /** Directory path where JSON CodeLists are stored inside the resources. */
    private static final String CODELIST_COLLECTION_PATH = "org/isda/codelist/json";

    private static final ObjectMapper mapper = RosettaObjectMapper.getNewRosettaObjectMapper();

    /** Cache to store loaded CodeLists to avoid redundant file reads. */
    private static final Map<String, CodeList> cache = new HashMap<>();

    @Inject
    LoadCodeList loadCodeListFunc;

    /**
     * Loads a CodeList JSON file corresponding to the given domain.
     * Can be removed by loaCodeListFunc + handle cache management
     *
     * @param domain The domain name used to identify the relevant CodeList file.
     * @return The deserialized CodeList object.
     * @throws IOException If an error occurs while reading the file.
     * @throws URISyntaxException If the resource path cannot be converted to a URI.
     */
    private static CodeList loadCodeList(String domain) throws IOException, URISyntaxException {
        ClassLoader classLoader = ValidateFpMLCodingSchemeImpl.class.getClassLoader();

        // Retrieve the list of available CodeList JSON files
        Set<String> jsonFiles = listCodeListResources();

        // Find the first JSON file whose name contains the given domain
        String matchedFile = jsonFiles.stream()
                .filter(name -> name.toLowerCase().contains(domain.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new FileNotFoundException("No matching CodeList found for: " + domain));

        // Load the JSON file as an InputStream from the classpath
        try (InputStream inputStream = classLoader.getResourceAsStream(matchedFile)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Failed to load resource: " + matchedFile);
            }
            return mapper.readValue(inputStream, CodeList.class);
        }
    }

    /**
     * Retrieves a set of JSON filenames from the CodeList directory.
     *
     * This method detects whether the resources are being accessed from a JAR file or from a standard
     * file system (development mode). It lists the JSON files accordingly.
     *
     * @return A set of CodeList JSON file paths found in the directory.
     * @throws IOException If an error occurs while reading the resources.
     * @throws URISyntaxException If the resource path cannot be converted to a URI.
     */
    private static Set<String> listCodeListResources() throws IOException, URISyntaxException {
        URL resourceUrl = ValidateFpMLCodingSchemeImpl.class.getClassLoader().getResource(CODELIST_COLLECTION_PATH);
        if (resourceUrl == null) {
            throw new FileNotFoundException("CodeList directory not found: " + CODELIST_COLLECTION_PATH);
        }

        Path resourcePath = Paths.get(resourceUrl.toURI());

        if (Files.isDirectory(resourcePath)) {
            // Development mode: List JSON files from the local resources directory
            try (Stream<Path> stream = Files.list(resourcePath)) {
                return stream.map(path -> CODELIST_COLLECTION_PATH + "/" + path.getFileName().toString())
                        .filter(name -> name.endsWith(".json"))
                        .collect(Collectors.toSet());
            }
        } else {
            // JAR mode: List JSON files inside the JAR
            String jarPath = resourceUrl.getPath().split("!")[0].replace("file:", "");
            try (JarFile jarFile = new JarFile(jarPath)) {
                return jarFile.stream()
                        .map(JarEntry::getName)
                        .filter(name -> name.startsWith(CODELIST_COLLECTION_PATH) && name.endsWith(".json"))
                        .collect(Collectors.toSet());
            }
        }
    }

    /**
     * Validates whether the provided code exists in the specified domain's CodeList.
     *
     * @param code   The code to validate.
     * @param domain The domain that determines which CodeList should be used.
     * @return True if the code exists in the corresponding CodeList; otherwise, false.
     */
    @Override
    protected Boolean doEvaluate(String code, String domain) {
        logger.debug("Triggered doEvaluate with code={} and domain={}", code, domain);
        CodeList codeList;

        // Check if the domain is already cached
        if (cache.containsKey(domain)) {
            codeList = cache.get(domain);
            logger.debug("{} already exists in cache with version {} and {} codes",
                    domain, codeList.getIdentification().getVersion(), codeList.getCodes().size());
        } else {
            // Load the CodeList from resources and cache it
            try {
                codeList = loadCodeList(domain);
                if (codeList.getCodes().isEmpty()) {
                    logger.error("Error loading CodeList for domain '{}'", domain);
                } else {
                    cache.put(domain, codeList);
                }
            }
            catch (IOException | URISyntaxException e) {
                logger.error("Error loading CodeList for domain '{}': {}", domain, e.getMessage());
                return Boolean.TRUE;
            }
        }

        // Check if the provided code exists in the CodeList
        return codeList.getCodes().stream()
                .map(CodeValue::getValue)
                .anyMatch(code::equalsIgnoreCase);
    }
}