package cdm.observable.event.processor;

import cdm.legaldocumentation.contract.processor.PartyMappingHelper;
import cdm.observable.event.CreditEventNotice;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.Collection;
import java.util.List;

public class NotifyingPartyMappingProcessor extends MappingProcessor {

	public NotifyingPartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, Collection<? extends T> instance, RosettaModelObjectBuilder parent) {
		PartyMappingHelper.getInstance(getContext())
				.ifPresent(helper ->
						helper.setCounterpartyRoleEnum(getModelPath(),
								synonymPath,
								((CreditEventNotice.CreditEventNoticeBuilder) parent)::addNotifyingParty));
	}
}

