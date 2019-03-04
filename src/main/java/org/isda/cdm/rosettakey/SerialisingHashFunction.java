package org.isda.cdm.rosettakey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.lib.postprocess.PostProcessorReport;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.lib.process.ProcessingException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SerialisingHashFunction implements PostProcessStep {

	@Override
	public Integer getPriority() {
		return Integer.MAX_VALUE;
	}

	@Override
	public String getName() {
		return "SHA-256 key calculator";
	}
	
	public String hash(RosettaModelObject object) {
		return computeHashes(object.getClass(), object);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends RosettaModelObject> String computeHashes(Class<T> clazz, RosettaModelObject object) {
		RosettaModelObjectBuilder<T> builder = (RosettaModelObjectBuilder<T>) object.toBuilder();
		StringHashProstProcessReport<? extends T> report = runProcessStep(clazz, builder);
		
		return report.getResultHash();
	}

	@Override
	public <T extends RosettaModelObject> StringHashProstProcessReport<? extends T> runProcessStep(Class<T> topClass,
			RosettaModelObjectBuilder<? extends T> builder) {
		T built = builder.build();
		try {
            byte[] bytes = RosettaObjectMapper.getDefaultRosettaObjectMapper().writeValueAsBytes(built);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return new StringHashProstProcessReport<>(Base64.getEncoder().encodeToString(digest.digest(bytes)), builder);
        } catch (JsonProcessingException | NoSuchAlgorithmException e) {
            throw new ProcessingException("Unable to generate hash for object: " + built.toString(), e);
        }
	}

	class StringHashProstProcessReport<T extends RosettaModelObject> implements PostProcessorReport<T> {
		private final String resultHash;
		private final  RosettaModelObjectBuilder<T> resultObject;
		
		public String getResultHash() {
			return resultHash;
		}

		public StringHashProstProcessReport(String resultHash, RosettaModelObjectBuilder<T> resultObject) {
			super();
			this.resultHash = resultHash;
			this.resultObject = resultObject;
		}

		@Override
		public RosettaModelObjectBuilder<T> getResultObject() {
			return resultObject;
		}
		
	}

}
