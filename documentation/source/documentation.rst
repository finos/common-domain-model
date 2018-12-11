.. |trade|  unicode:: U+02122 .. TRADE MARK SIGN

The ISDA Common Domain Model
============================
The ISDA Common Domain Model is an initiative that ISDA has spearheaded to produce a common, robust, digital blueprint for how derivatives are traded and managed across their lifecycle. It is based on the design principles specified as part of ISDA’s October 2017 `CDM concept paper <https://www.isda.org/a/gVKDE/CDM-FINAL.pdf>`_ for a product scope limited to simple interest and credit derivative products and an agreed sample of business events.

ISDA anticipates that establishing such digital data and processing standards will lead to the following benefits:

* Reduce the current need for continual reconciliations to address mismatches caused by variations in how each firm records trade lifecycle events;
* Enable consistency in regulatory compliance and reporting;
* Accelerate greater automation and efficiency in the derivatives market;
* Provide a common foundation for new technologies like distributed ledger, cloud and smart contracts to facilitate data consistency;
* Facilitate interoperability across firms and platforms.

A high-level presentation of the ISDA CDM and additional information is available on the ISDA website (`www.isda.org <http://www.isda.org/>`_) and particularly with the referred `Short Video <https://www.isda.org/2017/11/30/what-is-the-isda-cdm/>`_.

The Rosetta Workbench
=====================
To tie the design choices made by the Working Group to how they manifest practically, we make reference to the Rosetta Workbench which we will refer to simply as Rosetta. It is useful to think of Rosetta as a set of tools to use when creating domain models.  Much like how software engineers use programming languages and tools to create software.

Rosetta is a digital repository whose purpose is to consolidate market standards and practices into a cohesive model, from which executable code is automatically generated.

The key idea behind Rosetta is that financial markets presently have two unappealing characteristics as it relates to their supporting electronic data representation:

*  **Variety of data representations**. The plurality of data standards (the main ones being FIX, FpML, ISO 20022 and EFET) is compounded by the many variations in the implementation of those, to which we need to add a wide range of proprietary data representations.
*  **Limited availability of native digital tools** that would allow those data representations to be directly translated into executable code. Even the protocols that have a native digital representation (e.g. FpML and FIXML, which are available in the form of XML schemas) have associated specifications artefacts which require further manual specification and/or coding in order to result in a complete executable solution. In FpML, this is the case of the associated validation rules. In FIX, an example of such are the Recommended Practices/Guidelines, which are only available in the form of PDF documents.

Rosetta is looking to address those shortcomings by **consolidating various data and workflow representations into a cohesive model** (hence the naming reference to the Rosetta Stone), which can be **automatically translated into executable code**.

.. figure:: rosetta-components.png

As illustrated by the above picture, Rosetta has two main components as it relates to its usage as part of the ISDA CDM: the Model Repository and the Rosetta Workbench.

The **Model Repository** has two components:

* The **legible model**, that expresses the data and associated logic using the Rosetta syntax.
* The **projection of this model** into a variety of executable code representations (presently Java).

The **Rosetta Workbench** corresponds to the toolset that supports the CDM, through 4 sets of functions:

* The **grammar**, which is specified as part of a Domain Specific Language (DLS) component which has been developed using open source software. While the CDM syntax is based upon this grammar, access to this grammar is needed for using the CDM.
* The **navigation tools**, which expose through a web portal a textual and a graphical interface into the CDM.
* The **ingestion service**, which transforms events and trades expressed using alternative data representations into JSON documents that conform to the CDM.  At present, the ingested trades confirm to the version 5.10 of the FpML standard, and the events are originated using a custom data representation.
* The **code generators**, which are used to produce the executable code projections that are part of the model repository. Those code generators are developed using the same open source software component as the grammar, and access to those is not required for CDM usage purposes.

Those workbench components are presented in the below picture.

.. figure:: rosetta-home.png

The CDM Components
==================

The below **ISDA CDM Components Diagram** lays out the three set of CDM application components:

* The **Rosetta Workbench** corresponds to the 'under the hood' components with respect to the CDM: the Rosetta grammar and the Rosetta code generators, which together form the Rosetta DSL, and the associated test infrastructure and Rosetta portal, which have been developed through bespoke code.
* The **ISDA CDM Distribution** is made available to participants as part of the download available from the Rosetta Portal and is subject to the ISDA CDM licence.  The most crucial components of this ISDA CDM Distribution are the following:

  * The **Model Definition**, which corresponds to the actual Rosetta model, as expressed by the Rosetta syntax and which components are further detailed as part of the CDM Modelling Artefacts section of this documentation.
  * The **Model Code Projection**, currently available as Java and JSON.  As the Rosetta syntax represents not just data components but also logic, the JSON representation has a quite limited scope and usefulness, and might be deprecated at some future point.
  * While the two above components represent the essence of the model and are meant to be used as such by implementers, the **Default Apps** correspond to default implementations which can either be used as such, or be disabled or extended by those participants.  An example of such would be the rosettaKey implementation, which uses the default Java hash code function, but which might be deemed as inappropriate by some participants and hence be replaced by some alternative implementation.

* **CDM Implementations** by service providers. It is expected that a rich eco-system of such licensed application components that are based upon the CDM will develop over time. REGnosys is just the first to have taken the initiative to develop an offering of solutions, which purpose is to assist market participants in making use of the CDM.  ISDA doesn't endorse any of of those application components.


.. figure:: cdm-components-diagram.png

CDM Modelling Artefacts
=======================

The CDM combines **five modelling artefacts**, which are consistently expressed through the Rosetta syntax:

* Data representation
* Mapping
* Data validation
* Object qualification
* Calculation

The below sections of this documentation detail the purpose and features of each of those CDM artefacts, and highlight the relationships that exists among those.

Data Representation Artefacts
-----------------------------

Rosetta makes use of **six data representation components** to represent the CDM:

* Classes
* Attributes
* Enumerations
* Enumerations values
* Choice rules
* Aliases

Classes
^^^^^^^

Purpose
"""""""

CDM classes are objects that contain the granular data representation elements, in the form of attributes.

Syntax
""""""

The class content is delineated by brackets ``{`` ``}``.

The CDM supports the concept of **abstract classes**, which cannot be instantiated as part of the generated executable code and are meant to be extended by other classes.  An example of such is the ``IdentifiedProduct`` class, which acts as the baseline for the products which terms are abstracted through reference data and can be extended by the respective variations of such products, as illustrated by the ``Loan`` class.

.. code-block:: Java

 abstract class IdentifiedProduct <"An abstract class to specify a product which terms are abstracted through reference data.">
 {
  productIdentifier ProductIdentifier (1..1);
 }

 class Loan extends IdentifiedProduct
  [synonym FpML_5_10 value Loan]
 {
  borrower LegalEntity (0..*) <"Specifies the borrower. There can be more than one borrower. It is meant to be used in the event that there is no Bloomberg Id or the Secured List isn't applicable.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value borrower]
  lien string (0..1) scheme <"Specifies the seniority level of the lien.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value lien meta lienScheme]
  facilityType string (0..1) scheme <"The type of loan facility (letter of credit, revolving, ...).">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value facilityType meta facilityTypeScheme]
  creditAgreementDate date (0..1) <"The credit agreement date is the closing date (the date where the agreement has been signed) for the loans in the credit agreement. Funding of the facilities occurs on (or sometimes a little after) the Credit Agreement date. This underlyer attribute is used to help identify which of the company's outstanding loans are being referenced by knowing to which credit agreement it belongs. ISDA Standards Terms Supplement term: Date of Original Credit Agreement.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value creditAgreementDate]
  tranche string (0..1) scheme <"The loan tranche that is subject to the derivative transaction. It will typically be referenced as the Bloomberg tranche number. ISDA Standards Terms Supplement term: Bloomberg Tranche Number.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value tranche meta loanTrancheScheme]
 }

The Rosetta convention is that class names start with a capital letter. Class names need to be unique across the model, including with respect to rule names. Both those are controlled by the Rosetta grammar.

Attributes
^^^^^^^^^^

Purpose
"""""""

Attributes specify the granular model elements in terms of type of value (e.g. ``integer``, ``string``, enumerated value), cardinality and through an associated definition.

Syntax
""""""

A Rosetta attribute can be specified either as a basic type, a class or an enumeration.

The set of **types** available in Rosetta are:

Text - ``string``

Number - ``int`` - ``number``

Logic - ``boolean``

Date and Time - ``date`` - ``time`` - ``zonedDateTime``

As it relates to time zone adjustments, the CDM requires to specify time alongside with a time zone qualifier in one of two ways:

* Through the ``zonedDateTime`` type, which needs to be expressed either as UTC or as an offset to UTC, as specified by the ISO 8601 standard.
* Through the ``BusinessCenterTime`` class, where time is specified alongside a business center.  This is used to specify a time dimension in relation to a future event, e.g. the earliest or latest exercise time of an option.

While there has been discussion as to whether the CDM should support dates which are specified as an offset to UTC with the ``Z`` suffix, no positive conclusion has been reached so far. The main reason is that all dates which need a business date context are already being provided with the ability to specify an associated business center.


Calculation - ``calculation`` (The ``calculation`` qualifier represents the outcome of the CDM interest accrual calculation. It is currently associated with two attributes: ``cashflowCalculation`` in the ``Cashflow`` class, and ``callFunction`` in the ``computedAmount`` class.)

Product and event qualification - ``productType`` - ``eventType``

Rosetta syntax convention is for attribute names to be expressed in lower case, and a warning will be generated by the grammar if this is not the case. Attribute names need to be unique within the context of a class (and within the context of the base class, if a class extends another class), but can be duplicated across classes. The semi-column ``;`` acts as the terminal character for the attribute specification, with associated synonyms being positioned underneath that specification line.

The CDM provides the ability to associate a set of qualifiers to the attributes: the ``id``, ``reference`` and ``scheme`` metaTypes, and the ``rosettaKey`` and ``rosettaKeyValues``.

* The ``id`` and ``reference`` metaTypes replicate the cross-referencing mechanism widely used in the XML space (and particularly as part of the FpML standard) as a way to provide data integrity within the context of an instance document.
* The ``scheme`` metaType specifies scheme references. The relevant scheme value is then specified alongside the synonym information.

  The below ``Party`` and r``ContractIdentifier`` classes provide a good illustration as to how **metaTypes** are implemented, with the ``id`` attribute being associated to the ``Party`` class, while the ``reference`` is associated to the ``partyReference`` attribute of the ``ContractIdentifier`` class.  The ``partyId`` has an associated ``scheme``, which ``partyIdScheme`` value is associated with the relevant synonym sources.

.. code-block:: Java

 class Party <"The party class.">
  [synonym FpML_5_10 value Party]
 {
  id (0..1);
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 meta id]
   [synonym DTCC_11_0, DTCC_9_0 meta id maps 2]
  partyId string (1..*) scheme <"The identifier associated with a party, e.g. the 20 digits LEI code.">;
   [synonym FpML_5_10, DTCC_11_0, DTCC_9_0, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value partyId meta partyIdScheme]
   [synonym DTCC_11_0, DTCC_9_0 value partyId maps 2 meta partyIdScheme]
  legalEntity LegalEntity (0..1);
  naturalPerson NaturalPerson (0..*);
 }

 class ContractIdentifier extends Identifier <"A class defining a trade identifier issued by the indicated party. The CDM doesn't extends the base class PartyAndAccountReference because of the choice logic with the issuer element.">
  [synonym FpML_5_10 value TradeIdentifier]
 {
  partyReference string (0..1) reference <"Reference to a party.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value partyReference meta href]
  accountReference string (0..1) reference <"Reference to an account.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value accountReference meta href]
 }

* The ``rosettaKey`` corresponds to a hash code generated by the CDM as part of the ``EventEffect`` features, which are further detailed below as part of the CDM Model section. In essence, the ``rosettaKey`` hash value associated with the relevant class (``Payment`` in the below snippet) is also associated with the corresponding attribute in the ``EventEffect`` class (in this case, the ``payment`` attribute).

.. code-block:: Java

 class EventEffect <"The set of operational and positional effects associated with a lifecycle event, alongside the reference to the contract reference(s) that is subject to the event (and is positioned in the before state of the event primitive).">
 {
  effectedContract Contract (0..*) rosettaKey <"A pointer to the contract(s) to which the event effect(s) apply, i.e. in the before event state.">;
  contract Contract (0..*) rosettaKey <"A pointer to the contract effect(s), an example of such being the outcome of a new trade, swaption exercise or novation event.">;
  productIdentifier ProductIdentifier (0..*) rosettaKey <"A pointer to the product identifier effect(s), an example of such being the outcome of the physical exercise of a bond option.">;
  transfer Transfer (0..*) rosettaKey <"A pointer to the transfer effect(s), either a cash, security or other asset.">;
 }

 class Transfer rosettaKey <"A class to specify the transfer primitive by providing the ability to combine a set of transfer components which are specialised by asset class.">
 {
  identifier string (0..1) scheme <"The identifier which might be associated with the transfer.">;
   [synonym DTCC_11_0, DTCC_9_0 value tradeCashflowsId path "FpML" meta tradeCashflowsIdScheme]
  settlementType TransferSettlementEnum (0..1) <"The qualification as to how the transfer will settle, e.g. a DvP settlement.">;
   [synonym Rosetta_Workbench value transferType]
   [synonym DTCC_11_0, DTCC_9_0 value SettlementType path "PaymentDetails"]
  settlementDate AdjustableOrAdjustedOrRelativeDate (1..1);
   [synonym Rosetta_Workbench value settlementDate]
  cashTransfer CashTransferComponent (0..*) <"The cash transfer component of the transfer. In the case where several currencies are involved in the transfer, several components should be used, as the component supports one single currency amount.">;
   [synonym Rosetta_Workbench value cashTransfer]
   [synonym DTCC_11_0, DTCC_9_0 value payment path "FpML"]
  securityTransfer SecurityTransferComponent (0..*) <"The security transfer component of the transfer. In the case where several securities are involved in the transfer, several components should be used, as the component supports one single security.">;
   [synonym Rosetta_Workbench value securityTransfer]
  commodityTransfer CommodityTransferComponent (0..*);
  status TransferStatusEnum (0..1) <"The transfer status, e.g. Instructed, Settled...">;
   [synonym Rosetta_Workbench value status]
  settlementReference string (0..1) <"The settlement reference, when applicable.">;
   [synonym Rosetta_Workbench value settlementReference]
 }

* The ``rosettaKeyValue`` is a variation of the ``RosettaKey``, which associated hash function doesn't include any of those qualifiers that are associated with the attributes. The reasoning is that some of those qualifiers are automatically generated by algorithm (typically, the anchors and references associated with XML documents) and would then result in differences between two instance documents, even if those documents would have the same actual values. The ``RosettaKeyValue`` is meant to be used for supporting the reconciliation of economic terms, and is hence associated with the ``EconomicTerms`` class.

.. code-block:: Java

 class EconomicTerms rosettaKeyValue <"This class represents the full set of product economics: the payout component, as well as the legal optional provisions which have valuation implications. A rosettaKey hash is associated to the contractual product economic terms for the purpose of supporting hash-based reconciliations.">
 {
  payout Payout (1..1) <"The payout specification, which can combine several payout terms, e.g. an interest rate and a credit default payout in the case of a credit default swap.">;
  earlyTerminationProvision EarlyTerminationProvision (0..1) <"Parameters specifying provisions relating to the optional and mandatory early termination of a swap transaction.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value earlyTerminationProvision path "trade.swap", earlyTerminationProvision path "trade.capfloor"]
  cancelableProvision CancelableProvision (0..1) <"A provision that allows the specification of an embedded option within a swap giving the buyer of the option the right to terminate the swap, in whole or in part, on the early termination date.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value cancelableProvision path "trade.swap"]
  extendibleProvision ExtendibleProvision (0..1) <"A provision that allows the specification of an embedded option with a swap giving the buyer of the option the right to extend the swap, in whole or in part, to the extended termination date.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value extendibleProvision path "trade.swap"]
 }

Enumerations
^^^^^^^^^^^^

Purpose
"""""""

Enumerations are the mechanism through which controlled values are specified at the attribute level. They are the container for the corresponding set of enumeration values.

As mentioned in the preceding section, with respect to the FpML standard, the schemes which values are specified as part of the standard are represented through enumerations in the CDM, while schemes with no defined values are represented in the CDM as a type ``string``.  In both cases, the scheme reference associated with the originating element is also associated to the relevant synonym sources, one of the CDM principles being that no originating information should be disregarded.

Syntax
""""""

Enumerations are very simple modelling container artefacts. They can have associated synonyms.

Similar to the class, the enumeration is delineated by brackets ``{`` ``}``.

.. code-block:: Java

 enum MarketDisruptionEnum <"The enumerated values to specify the handling of an averaging date market disruption for an equity derivative transaction.">
  [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value marketDisruptionScheme_1_0]
 {
  ModifiedPostponement	<"As defined in section 6.7 paragraph (c) sub-paragraph (iii) of the ISDA 2002 Equity Derivative definitions.">
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value "ModifiedPostponement"],
  Omission	 <"As defined in section 6.7 paragraph (c) sub-paragraph (i) of the ISDA 2002 Equity Derivative definitions.">
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value "Omission"],
  Postponement	 <"As defined in section 6.7 paragraph (c) sub-paragraph (ii) of the ISDA 2002 Equity Derivative definitions.">
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value "Postponement"]
 }

Enumeration Values
^^^^^^^^^^^^^^^^^^

Purpose
"""""""

As indicated in the above section, enumeration values are the set of controlled values that are specified as part of an enumeration container.

Syntax
""""""

Enumeration values have a restricted syntax for the purpose of facilitating their integration with executable code: they cannot start with a numerical digit, and the only special character that can be associated with them is the underscore ``_``.

In order to handle the integration of FpML scheme values such as the *dayCountFractionScheme* which has values such as ``ACT/365.FIXED`` or ``30/360``, the Rosetta syntax provides the ability to associate a **displayName synonym**. Those values are then specified in the CDM as ``ACT_365_FIXED`` and ``_30_360``, with the associated display names of ``ACT/365.FIXED`` and ``30/360``, respectively.

.. code-block:: Java

 enum DayCountFractionEnum <"The enumerated values to specify the day count fraction.">
  [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value dayCountFractionScheme_2_2]
 {
  (..)
  _1_1 displayName "1/1" <"Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (a) or Annex to the 2000 ISDA Definitions (June 2000 Version), Section 4.16. Day Count Fraction, paragraph (a).">
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value "1/1"],
  _30E_360 displayName "30E/360" <"Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (g) or Annex to the 2000 ISDA Definitions (June 2000 Version), Section 4.16. Day Count Fraction, paragraph (f). Note that the algorithm defined for this day count fraction has changed between the 2000 ISDA Definitions and 2006 ISDA Definitions. See Introduction to the 2006 ISDA Definitions for further information relating to this change.">
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value "30E/360"],
  _30E_360_ISDA displayName "30E/360.ISDA" <"Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (h). Note the algorithm for this day count fraction under the 2006 ISDA Definitions is designed to yield the same results in practice as the version of the 30E/360 day count fraction defined in the 2000 ISDA Definitions. See Introduction to the 2006 ISDA Definitions for further information relating to this change.">
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value "30E/360.ISDA"],
  _30_360 displayName "30/360" <"Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (f) or Annex to the 2000 ISDA Definitions (June 2000 Version), Section 4.16. Day Count Fraction, paragraph (e).">
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value "30/360"]
 }

The **synonym syntax** associated with enumeration values differs in two respects from the synonyms associated with other CDM artefacts:

* The synonym value is of type ``string``, for the above reason related to the need to facilitate integration with executable code.  (The alternative approach consisting in specifying the value as a compatible identifier alongside with a display name has been disregarded because it has been deemed not appropriate to create a 'code-friendly' value for the respective synonyms.  A ``string`` type removes such need.)
* Although this use case is not part of the current CDM scope, the ability to associate a definition to a synonym value has been enabled, the objective being to effectively support the FIX use cases where the synonym value is a letter or numerical code, which is then positioned as the prefix of the associated definition. Although not part of the CDM 1.0 scope, the ``TimeInForceEnum`` illustrates this approach:

.. code-block:: Java

 enum TimeInForceEnum <"The enumeration values to specify the period of time during which an order remains in effect.">
  [synonym FIX value TimeInForce tag 59]
 {
   Day <"Day (or session)">
    [synonym FIX value "0" definition "0 = Day (or session)"],
   GoodTillCancel <"Good Till Cancel (GTC)">
    [synonym FIX value "1" definition "1 = Good Till Cancel (GTC)"],
   (...)
 }

Choice Rules
^^^^^^^^^^^^

Purpose
"""""""

Choice rules apply within the context of a class. They define a choice constraint between a set of attributes. They are meant as a simple and robust construct to translate the XML *xsd:choicesyntax* as part of any model created using Rosetta, although their usage is not limited to those XML use cases.

Syntax
""""""

Choice rules only apply within the context of a class, and the naming convention is ``<className>_choice``, e.g. ``TradeIdentifier_choice``. If multiple choice rules exist in relation to a class, the naming convention is to suffix the 'choice' term with a number, e.g. ``TradeIdentifier_choice1`` and ``TradeIdentifier_choice2``.

.. code-block:: Java

 class ContractIdentifier extends Identifier <"A class defining a trade identifier issued by the indicated party. The CDM doesn't extends the base class PartyAndAccountReference because of the choice logic with the issuer element.">
  [synonym FpML_5_10 value TradeIdentifier]
 {
  partyReference string (0..1) reference <"Reference to a party.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value partyReference meta href]
  accountReference string (0..1) reference <"Reference to an account.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value accountReference meta href]
 }

 choice rule ContractIdentifier_choice <"Choice rule to represent an FpML choice construct.">
  for ContractIdentifier required choice between
  issuer and partyReference

The choice constraint can either be **required** (implying that exactly one of the attributes needs to be present) or **optional** (implying that at most one of the attributes needs to be present).

While most of the choice rules have two attributes, there is no limit to the number of attributes associated with it… within the limit of the number of attributes associated with the class at stake. ``OptionCashSettlement_choice`` is a good illustration of this.

.. code-block:: Java

 choice rule OptionCashSettlement_choice <"Choice rule to represent an FpML choice construct.">
  for OptionCashSettlement optional choice between
  cashPriceMethod and cashPriceAlternateMethod and parYieldCurveAdjustedMethod and zeroCouponYieldAdjustedMethod
  and parYieldCurveUnadjustedMethod and crossCurrencyMethod and collateralizedCashPriceMethod

Members of a choice rule need to have their lower cardinality set to 0, something which is enforced by a validation rule.

One of syntax as a complement to the choice rule
""""""""""""""""""""""""""""""""""""""""""""""""

In the case where all the attributes of a given class are subject to a choice logic, Rosetta provides the ability to qualify the class information with the ``one of`` qualifier.  This feature is illustrated by the ``BondOptionStrike`` class.

.. code-block:: Java

 class BondOptionStrike one of <"A class to specify the strike of a bond or convertible bond option.">
  [synonym FpML_5_10 value BondOptionStrike]
 {
  referenceSwapCurve ReferenceSwapCurve (0..1) <"The strike of an option when expressed by reference to a swap curve. (Typically the case for a convertible bond option.)">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value referenceSwapCurve]
  price OptionStrike (0..1);
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value price]
 }

Aliases
^^^^^^^

Purpose
"""""""

Two related considerations stand behind the introduction of aliases as part of the Rosetta syntax:

* The recognition that model tree expressions can be cumbersome at time and hence may contradict the primary goals of clarity and legibility.
* Aliases can be reused across the various modelling artefacts that make use of those, i.e. currently data rule, event and product qualification, calculation and projection rules (note that this latter artefact is not currently used as part of the CDM).


Syntax
""""""

The alias syntax is straightforward: ``alias <name> <Rosetta expression>``.

The alias name needs to be unique across the product and event qualifications, the classes and the aliases, and validation logic is in place to enforce this.  The naming convention is to have one CamelCased word, instead of a composite name as for the Rosetta rules, with implied meaning.

The below snippet presents an example of such alias and its use as part of an event qualification.

.. code-block:: Java

 alias novatedContractEffectiveDate
  Event -> primitive -> inception -> after -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> calculationPeriodDates -> effectiveDate -> date
  or Event -> primitive -> inception -> after -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> calculationPeriodDates -> effectiveDate -> adjustableDate -> adjustedDate
  or Event -> primitive -> inception -> after -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> calculationPeriodDates -> effectiveDate -> adjustableDate -> unadjustedDate

 isEvent Novation <"The qualification of a novation event from the fact that (i) the intent is Novation when specified, (ii) the primitives quantityChange and inception exist, (iii) the remaining quantity = 0, (iv) the closedState of the contract is Novated, (v) the stepped-in contract has a different contract identifier than the novated contract, (vi) the stepped-in contract has the novation event date and the novation event effective date, and (vii) the contract counterparties have changed.">
  Event -> intent when present = IntentEnum.Novation
  and Event -> primitive -> quantityChange exists
  and Event -> primitive -> inception exists
  and quantityAfterQuantityChange = 0.0
  and Event -> primitive -> quantityChange -> after -> contract -> closedState -> state = ClosedStateEnum.Novated
  and Event -> primitive -> inception -> after -> contract -> contractIdentifier <> Event -> primitive -> quantityChange -> before -> contract -> contractIdentifier
  and Event -> eventDate = Event -> primitive -> inception -> after -> contract -> tradeDate -> date
  and Event -> effectiveDate = novatedContractEffectiveDate

Mapping Artefacts
-----------------

Synonyms
^^^^^^^^

Purpose
"""""""

Synonym is the baseline building block in the relationship between the CDM and alternative data representations, whether those are open standards or proprietary data representations. It can be complemented by relevant mapping logic when the relationship is not a one-to-one or is conditional.

Synonyms can be associated to all four sets of Rosetta data modelling artefacts:

*  Classes
*  Attributes
*  Enumerations
*  Enumeration values

There is no limit to the number of synonyms that can be associated with each of those artefacts, and there can even be several synonyms for a given data source (e.g. in the case of a conditional mapping).

The following set of synonym sources are currently in place as part of the CDM:

* **FpML standard**: synonymity to the version 5.10 of the standard through the ``FpML_5_10`` synonym source
* **FIX standard**: synonymity to the version 5.0 SP2 of the standard through the ``FIX_5_0_SP2`` synonym source
* **ISO 20022 standard**: synonymity to the standard throught the ``ISO_20022`` synonym source, with no version reference at present
* **Rosetta workbench**: synonymity to the *event.xsd* schema used for the purpose of ingesting sample lifecycle events through the ``Rosetta_Workbench`` synonym source
* **DTCC**: synonymity to the *OTC_Matching_11-0.xsd* schema (including the imported FpML schema version 4.9) that is used for trade matching confirmations through the ``DTCC_11_0`` synonym source, and synonymity to the *OTC_Matching_9-0.xsd* schema (also including the imported FpML schema version 4.9) that is used for payment notifications through the ``DTCC_9_0`` synonym source
* **CME**: synonymity to the *cme-conf-ext-1-17.xsd* schema (including the imported FpML schema version 5.0) that is used fo the clearing confirmation purposes through the ``CME_ClearedConfirm_1_17`` synonym source, and synonymity to the *bloombergTradeFixml* schema (including the imported FpML schema version 4.6) that is used for clearing submissions through the ``CME_SubmissionIRS_1_0`` synonym source
* **AcadiaSoft**: synonymity to the version 1 of the Agreement Manager through the ``AcadiaSoft_AM_1_0`` synonym source

Syntax
""""""

The baseline synonym syntax has two components:

* The **source**, whose possible values are controlled by the grammar and which current values are listed above;
* The **value**, which is of type ``identifier``.

Example:

  ``[synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value averagingInOut]``

A further set of attributes can be associated with a synonym, to address specific use cases:

* A **path** which purpose is allows mapping in cases where the data is nested in different ways between the respective models.  The ``Payout`` class is a good illustration of such cases:

.. code-block:: Java

 class Payout <"The payout can be specified through a number of combinations, e.g. by associating several interest rate payouts to specify an interest rate swap, or a credit default and an interest rate payout to specify a credit default swap. The implied product is inferred by the isProduct CDM artefact. Each of the payout classes have an associated rosettaKeyValue which can be referenced by implementers as part of their grossCashflow attribute in the Transfer class, but establishing a lineage between this computed cashflow and the originating payout, when applicable.">
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
  cashflow Cashflow (0..*) <"A cashflow between the parties to the trade. For interest rate products, this corresponds to the FpML additionalPayment element. For credit default swaps, this corresponds to the FpML initialPayment element and the singlePayment element of the fee leg. For option products, which corresponds to the FpML premium element.">;
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

* A **tag** or a **componentID** can be associated to a synonym value. In both cases, the purpose is to properly reflect the FIX standard, which makes use of those two artefacts. There are only two examples of such at present in the model, as a result of the scope focus on post-execution use cases and, hence, the limited reference to the FIX standard.

.. code-block:: Java

 class Strike <"A class describing a single cap or floor rate.">
  [synonym FpML_5_10 value Strike]
 {
  id (0..1);
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 meta id]
  strikeRate number (1..1) <"The rate for a cap or floor.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value strikeRate]
   [synonym FIX value StrikePrice tag 202]
  buyer PayerReceiverEnum (0..1) <"The buyer of the option.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value buyer]
  seller PayerReceiverEnum (0..1) <"The party that has sold.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value seller]
 }

* A **definition** can be associated with the enumeration value synonyms, the purpose being to provide a more explicit reference to the FIX enumeration values, which are specified through a single digit or letter, which value is then positioned as a prefix to the associated definition.  The only examples of such currently available in the model are associated with the enumeration ``InformationProviderEnum``:

.. code-block:: Java

 enum InformationProviderEnum <"The enumerated values to specify the list of information providers.">
	[synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value informationProviderScheme_2_1]
 {
  (...)
	Bloomberg <"Bloomberg LP.">
	 [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value "Bloomberg"]
	 [synonym FIX value "0" definition "0 = Bloomberg"],
	(...)
	Other
	 [synonym FIX value "99" definition "99 = Other"],
	(...)
	Telerate <"Telerate, Inc.">
	 [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value "Telerate"]
	 [synonym FIX value "2" definition "2 = Telerate"]
 }

Mapping Logic
^^^^^^^^^^^^^

Purpose
"""""""

There are cases where the relationship between the marketplace standards and protocols and their relation to the CDM is not one-to-one or is conditional.

Hence, the need to complement the synonyms with a syntax that provides the ability to express a mapping logic in a manner that provides a good balance between flexibility and legibility.

Syntax
""""""

The mapping logic differs from the data rule, choice rule and calculation syntax in that its syntax is not expressed as a stand-alone block with a qualifier prefix such as ``rule``. Rather, the mapping rule is positioned as an extension to the synonym expression, and each of the mapping expressions (several mapping expressions can be associated with a given synonym) is prefixed with the ``set`` qualifier, followed by the name of the Rosetta attribute to which the synonym is being mapped to.

The mapping syntax is composed of two (optional) expressions: a **mapping value** that is prefixed with ``to``, which purpose is to provide the ability to map a specific value that is distinct from the one originating from the source document, and a **conditional expression** that is prefixed with ``when``, which purpose is to associate conditional logic to the mapping expression.

The mapping logic associated with the below ``action`` attribute provides a good illustration of such logic.

.. code-block:: Java

 class Event
 {
  (...)
  action ActionEnum (1..1) <"Specifies whether the event is a new, a correction or a cancellation.">;
   [synonym Rosetta_Workbench
    set to ActionEnum.New when "isCorrection" = False,
    set to ActionEnum.Correct when "isCorrection" = True,
    set to ActionEnum.Cancel when "isRetraction" = True]
   [synonym FpML_5_10
    set to ActionEnum.New when "isCorrection" = False,
    set to ActionEnum.Correct when "isCorrection" = True]
   [synonym DTCC_11_0, DTCC_9_0 value Activity path "Header.OTC_RM.Manifest.TradeMsg"]
   [synonym CME_SubmissionIRS_1_0 value TransTyp path "TrdCaptRpt"]
  (...)
 }

Data Validation Artefacts
-------------------------

Data Rules
^^^^^^^^^^

Purpose
"""""""

Data rules are the primary channel through which data validation is enforced as part of Rosetta.

A good initial illustration of such role relates to how data constraints specified as part of the FpML documentation are expressed as part of those rules – and hence become part of the executable code case that is generated from the model.

As an example, the ``FpML_ird_57`` data rule implements the **FpML ird validation rule #57**, which states that if the notional step schedule is absent, then the initial value of the notional schedule must not be null.  While at present the FpML logic needs to be evaluated and transcribed into code by the relevant teams (with the implication that, more often than not, such logic is actually not enforced), its programmatic implementation is available alongside a legible view of it as part of Rosetta.

.. code-block:: Java

 class CalculationPeriodFrequency extends Frequency <"A class to specify the frequency at which calculation period end dates occur within the regular part of the calculation period schedule and their roll date convention.">
  [synonym FpML_5_10 value CalculationPeriodFrequency]
 {
  rollConvention RollConventionEnum (1..1) <"Used in conjunction with a frequency and the regular period start date of a calculation period to determine the calculation period end date within the regular part of the calculation period.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value rollConvention]
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

Syntax
""""""

Data rules apply to classes and associated attributes.

Their name needs to be unique across the model, and the naming convention often used is in the form of ``<className>_<attributeName>`` where attributeName refers to the attribute to which the rule applies. If the data rule applies to several attributes, it is appropriate to have a naming in the form of ``<className>_<attributeName1>_<attributeName2>``.

Variations from this naming convention are needed, as in the case of the data rules that implement FpML data validation rules, the ``FpML_rule_#`` convention has been used.

The main data rule syntax is in the form of ``when <Rosetta expression> then <Rosetta expression>``.

Here are a set of relevant examples of this data rule syntax:

* ``CalculationPeriodDates_firstCompoundingPeriodEndDate`` combines three Boolean assertions.

 .. code-block:: Java

  data rule CalculationPeriodDates_firstCompoundingPeriodEndDate <"FpML specifies that the firstCompoundingPeriodEndDate must only be specified when the compounding method is specified and not equal to a value of None.">
   when InterestRatePayout -> compoundingMethod is absent
    or InterestRatePayout -> compoundingMethod = CompoundingMethodEnum.None
   then InterestRatePayout -> calculationPeriodDates -> firstCompoundingPeriodEndDate is absent

* ``CalculationPeriod_calculationPeriodNumberOfDays`` involves an operator.

 .. code-block:: Java

  data rule CalculationPeriod_calculationPeriodNumberOfDays <"FpML specifies calculationPeriodNumberOfDays as a positive integer.">
   when PaymentCalculationPeriod -> calculationPeriod -> calculationPeriodNumberOfDays exists
   then PaymentCalculationPeriod -> calculationPeriod -> calculationPeriodNumberOfDays >= 0

* ``Obligations_physicalSettlementMatrix`` makes use of parentheses for the purpose of supporting nested assertions.

.. code-block:: Java

 data rule Obligations_physicalSettlementMatrix <"The below set of obligation of the reference entity are specified as optional boolean in FpML and the CDM because they would be specified as part of the Physical Settlement Matrix when such document governs the contract terms. As a result, this data rule specifies that those provisions cannot be omitted if the Physical Settlement Matrix governs the terms of the contract. This data rule also applies to cash settled contracts because those could still end-up being physically settled, in case the case where an auction could not take place because of, say, liquidity considerations.">
  when ( Contract -> documentation -> contractualMatrix -> matrixType <> MatrixTypeEnum.CreditDerivativesPhysicalSettlementMatrix
   or Contract -> documentation -> contractualMatrix -> matrixType is absent )
   and Contract -> contractualProduct -> economicTerms -> payout -> creditDefaultPayout -> protectionTerms -> obligations exists
  then ( Contract -> contractualProduct -> economicTerms -> payout -> creditDefaultPayout -> protectionTerms -> obligations -> notSubordinated
   and Contract -> contractualProduct -> economicTerms -> payout -> creditDefaultPayout -> protectionTerms -> obligations -> notSovereignLender
   and Contract -> contractualProduct -> economicTerms -> payout -> creditDefaultPayout -> protectionTerms -> obligations -> notDomesticLaw
   and Contract -> contractualProduct -> economicTerms -> payout -> creditDefaultPayout -> protectionTerms -> obligations -> notDomesticIssuance
  ) exists
  and (
   Contract -> contractualProduct -> economicTerms -> payout -> creditDefaultPayout -> protectionTerms -> obligations -> fullFaithAndCreditObLiability
   or Contract -> contractualProduct -> economicTerms -> payout -> creditDefaultPayout -> protectionTerms -> obligations -> generalFundObligationLiability
   or Contract -> contractualProduct -> economicTerms -> payout -> creditDefaultPayout -> protectionTerms -> obligations -> revenueObligationLiability
  ) exists


Object Qualification Artefacts
------------------------------

The CDM modelling approach consists in inferring the product and event qualification from their relevant attributes, rather than qualifying those upfront.  As a result, the Rosetta syntax has been adjusted to meet this requirement, with slight variations in the implementation across those two use cases.

The CDM Model section of this documentation details the positioning of those product and event qualification artefacts as part of the CDM and their representation as part of the associated object instantiations.

Product Qualification
^^^^^^^^^^^^^^^^^^^^^

18 interest rate derivative products have so been qualified as part of the CDM, in effect representing the full ISDA V2.0 scope.  Credit derivatives have not yet been qualified because their ISDA taxonomy is based upon the underlying transaction type, instead of the product features as for the interest rate swaps.  Follow-up is in progress with the ISDA Credit Group to evaluate whether an alternative product qualification could be developed that would leverage the approach adopted for interest rate derivatives.

Purpose
"""""""

The product qualification leverages the **alias** syntax presented earlier in this documentation, by qualifying a product from its economic terms, those latter being expressed through a set of assertions associated with modelling components.

Syntax
""""""

The product qualification syntax is as follows: ``isProduct <name> <Rosetta expression>``.

The product name needs to be unique across the product and event qualifications, the classes and the aliases, and validation logic is in place to enforce this. The naming convention is to have one CamelCased word.

The CDM makes use of the ISDA taxonomy V2.0 leaf level to qualify the event.  The synonymity with the ISDA taxonomy V1.0 has been systematically indicated as part of the model upon request from CDM group participants, who pointed out that a number of them use it internally.

.. code-block:: Java

 isProduct InterestRate_InflationSwap_Basis_YearOn_Year
  [synonym ISDA_Taxonomy_v1 value InterestRate_IRSwap_Inflation]
  EconomicTerms -> payout -> interestRatePayout -> interestRate -> floatingRate count = 1
  and EconomicTerms -> payout -> interestRatePayout -> interestRate -> inflationRate count = 1
  and EconomicTerms -> payout -> interestRatePayout -> interestRate -> fixedRate is absent
  and EconomicTerms -> payout -> interestRatePayout -> crossCurrencyTerms -> principalExchanges is absent
  and EconomicTerms -> payout -> optionPayout is absent
  and EconomicTerms -> payout -> interestRatePayout -> paymentDates -> paymentFrequency -> periodMultiplier = 1
  and EconomicTerms -> payout -> interestRatePayout -> paymentDates -> paymentFrequency -> period = PeriodExtendedEnum.Y

Event Qualification
^^^^^^^^^^^^^^^^^^^

23 lifecycle events have currently been qualified as part of the CDM.

Purpose
"""""""

Similar to the product qualification syntax, the purpose of the event qualifier is to qualify a product from the existence of the a set of modelling attributes.

Syntax
""""""

The event qualification syntax is similar to the product and the alias, the difference being that it is possible to associate a set of data rules to it.

The event name needs to be unique across the product and event qualifications, the classes and the aliases, and validation logic is in place to enforce this.  The naming convention is to have one CamelCased word.

The ``Increase`` illustrates quite well how the syntax qualifies this event by requiring that five conditions be met:

* When specified, the value associated with the ``intent`` attribute of the ``Event`` class must be ``Increase``;
* The ``QuantityChange`` primitive must exist, possibly alongside the ``Transfer`` one;
* The quantity/notional in the before state must be lesser than in the after state. This latter argument makes use of the ``quantityBeforeQuantityChange`` and ``quantityAfterQuantityChange`` aliases;
* The ``changedQuantity`` attribute must be absent (note that a later syntax enhancement will aim at confirming that this attribute corresponds to the difference between the before and after quantity/notional);
* The ``closedState`` attribute must be absent.

.. code-block:: Java

 isEvent Increase <"The qualification of a increase event from the fact that (i) the intent is Increase when specified, (ii) the associated primitives are the quantityChange and the cash transfer, the (iii) the quantity associated with the contract increases.">
  Event -> intent when present = IntentEnum.Increase
  and ( Event -> primitive -> quantityChange only exists
   or ( Event -> primitive -> quantityChange and Event -> primitive -> transfer -> cashTransfer ) exists )
  and quantityBeforeQuantityChange < quantityAfterQuantityChange
  and changedQuantity > 0.0
  and Event -> primitive -> quantityChange -> after -> contract -> closedState is absent

  alias quantityBeforeQuantityChange <"The alias to represent the quantity or notional amount in terms of number of units (i.e. ignoring the currency or units denomination) before a quantity change primitive.">
   Event -> primitive -> quantityChange -> before -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> quantity -> quantity -> amount
   and Event -> primitive -> quantityChange -> before -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> quantity -> notionalAmount -> amount
   and Event -> primitive -> quantityChange -> before -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> quantity -> notionalSchedule -> notionalStepSchedule -> initialValue
   and Event -> primitive -> quantityChange -> before -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> quantity -> notionalSchedule -> notionalStepSchedule -> step -> stepValue
   and Event -> primitive -> quantityChange -> before -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> quantity -> notionalSchedule -> notionalStepParameters -> notionalStepAmount
   and Event -> primitive -> quantityChange -> before -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> quantity -> fxLinkedNotional -> initialValue
   and Event -> primitive -> quantityChange -> before -> contract -> contractualProduct -> economicTerms -> payout -> creditDefaultPayout -> protectionTerms -> notionalAmount -> amount
   and Event -> primitive -> quantityChange -> before -> contract -> contractualProduct -> economicTerms -> payout -> optionPayout -> quantity -> notionalAmount -> amount

  alias quantityAfterQuantityChange <"The alias to represent the quantity or notional amount in terms of number of units (i.e. ignoring the currency or units denomination) after a quantity change primitive.">
   Event -> primitive -> quantityChange -> after -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> quantity -> quantity -> amount
   and Event -> primitive -> quantityChange -> after -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> quantity -> notionalAmount -> amount
   and Event -> primitive -> quantityChange -> after -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> quantity -> notionalSchedule -> notionalStepSchedule -> initialValue
   and Event -> primitive -> quantityChange -> after -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> quantity -> notionalSchedule -> notionalStepSchedule -> step -> stepValue
   and Event -> primitive -> quantityChange -> after -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> quantity -> notionalSchedule -> notionalStepParameters -> notionalStepAmount
   and Event -> primitive -> quantityChange -> after -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> quantity -> fxLinkedNotional -> initialValue
   and Event -> primitive -> quantityChange -> after -> contract -> contractualProduct -> economicTerms -> payout -> creditDefaultPayout -> protectionTerms -> notionalAmount -> amount
   and Event -> primitive -> quantityChange -> after -> contract -> contractualProduct -> economicTerms -> payout -> optionPayout -> quantity -> notionalAmount -> amount

Calculation Artefacts
---------------------

Purpose
^^^^^^^

One of the objectives of the CDM Initial Phase has been to express in a machine executable format some of the ISDA Definitions as a way to confirm the extent to which this digital CDM solution can be used.

The ISDA 2006 definitions of the **Fixed Amount** and **Floating Amount** have been used as an initial scope.

To this effect, the grammar component of the Rosetta workbench has been extended as a way to express a syntax that can support such expressions.

Syntax
^^^^^^

The calculation syntax has three components: the **calculation** itself, the **argument** used as an input to that calculation and (possibly) associated **function**.

The application of this syntax to the ``ACT/365.FIXED`` ISDA day count fraction definition provides a good illustration of that syntax:

.. code-block:: Java

 calculation DayCountFractionEnum._30E_360 <"2006 ISDA Definition Article 4 section 4.16(g): If '30E/360' or 'Eurobond Basis' is specified, the number of days in the Calculation Period or Compounding Period in respect of which payment is being made divided by 360, calculated on a formula basis as follows:[[360 x (Y2 - Y1)] + [30 x (M2 - M1)] + (D2 - D1)]/360">
 {
   number: (360 * (endYear - startYear) + 30 * (endMonth - startMonth) + (endDay - startDay)) / 360
 }

.. code-block:: Java

 arguments DayCountFractionEnum._30E_360 <"2006 ISDA Definition Article 4 section 4.16(g). 'Y1' is the year, expressed as a number, in which the first day of the Calculation Period or Compounding Period falls; 'Y2' is the year, expressed as a number, in which the day immediately following the last day included in the Calculation Period or Compounding Period falls; 'M1' is the calendar month, expressed as a number, in which the first day of the Calculation Period or Compounding Period falls; 'M2' is the calendar month, expressed as a number, in which the day immediately following the last day included in the Calculation Period or Compounding Period falls; 'D1' is the first calendar day, expressed as a number, of the Calculation Period or Compounding Period, unless such number would be 31, in which case D1 will be 30; and 'D2' is the calendar day, expressed as a number, immediately following the last day included in the Calculation Period or Compounding Period, unless such number would be 31, in which case D2 will be 30.">
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

 function ResolveRateIndex( index FloatingRateIndexEnum ) <"The function to specify that the floating rate index enumeration will be expressed as a number once the rate is observed.">
 {
  rate number;
 }

CDM Model
=========

This section presents an outline of the **five dimensions of the CDM model representation**:

* products
* events
* legal agreements
* interest calculation
* reference data

Product Model
-------------

CDM provides a composite product model whereby:

* The economic terms are specified by composition, leveraging the FpML building blocks to the extent possible while also looking for further consistency and simplicity whenever possible;
* The product qualification is inferred from those economic terms.

The current CDM scope is limited to contractual derivative products. Listed products, loans and mortgages are represented only in relation to the features needed to position those as underlyers of such derivative products.

Contractual Derivative Products
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The scope of products implemented as part the current scope is as follows:

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

Post-execution: the contract
""""""""""""""""""""""""""""

Contractual products are bilateral contracts between two parties, which terms are specified at trade inception and apply throughout the life of the contract.  Contractual products are fungible only under specific terms (e.g. existence of a close-out netting agreement between the parties).

The CDM ``Contract`` class incorporates all the elements that are part of the FpML *Trade* confirmation view, with the exception of the following elements: *tradeSummary*, *originatingPackage*, *allocations* and *approvals*.

The Rosetta ``Contract`` class includes a ``closedState`` attribute whose purpose is to specify what led to the contract closure alongside with the dates on which this closure took effect.

.. code-block:: Java

 class Contract rosettaKey <"A class to specify a contract object, which can be invoked either within the context of an event, or independently from it. It corresponds to the FpML Trade, although restricted to execution and post-execution contexts. Attributes also applicable to pre-execution (a.k.a. pre-trade view in FpML) contexts have been positioned as part of the ContractualProduct class.">
 {
  id (0..1);
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 meta id path "trade"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17, Rosetta_Workbench meta id]
  contractIdentifier Identifier (1..*) <"The contract identifier, which has 3 components: an actual identifier, an issuer and a version number. There can be several contract identifier, an example of such being a contract is reportable to both the CFTC and ESMA and then has an associated USI (Unique Swap Identifier) UTI (Unique Trade Identifier).">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0 value partyTradeIdentifier path "trade.tradeHeader"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0 value partyTradeIdentifier path "tradeHeader"]
   [synonym CME_ClearedConfirm_1_17 value partyTradeIdentifier path "trade.tradeHeader"]
   [synonym CME_ClearedConfirm_1_17 value universalSwapIdentifier path "trade.tradeHeader"]
   [synonym DTCC_11_0, DTCC_9_0 value Submitter]
   [synonym DTCC_11_0, DTCC_9_0 value ContraTradeId]
   [synonym DTCC_11_0, DTCC_9_0 value YourTradeId]
   [synonym DTCC_11_0, DTCC_9_0 value tradeIdentifyingItems]
   [synonym DTCC_11_0, DTCC_9_0 value USI]
   [synonym DTCC_11_0, DTCC_9_0 value OriginatingUSI]
   [synonym DTCC_11_0, DTCC_9_0 value UTI]
  tradeDate DateInstances (1..1) <"The trade date. This is the date on which the trade was originally executed. The trade date is specified through the DateInstances class so that in the case of a novation the novated part of the trade be reported (by both the remaining party and the transferee) using the date on which the novation was agreed on. The remaining part of a trade is to be reported (by both the transferor and the remaining party) using a trade date corresponding to the original execution date.">;
  clearedDate date (0..1) <"If the trade was cleared (novated) through a central counterparty clearing service, this represents the date the trade was cleared (transferred to the central counterparty).">;
   [synonym FpML_5_10, DTCC_11_0, DTCC_9_0, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value clearedDate path "trade.tradeHeader"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value clearedDate path "tradeHeader"]
  contractualProduct ContractualProduct (1..1) <"The contractual product information that is associated with the contract. The corresponding FpML construct is the product abstract element and the associated substitution group.">;
  collateral Collateral (0..1) <"Defines the collateral obligations of a party.">;
   [synonym FpML_5_10, DTCC_11_0, DTCC_9_0, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value collateral path "trade"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value collateral]
  documentation Documentation (0..1) <"Defines the definitions that govern the document and should include the year and type of definitions referenced, along with any relevant documentation (such as master agreement) and the date it was signed.">;
   [synonym FpML_5_10, DTCC_11_0, DTCC_9_0, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value documentation path "trade"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value documentation]
  governingLaw GoverningLawEnum (0..1) scheme <"Identification of the law governing the transaction.">;
   [synonym FpML_5_10, DTCC_11_0, DTCC_9_0, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value governingLaw path "trade" meta governingLawScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value governingLaw]
  party Party (0..*) <"The parties to the contract. The cardinality is optional to address the case where the contract object is part of an event record, in which case the party is specified as part of that object. In that respect, the CDM approach is distinct from FpML, where party information is specified as part of a wrapper alongside the trade object.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value party]
  account Account (0..*) <"Optional account information.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0 value account]
  partyRole PartyRole (0..*) <"The role(s) that party(ies) may have in relation to the contract, further to the principal parties (i.e payer/receive or buyer/seller) to it.">;
  calculationAgent CalculationAgent (0..1) <"The ISDA calculation agent responsible for performing duties as defined in the applicable product definitions.">;
  partyContractInformation PartyContractInformation (0..*) <"Additional contract information that may be provided by each involved party.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0 value partyTradeInformation path "trade.tradeHeader"]
   [synonym CME_ClearedConfirm_1_17 value partyTradeInformation path "trade.tradeHeader"]
  closedState ClosedState (0..1) <"The qualification of what led to the contract closure alongside with the dates on which this closure took effect.">;
   [synonym Rosetta_Workbench value closedState]
 }

The scope of the contract is limited to the post-execution lifecycle, as it involves legal entities and has a set of attributes which are only qualified at the execution and post-execution stage: trade date, calculation agent, documentation, governing law, etc.

The economic terms of the contract are positioned as part of the ``contractualProduct`` attribute, alongside the product identification and product taxonomy information.

.. code-block:: Java

 class ContractualProduct <"The contractual product class is meant to be used across the entire pre-execution, execution and (as part of the Contract) post-execution lifecycle contexts.">
 {
  productIdentification ProductIdentification (0..1);
  productTaxonomy ProductTaxonomy (1..*) <"The product taxonomy value(s) associated with a contractual product.">;
  economicTerms EconomicTerms (1..1);
 }

In this respect, the CDM ``contract`` corresponds to the confirmation view of the FpML *trade*, while the ``contractualProduct`` corresponds to the pre-trade view of the FpML *trade*.  (The FpML *trade* term has not been used as part of the CDM because deemed ambiguous in this respect.  Its use as part of the standard is largely due to an exclusive focus on post-execution activity in the initial stages of its development. Later adjustments in this respect would have been made difficult as a result of backward compatibility considerations.)


The economic terms
""""""""""""""""""

The CDM ``EconomicTerms`` class ands the underlying ``Payout`` class represent a significant departure from the FpML standard. While FpML qualifies the product through the *product* substitution group, CDM specifies the various set of possible economic terms as part of those afore mentioned ``economicTerms`` and ``payout`` classes.  A contractual product will then consist in an assembling of such economic terms, from which the product qualification will be syntactically inferred.

.. code-block:: Java

 class EconomicTerms rosettaKeyValue <"This class represents the full set of product economics: the payout component, as well as the legal optional provisions which have valuation implications. A rosettaKey hash is associated to the contractual product economic terms for the purpose of supporting hash-based reconciliations.">
 {
  payout Payout (1..1) <"The payout specification, which can combine several payout terms, e.g. an interest rate and a credit default payout in the case of a credit default swap.">;
  earlyTerminationProvision EarlyTerminationProvision (0..1) <"Parameters specifying provisions relating to the optional and mandatory early termination of a swap transaction.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value earlyTerminationProvision path "trade.swap", earlyTerminationProvision path "trade.capfloor"]
  cancelableProvision CancelableProvision (0..1) <"A provision that allows the specification of an embedded option within a swap giving the buyer of the option the right to terminate the swap, in whole or in part, on the early termination date.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value cancelableProvision path "trade.swap"]
  extendibleProvision ExtendibleProvision (0..1) <"A provision that allows the specification of an embedded option with a swap giving the buyer of the option the right to extend the swap, in whole or in part, to the extended termination date.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value extendibleProvision path "trade.swap"]
 }

The ``Payout`` class provides some provide some appropriate insight into the respective product representation between FpML and the CDM, through the relevant synonym sources and associated path expressions.  As an example, one can see that the FpML *feeLeg* is represented through the CDM ``interestRatePayout``, while the FpML *singlePayment* and *initialPayment* are both represented through the CDM ``cashflow``.

.. code-block:: Java

 class Payout <"The payout can be specified through a number of combinations, e.g. by associating several interest rate payouts to specify an interest rate swap, or a credit default and an interest rate payout to specify a credit default swap. The implied product is inferred by the isProduct CDM artefact. Each of the payout classes have an associated rosettaKeyValue which can be referenced by implementers as part of their grossCahflow attribute in the Transfer class, but establishing a lineage between this computed cashflow and the originating payout, when applicable.">
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
  cashflow Cashflow (0..*) <"A cashflow between the parties to the trade. For interest rate products, this corresponds to the FpML additionalPayment element. For credit default swaps, this corresponds to the FpML initialPayment element and the singlePayment element of the fee leg. For option products, which corresponds to the FpML premium element.">;
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

 class CreditDefaultPayout rosettaKey <"The credit default payout specification terms.">
 {
  id (0..1);
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 meta id path "trade.creditDefaultSwap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 meta id path "creditDefaultSwap"]
  generalTerms GeneralTerms (1..1) <"This element contains all the data that appears in the section entitled '1. General Terms' in the 2003 ISDA Credit Derivatives Confirmation, except for the effectiveDate, terminationDate and dateAdjustments elements, which have been positioned as part of the InterestRatePayout class.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value generalTerms path "trade.creditDefaultSwap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value generalTerms path "creditDefaultSwap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value generalTerms]
  protectionTerms ProtectionTerms (1..*) <"The credit protection terms.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value protectionTerms path "trade.creditDefaultSwap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value protectionTerms path "creditDefaultSwap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value protectionTerms]
  cashSettlementTerms CashSettlementTerms (0..*);
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value cashSettlementTerms path "trade.creditDefaultSwap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value cashSettlementTerms path "creditDefaultSwap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value cashSettlementTerms]
  physicalSettlementTerms PhysicalSettlementTerms (0..*);
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value physicalSettlementTerms path "trade.creditDefaultSwap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value physicalSettlementTerms path "creditDefaultSwap"]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value physicalSettlementTerms]
  transactedPrice TransactedPrice (0..1) <"The qualification of the price at which the contract has been transacted, in terms of market fixed rate, initial points, market price and/or quotation style. In FpML, those attributes are positioned as part of the fee leg.">;
 }

Inferring the product qualification from its economic terms
"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

The product qualification is inferred from the economic terms through a dedicated Rosetta syntax which navigates the CDM components.  This has been detailed as part of the above CDM Modelling Artefacts section.

The qualification of a **zero coupon fixed float inflation swap** provides a good example of the set of logic that can be used for such purpose, and which combines boolean and qualified expressions.

The CDM makes use of the ISDA taxonomy V2.0 leaf level to qualify the product.  That being said, the current CDM implementation only qualifies interest rate swaps, as the ISDA taxonomy V2.0 for credit default swap references the transaction type, which values are not publicly available and hence not positioned as a CDM enumeration.  This issue will be addressed as part of later versions of the model.

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

The product qualification is positioned as part of the ``ProductIdentification`` class, alongside the attributes currently used in FpML to specify the product: ``primaryAssetClass``, ``secondaryAssetClass``, ``productType`` and ``productId``.  This approach provides the ability to specify the credit derivatives products using this current approach until such time that an alternative approach to the transaction type is identified as a way to support a proper product qualification.

 .. code-block:: Java

 class ProductIdentification <"A class to combine the CDM product qualifier with other product qualifiers, such as the FpML ones. While the CDM product qualifier is derived by the CDM from the product payout features, the other product identification elements are assigned by some external sources and correspond to values specified by other data representation protocols.">
 {
  productQualifier productType (0..1) <"The CDM product qualifier, which corresponds to the outcome of the isProduct qualification logic. This value is derived by the CDM from the product payout features.">;
  primaryAssetClass AssetClassEnum (0..1) scheme <"A classification of the most important risk class of the trade. FpML defines a simple asset class categorisation using a coding scheme.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value primaryAssetClass path "trade.swap" meta assetClassScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value primaryAssetClass path "swap" meta assetClassScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value primaryAssetClass path "trade.creditDefaultSwap" meta assetClassScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value primaryAssetClass path "creditDefaultSwap" meta assetClassScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value primaryAssetClass path "trade.swaption" meta assetClassScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value primaryAssetClass path "swaption" meta assetClassScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value primaryAssetClass meta assetClassScheme]
  secondaryAssetClass AssetClassEnum (0..*) scheme <"A classification of additional risk classes of the trade, if any. FpML defines a simple asset class categorisation using a coding scheme.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value secondaryAssetClass path "trade.swap" meta assetClassScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value secondaryAssetClass path "swap" meta assetClassScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value secondaryAssetClass path "trade.creditDefaultSwap" meta assetClassScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value secondaryAssetClass path "creditDefaultSwap" meta assetClassScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value secondaryAssetClass path "trade.swaption" meta assetClassScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value secondaryAssetClass path "swaption" meta assetClassScheme]
  productType string (0..*) scheme <"A classification of the type of product. FpML defines a simple product categorisation using a coding scheme.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value productType path "trade.swap" meta productTypeScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value productType path "swap" meta productTypeScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value productType path "trade.creditDefaultSwap" meta productTypeScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value productType path "creditDefaultSwap" meta productTypeScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value productType path "trade.swaption" meta productTypeScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value productType path "swaption" meta productTypeScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value productType path "trade.swaption.swap" meta productTypeScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value productType path "swaption.swap" meta productTypeScheme]
   [synonym DTCC_11_0, DTCC_9_0 value ProductType]
  productId string (0..*) scheme <"A product reference identifier. The product Id is an identifier that describes the key economic characteristics of the trade type, with the exception of concepts such as size (notional, quantity, number of units) and price (fixed rate, strike, etc.) that are negotiated for each transaction. It can be used to hold identifiers such as the 'UPI' (universal product identifier) required by certain regulatory reporting rules. It can also be used to hold identifiers of benchmark products or product temnplates used by certain trading systems or facilities. FpML does not define the domain values associated with this element. Note that the domain values for this element are not strictly an enumerated list.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value productId path "trade.swap" meta productIdScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value productId path "swap" meta productIdScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value productId path "trade.creditDefaultSwap" meta productIdScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value productId path "creditDefaultSwap" meta productIdScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value productId path "trade.swaption" meta productIdScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value productId path "swaption" meta productIdScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value productId path "trade.swaption.swap" meta productIdScheme]
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, CME_ClearedConfirm_1_17 value productId path "swaption.swap" meta productIdScheme]
   [synonym DTCC_11_0, DTCC_9_0 value instrumentId path "payment.calculationDetails.calculationElements.underlyer.index" meta instrumentIdScheme]
 }

This CDM product qualification is stamped onto the generated CDM objects and their JSON serialized representation, as shown as part of the below JSON snippet which includes both product identification information associated with an originating FpML document and product qualification generated by the CDM:

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


Inferring the event qualification from its features
"""""""""""""""""""""""""""""""""""""""""""""""""""

The CDM lifecycle events are qualified as a function of the combination of their features and, when specified, the ``intent``. The associated syntax is specified as part of the above CDM Modelling Artefacts section.

The event qualification is positioned as an attribute of the ``Event`` class:

.. code-block:: Java

 class Event
 {
  id (0..1);
   [synonym Rosetta_Workbench meta id]
  messageInformation MessageInformation (0..1);
   [synonym DTCC_11_0, DTCC_9_0 value FpML path "Body.OTC_Matching.Trade"]
   [synonym DTCC_11_0, DTCC_9_0 value FpML path "Body.OTC_Matching.Payment"]
   [synonym DTCC_11_0, DTCC_9_0 value RouteInfo path "Header.OTC_RM.Delivery"]
   [synonym DTCC_11_0, DTCC_9_0 value Manifest path "Header.OTC_RM"]
   [synonym CME_ClearedConfirm_1_17 value header path "clearingConfirmed"]
  timestamp EventTimestamp (1..*) <"The set of timestamp(s) associated with the event as a collection of [dateTime, qualifier].">;
   [synonym Rosetta_Workbench value timestamp]
   [synonym DTCC_11_0, DTCC_9_0 value header path "Body.OTC_Matching.Trade.FpML"]
   [synonym DTCC_11_0, DTCC_9_0 value Route path "Header.OTC_RM.Delivery.RouteHist"]
   [synonym DTCC_11_0, DTCC_9_0 value header path "Body.OTC_Matching.Payment.FpML"]
   [synonym CME_ClearedConfirm_1_17 value timestamps path "clearingConfirmed.trade.tradeHeader"]
   [synonym CME_ClearedConfirm_1_17 value header path "clearingConfirmed"]
  eventIdentifier Identifier (1..*) <"The event identifier, which has 3 components: an actual identifier, an issuer and a version number. One and only one identifier can be associated with a lifecycle event.">;
   [synonym Rosetta_Workbench value eventIdentifier]
   [synonym DTCC_11_0, DTCC_9_0 value Submitter path "Header.OTC_RM.Manifest.TradeMsg"]
   [synonym DTCC_11_0, DTCC_9_0 value ContraTradeId path "Header.OTC_RM.Manifest.TradeMsg"]
   [synonym DTCC_11_0, DTCC_9_0 value YourTradeId path "Header.OTC_RM.Manifest.TradeMsg"]
   [synonym DTCC_11_0, DTCC_9_0 value tradeIdentifyingItems path "Header.OTC_RM.Manifest.TradeMsg"]
  eventQualifier eventType (0..1) <"The CDM event qualifier, which corresponds to the outcome of the isEvent qualification logic. This value is derived by the CDM from the event features.">;
  (...)
 }

Like the product qualifier, the event qualification is stamped onto the generated CDM objects and their JSON serialized representation, as illustrated by the below JSON lifecycle event snippet:

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

Derivative Products Underlyers
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

While the FpML specifies a number of underlyer product attributes as part of the contract representation, the CDM approach is to rather not to include any attribute that can be abstracted through reference data.  This is because specifying such information as part of the contract information leads to a risk or contradictory information, particularly for long-dated contracts.

As a result, the bond and convertible bond representation is limited to the product identifier.

Follow-up is in progress with the ISDA CDM Credit Workstream to confirm the approach with respect to the loan and mortgage-backed security underlyers.

.. code-block:: Java

 abstract class IdentifiedProduct <"An abstract class to specify a product which terms are abstracted through reference data.">
 {
  productIdentifier ProductIdentifier (1..1);
 }

 class Bond extends IdentifiedProduct <"A class to specify a bond as having a product identifier. As a difference with FpML, the CDM specifies the bond only with this product identifier attribute. The reason for this approach is to avoid the potential for conflicting information between the information associated with the contractual product and the reference information maintained by the relevant service provider.">
 {

 }

 class ConvertibleBond extends IdentifiedProduct <"A class to specify a convertible bond as having a product identifier. As a difference with FpML, the CDM specifies the convertible bond only with this product identifier attribute. The reason for this approach is to avoid the potential for conflicting information between the information associated with the contractual product and the reference information maintained by the relevant service provider.">
 {

 }

 class Loan extends IdentifiedProduct
  [synonym FpML_5_10 value Loan]
{
  borrower LegalEntity (0..*) <"Specifies the borrower. There can be more than one borrower. It is meant to be used in the event that there is no Bloomberg Id or the Secured List isn't applicable.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value borrower]
  lien string (0..1) scheme <"Specifies the seniority level of the lien.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value lien meta lienScheme]
  facilityType string (0..1) scheme <"The type of loan facility (letter of credit, revolving, ...).">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value facilityType meta facilityTypeScheme]
  creditAgreementDate date (0..1) <"The credit agreement date is the closing date (the date where the agreement has been signed) for the loans in the credit agreement. Funding of the facilities occurs on (or sometimes a little after) the Credit Agreement date. This underlyer attribute is used to help identify which of the company's outstanding loans are being referenced by knowing to which credit agreement it belongs. ISDA Standards Terms Supplement term: Date of Original Credit Agreement.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value creditAgreementDate]
  tranche string (0..1) scheme <"The loan tranche that is subject to the derivative transaction. It will typically be referenced as the Bloomberg tranche number. ISDA Standards Terms Supplement term: Bloomberg Tranche Number.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value tranche meta loanTrancheScheme]
 }

 class MortgageBackedSecurity extends ProductIdentifier
  [synonym FpML_5_10 value Mortgage]
 {
  pool AssetPool (0..1) <"The mortgage pool that is underneath the mortgage obligation.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value pool]
  sector MortgageSectorEnum (0..1) scheme <"The sector classification of the mortgage obligation.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value sector meta mortgageSectorScheme]
  tranche string (0..1) <"The mortgage obligation tranche that is subject to the derivative transaction.">;
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value tranche]
 }

Event Model
-----------

The CDM event model is based upon the same high-level principles as the product model:

* The events are specified by composition of **primitive events**, which make in turn use of a large set of FpML building blocks;
* The event qualification is inferred from those primitive events and, in some relevant cases, from an **intent** qualifier.

Baseline event modelling features
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The ``Event`` class carries the following set of information:

* **Messaging information**: ``messageId``, ``sentBy``, ``sentTo`` and ``copyTo``. This information is optional, as possibly not applicable in a context such as blockchain. It corresponds to some of the components of the FpML *MessageHeader.model*.
* **Timestamp information**: the CDM has adopted a generic approach to representing timestamp information as part of the release 1.1.70, consisting of a ``dateTime`` and a ``qualification`` attributes, with this latter being specified through a set of enumerated values.  The rationale for such approach is that the experience of mapping the CME clearing and the DTCC trade matching and cashflow confirmation transactions to the CDM did reveal a diverse set of timestamps.  The expected benefits of this generic approach are twofold: (i) this allows for flexibility in a context where it would challenging to mandate which points in the process should have associated timestamps, while gathering all of those in one place in the model provides the opportunity for evaluation and rationalisation down the road.  That being said, the CDM Group has expressed concerns with combining timestamps which are deemed 'technical' with 'business' ones.  A further evaluation of this modelling approach will be undertaken at a later point.

 .. code-block:: Java

  class EventTimestamp <"A class to represent the various set of timestamps that can be associated with lifecycle events, as a collection of [dateTime, qualifier]">
  {
   dateTime zonedDateTime (1..1) <"The CDM specifies that the zoned date time is to be expressed in accordance with ISO 8601, either as UTC as an offset to UTC.">;
    [synonym Rosetta_Workbench, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value creationTimestamp]
    [synonym Rosetta_Workbench, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value expiryTimestamp]
    [synonym DTCC_11_0, DTCC_9_0 value ReceiveTime]
    [synonym CME_ClearedConfirm_1_17 value submittedForClearing]
    [synonym CME_SubmissionIRS_1_0 value TxnTm path "TrdCaptRpt"]
    [synonym CME_SubmissionIRS_1_0 value Snt path "TrdCaptRpt.Hdr"]
   qualification EventTimeStampQualificationEnum (1..1);
    [synonym Rosetta_Workbench, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 set to EventTimeStampQualificationEnum.eventCreated when "creationTimestamp" exists]
    [synonym Rosetta_Workbench, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 set to EventTimeStampQualificationEnum.eventExpired when "expiryTimestamp" exists]
    [synonym CME_ClearedConfirm_1_17, Rosetta_Workbench set to EventTimeStampQualificationEnum.submittedForClearing when "submittedForClearing" exists]
    [synonym DTCC_11_0, DTCC_9_0 set to EventTimeStampQualificationEnum.eventSubmitted when "ReceiveTime" exists]
    [synonym CME_SubmissionIRS_1_0 set to EventTimeStampQualificationEnum.executed when path= "TrdCaptRpt.TxnTm"]
  }


Below is JSON snippet of an instance representation of such approach:

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


* **Event identification** information: the ``identifier``, alongside an optional ``version`` and ``issuer``. As a departure from FpML, which makes use of an event identifier construct (the *Correlation* which is distinct from the one associated with the trade (which itself comes in different variation: *PartyTradeIdentifier*, with the *TradeId* and the *VersionedTradeId* as sub-components of it), the CDM approach consists in using a common identifier component across products and events.

 .. code-block:: Java

  class Identifier
  {
   issuer string (0..1) reference scheme "issuerIdScheme" <"The reference to the party that assigns the trade identifier.">;
     [synonym Rosetta_Workbench value issuer]
   identifierValue IdentifierValue (1..1);
   version int (0..1);
     [synonym FpML value version]
     [synonym Rosetta_Workbench value version]
     [synonym FpML value version pathExpression "versionedTradeId"]
     [synonym Rosetta_Workbench value version pathExpression "versionedTradeId"]
  }

* **event qualifier**, which is derived from the event features.
* **Time dimension** information, through the ``eventDate`` and ``effectiveDate``, with this latter being optional as not applicable to certain events (e.g. observations).
* **Action qualification**, to specify whether the event is a new one, a correction or a cancellation of a prior one.
* **Intent qualification**, in the form of a set of enumerated values, such as ``allocation``, ``earlyTermination``, ``partialTermination``, etc.  This intent is used as part of the event qualification, in order to disambiguate events which features are shared across lifecycle events. As an example, a reduction in a trade quantity/notional could apply to a correction event or a partial termination (although the timing of such event could also be used to qualify the proper event).
* **Party information**, which is optional because not applicable to certain events (e.g. most of the observation events).
* **Lineage information**, in the form of a class that provides the ability to reference an unbounded set of contracts or events, as shown by the below code snippet:

 .. code-block:: Java

  class Lineage
  {
   contractReference Identifier (0..*) scheme "correlationIdScheme";
     [synonym Rosetta_Workbench value contractReference]
   eventReference Identifier (0..*);
     [synonym Rosetta_Workbench value eventReference]
  }

* **Primitive events**: the CDM composite approach uses the primitive events as its building blocks. Those primitive events are detailed in the next section of the documentation.
* **Function call**: an example of such a function call is the interpolation function that would be associated with a **derived observation** event that assembles two observed values (say, a 3 months and a 6 months rate observation) to provide a derived one (say, a 5 months observation). As part of the current CDM version this function call as been specified as a mere string element. It will be appropriately specified once such implementation is developed, some of which consisting in the machine readable implementation of the ISDA Definitions (see next Interest Calculation section).

* **EventEffect** corresponds to the set of operational and positional effects associated with a lifecycle event. This information is generated by the CDM and takes the form of a set of pointers to the relevant objects that are associated with a lifecycle event. The candidate objects are the classes that have an associated ``rosettaKey``. At present, those are the ``Contract``, ``Payment``, ``ListedProduct`` and ``ContractOrContractReference``. The ``rosettaKey`` is also associated with the ``Event`` so that a query of the ``EventEffect`` instantiated objects can provide a link to the respective event events. Events such as observations do not have an event effect, hence the optional cardinality.

 .. code-block:: Java

  class EventEffect <"The set of operational and positional effects associated with a lifecycle event.">
  {
   referenceContract Contract (0..*) rosettaKey <"A pointer to the contract to which the event effect(s) apply. This reference is optional to address the case where an event with candidate event effect(s) would only have associated referenceContract.">;
   referenceEvent Event (1..1) rosettaKey <"A pointer to the event to which the event effect(s) apply.">;
   product ContractOrListedProduct (0..*) rosettaKey <"A pointer to the products effect(s), an example of such being the outcome of an option physical exercise.">;
   payment Payment (0..*) rosettaKey <"A pointer to the payment effect(s).">;
   reset ResetPrimitive (0..*) rosettaKey <"A pointer to the reset effect(s).">;
  }

In the below JSON snippet of a swaption cash exercise, we can see that the eventEffect hash value points to the rosettaKeyentry that is associated with the payment.

 .. code-block:: Java

  "eventDate": "2018-04-10",
  "eventEffect": {
   "payment": [
     "5cafa672"
   ],
   "referenceContract": [
     "366e1ca6"
   ],
   "referenceEvent": "d70cf2e"
  (...)
  "payment": {
  "payerReceiver": {
    "payerPartyReference": "Party2",
    "receiverPartyReference": "Party1"
  },
  "paymentAmount": {
    "amount": 458600.53,
    "currency": "EUR"
  },
  "paymentDate": {
    "adjustedDate": "2019-04-17",
    "dateAdjustments": {
      "businessCenters": {
        "businessCenter": [
          "EUTA",
          "GBLO"
        ]
      },
      "businessDayConvention": "MODFOLLOWING"
    },
    "unadjustedDate": "2019-04-14"
  },
  "rosettaKey": "5cafa672"


Primitive events
^^^^^^^^^^^^^^^^

CDM primitive events are the building block components used to specify business events.

.. code-block:: Java

 class PrimitiveEvent <"The set of primitive events. The purpose of this class it to provide clarity with respect to the event qualification logic.">
 {
  newTrade NewTradePrimitive (0..*) <"The new trade primitive is unbounded to address the case of events such as portfolio compressions, which could result in multiple new trades.">;
    [synonym Rosetta_Workbench value newTrade]
  quantityChange QuantityChangePrimitive (0..*);
    [synonym Rosetta_Workbench value quantityChange]
  allocation AllocationPrimitive (0..*);
    [synonym Rosetta_Workbench value allocation]
  termsChange TermsChangePrimitive (0..1);
    [synonym Rosetta_Workbench value otherTermsChange]
  exercise ExercisePrimitive (0..1);
    [synonym Rosetta_Workbench value exercise]
  observation ObservationPrimitive (0..*);
    [synonym Rosetta_Workbench value observation]
  reset ResetPrimitive (0..*);
    [synonym Rosetta_Workbench value reset]
  payment Payment (0..*);
    [synonym Rosetta_Workbench value payment]
  }

Event qualification from primitive events and intent qualification
""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

Similar to the product modelling approach, the event qualification is inferred from the primitive events.

One distinction with the product approach is that the ``intent`` qualification is also deemed necessary to complement such primitive event information in certain cases. To this effect, the Rosetta event qualification syntax provides the ability to specify that the intent must have a specified value *when present*, as illustrated by the below snippet.

.. code-block:: Java

 isEvent Termination <"The qualification of a termination event from the fact that (i) the intent is Termination when specified, (ii) the only primitive is the quantityChange and there is only one such primitive involved, the (iii) the remaining quantity is null, and (iv) the contract state has the value 'terminated'.">
  Event -> intent when present = IntentEnum.Termination
  and Event -> primitive -> quantityChange single exists
  or quantityAfterQuantityChange = 0.0
  and ( Event -> primitive -> quantityChange -> after -> contract -> state = StateEnum.Terminated
  or Event -> primitive -> quantityChange -> after -> contractReference -> state = StateEnum.Terminated )


Interest Calculation
--------------------

The current CDM version implements the **Fixed Amount** and **Floating Amount** ISDA 2006 Definitions, alongside with some of the day count fractions.

Fixed Amount and Floating Amount Definitions
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The CDM syntax to express the Fixed Amount and Floating Amount is similar in structure: a calculation that reflects the terms of the ISDA 2006 Definitions, and associated arguments.

.. code-block:: Java

 calculation FixedAmount <"2006 ISDA Definition Article 5 Section 5.1. Calculation of a Fixed Amount: The Fixed Amount payable by a party on a Payment Date will be: (a) if an amount is specified for the Swap Transaction as the Fixed Amount payable by that party for that Payment Date or for the related Calculation Period, that amount; or (b) if an amount is not specified for the Swap Transaction as the Fixed Amount payable by that party for that Payment Date or for the related Calculation Period, an amount calculated on a formula basis for that Payment Date or for the related Calculation Period as follows: Fixed Amount = Calculation Amount × Fixed Rate × Day Count Fraction.">
 {
  fixedAmount number: calculationAmount * rate * dayCountFraction
  currencyAmount CurrencyEnum: currencyAmount
 }

 arguments FixedAmount <"The set of arguments to calculate the FixedAmount.">
 {
  calculationAmount: is InterestRatePayout -> quantity -> notionalSchedule -> notionalStepSchedule -> initialValue
  currencyAmount: is InterestRatePayout -> quantity -> notionalSchedule -> notionalStepSchedule -> currency
  rate: is InterestRatePayout -> interestRate -> fixedRate -> initialValue
  dayCountFraction: is InterestRatePayout -> dayCountFraction
 }

.. code-block:: Java

 calculation FloatingAmount <"2006 ISDA Definition Article 6 Section 6.1. Calculation of a Floating Amount: Subject to the provisions of Section 6.4 (Negative Interest Rates), the Floating Amount payable by a party on a Payment Date will be: (a) if Compounding is not specified for the Swap Transaction or that party, an amount calculated on a formula basis for that Payment Date or for the related Calculation Period as follows: Floating Amount = Calculation Amount × Floating Rate + Spread × Floating Rate Day Count Fraction (b) if “Compounding” is specified to be applicable to the Swap Transaction or that party and 'Flat Compounding' is not specified, an amount equal to the sum of the Compounding Period Amounts for each of the Compounding Periods in the related Calculation Period; or (c) if 'Flat Compounding' is specified to be applicable to the Swap Transaction or that party, an amount equal to the sum of the Basic Compounding Period Amounts for each of the Compounding Periods in the related Calculation Period plus the sum of the Additional Compounding Period Amounts for each such Compounding Period.">
 {
   floatingAmount number: calculationAmount * ( floatingRate + spread ) * dayCountFraction
   currencyAmount CurrencyEnum: currencyAmount
 }

 arguments FloatingAmount <"The set of arguments to calculate the FloatingAmount.">
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

 calculation DayCountFractionEnum._30E_360 <"2006 ISDA Definition Article 4 section 4.16(g): If '30E/360' or 'Eurobond Basis' is specified, the number of days in the Calculation Period or Compounding Period in respect of which payment is being made divided by 360, calculated on a formula basis as follows:[[360 x (Y2 - Y1)] + [30 x (M2 - M1)] + (D2 - D1)]/360">
 {
   number: (360 * (endYear - startYear) + 30 * (endMonth - startMonth) + (endDay - startDay)) / 360
 }

 arguments DayCountFractionEnum._30E_360 <"2006 ISDA Definition Article 4 section 4.16(g). 'Y1' is the year, expressed as a number, in which the first day of the Calculation Period or Compounding Period falls; 'Y2' is the year, expressed as a number, in which the day immediately following the last day included in the Calculation Period or Compounding Period falls; 'M1' is the calendar month, expressed as a number, in which the first day of the Calculation Period or Compounding Period falls; 'M2' is the calendar month, expressed as a number, in which the day immediately following the last day included in the Calculation Period or Compounding Period falls; 'D1' is the first calendar day, expressed as a number, of the Calculation Period or Compounding Period, unless such number would be 31, in which case D1 will be 30; and 'D2' is the calendar day, expressed as a number, immediately following the last day included in the Calculation Period or Compounding Period, unless such number would be 31, in which case D2 will be 30.">
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

 calculation DayCountFractionEnum.ACT_365_FIXED <"'2006 ISDA Definition Article 4 section 4.16(d): If'Actual/365 (Fixed)', 'Act/365 (Fixed)', 'A/365 (Fixed)' or 'A/365F' is specified, the actual number of days in the Calculation Period or Compounding Period in respect of which payment is being made divided by 365.">
 {
   number : daysInPeriod / 365
 }

 arguments DayCountFractionEnum.ACT_365_FIXED
 {
   daysInPeriod : is DaysInPeriod( InterestRatePayout -> calculationPeriodDates ) -> days
 }

Reference Data Model
--------------------

CDM only integrates the reference data components that are specifically needed to model the in-scope products, events and interest calculation components.

This translate into the representation of the **party**, with two alternate representations, modeled as attributes: the **legal entity** and the **natural person**.  Indeed, a number of product constructs, such as those applicable to credit default swaps, make use of a a legal entity representation.

It is expected that this CDM reference data representation will be further expanded once use cases for the model will be firmed out.

.. code-block:: Java

 class Party <"The party class.">
   [synonym FpML value Party]
 {
  id string (0..1) anchor;
    [synonym FpML value id]
  partyId string (1..1) scheme "partyIdScheme" <"The identifier associated with a party, e.g. the 20 digits LEI code.">;
    [synonym FpML value partyId]
    [synonym Rosetta_Workbench value partyId]
  legalEntity LegalEntity (0..1);
  naturalPerson NaturalPerson (0..1);
 }

 choice rule Party_choice <"A party is either a legal entity or a natural person.">
  for Party optional choice between
  legalEntity and naturalPerson

 class LegalEntity <"A class to represent the attributes that are specific to a legal entity.">
 {
  id string (0..1);
    [synonym FpML value id]
  entityId string (0..1) scheme "entityIdScheme" <"A legal entity identifier (e.g. RED entity code).">;
    [synonym FpML value entityId]
  name string (1..1) scheme "entityNameScheme" <"The legal entity name.">;
    [synonym FpML value partyName]
    [synonym FpML value entityName]
 }

 class NaturalPerson <"A class to represent the attributes that are specific to a natural person.">
 {
  honorific string (0..1) <"An honorific title, such as Mr., Ms., Dr. etc.">;
    [synonym FpML value honorific]
  firstName string (1..1) <"The natural person's first name. It is optional in FpML.">;
    [synonym FpML value firstName]
  middleName string (0..*);
    [synonym FpML value firstName]
  initial string (0..*);
    [synonym FpML value initial]
  surname string (1..1) <"The natural person's surname. It is optional in FpML.">;
    [synonym FpML value surname]
  suffix string (0..1) <"Name suffix, such as Jr., III, etc.">;
    [synonym FpML value suffix]
  dateOfBirth date (1..1) <"The natural person's date of birth.">;
    [synonym FpML value dateOfBirth]
 }
