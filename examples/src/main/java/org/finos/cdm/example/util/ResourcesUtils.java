package org.finos.cdm.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.io.Resources;
import com.regnosys.rosetta.common.hashing.ReferenceResolverProcessStep;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.RosettaModelObject;
import org.isda.cdm.processor.CdmReferenceConfig;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ResourcesUtils {

	private static final ObjectWriter OBJECT_WRITER =
			RosettaObjectMapper
					.getNewMinimalRosettaObjectMapper()
					.writerWithDefaultPrettyPrinter();
	
	public static String getJson(String resourceName) {
		try {
			URL url = Resources.getResource(resourceName);
            return Resources.toString(url, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
	}

	public static <T extends RosettaModelObject> T getObject(Class<T> clazz, String resourceName) {
		try {
			String json = getJson(resourceName);
            return RosettaObjectMapper.getNewRosettaObjectMapper().readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

	public static <T extends RosettaModelObject> T getObjectAndResolveReferences(Class<T> clazz, String resourceName) {
		T object = getObject(clazz, resourceName);
		return resolveReferences(object);
	}
	
	private static <T extends RosettaModelObject> T resolveReferences(T object) {
		RosettaModelObject builder = object.toBuilder();
		new ReferenceResolverProcessStep(CdmReferenceConfig.get()).runProcessStep(builder.getType(), builder);
		return (T) builder.build();
	}
	
	public static String serialiseAsJson(RosettaModelObject o) {
        try {
            return OBJECT_WRITER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
