package cdm.base.staticdata.party.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.processor.CounterpartyMappingHelper;

import java.util.List;
import java.util.Optional;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class CounterpartyEnumMappingProcessor extends MappingProcessor {

	public CounterpartyEnumMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		CounterpartyMappingHelper.getHelper(getContext())
				.ifPresent(h -> h.setCounterpartyEnum(parent, getModelPath(), synonymPath));
	}
}
