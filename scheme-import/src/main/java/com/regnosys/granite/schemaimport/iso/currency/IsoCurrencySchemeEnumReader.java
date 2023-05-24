package com.regnosys.granite.schemaimport.iso.currency;

import com.regnosys.granite.schemaimport.SchemeEnumReader;
import com.regnosys.granite.schemaimport.SchemeIdentifier;
import com.regnosys.rosetta.rosetta.RosettaEnumValue;
import org.iso.currency.ISO4217;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.SECONDS;

public class IsoCurrencySchemeEnumReader implements SchemeEnumReader<IsoCurrencyEnumReaderProperties> {
    private final HttpClient httpClient;

    public IsoCurrencySchemeEnumReader() {
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public List<RosettaEnumValue> generateEnumFromScheme(IsoCurrencyEnumReaderProperties properties) {
        try {
            ISO4217 iso4217 = parseSchemaFile(properties.getSchemaLocationForEnum());
            return List.of();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ISO4217 parseSchemaFile(URL schemaLocationForEnum) throws JAXBException, IOException, XMLStreamException, URISyntaxException, InterruptedException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ISO4217.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        String xmlContents = getXmlContents(schemaLocationForEnum);
        XMLStreamReader reader = inputFactory.createXMLStreamReader(new StringReader(Objects.requireNonNull(xmlContents)));
        JAXBElement<ISO4217> doc = unmarshaller.unmarshal(reader, ISO4217.class);
        return doc.getValue();
    }

    private String getXmlContents(URL schemaLocationForEnum) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(schemaLocationForEnum.toURI())
                .timeout(Duration.of(10, SECONDS))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Response from xml get: " + response.statusCode());
        }
        return response.body();
    }

    @Override
    public SchemeIdentifier applicableToScheme() {
        return new SchemeIdentifier("ISO", "ISO_4217_Currency_Scheme");
    }
}
