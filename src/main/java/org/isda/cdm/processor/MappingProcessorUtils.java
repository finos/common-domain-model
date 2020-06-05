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
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

class MappingProcessorUtils {

	static final String ISDA_CREATE_SYNONYM_SOURCE = "ISDA_Create_1_0";

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

	private static List<String> findMappedValues(List<Mapping> mappings) {
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
	static <E extends Enum<E>> Map<String, E> synonymToEnumValueMap(E[] enumValues, String synonymSource) {
		Map<String, E> synonymToEnumValueMap = new HashMap<>();
		Arrays.stream(enumValues).forEach(e -> getSynonymValues(e, synonymSource).forEach(s -> synonymToEnumValueMap.put(s, e)));
		return synonymToEnumValueMap;
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

	static void setValueFromMappings(Path synonymPath, Consumer<String> setter, List<Mapping> mappings, RosettaPath rosettaPath) {
		List<Mapping> mappingsFromSynonymPath = findMappings(mappings, synonymPath);
		findMappedValue(mappingsFromSynonymPath).ifPresent(value -> {
			// set value on model
			setter.accept(value);
			// update mappings
			mappingsFromSynonymPath.forEach(m -> updateMapping(m, rosettaPath));
		});
	}

	static void updateMappings(Path synonymPath, List<Mapping> mappings, RosettaPath rosettaPath) {
		findMappings(mappings, synonymPath).forEach(m -> updateMapping(m, rosettaPath));
	}

	static Path getSynonymPath(Path basePath, String synonym) {
		return getSynonymPath(basePath, "", synonym, null);
	}

	static Path getSynonymPath(Path basePath, String synonym, Integer index) {
		return getSynonymPath(basePath, "", synonym, index);
	}

	static Path getSynonymPath(Path basePath, String prefix, String synonym, Integer index) {
		Path.PathElement element = ofNullable(index)
				.map(i -> new Path.PathElement(prefix + synonym, i))
				.orElse(new Path.PathElement(prefix + synonym));
		return basePath.addElement(element);
	}
}
