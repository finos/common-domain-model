package cdm.event.common.processor;

import cdm.legaldocumentation.contract.processor.PartyMappingProcessor;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.updateMappingSuccess;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class NovationPartyMappingProcessor extends PartyMappingProcessor {

	public NovationPartyMappingProcessor(RosettaPath rosettaPath, List<Path> synonymPaths, MappingContext context) {
		super(rosettaPath, synonymPaths, context, new NovationPartyReferenceTranslator(rosettaPath, context.getMappings()));
	}

	private static class NovationPartyReferenceTranslator implements Function<String, Optional<String>> {

		private static final Logger LOGGER = LoggerFactory.getLogger(NovationPartyReferenceTranslator.class);

		private final Path modelPath;
		private final List<Mapping> mappings;
		private final boolean isContractFormationAfterTrade;
		private final Map<String, String> cache = new HashMap<>();

		NovationPartyReferenceTranslator(RosettaPath rosettaPath, List<Mapping> mappings) {
			this.modelPath = PathUtils.toPath(rosettaPath);
			this.mappings = mappings;
			this.isContractFormationAfterTrade =
					Path.parse("WorkflowStep.businessEvent.primitives.contractFormation.after").nameStartMatches(modelPath);
		}

		@Override
		public Optional<String> apply(String party) {
			return isContractFormationAfterTrade ?
					Optional.ofNullable(cache.computeIfAbsent(party, this::translate)) : Optional.empty();
		}

		private String translate(String party) {
			Optional<Mapping> transferor = getNonNullMapping(mappings, Path.parse("*.novation.transferor.href", true));
			Optional<Mapping> transferee = getNonNullMapping(mappings, Path.parse("*.novation.transferee.href", true));
			if (transferor.isPresent() && transferee.isPresent() && transferor.get().getXmlValue().equals(party)) {
				updateMappingSuccess(transferee.get(), modelPath);
				updateMappingSuccess(transferor.get(), modelPath);
				String translatedParty = (String) transferee.get().getXmlValue();
				LOGGER.info("Translated party reference {} to {}", party, translatedParty);
				return translatedParty;
			}
			return null;
		}

		private static Optional<Mapping> getNonNullMapping(List<Mapping> mappings, Path synonymPath) {
			return mappings.stream()
					.filter(m -> synonymPath.nameStartMatches(m.getXmlPath(), true))
					.filter(m -> m.getXmlValue() != null)
					.findFirst();
		}
	}
}
