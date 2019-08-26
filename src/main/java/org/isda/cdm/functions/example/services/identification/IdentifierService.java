package org.isda.cdm.functions.example.services.identification;

import org.isda.cdm.AssignedIdentifier;
import org.isda.cdm.Identifier;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.ReferenceWithMetaParty;

import com.google.inject.Singleton;

import java.util.TreeSet;

/**
 * An example id service that
 */
@Singleton
public class IdentifierService {

    private final TreeSet<IdentifierRecord> data = new TreeSet<>();

    public void put(Identifier id) {
        data.add(IdentifierRecord.create(id));
    }

    public Identifier nextType(String issuer, String type) {
        return data.stream().filter(r -> r.issuer.equalsIgnoreCase(issuer) && r.type.equals(type))
                .max(IdentifierRecord::compareTo)
                .map(IdentifierRecord::incrementType)
                .orElse(new IdentifierRecord(issuer, type, 1, 1)).toIdentifier();
    }

    public Identifier nextVersion(String issuer, String type) {
        return data.stream().filter(r -> r.issuer.equalsIgnoreCase(issuer) && r.type.equals(type))
                .max(IdentifierRecord::compareTo)
                .map(IdentifierRecord::incrementVersion)
                .orElse(new IdentifierRecord(issuer, type, 1, 1)).toIdentifier();
    }

    public Identifier nextType(Identifier id) {
        IdentifierRecord identifierRecord = IdentifierRecord.create(id);
        return nextType(identifierRecord.issuer, identifierRecord.type);
    }

    private static class IdentifierRecord implements Comparable<IdentifierRecord> {

        private final String issuer;
        private final String type;
        private final int typeIndex;
        private final int version;

        private IdentifierRecord(String issuer, String type, int typeIndex, int version) {
            this.issuer = issuer;
            this.type = type;
            this.typeIndex = typeIndex;
            this.version = version;
        }

        public static IdentifierRecord create(Identifier id) {
            String[] identifier = id.getAssignedIdentifier().get(0).getIdentifier().getValue().split("-");
            return new IdentifierRecord(id.getIssuer().getValue(), identifier[0], Integer.parseInt(identifier[1]), id.getAssignedIdentifier().get(0).getVersion());
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
                                    .setValue(type + "-" + typeIndex).build())
                            .setVersion(version))
                    .build();
        }

        private IdentifierRecord incrementType() {
            return new IdentifierRecord(issuer, type, typeIndex + 1, 1);
        }

        private IdentifierRecord incrementVersion() {
            return new IdentifierRecord(issuer, type, typeIndex, version + 1);
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
        public String toString() {
            return String.format("%s-%s-%s-%s", issuer, type, typeIndex, version);
        }

        @Override
        public int compareTo(IdentifierRecord o) {
            return toString().compareToIgnoreCase(o.toString());
        }
    }
}
