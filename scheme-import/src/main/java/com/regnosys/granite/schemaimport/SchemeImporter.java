package com.regnosys.granite.schemaimport;

import com.regnosys.rosetta.rosetta.RosettaEnumValue;
import com.regnosys.rosetta.rosetta.RosettaEnumeration;
import com.regnosys.rosetta.rosetta.RosettaModel;
import org.eclipse.emf.ecore.resource.Resource;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SchemeImporter {
    @Inject
	private FpMLSchemeEnumReader fpMLSchemeEnumReader;
    @Inject
	private AnnotatedRosettaEnumReader enumReader;
    @Inject
	private RosettaResourceWriter rosettaResourceWriter;

	public Map<String, String> generateRosettaEnums(URL enumCodingSchemeUrl, String enumCodingSchemeRelativePath, List<RosettaModel> models, String body, String corpus) {
		List<RosettaEnumeration> annotatedEnums = enumReader.getAnnotatedEnum(models, body, corpus);
		for (RosettaEnumeration annotatedEnum : annotatedEnums) {
			Optional<String> schemaLocationForEnumMaybe = enumReader.getSchemaLocationForEnum(annotatedEnum, body, corpus);
			if (schemaLocationForEnumMaybe.isEmpty()) {
				continue;
			}
			List<RosettaEnumValue> newEnumValues = fpMLSchemeEnumReader.generateEnumFromScheme(enumCodingSchemeUrl, enumCodingSchemeRelativePath, schemaLocationForEnumMaybe.get());
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
