package com.regnosys.cdm.example;

import cdm.product.asset.InterestRatePayout;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Example to illustrate how to serialise/deserialize to/from JSON.
 * <p>
 * Rosetta Model Object serialisation should use {@link RosettaObjectMapper}. Currently only JSON is supported as a
 * serialised format.
 */
public class Serialisation {

    public static void main(String[] args) throws Exception {

        // Instantiate the pre-configured Rosetta ObjectMapper
        //
        ObjectMapper rosettaObjectMapper = RosettaObjectMapper.getDefaultRosettaObjectMapper();

        // Create an instance of a Fixed Rate Payout CDM object
        //
        InterestRatePayout fixedRatePayout = InterestRatePayoutCreation.getFixedRatePayout(BigDecimal.valueOf(0.05));

        // Serialise to JSON
        //
        String json = rosettaObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(fixedRatePayout);

        System.out.println("Serialise to JSON");
        System.out.println(json);

        // Deserialize back to Java
        //
        InterestRatePayout deserializedObject = rosettaObjectMapper.readValue(json, fixedRatePayout.getClass());

        System.out.println("Deserialize to Java");
        System.out.println(deserializedObject.toString());

        // Compare the original and deserialized CDM Java objects
        //
        assertThat("The fixedRatePayout and serialized-then-deserialized objects are the same",
                fixedRatePayout, equalTo(deserializedObject));
    }
}
