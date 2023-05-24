package com.regnosys.granite.schemaimport;

import com.regnosys.rosetta.rosetta.RosettaEnumValue;
import com.regnosys.rosetta.rosetta.RosettaEnumeration;
import com.regnosys.rosetta.rosetta.RosettaModel;
import org.eclipse.emf.ecore.resource.Resource;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SchemeImporter {
    @Inject
	private FpMLSchemeEnumReader fpMLSchemeEnumReader;

	@Inject
	private IsoCurrencySchemeEnumReader isoCurrencySchemeEnumReader;

    @Inject
	private AnnotatedRosettaEnumReader enumReader;
    @Inject
	private RosettaResourceWriter rosettaResourceWriter;

	public Map<String, String> generateRosettaEnums(List<RosettaModel> models, String body, String corpus) throws MalformedURLException {
		List<RosettaEnumeration> annotatedEnums = enumReader.getAnnotatedEnum(models, body, corpus);
		for (RosettaEnumeration annotatedEnum : annotatedEnums) {
			Optional<String> schemaLocationForEnumMaybe = enumReader.getSchemaLocationForEnum(annotatedEnum, body, corpus);
			if (schemaLocationForEnumMaybe.isEmpty()) {
				continue;
			}
			SchemeIdentifier schemeIdentifier = new SchemeIdentifier(body, corpus);

			List<RosettaEnumValue> newEnumValues;
			String schemaLocationForEnum = schemaLocationForEnumMaybe.get();
			if (fpMLSchemeEnumReader.applicableToScheme().equals(schemeIdentifier)) {
				String codingSchemeRelativePath = "coding-schemes/fpml/";
				FpMLSchemeEnumReaderProperties fpMLSchemeEnumReaderProperties = new FpMLSchemeEnumReaderProperties(codingSchemeRelativePath, schemaLocationForEnum);
				newEnumValues = fpMLSchemeEnumReader.generateEnumFromScheme(fpMLSchemeEnumReaderProperties);
			} else if (isoCurrencySchemeEnumReader.applicableToScheme().equals(schemeIdentifier)) {
				URL schemaUrl = new URL(schemaLocationForEnum);
				IsoCurrencyEnumReaderProperties isoCurrencyEnumReaderProperties = new IsoCurrencyEnumReaderProperties(schemaUrl);
				newEnumValues = isoCurrencySchemeEnumReader.generateEnumFromScheme(isoCurrencyEnumReaderProperties);
			} else {
				throw new RuntimeException("No scheme reader found for " + body + ", " + corpus);
			}

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
