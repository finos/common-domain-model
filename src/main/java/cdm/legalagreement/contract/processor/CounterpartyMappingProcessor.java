package cdm.legalagreement.contract.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

/**
 * Counterparty mapping processor.
 *
 * Mapper must be in same namespace as the type it is used for.
 */
@SuppressWarnings("unused")
	public class CounterpartyMappingProcessor extends org.isda.cdm.processor.CounterpartyMappingProcessor {

	public CounterpartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}
}
