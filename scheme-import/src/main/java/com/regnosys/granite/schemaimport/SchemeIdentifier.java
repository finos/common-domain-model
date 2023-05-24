package com.regnosys.granite.schemaimport;

import java.util.Objects;

public class SchemeIdentifier {

    private final String bodyOrganisation;
    private final String corpusScheme;

    public SchemeIdentifier(String bodyOrganisation, String corpusScheme) {
        this.bodyOrganisation = bodyOrganisation;
        this.corpusScheme = corpusScheme;
    }

    public String getBodyOrganisation() {
        return bodyOrganisation;
    }

    public String getCorpusScheme() {
        return corpusScheme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchemeIdentifier that = (SchemeIdentifier) o;
        return Objects.equals(bodyOrganisation, that.bodyOrganisation) && Objects.equals(corpusScheme, that.corpusScheme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bodyOrganisation, corpusScheme);
    }
}
