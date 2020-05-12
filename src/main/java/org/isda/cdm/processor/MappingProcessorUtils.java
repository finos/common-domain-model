package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

class MappingProcessorUtils {

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
}
