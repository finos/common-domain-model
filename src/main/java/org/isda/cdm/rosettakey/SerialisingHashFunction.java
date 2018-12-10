package org.isda.cdm.rosettakey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.HashException;
import com.rosetta.model.lib.HashFunction;
import com.rosetta.model.lib.RosettaModelObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SerialisingHashFunction implements HashFunction<RosettaModelObject, String> {

    @Override
    public String hash(RosettaModelObject modelObject) {
        try {
            byte[] bytes = RosettaObjectMapper.getDefaultRosettaObjectMapper().writeValueAsBytes(modelObject);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString((digest.digest(bytes)));
        } catch (JsonProcessingException | NoSuchAlgorithmException e) {
            throw new HashException("Unable to generate hash for object: " + modelObject.toString(), e);
        }
    }

}
