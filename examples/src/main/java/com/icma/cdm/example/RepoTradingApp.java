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
import java.awt.event.*;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.border.Border;

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
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.icma.cdm.example.util.ResourcesUtils;
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
import com.icma.cdm.example.util.ResourcesUtils;

import com.icma.cdm.example.IcmaRepoUtil;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cdm.event.common.functions.Create_RollPrimitiveInstruction;
import org.isda.cdm.CdmRuntimeModule;

import static com.rosetta.model.lib.records.Date.of;

public class RepoTradingApp extends JFrame implements ActionListener{
	
	private JFrame frame;
	private JPanel mainContainer;
	private JPanel panel;
	private JPanel panel2;
	
	private JButton newTradeBtn;
	private JButton bookTradeBtn;
	private JButton rollTradeBtn;
	private JButton terminateTradeBtn;
	private JButton rerateTradeBtn;

	private JButton onDemandPaymentBtn;
	private JButton reportTradeBtn;


	private JComboBox transactionTypeField;

	private JComboBox tradeDirectionField;
	private JTextField tradeDateField;
	private JTextField purchaseDateField;
	private JTextField repurchaseDateField;
	private JTextField tradeUTIField;
	private JComboBox buyerLEIField;
	private JComboBox sellerLEIField;
	private JTextField collateralDescriptionField;
	private JTextField collateralISINField;
	private JTextField collateralQuantityField;
	private JTextField collateralCleanPriceField;
	private JTextField collateralDirtyPriceField;
	private JTextField collateralAdjustedValueField;
	private JTextField collateralCurrencyField;
	private JTextField repoRateField;
	private JTextField cashCurrencyField;
	private JTextField cashQuantityField;
	private JTextField haircutField;
	private JComboBox termTypeField;
	private JComboBox terminationOptionField;
	private JTextField noticePeriodField;
	private JTextField deliveryMethodField;
	private JComboBox  substitutionAllowedField;
	private JComboBox  rateTypeField;
	private JTextField dayCountFractionField;
	private JTextField termDaysField;
	private JTextField purchasePriceField;
	private JTextField repurchasePriceField;

	//Panel 2 fields


	private JComboBox tradingBookOptionField;
	private JComboBox  businessCenterOptionField;
	private JComboBox  timeZoneOptionField;
	private JTextField executionTimeField;

	private JComboBox venueCodeOptionField;

	private JTextField traderNameField;
	private JTextField traderLocationField;
	private JTextField traderLocalDateField;
	private JTextField traderLocalTimeField;
	private JComboBox brokerField;
	private JComboBox tripartyField;
	private JComboBox beneficiaryField;

	private JTextField floatingRateReferenceField;
	private JComboBox floatingRateReferencePeriodField;
	private JTextField floatingRateReferenceMultiplierField;
	private JComboBox floatingRatePaymentFreqField;
	private JTextField floatingRatePaymentMultiplierField;
	private JComboBox floatingRateResetFreqField;
	private JTextField floatingRateResetMultiplierField;
	private JTextField floatingRateField;
	private JTextField floatingRateSpreadField;
	private JComboBox settlementAgentOptionField;

	private JComboBox clearingMemberField;

	private JComboBox agentLenderField;
	private JComboBox centralClearingCounterpartyOptionField;
	private JComboBox csdOptionField;
	private JTextField firmNamFielde;
	private JTextField firmLeiField;
	private JTextField firmCapacityField;

	private JTextField agreementNameField;

	private JTextField agreementVersionField;

	private JTextField tradeIdField;
	private JTextField statusField;
	
	private boolean dbEnabled = false;
	
	//Defaults
	private String defaultLocalTimeZone = "UTC";

	private String tradeStateStr = null;
	private String beforeTradeStateStr = null;
	private String afterTradeStateStr = null;


	private List<? extends TradeState>afterTradeStateList = null;
	private List<String> businessEventList;
	
	public ZonedDateTime TDzonedDateTime;

	public ZonedDateTime LDzonedDateTime;
	public String TDformattedDateTimeString;
	public ZonedDateTime PDzonedDateTime;
	public String PDformattedDateTimeString;
	public ZonedDateTime RDzonedDateTime;
	public String RDformattedDateTimeString;

	public String ETformattedDateTimeString;
	public DateTimeFormatter formatter;

	public ZonedDateTime ETzonedDateTime;
	public DateTimeFormatter ETformatter;

	public DateTimeFormatter eventFileformatter;
	private JTextArea outputArea;

	private JFrame eout;

	JScrollPane scrollPane = null;

	Map<String, String> cdmMap = new HashMap<>();

	
	public static void main( String[] args ) throws JsonProcessingException  {


		RepoTradingApp repoTradingApp = new RepoTradingApp();

		repoTradingApp.BuildTicketPanel();
	        
		
    }

	public void BuildTicketPanel () throws JsonProcessingException {

		IcmaRepoUtil ru = new IcmaRepoUtil();

		JFrame eout= new JFrame("Business Event Output");
		eout.setLayout(new FlowLayout(FlowLayout.LEFT));
		eout.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		eout.setSize( 800, 800 );
		JPanel outpanel = new JPanel(new BorderLayout());


		outputArea = new JTextArea(50,80);
		outputArea.setEditable(false);
		scrollPane = new JScrollPane(outputArea);
		scrollPane.setViewportView(outputArea);
        scrollPane.getPreferredSize();
		outpanel.add(scrollPane, BorderLayout.CENTER);
		eout.add(outpanel);

		eout.setVisible( true );

		frame = new JFrame( "ICMA CDM Repo Demo" );
		frame.setLayout(new FlowLayout(FlowLayout.LEFT));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize( 800, 800 );

		JPanel logoPanel = new JPanel();
		try{
		Image image = ImageIO.read(getClass().getResource("/images/icma-logo.jfif"));
		frame.setIconImage(image);
		eout.setIconImage(image);
		} catch (IOException ioe) {
		ioe.printStackTrace();
		}

		Border blackline = BorderFactory.createLineBorder(Color.black);
		mainContainer = new JPanel();
		mainContainer.setLayout(new GridBagLayout());
		//mainContainer.setBorder(blackline);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.anchor = gbc.PAGE_START;
		frame.add(mainContainer,gbc);

		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		mainContainer.add(panel);

		panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
		//panel2.setBorder(blackline);
		panel2.setAlignmentY(0f);
		mainContainer.add(panel2,gbc);



		//Type
		String[] typechoices = { "Repurchase Agreement", "Buy/Sell-back Agreement"};
		JPanel transactionTypePanel = new JPanel(new GridBagLayout());
		JLabel transactionTypeLabel = new JLabel("Type:",JLabel.LEFT);
		transactionTypeLabel.setPreferredSize(new Dimension(150, 15));

		transactionTypeField = new JComboBox<String>(typechoices);
		transactionTypeField.setAlignmentX(Component.LEFT_ALIGNMENT);
		transactionTypeField.setPreferredSize(new Dimension(170, 20));
		transactionTypePanel.add(transactionTypeLabel);
		transactionTypePanel.add(transactionTypeField);
		panel.add(transactionTypePanel);

		//Direction
		String[] directionchoices = { "Buy (Reverse Repo)", "Sell (Repo)"};
		JPanel tradeDirectionPanel = new JPanel(new GridBagLayout());
		JLabel tradeDirectionLabel = new JLabel("Direction (B/S):",JLabel.LEFT);
		tradeDirectionLabel.setPreferredSize(new Dimension(150, 15));

		tradeDirectionField = new JComboBox<String>(directionchoices);
		tradeDirectionField.setAlignmentX(Component.LEFT_ALIGNMENT);
		tradeDirectionField.setPreferredSize(new Dimension(170, 20));
		tradeDirectionPanel.add(tradeDirectionLabel);
		tradeDirectionPanel.add(tradeDirectionField);
		panel.add(tradeDirectionPanel);


		//Trade Date
		JPanel tradeDatePanel = new JPanel(new GridBagLayout());
		JLabel tradeDateLabel = new JLabel("Trade Date:",JLabel.LEFT);
		tradeDateLabel.setPreferredSize(new Dimension(150, 15));
		tradeDateField = new JTextField(15);
		LocalDateTime localDateTime = LocalDateTime.now();
		TDzonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of(this.defaultLocalTimeZone));
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		TDformattedDateTimeString = TDzonedDateTime.format(formatter);
		
		tradeDateField.setText(TDformattedDateTimeString);
		tradeDatePanel.add(tradeDateLabel);
		tradeDatePanel.add(tradeDateField);
		panel.add(tradeDatePanel);


		//Purchase Date
		JPanel purchaseDatePanel = new JPanel(new GridBagLayout());
		JLabel purchaseDateLabel = new JLabel("Purchase Date:",JLabel.LEFT);
		purchaseDateLabel.setPreferredSize(new Dimension(150, 15));
		purchaseDateField = new JTextField(15);
		
		PDzonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of(this.defaultLocalTimeZone));
		PDformattedDateTimeString = PDzonedDateTime.format(formatter);
		purchaseDateField.setText(PDformattedDateTimeString);
		purchaseDatePanel.add(purchaseDateLabel);
		purchaseDatePanel.add(purchaseDateField);
		panel.add(purchaseDatePanel);
		
		
		//Repurchase Date
		JPanel repurchaseDatePanel = new JPanel(new GridBagLayout());
		JLabel repurchaseDateLabel = new JLabel("Repurchase Date:",JLabel.LEFT);
		repurchaseDateLabel.setPreferredSize(new Dimension(150, 15));
		repurchaseDateField = new JTextField(15);
		
		RDzonedDateTime = TDzonedDateTime.plusDays(1);
		RDformattedDateTimeString = RDzonedDateTime.format(formatter);
		
		repurchaseDateField.setText(RDformattedDateTimeString);
		repurchaseDatePanel.add(repurchaseDateLabel);
		repurchaseDatePanel.add(repurchaseDateField);
		panel.add(repurchaseDatePanel);

		repurchaseDateField.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
		repurchaseDateField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if ((e.getKeyChar() == KeyEvent.VK_TAB) || (e.getKeyChar() == KeyEvent.VK_ENTER)) {
					//System.out.print("Tab Pressed");
					updateTotalsXPrice();
				}
			}
		});
		
		//UTI
		JPanel tradeUTIPanel = new JPanel(new GridBagLayout());
		JLabel tradeUTILabel = new JLabel("Trade UTI:",JLabel.LEFT);
		tradeUTILabel.setPreferredSize(new Dimension(150, 15));
		tradeUTIField = new JTextField(15);
		tradeUTIField.setText("ICMA202303231000178");
		tradeUTIPanel.add(tradeUTILabel);
		tradeUTIPanel.add(tradeUTIField);
		panel.add(tradeUTIPanel);
		
		//Buyer
		JPanel buyerLEIPanel = new JPanel(new GridBagLayout());
		JLabel buyerLEILabel = new JLabel("Buyer:",JLabel.LEFT);
		buyerLEILabel.setPreferredSize(new Dimension(150, 15));

		buyerLEIField = new JComboBox();
		ru.addParties(buyerLEIField);

		buyerLEIField.setAlignmentX(Component.LEFT_ALIGNMENT);
		buyerLEIField.setPreferredSize(new Dimension(170, 20));
		buyerLEIPanel.add(buyerLEILabel);
		buyerLEIPanel.add(buyerLEIField);
		panel.add(buyerLEIPanel);
		
		//Seller
		JPanel sellerLEIPanel = new JPanel(new GridBagLayout());
		JLabel sellerLEILabel = new JLabel("Seller:",JLabel.LEFT);
		sellerLEILabel.setPreferredSize(new Dimension(150, 15));

		sellerLEIField = new JComboBox();
		ru.addParties(sellerLEIField);

		sellerLEIField.setAlignmentX(Component.LEFT_ALIGNMENT);
		sellerLEIField.setPreferredSize(new Dimension(170, 20));

		sellerLEIPanel.add(sellerLEILabel);
		sellerLEIPanel.add(sellerLEIField);
		panel.add(sellerLEIPanel);
		
		//Collateral
		JPanel collateralDescriptionPanel = new JPanel(new GridBagLayout());
		JLabel collateralDescriptionLabel = new JLabel("Security:",JLabel.LEFT);
		collateralDescriptionLabel.setPreferredSize(new Dimension(150, 15));
		collateralDescriptionField = new JTextField(15);
		collateralDescriptionField.setText("GILT .5 22/07/2022");
		collateralDescriptionPanel.add(collateralDescriptionLabel);
		collateralDescriptionPanel.add(collateralDescriptionField);
		panel.add(collateralDescriptionPanel);

		JPanel collateralISINPanel = new JPanel(new GridBagLayout());
		JLabel collateralISINLabel = new JLabel("Collateral ISIN:",JLabel.LEFT);
		collateralISINLabel.setPreferredSize(new Dimension(150, 15));
		collateralISINField = new JTextField(15);
		collateralISINField.setText("GB00BD0PCK97");
		collateralISINPanel.add(collateralISINLabel);
		collateralISINPanel.add(collateralISINField);
		panel.add(collateralISINPanel);
		
		JPanel collateralQuantityPanel = new JPanel(new GridBagLayout());
		JLabel collateralQuantityLabel = new JLabel("Quantity:",JLabel.LEFT);
		collateralQuantityLabel.setPreferredSize(new Dimension(150, 15));
		collateralQuantityField = new JTextField(15);
		collateralQuantityField.setText("1000");
		collateralQuantityPanel.add(collateralQuantityLabel);
		collateralQuantityPanel.add(collateralQuantityField);
		panel.add(collateralQuantityPanel);

		collateralQuantityField.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
		collateralQuantityField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if ((e.getKeyChar() == KeyEvent.VK_TAB) || (e.getKeyChar() == KeyEvent.VK_ENTER)) {
					//System.out.print("Tab Pressed");
					updateTotalsXPrice();
				}
			}
		});


		JPanel collateralCleanPricePanel = new JPanel(new GridBagLayout());
		JLabel collateralCleanPriceLabel = new JLabel("Clean Price:",JLabel.LEFT);
		collateralCleanPriceLabel.setPreferredSize(new Dimension(150, 15));
		collateralCleanPriceField = new JTextField(15);
		collateralCleanPriceField.setText("100.75");
		collateralCleanPricePanel.add(collateralCleanPriceLabel);
		collateralCleanPricePanel.add(collateralCleanPriceField);
		panel.add(collateralCleanPricePanel);

		JPanel collateralDirtyPricePanel = new JPanel(new GridBagLayout());
		JLabel collateralDirtyPriceLabel = new JLabel("Dirty Price:",JLabel.LEFT);
		collateralDirtyPriceLabel.setPreferredSize(new Dimension(150, 15));
		collateralDirtyPriceField = new JTextField(15);
		collateralDirtyPriceField.setText("100.8066");
		collateralDirtyPricePanel.add(collateralDirtyPriceLabel);
		collateralDirtyPricePanel.add(collateralDirtyPriceField);
		panel.add(collateralDirtyPricePanel);


		collateralDirtyPriceField.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
		collateralDirtyPriceField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if ((e.getKeyChar() == KeyEvent.VK_TAB) || (e.getKeyChar() == KeyEvent.VK_ENTER)) {
					//System.out.print("Tab Pressed");
					updateTotalsXPrice();
				}
			}
		});

		JPanel collateralAdjustedValuePanel = new JPanel(new GridBagLayout());
		JLabel collateralAdjustedValueLabel = new JLabel("Adjusted Value:",JLabel.LEFT);
		collateralAdjustedValueLabel.setPreferredSize(new Dimension(150, 15));
		collateralAdjustedValueField = new JTextField(15);
		collateralAdjustedValueField.setText("1008066.00");
		collateralAdjustedValuePanel.add(collateralAdjustedValueLabel);
		collateralAdjustedValuePanel.add(collateralAdjustedValueField);
		panel.add(collateralAdjustedValuePanel);

		JPanel collateralCurrencyPanel = new JPanel(new GridBagLayout());
		JLabel collateralCurrencyLabel = new JLabel("Currency:",JLabel.LEFT);
		collateralCurrencyLabel.setPreferredSize(new Dimension(150, 15));
		collateralCurrencyField = new JTextField(15);
		collateralCurrencyField.setText("GBP");
		collateralCurrencyPanel.add(collateralCurrencyLabel);
		collateralCurrencyPanel.add(collateralCurrencyField);
		panel.add(collateralCurrencyPanel);
		
		
		//Repo Rate Type
		String[] choices = { "FIXED", "FLOAT"};
		JPanel rateTypePanel = new JPanel(new GridBagLayout());
		JLabel rateTypeLabel = new JLabel("Rate Type:",JLabel.LEFT);
		rateTypeLabel.setPreferredSize(new Dimension(150, 15));
		
		rateTypeField = new JComboBox<String>(choices);
		rateTypeField.setAlignmentX(Component.LEFT_ALIGNMENT);
		rateTypeField.setPreferredSize(new Dimension(170, 20));
		rateTypePanel.add(rateTypeLabel);
		rateTypePanel.add(rateTypeField);

		rateTypeField.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED) {
					rateTypeFieldEvent(rateTypeField.getSelectedItem().toString());
				}
			}
		});

		panel.add(rateTypePanel);

		//Repo Rate
		JPanel repoRatePanel = new JPanel(new GridBagLayout());
		JLabel repoRateLabel = new JLabel("Repo Rate:",JLabel.LEFT);
		repoRateLabel.setPreferredSize(new Dimension(150, 15));
		repoRateField = new JTextField(15);
		repoRateField.setText("4.65");
		repoRatePanel.add(repoRateLabel);
		repoRatePanel.add(repoRateField);
		panel.add(repoRatePanel);

		repoRateField.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
		repoRateField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if ((e.getKeyChar() == KeyEvent.VK_TAB) || (e.getKeyChar() == KeyEvent.VK_ENTER)) {
					//System.out.print("Tab Pressed");
					updateTotalsXPrice();
				}
			}
		});


		JPanel cashCurrencyPanel = new JPanel(new GridBagLayout());
		JLabel cashCurrencyLabel = new JLabel("Loan Currency:",JLabel.LEFT);
		cashCurrencyLabel.setPreferredSize(new Dimension(150, 15));
		cashCurrencyField = new JTextField(15);
		cashCurrencyField.setText("GBP");
		cashCurrencyPanel.add(cashCurrencyLabel);
		cashCurrencyPanel.add(cashCurrencyField);
		panel.add(cashCurrencyPanel);


		JPanel haircutPanel = new JPanel(new GridBagLayout());
		JLabel haircutLabel = new JLabel("Haircut %:",JLabel.LEFT);
		haircutLabel.setPreferredSize(new Dimension(150, 15));
		haircutField = new JTextField(15);
		haircutField.setText("2");
		haircutPanel.add(haircutLabel);
		haircutPanel.add(haircutField);
		panel.add(haircutPanel);

		haircutField.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
		haircutField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if ((e.getKeyChar() == KeyEvent.VK_TAB) || (e.getKeyChar() == KeyEvent.VK_ENTER)) {
					//System.out.print("Tab Pressed");
					updateTotalsXPrice();
				}
			}
		});


		//Term Type

		JPanel termTypePanel = new JPanel(new GridBagLayout());
		JLabel termTypeLabel = new JLabel("Term Type:",JLabel.LEFT);
		termTypeLabel.setPreferredSize(new Dimension(150, 15));

		String[] termTypeChoices = { "FIXED", "OPEN"};
		termTypeField = new JComboBox<String>(termTypeChoices);
		termTypeField.setAlignmentX(Component.LEFT_ALIGNMENT);
		termTypeField.setPreferredSize(new Dimension(170, 20));

		termTypeField.setSelectedItem("FIXED");
		termTypePanel.add(termTypeLabel);
		termTypePanel.add(termTypeField);
		panel.add(termTypePanel);


		//Termination Option
		String[] terminationChoices = { "","Early Termination", "Evergreen", "Extendible"};
		JPanel terminationOptionPanel = new JPanel(new GridBagLayout());
		JLabel terminationOptionLabel = new JLabel("Termination Option:",JLabel.LEFT);
		terminationOptionLabel.setPreferredSize(new Dimension(150, 15));
		terminationOptionField = new JComboBox<String>(terminationChoices);

		terminationOptionField = new JComboBox<String>(terminationChoices);
		terminationOptionField.setAlignmentX(Component.LEFT_ALIGNMENT);
		terminationOptionField.setPreferredSize(new Dimension(170, 20));
		terminationOptionPanel.add(terminationOptionLabel);
		terminationOptionPanel.add(terminationOptionField);
		panel.add(terminationOptionPanel);
		
	
		
		JPanel noticePeriodPanel = new JPanel(new GridBagLayout());
		JLabel noticePeriodLabel = new JLabel("Notice Period:",JLabel.LEFT);
		noticePeriodLabel.setPreferredSize(new Dimension(150, 15));
		noticePeriodField = new JTextField(15);
		noticePeriodField.setText("0");
		noticePeriodPanel.add(noticePeriodLabel);
		noticePeriodPanel.add(noticePeriodField);
		panel.add(noticePeriodPanel);		
		
		//Delivery Method
		JPanel deliveryMethodPanel = new JPanel(new GridBagLayout());
		JLabel deliveryMethodLabel = new JLabel("Delivery Method:",JLabel.LEFT);
		deliveryMethodLabel.setPreferredSize(new Dimension(150, 15));
		deliveryMethodField = new JTextField(15);
		deliveryMethodField.setText("DVP");
		deliveryMethodPanel.add(deliveryMethodLabel);
		deliveryMethodPanel.add(deliveryMethodField);
		panel.add(deliveryMethodPanel);		
		
		//Substitution
		String[] substitutionChoices = { "N", "Y"};
		JPanel substitutionAllowedPanel = new JPanel(new GridBagLayout());
		JLabel substitutionAllowedLabel = new JLabel("Substitution:",JLabel.LEFT);
		substitutionAllowedLabel.setPreferredSize(new Dimension(150, 15));
		
		substitutionAllowedField = new JComboBox<String>(substitutionChoices);
		substitutionAllowedField.setAlignmentX(Component.LEFT_ALIGNMENT);
		substitutionAllowedField.setPreferredSize(new Dimension(170, 20));
		substitutionAllowedPanel.add(substitutionAllowedLabel);
		substitutionAllowedPanel.add(substitutionAllowedField);
		panel.add(substitutionAllowedPanel);

		
	
		//Day Count
		JPanel dayCountFractionPanel = new JPanel(new GridBagLayout());
		JLabel dayCountFractionLabel = new JLabel("Day Count:",JLabel.LEFT);
		dayCountFractionLabel.setPreferredSize(new Dimension(150, 15));
		dayCountFractionField = new JTextField(15);
		dayCountFractionField.setText("ACT/360");
		dayCountFractionPanel.add(dayCountFractionLabel);
		dayCountFractionPanel.add(dayCountFractionField);
		panel.add(dayCountFractionPanel);

		//Day Count
		JPanel termDaysPanel = new JPanel(new GridBagLayout());
		JLabel termDaysLabel = new JLabel("Term Days:",JLabel.LEFT);
		termDaysLabel.setPreferredSize(new Dimension(150, 15));
		termDaysField = new JTextField(15);
		termDaysField.setText("1");
		termDaysPanel.add(termDaysLabel);
		termDaysPanel.add(termDaysField);
		panel.add(termDaysPanel);

		long daysBetween = Duration.between(PDzonedDateTime,RDzonedDateTime).toDays();
		DecimalFormat intFormat = new DecimalFormat("###");
		String termsDaysStr = intFormat.format(daysBetween);
		termDaysField.setText(termsDaysStr);

		//Purchase Price
		JPanel purchasePricePanel = new JPanel(new GridBagLayout());
		JLabel purchasePriceLabel = new JLabel("Purchase Price:",JLabel.LEFT);
		purchasePriceLabel.setPreferredSize(new Dimension(150, 15));
		purchasePriceField = new JTextField(15);

		Double cv = Double.valueOf(collateralAdjustedValueField.getText());
		Double hc = Double.valueOf(haircutField.getText());
		Double pp = cv * (1- hc/100);

		DecimalFormat formatter = new DecimalFormat("#,###.00");
		String purchasePriceStr = formatter.format(pp);
		purchasePriceField.setText(purchasePriceStr);
		purchasePricePanel.add(purchasePriceLabel);
		purchasePricePanel.add(purchasePriceField);
		panel.add(purchasePricePanel);

		//Repurchase Price
		JPanel repurchasePricePanel = new JPanel(new GridBagLayout());
		JLabel repurchasePriceLabel = new JLabel("Repurchase Price:",JLabel.LEFT);
		repurchasePriceLabel.setPreferredSize(new Dimension(150, 15));
		repurchasePriceField = new JTextField(15);

		Double rr = Double.valueOf(repoRateField.getText());
		Double rp = pp  + (pp * (1.00/360.00 * rr/100.00));
		String repurchasePriceStr = formatter.format(rp);
		repurchasePriceField.setText(repurchasePriceStr);

		repurchasePricePanel.add(repurchasePriceLabel);
		repurchasePricePanel.add(repurchasePriceField);
		panel.add(repurchasePricePanel);

		//Panel 2

		//Trading Book
		String[] tradingBookChoices = { "UKBOOK", "USBOOK"};
		JPanel tradingBookOptionPanel = new JPanel(new GridBagLayout());
		JLabel tradingBookOptionLabel = new JLabel("Trading Book",JLabel.LEFT);
		tradingBookOptionLabel.setPreferredSize(new Dimension(150, 15));

		tradingBookOptionField = new JComboBox<String>(tradingBookChoices);
		tradingBookOptionField.setAlignmentX(Component.LEFT_ALIGNMENT);
		tradingBookOptionField.setPreferredSize(new Dimension(170, 20));
		tradingBookOptionPanel.add(tradingBookOptionLabel);
		tradingBookOptionPanel.add(tradingBookOptionField);
		panel2.add(tradingBookOptionPanel);


		//Business Center Option
		String[] businessCenterChoices = { "GBLO", "NYFD"};
		JPanel businessCenterOptionPanel = new JPanel(new GridBagLayout());
		JLabel businessCenterOptionLabel = new JLabel("Business Center:",JLabel.LEFT);
		businessCenterOptionLabel.setPreferredSize(new Dimension(150, 15));

		businessCenterOptionField = new JComboBox<String>(businessCenterChoices);
		businessCenterOptionField.setAlignmentX(Component.LEFT_ALIGNMENT);
		businessCenterOptionField.setPreferredSize(new Dimension(170, 20));
		businessCenterOptionPanel.add(businessCenterOptionLabel);
		businessCenterOptionPanel.add(businessCenterOptionField);
		panel2.add(businessCenterOptionPanel);

		//TimeZone
		String[] timeZoneChoices = { "UTC"};
		JPanel timeZoneOptionPanel = new JPanel(new GridBagLayout());
		JLabel timeZoneOptionLabel = new JLabel("Time Zone:",JLabel.LEFT);
		timeZoneOptionLabel.setPreferredSize(new Dimension(150, 15));

		timeZoneOptionField = new JComboBox<String>(timeZoneChoices);
		timeZoneOptionField.setAlignmentX(Component.LEFT_ALIGNMENT);
		timeZoneOptionField.setPreferredSize(new Dimension(170, 20));
		timeZoneOptionPanel.add(timeZoneOptionLabel);
		timeZoneOptionPanel.add(timeZoneOptionField);
		panel2.add(timeZoneOptionPanel);

		//Execution Time
		JPanel executionTimePanel = new JPanel(new GridBagLayout());
		JLabel executionTimeLabel = new JLabel("Execution Time:",JLabel.LEFT);
		executionTimeLabel.setPreferredSize(new Dimension(150, 15));
		executionTimeField = new JTextField(15);

		DateTimeFormatter ETformatter = DateTimeFormatter.ofPattern("hh:mm:ss");
		ETzonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of(this.defaultLocalTimeZone));
		ETformattedDateTimeString = ETzonedDateTime.format(ETformatter);
		executionTimeField.setText(ETformattedDateTimeString);
		executionTimePanel.add(executionTimeLabel);
		executionTimePanel.add(executionTimeField);
		panel2.add(executionTimePanel);

		//Venue
		String[] venueCodeChoices = { "OTC", "FINX"};
		JPanel venueCodeOptionPanel = new JPanel(new GridBagLayout());
		JLabel venueCodeOptionLabel = new JLabel("Execution Venue:",JLabel.LEFT);
		venueCodeOptionLabel.setPreferredSize(new Dimension(150, 15));

		venueCodeOptionField = new JComboBox<String>(venueCodeChoices);
		venueCodeOptionField.setAlignmentX(Component.LEFT_ALIGNMENT);
		venueCodeOptionField.setPreferredSize(new Dimension(170, 20));
		venueCodeOptionPanel.add(venueCodeOptionLabel);
		venueCodeOptionPanel.add(venueCodeOptionField);
		panel2.add(venueCodeOptionPanel);

		//Settlement Agent
		JPanel settlementAgentOptionPanel = new JPanel(new GridBagLayout());
		JLabel settlementAgentOptionLabel = new JLabel("Settlement Agent:",JLabel.LEFT);
		settlementAgentOptionLabel.setPreferredSize(new Dimension(150, 15));


		settlementAgentOptionField = new JComboBox();
		settlementAgentOptionField.addItem(new CItem("",""));
		settlementAgentOptionField.addItem(new CItem("Euroclear Bank","549300OZ46BRLZ8Y6F65"));
		settlementAgentOptionField.addItem(new CItem("BNP Paribas Securities Services","549300WCGB70D06XZS54"));

		settlementAgentOptionField.setAlignmentX(Component.LEFT_ALIGNMENT);
		settlementAgentOptionField.setPreferredSize(new Dimension(170, 20));
		settlementAgentOptionPanel.add(settlementAgentOptionLabel);
		settlementAgentOptionPanel.add(settlementAgentOptionField);
		panel2.add(settlementAgentOptionPanel);

		//CCP
		JPanel centralClearingCounterpartyOptionPanel = new JPanel(new GridBagLayout());
		JLabel centralClearingCounterpartyOptionLabel = new JLabel("CCP:",JLabel.LEFT);
		centralClearingCounterpartyOptionLabel.setPreferredSize(new Dimension(150, 15));

		centralClearingCounterpartyOptionField = new JComboBox();
		centralClearingCounterpartyOptionField.addItem(new CItem("",""));
		centralClearingCounterpartyOptionField.addItem(new CItem("LCH SA","R1IO4YJ0O79SMWVCHB58"));

		centralClearingCounterpartyOptionField.setAlignmentX(Component.LEFT_ALIGNMENT);
		centralClearingCounterpartyOptionField.setPreferredSize(new Dimension(170, 20));
		centralClearingCounterpartyOptionPanel.add(centralClearingCounterpartyOptionLabel);
		centralClearingCounterpartyOptionPanel.add(centralClearingCounterpartyOptionField);
		panel2.add(centralClearingCounterpartyOptionPanel);


		//CSD Participant
		JPanel csdOptionPanel = new JPanel(new GridBagLayout());
		JLabel csdOptionLabel = new JLabel("CSD Particpant:",JLabel.LEFT);
		csdOptionLabel.setPreferredSize(new Dimension(150, 15));
		csdOptionField = new JComboBox();
		ru.addParties(csdOptionField);

		csdOptionField.setAlignmentX(Component.LEFT_ALIGNMENT);
		csdOptionField.setPreferredSize(new Dimension(170, 20));
		csdOptionPanel.add(csdOptionLabel);
		csdOptionPanel.add(csdOptionField);
		panel2.add(csdOptionPanel);

		//Clearing Member
		JPanel clearingMemberPanel = new JPanel(new GridBagLayout());
		JLabel clearingMemberLabel = new JLabel("Clearing Member:",JLabel.LEFT);
		clearingMemberLabel.setPreferredSize(new Dimension(150, 15));


		clearingMemberField = new JComboBox();
		ru.addParties(clearingMemberField);

		clearingMemberField.setAlignmentX(Component.LEFT_ALIGNMENT);
		clearingMemberField.setPreferredSize(new Dimension(170, 20));
		clearingMemberPanel.add(clearingMemberLabel);
		clearingMemberPanel.add(clearingMemberField);
		panel2.add(clearingMemberPanel);



		//Agent Lender
		JPanel agentLenderPanel = new JPanel(new GridBagLayout());
		JLabel agentLenderLabel = new JLabel("Agent Lender:",JLabel.LEFT);
		agentLenderLabel.setPreferredSize(new Dimension(150, 15));

		agentLenderField = new JComboBox();
		ru.addParties(agentLenderField);

		agentLenderField.setAlignmentX(Component.LEFT_ALIGNMENT);
		agentLenderField.setPreferredSize(new Dimension(170, 20));
		agentLenderPanel.add(agentLenderLabel);
		agentLenderPanel.add(agentLenderField);
		panel2.add(agentLenderPanel);

		//Broker
		JPanel brokerPanel = new JPanel(new GridBagLayout());
		JLabel brokerLabel = new JLabel("Broker:",JLabel.LEFT);
		brokerLabel.setPreferredSize(new Dimension(150, 15));

		brokerField = new JComboBox();
		brokerField.addItem(new CItem("","", "", ""));
		brokerField.addItem(new CItem("Tullet Prebon Securities","549300BMVW85YF9FGN67", "TSRE", "MIC"));

		brokerField.setAlignmentX(Component.LEFT_ALIGNMENT);
		brokerField.setPreferredSize(new Dimension(170, 20));
		brokerPanel.add(brokerLabel);
		brokerPanel.add(brokerField);
		panel2.add(brokerPanel);

		//TriParty Agent
		JPanel tripartyPanel = new JPanel(new GridBagLayout());
		JLabel tripartyLabel = new JLabel("Tri-Party Agent:",JLabel.LEFT);
		tripartyLabel.setPreferredSize(new Dimension(150, 15));

		tripartyField = new JComboBox();
		tripartyField.addItem(new CItem("",""));
		tripartyField.addItem(new CItem("Euroclear Bank","549300OZ46BRLZ8Y6F65"));
		tripartyField.addItem(new CItem("BNP Paribas Securities Services","549300WCGB70D06XZS54"));

		tripartyField.setAlignmentX(Component.LEFT_ALIGNMENT);
		tripartyField.setPreferredSize(new Dimension(170, 20));
		tripartyPanel.add(tripartyLabel);
		tripartyPanel.add(tripartyField);
		panel2.add(tripartyPanel);

		//Beneficiary
		JPanel beneficiaryPanel = new JPanel(new GridBagLayout());
		JLabel beneficiaryLabel = new JLabel("Beneficiary:",JLabel.LEFT);
		beneficiaryLabel.setPreferredSize(new Dimension(150, 15));

		beneficiaryField = new JComboBox();
		beneficiaryField = ru.addParties(beneficiaryField);

		beneficiaryField.setAlignmentX(Component.LEFT_ALIGNMENT);
		beneficiaryField.setPreferredSize(new Dimension(170, 20));
		beneficiaryPanel.add(beneficiaryLabel);
		beneficiaryPanel.add(beneficiaryField);
		panel2.add(beneficiaryPanel);


		//Floating Rate Reference
		JPanel floatingRateReferencePanel = new JPanel(new GridBagLayout());
		JLabel floatingRateReferenceLabel = new JLabel("Floating Rate Ref:",JLabel.LEFT);
		floatingRateReferenceLabel.setPreferredSize(new Dimension(150, 15));
		floatingRateReferenceField = new JTextField(15);

		floatingRateReferencePanel.add(floatingRateReferenceLabel);
		floatingRateReferencePanel.add(floatingRateReferenceField);
		floatingRateReferenceField.setEnabled(false);
		panel2.add(floatingRateReferencePanel);

		//Floating Rate Reference Period
		JPanel floatingRateReferencePeriodPanel = new JPanel(new GridBagLayout());
		JLabel floatingRateReferencePeriodLabel = new JLabel("Floating Rate Period:",JLabel.LEFT);
		floatingRateReferencePeriodLabel.setPreferredSize(new Dimension(150, 15));

		String[] referencePeriods = { "DAYS", "WEEKS", "MONTHS"};
		floatingRateReferencePeriodField = new JComboBox<String>(referencePeriods);
		floatingRateReferencePeriodField .setAlignmentX(Component.LEFT_ALIGNMENT);
		floatingRateReferencePeriodField.setPreferredSize(new Dimension(170, 20));


		floatingRateReferencePeriodPanel.add(floatingRateReferencePeriodLabel);
		floatingRateReferencePeriodPanel.add(floatingRateReferencePeriodField);
		floatingRateReferencePeriodField.setEnabled(false);
		panel2.add(floatingRateReferencePeriodPanel);


		//Floating Rate Reference Multiplier
		JPanel floatingRateReferenceMultiplierPanel = new JPanel(new GridBagLayout());
		JLabel floatingRateReferenceMultiplierLabel = new JLabel("Floating Rate Freq:",JLabel.LEFT);
		floatingRateReferenceMultiplierLabel.setPreferredSize(new Dimension(150, 15));
		floatingRateReferenceMultiplierField = new JTextField(15);

		floatingRateReferenceMultiplierPanel.add(floatingRateReferenceMultiplierLabel);
		floatingRateReferenceMultiplierPanel.add(floatingRateReferenceMultiplierField);
		panel2.add(floatingRateReferenceMultiplierPanel);

		//Floating Payment Frequency
		JPanel floatingRatePaymentFreqPanel = new JPanel(new GridBagLayout());
		JLabel floatingRatePaymentFreqLabel = new JLabel("Floating Payment Freq:",JLabel.LEFT);
		floatingRatePaymentFreqLabel.setPreferredSize(new Dimension(150, 15));

		String[] paymentPeriods = { "DAY", "WEEK", "MONTH"};
		floatingRatePaymentFreqField = new JComboBox<String>(paymentPeriods);
		floatingRatePaymentFreqField.setAlignmentX(Component.LEFT_ALIGNMENT);
		floatingRatePaymentFreqField.setPreferredSize(new Dimension(170, 20));

		floatingRatePaymentFreqPanel.add(floatingRatePaymentFreqLabel);
		floatingRatePaymentFreqPanel.add(floatingRatePaymentFreqField);
		floatingRatePaymentFreqField.setEnabled(false);
		panel2.add(floatingRatePaymentFreqPanel);

		//Floating Payment Multiplier
		JPanel floatingRatePaymentMultiplierPanel = new JPanel(new GridBagLayout());
		JLabel floatingRatePaymentMultiplierLabel = new JLabel("Floating Payment Multi:",JLabel.LEFT);
		floatingRatePaymentMultiplierLabel.setPreferredSize(new Dimension(150, 15));
		floatingRatePaymentMultiplierField = new JTextField(15);

		floatingRatePaymentMultiplierPanel.add(floatingRatePaymentMultiplierLabel);
		floatingRatePaymentMultiplierPanel.add(floatingRatePaymentMultiplierField);
		floatingRatePaymentMultiplierField.setEnabled(false);
		panel2.add(floatingRatePaymentMultiplierPanel);

		//Floating Reset Frequency
		JPanel floatingRateResetFreqPanel = new JPanel(new GridBagLayout());
		JLabel floatingRateResetFreqLabel = new JLabel("Floating Reset Freq:",JLabel.LEFT);
		floatingRateResetFreqLabel.setPreferredSize(new Dimension(150, 15));
		floatingRateResetFreqField = new JComboBox<String>(referencePeriods);
		floatingRateResetFreqField.setAlignmentX(Component.LEFT_ALIGNMENT);
		floatingRateResetFreqField.setPreferredSize(new Dimension(170, 20));

		floatingRateResetFreqPanel.add(floatingRateResetFreqLabel);
		floatingRateResetFreqPanel.add(floatingRateResetFreqField);
		floatingRateResetFreqField.setEnabled(false);
		panel2.add(floatingRateResetFreqPanel);

		//Floating Reset Multiplier
		JPanel floatingRateResetMultiplierPanel = new JPanel(new GridBagLayout());
		JLabel floatingRateResetMultiplierLabel = new JLabel("Floating Reset Multi:",JLabel.LEFT);
		floatingRateResetMultiplierLabel.setPreferredSize(new Dimension(150, 15));
		floatingRateResetMultiplierField = new JTextField(15);

		floatingRateResetMultiplierPanel.add(floatingRateResetMultiplierLabel);
		floatingRateResetMultiplierPanel.add(floatingRateResetMultiplierField);
		floatingRateResetMultiplierField.setEnabled(false);
		panel2.add(floatingRateResetMultiplierPanel);

		//Floating Reference Rate
		JPanel floatingRatePanel = new JPanel(new GridBagLayout());
		JLabel floatingRateLabel = new JLabel("Reference Rate:",JLabel.LEFT);
		floatingRateLabel.setPreferredSize(new Dimension(150, 15));
		floatingRateField = new JTextField(15);

		floatingRatePanel.add(floatingRateLabel);
		floatingRatePanel.add(floatingRateField);
		floatingRateField.setEnabled(false);
		panel2.add(floatingRatePanel);

		floatingRateField.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
		floatingRateField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if ((e.getKeyChar() == KeyEvent.VK_TAB) || (e.getKeyChar() == KeyEvent.VK_ENTER)) {
					//System.out.print("Tab Pressed");
					updateTotalsXPrice();
				}
			}
		});


		//Spread
		JPanel floatingRateSpreadPanel = new JPanel(new GridBagLayout());
		JLabel floatingRateSpreadLabel = new JLabel("Floating Spread:",JLabel.LEFT);
		floatingRateSpreadLabel.setPreferredSize(new Dimension(150, 15));
		floatingRateSpreadField = new JTextField(15);

		floatingRateSpreadPanel.add(floatingRateSpreadLabel);
		floatingRateSpreadPanel.add(floatingRateSpreadField);
		floatingRateSpreadField.setEnabled(false);
		panel2.add(floatingRateSpreadPanel);

		floatingRateSpreadField.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
		floatingRateSpreadField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if ((e.getKeyChar() == KeyEvent.VK_TAB) || (e.getKeyChar() == KeyEvent.VK_ENTER)) {
					//System.out.print("Tab Pressed");
					updateTotalsXPrice();
				}
			}
		});




		//AgreementName
		JPanel agreementNamePanel = new JPanel(new GridBagLayout());
		JLabel agreementNameLabel = new JLabel("Agreement Name:",JLabel.LEFT);
		agreementNameLabel.setPreferredSize(new Dimension(150, 15));
		agreementNameField = new JTextField(15);
		agreementNameField.setText("GMRA");
		agreementNamePanel.add(agreementNameLabel);
		agreementNamePanel.add(agreementNameField);
		panel2.add(agreementNamePanel);

		//Agreement Version
		JPanel agreementVersionPanel = new JPanel(new GridBagLayout());
		JLabel agreementVersionLabel = new JLabel("Agreement Version:",JLabel.LEFT);
		agreementVersionLabel.setPreferredSize(new Dimension(150, 15));
		agreementVersionField = new JTextField(15);
		agreementVersionField.setText("2011");
		agreementVersionPanel.add(agreementVersionLabel);
		agreementVersionPanel.add(agreementVersionField);
		panel2.add(agreementVersionPanel);

		//Trade ID
		JPanel tradeIdPanel = new JPanel(new GridBagLayout());
		JLabel tradeIdLabel = new JLabel("Trade ID",JLabel.LEFT);
		tradeIdLabel.setPreferredSize(new Dimension(150, 15));
		tradeIdField = new JTextField(15);
		tradeIdField.setText("");
		tradeIdPanel.add(tradeIdLabel);
		tradeIdPanel.add(tradeIdField);
		panel2.add(tradeIdPanel);

		//Agreement Version
		JPanel statusPanel = new JPanel(new GridBagLayout());
		JLabel statusLabel = new JLabel("Status:",JLabel.LEFT);
		statusLabel.setPreferredSize(new Dimension(150, 15));
		statusField = new JTextField(15);
		statusField.setText("NEW");
		statusPanel.add(statusLabel);
		statusPanel.add(statusField);
		panel2.add(statusPanel);




		//Action Button Panel
		JPanel actionPanel = new JPanel(new GridBagLayout());
		GridBagConstraints apgbc = new GridBagConstraints();
		apgbc.gridy = 0;
		apgbc.anchor = gbc.PAGE_END;
		actionPanel.setSize( 800, 200 );
		//actionPanel.setBorder(blackline);

		JPanel actionBtnPanel = new JPanel(new GridLayout(2,4));
		actionBtnPanel.setSize( 800, 200 );

		newTradeBtn = new JButton("New Trade");
		newTradeBtn.addActionListener(this);
		newTradeBtn.setPreferredSize(new Dimension(160, 30));
		actionBtnPanel.add(newTradeBtn);
		
		bookTradeBtn = new JButton("Book Trade");
		bookTradeBtn.addActionListener(this);
		bookTradeBtn.setPreferredSize(new Dimension(160, 30));
		actionBtnPanel.add(bookTradeBtn);


		rollTradeBtn = new JButton("Roll");
		rollTradeBtn.addActionListener(this);
		actionBtnPanel.add(rollTradeBtn);

		rerateTradeBtn = new JButton("Re-Rate");
		rerateTradeBtn.addActionListener(this);
		actionBtnPanel.add(rerateTradeBtn);

		terminateTradeBtn = new JButton("Terminate");
		terminateTradeBtn.addActionListener(this);
		actionBtnPanel.add(terminateTradeBtn);

		onDemandPaymentBtn = new JButton("Interest Payment");
		onDemandPaymentBtn.addActionListener(this);
		actionBtnPanel.add(onDemandPaymentBtn);

		reportTradeBtn = new JButton("SFTR");
		reportTradeBtn.addActionListener(this);
		actionBtnPanel.add(reportTradeBtn);

		JButton btn24 = new JButton("BTN 8");
		btn24.addActionListener(this);
		actionBtnPanel.add(btn24);

		actionPanel.add(actionBtnPanel);
		frame.add(actionPanel,apgbc);

		frame.setVisible( true );


	}
	
	public void loadNewTrade() {
	
		
		
		//Trade Date

		LocalDateTime localDateTime = LocalDateTime.now();
		ZonedDateTime startDateTime = ZonedDateTime.of(localDateTime, ZoneId.of(this.defaultLocalTimeZone));

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDateTimeString = startDateTime.format(formatter);
		tradeDateField.setText(formattedDateTimeString);

		//Execution Time
		DateTimeFormatter ETformatter = DateTimeFormatter.ofPattern("hh:mm:ss");
		ETzonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of(this.defaultLocalTimeZone));
		ETformattedDateTimeString = ETzonedDateTime.format(ETformatter);
		executionTimeField.setText(ETformattedDateTimeString);

		//Purchase Date
		purchaseDateField.setText(formattedDateTimeString);

		
		
		//Repurchase Date
		ZonedDateTime endDateTime = startDateTime.plusDays(1);
		formattedDateTimeString = endDateTime.format(formatter);
		repurchaseDateField.setText(formattedDateTimeString);

		
		//UTI
		tradeUTIField.setText("ICMA202303231000178");


		
		//Collateral
		collateralDescriptionField.setText("GILT .5 22/07/2022");


		//Collateral ISIN
		collateralISINField.setText("GB00BD0PCK97");

		
		//Collateral Quantity
		collateralQuantityField.setText("1000");


		//Collateral Price
		collateralCleanPriceField.setText("100.75");


		//Collateral Dirty Price
		collateralDirtyPriceField.setText("100.8066");


		//Collateral Adjusted Value
		collateralAdjustedValueField.setText("1008066.00");

		//Collateral Currency
		collateralCurrencyField.setText("GBP");

		
		//Repo Rate
		repoRateField.setText("4.65");


		//Cash Currency
		cashCurrencyField.setText("GBP");



		//Haircut
		haircutField.setText("2");


		//Term Type
		termTypeField.setSelectedItem("FIXED");


		
		//Day Count
		dayCountFractionField.setText("ACT/360");

		//Term Days
		long daysBetween = Duration.between(startDateTime,endDateTime).toDays();
		DecimalFormat intFormat = new DecimalFormat("###");
		String termsDaysStr = intFormat.format(daysBetween);
		termDaysField.setText(termsDaysStr);

		//Purchase Price
		purchasePriceField.setText("9879046.80");

		rateTypeField.setSelectedItem("FIXED");
		rateTypeFieldEvent("FIXED");

		//Repurchase Price
		repurchasePriceField.setText("9879155.06");

		agreementNameField.setText("GMRA");

		agreementVersionField.setText("2011");

		//Reset trade states to null
		this.afterTradeStateStr = null;
		this.beforeTradeStateStr = null;
		this.tradeStateStr = null;

		this.initRepoWorkflow();

	}
	
	
	//Button Events
	public void actionPerformed(ActionEvent ae) {
		
		if(ae.getSource() == this.newTradeBtn){
				JOptionPane.showMessageDialog(this, "Create New Trade", "Alert", JOptionPane.INFORMATION_MESSAGE);
				this.loadNewTrade();

		   }
		else if(ae.getSource() == this.bookTradeBtn){
			JOptionPane.showMessageDialog(this, "Book Trade", "Alert", JOptionPane.INFORMATION_MESSAGE);
			try {
				String businessEvent = this.bookTrade();
				this.statusField.setText("EXECUTED");
				System.out.println(businessEvent);
				outputArea.setText(businessEvent);

				IcmaRepoUtil ru = new IcmaRepoUtil();
				this.tradeStateStr = ru.getAfterTradeState(businessEvent);
				this.afterTradeStateStr = this.tradeStateStr;


				DateTimeFormatter eventDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
				LocalDateTime localDateTime = LocalDateTime.now();
				String eventDateTime = localDateTime.format(eventDateFormat);

				ru.writeEventToFile("execution", eventDateTime, businessEvent);

				} catch (IOException e){
			}
		   }  
		else if(ae.getSource() == this.rollTradeBtn){
			JOptionPane.showMessageDialog(this, "Roll Trade", "Alert", JOptionPane.INFORMATION_MESSAGE);
			//Purchase Date
			PDzonedDateTime = PDzonedDateTime.plusDays(1);
			PDformattedDateTimeString = PDzonedDateTime.format(formatter);
			purchaseDateField.setText(PDformattedDateTimeString);

			//Repurchase Date
			RDzonedDateTime = RDzonedDateTime.plusDays(1);
			RDformattedDateTimeString = RDzonedDateTime.format(formatter);
			repurchaseDateField.setText(RDformattedDateTimeString);
			
			try {
				String businessEvent = this.rollTrade();
				this.statusField.setText("ROLLED");
				System.out.println(businessEvent);
				outputArea.setText(businessEvent);

				IcmaRepoUtil ru = new IcmaRepoUtil();

				DateTimeFormatter eventDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
				LocalDateTime localDateTime = LocalDateTime.now();
				String eventDateTime = localDateTime.format(eventDateFormat);
				ru.writeEventToFile("roll_trade", eventDateTime, businessEvent);


			} catch (JsonProcessingException e){
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		else if(ae.getSource() == this.terminateTradeBtn){
			JOptionPane.showMessageDialog(this, "Terminate Trade", "Alert", JOptionPane.INFORMATION_MESSAGE);

			//Repurchase Date
			RDzonedDateTime = RDzonedDateTime.plusDays(2);
			RDformattedDateTimeString = RDzonedDateTime.format(formatter);
			repurchaseDateField.setText(RDformattedDateTimeString);

			try {
				String businessEvent = this.earlyTerminateTrade();
				this.statusField.setText("EARLY TERMINATION");
				System.out.println(businessEvent);
				outputArea.setText(businessEvent);

				IcmaRepoUtil ru = new IcmaRepoUtil();

				DateTimeFormatter eventDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
				LocalDateTime localDateTime = LocalDateTime.now();
				String eventDateTime = localDateTime.format(eventDateFormat);
				ru.writeEventToFile("termination", eventDateTime, businessEvent);


			} catch (JsonProcessingException e){
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		else if(ae.getSource() == this.rerateTradeBtn){
			JOptionPane.showMessageDialog(this, "Rerate Trade", "Alert", JOptionPane.INFORMATION_MESSAGE);

			try {

				String businessEvent = this.onDemandRateChange();
				this.statusField.setText("RERATE");
				//String repoBusinesseventJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(businessEvent);

				System.out.println(businessEvent);
				outputArea.setText(businessEvent);

				IcmaRepoUtil ru = new IcmaRepoUtil();

				DateTimeFormatter eventDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
				LocalDateTime localDateTime = LocalDateTime.now();
				String eventDateTime = localDateTime.format(eventDateFormat);
				ru.writeEventToFile("rerate", eventDateTime, businessEvent);


			} catch (JsonProcessingException e){
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		else if(ae.getSource() == this.onDemandPaymentBtn){
			JOptionPane.showMessageDialog(this, "Interest Payment", "Alert", JOptionPane.INFORMATION_MESSAGE);


			try {
				String businessEvent = this.onDemandPayment();
				this.statusField.setText("INTERESTPAYMENT");
				//String repoBusinesseventJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(businessEvent);

				System.out.println(businessEvent);
				outputArea.setText(businessEvent);

				IcmaRepoUtil ru = new IcmaRepoUtil();

				DateTimeFormatter eventDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
				LocalDateTime localDateTime = LocalDateTime.now();
				String eventDateTime = localDateTime.format(eventDateFormat);
				ru.writeEventToFile("interestpayment", eventDateTime, businessEvent);


			} catch (JsonProcessingException e){
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		else if(ae.getSource() == this.reportTradeBtn){
			JOptionPane.showMessageDialog(this, "SFTR Reporting Not Available", "Alert", JOptionPane.INFORMATION_MESSAGE);

		}
			
		}
		public void updateTotalsXPrice(){

			//System.out.println("Update Totals");

			DecimalFormat formatter = new DecimalFormat("#,###.00");

			DateTimeFormatter dtFormat  = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");

			String pdt = purchaseDateField.getText();
			String purchaseDateStr = pdt.replaceAll("\\s", "") + "T00:00:00.000+00:00";
			ZonedDateTime startDateTime = ZonedDateTime.parse(purchaseDateStr,dtFormat);

			String rpdt = repurchaseDateField.getText();
			String repurchaseDateStr = rpdt.replaceAll("\\s", "") + "T00:00:00.000+00:00";
			ZonedDateTime endDateTime = ZonedDateTime.parse(repurchaseDateStr,dtFormat);

			long daysBetween = Duration.between(startDateTime,endDateTime).toDays();
			DecimalFormat intFormat = new DecimalFormat("###");
			String termsDaysStr = intFormat.format(daysBetween);
			termDaysField.setText(termsDaysStr);

			Double dp = Double.valueOf(collateralDirtyPriceField.getText().replaceAll(",","").trim());
			Double cc = Double.valueOf(collateralQuantityField.getText().replaceAll(",","").trim());
			Double cap = cc * dp/100.00 * 1000;
			String collateralAdjustedValueStr = formatter.format(cap);
			collateralAdjustedValueField.setText(collateralAdjustedValueStr);

			//s.replaceAll(",","").trim();

			Double cv = Double.valueOf(collateralAdjustedValueField.getText().replaceAll(",","").trim());
			Double hc = Double.valueOf(haircutField.getText().replaceAll(",","").trim());
			Double pp = cv * (1- hc/100.00);


			String purchasePriceStr = formatter.format(pp);
			purchasePriceField.setText(purchasePriceStr);

			if(this.rateTypeField.getSelectedItem().toString().equals("FLOAT")) {

				Double refrate = Double.parseDouble(this.floatingRateField.getText());
				Double spread = Double.parseDouble(this.floatingRateSpreadField.getText());
				Double reporate = (double) Math.round((refrate + spread/100.0)*100)/100;
				this.repoRateField.setText(reporate.toString());

			}


			Double rr = Double.valueOf(repoRateField.getText().replaceAll(",","").trim());
			Double rp = pp  + (pp * (daysBetween/360.00 * rr/100.00));
			String repurchasePriceStr = formatter.format(rp);
			repurchasePriceField.setText(repurchasePriceStr);



		}
	
	

	//Functions linked to button actions
	
	public String bookTrade()throws IOException{


		String tdt = this.tradeDateField.getText();
		String tradeDateStr = tdt.replaceAll("\\s", "") + "T00:00:00.000+00:00";
		
		String pdt = this.purchaseDateField.getText();
		String purchaseDateStr = pdt.replaceAll("\\s", "") + "T00:00:00.000+00:00";
		
		String rdt = this.repurchaseDateField.getText();
		String repurchaseDateStr = rdt.replaceAll("\\s", "") + "T00:00:00.000+00:00";
		
		String tradeUTIStr = this.tradeUTIField.getText();


		CItem buyer = (CItem) this.buyerLEIField.getSelectedItem();
		String buyerNameStr = buyer.getClabel();
		String buyerLEIStr = buyer.getCValue();
		if(buyerLEIStr .equals("")) {
			JOptionPane.showMessageDialog(this, "Buyer cannot be empty", "Alert", JOptionPane.INFORMATION_MESSAGE);
			throw new IOException("Buyer cannot be empty");
		}

		CItem seller = (CItem) this.sellerLEIField.getSelectedItem();
		String sellerNameStr = seller.getClabel();
		String sellerLEIStr = seller.getCValue();
		if(sellerLEIStr .equals("")) {
			JOptionPane.showMessageDialog(this, "Seller cannot be empty", "Alert", JOptionPane.INFORMATION_MESSAGE);
			throw new IOException("Seller cannot be empty");
		}

		String collateralDescriptionStr = this.collateralDescriptionField.getText();
		String collateralISINStr = this.collateralISINField.getText();
		
		String collateralQuantityStr = this.collateralQuantityField.getText().replaceAll(",","").trim();
		String collateralCleanPriceStr = this.collateralCleanPriceField.getText();
		String collateralDirtyPriceStr = this.collateralDirtyPriceField.getText();
		String collateralAdjustedValueStr = this.collateralAdjustedValueField.getText().replaceAll(",","").trim();
		String collateralCurrencyStr = this.collateralCurrencyField.getText();
		String repoRateStr = this.repoRateField.getText();
		String cashCurrencyStr = this.cashCurrencyField.getText();
		String haircutStr = this.haircutField.getText();
		String termTypeStr = this.termTypeField.getSelectedItem().toString();
		String terminationOptionStr = this.terminationOptionField.getSelectedItem().toString();
		String noticePeriodStr = this.noticePeriodField.getText();
		String deliveryMethodStr = this.deliveryMethodField.getText();
		String substitutionAllowedStr = this.substitutionAllowedField.getSelectedItem().toString();
		String rateTypeStr = this.rateTypeField.getSelectedItem().toString();
		String dayCountFractionStr = this.dayCountFractionField.getText();
		String purchasePriceStr = this.purchasePriceField.getText().replaceAll(",","").trim();
		String cashQuantityStr = purchasePriceStr;
		String repurchasePriceStr = this.repurchasePriceField.getText().replaceAll(",","").trim();

		//Predefined Values
		String agreementNameStr = this.agreementNameField.getText();
		String agreementGoverningLawStr = "England";
		String agreementVintageStr = this.agreementVersionField.getText();
		String agreementPublisherStr = "ICMA";
		String agreementDateStr = "2011-04-20";
		String agreementIdentifierStr =  "GMRA-2011_2011-04-20";
		String agreementEffectiveDate = "2011-04-20";
		String agreementUrl = "https://www.icmagroup.org/assets/documents/Legal/GMRA-2011_2011.04.20_formular_copy-incl-annexes-and-buy-sell-back-annex.pdf";
		String businessCenter = this.businessCenterOptionField.getSelectedItem().toString();
		String execVenueCode = this.venueCodeOptionField.getSelectedItem().toString();
		String execVenueScheme = "MIC";

		String floatingRateReferenceStr = this.floatingRateReferenceField.getText();
		String floatingRateReferencePeriodStr = this.floatingRateReferencePeriodField.getSelectedItem().toString();
		String floatingRateReferenceMultiplierStr = this.floatingRateReferenceMultiplierField.getText();
		String floatingRateResetFreqStr = this.floatingRateResetFreqField.getSelectedItem().toString();
		String floatingRateResetMultiplierStr = this.floatingRateResetMultiplierField.getText();
		String floatingRatePaymentFreqStr = this.floatingRatePaymentFreqField.getSelectedItem().toString();
		String floatingRatePaymentMultiplierStr = this.floatingRatePaymentMultiplierField.getText();

		//Set settlement agent
		CItem settlementAgent= (CItem) this.settlementAgentOptionField.getSelectedItem();
		String settlementAgentNameStr = settlementAgent.getClabel();
		String settlementAgentLEIStr = settlementAgent.getCValue();

		if(settlementAgentNameStr.equals("")) {
			JOptionPane.showMessageDialog(this, "Settlement Agent cannot be empty", "Alert", JOptionPane.INFORMATION_MESSAGE);
			throw new IOException("Settlement Agent cannot be empty");
		}

		//Set ccp
		CItem ccp = (CItem) this.centralClearingCounterpartyOptionField.getSelectedItem();
		String ccpNameStr = ccp.getClabel();
		String ccpLeiStr = ccp.getCValue();

		//Set CSD Participant
		CItem csdParticipant = (CItem) this.csdOptionField.getSelectedItem();
		String csdParticipantNameStr = csdParticipant.getClabel();
		String csdParticipantLeiStr = csdParticipant.getCValue();

		//Set Clearing Member
		CItem clearingMember = (CItem) this.clearingMemberField.getSelectedItem();
		String clearingMemberNameStr = clearingMember.getClabel();
		String clearingMemberLeiStr = clearingMember.getCValue();

		//Set Broker
		CItem broker = (CItem) this.brokerField.getSelectedItem();
		String brokerNameStr = broker.getClabel();
		String brokerLeiStr = broker.getCValue();

		//Set Triparty
		CItem triparty= (CItem) this.tripartyField.getSelectedItem();
		String tripartyNameStr = triparty.getClabel();
		String tripartyLeiStr = triparty.getCValue();

		//Enum Mapping
		CdmEnumMap map = new CdmEnumMap();
		map.buildEnumMap(cdmMap);

		deliveryMethodStr = cdmMap.get(deliveryMethodStr);
		floatingRateReferencePeriodStr = cdmMap.get(floatingRateReferencePeriodStr);
		floatingRateResetFreqStr = cdmMap.get(floatingRateResetFreqStr);
		floatingRatePaymentFreqStr = cdmMap.get(floatingRatePaymentFreqStr);
		floatingRateReferenceStr = cdmMap.get(floatingRateReferenceStr);

		RepoLifeCycle repoExecution = new RepoLifeCycle();

		String businessEvent = repoExecution.RepoExecution(
		tradeDateStr,					// tradeDateStr = .getText();
		purchaseDateStr,				// purchaseDateStr
		repurchaseDateStr,				// repurchaseDateStr
		tradeUTIStr,  					// tradeUTIStr
		buyerLEIStr, 					// buyerLEIStr,
		buyerNameStr,			// buyerNameStr
		sellerLEIStr, 					// sellerLEIStr
		sellerNameStr,
		collateralDescriptionStr,		// collateralDescriptionStr
		collateralISINStr,				// collateralISINStr
		collateralQuantityStr,			// collateralQuantitySt
		collateralCleanPriceStr,		// collateralDirtyPriceStr
		collateralDirtyPriceStr,		// collateralDirtyPriceStr
		collateralAdjustedValueStr,		// collateralAdjustedValueStr
		collateralCurrencyStr,			// collateralCurrencyStr
		repoRateStr,					// repoRateStr
		cashCurrencyStr,				// cashCurrencyStr
		cashQuantityStr,				// cashQuantityStr
		haircutStr,						// haircutStr
		termTypeStr,					// termTypeStr
		terminationOptionStr,			// terminationOptionStr
		noticePeriodStr,				// noticePeriodStr
		deliveryMethodStr,				// deliveryMethodStr
		substitutionAllowedStr,			// substitutionAllowedStr
		rateTypeStr,					// rateTypeStr
		dayCountFractionStr,			// dayCountFractionStr
		purchasePriceStr, 				// purchasePriceStr
		repurchasePriceStr,				// repurchasePriceStr
		agreementNameStr,
		agreementGoverningLawStr,
		agreementVintageStr,
		agreementPublisherStr,
		agreementDateStr,
		agreementIdentifierStr,
		agreementEffectiveDate,
		agreementUrl,
		businessCenter,
		execVenueCode,
		execVenueScheme,
		settlementAgentLEIStr,
		settlementAgentNameStr,
		ccpLeiStr,
		ccpNameStr,
		csdParticipantLeiStr,
		csdParticipantNameStr,
		brokerLeiStr,
		brokerNameStr,
		tripartyLeiStr,
		tripartyNameStr,
		clearingMemberLeiStr,
		clearingMemberNameStr,
		floatingRateReferenceStr,
		floatingRateReferencePeriodStr,
		floatingRateReferenceMultiplierStr,
		floatingRateResetFreqStr,
		floatingRateResetMultiplierStr,
		floatingRatePaymentFreqStr,
		floatingRatePaymentMultiplierStr
		);
		

		return businessEvent;
	
	}
	
	public String rollTrade() throws IOException {
			String purchasePriceStr = this.purchasePriceField.getText().replaceAll(",","").trim();
			String cashQuantityStr = purchasePriceStr;
			String repurchasePriceStr = this.repurchasePriceField.getText().replaceAll(",","").trim();
			String cashCurrencyStr = this.cashCurrencyField.getText();
			String collateralISINStr = this.collateralISINField.getText();
			String collateralQuantityStr = this.collateralQuantityField.getText().replaceAll(",","").trim();
			String collateralCleanPriceStr = this.collateralCleanPriceField.getText();
			String collateralDirtyPriceStr = this.collateralDirtyPriceField.getText();
			String collateralAdjustedValueStr = this.collateralAdjustedValueField.getText().replaceAll(",","").trim();
			String collateralCurrencyStr = this.collateralCurrencyField.getText();
			String repoRateStr = this.repoRateField.getText();
			String haircutStr = this.haircutField.getText();
			//set the effectiveRollDate
			String pdt = this.purchaseDateField.getText();
			String purchaseDateStr = pdt.replaceAll("\\s", "") + "T00:00:00.000+00:00";
			String rdt = this.repurchaseDateField.getText();
			String repurchaseDateStr = rdt.replaceAll("\\s", "") + "T00:00:00.000+00:00";


			RepoLifeCycle repoRoll = new RepoLifeCycle();

			String eventResult = repoRoll.RepoRoll(
				this.afterTradeStateStr,
					purchasePriceStr,
					cashQuantityStr,
					repurchasePriceStr,
					cashCurrencyStr,
					collateralISINStr,
					collateralQuantityStr,
					collateralCleanPriceStr,
					collateralDirtyPriceStr,
					collateralAdjustedValueStr,
					collateralCurrencyStr,
					repoRateStr,
					haircutStr,
					purchaseDateStr,
					repurchaseDateStr);


			
			return eventResult;
	}


	public String earlyTerminateTrade()throws JsonProcessingException{


		if (this.afterTradeStateStr == null){
			JOptionPane.showMessageDialog(this, "No active trade, please book trade before attempting to terminate.", "Alert", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
		String rdt = this.repurchaseDateField.getText();
		String repurchaseDateStr = rdt.replaceAll("\\s", "") + "T00:00:00.000+00:00";
		String repurchasePriceStr = this.repurchasePriceField.getText().replaceAll(",","").trim();

		LocalDateTime localDateTime = LocalDateTime.now();
		ZonedDateTime eventDate = ZonedDateTime.of(localDateTime, ZoneId.of(this.defaultLocalTimeZone));

		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String eventDateStr = eventDate.format(formatter);
		String effectiveDateStr = eventDate.format(formatter);

		String rePurchasePriceStr = this.repurchasePriceField.getText().replaceAll(",","").trim();


		RepoLifeCycle rlfc = new RepoLifeCycle();

		String eventResult = rlfc.RepoEarlyTermination(
				this.afterTradeStateStr,
				rePurchasePriceStr,
				repurchaseDateStr,
				eventDateStr,
				effectiveDateStr
		);

		return eventResult;

	}
	
	public String onDemandRateChange() throws JsonProcessingException{

			String agreedRate = this.repoRateField.getText();
			String repurchasePriceStr = this.repurchasePriceField.getText().replaceAll(",","").trim();

			LocalDateTime localDateTime = LocalDateTime.now();
			ZonedDateTime eventDate = ZonedDateTime.of(localDateTime, ZoneId.of(this.defaultLocalTimeZone));
			DateTimeFormatter simpleDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String eventDateStr = eventDate.format(simpleDateFormatter);
			String effectiveDateStr = eventDate.format(simpleDateFormatter);


			RepoLifeCycle rlc = new RepoLifeCycle();

			String eventResult = rlc.OnDemandRateChange(
				this.afterTradeStateStr,
				agreedRate,
				repurchasePriceStr,
				effectiveDateStr,
				eventDateStr);

				return eventResult;

	}

	public String onDemandPayment()throws JsonProcessingException{

		String repurchasePriceStr = this.repurchasePriceField.getText().replaceAll(",","").trim();
		String interestAmountStr = "0.00";
		String cashCurrencyStr = this.cashCurrencyField.getText();

		LocalDateTime localDateTime = LocalDateTime.now();
		ZonedDateTime eventDate = ZonedDateTime.of(localDateTime, ZoneId.of(this.defaultLocalTimeZone));
		DateTimeFormatter simpleDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String eventDateStr = eventDate.format(simpleDateFormatter);
		String effectiveDateStr = eventDate.format(simpleDateFormatter);

		RepoLifeCycle rlc = new RepoLifeCycle();

		String eventResult = rlc.OnDemandInterestPayment(
				this.afterTradeStateStr,
				interestAmountStr,
				cashCurrencyStr,
				repurchasePriceStr,
				effectiveDateStr,
				eventDateStr);


		return eventResult;

	}
	
	public void reportTrade()throws JsonProcessingException{}
	
	public void shapeTrade()throws JsonProcessingException{}
	
	public void pairOffTrade()throws JsonProcessingException{}

	public void initRepoWorkflow(){}

	public void rateTypeFieldEvent(String selectedRateType){

		if (selectedRateType.equals("FLOAT")){

			this.floatingRateReferenceField.setText("SONIA");
			this.floatingRateReferenceField.setEnabled(true);
			this.floatingRateReferenceField.setBackground(Color.WHITE);

			this.floatingRateReferencePeriodField.setSelectedItem("DAYS");
			this.floatingRateReferencePeriodField.setEnabled(true);
			this.floatingRateReferencePeriodField.setBackground(Color.WHITE);

			this.floatingRateReferenceMultiplierField.setText("1");
			this.floatingRateReferenceMultiplierField.setEnabled(true);
			this.floatingRateReferenceMultiplierField.setBackground(Color.WHITE);

			this.floatingRatePaymentFreqField.setSelectedItem("MONTH");
			this.floatingRatePaymentFreqField.setEnabled(true);
			this.floatingRatePaymentFreqField.setBackground(Color.WHITE);

			this.floatingRatePaymentMultiplierField.setText("1");
			this.floatingRatePaymentMultiplierField.setEnabled(true);
			this.floatingRatePaymentMultiplierField.setBackground(Color.WHITE);

			this.floatingRateResetFreqField.setSelectedItem("DAYS");
			this.floatingRateResetFreqField.setEnabled(true);
			this.floatingRateResetFreqField.setBackground(Color.WHITE);

			this.floatingRateResetMultiplierField.setText("1");
			this.floatingRateResetMultiplierField.setEnabled(true);
			this.floatingRateResetMultiplierField.setBackground(Color.WHITE);

			this.floatingRateField.setText("4.43");
			this.floatingRateField.setEnabled(true);
			this.floatingRateField.setBackground(Color.WHITE);

			this.floatingRateSpreadField.setText("2");
			this.floatingRateSpreadField.setEnabled(true);
			this.floatingRateSpreadField.setBackground(Color.WHITE);

			Double refrate = Double.parseDouble(this.floatingRateField.getText());
			Double spread = Double.parseDouble(this.floatingRateSpreadField.getText());

			Double reporate = (double) Math.round((refrate + spread/100.0)*100)/100;

			this.repoRateField.setText(reporate.toString());


		}else{
			this.floatingRateReferenceField.setText("");
			this.floatingRateReferenceField.setEnabled(false);
			this.floatingRateField.setBackground(Color.LIGHT_GRAY);

			this.floatingRateReferencePeriodField.setSelectedItem("");
			this.floatingRateReferencePeriodField.setEnabled(false);
			this.floatingRateReferencePeriodField.setBackground(Color.LIGHT_GRAY);

			this.floatingRateReferenceMultiplierField.setText("");
			this.floatingRateReferenceMultiplierField.setEnabled(false);
			this.floatingRateReferenceMultiplierField.setBackground(Color.LIGHT_GRAY);

			this.floatingRatePaymentFreqField.setSelectedItem("");
			this.floatingRatePaymentFreqField.setEnabled(false);
			this.floatingRatePaymentFreqField.setBackground(Color.LIGHT_GRAY);

			this.floatingRatePaymentMultiplierField.setText("");
			this.floatingRatePaymentMultiplierField.setEnabled(false);
			this.floatingRatePaymentMultiplierField.setBackground(Color.LIGHT_GRAY);

			this.floatingRateResetFreqField.setSelectedItem("");
			this.floatingRateResetFreqField.setEnabled(false);
			this.floatingRateResetFreqField.setBackground(Color.LIGHT_GRAY);

			this.floatingRateResetMultiplierField.setText("");
			this.floatingRateResetMultiplierField.setEnabled(false);
			this.floatingRateResetMultiplierField.setBackground(Color.LIGHT_GRAY);

			this.floatingRateField.setText("");
			this.floatingRateField.setEnabled(false);
			this.floatingRateField.setBackground(Color.WHITE);

			this.floatingRateSpreadField.setText("");
			this.floatingRateSpreadField.setEnabled(false);
			this.floatingRateSpreadField.setBackground(Color.WHITE);

			this.repoRateField.setText("4.65");

		}
	}
}