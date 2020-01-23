package org.isda.cdm.builders;

import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.ReferenceWithMetaParty;

import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;

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
