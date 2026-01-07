package org.finos.cdm.example;

import cdm.event.common.TradeState;
import cdm.product.asset.InterestRatePayout;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.util.ClassPathUtils;
import com.rosetta.model.lib.records.Date;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Example to illustrate how to serialise/deserialize to/from JSON.
 * <p>
 * Rosetta Model Object serialisation should use {@link RosettaObjectMapper}. Currently only JSON is supported as a
 * serialised format.
 */
public class SerialisationTest {

    @Test
    void shouldDeserialiseCdmSampleFileWithClassLoader() throws IOException {
        // Get the classLoader from any class in CDM
        ClassLoader classLoader = TradeState.class.getClassLoader();
        Path sampleFilePath = ClassPathUtils
                .loadFromClasspath("ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-equity/eqs-ex01-single-underlyer-execution-long-form.json", classLoader)
                .findFirst()
                .orElseThrow();
        assertNotNull(sampleFilePath);

        TradeState deserializedTradeState =
                RosettaObjectMapper.getNewRosettaObjectMapper()
                        .readValue(sampleFilePath.toUri().toURL(), TradeState.class);
        assertNotNull(deserializedTradeState);
        assertEquals(Date.parse("2001-09-24"), deserializedTradeState.getTrade().getTradeDate().getValue());
    }

    @Test
    void shouldDeserialiseCdmSampleFileWithResources() throws IOException {
        // Get the classLoader from any class in CDM
        URL sampleFilePath = Resources.getResource("ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-equity/eqs-ex01-single-underlyer-execution-long-form.json");
        assertNotNull(sampleFilePath);

        TradeState deserializedTradeState =
                RosettaObjectMapper.getNewRosettaObjectMapper().readValue(sampleFilePath, TradeState.class);
        assertNotNull(deserializedTradeState);
        assertEquals(Date.parse("2001-09-24"), deserializedTradeState.getTrade().getTradeDate().getValue());
    }

    @Test
    void shouldSerialiseCdmObjectThenDeserialise() throws JsonProcessingException {
        // Instantiate the pre-configured Rosetta ObjectMapper
        ObjectMapper rosettaObjectMapper = RosettaObjectMapper.getNewRosettaObjectMapper();

        // Create an instance of a Fixed Rate Payout CDM object
        InterestRatePayout fixedRatePayout = InterestRatePayoutCreation.getFixedRatePayout(BigDecimal.valueOf(0.05));

        // Serialise to JSON
        String json = rosettaObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(fixedRatePayout);
        assertNotNull(json);

        // Deserialize back to Java
        InterestRatePayout deserializedObject = rosettaObjectMapper.readValue(json, fixedRatePayout.getClass());
        assertNotNull(deserializedObject);
    }
}
