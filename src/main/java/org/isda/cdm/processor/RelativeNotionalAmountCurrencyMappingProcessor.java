package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.AssetIdentifier.AssetIdentifierBuilder;

/**
 * Custom mapping processor for ResolvablePayoutQuantity used to set the assetIdentifier.currency.
 *
 * The currency to be set is the FpML id/href attribute relativeNotionalAmount.
 *
 * E.g. find id at path "relativeNotionalAmount.href", and use that id to find the anchor at
 * path "notionalAmount.id", from there use currency from path "notionalAmount.currency".
 */
@SuppressWarnings("unused")
public class RelativeNotionalAmountCurrencyMappingProcessor extends MappingProcessor {

	public RelativeNotionalAmountCurrencyMappingProcessor(RosettaPath path, List<Mapping> mappings) {
		super(path, mappings);
	}

	@Override
	protected <R extends RosettaModelObject> void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		findHrefMapping()
				.ifPresent(hrefMapping ->
						Optional.ofNullable(hrefMapping.getXmlValue())
								.map(Object::toString)
								.flatMap(this::findMappingForId)
								.ifPresent(mapping -> {
									Path notionalCurrencyPath = toNotionalCurrencyPath(mapping);
									findXmlValue(notionalCurrencyPath).ifPresent(currency -> {
										AssetIdentifierBuilder assetIdentifierBuilder = (AssetIdentifierBuilder) builder;
										assetIdentifierBuilder.getOrCreateCurrency().setValue(currency);
									});
								}));
	}

	@Override
	protected void map(List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
	}

	private Optional<Mapping> findHrefMapping() {
		return getMappings().stream()
				.filter(m -> m.getXmlPath().endsWith("relativeNotionalAmount", "href"))
				.findFirst();
	}

	private Optional<Mapping> findMappingForId(String id) {
		return getMappings().stream()
				.filter(m -> m.getXmlPath().endsWith("notionalAmount", "id"))
				.filter(m -> id.equals(String.valueOf(m.getXmlValue())))
				.findFirst();
	}

	private Path toNotionalCurrencyPath(Mapping mapping) {
		return mapping.getXmlPath().getParent().append(Path.parse("currency"));
	}

	private Optional<String> findXmlValue(Path xmlPath) {
		return getMappings().stream()
				.filter(m -> m.getXmlPath().fullStartMatches(xmlPath))
				.map(Mapping::getXmlValue)
				.map(Object::toString)
				.distinct()
				.findFirst();
	}
}
