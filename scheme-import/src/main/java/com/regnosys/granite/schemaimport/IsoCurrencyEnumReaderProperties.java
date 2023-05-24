package com.regnosys.granite.schemaimport;

import java.net.URL;

public class IsoCurrencyEnumReaderProperties {
    private final URL schemaLocationForEnum;

    public IsoCurrencyEnumReaderProperties(URL schemaLocationForEnum) {

        this.schemaLocationForEnum = schemaLocationForEnum;
    }

    public URL getSchemaLocationForEnum() {
        return schemaLocationForEnum;
    }

}
