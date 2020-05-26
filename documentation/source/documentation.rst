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

Regardless of whether the data structure is the same or different from FpML, the CDM includes defined Synonyms that map to FpML (and other models) and can be used for transformation purposes. More details on Synonyms are provided in the Mapping (Synonym) section of this document.

TradableProduct
^^^^^^^^^^^^^^^

A tradable product represents a financial product that is ready to be traded, meaning that there is an agreed financial product, price, quantity, and other details necessary to complete an execution of a security or a negotiated contract.  Tradable products are represented by the ``TradableProduct`` type. 

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
    
The ``AssetIdentifier`` type requires the specification of either a product, currency or a floating rate option. This choice constraint is supported by specifying a ``one-of`` condition, as described in the `Special Syntax Section`_ of the Rosetta DSL documentation.

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
    
The ``price`` attribute is of type ``Price``, which requires the selection of one of the attributes that describe different types of prices. The set of attributes collectively support all products in the CDM.

.. code-block:: Haskell

 type Price: 
    cashPrice CashPrice (0..1)
    exchangeRate ExchangeRate (0..1)
    fixedInterestRate FixedInterestRate (0..1) 
    // For IR Swaps, CDS, Repo, and FRA
    floatingInterestRate FloatingInterestRate (0..1)
    condition: one-of

For example, ``cashPrice`` would be used to represent the reference price in an Equity Swap and ``fixedInterestRate`` would be used to represent the fixed rate on an Interest Rate Swap. ``floatingInterestRate`` would be used to represent a cap or floor, or could be used to represent the known initial reset rate of a floating leg in an Interest Rate Swap, if it is agreed between the parties as part of the trade.

Financial Product
"""""""""""""""""

A financial product is an instrument that is used to transfer financial risk between two parties. Financial products are represented in the ``Product`` type, which is also constrained by a ``one-of`` condition, meaning that for a single Tradable Product, there can only be one Product.

.. code-block:: Haskell
 
 type Product: 
    [metadata key]
    contractualProduct ContractualProduct (0..1)
    index Index (0..1)
    loan Loan (0..1)
    foreignExchange ForeignExchange (0..1)
    security Security (0..1)
    condition: one-of

The CDM allows any one of these products to included in a trade or used as an underlier for another product (see the *Underlier* section). One unlikely case for a direct trade is Index, which is primarily used as an underlier.

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
  
* **Options**:

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

The CDM specifies the various sets of possible remaining economic terms using the ``EconomicTerms`` type.  This type includes contractual provisions that are not specific to the type of payout, but do impact the value of the contract, such as effective date, termination date, date adjustments, and early termination provisions.  A valid population of this type is constrained by a set of conditions which are not shown here in the interests of brevity.

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
The ``Payout`` type defines the composable payout types, each of which describes a set of terms and conditions for the financial responsibilities between the contractual parties. Payout types can be combined to compose a product.  For example, an Equity Swap can be composed by combining an ``InterestRatePayout`` and an ``EquityPayout``.

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

**Product qualification is inferred from the economic terms of the product** instead of explicitly naming the product type.  The CDM uses a set of Product Qualification functions to achieve this purpose. These functions can be identified as those annotated with ``[qualification Product]``.

A Product Qualification function applies a taxonomy-specific business logic to identify if the product attribute values, as represented by the product's economic terms, match the specified criteria for the product named in that taxonomy. For example, if a certain set of attributes are populated and others are absent, then that specific product type is inferred. The Product Qualification function name in the CDM begins with the word ``Qualify`` followed by an underscore ``_`` and then the product type from the applicable taxonomy  (also separated by underscores).

The CDM implements the ISDA Product Taxonomy v2.0 to qualify contractual products, foreign exchange, and repurchase agreements. Given the prevalence of usage of the ISDA Product Taxonomy v1.0, the equivalent name from that taxonomy is also systematically indicated in the CDM, using a ``synonym`` annotation displayed under the function output. An example is provided below for the qualification of a Zero-Coupon Fixed-Float Inflation Swap:

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

If all the statements above are true, then the function evaluates to True, and the product is determined to be qualified as the product type referenced by the function name.

.. note:: In a typical CDM model implementation, the full set of Product Qualification functions would be invoked against each instance of the product in order to determine the inferred product type. Given the product model composability, a single product instance may be qualified as more than one type: for example in an Interest Rate Swaption, both the Option and the underlying Interest Rate Swap would be qualified.

The CDM supports Product Qualification functions for Credit Derivatives, Interest Rate Derivatives, Equity Derivatives, Foreign Exchange, and Repurchase Agreements. The full scope for Interest Rate Products has been represented down to the full level of detail in the taxonomy. This is shown in the example above, where the ``ZeroCoupon`` qualifying suffix is part of the function name. Credit Default products are qualified, but not down to the full level of detail. The ISDA Product Taxonomy v2.0 references the FpML *transaction type* field instead of just the product features, whose possible values are not publicly available and hence not positioned as a CDM enumeration.

The output of the qualification function is used to populate the ``productQualifier`` attribute of the ``ProductIdentification`` object, which is created when a ``ContractualProduct`` object is created. The product identification includes both the product qualification generated by the CDM and any additional product identification information which may come from the originating document, such as FpML. In this case, taxonomy schemes may be associated to such product identification information, which are also propagated in the ``ProductIdentification`` object.

The ``productIdentification`` data structure and an instance of a CDM object (`serialised`_ into JSON) are shown below:

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

.. code-block:: Java

 "productIdentification" : {
   "primaryAssetdata" : {
     "value" : "INTEREST_RATE",
     "meta" : {
       "scheme" : "http://www.fpml.org/coding-scheme/asset-class-simple"
     }
   },
   "productId" : [ {
     "value" : "InterestRate:IRSwap:FixedFloat",
     "meta" : {
       "scheme" : "http://www.fpml.org/coding-scheme/product-taxonomy"
     }
   } ],
   "productQualifier" : "InterestRate_IRSwap_FixedFloat_PlainVanilla",
   "productType" : [ {
     "value" : "InterestRate:IRSwap:FixedFloat",
     "meta" : {
       "scheme" : "http://www.fpml.org/coding-scheme/product-taxonomy"
     }
   } ]
 }

.. note:: ``productType`` is a *meta-type* that indicates that its value is meant to be populated via a function. This mechanism is explained in the `Qualified Type Section`_ of the Rosetta DSL documentation. For a further understanding of the underlying qualification logic in the Product Qualification, see the explanation of the *object qualification* feature of the Rosetta DSL, as described in the `Function Definition Section`_.

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

Purpose
^^^^^^^

Why a Process Model
"""""""""""""""""""

**The CDM lays the foundation for the standardisation, automation and inter-operability of industry processes**. Industry processes represent events and actions that occur through the transaction's lifecycle, from negotiating a legal agreement to allocating a block-trade or calculating settlement amounts.

While ISDA defines the protocols for industry processes in its library of ISDA Documentation, differences in the implementation minutia may cause operational friction between market participants. Evidence shows that even when calculations are defined in mathematical notation (for example, day count fraction formulae which are used when calculating interest rate payments) can be a source of dispute between parties in a transaction.

What Is the Process Model
"""""""""""""""""""""""""

**The CDM Process Model has been designed to translate the technical standards that support those industry processes** into a standardised machine-readable and machine-executable format.

Machine readability and executability is crucial to eliminate implementation discrepancy between market participants and increase interoperability between technology solutions. It greatly minimises the cost of adoption and provides a blueprint on which industry utilities can be built.

How Does It Work
""""""""""""""""

The process model is systematically translated into executable code for the Java representation of the CDM using purpose-built technology as described in the `Code Generation Section`_. The CDM data model and process model specifications can also be translated into a number of other modern, widely adopted and freely available programming languages:

* Scala
* DAML
* Typescript

The CDM has the capability to add support for other languages as required by market participants. Executable code artefacts in all supported languages are systematically distributed with the CDM and freely available to download from the ISDA CDM `Portal`_.

Scope
^^^^^

The scope of the process model has two dimensions:

#. **Coverage** - which industry processes should be covered.
#. **Granularity** - at which level of detail each process should be specified.

Coverage
""""""""

**The CDM process model currently covers the post-trade lifecycle of securities, contractual products, and foreign exchange**. Generally, a process is in-scope when it is already covered in ISDA Documentation or other technical documents. For example, the following processes are all in scope:

* Trade execution and confirmation
* Clearing
* Allocation
* Settlement (including any future contingent cashflow payment)
* Exercise of options
* Margin calculation
* Regulatory reporting (although covered in a different documentation section)

Granularity
"""""""""""

**It is important for implementors of the CDM to understand the scope of the model** with regard to specifications and executable code for the above list of post-trade lifecycle processes.

The CDM process model leverages the *function* component of the Rosetta DSL. As detailed in the `Function Component Section`_ of the documentation, a function receives a set of input values and applies logical instructions to return an output. The input and output are both CDM objects (including basic types). While a function specifies its inputs and output, its logic may be *fully defined* or only *partially defined* depending on how much of the output's attribute values it builds. Unspecified parts of a process represent functionality that firms are expected to implement, either internally or through third-parties such as utilities.

It is not always possible or practical to fully specify the business logic of a process from a model. Parts of processes or sub-processes may be omitted from the CDM for the following reasons:

* The sub-process is not needed to create a functional CDM output object.
* The sub-process has already been defined and its implementation is widely adopted by the industry.
* The sub-process is specific to a firm's internal process and therefore cannot be specified in an industry standard.

Given these reasons, the CDM process model focuses on the most critical data and processes required to create functional objects that satisfy the below criterion:

* All of the qualifiable constituents (such as ``BusinessEvent`` and ``Product``) of a function's output can be qualified, which means that they evaluate to True according to at least one of the applicable Qualification functions.
* Lineage and cross-referencing between objects is accurate for data integrity purposes.

For any remaining data or processes, implementors can populate the remaining attribute values required for the output to be valid by extending the executable code generated by the process model or by creating their own functions.

For the trade lifecycle processes that are in scope, the CDM process model covers the following sub-process components, which are each detailed in the next sections:

#. Validation process
#. Calculation process
#. Event creation process


Validation Process
^^^^^^^^^^^^^^^^^^

In many legacy models and technical standards, validation rules are generally specified in text-based documentation, which requires software engineers to evaluate and translate the logic into code. The frequently occuring result of this human interpretation process is inconsistent enforcement of the intended logic.

By contrast, in the CDM, validation components are an integral part of the process model specifications and are distributed as executable code in the Java representation of the CDM. The CDM validation components leverage the validation components of the Rosetta DSL, as described in the `Validation Component Section`_.

As an example, the *FpML ird validation rule #57*, states that if the calculation period frequency is expressed in units of month or year, then the roll convention cannot be a weekday. A machine readable and executable definition of that specification is provided in the CDM, as a ``condition`` attached to the ``CalculationPeriodFrequency`` type:

.. code-block:: Haskell

 condition FpML_ird_57: <"FpML validation rule ird-57 - Context: CalculationPeriodFrequency. [period eq ('M', 'Y')] not(rollConvention = ('NONE', 'SFE', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT','SUN')).">
   if period = PeriodExtendedEnum -> M or period = PeriodExtendedEnum -> Y
   then rollConvention <> RollConventionEnum -> NONE
     or rollConvention <> RollConventionEnum -> SFE
     or rollConvention <> RollConventionEnum -> MON
     or rollConvention <> RollConventionEnum -> TUE
     or rollConvention <> RollConventionEnum -> WED
     or rollConvention <> RollConventionEnum -> THU
     or rollConvention <> RollConventionEnum -> FRI
     or rollConvention <> RollConventionEnum -> SAT
     or rollConvention <> RollConventionEnum -> SUN


Calculation Process
^^^^^^^^^^^^^^^^^^^

The CDM provides certain ISDA Definitions as machine executable formulas to standardise the industry calculation processes that depend on those definitions.  Examples include the ISDA 2006 definitions of *Fixed Amount* and *Floating Amount* , the ISDA 2006 definitions of Day Count Fraction rules, and performance calculations for Equity Swaps. The CDM also specifies related utility functions.

These calculation processes leverage the *calculation function* component of the Rosetta DSL, as detailed in the `Function Definition Section`_, and accordingly are associated to a ``calculation`` annotation.

Explanations of these processes are provided in the following sections.

Fixed Amount and Floating Amount Definitions
""""""""""""""""""""""""""""""""""""""""""""

The CDM expressions of ``FixedAmount`` and ``FloatingAmount`` are similar in structure: a calculation formula that reflects the terms of the ISDA 2006 Definitions and the arguments associated with the formula.

.. code-block:: Haskell

 func FloatingAmount:
   [calculation]
   inputs:
     interestRatePayout InterestRatePayout (1..1)
     rate FloatingInterestRate (1..1)
     quantity NonNegativeQuantity (1..1)
     date date (1..1)
   output: floatingAmount number (1..1)
   
   alias calculationAmount: quantity -> amount
   alias floatingRate: ResolveRateIndex( interestRatePayout -> rateSpecification -> floatingRate -> assetIdentifier -> rateOption -> floatingRateIndex )
   alias spreadRate: rate -> spread
   alias dayCountFraction: DayCountFraction(interestRatePayout, interestRatePayout -> dayCountFraction, date)
   
   assign-output floatingAmount: calculationAmount * (floatingRate + spreadRate) * dayCountFraction

Day Count Fraction
""""""""""""""""""

The CDM process model incorporates calculations that represent the set of day count fraction rules specified as part of the ISDA 2006 Definitions, e.g. the *ACT/365.FIXED* and the *30E/360* day count fraction rules. Although these rules are widely accepted in international markets, many of them have complex nuances which can lead to inconsistent implementations and potentially mismatched settlements.

For example, there are three distinct rule sets in which the length of each month is generally assumed to be 30 days for accrual purposes (and each year is assumed to be 360 days). However there are nuances in the rule sets that distinquish the resulting calculations under different circumstances, such as when the last day of the period is the last day of February. These distinct rule sets are defined by ISDA as 30/360 (also known as 30/360 US), 30E/360 (formerly known as 30/360 ICMA or 30/360 Eurobond), and the 30E/360.ISDA.

The CDM process model eliminates the need for implementators to interpret the logic and write unique code for these rules. Instead, it provides a machine-readable expression that generates executable code, such as the example below:

.. code-block:: Haskell

 func DayCountFraction(dayCountFractionEnum: DayCountFractionEnum -> _30E_360):
   [calculation]
   
   alias calculationPeriod: CalculationPeriod(interestRatePayout -> calculationPeriodDates, date)
   alias startYear: calculationPeriod -> startDate -> year
   alias endYear: calculationPeriod -> endDate -> year
   alias startMonth: calculationPeriod -> startDate -> month
   alias endMonth: calculationPeriod -> endDate -> month
   alias endDay: Min(calculationPeriod -> endDate -> day, 30)
   alias startDay: Min(calculationPeriod -> startDate -> day, 30)
   
   assign-output result:
     (360 * (endYear - startYear) + 30 * (endMonth - startMonth) + (endDay - startDay)) / 360

Utility Function
""""""""""""""""

CDM elements often need to be transformed by a function to construct the arguments for a formula in a calculation. A typical example is the requirement to identify a period start date, end date, and other date-related attributes required to compute a cashflow amount in accordance with a schedule (as illustrated in the day count fraction calculation shown above). The CDM has two main types to address this requirement:

* ``CalculationPeriodDates`` specifies the inputs required to construct a calculation period schedule
* ``CalculationPeriodData`` specifies actual attribute values of a calculation period such as start date, end date, etc.

The CalculationPeriod functon receives the ``CalculationPeriodDates`` and the current date as the inputs and produces the ``CalculationPeriodData` as the output, as shown below:

.. code-block:: Haskell

 func CalculationPeriod:
   inputs:
     calculationPeriodDates CalculationPeriodDates (1..1)
     date date (1..1)
   output: result CalculationPeriodData (1..1)

Equity Performance
""""""""""""""""""

The CDM process model includes calculations to support the equity performance concepts applied to reset and pay cashflows on Equity Swaps. Those calculations follow the definitions as normalised in the new *2018 ISDA CDM Equity Confirmation for Security Equity Swap* (although this is a new template that is not yet in use across the industry).

Some of those calculations are presented below:

.. code-block:: Haskell

 func EquityCashSettlementAmount: <"Part 1 Section 12 of the 2018 ISDA CDM Equity Confirmation for Security Equity Swap, Para 72. 'Equity Cash Settlement Amount' means, in respect of an Equity Cash Settlement Date, an amount in the Settlement Currency determined by the Calculation Agent as of the Equity Valuation Date to which the Equity Cash Settlement Amount relates, pursuant to the following formula: Equity Cash Settlement Amount = ABS(Rate Of Return) × Equity Notional Amount.">
   inputs:
     contractState ContractState (1..1)
     date date (1..1)
   output:
     equityCashSettlementAmount Money (1..1)
   
   alias equityPayout: contractState -> contract -> tradableProduct -> product -> contractualProduct -> economicTerms -> payout -> equityPayout
   
   condition: equityPayout -> payoutQuantity -> assetIdentifier -> productIdentifier = equityPayout -> underlier -> underlyingProduct -> security -> equity -> productIdentifier
   
   assign-output equityCashSettlementAmount -> amount:
     Abs(contractState -> updatedContract -> tradableProduct -> product -> contractualProduct -> economicTerms -> payout -> equityPayout only-element -> performance)
   assign-output equityCashSettlementAmount -> currency:
     ResolveEquityInitialPrice( equityPayout only-element -> underlier, contractState -> contract -> tradableProduct -> priceNotation ) -> netPrice -> currency

.. code-block:: Haskell

 func RateOfReturn: <"Part 1 Section 12 of the 2018 ISDA CDM Equity Confirmation for Security Equity Swap, Para 139. 'Rate Of Return' means, in respect of any Equity Valuation Date, the amount determined pursuant to the following formula: Rate Of Return = (Final Price - Initial Price) / Initial Price.">
   inputs:
     initialPrice number (1..1)
     finalPrice number (1..1)
   output:
     rateOfReturn number (1..1)
   
   assign-output rateOfReturn:
     (finalPrice - initialPrice) / initialPrice

Event Creation Process
^^^^^^^^^^^^^^^^^^^^^^

(*Coming soon*)


Reference Data Model
--------------------

The CDM only integrates the reference data components that are specifically needed to model the in-scope products, events, legal agreements and function components.

This translates into the representation of the **party**, with two alternate representations, modelled as attributes: the **legal entity** and the **natural person**.  The reason for making use of the class inheritance model, with Party as a the base type that would be extended by LegalEntity and NaturalPerson, is that the Rosetta model doesn't support downcasting, which was causing issues in some scenarios. This will be further assess at some future point.

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

In order to facilitate the translation of existing industry messages (based on open standards or proprietary ones) into CDM, the CDM is mapped to a set of those alternative data representations using the Rosetta DSL *synonym* feature, as described in the `Mapping Component Section`_.

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
.. _Function Component Section: https://docs.rosetta-technology.io/dsl/documentation.html#function-component
.. _Code Generation Section: https://docs.rosetta-technology.io/dsl/codegen-readme.html
.. _Validation Component Section: https://docs.rosetta-technology.io/dsl/documentation.html#validation-component
.. _Mapping Component Section: https://docs.rosetta-technology.io/dsl/documentation.html#mapping-component
.. _Special Syntax Section: https://docs.rosetta-technology.io/dsl/documentation.html#special-syntax
.. _serialised: https://en.wikipedia.org/wiki/Serialization

.. _Portal: https://portal.cdm.rosetta-technology.io
.. _function coverage matrix: Portal
.. _data modelling: https://en.wikipedia.org/wiki/Cardinality_(data_modeling)#Application_program_modeling_approaches
