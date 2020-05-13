The CDM Model
=============

**There are six dimensions** to the CDM model:

* Product
* Event
* Legal Agreement
* Process
* Reference Data
* Mapping (Synonym)

The following sections define each of these dimensions.  Selected examples of data structures are used as illustrations to help explain each dimension.  The complete data structure, including descriptions and other details can be viewed in the `Textual Browser <https://portal.cdm.rosetta-technology.io/#/text-browser>`_ on the ISDA CDM Portal.

Product Model
-------------

Where applicable, the CDM follows the data structure of the Financial Products Markup Language (FpML), which is widely used in the OTC Derivatives market.  For example, the CDM type ``PayerReceiver`` is equivalent to the FpML PayerReceiver.model. Both of these are data structures used frequently throughout each respective model. In other cases, the CDM data structure is more normalised, per requirements from the CDM Design Working Group.  For example, price and quantity are represented in a single type, ``TradableProduct``, which is shared by all products. Another example is the use of a composable product model whereby:

* **Economic terms are specified by composition**, For example, the ``InterestRatePayout`` type is a component used in the definition of any product with one or more interest rate legs (e.g. Interest Rate Swaps, Equity Swaps, and Credit Default Swaps).  
* **Product qualification is inferred** from those economic terms rather than explicitly naming the product type, whereas FpML qualifies the product explcitly through the *product* substitution group.

Regardless of whether the data structure is the same or different from FpML, the CDM model includes defined Synonyms that map to FpML (and other models) and can be used for transformation purposes. More details on Synonyms are provided in the Mapping (Synonym) section of this document.

TradableProduct
^^^^^^^^^^^^^^^

A tradable product represents a financial product that is ready to be traded, meaning that there is an agreed financial product, price, quantity, and other details necessary to complete an execution of a security or a negotiated contract.  Tradable products are represented in the ``TradableProduct`` type. 

.. literalinclude:: code-snippets/TradableProduct.snippet
  :language: haskell
        
Quantity and price are represented in the ``TradableProduct`` type because they are attributes shared by all products. All of the other attributes required to describe a product are defined in distinct product types.

QuantityNotation
""""""""""""""""
The ``QuantityNotation`` type supports the quantity (or notional) for any product.

.. code-block:: Haskell

 type QuantityNotation: 
    quantity NonNegativeQuantity (1..1)
    assetIdentifier AssetIdentifier (1..1) 
    
The ``AssetIdentifier`` type requires the specification of either a product, currency or a floating rate option.

.. code-block:: Haskell

 type AssetIdentifier: 
    productIdentifier ProductIdentifier (0..1) 
    currency string (0..1) 
       [metadata scheme]
    rateOption FloatingRateOption (0..1) 
    condition: one-of
     
PriceNotation
"""""""""""""
The ``PriceNotation`` type supports the price for any product.

.. code-block:: Haskell

 type PriceNotation: 
    price Price (1..1)
    assetIdentifier AssetIdentifier (1..1) 
    
The ``Price`` type that is encapsulated within ``PriceNotation`` requires the definition of one of the price types represented in its data structure, which collectively support all the types for all of the products in the CDM.

.. code-block:: Haskell

 type Price: 
    cashPrice CashPrice (0..1)
    exchangeRate ExchangeRate (0..1)
    fixedInterestRate FixedInterestRate (0..1) 
    // For IR Swaps, CDS, Repo, and FRA
    floatingInterestRate FloatingInterestRate (0..1)
    condition: one-of
    
Financial Product
"""""""""""""""""

A financial product is an instrument that is used to transfer financial risk between two parties. Financial products are represented in the ``Product`` type, which is constrained by the ``one of`` condition, meaning that for a single Tradable Product, there can only be one Product:

.. code-block:: Haskell

 type Product: 
    [metadata key]
    contractualProduct ContractualProduct (0..1)
    index Index (0..1)
    loan Loan (0..1)
    foreignExchange ForeignExchange (0..1)
    security Security (0..1)
    condition: one-of

The CDM allows any one of these products to included in a trade or used as an underlier for another product (see the *Underlier* section).  One unlikely case for a direct trade is Index, which is primarily used as an underlier.

Among this set of products, the contractual product is the most complicated and requires the largest data structure. In a contractual product, an exchange of financial risk is materialised by a unique bilateral contract that specifies the financial obligations of each party. The terms of the contract are specified at trade inception and apply throughout the life of the contract (which can last for decades for certain long-dated products), unless amended by mutual agreement. Contractual products are fungible (in other words, replaceable by other identical or similar contracts) only under specific terms: e.g. the existence of a close-out netting agreement between the parties. 

Given that each contractual product transaction is unique, all of the contract terms must be specified and stored in an easily accessible transaction lifecycle model so that each party can evaluate the financial and counterparty risks during the life of the agreement.

Foreign Exchange (FX) spot and forward trades (including Non-Deliverable Forwards) and private loans also represent an exchange of financial risk represented by a form of bilateral agreements. FX forwards and private loans can have an extended term, and are generally not fungible. However, these products share few other commonalities with contractual products such as Interest Rate Swaps. Therefore, they are defined separately.  

By contrast, in the case of the execution of a security (e.g. a listed equity), the exchange of finanical risk is a one-time event that takes place on the settlement date, which is usually within a few business days of the agreement. The other significant distinction is that securities are fungible instruments for which the terms and security identifiers are publically available.  Therefore, the terms of the security do not have to be stored in a transaction lifecycle model, but can be referenced with public identifiers.

An Index product is an exception because it's not directly tradable, but is included here because it can be referenced as an underlier for a tradable product and can be identified by a public identifier.

Contractual Product
^^^^^^^^^^^^^^^^^^^
The scope of contractual products in the current model are summarized below:

* **Interest rate derivatives**:

  * Interest Rate Swaps (incl. cross-currency swaps, non-deliverable swaps, basis swaps, swaps with  non-regular periods, ...)
  * Swaptions
  * Caps/floors
  * FRAs
  * OTC Options on Bonds

* **Credit derivatives**:

  * Credit Default Swaps (incl. baskets, tranche, swaps with mortgage and loans underlyers, ...)
  * Options on Credit Default Swaps

* **Equity derivatives**:

  * Equity Swaps (single name)
  
* **Optons**:

  * Any other OTC Options (incl. FX Options)

In the CDM, contractual products are represented by the ``ContractualProduct`` type:

.. code-block:: Haskell

 type ContractualProduct:
    productIdentification ProductIdentification (0..1)
    productTaxonomy ProductTaxonomy (0..*)
    economicTerms EconomicTerms (1..1)

Note that price and quantity are defined in ``TradableProduct`` as these are attributes common to all products.  The remaining economic terms of the contractual product are defined in ``EconomicTerms`` which is an encapsulated type in ``ContractualProduct`` .

Economic Terms
""""""""""""""

The CDM specifies the various set of possible remaining economic terms using the ``EconomicTerms`` type, which includes attributes that are common to all payout types, such as effective date, termination date, and date adjustments.  A valid population of this type is constrained by a set of conditions which are not shown here in the interests of brevity.

.. code-block:: Haskell

 type EconomicTerms: 
    [partialKey]
    effectiveDate AdjustableOrRelativeDate (0..1)
    terminationDate AdjustableOrRelativeDate (0..1) 
    dateAdjustments BusinessDayAdjustments (0..1) 
    payout Payout (1..1) ;
    earlyTerminationProvision EarlyTerminationProvision (0..1)
    optionProvision OptionProvision (0..1) 
    extraordinaryEvents ExtraordinaryEvents (0..1) 
 
Payout
""""""
The ``Payout`` type defines the composable payout types, each of which describes a set of terms and conditions for the financial responsibilities between the two contractual parties. Payout types can be combined to compose a product.  For example, an Equity Swap can be composed by combining an ``InterestRatePayout`` and an ``EquityPayout``.

.. code-block:: Haskell

 type Payout:
    interestRatePayout InterestRatePayout (0..*)
    creditDefaultPayout CreditDefaultPayout (0..1)
    equityPayout EquityPayout (0..*) 
    optionPayout OptionPayout (0..*) 
    forwardPayout ForwardPayout (0..*)
    securityPayout SecurityPayout (0..*) 
    cashflow Cashflow (0..*)
   
The relationship between one of the payout classes and a similar structure in FpML can be identified through the defined Synonyms, as explained in an earlier section.  For example, the ``InterestRatePayout`` is equivalent to the following complex types in FpML: *swapStream*, *feeLeg* *capFloorStream*, *fra*, and *interestLeg*.

.. code-block:: Haskell

 type InterestRatePayout extends PayoutBase: 
    [metadata key]
    payerReceiver PayerReceiver (0..1)
    dayCountFraction DayCountFractionEnum (0..1) 
    [metadata scheme]
    calculationPeriodDates CalculationPeriodDates (0..1)
    paymentDates PaymentDates (0..1)  
    paymentDate AdjustableDate (0..1) 
    paymentDelay boolean (0..1)
    resetDates ResetDates (0..1)
    discountingMethod DiscountingMethod (0..1) 
    compoundingMethod CompoundingMethodEnum (0..1) 
    cashflowRepresentation CashflowRepresentation (0..1) 
    crossCurrencyTerms CrossCurrencyTerms (0..1)
    stubPeriod StubPeriod (0..1) 
    bondReference BondReference (0..1) 
    fixedAmount calculation (0..1)
    floatingAmount calculation (0..1) 

There are as set of conditions associated with this type which are not shown here in the interests of brevity.

Reusable Components
"""""""""""""""""""

There are a number of components that are reusable across several payout types.  For example,  the ``CalculationPeriodDates`` class describes the inputs for the underlying schedule of a stream of payments.

.. code-block:: Haskell

 type CalculationPeriodDates 
    [metadata key]
    effectiveDate AdjustableOrRelativeDate (0..1)
    terminationDate AdjustableOrRelativeDate (0..1)
    calculationPeriodDatesAdjustments BusinessDayAdjustments (0..1)
    firstPeriodStartDate AdjustableDate (0..1)
    firstRegularPeriodStartDate date (0..1)
    firstCompoundingPeriodEndDate date (0..1) 
    lastRegularPeriodEndDate date (0..1) 
    stubPeriodType StubPeriodTypeEnum (0..1) 
    calculationPeriodFrequency CalculationPeriodFrequency (0..1) 

Underlier
"""""""""

The ``Underlier`` type allows for any product to be used as the underlier for a higher-level product such as an option, forward, or an equity swap. 

.. code-block:: Haskell

 type Underlier:
    underlyingProduct Product (1..1) 

This nesting of the product component is another example of a composable product model. One use case is an interest rate swaption for which the high-level product uses the ``OptionPayout`` type and underlier is an Interest Rate Swap composed of two ``InterestRatePayout`` types. Similiarly, the product underlying an Equity Swap composed of an ``InterestRatePayout`` and an ``EquityPayout`` would be a non-contractual product: an equity security.

Identified Product
""""""""""""""""""

For identified products the CDM approach is to exclude any attribute that can be abstracted through reference data. Specifying such information as part of the contract information would lead to a risk or contradictory information with the reference data.

.. code-block:: Haskell

 type IdentifiedProduct
    productIdentifier ProductIdentifier (1..1)

As a result, the bond, equity, and other securities are defined as extensions of the product identifier without any additional attributes. 

Product Qualification
^^^^^^^^^^^^^^^^^^^^^

**The product qualification is inferred from the product's economic terms through qualification logic** which is predicated on the product's model components. The qualification logic leverages the *object qualification* feature of the Rosetta DSL described in the `Function Definition Section`_

The CDM makes use of the ISDA taxonomy V1.0 leaf level to qualify the product. 25 product types across interest rate swap and foreign exchange have so far been qualified as part of the CDM, in effect representing the full ISDA V1.0 scope. The current CDM implementation does not qualify credit default swap products, as the ISDA taxonomy V1.0 references the transaction type instead of the product features for those, which values are not publicly available and hence not positioned as a CDM enumeration.

.. note:: Follow-up is in progress with the ISDA Credit Group to evaluate whether an alternative product qualification could be developed in subsequent versions of the CDM that would leverage the approach adopted for interest rate derivatives.

The qualification of a Zero-Coupon Fixed-Float Inflation Swap provides an example of the product qualification logic, which combines a series of boolean expressions with an ``and`` operator.

.. code-block:: Haskell

 func Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon:
   [qualification Product]
   inputs: economicTerms EconomicTerms (1..1)
   output: is_product boolean (1..1)
     	[synonym ISDA_Taxonomy_v1 value "InterestRate_IRSwap_Inflation"]
   assign-output is_product:
     economicTerms -> payout -> interestRatePayout -> rateSpecification -> fixedRate count = 1
     and economicTerms -> payout -> interestRatePayout -> rateSpecification -> inflationRate count = 1
     and economicTerms -> payout -> interestRatePayout -> rateSpecification -> floatingRate is absent
     and economicTerms -> payout -> interestRatePayout -> crossCurrencyTerms -> principalExchanges is absent
     and economicTerms -> payout -> optionPayout is absent
     and economicTerms -> payout -> interestRatePayout -> paymentDates -> paymentFrequency -> periodMultiplier = 1
     and economicTerms -> payout -> interestRatePayout -> paymentDates -> paymentFrequency -> period = PeriodExtendedEnum -> T

The synonym value associated to the function indicates "InterestRate_IRSwap_Inflation" as the qualification name, with ``ISDA_Taxonomy_v1`` as the source. The product is qualified according to that name when the function evaluates to True.

The resulting product qualification is positioned as the ``productQualifier`` attribute of the ``ProductIdentification`` type, alongside the attributes currently used in FpML to specify the product. These FpML attributes are each associated to a ``scheme``:

.. code-block:: Haskell

 type ProductIdentification:
   productQualifier productType (0..1)
   primaryAssetdata AssetClassEnum (0..1)
     [metadata scheme]
   secondaryAssetdata AssetClassEnum (0..*)
     [metadata scheme]
   productType string (0..*)
     [metadata scheme]
   productId string (0..*)
     [metadata scheme]

This approach allows to specify the credit derivatives products, until such time when an alternative approach to the transaction type is identified to support a proper product qualification for credit derivatives.

The *qualified type* feature of the Rosetta DSL, as detailed in the `Qualified Type Section`_, provides a stamping mechanism that identifies the ``productQualifier`` attribute as the output of an object qualification function. The qualification function is annotated with ``[qualification Product]``, which connects its output to the ``productType`` qualified type.

When creating the CDM product object, the result of the product qualification is stamped onto the generated object, as illustrated in the below (serialised) JSON snippet. The product identification includes both the product qualification generated by the CDM and the product identification information from the originating FpML document.

.. code-block:: Java

  "productIdentification": {
    "primaryAssetClass": "INTEREST_RATE",
    "productId": [
      "InterestRate:IRSwap:OIS"
    ],
    "productIdScheme": "http://www.fpml.org/coding-scheme/product-taxonomy",
    "productQualifier": "InterestRate_IRSwap_FixedFloat",
    "productType": [
     "InterestRate:IRSwap:OIS"
    ],
    "productTypeScheme": "http://www.fpml.org/coding-scheme/product-taxonomy",
    "secondaryAssetClassScheme": "http://www.fpml.org/coding-scheme/asset-class-simple"
  }


Event Model
-----------

The CDM event model is based upon the same composition principle as the product model:

* **Business events are specified by composition** of *primitive events*, which use a large set of the FpML event building blocks.
* **Event qualification is inferred** from those primitive events and, in some relevant cases, from an **intent** qualifier.

Primitive Event
^^^^^^^^^^^^^^^

CDM primitive events are the building block components used to specify business events. The current list of primitive events can be seen in the below snippet, as well as a few examples of such primitive events:

.. code-block:: Java

 class PrimitiveEvent
 {
  inception Inception (0..*);
  quantityChange QuantityChangePrimitive (0..*);
  allocation AllocationPrimitive (0..*);
  termsChange TermsChangePrimitive (0..1);
  exercise ExercisePrimitive (0..1);
  observation ObservationPrimitive (0..*);
  reset ResetPrimitive (0..*);
  transfer Transfer (0..*);
 }
 
 class Inception
 {
  before ContractState (0..0);
  after PostInceptionState (1..1);
 }
 
 class ObservationPrimitive
 {
  source ObservationSource (1..1);
  observation number (1..1)
  date date (1..1);
  time TimeZone (0..1);
  side QuotationSideEnum (0..1);
 }

Primitive events can be thought of as mathematical operators on a state of a transaction lifecycle. Apart from the ``ObservationPrimitive``, they each have a ``before`` and ``after`` attributes that define the state transition components. From an observation, which is independent from any transaction and is the equivalent of the **market data oracle** in a distributed ledger context, a ``ResetPrimitive`` can be built which does affect a particular transaction. A separate ``Transfer`` can be built in case that reset generates a cashflow.

**Note 1**: In the ``Inception`` primitive, which corresponds to the execution of a contract, the ``before`` state is a ``ContractState`` with 0 cardinality, as the CDM currently does not tackle any the pre-execution lifecycle.

**Note 2**: Not all primitive events are currently composed of a ``before`` and ``after`` state. This will need to be reviewed and potentially harmonised to establish a clean state-transition model in the CDM.

As mathematical operators, primitive events reflect a many-to-one mapping with actual business events. An example composition of the primitive building blocks to represent a business event is the **partial novation** of a contract:

* an ``Inception`` primitive creates the contract with the novation party. The ``tradeDate`` on the novated portion of the contract should reflect the date of the novation event.
* a ``QuantityChange`` primitive applies to the original contract where the quantity after change is different from 0 (0 would represent the case of a full novation).

Baseline Event Features
^^^^^^^^^^^^^^^^^^^^^^^

The ``Event`` class that represents business events carries the following information:

.. code-block:: Java

 class Event key
 {
  messageInformation MessageInformation (0..1);
  timestamp EventTimestamp (1..*);
  eventIdentifier Identifier (1..*);
  eventQualifier eventType (0..1);
  eventDate date (1..1);
  effectiveDate date (0..1);
  action ActionEnum (1..1);
  intent IntentEnum (0..1);
  party Party (0..*);
  account Account (0..*);
  lineage Lineage (0..1);
  primitive PrimitiveEvent (1..1);
  functionCall string (0..1);
  eventEffect EventEffect (0..1);
 }

The ``primitive`` attribute describing the mathematical set of operators for the business event is of cardinality 1, whereby the actual composition into possibly multiple primitive events happens in the ``PrimitiveEvent`` class.

Message Information
"""""""""""""""""""

The ``messageInformation`` attribute corresponds to some of the components of the FpML *MessageHeader.model*.

.. code-block:: Java

 class MessageInformation
 {
  messageId string (1..1) scheme;
  sentBy string (0..1) scheme;
  sentTo string (0..*) scheme;
  copyTo string (0..*) scheme;
 }

``sentBy``, ``sentTo`` and ``copyTo`` information is optional, as possibly not applicable in a distributed ledger context.

Timestamp
"""""""""

The CDM adopts a generic approach to represent timestamp information, consisting of a ``dateTime`` and a ``qualification`` attributes, with the latter specified through an enumeration value.

.. code-block:: Java

 class EventTimestamp
 {
  dateTime zonedDateTime (1..1) ;
  qualification EventTimeStampQualificationEnum (1..1);
 }

The experience of mapping the CME clearing and the DTCC trade matching and cashflow confirmation transactions to the CDM did reveal a diverse set of timestamps. The expected benefits of the CDM generic approach are twofold:

* It allows for flexibility in a context where it would be challenging to mandate which points in the process should have associated timestamps.
* Gathering all of those in one place in the model allows for evaluation and rationalisation down the road.

The CDM Group has expressed concerns with combining timestamps which are deemed 'technical' with 'business' ones. A further evaluation of this timestamp modelling approach will be required.

Below is JSON representation instance of this approach.

.. code-block:: Java

 "timestamp": [
  {
     "dateTime": "2007-10-31T18:08:40.335-05:00",
     "qualification": "EVENT_SUBMITTED"
  },
  {
     "dateTime": "2007-10-31T18:08:40.335-05:00",
     "qualification": "EVENT_CREATED"
  }
 ]

Event Identification
""""""""""""""""""""

The event identification information comprises the ``identifier`` and an optional ``version`` and ``issuer``. FpML also uses an event identifier construct: the *CorrelationId*, distinct from the identifier associated with the trade (which itself comes in different variations: *PartyTradeIdentifier*, with the *TradeId* and the *VersionedTradeId* as sub-components). As a departure from FpML, the CDM approach consists in using a common identifier component across products and events.

.. code-block:: Java

 class Identifier key
 {
  issuerReference Party (0..1) reference;
  issuer string (1..1) scheme;
  assignedIdentifier AssignedIdentifier (1..*);
 }

Intent Qualification
""""""""""""""""""""

Intent qualification is an enumeration value such as ``allocation``, ``earlyTermination``, ``partialTermination`` etc. It is used as part of the event qualification logic, to disambiguate events which features are shared across lifecycle events. As an example, a reduction in a trade quantity/notional could apply to a correction event or a partial termination (although the timing of such event could also be used to qualify the proper event).

Further evaluation of the appropriateness of this intent qualification is required.

Lineage Information
"""""""""""""""""""

``Lineage`` is a class that is used to reference an unbounded set of contracts, events and/or payout components, as shown by the below code snippet:

.. code-block:: Java

 class Lineage
 {
  contractReference Contract (0..*) reference;
  eventReference Event (0..*) reference;
  transferReference Transfer (0..*) reference;
  creditDefaultPayoutReference CreditDefaultPayout (0..*) reference;
  interestRatePayoutReference InterestRatePayout (0..*) reference;
  optionPayoutReference OptionPayout (0..*) reference;
 }

Function Call
"""""""""""""

An example of a function call is the interpolation function that would be associated with a **derived observation** event, which assembles two observed values (say, a 3 months and a 6 months rate observation) to provide a derived one (say, a 5 months observation).

As part of the current CDM version this function call as been specified as a mere string element. It will be appropriately specified once such implementation is developed, some of which consisting in the machine executable implementation of the ISDA Definitions (see the *Calculation* section).

Event Effect
""""""""""""

The ``eventEffect`` attribute corresponds to the set of operational and positional effects associated with a lifecycle event. This information is generated by the CDM as a set of pointers to the relevant objects that are affected by the event. The candidate objects are classes that are referenceable with an associated ``key``.

Events such as observations do not have any event effect, hence the optional cardinality.

.. code-block:: Java

 class EventEffect
 {
  effectedContract Contract (0..*) reference;
  contract Contract (0..*) reference;
  effectedExecution Execution (0..*) reference;
  execution Execution (0..*) reference;
  productIdentifier ProductIdentifier (0..*) reference;
  transfer Transfer (0..*) reference;
 }

In the below JSON snippet of a quantity change event on a contract, we can see that the ``eventEffect`` contains a  number of hash value references:

.. code-block:: Java
  
  "action": "NEW",
  "effectiveDate": "2018-03-15",
  "eventDate": "2018-03-14",
  "eventEffect": {
    "contract": [
      {
        "globalReference": "600e4873"
      }
    ],
    "effectedContract": [
      {
        "globalReference": "d36e1d72"
      }
    ],
    (...)
    "transfer": [
      {
        "globalReference": "ee4f7520"
      }
    ]
  },
  (...)
  "primitive": {
    "quantityChange": [
      {
        "after": {
          "contract": {
            (...)
            "meta": {
              "globalKey": "600e4873"
            }
            "tradeDate": {
              "date": "2002-12-04",
              "meta": {
                "globalKey": "793cd7c"
              }
            }
          }
        },
        "before": {
          "contract": {
            (...)
            "meta": {
              "globalKey": "d36e1d72"
            },
            "tradeDate": {
              "date": "2002-12-04",
              "meta": {
                "globalKey": "793cd7c"
              }
            }
          }
        }
      }
    ],
    "transfer": [
      {
        "cashTransfer": [
          {
            "amount": {
              "amount": 45860.23,
              "currency": {
                "value": "JPY"
              },
              "meta": {
                "globalKey": "66c5234f"
              }
            },
            (...)
          }
        ],
        "meta": {
          "globalKey": "ee4f7520"
        },
        "settlementDate": {
          "adjustedDate": {
            "value": "2018-03-17"
          }
        }
      }
    ]
  }

* For the ``effectedContract`` effect: ``d36e1d72`` points to the original contract in the ``before`` state of the ``quantityChange`` primitive event.
* For the ``contract`` effect: ``600e4873`` points to the new contract in the ``after`` state of the ``quantityChange`` primitive event. Note how the new contract retains the initial ``tradeDate`` attribute of the original contract even after a quantity change.
* For the ``transfer`` effect: ``ee4f7520`` points to the ``transfer`` primitive event.

Post-Execution: Contract
""""""""""""""""""""""""

For a contractual product, once a transaction has been agreed between the parties, a contract gets executed between the contractual legal entities for that transaction. In addition to the product economics captured by the ``contractualProduct`` attribute, a contract has a set of attributes which are only qualified at the execution and post-execution stage: trade date, calculation agent, documentation, governing law, etc.

The current CDM scope is limited to the post-execution part of the transaction lifecycle.

.. code-block:: Java

 type Contract: 
   [metadata key]
   [rootType]
   contractIdentifier Identifier (1..*)
   tradeDate TradeDate (1..1)
   clearedDate date (0..1)
   tradableProduct TradableProduct (1..1)
   collateral Collateral (0..1)
   documentation RelatedAgreement (0..1)
   governingLaw GoverningLawEnum (0..1) 
      [metadata scheme]
   party Party (0..*) 
   account Account (0..*) 
   partyRole PartyRole (0..*) 
   calculationAgent CalculationAgent (0..1)
   partyContractInformation PartyContractInformation (0..*)
   closedState ClosedState (0..1)

The ``Contract`` class incorporates all the elements that are part of the FpML *trade* confirmation view, with the exception of: *tradeSummary*, *originatingPackage*, *allocations* and *approvals*, whereas the ``ContractualProduct`` class corresponds to the pre-trade view of the FpML *trade*.

**Note**: The FpML *trade* term has not been used as part of the CDM because deemed ambiguous in this respect. Its use as part of the FpML standard is due to an exclusive focus on post-execution activity in the initial stages of its development. Later adjustments in this respect would have been made difficult as a result of backward compatibility considerations.

Other Misc. Information
"""""""""""""""""""""""

* **Date information** is provided through the ``eventDate`` and ``effectiveDate`` attributes, the latter being optional as not applicable to certain events (e.g. observations).
* **Action qualification** specifies whether the event is a new one or a correction or cancellation of a prior one.
* **Party information** is optional because not applicable to certain events (e.g. most of the observation events).
* **Event qualifier** is derived from the event features, as per the *Event Qualification* section.

Event Qualification
^^^^^^^^^^^^^^^^^^^

Similar to the product modelling approach, the CDM lifecycle events are qualified as a function of the combination of their primitive event features and, when specified, the ``intent`` attribute. The event qualification uses the ``isEvent`` syntax in Rosetta, which is specified as part of the *Object Qualification* in the *CDM Modelling Artefacts* section of the documentation.

The CDM makes use of the ISDA taxonomy V2.0 leaf level to qualify the event.  The synonymity with the ISDA taxonomy V1.0 has been systematically indicated as part of the model upon request from CDM group participants, who pointed out that a number of them use it internally. 22 lifecycle events have currently been qualified as part of the CDM.

One distinction with the product approach is that the ``intent`` qualification is also deemed necessary to complement the primitive event information in certain cases. To this effect, the Rosetta event qualification syntax allows to specify that the intent must have a specified value *when present*, as illustrated by the below snippet.

.. code-block:: Java

 isEvent Termination
  Event -> intent when present = IntentEnum.Termination
  and Event -> primitive -> quantityChange single exists
  and quantityAfterQuantityChange = 0.0
  and Event -> primitive -> quantityChange -> after -> contract -> closedState -> state = ClosedStateEnum.Terminated
  and Event -> primitive -> quantityChange -> after -> clearingStatus is absent

The event qualification is positioned as a the ``eventQualifier`` attribute of the ``Event`` class. Like the product qualifier, the event qualification is stamped onto the generated CDM objects and their JSON serialized representation, as illustrated by the below JSON lifecycle event snippet:

.. code-block:: Java

  "eventDate": "2018-03-20",
  "eventEffect": {
   "referenceEvent": "d4afb0aa"
  },
  "eventIdentifier": {
   "identifierValue": {
     "identifier": "789325456"
   }
  },
  "eventQualifier": "NewTradeEvent",
  "messageInformation": {
   "messageId": "1486297",
   "messageIdScheme": "http://www.party1.com/message-id",
   "sentBy": "894500DM8LVOSCMP9T34",
   "sentTo": "49300JZDC6K840D7F79"
  },

Legal Agreement
---------------

The CDM provides a digital representation of the legal agreements that govern financial contracts and workflows. The benefits are:

* **Supporting marketplace initiatives to streamline and standardise legal agreements** with a comprehensive digital representation of such agreements. While the initial scope is focused on the ISDA legal agreements, it is not limited to those.  As an example, as a follow-up from work to represent secured funding contracts and associated lifecycle events, the CDM will look to represent the associated governing legal agreements (such as GMRA for repo).
* **Providing a comprehensive representation of the financial workflows** by complementing the contract and lifecycle event representation. Collateral management is an example of the applicability of such approach, as most of the flows require reference to the associated legal agreements (such as the ISDA Initial Margin and Variation Margin Credit Support Annex).

The current CDM scope comprises the following features:

* **Composable and normalised model representation** of a number of legal agreements:

  * ISDA 2016 Credit Support Annex for Initial Margin, with the New York, Japanese and English governing laws
  * ISDA 2016 Credit Support Annex for Variation Margin, New York governing law

* **Mapping to existing marketplace representations** for the following initiatives:
  
  * **ISDA Create Initial Margin**: Ingestion of JSON sample files generated from the ISDA Create platform for the elections associated with the ISDA 2016 CSA for Initial Margin has been implemented, to demonstrate connectivity between the ISDA Create Initial Margin negotiation tool and the CDM. (The ISDA CSA Variation Margin is not yet represented in ISDA Create.) A specific set of synonyms associated to the ``ISDA_Create_1_0`` synonym source has been developed to enable this mapping (see *Mapping* section).
  * **AcadiaSoft Agreement Manager**: Initial work has been developed to map the CDM to the AcadiaSoft Agreement Manager, although only limited progress has been made so far.
  
* **Linking of legal agreement into contract** through the CDM referencing mechanism.

Modelling Approach
^^^^^^^^^^^^^^^^^^

The current CDM model leverages some prior and current work:

* The FpML Legal View, which was developed in 2013-14 to support the ISDA Standard CSA in a generic manner
* The ISDA Create solution, in its version 1.0.

The intent is to also leverage the **AcadiaSoft Agreement Manager** solution as part of further iterations of the model, to enable integration with the collateral management workflow.

The key modelling principles that have been adopted to represent legal agreements are:

* **Distinction between the agreement identification features and the content features** (i.e. elections).

  * The agreement identification features: agreement name, publisher, identification, etc are represented by the ``LegalAgreementBase`` abstract class.
  * The elections are represented through classes aligned with the legal agreement template which they represent. An example is the ``CsaInitialMargin2016JapaneseLaw`` class, which represents the ISDA 2016 Japanese Law CSA for Initial Margin.
  
* **Composite model**.

  * The ``LegalAgreementBase`` abstract class uses components that are also used as part of the CDM contract and lifecycle event components: e.g. ``Party``, ``Identifier``, ``PartyRole``.
  * As part of the election classes: the above mentioned ``CsaInitialMargin2016JapaneseLaw`` class extends the ``CsaInitialMargin2016`` abstract class which specifies the elections that are common among governing laws. The ``CsaInitialMargin2016`` in turn extends the ``Csa2016`` abstract class which specifies the elections that are common among the ISDA 2016 Initial Margin and Variation Margin CSA agreements.
  
* **Representation of legal agreement elections as data**, as opposed to their whole write-up. Similar to what has been done in ISDA Create, such approach still allows CDM users to wrap those normalised elections into the corresponding legal agreement template, in order to provide a complete legal agreement.
* **Normalisation of the data representation** to be machine readable and executable. In practice, the use of elections expressed in a ``string`` format has been restricted whenever possible, as ``string`` requires language parsing and disassembling to be machine executable. The CDM leverages the ISDA Create data representation and extends it in some cases, leveraging some output of the FpML work to digitise the Standard CSA. Notable examples of such approach are:

  * The ``EligibleCollateral`` class comprehensively specifies the eligible collateral for initial and variation margin as directly machine readable, through the combination of an enumeration of eligible assets (based upon the 2003 ISDA Collateral Asset Definitions), normalised maturity bands and agency rating notations.
  * The ``EligibilityToHoldCollateral`` class specifies the conditions under which a party and its custodian(s) are entitled to hold collateral under the ISDA CSA for Variation Margin, through the combination of party terms that are specified through an enumeration, normalised custodian terms (see below) and/or the enumeration of countries in which such collateral can be held.
  * The ``CustodianTerms`` class specifies the requirements applicable to the custodian with respect to the holding of posted collateral, through the combination of minimal assets and minimal rating considerations or through the designation of a specific custodian.

The Elective Provisions
^^^^^^^^^^^^^^^^^^^^^^^

The current CDM scope is limited to the ISDA 2016 CSA for Initial Margin and Variation Margin. In this context, the model components are organised around 3 levels, in this order of abstraction:

* **Vintage**, such as CSA 2016
* **Margin Type**, i.e. Initial or Variation Margin
* **Governing Law**, such as New York or Japanese

The ``Csa2016`` abstract class specifies the set of provisions that are common among governing laws and across Initial and Variation Margin templates. This abstract class will evolve as further vintages of the ISDA CSA are being modelled.

.. code-block:: Java

  abstract class Csa2016
  {
   baseCurrency string (1..1) scheme;
   additionalObligations string (0..1);
   conditionsPrecedent ConditionsPrecedent (1..1);
   substitution Substitution (1..1);
   disputeResolution DisputeResolution (1..1);
   additionalRepresentation AdditionalRepresentation (1..1);
   demandsAndNotices ContactElection (1..1);
   addressesForTransfer ContactElection (1..1);
   bespokeProvision string (0..1) ;
  }

The ``CsaInitialMargin2016`` abstract class extends the ``Csa2016`` class to specify the provisions for the 2016 ISDA Credit Support Annex for Initial Margin that are common across the applicable governing laws.

.. code-block:: Java

  abstract class CsaInitialMargin2016 extends Csa2016
  {
   regime Regime (1..1);
   oneWayProvisions OneWayProvisions (1..1);
   method Method (1..1);
   identifiedCrossCurrencySwap boolean (1..1);
   sensitivityToEquity SensitivityMethodology (1..1);
   sensitivityToCommodity SensitivityMethodology (1..1);
   fxHaircutCurrency FxHaircutCurrency (1..1);
   creditSupportObligations CreditSupportObligationsInitialMargin (1..1);
   calculationDateLocation CalculationDateLocation (1..1);
   notificationTime NotificationTime (1..1);
   terminationCurrency TerminationCurrencyAmendment (1..1) ;
  }

The ``CsaVariationMargin2016`` abstract class extends the ``Csa2016`` class to specify the provisions for the 2016 ISDA Credit Support Annex for Variation Margin that are common across the applicable governing laws.  At this point its implementation has been undertaken without a thorough review of the Japanese and English governing laws as only a New York sample agreement was available. It might have to be adjusted to integrate those governing laws.

.. code-block:: Java

  abstract class CsaVariationMargin2016 extends Csa2016
  {
   creditSupportObligations CreditSupportObligationsVariationMargin (1..1);
   valuationAgent Party (1..1) reference;
   valuationDateLocation CalculationDateLocation (1..1);
   valuationTime BusinessCenterTime (1..*);
   notificationTime int (1..1);
   holdingAndUsingPostedCollateral HoldingAndUsingPostedCollateral (1..1);
   creditSupportOffsets boolean (1..1);
   otherCsa RelatedAgreement (1..1);
  }

The (non-abstract) classes that represent the ISDA CSA elections extend the above abstract constructs:

* For Initial Margin: the ``CsaInitialMargin2016JapaneseLaw``, ``CsaInitialMargin2016NewYorkLaw`` and ``CsdInitialMargin2016EnglishLaw`` classes extend the ``CsaInitialMargin2016`` abstract class to specify the Initial Margin elections that are specific to those governing laws.
* For Variation Margin: the ``CsaVariationMargin2016NewYorkLaw`` class extends the ``CsaVariationMargin2016`` abstract class to specify the Variation Margin elections that are specific to New York law.

Linking Legal Agreements to Contracts and Events
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The CDM uses the key / referencing mechanism to tie a legal agreement with the relevant contract or event.

This referencing mechanism has been implemented for the ``Contract`` but not yet for the ``Event``, since no lifecycle event workflow has yet been specified that references legal agreement other than through the contract itself.

Referencing the legal agreement from the ``Contract`` is done through the ``documentation`` attribute.  The associated ``Documentation`` class allows to:

* Identify some of the key terms of a governing legal agreement such as the agreement identifier, the publisher, the document vintage and the agreement date, as part of the ``documentationIdentification`` attribute
* Reference a legal agreement that is electronically represented in the CDM through the ``legalAgreement`` attribute, which has a reference key into the agreement instance

The below snippet represents this ``Documentation`` class, which ``legalAgreement`` attribute carries the ``reference`` qualifier and where the ``LegalAgreement`` class carries associated ``key`` qualifier:

.. code-block:: Java

 class Documentation
 {
  legalAgreement LegalAgreement (0..*) reference;
  documentationIdentification DocumentationIdentification (0..1);
 }

 class LegalAgreement extends LegalAgreementBase key one of
 {
  csdInitialMargin2016EnglishLaw CsdInitialMargin2016EnglishLaw (0..1);
  csaInitialMargin2016JapaneseLaw CsaInitialMargin2016JapaneseLaw (0..1);
  csaInitialMargin2016NewYorkLaw CsaInitialMargin2016NewYorkLaw (0..1);
  csaVariationMargin2016NewYorkLaw CsaVariationMargin2016NewYorkLaw (0..1);
 }


Process Model
-------------

Industry processes represent events and actions through a trade's lifecycle, such as allocating a block-trade or calculating coupon payments. While ISDA already defines how industry processes should work in the ISDA Documentation, errors in implementation still cause friction amongst market participants.

According to ISDA, even processes that are generally considered trivial continue to cause conflict amongst market participants. Evidence shows that even calculations defined in mathematical notation have been implemented incorrectly by seasoned practitioners. For example, day count fraction formulae are used when calculating coupon payments and are often a source of dispute between parties in a transaction.

ISDA CDM's Process Model goes beyond many existing technical standards by representing industry processes in an unambiguous, machine-readable and machine-executable way. Machine readability and execution is crucial in reducing implementation risk for market participants and provides a blueprint for industry utilities. Machine-executable code is systematically generated from the model, which further reduces risk during model version upgrades.

Scope
^^^^^

When trying to understand the scope of the Process Model, it is important to consider two dimensions:

#. The industry processes that should be covered, which we will call **Coverage**.
#. The level of detail each process should be specified, which we will call **Granularity**.


Coverage
""""""""

The Process Model covers the post-trade lifecycle of over-the-counter derivative transactions. Processes such as the following are all considered to be in scope:

* Trade execution
* Margin calculation
* Coupon Payment
* Clearing

Generally, where a process is defined in ISDA Documentation, it should be considered as in-scope. Regulatory reporting is included in the ISDA CDM, but will be covered in a different documentation section.

For an up-to-date list of model coverage, please refer to the `function coverage matrix`_ (coming soon).

Granularity
"""""""""""

It is not always possible or desirable to specify processes to minute detail. Parts of processes (or sub-processes) may be omitted from the model for the following reasons:

* The sub-process is not needed to create a functional ISDA CDM data object.
* The sub-process has already been defined and its implementation is widely adopted by the industry.
* The sub-process is specific to a firm's internal process and therefore cannot be specified in an industry standard.

Details on each point are documented below.

This is especially important for those considering adoption to understand what is specified and what is not, as unspecified parts represent functionality that must be implemented internally. To understand the granularity of a process, look at what data it produces. Each process defines the output data type, and how attributes on that data type should be populated. Attributes without any definition in the process will need to be populated by the implementor.

**Functional ISDA CDM data objects**

Whilst it is possible to define every process, sub-process and sub-sub-process, the Process Model focuses on what is necessary to create functional ISDA CDM data objects, that is, an object that satisfies the below criterion:

* Any `BusinessEvent` and `EconomicTerms` inside the object can be qualified.
* Linage between events is preserved.

It is the responsibility of the adopter to populate the remaining data values required for data objects to be valid. This must be done by extending the processes defined in the model at the implementation level - by writing executable code.

**Reuse where possible**

Where widely adopted process models exist, they should be reused and not redefined. Following are some examples:

* Quantitative finance. Open-source quantitative finance software already defines many granular processes, such as:

  * Computing a coupon schedule from a set of parameters.
  * Adjusting dates given a holiday calendar.

* Mathematical functions. Functions such as sum, absolute, and average are widely understood, so are not redefined in the model.

.. _function coverage matrix: Portal_

Modelling Approach
^^^^^^^^^^^^^^^^^^

This concept of combining and reusing small components is consistent with how the Product Model and Event Model are designed.

Introducing Functions
^^^^^^^^^^^^^^^^^^^^^

Functions are the foundations of the Process Model. They are very generic and can be used to represent anything that changes data. To give some concrete examples, they are used in the following scenarios:

* Transition a trade from "executed" to "terminated" state.
* Compute the amount and direction of margin required between two parties.
* Checking whether preconditions in a workflow step have been met.

To define functions, ISDA CDM uses Rosetta.

For detailed documentation on the Rosetta syntax used to define functions in ISDA CDM, see the `Rosetta DSL Documentation on Functions`_.

.. _Rosetta DSL Documentation on Functions: https://docs.rosetta-technology.io/dsl/documentation.html#function-artefacts
.. _data modelling: https://en.wikipedia.org/wiki/Cardinality_(data_modeling)#Application_program_modeling_approaches

Validation Process
^^^^^^^^^^^^^^^^^^

While validation rules are generally specified for existing data standards like FpML alongside the standard documentation, the logic needs to be evaluated and transcribed into code by the relevant teams. More often than not, it results in such logic not being consistently enforced.

As an example, the ``FpML_ird_57`` data rule implements the **FpML ird validation rule #57**, which states that if the calculation period frequency is expressed in units of month or year, then the roll convention cannot be a week day. With Rosetta, this legible view is provided alongside a programmatic implementation thanks to automatic code generation.

.. code-block:: Java

 class Frequency key
 {
  periodMultiplier int (1..1);
  period PeriodExtendedEnum (1..1);
 }

 class CalculationPeriodFrequency extends Frequency
 {
  rollConvention RollConventionEnum (1..1);
 }

 data rule FpML_ird_57 <"FpML validation rule ird-57 - Context: CalculationPeriodFrequency. [period eq ('M', 'Y')] not(rollConvention = ('NONE', 'SFE', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT','SUN')).">
  when CalculationPeriodFrequency -> period = PeriodExtendedEnum.M or CalculationPeriodFrequency -> period = PeriodExtendedEnum.Y
  then CalculationPeriodFrequency -> rollConvention <> RollConventionEnum.NONE
   or CalculationPeriodFrequency -> rollConvention <> RollConventionEnum.SFE
   or CalculationPeriodFrequency -> rollConvention <> RollConventionEnum.MON
   or CalculationPeriodFrequency -> rollConvention <> RollConventionEnum.TUE
   or CalculationPeriodFrequency -> rollConvention <> RollConventionEnum.WED
   or CalculationPeriodFrequency -> rollConvention <> RollConventionEnum.THU
   or CalculationPeriodFrequency -> rollConvention <> RollConventionEnum.FRI
   or CalculationPeriodFrequency -> rollConvention <> RollConventionEnum.SAT
   or CalculationPeriodFrequency -> rollConvention <> RollConventionEnum.SUN

Adopting the Process Model
^^^^^^^^^^^^^^^^^^^^^^^^^^

ISDA CDM minimises the cost of adoption, which is another differentiator from other existing technical standards. Using purpose-built technology, the Process Model is systematically translated into machine-executable code. The following modern, widely, and freely available programming languages are supported:

* Java
* Scale
* DAML
* Typescript

Executable code artefacts are made freely available for use and can be download from the ISDA CDM Portal_.

In contrast, adopting a traditional standard that is written in prose might look like the following:

#. Domain experts need to understand the intentions of the standard.
#. Business analysts need to translate the above into a set of technical requirements.
#. Software engineers need to turn technical requirements into executable software.

Systematically generating executable code reduces the work required in points 2 and 3. Each step comes with the risk of misinterpretation and failed implementation. Each adopting firm will typically go through the same process, which further increases the risk for the industry. These risks ultimately add up to high implementation costs.

.. _Portal: https://portal.cdm.rosetta-technology.io

Reference Data Model
--------------------

The CDM only integrates the reference data components that are specifically needed to model the in-scope products, events, legal agreements and function components.

This translates into the representation of the **party**, with two alternate representations, modelled as attributes: the **legal entity** and the **natural person**.  The reason for making use of the class inheritance model, with Party as a the base type that would be extended by LegalEntity and NaturalPerson, is that the Rosetta model doesn't support downcasting, which was causing issues in some scenarios. This will be further assess at some future point.

The CDM reference data representation will be further expanded once use cases for the model is firmed out.

.. code-block:: Java

 class Party
 {
  id (0..1);
  partyId string (1..*) scheme ;
  legalEntity LegalEntity (0..1);
  naturalPerson NaturalPerson (0..*);
 }

 choice rule Party_choice <"A party is either a legal entity or a natural person.">
  for Party optional choice between
  legalEntity and naturalPerson

 class LegalEntity
 {
  id (0..1);
  entityId string (0..*) scheme ;
  name string (1..1) scheme ;
 }

 class NaturalPerson
 {
  id (0..1);
  honorific string (0..1) ;
  firstName string (1..1) ;
  middleName string (0..*);
  initial string (0..*);
  surname string (1..1) ;
  suffix string (0..1) ;
  dateOfBirth date (0..1) ;
 }

Mapping (Synonym)
-----------------

In order to facilitate the translation of existing industry messages (based on open standards or proprietary ones) into CDM, the CDM is mapped to a set of those alternative data representations using the ``synonym`` feature available in the Rosetta DSL.

The following set of synonym sources are currently in place for the CDM:

* **FpML standard** (synonym source: ``FpML_5_10``): synonyms to the version 5.10 of the FpML standard
* **FIX standard** (synonym source: ``FIX_5_0_SP2``): synonyms to the version 5.0 SP2 of the FIX protocol
* **ISO 20022 standard** (synonym source: ``ISO_20022``): synonyms to the ISO 20022 reporting standard, with no version reference at present
* **Workflow event** (synonym source: ``Workflow_Event``): synonyms to the *event.xsd* schema used internally in Rosetta to ingest sample lifecycle events
* **DTCC** (synonym sources: ``DTCC_11_0`` and ``DTCC_9_0``): synonyms to the *OTC_Matching_11-0.xsd* schema used for trade matching confirmations, and to the *OTC_Matching_9-0.xsd* schema used for payment notifications, both including the imported FpML schema version 4.9.
* **CME** (synonym sources: ``CME_ClearedConfirm_1_17`` and ``CME_SubmissionIRS_1_0``): synonyms to the *cme-conf-ext-1-17.xsd* schema (including the imported FpML schema version 5.0) used for clearing confirmation, and to the *bloombergTradeFixml* schema (including the imported FpML schema version 4.6) used for clearing submission
* **AcadiaSoft** (synonym source: ``AcadiaSoft_AM_1_0``): synonyms to version 1.0 of AcadiaSoft Agreement Manager
* **ISDA Create** (synonym source: ``ISDA_Create_1_0``): synonyms to version 1.0 of the ISDA Create tool for Initial Margin negotiation

Those synonym sources are listed as part of a configuration file in the CDM using a special ``synonym source`` enumeration, so that the synonym source value can be controlled when editing synonyms.

.. _Qualified Type Section: https://docs.rosetta-technology.io/dsl/documentation.html#qualified-type
.. _Function Definition Section: https://docs.rosetta-technology.io/dsl/documentation.html#function-definition
