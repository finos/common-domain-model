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
    assetIdentifier AssetIdentifier (0..1)

The ``price`` attribute is of type ``Price``, which requires the selection of one of the attributes that describe different types of prices. The set of attributes collectively support all products in the CDM.

.. code-block:: Haskell

 type Price:
    cashPrice CashPrice (0..1)
    exchangeRate ExchangeRate (0..1)
    fixedInterestRate FixedInterestRate (0..1)

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
    effectiveDate AdjustableOrRelativeDate (0..1)
    terminationDate AdjustableOrRelativeDate (0..1)
    dateAdjustments BusinessDayAdjustments (0..1)
    payout Payout (1..1)
    earlyTerminationProvision EarlyTerminationProvision (0..1)
    optionProvision OptionProvision (0..1)
    extraordinaryEvents ExtraordinaryEvents (0..1)

Payout
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
    rateSpecification RateSpecification (1..1)
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

 type CalculationPeriodDates:
	[metadata key]
	effectiveDate AdjustableOrRelativeDate (0..1)
	terminationDate AdjustableOrRelativeDate (0..1)
	calculationPeriodDatesAdjustments BusinessDayAdjustments (0..1)
	firstPeriodStartDate AdjustableOrRelativeDate (0..1)
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

 type IdentifiedProduct:
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

.. code-block:: Javascript

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

**The CDM event model provides data structures to represent the trade lifecycle events of financial transactions**. A trade moves from one state to another as the result of *state transition* events initiated by one or both trading parties, by external factors or by contractual terms such as maturity. For example, the execution of the trade is the initial event which results in the state of an executed trade. Subsequently, one party might initiate an allocation, both parties might initiate an amendment to a contractual agreement, or a default by an underlying entity on a Credit Default Swap would trigger a settlement according to defined protection terms.

Examples of lifecyle events supported by the CDM Event Model include the following:

* Trade execution and confirmation
* Clearing
* Allocation
* Settlement (including any future contingent cashflow payment)
* Exercise of options

The representation of state transitions in the CDM event model is based on the following design principles:

* **A lifecycle event describes a change in the state of a trade**, i.e. there must be different before/after trade states based on that lifecycle event.
* **The product definition that underlies the transaction remains immutable**, unless agreed (negotiated) between the parties to that transaction as part of a specific trade lifecycle event. Automated events, for instance resets or cashflow payments, should not alter the product definition.
* **The history of the trade state can be reconstructed at any point in the trade lifecycle**, i.e. the CDM implements a *lineage* between states as the trade goes through state transitions.
* **The state is trade-specific**, not product-specific (i.e. it is not an asset-servicing model). The same product may be associated to infinitely many trades, each with its own specific state, between any two parties.

The data structures in the event model are organised into four main sub-structures to represent state transitions, as described below.

.. figure:: event-model-overview.png

* **Trade state** represents the state in the lifecycle that the trade is in, from execution to settlement and maturity.
* **Primitive event** is a building block component used to specify business events in the CDM. Each primitive event describes a fundamental state-transition component that impacts the trade state during its lifecycle.
* **Business (i.e. trade lifecycle) event** represents a lifecycle event, which may consist of one or more primitive events.
* **Workflow** represents a set of actions or steps that are required to trigger a business event.

Each of these sub-structures are described in the subsequent sections.

Trade State
^^^^^^^^^^^

The trade state is currently described in the CDM by the ``Trade`` type. The trade state can be either an ``Execution`` or a ``Contract``, as controlled by the ``one-of`` condition:

.. code-block:: Haskell

 type Trade:
   [metadata key]
   execution Execution (0..1)
   contract Contract (0..1)
   condition Trade: one-of

While many different types of events may occur through the transaction lifecycle, the execution and contract states are deemed sufficient to describe all of the possible (post-trade) states which may result from those lifecycle events. The execution and contract states always contain a tradable product, which defines all of the current economic terms of the transaction as they have been agreed between the parties.

For instance in a partial termination scenario, the initial state is a contract and the resulting state is also a contract, where the quantity associated with the tradable product is smaller.

.. note:: A tradable product is represented by the ``TradableProduct`` type, which is further detailed in the `Tradable Product Section`_ of the documentation.

The execution and contract types are detailed in the sections below.

Execution
"""""""""

The lifecycle of a transaction between two parties starts with an *execution* state, which is represented by the ``Execution`` type. In addition to the tradable product, an execution includes attributes such as the trade date, transacting parties, execution venue (if any) and settlement terms to describe the execution. Some attributes, such as the parties, may already be defined in a workflow step or business event and can simply be referenced as part of the execution.

.. code-block:: Haskell

 type Execution:
	[metadata key]
	executionType ExecutionTypeEnum (1..1)
	executionVenue LegalEntity (0..1)
	identifier Identifier (1..*)
	tradeDate date (1..1)
		[metadata id]
	tradableProduct TradableProduct (1..1)
	party Party (0..*)
		[metadata reference]
	partyRole PartyRole (0..*)
	closedState ClosedState (0..1)
	settlementTerms SettlementTerms (0..1)

The ``settlementTerms`` attribute define how the transaction should be settled (including the settlement date). For instance, a settlement could be a *delivery-versus-payment* scenario for a cash security transaction or a *payment-versus-payment* scenario for an FX spot or forward transaction. The actual settlement amount(s) will need to use the *price* and *quantity* agreed as part of the tradable product.

.. code-block:: Haskell

 type SettlementTerms extends SettlementBase:
   settlementType SettlementTypeEnum (0..1)
   settlementDate AdjustableOrRelativeDate (0..1)
   valueDate date (0..1)
   settlementAmount Money (0..1)
   transferSettlementType TransferSettlementEnum (0..1)

Post-Execution: Contract
""""""""""""""""""""""""

The contract type is only applicable to contractual products.  It represents the state of a trade after the execution has been confirmed.  A contract has a set of attributes which are optional but would only apply to a post-execution stage: calculation agent, documentation, governing law, etc.

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

.. note:: The ``Contract`` type incorporates all the elements that are part of the FpML *trade confirmation* view (with the exception of: *tradeSummary*, *originatingPackage*, *allocations* and *approvals*), whereas the ``TradableProduct`` type corresponds to the *pre-trade* view in FpML.

Closed State
""""""""""""

In the case when a contract or an execution is closed, it is necessary to record that closure as part of the trade state.

For instance in a novation scenario, the initial state is a contract and the resulting state is two contracts: the first contract is a new contract, which is the same as the original one but where one of the parties has been changed, and the second contract is the original contract, now marked as *closed*.

The ``closedState`` attribute on ``Contract`` and ``Execution`` captures this closed state and defines the reason for closure.

.. code-block:: Haskell

 type ClosedState:
   state ClosedStateEnum (1..1)
   activityDate date (1..1)
   effectiveDate date (0..1)
   lastPaymentDate date (0..1)

.. code-block:: Haskell

 enum ClosedStateEnum:
   Allocated
   Cancelled
   Exercised
   Expired
   Matured
   Novated
   Terminated

Primitive Event
^^^^^^^^^^^^^^^

**Primitive events are the building block components used to specify business events in the CDM**. They describe the fundamental state-transition components that may impact the trade state during its lifecycle. The trade state always transitions to and from one of the trade types, i.e. contract or execution.

Most of the primitive events include ``before`` and ``after`` trade state attributes that define the state transition in terms of evolution in the trade state.  The exceptions are ``ObservationPrimitive`` and ``TransferPrimitive``.

The ``before`` attribute is included as a reference using the ``[metadata reference]`` annotation, because by definition the primitive event points to a state that *already* exists. By contrast, the ``after`` state provides a full definition of that object, because that state is occurring for the first time and it is the occurence of the primitive event that triggers a transition to that new state. By tying each state in the lifecycle to a previous state, primitive events are one of the mechanisms by which *lineage* is implemented in the CDM.

A ``PrimitiveEvent`` object consists of one of the primitive components, as captured by the ``one-of`` condition.  The list of primitive events can be seen in the ``PrimitiveEvent`` type definition:

.. code-block:: Haskell

 type PrimitiveEvent:
   execution ExecutionPrimitive (0..1)
   contractFormation ContractFormationPrimitive (0..1)
   split SplitPrimitive (0..1)
   exercise ExercisePrimitive (0..1)
   observation ObservationPrimitive (0..1)
   quantityChange QuantityChangePrimitive (0..1)
   reset ResetPrimitive (0..1)
   termsChange TermsChangePrimitive (0..1)
   transfer TransferPrimitive (0..1)

   condition PrimitiveEvent: one-of

A number of examples are illustrated below.

Example 1: Execution and Contract Formation
"""""""""""""""""""""""""""""""""""""""""""

Within the scope of the CDM, the first step in instantiating a transaction between two parties is an *execution* or a *contract formation*, which is an execution that has been confirmed between the executing parties. In some cases, there is a time delay between execution and confirmation, therefore the execution can be recorded as the first instantiation. In some other cases, the confirmation is nearly simultaneous with the execution, thus there is no need for an intermediate step.

The transition to an executed state prior to confirmation is represented by the ``ExecutionPrimitive``.

.. code-block:: Haskell

 type ExecutionPrimitive:
   before ExecutionState (0..0)
     [metadata reference]
   after ExecutionState (1..1)

The execution primitive does not allow any before state (as marked by the 0 cardinality of the ``before`` attribute) because the current CDM event model only covers post-trade lifecycle events. In practice, this execution state would be the conclusion of a pre-trade process, which may be a client order that gets filled or a quote that gets accepted by the client.

Following that execution, the trade gets confirmed and a legally binding contract is signed between the two executing parties. In an allocation scenario, the trade would first get split into sub-accounts as  designated by one of the executing parties, before a set of legally binding contracts is signed with each of those sub-accounts.

The ``ContractFormationPrimitive`` represents that transition to the trade state after the trade is confirmed, which results in a ``PostContractFormationState`` containing a contract object.

.. code-block:: Haskell

 type ContractFormationPrimitive:
   before ExecutionState (0..1)
     [metadata reference]
   after PostContractFormationState (1..1)

The before state in the contract formation primitive is optional (as marked by the 0 cardinality lower bound of the ``before`` attribute), to represent cases where a new contract may be instantiated between parties without any prior execution, for instance in a clearing or novation scenario.

Example 2: Reset
""""""""""""""""

In many cases, a trade relies on observable values which will become known in the future: for instance, a floating rate observation at the beginning of each period in the case of a Interest Rate Swap, or the equity price at the end of each period in an Equity Swap. That primitive event is known as a *reset*.

The predecessor to a reset is an *observation* which occurs when that observable value becomes known (as provided by the relevant market data provider), independently from any specific transaction. This primitive event is captured by the ``ObservationPrimitive`` type.

.. code-block:: Haskell

 type ObservationPrimitive:
   source ObservationSource (1..1)
   observation number (1..1)
   date date (1..1)
   time TimeZone (0..1)
   side QuotationSideEnum (0..1)

From that observation, a *reset* can be built which does affect the specific transaction. A reset is represented by the ``ResetPrimitive`` type.

.. code-block:: Haskell

 type ResetPrimitive:
   before ContractState (1..1)
     [metadata reference]
   after ContractState (1..1)
   condition Contract:
     if ResetPrimitive exists
     then before -> contract = after -> contract

Example 3: Transfer
"""""""""""""""""""

A ``TransferPrimitive`` is a multi-purpose primitive that can represent the transfer of any asset, including cash, from one party to another.

.. code-block:: Haskell

 type TransferPrimitive:
   [metadata key]
   identifier string (0..1)
     [metadata scheme]
   settlementType TransferSettlementEnum (0..1)
   settlementDate AdjustableOrAdjustedOrRelativeDate (1..1)
   cashTransfer CashTransferComponent (0..*)
   securityTransfer SecurityTransferComponent (0..*)
   commodityTransfer CommodityTransferComponent (0..*)
   status TransferStatusEnum (0..1)
   settlementReference string (0..1)

By design, the CDM treats the reset and the transfer primitive events separately because there is no one-to-one relationship between reset and transfer.

* Many transfer events are not tied to any reset: for instance, the currency settlement from an FX spot or forward transaction.
* Conversely, not all reset events generate a cashflow: for instance, the single, final settlement that is based on all the past floating rate resets in the case of a compounding floating zero-coupon swap.

Business Event
^^^^^^^^^^^^^^

A Business Event represents a transaction lifecycle event and is built according to the following design principle in the CDM:

* **Business events are specified by composition of primitive events**, which describe the fundamental state-transition components that may impact the trade state during its lifecycle.
* **Business event qualification is inferred from those primitive event components** and, in some relevant cases, from an *intent* qualifier associated with the business event. The inferred value is populated in the ``eventQualifier`` attribute.

.. code-block:: Haskell

 type BusinessEvent:
   [metadata key]
   [rootType]
   primitives PrimitiveEvent (1..*)
   intent IntentEnum (0..1)
   functionCall string (0..1)
   eventQualifier eventType (0..1)
   eventDate date (1..1)
   effectiveDate date (0..1)
   eventEffect EventEffect (0..1)
   workflowEventState WorkflowStepState (0..1)
   [deprecated]

As can be observed in the definition above, the only mandatory attributes of a business event are the ones listed below:

* The ``primitives`` attribute, which contains the list of one or more primitive events composing that business event, each representing one and only one fundamental state-transition.
* The event date. The time dimension has been purposely ommitted from the event's attributes. That is because, while a business event has a unique date, several time stamps may potentially be associated to that event depending on when it was submitted, accepted, rejected etc, all of which are *workflow* considerations.

An example composition of the primitive events to represent a complete lifecycle event is the *partial novation* of a contract, which comprises the following:

* a ``ContractFormation`` primitive that represents the contract between the remaining party and the step in novation party. The ``tradeDate`` in the ``ContractFormation`` primitive should reflect the date of that the novation event was agreed.
* a ``QuantityChange`` primitive which includes a before attribute that defines the terms of the trade between the original parties before the novation and an after attribute the defines the terms of the trade between the original parties after the novation, in which the quantity should be less than the quantity in the before state and greater than 0 (0 would represent the case of a *full novation*).

A business event is *atomic* in the sense that its underlying primitive event constituents cannot happen independently: they either all happen together or they do not happen. In the above partial novation example, the existing trade between the parties must be downsized at the same time as the new trade is instantiated.

Selected attributes of a business event are further explained below:

Intent
""""""

The Intent attribute is an enumeration value that represents the intent of a particular business event, e.g. ``Allocation``, ``EarlyTermination``, ``PartialTermination`` etc. It is used in cases where the primitive events are not sufficient to uniquely inferr a lifecycle event. As an example, a reduction in a trade quantity/notional could apply to a correction event or a partial termination.

Event Effect
""""""""""""

The event effect attribute corresponds to the set of operational and positional effects associated with a lifecycle event. This information is generated by a post-processor associated to the CDM. Certain events such as observations do not have any event effect, hence the optional cardinality.

The ``eventEffect`` contains a set of pointers to the relevant objects that are affected by the event and annotated with ``[metadata reference]``. The candidate objects are types that are marked as referenceable via an associated ``metadata key`` annotation.

.. note:: The use of the key/reference mechanism is further decribed in the `Meta-Data Section`_ of the Rosetta DSL documentation.

.. code-block:: Haskell

 type EventEffect:
   effectedContract Contract (0..*)
     [metadata reference]
   effectedExecution Execution (0..*)
     [metadata reference]
   contract Contract (0..*)
     [metadata reference]
	execution Execution (0..*)
    [metadata reference]
  productIdentifier ProductIdentifier (0..*)
    [metadata reference]
  transfer TransferPrimitive (0..*)
    [metadata reference]

The JSON snippet below for a quantity change event on a contract illustrates the use of multiple metadata reference values in ``eventEffect``.

.. code-block:: Javascript

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

* The effective date is optional as it is not applicable to certain events (e.g. observations), or may be redundant with the event date.
* The event qualifier attribute is derived from the event qualification features. This is further detailed in the `Event Qualification Section`_.

Workflow
^^^^^^^^

The CDM provides support for implementors to develop workflows to process transaction lifecycle events and provides attributes to define lineage from one workflow step to another.

A *workflow* represents a set of actions or steps that are required to trigger a business event, including the initial execution or contract formation. A workflow is organised into a sequence in which each step is represented by a *workflow step*. A workflow may involve multiple parties in addition to the parties to the transaction, and may include automated and manual steps. A workflow may involve only one step.

.. code-block:: Haskell

 type WorkflowStep:
   [metadata key]
   [rootType]
   businessEvent BusinessEvent (0..1)
   proposedInstruction Instruction (0..1)
   rejected boolean (0..1)
   previousWorkflowStep WorkflowStep (0..1)
     [metadata reference]
   messageInformation MessageInformation (0..1)
   timestamp EventTimestamp (1..*)
   eventIdentifier Identifier (1..*)
   action ActionEnum (0..1)
   party Party (0..*)
   account Account (0..*)
   lineage Lineage (0..1)

The different attributes of a workflow step are detailed in the sections below.

Business Event
""""""""""""""

This attribute specifies the business event that the workflow step is meant to generate. It is optional because the workflow may require a number of interim steps before the state-transition embedded within the business event becomes effective, therefore the business event does not exist yet in those steps. The business event attribute is typically associated with the final step in the workflow.

Proposed Instruction
""""""""""""""""""""

This attribute allows for the specification of inputs that when combined with the current trade state, are referenced to generate the state-transition. For example, allocation instructions describe how to divide the initial block trade into smaller pieces, each of which is assigned to a specific party representing a legal entity related to the executing party.  It is optional because it is not required for all workflow steps.  Validation components are in place to check that the ``businessEvent`` and ``proposedInstruction`` attributes are mutually exclusive.

Previous Workflow Step
""""""""""""""""""""""

This attribute, which is provided as a reference, defines the lineage between steps in a workflow. The result is an audit trail for a business event, which can trace the various steps leading to the business event that was triggered.

Action
""""""

The action enumeration qualification specifies whether the event is a new one or a correction or cancellation of a prior one, which are trade entry references and not reflective of negotiated changes to a contract.

Message Information
"""""""""""""""""""

The ``messageInformation`` attribute defines details for delivery of the message containing the workflow steps.

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

.. note::  MessageInformation corresponds to some of the components of the FpML *MessageHeader.model*.

Timestamp
"""""""""

The CDM adopts a generic approach to represent timestamp information, consisting of a ``dateTime`` and a ``qualification`` attributes, with the latter specified through an enumeration value.

.. code-block:: Haskell

 type EventTimestamp:
   dateTime zonedDateTime (1..1)
   qualification EventTimestampQualificationEnum (1..1)

The benefits of the CDM generic approach are twofold:

* It allows for flexibility in a context where it would be challenging to mandate which points in the process should have associated timestamps.
* Gathering all of those in one place in the model allows for evaluation and rationalisation down the road.

Below is an instance of a CDM representation (`serialised`_ into JSON) of this approach.

.. code-block:: Javascript

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

The Event Identifier provides a unique id that can be used for reference by other workflow steps. The data type is a generic identifier component that is used throughout the product and event models. The event identifier information comprises the ``assignedIdentifier`` and an ``issuer``, which may be provided as a reference or via a scheme.

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

The CDM uses the ISDA taxonomy V2.0 leaf level to qualify the event. 22 lifecycle events have currently been qualified as part of the CDM.

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

The output of the qualification function is used to populate the ``eventQualifier`` attribute of the ``BusinessEvent`` object, similar to how product qualification works. An implementation of the CDM would call all of the Event Qualification functions following the creation of each event and then insert the appropriate value or provide an exception message.

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

.. code-block:: Haskell

 type Csa2016 extends Csa:
   baseCurrency string (1..1)
   [metadata scheme]
   additionalObligations string (0..1)
   conditionsPrecedent ConditionsPrecedent (1..1)
   substitution Substitution (1..1)
   disputeResolution DisputeResolution (1..1)
   additionalRepresentation AdditionalRepresentation (1..1)
   demandsAndNotices ContactElection (1..1)
   addressesForTransfer ContactElection (1..1)
   bespokeProvision string (0..1)
   umbrellaAgreement UmbrellaAgreement (0..1)

The ``CsaVariationMargin2016`` abstract class extends the ``Csa2016`` class to specify the provisions for the 2016 ISDA Credit Support Annex for Variation Margin that are common across the applicable governing laws.  At this point its implementation has been undertaken without a thorough review of the Japanese and English governing laws as only a New York sample agreement was available. It might have to be adjusted to integrate those governing laws.

.. code-block:: Haskell

 type CsaVariationMargin2016 extends Csa2016:
   creditSupportObligations CreditSupportObligationsVariationMargin (1..1)
   valuationAgent Party (1..1)
     [metadata reference]
   valuationDateLocation CalculationDateLocation (1..1)
   valuationTime BusinessCenterTime (1..*)
   notificationTime int (1..1)
   holdingAndUsingPostedCollateral HoldingAndUsingPostedCollateral (1..1)
   creditSupportOffsets boolean (1..1)
   otherCsa OtherAgreements (1..1)

The (non-abstract) classes that represent the ISDA CSA elections extend the above abstract constructs:

* For Initial Margin: the ``CsaInitialMargin2016JapaneseLaw``, ``CsaInitialMargin2016NewYorkLaw`` and ``CsdInitialMargin2016EnglishLaw`` classes extend the ``CsaInitialMargin2016`` abstract class to specify the Initial Margin elections that are specific to those governing laws.
* For Variation Margin: the ``CsaVariationMargin2016NewYorkLaw`` class extends the ``CsaVariationMargin2016`` abstract class to specify the Variation Margin elections that are specific to New York law.

Linking Legal Agreements to Contracts and Events
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The CDM uses the key / referencing mechanism to tie a legal agreement with the relevant contract or event.

This referencing mechanism has been implemented for the ``Contract`` but not yet for the ``Event``, since no lifecycle event workflow has yet been specified that references legal agreement other than through the contract itself.

Referencing the legal agreement from the ``Contract`` is done through the ``documentation`` attribute.  The associated ``RelatedAgreement`` type allows to:

* Identify some of the key terms of a governing legal agreement such as the agreement identifier, the publisher, the document vintage and the agreement date, as part of the ``documentationIdentification`` attribute
* Reference a legal agreement that is electronically represented in the CDM through the ``legalAgreement`` attribute, which has a reference key into the agreement instance

The below snippet represents this ``RelatedAgreement`` type, which ``legalAgreement`` attribute carries the ``reference`` annotation and where the ``LegalAgreement`` class carries associated ``metadata key`` annotation:

.. code-block:: Haskell

 type RelatedAgreement:
   legalAgreement LegalAgreement (0..1)
   documentationIdentification DocumentationIdentification (0..1)

.. code-block:: Haskell

 type LegalAgreement extends LegalAgreementBase:
   [metadata key]
   [rootType]
   agreementTerms AgreementTerms (0..1)

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

The data and proces model definitions of the CDM are systematically translated into executable code using purpose-built code generation technology. The CDM executable code is available in a number of modern, widely adopted and freely available programming languages and is systematically distributed as part of the CDM release.

The code generation process is based on the Rosetta DSL and is further described in the `Code Generation Section`_, including an up-to-date `list of available languages <https://docs.rosetta-technology.io/dsl/codegen-readme.html#what-code-generators-are-available>`_. Support for further languages can be added as required by market participants.

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

Product Validation
""""""""""""""""""

As an example, the *FpML ird validation rule #57*, states that if the calculation period frequency is expressed in units of month or year, then the roll convention cannot be a weekday. A machine readable and executable definition of that specification is provided in the CDM, as a ``condition`` attached to the ``CalculationPeriodFrequency`` type:

.. code-block:: Haskell

 condition FpML_ird_57:
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

The CalculationPeriod function receives the ``CalculationPeriodDates`` and the current date as the inputs and produces the ``CalculationPeriodData`` as the output, as shown below:

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

 func EquityCashSettlementAmount:
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

 func RateOfReturn:
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

Each of those components can leverage any calculation or utility function already defined in the CDM. As part of the validation processe embedded in the CDM, an object validation step is included in all these object creation functions to ensure that they each construct valid CDM objects. Further details on the underlying calculation and validation processes are described in the `Calculation Process Section`_ and `Validation Process Section`_.

Illustration of the three components are given in the sections below.

Primitive Creation
""""""""""""""""""

Primitive creation functions can be thought of as the fundamental mathematical operators that operate on a trade *state*. While a primitive event object describes each state transition in terms of *before* and *after* states, a primitive creation function defines the logic to transition from that *before* state to the *after* state, using a set of *instructions*.

An example of such use is the handling of a reset event, hereby presented an an equity reset example. The reset is processed in two steps:

* An ``ObservationPrimitive`` is built for the equity price, independently from any single contract.
* This observation is used to construct a ``ResetPrimitive`` on any contract affected by it.

For the observation primitive, checks are performed on the valuation date and/or time inputs and on their consistency with a given price determination method. The function to fetch the equity price is also specified to ensure integrity of the observation number.

.. code-block:: Haskell

 func EquityPriceObservation:
   inputs:
     equity Equity (1..1)
     valuationDate AdjustableOrRelativeDate (1..1)
     valuationTime BusinessCenterTime (0..1)
     timeType TimeTypeEnum (0..1)
     determinationMethod DeterminationMethodEnum (1..1)
   output:
     observation ObservationPrimitive (1..1)

   condition:
     if valuationTime exists then timeType is absent
     else if timeType exists then valuationTime is absent
     else False

   post-condition:
     observation -> date = ResolveAdjustableDate(valuationDate)
     and if valuationTime exists
       then observation -> time = TimeZoneFromBusinessCenterTime(valuationTime)
       else observation -> time = ResolveTimeZoneFromTimeType(timeType, determinationMethod)

   post-condition:
     observation -> observation = EquitySpot(equity, observation -> date, observation -> time)

The observation is used as an input to *resolve* any Equity Derivative contract (i.e. update its resettable values) that depends on this observation:

.. code-block:: Haskell

 func ResolveEquityContract:
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
   assign-output updatedContract -> tradableProduct -> product -> contractualProduct -> economicTerms -> payout -> equityPayout -> performance:
     equityPerformance
   assign-output updatedContract -> tradableProduct -> product -> contractualProduct -> economicTerms -> payout -> equityPayout -> payoutQuantity -> quantityMultiplier -> multiplierValue:
     1 + equityPerformance / 100

The set of updated values include the ``performance`` attribute on the ``equityPayout``, which represents the performance of the current calculation period. The resolution function uses some of the already defined *utility functions* such as ``CalculationPeriod`` and also a *calculation function* for the Equity performance.

This contract resolution mechanism is wired into the function that creates the ``ResetPrimitive`` object:

.. code-block:: Haskell

 func Create_ResetPrimitive:
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
   assign-output resetPrimitive -> after -> updatedContract:
     ResolveUpdatedContract(contractState, observation, date)

.. note:: The Reset Event only resets some values on the contract but does not calculate nor pay any cashflow. Any cashflow calculation and payment would be handled separately as part of a Transfer Event which, when such cashflow depends on any resettable values, will use the values updated as part of the Reset Event (as is the case of the *Equity Cash Settlement Amount*).

Workflow Step Creation
""""""""""""""""""""""

(*This feature is currently being developed and will be documented upon release in the CDM*)


Reference Data Model
--------------------

The CDM only integrates the reference data components that are specifically needed to model the in-scope products, events, legal agreements and function components.

This translates into the representation of the **party** and **legal entity**.

Parties are not explicitly qualified as a legal entity or a natural person, although the model provides the ability to associate a person (or set of persons) to a party, which use case would imply that such party would be a legal entity (even if not formally specified as such).

The ``LegalEntity`` type is used when only a legal entity reference is appropriate i.e. the value would never be that of a natural person.

.. code-block:: Haskell

 type Party:
   [metadata key]
   partyId string (1..*)
     [metadata scheme]
   name string (0..1)
     [metadata scheme]
   person NaturalPerson (0..*)
   account Account (0..1)

.. code-block:: Haskell

 type NaturalPerson:
   [metadata key]
   honorific string (0..1)
   firstName string (1..1)
   middleName string (0..*)
   initial string (0..*)
   surname string (1..1)
   suffix string (0..1)
   dateOfBirth date (0..1)

.. code-block:: Haskell

 type LegalEntity:
   [metadata key]
   entityId string (0..*)
     [metadata scheme]
   name string (1..1)
     [metadata scheme]

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

.. _Portal: https://portal.cdm.rosetta-technology.io

.. _Rosetta DSL Documentation: https://docs.rosetta-technology.io/dsl/documentation.html
.. _Qualified Type Section: https://docs.rosetta-technology.io/dsl/documentation.html#qualified-type
.. _Function Definition Section: https://docs.rosetta-technology.io/dsl/documentation.html#function-definition
.. _Function Component Section: https://docs.rosetta-technology.io/dsl/documentation.html#function-component
.. _Code Generation Section: https://docs.rosetta-technology.io/dsl/codegen-readme.html
.. _Validation Component Section: https://docs.rosetta-technology.io/dsl/documentation.html#validation-component
.. _Mapping Component Section: https://docs.rosetta-technology.io/dsl/documentation.html#mapping-component
.. _Special Syntax Section: https://docs.rosetta-technology.io/dsl/documentation.html#special-syntax
.. _Meta-Data Section: https://docs.rosetta-technology.io/dsl/documentation.html#meta-data-and-reference

.. _Event Model Section: https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#event-model
.. _Event Qualification Section: https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#event-qualification
.. _Validation Process Section: https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#validation-process
.. _Calculation Process Section: https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#calculation-process
.. _Workflow Section: https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#workflow
.. _Product Model Section: https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#product-model
.. _Tradable Product Section: https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#tradable-product

.. _serialised: https://en.wikipedia.org/wiki/Serialization
.. _data modelling: https://en.wikipedia.org/wiki/Cardinality_(data_modeling)#Application_program_modeling_approaches
