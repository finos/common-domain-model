package org.isda.cdm.rosettakey;

import java.util.Map;
import java.util.stream.Collectors;

import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.regnosys.rosetta.common.hashing.RosettaKeyProcessStep;
import com.regnosys.rosetta.common.hashing.RosettaKeyProcessStep.KeyPostProcessReport;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;

public class RosettaHashCalculator {
	private RosettaKeyProcessStep processor;

	public RosettaHashCalculator() {
		processor = new RosettaKeyProcessStep(()->new NonNullHashCollector());
	}
	
	public Map<String, RosettaModelObject> computeHashes(RosettaModelObject object) {
		return computeHashes(object.getClass(), object);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends RosettaModelObject> Map<String, RosettaModelObject> computeHashes(Class<T> clazz, RosettaModelObject object) {
		RosettaModelObjectBuilder<T> builder = (RosettaModelObjectBuilder<T>) object.toBuilder();
		KeyPostProcessReport<? extends T> report = processor.runProcessStep(clazz, builder);
		
		return report.getKeyMap().entrySet().stream().collect(Collectors.toMap(e->e.getKey(), e->e.getValue().build()));
	}
}
