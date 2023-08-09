package com.regnosys.granite.projector.fpml_5_10;

import cdm.base.datetime.AdjustableOrAdjustedOrRelativeDate;
import cdm.base.datetime.daycount.metafields.FieldWithMetaDayCountFractionEnum;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import cdm.base.math.MeasureSchedule;
import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.ReferenceWithMetaNonNegativeQuantitySchedule;
import cdm.base.staticdata.asset.common.ProductTaxonomy;
import cdm.base.staticdata.asset.rates.metafields.FieldWithMetaFloatingRateIndexEnum;
import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.party.*;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.TradeState;
import cdm.observable.asset.FxSpotRateSource;
import cdm.observable.asset.Price;
import cdm.observable.asset.*;
import cdm.observable.asset.metafields.*;
import cdm.product.asset.CashflowRepresentation;
import cdm.product.asset.FixedRateSpecification;
import cdm.product.asset.InterestRatePayout;
import cdm.product.asset.RateSpecification;
import cdm.product.asset.metafields.FieldWithMetaSpreadScheduleTypeEnum;
import cdm.product.common.schedule.RateSchedule;
import cdm.product.common.schedule.metafields.ReferenceWithMetaCalculationPeriodDates;
import cdm.product.common.schedule.metafields.ReferenceWithMetaPaymentDates;
import cdm.product.common.settlement.PhysicalSettlementTerms;
import cdm.product.common.settlement.ValuationDate;
import cdm.product.common.settlement.*;
import cdm.product.template.OptionExercise;
import cdm.product.template.Product;
import cdm.product.template.*;
import com.google.common.base.CaseFormat;
import com.regnosys.rosetta.common.hashing.ReferenceResolverProcessStep;
import com.rosetta.model.lib.GlobalKey;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.FieldWithMeta;
import com.rosetta.model.lib.meta.GlobalKeyFields;
import com.rosetta.model.lib.meta.ReferenceWithMeta;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaDate;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import com.rosetta.model.metafields.ReferenceWithMetaDate;
import org.fpml.fpml_5.confirmation.CalculationAgent;
import org.fpml.fpml_5.confirmation.Currency;
import org.fpml.fpml_5.confirmation.EuropeanExercise;
import org.fpml.fpml_5.confirmation.ExerciseNotice;
import org.fpml.fpml_5.confirmation.ExerciseProcedure;
import org.fpml.fpml_5.confirmation.FxFixingDate;
import org.fpml.fpml_5.confirmation.InformationSource;
import org.fpml.fpml_5.confirmation.ManualExercise;
import org.fpml.fpml_5.confirmation.Money;
import org.fpml.fpml_5.confirmation.Party;
import org.fpml.fpml_5.confirmation.QuotationRateTypeEnum;
import org.fpml.fpml_5.confirmation.RateObservation;
import org.fpml.fpml_5.confirmation.SettlementProvision;
import org.fpml.fpml_5.confirmation.SettlementRateOption;
import org.fpml.fpml_5.confirmation.Strike;
import org.fpml.fpml_5.confirmation.*;
import org.isda.cdm.processor.CdmReferenceConfig;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class Fpml510ProjectionMapper {

	private static final String FPML_VERSION_5_10 = "5-10";

	private final ObjectFactory objectFactory;
	private final DatatypeFactory datatypeFactory;

	private final List<String> SUPPORTED_QUALIFICATIONS =
			List.of("InterestRate_IRSwap_FixedFloat",
					"InterestRate_IRSwap_FixedFloat_OIS",
					"InterestRate_IRSwap_FixedFloat_ZeroCoupon",
					"InterestRate_IRSwap_Basis",
					"InterestRate_IRSwap_Basis_OIS",
					"InterestRate_Option_Swaption");

	public Fpml510ProjectionMapper() {
		this.objectFactory = new ObjectFactory();
		try {
			this.datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new IllegalStateException("Failed to create Fpml5ObjectMapper", e);
		}
	}

	@SuppressWarnings("unchecked")
	public <R extends RosettaModelObject, T extends Document> T getDocument(R rosettaModelInstance, Class<T> fpmlClass) {
		if (!(rosettaModelInstance instanceof TradeState)) {
			throw new IllegalArgumentException("Unsupported CDM type " + rosettaModelInstance.getClass().getSimpleName());
		}
		RosettaModelObjectBuilder builder = rosettaModelInstance.toBuilder();
		new ReferenceResolverProcessStep(CdmReferenceConfig.get()).runProcessStep(TradeState.class, builder);
		TradeState tradeState = (TradeState) builder.build();
		String productQualifier = getProductQualifier(tradeState);
		checkProductQualifier(productQualifier);

		if (fpmlClass.isAssignableFrom(DataDocument.class)) {
			return (T) getDataDocument(tradeState, productQualifier);
		} else if (fpmlClass.isAssignableFrom(RequestClearing.class)) {
			return (T) getRequestClearing(tradeState, productQualifier);
		} else {
			throw new IllegalArgumentException("Unsupported FpML type " + fpmlClass.getSimpleName());
		}
	}

	private DataDocument getDataDocument(TradeState cdmTradeState, String cdmProductQualifier) {
		DataDocument dataDocument = objectFactory.createDataDocument();
		dataDocument.setFpmlVersion(FPML_VERSION_5_10);
		getTrade(cdmTradeState, cdmProductQualifier).ifPresent(t -> dataDocument.getTrade().add(t));
		dataDocument.getParty().addAll(getParties(cdmTradeState));
		return dataDocument;
	}

	private RequestClearing getRequestClearing(TradeState cdmTradeState, String cdmProductQualifier) {
		RequestClearing requestClearing = objectFactory.createRequestClearing();
		requestClearing.setFpmlVersion(FPML_VERSION_5_10);
		getTrade(cdmTradeState, cdmProductQualifier).ifPresent(requestClearing::setTrade);
		requestClearing.getParty().addAll(getParties(cdmTradeState));
		return requestClearing;
	}

	private void checkProductQualifier(String cdmProductQualifier) {
		String q = Optional.ofNullable(cdmProductQualifier)
			.orElseThrow(() -> new UnsupportedOperationException(
				"Only products qualified as " + SUPPORTED_QUALIFICATIONS + " are supported. This object was unqualified."));

		if (SUPPORTED_QUALIFICATIONS.stream().noneMatch(q::equals)) {
			throw new UnsupportedOperationException(String.format(
				"Only products qualified as " + SUPPORTED_QUALIFICATIONS + " are supported. This object qualified as %s", q));
		}
	}

	private String getProductQualifier(TradeState tradeState) {
		return Optional.ofNullable(tradeState)
				.map(TradeState::getTrade)
				.map(cdm.event.common.Trade::getTradableProduct)
				.map(TradableProduct::getProduct)
				.map(Product::getContractualProduct)
				.map(ContractualProduct::getProductTaxonomy)
				.orElse(Collections.emptyList()).stream()
				.map(ProductTaxonomy::getProductQualifier)
				.filter(Objects::nonNull)
				.findFirst()
				.orElse(null);
	}

	private Optional<Trade> getTrade(TradeState cdmTradeState, String cdmProductQualifier) {
		return Optional.ofNullable(cdmTradeState)
			.map(c -> {
				Trade trade = objectFactory.createTrade();
				getTradeHeader(c).ifPresent(trade::setTradeHeader);
				switch (cdmProductQualifier) {
					case "InterestRate_IRSwap_FixedFloat_PlainVanilla":
					case "InterestRate_IRSwap_FixedFloat":
					case "InterestRate_IRSwap_Basis":
					case "InterestRate_IRSwap_FixedFloat_ZeroCoupon": {
						Optional.ofNullable(c.getTrade())
							.map(cdm.event.common.Trade::getTradableProduct)
							.flatMap(t -> getSwap(t.getProduct(), t.getCounterparty(), getPriceQuantity(t.getTradeLot())))
							.map(objectFactory::createSwap)
							.ifPresent(trade::setProduct);
					}
					break;
					case "InterestRate_Option_Swaption":
						Optional.ofNullable(c.getTrade())
							.map(cdm.event.common.Trade::getTradableProduct)
							.flatMap(this::getSwaption)
							.map(objectFactory::createSwaption)
							.ifPresent(trade::setProduct);
						break;
				}
				Optional.ofNullable(c.getTrade())
					.map(cdm.event.common.Trade::getTradableProduct)
					.ifPresent(t -> Optional.ofNullable(t.getProduct())
						.map(Product::getContractualProduct)
						.map(ContractualProduct::getEconomicTerms)
						.map(EconomicTerms::getCalculationAgent)
						.ifPresent(a -> {
							getCalculationAgent(a, t.getAncillaryParty()).ifPresent(trade::setCalculationAgent);
							getBusinessCenter(a.getCalculationAgentBusinessCenter()).ifPresent(trade::setCalculationAgentBusinessCenter);
						}));
//				Optional.ofNullable(c.getTrade().getContractDetails())
//					.map(ContractDetails::getDocumentation)
//					.filter(relatedAgreements -> relatedAgreements.size() > 0)
//					.map(x -> x.get(0))
//					.flatMap(this::getDocumentation)
//					.ifPresent(trade::setDocumentation);

				return trade;
			});
	}

	private List<? extends PriceQuantity> getPriceQuantity(List<? extends TradeLot> cdmTradeLot) {
		return emptyIfNull(cdmTradeLot).stream().map(TradeLot::getPriceQuantity)
			.flatMap(Collection::stream).collect(Collectors.toList());
	}

	private Optional<TradeHeader> getTradeHeader(TradeState cdmTradeState) {
		return Optional.ofNullable(cdmTradeState)
			.map(cdm.event.common.TradeState::getTrade)
			.map(t -> {
				TradeHeader tradeHeader = objectFactory.createTradeHeader();
				tradeHeader.getPartyTradeIdentifier().addAll(getPartyTradeIdentifiers(t.getTradeIdentifier()));
				getIdentifiedDate(t.getTradeDate()).ifPresent(tradeHeader::setTradeDate);
				return tradeHeader;
			});
	}

	private List<PartyTradeIdentifier> getPartyTradeIdentifiers(List<? extends Identifier> cdmTradeIdentifier) {
		return emptyIfNull(cdmTradeIdentifier).stream()
			.map(i -> {
				PartyTradeIdentifier partyTradeIdentifier = objectFactory.createPartyTradeIdentifier();
				getIssuerId(i.getIssuer()).ifPresent(partyTradeIdentifier::setIssuer);
				Optional.ofNullable(i.getIssuerReference()).flatMap(this::getPartyReference).ifPresent(partyTradeIdentifier::setPartyReference);
				partyTradeIdentifier.getTradeIdOrVersionedTradeId().addAll(emptyIfNull(i.getAssignedIdentifier()).stream()
					.map(this::getVersionedTradeId)
					.flatMap(Optional::stream)
					.collect(Collectors.toList()));
				partyTradeIdentifier.getTradeIdOrVersionedTradeId().addAll(emptyIfNull(i.getAssignedIdentifier()).stream()
					.map(this::getTradeId)
					.flatMap(Optional::stream)
					.collect(Collectors.toList()));
				getExternalKey(i.getMeta()).ifPresent(partyTradeIdentifier::setId);
				return partyTradeIdentifier;
			})
			.collect(Collectors.toList());
	}

	private Optional<VersionedTradeId> getVersionedTradeId(AssignedIdentifier cdmAssignedIdentifier) {
		return Optional.ofNullable(cdmAssignedIdentifier)
			.filter(i -> i.getVersion() != null)
			.map(i -> {
				VersionedTradeId versionedTradeId = objectFactory.createVersionedTradeId();
				getTradeId(i.getIdentifier()).ifPresent(versionedTradeId::setTradeId);
				getBigInteger(i.getVersion()).ifPresent(versionedTradeId::setVersion);
				return versionedTradeId;
			});
	}

	private Optional<TradeId> getTradeId(AssignedIdentifier cdmAssignedIdentifier) {
		return Optional.ofNullable(cdmAssignedIdentifier)
			.filter(i -> i.getVersion() == null)
			.map(AssignedIdentifier::getIdentifier)
			.flatMap(this::getTradeId);
	}

	private Optional<TradeId> getTradeId(FieldWithMetaString cdmIdentifier) {
		return Optional.ofNullable(cdmIdentifier)
			.map(i -> {
				TradeId tradeId = objectFactory.createTradeId();
				getValue(i).ifPresent(tradeId::setValue);
				getScheme(i.getMeta()).ifPresent(tradeId::setTradeIdScheme);
				return tradeId;
			});
	}

	private Optional<IssuerId> getIssuerId(FieldWithMetaString cdmIssuer) {
		return Optional.ofNullable(cdmIssuer)
			.map(i -> {
				IssuerId issuerId = objectFactory.createIssuerId();
				getValue(i).ifPresent(issuerId::setValue);
				getScheme(i.getMeta()).ifPresent(issuerId::setIssuerIdScheme);
				return issuerId;
			});
	}

	private Optional<Swap> getSwap(Product cdmProduct,
								   List<? extends Counterparty> cdmCounterparties,
								   List<? extends PriceQuantity> cdmPriceQuantity) {
		return Optional.ofNullable(cdmProduct)
			.map(p -> {
				Swap swap = objectFactory.createSwap();
				swap.getSwapStream().addAll(getSwapStreams(p, cdmCounterparties));
				swap.getAdditionalPayment().addAll(getPayments(cdmCounterparties, cdmPriceQuantity));
				return swap;
			});
	}

	private Optional<Swaption> getSwaption(TradableProduct cdmTradableProduct) {
		return Optional.ofNullable(cdmTradableProduct)
			.map(t -> {
				Swaption swaption = objectFactory.createSwaption();
				Optional.ofNullable(t.getProduct())
					.map(Product::getContractualProduct)
					.map(ContractualProduct::getEconomicTerms)
					.ifPresent(et -> {
						getCalculationAgent(et.getCalculationAgent(), t.getAncillaryParty()).ifPresent(swaption::setCalculationAgent);
						Optional.ofNullable(et.getPayout())
							.ifPresent(p -> {
								Optional.ofNullable(p.getOptionPayout())
									.stream().flatMap(Collection::stream).findFirst()
									.ifPresent(o -> {
										getBuyerPartyReference(o.getBuyerSeller(), t.getCounterparty()).ifPresent(swaption::setBuyerPartyReference);
										getSellerPartyReference(o.getBuyerSeller(), t.getCounterparty()).ifPresent(swaption::setSellerPartyReference);
										getSwap(o.getUnderlier(),
											t.getCounterparty(),
											getPriceQuantity(t.getTradeLot()))
											.ifPresent(swaption::setSwap);
										Optional.ofNullable(o.getExerciseTerms())
											.ifPresent(e -> {
												getEuropeanExercise((OptionExercise) e).map(objectFactory::createEuropeanExercise).ifPresent(swaption::setExercise);
												getExerciseProcedure(e.getExerciseProcedure(), o.getBuyerSeller(), t.getCounterparty(), t.getAncillaryParty())
													.ifPresent(swaption::setExerciseProcedure);
											});
										Optional.ofNullable(o.getSettlementTerms())
											.map(cdm.product.common.settlement.SettlementTerms::getPhysicalSettlementTerms)
											.flatMap(this::getSwaptionPhysicalSettlement)
											.ifPresent(swaption::setPhysicalSettlement);
										getCashSettlement(o.getSettlementTerms()).ifPresent(swaption::setCashSettlement);
									});
								swaption.getPremium().addAll(getPayments(t.getCounterparty(), getPriceQuantity(t.getTradeLot())));
							});
					});
				return swaption;
			});
	}

	private Optional<CalculationAgent> getCalculationAgent(cdm.observable.asset.CalculationAgent cdmCalculationAgent, List<? extends AncillaryParty> cdmAncillaryParties) {
		return Optional.ofNullable(cdmCalculationAgent)
			.map(a -> {
				CalculationAgent calculationAgent = objectFactory.createCalculationAgent();
				Optional.ofNullable(a.getCalculationAgentParty())
					.ifPresent(p -> {
						List<PartyReference> calculationAgentPartyReferences = calculationAgent.getCalculationAgentPartyReference();
						getAncillaryPartyExternalReference(p, cdmAncillaryParties)
							.flatMap(this::getPartyReference)
							.ifPresent(calculationAgentPartyReferences::add);
					});
				return calculationAgent;
			});
	}

	private Optional<ExerciseProcedure> getExerciseProcedure(cdm.product.template.ExerciseProcedure cdmExerciseProcedure,
															 BuyerSeller cdmBuyerSeller,
															 List<? extends Counterparty> cdmCounterparties,
															 List<? extends AncillaryParty> cdmAncillaryParties) {
		return Optional.ofNullable(cdmExerciseProcedure)
			.map(p -> {
				ExerciseProcedure exerciseProcedure = objectFactory.createExerciseProcedure();
				Optional.ofNullable(p.getFollowUpConfirmation()).ifPresent(exerciseProcedure::setFollowUpConfirmation);
				Optional.ofNullable(p.getLimitedRightToConfirm()).ifPresent(exerciseProcedure::setLimitedRightToConfirm);
				Optional.ofNullable(p.getSplitTicket()).ifPresent(exerciseProcedure::setSplitTicket);
				getManualExercise(p.getManualExercise(), cdmBuyerSeller, cdmCounterparties, cdmAncillaryParties).ifPresent(exerciseProcedure::setManualExercise);
				return exerciseProcedure;
			});
	}

	private Optional<ManualExercise> getManualExercise(cdm.product.template.ManualExercise cdmManualExercise,
													   BuyerSeller cdmBuyerSeller,
													   List<? extends Counterparty> cdmCounterparties,
													   List<? extends AncillaryParty> cdmAncillaryParties) {
		return Optional.ofNullable(cdmManualExercise)
			.map(e -> {
				ManualExercise manualExercise = objectFactory.createManualExercise();
				getExerciseNotice(e.getExerciseNotice(), cdmBuyerSeller, cdmCounterparties, cdmAncillaryParties).ifPresent(manualExercise::setExerciseNotice);
				Optional.ofNullable(e.getFallbackExercise()).ifPresent(manualExercise::setFallbackExercise);
				return manualExercise;
			});
	}

	private Optional<ExerciseNotice> getExerciseNotice(cdm.product.template.ExerciseNotice cdmExerciseNotice,
													   BuyerSeller cdmBuyerSeller,
													   List<? extends Counterparty> cdmCounterparties,
													   List<? extends AncillaryParty> cdmAncillaryParties) {
		return Optional.ofNullable(cdmExerciseNotice)
			.map(e -> {
				ExerciseNotice exerciseNotice = objectFactory.createExerciseNotice();
				getBusinessCenter(e.getBusinessCenter()).ifPresent(exerciseNotice::setBusinessCenter);
				switch (e.getExerciseNoticeGiver()) {
					case BUYER:
						getBuyerPartyReference(cdmBuyerSeller, cdmCounterparties).ifPresent(exerciseNotice::setPartyReference);
						break;
					case SELLER:
						getSellerPartyReference(cdmBuyerSeller, cdmCounterparties).ifPresent(exerciseNotice::setPartyReference);
						break;
				}
				getPartyReference(e.getExerciseNoticeReceiver(), cdmAncillaryParties).ifPresent(exerciseNotice::setExerciseNoticePartyReference);
				return exerciseNotice;
			});
	}

	private Optional<CashSettlement> getCashSettlement(cdm.product.common.settlement.SettlementTerms cdmSettlementTerms) {
		return Optional.ofNullable(cdmSettlementTerms)
			.map(st -> {
				CashSettlement cashSettlement = objectFactory.createCashSettlement();
				Optional.ofNullable(st.getCashSettlementTerms())
					.map(Collection::stream)
					.flatMap(Stream::findFirst)
					.ifPresent(cst -> {
						getExternalKey(cst.getMeta()).ifPresent(cashSettlement::setId);
						Optional.ofNullable(cst.getCashSettlementMethod())
							.ifPresent(cashSettlementMethod -> {
									switch (cashSettlementMethod) {
									case CASH_PRICE_METHOD: {
										getCashPriceMethod(cst.getValuationMethod()).ifPresent(cashSettlement::setCashPriceMethod);
										break;
									}
									case CASH_PRICE_ALTERNATE_METHOD: {
										getCashPriceMethod(cst.getValuationMethod()).ifPresent(cashSettlement::setCashPriceAlternateMethod);
										break;
									}
									case COLLATERALIZED_CASH_PRICE_METHOD: {
										getYieldCurveMethod(cst.getValuationMethod()).ifPresent(cashSettlement::setCollateralizedCashPriceMethod);
										break;
									}
									case PAR_YIELD_CURVE_ADJUSTED_METHOD: {
										getYieldCurveMethod(cst.getValuationMethod()).ifPresent(cashSettlement::setParYieldCurveAdjustedMethod);
										break;
									}
									case PAR_YIELD_CURVE_UNADJUSTED_METHOD: {
										getYieldCurveMethod(cst.getValuationMethod()).ifPresent(cashSettlement::setParYieldCurveUnadjustedMethod);
										break;
									}
									case ZERO_COUPON_YIELD_ADJUSTED_METHOD: {
										getYieldCurveMethod(cst.getValuationMethod()).ifPresent(cashSettlement::setZeroCouponYieldAdjustedMethod);
										break;
									}
									case CROSS_CURRENCY_METHOD: {
										getCrossCurrencyMethod(cst.getValuationMethod()).ifPresent(cashSettlement::setCrossCurrencyMethod);
										break;
									}
								}
							});
						Optional.ofNullable(cst.getValuationDate())
							.map(ValuationDate::getValuationDate)
							.flatMap(this::getRelativeDateOffset)
							.ifPresent(cashSettlement::setCashSettlementValuationDate);
						getBusinessCenterTime(cst.getValuationTime()).ifPresent(cashSettlement::setCashSettlementValuationTime);
					});
				getCashSettlementPaymentDate(st.getSettlementDate()).ifPresent(cashSettlement::setCashSettlementPaymentDate);
				return cashSettlement;
			});
	}

	private Optional<CashSettlementPaymentDate> getCashSettlementPaymentDate(cdm.product.common.settlement.SettlementDate cdmSettlementDate) {
		return Optional.ofNullable(cdmSettlementDate)
			.map(d -> {
				CashSettlementPaymentDate cashSettlementPaymentDate = objectFactory.createCashSettlementPaymentDate();
				getAdjustableDates(d.getAdjustableDates()).ifPresent(cashSettlementPaymentDate::setAdjustableDates);
				//				getBusinessDateRange(d.getBusinessDateRange()).ifPresent(cashSettlementPaymentDate::setBusinessDateRange);
				Optional.ofNullable(d.getAdjustableOrRelativeDate()).map(AdjustableOrAdjustedOrRelativeDate::getRelativeDate).flatMap(this::getRelativeDateOffset).ifPresent(cashSettlementPaymentDate::setRelativeDate);
				return cashSettlementPaymentDate;
			});
	}

	private Optional<CrossCurrencyMethod> getCrossCurrencyMethod(ValuationMethod cdmValuationMethod) {
		return Optional.ofNullable(cdmValuationMethod)
			.map(m -> {
				CrossCurrencyMethod crossCurrencyMethod = objectFactory.createCrossCurrencyMethod();
				// TODO CashSettlementCurrency
				getQuotationRateTypeEnum(m.getQuotationMethod()).ifPresent(crossCurrencyMethod::setQuotationRateType);
				// TODO CashSettlementReferenceBanks
				return crossCurrencyMethod;
			});
	}

	private Optional<YieldCurveMethod> getYieldCurveMethod(ValuationMethod cdmValuationMethod) {
		return Optional.ofNullable(cdmValuationMethod)
			.map(m -> {
				YieldCurveMethod yieldCurveMethod = objectFactory.createYieldCurveMethod();
				getQuotationRateTypeEnum(m.getQuotationMethod()).ifPresent(yieldCurveMethod::setQuotationRateType);
				getSettlementRateSource(m.getValuationSource()).ifPresent(yieldCurveMethod::setSettlementRateSource);
				return yieldCurveMethod;
			});
	}

	private Optional<SettlementRateSource> getSettlementRateSource(ValuationSource cdmValuationSource) {
		return Optional.ofNullable(cdmValuationSource)
			.map(s -> {
				SettlementRateSource settlementRateSource = objectFactory.createSettlementRateSource();
				getInformationSource(s.getInformationSource()).ifPresent(settlementRateSource::setInformationSource);
				// TODO CashSettlementReferenceBanks
				return settlementRateSource;
			});
	}

	private Optional<InformationSource> getInformationSource(FxSpotRateSource cdmFxSpotRateSource) {
		return Optional.ofNullable(cdmFxSpotRateSource)
			.map(FxSpotRateSource::getPrimarySource)
			.map(s -> {
				InformationSource informationSource = objectFactory.createInformationSource();
				getSourcePage(s.getSourcePage()).ifPresent(informationSource::setRateSourcePage);
				Optional.ofNullable(s.getSourcePageHeading()).ifPresent(informationSource::setRateSourcePageHeading);
				getInformationProvider(s.getSourceProvider()).ifPresent(informationSource::setRateSource);
				return informationSource;
			});
	}

	private Optional<InformationProvider> getInformationProvider(FieldWithMetaInformationProviderEnum cdmSourceProvider) {
		return Optional.ofNullable(cdmSourceProvider)
			.map(s -> {
				InformationProvider informationProvider = objectFactory.createInformationProvider();
				getValue(s).ifPresent(informationProvider::setValue);
				getScheme(s.getMeta()).ifPresent(informationProvider::setInformationProviderScheme);
				return informationProvider;
			});
	}

	private Optional<RateSourcePage> getSourcePage(FieldWithMetaString cdmSourcePage) {
		return Optional.ofNullable(cdmSourcePage)
			.map(s -> {
				RateSourcePage rateSourcePage = objectFactory.createRateSourcePage();
				getValue(s).ifPresent(rateSourcePage::setValue);
				getScheme(s.getMeta()).ifPresent(rateSourcePage::setRateSourcePageScheme);
				return rateSourcePage;
			});
	}

	private Optional<CashPriceMethod> getCashPriceMethod(ValuationMethod cdmValuationMethod) {
		return Optional.ofNullable(cdmValuationMethod)
			.map(m -> {
				CashPriceMethod cashPriceMethod = objectFactory.createCashPriceMethod();
				getCurrency(m.getQuotationAmount().getUnit()).ifPresent(cashPriceMethod::setCashSettlementCurrency);
				getQuotationRateTypeEnum(m.getQuotationMethod()).ifPresent(cashPriceMethod::setQuotationRateType);
				return cashPriceMethod;
			});
	}

	private Optional<QuotationRateTypeEnum> getQuotationRateTypeEnum(cdm.observable.asset.QuotationRateTypeEnum cdmQuotationRateType) {
		return Optional.ofNullable(cdmQuotationRateType)
			.map(Enum::name)
			.map(n -> CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, n))
			.map(QuotationRateTypeEnum::fromValue);
	}

	private Optional<SwaptionPhysicalSettlement> getSwaptionPhysicalSettlement(PhysicalSettlementTerms cdmPhysicalSettlementTerms) {
		return Optional.ofNullable(cdmPhysicalSettlementTerms)
			.map(s -> {
				SwaptionPhysicalSettlement swaptionPhysicalSettlement = objectFactory.createSwaptionPhysicalSettlement();
				Optional.ofNullable(s.getClearedPhysicalSettlement()).ifPresent(swaptionPhysicalSettlement::setClearedPhysicalSettlement);
				return swaptionPhysicalSettlement;
			});
	}

	private Optional<EuropeanExercise> getEuropeanExercise(OptionExercise cdmOptionExercise) {
		return Optional.ofNullable(cdmOptionExercise)
			.map(OptionExercise::getOptionStyle)
			.map(OptionStyle::getEuropeanExercise)
			.map(e -> {
				EuropeanExercise europeanExercise = objectFactory.createEuropeanExercise();
				getExternalKey(e.getMeta()).ifPresent(europeanExercise::setId);
				getBusinessCenterTime(e.getEarliestExerciseTime()).ifPresent(europeanExercise::setEarliestExerciseTime);
				// TODO ExerciseFee

				Optional.ofNullable(e.getExpirationDate())
					.stream().flatMap(Collection::stream).findFirst()
					.flatMap(this::getAdjustableOrRelativeDate)
					.ifPresent(europeanExercise::setExpirationDate);

				getBusinessCenterTime(e.getExpirationTime()).ifPresent(europeanExercise::setExpirationTime);
				// TODO PartialExercise
				getAdjustableOrRelativeDates(e.getRelevantUnderlyingDate()).ifPresent(europeanExercise::setRelevantUnderlyingDate);
				return europeanExercise;
			});
	}

	private Optional<BusinessCenterTime> getBusinessCenterTime(cdm.base.datetime.BusinessCenterTime cdmBusinessCenterTime) {
		return Optional.ofNullable(cdmBusinessCenterTime)
			.map(t -> {
				BusinessCenterTime businessCenterTime = objectFactory.createBusinessCenterTime();
				getBusinessCenter(t.getBusinessCenter()).ifPresent(businessCenterTime::setBusinessCenter);
				getHourMinuteTime(t.getHourMinuteTime()).ifPresent(businessCenterTime::setHourMinuteTime);
				return businessCenterTime;
			});
	}

	private List<Payment> getPayments(List<? extends Counterparty> cdmCounterparties, List<? extends PriceQuantity> cdmPriceQuantity) {
		return emptyIfNull(cdmPriceQuantity).stream()
			.map(pq -> getPayment(pq, cdmCounterparties))
			.flatMap(Optional::stream)
			.collect(Collectors.toList());
	}

	private Optional<Payment> getPayment(PriceQuantity cdmPriceQuantity,  List<? extends Counterparty> cdmCounterparties) {
		return getPricePayment(cdmPriceQuantity)
			.map(pricePayment -> {
				Payment payment = objectFactory.createPayment();
				getNonNegativeMoney(pricePayment).ifPresent(payment::setPaymentAmount);
				Optional.ofNullable(cdmPriceQuantity.getSettlementTerms()).map(SettlementBase::getSettlementDate)
					.map(SettlementDate::getAdjustableOrRelativeDate).flatMap(this::getAdjustableOrAdjustedDate).ifPresent(payment::setPaymentDate);
				getBuyerPartyReference(cdmPriceQuantity.getBuyerSeller(), cdmCounterparties).ifPresent(payment::setPayerPartyReference);
				getSellerPartyReference(cdmPriceQuantity.getBuyerSeller(), cdmCounterparties).ifPresent(payment::setReceiverPartyReference);
				return payment;
		});
	}

	private Optional<PriceSchedule> getPricePayment(PriceQuantity pq) {
		return Optional.ofNullable(pq)
			.map(PriceQuantity::getPrice)
			.map(Collection::stream).orElse(Stream.of())
			.map(FieldWithMetaPriceSchedule::getValue)
			.filter(p -> Optional.ofNullable(p)
				.map(PriceSchedule::getPriceType)
				.map(t -> t == PriceTypeEnum.CASH_PRICE)
				.orElse(false))
			.findFirst();
	}

	private List<InterestRateStream> getSwapStreams(Product cdmProduct, List<? extends Counterparty> cdmCounterparties) {
		return Optional.ofNullable(cdmProduct)
			.map(Product::getContractualProduct)
			.map(ContractualProduct::getEconomicTerms)
			.map(EconomicTerms::getPayout)
			.map(Payout::getInterestRatePayout)
			.stream().flatMap(Collection::stream)
			.map(p -> getInterestRateStream(p, cdmCounterparties))
			.flatMap(Optional::stream)
			.collect(Collectors.toList());
	}

	private Optional<InterestRateStream> getInterestRateStream(InterestRatePayout cdmInterestRatePayout, List<? extends Counterparty> cdmCounterparties) {
		return Optional.ofNullable(cdmInterestRatePayout)
			.map(p -> {
				InterestRateStream interestRateStream = objectFactory.createInterestRateStream();
				getExternalKey(p.getMeta()).ifPresent(interestRateStream::setId);
				getPayerPartyReference(p.getPayerReceiver(), cdmCounterparties).ifPresent(interestRateStream::setPayerPartyReference);
				getReceiverPartyReference(p.getPayerReceiver(), cdmCounterparties).ifPresent(interestRateStream::setReceiverPartyReference);
				getCalculationPeriodDates(p.getCalculationPeriodDates()).ifPresent(interestRateStream::setCalculationPeriodDates);
				getPaymentDates(p.getPaymentDates()).ifPresent(interestRateStream::setPaymentDates);
				getResetDates(p.getResetDates()).ifPresent(interestRateStream::setResetDates);
				getCalculationPeriodAmount(p).ifPresent(interestRateStream::setCalculationPeriodAmount);
				getSettlementProvision(p.getSettlementTerms()).ifPresent(interestRateStream::setSettlementProvision);
				getCashflows(p.getCashflowRepresentation()).ifPresent(interestRateStream::setCashflows);
				return interestRateStream;
			});
	}

	private Optional<PartyReference> getPayerPartyReference(PayerReceiver cdmPayerReceiver, List<? extends Counterparty> cdmCounterparties) {
		return Optional.ofNullable(cdmPayerReceiver)
			.map(PayerReceiver::getPayer)
			.flatMap(payer -> getPartyReference(payer, cdmCounterparties));
	}

	private Optional<PartyReference> getReceiverPartyReference(PayerReceiver cdmPayerReceiver, List<? extends Counterparty> cdmCounterparties) {
		return Optional.ofNullable(cdmPayerReceiver)
			.map(PayerReceiver::getReceiver)
			.flatMap(receiver -> getPartyReference(receiver, cdmCounterparties));
	}

	private Optional<PartyReference> getBuyerPartyReference(BuyerSeller cdmBuyerSeller, List<? extends Counterparty> cdmCounterparties) {
		return Optional.ofNullable(cdmBuyerSeller)
			.map(BuyerSeller::getBuyer)
			.flatMap(buyer -> getPartyReference(buyer, cdmCounterparties));
	}

	private Optional<PartyReference> getSellerPartyReference(BuyerSeller cdmBuyerSeller, List<? extends Counterparty> cdmCounterparties) {
		return Optional.ofNullable(cdmBuyerSeller)
			.map(BuyerSeller::getSeller)
			.flatMap(seller -> getPartyReference(seller, cdmCounterparties));
	}

	private Optional<Cashflows> getCashflows(CashflowRepresentation cdmCashflowRepresentation) {
		return Optional.ofNullable(cdmCashflowRepresentation)
			.map(r -> {
				Cashflows cashflows = objectFactory.createCashflows();
				cashflows.getPaymentCalculationPeriod().addAll(getPaymentCalculationPeriods(r.getPaymentCalculationPeriod()));
				// TODO
				return cashflows;
			});
	}

	private List<PaymentCalculationPeriod> getPaymentCalculationPeriods(List<? extends cdm.product.common.schedule.PaymentCalculationPeriod> cdmPaymentCalculationPeriods) {
		return emptyIfNull(cdmPaymentCalculationPeriods).stream()
			.map(p -> {
				PaymentCalculationPeriod paymentCalculationPeriod = objectFactory.createPaymentCalculationPeriod();
				paymentCalculationPeriod.getCalculationPeriod().addAll(Optional.ofNullable(p.getCalculationPeriod()).stream()
					.flatMap(Collection::stream)
					.map(this::getCalculationPeriod)
					.flatMap(Optional::stream)
					.collect(Collectors.toList()));
				getDate(p.getAdjustedPaymentDate()).ifPresent(paymentCalculationPeriod::setAdjustedPaymentDate);
				getDate(p.getUnadjustedPaymentDate()).ifPresent(paymentCalculationPeriod::setUnadjustedPaymentDate);
				Optional.ofNullable(p.getDiscountFactor()).ifPresent(paymentCalculationPeriod::setDiscountFactor);
				getMoney(p.getFixedPaymentAmount()).map(Money::getAmount).ifPresent(paymentCalculationPeriod::setFixedPaymentAmount);
				getMoney(p.getForecastPaymentAmount()).ifPresent(paymentCalculationPeriod::setForecastPaymentAmount);
				getMoney(p.getPresentValueAmount()).ifPresent(paymentCalculationPeriod::setPresentValueAmount);
				return paymentCalculationPeriod;
			})
			.collect(Collectors.toList());
	}

	private Optional<Money> getMoney(cdm.observable.asset.Money cdmMoney) {
		return Optional.ofNullable(cdmMoney)
			.map(m -> {
				Money money = objectFactory.createMoney();
				Optional.ofNullable(m.getValue()).ifPresent(money::setAmount);
				getCurrency(m.getUnit().getCurrency()).ifPresent(money::setCurrency);
				return money;
			});
	}

	private Optional<NonNegativeMoney> getNonNegativeMoney(PriceSchedule cdmPrice) {
		return Optional.ofNullable(cdmPrice)
			.map(p -> {
				NonNegativeMoney nonNegativeMoney = objectFactory.createNonNegativeMoney();
				Optional.ofNullable(p.getValue()).ifPresent(nonNegativeMoney::setAmount);
				getCurrency(p.getUnit()).ifPresent(nonNegativeMoney::setCurrency);
				return nonNegativeMoney;
			});
	}

	private Optional<CalculationPeriod> getCalculationPeriod(cdm.product.common.schedule.CalculationPeriod cdmCalculationPeriod) {
		return Optional.of(cdmCalculationPeriod)
			.map(p -> {
				CalculationPeriod calculationPeriod = objectFactory.createCalculationPeriod();
				getDate(p.getUnadjustedStartDate()).ifPresent(calculationPeriod::setUnadjustedStartDate);
				getDate(p.getUnadjustedEndDate()).ifPresent(calculationPeriod::setUnadjustedEndDate);
				getDate(p.getAdjustedStartDate()).ifPresent(calculationPeriod::setAdjustedStartDate);
				getDate(p.getAdjustedStartDate()).ifPresent(calculationPeriod::setAdjustedEndDate);
				getBigInteger(p.getCalculationPeriodNumberOfDays()).ifPresent(calculationPeriod::setCalculationPeriodNumberOfDays);
				Optional.ofNullable(p.getDayCountYearFraction()).ifPresent(calculationPeriod::setDayCountYearFraction);
				getFloatingRateDefinition(p.getFloatingRateDefinition()).ifPresent(calculationPeriod::setFloatingRateDefinition);
				getMoney(p.getForecastAmount()).ifPresent(calculationPeriod::setForecastAmount);
				Optional.ofNullable(p.getFixedRate()).ifPresent(calculationPeriod::setFixedRate);
				Optional.ofNullable(p.getForecastRate()).ifPresent(calculationPeriod::setForecastRate);
				getFxLinkedNotionalAmount(p.getFxLinkedNotionalAmount()).ifPresent(calculationPeriod::setFxLinkedNotionalAmount);
				Optional.ofNullable(p.getNotionalAmount()).ifPresent(calculationPeriod::setNotionalAmount);
				return calculationPeriod;
			});
	}

	private Optional<FloatingRateDefinition> getFloatingRateDefinition(cdm.product.asset.FloatingRateDefinition cdmFloatingRateDefinition) {
		return Optional.ofNullable(cdmFloatingRateDefinition)
			.map(d -> {
				FloatingRateDefinition floatingRateDefinition = objectFactory.createFloatingRateDefinition();
				Optional.ofNullable(d.getCalculatedRate()).ifPresent(floatingRateDefinition::setCalculatedRate);
				floatingRateDefinition.getCapRate().addAll(getStrike(d.getCapRate()));
				Optional.ofNullable(d.getFloatingRateMultiplier()).ifPresent(floatingRateDefinition::setFloatingRateMultiplier);
				floatingRateDefinition.getFloorRate().addAll(getStrike(d.getFloorRate()));
				floatingRateDefinition.getRateObservation().addAll(getRateObservation(d.getRateObservation()));
				Optional.ofNullable(d.getSpread()).ifPresent(floatingRateDefinition::setSpread);
				return floatingRateDefinition;
			});
	}

	private List<RateObservation> getRateObservation(List<? extends cdm.observable.asset.RateObservation> cdmRateObservation) {
		return emptyIfNull(cdmRateObservation).stream()
			.map(o -> {
				RateObservation rateObservation = objectFactory.createRateObservation();
				getDate(o.getAdjustedFixingDate()).ifPresent(rateObservation::setAdjustedFixingDate);
				getBigInteger(o.getObservationWeight()).ifPresent(rateObservation::setObservationWeight);
				Optional.ofNullable(o.getObservedRate()).ifPresent(rateObservation::setObservedRate);
				// TODO
				return rateObservation;
			})
			.collect(Collectors.toList());
	}

	private List<Strike> getStrike(List<? extends cdm.product.template.Strike> cdmStrikes) {
		return emptyIfNull(cdmStrikes).stream()
			.map(s -> {
				Strike strike = objectFactory.createStrike();
				getIdentifiedPayerReceiver(s.getBuyer()).ifPresent(strike::setBuyer);
				getIdentifiedPayerReceiver(s.getSeller()).ifPresent(strike::setSeller);
				Optional.ofNullable(s.getStrikeRate()).ifPresent(strike::setStrikeRate);
				return strike;
			})
			.collect(Collectors.toList());
	}

	private Optional<IdentifiedPayerReceiver> getIdentifiedPayerReceiver(cdm.base.staticdata.party.PayerReceiverEnum cdmPayerReceiverEnum) {
		return Optional.ofNullable(cdmPayerReceiverEnum)
			.map(Enum::name)
			.map(org.fpml.fpml_5.confirmation.PayerReceiverEnum::fromValue)
			.map(e -> {
				IdentifiedPayerReceiver identifiedPayerReceiver = objectFactory.createIdentifiedPayerReceiver();
				identifiedPayerReceiver.setValue(e);
				return identifiedPayerReceiver;
			});
	}

	private Optional<FxLinkedNotionalAmount> getFxLinkedNotionalAmount(cdm.product.common.schedule.FxLinkedNotionalAmount cdmFxLinkedNotionalAmount) {
		return Optional.ofNullable(cdmFxLinkedNotionalAmount)
			.map(a -> {
				FxLinkedNotionalAmount fxLinkedNotionalAmount = objectFactory.createFxLinkedNotionalAmount();
				getDate(a.getAdjustedFxSpotFixingDate()).ifPresent(fxLinkedNotionalAmount::setAdjustedFxSpotFixingDate);
				Optional.ofNullable(a.getNotionalAmount()).ifPresent(fxLinkedNotionalAmount::setNotionalAmount);
				Optional.ofNullable(a.getObservedFxSpotRate()).ifPresent(fxLinkedNotionalAmount::setObservedFxSpotRate);
				getDate(a.getResetDate()).ifPresent(fxLinkedNotionalAmount::setResetDate);
				return fxLinkedNotionalAmount;
			});
	}

	private Optional<SettlementProvision> getSettlementProvision(cdm.product.common.settlement.SettlementTerms settlementTerms) {
		return Optional.ofNullable(settlementTerms)
			.map(s -> {
				SettlementProvision settlementProvision = objectFactory.createSettlementProvision();
				getCurrency(s.getSettlementCurrency()).ifPresent(settlementProvision::setSettlementCurrency);
				Optional.ofNullable(s.getCashSettlementTerms())
					.map(Collection::stream)
					.flatMap(Stream::findFirst)
					.flatMap(this::getNonDeliverableSettlement)
					.ifPresent(settlementProvision::setNonDeliverableSettlement);
				return settlementProvision;
			});
	}

	private Optional<NonDeliverableSettlement> getNonDeliverableSettlement(cdm.product.common.settlement.CashSettlementTerms nonDeliverableSettlement) {
		return Optional.ofNullable(nonDeliverableSettlement)
			.map(d -> {
				NonDeliverableSettlement settlement = objectFactory.createNonDeliverableSettlement();
				Optional.ofNullable(d.getValuationDate())
					.map(cdm.product.common.settlement.ValuationDate::getFxFixingDate)
					.flatMap(this::getFxFixingDate)
					.ifPresent(settlement::setFxFixingDate);
				Optional.ofNullable(d.getCashSettlementAmount())
					.map(cdm.observable.asset.Money::getUnit)
					.flatMap(this::getCurrency)
					.ifPresent(settlement::setReferenceCurrency);
				Optional.ofNullable(d.getValuationMethod())
					.map(ValuationMethod::getValuationSource)
					.map(ValuationSource::getSettlementRateOption)
					.map(cdm.observable.asset.SettlementRateOption::getSettlementRateOption)
					.flatMap(this::getSettlementRateOption)
					.ifPresent(settlement::setSettlementRateOption);
				return settlement;
			});
	}

	private Optional<SettlementRateOption> getSettlementRateOption(FieldWithMetaSettlementRateOptionEnum settlementRateOption) {
		return Optional.ofNullable(settlementRateOption)
			.map(s -> {
				SettlementRateOption rateOption = objectFactory.createSettlementRateOption();
				Optional.ofNullable(s.getValue()).map(Objects::toString).ifPresent(rateOption::setValue);
				return rateOption;
			});
	}

	private Optional<FxFixingDate> getFxFixingDate(cdm.product.common.settlement.FxFixingDate fxFixingDate) {
		return Optional.ofNullable(fxFixingDate)
			.map(f -> {
				FxFixingDate fixingDate = objectFactory.createFxFixingDate();
				getExternalKey(f.getMeta()).ifPresent(fixingDate::setId);
				getPeriodEnum(f.getPeriod()).ifPresent(fixingDate::setPeriod);
				getBusinessCenters(f.getBusinessCenters()).ifPresent(fixingDate::setBusinessCenters);
				getDayTypeEnum(f.getDayType()).ifPresent(fixingDate::setDayType);
				Optional.ofNullable(f.getBusinessDayConvention())
					.map(Object::toString)
					.map(BusinessDayConventionEnum::valueOf)
					.ifPresent(fixingDate::setBusinessDayConvention);
				getBigInteger(f.getPeriodMultiplier()).ifPresent(fixingDate::setPeriodMultiplier);
				getDateRelativeToCalculationPeriodDates(f.getDateRelativeToCalculationPeriodDates()).ifPresent(
					fixingDate::setDateRelativeToCalculationPeriodDates);
				getDateRelativeToPaymentDates(f.getDateRelativeToPaymentDates()).ifPresent(fixingDate::setDateRelativeToPaymentDates);
				return fixingDate;
			});
	}

	private Optional<DateRelativeToCalculationPeriodDates> getDateRelativeToCalculationPeriodDates(
		cdm.product.common.schedule.DateRelativeToCalculationPeriodDates cdmDateRelativeToCalculationPeriodDates) {
		return Optional.ofNullable(cdmDateRelativeToCalculationPeriodDates)
			.map(d -> {
				DateRelativeToCalculationPeriodDates dateRelativeToCalculationPeriodDates = objectFactory.createDateRelativeToCalculationPeriodDates();
				dateRelativeToCalculationPeriodDates.getCalculationPeriodDatesReference().addAll(
					emptyIfNull(d.getCalculationPeriodDatesReference()).stream()
						.map(this::getCalculationPeriodDatesReference)
						.flatMap(Optional::stream)
						.collect(Collectors.toList()));
				return dateRelativeToCalculationPeriodDates;
			});
	}

	private Optional<DateRelativeToPaymentDates> getDateRelativeToPaymentDates(
		cdm.product.common.schedule.DateRelativeToPaymentDates cdmDateRelativeToPaymentDates) {
		return Optional.ofNullable(cdmDateRelativeToPaymentDates)
			.map(d -> {
				DateRelativeToPaymentDates dateRelativeToPaymentDates = objectFactory.createDateRelativeToPaymentDates();
				dateRelativeToPaymentDates.getPaymentDatesReference().addAll(
					emptyIfNull(d.getPaymentDatesReference()).stream()
						.map(this::getPaymentDatesReference)
						.flatMap(Optional::stream)
						.collect(Collectors.toList()));
				return dateRelativeToPaymentDates;
			});
	}

	private Optional<Offset> getOffset(cdm.base.datetime.Offset cdmOffset) {
		return Optional.ofNullable(cdmOffset)
			.map(f -> {
				Offset offset = objectFactory.createFxFixingDate();
				getPeriodEnum(f.getPeriod()).ifPresent(offset::setPeriod);
				getDayTypeEnum(f.getDayType()).ifPresent(offset::setDayType);
				getBigInteger(f.getPeriodMultiplier()).ifPresent(offset::setPeriodMultiplier);
				return offset;
			});
	}

	private Optional<PartyReference> getPartyReference(AncillaryRoleEnum cdmAncillaryRoleEnum, List<? extends AncillaryParty> cdmAncillaryParties) {
		return Optional.ofNullable(cdmAncillaryRoleEnum)
			.flatMap(c -> getAncillaryPartyExternalReference(c, cdmAncillaryParties))
			.flatMap(this::getPartyReference);
	}

	private Optional<PartyReference> getPartyReference(CounterpartyRoleEnum cdmCounterpartyRoleEnum, List<? extends Counterparty> cdmCounterparties) {
		return Optional.ofNullable(cdmCounterpartyRoleEnum)
			.flatMap(c -> getCounterpartyExternalReference(c, cdmCounterparties))
			.flatMap(this::getPartyReference);
	}

	private Optional<PartyReference> getPartyReference(ReferenceWithMetaParty cdmPartyReference) {
		return Optional.ofNullable(cdmPartyReference)
			.map(ReferenceWithMeta::getExternalReference)
			.flatMap(this::getPartyReference);
	}

	private Optional<PartyReference> getPartyReference(String cdmPartyRef) {
		return Optional.ofNullable(cdmPartyRef)
			.map(r -> {
				Party partyRef = objectFactory.createParty();
				partyRef.setId(r);
				PartyReference partyReference = objectFactory.createPartyReference();
				partyReference.setHref(partyRef);
				return partyReference;
			});
	}

	private Optional<String> getCounterpartyExternalReference(CounterpartyRoleEnum cdmCounterpartyRoleEnum, List<? extends Counterparty> cdmCounterparties) {
		return emptyIfNull(cdmCounterparties).stream()
			.filter(c -> c.getRole() == cdmCounterpartyRoleEnum)
			.map(Counterparty::getPartyReference)
			.map(ReferenceWithMeta::getExternalReference)
			.findFirst();
	}

	private Optional<String> getAncillaryPartyExternalReference(AncillaryRoleEnum cdmAncillaryRoleEnum, List<? extends AncillaryParty> cdmAncillaryParties) {
		return emptyIfNull(cdmAncillaryParties).stream()
			.filter(c -> c.getRole() == cdmAncillaryRoleEnum)
			.map(AncillaryParty::getPartyReference)
			.flatMap(Collection::stream)
			.findFirst()
			.map(ReferenceWithMeta::getExternalReference);
	}

	private Optional<CalculationPeriodDates> getCalculationPeriodDates(cdm.product.common.schedule.CalculationPeriodDates cdmCalculationPeriodDates) {
		return Optional.ofNullable(cdmCalculationPeriodDates)
			.map(d -> {
				CalculationPeriodDates calculationPeriodDates = objectFactory.createCalculationPeriodDates();
				getExternalKey(d.getMeta()).ifPresent(calculationPeriodDates::setId);
				Optional.ofNullable(d.getEffectiveDate())
					.flatMap(ed -> getAdjustableDate(ed.getAdjustableDate(), ed.getMeta()))
					.ifPresent(calculationPeriodDates::setEffectiveDate);
				Optional.ofNullable(d.getTerminationDate())
					.flatMap(td -> getAdjustableDate(td.getAdjustableDate(), td.getMeta()))
					.ifPresent(calculationPeriodDates::setTerminationDate);
				getBusinessDayAdjustments(d.getCalculationPeriodDatesAdjustments()).ifPresent(calculationPeriodDates::setCalculationPeriodDatesAdjustments);
				getCalculationPeriodFrequency(d.getCalculationPeriodFrequency()).ifPresent(calculationPeriodDates::setCalculationPeriodFrequency);
				return calculationPeriodDates;
			});
	}

	private Optional<AdjustableOrRelativeDates> getAdjustableOrRelativeDates(cdm.base.datetime.AdjustableOrRelativeDates cdmAdjustableOrRelativeDates) {
		return Optional.ofNullable(cdmAdjustableOrRelativeDates)
			.map(d -> {
				AdjustableOrRelativeDates adjustableOrRelativeDates = objectFactory.createAdjustableOrRelativeDates();
				getAdjustableDates(d.getAdjustableDates()).ifPresent(adjustableOrRelativeDates::setAdjustableDates);
				getRelativeDates(d.getRelativeDates()).ifPresent(adjustableOrRelativeDates::setRelativeDates);
				return adjustableOrRelativeDates;
			});
	}

	private Optional<AdjustableOrRelativeDate> getAdjustableOrRelativeDate(cdm.base.datetime.AdjustableOrRelativeDate cdmAdjustableOrRelativeDate) {
		return Optional.ofNullable(cdmAdjustableOrRelativeDate)
			.map(d -> {
				AdjustableOrRelativeDate adjustableOrRelativeDate = objectFactory.createAdjustableOrRelativeDate();
				getAdjustableDate(d.getAdjustableDate(), d.getMeta()).ifPresent(adjustableOrRelativeDate::setAdjustableDate);
				getAdjustedRelativeDateOffset(d.getRelativeDate()).ifPresent(adjustableOrRelativeDate::setRelativeDate);
				getExternalKey(d.getMeta()).ifPresent(adjustableOrRelativeDate::setId);
				return adjustableOrRelativeDate;
			});
	}

	private Optional<AdjustableDates> getAdjustableDates(cdm.base.datetime.AdjustableDates cdmAdjustableDates) {
		return Optional.ofNullable(cdmAdjustableDates)
			.map(d -> {
				AdjustableDates adjustableDates = objectFactory.createAdjustableDates();
				adjustableDates.getAdjustedDate().addAll(emptyIfNull(d.getAdjustedDate()).stream()
					.map(FieldWithMetaDate::getValue)
					.map(this::getIdentifiedDate)
					.flatMap(Optional::stream)
					.collect(Collectors.toList()));
				getBusinessDayAdjustments(d.getDateAdjustments()).ifPresent(adjustableDates::setDateAdjustments);
				return adjustableDates;
			});
	}

	private Optional<AdjustableDate> getAdjustableDate(cdm.base.datetime.AdjustableDate cdmAdjustableDate, GlobalKeyFields meta) {
		return Optional.ofNullable(cdmAdjustableDate)
			.map(d -> {
				AdjustableDate adjustableDate = objectFactory.createAdjustableDate();
				getIdentifiedDate(d.getUnadjustedDate()).ifPresent(adjustableDate::setUnadjustedDate);
				Optional.ofNullable(d.getAdjustedDate()).map(FieldWithMetaDate::getValue).flatMap(this::getIdentifiedDate).ifPresent(adjustableDate::setAdjustedDate);
				getBusinessDayAdjustments(d.getDateAdjustments()).ifPresent(adjustableDate::setDateAdjustments);
				getExternalKey(meta).ifPresent(adjustableDate::setId);
				return adjustableDate;
			});
	}

	private Optional<AdjustableOrAdjustedDate> getAdjustableOrAdjustedDate(AdjustableOrAdjustedOrRelativeDate cdmAdjustableOrAdjustedOrRelativeDate) {
		return Optional.ofNullable(cdmAdjustableOrAdjustedOrRelativeDate)
			.map(d -> {
				AdjustableOrAdjustedDate adjustableOrAdjustedDate = objectFactory.createAdjustableOrAdjustedDate();
				List<JAXBElement<?>> content = adjustableOrAdjustedDate.getContent();
				getIdentifiedDate(d.getUnadjustedDate())
					.map(id -> getJAXBElement("unadjustedDate", IdentifiedDate.class, id))
					.ifPresent(content::add);
				getIdentifiedDate(d.getAdjustedDate())
					.map(id -> getJAXBElement("adjustedDate", IdentifiedDate.class, id))
					.ifPresent(content::add);
				getBusinessDayAdjustments(d.getDateAdjustments())
					.map(a -> getJAXBElement("dateAdjustments", BusinessDayAdjustments.class, a))
					.ifPresent(content::add);
				return adjustableOrAdjustedDate;
			});
	}

	private <T> JAXBElement<T> getJAXBElement(String localPart, Class<T> clazz, T value) {
		return new JAXBElement<>(new QName("http://www.fpml.org/FpML-5/confirmation", localPart), clazz, value);
	}

	private Optional<AdjustedRelativeDateOffset> getAdjustedRelativeDateOffset(cdm.base.datetime.AdjustedRelativeDateOffset cdmAdjustedRelativeDateOffset) {
		return Optional.ofNullable(cdmAdjustedRelativeDateOffset)
			.map(d -> {
				AdjustedRelativeDateOffset adjustedRelativeDateOffset = objectFactory.createAdjustedRelativeDateOffset();
				getExternalKey(d.getMeta()).ifPresent(adjustedRelativeDateOffset::setId);
				Optional.ofNullable(d.getAdjustedDate()).flatMap(this::getIdentifiedDate).ifPresent(adjustedRelativeDateOffset::setAdjustedDate);
				getBusinessCenters(d.getBusinessCenters()).ifPresent(adjustedRelativeDateOffset::setBusinessCenters);
				getBusinessCentersReference(d.getBusinessCentersReference()).ifPresent(adjustedRelativeDateOffset::setBusinessCentersReference);
				getBusinessDayConventionEnum(d.getBusinessDayConvention()).ifPresent(adjustedRelativeDateOffset::setBusinessDayConvention);
				getDateRelativeTo(d.getDateRelativeTo()).ifPresent(adjustedRelativeDateOffset::setDateRelativeTo);
				getDayTypeEnum(d.getDayType()).ifPresent(adjustedRelativeDateOffset::setDayType);
				getPeriodEnum(d.getPeriod()).ifPresent(adjustedRelativeDateOffset::setPeriod);
				getBigInteger(d.getPeriodMultiplier()).ifPresent(adjustedRelativeDateOffset::setPeriodMultiplier);
				getBusinessDayAdjustments(d.getRelativeDateAdjustments()).ifPresent(adjustedRelativeDateOffset::setRelativeDateAdjustments);
				return adjustedRelativeDateOffset;
			});
	}

	private Optional<RelativeDateOffset> getRelativeDateOffset(cdm.base.datetime.RelativeDateOffset cdmRelativeDateOffset) {
		return Optional.ofNullable(cdmRelativeDateOffset)
			.map(d -> {
				RelativeDateOffset relativeDateOffset = objectFactory.createRelativeDateOffset();
				getExternalKey(d.getMeta()).ifPresent(relativeDateOffset::setId);
				Optional.ofNullable(d.getAdjustedDate()).flatMap(this::getIdentifiedDate).ifPresent(relativeDateOffset::setAdjustedDate);
				getBusinessCenters(d.getBusinessCenters()).ifPresent(relativeDateOffset::setBusinessCenters);
				getBusinessCentersReference(d.getBusinessCentersReference()).ifPresent(relativeDateOffset::setBusinessCentersReference);
				getBusinessDayConventionEnum(d.getBusinessDayConvention()).ifPresent(relativeDateOffset::setBusinessDayConvention);
				getDateRelativeTo(d.getDateRelativeTo()).ifPresent(relativeDateOffset::setDateRelativeTo);
				getDayTypeEnum(d.getDayType()).ifPresent(relativeDateOffset::setDayType);
				getPeriodEnum(d.getPeriod()).ifPresent(relativeDateOffset::setPeriod);
				getBigInteger(d.getPeriodMultiplier()).ifPresent(relativeDateOffset::setPeriodMultiplier);
				return relativeDateOffset;
			});
	}

	private Optional<RelativeDates> getRelativeDates(cdm.base.datetime.RelativeDates cdmRelativeDates) {
		return Optional.ofNullable(cdmRelativeDates)
			.map(d -> {
				RelativeDates relativeDates = objectFactory.createRelativeDates();
				getExternalKey(d.getMeta()).ifPresent(relativeDates::setId);
				Optional.ofNullable(d.getAdjustedDate()).flatMap(this::getIdentifiedDate).ifPresent(relativeDates::setAdjustedDate);
				getBusinessCenters(d.getBusinessCenters()).ifPresent(relativeDates::setBusinessCenters);
				getBusinessCentersReference(d.getBusinessCentersReference()).ifPresent(relativeDates::setBusinessCentersReference);
				getBusinessDayConventionEnum(d.getBusinessDayConvention()).ifPresent(relativeDates::setBusinessDayConvention);
				getDateRelativeTo(d.getDateRelativeTo()).ifPresent(relativeDates::setDateRelativeTo);
				getDayTypeEnum(d.getDayType()).ifPresent(relativeDates::setDayType);
				getPeriodEnum(d.getPeriod()).ifPresent(relativeDates::setPeriod);
				getBigInteger(d.getPeriodMultiplier()).ifPresent(relativeDates::setPeriodMultiplier);
				getBigInteger(d.getPeriodSkip()).ifPresent(relativeDates::setPeriodSkip);
				getDateRange(d.getScheduleBounds()).ifPresent(relativeDates::setScheduleBounds);
				return relativeDates;
			});
	}

	private Optional<BigInteger> getBigInteger(Integer integer) {
		return Optional.ofNullable(integer).map(BigInteger::valueOf);
	}

	private Optional<DayTypeEnum> getDayTypeEnum(cdm.base.datetime.DayTypeEnum cdmDayType) {
		return Optional.ofNullable(cdmDayType).map(Enum::name).map(DayTypeEnum::valueOf);
	}

	private Optional<IdentifiedDate> getIdentifiedDate(FieldWithMetaDate cdmDate) {
		return Optional.ofNullable(cdmDate)
			.map(d -> {
				IdentifiedDate identifiedDate = objectFactory.createIdentifiedDate();
				getDate(d.getValue()).ifPresent(identifiedDate::setValue);
				getExternalKey(d.getMeta()).ifPresent(identifiedDate::setId);
				return identifiedDate;
			});
	}

	private Optional<IdentifiedDate> getIdentifiedDate(Date cdmDate) {
		return getDate(cdmDate)
			.map(d -> {
				IdentifiedDate identifiedDate = objectFactory.createIdentifiedDate();
				identifiedDate.setValue(d);
				return identifiedDate;
			});
	}

	private Optional<XMLGregorianCalendar> getDate(Date cdmDate) {
		return Optional.ofNullable(cdmDate).map(Date::toLocalDate).flatMap(this::getDate);
	}

	private Optional<XMLGregorianCalendar> getDate(LocalDate localDate) {
		return Optional.ofNullable(localDate)
			.map(d -> datatypeFactory.newXMLGregorianCalendarDate(
				d.getYear(), d.getMonthValue(), d.getDayOfMonth(), DatatypeConstants.FIELD_UNDEFINED));
	}

	private Optional<XMLGregorianCalendar> getHourMinuteTime(LocalTime localTime) {
		return Optional.ofNullable(localTime)
			.map(t -> datatypeFactory.newXMLGregorianCalendarTime(
				t.getHour(), t.getMinute(), t.getSecond(), DatatypeConstants.FIELD_UNDEFINED));
	}

	private Optional<DateRange> getDateRange(cdm.base.datetime.DateRange cdmDateRange) {
		return Optional.ofNullable(cdmDateRange)
			.map(d -> {
				DateRange dateRange = objectFactory.createDateRange();
				getDate(d.getStartDate()).ifPresent(dateRange::setUnadjustedFirstDate);
				getDate(d.getEndDate()).ifPresent(dateRange::setUnadjustedLastDate);
				return dateRange;
			});
	}

	private Optional<DateReference> getDateRelativeTo(ReferenceWithMetaDate cdmDateRelativeTo) {
		return Optional.ofNullable(cdmDateRelativeTo)
			.map(ReferenceWithMetaDate::getExternalReference)
			.map(r -> {
				IdentifiedDate dateRef = objectFactory.createIdentifiedDate();
				dateRef.setId(r);
				DateReference dateReference = objectFactory.createDateReference();
				dateReference.setHref(dateRef);
				return dateReference;
			});
	}

	private Optional<BusinessDayConventionEnum> getBusinessDayConventionEnum(cdm.base.datetime.BusinessDayConventionEnum cdmBusinessDayConvention) {
		return Optional.ofNullable(cdmBusinessDayConvention).map(Enum::name).map(BusinessDayConventionEnum::fromValue);
	}

	private Optional<BusinessDayAdjustments> getBusinessDayAdjustments(cdm.base.datetime.BusinessDayAdjustments cdmBusinessDayAdjustments) {
		return Optional.ofNullable(cdmBusinessDayAdjustments)
			.map(a -> {
				BusinessDayAdjustments businessDayAdjustments = objectFactory.createBusinessDayAdjustments();
				Optional.ofNullable(a.getBusinessDayConvention())
					.map(Object::toString)
					.map(BusinessDayConventionEnum::valueOf)
					.ifPresent(businessDayAdjustments::setBusinessDayConvention);
				getBusinessCenters(a.getBusinessCenters()).ifPresent(businessDayAdjustments::setBusinessCenters);
				Optional.ofNullable(a.getBusinessCenters())
					.map(cdm.base.datetime.BusinessCenters::getBusinessCentersReference)
					.flatMap(this::getBusinessCentersReference)
					.ifPresent(businessDayAdjustments::setBusinessCentersReference);
				return businessDayAdjustments;
			});
	}

	private Optional<BusinessCenters> getBusinessCenters(cdm.base.datetime.BusinessCenters cdmBusinessCenters) {
		return Optional.ofNullable(cdmBusinessCenters)
			.map(c -> {
				BusinessCenters businessCenters = objectFactory.createBusinessCenters();
				businessCenters.getBusinessCenter().addAll(getBusinessCenters(c.getBusinessCenter()));
				getExternalKey(c.getMeta()).ifPresent(businessCenters::setId);
				return businessCenters;
			});
	}

	private List<BusinessCenter> getBusinessCenters(List<? extends FieldWithMetaBusinessCenterEnum> businessCenters) {
		return emptyIfNull(businessCenters).stream()
			.map(this::getBusinessCenter)
			.flatMap(Optional::stream)
			.collect(Collectors.toList());
	}

	private Optional<BusinessCentersReference> getBusinessCentersReference(ReferenceWithMetaBusinessCenters cdmBusinessCentersReference) {
		return Optional.ofNullable(cdmBusinessCentersReference)
			.map(ReferenceWithMetaBusinessCenters::getExternalReference)
			.map(r -> {
				BusinessCenters businessCenters = objectFactory.createBusinessCenters();
				businessCenters.setId(r);
				BusinessCentersReference businessCentersReference = objectFactory.createBusinessCentersReference();
				businessCentersReference.setHref(businessCenters);
				return businessCentersReference;
			});
	}

	private Optional<BusinessCenter> getBusinessCenter(FieldWithMetaBusinessCenterEnum cdmBusinessCenter) {
		return Optional.ofNullable(cdmBusinessCenter)
			.map(c -> {
				BusinessCenter businessCenter = objectFactory.createBusinessCenter();
				getValue(c).ifPresent(businessCenter::setValue);
				getExternalKey(c.getMeta()).ifPresent(businessCenter::setId);
				getScheme(c.getMeta()).ifPresent(businessCenter::setBusinessCenterScheme);
				return businessCenter;
			});
	}

	private Optional<CalculationPeriodFrequency> getCalculationPeriodFrequency(cdm.base.datetime.CalculationPeriodFrequency cdmCalculationPeriodFrequency) {
		return Optional.ofNullable(cdmCalculationPeriodFrequency)
			.map(f -> {
				CalculationPeriodFrequency calculationPeriodFrequency = objectFactory.createCalculationPeriodFrequency();
				getBigInteger(f.getPeriodMultiplier()).ifPresent(calculationPeriodFrequency::setPeriodMultiplier);
				Optional.ofNullable(f.getPeriod()).map(Enum::name).ifPresent(calculationPeriodFrequency::setPeriod);
				Optional.ofNullable(f.getRollConvention()).map(Objects::toString).ifPresent(calculationPeriodFrequency::setRollConvention);
				return calculationPeriodFrequency;
			});
	}

	private Optional<PaymentDates> getPaymentDates(cdm.product.common.schedule.PaymentDates cdmPaymentDates) {
		return Optional.ofNullable(cdmPaymentDates)
			.map(d -> {
				PaymentDates paymentDates = objectFactory.createPaymentDates();
				getExternalKey(d.getMeta()).ifPresent(paymentDates::setId);
				getPaymentFrequency(d.getPaymentFrequency()).ifPresent(paymentDates::setPaymentFrequency);
				Optional.ofNullable(d.getPayRelativeTo()).map(Objects::toString).map(PayRelativeToEnum::valueOf).ifPresent(paymentDates::setPayRelativeTo);
				getBusinessDayAdjustments(d.getPaymentDatesAdjustments()).ifPresent(paymentDates::setPaymentDatesAdjustments);
				getOffset(d.getPaymentDaysOffset()).ifPresent(paymentDates::setPaymentDaysOffset);
				return paymentDates;
			});
	}

	private Optional<Frequency> getPaymentFrequency(cdm.base.datetime.Frequency cdmFrequency) {
		return Optional.ofNullable(cdmFrequency)
			.map(f -> {
				Frequency frequency = objectFactory.createFrequency();
				getBigInteger(f.getPeriodMultiplier()).ifPresent(frequency::setPeriodMultiplier);
				Optional.ofNullable(f.getPeriod()).map(Object::toString).ifPresent(frequency::setPeriod);
				return frequency;
			});
	}

	private Optional<ResetDates> getResetDates(cdm.product.common.schedule.ResetDates cdmResetDates) {
		return Optional.ofNullable(cdmResetDates)
			.map(d -> {
				ResetDates resetDates = objectFactory.createResetDates();
				getExternalKey(d.getMeta()).ifPresent(resetDates::setId);
				getCalculationPeriodDatesReference(d.getCalculationPeriodDatesReference()).ifPresent(resetDates::setCalculationPeriodDatesReference);
				Optional.ofNullable(d.getResetRelativeTo()).map(Objects::toString).map(ResetRelativeToEnum::valueOf).ifPresent(resetDates::setResetRelativeTo);
				getFixingDates(d.getFixingDates()).ifPresent(resetDates::setFixingDates);
				getResetFrequency(d.getResetFrequency()).ifPresent(resetDates::setResetFrequency);
				getBusinessDayAdjustments(d.getResetDatesAdjustments()).ifPresent(resetDates::setResetDatesAdjustments);
				return resetDates;
			});
	}

	private Optional<CalculationPeriodDatesReference> getCalculationPeriodDatesReference(
		ReferenceWithMetaCalculationPeriodDates cdmCalculationPeriodDatesReference) {
		return Optional.ofNullable(cdmCalculationPeriodDatesReference)
			.map(ReferenceWithMetaCalculationPeriodDates::getExternalReference)
			.map(r -> {
				CalculationPeriodDates calculationPeriodDatesRef = objectFactory.createCalculationPeriodDates();
				calculationPeriodDatesRef.setId(r);
				CalculationPeriodDatesReference calculationPeriodDatesReference = objectFactory.createCalculationPeriodDatesReference();
				calculationPeriodDatesReference.setHref(calculationPeriodDatesRef);
				return calculationPeriodDatesReference;
			});
	}

	private Optional<PaymentDatesReference> getPaymentDatesReference(ReferenceWithMetaPaymentDates cdmPaymentDatesReference) {
		return Optional.ofNullable(cdmPaymentDatesReference)
			.map(ReferenceWithMetaPaymentDates::getExternalReference)
			.map(r -> {
				PaymentDates paymentDatesRef = objectFactory.createPaymentDates();
				paymentDatesRef.setId(r);
				PaymentDatesReference paymentDatesReference = objectFactory.createPaymentDatesReference();
				paymentDatesReference.setHref(paymentDatesRef);
				return paymentDatesReference;
			});
	}

	private Optional<RelativeDateOffset> getFixingDates(cdm.base.datetime.RelativeDateOffset cdmFixingDates) {
		return Optional.ofNullable(cdmFixingDates)
			.map(d -> {
				RelativeDateOffset fixingDates = objectFactory.createRelativeDateOffset();
				getBigInteger(d.getPeriodMultiplier()).ifPresent(fixingDates::setPeriodMultiplier);
				getPeriodEnum(d.getPeriod()).ifPresent(fixingDates::setPeriod);
				Fpml510ProjectionMapper.this.getDayTypeEnum(d.getDayType()).ifPresent(fixingDates::setDayType);
				Optional.ofNullable(d.getBusinessDayConvention())
					.map(Objects::toString)
					.map(BusinessDayConventionEnum::valueOf)
					.ifPresent(fixingDates::setBusinessDayConvention);
				getBusinessCenters(d.getBusinessCenters()).ifPresent(fixingDates::setBusinessCenters);
				getResetDateRelativeToReference(d).ifPresent(fixingDates::setDateRelativeTo);
				return fixingDates;
			});
	}

	private Optional<DateReference> getResetDateRelativeToReference(cdm.base.datetime.RelativeDateOffset cdmFixingDates) {
		return Optional.ofNullable(cdmFixingDates)
			.map(cdm.base.datetime.RelativeDateOffset::getDateRelativeTo)
			.map(ReferenceWithMeta::getExternalReference)
			.map(r -> {
				ResetDates resetDatesRef = objectFactory.createResetDates();
				resetDatesRef.setId(r);
				DateReference dateRelativeTo = objectFactory.createDateReference();
				dateRelativeTo.setHref(resetDatesRef);
				return dateRelativeTo;
			});
	}

	private Optional<ResetFrequency> getResetFrequency(cdm.product.common.schedule.ResetFrequency cdmResetFrequency) {
		return Optional.ofNullable(cdmResetFrequency)
			.map(f -> {
				ResetFrequency resetFrequency = objectFactory.createResetFrequency();
				getBigInteger(f.getPeriodMultiplier()).ifPresent(resetFrequency::setPeriodMultiplier);
				Optional.ofNullable(f.getPeriod()).map(Object::toString).ifPresent(resetFrequency::setPeriod);
				return resetFrequency;
			});
	}

	private Optional<CalculationPeriodAmount> getCalculationPeriodAmount(InterestRatePayout cdmInterestRatePayout) {
		return Optional.ofNullable(cdmInterestRatePayout)
			.map(p -> {
				CalculationPeriodAmount calculationPeriodAmount = objectFactory.createCalculationPeriodAmount();
				getCalculation(p).ifPresent(calculationPeriodAmount::setCalculation);
				return calculationPeriodAmount;
			});
	}

	private Optional<Calculation> getCalculation(InterestRatePayout cdmInterestRatePayout) {
		return Optional.ofNullable(cdmInterestRatePayout)
			.map(p -> {
				Calculation calculation = objectFactory.createCalculation();
				getNotionalSchedule(p).ifPresent(calculation::setNotionalSchedule);
				getFixedRateSchedule(p.getRateSpecification()).ifPresent(calculation::setFixedRateSchedule);
				getFloatingRateCalculation(p.getRateSpecification()).ifPresent(calculation::setRateCalculation);
				getDayCountFraction(p.getDayCountFraction()).ifPresent(calculation::setDayCountFraction);
				getFutureValueNotional(p.getPriceQuantity()).ifPresent(calculation::setFutureValueNotional);
				Optional.ofNullable(p.getCompoundingMethod())
					.map(Objects::toString)
					.map(CompoundingMethodEnum::valueOf)
					.ifPresent(calculation::setCompoundingMethod);
				return calculation;
			});
	}

	private Optional<FutureValueAmount> getFutureValueNotional(ResolvablePriceQuantity payoutQuantity) {
		return Optional.ofNullable(payoutQuantity)
			.map(ResolvablePriceQuantity::getFutureValueNotional)
			.map(n -> {
				FutureValueAmount futureValueAmount = objectFactory.createFutureValueAmount();
				getBigInteger(n.getCalculationPeriodNumberOfDays())
					.ifPresent(futureValueAmount::setCalculationPeriodNumberOfDays);
				getCurrency(n.getCurrency()).ifPresent(futureValueAmount::setCurrency);
				return futureValueAmount;
			});
	}

	private Optional<Notional> getNotionalSchedule(InterestRatePayout cdmInterestRatePayout) {
		return getNotionalStepSchedule(cdmInterestRatePayout)
			.map(s -> {
				Notional notional = objectFactory.createNotional();
				notional.setNotionalStepSchedule(s);
				Optional.ofNullable(cdmInterestRatePayout)
					.map(PayoutBase::getPriceQuantity)
					.map(GlobalKey::getMeta)
					.flatMap(this::getExternalKey)
					.ifPresent(notional::setId);
				return notional;
			});
	}

	private Optional<NonNegativeAmountSchedule> getNotionalStepSchedule(InterestRatePayout cdmInterestRatePayout) {
		return Optional.ofNullable(cdmInterestRatePayout)
			.map(InterestRatePayout::getPriceQuantity)
			.map(ResolvablePriceQuantity::getQuantitySchedule)
			.map(qs -> {
				NonNegativeAmountSchedule notionalStepSchedule = objectFactory.createNonNegativeAmountSchedule();
				Optional.ofNullable(qs.getValue())
					.ifPresent(q -> {
						Optional.ofNullable(q.getValue()).ifPresent(notionalStepSchedule::setInitialValue);
						getCurrency(q.getUnit()).ifPresent(notionalStepSchedule::setCurrency);
					});
				getSteps(cdmInterestRatePayout).ifPresent(notionalStepSchedule.getStep()::addAll);
				return notionalStepSchedule;
			});
	}

	private Optional<List<? extends NonNegativeStep>> getSteps(InterestRatePayout cdmInterestRatePayout) {
		return Optional.ofNullable(cdmInterestRatePayout)
			.map(PayoutBase::getPriceQuantity)
			.map(ResolvablePriceQuantity::getQuantitySchedule)
			.map(ReferenceWithMetaNonNegativeQuantitySchedule::getValue)
			.map(NonNegativeQuantitySchedule::getDatedValue)
			.map(sl -> sl.stream()
				.map(this::getNonNegativeStep)
				.flatMap(Optional::stream)
				.collect(Collectors.toList()));
	}

	private Optional<NonNegativeStep> getNonNegativeStep(cdm.base.math.DatedValue nonNegativeStep) {
		return Optional.ofNullable(nonNegativeStep)
			.map(s -> {
				NonNegativeStep step = objectFactory.createNonNegativeStep();
				Optional.ofNullable(s.getValue()).ifPresent(step::setStepValue);
				Optional.ofNullable(s.getDate()).flatMap(this::getDate).ifPresent(step::setStepDate);
				return step;
			});
	}

	private Optional<Currency> getCurrency(UnitType cdmUnitType) {
		return Optional.ofNullable(cdmUnitType)
			.map(UnitType::getCurrency)
			.flatMap(this::getCurrency);
	}

	private Optional<Currency> getCurrency(FieldWithMetaString cdmCurrency) {
		return Optional.ofNullable(cdmCurrency)
			.map(c -> {
				Currency currency = objectFactory.createCurrency();
				getValue(c).ifPresent(currency::setValue);
				getScheme(c.getMeta()).ifPresent(currency::setCurrencyScheme);
				return currency;
			});
	}

	private Optional<Schedule> getFixedRateSchedule(RateSpecification cdmRateSpecification) {
		return Optional.ofNullable(cdmRateSpecification)
			.map(RateSpecification::getFixedRate)
			.map(FixedRateSpecification::getRateSchedule)
			.map(RateSchedule::getPrice)
			.map(ReferenceWithMetaPriceSchedule::getValue)
			.map(s -> {
				Schedule fixedRateSchedule = objectFactory.createSchedule();
				Optional.ofNullable(s.getValue())
					.ifPresent(fixedRateSchedule::setInitialValue);
				Optional.ofNullable(s.getDatedValue())
					.flatMap(this::getSteps)
					.ifPresent(fixedRateSchedule.getStep()::addAll);
				return fixedRateSchedule;
			});
	}

	private Optional<List<Step>> getSteps(List<? extends cdm.base.math.DatedValue> steps) {
		return Optional.ofNullable(steps)
			.map(sl -> sl.stream()
				.map(this::getStep)
				.flatMap(Optional::stream)
				.collect(Collectors.toList()));
	}

	private Optional<Step> getStep(cdm.base.math.DatedValue cdmStep) {
		return Optional.ofNullable(cdmStep)
			.map(s -> {
				Step step = objectFactory.createStep();
				Optional.ofNullable(s.getValue()).ifPresent(step::setStepValue);
				Optional.ofNullable(s.getDate()).flatMap(this::getDate).ifPresent(step::setStepDate);
				return step;
			});
	}

	private Optional<JAXBElement<FloatingRateCalculation>> getFloatingRateCalculation(RateSpecification cdmRateSpecification) {
		return Optional.ofNullable(cdmRateSpecification)
			.map(RateSpecification::getFloatingRate)
			.map(floatingRate -> {
				FloatingRateCalculation floatingRateCalculation = objectFactory.createFloatingRateCalculation();
				Optional.ofNullable(floatingRate.getRateOption()).map(ReferenceWithMetaFloatingRateOption::getValue).ifPresent(o -> {
					getFloatingRateIndex(o.getFloatingRateIndex()).ifPresent(floatingRateCalculation::setFloatingRateIndex);
					getPeriod(o.getIndexTenor()).ifPresent(floatingRateCalculation::setIndexTenor);
				});
				Optional.ofNullable(floatingRate.getInitialRate())
					.map(Price::getValue)
					.ifPresent(floatingRateCalculation::setInitialRate);
				getSpreadSchedule(floatingRate.getSpreadSchedule())
					.ifPresent(floatingRateCalculation.getSpreadSchedule()::add);
				return objectFactory.createFloatingRateCalculation(floatingRateCalculation);
			});
	}

	private Optional<SpreadSchedule> getSpreadSchedule(cdm.product.asset.SpreadSchedule spreadSchedule) {
		return Optional.ofNullable(spreadSchedule)
			.map(s -> {
				SpreadSchedule schedule = objectFactory.createSpreadSchedule();
				Optional<PriceSchedule> priceSchedule = Optional.ofNullable(s.getPrice())
						.map(ReferenceWithMetaPriceSchedule::getValue);
				priceSchedule
						.map(PriceSchedule::getValue)
						.ifPresent(schedule::setInitialValue);
				getSpreadScheduleType(s.getSpreadScheduleType()).ifPresent(schedule::setType);
				priceSchedule
						.map(MeasureSchedule::getDatedValue)
						.flatMap(this::getSteps)
						.ifPresent(schedule.getStep()::addAll);
				return schedule;
			});
	}

	private Optional<SpreadScheduleType> getSpreadScheduleType(FieldWithMetaSpreadScheduleTypeEnum spreadScheduleType) {
		return Optional.ofNullable(spreadScheduleType)
			.map(s -> {
				SpreadScheduleType scheduleType = objectFactory.createSpreadScheduleType();
				getValue(s).ifPresent(scheduleType::setValue);
				return scheduleType;
			});
	}

	private Optional<FloatingRateIndex> getFloatingRateIndex(FieldWithMetaFloatingRateIndexEnum cdmFloatingRateIndex) {
		return Optional.ofNullable(cdmFloatingRateIndex)
			.map(r -> {
				FloatingRateIndex floatingRateIndex = objectFactory.createFloatingRateIndex();
				getValue(r).ifPresent(floatingRateIndex::setValue);
				getScheme(r.getMeta()).ifPresent(floatingRateIndex::setFloatingRateIndexScheme);
				return floatingRateIndex;
			});
	}

	private Optional<Period> getPeriod(cdm.base.datetime.Period cdmPeriod) {
		return Optional.ofNullable(cdmPeriod)
			.map(p -> {
				Period period = objectFactory.createPeriod();
				getBigInteger(p.getPeriodMultiplier()).ifPresent(period::setPeriodMultiplier);
				getPeriodEnum(p.getPeriod()).ifPresent(period::setPeriod);
				return period;
			});
	}

	private Optional<PeriodEnum> getPeriodEnum(cdm.base.datetime.PeriodEnum cdmPeriodEnum) {
		return Optional.ofNullable(cdmPeriodEnum).map(Enum::name).map(PeriodEnum::valueOf);
	}

	private Optional<DayCountFraction> getDayCountFraction(FieldWithMetaDayCountFractionEnum cdmDayCountFraction) {
		return Optional.ofNullable(cdmDayCountFraction)
			.map(dcf -> {
				DayCountFraction dayCountFraction = objectFactory.createDayCountFraction();
				getValue(dcf).ifPresent(dayCountFraction::setValue);
				getScheme(dcf.getMeta()).ifPresent(dayCountFraction::setDayCountFractionScheme);
				return dayCountFraction;
			});
	}

	private List<Party> getParties(TradeState cdmTradeState) {
		return cdmTradeState.getTrade().getParty().stream()
			.map(this::getParty)
			.flatMap(Optional::stream)
			.collect(Collectors.toList());
	}

	private Optional<Party> getParty(cdm.base.staticdata.party.Party cdmParty) {
		return Optional.ofNullable(cdmParty)
			.map(p -> {
				Party party = objectFactory.createParty();
				getExternalKey(p.getMeta()).ifPresent(party::setId);
				party.getPartyId().addAll(getPartyIds(p.getPartyId()));
				getPartyName(p.getName()).ifPresent(party::setPartyName);
				party.getPerson().addAll(getPerson(p.getPerson()));
				return party;
			});
	}

	private List<Person> getPerson(List<? extends NaturalPerson> cdmPerson) {
		return emptyIfNull(cdmPerson).stream()
			.map(p -> {
				Person person = objectFactory.createPerson();
				Optional.ofNullable(p.getFirstName()).ifPresent(person::setFirstName);
				Optional.ofNullable(p.getSurname()).ifPresent(person::setSurname);
				getExternalKey(p.getMeta()).ifPresent(person::setId);
				return person;
			})
			.collect(Collectors.toList());
	}

	private Optional<PartyName> getPartyName(FieldWithMetaString cdmPartyName) {
		return Optional.ofNullable(cdmPartyName)
			.map(p -> {
				PartyName partyName = objectFactory.createPartyName();
				getValue(p).ifPresent(partyName::setValue);
				return partyName;
			});
	}

	private List<PartyId> getPartyIds(List<? extends PartyIdentifier> cdmPartyIds) {
		return cdmPartyIds.stream()
			.map(this::getPartyId)
			.flatMap(Optional::stream)
			.collect(Collectors.toList());
	}

	private Optional<PartyId> getPartyId(PartyIdentifier cdmPartyId) {
		return Optional.ofNullable(cdmPartyId)
			.map(p -> {
				PartyId partyId = objectFactory.createPartyId();
				getValue(p.getIdentifier()).ifPresent(partyId::setValue);
				getScheme(p.getMeta()).ifPresent(partyId::setPartyIdScheme);
				return partyId;
			});
	}

	private Optional<String> getValue(FieldWithMeta<?> f) {
		return Optional.ofNullable(f.getValue()).map(Object::toString);
	}

	private Optional<String> getScheme(MetaFields meta) {
		return Optional.ofNullable(meta).map(MetaFields::getScheme);
	}

	private Optional<String> getExternalKey(GlobalKeyFields meta) {
		return Optional.ofNullable(meta).map(GlobalKeyFields::getExternalKey);
	}
}
