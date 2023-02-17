package com.regnosys.cdm.example;

import cdm.base.datetime.daycount.DayCountFractionEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Example to illustrate how to serialise/deserialise rosetta enumerations to/from JSON.
 * <p>
 * Rosetta Model Object serialisation should use {@link RosettaObjectMapper}. Currently only JSON is supported as a
 * serialised format.
 */
public class EnumSerialisation extends AbstractExample {

    @Override
    public void example() throws RuntimeException {

        // Instantiate the pre-configured Rosetta ObjectMapper
        //
        ObjectMapper rosettaObjectMapper = RosettaObjectMapper.getNewRosettaObjectMapper();

        // Check an instance of an enumeration without a displayName
        System.out.println("Example of enum without a displayName");
        checkSerialisation(rosettaObjectMapper, DayCountFractionEnum.ACT_360);

        System.out.println("-------------------------------------------------");

        System.out.println("Example of enum with a displayName");
        // Check an instance of an enumeration with a displayName
        // DJ_CDX_EM_ displayName "DJ.CDX.EM " <"Broker Confirmation Type for CDS Index trades relating to Dow Jones CDX.EM index series.">
        checkSerialisation(rosettaObjectMapper, DayCountFractionEnum.ACT_365L);
    }

	private static void checkSerialisation(ObjectMapper rosettaObjectMapper, DayCountFractionEnum myEnum) {
        try {

            // Serialise to JSON
            //
            String json = rosettaObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(myEnum);

            System.out.println("Enum: " + myEnum.name());
            System.out.println("Serialise to JSON: " + json);

            // Deserialise back to Java
            //
            DayCountFractionEnum deserializedObject = rosettaObjectMapper.readValue(json, myEnum.getClass());

            System.out.println("Deserialised Enum: " + deserializedObject.name());

            // Compare the original and deserialised CDM Java objects
            //
            assertThat("The enum and serialized-then-deserialized objects are the same",
                    myEnum, equalTo(deserializedObject));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}

    public static void main(String[] args) {
        new EnumSerialisation().run();
    }
}
