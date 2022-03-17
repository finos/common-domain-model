package com.regnosys.cdm.example.util;

import com.google.common.io.Resources;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.RosettaModelObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class ResourcesUtils {

	public static <T extends RosettaModelObject> T getObject(Class<T> clazz, String resourceName) throws IOException {
		URL url = Resources.getResource(resourceName);
		String json = Resources.toString(url, Charset.defaultCharset());
		return RosettaObjectMapper.getNewRosettaObjectMapper().readValue(json, clazz);
	}
}
