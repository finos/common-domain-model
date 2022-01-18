package org.isda.cdm.processor;

import cdm.base.staticdata.asset.common.ISOCurrencyCodeEnum;
import com.regnosys.rosetta.common.translation.MappingProcessorUtils;
import com.rosetta.model.lib.annotations.RosettaSynonym;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class CdmMappingProcessorUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(MappingProcessorUtils.class);

	@NotNull
	public static FieldWithMetaString toFieldWithMetaString(String c) {
		return FieldWithMetaString.builder()
				.setValue(c)
				.build();
	}

	@NotNull
	public static FieldWithMetaString toFieldWithMetaString(String c, String scheme) {
		return FieldWithMetaString.builder()
				.setValue(c)
				.setMeta(MetaFields.builder()
						.setScheme(scheme)
						.build())
				.build();
	}

	public static boolean setIsoCurrency(Map<String, ISOCurrencyCodeEnum> synonymToIsoCurrencyCodeEnumMap, Consumer<FieldWithMetaString> setter, String synonym) {
		Optional<ISOCurrencyCodeEnum> isoCurrencyCode = getEnumValue(synonymToIsoCurrencyCodeEnumMap, synonym, ISOCurrencyCodeEnum.class);
		isoCurrencyCode.ifPresent(c -> setter.accept(toFieldWithMetaString(c.name(), "http://www.fpml.org/ext/iso4217")));
		return isoCurrencyCode.isPresent();
	}

	public static <E extends Enum<E>> Map<String, E> synonymToEnumValueMap(E[] enumValues, String synonymSource) {
		Map<String, E> synonymToEnumValueMap = new HashMap<>();
		Arrays.stream(enumValues)
				.forEach(e -> getEnumSynonyms(e, synonymSource)
						.forEach(s -> synonymToEnumValueMap.put(s, e)));
		return synonymToEnumValueMap;
	}

	public static <E extends Enum<E>> Optional<E> getEnumValue(Map<String, E> synonymToEnumMap, String key, Class<E> clazz) {
		E value = synonymToEnumMap.get(key);
		if (value == null) {
			LOGGER.info("Could not find matching enum {} for {}", clazz.getSimpleName(), key);
		}
		return ofNullable(value);
	}

	private static Set<String> getEnumSynonyms(Enum enumValue, String synonymSource) {
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

	@NotNull
	public static String removeHtml(String value) {
		return Jsoup.parse(value).text();
	}
}
