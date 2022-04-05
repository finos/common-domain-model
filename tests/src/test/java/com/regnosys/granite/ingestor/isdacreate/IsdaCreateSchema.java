package com.regnosys.granite.ingestor.isdacreate;

import java.util.HashMap;
import java.util.Map;

class IsdaCreateSchema {
	private final Map<String, String> schema = new HashMap<>();

	public IsdaCreateSchema(String env, String documentName, String version) {
		schema.put("env", env);
		schema.put("document-name", documentName);
		schema.put("version", version);
	}

	public Map<String, String> getSchema() {
		return schema;
	}
}
