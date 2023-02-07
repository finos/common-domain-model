package cdm.base.staticdata.party.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

import static cdm.base.staticdata.party.metafields.ReferenceWithMetaAccount.ReferenceWithMetaAccountBuilder;

/**
 * Events mapping processor.
 *
 * Allow payerReceiver accountReference to be set until future work on accounts is complete.
 *
 * Mapper required due to conditional mapping issues for syntax: set when path = "cashTransfer payerReceiver".
 */
@SuppressWarnings("unused")
public class CashTransferAccountMappingProcessor extends MappingProcessor {

	public CashTransferAccountMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		if (!isCashTransferSynonymPath(synonymPath)) {
			((ReferenceWithMetaAccountBuilder) builder).setExternalReference(null);
		}
	}

	private boolean isCashTransferSynonymPath(Path synonymPath) {
		return synonymPath.getElements().size() > 3
				&& synonymPath.getParent().getParent().getLastElement().getPathName().equalsIgnoreCase("cashTransfer");
	}
}
