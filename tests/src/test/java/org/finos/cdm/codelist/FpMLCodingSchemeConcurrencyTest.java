package org.finos.cdm.codelist;

import cdm.base.staticdata.codelist.validation.datarule.FpMLCodingSchemeIsValidCodingScheme;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.validation.ValidationResult;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test pack generation validates samples on worker threads, so loading a code list has to be thread-safe. A code
 * list that fails to load makes ValidateFpMLCodingSchemeDomain return false, reporting a valid code as a data rule
 * failure. Only catches that if it is the first code list use in its JVM, as the code lists are loaded once.
 */
class FpMLCodingSchemeConcurrencyTest {

    private static final int THREADS = 8;

    @Test
    void validatesTheSameCodeOnEveryThread() throws Exception {
        Injector injector = Guice.createInjector(new CdmRuntimeModule());
        FpMLCodingSchemeIsValidCodingScheme rule = injector.getInstance(FpMLCodingSchemeIsValidCodingScheme.class);
        RosettaPath path = RosettaPath.valueOf("businessCenter");

        CyclicBarrier start = new CyclicBarrier(THREADS);
        Callable<List<ValidationResult<?>>> validate = () -> {
            start.await(30, TimeUnit.SECONDS);
            return rule.getValidationResults(path, "GBLO", "business-center");
        };

        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        try {
            List<String> failures = new ArrayList<>();
            for (Future<List<ValidationResult<?>>> result : executor.invokeAll(Collections.nCopies(THREADS, validate))) {
                result.get().stream()
                        .filter(validationResult -> !validationResult.isSuccess())
                        .forEach(validationResult -> failures.add(validationResult.getFailureReason().orElse("")));
            }
            assertEquals(Collections.emptyList(), failures, "GBLO is a business-center code, so every thread should accept it");
        } finally {
            executor.shutdownNow();
        }
    }
}
