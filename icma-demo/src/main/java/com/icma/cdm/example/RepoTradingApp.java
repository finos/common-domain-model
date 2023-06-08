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
import java.util.List;
import cdm.event.common.functions.Create_RollPrimitiveInstruction;
import org.isda.cdm.CdmRuntimeModule;

import static com.rosetta.model.lib.records.Date.of;

public class RepoTradingApp extends JFrame implements ActionListener{
	
	private JFrame frame;
	private JPanel panel;
	
	private JButton newTradeBtn;
	private JButton bookTradeBtn;
	private JButton rollTradeBtn;
	private JButton terminateTradeBtn;
	private JButton rerateTradeBtn;
	private JButton reportTradeBtn;
	
	private JTextField tradeDateField;
	private JTextField purchaseDateField;
	private JTextField repurchaseDateField;
	private JTextField tradeUTIField;
	private JTextField buyerLEIField;
	private JTextField sellerLEIField;
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
	private JTextField termTypeField;
	private JComboBox terminationOptionField;
	private JTextField noticePeriodField;
	private JTextField deliveryMethodField;
	private JComboBox  substitutionAllowedField;
	private JComboBox  rateTypeField;
	private JTextField dayCountFractionField;
	private JTextField termDaysField;
	private JTextField purchasePriceField;
	private JTextField repurchasePriceField;
	
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
	public DateTimeFormatter formatter;

	public DateTimeFormatter eventFileformatter;
	private JTextArea outputArea;

	private JFrame eout;

	JScrollPane scrollPane = null;



	
	public static void main( String[] args ) throws JsonProcessingException  {


		RepoTradingApp repoTradingApp = new RepoTradingApp();

		repoTradingApp.BuildTicketPanel();
	        
		
    }

	public void BuildTicketPanel () throws JsonProcessingException {

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
		frame.setSize( 400, 800 );

		JPanel logoPanel = new JPanel();
		try{
		Image image = ImageIO.read(getClass().getResource("/images/icma-logo.jfif"));
		frame.setIconImage(image);
		eout.setIconImage(image);
		} catch (IOException ioe) {
		ioe.printStackTrace();
		}




		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		frame.add(panel);
		
		
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
		JLabel buyerLEILabel = new JLabel("Buyer LEI:",JLabel.LEFT);
		buyerLEILabel.setPreferredSize(new Dimension(150, 15));
		buyerLEIField = new JTextField(15);
		buyerLEIField.setText("529900DTJ5A7S5UCBB52");
		buyerLEIPanel.add(buyerLEILabel);
		buyerLEIPanel.add(buyerLEIField);
		panel.add(buyerLEIPanel);
		
		//Seller
		JPanel sellerLEIPanel = new JPanel(new GridBagLayout());
		JLabel sellerLEILabel = new JLabel("Seller LEI:",JLabel.LEFT);
		sellerLEILabel.setPreferredSize(new Dimension(150, 15));
		sellerLEIField = new JTextField(15);
		sellerLEIField.setText("5493000SCC07UI6DB380");
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
		panel.add(rateTypePanel);

		//Repo Rate
		JPanel repoRatePanel = new JPanel(new GridBagLayout());
		JLabel repoRateLabel = new JLabel("Repo Rate:",JLabel.LEFT);
		repoRateLabel.setPreferredSize(new Dimension(150, 15));
		repoRateField = new JTextField(15);
		repoRateField.setText(".65");
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

		JPanel termTypePanel = new JPanel(new GridBagLayout());
		JLabel termTypeLabel = new JLabel("Term Type:",JLabel.LEFT);
		termTypeLabel.setPreferredSize(new Dimension(150, 15));
		termTypeField = new JTextField(15);
		termTypeField.setText("FIXED");
		termTypePanel.add(termTypeLabel);
		termTypePanel.add(termTypeField);
		panel.add(termTypePanel);


		//Termination Option
		String[] terminationChoices = { "N", "Y"};
		JPanel terminationOptionPanel = new JPanel(new GridBagLayout());
		JLabel terminationOptionLabel = new JLabel("Early Termination:",JLabel.LEFT);
		terminationOptionLabel.setPreferredSize(new Dimension(150, 15));
		
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




		

		JPanel actionPanel = new JPanel(new GridLayout(3,6));
		
		newTradeBtn = new JButton("New Trade");
		newTradeBtn.addActionListener(this);
		actionPanel.add(newTradeBtn);
		
		bookTradeBtn = new JButton("Book Trade");
		bookTradeBtn.addActionListener(this);
		actionPanel.add(bookTradeBtn);
		
		rollTradeBtn = new JButton("Roll");
		rollTradeBtn.addActionListener(this);
		actionPanel.add(rollTradeBtn);
		
		terminateTradeBtn = new JButton("Terminate");
		terminateTradeBtn.addActionListener(this);
		actionPanel.add(terminateTradeBtn);
		
		rerateTradeBtn = new JButton("Re-Rate");
		rerateTradeBtn.addActionListener(this);
		actionPanel.add(rerateTradeBtn);
		
		reportTradeBtn = new JButton("SFTR");
		reportTradeBtn.addActionListener(this);
		actionPanel.add(reportTradeBtn);
		
		panel.add(actionPanel, BorderLayout.SOUTH);

		frame.setVisible( true );


	}
	
	public void loadNewTrade() {
	
		
		
		//Trade Date

		LocalDateTime localDateTime = LocalDateTime.now();
		ZonedDateTime startDateTime = ZonedDateTime.of(localDateTime, ZoneId.of(this.defaultLocalTimeZone));

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDateTimeString = startDateTime.format(formatter);
		tradeDateField.setText(formattedDateTimeString);



		//Purchase Date
		purchaseDateField.setText(formattedDateTimeString);

		
		
		//Repurchase Date
		ZonedDateTime endDateTime = startDateTime.plusDays(1);
		formattedDateTimeString = endDateTime.format(formatter);
		repurchaseDateField.setText(formattedDateTimeString);

		
		//UTI
		tradeUTIField.setText("ICMA202303231000178");

		
		//Buyer
		buyerLEIField.setText("529900DTJ5A7S5UCBB52");

		
		//Seller
		sellerLEIField.setText("5493000SCC07UI6DB380");

		
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
		repoRateField.setText(".65");


		//Cash Currency
		cashCurrencyField.setText("GBP");



		//Haircut
		haircutField.setText("2");


		//Term Type
		termTypeField.setText("FIXED");


		
		//Day Count
		dayCountFractionField.setText("ACT/360");

		//Term Days
		long daysBetween = Duration.between(startDateTime,endDateTime).toDays();
		DecimalFormat intFormat = new DecimalFormat("###");
		String termsDaysStr = intFormat.format(daysBetween);
		termDaysField.setText(termsDaysStr);

		//Purchase Price
		purchasePriceField.setText("9879046.80");


		//Repurchase Price
		repurchasePriceField.setText("9879155.06");

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

				String repoBusinesseventJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(businessEvent);
				System.out.println(repoBusinesseventJson);
				outputArea.setText(repoBusinesseventJson);

				IcmaRepoUtil ru = new IcmaRepoUtil();

				DateTimeFormatter eventDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
				LocalDateTime localDateTime = LocalDateTime.now();
				String eventDateTime = localDateTime.format(eventDateFormat);
				ru.writeEventToFile("execution", eventDateTime, businessEvent);


			} catch (JsonProcessingException e){
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		else if(ae.getSource() == this.rerateTradeBtn){
			JOptionPane.showMessageDialog(this, "Rerate Trade", "Alert", JOptionPane.INFORMATION_MESSAGE);
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

				//String repoBusinesseventJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(businessEvent);

				System.out.println(businessEvent);
				outputArea.setText(businessEvent);

				IcmaRepoUtil ru = new IcmaRepoUtil();

				DateTimeFormatter eventDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
				LocalDateTime localDateTime = LocalDateTime.now();
				String eventDateTime = localDateTime.format(eventDateFormat);
				ru.writeEventToFile("execution", eventDateTime, businessEvent);


			} catch (JsonProcessingException e){
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		else if(ae.getSource() == this.reportTradeBtn){
			JOptionPane.showMessageDialog(this, "Report Trade", "Alert", JOptionPane.INFORMATION_MESSAGE);
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
				//String repoBusinesseventJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(businessEvent);
				System.out.println(businessEvent);
				outputArea.setText(businessEvent);

				IcmaRepoUtil ru = new IcmaRepoUtil();

				DateTimeFormatter eventDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
				LocalDateTime localDateTime = LocalDateTime.now();
				String eventDateTime = localDateTime.format(eventDateFormat);
				ru.writeEventToFile("execution", eventDateTime, businessEvent);


			} catch (JsonProcessingException e){
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
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
		String buyerLEIStr = this.buyerLEIField.getText();
		String sellerLEIStr = this.sellerLEIField.getText();
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
		String termTypeStr = this.termTypeField.getText();
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
		String agreementNameStr = "GMRA";
		String agreementGoverningLawStr = "England";
		String agreementVintageStr = "2011";
		String agreementPublisherStr = "ICMA";
		String agreementDateStr = "2011-04-20";
		String agreementIdentifierStr =  "GMRA-2011_2011-04-20";
		String agreementEffectiveDate = "2011-04-20";
		String agreementUrl = "https://www.icmagroup.org/assets/documents/Legal/GMRA-2011_2011.04.20_formular_copy-incl-annexes-and-buy-sell-back-annex.pdf";
		

		
		RepoLifeCycle repoExecution = new RepoLifeCycle();
		

		
		String businessEvent = repoExecution.RepoExecution(
		tradeDateStr,					// tradeDateStr = .getText();
		purchaseDateStr,				// purchaseDateStr
		repurchaseDateStr,				// repurchaseDateStr
		tradeUTIStr,  					// tradeUTIStr
		buyerLEIStr, 					// buyerLEIStr
		sellerLEIStr, 					// sellerLEIStr
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
		agreementUrl
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
		String repurchaseDateStr = rdt.replaceAll("\\s", "") ;
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
	
	public void rateChange()throws JsonProcessingException{}
	
	public void reportTrade()throws JsonProcessingException{}
	
	public void shapeTrade()throws JsonProcessingException{}
	
	public void pairOffTrade()throws JsonProcessingException{}

	public void initRepoWorkflow(){}


}