package cdm.legalagreement.common.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

/**
 * ISDA Create mapping processor.
 * <p>
 * Sets LegalAgreement.contractualParty
 */
@SuppressWarnings("unused")
public class ContractualPartyMappingProcessor extends org.isda.cdm.processor.ContractualPartyMappingProcessor {

	public ContractualPartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}
}