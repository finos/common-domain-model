package cdm.product.asset.processor;

import cdm.base.staticdata.party.AncillaryRoleEnum;
import cdm.legalagreement.contract.processor.PartyMappingHelper;
import cdm.product.asset.DividendReturnTerms;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class ExtraordinaryDividendsPartyMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExtraordinaryDividendsPartyMappingProcessor.class);

	public ExtraordinaryDividendsPartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		PartyMappingHelper.getInstanceOrThrow(getContext())
				.setAncillaryRoleEnum(getModelPath(),
						synonymPath.addElement("href"),
						((DividendReturnTerms.DividendReturnTermsBuilder) parent)::setExtraordinaryDividendsParty,
						AncillaryRoleEnum.EXTRAORDINARY_DIVIDENDS_PARTY);
	}
}
