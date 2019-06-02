package org.isda.cdm.builders;

import org.isda.cdm.AssignedIdentifier;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.ReferenceWithMetaParty;

public class IdentifierBuilder {

    public static org.isda.cdm.Identifier.IdentifierBuilder get(String issuer, String id, int version) {
        return org.isda.cdm.Identifier.builder()
                .setIssuerReferenceBuilder(ReferenceWithMetaParty.builder()
                        .setExternalReference(issuer))
                .addAssignedIdentifierBuilder(AssignedIdentifier.builder()
                        .setVersion(version)
                        .setIdentifier(FieldWithMetaString.builder().setValue(id).build()));
    }
}
