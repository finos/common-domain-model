package com.regnosys.granite.schemaimport.fpml;

import java.util.Objects;

public class FpMLSchemeEnumReaderProperties {
    private final String codingSchemeRelativePath;
    private final String schemeLocation;

    public FpMLSchemeEnumReaderProperties(String codingSchemeRelativePath, String schemeLocation) {
        this.codingSchemeRelativePath = codingSchemeRelativePath;
        this.schemeLocation = schemeLocation;
    }

    public String getCodingSchemeRelativePath() {
        return codingSchemeRelativePath;
    }

    public String getSchemeLocation() {
        return schemeLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FpMLSchemeEnumReaderProperties that = (FpMLSchemeEnumReaderProperties) o;
        return Objects.equals(codingSchemeRelativePath, that.codingSchemeRelativePath) && Objects.equals(schemeLocation, that.schemeLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codingSchemeRelativePath, schemeLocation);
    }
}
