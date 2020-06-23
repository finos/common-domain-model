package org.isda.cdm.processor;

import cdm.base.staticdata.asset.common.ISOCurrencyCodeEnum;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.annotations.RosettaSynonym;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.regnosys.rosetta.common.translation.Path.*;
import static com.regnosys.rosetta.common.translation.Path.parse;
import static com.regnosys.rosetta.common.util.PathUtils.toRosettaPath;
import static java.util.Optional.ofNullable;

class MappingProcessorUtils {

	static final List<String> PARTIES = Arrays.asList("partyA", "partyB");
	static final String ISDA_CREATE_SYNONYM_SOURCE = "ISDA_Create_1_0";

	private static final Logger LOGGER = LoggerFactory.getLogger(MappingProcessorUtils.class);

	@NotNull
	static FieldWithMetaString toFieldWithMetaString(String c) {
		return FieldWithMetaString.builder()
				.setValue(c)
				.build();
	}

	@NotNull
	static FieldWithMetaString toFieldWithMetaString(String c, String scheme) {
		return FieldWithMetaString.builder()
				.setValue(c)
				.setMeta(MetaFields.builder()
						.setScheme(scheme)
						.build())
				.build();
	}

	@NotNull
	static <E extends Enum<E>> Map<String, E> synonymToEnumValueMap(E[] enumValues, String synonymSource) {
		Map<String, E> synonymToEnumValueMap = new HashMap<>();
		Arrays.stream(enumValues).forEach(e -> getSynonymValues(e, synonymSource).forEach(s -> synonymToEnumValueMap.put(s, e)));
		return synonymToEnumValueMap;
	}

	@NotNull
	static <E extends Enum<E>> Optional<E> getEnumValue(Map<String, E> synonymToEnumMap, String key, Class<E> clazz) {
		E value = synonymToEnumMap.get(key);
		if (value == null) {
			LOGGER.info("Could not find matching enum {} for {}", clazz.getSimpleName(), key);
		}
		return ofNullable(value);
	}

	@NotNull
	static boolean setIsoCurrency(Map<String, ISOCurrencyCodeEnum> synonymToIsoCurrencyCodeEnumMap, Consumer<FieldWithMetaString> setter, String synonym) {
		Optional<ISOCurrencyCodeEnum> isoCurrencyCode = getEnumValue(synonymToIsoCurrencyCodeEnumMap, synonym, ISOCurrencyCodeEnum.class);
		isoCurrencyCode.ifPresent(c -> setter.accept(toFieldWithMetaString(c.name(), "http://www.fpml.org/ext/iso4217")));
		return isoCurrencyCode.isPresent();
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

	static void setValueAndUpdateMappings(Path synonymPath, Consumer<String> setter, List<Mapping> mappings, RosettaPath rosettaPath) {
		List<Mapping> mappingsFromSynonymPath = filterMappings(mappings, synonymPath);
		getNonNullMappedValue(mappingsFromSynonymPath).ifPresent(value -> {
			// set value on model
			setter.accept(value);
			// update mappings
			mappingsFromSynonymPath.forEach(m -> updateMappingSuccess(m, rosettaPath));
		});
	}

	static void setValueAndOptionallyUpdateMappings(Path synonymPath, Function<String, Boolean> func, List<Mapping> mappings, RosettaPath rosettaPath) {
		List<Mapping> mappingsFromSynonymPath = filterMappings(mappings, synonymPath);
		getNonNullMappedValue(mappingsFromSynonymPath).ifPresent(value -> {
			// set value on model, return boolean whether to update mappings
			if (func.apply(value)) {
				// update mappings
				mappingsFromSynonymPath.forEach(m -> updateMappingSuccess(m, rosettaPath));
			}
		});
	}

	static List<Mapping> filterMappings(List<Mapping> mappings, Path synonymPath) {
		return mappings.stream()
				.filter(p -> synonymPath.fullStartMatches(p.getXmlPath()))
				.collect(Collectors.toList());
	}

	static List<Mapping> filterMappings(List<Mapping> mappings, RosettaPath rosettaPath) {
		return mappings.stream()
				.filter(m -> m.getRosettaPath() != null && m.getRosettaValue() != null)
				.filter(p -> rosettaPath.equals(toRosettaPath(p.getRosettaPath())))
				.collect(Collectors.toList());
	}

	/**
	 * After filtering mappings (either by synonym or cdm path), there are sometimes multiple mapping objects
	 * but there should be only one non-null value.
	 */
	static Optional<String> getNonNullMappedValue(List<Mapping> filteredMappings) {
		return filteredMappings.stream()
				.map(Mapping::getXmlValue)
				.filter(Objects::nonNull)
				.map(String::valueOf)
				.findFirst();
	}

	static void updateMappings(Path synonymPath, List<Mapping> mappings, RosettaPath rosettaPath) {
		filterMappings(mappings, synonymPath).forEach(m -> updateMappingSuccess(m, rosettaPath));
	}

	static void updateMappingSuccess(Mapping mapping, RosettaPath rosettaPath) {
		mapping.setRosettaPath(parse(rosettaPath.buildPath()));
		mapping.setError(null);
		mapping.setCondition(true);
	}

	static void updateMappingFail(Mapping mapping, String error) {
		mapping.setRosettaPath(null);
		mapping.setRosettaValue(null);
		mapping.setError(error);
		mapping.setCondition(true);
	}

	static Path getSynonymPath(Path basePath, String synonym) {
		return getSynonymPath(basePath, "", synonym, null);
	}

	static Path getSynonymPath(Path basePath, String synonym, Integer index) {
		return getSynonymPath(basePath, "", synonym, index);
	}

	static Path getSynonymPath(Path basePath, String prefix, String synonym) {
		return getSynonymPath(basePath, prefix, synonym, null);
	}

	static Path getSynonymPath(Path basePath, String prefix, String synonym, Integer index) {
		PathElement element = ofNullable(index)
				.map(i -> new PathElement(prefix + synonym, i))
				.orElse(new PathElement(prefix + synonym));
		return basePath.addElement(element);
	}
}
