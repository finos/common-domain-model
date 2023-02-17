package com.regnosys.granite.schemaimport;

import com.google.common.collect.Lists;
import com.regnosys.rosetta.common.util.UrlUtils;
import com.regnosys.rosetta.rosetta.RosettaEnumValue;
import com.regnosys.rosetta.rosetta.RosettaFactory;
import com.regnosys.rosetta.rosetta.impl.RosettaFactoryImpl;
import org.eclipse.xtext.util.Pair;
import org.eclipse.xtext.util.Tuples;
import org.genericode.xml._2004.ns.codelist._0.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class FpMLSchemeEnumReader implements SchemeEnumReader {

	private static final Logger LOGGER = LoggerFactory.getLogger(FpMLSchemeEnumReader.class);

	public static final String VERSION = "Version";
	public static final String CODE = "Code";
	public static final String DESCRIPTION = "Description";
	public static final String CODING_SCHEME = "http://www.fpml.org/coding-scheme/";

    /**
     *
     * @param codingSchemeUrl this is the url of the set-of-schemes-n-n.xml published by FpML
     * @param codingSchemeRelativePath
     * @param schemeLocation
     */
	@Override
	public List<RosettaEnumValue> generateEnumFromScheme(URL codingSchemeUrl, String codingSchemeRelativePath, String schemeLocation) {
        try {
            Map<String, CodeListDocument> stringCodeListDocumentMap = readSchemaFiles(codingSchemeUrl, codingSchemeRelativePath);
            CodeListDocument codeListDocument = stringCodeListDocumentMap.get(schemeLocation);
            if (codeListDocument != null) {
                Pair<List<RosettaEnumValue>, String> transform = transform(codeListDocument);
                return transform.getFirst();
            } else {
                LOGGER.warn("No document found for schema location {}", schemeLocation);
            }
        } catch (JAXBException | IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
		return new ArrayList<>();
	}

	private Pair<List<RosettaEnumValue>, String> transform(CodeListDocument doc) {
		int nameIndex = 0;
		int descriptionIndex = 0;

		String versionString = null;

		List<JAXBElement<String>> content = doc.getIdentification().getContent();
		for (JAXBElement<String> element : content) {
			QName name = element.getName();
			if (name.getLocalPart().equals(VERSION)) {
				versionString = element.getValue();
			}
		}

		List<Column> cols = doc.getColumnSet().getColumnChoice().stream().filter(Column.class::isInstance).map(Column.class::cast).collect(Collectors.toList());
		for (int i = 0; i < cols.size(); i++) {
			Column c = cols.get(i);
			if (c.getId().equals(CODE)) nameIndex = i;
			if (c.getId().equals(DESCRIPTION)) descriptionIndex = i;
		}

		List<RosettaEnumValue> result = new ArrayList<>();
		for (Row row : Lists.reverse(doc.getSimpleCodeList().getRow())) {
			result.add(createEnumValue(result, row, nameIndex, descriptionIndex));
		}
		return Tuples.create(Lists.reverse(result), versionString);
	}

	private RosettaEnumValue createEnumValue(List<RosettaEnumValue> result, Row r, int nameIndex, int descriptionIndex) {
		RosettaFactory factory = RosettaFactoryImpl.eINSTANCE;
		RosettaEnumValue ev = factory.createRosettaEnumValue();
		String name = r.getValue().get(nameIndex).getSimpleValue().getValue();
		long duplicateCount = result.stream()
			.filter(enumValue -> enumValue.getName().equalsIgnoreCase(encode(name)))
			.count();

		String encode = encode(name) + (duplicateCount > 0 ? "_" + duplicateCount : "");
		ev.setName(encode);
		if (!name.equals(encode)) {
			ev.setDisplay(name);
		}

		ev.setDefinition(r.getValue().get(descriptionIndex)
			.getSimpleValue().getValue()
				.replace("\n", " ")
				.replace("\t", "")
				.replace("\"", "'")
				.replace("“", "'")
				.replace("”", "'"));
		return ev;
	}

	private String encode(String name) {
		String replaced = name.replaceAll("[-/\"&. ()#':=+]", "_");
		if (replaced.matches("^[0-9].*")) {
			replaced = "_" + replaced;
		}
		return replaced;
	}

	private Map<String, CodeListDocument> readSchemaFiles(URL codingSchemeUrl, String codingSchemeRelativePath) throws JAXBException, IOException, XMLStreamException {
		JAXBContext jaxbContext = JAXBContext.newInstance(CodeListDocument.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLStreamReader reader = inputFactory.createXMLStreamReader(UrlUtils.openURL(codingSchemeUrl));
		JAXBElement<CodeListSetDocument> doc = unmarshaller.unmarshal(reader, CodeListSetDocument.class);
		CodeListSetDocument schemeList = doc.getValue();

		return schemeList.getCodeListRef().stream()
			.filter(r -> {
				if (r.getLocationUri().get(0).isEmpty()) {
					LOGGER.warn("No location URI for resource: " + r.getCanonicalUri());
					return false;
				}
				return true;
			})
			.map(r -> loadCodeListDocumentEntry(codingSchemeRelativePath, r, jaxbContext, inputFactory))
			.filter(Objects::nonNull)
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private Map.Entry<String, CodeListDocument> loadCodeListDocumentEntry(String codingSchemeRelativePath, CodeListRef codeListRef, JAXBContext jaxbContext, XMLInputFactory inputFactory) {
		List<String> locationUri = codeListRef.getLocationUri();
		URL localUrl = makeUriLocal(codingSchemeRelativePath, codeListRef);

		if (localUrl != null) {
			CodeListDocument codeListDocument = readUrl(jaxbContext, inputFactory, localUrl);
			if (codeListDocument != null) {
				return Map.entry(codeListRef.getCanonicalUri(), codeListDocument);
			}
		} else {
			LOGGER.warn("The resource: " + locationUri + " cannot be loaded");
		}
		return null;
	}

	private URL makeUriLocal(String codingSchemeRelativePath, CodeListRef codeListRef) {
		List<String> locationUris = codeListRef.getLocationUri();

		if (locationUris.size() == 1) {
			String path = locationUris.get(0).replace(CODING_SCHEME, codingSchemeRelativePath);
			String localPath = path.endsWith(".xml") ? path : path + ".xml";

			return getClass().getClassLoader().getResource(localPath);
		} else {
			LOGGER.warn("There were multiple location uris for scheme uri '{}': {}", codeListRef.getCanonicalUri(), locationUris);
		}
		return null;
	}

	private  CodeListDocument readUrl(JAXBContext jaxbContext, XMLInputFactory inputFactory, URL url) {
		try {
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			XMLStreamReader reader = inputFactory.createXMLStreamReader(UrlUtils.openURL(url));
			JAXBElement<CodeListDocument> codeListDocumentJAXBElement = unmarshaller.unmarshal(reader, CodeListDocument.class);
			return codeListDocumentJAXBElement.getValue();
		} catch (XMLStreamException | IOException | JAXBException e) {
			LOGGER.warn("Error reading scheme file " + url, e);
		}
		return null;
	}
}
