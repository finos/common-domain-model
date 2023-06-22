package com.regnosys.granite.projector.fpml_5_10;

import org.fpml.fpml_5.confirmation.DataDocument;
import org.fpml.fpml_5.confirmation.Document;
import org.fpml.fpml_5.confirmation.ObjectFactory;
import org.fpml.fpml_5.confirmation.RequestClearing;

import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;
import java.io.StringWriter;

public class Fpml510Marshaller {

	private static final String FPML_5_CONFIRMATION_PACKAGE = "org.fpml.fpml_5.confirmation";
	private static final String XML_DECLARATION_PROPERTY = "com.sun.xml.bind.xmlDeclaration";

	public static <T extends Document> T unmarshal(Class<T> clazz, String path) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(FPML_5_CONFIRMATION_PACKAGE);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		StreamSource fpmlDocument = new StreamSource(Fpml510Marshaller.class.getClassLoader().getResourceAsStream(path));
		JAXBElement<T> root = jaxbUnmarshaller.unmarshal(fpmlDocument, clazz);
		return root.getValue();
	}

	public static <T extends Document> String marshal(T document) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(FPML_5_CONFIRMATION_PACKAGE);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.fpml.org/FpML-5/confirmation ../../../schemas/fpml-5-10/confirmation/fpml-main-5-10.xsd");
		jaxbMarshaller.setProperty(XML_DECLARATION_PROPERTY, Boolean.TRUE);

		JAXBElement<T> root = createDocument(document);

		StringWriter writer = new StringWriter();
		jaxbMarshaller.marshal(root, writer);
		return writer.toString();
	}

	private static <T extends Document> JAXBElement<T> createDocument(T document) {
		if (document instanceof DataDocument) {
			return (JAXBElement<T>) new ObjectFactory().createDataDocument((DataDocument) document);
		}
		if (document instanceof RequestClearing) {
			return (JAXBElement<T>) new ObjectFactory().createRequestClearing((RequestClearing) document);
		}
		else {
			throw new UnsupportedOperationException("Unknown FpML Document " + document.getClass().getSimpleName());
		}
	}
}
