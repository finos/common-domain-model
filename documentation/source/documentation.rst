.. |trade|  unicode:: U+02122 .. TRADE MARK SIGN

The ISDA Common Domain Model
============================

The ISDA Common Domain Model is an initiative that ISDA has spearheaded to produce a common, robust, digital blueprint for how derivatives are traded and managed across their lifecycle.

ISDA anticipates that establishing such digital data and processing standards will lead to the following benefits:

* Reduce the current need for continual reconciliations to address mismatches caused by variations in how each firm records trade lifecycle events;
* Enable consistency in regulatory compliance and reporting;
* Accelerate greater automation and efficiency in the derivatives market;
* Provide a common foundation for new technologies like distributed ledger, cloud and smart contracts to facilitate data consistency;
* Facilitate interoperability across firms and platforms.

A high-level `presentation <https://www.isda.org/a/z8AEE/ISDA-CDM-Factsheet.pdf>`_ of the ISDA CDM and additional information is available on the ISDA website (`www.isda.org <http://www.isda.org/>`_), particularly with the referred `Short Video <https://www.isda.org/2017/11/30/what-is-the-isda-cdm/>`_. It is based on the design principles specified as part of ISDA’s October 2017 `CDM concept paper <https://www.isda.org/a/gVKDE/CDM-FINAL.pdf>`_ which the *ISDA CDM Design Working Group* is tasked with implementing.

The ISDA CDM is made openly accessible to all industry participants.

The Rosetta DSL
=====================

As a practical manifestation of the design choices made by the Working Group, the ISDA CDM is a model written in a Domain-Specific Language (DSL) called the *Rosetta DSL*. This DSL is now open source under an Apache 2.0 licence and hosted in its own `repository <https://github.com/REGnosys/rosetta-dsl#the-rosetta-dsl>`_.


The CDM Components
==================

The below ISDA CDM Components Diagram lays out the three set of CDM application components:

* **Rosetta** corresponds to the 'under the hood' components with respect to the CDM: the Rosetta grammar and the default code generators (currently Java), which together form the Rosetta DSL. To facilitate adoption and implementation of the CDM by the community, a dedicated `repository <https://github.com/REGnosys/rosetta-code-generators>`_ has been open-sourced, also under an Apache 2.0 license, for other industry participants to write code generators in any other languages.
* **The ISDA CDM Distribution** is made available to participants through download from the CDM Portal and is subject to the ISDA CDM licence.  The most crucial components of this ISDA CDM Distribution are:

  * **Model Definition**, which corresponds to the actual model as expressed in the Rosetta syntax and which components are further detailed as part of the CDM Modelling Artefacts section of this documentation.
  * **Model Code Projection**, currently available as Java and JSON.  As the Rosetta syntax represents not just data components but also logic, the JSON representation has quite a limited scope and usefulness but is being used in practice by downstream consumers of the CDM.
  * **Default Apps**. While the two above components represent the essence of the model and are meant to be used as such by implementors, the Default Apps correspond to default implementations which can either be used as such, or be disabled or extended by those participants.  An example of such would be the ``rosettaKey`` implementation, which uses the default Java hash code function, but which might be deemed as inappropriate by some participants and hence be replaced by some alternative implementation.

* **CDM Applications** by service providers. It is expected that a rich eco-system of such licensed application components that are based upon the CDM will develop over time. REGnosys is the first to have taken the initiative to develop an offering of solutions, which purpose is to assist market participants in making use of the CDM. In particular, the CDM Portal provides a few UI components allowing participants to browse through the CDM. ISDA doesn't endorse any of those application components.

.. figure:: cdm-components-diagram.png

CDM Modelling Artefacts
=======================

All the modelling artefacts available in the CDM, with their associated syntax and purpose, are detailed in the Rosetta DSL `documentation <https://github.com/REGnosys/rosetta-dsl/blob/master/documentation/documentation.rst>`_.

CDM Model
=========

This section presents an outline of the five dimensions of the CDM model:

* Product
* Event
* Legal Agreement
* Calculation
* Reference Data

Product Model
-------------

The CDM provides a composite product model whereby:

* **Economic terms are specified by composition**, leveraging the FpML building blocks to the extent possible while also looking for further consistency and simplicity.
* **Product qualification is inferred** from those economic terms.

The initial CDM scope tackles contractual derivative products. Listed products, loans and mortgages are represented in relation to the features needed to position those as underlyers of such derivative products. It is envisioned that further product types will be covered in the CDM model over time.

Contractual Derivative Products
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The scope of products implemented as part the current scope is:

* Interest rate derivatives:

  * Interest rate swaps (incl. cross-currency swaps, non-deliverable swaps, basis swaps, swaps with  non-regular periods, ...)
  * Swaptions
  * Caps/floors
  * FRAs
  * Bond and convertible bond options

* Credit derivatives:

  * Credit default swaps (incl. baskets, tranche, swaps with mortgage and loans underlyers, ...)
  * Options on credit default swaps

The below sections detail the key features of this product implementation: contract representation, economic terms component and how the product qualification is inferred from those economic terms.

Post-Execution: Contract
""""""""""""""""""""""""

Contractual products are bilateral contracts between two parties. Terms are specified at trade inception and apply throughout the life of the contract, unless amended by mutual agreement.  Contractual products are fungible only under specific terms (e.g. existence of a close-out netting agreement between the parties).

The scope of the contract is limited to the post-execution part of the lifecycle. It involves the contractual legal entities and has a set of attributes which are only qualified at the execution and post-execution stage: trade date, calculation agent, documentation, governing law, etc.

.. code-block:: Java

 class Contract key
 {
  contractIdentifier Identifier (1..*);
  tradeDate TradeDate (1..1);
  clearedDate date (0..1);
  contractualProduct ContractualProduct (1..1);
  collateral Collateral (0..1);
  documentation Documentation (0..1);
  governingLaw GoverningLawEnum (0..1) scheme;
  party Party (0..*);
  account Account (0..*);
  partyRole PartyRole (0..*);
  calculationAgent CalculationAgent (0..1);
  partyContractInformation PartyContractInformation (0..*);
  closedState ClosedState (0..1);
 }

The economic terms of the contract are positioned as part of the ``contractualProduct`` attribute, alongside the product identification and product taxonomy information.

.. code-block:: Java

 class ContractualProduct
 {
  productIdentification ProductIdentification (0..1) ;
  productTaxonomy ProductTaxonomy (1..*) ;
  economicTerms EconomicTerms (1..1) ;
 }

The ``Contract`` class incorporates all the elements that are part of the FpML *trade* confirmation view, with the exception of the following elements: *tradeSummary*, *originatingPackage*, *allocations* and *approvals*. Whereas the ``ContractualProduct`` class corresponds to the pre-trade view of the FpML *trade*. (The FpML *trade* term has not been used as part of the CDM because deemed ambiguous in this respect.  Its use as part of the standard is largely due to an exclusive focus on post-execution activity in the initial stages of its development. Later adjustments in this respect would have been made difficult as a result of backward compatibility considerations.)

Economic Terms
""""""""""""""

The ``EconomicTerms`` class and the underlying ``Payout`` class represent a significant departure from the FpML standard. While FpML qualifies the product through the *product* substitution group, the CDM specifies the various set of possible economic terms as part of the ``economicTerms`` and ``payout`` attributes.  A contractual product will then consist in an assembling of such economic terms, from which the product qualification will be syntactically inferred.

.. code-block:: Java

 class EconomicTerms rosettaKeyValue
 {
  payout Payout (1..1) ;
  earlyTerminationProvision EarlyTerminationProvision (0..1) ;
  cancelableProvision CancelableProvision (0..1) ;
  extendibleProvision ExtendibleProvision (0..1) ;
 }

The ``Payout`` class provides some provide some insight into the respective product representation between FpML and CDM, through the relevant synonym sources and associated path expressions. As an example, the FpML *feeLeg* is represented through the CDM ``interestRatePayout``, while the FpML *singlePayment* and *initialPayment* are both represented through the CDM ``cashflow``.

.. code-block:: Java

 class Payout
 {
  interestRatePayout InterestRatePayout (0..*);
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value swapStream path "trade.swap" ]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value swapStream path "swap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value swapStream]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value feeLeg path "trade.creditDefaultSwap", generalTerms path "trade.creditDefaultSwap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value feeLeg path "creditDefaultSwap", generalTerms path "creditDefaultSwap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value feeLeg, generalTerms]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value capFloorStream path "trade.capFloor"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value fra path "trade"]
  creditDefaultPayout CreditDefaultPayout (0..1);
  cashflow Cashflow (0..*);
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value additionalPayment path "trade.swap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value additionalPayment path "swap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value additionalPayment]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value initialPayment path "trade.creditDefaultSwap.feeLeg"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value initialPayment path "creditDefaultSwap.feeLeg"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value singlePayment path "trade.creditDefaultSwap.feeLeg"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value singlePayment path "creditDefaultSwap.feeLeg"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value singlePayment]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value premium path "trade.swaption"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value premium path "swaption"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value premium path "trade.creditDefaultSwapOption"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value premium path "creditDefaultSwapOption"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value premium path "trade.bondOption"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value premium path "bondOption"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value premium path "trade.capFloor", additionalPayment path "trade.capFloor"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value otherPartyPayment path "trade"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value otherPartyPayment]
  optionPayout OptionPayout (0..*);
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value swaption path "trade"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value swaption]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value creditDefaultSwapOption path "trade"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value creditDefaultSwapOption]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value bondOption path "trade"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value bondOption]
 }

The absence of synonym entry for the ``creditDefaultPayout`` attribute is due to the fact that the corresponding CDS constructs are positioned within the ``CreditDefaultPayout`` class:

.. code-block:: Java

 class CreditDefaultPayout key <"The credit default payout specification provides the details necessary for determining when a credit payout will be triggered as well as the parameters for calculating the payout and the settlement terms. The associated rosettaKey denotes the ability to associate a hash value to the CreditDefaultPayout instantiations for the purpose of model cross-referencing, in support of functionality such as the event effect and the lineage.">
 {
  generalTerms GeneralTerms (1..1) <"The specification of the non-monetary terms for the Credit Derivative Transaction, including the buyer and seller and selected items from the ISDA 2014 Credit Definition article II, such as the reference obligation and related terms.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value generalTerms path "trade.creditDefaultSwap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value generalTerms path "creditDefaultSwap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value generalTerms]
  protectionTerms ProtectionTerms (1..*) <"Specifies the terms for calculating a payout to protect the buyer of the swap in the case of a qualified credit event. These terms include the notional amount, the applicable credit events, the reference obligation, and in the case of a CDS on mortgage-backed securities, the floatingAmountEvents.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value protectionTerms path "trade.creditDefaultSwap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value protectionTerms path "creditDefaultSwap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value protectionTerms]
  cashSettlementTerms CashSettlementTerms (0..*) <"Specifies the terms applicable to the cash settlement of a credit event.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value cashSettlementTerms path "trade.creditDefaultSwap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value cashSettlementTerms path "creditDefaultSwap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value cashSettlementTerms]
  physicalSettlementTerms PhysicalSettlementTerms (0..*) <"Specifies the terms applicable to the physical settlement of a credit event.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value physicalSettlementTerms path "trade.creditDefaultSwap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value physicalSettlementTerms path "creditDefaultSwap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value physicalSettlementTerms]
  transactedPrice TransactedPrice (0..1) <"The qualification of the price at which the contract has been transacted, in terms of market fixed rate, initial points, market price and/or quotation style. In FpML, those attributes are positioned as part of the fee leg.">;
 }

Derivative Products Underlyers
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

While FpML specifies a number of underlier product attributes as part of the contract representation, the CDM approach is to exclude any attribute that can be abstracted through reference data.  This is because specifying such information as part of the contract information leads to a risk or contradictory information, particularly for long-dated contracts.

As a result, the bond and convertible bond representation is limited to the product identifier.

Follow-up is in progress with the ISDA CDM Credit Workstream to confirm the approach with respect to the loan and mortgage-backed security underliers.

.. code-block:: Java

 abstract class IdentifiedProduct
 {
  productIdentifier ProductIdentifier (1..1);
 }

 class Bond extends IdentifiedProduct
 {
 }

 class ConvertibleBond extends IdentifiedProduct
 {
 }

 class Loan extends IdentifiedProduct
 {
  borrower LegalEntity (0..*) ;
  lien string (0..1) scheme ;
  facilityType string (0..1) scheme ;
  creditAgreementDate date (0..1) ;
  tranche string (0..1) scheme ;
 }

 class MortgageBackedSecurity extends ProductIdentifier
 {
  pool AssetPool (0..1);
  sector MortgageSectorEnum (0..1) scheme;
  tranche string (0..1);
 }

Product Qualification
^^^^^^^^^^^^^^^^^^^^^

The product qualification is inferred from the product's economic terms through a dedicated logic which navigates the model components. It uses the ``isProduct`` Rosetta syntax detailed as part of the *Object Qualification* in the *CDM Modelling Artefacts* section of the documentation

**The qualification of a Zero-Coupon Fixed-Float Inflation Swap** provides an example of the logic to serve that purpose, which combines Boolean and qualified expressions.

The CDM makes use of the ISDA taxonomy V2.0 leaf level to qualify the product. 18 interest rate derivative products have so far been qualified as part of the CDM, in effect representing the full ISDA V2.0 scope. The current CDM implementation only qualifies interest rate swaps, as the ISDA taxonomy V2.0 for credit default swap references the transaction type instead of the product features, which values are not publicly available and hence not positioned as a CDM enumeration.

Follow-up is in progress with the ISDA Credit Group to evaluate whether an alternative product qualification could be developed that would leverage the approach adopted for interest rate derivatives. This issue will be addressed as part of later versions of the CDM.

.. code-block:: Java

 isProduct InterestRate_InflationSwap_FixedFloat_ZeroCoupon
  [synonym ISDA_Taxonomy_v1 value InterestRate_IRSwap_Inflation]
  EconomicTerms -> payout -> interestRatePayout -> interestRate -> fixedRate count = 1
  and EconomicTerms -> payout -> interestRatePayout -> interestRate -> inflationRate count = 1
  and EconomicTerms -> payout -> interestRatePayout -> interestRate -> floatingRate is absent
  and EconomicTerms -> payout -> interestRatePayout -> crossCurrencyTerms -> principalExchanges is absent
  and EconomicTerms -> payout -> optionPayout is absent
  and EconomicTerms -> payout -> interestRatePayout -> paymentDates -> paymentFrequency -> periodMultiplier = 1
  and EconomicTerms -> payout -> interestRatePayout -> paymentDates -> paymentFrequency -> period = PeriodExtendedEnum.T

The product qualification is positioned as the ``productQualifier`` attribute of the ``ProductIdentification`` class, alongside the attributes currently used in FpML to specify the product: ``primaryAssetClass``, ``secondaryAssetClass``, ``productType`` and ``productId``.  This approach allows to specify the credit derivatives products until such time when an alternative approach to the transaction type is identified to support a proper product qualification for credit derivatives.

.. code-block:: Java

 class ProductIdentification
 {
  productQualifier productType (0..1);
  primaryAssetClass AssetClassEnum (0..1) scheme;
  secondaryAssetClass AssetClassEnum (0..*) scheme;
  productType string (0..*) scheme;
  productId string (0..*) scheme;
 }

The CDM product qualification is stamped onto the generated CDM objects and their JSON serialised representation, as shown in the below JSON snippet. It includes both the product identification information associated with an originating FpML document and the product qualification generated by the CDM:

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

The CDM event model is based upon the same high-level principles as the product model:

* **Events are specified by composition** of *primitive events**, which use of a large set of the FpML building blocks.
* **Event qualification is inferred** from those primitive events and, in some relevant cases, from an **intent** qualifier.

Baseline Event Features
^^^^^^^^^^^^^^^^^^^^^^^

The ``Event`` class carries the following information:

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

* It allows for flexibility in a context where it would challenging to mandate which points in the process should have associated timestamps.
* Gathering all of those in one place in the model allows for evaluation and rationalisation down the road.

The CDM Group has expressed concerns with combining timestamps which are deemed 'technical' with 'business' ones. A further evaluation of this timestamp modelling approach will be required.

Below is JSON snippet of an instance representation of this approach.

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

Primitive Event
"""""""""""""""

The CDM composite approach uses the primitive events as building blocks for lifecycle events. These primitive events are detailed in the *Primitive Event* section of the documentation.

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
* For the ``contract`` effect: ``600e4873`` points to the new contract in the ``after`` state of the ``quantityChange`` primitive event. Note how the new contract retains the original ``tradeDate`` attribute of the initial contract even after a quantity change.
* For the ``transfer`` effect: ``ee4f7520`` points to the ``transfer`` primitive event.

Other Information
""""""""""""""""

* **Date information** is provided through the ``eventDate`` and ``effectiveDate`` attributes, the latter being optional as not applicable to certain events (e.g. observations).
* **Action qualification** specifies whether the event is a new one or a correction or cancellation of a prior one.
* **Party information** is optional because not applicable to certain events (e.g. most of the observation events).
* **Event qualifier** is derived from the event features, as per the *Event Qualification* section.

Primitive Event
^^^^^^^^^^^^^^^

CDM primitive events are the building block components used to specify business events.

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

Event Qualification
^^^^^^^^^^^^^^^^^^^

Similar to the product modelling approach, the CDM lifecycle events are qualified as a function of the combination of their primitive event features and, when specified, the ``intent`` attribute. The event qualification uses the ``isEvent`` syntax in Rosetta, which is specified as part of the *Object Qualification* in the *CDM Modelling Artefacts* section of the documentation.

The CDM makes use of the ISDA taxonomy V2.0 leaf level to qualify the event.  The synonymity with the ISDA taxonomy V1.0 has been systematically indicated as part of the model upon request from CDM group participants, who pointed out that a number of them use it internally. 23 lifecycle events have currently been qualified as part of the CDM.

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

The CDM aims at providing a digital representation of the legal agreements that govern the financial contracts and workflows.

This is expected to yield two sets of benefits:

* Support the marketplace initiatives that aim at streamlining and standardizing the legal agreements by providing a comprehensive digital representation of such agreements.  As part of that, the CDM is looking to effectively integrate with some of those marketplace initiatives, such as (but not limited to) ISDA Create and AcadiaSoft Agreement Manager.  While the initial scope is focused on the ISDA legal agreements, it is not limited to those.  As an example, as a follow-up from the work in progress to represent secured funding contracts and associated lifecycle events it is expected that the CDM will look to represent the associated governing legal agreements.
* Complement the contract and lifecycle event representation in order to provide a comprehensive representation of the financial workflows.  Collateral management is a good example of the applicability of such approach, as most of the associated workflows require to reference the associated legal agreements, such as the ISDA Initial Margin and Variation Margin Credit Support Annex.


The current CDM scope encompasses the following features:

* Model representation of the following legal agreements:

  * ISDA 2016 Credit Support Annex for Initial Margin, with the New York, Japanese and English governing laws;
  * ISDA 2016 Credit Support Annex for Variation Margin, New York governing law.

* Mapping to the ISDA Create data representation for the elections associated with the ISDA 2016 CSA for Initial Margin (not the ISDA CSA Variation Margin, which is not yet represented in ISDA Create);
* Initial work has been developed to map the CDM to the AcadiaSoft Agreement Manager, although only limited progress has been made so far;
* Integration of the ``LegalAgreement`` with the ``Contract``, through the CDM referencing mechanism.

The ability to ingest sample legal agreements is currently being worked out, but not yet supported as part of the CDM.

Modelling Approach
^^^^^^^^^^^^^^^^^^

The current CDM model leverages some prior and current work:

* The FpML Legal View, which was developed in 2013-14 with the aim of supporting the ISDA Standard CSA in a generic manner;
* The ISDA Create solution (in its version 1.0).

The intent is also to further leverage the AcadiaSoft Agreement Manager solution as part of the further iterations of the model, particularly as it relates to the integration with the collateral management workflow.

The key modelling principles that have been adopted to represent legal agreements are as follows:

* Distinction between the agreement identification features (agreement name, publisher, identification, ...), which are represented through the ``LegalAgreementBase`` abstract class, and the elections, which are the content features and are represented through classes which are aligned with the legal agreement template which they meant to represent, an example of which being the ``CsaInitialMargin2016JapaneseLaw`` class, which represents the ISDA 2016 Japanese Law CSA for Initial Margin;
* Composite model, both as part of the ``Base`` abstract class, which makes use of classes that are also used as part of the contract and lifecycle event components of the CDM (e.g. ``Party``, ``Identifier``, ``PartyRole``), and as part of the elective classes, with the above mentioned ``CsaInitialMargin2016JapaneseLaw`` class extending the ``CsaInitialMargin2016`` abstract class which specifies the elections that are common among governing laws, and which in turn extends the ``Csa2016`` abstract class which specifies the elections that are common among the ISDA 2016 Initial Margin and Variation Margin CSA agreements;
* Representation of the legal agreement elections, as opposed to their whole write-up.  Similar to what has been done as part of the ISDA Create solution, such approach still provides the ability for CDM users to wrap those normalized elections into the corresponding legal agreement template, in order to provide a complete legal agreement;
* Focus on providing whenever possible a normalized data representation which can be digitally usable as such once projected through a machine executable language.  Practically speaking, that means restricting the use of elections expressed in a ``string`` format whenever possible.  To this effect, the CDM leverages the ISDA Create data representation, while also extends it in some cases by leveraging some of the work developed in 2013-14 as part of the FpML work to provide a digital representation of the Standard CSA.  Notable examples of such approach are:

  * The ``EligibleCollateral`` class provides the ability to specify the eligible collateral in a comprehensive manner for the purpose of initial and variation margin, which can be directly useable digitally through the combination of an enumerated list of eligible assets (based upon the 2003 ISDA Collateral Asset Definitions), normalized maturity bands and agency rating notations;
  * The ``EligibilityToHoldCollateral`` class specifies the conditions under which a party and its custodian(s) are entitled to hold collateral in relation to the ISDA CSA for Variation Margin, through the combination of party terms that are specified through an enumeration, normalized custodian terms (see below) and/or the determination of countries in which such collateral can he held into;
  * The ``CustodianTerms`` class provides the ability to specify the requirements applicable to the custodian with respect to the holding of posted collateral through the combination of minimal assets and minimal rating considerations, or through the designation of a specific custodian.

The Elective Provisions
^^^^^^^^^^^^^^^^^^^^^^^

As already mentioned, the current CDM scope is limited to the ISDA 2016 CSA for Initial Margin and Variation Margin.  Taking this context in consideration, the data representation is currently organised around 3 levels of composition:

* The ``Csa2016`` abstract class specifies the set of provisions that are common among governing laws and across Initial and Variation Margin templates.  It is expected that this abstract class will evolve as further vintages of the ISDA CSA are being modelled.

 .. code-block:: Java

  abstract class Csa2016
  {
	 baseCurrency string (1..1) scheme ;
	  additionalObligations string (0..1) ;
	  conditionsPrecedent ConditionsPrecedent (1..1) ;
	  substitution Substitution (1..1) ;
    disputeResolution DisputeResolution (1..1) ;
	  additionalRepresentation AdditionalRepresentation (1..1) ;
	  demandsAndNotices ContactElection (1..1) ;
	  addressesForTransfer ContactElection (1..1) ;
	  bespokeProvision string (0..1) ;
  }

* The ``CsaInitialMargin2016`` abstract class extends the ``Csa2016`` class to specify the provisions for the 2016 ISDA Credit Support Annex for Initial Margin that are common among the applicable governing laws.

 .. code-block:: Java

  abstract class CsaInitialMargin2016 extends Csa2016
  {
 	 regime Regime (1..1) ;
 	 oneWayProvisions OneWayProvisions (1..1) ;
 	 method Method (1..1) ;
 	 identifiedCrossCurrencySwap boolean (1..1) ;
 	 sensitivityToEquity SensitivityMethodology (1..1) ;
 	 sensitivityToCommodity SensitivityMethodology (1..1) ;
 	 fxHaircutCurrency FxHaircutCurrency (1..1) ;
 	 creditSupportObligations CreditSupportObligationsInitialMargin (1..1) ;
 	 calculationDateLocation CalculationDateLocation (1..1) ;
 	 notificationTime NotificationTime (1..1) ;
 	 terminationCurrency TerminationCurrencyAmendment (1..1) ;
  }

* The ``CsaVariationMargin2016`` abstract class extends the ``Csa2016`` class to specify the provisions for the 2016 ISDA Credit Support Annex for Variation Margin that are common among the applicable governing laws.  It should be noted that its implementation has been undertaken without a thorough review of the Japanese and English governing laws (as only a New York sample agreement was available), and it should be expected that it might have to be adjusted as part of the integration of those governing laws.

 .. code-block:: Java

  abstract class CsaVariationMargin2016 extends Csa2016
  {
 	 creditSupportObligations CreditSupportObligationsVariationMargin (1..1) ;
 	 valuationAgent Party (1..1) reference ;
 	 valuationDateLocation CalculationDateLocation (1..1) ;
 	 valuationTime BusinessCenterTime (1..*) ;
 	 notificationTime int (1..1) ;
 	 holdingAndUsingPostedCollateral HoldingAndUsingPostedCollateral (1..1) ;
 	 creditSupportOffsets boolean (1..1) ;
 	 otherCsa RelatedAgreement (1..1) ;
  }

* The classes that represent the ISDA CSA elections by extending the above abstract constructs are the following:

  * The ``CsaInitialMargin2016JapaneseLaw``, ``CsaInitialMargin2016NewYorkLaw`` and ``CsdInitialMargin2016EnglishLaw`` classes extend the ``CsaInitialMargin2016`` abstract class to specify the Initial Margin elections which are specific to those governing laws;
  * The ``CsaVariationMargin2016NewYorkLaw`` class extends the ``CsaVariationMargin2016`` abstract class to specify the Variation Margin elections that are specific to the New York law.

Linking Legal Agreements to Contracts and Events
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The way in which the CDM relates/ties a legal agreement with the relevant contract or event is through the referencing mechanism.

This referencing mechanism has been implemented for the ``Contract`` (but not yet for the ``Event``, the reason being that no lifecycle event workflow has yet been specified that references legal agreement other than through the contract).

This referencing of the legal agreement from the ``Contract`` is done through the ``documentation`` attribute.  Alongside with providing the ability to identify some of the key terms of a governing legal agreement (such as the agreement identifier, the publisher, the document vintage and the agreement date) as part of the ``documentationIdentification`` attribute, the associated ``Documentation`` class provides the ability to reference a legal agreement that is electronically represented in the CDM through the ``legalAgreement`` attribute, which has a reference key into the instance agreement.

The below snippet represents this ``Documentation`` class, which ``legalAgreement`` attribute carries the ``reference`` qualifier.

.. code-block:: Java

 class Documentation
 {
  legalAgreement LegalAgreement (0..*) reference;
  documentationIdentification DocumentationIdentification (0..1);
 }

This further snippet presents the ``LegalAgreement`` class and its associated ``key`` qualifier.

.. code-block:: Java

 class LegalAgreement extends LegalAgreementBase key one of
 {
  csdInitialMargin2016EnglishLaw CsdInitialMargin2016EnglishLaw (0..1);
  csaInitialMargin2016JapaneseLaw CsaInitialMargin2016JapaneseLaw (0..1);
  csaInitialMargin2016NewYorkLaw CsaInitialMargin2016NewYorkLaw (0..1);
  csaVariationMargin2016NewYorkLaw CsaVariationMargin2016NewYorkLaw (0..1);
 }


Calculation
-----------

The current CDM version implements the **Fixed Amount** and **Floating Amount** ISDA 2006 Definitions, alongside with some of the day count fractions.

Fixed Amount and Floating Amount Definitions
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The CDM syntax to express the Fixed Amount and Floating Amount is similar in structure: a calculation that reflects the terms of the ISDA 2006 Definitions, and associated arguments.

.. code-block:: Java

 calculation FixedAmount
 {
  fixedAmount number: calculationAmount * rate * dayCountFraction
  currencyAmount CurrencyEnum: currencyAmount
 }

 arguments FixedAmount
 {
  calculationAmount: is InterestRatePayout -> quantity -> notionalSchedule -> notionalStepSchedule -> initialValue
  currencyAmount: is InterestRatePayout -> quantity -> notionalSchedule -> notionalStepSchedule -> currency
  rate: is InterestRatePayout -> interestRate -> fixedRate -> initialValue
  dayCountFraction: is InterestRatePayout -> dayCountFraction
 }

.. code-block:: Java

 calculation FloatingAmount
 {
   floatingAmount number: calculationAmount * ( floatingRate + spread ) * dayCountFraction
   currencyAmount CurrencyEnum: currencyAmount
 }

 arguments FloatingAmount
 {
   calculationAmount: is InterestRatePayout -> quantity -> notionalSchedule -> notionalStepSchedule -> initialValue
   currencyAmount: is InterestRatePayout -> quantity -> notionalSchedule -> notionalStepSchedule -> currency
   floatingRate: is ResolveRateIndex( InterestRatePayout -> interestRate -> floatingRate -> floatingRateIndex ) -> rate
   spread: is GetRateSchedule( InterestRatePayout -> interestRate -> floatingRate ) -> schedule -> initialValue
   dayCountFraction: is InterestRatePayout -> dayCountFraction
 }

Day Count Fractions
^^^^^^^^^^^^^^^^^^^

The current CDM version incorporates day count fractions calculations which are quite representative of the set of day count fractions that are specified as part of the ISDA 2006 Definitions.  Among those are the 30E/360 and the ACT/365.FIXED day count fractions. While the **30E/360** definition specifies the actual computation in quite details as a result of the use of a 360 days year and a 30 maximum days month, the **ACT/365.FIXED** is much simpler and relies upon a computation of the number of days in a period which is not specified as part of the syntax because not involving any specific logic.

.. code-block:: Java

 calculation DayCountFractionEnum._30E_360
 {
   number: (360 * (endYear - startYear) + 30 * (endMonth - startMonth) + (endDay - startDay)) / 360
 }

 arguments DayCountFractionEnum._30E_360
 {
  alias period CalculationPeriod( InterestRatePayout -> calculationPeriodDates )

  endYear : is period -> endDate -> year
  startYear : is period -> startDate -> year
  endMonth : is period -> endDate -> month
  startMonth : is period -> startDate -> month
  startDay : is Min( period -> startDate -> day, 30 )
  endDay : is Min( period -> endDate -> day, 30 )
 }

.. code-block:: Java

 calculation DayCountFractionEnum.ACT_365_FIXED
 {
   number : daysInPeriod / 365
 }

 arguments DayCountFractionEnum.ACT_365_FIXED
 {
   daysInPeriod : is DaysInPeriod( InterestRatePayout -> calculationPeriodDates ) -> days
 }

Reference Data Model
--------------------

The CDM only integrates the reference data components that are specifically needed to model the in-scope products, events and interest calculation components.

This translates into the representation of the **party**, with two alternate representations, modelled as attributes: the **legal entity** and the **natural person**.  The reason for making use of the class inheritance model, with Party as a the base type that would be extended by LegalEntity and NaturalPerson, is that the Rosetta model doesn't support downcasting, which was causing issues in some scenarios. This will be further assess at some future point.

It is expected that this CDM reference data representation will be further expanded once use cases for the model will be firmed out.

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

Model Mappings (Synonyms)
-------------------------

In order to facilitate the translation of existing industry messages (based on open standards or proprietary ones) into CDM, the CDM is mapped to a set of those alternative data representations using the ``synonym`` feature available in the Rosetta DSL.

The following set of synonym sources are currently in place for the CDM:

* **FpML standard**: synonymity to the version 5.10 of the standard through the ``FpML_5_10`` synonym source
* **FIX standard**: synonymity to the version 5.0 SP2 of the standard through the ``FIX_5_0_SP2`` synonym source
* **ISO 20022 standard**: synonymity to the standard throught the ``ISO_20022`` synonym source, with no version reference at present
* **Rosetta workbench**: synonymity to the *event.xsd* schema used for the purpose of ingesting sample lifecycle events through the ``Rosetta_Workbench`` synonym source
* **DTCC**: synonymity to the *OTC_Matching_11-0.xsd* schema (including the imported FpML schema version 4.9) that is used for trade matching confirmations through the ``DTCC_11_0`` synonym source, and synonymity to the *OTC_Matching_9-0.xsd* schema (also including the imported FpML schema version 4.9) that is used for payment notifications through the ``DTCC_9_0`` synonym source
* **CME**: synonymity to the *cme-conf-ext-1-17.xsd* schema (including the imported FpML schema version 5.0) that is used fo the clearing confirmation purposes through the ``CME_ClearedConfirm_1_17`` synonym source, and synonymity to the *bloombergTradeFixml* schema (including the imported FpML schema version 4.6) that is used for clearing submissions through the ``CME_SubmissionIRS_1_0`` synonym source
* **AcadiaSoft**: synonymity to the version 1 of the Agreement Manager through the ``AcadiaSoft_AM_1_0`` synonym source

Those synonym sources are listed as part of the Rosetta grammar, so that the synonym source value can be controlled when editing synonyms.
