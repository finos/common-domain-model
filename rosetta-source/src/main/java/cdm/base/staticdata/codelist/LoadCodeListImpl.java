package cdm.base.staticdata.codelist;

import cdm.base.staticdata.codelist.functions.LoadCodeList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.util.ClassPathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class LoadCodeListImpl extends LoadCodeList {

    /** Directory path where JSON CodeLists are stored inside the resources. */
    private final String CODELIST_COLLECTION_PATH = "org/isda/codelist/json";

    private final Logger logger = LoggerFactory.getLogger(LoadCodeListImpl.class);
    private final ObjectMapper mapper = RosettaObjectMapper.getNewRosettaObjectMapper();

    /** Cache to store loaded CodeLists to avoid redundant file reads. */
    private static final Cache<String, CodeList> cache = CacheBuilder.newBuilder()
            .maximumSize(15) //Limits the number of entries; evicts least-recently-used (LRU) when full
            .build();

    @Override
    protected CodeList.CodeListBuilder doEvaluate(String domain) {
        try {
            return loadCodeList(domain).toBuilder();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a CodeList JSON file corresponding to the given domain.
     *
     * @param domain The domain name used to identify the relevant CodeList file.
     * @return The deserialized CodeList object.
     * @throws IOException If an error occurs while reading the file.
     */
    private CodeList loadCodeList(String domain) throws IOException {

        CodeList codeList = cache.getIfPresent(domain);
        if (codeList != null) return codeList;

        // Retrieve the list of available CodeList JSON files
        List<Path> codeListPaths = ClassPathUtils.findPathsFromClassPath(Collections.singletonList(CODELIST_COLLECTION_PATH), ".*\\.json", Optional.empty(), LoadCodeListImpl.class.getClassLoader());

        // Find the first JSON file whose name contains the given domain
        Pattern pattern = Pattern.compile("^(.*?)org/isda/codelist/json/" + Pattern.quote(domain.toLowerCase()) + "-\\d+-\\d+\\.json$");
        Path match = codeListPaths.stream()
                .filter(path -> pattern.matcher(path.toString().replace(File.separatorChar, '/')).matches())
                .findFirst()
                .orElseThrow(() -> new FileNotFoundException("No matching CodeList found for: " + domain));

        // Load the JSON resource as a CodeList and save cache
        codeList = mapper.readValue(match.toFile(), CodeList.class);
        logger.debug("Saving cache for {} with version {} and {} codes",
                domain, codeList.getIdentification().getVersion(), codeList.getCodes().size());
        cache.put(domain, codeList);
        return codeList;
    }
}