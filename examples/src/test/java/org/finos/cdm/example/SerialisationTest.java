package org.finos.cdm.example;

import cdm.event.common.TradeState;
import cdm.product.asset.InterestRatePayout;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.util.ClassPathUtils;
import com.rosetta.model.lib.records.Date;
import org.finos.cdm.example.util.ResourcesUtils;
import org.finos.rune.mapper.RuneJsonObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Example to illustrate how to serialise/deserialize to/from JSON.
 * <p>
 * Rosetta Model Object serialisation should use {@link RosettaObjectMapper}. Currently only JSON is supported as a
 * serialised format.
 */
public class SerialisationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerialisationTest.class);
    
    @Test
    void shouldDeserialiseCdmSampleFileWithClassLoader_RuneJsonFormat() throws IOException {
        // Get the classLoader from any class in CDM
        ClassLoader classLoader = TradeState.class.getClassLoader();
        Path sampleFilePath = ClassPathUtils
                .loadFromClasspath("ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-equity/eqs-ex01-single-underlyer-execution-long-form.json", classLoader)
                .findFirst()
                .orElseThrow();
        assertNotNull(sampleFilePath);

        TradeState deserializedTradeState =
                new RuneJsonObjectMapper()
                        .readValue(sampleFilePath.toUri().toURL(), TradeState.class);
        assertNotNull(deserializedTradeState);
        assertEquals(Date.parse("2001-09-24"), deserializedTradeState.getTrade().getTradeDate().getValue());
    }

    @Test
    void shouldDeserialiseCdmSampleFileWithResources_RuneJsonFormat() throws IOException {
        // Get the classLoader from any class in CDM
        URL sampleFilePath = Resources.getResource("ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-equity/eqs-ex01-single-underlyer-execution-long-form.json");
        assertNotNull(sampleFilePath);

        TradeState deserializedTradeState =
                new RuneJsonObjectMapper().readValue(sampleFilePath, TradeState.class);
        assertNotNull(deserializedTradeState);
        assertEquals(Date.parse("2001-09-24"), deserializedTradeState.getTrade().getTradeDate().getValue());
    }

    @Test
    void shouldSerialiseCdmObjectThenDeserialise_LegacyJsonFormat() throws JsonProcessingException {
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

    @Test
    void shouldConvertSampleBetweenJsonFormats() throws IOException {
        String sampleInRuneJson = ResourcesUtils.getJson("ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-equity/eqs-ex01-single-underlyer-execution-long-form.json");
        
        LOGGER.info("Sample serialized in Rune JSON : {}", sampleInRuneJson);

        String sampleInLegacyJson = convertFromRuneJsonToLegacyJson(sampleInRuneJson);

        LOGGER.info("Sample serialized in Legacy JSON : {}", sampleInLegacyJson);

        String sampleInRuneJson2 = convertFromLegacyJsonToRuneJson(sampleInLegacyJson);
        
        // This shows that there is no loss of data when converting between old and new JSON formats
        assertEquals(sampleInRuneJson, sampleInRuneJson2);
    }

    private static String convertFromRuneJsonToLegacyJson(String sampleInRuneJson) throws JsonProcessingException {
        // Create a RuneJsonObjectMapper to deserialize the sample from Rune JSON  (default format in CDM v7)
        RuneJsonObjectMapper runeJsonObjectMapper = new RuneJsonObjectMapper();

        // Deserialize sample
        TradeState tradeStateDeserializedFromRuneJson =
                runeJsonObjectMapper.readValue(sampleInRuneJson, TradeState.class);
        assertNotNull(tradeStateDeserializedFromRuneJson);

        // Create a RosettaObjectMapper to serialize the sample to Legacy JSON  (default format in CDM v6 and v5)
        ObjectMapper legacyJsonObjectMapper = RosettaObjectMapper.getNewMinimalRosettaObjectMapper();

        // Serialize sample
        return legacyJsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tradeStateDeserializedFromRuneJson);
    }

    private static String convertFromLegacyJsonToRuneJson(String sampleInLegacyJson) throws JsonProcessingException {
        // Create a RosettaObjectMapper to deserialize the sample from Legacy JSON  (default format in CDM v6 and v5)
        ObjectMapper legacyJsonObjectMapper = RosettaObjectMapper.getNewMinimalRosettaObjectMapper();
       
        // Deserialize sample
        TradeState tradeStateDeserializedFromRuneJson =
                legacyJsonObjectMapper.readValue(sampleInLegacyJson, TradeState.class);
        assertNotNull(tradeStateDeserializedFromRuneJson);

        // Create a RuneJsonObjectMapper to serialize the sample to Rune JSON  (default format in CDM v7)
        RuneJsonObjectMapper runeJsonObjectMapper = new RuneJsonObjectMapper();

        // Serialize sample
        return runeJsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tradeStateDeserializedFromRuneJson);
    }
}
