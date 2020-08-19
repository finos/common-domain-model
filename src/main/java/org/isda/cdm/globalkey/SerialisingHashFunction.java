package org.isda.cdm.globalkey;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.lib.postprocess.PostProcessorReport;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.lib.process.ProcessingException;

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
	
	private <T extends RosettaModelObject> String computeHashes(Class<T> clazz, RosettaModelObject object) {
		RosettaModelObjectBuilder builder = object.toBuilder();
		StringHashPostProcessReport report = runProcessStep(clazz, builder);
		
		return report.getResultHash();
	}

	@Override
	public <T extends RosettaModelObject> StringHashPostProcessReport runProcessStep(Class<T> topClass,
			RosettaModelObjectBuilder builder) {
		RosettaModelObject built = builder.build();
		try {
            byte[] bytes = RosettaObjectMapper.getDefaultRosettaObjectMapper().writeValueAsBytes(built);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return new StringHashPostProcessReport(Base64.getEncoder().encodeToString(digest.digest(bytes)), builder);
        } catch (JsonProcessingException | NoSuchAlgorithmException e) {
            throw new ProcessingException("Unable to generate hash for object: " + built.toString(), built.getClass().getSimpleName(), getName(), RosettaPath.valueOf(""), e);
        }
	}

	class StringHashPostProcessReport implements PostProcessorReport {
		private final String resultHash;
		private final  RosettaModelObjectBuilder resultObject;
		
		public String getResultHash() {
			return resultHash;
		}

		public StringHashPostProcessReport(String resultHash, RosettaModelObjectBuilder resultObject) {
			super();
			this.resultHash = resultHash;
			this.resultObject = resultObject;
		}

		@Override
		public RosettaModelObjectBuilder getResultObject() {
			return resultObject;
		}
		
	}

}
