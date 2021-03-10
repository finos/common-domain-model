package cdm.event.common.processor;

import cdm.base.staticdata.party.PartyRole;
import cdm.base.staticdata.party.PartyRoleEnum;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.Trade;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

public class PartyRoleMappingProcessor extends MappingProcessor {

	public PartyRoleMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		setValueAndUpdateMappings(synonymPath.addElement("href"),
				(partyReference) ->
						((Trade.TradeBuilder) parent).addPartyRole(PartyRole.builder()
								.setPartyReference(ReferenceWithMetaParty.builder().setExternalReference(partyReference))
								.setRole(getPartyRoleEnum(synonymPath))));
	}

	private PartyRoleEnum getPartyRoleEnum(Path synonymPath) {
		if (synonymPath.endsWith("determiningParty")) {
			return PartyRoleEnum.DETERMINING_PARTY;
		} else if (synonymPath.endsWith("barrierDeterminationAgent")) {
			return PartyRoleEnum.BARRIER_DETERMINATION_AGENT;
		} else if (synonymPath.endsWith("hedgingParty")) {
			return PartyRoleEnum.HEDGING_PARTY;
		} else if (synonymPath.endsWith("brokerPartyReference")) {
			return PartyRoleEnum.ARRANGING_BROKER;
		} else {
			return null;
		}
	}
}