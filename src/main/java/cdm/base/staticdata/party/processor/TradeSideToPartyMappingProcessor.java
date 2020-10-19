package cdm.base.staticdata.party.processor;

import static cdm.legalagreement.contract.processor.PartyMappingHelper.PRODUCT_SUB_PATH;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty.ReferenceWithMetaPartyBuilder;

/**
 * TradeSide.id to TradeSide.orderer.party.id CME Submission mapping processor.
 */
public class TradeSideToPartyMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(TradeSideToPartyMappingProcessor.class);

	private final Function<String, Optional<String>> tradeSideToPartyTranslator;

	public TradeSideToPartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
		this.tradeSideToPartyTranslator = new TradeSideToPartyMappingHelper(mappingContext.getMappings());
	}

	@Override
	public void map(Path synonymPath, Optional<RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		if (!getModelPath().containsPath(PRODUCT_SUB_PATH)) {
			ReferenceWithMetaPartyBuilder partyReference = (ReferenceWithMetaPartyBuilder) parent;
			setValueAndUpdateMappings(synonymPath.addElement("href"),
					(tradeSideId) -> tradeSideToPartyTranslator.apply(tradeSideId)
							.ifPresent(partyId -> {
								LOGGER.info("Mapped tradeSide.id ({}) to tradeSide.orderer.party.id ({}) at path {}", tradeSideId, partyId, getModelPath());
								partyReference.setExternalReference(partyId).build();
							}));
		}
	}
}