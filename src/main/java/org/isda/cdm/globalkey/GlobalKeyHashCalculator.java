package org.isda.cdm.globalkey;

import java.util.Map;
import java.util.stream.Collectors;

import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep.KeyPostProcessReport;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;

public class GlobalKeyHashCalculator {
	private GlobalKeyProcessStep processor;

	public GlobalKeyHashCalculator() {
		processor = new GlobalKeyProcessStep(()->new NonNullHashCollector());
	}
	
	public Map<String, RosettaModelObject> computeHashes(RosettaModelObject object) {
		return computeHashes(object.getClass(), object);
	}
	
	private <T extends RosettaModelObject> Map<String, RosettaModelObject> computeHashes(Class<T> clazz, RosettaModelObject object) {
		RosettaModelObjectBuilder builder = object.toBuilder();
		KeyPostProcessReport report = processor.runProcessStep(clazz, builder);
		
		return report.getKeyMap().entrySet().stream().collect(Collectors.toMap(e->e.getValue().getMeta().getGlobalKey(),
				e->((RosettaModelObjectBuilder)e.getValue()).build()));
	}
}
