package cdm.namepace;

import com.regnosys.testing.RosettaFileNameValidator;
import com.regnosys.testing.ValidationReport;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidateNamespaceTest {

    @Test
    void validateFileNamesMatchNamespace() throws IOException {
        RosettaFileNameValidator validator = new RosettaFileNameValidator("cdm", Paths.get("src/main/rosetta"), null);
        ValidationReport validationReport = validator.validateFileNamesMatchNamespace();
        assertTrue(validationReport.getPassed(),validationReport.getErrors().toString());
    }
}
