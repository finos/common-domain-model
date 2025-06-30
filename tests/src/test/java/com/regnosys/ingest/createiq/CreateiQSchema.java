package com.regnosys.ingest.createiq;

import java.util.HashMap;
import java.util.Map;

import static com.regnosys.rosetta.common.testing.MappingCoverage.*;

class CreateiQSchema {
	private final Map<String, String> schema = new HashMap<>();

	public CreateiQSchema(String env, String documentName, String version) {
		schema.put(ENV, env);
		schema.put(DOCUMENT_NAME, documentName);
		schema.put(VERSION, version);
	}

	public Map<String, String> getSchema() {
		return schema;
	}
}
