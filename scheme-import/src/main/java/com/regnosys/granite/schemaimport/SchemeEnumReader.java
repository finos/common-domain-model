package com.regnosys.granite.schemaimport;

import com.regnosys.rosetta.rosetta.RosettaEnumValue;

import java.net.URL;
import java.util.List;

public interface SchemeEnumReader {
    List<RosettaEnumValue> generateEnumFromScheme(URL schemaLocationForEnum);

}
