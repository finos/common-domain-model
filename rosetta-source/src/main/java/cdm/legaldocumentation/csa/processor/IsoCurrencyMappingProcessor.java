package cdm.legaldocumentation.csa.processor;

import cdm.base.staticdata.asset.common.ISOCurrencyCodeEnum;
import com.google.common.base.Enums;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;

import java.util.List;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.filterMappings;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.updateMappingFail;

/**
 * CreateiQ mapping processor.
 */
@SuppressWarnings("unused")
public class IsoCurrencyMappingProcessor extends MappingProcessor {

	public IsoCurrencyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		if (builder instanceof FieldWithMetaString.FieldWithMetaStringBuilder) {
			FieldWithMetaString.FieldWithMetaStringBuilder currencyBuilder = (FieldWithMetaString.FieldWithMetaStringBuilder) builder;
			String currencyValue = currencyBuilder.getValue();
			// Currency value should either already be a ISO Currency Code or maps to one via synonym
			if (setCurrency(currencyBuilder, getSynonymToEnumMap().getEnumValueOptional(ISOCurrencyCodeEnum.class, currencyValue)
					.orElse(Enums.getIfPresent(ISOCurrencyCodeEnum.class, currencyValue)
							.orNull()))) {
				return;
			}
			// Update mapping to failed if could not be mapped to an ISO currency code
			filterMappings(getMappings(), getModelPath()).forEach(m ->
					updateMappingFail(m, String.format("Element with value \"%s\" could not be mapped to a ISO currency code", currencyValue)));
		}
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
