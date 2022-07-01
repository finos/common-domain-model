package cdm.namepace;

import com.google.common.collect.ImmutableList;
import com.regnosys.rosetta.common.util.Pair;
import com.regnosys.testing.RosettaFileNameValidator;
import com.regnosys.testing.ValidationReport;
import org.isda.cdm.functions.AbstractFunctionTest;
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

public class ValidateNamespaceTest {


    @Test

    void validateFileNamesMatchNamespace() throws IOException {
        String modelShortName = "cdm";
        Path path = Paths.get("src/main/rosetta");
        RosettaFileNameValidator validator = new RosettaFileNameValidator();

        ValidationReport validationReport = validator.validateFileNamesMatchNamespace(modelShortName, path);

        assertTrue(validationReport.getPassed(),validationReport.getErrors().toString());

    }

}
