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

The below **ISDA CDM Components Diagram** lays out the three set of CDM application components:

* **Rosetta** corresponds to the 'under the hood' components with respect to the CDM: the Rosetta grammar and the default code generators (currently Java), which together form the Rosetta DSL. To facilitate adoption and implementation of the CDM by the community, a dedicated `repository <https://github.com/REGnosys/rosetta-code-generators>`_ has been open-sourced, also under an Apache 2.0 license, for other industry participants to write code generators in any other languages.
* The **ISDA CDM Distribution** is made available to participants as part of the download available from the CDM Portal and is subject to the ISDA CDM licence.  The most crucial components of this ISDA CDM Distribution are the following:

  * The **Model Definition**, which corresponds to the actual Rosetta model, as expressed by the Rosetta syntax and which components are further detailed as part of the CDM Modelling Artefacts section of this documentation.
  * The **Model Code Projection**, currently available as Java and JSON.  As the Rosetta syntax represents not just data components but also logic, the JSON representation has a quite limited scope and usefulness, and might be deprecated at some future point.
  * While the two above components represent the essence of the model and are meant to be used as such by implementers, the **Default Apps** correspond to default implementations which can either be used as such, or be disabled or extended by those participants.  An example of such would be the rosettaKey implementation, which uses the default Java hash code function, but which might be deemed as inappropriate by some participants and hence be replaced by some alternative implementation.

* **CDM Applications** by service providers. It is expected that a rich eco-system of such licensed application components that are based upon the CDM will develop over time. REGnosys is the first to have taken the initiative to develop an offering of solutions, which purpose is to assist market participants in making use of the CDM. In particular, the CDM Portal provides a few UI components allowing participants to browse through the CDM. ISDA doesn't endorse any of those application components.

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

**Note**: For clarity purposes, the documentation snippets omit the synonyms and definitions that are associated with the classes and attributes, unless the purpose of the snippet it to highlight some of those features.

.. code-block:: Java

 abstract class IdentifiedProduct
 {
  productIdentifier ProductIdentifier (1..1);
 }

 class Loan extends IdentifiedProduct
 {
  borrower LegalEntity (0..*) ;
  lien string (0..1) scheme ;
  facilityType string (0..1) scheme ;
  creditAgreementDate date (0..1) ;
  tranche string (0..1) scheme ;
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

 class Party
 {
  id (0..1) ;
  partyId string (1..*) scheme ;
  naturalPerson NaturalPerson (0..*) ;
 }

 class ContractIdentifier extends Identifier
 {
  partyReference string (0..1) reference;
  accountReference string (0..1) reference;
 }

* The ``rosettaKey`` corresponds to a hash code generated by the CDM as part of the ``EventEffect`` features, which are further detailed below as part of the CDM Model section. In essence, the ``rosettaKey`` hash value associated with the relevant class (``Payment`` in the below snippet) is also associated with the corresponding attribute in the ``EventEffect`` class (in this case, the ``payment`` attribute).

.. code-block:: Java

 class EventEffect
 {
  effectedContract Contract (0..*) rosettaKey ;
  contract Contract (0..*) rosettaKey ;
  productIdentifier ProductIdentifier (0..*) rosettaKey ;
  transfer Transfer (0..*) rosettaKey ;
 }

 class Transfer rosettaKey
 {
  identifier string (0..1) scheme ;
  settlementType TransferSettlementEnum (0..1) ;
  settlementDate AdjustableOrAdjustedOrRelativeDate (1..1) ;
  cashTransfer CashTransferComponent (0..*) ;
  securityTransfer SecurityTransferComponent (0..*) ;
  commodityTransfer CommodityTransferComponent (0..*) ;
  status TransferStatusEnum (0..1) ;
  settlementReference string (0..1) ;
 }

* The ``rosettaKeyValue`` is a variation of the ``RosettaKey``, which associated hash function doesn't include any of those qualifiers that are associated with the attributes. The reasoning is that some of those qualifiers are automatically generated by algorithm (typically, the anchors and references associated with XML documents) and would then result in differences between two instance documents, even if those documents would have the same actual values. The ``RosettaKeyValue`` is meant to be used for supporting the reconciliation of economic terms, and is hence associated with the ``EconomicTerms`` class.

.. code-block:: Java

 class EconomicTerms rosettaKeyValue
 {
  payout Payout (1..1) ;
  earlyTerminationProvision EarlyTerminationProvision (0..1) ;
  cancelableProvision CancelableProvision (0..1) ;
  extendibleProvision ExtendibleProvision (0..1) ;
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
  ModifiedPostponement <"As defined in section 6.7 paragraph (c) sub-paragraph (iii) of the ISDA 2002 Equity Derivative definitions.">
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value "ModifiedPostponement"],
  Omission	<"As defined in section 6.7 paragraph (c) sub-paragraph (i) of the ISDA 2002 Equity Derivative definitions.">
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value "Omission"],
  Postponement	<"As defined in section 6.7 paragraph (c) sub-paragraph (ii) of the ISDA 2002 Equity Derivative definitions.">
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

In order to handle the integration of FpML scheme values such as the *dayCountFractionScheme* which has values such as ``ACT/365.FIXED`` or ``30/360``, the Rosetta syntax provides the ability to associate a **displayName synonym**. Those values with special characters have those special characters replaced with ``_`` and have an associated ``displayName`` entry which corresponds to the actual value. Examples of such are ``ACT_365_FIXED`` and ``_30_360``, with the associated display names of ``ACT/365.FIXED`` and ``30/360``, respectively.

.. code-block:: Java

 enum DayCountFractionEnum
 {
  ACT_360 displayName "ACT/360"
  ACT_365L displayName "ACT/365L"
  ACT_365_FIXED displayName "ACT/365.FIXED"
  ACT_ACT_AFB displayName "ACT/ACT.AFB"
  ACT_ACT_ICMA displayName "ACT/ACT.ICMA"
  ACT_ACT_ISDA displayName "ACT/ACT.ISDA"
  ACT_ACT_ISMA displayName "ACT/ACT.ISMA"
  BUS_252 displayName "BUS/252"
  _1_1 displayName "1/1"
  _30E_360 displayName "30E/360"
  _30E_360_ISDA displayName "30E/360.ISDA"
  _30_360 displayName "30/360"
 }

The **synonym syntax** associated with enumeration values differs in two respects from the synonyms associated with other CDM artefacts:

* The synonym value is of type ``string``, for the above reason related to the need to facilitate integration with executable code.  (The alternative approach consisting in specifying the value as a compatible identifier alongside with a display name has been disregarded because it has been deemed not appropriate to create a 'code-friendly' value for the respective synonyms.  A ``string`` type removes such need.)
* The ability to associate a definition to a synonym value has been enabled, the objective being to effectively support the FIX use cases where the synonym value is a letter or numerical code, which is then positioned as the prefix of the associated definition. The below entries to the ``InformationProviderEnum`` illustrates this approach:

.. code-block:: Java

 enum InformationProviderEnum <"The enumerated values to specify the list of information providers.">
  [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value informationProviderScheme_2_1]
 {
  (...)
  Bloomberg <"Bloomberg LP.">
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value "Bloomberg"]
   [synonym FIX_5_0_SP2 value "0" definition "0 = Bloomberg"],
  (...)
  Other
   [synonym FIX_5_0_SP2 value "99" definition "99 = Other"],
  (...)
  Telerate <"Telerate, Inc.">
   [synonym FpML_5_10, CME_SubmissionIRS_1_0, DTCC_11_0, DTCC_9_0, CME_ClearedConfirm_1_17 value "Telerate"]
   [synonym FIX_5_0_SP2 value "2" definition "2 = Telerate"]
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

 class Identifier
 {
  id (0..1) ;
  issuer string (1..1) scheme, reference ;
  assignedIdentifier AssignedIdentifier (1..*) ;
 }

 class ContractIdentifier extends Identifier
 {
  partyReference string (0..1) reference ;
  accountReference string (0..1) reference ;
 }

 choice rule ContractIdentifier_choice
  for ContractIdentifier required choice between
  issuer and partyReference

The choice constraint can either be **required** (implying that exactly one of the attributes needs to be present) or **optional** (implying that at most one of the attributes needs to be present).

While most of the choice rules have two attributes, there is no limit to the number of attributes associated with it… within the limit of the number of attributes associated with the class at stake. ``OptionCashSettlement_choice`` is a good illustration of this.

.. code-block:: Java

 class OptionCashSettlement
 {
  id (0..1);
	cashSettlementValuationTime BusinessCenterTime (0..1) ;
	cashSettlementValuationDate RelativeDateOffset (0..1) ;
	cashSettlementPaymentDate CashSettlementPaymentDate (0..1) ;
	cashPriceMethod CashPriceMethod (0..1) ;
	cashPriceAlternateMethod CashPriceMethod (0..1) ;
	parYieldCurveAdjustedMethod YieldCurveMethod (0..1) ;
	zeroCouponYieldAdjustedMethod YieldCurveMethod (0..1) ;
	parYieldCurveUnadjustedMethod YieldCurveMethod (0..1) ;
	crossCurrencyMethod CrossCurrencyMethod (0..1) ;
	collateralizedCashPriceMethod YieldCurveMethod (0..1) ;
 }

 choice rule OptionCashSettlement_choice
  for OptionCashSettlement optional choice between
  cashPriceMethod and cashPriceAlternateMethod and parYieldCurveAdjustedMethod and zeroCouponYieldAdjustedMethod
  and parYieldCurveUnadjustedMethod and crossCurrencyMethod and collateralizedCashPriceMethod

Members of a choice rule need to have their lower cardinality set to 0, something which is enforced by a validation rule.

One of syntax as a complement to the choice rule
""""""""""""""""""""""""""""""""""""""""""""""""

In the case where all the attributes of a given class are subject to a choice logic, Rosetta provides the ability to qualify the class information with the ``one of`` qualifier.  This feature is illustrated by the ``BondOptionStrike`` class.

.. code-block:: Java

 class BondOptionStrike one of
 {
  referenceSwapCurve ReferenceSwapCurve (0..1) ;
  price OptionStrike (0..1);
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

 isEvent Novation
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

 class Payout
 {
  interestRatePayout InterestRatePayout (0..*) ;
  creditDefaultPayout CreditDefaultPayout (0..1) ;
  cashflow Cashflow (0..*) ;
  optionPayout OptionPayout (0..*);
 }

* A **tag** or a **componentID** can be associated to a synonym value. In both cases, the purpose is to properly reflect the FIX standard, which makes use of those two artefacts. There are only two examples of such at present in the model, as a result of the scope focus on post-execution use cases and, hence, the limited reference to the FIX standard.

.. code-block:: Java

 class Strike
 {
  id (0..1);
  strikeRate number (1..1) ;
  buyer PayerReceiverEnum (0..1) ;
  seller PayerReceiverEnum (0..1) ;
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

 class Frequency
 {
  id (0..1) ;
  periodMultiplier int (1..1) ;
  period PeriodExtendedEnum (1..1) ;
 }

 class CalculationPeriodFrequency extends Frequency
 {
  rollConvention RollConventionEnum (1..1) ;
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

  data rule CalculationPeriodDates_firstCompoundingPeriodEndDate
   when InterestRatePayout -> compoundingMethod is absent
    or InterestRatePayout -> compoundingMethod = CompoundingMethodEnum.None
   then InterestRatePayout -> calculationPeriodDates -> firstCompoundingPeriodEndDate is absent

* ``CalculationPeriod_calculationPeriodNumberOfDays`` involves an operator.

 .. code-block:: Java

  data rule CalculationPeriod_calculationPeriodNumberOfDays
   when PaymentCalculationPeriod -> calculationPeriod -> calculationPeriodNumberOfDays exists
   then PaymentCalculationPeriod -> calculationPeriod -> calculationPeriodNumberOfDays >= 0

* ``Obligations_physicalSettlementMatrix`` makes use of parentheses for the purpose of supporting nested assertions.

.. code-block:: Java

 data rule Obligations_physicalSettlementMatrix
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

 isEvent Increase
  Event -> intent when present = IntentEnum.Increase
  and ( Event -> primitive -> quantityChange only exists
   or ( Event -> primitive -> quantityChange and Event -> primitive -> transfer -> cashTransfer ) exists )
  and quantityBeforeQuantityChange < quantityAfterQuantityChange
  and changedQuantity > 0.0
  and Event -> primitive -> quantityChange -> after -> contract -> closedState is absent

  alias quantityBeforeQuantityChange
   Event -> primitive -> quantityChange -> before -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> quantity -> quantity -> amount
   and Event -> primitive -> quantityChange -> before -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> quantity -> notionalAmount -> amount
   and Event -> primitive -> quantityChange -> before -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> quantity -> notionalSchedule -> notionalStepSchedule -> initialValue
   and Event -> primitive -> quantityChange -> before -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> quantity -> notionalSchedule -> notionalStepSchedule -> step -> stepValue
   and Event -> primitive -> quantityChange -> before -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> quantity -> notionalSchedule -> notionalStepParameters -> notionalStepAmount
   and Event -> primitive -> quantityChange -> before -> contract -> contractualProduct -> economicTerms -> payout -> interestRatePayout -> quantity -> fxLinkedNotional -> initialValue
   and Event -> primitive -> quantityChange -> before -> contract -> contractualProduct -> economicTerms -> payout -> creditDefaultPayout -> protectionTerms -> notionalAmount -> amount
   and Event -> primitive -> quantityChange -> before -> contract -> contractualProduct -> economicTerms -> payout -> optionPayout -> quantity -> notionalAmount -> amount

  alias quantityAfterQuantityChange
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

To this effect, the Rosetta grammar has been extended as a way to express a syntax that can support such expressions.

Syntax
^^^^^^

The calculation syntax has three components: the **calculation** itself, the **argument** used as an input to that calculation and (possibly) associated **function**.

The application of this syntax to the ``ACT/365.FIXED`` ISDA day count fraction definition provides a good illustration of that syntax:

.. code-block:: Java

 calculation DayCountFractionEnum._30E_360
 {
   number: (360 * (endYear - startYear) + 30 * (endMonth - startMonth) + (endDay - startDay)) / 360
 }

.. code-block:: Java

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

 function ResolveRateIndex( index FloatingRateIndexEnum )
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

Contractual products are bilateral contracts between two parties, its terms are specified at trade inception and apply throughout the life of the contract.  Contractual products are fungible only under specific terms (e.g. existence of a close-out netting agreement between the parties).

The CDM ``Contract`` class incorporates all the elements that are part of the FpML *Trade* confirmation view, with the exception of the following elements: *tradeSummary*, *originatingPackage*, *allocations* and *approvals*.

.. code-block:: Java

 class Contract rosettaKey
 {
  id (0..1);
  contractIdentifier Identifier (1..*) ;
  tradeDate TradeDate (1..1) ;
  clearedDate date (0..1) ;
  contractualProduct ContractualProduct (1..1) ;
  collateral Collateral (0..1) ;
  documentation Documentation (0..1) ;
  governingLaw GoverningLawEnum (0..1) scheme ;
  party Party (0..*) ;
  account Account (0..*) ;
  partyRole PartyRole (0..*) ;
  calculationAgent CalculationAgent (0..1) ;
  partyContractInformation PartyContractInformation (0..*) ;
  closedState ClosedState (0..1) ;
 }

The scope of the contract is limited to the post-execution lifecycle, as it involves legal entities and has a set of attributes which are only qualified at the execution and post-execution stage: trade date, calculation agent, documentation, governing law, etc.

The economic terms of the contract are positioned as part of the ``contractualProduct`` attribute, alongside the product identification and product taxonomy information.

.. code-block:: Java

 class ContractualProduct
 {
  productIdentification ProductIdentification (0..1) ;
  productTaxonomy ProductTaxonomy (1..*) ;
  economicTerms EconomicTerms (1..1) ;
 }

In this respect, the CDM ``contract`` corresponds to the confirmation view of the FpML *trade*, while the ``contractualProduct`` corresponds to the pre-trade view of the FpML *trade*.  (The FpML *trade* term has not been used as part of the CDM because deemed ambiguous in this respect.  Its use as part of the standard is largely due to an exclusive focus on post-execution activity in the initial stages of its development. Later adjustments in this respect would have been made difficult as a result of backward compatibility considerations.)


The economic terms
""""""""""""""""""

The CDM ``EconomicTerms`` class ands the underlying ``Payout`` class represent a significant departure from the FpML standard. While FpML qualifies the product through the *product* substitution group, CDM specifies the various set of possible economic terms as part of those afore mentioned ``economicTerms`` and ``payout`` classes.  A contractual product will then consist in an assembling of such economic terms, from which the product qualification will be syntactically inferred.

.. code-block:: Java

 class EconomicTerms rosettaKeyValue
 {
  payout Payout (1..1) ;
  earlyTerminationProvision EarlyTerminationProvision (0..1) ;
  cancelableProvision CancelableProvision (0..1) ;
  extendibleProvision ExtendibleProvision (0..1) ;
 }

The ``Payout`` class provides some provide some appropriate insight into the respective product representation between FpML and the CDM, through the relevant synonym sources and associated path expressions.  As an example, one can see that the FpML *feeLeg* is represented through the CDM ``interestRatePayout``, while the FpML *singlePayment* and *initialPayment* are both represented through the CDM ``cashflow``.

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

  class ProductIdentification
  {
   productQualifier productType (0..1) ;
   primaryAssetClass AssetClassEnum (0..1) scheme ;
   secondaryAssetClass AssetClassEnum (0..*) scheme ;
   productType string (0..*) scheme ;
   productId string (0..*) scheme ;
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
  id (0..1) ;
  messageInformation MessageInformation (0..1) ;
  timestamp EventTimestamp (1..*) ;
  eventIdentifier Identifier (1..*) ;
  eventQualifier eventType (0..1) ;
  eventDate date (1..1);
  effectiveDate date (0..1);
  action ActionEnum (1..1) ;
  intent IntentEnum (0..1);
  party Party (0..*) ;
  account Account (0..*) ;
  lineage Lineage (0..1) ;
  primitive PrimitiveEvent (1..1) ;
  functionCall string (0..1) ;
  eventEffect EventEffect (0..1) ;
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

While the FpML specifies a number of underlier product attributes as part of the contract representation, the CDM approach is to rather not to include any attribute that can be abstracted through reference data.  This is because specifying such information as part of the contract information leads to a risk or contradictory information, particularly for long-dated contracts.

As a result, the bond and convertible bond representation is limited to the product identifier.

Follow-up is in progress with the ISDA CDM Credit Workstream to confirm the approach with respect to the loan and mortgage-backed security underliers.

.. code-block:: Java

 abstract class IdentifiedProduct
 {
  productIdentifier ProductIdentifier (1..1) ;
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
  pool AssetPool (0..1) ;
  sector MortgageSectorEnum (0..1) scheme ;
  tranche string (0..1) ;
 }

Event Model
-----------

The CDM event model is based upon the same high-level principles as the product model:

* The events are specified by composition of **primitive events**, which make in turn use of a large set of FpML building blocks;
* The event qualification is inferred from those primitive events and, in some relevant cases, from an **intent** qualifier.

Baseline event modelling features
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. code-block:: Java

 class Event
 {
  id (0..1) ;
  messageInformation MessageInformation (0..1) ;
  timestamp EventTimestamp (1..*) ;
  eventIdentifier Identifier (1..*) ;
  eventQualifier eventType (0..1) ;
  eventDate date (1..1);
  effectiveDate date (0..1) ;
  action ActionEnum (1..1) ;
  intent IntentEnum (0..1);
  party Party (0..*) ;
  account Account (0..*) ;
  lineage Lineage (0..1) ;
  primitive PrimitiveEvent (1..1) ;
  functionCall string (0..1) ;
  eventEffect EventEffect (0..1) ;
 }

The ``Event`` class carries the following set of information:

* **Messaging information**: ``messageId``, ``sentBy``, ``sentTo`` and ``copyTo``. This information is optional, as possibly not applicable in a context such as blockchain. It corresponds to some of the components of the FpML *MessageHeader.model*.
* **Timestamp information**: the CDM has adopted a generic approach to representing timestamp information as part of the release 1.1.70, consisting of a ``dateTime`` and a ``qualification`` attributes, with this latter being specified through a set of enumerated values.  The rationale for such approach is that the experience of mapping the CME clearing and the DTCC trade matching and cashflow confirmation transactions to the CDM did reveal a diverse set of timestamps.  The expected benefits of this generic approach are twofold: (i) this allows for flexibility in a context where it would challenging to mandate which points in the process should have associated timestamps, while gathering all of those in one place in the model provides the opportunity for evaluation and rationalisation down the road.  That being said, the CDM Group has expressed concerns with combining timestamps which are deemed 'technical' with 'business' ones.  A further evaluation of this modelling approach will be undertaken at a later point.

 .. code-block:: Java

  class EventTimestamp
  {
   dateTime zonedDateTime (1..1) ;
   qualification EventTimeStampQualificationEnum (1..1);
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
   id (0..1) ;
   issuer string (1..1) scheme, reference ;
   assignedIdentifier AssignedIdentifier (1..*) ;
  }

* **event qualifier**, which is derived from the event features.
* **Time dimension** information, through the ``eventDate`` and ``effectiveDate``, with this latter being optional as not applicable to certain events (e.g. observations).
* **Action qualification**, to specify whether the event is a new one, a correction or a cancellation of a prior one.
* **Intent qualification**, in the form of a set of enumerated values, such as ``allocation``, ``earlyTermination``, ``partialTermination``, etc.  This intent is used as part of the event qualification, in order to disambiguate events which features are shared across lifecycle events. As an example, a reduction in a trade quantity/notional could apply to a correction event or a partial termination (although the timing of such event could also be used to qualify the proper event).
* **Party information**, which is optional because not applicable to certain events (e.g. most of the observation events).
* **Lineage information**, in the form of a class that provides the ability to reference an unbounded set of contracts, events and/or payout components, as shown by the below code snippet:

 .. code-block:: Java

  class Lineage
  {
   contractReference Contract (0..*) rosettaKey reference ;
   eventReference Event (0..*) rosettaKey reference ;
   transferReference Transfer (0..*) rosettaKey reference ;
   creditDefaultPayoutReference CreditDefaultPayout (0..*) rosettaKey reference ;
   interestRatePayoutReference InterestRatePayout (0..*) rosettaKey reference ;
   optionPayoutReference OptionPayout (0..*) rosettaKey reference ;
  }

* **Primitive events**: the CDM composite approach uses the primitive events as its building blocks. Those primitive events are detailed in the next section of the documentation.
* **Function call**: an example of such a function call is the interpolation function that would be associated with a **derived observation** event that assembles two observed values (say, a 3 months and a 6 months rate observation) to provide a derived one (say, a 5 months observation). As part of the current CDM version this function call as been specified as a mere string element. It will be appropriately specified once such implementation is developed, some of which consisting in the machine readable implementation of the ISDA Definitions (see next Interest Calculation section).
* **EventEffect** corresponds to the set of operational and positional effects associated with a lifecycle event. This information is generated by the CDM and takes the form of a set of pointers to the relevant objects that are associated with a lifecycle event. The candidate objects are the classes that have an associated ``rosettaKey``. At present, those are the ``Contract``, ``ProductIdentifier`` and ``Transfer``. The ``rosettaKey`` is also associated with the ``Event`` so that a query of the ``EventEffect`` instantiated objects can provide a link to the respective event events. Events such as observations do not have an event effect, hence the optional cardinality.

 .. code-block:: Java

  class EventEffect
  {
   effectedContract Contract (0..*) rosettaKey ;
   contract Contract (0..*) rosettaKey ;
   productIdentifier ProductIdentifier (0..*) rosettaKey ;
   transfer Transfer (0..*) rosettaKey ;
  }

In the below JSON snippet of a swaption cash exercise, we can see that the ``eventEffect`` hash value points to the ``rosettaKey`` entry that is associated with the cash transfer.

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

Event qualification from primitive events and intent qualification
""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

Similar to the product modelling approach, the event qualification is inferred from the primitive events.

One distinction with the product approach is that the ``intent`` qualification is also deemed necessary to complement such primitive event information in certain cases. To this effect, the Rosetta event qualification syntax provides the ability to specify that the intent must have a specified value *when present*, as illustrated by the below snippet.

.. code-block:: Java

 isEvent Termination
  Event -> intent when present = IntentEnum.Termination
  and Event -> primitive -> quantityChange single exists
  and quantityAfterQuantityChange = 0.0
  and Event -> primitive -> quantityChange -> after -> contract -> closedState -> state = ClosedStateEnum.Terminated
  and Event -> primitive -> quantityChange -> after -> clearingStatus is absent


Legal Agreements
----------------

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
*  Focus on providing whenever possible a normalized data representation which can be digitally usable as such once projected through a machine executable language.  Practically speaking, that means restricting the use of elections expressed in a ``string`` format whenever possible.  To this effect, the CDM leverages the ISDA Create data representation, while also extends it in some cases by leveraging some of the work developed in 2013-14 as part of the FpML work to provide a digital representation of the Standard CSA.  Notable examples of such approach are:

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
	legalAgreement LegalAgreement (0..*) reference ;
	documentationIdentification DocumentationIdentification (0..1) ;
 }

This further snippet presents the ``LegalAgreement`` class and its associated ``key`` qualifier.

.. code-block:: Java

 class LegalAgreement extends LegalAgreementBase key one of
 {
 	csdInitialMargin2016EnglishLaw CsdInitialMargin2016EnglishLaw (0..1) ;
 	csaInitialMargin2016JapaneseLaw CsaInitialMargin2016JapaneseLaw (0..1) ;
 	csaInitialMargin2016NewYorkLaw CsaInitialMargin2016NewYorkLaw (0..1) ;
 	csaVariationMargin2016NewYorkLaw CsaVariationMargin2016NewYorkLaw (0..1) ;
 }


Interest Calculation
--------------------

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
