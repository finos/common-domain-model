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
	
	public <T extends RosettaModelObject> Map<String, RosettaModelObject> computeHashes(T object) {
		return computeHashes((Class<T>)object.getClass(), object);
	}
	
	private <T extends RosettaModelObject> Map<String, RosettaModelObject> computeHashes(Class<T> clazz, T object) {
		KeyPostProcessReport report = processor.runProcessStep(clazz, object);
		
		return report.getKeyMap().entrySet().stream().collect(Collectors.toMap(e->e.getValue().getMeta().getGlobalKey(),
				e->((RosettaModelObjectBuilder)e.getValue()).build()));
	}
}
