package com.regnosys.ingest.fpml;

import cdm.event.common.TradeState;
import com.regnosys.ingest.test.framework.ingestor.IngestionTest;
import com.regnosys.ingest.test.framework.ingestor.IngestionTestUtil;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionService;
import com.regnosys.ingest.test.framework.ingestor.testing.Expectation;
import com.regnosys.ingest.test.framework.ingestor.testing.QualificationExpectation;
import com.regnosys.rosetta.common.util.ClassPathUtils;
import com.regnosys.rosetta.common.util.UrlUtils;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.Arguments;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.regnosys.ingest.IngestionEnvUtil.getFpml5ConfirmationToTradeState;

public class Fpml513ProductIngestionServiceTest extends IngestionTest<TradeState> {

    private static final String BASE_DIR = "cdm-sample-files/fpml-5-13/products/";

    private static IngestionService ingestionService;

    @BeforeAll
    static void setup() {
        CdmRuntimeModule runtimeModule = new CdmRuntimeModule();
        initialiseIngestionFactory(runtimeModule, IngestionTestUtil.getPostProcessors(runtimeModule));
        ingestionService = getFpml5ConfirmationToTradeState();
    }

    @Override
    protected Class<TradeState> getClazz() {
        return TradeState.class;
    }

    @Override
    protected IngestionService ingestionService() {
        return ingestionService;
    }

    @SuppressWarnings("unused")//used by the junit parameterized test
    private static Stream<Arguments> fpMLFiles() {
        return readExpectationsFromPath(BASE_DIR);
    }

//    protected static Stream<Arguments> readExpectationsFromPath(String basePath) {
//        List<URL> expectations = ClassPathUtils
//                .findPathsFromClassPath(List.of(basePath), "expectations.json", Optional
//                        .empty(), IngestionTest.class.getClassLoader())
//                .stream().map(UrlUtils::toUrl).collect(Collectors.toList());
//        return expectations.stream()
//                .flatMap(u -> getArguments(u));
//    }

    private static Stream<Arguments> getArguments(URL expectationUrl) {

        try {

            String expectationPath = expectationUrl.getPath();
            Path parent = Path.of(expectationPath).getParent();
            System.out.println("dir: " + parent);
            return Files.walk(parent)
                    .filter(p -> p.getFileName().toString().endsWith(".xml"))
                    .map(p -> {
                        System.out.println("processing: " + p);
                        return Arguments
                                .of(expectationPath.toString(),
                                        Expectation.expect("", p.toString().replace("/Users/hugohills/dev/github/rosetta-models/common-domain-model/rosetta-source/target/classes/", ""), 0, true, 0, new QualificationExpectation(true, List.of(), 0)),
                                        p.getFileName().toString());
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //return Stream.of();

//

    }

    //Expectation
}
