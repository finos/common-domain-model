package com.regnosys.granite.schemaimport.iso.currency;

import com.regnosys.granite.schemaimport.SchemeEnumReader;
import com.regnosys.granite.schemaimport.SchemeIdentifier;
import com.regnosys.rosetta.rosetta.RosettaEnumValue;

import java.util.List;

public class IsoCurrencySchemeEnumReader implements SchemeEnumReader<IsoCurrencyEnumReaderProperties> {
    @Override
    public List<RosettaEnumValue> generateEnumFromScheme(IsoCurrencyEnumReaderProperties properties) {
        return List.of();
    }

    @Override
    public SchemeIdentifier applicableToScheme() {
        return new SchemeIdentifier("ISO", "ISO_4217_Currency_Scheme");
    }
}
