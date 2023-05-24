package com.regnosys.granite.schemaimport.iso.currency;

import com.regnosys.granite.schemaimport.SchemeEnumReader;
import com.regnosys.granite.schemaimport.SchemeIdentifier;
import com.regnosys.rosetta.rosetta.RosettaEnumValue;
import com.regnosys.rosetta.rosetta.RosettaFactory;
import com.regnosys.rosetta.rosetta.impl.RosettaFactoryImpl;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.iso.currency.ISO4217;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

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
            return transformToEnums(iso4217);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<RosettaEnumValue> transformToEnums(ISO4217 iso4217) {
        return iso4217.getCcyTbl().getCcyNtry().stream()
                .map(ccyNtry -> new ImmutablePair<>(ccyNtry.getCcy(), ccyNtry.getCcyNm().getValue()))
                .distinct()
                .map(pair -> createEnumValue(pair.getLeft(), pair.getRight()))
                .collect(Collectors.toList());
    }

    private RosettaEnumValue createEnumValue(String currencyCode, String currencyName) {
        RosettaFactory factory = RosettaFactoryImpl.eINSTANCE;
        RosettaEnumValue ev = factory.createRosettaEnumValue();
        ev.setName(currencyCode);
        ev.setDisplay(currencyCode);
        ev.setDefinition(currencyName);
        return ev;
    }

    private ISO4217 parseSchemaFile(URL schemaLocationForEnum) throws JAXBException, IOException, URISyntaxException, InterruptedException {
        String xmlContents = getXmlContents(schemaLocationForEnum);
        JAXBContext jaxbContext = JAXBContext.newInstance(ISO4217.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        InputStream inputStream = new ByteArrayInputStream(removeLineBreaks(xmlContents).getBytes());
        return (ISO4217) jaxbUnmarshaller.unmarshal(inputStream);
    }

    private String removeLineBreaks(String response) {
        return response
                .replace("\n", "")
                .replace("\t", "");
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
