package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.annotations.RosettaSynonym;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

class MappingProcessorUtils {

	public static final String ISDA_CREATE_SYNONYM_SOURCE = "ISDA_Create_1_0";

	private static final Logger LOGGER = LoggerFactory.getLogger(MappingProcessorUtils.class);

	static FieldWithMetaString toFieldWithMetaString(String c) {
		return FieldWithMetaString.builder()
				.setValue(c)
				.build();
	}

	static List<Mapping> findMappings(List<Mapping> mappings, Path path) {
		return mappings.stream()
				.filter(p -> path.fullStartMatches(p.getXmlPath()))
				.collect(Collectors.toList());
	}

	static List<String> findMappedValues(List<Mapping> mappings) {
		return mappings.stream()
				.map(Mapping::getXmlValue)
				.filter(Objects::nonNull)
				.map(String::valueOf)
				.collect(Collectors.toList());
	}

	static Optional<String> findMappedValue(List<Mapping> mappings) {
		return findMappedValues(mappings).stream().findFirst();
	}

	static void updateMapping(Mapping mapping, RosettaPath rosettaPath) {
		mapping.setRosettaPath(Path.parse(rosettaPath.buildPath()));
		mapping.setError(null);
		mapping.setCondition(true);
	}

	@NotNull
	static Set<String> getSynonymValues(Enum enumValue, String synonymSource) {
		try {
			return Arrays.stream(enumValue.getDeclaringClass().getField(enumValue.name()).getAnnotationsByType(RosettaSynonym.class))
					.filter(s -> s.source().equals(synonymSource))
					.map(RosettaSynonym::value)
					.collect(Collectors.toSet());
		} catch (NoSuchFieldException e) {
			LOGGER.error("Exception occurred getting synonym annotation from enum {}", enumValue, e);
			return Collections.emptySet();
		}
	}
}
