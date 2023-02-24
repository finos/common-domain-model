package com.regnosys.granite.projector.isdacreate;

import cdm.legaldocumentation.common.LegalAgreement;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.regnosys.ingest.test.framework.ingestor.postprocess.pathduplicates.PathCollector;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionFactory;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionService;
import com.regnosys.rosetta.common.hashing.ReferenceConfig;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.util.UrlUtils;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.RosettaModelObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

import static com.regnosys.granite.projector.isdacreate.IsdaCreateIsdaCsaIm2016ProjectionMapper.*;
import static org.junit.jupiter.api.Assertions.*;

@Disabled
class IsdaCreateIsdaCsdIm2016EngLawProjectionMapperTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(IsdaCreateIsdaCsdIm2016EngLawProjectionMapperTest.class);

	private static final String INSTANCE_NAME = "target/ISDA_CREATE";

	private static IngestionService ingestionServiceEnglishLaw;

	private final ObjectMapper objectMapper = RosettaObjectMapper.getNewRosettaObjectMapper();

	private IsdaCreateIsdaCsdIm2016EngLawProjectionMapper mapper;

	@BeforeAll
	static void globalSetUp() {
		initialiseIngestionFactory();
		ingestionServiceEnglishLaw = IngestionFactory.getInstance(INSTANCE_NAME).getService("isdaCreateAll");
	}

	@BeforeEach
	void setUp() {
		mapper = new IsdaCreateIsdaCsdIm2016EngLawProjectionMapper();
	}

	@Test
	void shouldIngestLegalAgreementAndBuildIsdaCreateDocument() throws IOException {
		var url = Resources.getResource("isda-create/initial-margin-2016_englaw_test1.json");
		var legalAgreement = ingest(LegalAgreement.class, url);
		assertNotNull(legalAgreement);

		var isdaCreate = mapper.getIsdaCreate(legalAgreement);
		assertNotNull(isdaCreate);

		var document = isdaCreate.getDocument();
		assertNotNull(document);
		assertEquals("016764cc-5e05-6935-2562-0ad2d0b51285", document.getId());
		assertEquals(Integer.valueOf(2016), document.getYear());
		assertEquals("Credit Support Deed", document.getDocumentType());
		assertEquals("English", document.getGoverningLaw());
		assertEquals("2016 IM CSD (Eng Law)", document.getAbbreviation());
		assertEquals("ISDA 2016 Phase One IM Credit Support Deed (Security Interest - English Law)", document.getDescription());
		assertEquals("ISDA", document.getPublisher());
		assertTrue(document.getHasSchema());

		var answers = isdaCreate.getAnswers();
		assertNotNull(answers);

		var partyA = answers.getPartyA();
		assertNotNull(partyA);

		var accessConditions = partyA.getAccessConditions();
		assertNotNull(accessConditions);
		assertEquals(APPLICABLE, accessConditions.getPartyAIllegality());
		assertEquals(APPLICABLE, accessConditions.getPartyBIllegality());
		assertEquals(APPLICABLE, accessConditions.getPartyAForceMajeure());
		assertEquals(APPLICABLE, accessConditions.getPartyBForceMajeure());
		assertEquals(APPLICABLE, accessConditions.getPartyATaxEvent());
		assertEquals(APPLICABLE, accessConditions.getPartyBTaxEvent());
		assertEquals(APPLICABLE, accessConditions.getPartyATaxEventUponMerger());
		assertEquals(APPLICABLE, accessConditions.getPartyBTaxEventUponMerger());
		assertEquals(APPLICABLE, accessConditions.getPartyACreditEventUponMerger());
		assertEquals(APPLICABLE, accessConditions.getPartyBCreditEventUponMerger());
//		assertEquals(APPLICABLE, accessConditions.getPartyAAdditionalTerminationEvents());
//		assertEquals(APPLICABLE, accessConditions.getPartyBAdditionalTerminationEvents());

		// TODO alternative test case
		var additionalObligations = partyA.getAdditionalObligations();
		assertNotNull(additionalObligations);
		assertEquals(NONE, additionalObligations.getAdditionalObligations());
		assertNull(additionalObligations.getAdditionalObligationsSpecify());

		// TODO alternative test case
		var additionalRegimes = partyA.getAdditionalRegimes();
		assertNotNull(additionalRegimes);
		assertEquals(NOT_APPLICABLE, additionalRegimes.getIsApplicable());

		var additionalRepresentations = partyA.getAdditionalRepresentations();
		assertNotNull(additionalRepresentations);
		assertEquals(NOT_APPLICABLE, additionalRepresentations.getPartyAAdditionalRepresentations());
		assertEquals(NOT_APPLICABLE, additionalRepresentations.getPartyBAdditionalRepresentations());

		var addressesForTransfers = partyA.getAddressesForTransfers();
		assertNotNull(addressesForTransfers);
		assertEquals(NONE, addressesForTransfers.getPartyASpecify());
		assertEquals(NONE, addressesForTransfers.getPartyBSpecify());

		var amendmentToTerminationCurrency = partyA.getAmendmentToTerminationCurrency();
		assertNotNull(amendmentToTerminationCurrency);
		assertEquals(NOT_APPLICABLE, amendmentToTerminationCurrency.getIsApplicable());

		var baseCurrency = partyA.getBaseCurrency();
		assertNotNull(baseCurrency);
		assertEquals("Japanese Yen", baseCurrency.getCurrency());

		var bespokeProvisions = partyA.getBespokeProvisions();
		assertNotNull(bespokeProvisions);
		assertEquals(NOT_APPLICABLE, bespokeProvisions.getIsApplicable());

		var calculationDateLocation = partyA.getCalculationDateLocation();
		assertNotNull(calculationDateLocation);
		assertEquals(SELECT_LOCATION, calculationDateLocation.getPartyACalculationDateLocation());
		assertEquals("Frankfurt, Germany", calculationDateLocation.getPartyALocation());
		assertEquals(SELECT_LOCATION, calculationDateLocation.getPartyBCalculationDateLocation());
		assertEquals("Frankfurt, Germany", calculationDateLocation.getPartyBLocation());

		var canada = partyA.getCanada();
		assertNotNull(canada);
		assertEquals(NOT_APPLICABLE, canada.getPartyASecuredParty());
		assertEquals(NOT_APPLICABLE, canada.getPartyBSecuredParty());

		var cftc = partyA.getCftc();
		assertNotNull(cftc);
		assertEquals(NOT_APPLICABLE, cftc.getPartyASecuredParty());
		assertEquals(NOT_APPLICABLE, cftc.getPartyBSecuredParty());

		var emir = partyA.getEmir();
		assertNotNull(emir);
		assertEquals(NOT_APPLICABLE, emir.getPartyASecuredParty());
		assertEquals(NOT_APPLICABLE, emir.getPartyBSecuredParty());

		var japan = partyA.getJapan();
		assertNotNull(japan);
		assertEquals(NOT_APPLICABLE, japan.getPartyASecuredParty());
		assertEquals(NOT_APPLICABLE, japan.getPartyBSecuredParty());

		var prudential = partyA.getPrudential();
		assertNotNull(prudential);
		assertEquals(NOT_APPLICABLE, prudential.getPartyASecuredParty());
		assertEquals(NOT_APPLICABLE, prudential.getPartyBSecuredParty());

		var sec = partyA.getSec();
		assertNotNull(sec);
		assertEquals(NOT_APPLICABLE, sec.getPartyASecuredParty());
		assertEquals(NOT_APPLICABLE, sec.getPartyBSecuredParty());

		var switzerland = partyA.getSwitzerland();
		assertNotNull(switzerland);
		assertEquals(NOT_APPLICABLE, switzerland.getPartyASecuredParty());
//		assertEquals(NOT_APPLICABLE, switzerland.getPartyARetrospective());
//		assertEquals("Specific terms", switzerland.getPartyASIMMApplicableSpecify());
//		assertEquals(APPLICABLE, switzerland.getPartyBSecuredParty());
//		assertEquals(NOT_APPLICABLE, switzerland.getPartyBRetrospective());
		assertNull(switzerland.getPartyBSIMMApplicableSpecify());

		var ceEndDate = partyA.getCeEndDate();
		assertNotNull(ceEndDate);
//		assertEquals("days", ceEndDate.getReleaseDate());
//		assertEquals("5", ceEndDate.getReleaseDays()); // TODO: missing conditional mapping
//		assertEquals("calendar_day_release_days", ceEndDate.getReleaseDaysType());
//		assertNull(ceEndDate.getSpecifyReleaseDays());
//		assertEquals("days", ceEndDate.getDateOfTimelyStatement());
//		assertEquals("5", ceEndDate.getTimelyDays());
//		assertEquals("calendar_days_timely_days", ceEndDate.getTimelyDaysType());
//		assertNull(ceEndDate.getSpecifyTimelyDays());
//		assertEquals("days", ceEndDate.getDaysAfterCustodianEvent());
//		assertEquals("5", ceEndDate.getAfterDays()); // TODO: missing conditional mapping
//		assertEquals("calendar_days_after_days", ceEndDate.getAfterDaysType());
//		assertNull(ceEndDate.getSpecifyAfterDays());

		var chargorAdditionalRightsEvent = partyA.getChargorAdditionalRightsEvent();
		assertNotNull(chargorAdditionalRightsEvent);
		assertEquals(FALSE, chargorAdditionalRightsEvent.getApplicable());

		var chargorPostingObligations = partyA.getChargorPostingObligations();
		assertNotNull(chargorPostingObligations);
		assertEquals("partyA", chargorPostingObligations.getChargor());
		// TODO: PostingObligationsElection.party ingestion mapping broken
//		assertEquals("control_agreement", chargorPostingObligations.getPartyAType());
//		assertEquals("ney", chargorPostingObligations.getPartyAControlAgreement());
//		assertNull(chargorPostingObligations.getPartyAControlAgreementSpecify());
//		assertNull(chargorPostingObligations.getPartyBType());
//		assertNull(chargorPostingObligations.getPartyBControlAgreement());
//		assertNull(chargorPostingObligations.getPartyBControlAgreementSpecify());

		var chargorRightsEvent = partyA.getChargorRightsEvent();
		assertNotNull(chargorRightsEvent);
		assertEquals(NOT_SPECIFIED, chargorRightsEvent.getCoolingOffLanguage());

		var conditionsPrecedent = partyA.getConditionsPrecedent();
		assertNotNull(conditionsPrecedent);
		assertEquals(FALSE, conditionsPrecedent.getSpecified());

		var consent = partyA.getConsent();
		assertNotNull(consent);
		assertEquals(OTHER, consent.getIsApplicable());
		assertEquals("Non-standard consent language agreed on", consent.getSpecify());

		var controlAgreementAsACreditSupportDocument = partyA.getControlAgreementAsACreditSupportDocument();
		assertNotNull(controlAgreementAsACreditSupportDocument);
		assertEquals(NOT_SPECIFIED, controlAgreementAsACreditSupportDocument.getDefinition());

		var crossCurrencySwap = partyA.getCrossCurrencySwap();
		assertNotNull(crossCurrencySwap);
		assertEquals(FALSE, crossCurrencySwap.getYeyNey());

		// TODO: CustodianAndSegregatedAccountDetails ingestion mapping broken
		var custodianAndSegregatedAccountDetails = partyA.getCustodianAndSegregatedAccountDetails();
		assertNull(custodianAndSegregatedAccountDetails);

		var custodianEvent = partyA.getCustodianEvent();
		assertNotNull(custodianEvent);
		assertEquals(APPLICABLE, custodianEvent.getDefinition());

		var custodianRisk = partyA.getCustodianRisk();
		assertNotNull(custodianRisk);
		assertEquals(NOT_SPECIFIED, custodianRisk.getPartyACustodianRisk());
		assertEquals(NOT_SPECIFIED, custodianRisk.getPartyBCustodianRisk());

		// TODO: DateOfIsdaMasterAgreement not modelled in CDM
		var dateOfIsdaMasterAgreement = partyA.getDateOfIsdaMasterAgreement();
		assertNull(dateOfIsdaMasterAgreement);

		var deliveryInLieuRight = partyA.getDeliveryInLieuRight();
		assertNotNull(deliveryInLieuRight);
		assertEquals(APPLICABLE, deliveryInLieuRight.getRight());

		var demandsAndNotices = partyA.getDemandsAndNotices();
		assertNotNull(demandsAndNotices);
		assertEquals(NONE, demandsAndNotices.getPartyASpecify());
		assertEquals(NONE, demandsAndNotices.getPartyBSpecify());

		var earlyTerminationDate = partyA.getEarlyTerminationDate();
		assertNotNull(earlyTerminationDate);
		assertEquals(EXCLUDE, earlyTerminationDate.getPaidInFullLanguage());

		var executionDate = partyA.getExecutionDate();
		assertNotNull(executionDate);
		assertEquals("2019-04-01", executionDate.getExecutionDate());

		var executionLanguage = partyA.getExecutionLanguage();
		assertNull(executionLanguage);

		var japaneseSecuritiesProvisions = partyA.getJapaneseSecuritiesProvisions();
		assertNotNull(japaneseSecuritiesProvisions);
		assertEquals(NOT_APPLICABLE, japaneseSecuritiesProvisions.getIsApplicable());

		var minimumTransferAmount = partyA.getMinimumTransferAmount();
		assertNotNull(minimumTransferAmount);
//		assertEquals(ZERO, minimumTransferAmount.getPartyAMinimumTransferAmount());
		assertEquals("24000", minimumTransferAmount.getPartyAAmount());
		assertEquals("Euro", minimumTransferAmount.getPartyACurrency());
//		assertEquals(ZERO, minimumTransferAmount.getPartyBMinimumTransferAmount());
		assertEquals("0", minimumTransferAmount.getPartyBAmount());
		assertNull(minimumTransferAmount.getPartyBCurrency());

		var notificationTime = partyA.getNotificationTime();
		assertNotNull(notificationTime);
		assertEquals(TRUE, notificationTime.getPartyANotificationTime());
		assertEquals("11:00:00", notificationTime.getPartyATime());
		assertEquals("Frankfurt, Germany", notificationTime.getPartyALocation());
		assertEquals(TRUE, notificationTime.getPartyBNotificationTime());
		assertEquals("11:00:00", notificationTime.getPartyBTime());
		assertEquals("Frankfurt, Germany", notificationTime.getPartyBLocation());

		var oneWayProvisions = partyA.getOneWayProvisions();
		assertNotNull(oneWayProvisions);
		assertEquals(NOT_APPLICABLE, oneWayProvisions.getIsApplicable());

		var otherProvisions = partyA.getOtherProvisions();
		assertNotNull(otherProvisions);
		assertEquals(NONE_SPECIFIED, otherProvisions.getSpecifyProvisions());

		var parties = partyA.getParties();
		assertNotNull(parties);
		assertEquals("Foo", parties.getPartyAName());
		assertEquals("Bar", parties.getPartyBName());

		var processAgent = partyA.getProcessAgent();
		assertNotNull(processAgent);
		assertEquals(NOT_APPLICABLE, processAgent.getPartyAProcessAgent());
		assertEquals(NOT_APPLICABLE, processAgent.getPartyBProcessAgent());

		var relationshipWithTheControlAgreement = partyA.getRelationshipWithTheControlAgreement();
		assertNotNull(relationshipWithTheControlAgreement);
		assertEquals(EXCLUDE, relationshipWithTheControlAgreement.getIncludeLanguage());

		var resolutionTime = partyA.getResolutionTime();
		assertNotNull(resolutionTime);
		assertEquals(FALSE, resolutionTime.getSpecified());

		var rounding = partyA.getRounding();
		assertNotNull(rounding);
		assertEquals("10000", rounding.getDeliveryAmount());
		assertEquals("10000", rounding.getReturnAmount());

		var sensitivitiesToCommodityIndices = partyA.getSensitivitiesToCommodityIndices();
		assertNotNull(sensitivitiesToCommodityIndices);
		assertEquals(STANDARD, sensitivitiesToCommodityIndices.getSensitivitiesCommodityIndices());

		var sensitivitiesToEquityIndicesFundsEtfs = partyA.getSensitivitiesToEquityIndicesFundsEtfs();
		assertNotNull(sensitivitiesToEquityIndicesFundsEtfs);
		assertEquals(STANDARD, sensitivitiesToEquityIndicesFundsEtfs.getSensitivitiesEquityIndicesFundsEtfs());

		var simmCalculationCurrency = partyA.getSimmCalculationCurrency();
		assertNotNull(simmCalculationCurrency);
		assertEquals(TRUE, simmCalculationCurrency.getPartyAUseBaseCurrency());
		assertEquals(TRUE, simmCalculationCurrency.getPartyBUseBaseCurrency());

		var threshold = partyA.getThreshold();
		assertNotNull(threshold);
//		assertEquals(ZERO, threshold.getPartyAThreshold());
		assertEquals("0", threshold.getPartyAAmount());
		assertNull(threshold.getPartyACurrency());
//		assertEquals(ZERO, threshold.getPartyBThreshold());
		assertEquals("45000", threshold.getPartyBAmount());
		assertEquals("Pound Sterling", threshold.getPartyBCurrency());

		var transferTiming = partyA.getTransferTiming();
		assertNotNull(transferTiming);
		assertEquals(FALSE, transferTiming.getSpecified());

		var umbrellaAgreementAndPrincipalIdentification = partyA.getUmbrellaAgreementAndPrincipalIdentification();
		assertNotNull(umbrellaAgreementAndPrincipalIdentification);
		assertEquals(NOT_APPLICABLE, umbrellaAgreementAndPrincipalIdentification.getIsApplicable());

		var valuationOfAppropriatedCollateral = partyA.getValuationOfAppropriatedCollateral();
		assertNotNull(valuationOfAppropriatedCollateral);
		assertEquals(FALSE, valuationOfAppropriatedCollateral.getSpecified());

		var json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(isdaCreate);
		assertNotNull(json);
		LOGGER.debug(json);
	}

	private static void initialiseIngestionFactory() {
		IngestionFactory.init(INSTANCE_NAME, IsdaCreateIsdaCsdIm2016EngLawProjectionMapperTest.class.getClassLoader(),
			ReferenceConfig.noScopeOrExcludedPaths(),
			new PathCollector<>(),
			new RosettaTypeValidator());
	}

	private <R extends RosettaModelObject> R ingest(Class<R> clazz, URL url) throws IOException {
		var ingested = ingestionServiceEnglishLaw.ingestAndPostProcessJson(clazz, UrlUtils.openURL(url));
		return ingested.getRosettaModelInstance();
	}
}
