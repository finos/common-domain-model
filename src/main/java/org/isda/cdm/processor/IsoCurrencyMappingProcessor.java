package org.isda.cdm.processor;

import cdm.base.staticdata.asset.common.ISOCurrencyCodeEnum;
import com.google.common.base.Enums;
import com.regnosys.rosetta.common.translation.Mapping;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;

import java.util.List;
import java.util.Map;

import static org.isda.cdm.processor.MappingProcessorUtils.*;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class IsoCurrencyMappingProcessor extends MappingProcessor {

	private final Map<String, ISOCurrencyCodeEnum> synonymToIsoCurrencyCodeEnumMap;

	public IsoCurrencyMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
		this.synonymToIsoCurrencyCodeEnumMap = synonymToEnumValueMap(ISOCurrencyCodeEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
	}

	@Override
	protected <T> void mapBasic(T value, RosettaModelObjectBuilder parent) {
		if (value instanceof String && parent instanceof FieldWithMetaString.FieldWithMetaStringBuilder) {
			FieldWithMetaString.FieldWithMetaStringBuilder currencyBuilder = (FieldWithMetaString.FieldWithMetaStringBuilder) parent;
			String currencyValue = (String) value;
			// Currency value should either already be a ISO Currency Code or maps to one via synonym
			if (setCurrency(currencyBuilder, getEnumValue(synonymToIsoCurrencyCodeEnumMap, currencyValue, ISOCurrencyCodeEnum.class)
					.orElse(Enums.getIfPresent(ISOCurrencyCodeEnum.class, currencyValue).orNull()))) {
				return;
			}
		}
		// Update mapping to failed if could not be mapped to an ISO currency code
		findMappedValue(getMappings(), getPath()).forEach(m ->
				updateMappingFail(m, String.format("Element with value \"%s\" could not be mapped to a ISO currency code", value)));
	}

	private boolean setCurrency(FieldWithMetaString.FieldWithMetaStringBuilder currencyBuilder, ISOCurrencyCodeEnum isoCurrencyCode) {
		if (isoCurrencyCode != null) {
			currencyBuilder.setValue(isoCurrencyCode.name())
					.setMeta(MetaFields.builder()
							.setScheme("http://www.fpml.org/ext/iso4217")
							.build());
			return true;
		} else {
			currencyBuilder.setValue(null);
			return false;
		}
	}
}
