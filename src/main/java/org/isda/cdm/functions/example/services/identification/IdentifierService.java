package org.isda.cdm.functions.example.services.identification;

import com.rosetta.model.lib.RosettaModelObject;
import org.isda.cdm.AssignedIdentifier;
import org.isda.cdm.Identifier;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.ReferenceWithMetaParty;

import java.util.TreeSet;

/**
 * An example id service that
 */
public class IdentifierService {

    private final TreeSet<IdentifierRecord> data = new TreeSet<>();

    public Identifier next(String issuer, Class<? extends RosettaModelObject> type) {
        return data.stream().filter(r -> r.issuer.equalsIgnoreCase(issuer) && r.type.equals(type))
                .max(IdentifierRecord::compareTo)
                .map(IdentifierRecord::incrementVersion)
                .orElse(new IdentifierRecord(issuer, type, 1, 1).toIdentifier());
    }

    public Identifier next(String issuer, Class<? extends RosettaModelObject> type, int typeIndex) {
        return data.stream().filter(r -> r.issuer.equalsIgnoreCase(issuer) && r.type.equals(type) && r.typeIndex == typeIndex)
                .max(IdentifierRecord::compareTo)
                .map(IdentifierRecord::incrementVersion)
                .orElse(new IdentifierRecord(issuer, type, 1, 1).toIdentifier());
    }

    private static class IdentifierRecord implements Comparable<IdentifierRecord> {

        private final String issuer;
        private final Class<? extends RosettaModelObject> type;
        private final int typeIndex;
        private final int version;

        private IdentifierRecord(String issuer, Class<? extends RosettaModelObject> type, int typeIndex, int version) {
            this.issuer = issuer;
            this.type = type;
            this.typeIndex = typeIndex;
            this.version = version;
        }

        private Identifier toIdentifier() {
            return Identifier.builder()
                    .setIssuer(FieldWithMetaString.builder()
                            .setValue(issuer)
                            .build())
                    .setIssuerReferenceBuilder(ReferenceWithMetaParty.builder()
                            .setExternalReference(issuer))
                    .addAssignedIdentifierBuilder(AssignedIdentifier.builder()
                            .setIdentifier(FieldWithMetaString.builder()
                                    .setValue(type.getSimpleName() + typeIndex).build())
                            .setVersion(version))
                    .build();
        }

        private Identifier incrementType() {
            return new IdentifierRecord(issuer, type, typeIndex + 1, 1).toIdentifier();
        }

        private Identifier incrementVersion() {
            return new IdentifierRecord(issuer, type, typeIndex, version + 1).toIdentifier();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            IdentifierRecord that = (IdentifierRecord) o;

            if (typeIndex != that.typeIndex) return false;
            if (version != that.version) return false;
            if (issuer != null ? !issuer.equals(that.issuer) : that.issuer != null) return false;
            return type != null ? type.equals(that.type) : that.type == null;

        }

        @Override
        public int hashCode() {
            int result = issuer != null ? issuer.hashCode() : 0;
            result = 31 * result + (type != null ? type.hashCode() : 0);
            result = 31 * result + typeIndex;
            result = 31 * result + version;
            return result;
        }

        @Override
        public int compareTo(IdentifierRecord o) {
            return (issuer + type.getSimpleName() + typeIndex + version).compareToIgnoreCase(
                    o.issuer + o.type.getSimpleName() + o.typeIndex + o.version);
        }
    }
}
