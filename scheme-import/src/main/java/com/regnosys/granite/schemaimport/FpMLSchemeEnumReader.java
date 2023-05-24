package com.regnosys.granite.schemaimport;

import com.google.common.collect.Lists;
import com.regnosys.rosetta.common.util.ClassPathUtils;
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
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FpMLSchemeEnumReader implements SchemeEnumReader<FpMLSchemeEnumReaderProperties> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FpMLSchemeEnumReader.class);

	public static final String VERSION = "Version";
	public static final String CODE = "Code";
	public static final String DESCRIPTION = "Description";
	public static final String CODING_SCHEME = "http://www.fpml.org/coding-scheme/";
	private final URL codingSchemeUrl;

	public FpMLSchemeEnumReader() {
		try {
			codingSchemeUrl = getCodingSchemeUrl();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
     *
     * @param codingSchemeUrl this is the url of the set-of-schemes-n-n.xml published by FpML
     * @param codingSchemeRelativePath
     * @param schemeLocation
     */
	@Override
	public List<RosettaEnumValue> generateEnumFromScheme(FpMLSchemeEnumReaderProperties properties) {
		try {
            Map<String, CodeListDocument> stringCodeListDocumentMap = readSchemaFiles(codingSchemeUrl, properties.getCodingSchemeRelativePath());
            CodeListDocument codeListDocument = stringCodeListDocumentMap.get(properties.getSchemeLocation());
            if (codeListDocument != null) {
                Pair<List<RosettaEnumValue>, String> transform = transform(codeListDocument);
                return transform.getFirst();
            } else {
                LOGGER.warn("No document found for schema location {}", properties.getSchemeLocation());
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

	private URL getCodingSchemeUrl() throws IOException {
		String schemaPath = getLatestSetOfSchemeFile();
		return ClassPathUtils.loadFromClasspath(schemaPath, getClass().getClassLoader())
				.map(UrlUtils::toUrl)
				.findFirst().orElseThrow();
	}

	public String getLatestSetOfSchemeFile() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		Path baseFolder = ClassPathUtils
				.loadFromClasspath("coding-schemes/fpml", classLoader)
				.findFirst()
				.orElseThrow();
		assertNotNull(baseFolder);

		HashMap<String, BigDecimal> versionNumberFileName = new HashMap<>();
		try (Stream<Path> walk = Files.walk(baseFolder)) {
			walk.filter(this::isSetOfSchemeXmlFile)
					.forEach(inFile -> {
						String fileName = inFile.getFileName().toString();
						String versionNumber = fileName.substring(15, fileName.indexOf(".")).replace("-", ".");

						versionNumberFileName.put(fileName,new BigDecimal(versionNumber));
					});
		}

		BigDecimal highestVersion = new BigDecimal(0);
		String latestSetOfSchemesFile = "coding-schemes/fpml/set-of-schemes-2-2.xml";
		for (Map.Entry<String, BigDecimal> entry : versionNumberFileName.entrySet()) {
			BigDecimal value = entry.getValue();
			if (value.unscaledValue().compareTo(highestVersion.unscaledValue())>0){
				highestVersion = value;
				latestSetOfSchemesFile = "coding-schemes/fpml/" + entry.getKey();
			}

		}

		return latestSetOfSchemesFile;
	}

	private boolean isSetOfSchemeXmlFile(Path inFile) {
		return inFile.getFileName().toString().endsWith(".xml") && inFile.getFileName().toString().contains("set-of-schemes-");
	}

	@Override
	public SchemeIdentifier applicableToScheme() {
		return new SchemeIdentifier("ISDA", "FpML_Coding_Scheme");
	}
}
