package com.regnosys.granite.projector.fpml_5_10;

import com.google.common.io.Resources;
import org.fpml.fpml_5.confirmation.DataDocument;
import org.fpml.fpml_5.confirmation.Party;
import org.fpml.fpml_5.confirmation.Trade;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Disabled
public class Fpml510RecordKeepingSampleWriter {

	private static final Logger LOGGER = LoggerFactory.getLogger(Fpml510RecordKeepingSampleWriter.class);

	private static final String FPML_5_CONFIRMATION_PACKAGE = "org.fpml.fpml_5.confirmation";
	private static final String XML_DECLARATION_PROPERTY = "com.sun.xml.bind.xmlDeclaration";

	@Test
	void generate() throws URISyntaxException, IOException {
		Path baseFolder = Paths.get(Resources.getResource("cdm-sample-files/fpml-5-10/products").toURI());

		try (Stream<Path> walk = Files.walk(baseFolder)) {
			walk.filter(Files::isRegularFile)
				.filter(this::isXmlFile)
				.forEach(inFile -> {
					System.out.println(inFile);
					try {
						DataDocument dataDocument = Fpml510Marshaller.unmarshal(DataDocument.class, toResourcesPath(inFile));
						String fileContent = writeFileContent(dataDocument);
						writeFile(toOutFile(inFile), fileContent);
					} catch (JAXBException e) {
						e.printStackTrace();
					}
				});
		}
	}

	private boolean isXmlFile(Path inFile) {
		return inFile.getFileName().toString().endsWith(".xml");
	}

	private Path toOutFile(Path inFile) {
		return Paths.get(inFile.toString()
			.replace("cdm-sample-files", "generated-sample-files"));
	}

	private String toResourcesPath(Path inFile) {
		return inFile.toString()
			.substring(inFile.toString().lastIndexOf("cdm-sample-files"));
	}

	private String writeFileContent(DataDocument dataDocument) throws JAXBException {
		String fileContent = marshal(Trade.class, dataDocument.getTrade().get(0), "trade");
		String partyId1 = null;
		String partyId2 = null;
		for (var party : dataDocument.getParty()) {
			if (partyId1 == null)
				partyId1 = party.getId();
			else if (partyId2 == null)
				partyId2 = party.getId();
			fileContent += marshal(Party.class, party, "party");
		}
		fileContent = fileContent.replace("<tradeDate>", "<partyTradeInformation>\n<partyReference href=\"bbbbbbbb\"/>\n</partyTradeInformation>\n<partyTradeInformation>\n<partyReference href=\"cccccccc\"/>\n</partyTradeInformation>\n<tradeDate>")
			.replace("bbbbbbbb", partyId1)
			.replace("cccccccc", partyId2);
		return "<aaaaaaa>\n" + fileContent + "\n</nonpublicExecutionReport>";
	}

	private static <T> String marshal(Class<T> clazz, T o, String name) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(FPML_5_CONFIRMATION_PACKAGE);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.setProperty(XML_DECLARATION_PROPERTY, Boolean.FALSE);

		JAXBElement<T> root = new JAXBElement<>(new QName(name), clazz, null, o);

		StringWriter writer = new StringWriter();
		jaxbMarshaller.marshal(root, writer);
		return writer.toString()
			.replace("ns3:", "")
			.replace(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns3=\"http://www.fpml.org/FpML-5/confirmation\"", "");
	}

	private static void writeFile(Path writePath, String json) {
		try {
			Files.createDirectories(writePath.getParent());
			Files.write(writePath, json.getBytes());
			LOGGER.info("Wrote output to {}", writePath);
		} catch (IOException e) {
			LOGGER.error("Failed to write output to {}", writePath, e);
		}
	}

}
