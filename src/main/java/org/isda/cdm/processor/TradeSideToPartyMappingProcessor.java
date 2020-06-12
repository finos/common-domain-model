package org.isda.cdm.processor;

import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty.ReferenceWithMetaPartyBuilder;
import com.google.common.collect.MoreCollectors;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Maps from TradeSide.id to TradeSide.orderer.party.id.
 */
public class TradeSideToPartyMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(TradeSideToPartyMappingProcessor.class);

	public TradeSideToPartyMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
	}

	@Override
	public <R extends RosettaModelObject> void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		ReferenceWithMetaPartyBuilder partyReference = (ReferenceWithMetaPartyBuilder) builder;
		String tradeSideId = partyReference.getExternalReference();
		getPartyId(tradeSideId)
				.ifPresent(partyId -> {
					LOGGER.info("Mapped tradeSide.id ({}) to tradeSide.orderer.party.id ({}) at path {}", tradeSideId, partyId, getPath());
					partyReference.setExternalReference(partyId).build();
				});
	}

	private Optional<String> getPartyId(String tradeSideId) {
		// Relative path between tradeSide and party.  E.g. <trade-side>.orderer.party.href
		Path relativePath = Path.parse("orderer.party.href");
		// Find tradeSide.id path, then append the relative path to get the party path.
		Stream<Path> partyPath = getMappings().stream()
				.filter(m -> matches(m, tradeSideId))
				.map(Mapping::getXmlPath)
				.map(Path::getParent)
				.map(p -> p.append(relativePath));
		// Find the mapping object for the party path, and get the party id value
		Optional<String> newValue = partyPath.map(this::extractXmlValueFromMappings).distinct().collect(MoreCollectors.onlyElement());
		return newValue;
	}

	private Optional<String> extractXmlValueFromMappings(Path partyPath) {
		return getMappings().stream()
				.filter(p -> partyPath.fullStartMatches(p.getXmlPath()))
				.map(Mapping::getXmlValue)
				.map(String::valueOf)
				.collect(MoreCollectors.toOptional());
	}

	private boolean matches(Mapping mapping, String tradeSideId) {
		return mapping.getXmlPath().endsWith("id") && String.valueOf(mapping.getXmlValue()).equals(tradeSideId);
	}
}