import cdm.event.common.TradeState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapperCreator;
import com.rosetta.model.lib.RosettaModelObject;
import org.finos.rune.mapper.RuneJsonObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SerialisationTest {
    private final ObjectMapper outputMapper;
    private final ObjectMapper inputMapper;

    public SerialisationTest() {
        inputMapper = RosettaObjectMapperCreator.forJSON().create();
        outputMapper = new RuneJsonObjectMapper();
    }

    public static void main(String[] args) throws IOException {
        SerialisationTest serialisationTest = new SerialisationTest();
        Path productsPath = Paths.get("/Users/david.al-kanani/Developer/rosetta-models/common-domain-model/rosetta-source/src/main/resources/result-json-files/fpml-5-10/products");
        Path writePath = Paths.get("/Users/david.al-kanani/Developer/rosetta-models/common-domain-model/rosetta-source/src/main/resources/result-json-files/fpml-5-10/products-new");
        try (Stream<Path> paths = Files.walk(productsPath)) {
            paths.forEach(p -> {
                if (Files.isRegularFile(p)) {
                    String input = serialisationTest.readAsString(p);
                    TradeState tradeState = serialisationTest.fromJson(input, TradeState.class);
                    String output = serialisationTest.toJson(tradeState);
                    Path outPath = writePath.resolve(productsPath.relativize(p).normalize());
                    try {
                        Files.createDirectories(outPath.getParent());
                        Files.write(outPath, output.getBytes(StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    private String readAsString(Path jsonPath) {
        try {
            return new String(Files.readAllBytes(jsonPath));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private <T extends RosettaModelObject> T fromJson(String runeJson, Class<T> type) {
        try {
            return inputMapper.readValue(runeJson, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends RosettaModelObject> String toJson(T runeObject) {
        try {
            return outputMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(runeObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
