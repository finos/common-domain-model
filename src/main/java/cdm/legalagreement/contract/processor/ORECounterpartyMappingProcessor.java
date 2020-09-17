package cdm.legalagreement.contract.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

public class ORECounterpartyMappingProcessor extends org.isda.cdm.processor.ORECounterpartyMappingProcessor {

	public ORECounterpartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

}
