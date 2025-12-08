package cdm.base.staticdata.party.processor;

import com.google.common.collect.MoreCollectors;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Helper class to translate TradeSide.id to TradeSide.orderer.party.id for CME Submission mapping processors.
 */
public class TradeSideToPartyMappingHelper implements Function<String, Optional<String>> {

	// Relative path between tradeSide and party.  E.g. <trade-side>.orderer.party.href
	private static final Path RELATIVE_PATH = Path.parse("orderer.party.href");

	private final List<Mapping> mappings;

	public TradeSideToPartyMappingHelper(List<Mapping> mappings) {
		this.mappings = mappings;
	}

	@Override
	public Optional<String> apply(String tradeSideId) {
		// Find tradeSide.id path, then append the relative path to get the party path.
		Stream<Path> partyPath = mappings.stream()
				.filter(m -> matches(m, tradeSideId))
				.map(Mapping::getXmlPath)
				.map(Path::getParent)
				.map(p -> p.append(RELATIVE_PATH));
		// Find the mapping object for the party path, and get the party id value
		Optional<String> newValue = partyPath.map(this::extractXmlValueFromMappings).distinct().collect(MoreCollectors.onlyElement());
		return newValue;
	}

	private Optional<String> extractXmlValueFromMappings(Path partyPath) {
		return mappings.stream()
				.filter(p -> partyPath.nameIndexMatches(p.getXmlPath()))
				.map(Mapping::getXmlValue)
				.map(String::valueOf)
				.collect(MoreCollectors.toOptional());
	}

	private boolean matches(Mapping mapping, String tradeSideId) {
		return mapping.getXmlPath().endsWith("id") && String.valueOf(mapping.getXmlValue()).equals(tradeSideId);
	}
}
