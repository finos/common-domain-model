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

	@Override
	public <T extends RosettaModelObject> PostProcessorReport<? extends T> runProcessStep(Class<T> topClass,
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
