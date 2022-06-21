package cdm;

import com.google.common.collect.ImmutableList;
import com.regnosys.rosetta.common.util.Pair;
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
import static cdm.RosettaNamespaceTest.*;

public class CDMNamespaceTest extends RosettaNamespaceTest {


    @Test
    void assertFileNamesMatchNamespace() throws IOException {
        String modelShortName = "cdm";
        String path = "src/main/rosetta";
        assertFileNamesMatchNamespace(modelShortName, path);

    }

}
