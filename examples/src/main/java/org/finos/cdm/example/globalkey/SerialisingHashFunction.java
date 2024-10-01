package org.finos.cdm.example.globalkey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.lib.postprocess.PostProcessorReport;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.path.RosettaPath;
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
	
	private <T extends RosettaModelObject> String computeHashes(Class<? extends T> clazz, T object) {
		StringHashPostProcessReport report = runProcessStep(clazz, object);
		
		return report.getResultHash();
	}

	//@Override
	public <T extends RosettaModelObject> StringHashPostProcessReport runProcessStep(Class<? extends T> topClass,
			T instance) {
		RosettaModelObject built = instance.build();
		try {
            byte[] bytes = RosettaObjectMapper.getNewRosettaObjectMapper().writeValueAsBytes(built);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return new StringHashPostProcessReport(Base64.getEncoder().encodeToString(digest.digest(bytes)), instance);
        } catch (JsonProcessingException | NoSuchAlgorithmException e) {
            throw new ProcessingException("Unable to generate hash for object: " + built.toString(), built.getClass().getSimpleName(), getName(), RosettaPath.valueOf(""), e);
        }
	}

	class StringHashPostProcessReport implements PostProcessorReport {
		private final String resultHash;
		private final  RosettaModelObject resultObject;
		
		public String getResultHash() {
			return resultHash;
		}

		public StringHashPostProcessReport(String resultHash, RosettaModelObject resultObject) {
			super();
			this.resultHash = resultHash;
			this.resultObject = resultObject;
		}

		@Override
		public RosettaModelObject getResultObject() {
			return resultObject;
		}
		
	}

}
