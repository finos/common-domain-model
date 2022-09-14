package com.regnosys.granite.schemaimport;

import com.regnosys.rosetta.rosetta.RosettaEnumValue;
import com.regnosys.rosetta.rosetta.RosettaEnumeration;
import org.eclipse.emf.ecore.resource.Resource;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SchemeImporter {
	private final SchemeEnumReader fpMLSchemeEnumReader;
	private final AnnotatedRosettaEnumReader enumReader;
	private final RosettaResourceWriter rosettaResourceWriter;

	public SchemeImporter(FpMLSchemeEnumReader fpMLSchemeEnumReader,
						  AnnotatedRosettaEnumReader enumReader, RosettaResourceWriter rosettaEnumWriter) {
		this.fpMLSchemeEnumReader = fpMLSchemeEnumReader;
		this.enumReader = enumReader;
		this.rosettaResourceWriter = rosettaEnumWriter;
	}

	public Map<String, String> generateRosettaEnums(String body, String corpus) {
		List<RosettaEnumeration> annotatedEnums = enumReader.getAnnotatedEnum(body, corpus);
		for (RosettaEnumeration annotatedEnum : annotatedEnums) {
			Optional<String> schemaLocationForEnumMaybe = enumReader.getSchemaLocationForEnum(annotatedEnum, body, corpus);
			if (schemaLocationForEnumMaybe.isEmpty()) {
				continue;
			}
			List<RosettaEnumValue> newEnumValues = fpMLSchemeEnumReader.generateEnumFromScheme(schemaLocationForEnumMaybe.get());
			overwriteEnums(annotatedEnum, newEnumValues);
		}

		Map<Resource, List<RosettaEnumeration>> enumsGroupedByRosettaResource = annotatedEnums.stream()
			.collect(Collectors.groupingBy(x -> x.eContainer().eResource()));

		return rosettaResourceWriter.generateRosettaFiles(enumsGroupedByRosettaResource.keySet());
	}

	private void overwriteEnums(RosettaEnumeration rosettaEnumeration, List<RosettaEnumValue> newEnumValues) {
		rosettaEnumeration.getEnumValues().clear();
		rosettaEnumeration.getEnumValues().addAll(newEnumValues);
	}
}
