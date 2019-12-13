package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.AssetIdentifier.AssetIdentifierBuilder;

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
									findValue(notionalCurrencyPath).ifPresent(currency -> {
										AssetIdentifierBuilder assetIdentifierBuilder = (AssetIdentifierBuilder) builder;
										assetIdentifierBuilder.getOrCreateCurrency().setValue(currency);
									});
								}));
	}

	@Override
	protected void map(List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
	}


	private Path toNotionalCurrencyPath(Mapping mapping) {
		return mapping.getXmlPath().getParent().append(Path.parse("currency"));
	}

	private Optional<String> findValue(Path path) {
		return getMappings().stream()
				.filter(m -> m.getXmlPath().fullStartMatches(path))
				.map(Mapping::getXmlValue)
				.map(Object::toString)
				.distinct()
				.findFirst();
	}

	private Optional<Mapping> findHrefMapping() {
		return getMappings().stream()
				.filter(m -> m.getXmlPath().endsWith("relativeNotionalAmount", "href"))
				.findFirst();
	}

	private Optional<String> findHrefValue() {
		return getMappings().stream()
				.filter(m -> m.getXmlPath().endsWith("relativeNotionalAmount", "href"))
				.map(Mapping::getXmlValue)
				.map(Object::toString)
				.findFirst();
	}

	private Optional<Mapping> findMappingForId(String id) {
		return getMappings().stream()
				.filter(m -> m.getXmlPath().endsWith("notionalAmount", "id"))
				.filter(m -> id.equals(String.valueOf(m.getXmlValue())))
				.findFirst();
	}
}
