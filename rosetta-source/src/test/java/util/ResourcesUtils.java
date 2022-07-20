package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.regnosys.rosetta.common.hashing.ReKeyProcessStep;
import com.regnosys.rosetta.common.hashing.ReferenceResolverProcessStep;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.process.PostProcessStep;
import org.isda.cdm.processor.CdmReferenceConfig;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

public class ResourcesUtils {

	public static <T extends RosettaModelObject> T getObject(Class<T> clazz, String resourceName) throws IOException {
		String json = getJson(resourceName);
		return RosettaObjectMapper.getNewRosettaObjectMapper().readValue(json, clazz);
	}

	public static <T extends RosettaModelObject> T getObjectAndResolveReferences(Class<T> clazz, String resourceName) throws IOException {
		T object = getObject(clazz, resourceName);
		return resolveReferences(object);
	}

	public static String getJson(String resourceName) throws IOException {
		URL url = Resources.getResource(resourceName);
		String json = Resources.toString(url, Charset.defaultCharset());
		return json;
	}

	public static <T> T getInputObject(String funcInputFile, String funcInputName, Class<T> funcInputType) throws IOException {
		URL url = Resources.getResource(funcInputFile);
		JsonNode jsonNode = new ObjectMapper().readTree(url);
		String json = jsonNode.get(funcInputName).toString();
		T object = RosettaObjectMapper.getNewRosettaObjectMapper().readValue(json, funcInputType);
		if (object instanceof RosettaModelObject) {
			return (T) resolveReferences((RosettaModelObject) object);
		}
		return object;
	}

	public static <T> T getInputObjectAndResolveReferences(String funcInputFile, String funcInputName, Class<T> funcInputType) throws IOException {
		T object = getInputObject(funcInputFile, funcInputName, funcInputType);
		if (object instanceof RosettaModelObject) {
			return (T) resolveReferences((RosettaModelObject) object);
		}
		return object;
	}

	private static <T extends RosettaModelObject> T resolveReferences(T object) {
		RosettaModelObject builder = object.toBuilder();
		new ReferenceResolverProcessStep(CdmReferenceConfig.get()).runProcessStep(builder.getType(), builder);
		return (T) builder.build();
	}

	@SuppressWarnings("unused")
	public static void print(RosettaModelObject o) throws JsonProcessingException {
		System.out.println(RosettaObjectMapper.getNewRosettaObjectMapper()
				.writerWithDefaultPrettyPrinter()
				.writeValueAsString(o.toBuilder().prune().build()));
	}

	@SuppressWarnings("unused")
	public static <T extends RosettaModelObjectBuilder> T reKey(T builder) {
		GlobalKeyProcessStep globalKeyProcessStep = new GlobalKeyProcessStep(NonNullHashCollector::new);
		List<PostProcessStep> postProcessors = Arrays.asList(globalKeyProcessStep, new ReKeyProcessStep(globalKeyProcessStep));
		postProcessors.forEach(p -> p.runProcessStep(builder.getType(), builder));
		return builder;
	}
}
