package org.isda.cdm.processor;

import cdm.base.staticdata.asset.common.ISOCurrencyCodeEnum;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import org.jsoup.Jsoup;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Optional.ofNullable;

public class CreateiQMappingProcessorUtils {

	public static final String PARTY_A = "partyA";
	public static final String PARTY_B = "partyB";
	public static final List<String> PARTIES = Arrays.asList(PARTY_A, PARTY_B);

	public static CounterpartyRoleEnum toCounterpartyRoleEnum(String party) {
		if (PARTY_A.equalsIgnoreCase(party))
			return CounterpartyRoleEnum.PARTY_1;
		else if (PARTY_B.equalsIgnoreCase(party))
			return CounterpartyRoleEnum.PARTY_2;
		else
			throw new IllegalArgumentException(String.format("Unknown CreateiQ party %s", party));
	}

	public static FieldWithMetaString toFieldWithMetaString(String c) {
		return FieldWithMetaString.builder()
				.setValue(c)
				.build();
	}

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

	public static String removeHtml(String value) {
		return Jsoup.parse(value).text();
	}
}
