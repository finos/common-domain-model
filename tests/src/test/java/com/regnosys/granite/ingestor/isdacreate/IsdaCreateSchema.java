package com.regnosys.granite.ingestor.isdacreate;

import java.util.HashMap;
import java.util.Map;

import static com.regnosys.rosetta.common.testing.MappingCoverage.*;

class IsdaCreateSchema {
	private final Map<String, String> schema = new HashMap<>();

	public IsdaCreateSchema(String env, String documentName, String version) {
		schema.put(ENV, env);
		schema.put(DOCUMENT_NAME, documentName);
		schema.put(VERSION, version);
	}

	public Map<String, String> getSchema() {
		return schema;
	}
}
