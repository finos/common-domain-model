package org.isda.cdm.processor;

import cdm.base.staticdata.asset.common.ISOCurrencyCodeEnum;
import com.regnosys.rosetta.common.translation.Mapping;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
	protected <T> void mapBasic(T currencySynonym, RosettaModelObjectBuilder parent) {
		if (currencySynonym instanceof String && parent instanceof FieldWithMetaString.FieldWithMetaStringBuilder) {
			Optional<ISOCurrencyCodeEnum> isoCurrencyCode = getEnumValue(synonymToIsoCurrencyCodeEnumMap, (String) currencySynonym, ISOCurrencyCodeEnum.class);
			if (isoCurrencyCode.isPresent()) {
				FieldWithMetaString.FieldWithMetaStringBuilder currencyBuilder = (FieldWithMetaString.FieldWithMetaStringBuilder) parent;
				currencyBuilder.setValue(isoCurrencyCode.get().name())
						.setMeta(MetaFields.builder()
								.setScheme("http://www.fpml.org/ext/iso4217")
								.build());
				return;
			}
		}
		// Update mapping to not found
	}
}
