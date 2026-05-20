package cdm.migration.fpml;

public enum Confidence {
    HIGH(3),
    MEDIUM(2),
    LOW(1),
    UNRESOLVED(0);

    private final int level;

    Confidence(int level) {
        this.level = level;
    }

    public boolean allows(Confidence candidate) {
        return candidate.level >= this.level;
    }
}
