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
class IsdaCreateIsdaCsaIm2016NyLawProjectionMapperTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(IsdaCreateIsdaCsaIm2016NyLawProjectionMapperTest.class);

	private static final String INSTANCE_NAME = "target/ISDA_CREATE";

	private static IngestionService ingestionServiceEnglishLaw;

	private final ObjectMapper objectMapper = RosettaObjectMapper.getNewRosettaObjectMapper();

	private IsdaCreateIsdaCsaIm2016NyLawProjectionMapper mapper;

	@BeforeAll
	static void globalSetUp() {
		initialiseIngestionFactory();
		ingestionServiceEnglishLaw = IngestionFactory.getInstance(INSTANCE_NAME).getService("isdaCreateAll");
	}

	@BeforeEach
	void setUp() {
		mapper = new IsdaCreateIsdaCsaIm2016NyLawProjectionMapper();
	}

	@Test
	void shouldIngestLegalAgreementAndBuildIsdaCreateDocument() throws IOException {
		var url = Resources.getResource("isda-create/initial-margin-2016_ny-law_test1.json");
		var legalAgreement = ingest(LegalAgreement.class, url);
		assertNotNull(legalAgreement);

		var isdaCreate = mapper.getIsdaCreate(legalAgreement);
		assertNotNull(isdaCreate);

		var document = isdaCreate.getDocument();
		assertNotNull(document);
		assertEquals("016764cc-5e03-36c6-fc79-7f6344b867d0", document.getId());
		assertEquals(Integer.valueOf(2016), document.getYear());
		assertEquals("Credit Support Annex", document.getDocumentType());
		assertEquals("New York", document.getGoverningLaw());
		assertEquals("2016 IM CSA (NY Law)", document.getAbbreviation());
		assertEquals("ISDA 2016 Phase One Credit Support Annex for Initial Margin (IM) (Security Interest - New York Law)", document.getDescription());
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
		assertEquals(APPLICABLE, additionalRepresentations.getPartyAAdditionalRepresentations());
		assertEquals(NOT_APPLICABLE, additionalRepresentations.getPartyBAdditionalRepresentations());
		assertEquals("Distinct elections between the parties - Applicable / NA", additionalRepresentations.getSpecify());

		var addressesForTransfers = partyA.getAddressesForTransfers();
		assertNotNull(addressesForTransfers);
		assertEquals(NONE, addressesForTransfers.getPartyASpecify());
		assertEquals(NONE, addressesForTransfers.getPartyBSpecify());

		var amendmentToTerminationCurrency = partyA.getAmendmentToTerminationCurrency();
		assertNotNull(amendmentToTerminationCurrency);
		assertEquals(APPLICABLE, amendmentToTerminationCurrency.getIsApplicable());
		assertNull(amendmentToTerminationCurrency.getDateOfAnnex());
		assertEquals("annex", amendmentToTerminationCurrency.getAnnexDate());
//		assertEquals("Pound Sterling", amendmentToTerminationCurrency.getPartyATerminationCurrency());
//		assertEquals("Pound Sterling", amendmentToTerminationCurrency.getPartyBTerminationCurrency());
//		assertEquals("Japanese Yen", amendmentToTerminationCurrency.getBothPartiesTerminationCurrency());

		var baseCurrency = partyA.getBaseCurrency();
		assertNotNull(baseCurrency);
		assertEquals("Euro", baseCurrency.getCurrency());

		var bespokeProvisions = partyA.getBespokeProvisions();
		assertNotNull(bespokeProvisions);
		assertEquals(NOT_APPLICABLE, bespokeProvisions.getIsApplicable());

		var calculationDateLocation = partyA.getCalculationDateLocation();
		assertNotNull(calculationDateLocation);
		assertEquals(SELECT_LOCATION, calculationDateLocation.getPartyACalculationDateLocation());
		assertEquals("Beijing, China", calculationDateLocation.getPartyALocation());
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
		assertEquals(NOT_APPLICABLE, switzerland.getPartyBSecuredParty());

		var ceEndDate = partyA.getCeEndDate();
		assertNotNull(ceEndDate);
//		assertEquals("days", ceEndDate.getReleaseDate());
//		assertEquals("2", ceEndDate.getReleaseDays()); // TODO: missing conditional mapping
//		assertEquals("calendar_day_release_days", ceEndDate.getReleaseDaysType());
//		assertNull(ceEndDate.getSpecifyReleaseDays());
//		assertEquals("days", ceEndDate.getDateOfTimelyStatement());
//		assertEquals("2", ceEndDate.getTimelyDays());
//		assertEquals("calendar_days_timely_days", ceEndDate.getTimelyDaysType());
//		assertNull(ceEndDate.getSpecifyTimelyDays());
//		assertEquals("days", ceEndDate.getDaysAfterCustodianEvent());
//		assertEquals("2", ceEndDate.getAfterDays()); // TODO: missing conditional mapping
//		assertEquals("calendar_days_after_days", ceEndDate.getAfterDaysType());
//		assertNull(ceEndDate.getSpecifyAfterDays());

		var pledgorAdditionalRightsEvent = partyA.getPledgorAdditionalRightsEvent();
		assertNotNull(pledgorAdditionalRightsEvent);
		assertEquals(FALSE, pledgorAdditionalRightsEvent.getApplicable());

		var pledgorPostingObligations = partyA.getPledgorPostingObligations();
		assertNotNull(pledgorPostingObligations);
		// TODO: PostingObligationsElection.party ingestion mapping broken
		//		assertEquals("control_agreement", pledgorPostingObligations.getPartyAType());
		//		assertEquals("ney", pledgorPostingObligations.getPartyAControlAgreement());
		//		assertNull(pledgorPostingObligations.getPartyAControlAgreementSpecify());
		//		assertNull(pledgorPostingObligations.getPartyBType());
		//		assertNull(pledgorPostingObligations.getPartyBControlAgreement());
		//		assertNull(pledgorPostingObligations.getPartyBControlAgreementSpecify());

		var pledgorRightsEvent = partyA.getPledgorRightsEvent();
		assertNotNull(pledgorRightsEvent);
		assertEquals(NOT_SPECIFIED, pledgorRightsEvent.getCoolingOffLanguage());

		var conditionsPrecedent = partyA.getConditionsPrecedent();
		assertNotNull(conditionsPrecedent);
		assertEquals(FALSE, conditionsPrecedent.getSpecified());

		var consent = partyA.getConsent();
		assertNotNull(consent);
		assertEquals(STANDARD, consent.getIsApplicable());
		assertNull(consent.getSpecify());

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
		assertEquals(NOT_APPLICABLE, deliveryInLieuRight.getRight());

		var demandsAndNotices = partyA.getDemandsAndNotices();
		assertNotNull(demandsAndNotices);
		assertEquals(NONE, demandsAndNotices.getPartyASpecify());
		assertEquals(NONE, demandsAndNotices.getPartyBSpecify());

		var earlyTerminationDate = partyA.getEarlyTerminationDate();
		assertNotNull(earlyTerminationDate);
		assertEquals(EXCLUDE, earlyTerminationDate.getPaidInFullLanguage());

		var executionDate = partyA.getExecutionDate();
		assertNotNull(executionDate);
		assertEquals("2019-04-04", executionDate.getExecutionDate());

		var executionLanguage = partyA.getExecutionLanguage();
		assertNull(executionLanguage);

		var japaneseSecuritiesProvisions = partyA.getJapaneseSecuritiesProvisions();
		assertNotNull(japaneseSecuritiesProvisions);
		assertEquals(NOT_APPLICABLE, japaneseSecuritiesProvisions.getIsApplicable());

		var minimumTransferAmount = partyA.getMinimumTransferAmount();
		assertNotNull(minimumTransferAmount);
//		assertEquals(ZERO, minimumTransferAmount.getPartyAMinimumTransferAmount());
//		assertEquals(ZERO, minimumTransferAmount.getPartyBMinimumTransferAmount());

		var notificationTime = partyA.getNotificationTime();
		assertNotNull(notificationTime);
		assertEquals(TRUE, notificationTime.getPartyANotificationTime());
		assertEquals("11:00:00", notificationTime.getPartyATime());
		assertEquals("Amsterdam, Netherlands", notificationTime.getPartyALocation());
		assertEquals(TRUE, notificationTime.getPartyBNotificationTime());
		assertEquals("11:00:00", notificationTime.getPartyBTime());
		assertEquals("Amsterdam, Netherlands", notificationTime.getPartyBLocation());

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
//		assertEquals(ZERO, threshold.getPartyBThreshold());

		var transferTiming = partyA.getTransferTiming();
		assertNotNull(transferTiming);
		assertEquals(FALSE, transferTiming.getSpecified());

		var umbrellaAgreementAndPrincipalIdentification = partyA.getUmbrellaAgreementAndPrincipalIdentification();
		assertNotNull(umbrellaAgreementAndPrincipalIdentification);
		assertEquals(NOT_APPLICABLE, umbrellaAgreementAndPrincipalIdentification.getIsApplicable());

		var json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(isdaCreate);
		assertNotNull(json);
		LOGGER.debug(json);
	}

	@Test
	void shouldIngestLegalAgreementAndBuildIsdaCreateDocument2() throws IOException {
		var url = Resources.getResource("isda-create/initial-margin-2016_ny-law_test2.json");
		var legalAgreement = ingest(LegalAgreement.class, url);
		assertNotNull(legalAgreement);

		var isdaCreate = mapper.getIsdaCreate(legalAgreement);
		assertNotNull(isdaCreate);

		var document = isdaCreate.getDocument();
		assertNotNull(document);
		assertEquals("016764cc-5e03-36c6-fc79-7f6344b867d0", document.getId());
		assertEquals(Integer.valueOf(2016), document.getYear());
		assertEquals("Credit Support Annex", document.getDocumentType());
		assertEquals("New York", document.getGoverningLaw());
		assertEquals("2016 IM CSA (NY Law)", document.getAbbreviation());
		assertEquals("ISDA 2016 Phase One Credit Support Annex for Initial Margin (IM) (Security Interest - New York Law)", document.getDescription());
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
		assertNull(additionalRepresentations.getSpecify());

		var addressesForTransfers = partyA.getAddressesForTransfers();
		assertNotNull(addressesForTransfers);
		assertEquals(NONE, addressesForTransfers.getPartyASpecify());
		assertEquals(NONE, addressesForTransfers.getPartyBSpecify());

		var amendmentToTerminationCurrency = partyA.getAmendmentToTerminationCurrency();
		assertNotNull(amendmentToTerminationCurrency);
		assertEquals(NOT_APPLICABLE, amendmentToTerminationCurrency.getIsApplicable());
		assertNull(amendmentToTerminationCurrency.getDateOfAnnex());
		assertNull(amendmentToTerminationCurrency.getAnnexDate());
		assertNull(amendmentToTerminationCurrency.getPartyATerminationCurrency());
		assertNull(amendmentToTerminationCurrency.getPartyBTerminationCurrency());
		assertNull(amendmentToTerminationCurrency.getBothPartiesTerminationCurrency());

		var baseCurrency = partyA.getBaseCurrency();
		assertNotNull(baseCurrency);
		assertEquals("United States Dollar", baseCurrency.getCurrency());

		var bespokeProvisions = partyA.getBespokeProvisions();
		assertNotNull(bespokeProvisions);
		assertEquals(NOT_APPLICABLE, bespokeProvisions.getIsApplicable());

		var calculationDateLocation = partyA.getCalculationDateLocation();
		assertNotNull(calculationDateLocation);
		assertEquals(SELECT_LOCATION, calculationDateLocation.getPartyACalculationDateLocation());
		assertEquals("Amsterdam, Netherlands", calculationDateLocation.getPartyALocation());
		assertEquals(SELECT_LOCATION, calculationDateLocation.getPartyBCalculationDateLocation());
		assertEquals("London, United Kingdom", calculationDateLocation.getPartyBLocation());

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
		assertNull(emir.getPartyASecuredPartySpecify());
//		assertEquals(NOT_APPLICABLE, emir.getPartyASIMM());
		assertNull(emir.getPartyASIMMApplicableSpecify());
//		assertEquals(NOT_APPLICABLE, emir.getPartyARetrospective());
		assertNull(emir.getPartyARetrospectiveSpecify());
//		assertEquals(APPLICABLE, emir.getPartyBSecuredParty());
		assertNull(emir.getPartyBSecuredPartySpecify());
//		assertEquals(NOT_APPLICABLE, emir.getPartyBSIMM());
		assertNull(emir.getPartyBSIMMApplicableSpecify());
//		assertEquals(NOT_APPLICABLE, emir.getPartyBRetrospective());
		assertNull(emir.getPartyBRetrospectiveSpecify());

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
		assertEquals(NOT_APPLICABLE, switzerland.getPartyBSecuredParty());

		var ceEndDate = partyA.getCeEndDate();
		assertNotNull(ceEndDate);
		assertNull(ceEndDate.getReleaseDate());
		assertNull(ceEndDate.getReleaseDays());
		assertNull(ceEndDate.getReleaseDaysType());
		assertNull(ceEndDate.getSpecifyReleaseDays());
		assertNull(ceEndDate.getDateOfTimelyStatement());
		assertNull(ceEndDate.getTimelyDays());
		assertNull(ceEndDate.getTimelyDaysType());
		assertNull(ceEndDate.getSpecifyTimelyDays());
		assertNull(ceEndDate.getDaysAfterCustodianEvent());
		assertNull(ceEndDate.getAfterDays());
		assertNull(ceEndDate.getAfterDaysType());
		assertNull(ceEndDate.getSpecifyAfterDays());

		var pledgorAdditionalRightsEvent = partyA.getPledgorAdditionalRightsEvent();
		assertNotNull(pledgorAdditionalRightsEvent);
		assertEquals(FALSE, pledgorAdditionalRightsEvent.getApplicable());

		var pledgorPostingObligations = partyA.getPledgorPostingObligations();
		assertNotNull(pledgorPostingObligations);
		// TODO: PostingObligationsElection.party ingestion mapping broken
		//		assertEquals("control_agreement", pledgorPostingObligations.getPartyAType());
		//		assertEquals("ney", pledgorPostingObligations.getPartyAControlAgreement());
		//		assertNull(pledgorPostingObligations.getPartyAControlAgreementSpecify());
		//		assertNull(pledgorPostingObligations.getPartyBType());
		//		assertNull(pledgorPostingObligations.getPartyBControlAgreement());
		//		assertNull(pledgorPostingObligations.getPartyBControlAgreementSpecify());

		var pledgorRightsEvent = partyA.getPledgorRightsEvent();
		assertNotNull(pledgorRightsEvent);
		assertEquals(NOT_SPECIFIED, pledgorRightsEvent.getCoolingOffLanguage());

		var conditionsPrecedent = partyA.getConditionsPrecedent();
		assertNotNull(conditionsPrecedent);
		assertEquals(FALSE, conditionsPrecedent.getSpecified());

		var consent = partyA.getConsent();
		assertNotNull(consent);
		assertEquals(STANDARD, consent.getIsApplicable());
		assertNull(consent.getSpecify());

		var controlAgreementAsACreditSupportDocument = partyA.getControlAgreementAsACreditSupportDocument();
		assertNotNull(controlAgreementAsACreditSupportDocument);
		assertEquals(NOT_SPECIFIED, controlAgreementAsACreditSupportDocument.getDefinition());

		var crossCurrencySwap = partyA.getCrossCurrencySwap();
		assertNotNull(crossCurrencySwap);
		assertEquals(TRUE, crossCurrencySwap.getYeyNey());

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
		assertEquals(NOT_APPLICABLE, deliveryInLieuRight.getRight());

		var demandsAndNotices = partyA.getDemandsAndNotices();
		assertNotNull(demandsAndNotices);
		assertEquals(NONE, demandsAndNotices.getPartyASpecify());
		assertEquals(NONE, demandsAndNotices.getPartyBSpecify());

		var earlyTerminationDate = partyA.getEarlyTerminationDate();
		assertNotNull(earlyTerminationDate);
		assertEquals(EXCLUDE, earlyTerminationDate.getPaidInFullLanguage());

		var executionDate = partyA.getExecutionDate();
		assertNotNull(executionDate);
		assertEquals("2019-01-25", executionDate.getExecutionDate());

		var executionLanguage = partyA.getExecutionLanguage();
		assertNull(executionLanguage);

		var japaneseSecuritiesProvisions = partyA.getJapaneseSecuritiesProvisions();
		assertNotNull(japaneseSecuritiesProvisions);
		assertEquals(NOT_APPLICABLE, japaneseSecuritiesProvisions.getIsApplicable());

		var minimumTransferAmount = partyA.getMinimumTransferAmount();
		assertNotNull(minimumTransferAmount);
//		assertEquals(ZERO, minimumTransferAmount.getPartyAMinimumTransferAmount());
//		assertEquals(ZERO, minimumTransferAmount.getPartyBMinimumTransferAmount());

		var notificationTime = partyA.getNotificationTime();
		assertNotNull(notificationTime);
		assertEquals(TRUE, notificationTime.getPartyANotificationTime());
		assertEquals("09:00:00", notificationTime.getPartyATime());
		assertEquals("Amsterdam, Netherlands", notificationTime.getPartyALocation());
		assertEquals(TRUE, notificationTime.getPartyBNotificationTime());
		assertEquals("10:00:00", notificationTime.getPartyBTime());
		assertEquals("London, United Kingdom", notificationTime.getPartyBLocation());

		var oneWayProvisions = partyA.getOneWayProvisions();
		assertNotNull(oneWayProvisions);
		assertEquals(NOT_APPLICABLE, oneWayProvisions.getIsApplicable());

		var otherProvisions = partyA.getOtherProvisions();
		assertNotNull(otherProvisions);
		assertEquals(NONE_SPECIFIED, otherProvisions.getSpecifyProvisions());

		var parties = partyA.getParties();
		assertNotNull(parties);
		assertEquals("Star Investments CP1", parties.getPartyAName());
		assertEquals("AcadiaSoft Bank CP", parties.getPartyBName());

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
		assertEquals("3000000", threshold.getPartyAAmount());
		assertEquals("United States Dollar", threshold.getPartyACurrency());
//		assertEquals(ZERO, threshold.getPartyBThreshold());
		assertEquals("30000000", threshold.getPartyBAmount());
		assertEquals("United States Dollar", threshold.getPartyBCurrency());

		var transferTiming = partyA.getTransferTiming();
		assertNotNull(transferTiming);
		assertEquals(FALSE, transferTiming.getSpecified());

		var umbrellaAgreementAndPrincipalIdentification = partyA.getUmbrellaAgreementAndPrincipalIdentification();
		assertNotNull(umbrellaAgreementAndPrincipalIdentification);
		assertEquals(NOT_APPLICABLE, umbrellaAgreementAndPrincipalIdentification.getIsApplicable());

		var json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(isdaCreate);
		assertNotNull(json);
		LOGGER.debug(json);
	}

	private static void initialiseIngestionFactory() {
		IngestionFactory.init(INSTANCE_NAME, IsdaCreateIsdaCsaIm2016NyLawProjectionMapperTest.class.getClassLoader(),
			ReferenceConfig.noScopeOrExcludedPaths(),
			new PathCollector<>(),
			new RosettaTypeValidator());
	}

	private <R extends RosettaModelObject> R ingest(Class<R> clazz, URL url) throws IOException {
		var ingested = ingestionServiceEnglishLaw.ingestAndPostProcessJson(clazz, UrlUtils.openURL(url));
		return ingested.getRosettaModelInstance();
	}
}
