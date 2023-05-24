package com.regnosys.granite.schemaimport;

import com.regnosys.rosetta.rosetta.RosettaEnumValue;

import java.util.List;

public interface SchemeEnumReader<T> {
    List<RosettaEnumValue> generateEnumFromScheme(T properties);

    SchemeIdentifier applicableToScheme();
}
