package com.regnosys.translate2;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapperCreator;
import fpml.flattened.Party;
import fpml.flattened.PartyId;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class FlattenedFpmlPartySerialisationTest {

    @Test
    void testMinimalFpmlPartySerialisation() throws IOException, SAXException {
        ObjectMapper objectMapper = createObjectMapper("fpml.flattened");

        String xsdPath = "schemas/fpml-5-13/confirmation/fpml-main-5-13.xsd";
        String xmlPath = "fpml/xml/minimal-party.xml";
        assertThat(isValidXml(xsdPath, xmlPath), equalTo(true));

        File file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource(xmlPath)).getFile());
        String xml = stripDataDocument(FileUtils.readFileToString(file, "UTF-8"));

        Party party = objectMapper.readValue(xml, Party.class);

        assertThat(party.getId(), equalTo("party1"));
        PartyId partyId = party.getPartyId().get(0);
        assertThat(partyId.getValue(), equalTo("549300VBWWV6BYQOWM67"));
        assertThat(partyId.getPartyIdScheme(), equalTo("http://www.fpml.org/coding-scheme/external/iso17442"));
        assertThat(party.getPartyName().getValue(), equalTo("PARTYA"));
    }

    public boolean isValidXml(String xsdPath, String xmlPath) throws IOException, SAXException {
        Validator validator = initValidator(xsdPath);
        try {
            validator.validate(new StreamSource(getFile(xmlPath)));
            return true;
        } catch (SAXException e) {
            return false;
        }
    }

    private Validator initValidator(String xsdPath) throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Source schemaFile = new StreamSource(getFile(xsdPath));
        Schema schema = factory.newSchema(schemaFile);
        return schema.newValidator();
    }

    private File getFile(String location) {
        return new File(getClass().getClassLoader().getResource(location).getFile());
    }

    private String stripDataDocument(String xml) {
        return xml.replaceAll("(?s)<dataDocument.+?>(.*)</dataDocument>", "$1");
    }

    private static ObjectMapper createObjectMapper(String packageName) throws IOException {
        String formatted = String.format("               {\n" +
                "                  \"%s.Party\": {\n" +
                "                    \"xmlRootElementName\" : \"party\",\n" +
                "                    \"attributes\": {\n" +
                "                      \"id\": {\n" +
                "                        \"xmlName\": \"id\",\n" +
                "                        \"xmlRepresentation\": \"ATTRIBUTE\"\n" +
                "                      }\n" +
                "                    }\n" +
                "                  },\n" +
                "                  \"%s.PartyId\": {\n" +
                "                    \"attributes\": {\n" +
                "                      \"value\": {\n" +
                "                        \"xmlRepresentation\": \"VALUE\"\n" +
                "                      },\n" +
                "                      \"partyIdScheme\": {\n" +
                "                        \"xmlName\": \"partyIdScheme\",\n" +
                "                        \"xmlRepresentation\": \"ATTRIBUTE\"\n" +
                "                      }\n" +
                "                    }\n" +
                "                  },\n" +
                "                  \"%s.PartyName\": {\n" +
                "                    \"attributes\": {\n" +
                "                      \"value\": {\n" +
                "                        \"xmlRepresentation\": \"VALUE\"\n" +
                "                      },\n" +
                "                      \"partyNameScheme\": {\n" +
                "                        \"xmlName\": \"partyNameScheme\",\n" +
                "                        \"xmlRepresentation\": \"ATTRIBUTE\"\n" +
                "                      }\n" +
                "                    }\n" +
                "                  }\n" +
                "                }", packageName, packageName, packageName, packageName);

        RosettaObjectMapperCreator rosettaObjectMapperCreator = RosettaObjectMapperCreator.forXML(new ByteArrayInputStream(formatted.getBytes()));
        return rosettaObjectMapperCreator.create();
    }
}
