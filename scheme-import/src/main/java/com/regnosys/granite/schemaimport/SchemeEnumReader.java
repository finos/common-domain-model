package com.regnosys.granite.schemaimport;

import com.regnosys.rosetta.rosetta.RosettaEnumValue;

import java.util.List;

public interface SchemeEnumReader {

	List<RosettaEnumValue> generateEnumFromScheme(String schemeLocation);

}
