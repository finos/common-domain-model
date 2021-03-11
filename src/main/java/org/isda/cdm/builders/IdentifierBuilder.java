package org.isda.cdm.builders;

import com.rosetta.model.metafields.FieldWithMetaString;

import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;

public class IdentifierBuilder {

    public static Identifier.IdentifierBuilder get(String issuer, String id, int version) {
        return Identifier.builder()
        		
                .setIssuerReferenceBuilder(ReferenceWithMetaParty.builder()
                        .setExternalReference(issuer))
                .addAssignedIdentifierBuilder(AssignedIdentifier.builder()
                        .setVersion(version)
                        .setIdentifier(FieldWithMetaString.builder().setValue(id).build()));
    }
}
