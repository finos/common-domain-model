/*******************************************************************************
 * Copyright (c) icmagroup.org  All rights reserved.
 *
 * This file is part of the International Capital Market Association (ICMA)
 * CDM for Repo and Bonds Demo
 *
 * This file is intended for demo purposes only and may not be distributed
 * or used in any commercial capacity other than its intended purpose.
 *
 * This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING
 * THE WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE.
 *
 *
 * Contact International Capital Market Association (ICMA),110 Cannon Street,
 * London EC4N 6EU, ph. +44 20 7213 0310, if you have any questions.
 *
 ******************************************************************************/

package com.icma.cdm.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import cdm.base.datetime.*;
import cdm.base.datetime.AdjustableDate;
import cdm.base.datetime.AdjustableDates;
import cdm.base.datetime.AdjustableOrRelativeDate;
import cdm.base.datetime.daycount.DayCountFractionEnum;
import cdm.base.datetime.daycount.metafields.FieldWithMetaDayCountFractionEnum;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule;
import cdm.base.math.metafields.ReferenceWithMetaNonNegativeQuantitySchedule;
import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.identifier.TradeIdentifierTypeEnum;
import cdm.base.staticdata.party.*;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.base.staticdata.asset.common.metafields.FieldWithMetaProductIdentifier;
import cdm.base.staticdata.asset.common.*;
import cdm.event.common.ExecutionInstruction;
import cdm.event.common.Trade;
import cdm.event.common.TradeIdentifier;
import cdm.event.common.ExecutionDetails;
import cdm.event.common.*;
import cdm.observable.asset.Observable;
import cdm.product.asset.*;
import cdm.product.collateral.*;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.schedule.PayRelativeToEnum;
import cdm.product.common.schedule.PaymentDates;
import cdm.product.common.schedule.RateSchedule;
import cdm.product.common.settlement.ResolvablePriceQuantity;
import cdm.observable.asset.*;
import cdm.observable.asset.metafields.FieldWithMetaPriceSchedule;
import cdm.observable.asset.FloatingRateOption;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceExpression;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.metafields.ReferenceWithMetaPriceSchedule;
import cdm.product.common.settlement.*;
import cdm.product.template.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.GlobalKey;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.Key;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaDate;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.records.Date;


import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.rosetta.model.lib.records.Date.of;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class IcmaRepoUtil {


	private final PostProcessStep keyProcessor;

	public IcmaRepoUtil() {
		keyProcessor = new GlobalKeyProcessStep(NonNullHashCollector::new);
	}

	/**
	 * Utility to post process a {@link RosettaModelObject} to add ll gloval keys.
	 */
	private <T extends RosettaModelObject> T addGlobalKey(Class<T> type, T modelObject) {
		RosettaModelObjectBuilder builder = modelObject.toBuilder();
		keyProcessor.runProcessStep(type, builder);
		return type.cast(builder.build());
	}

	/**
	 * Utility to get the global reference string from a {@link GlobalKey} instance.
	 */
	private String getGlobalReference(GlobalKey globalKey) {
		return globalKey.getMeta().getGlobalKey();
	}

	private TradeState tradeState;


	public FieldWithMetaDate createTradeDate(int y, int m, int d) {

		Date dt;
		dt = of(y, m, d);

		return FieldWithMetaDate.builder().setValue(dt).build();


	}

	public TradeIdentifier createRepoTradeIdentifier(String identifier, String scheme, String issuer) {

		return TradeIdentifier.builder().addAssignedIdentifier(
						AssignedIdentifier.builder().setIdentifier(
										FieldWithMetaString.builder().setValue(identifier)
												.setMeta(MetaFields.builder()
														.setScheme(scheme)
														.build())
												.build())
								.build())
				.setIssuer(FieldWithMetaString.builder()
						.setValue(issuer)
						.build())
				.setIdentifierType(TradeIdentifierTypeEnum.valueOf("UNIQUE_TRANSACTION_IDENTIFIER"))
				.build();
	}

	public Party createRepoParty(String partyId, String scheme, String pName) {
		return addGlobalKey(Party.class,
				Party.builder()
						.addPartyId(PartyIdentifier.builder()
								.setIdentifierValue(partyId)
								.setMeta(MetaFields.builder().setScheme(scheme).build())
								.build())
						.setNameValue(pName)
						.build());
	}

	public PartyRole createRepoPartyRole(Party party, String reference, String role) {
		return PartyRole.builder()
				.setPartyReference(ReferenceWithMetaParty.builder()
						.setGlobalReference(getGlobalReference(party))
						.setExternalReference(reference)
						.build())
				.setRole(PartyRoleEnum.valueOf(role))
				.build();
	}

	public Counterparty createRepoCounterparty(Party party, String role) {

		return Counterparty.builder()
				.setPartyReferenceValue(party)
				.setRole(CounterpartyRoleEnum.valueOf(role))
				.build();
	}

	public List<PriceQuantity> createRepoPriceQuantity() {

		PriceQuantity pc = null;

		return List.of(pc, pc);
	}

	public List<PriceQuantity> createRepoPriceQuantity(
			String cashQuantityStr,
			String cashCurrencyStr,
			String collateralISINStr,
			String collateralQuantityStr,
			String collateralCurrencyStr,
			String repoRateStr,
			String haircutStr,
			String collateralCleanPriceStr,
			String collateralDirtyPriceStr
	) throws JsonProcessingException {

		double cashQuantity = Double.parseDouble(cashQuantityStr);
		double collateralQuantity = Double.parseDouble(collateralQuantityStr);
		double repoRate = Double.parseDouble(repoRateStr);
		double haircut = Double.parseDouble(haircutStr);
		double collateralCleanPrice = Double.parseDouble(collateralCleanPriceStr);
		double collateralDirtyPrice = Double.parseDouble(collateralDirtyPriceStr);

		PriceQuantity loanPriceQuantity = addGlobalKey(PriceQuantity.class,
				createLoanPriceQuantity(cashCurrencyStr, cashQuantity, collateralCurrencyStr, collateralQuantity, collateralCleanPrice, collateralDirtyPrice, repoRate, collateralISINStr, ""));

		PriceQuantity collateralPriceQuantity = addGlobalKey(PriceQuantity.class,
				createCollateralPriceQuantity(cashCurrencyStr, cashQuantity, collateralCurrencyStr, collateralQuantity, collateralCleanPrice, collateralDirtyPrice, repoRate, collateralISINStr, ""));

		List<PriceQuantity> repoPriceQuantity = List.of(loanPriceQuantity, collateralPriceQuantity);

		return repoPriceQuantity;
	}

	public PriceQuantity createLoanPriceQuantity(
			String cashCurrencyStr,
			double cashQuantity,
			String collateralCurrencyStr,
			double collateralQuantity,
			double collteralCleanPrice,
			double collateralDirtyPrice,
			double rate,
			String collateralISINStr,
			String scheme) throws JsonProcessingException {

		return PriceQuantity.builder()
				// Set cash amount and rate
				.addPrice(FieldWithMetaPriceSchedule.builder()
						.setMeta(MetaFields.builder().setScheme(scheme).build())
						.setValue(Price.builder()
								.setUnit(UnitType.builder()
										.setCurrencyValue(cashCurrencyStr)))
						.setValue(Price.builder()
								.setValue(BigDecimal.valueOf(rate))
								.setUnit(UnitType.builder()
										.setCurrencyValue(cashCurrencyStr))
								.setPerUnitOf(UnitType.builder()
										.setCurrencyValue(cashCurrencyStr))
								.setPriceExpression(PriceExpression.builder().setPriceType(PriceTypeEnum.INTEREST_RATE)))
						.build())

				.addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
						.setValue(NonNegativeQuantitySchedule.builder()
								.setValue(BigDecimal.valueOf(cashQuantity))
								.setUnit(UnitType.builder()
										.setCurrencyValue(cashCurrencyStr)))
						.build())

				.build();

	}


	private PriceQuantity createCollateralPriceQuantity(
			String cashCurrencyStr,
			double cashQuantity,
			String collateralCurrencyStr,
			double collateralQuantity,
			double collteralCleanPrice,
			double collateralDirtyPrice,
			double rate,
			String collateralISINStr,
			String scheme) throws JsonProcessingException {

		return PriceQuantity.builder()
				// Set cash amount and rate
				.setObservable(Observable.builder()
						.addProductIdentifierValue(ProductIdentifier.builder()
								.setIdentifierValue(collateralISINStr)
								.setSource(ProductIdTypeEnum.ISIN)
								.build()))
				// Set collateral amount and price
				.addPrice(FieldWithMetaPriceSchedule.builder()
						.setValue(Price.builder()
								.setValue(BigDecimal.valueOf(collateralDirtyPrice))
								.setUnit(UnitType.builder()
										.setCurrencyValue(collateralCurrencyStr))
								.setPerUnitOf(UnitType.builder()
										.setCurrencyValue(collateralCurrencyStr))
								.setPriceExpression(PriceExpression.builder()
										.setPriceType(PriceTypeEnum.ASSET_PRICE)
										.setPriceExpression(PriceExpressionEnum.PERCENTAGE_OF_NOTIONAL)
										.setCleanOrDirty(CleanOrDirtyPriceEnum.DIRTY)))
						.build())

				.addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
						.setValue(NonNegativeQuantitySchedule.builder()
								.setValue(BigDecimal.valueOf(collateralQuantity))
								.setUnit(UnitType.builder()
										.setCurrencyValue(collateralCurrencyStr)))
						.build())

				.build();

	}

	public void writeEventToFile(String eventName, String eventId, String data) throws IOException {

		String userDirectory = Paths.get("")
				.toAbsolutePath()
				.toString();
		File udir = new File(userDirectory);
		File pudir = udir.getParentFile();
		String eventlogs = pudir.getPath() + "/eventlogs";

		File logFile = new File(eventlogs);
		final File log_directory = logFile.getAbsoluteFile();
		if (null != log_directory) {
			log_directory.mkdirs();
		}

		File eventfile = new File(eventlogs + "/" + eventName + "_" + eventId + ".txt");
		FileWriter fr = new FileWriter(eventfile);

		try {
			boolean result = eventfile.exists();
			if (result) {
				fr.write(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//close resources
			try {
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("writing event file output:" + eventfile);

	}

	public AdjustableOrRelativeDate createAdjustableOrRelativeDate(String dateStr) {

		//Set the termination date
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
		ZonedDateTime zdtWithZoneOffset = ZonedDateTime.parse(dateStr + "T00:00:00.000+00:00", formatter);
		ZonedDateTime zdtInLocalTimeline = zdtWithZoneOffset.withZoneSameInstant(ZoneId.systemDefault());

		Date terminationDate = of(zdtWithZoneOffset.getYear(), zdtWithZoneOffset.getMonthValue(), zdtWithZoneOffset.getDayOfMonth());

		AdjustableOrRelativeDate adjustableOrRelativeDateTerminationDateDate = AdjustableOrRelativeDate.builder()
				.setAdjustableDate(AdjustableDate.builder()
						.setUnadjustedDate(terminationDate)
						.setDateAdjustments(BusinessDayAdjustments.builder()
								.setBusinessDayConvention(BusinessDayConventionEnum.NONE)));

		return adjustableOrRelativeDateTerminationDateDate;
	}

	public AdjustableOrAdjustedOrRelativeDate createAdjustableOrAdjustedOrRelativeDate(String dateStr) {

		//Set the termination date
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
		ZonedDateTime zdtWithZoneOffset = ZonedDateTime.parse(dateStr , formatter);
		ZonedDateTime zdtInLocalTimeline = zdtWithZoneOffset.withZoneSameInstant(ZoneId.systemDefault());

		Date adjDate = of(zdtWithZoneOffset.getYear(), zdtWithZoneOffset.getMonthValue(), zdtWithZoneOffset.getDayOfMonth());

		AdjustableOrAdjustedOrRelativeDate adjustableOrAdjustedOrRelativeDate = AdjustableOrAdjustedOrRelativeDate.builder()
				.setUnadjustedDate(adjDate)
				.setDateAdjustments(BusinessDayAdjustments.builder()
						.setBusinessDayConvention(BusinessDayConventionEnum.NONE))
				.build();

		return adjustableOrAdjustedOrRelativeDate;
	}


	public Date createCDMDate(String dateStr) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
		ZonedDateTime zdtWithZoneOffset = ZonedDateTime.parse(dateStr + "T00:00:00.000+00:00", formatter);
		ZonedDateTime zdtInLocalTimeline = zdtWithZoneOffset.withZoneSameInstant(ZoneId.systemDefault());
		Date cdmDate = of(zdtWithZoneOffset.getYear(), zdtWithZoneOffset.getMonthValue(), zdtWithZoneOffset.getDayOfMonth());

		return cdmDate;
	}

	public String getAfterTradeState(String businessEvent) throws JsonProcessingException {

		ObjectMapper rosettaObjectMapper = RosettaObjectMapper.getNewRosettaObjectMapper();
		BusinessEvent beobj = new BusinessEvent.BusinessEventBuilderImpl();
		BusinessEvent be = rosettaObjectMapper.readValue(businessEvent, beobj.getClass());

		TradeState tradeState = be.getAfter().get(0);
		String tsjson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(tradeState);

		return tsjson;
		//String deserializedObject = String.valueOf(rosettaObjectMapper.readValue(tsjson, tradeState.getClass()));

		//return deserializedObject;
	}

	public JComboBox addParties(JComboBox jb) {
		jb.addItem(new CItem("", ""));
		jb.addItem(new CItem("HSBC Bank Plc", "MP6I5ZYZBEU3UXPYFY54"));
		jb.addItem(new CItem("Credit Suisse Securities (Europe) Ltd", "DL6FFRRLF74S01HE2M14"));
		jb.addItem(new CItem("XYZ Bank Plc Paris branch", "BG661XYBNEU6ASPGLA12"));
		jb.addItem(new CItem("Hochreutiner Cuckoo Clocks AG", "EM774SWRESA3UXBPIF77"));
		jb.addItem(new CItem("Global Fund Management Ltd (investment/asset manager)", "549300RM34L56MA11M54"));
		jb.addItem(new CItem("Dealer Bank Europe Ltd", "AL61GG34LM12CV28I911"));
		jb.addItem(new CItem("Swiss Reinsurance Company Ltd", "549300WZRVQERM819Z90"));
		jb.addItem(new CItem("MUFG Securities EMEA Plc", "U7M81AY481YLIOR75625"));
		jb.addItem(new CItem("Tullett Liberty Securities LLC", "5493002MIGPVI71S2611"));
		jb.addItem(new CItem("WhiteSands US Treasury Fund ", "4138114CCP90NM2127HG2"));
		jb.addItem(new CItem("BNY Mellon Capital Markets LLC", "VJW2DOOHGDT6PR0ZRO63"));
		jb.addItem(new CItem("LCH SA", "R1IO4YJ0O79SMWVCHB58"));

		return jb;
	}
}

//Combobox lists
class CItem {
	String label;
	String value;
	String code;
	String scheme;

	public CItem(String label, String value) {
		this.label = label;
		this.value = value;
	}

	public CItem(String label, String value, String code, String scheme){
		this.label = label;
		this.value = value;
		this.code = code;
		this.scheme = scheme;
	}

	public String getClabel() {
			return label;
	}

	public String getCValue(){

		return value;

	}

	@Override
	public String toString() {
		return label;
	}

}

class CdmEnumMap{

	private List<Map.Entry<String, String>> list;
	public Map buildEnumMap(Map<String, String> map) {

		map.put("DVP", "DELIVERY_VERSUS_PAYMENT");

		return map;
	}

}

