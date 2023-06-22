package com.regnosys.granite.projector.fpml_5_10;

import org.fpml.fpml_5.confirmation.DataDocument;
import org.fpml.fpml_5.confirmation.RequestClearing;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;

public class Fpml510MarshallerTest {

	private static final String RATES_SAMPLE = "cdm-sample-files/fpml-5-10/products/rates/EUR-Vanilla-uti.xml";
	private static final String LCH_SAMPLE = "available-samples/lch-samples/ClearLink-requestClearingSample.xml";

	@Test
	void shouldUnmarshalAndMarshalFpmlRatesSample() throws JAXBException {
		DataDocument dataDocument = Fpml510Marshaller.unmarshal(DataDocument.class, RATES_SAMPLE);
		String fpml = Fpml510Marshaller.marshal(dataDocument);

		assertThat(fpml, startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
			+ "<dataDocument fpmlVersion=\"5-10\" xsi:schemaLocation=\"http://www.fpml.org/FpML-5/confirmation ../../../schemas/fpml-5-10/confirmation/fpml-main-5-10.xsd\" xmlns=\"http://www.fpml.org/FpML-5/confirmation\" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
			+ "    <trade>\n"
			+ "        <tradeHeader>\n"
			+ "            <partyTradeIdentifier>\n"
			+ "                <issuer issuerIdScheme=\"http://www.fpml.org/coding-scheme/external/iso17442\">54930084UKLVMY22DS16</issuer>\n"
			+ "                <tradeId tradeIdScheme=\"http://www.fpml.org/coding-scheme/external/uti\">UITD7895394</tradeId>\n"
			+ "            </partyTradeIdentifier>\n"
			+ "            <tradeDate>2018-01-29</tradeDate>\n"
			+ "        </tradeHeader>\n"
			+ "        <swap>\n"
			+ "            <primaryAssetClass assetClassScheme=\"http://www.fpml.org/coding-scheme/asset-class-simple\">InterestRate</primaryAssetClass>\n"
			+ "            <productType productTypeScheme=\"http://www.fpml.org/coding-scheme/product-taxonomy\">InterestRate:IRSwap:FixedFloat</productType>\n"
			+ "            <productId productIdScheme=\"http://www.fpml.org/coding-scheme/product-taxonomy\">InterestRate:IRSwap:FixedFloat</productId>"));
	}

	@Test
	void shouldUnmarshalAndMarshalFpmlLchSample() throws JAXBException {
		RequestClearing requestClearing = Fpml510Marshaller.unmarshal(RequestClearing.class, LCH_SAMPLE);
		String fpml = Fpml510Marshaller.marshal(requestClearing);

		assertThat(fpml, startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
			+ "<requestClearing fpmlVersion=\"5-10\" xsi:schemaLocation=\"http://www.fpml.org/FpML-5/confirmation ../../../schemas/fpml-5-10/confirmation/fpml-main-5-10.xsd\" xmlns=\"http://www.fpml.org/FpML-5/confirmation\" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
			+ "    <header>\n"
			+ "        <messageId messageIdScheme=\"http://xsd.swapclear.com/coding-scheme/click/message-id\">585552</messageId>\n"
			+ "        <sentBy messageAddressScheme=\"http://www.lchclearnet.com/clearlink/coding-scheme/party-id\">SEF1</sentBy>\n"
			+ "        <sendTo messageAddressScheme=\"http://www.lchclearnet.com/clearlink/coding-scheme/party-id\">LCHLGB2L</sendTo>\n"
			+ "        <creationTimestamp>2019-09-13T15:07:55.449Z</creationTimestamp>\n"
			+ "    </header>\n"
			+ "    <isCorrection>false</isCorrection>\n"
			+ "    <correlationId correlationIdScheme=\"http://SEF1/correlation-id\">4511</correlationId>\n"
			+ "    <sequenceNumber>1</sequenceNumber>\n"
			+ "    <trade>\n"
			+ "        <tradeHeader>\n"
			+ "            <partyTradeIdentifier>\n"
			+ "                <tradeId tradeIdScheme=\"http://www.lchclearnet.com/clearlink/coding-scheme/trade-id\">STEL123</tradeId>\n"
			+ "                <partyReference href=\"party1\"/>\n"
			+ "            </partyTradeIdentifier>\n"));
	}
}
