package cdm.base.staticdata.party.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.processor.CounterpartyMappingHelper;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class CmeCounterpartyEnumMappingProcessor extends MappingProcessor {

	private final Function<String, Optional<String>> externalRefTranslator;

	public CmeCounterpartyEnumMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
		this.externalRefTranslator = new TradeSideToPartyMappingHelper(mappingContext.getMappings());
	}

	@Override
	public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		CounterpartyMappingHelper.getInstance(getContext())
				.ifPresent(h -> h.setCounterpartyEnum(parent, getModelPath(), synonymPath, externalRefTranslator));
	}
}