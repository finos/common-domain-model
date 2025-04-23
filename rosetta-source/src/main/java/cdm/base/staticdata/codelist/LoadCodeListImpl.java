package cdm.base.staticdata.codelist;

import cdm.base.staticdata.codelist.functions.LoadCodeList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoadCodeListImpl extends LoadCodeList {

    /** Directory path where JSON CodeLists are stored inside the resources. */
    private static final String CODELIST_COLLECTION_PATH = "org/isda/codelist/json";
    private static final ObjectMapper mapper = RosettaObjectMapper.getNewRosettaObjectMapper();

    @Override
    protected CodeList.CodeListBuilder doEvaluate(String domain) {
        try {
            return loadCodeList(domain).toBuilder();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a CodeList JSON file corresponding to the given domain.
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
        Pattern pattern = Pattern.compile("org/isda/codelist/json/" + Pattern.quote(domain.toLowerCase()) + "-\\d+-\\d+\\.json$");
        String matchedFile = jsonFiles.stream()
                .map(name -> name.replace("\\", "/")) // Normalize
                .filter(name -> pattern.matcher(name.toLowerCase()).matches())
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
}
