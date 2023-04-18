.. include:: links.rst

Repurchase Agreement Representation
==================================

Background
------------
The ICMA CDM initiative is extended to support the digitalization of the 
repo market through the adoption of a standardized domain model and associated 
lifecycle events based on the ERCC Best Practices Guide and industry standards.  
The ICMA promotes adoption of CDM by organizing working groups and workshops on 
proof-of-concept projects and integration.

The repo elements of the model were designed with participation and contribution 
of the ICMA CDM for Repo and Bonds Steering Committee.


Introduction
------------
The purpose of the CDM for repo and bonds is to provide standardized digital 
representation of repo products and lifecycle events that will reduce reconciliation 
failures caused by data and processing mismatches and provides firms with a software 
toolkit that can be incorporated into existing systems or established as the baseline 
for a new platform.

Repurchase Agreements are represented in the CDM as contractualProducts
with an InterestRatePayout and an AssetPayout. Purchase date and repurchase
date are defined in InterestRatePayout. Margin and haircuts are 
defined in the collateralProvisions. All other trade related attributes 
are defined in Trade type.

The types of repo products can be defined is only limited by the structure
InterestRatePayout and AssetPayout but a a minimum supports fixed term repo,
open repo, fixed rate and floating rate. 

Repo lifecycle events are supported through a set of functions that accept
a small set of inputs to auto-generate primitive instructions needed to execute
business events. Repo lifecycle events include, rolling, re-rating, interium payments, 
pair-off and shaping.


Benefits:
------------

The benefits of using the CDM for repo transactions is that it provides a 
standard digital representation of the data required to define a repo product and 
a standardized set of lifecyle events.

Examples of where the CDM can be used in the repo market:

-  Post-trade matching using a single digital object.

-  Lifecycle event processing across counterparties and settlement services.

-  Internal and external reporting. 

-  Capturing event history.

-  Market standard taxonomy and mapping interface to other platforms.


ICMA Contacts:
------------
Gabriel Callsen, Director	 
gabriel.callsen@icmagroup.org
+44 (0)20 7213 0334

Tom Healey, Consultant
tom.healey@icmagroup.org



Scope:
------------

The scope of the CDM Repo initiative was intended to define:

-  Consistent definition of buyer-seller entities based on LEI data

-  Collateral, margin anf haircut attributes.

-  Flexible interest rate payout model to support complex structures.

-  Standardized product taxonomy.

-  Predefine lifecycle event processing and event history.


Repo Product Model
------------------------------

Building upon the ISDA CDM, the Repo CDM added new data types, attributes and events 
needed to create fixed term, open and floating rate repos, and execute various lifecycle 
events.

A repo product is formed from a ContractualProduct as an extension of ProductBase. 
type Product:
    [metadata key]
    contractualProduct ContractualProduct (0..1)

At the core of a ContractualProduct is the product’s EconomicTerms that define the 
attributes that are needed to derive the product’s economic value and determine its cashflows.

.. code-block:: Haskell

type EconomicTerms:

    effectiveDate AdjustableOrRelativeDate (0..1) 
        [docReference ICMA GMRA namingConvention "Purchase Date"
            provision "As defined in GMRA paragraph 2(mm) The date on which 
			Purchased Securities are sold or are to be sold by Seller to Buyer."]
        [docReference ICMA ERCCBestPractice namingConvention "Purchase Date"
            provision "ERCC Guide: Annex II  Glossary of repo terminology. 
			The term for the value date of a repo."]
    terminationDate AdjustableOrRelativeDate (0..1) 
        [docReference ICMA GMRA namingConvention "Repurchase Date"
            provision "As defined in GMRA paragraph 2(qq) The date on which 
			Buyer is to sell Equivalent Securities to Seller."]
        [docReference ICMA ERCCBestPractice namingConvention "Repurchase Date"
            provision "ERCC Guide: Annex II  Glossary of repo terminology. 
			The term for the maturity date of a repo."]
    dateAdjustments BusinessDayAdjustments (0..1)
    terminationProvision TerminationProvision (0..1) 
    extraordinaryEvents ExtraordinaryEvents (0..1)
    calculationAgent CalculationAgent (0..1) 
    nonStandardisedTerms boolean (0..1) 
    collateral Collateral (0..1)



The primary structure of a repo is composed of an InterestRatePayout and an AssetPayout. 
The AssetPayout represents the collateral on the repo trade and the InterestRatePayout represents 
the cash loan side of the trade. The InterestRatePayout is a shared payout model with 
derivatives like interest rate swaps, in the economicTerms->payout object:


.. code-block:: Haskell

type Payout: 
    [metadata key]
    interestRatePayout InterestRatePayout (0..*)
    assetPayout AssetPayout (0..*)


Defining the rate as an interest rate is done by setting PriceTypeEnum->InterestRate.

A floating rate repo is defined using the FloatingRateSpecification type:

.. code-block:: Haskell

type FloatingRateSpecification extends FloatingRate:
    initialRate Price (0..1) 
    finalRateRounding Rounding (0..1)
    averagingMethod AveragingWeightingMethodEnum (0..1)
    negativeInterestRateTreatment NegativeInterestRateTreatmentEnum (0..1)



Payer and Receiver
^^^^^^^^^^^^^^^^^^

The InterestRatePayout object must also define the payer and receiver party that 
are linked to the trade object that defines the counterparty and partyrole. 
In a repo transaction, the seller (collateral giver -  borrower) will be the payer 
and the buyer(collateral taker – lender) will be receiver. The payer and receiver 
are extensions from the PayoutBase:

.. code-block:: Haskell

type PayoutBase: 
	payerReceiver PayerReceiver (1..1)
    priceQuantity ResolvablePriceQuantity (0..1)
    principalPayment PrincipalPayments (0..1)
    settlementTerms SettlementTerms (0..1)




Collateral
^^^^^^^^^^

Collateral on a repo transaction is defined in the AssetPayout object. Security 
identification is created in the securityInformation attribute which itself is a 
ProductType allow for the possibility of creating products defined in terms of 
other products using the same Product model.
The assetPayoutLeg defines the delivery dates, as relative references to purchase 
date and repurchase date, and delivery methods (DVP). 

.. code-block:: Haskell

type AssetLeg:
    settlementDate AdjustableOrRelativeDate (1..1)
    deliveryMethod DeliveryMethodEnum (1..1)



Purchase Date and Repurchase Date

The start date of a repo and the termination date are defined in the GMRA as the 
purchase date and repurchase date. The repo dates are set in the economicTerms 
object using the effectiveDate and terminationDate:

.. code-block:: Haskell

effectiveDate AdjustableOrRelativeDate (0..1)
        [docReference ICMA GMRA namingConvention "Purchase Date"
            provision "As defined in GMRA paragraph 2(mm) The date on which 
			Purchased Securities are sold or are to be sold by Seller to Buyer."]
        [docReference ICMA ERCCBestPractice namingConvention "Purchase Date"
            provision "ERCC Guide: Annex II  Glossary of repo terminology. 
			The term for the value date of a repo."]
    terminationDate AdjustableOrRelativeDate (0..1)
        [docReference ICMA GMRA namingConvention "Repurchase Date"
            provision "As defined in GMRA paragraph 2(qq) The date on which 
			Buyer is to sell Equivalent Securities to Seller."]
        [docReference ICMA ERCCBestPractice namingConvention "Repurchase Date"
            provision "ERCC Guide: Annex II  Glossary of repo terminology. 
			The term for the maturity date of a repo."]


Haircut and Margin
Most repo trades include a haircut or margin adjustment to the collateral value 
that affords the collateral holder a level of risk protection in the case of 
default and the value of the collateral is lower than the loan value. 
Haircuts and margin adjustments are set on the collateralProvision under 
economicTerms->collateral-> collateralProvisions. 

.. code-block:: Haskell

type CollateralProvisions:

    collateralType CollateralTypeEnum (1..1)
        [docReference ICMA GMRA namingConvention "marginType"]
    eligibleCollateral EligibleCollateralSchedule (0..*) 
    substitutionProvisions SubstitutionProvisions (0..1)

In phase 2 of the ICMA CDM for Repo and Bonds initiative the definition of 
collateral was modified. Collateral is now part of economicTerms and haircuts 
are defined as an attribute of CollateralTreatment->valuationTreatment->hairCutPercentage:

.. code-block:: Haskell
type CollateralValuationTreatment:

    haircutPercentage number (0..1)
        [docReference ICMA GMRA namingConvention "Haircut"
            provision "As defined in GMRA paragraph 2(xx)(B). The haircut for the 
			relevant Securities, if any, as agreed by the parties from time to time, 
			being a discount from the Market Value of the Securities."]
        [docReference ICMA ERCCBestPractice namingConvention "Haircut"
            provision "ERCC Guide 3.1: Initial margins and Haircuts are alternative 
			ways to risk-adjust the value of collateral sold in a repurchase 
			transaction in order to try to anticipate the loss of value that may be 
			experienced if the collateral has to be liquidated following an event 
			of default by the counterparty. Both amounts are therefore used to fix the 
			expected liquidation value of collateral. Annex II  Glossary of repo 
			terminology Haircut: An agreed percentage discount applied to the Market Value 
			of collateral to fix the Purchase Price on the Purchase Date of a repo. 
			A haircut is expressed as the percentage difference between the initial 
			Market Value and the Purchase Price. "]
			
    marginPercentage number (0..1) 
        [docReference ICMA GMRA namingConvention "Margin Ratio"
            provision "As defined in GMRA paragraph 2(bb). Margin Ratio, with respect 
			to a Transaction, the Market Value of the Purchased Securities at the time 
			when the Transaction was entered into divided by the Purchase Price (and so 
			that, where a Transaction relates to Securities of different descriptions and 
			the Purchase Price is apportioned by the parties among Purchased Securities of 
			each such description, a separate Margin Ratio shall apply in respect of 
			Securities of each such description), or such other proportion as the parties 
			may agree with respect to that Transaction;"]
			
        [docReference ICMA ERCCBestPractice namingConvention "Margin Ratio"
            provision "ERCC Guide 3.1: Initial margins and Haircuts are alternative 
			ways to risk-adjust the value of collateral sold in a repurchase transaction 
			in order to try to anticipate the loss of value that may be experienced if 
			the collateral has to be liquidated following an event of default by the 
			counterparty. Both amounts are therefore used to fix the expected liquidation 
			value of collateral. Annex II  Glossary of repo terminology: Initial margin: 
			An agreed premium applied to the Purchase Price of a repo to determine the 
			required Market Value of the collateral to be delivered on the Purchase Date. 
			It is also applied each day during the term of a repo, as part of the process 
			of Margin Maintenance, to the Repurchase Price on that day to calculate the 
			Market Value of collateral required subsequently in order to maintain adequate 
			collateralisation. Under the GMRA, if there is a material difference between 
			(1) the Repurchase Price of a repo plus any initial margin and (2) the current 
			Market Value of collateral, that repo has a Transaction Exposure. This will go 
			into the calculation of Net Exposure, which determines if either party has the 
			right to call for Margin Maintenance. An initial margin can be expressed either 
			as (1) the Market Value as a percentage of the Purchase Price or (2) a ratio of 
			the two amounts. In the GMRA, an initial margin is called a Margin Ratio and is 
			defined as a ratio but the market tends to quote a percentage. A percentage 
			initial margin of 100% or ratio of one means there is no initial margin. See Guide 3.2"]
			
    fxHaircutPercentage number (0..1)
    additionalHaircutPercentage number (0..1)


Creating a Repo Product
-------------------------

Example: Fixed Term, Fixed Rate bi-lateral repo

A fixed term, fixed rate bilateral repo transaction is an agreement 
between two counterparties to exchange cash for collateral at an agreed 
interest rate for an agreed fixed term.  On the effective date the seller 
delivers collateral to the buyer and receives cash. On the termination date 
the buyer returns collateral to the seller and receives the cash principal 
plus interest.

As previously described, to build a repo product the following components are needed:

•	Purchase Date
•	Repurchase Date
•	Term Type
•	Repo Rate
•	Collateral
•	Haircut

When full constructed in json format a fixed term, fixed rate repo can be found here:

Fixed-Term,Fixed-Rate Repo Product :download:'json <code-snippets/fixed-term-fixed-rate-repo-product.json>'


Create a Repo Trade
-------------------

Creating a repo transaction is a two-step process, 
1) Create an ExecutionInstruction and 2) execute a BusinessEvent 
using the execution Instruction. A helper function, 
Create_Execution will automatically create the instruction 
and execute the business event. For other lifecycle events it’s 
necessary to create a primitive instruction and then execute the business event.
Creating a repo trade can be performed using a service call to 
Create_Execution() or using Java to set and execute the model parameters.
A completed repo execution will describe the state of the trade 
object in the tradeState object:

.. code-block:: Haskell

type TradeState:
    [metadata key]
    [rootType]
    trade Trade (1..1)
    state State (0..1)
    resetHistory Reset (0..*)
    transferHistory TransferState (0..*)
    observationHistory ObservationEvent (0..*)

A CDM state is defined by a state object that describes a closedState and positionState:

.. code-block:: Haskell
type State:
    closedState ClosedState (0..1)
    positionState PositionStatusEnum (0..1)



Calling Create_ExecutionInstruction
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The Create_ExecutionInstruction requires passing a fully formed product object and other parameters as defined below:

{ 
    "product": < insert Product >, 
    "priceQuantity": [ < insert PriceQuantity > ], 
    "counterparty": < insert Counterparty >, 
    "ancillaryParty": [ < insert AncillaryParty > ], 
    "parties": [ < insert Party > ], 
    "partyRoles": [ < insert PartyRole > ], 
    "executionDetails": < insert optional ExecutionDetails >, 
    "tradeDate": < insert date >, 
    "tradeIdentifier": [ < insert TradeIdentifier > ] 
}


priceQuantity:

The priceQuantity object is used to define security prices, interest rates and trade amounts.

The repo rate is defined as a price with a priceType of “INTEREST_RATE”. 

"price": [
	{
	"meta": {},
		"value": {
			"unit": {
				"currency": {
					"value": "GBP"
				}
			},
			"value": 0.004,
			"perUnitOf": {
			"currency": {
				"value": "GBP"
			}
		},
		"priceExpression": {
			"priceType": "INTEREST_RATE"
		}
	}}]
	
	
The priceQuantity object is also used to define the collateral price and value:

"quantity": [
	{
		"meta": {},
		"value": {
			"unit": {
				"currency": {
					"value": "GBP"
				}
			},
			"value": 9974250
	}}]


Collateral amount is defined as a nominal par amount:

"quantity": [
	{
		"meta": {},
		"value": {
			"unit": {
				"currency": {
					"value": "GBP"
				}},
			"value": 10000000
		}}]

Collateral price can be defined as either Clean or Dirty price:

"price": [
	{
		"meta": {},
		"value": {
			"unit": {
				"currency": {
					"value": "GBP"
				}
			},
			"value": 1.0075,
			"perUnitOf": {
			"currency": {
				"value": "GBP"
			}
		},
		"priceExpression": {
			"cleanOrDirty": "DIRTY",
				"priceExpression": "PERCENTAGE_OF_NOTIONAL",
			"priceType": "ASSET_PRICE"
		}}}]



Counterparties are defined in the counterparty object and need to define the 
role attribute as PARTY_1 or PARTY_2 as it relates to the counterparty being 
the buyer or seller.


{"partyReference": {
	"value": {
		"meta": {
			"externalKey": "UkBank",
			"globalKey": "1ef4886d"
		},
		"name": {
		"value": "UK Bank plc"
		}
	}
},
"role": "PARTY_2"
}]
{"partyReference": {
	"value": {
		"meta": {
			"externalKey": "UkBank",
		"globalKey": "1ef4886d"
		},
		"name": {
		"value": "UK Bank plc"
		}}},
"role": "PARTY_2"
}]


Even if there are no ancillary parties to the trade the ancillaryParty 
object must be defined and can be done using an empty object:
	"ancillaryParty": [{}]



partyRoles
^^^^^^^^^^

PartyRoles are necessary to define the buyer (cash lender) and 
seller (collateral giver). A reference global key is used to link the 
party role to the party defined in the party object:

"partyRoles": [{
"partyReference": {
		"externalReference": "GlobalBank",
			"globalReference": "296093b7"
		},
	"role": "SELLER"
	},
{"partyReference": {
		"externalReference": "UkBank",
		"globalReference": "1ef4886d"
	},
	"role": "BUYER"
}]


Trade Date
^^^^^^^^^^

Trade Date is a simple date string:
	"tradeDate": "2021-03-18"



Executing Business Event
^^^^^^^^^^^^^^^^^^^^^^^^

Executing events in the CDM is performed by calling Create_BusinessEvent using one or more valid Instructions:


.. code-block:: Haskell

func Create_BusinessEvent: 
    [creation BusinessEvent]
    inputs:
        instruction Instruction (1..*)
        intent EventIntentEnum (0..1)
        eventDate date (1..1)
        effectiveDate date (1..1)
    output:
        businessEvent BusinessEvent (1..1)



Lifecycle Events
-------------------

Lifecycle events are actions that modify or close open contracts. These actions may be taken by a counterparty or automatically generated due to events such as rate changes, contract changes, extensions, or terminations. Repo lifecycle events are important to the daily functioning of the market but also a source of errors and failures caused by different methodologies implemented by systems for the same event or mismatches in data, workflow and calculations. The CDM provides a software implementation based on industry practice and ERCC Best Practices industry intended to reduce mismatches in workflow and data.

In the CDM a lifecycle event results in a state transition. State changes are trade specific and are automatically linked in the CDM.

All repo events follow the CDM Event Model design and process. Initiating a repo event requires the creation of an event primitive instruction followed by a call to Create_BusinessEvent. 
Using an instantiated repo trade that was created with a repo execution will result in a TradeState object and the state is positionState=Executed.


.. code-block:: Haskell

type TradeState:
    [metadata key]
    [rootType]
    trade Trade (1..1) 
    state State (0..1)
    transferHistory TransferState (0..*) 
    observationHistory ObservationEvent (0..*)


A repo product represents a contract between the counterparties and is 
instantiated through a set of parameters defining the economic and other 
characteristics that can only be modified through a change in terms of 
the trade. As the CDM event model state, “The product underlying the 
transaction remains immutable. Automated events, for instance resets 
or cashflow payments, do not alter the product definition. Lifecycle 
events negotiated between the parties that give rise to a change in the 
terms and economics will generate a new instance of the product or trade 
as part of that specific event.”


Repo events currently supported in the CDM include:

•	Execution
•	Roll
•	Re-Rate
•	Early Termination
•	Pair-off
•	Shaping
•	On Demand Interest Payment


Closing and Opening Trades
^^^^^^^^^^^^^^^^^^^^^^^^^^

Many types of lifecycle events, especially those that modify the 
terms of the contract result in closing the existing contract and 
opening a new one with modifier terms. There is no established model 
and there is considerable inconsistency across the industry 
implemented in systems and backoffice processes as to whether a 
contact is closed or just modified.

In the repo lifecycle functions, those events that close a contract 
and open a new one will include the code:

    set instruction -> split -> breakdown: 
        [ Create_TerminationInstruction( tradeState ) ]

This step creates two instructions with separate copies of the trade 
state so that the first one can be terminated.


Roll
^^^^

The repo roll is intended to simplify the process of moving the end date of a 
fixed term repo usually for one day. Although the original trade is closed and 
a new trade opened for the same term but usually at a different rate, 
counterparties will instruct their settlement agents to not exchange principal 
and collateral thus saving transaction costs and eliminating the potential 
for delivery failures. As described in the ERCC Best Practices, rolling may be 
combined with pair-offs as another approach to reducing exposure and improving efficiency.


.. code-block:: Haskell

func Create_RollPrimitiveInstruction: 
    inputs:
        tradeState TradeState (1..1)
        effectiveRollDate AdjustableOrRelativeDate (1..1)
        terminationDate AdjustableOrRelativeDate (1..1)
        priceQuantity PriceQuantity (1..*) 
    output:
        instruction PrimitiveInstruction (1..1)


The Create_RollPrmitiveInstruction takes four inputs, an existing tradeState, 
the effective roll date, termination date of the new trade and the quantity 
and rate of the new trade.

Sample JSON input to roll a repo trade:

{
  "tradeState": {
		…
  "effectiveRollDate": {
    "adjustableDate": {
      "unadjustedDate": "2021-03-22"
    },
    "meta": {
      "externalKey": "RepurchaseDate"
    }
  },
  "terminationDate": {
    "adjustableDate": {
      "unadjustedDate": "2021-03-25"
    }
  },


When a repo is rolled it is also common to re-negotiate the rate so the 
Create_RollPrimitiveInstruction allows a new rate and even a new amount 
to be input. Other terms of the agreement must remain the same:

  "priceQuantity": [
    	{
	…See PriceQuantity type for full description.
}]


Using the roll primitive instruction, the next step is to call 
Create_BusinessEvent using the Instruction created above:

{
	"instruction": [
		{
			"primitiveInstruction": {
…
	],
	"eventDate": "2021-03-22",
	"effectiveDate": "2021-03-22"
}


On Demand Rate Change
^^^^^^^^^^^^^^^^^^^^^

Open repos and other cases where the terms of the contact allow, the rate on a repo can be re-negotiated.  This is not the same as an automatic rate change when the rate is pegged an index.  An on demand rate change or spread is a change in the contract terms and results in  closing the existing contract and opening a new contract.

To support this event the function Create_OnDemandRateChange is used:


.. code-block:: Haskell


func Create_OnDemandRateChangePrimitiveInstruction:
    inputs:
        tradeState TradeState (1..1) 
        effectiveDate AdjustableOrRelativeDate (1..1)
        agreedRate number (1..1)
    output:
        instruction PrimitiveInstruction (1..1)


Early Termination
^^^^^^^^^^^^^^^^^

Open repos and term repos with an early termination option allow either 
counterparty to invoke an early termination of the repo within a termination period. 
Due to the naming convention adopted by the CDM Architecture Review Committee, 
an early termination that is not a event resulting in an exercise is defined as a cancellation. 
This repo early terminations are implemented using the Create_CancellationPrimitiveInstruction.
 

.. code-block:: Haskell

func Create_CancellationPrimitiveInstruction: 
    inputs:
        tradeState TradeState (1..1)
        newRepurchasePrice number (0..1)
        cancellationDate AdjustableOrRelativeDate (1..1)
    output:
        instruction PrimitiveInstruction (1..1)


Create_CancellationPrimitiveInstruction takes three inputs, an existing trade, 
a new rate that will be in effect from the effective date to the termination 
date and the cancellation date.


Pair-Off
^^^^^^^^

Pair-offs involve matching repo and reverse repo trades with the same counterparty, 
currency, security, custodian and instead of exchange securities and cash for each trade, 
will exchange only a net cash payment. The process of matching parameters that 
will determine a match is a workflow steps that takes place prior to the pair-off 
business event.

To perform a pair-off use the Create_PairoffInstruction function:

.. code-block:: Haskell

func Create_PairOffInstruction:
    inputs:
        tradeState TradeState (2..*)
        pairReference Identifier (1..1)
    output:
        instruction Instruction (1..*)

Create_PairOffInstruction takes two imputs, two or more trades to pair and a 
reference identifier that will link the trades.


{
	"tradeState": [
… (The trades to be paired and entered here.)
]
	"pairReference": {
		"assignedIdentifier": [
			{
				"identifier": {
					"value": "L302TW1XER000101"
	}}]}
}


To complete the pair-off event, call Create_BusinessEvent with the instruction created above.


Shaping
^^^^^^^

Shaping is the process of dividing large amounts into smaller transactions to 
reduce the impact of a settlement failure. There are two types of shaping, 
either as an operational process carried out by the settlement agent or by the 
counterparty by splitting a large trade in advance of settlement.


.. code-block:: Haskell

func Create_ShapingInstruction:
    inputs:
        tradeState TradeState (1..1)
        tradeLots TradeLot (2..*)
        shapeIdentifier Identifier (1..1)
    output:
        instruction PrimitiveInstruction (1..1)


Create_ShapingInstruction takes three inputs, an existing trade, an allocation 
of trade lots that represent the size breakdown for each trade and a shape 
identifier that links the trades together. 


Example of inputs to shape function:
{
	"tradeState": {
	"tradeLots": [
		{
			"priceQuantity": […}, (The quantity of each lot is entered here)
		{
			"priceQuantity": […}, (The quantity of each lot is entered here)

		{
			"priceQuantity": […} (The quantity of each lot is entered here)
],
	"pairReference": {
		"assignedIdentifier": [
			{"identifier": {"value": "L302TW1XER000101"
				}}]}}


On Demand Payment
^^^^^^^^^^^^^^^^^

Scenarios arise during repo lifecycles where the counterparties agree to close out outstanding accrued repo interest to reduce exposure or in some cases where a contract is closed but collateral and principal is not exchanged a net payment may be required.

Invoking an on demand payment involves calling the function, Create_OnDemandInterestPaymentPrimitiveInstruction:

.. code-block:: Haskell

func Create_OnDemandInterestPaymentPrimitiveInstruction:
    inputs:
        tradeState TradeState (1..1)
        interestAmount Money (1..1)
        settlementDate SettlementDate (1..1)
    output:
        instruction PrimitiveInstruction (1..1)


Create_OnDemandInterestPaymentPrimitiveInstruction takes three inputs, 
an existing trade, the amount of interest payment and the settlement date.

Json example of inputs:

{
	"tradeState": {
…
	"interestAmount": {
		"multiplier": {
			"value": 2000,
			"unit": {
				"currency": {
					"value": "GBP"
				}
			}
		}
	},
	"settlementDate": {
		"adjustableOrRelativeDate": {
			"unadjustedDate": 
				{
					"day": 11,
					"month": 7,
					"year": 2022
				}
		}
	}


The completion of a on demand payment is performed by calling Create_BusinessEvent 
using the instruction created by the above function.