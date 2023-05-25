package com.regnosys.granite.schemaimport;

import com.regnosys.granite.schemaimport.fpml.FpMLSchemeEnumReader;
import com.regnosys.granite.schemaimport.iso.currency.IsoCurrencySchemeEnumReader;
import com.regnosys.rosetta.rosetta.RosettaEnumValue;
import com.regnosys.rosetta.rosetta.RosettaEnumeration;
import com.regnosys.rosetta.rosetta.RosettaModel;
import org.eclipse.emf.ecore.resource.Resource;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SchemeImporter {
    @Inject
	private AnnotatedRosettaEnumReader enumReader;
    @Inject
	private RosettaResourceWriter rosettaResourceWriter;

	public Map<String, String> generateRosettaEnums(List<RosettaModel> models, String body, String corpus, SchemeEnumReader schemeEnumReader) throws MalformedURLException {
		List<RosettaEnumeration> annotatedEnums = enumReader.getAnnotatedEnum(models, body, corpus);
		for (RosettaEnumeration annotatedEnum : annotatedEnums) {
			Optional<String> schemaLocationForEnumMaybe = enumReader.getSchemaLocationForEnum(annotatedEnum, body, corpus);
			if (schemaLocationForEnumMaybe.isEmpty()) {
				continue;
			}
			String schemaLocationForEnum = schemaLocationForEnumMaybe.get();
			List<RosettaEnumValue> newEnumValues = schemeEnumReader.generateEnumFromScheme(new URL(schemaLocationForEnum));

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
