package com.regnosys.translate2;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.regnosys.rosetta.common.postprocess.WorkflowPostProcessor;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapperCreator;
import com.rosetta.model.lib.process.PostProcessor;
import fpml.flattened.*;
import fpml.flattened.translate.TranslatePaAndAcAndReAndAcAndPaAndAcAndPaToPartyUsingFpML;
import org.apache.commons.io.FileUtils;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlattenedFpmlPartySerialisationTest {

    public static final String FPML_FLATTENED_PACKAGE = "fpml.flattened";
    private static Injector injector;

    @BeforeAll
    static void setUp() {
        Module module = Modules.override(new CdmRuntimeModule())
                .with(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(PostProcessor.class).to(WorkflowPostProcessor.class);
                    }
                });
        injector = Guice.createInjector(module);
    }

    @Test
    void testMinimalFpmlPartySerialisation() throws IOException, SAXException {
        ObjectMapper objectMapper = createObjectMapper();

        String xsdPath = "schemas/fpml-5-13/confirmation/fpml-main-5-13.xsd";
        String xmlPath = "fpml/xml/minimal-party.xml";
        assertThat(isValidXml(xsdPath, xmlPath), equalTo(true));

        String xml = loadResourceFile(xmlPath);
        DataDocument dataDocument = objectMapper.readValue(xml, DataDocument.class);

        Party party = dataDocument.getParty().get(0);

        assertThat(party.getId(), equalTo("party1"));
        PartyId partyId = party.getPartyId().get(0);
        assertThat(partyId.getValue(), equalTo("549300VBWWV6BYQOWM67"));
        assertThat(partyId.getPartyIdScheme(), equalTo("http://www.fpml.org/coding-scheme/external/iso17442"));
        assertThat(party.getPartyName().getValue(), equalTo("PARTYA"));

        TranslatePaAndAcAndReAndAcAndPaAndAcAndPaToPartyUsingFpML translateFunc = injector.getInstance(TranslatePaAndAcAndReAndAcAndPaAndAcAndPaToPartyUsingFpML.class);
        cdm.base.staticdata.party.Party cdmParty = translateFunc.evaluate(party, null, null, null, null, null, null);
        System.out.println(cdmParty);
        assertThat(cdmParty.getName().getValue(), equalTo("PARTYA"));
        assertThat(cdmParty.getPartyId().get(0).getIdentifier().getValue(), equalTo("549300VBWWV6BYQOWM67"));
    }

    @Test
    void testFullFpmlPartySerialisation() throws IOException, SAXException {
        ObjectMapper objectMapper = createObjectMapper();

        String xsdPath = "schemas/fpml-5-13/confirmation/fpml-main-5-13.xsd";
        String xmlPath = "fpml/xml/full-party-mapping-input-data.xml";
        assertThat(isValidXml(xsdPath, xmlPath), equalTo(true));

        String xml = loadResourceFile(xmlPath);
        DataDocument dataDocument = objectMapper.readValue(xml, DataDocument.class);

        Party party = dataDocument.getParty().stream().filter(p -> p.getId().equals("party3")).findFirst().orElseThrow();
        Account account = dataDocument.getAccount().stream().filter(a -> a.getId().equals("primaryAct1")).findFirst().orElseThrow();
        AccountReference payerAccountReference = AccountReference.builder().setHref("primaryAct1").build();
        PartyReference payerPartyReference = PartyReference.builder().setHref("payerPartyReferenceHrefValue").build();
        AccountReference receiverAccountReference = AccountReference.builder().setHref("someOtherAccount").build();
        PartyReference receiverPartyReference = PartyReference.builder().setHref("receiverPartyReferenceHrefValue").build();
        RelatedPerson relatedPerson = RelatedPerson.builder()
                .setPersonReference(PersonReference.builder().setHref("personReferenceHrefValue"))
                .setRole(PersonRole.builder()
                        .setPersonRoleScheme("personRoleSchemeValue")
                        .setValue("Broker"))
                .build();

        TranslatePaAndAcAndReAndAcAndPaAndAcAndPaToPartyUsingFpML translateFunc = injector.getInstance(TranslatePaAndAcAndReAndAcAndPaAndAcAndPaToPartyUsingFpML.class);
        cdm.base.staticdata.party.Party cdmParty = translateFunc.evaluate(party, account, relatedPerson, payerAccountReference, payerPartyReference, receiverAccountReference, receiverPartyReference);


        ObjectMapper jsonObjectMapper = RosettaObjectMapperCreator.forJSON().create();
        String translateResult = jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cdmParty);
        String expected = loadResourceFile("fpml/expectations/full-fpml-test.json");
        assertEquals( expected, translateResult);
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
        return new File(Objects.requireNonNull(getClass().getClassLoader().getResource(location)).getFile());
    }

    private ObjectMapper createObjectMapper() throws IOException {
        String conf = loadResourceFile("fpml/xml-conf.json");

        String formatted = conf.replace("%s", FPML_FLATTENED_PACKAGE);
        RosettaObjectMapperCreator rosettaObjectMapperCreator = RosettaObjectMapperCreator.forXML(new ByteArrayInputStream(formatted.getBytes()));
        return rosettaObjectMapperCreator.create();
    }

    private String loadResourceFile(String name) throws IOException {
        File confFile = new File(Objects.requireNonNull(getClass().getClassLoader().getResource(name)).getFile());
        return FileUtils.readFileToString(confFile, "UTF-8");
    }
}
