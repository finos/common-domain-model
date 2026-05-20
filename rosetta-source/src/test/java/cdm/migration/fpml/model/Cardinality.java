package cdm.migration.fpml.model;

public class Cardinality {
    public final String min;
    public final String max;

    public Cardinality(String min, String max) {
        this.min = min;
        this.max = max;
    }

    public static Cardinality parse(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return new Cardinality("0", "1");
        }
        String clean = raw.trim();
        if (clean.startsWith("(") && clean.endsWith(")")) {
            clean = clean.substring(1, clean.length() - 1);
        }
        String[] parts = clean.split("\\.\\.");
        if (parts.length == 2) {
            return new Cardinality(parts[0].trim(), parts[1].trim());
        }
        return new Cardinality("0", "1");
    }

    public boolean isCompatible(Cardinality other) {
        if (other == null) {
            return false;
        }
        return min.equals(other.min) && max.equals(other.max);
    }

    @Override
    public String toString() {
        return min + ".." + max;
    }
}
