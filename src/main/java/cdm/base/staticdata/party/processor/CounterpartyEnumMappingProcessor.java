package cdm.base.staticdata.party.processor;

import java.util.List;
import java.util.Optional;

import org.isda.cdm.processor.CounterpartyMappingHelper;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class CounterpartyEnumMappingProcessor extends MappingProcessor {

	public CounterpartyEnumMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		CounterpartyMappingHelper.getInstance(getContext())
				.ifPresent(h -> h.setCounterpartyEnum(parent, getModelPath(), synonymPath, null));
	}
}
