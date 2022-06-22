package cdm;

import com.google.common.collect.ImmutableList;
import com.regnosys.rosetta.common.util.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class RosettaNamespaceTest {

    private static final List<String> VALID_SUFFIX = ImmutableList.of("func", "rule", "enum", "type", "synonym", "desc");


    public List<String> assertFileNamesMatchNamespace( String shortName, String path) throws IOException {
        List<String> executables = Files.walk(Paths.get(path))
                .filter(x -> x.getFileName().toString().endsWith(".rosetta"))
                .map(this::extractNamespace)
                .map(rosettaFileToNamespace ->
                        ensureFileNameSuffix(shortName, rosettaFileToNamespace.left(), rosettaFileToNamespace.right()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return executables;
    }

    public List<String> ensureFileNameSuffix(String modelShortName, String rosettaFileName, String rosettaNamespace) {
        List<String> validationResults = new ArrayList<>();

        String name = rosettaFileName.substring(0, rosettaFileName.indexOf(".rosetta"));
        String[] parts = name.split("-");
        if (parts.length == 0) {
            validationResults.add("No suffix for file '" + rosettaFileName + "' with namespace '" + rosettaNamespace + "'. Should be one of " + VALID_SUFFIX);
            return validationResults;
        }

        String suffix = parts[parts.length - 1];
        if(!VALID_SUFFIX.contains(suffix)) {
            validationResults.add("suffix for file '" + rosettaFileName + "' with namespace '" + rosettaNamespace + "'. Should be one of " + VALID_SUFFIX);

            return validationResults;
        }
        String fileWithoutSuffix = modelShortName + "." + String.join(".", Arrays.copyOfRange(parts, 0, parts.length - 1));

        if(null == rosettaNamespace) {
            validationResults.add("File name '" + rosettaFileName + "' is not in line with namespace '" + rosettaNamespace + "'. Namespace should be " + fileWithoutSuffix);

            return validationResults;
        }
        else{

            if (!modelShortName.equals(rosettaNamespace.split("\\.")[0])) {
                validationResults.add("file '" + rosettaFileName + "' with namespace '" + rosettaNamespace + "' should start with model name '" + modelShortName + "'");

                return validationResults;
            }


            if (!rosettaNamespace.equals(fileWithoutSuffix)) {
                validationResults.add("File name '" + rosettaFileName + "' is not in line with namespace '" + rosettaNamespace + ". Namespace should be " + fileWithoutSuffix);

                return validationResults;
            }

        }
        return validationResults;
    }

    public Pair<String, String> extractNamespace(Path rosettaFile) {
        try {
            return Files.readAllLines(rosettaFile).stream()
                    .filter(line -> line.contains("namespace"))
                    .map(line -> extractNamespaceFromLine(rosettaFile, line))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Could not find namespace for " + rosettaFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Pair<String, String> extractNamespaceFromLine(Path rosettaFile, String line) {
        Pattern pattern = Pattern.compile("^namespace ([a-zA-Z\\.]*)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find() && matcher.groupCount() == 1) {
            return Pair.of(rosettaFile.getFileName().toString(), matcher.group(1));
        }
        return Pair.of(rosettaFile.getFileName().toString(), null);
    }
}
