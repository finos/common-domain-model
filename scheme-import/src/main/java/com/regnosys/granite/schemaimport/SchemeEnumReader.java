package com.regnosys.granite.schemaimport;

import com.regnosys.rosetta.rosetta.RosettaEnumValue;
import org.genericode.xml._2004.ns.codelist._0.CodeListDocument;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public interface SchemeEnumReader {
    List<RosettaEnumValue> generateEnumFromScheme(URL codingSchemeUrl, String codingSchemeRelativePath, String schemeLocation);
}
