The Common Domain Model
=======================

**There are six modelling dimensions** to the CDM:

* Product
* Event
* Legal Agreement
* Process
* Reference Data
* Mapping (Synonym)

The following sections define each of these dimensions. Selected examples of model definitions are used as illustrations to help explain each dimension and include, where applicable, data samples to help demonstrate the structure. All the Rosetta DSL modelling components that are used to express the CDM are described in the `Rosetta DSL Documentation`_

The complete model definition, including descriptions and other details can be viewed in the `Textual Browser <https://portal.cdm.rosetta-technology.io/#/text-browser>`_ on the ISDA CDM Portal.

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

**The product qualification is inferred from the product's economic terms through qualification logic** that is predicated on the product's model components. The qualification logic leverages the *object qualification* feature of the Rosetta DSL described in the `Function Definition Section`_

The CDM makes use of the ISDA taxonomy V1.0 leaf level to qualify the product. 25 product types across interest rate swap and foreign exchange have so far been qualified in the CDM, in effect representing the full ISDA V1.0 scope. The current CDM implementation does not qualify credit default swap products, as the ISDA taxonomy V1.0 references the transaction type instead of the product features. Those values are not publicly available and hence not positioned as a CDM enumeration.

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

**The CDM event model currently covers the post-trade lifecycle of over-the-counter transactions**. Although a non-exhaustive list, the following *business events* are all in scope:

* Trade execution and confirmation
* Clearing
* Allocation
* Settlement (including any future contingent cashflow payment)
* Exercise of options

The CDM event model follows the same composition principle as the product model:

* **Business events are specified by composition** of *primitive events*, which use a large set of the FpML event building blocks.
* **Business event qualification is inferred** from those primitive event components and, in some relevant cases, from an *intent* qualifier associated with the business event.

The CDM event model is supported by four main components, which are detailed in the sections below:

* Trade State
* Primitive event
* Business (i.e. trade lifecycle) event
* Workflow


Trade State
^^^^^^^^^^^

When market participants transact with each other in financial products, a *trade state* is associated to the underlying product of that transaction at each stage in the transaction lifecycle, from execution to settlement to maturity. The trade lifecycle events that are represented by the CDM Event Model describe the *state-transition* events that impact the trade state.

The CDM state-transition model is based on the following design principles:

* **The state is trade-specific**, not product-specific (i.e. it is not an asset-servicing model). The same product may be associated to infinitely many trades, each with its own specific state, between any two parties.
* **A lifecycle event describes a change in the state of a trade**, i.e. there must be different before/after trade states based on that lifecycle event.
* **The product definition that underlies the transaction remains immutable**, unless agreed (negotiated) between the parties to that transaction as part of a specific trade lifecycle event. Automated events, for instance resets or cashflow payments, should not alter the product definition.
* **The trade state can be reconstructed at any point in the trade lifecycle**, i.e. the state-transition model implements a *lineage* concept.

The trade state is currently described in the CDM by the ``Trade`` type. It always contains a ``TradableProduct`` object, which is used to define the economic terms of the financial product that has been traded between the parties. The trade state can be either an ``execution`` or a ``contract``, as controlled by the ``one-of`` condition:

.. code-block:: Haskell

 type Trade: <"A class to represent the general trade concept, which can either be an execution or a contract. The execution consists essentially in the economic terms which are agreed between the parties. The contract will further qualify those with the legal entities (think of the allocation case, which execution state can involve the investment adviser rather not the actual funds) while not specify the master agreement or collateral terms which might be associated with the subsequent contract.">
   [metadata key]
   execution Execution (0..1) <"The execution corresponds to economic terms that are agreed between parties, but which legal terms are not yet specified. The execution attribute applies to the post-execution scenario of a product that is subject to the clearing mandate and is then routed to the CCP as an execution.">
   contract Contract (0..1) <"The contract differs from the execution by the fact that its legal terms are fully specified. This includes the legal entities that are associated to it as well as any associated legal agreement, e.g. master agreement, credit and collateral terms, ... ">
   condition Trade: one-of

Those two types are detailed in the sections below.

.. note:: The ``TradableProduct`` type is further detailed in the `Tradable Product Section`_ of the documentation.

Execution
"""""""""

The current CDM event model only covers post-trade lifecycle events, so the first step in instantiating a transaction between two parties begins with an *execution* state. In addition to the tradable product, the ``Execution`` type includes attributes such as the trade date, transacting parties, execution venue (if any) and settlement terms to describe an execution.

.. code-block:: Haskell

 type Execution: <" A class to specify an execution, which consists essentially in the economic terms which are agreed between the parties, alongside with the qualification of the type of execution. The associated globalKey denotes the ability to associate a hash value to the respective Execution instantiations for the purpose of model cross-referencing, in support of functionality such as the event effect and the lineage.">
   [metadata key]
   executionType ExecutionTypeEnum (1..1) <"Specifies the type of execution, e.g. via voice, electronically...">
   executionVenue LegalEntity (0..1) <"The execution venue identification, when applicable.">
   identifier Identifier (1..*) <"The identifier(s) associated with the execution.">
   tradeDate date (1..1) <"The trade/execution date.">
   [metadata id]
   tradableProduct TradableProduct (1..1) <"The product traded as part of this execution, including quantity and price">
   party Party (0..*) <"The party reference is optional because positioned as part of the Event class when the execution is specified as part of such context.">
   [metadata reference]
   partyRole PartyRole (0..*) <"The role(s) that party(ies) may have in relation to the execution, further to the principal parties (i.e payer/receive or buyer/seller) to it.">
   closedState ClosedState (0..1) <"The qualification of what led to the execution closure alongside with the dates on which this closure takes effect.">
   settlementTerms SettlementTerms (0..1) <"The execution settlement terms, which is applicable for products such as securities">

The ``settlementTerms`` attribute define how the transaction should be settled (including the settlement date), for instance in a *delivery-versus-payment* scenario for a cash security transaction or a *payment-versus-payment* scenario for an FX spot or forward transaction. The actual settlement amount(s) will need to use the *price* and *quantity* agreed as part of the tradable product.

.. code-block:: Haskell

 type SettlementTerms extends SettlementBase: <"A class to specify the settlement terms. This class reflects the FpML OptionSettlement.model, although with no option reference.">
   settlementType SettlementTypeEnum (0..1) <"Whether the settlement will be cash, physical, by election, ...">
   settlementDate AdjustableOrRelativeDate (0..1)
   valueDate date (0..1) <"The settlement date for a forward settling product. For Foreign Exchange contracts, this represents a common settlement date between both currency legs. To specify different settlement dates for each currency leg, see the ForeignExchange class. This attribute is meant to be merged with the 'settlementDate' at some future point noce we refactor 'Date' to use a single complex type across the model.">
   settlementAmount Money (0..1) <"The Settlement Amount, when known in advance.">
   transferSettlementType TransferSettlementEnum (0..1) <"The qualification as to how the transfer will settle, e.g. a DvP settlement.">

Post-Execution: Contract
""""""""""""""""""""""""

For a contractual product, once a transaction has been agreed between the parties, a contract gets executed between the contractual legal entities for that transaction. In addition to the tradable product economics, a contract has a set of attributes which are only qualified at the post-execution stage: calculation agent, documentation, governing law, etc.

.. code-block:: Haskell

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

The ``Contract`` type incorporates all the elements that are part of the FpML *trade confirmation* view (with the exception of: *tradeSummary*, *originatingPackage*, *allocations* and *approvals*), whereas the ``TradableProduct`` type corresponds to the *pre-trade* view in FpML.


Primitive Event
^^^^^^^^^^^^^^^

Primitive events are the building block components used to specify business events in the CDM. They describe the fundamental state-transition components that may impact the trade state during its lifecycle.

The current list of primitive events can be seen in the ``PrimitiveEvent`` type definition:

.. code-block:: Haskell

 type PrimitiveEvent: <"A primitive event is defined by one and only one atomic change in state of a trade. An example of this is a contract formation where the legal terms of the contact are added to the trade. A Primitive event contains a before and after state where the before is a reference to another after state of a primitive event in order to preserve lineage.">
   
   execution ExecutionPrimitive (0..1)
   contractFormation ContractFormationPrimitive (0..1)
   split SplitPrimitive (0..1)
   exercise ExercisePrimitive (0..1)
   inception InceptionPrimitive (0..1)
   observation ObservationPrimitive (0..1)
   quantityChange QuantityChangePrimitive (0..1)
   reset ResetPrimitive (0..1)
   termsChange TermsChangePrimitive (0..1)
   transfer TransferPrimitive (0..1)
   
   condition PrimitiveEvent: one-of

A ``PrimitiveEvent`` object is made of either one of those primitive components, as captured by the ``one-of`` condition. A couple of examples of such primitive event components are illustrated below, using a simple transaction lifecycle sequence.

Example: Observation/Reset/Transfer
"""""""""""""""""""""""""""""""""""

The sequence starts with the *inception* of a new transaction, which results in a ``PostInceptionState`` that contains a ``Contract`` object:

.. code-block:: Haskell

 type InceptionPrimitive: <"The primitive event for the inception of a new contract between parties. It is expected that this primitive will be adjusted or deprecated once the CDM scope is extended to the pre-execution space.">
   before ContractState (0..0) <"The (0..0) cardinality reflects the fact that there is no contract in the before state of an inception primitive. As noted in the definition associated with the class, this is expected to change once the CDM scope is extended to the pre-execution space.">
     [metadata reference]
   after PostInceptionState (1..1) <"The after state corresponds to the new contract between the parties.">

We assume that the trade relies on a future observable value, for which an *observation* then occurs. This observation is provided by some data provider (a.k.a. *market data oracle*) and independently from any specific transaction.

.. code-block:: Haskell

 type ObservationPrimitive: <"A class to specify the primitive object to specify market observation events, which is applicable across all asset classes.">
   source ObservationSource (1..1) <"The observation source, such as an interest rate curve or an information provider.">
   observation number (1..1) <"The observed value.">
   date date (1..1) <"The observation date.">
   time TimeZone (0..1) <"The observation time.">
   side QuotationSideEnum (0..1) <"The side (bid/mid/ask) of the observation, when applicable.">

From that observation, a ``ResetPrimitive`` is built which does affect the specific transaction:

.. code-block:: Haskell

 type ResetPrimitive: <"The primitive event to represent a reset.">
   before ContractState (1..1) <"Contract state before the reset, as per previous events processed on the contract.">
     [metadata reference]
   after ContractState (1..1) <"Contract state after the reset, that embeds the reset value as an updated field on the contract state.">
   condition Contract: <"The original contract in the before/after state of a reset should match.">
     if ResetPrimitive exists
     then before -> contract = after -> contract

A ``TransferPrimitive`` then handle any cashflow (or other types of asset transfer) that the reset may generate.

.. code-block:: Haskell

 type TransferPrimitive: <"A class to specify the transfer of assets between parties, those assets being either cash, securities or physical assets. This class combines components that are cross-assets (settlement date, settlement type, status, ...) and some other which are specialized by asset class (e.g. the payer/receiver amount and cashflow type for a cash transfer, the transferor/transferee, security indication, quantity, and asset transfer type qualification for the case of a security). The associated globalKey denotes the ability to associate a hash value to the respective Execution instantiations for the purpose of model cross-referencing, in support of functionality such as the event effect and the lineage.">
   [metadata key]
   identifier string (0..1) <"The identifier which might be associated with the transfer.">
     [metadata scheme]
   settlementType TransferSettlementEnum (0..1) <"The qualification as to how the transfer will settle, e.g. a DvP settlement.">
   settlementDate AdjustableOrAdjustedOrRelativeDate (1..1)
   cashTransfer CashTransferComponent (0..*) <"The cash transfer component of the transfer. In the case where several currencies are involved in the transfer, several components should be used, as the component supports one single currency amount.">
   securityTransfer SecurityTransferComponent (0..*) <"The security transfer component of the transfer. In the case where several securities are involved in the transfer, several components should be used, as the component supports one single security.">
   commodityTransfer CommodityTransferComponent (0..*)
   status TransferStatusEnum (0..1) <"The transfer status, e.g. Instructed, Settled, ...">
   settlementReference string (0..1) <"The settlement reference, when applicable.">

The CDM has been designed to treat the reset and the transfer primitive events separately because there is no 1-to-1 relationship between reset and transfer.

* Many transfer events are not tied to any reset: for instance, the currency settlement from an FX spot or forward transaction.
* Conversely, not all reset events generate a cashflow: for instance, the single, final settlement that is based on all the past floating rate resets in the case of a compounding floating zero-coupon swap.

Else than the ``ObservationPrimitive`` and ``TransferPrimitive`` above, each primitive event is designed to include a ``before`` and an ``after`` trade state attributes, that define the state transition in terms of evolution in the trade state.

The ``before`` attribute is included as a reference using the ``[metadata reference]`` annotation, because by definition the primitive event points to a state that *already* exists. By contrast, the primitive event includes the full ``after`` state, since it is the occurence of that primitive event that transitions to a new state. By tying each state in the lifecycle to a previous state, primitive events are the mechanism by which the *lineage* model is implemented in the CDM.

.. note:: Not all primitive events are currently composed of a ``before`` and ``after`` state defined in terms of the ``Trade`` object. This is subject to review, for potential harmonisation to establish a clean state-transition model in the CDM.


Business Event
^^^^^^^^^^^^^^

Each trade lifecycle event is represented as a collection of primitive event components in the ``BusinessEvent`` type:  

.. code-block:: Haskell

 type BusinessEvent: <"A business event represents a life cycle event of a trade and consists of a series of primitive events. The combination of the state changes results in a qualifiable life cycle event. An example of a Business Event is a PartialTermination which is a defined by a quantity change primitive event.">
   [metadata key]
   [rootType]
   primitives PrimitiveEvent (1..*) <"The elemental component(s) that specify the lifecycle events. Each of the primitive/elemental components listed as part of the PrimitiveEvent class has distinctive features that allow to specify the lifecycle event, either by itself or in combination with some other of such components.">
   intent IntentEnum (0..1) <"The intent attribute is meant to be specified when the event qualification cannot be programmatically inferred from the event features. As a result it is only associated with those primitives that can give way to such ambiguity, the quantityChange being one of those. An example of such is a reduction in the trade notional, which could be interpreted as either a trade correction (unless a maximum period of time post-event is specified as part of the qualification), a partial termination or a portfolio rebalancing in the case of an equity swap. On the other hand, an event such as the exercise is not expected to have an associated intent as there should not be ambiguity.">
   functionCall string (0..1) <"This is placeholder concept for a function call into a calculation that will return an outcome. This concept needs to be further firmed out.">
   eventQualifier eventType (0..1) <"The CDM event qualifier, which corresponds to the outcome of the isEvent qualification logic which qualifies the lifecycle event as a function of its features (e.g. PartialTermination, ClearingSubmission, Novation, ...).">
   eventDate date (1..1) <"The date on which the event is taking place. This is the equivalent of the trade date in the case of an execution or a contract.">
   effectiveDate date (0..1) <"The date on which the event contractually takes effect, when different from the event date.">
   eventEffect EventEffect (0..1) <"The set of effects associated with the lifecycle event, i.e. generated cashflows, contracts (from, say, novation events), listed products (from, say, a bond option exercise event) values and more. Those are represented through a set of globalKey references. This attribute is optional in order to provide implementers with the ability not to make use of this feature.">
   
   // TODO - this needs to be moved/merged into the WorkflowStep
   workflowEventState WorkflowStepState (0..1) <"The event workflow information, i.e. the workflow status, the associated comment and the partyCustomisedWorkflow which purpose is to provide the ability to associate custom workflow information to the CDM.">
   [deprecated]

The only mandatory attributes of a business event are:

* The ``primitives`` attribute, which contains the list of primitive events composing that business event, each representing one and only one fundamental state-transition.
* The event date. The time dimension has been purposely ommitted from the event's attributes. That is because, while a business event has a unique date, several time stamps may potentially be associated to that event depending on when it was submitted, accepted, rejected etc, all of which are *workflow* considerations.

An example composition of the primitive events to represent a complete business event is the *partial novation* of a contract, which comprises:

* an ``InceptionPrimitive`` creates the contract with the novation party. The ``tradeDate`` on the novated portion of the contract should reflect the date of the novation event.
* a ``QuantityChange`` primitive applies to the original contract where the quantity after change is different from 0 (0 would represent the case of a *full novation*).

A business event is *atomic* in the sense that its underlying primitive event constituents cannot happen independently: they either all happen together or they do not happen. In the above partial novation example, the existing trade between the parties must be downsized at the same time as the new trade with the novation party is instantiated.

The other attributes of a business event are presented in the following sections.

Intent Qualification
""""""""""""""""""""

Intent qualification is an enumeration value that represents the intent of a particular business event, e.g. ``Allocation``, ``EarlyTermination``, ``PartialTermination`` etc. It is used as part of the event qualification logic, to disambiguate lifecyle events which may share the same primitive event features. As an example, a reduction in a trade quantity/notional could apply to a correction event or a partial termination (although the timing of such event could also be used to qualify the event).

Function Call
"""""""""""""

An example of a function call is the interpolation function that would be associated with a *derived observation* event, which assembles two observed values (say, a 3 months and a 6 months rate observation) to provide a derived one (say, a 5 months observation).

As part of the current CDM version this function call as been specified as a mere string element. It will be appropriately specified once a machine executable implementation of the ISDA Definitions is developed, as per the `Calculation Process Section`_.

Event Effect
""""""""""""

The event effect attribute corresponds to the set of operational and positional effects associated with a lifecycle event. This information is generated by a post-processor associated to the CDM. The ``eventEffect`` contains a set of pointers (annotated with ``[metadata reference]``) to the relevant objects that are affected by the event. The candidate objects are types that are referenceable with an associated ``key``.

Certain events such as observations do not have any event effect, hence the optional cardinality.

.. code-block:: Haskell

 type EventEffect: <"The set of operational and positional effects associated with a lifecycle event, alongside the reference to the contract reference(s) that is subject to the event (and is positioned in the before state of the event primitive).">
   effectedContract Contract (0..*) <"A pointer to the contract(s) to which the event effect(s) apply, i.e. in the before event state.">
     [metadata reference]
   effectedExecution Execution (0..*) <"A pointer to the execution(s) to which the event effect(s) apply, i.e. in the before event state.">
     [metadata reference]
   contract Contract (0..*) <"A pointer to the contract effect(s), an example of such being the outcome of a new trade, swaption exercise or novation event.">
     [metadata reference]
	execution Execution (0..*) <"A pointer to the execution effect(s), an example of such being a clearing submission event when taking place on the back of an execution.">
    [metadata reference]
  productIdentifier ProductIdentifier (0..*) <"A pointer to the product identifier effect(s), an example of such being the outcome of the physical exercise of a bond option.">
    [metadata reference]
  transfer TransferPrimitive (0..*) <"A pointer to the transfer effect(s), either a cash, security or other asset.">
    [metadata reference]

In the below JSON snippet of a quantity change event on a contract, we can see that the ``eventEffect`` contains a  number of hash value references:

.. code-block:: Java
  
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

Other Misc. Information
"""""""""""""""""""""""

* The effective date is optional as not applicable to certain events (e.g. observations), or may be redundant with the event date.
* The event qualifier attribute is derived from the event qualification features. This is further detailed in the `Event Qualification Section`_.

Workflow
^^^^^^^^

A *workflow* is meant to represent a set of actions or steps that are required to trigger a business event. A workflow may involve multiple parties in addition to the parties to the transaction, and may include automated and manual steps. A workflow is organised into a sequence and each step is represented by a *workflow step*.

.. code-block:: Haskell

 type WorkflowStep:
   [metadata key]
   [rootType]
   businessEvent BusinessEvent (0..1)
   proposedInstruction Instruction (0..1)
   // TODO - This will be replaced with a list of signatories that must sign off the 'previousWorkflowStep' and any operational reasons why it may be rejected.
   rejected boolean (0..1)
   previousWorkflowStep WorkflowStep (0..1)
     [metadata reference]
   messageInformation MessageInformation (0..1)
   timestamp EventTimestamp (1..*)
   eventIdentifier Identifier (1..*)
   action ActionEnum (1..1)
   party Party (0..*)
   account Account (0..*)
   lineage Lineage (0..1)

.. note:: This type has been introduced following a refactoring of business events in the CDM, to separate workflow considerations from the business event itself. As a result, a number of attributes that were previously part of the ``Event`` type have been moved to the ``WorkflowStep`` type.

The different attributes of a workflow step are detailed in the sections below.

Business Event
""""""""""""""

This attribute specifies the business event that the workflow step is meant to generate. It is optional because the workflow may require a number of interim steps before the state-transition embedded within the business event becomes effective, so the business event does not exist yet in those steps. The business event attribute is typically associated to the final step in the workflow.

(*To be completed: clearing example*)

Proposed Instruction
""""""""""""""""""""

This attribute allows to specify any additional input, other than the current trade state, that is required to generate the state-transition. It is also optional because typically applicable to the early step of a workflow, when instructions for a new business event are being proposed and the event is not effective yet. Validation components are in place to check that the ``businessEvent`` and ``proposedInstruction`` attributes are mutually exclusive.

(*To be completed: allocation example*)

Previous Workflow Step
""""""""""""""""""""""

This attribute, which is provided as a reference, allows to define the lineage between steps in a workflow. It is used to provide an audit trail for a business event, which can trace back the various steps leading to that business event being triggered.

Action
""""""

The action enumeration qualification specifies whether the event is a new one or a correction or cancellation of a prior one.

Message Information
"""""""""""""""""""

The ``messageInformation`` attribute corresponds to some of the components of the FpML *MessageHeader.model*.

.. code-block:: Haskell

 type MessageInformation:
   messageId string (1..1)
     [metadata scheme]
   sentBy string (0..1)
     [metadata scheme]
   sentTo string (0..*)
     [metadata scheme]
   copyTo string (0..*)
     [metadata scheme]

``sentBy``, ``sentTo`` and ``copyTo`` information is optional, as possibly not applicable in a all technology contexts (e.g. in case of a distributed architecture).

Timestamp
"""""""""

The CDM adopts a generic approach to represent timestamp information, consisting of a ``dateTime`` and a ``qualification`` attributes, with the latter specified through an enumeration value.

.. code-block:: Haskell

 type EventTimestamp:
   dateTime zonedDateTime (1..1)
   qualification EventTimestampQualificationEnum (1..1)

The experience of mapping the CME clearing and the DTCC trade matching and cashflow confirmation transactions to the CDM did reveal a diverse set of timestamps. The expected benefits of the CDM generic approach are twofold:

* It allows for flexibility in a context where it would be challenging to mandate which points in the process should have associated timestamps.
* Gathering all of those in one place in the model allows for evaluation and rationalisation down the road.

Below is (serialised) JSON representation of this approach.

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

Event Identifier
""""""""""""""""

The CDM approach consists in using a common identifier component across products and events. The event identifier information comprises the ``assignedIdentifier`` and an ``issuer``, which may be provided as a reference or via a scheme.

.. code-block:: Haskell

 type Identifier:
   [metadata key]
   issuerReference Party (0..1)
     [metadata reference]
   issuer string (0..1)
     [metadata scheme]
   assignedIdentifier AssignedIdentifier (1..*)
   
   condition IssuerChoice:
     required choice issuerReference, issuer

.. note:: FpML also uses an event identifier construct: the ``CorrelationId``, but it is distinct from the identifier associated with the trade itself, which comes in different variations: ``PartyTradeIdentifier``, with the ``TradeId`` and the ``VersionedTradeId`` as sub-components).

Other Misc. Attributes
""""""""""""""""""""""

* The ``party`` and ``account`` information are optional because not applicable to certain events.
* The ``lineage`` attribute was previously used to reference an unbounded set of contracts, events and/or payout components, that an event may be associated to.

.. note:: The ``lineage`` attribute is superseded by the implementation in the CDM of: (i) trade state lineage, via the ``before`` / ``after`` attributes in the primitive event component, and (ii) workflow lineage, via the ``previousWorkflowStep`` attribute.


Event Qualification
^^^^^^^^^^^^^^^^^^^

**The CDM qualifies lifecycle events as a function of their primitive event components** rather than explicitly naming the event type. The CDM uses the same approach for event qualification as for product qualification, which is based on a set of Event Qualification functions. These functions can be identified as those annotated with ``[qualification BusinessEvent]``.

Event Qualification functions apply a taxonomy-specific business logic to identify if the state-transition attributes values, which are embedded in the primitive event components, match the specified criteria for the event named in that taxonomy. Like Product Qualification functions, the Event Qualification function name begins with the word ``Qualify`` followed by an underscore ``_`` and then the taxonomy name.

One distinction with the product approach is that the ``intent`` qualification is also deemed necessary to complement the primitive event information in certain cases. To this effect, the Event Qualification function allows to specify that when present, the intent must have a specified value, as illustrated by the below example.

.. code-block:: Haskell

 func Qualify_Termination:
   [qualification BusinessEvent]
   inputs:
     businessEvent BusinessEvent(1..1)
   output: is_event boolean (1..1)
     assign-output is_event:
   
   (businessEvent -> intent is absent or businessEvent -> intent = IntentEnum -> Termination)
   and (
     businessEvent -> primitives count = 1
     and businessEvent -> primitives -> quantityChange exists
     or (
       businessEvent -> primitives -> quantityChange exists
       and businessEvent -> primitives -> transfer -> cashTransfer exists
     )
   )
   and QuantityDecreasedToZero(businessEvent -> primitives -> quantityChange) = True
   and businessEvent -> primitives -> quantityChange -> after -> contract -> closedState -> state = ClosedStateEnum -> Terminated

If all the statements above are true, then the function evaluates to True. In this case, the event is determined to be qualified as the event type referenced by the function name.

The CDM makes use of the ISDA taxonomy V2.0 leaf level to qualify the event. 22 lifecycle events have currently been qualified as part of the CDM.

The output of the qualification function is used to populate the ``eventQualifier`` attribute of the ``BusinessEvent`` object, similar to how product qualification works.

.. note:: ``eventType`` is a *meta-type* that indicates that its value is meant to be populated via a function. This mechanism is explained in the `Qualified Type Section`_ of the Rosetta DSL documentation. For a further understanding of the underlying qualification logic in the Product Qualification, see the explanation of the *object qualification* feature of the Rosetta DSL, as described in the `Function Definition Section`_.


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

While ISDA already defines how industry processes should work in the ISDA Documentation, differences in the implementation minutia may still cause operational friction between market participants. Evidence shows that even calculations defined in mathematical notation (for example, day count fraction formulae which are used when calculating interest rate payments) can be a source of dispute between parties in a transaction.

What Is the Process Model
"""""""""""""""""""""""""

**The CDM Process Model has been designed to translate the technical standards that support those industry processes** into a standardised machine-readable and machine-executable format.

Machine readability and executability is crucial to eliminate implementation discrepancy between market participants and increase interoperability between technology solutions. It greatly minimises the cost of adoption and provides a blueprint on which industry utilities can be built.

How Does It Work
""""""""""""""""

The process model is systematically translated into executable code using purpose-built technology as described in the `Code Generation Section`_. A number of modern, widely adopted and freely available programming languages are currently supported:

* Java
* Scala
* DAML
* Typescript

and the list will continue to grow with the requirements of the CDM user community.

Executable code artefacts are distributed with the CDM and freely available to download from the ISDA CDM `Portal`_.

Scope
^^^^^

The scope of the process model has two dimensions:

#. **Coverage** - which industry processes should be covered.
#. **Granularity** - at which level of detail each process should be specified.

Coverage
""""""""

**The CDM process model covers the processes that are in scope of the ISDA documentation and other technical documents**. The trade lifecycle events listed in the `Event Model Section`_ are all in scope of the process model. For an up-to-date model coverage of those events, please refer to the `function coverage matrix`_ (*coming soon*).

In addition to those trade lifecycle events, the following processes are also in scope:

* Margin calculation and collateral management
* Regulatory reporting (although covered in a different documentation section)


Granularity
"""""""""""

**It is important for implementors of the CDM to understand what the model does and does not specify** regarding the above list of post-trade lifecycle processes.

The CDM process model leverages the *function* component of the Rosetta DSL. As detailed in the `Function Component Section`_ of the documentation, a function takes a set of input values and applies some logical instructions to return an output, both of which may be CDM objects or basic types. While a function specifies at minimum its inputs and output, its logic may be *fully defined* or only *partially defined* depending on how much of the output's attribute values it builds. Unspecified parts of a process represent functionality that firms are expected to implement, either internally or through third-parties such as utilities.

It is not always possible or desirable to fully specify the business logic of a process from a model. Parts of processes or sub-processes may be omitted from the CDM for the following reasons:

* The sub-process is not needed to create a functional CDM output object.
* The sub-process has already been defined and its implementation is widely adopted by the industry.
* The sub-process is specific to a firm's internal process and therefore cannot be specified in an industry standard.

The CDM process model focuses on what is necessary to create functional objects that satisfy the below criterion:

* Any of the object's qualifiable constituents (such as ``BusinessEvent`` and ``Product``) can be qualified.
* Lineage and cross-referencing between objects is accurate for data integrity purposes.

Implementors must populate the remaining attribute values required for the output to be valid, by extending the executable code generated by the process model in their implementation.

For the trade lifecycle processes in scope, the CDM process model covers the following sub-process components, which are each detailed in the next sections:

#. Validation process
#. Calculation process
#. Event creation process


Validation Process
^^^^^^^^^^^^^^^^^^

While validation rules are generally specified for existing data standards like FpML alongside the standard documentation, the logic needs to be evaluated and translated into code by software engineering teams. It often results in the validation logic not being consistently enforced.

By contrast, validation components are an integral part of the CDM process model and distributed as executable code. Those CDM validation components leverage the validation components of the Rosetta DSL, as described in the `Validation Component Section`_.

Product Validation
""""""""""""""""""

As an example, the ``FpML_ird_57`` data rule implements the **FpML ird validation rule #57**, which states that if the calculation period frequency is expressed in units of month or year, then the roll convention cannot be a week day. A machine readable and executable definition of that specification is provided in the CDM, as a ``condition`` attached to the ``CalculationPeriodFrequency`` type:

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

Event Validation
""""""""""""""""

(*Coming soon*)

Calculation Process
^^^^^^^^^^^^^^^^^^^

The CDM provides certain ISDA Definitions as machine executable formulas to standardise the industry calculation processes that use those definitions. The ISDA 2006 definitions of **Fixed Amount** and **Floating Amount** have been used as an initial scope to confirm applicability, alongside some of the required day count fractions. Performance calculations are also being introduced in the CDM to support the Total Return Swap model used in particular for Equity.

Those calculation processes leverage the *calculation function* component of the Rosetta DSL, as detailed in the `Function Definition Section`_, and accordingly are associated to a ``calculation`` annotation.

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

The current CDM version incorporates day count fractions calculations representing the set of day count fractions specified as part of the ISDA 2006 Definitions, e.g. the *ACT/365.FIXED* and the *30E/360* day count fractions. While the ACT/365.FIXED definition is simple and relies upon a computation of the number of days in a period, the 30E/360 definition specifies the actual computation in details to account for a 360 days year and a 30 maximum days month.

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

CDM model elements often need to be transformed by a function to construct the arguments for a formula in a calculation. A typical example required to compute a cashflow amount in accordance with a schedule (as illustrated in the day count fraction calculation shown above) is to identify the characteristics of the current calculation period.

The CDM has two main types for this:

* ``CalculationPeriodDates`` specifies the inputs required to construct a calculation period schedule
* ``CalculationPeriodData`` specifies actual attribute values of a calculation period such as start date, end date, etc.

From this data model, a function is required to compute the latter based on the former (and also the current date):

.. code-block:: Haskell

 func CalculationPeriod:
   inputs:
     calculationPeriodDates CalculationPeriodDates (1..1)
     date date (1..1)
   output: result CalculationPeriodData (1..1)

Equity Performance
""""""""""""""""""

To support the implementation of Equity Swaps in CDM, calculations have been introduced to support the equity performance concepts used to reset and pay cashflows on such contracts. Those calculations follow the definitions as normalised in the new **2018 ISDA CDM Equity Confirmation for Security Equity Swap** (although this is a new template that is not yet in use across the industry).

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

Lifecycle Event Process
^^^^^^^^^^^^^^^^^^^^^^^

While the lifecycle event model described in the `Event Model Section`_ provides a standardised data representation of those events using the concept of *primitive event* components, the CDM must further specify the processing of those events to ensure standardised implementations across the industry. This means specifying the *logic* of the state-transition as described by each primitive event component.

In particular, the CDM must ensure that:

* The lifecycle event process model constructs valid CDM event objects.
* The constructed events qualify according to the qualification logic described in the `Event Qualification Section`_.
* The lineage between states allows an accurate reconstruction of the trade's lifecycle sequence.

There are three levels of function components in the CDM to define the processing of lifecycle events:

#. Primitive creation
#. Event creation
#. Workflow step creation

Each of those components can leverage any calculation or utility function defined in the CDM, as described in the `Calculation Process Section`_. An object validation step, such as those described in the `Validation Process Section`_, is included in all these object creation functions to ensure that they each construct valid CDM objects.

Illustration of the three components are given in the sections below.

Primitive Creation
""""""""""""""""""

Primitive creation functions can be thought of as the fundamental mathematical operators that operate on a trade *state*. While a primitive event object describes each state transition in terms of *before* and *after* states, a primitive creation function defines the logic to transition from that *before* state to the *after* state, using a set of *instructions*.

An example of such use is the handling of a reset event, hereby presented an an equity reset example. The reset is processed in two steps:

* An ``ObservationPrimitive`` is built for the equity price, independently from any single contract.
* This observation is used to construct a ``ResetPrimitive`` on any contract affected by it.

For the observation primitive, checks are performed on the valuation date and/or time inputs and on their consistency with a given price determination method. The function to fetch the equity price is also specified to ensure integrity of the observation number.

.. code-block:: Haskell

 func EquityPriceObservation: <"Function specification for the observation of an equity price, based on the attributes of the 'EquityValuation' class.">
   inputs:
     equity Equity (1..1)
     valuationDate AdjustableOrRelativeDate (1..1)
     valuationTime BusinessCenterTime (0..1)
     timeType TimeTypeEnum (0..1)
     determinationMethod DeterminationMethodEnum (1..1)
   output:
     observation ObservationPrimitive (1..1)
   
   condition: <"Optional choice between directly passing a time or a timeType, which has to be resolved into a time based on the determination method.">
     if valuationTime exists then timeType is absent
     else if timeType exists then valuationTime is absent
     else False
     
   post-condition: <"The date and time must be properly resolved as attributes on the output.">
     observation -> date = ResolveAdjustableDate(valuationDate)
     and if valuationTime exists
       then observation -> time = TimeZoneFromBusinessCenterTime(valuationTime)
       else observation -> time = ResolveTimeZoneFromTimeType(timeType, determinationMethod)
       
   post-condition: <"The number recorded in the observation must match the number fetched from the source.">
     observation -> observation = EquitySpot(equity, observation -> date, observation -> time)

The observation is used as an input to *resolve* any Equity Derivative contract (i.e. update its resettable values) that depends on this observation:

.. code-block:: Haskell

 func ResolveEquityContract: <"Specifies how the updated contract should be constructed in a Equity Reset event.">
   inputs:
     contractState ContractState (1..1)
     observation ObservationPrimitive (1..1)
     date date (1..1)
   output:
     updatedContract Contract (1..1)
     
   alias price: observation -> observation
   alias equityPayout: contractState -> contract -> tradableProduct -> product -> contractualProduct -> economicTerms -> payout -> equityPayout only-element
   alias updatedEquityPayout: updatedContract -> tradableProduct -> product -> contractualProduct -> economicTerms -> payout -> equityPayout only-element
   alias periodEndDate: CalculationPeriod( equityPayout -> calculationPeriodDates, date ) -> endDate
   alias equityPerformance: EquityPerformance(contractState, observation -> observation, periodEndDate)
   
   condition IsEquityContract: equityPayout exists
   
   assign-output updatedEquityPayout -> priceReturnTerms -> valuationPriceFinal -> netPrice -> amount:
     if CalculationPeriod( equityPayout -> calculationPeriodDates, periodEndDate ) -> isLastPeriod then price
   assign-output updatedEquityPayout -> priceReturnTerms -> valuationPriceInterim -> netPrice -> amount:
     if CalculationPeriod( equityPayout -> calculationPeriodDates, periodEndDate ) -> isLastPeriod = False then price
   assign-output updatedContract -> tradableProduct -> product -> contractualProduct -> economicTerms -> payout -> equityPayout -> performance: <"Reset primitive after state must be correctly populated with the equity payout including the performance.">
     equityPerformance
   assign-output updatedContract -> tradableProduct -> product -> contractualProduct -> economicTerms -> payout -> equityPayout -> payoutQuantity -> quantityMultiplier -> multiplierValue: <"Using the Rate of Return we 'reset' the multiplier, which is used to resolve the ultimate notional amount for the equity swap.">
     1 + equityPerformance / 100

The set of updated values include the ``performance`` attribute on the ``equityPayout``, which represents the performance of the current calculation period. The resolution function uses some of the already defined *utility functions* such as ``CalculationPeriod`` and also a *calculation function* for the Equity performance.

This contract resolution mechanism is wired into the function that creates the ``ResetPrimitive`` object:

.. code-block:: Haskell

 func Create_ResetPrimitive: <"Specifies how a Reset Primitive should be constructed.">
   [creation PrimitiveEvent]
   inputs:
     contractState ContractState (1..1)
     observation ObservationPrimitive (1..1)
     date date (1..1)
   output:
     resetPrimitive ResetPrimitive (1..1)
     
   alias contract: contractState -> contract
   
   assign-output resetPrimitive -> before: contractState
   assign-output resetPrimitive -> after -> contract: contractState -> contract
   assign-output resetPrimitive -> after -> updatedContract: <"To handle the various ways Contracts can change over time, ">
     ResolveUpdatedContract(contractState, observation, date)

.. note:: The Reset Event only resets some values on the contract but does not calculate nor pay any cashflow. Any cashflow calculation and payment would be handled separately as part of a Transfer Event which, when such cashflow depends on any resettable values, will use the values updated as part of the Reset Event (as is the case of the *Equity Cash Settlement Amount*).

Workflow Step Creation
""""""""""""""""""""""
(*Coming soon*)


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

.. _Rosetta DSL Documentation: https://docs.rosetta-technology.io/dsl/documentation.html
.. _Qualified Type Section: https://docs.rosetta-technology.io/dsl/documentation.html#qualified-type
.. _Function Definition Section: https://docs.rosetta-technology.io/dsl/documentation.html#function-definition
.. _Function Component Section: https://docs.rosetta-technology.io/dsl/documentation.html#function-component
.. _Code Generation Section: https://docs.rosetta-technology.io/dsl/codegen-readme.html
.. _Validation Component Section: https://docs.rosetta-technology.io/dsl/documentation.html#validation-component
.. _Mapping Component Section: https://docs.rosetta-technology.io/dsl/documentation.html#mapping-component
.. _Special Syntax Section: https://docs.rosetta-technology.io/dsl/documentation.html#special-syntax

.. _Event Model Section: https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#event-model
.. _Event Qualification Section: https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#event-qualification
.. _Validation Process Section: https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#validation-process
.. _Calculation Process Section: https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#calculation-process
.. _Workflow Section: https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#workflow
.. _Product Model Section: https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#product-model
.. _Tradable Product Section: https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#tradable-product

.. _Portal: https://portal.cdm.rosetta-technology.io
.. _function coverage matrix: Portal
.. _data modelling: https://en.wikipedia.org/wiki/Cardinality_(data_modeling)#Application_program_modeling_approaches
