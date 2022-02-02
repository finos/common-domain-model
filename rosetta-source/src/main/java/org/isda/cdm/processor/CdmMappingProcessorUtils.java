package org.isda.cdm.processor;

import cdm.base.staticdata.asset.common.ISOCurrencyCodeEnum;
import com.regnosys.rosetta.common.translation.MappingProcessorUtils;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Consumer;

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

	public static boolean setIsoCurrency(ISOCurrencyCodeEnum isoCurrencyCodeEnum, Consumer<FieldWithMetaString> setter) {
		Optional<ISOCurrencyCodeEnum> isoCurrencyCode = ofNullable(isoCurrencyCodeEnum);
		isoCurrencyCode.ifPresent(c -> setter.accept(toFieldWithMetaString(c.name(), "http://www.fpml.org/ext/iso4217")));
		return isoCurrencyCode.isPresent();
	}

	@NotNull
	public static String removeHtml(String value) {
		return Jsoup.parse(value).text();
	}
}
