˜.. figure:: rosetta-overview.png
**Rosetta Overview**

The ISDA CDM and the Rosetta Workbench
======================================

The ISDA Common Domain Model (CDM) is supported by a digital repository called Rosetta whose purpose is to consolidate market standards and practices into a cohesive model, from which executable code is automatically generated.

The key idea behind Rosetta is that financial markets presently have two unappealing characteristics as it relates to their supporting electronic data representation:

*  **Variety of data representations**. The plurality of data standards (the main ones being FIX, FpML, ISO 20022 and EFET) is compounded by the many variations in the implementation of those, to which we need to add a wide range of proprietary data representations.
*  **Limited availability of native digital tools** that would allow those data representations to be directly translated into executable code. Even the protocols that have a native digital representation (e.g. FpML and FIXML, which are available in the form of XML schemas) have associated specifications artefacts which require further manual specification and/or coding in order to result in a complete executable solution. In FpML, this is the case of the associated validation rules. In FIX, an example of such are the Recommended Practices/Guidelines, which are only available in the form of PDF documents.

Rosetta is looking to address those shortcomings by **consolidating various data and workflow representations into a cohesive model** (hence the naming reference to the Rosetta Stone), which can be **automatically translated into executable code**.

.. figure:: rosetta-components.png

As illustrated by the above picture, Rosetta has two main components as it relates to its usage as part of the ISDA CDM:
* The **Model Repository** has two components:
    1- The legible model, that expresses the data and associated logic using the Rosetta syntax.
    2- The projection of this model into a variery of executable code representations (presently, JSON and Java).
* The **Rosetta Workbench** corresponds to the toolset that supports the CDM, through 4 sets of functions:
    1- The grammar, which is specified as part of a Domain Specific Language (DLS) component which has been developed using open source software. While the CDM syntax is based upon this grammar, access to this grammar is needed for using the CDM.
    2- The navigation tools, which expose through a web portal a textual and a graphical interface into the CDM.
    3- The ingestion service, which transforms events and trades expressed using alternative data representations into JSON documents that conform to the CDM. At present, the ingested trades confirm to the version 5.10 of the FpML standard, and the events are originated using a custom data representation.
    4- The code generators, which are used to produce the executable code projections that are part of the model repository. Those code generators are developed using the same open source software component as the grammar, and access to those is not required for CDM usage purposes.

Those worbench components are presented in the below picture, the further one being the milestoned CDM 1.0 version of the model. Once further version of the CDM will be release, this icon will be repositioned as a generic access to those respective versions.

.. figure:: rosetta-home.png



CDM Modelling Artefacts
=======================

The CDM combines **five modelling artefacts**, which are consistently expressed through the Rosetta syntax:

* Data representation
* Mapping
* Data validation
* Model qualification
* Calculation

The below sections of this documentation detail the purpose and features of each of those CDM artefacts, and highlight the relationships that exists among those.


CDM Data Representation
-----------------------

Rosetta makes use of **four data representation components** to represent the CDM:

* Classes
* Attributes
* Enumerations
* Enumerations values

Rosetta also provides the ability to express some of those data artefacts through **aliases**, as a way to enhance model legibility.

Classes
~~~~~~~

Purpose
^^^^^^^

CDM classes are objects that contain the granular data representation elements, in the form of attributes.

Syntax
^^^^^^

The class is delineated by brackets ``{`` ``}``.

The CDM supports the concept of **abstract classes**, which cannot be instantiated as part of the generated executable code and are meant to be extended by other classes.  An example of such is the ``EventBase`` class, which contains the attributes that are applicable to all events.

  .. code-block:: Java

  abstract class EventBase stereotype preExecution, execution, postExecution <"The event base abstract class.">
  {
  	messageInformation MessageInformation (0..1);
  	timeStamp EventTimeStamp (1..1);
  		[synonym Rosetta_Workbench value timeStamp]
  	eventIdentifier Identifier (1..1);
  		[synonym Rosetta_Workbench value eventIdentifier]
  	eventDate date (1..1);
  		[synonym Rosetta_Workbench value eventDate]
  	effectiveDate date (0..1);
  		[synonym Rosetta_Workbench value effectiveDate]
  	action ActionEnum (1..1) <"Specifies whether the event is a new, a correction or a cancellation.">;
  		[synonym FpML value isCorrection
  			set action to ActionEnum.new when False,
  			set action to ActionEnum.correct when True]
  }


**Stereotype values**, such as ``postExecution`` in the above example, are specified for the purpose of supporting analytical queries and navigation tools at some further point down the road.

The Rosetta convention is that class names start with a capital letter. Class names need to be unique across the model, including with respect to rule names. Both those are controlled by the Rosetta Workbench grammar.

Attributes
~~~~~~~~~~

Purpose
^^^^^^^

Attributes specify the granular model elements in terms of type of value (e.g. ``integer``, ``string``, enumerated value), cardinality and through an associated definition.

Syntax
^^^^^^

A Rosetta attribute can be specified either as a basic type, a class or an enumeration.

The set of **basic types** available in Rosetta are:

Text - ``string``

Number - ``int`` - ``number``

Logic - ``boolean``

Date and Time - ``date`` - ``dateTime`` - ``time``

The CDM provides the ability to associate either a ``reference``, an identifier (expressed as ``anchor``) and/or a ``scheme`` qualifier to the attribute. The purpose here is to provide the ability to properly map source XML documents, such as FpML ones, which make use of such cross-referencing modelling representation. The implementation works as follows:

* In the case where a source element is specified by reference to another element, Rosetta will replicate this reference:

  .. code-block:: Java

  class DateRelativeToPaymentDates stereotype contractualProduct <"A class to provide the ability to point to multiple payment nodes in the document through the unbounded paymentDatesReference.">
  	[synonym FpML value DateRelativeToPaymentDates]
  {
  	paymentDatesReference string (1..*) reference <"A set of href pointers to payment dates defined somewhere else in the document.">;
  		[synonym FpML value paymentDatesReference]
  }


* In the case where a source element makes reference to a scheme and if the values for that scheme are specified, that scheme is positioned as an enumeration.  An example of such is the FpML *creditSupportAgreementTypeScheme* which is represented in Rosetta via the ``CreditSupportAgreementTypeEnum``. While the scheme value is represented as part of the enumeration, the CDM attribute also carries the scheme associated with the original document, such as not to discard any origination information.

  .. code-block:: Java

  class CreditSupportAgreement stereotype contractualProduct <"The agreement executed between the parties and intended to govern collateral arrangement for all OTC derivatives transactions between those parties.">
  	[synonym FpML value CreditSupportAgreement]
  {
  	type CreditSupportAgreementTypeEnum (1..1) scheme "creditSupportAgreementTypeScheme" <"The type of ISDA Credit Support Agreement.">;
  		[synonym FpML value type]
  	date date (1..1) <"The date of the agreement executed between the parties and intended to govern collateral arrangements for all OTC derivatives transactions between those parties.">;
  		[synonym FpML value date]
  	identifierValue string (0..1) <"An identifier used to uniquely identify the CSA. FpML specifies the type as creditSupportAgreementIdScheme, but without proposing any value.  As far as e understand, no scheme has yet been developed at this point.">;
  		[synonym FpML value identifier]
  }


* In the case where a source element makes reference to a scheme while the values for that scheme are not specified, the corresponding attribute is set as a ``string``, with an associated scheme reference.  An example of such is the FpML *linkIdScheme*.

  .. code-block:: Java

    class LinkId <"The class to represent link identifiers.">
    	[synonym FpML value LinkId]
    {
    	id string (0..1);
    		[synonym FpML value id]
    	linkId string (1..1) scheme "linkIdScheme";
    		[synonym FpML value linkId]
    }

CDM syntax convention is for attribute names to be expressed in lower case, and a warning will be generated by the grammar if this is not the case. Attribute names need to be unique within the context of a class (and within the context of the base class, if a class extends another class), but can be duplicated across classes. The semi-column ``;`` acts as the terminal character for the attribute specification, with associated synonyms being positioned underneath that specification line.

Enumerations
~~~~~~~~~~~~

Purpose
^^^^^^^

Enumerations are the mechanism through which controlled values are specified at the attribute level. They are the container for the corresponding set of enumeration values.

With respect to the FpML standard, the schemes which values are specified as part of the standard are represented through enumerations in the CDM. As mentioned in the preceding section, FpML schemes with no defined values are represented in the CDM as a type ``string`` alongside that an associated scheme qualification.

Syntax
^^^^^^

Enumerations are very simple modelling container artefacts. They can have associated synonyms and regulatory references.

Similar to the class, the enumeration is delineated by brackets ``{`` ``}``.

 .. code-block:: Java

 enum CouponTypeEnum <"The enumerated values to specify if the bond has a variable coupon, step-up/down coupon or a zero-coupon.">
  [synonym FpML value couponTypeScheme]
  {
  Fixed <"Bond has fixed rate coupon.">
    [synonym FpML value "Fixed"],
  Float <"Bond has floating rate coupon.">
    [synonym FpML value "Float"],
  Structured <"Bond has structured coupon.">
    [synonym FpML value "Struct"]
  }


Enumeration Values
~~~~~~~~~~~~~~~~~~

Purpose
^^^^^^^

As indicated in the above section, enumeration values are the set of controlled values that are specified as part of an enumeration container.

Syntax
^^^^^^

Enumeration values have a restricted syntax for the purpose of facilitating their integration with executable code: they cannot start with a numerical digit, and the only special character that can be associated with them is the underscore ``_``.

In order to handle the integration of FpML scheme values such as the *dayCountFractionScheme* which has values such as ``ACT/365.FIXED`` or ``30/360``, the creation of a **displayName synonym** has been enabled, so that those values can be represented as, respectively, ``ACT_365_FIXED`` and ``_30_360`` as enumeration values, with the values of ``ACT/365.FIXED`` and ``30/360`` as display names.

 .. code-block:: Java

   enum DayCountFractionEnum <"The enumerated values to specify the day count fraction.">
   	[synonym FpML value dayCountFractionScheme]
   {
   	_1_1 displayName "1/1" <"Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (a) or Annex to the 2000 ISDA Definitions (June 2000 Version), Section 4.16. Day Count Fraction, paragraph (a).">
   		[synonym FpML value "1/1"],
   	_30_360 displayName "30/360" <"Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (f) or Annex to the 2000 ISDA Definitions (June 2000 Version), Section 4.16. Day Count Fraction, paragraph (e).">
   		[synonym FpML value "30/360"],
   	_30E_360 displayName "30E/360" <"Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (g) or Annex to the 2000 ISDA Definitions (June 2000 Version), Section 4.16. Day Count Fraction, paragraph (f). Note that the algorithm defined for this day count fraction has changed between the 2000 ISDA Definitions and 2006 ISDA Definitions. See Introduction to the 2006 ISDA Definitions for further information relating to this change.">
   		[synonym FpML value "30E/360"],
   	_30E_360_ISDA displayName "30E/360.ISDA" <"DPer 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (h). Note the algorithm for this day count fraction under the 2006 ISDA Definitions is designed to yield the same results in practice as the version of the 30E/360 day count fraction defined in the 2000 ISDA Definitions. See Introduction to the 2006 ISDA Definitions for further information relating to this change.">
   		[synonym FpML value "30E/360.ISDA"],
   	ACT_360 displayName "ACT/360" <"Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (e) or Annex to the 2000 ISDA Definitions (June 2000 Version), Section 4.16. Day Count Fraction, paragraph (d).">
   		[synonym FpML value "ACT/360"],
   	ACT_365_FIXED displayName "ACT/365.FIXED" <"Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (d) or Annex to the 2000 ISDA Definitions (June 2000 Version), Section 4.16. Day Count Fraction, paragraph (c).">
   		[synonym FpML value "ACT/365.FIXED"],
   	ACT_365L displayName "ACT/365L" <"Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (i).">
   		[synonym FpML value "ACT/365L"],
   	ACT_ACT_AFB displayName "ACT/ACT.AFB" <"The Fixed/Floating Amount will be calculated in accordance with the 'BASE EXACT/EXACT' day count fraction, as defined in the 'Definitions Communes plusieurs Additifs Techniques' published by the Association Francaise des Banques in September 1994.">
   		[synonym FpML value "ACT/ACT.AFB"],
   	ACT_ACT_ICMA displayName "ACT/ACT.ICMA" <"Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (c). This day count fraction code is applicable for transactions booked under the 2006 ISDA Definitions. Transactions under the 2000 ISDA Definitions should use the ACT/ACT.ISMA code instead.">
   		[synonym FpML value "ACT/ACT.ICMA"],
   	ACT_ACT_ISDA displayName "ACT/ACT.ISDA" <"Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (b) or Annex to the 2000 ISDA Definitions (June 2000 Version), Section 4.16. Day Count Fraction, paragraph (b). Note that going from FpML 2.0 Recommendation to the FpML 3.0 Trial Recommendation the code in FpML 2.0 'ACT/365.ISDA' became 'ACT/ACT.ISDA'.">
   		[synonym FpML value "ACT/ACT.ISDA"],
   	ACT_ACT_ISMA displayName "ACT/ACT.ISMA" <"The Fixed/Floating Amount will be calculated in accordance with Rule 251 of the statutes, by-laws, rules and recommendations of the International Securities Market Association, as published in April 1999, as applied to straight and convertible bonds issued after December 31, 1998, as though the Fixed/Floating Amount were the interest coupon on such a bond. This day count fraction code is applicable for transactions booked under the 2000 ISDA Definitions. Transactions under the 2006 ISDA Definitions should use the ACT/ACT.ICMA code instead.">
   		[synonym FpML value "ACT/ACT.ISMA"],
   	BUS_252 displayName "BUS/252" <"The number of Business Days in the Calculation Period or Compounding Period in respect of which payment is being made divided by 252.">
   		[synonym FpML value "BUS/252"]
   }


The **synonym syntax** associated with enumeration values differs in two respects from the synonyms associated with other CDM artefacts:

* The synonym value is of type ``string``, for the above reason related to the need to facilitate integration with executable code. (The alternative approach consisting in specifying the value as a compatible identifier alongside with a display name has been disregarded because it has been deemed not appropriate to create a 'code-friendly' value for the respective synonyms. A ``string`` type removes such need.)
* Although this use case is not part of the current CDM scope, the ability to associate a definition to a synonym value has been enabled, the objective being to effectively support the FIX use cases where the synonym value is a letter or numerical code, which is then positioned as the prefix of the associated definition. The ``TimeInForceEnum`` illustrates this approach.

  .. code-block:: Java

   enum TimeInForceEnum <"The enumeration values to specify the period of time during which an order remains in effect.">
  	[synonym FIX value TimeInForce tag 59]
    {
    	Day <"Day (or session)">
    		[synonym FIX value "0" definition "0 = Day (or session)"],
    	GoodTillCancel <"Good Till Cancel (GTC)">
    		[synonym FIX value "1" definition "1 = Good Till Cancel (GTC)"],
    	AtTheOpening <"At the Opening (OPG)">
    		[synonym FIX value "2" definition "2 = At the Opening (OPG)"],
    	ImmediateOrCancel <"Immediate Or Cancel (IOC)">
    		[synonym FIX value "3" definition "3 = Immediate Or Cancel (IOC)"],
    	FillOrKill <"Fill Or Kill (FOK)">
    		[synonym FIX value "4" definition "4 = Fill Or Kill (FOK)"],
    	GoodTillCrossing <"Good Till Crossing (GTX)">
    		[synonym FIX value "5" definition "5 = Good Till Crossing (GTX)"],
    	GoodTillDate <"Good Till Date (GTD)">
    		[synonym FIX value "6" definition "6 = Good Till Date (GTD)"],
    	AtTheClose <"At the Close">
    		[synonym FIX value "7" definition "7 = At the Close"],
    	GoodThroughCrossing <"Good Through Crossing">
    		[synonym FIX value "8" definition "8 = Good Through Crossing"],
    	AtCrossing <"At Crossing">
    		[synonym FIX value "9" definition "9 = At Crossing"]
    }


Aliases
~~~~~~~

Purpose
^^^^^^^

Two considerations stand behind the introduction of aliases as part of Rosetta:

* The recognition that model tree expressions can be cumbersome at time and hence may contradict the primary goals of clarity and legibility that are associated with Rosetta. The current CDM model only makes use of the alias as part of the interest calculation syntax, with the ``period`` alias which purpose is to provide further clarity and legibility as part of the date arguments associated with the day count fraction computation.

  .. code-block:: Java

  alias period CalculationPeriod( InterestRatePayout -> calculationPeriodDates )

  endYear : is period -> endDate -> year
  startYear : is period -> startDate -> year
  endMonth : is period -> endDate -> month
  startMonth : is period -> startDate -> month
  startDay : is Min( period -> startDate -> day, 30 )
  endDay : is Min( period -> endDate -> day, 30 )

  .. code-block:: Java

It can be expected that the further developments of the CDM might make a broader use of this syntax artefact.

* As part of the Rosetta model which has been used as an input for the CDM, aliases have been used to express key concepts such as the price or the notional of a financial instrument in order to provide a straightforward and cohesive way to express / access them across products. The aliases ``CdsNotional`` and ``IrsInitialNotional`` were good illustrations of such approach.

  .. code-block:: Java

    alias CdsNotional <"The notional of credit default swap.">
    	CreditDefaultSwap -> protectionTerms -> calculationAmount

    alias IrsInitialNotional <"The initial notional of an interest rate swap.">
    	Swap -> swapStream -> calculationPeriodAmount -> calculation -> notionalSchedule -> notionalStepSchedule -> initialValue

Syntax
^^^^^^

The alias syntax is straightforward: ``alias <name> <Rosetta expression>``.

The alias name needs to be unique across the aliases, and validation logic is in place to enforce this. The naming convention is to have one CamelCased word, instead of a composite name as for the Rosetta rules, with implied meaning.

The examples referred to above provide good illustrations of that syntax and naming.

External Reference Artefacts
-----------------------------

Synonyms
~~~~~~~~

Purpose
^^^^^^^

Synonym is the baseline building block in the relationship between the CDM and alternative data representations, whether those are open standards or proprietary data representations. It can be complemented by relevant mapping logic when the relationship is not a one-to-one or is conditional.

Synonyms can be associated to all four sets of Rosetta data modelling artefacts:

*  Classes
*  Attributes
*  Enumerations
*  Enumeration values

There is no limit to the number of synonyms that can be associated with each of those Rosetta data modelling artefacts, and there can even be several synonyms for a given data source.

Syntax
^^^^^^

The baseline synonym syntax has two components:

*  The **source**, whose possible values are controlled by the grammar and correspond to the various standards and protocols which are subject to associations as part of Rosetta (e.g. ``FpML``, ``ISO 20022``).
*  The **value**, which is of type ``identifier``.

Example:

  ``[synonym FpML value accountTypeScheme]``

A further set of attributes can be associated with a synonym, to address specific use cases:

*  A **tag** (e.g. ``[synonym FIX value AccountType tag 581]``) or a **componentID** (e.g. ``[synonym FIX value RateSource componentID 1062]``) can be associated to a synonym value. Those are of type ``integer``. The purpose here is to properly represent the FIX standard. It should be noted that the ability to set those attributes is not restricted to the source value FIX, because it is expected that further protocol sources will actually be variations of the FIX standard. (Note: this is not a relevant use case as it relates to the current CDM model, which scope is limited to the equivalence with the FpML standard.)
*  A **mapping logic** can be associated to a synonym to address the case where the relationship between the CDM data element and that synonym is subject to a logic of some sort.
*  A **definition** (of type ``string``) can be associated with the enumeration value synonyms, as noted above, the purpose being to provide a more explicit reference to the FIX enumeration values, which are specified through a single digit or letter, which value is then positioned as a prefix to the associated definition.

Rule Artefacts
--------------

Rosetta model currently comprises **five distinct set of rules**, each with its own specific purpose:

-  Mapping rules
-  Projection rules
-  Choice rules
-  Data rules
-  Workflow rules

The syntax that governs those respective rules is governed by the underlying grammar. There is a lot of commonality across those five sets of rules, as one of the perceived ways to achieve a good model legibility is to make use of a common syntax across its various components.

Mapping rules
~~~~~~~~~~~~~

Purpose
^^^^^^^

Mapping rules are designed to programmatically map the various data standards and protocols into Rosetta when the relationship is not a one-to-one or is conditional.

Exposing mapping logic through the Rosetta syntax represents a departure from common practice, typically consisting in tackling such logic through executable code, with the implication that it is not readily accessible beyond software developers. The intent here is to establish an explicit and legible relationship between Rosetta and all other relevant data representations available across the marketplace.

Too much effort is currently spent by marketplace participants to map data representations, and the operational risk implied by the many issues derived from mapping issues is well too high. The vision is to position Rosetta as a new paradigm in this respect.

Syntax
^^^^^^

Mapping rules differ from the other Rosetta rules in that their syntax is not expressed as a stand-alone syntax block that is prefixed with the ``rule`` word. Rather, the mapping rule is positioned as an extension to the synonym expression, and each of the mapping expressions (several mapping expressions can be associated with a given synonym) is prefixed with the ``set`` qualifier, followed by the name of the Rosetta attribute to which the synonym is being mapped to.

The mapping syntax is composed of two (optional) expressions: a **mapping value** that is prefixed with ``to``, which purpose is to provide the ability to map a specific value that is distinct from the one originating from the source document, and a **conditional expression** that is prefixed with ``when``, which purpose is to associate conditional logic to the mapping expression.

The mapping logic associated with the below ``action`` attribute provides a good illustration of such logic.

 .. code-block:: Java

   abstract class Event stereotype preExecution, execution, postExecution
    {
    	messageInformation MessageInformation (0..1);
    	timeStamp EventTimeStamp (1..1);
    	correlation Correlation (1..1) <"The correlation Id provides a lineage across related transactions. While optional in FpML, it is made required as part of the Rosetta model, as there is a need for an event identifier of some sort">;
    	eventDate date (1..1);
    	effectiveDate date (0..1);
    	action ActionEnum (1..1) <"Specifies whether the event is a new, a correction or a cancellation.">;
    		[synonym FpML value isCorrection
    			set action to ActionEnum.new when False,
    			set action to ActionEnum.correct when True]
    		[synonym FIX value TradeReportTransType tag 487
    			set action to ActionEnum.new when ["0", "5"],
    			set action to ActionEnum.correct when "2",
    			set action to ActionEnum.cancel when "1"]
    }


Choice Rules
~~~~~~~~~~~~

Purpose
^^^^^^^

Choice rules apply within the context of a class. They define a choice constraint between a set of attributes. They are meant as a simple and robust construct to translate the XML *xsd:choicesyntax* as part of the Rosetta model, although their usage is not limited to those XML use cases.

Syntax
^^^^^^

Choice rules only apply within the context of a class, and the naming convention is ``<className>_choice``, e.g. ``TradeIdentifier_choice``. If multiple choice rules exist in relation to a class, the naming convention is to suffix the 'choice' term with a number, e.g. ``TradeIdentifier_choice1`` and ``TradeIdentifier_choice2``.

 .. code-block:: Java

     class TradeIdentifier stereotype execution, postExecution <"A class defining a trade identifier issued by the indicated party. Rosetta implementation doesn't extends the base class PartyAndAccountReference because of the choice logic with the issuer element.">
      [synonym FpML value TradeIdentifier]
        {
        id string (0..1);
          [synonym FpML value id]
        issuer Party (0..1) scheme "issuerIdScheme" <"The party that assigns the trade identifier. The FpML required cardinality for the issuing party has been relaxed to accommodate FIX messages.">;
          [synonym FpML value issuer]
        party Party (0..1) reference <"FpML implements this element as a reference to a party.">;
          [synonym FpML value partyReference]
        account Account (0..1) reference <"FpML implements this element as a reference to an account.">;
          [synonym FpML value accountReference]
        tradeId string (0..1) anchor scheme "tradeIdScheme" <"In FIX, the unique ID assigned to the trade entity once it is received or matched by the exchange or central counterparty. In FpML, a trade reference identifier allocated by a party.">;
          [synonym FIX value TradeID tag 1003]
          [synonym FIX value SecondaryTradeID tag 1040]
          [synonym FIX value FirmTradeID tag 1041]
          [synonym FIX value SecondaryFirmTradeID tag 1042]
          [synonym FpML value tradeId]
        versionedTradeId VersionedTradeId (0..1) <"A trade identifier accompanied by a version number.">;
          [synonym FpML value versionedTradeId]
        }

      choice rule TradeIdentifier_choice1 <"Choice rule to represent an FpML choice construct.">
      for TradeIdentifier required choice between
      tradeId and versionedTradeId

      choice rule TradeIdentifier_choice2 <"Choice rule to represent an FpML choice construct.">
      for TradeIdentifier required choice between
      issuer and party

The choice constraint can either be **required** (implying that exactly one of the attributes needs to be present) or **optional** (implying that at most one of the attributes needs to be present).

While most of the choice rules have two attributes, there is no limit to the number of attributes associated with it… within the limit of the number of attributes associated with the class at stake. ``CashSettlement_choice`` is a good illustration of this.

 .. code-block:: Java

   choice rule CashSettlement_choice <"Choice rule to represent an FpML choice construct.">
  	for CashSettlement optional choice between
  	cashPriceMethod and cashPriceAlternateMethod and parYieldCurveAdjustedMethod and zeroCouponYieldAdjustedMethod
  	and parYieldCurveUnadjustedMethod and crossCurrencyMethod and collateralizedCashPriceMethod

Members of a choice rule need to have their lower cardinality set to 0, something which is enforced by a validation rule.

``one of`` syntax as a complement to the choice rule
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

In the case where all the attributes of a given class are subject to a choice logic, Rosetta provides the ability to qualify the class information with the ``one of`` qualifier, that is positioned after the stereotype information. This feature is illustrated by the ``BondOptionStrike`` class.

.. code-block:: Java

  class BondOptionStrike stereotype contractualProduct one of <"A class to specify the strike of a bond or convertible bond option.">
  	[synonym FpML value BondOptionStrike]
  {
  	referenceSwapCurve ReferenceSwapCurve (0..1) <"The strike of an option when expressed by reference to a swap curve. (Typically the case for a convertible bond option.)">;
  		[synonym FpML value referenceSwapCurve]
  	price OptionStrike (0..1);
  		[synonym FpML value price]
  }


Data Rules
~~~~~~~~~~

Purpose
^^^^^^^

Data rules are the primary channel through which data validation is enforced as part of Rosetta.

A good initial illustration of such role relates to how data constraints specified as part of the FIX and FpML documentation are expressed as part of those rules – and hence become part of the executable code case that is generated from the model.

As an example, the ``Notional_notionalStepSchedule`` data rule implements the **FpML ird validation rule #61**, which states that if the notional step schedule is absent, then the initial value of the notional schedule must not be null. While at present the FpML logic needs to be evaluated and transcribed into code by the various teams (with the implication that, more often than not, such logic is actually not enforced), its programmatic implementation is available alongside a legible view of it as part of Rosetta.

  .. code-block:: Java

    class Notional stereotype contractualProduct <"A class defining the notional amount or notional amount schedule associated with a swap stream.">
    	[synonym FpML value Notional]
    {
    	id string (0..1);
    		[synonym FpML value id]
    	notionalStepSchedule NonNegativeAmountSchedule (1..1) <"The notional amount or notional amount schedule expressed as explicit outstanding notional amounts and dates. In the case of a schedule, the step dates may be subject to adjustments in accordance with any adjustments specified in calculationPeriodDatesAdjustments.">;
    		[synonym FpML value notionalStepSchedule]
    	notionalStepParameters NotionalStepRule (0..1) <"A parametric representation of the notional step schedule, i.e. parameters used to generate the notional schedule.">;
    		[synonym FpML value notionalStepParameters]
    }

    data rule Notional_notionalStepSchedule <"FpML validation rule ird-61 - Context: NonNegativeSchedule (complex type). If step does not exist, then initialValue must not be equal to 0.">
    	when Notional -> notionalStepSchedule -> step is absent
    	then Notional -> notionalStepSchedule -> initialValue <> 0b

(**Note**: the above ``0b`` notation is meant to denote the fact that the ``initialValue`` attribute is of type ``number``.)


Syntax
^^^^^^

Data rules apply to classes and associated attributes (i.e. not to the rule modelling artefacts).

Their name needs to be unique across the model, and the naming convention is in the form of ``<className>_<attributeName>`` where attributeName refers to the attribute to which the rule applies. If the data rule applies to several attributes, it is appropriate to have a naming in the form of ``<className>_<attributeName1>_<attributeName2>``.

Variations from this naming convention are needed. An example of such is the representation of the **FpML ird validation rule #7**, which states that the existence of *compoundingMethod* is required when the calculation period and payment frequencies differ, and prohibited when they are the same. This validation logic is implemented in Rosetta through two data rules, respectively named ``CompoundingMethod_paymentFrequency_same`` and ``CompoundingMethod_paymentFrequency_different``.

 .. code-block:: Java

   data rule CompoundingMethod_paymentFrequency_same <"FpML validation rule ird-7 1/2 - Context: InterestRateStream (complex type). The existence of compoundingMethod is required when the calculation-period and payment frequencies differ, and prohibited when they are the same.">
   	when InterestRateStream -> paymentDates -> paymentFrequency -> period = InterestRateStream -> calculationPeriodDates -> calculationPeriodFrequency -> period
   		and InterestRateStream -> paymentDates -> paymentFrequency -> periodMultiplier = InterestRateStream -> calculationPeriodDates -> calculationPeriodFrequency -> periodMultiplier
   	then InterestRateStream -> calculationPeriodAmount -> calculation -> compoundingMethod must be absent

    data rule CompoundingMethod_paymentFrequency_different <"FpML validation rule ird-7 2/2 - Context: InterestRateStream (complex type). The existence of compoundingMethod is required when the calculation-period and payment frequencies differ, and prohibited when they are the same.">
     	when (InterestRateStream -> paymentDates -> paymentFrequency -> period <> InterestRateStream -> calculationPeriodDates -> calculationPeriodFrequency -> period
     		and InterestRateStream -> paymentDates -> paymentFrequency -> periodMultiplier <> InterestRateStream -> calculationPeriodDates -> calculationPeriodFrequency -> periodMultiplier)
     		or (InterestRateStream -> paymentDates -> paymentFrequency -> period <> InterestRateStream -> calculationPeriodDates -> calculationPeriodFrequency -> period
     		or InterestRateStream -> paymentDates -> paymentFrequency -> periodMultiplier <> InterestRateStream -> calculationPeriodDates -> calculationPeriodFrequency -> periodMultiplier)
     	then InterestRateStream -> calculationPeriodAmount -> calculation -> compoundingMethod must exist

Another variation example of this naming convention is ``CalculationPeriodFrequency_rollConvention_M_Y``, which sets constraints with respect to the enumeration values applicable to one attribute as a function of the values applicable to another one; as a result, the rule name suffixes the attribute which is subject to that logic with a hint about the conditional terms. This provides an appropriate differenciation with the two other data rules that apply to the ``CalculationPeriodFrequency`` class, as illustrated below.

 .. code-block:: Java

  data rule CalculationPeriodFrequency_rollConvention_M_Y <"FpML validation rule ird-57 - Context: CalculationPeriodFrequency. [period eq ('M', 'Y')] not(rollConvention = ('NONE', 'SFE', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT','SUN')).">
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

   data rule CalculationPeriodFrequency_rollConvention_W <"FpML validation rule ird-58 - Context: CalculationPeriodFrequency (complex type). When the period is 'W', the rollConvention must be a week day, 'SFE' or 'NONE'.">
   	when CalculationPeriodFrequency -> period = PeriodExtendedEnum.W
   	then CalculationPeriodFrequency -> rollConvention = RollConventionEnum.NONE
   		or CalculationPeriodFrequency -> rollConvention = RollConventionEnum.SFE
   		or CalculationPeriodFrequency -> rollConvention = RollConventionEnum.MON
   		or CalculationPeriodFrequency -> rollConvention = RollConventionEnum.TUE
   		or CalculationPeriodFrequency -> rollConvention = RollConventionEnum.WED
   		or CalculationPeriodFrequency -> rollConvention = RollConventionEnum.THU
   		or CalculationPeriodFrequency -> rollConvention = RollConventionEnum.FRI
   		or CalculationPeriodFrequency -> rollConvention = RollConventionEnum.SAT
   		or CalculationPeriodFrequency -> rollConvention = RollConventionEnum.SUN

   data rule CalculationPeriodFrequency_rollConvention_T <"FpML validation rule ird-60 - Context: CalculationPeriodFrequency (complex type). When the period is 'T', the rollConvention must be 'NONE'.">
   	when CalculationPeriodFrequency -> period = PeriodExtendedEnum.T
   	then CalculationPeriodFrequency -> rollConvention = RollConventionEnum.NONE

Main data rule syntax
'''''''''''''''''''''

The main data rule syntax is in the form of ``when <Rosetta expression> then <Rosetta expression>``.

Here are a set of relevant examples of this data rule syntax:

*   ``CompoundingMethod_fixedRate`` combines two Boolean assertions.

 .. code-block:: Java

   data rule CompoundingMethod_fixedRate <"FpML validation rule ird-29 - Context: Calculation (complex type). If compoundingMethod exists, then fixedRateSchedule must not exist.">
  	when Calculation -> compoundingMethod exists
  	then Calculation -> fixedRateSchedule must be absent

*   ``CalculationPeriod_calculationPeriodNumberOfDays`` involves an operator.

 .. code-block:: Java

   data rule CalculationPeriod_calculationPeriodNumberOfDays <"FpML specifies calculationPeriodNumberOfDays as a positive integer.">
  	when PaymentCalculationPeriod -> calculationPeriod -> calculationPeriodNumberOfDays exists
  	then PaymentCalculationPeriod -> calculationPeriod -> calculationPeriodNumberOfDays >= 0

*   ``CalculationPeriodDates_firstPeriodStartDate_stubPeriodType`` involves three assertions as part of the ``when`` statement, two of which consist in evaluating Boolean values.

 .. code-block:: Java

   data rule CalculationPeriodDates_firstPeriodStartDate_stubPeriodType <"FpML specifies that the firstRegularPeriodStartDate must only be specified if there is an initial stub calculation period.">
  	when CalculationPeriodDates -> stubPeriodType is absent
  		or ( CalculationPeriodDates -> stubPeriodType <> StubPeriodTypeEnum.ShortInitial
  			and CalculationPeriodDates -> stubPeriodType <> StubPeriodTypeEnum.LongInitial )
  	then CalculationPeriodDates -> firstRegularPeriodStartDate must be absent

*   ``SettlementProvision_settlementCurrency`` makes use of parentheses for the purpose of supporting nested assertions.

 .. code-block:: Java

   data rule SettlementProvision_settlementCurrency <"FpML specifies that the SettlementProvision exists when the settlement currency is different to the notional currency of the trade.">
  	when ( InterestRateStream -> settlementProvision exists
  		and InterestRateStream -> settlementProvision -> settlementCurrency <> InterestRateStream -> calculationPeriodAmount -> calculation -> notionalSchedule -> notionalStepSchedule -> currency )
  	or ( InterestRateStream -> settlementProvision exists
  		and InterestRateStream -> settlementProvision -> settlementCurrency <> InterestRateStream -> calculationPeriodAmount -> calculation -> fxLinkedNotionalSchedule -> varyingNotionalCurrency )
  	then InterestRateStream -> settlementProvision -> nonDeliverableSettlement must exist

Conditional data rule syntax
''''''''''''''''''''''''''''

Rosetta grammar also provides the ability to associate a conditional syntax to the Rosetta expression following the ``then`` statement.

A good illustration of this syntax is the ``NaturalPersonIdentifier_country``, which purpose is to programmatically express the ESMA RTS 22 provision which specifies that the natural person identifier to be used (e.g. passport, driver license number, …) varies as a function of the EU country.

 .. code-block:: Java

   data rule NaturalPersonIdentifier_country <"ESMA RTS 22 specifies the natural person identifier to be used as a function of the EU country.">
  	[regulatoryReference ESMA_MiFIR specification "ISO 20022 - Part 2" section "20.1.8.2.2" provision "In the scope of MIFIR, the scheme name is restricted to a passport number (use of code CCPT), any other national identifier (use of code NIDN) or CONCAT (use of proprietary with value CONCAT). Issuer field will be ignored and should not be populated by reporting parties."]
  	[regulatoryReference ESMA_MiFIR regulation "RTS 22" annex "II" provision "National client identifiers for natural persons to be used in transaction reports."]
  	when Party -> naturalPerson exists
  		and Party -> regulatoryQualification_MiFID_II -> isEligibleTo = True
  	then
  		if Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.AT
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.DE
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.FR
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.HU
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.IE
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.LU
  			then Party -> partyId -> proprietaryScheme = "CONCAT"
  		else if Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.EE
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.ES
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.IS
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.IT
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.PL
  			then Party -> partyId -> partyIdSource = PartyIdSourceEnum.NIDN
  		else if Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.BE
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.BG
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.DK
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.FI
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.GB
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.GR
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.HR
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.LV
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.NO
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.SE
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.SI
  			then Party -> partyId -> partyIdSource = PartyIdSourceEnum.NIDN
  				or Party -> partyId -> proprietaryScheme = "CONCAT"
  		else if Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.MT
  			then Party -> partyId -> partyIdSource = PartyIdSourceEnum.CCPT
  				or Party -> partyId -> partyIdSource = PartyIdSourceEnum.NIDN
  		else if Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.CZ
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.LI
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.LT
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.NL
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.PT
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.RO
  			or Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.SK
  			then Party -> partyId -> partyIdSource = PartyIdSourceEnum.CCPT
  				or Party -> partyId -> partyIdSource = PartyIdSourceEnum.NIDN
  				or Party -> partyId -> proprietaryScheme = "CONCAT"
  		else if Party -> naturalPerson -> nationality -> alpha2 = CountryAlpha2Enum.CY
  			then Party -> partyId -> partyIdSource = PartyIdSourceEnum.CCPT
  				or Party -> partyId -> proprietaryScheme = "CONCAT"
  		else Party -> partyId -> partyIdSource = PartyIdSourceEnum.CCPT
  				or Party -> partyId -> proprietaryScheme = "CONCAT"


CDM Model
=========


Standardising Data and Workflows
--------------------------------

Machine Executable ISDA Definitions
-----------------------------------


This section presents an outline of the **four dimensions of the CDM model representation**: event, product, reference data.

Event Model
-----------

Rosetta model representation encompasses the pre-execution, execution and post-execution lifecycle.

Its implementation takes into consideration the concept specified as part of the `ISDA CDM Design Definition Document <https://www.isda.org/a/gVKDE/CDM-FINAL.pdf>`__, while extending it to the pre-execution space as well as to the listed products.

Baseline event modelling features
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Two classes act as foundational blocks for the Rosetta event model: the ``Event`` abstract class and the ``FinancialTransaction`` root class.

* All events inherit from the ``Event`` abstract class, which includes five set of information:

  - **Messaging information**, such as ``messageId``, ``sentBy``, ``sentTo``; this information is optional, as possibly not applicable in a context such as blockchain;
  - **Timestamp information**;
  - **Event identification** information, leveraging the FpML *correlation* construct;
  - **Time dimension** information, through the event date and effective date;
  - **Action qualification**, to specify whether the event is a new one, a correction or a cancellation of a prior one.

  .. code-block:: Java

   abstract class Event stereotype preExecution, execution, postExecution
     {
     	messageInformation MessageInformation (0..1);
     	timeStamp EventTimeStamp (1..1);
     	correlation Correlation (1..1) <"The correlation Id provides a lineage across related transactions. While optional in FpML, it is made required as part of the Rosetta model, as there is a need for an event identifier of some sort">;
     	eventDate date (1..1);
     	effectiveDate date (0..1);
     	action ActionEnum (1..1) <"Specifies whether the event is a new, a correction or a cancellation.">;
     		[synonym FpML value isCorrection
     			set action to ActionEnum.new when False,
     			set action to ActionEnum.correct when True]
     		[synonym FIX value TradeReportTransType tag 487
     			set action to ActionEnum.new when ["0", "5"],
     			set action to ActionEnum.correct when "2",
     			set action to ActionEnum.cancel when "1"]
     }

* ``FinancialTransaction`` is positioned as the 'entry point' for all financial transactions. It is used as such as part of the Rosetta graphical navigation.

  .. code-block:: Java

    root class FinancialTransaction one of <"The entry point for all financial transactions.">
    {
    	requestForQuote RequestForQuote (0..1);
    		[synonym FIX value MsgType."R"]
    	quote Quote (0..1);
    		[synonym FIX value MsgType."S"]
    		[synonym FIX value MsgType."Z" set quote -> Quote -> action to ActionEnum.cancel]
    	order Order (0..1);
    		[synonym FIX value MsgType."D"]
    		[synonym FpML value orderReport]
    	execution Execution (0..1);
    		[synonym FIX value MsgType."8"]
    		[synonym FpML value executionNotification]
    	clearing Clearing (0..1);
    	allocation Allocation (0..1);
    	optionExercise OptionExercise (0..1);
    	partialTermination PartialTermination (0..1);
    	termination Termination (0..1);
    	intermediation Intermediation (0..1);
    	aggregation Aggregation (0..1);
    	portfolioCompression PortfolioCompression (0..1);
    }

Event typology
~~~~~~~~~~~~~~

While the `ISDA CDM Design Definition Document <https://www.isda.org/a/gVKDE/CDM-FINAL.pdf>`__ proposes to operate a distinction between *Independent Events* (those that have to be negotiated) and *Dependent Events* (those that don't involve a negotiation between parties), Rosetta current event implementation is slightly different and articulated around the distinction between the following **three types of events**:

* **Transaction events** are characterised by the fact that they **require party information** and, as an implication, may involve regulatory eligibility qualification. As a result, the set of events which are part of that scope is slightly broader than those that are part of the *Independent Events*, as the **option exercise** would, among others, also be part of it. While the *Independent Events* focuses on the underlying driver for the event (the negotiation), the Rosetta approach focuses upon the actual event features (presence of party information).
* **Market events** are characterised by the fact they **do not involve party nor contract information**. A typical example of such is an **observation event**. When applicable to listed products, market events may involve product information (e.g. a dividend event).
* **Servicing events** do not involve party information, but do **require contract information**. An example of such is a **reset event**, which will refer to an observation event and will carry reference to the contract(s) to which it applies.

Rosetta event model applies the design principle specified as part of the `ISDA CDM Design Definition Document <https://www.isda.org/a/gVKDE/CDM-FINAL.pdf>`__ by structuring the transaction events via a ``before`` and ``after`` modelling construct.  This modelling construct is applied differently depending upon certain considerations:

* **Post-execution events applicable to contractual products** are specified by a ``before`` and ``after`` **contract** state, with a cardinality that varies as a function of the event, as suggested by the CDM Concept Paper. Leveraging Rosetta rule logic capability, the model associates contract state logic to those events, as shown below in relation to the termination event. In some cases, as for the **partial termination event**, the ``before`` and ``after`` qualification is complemented with some further relevant information (in such case, the variation in the quantity/notional, which is deemed more efficient to compute upfront at event creation rather than having to be inferred by each consumer).  The below are a sample example of such post-execution transaction events:

  .. code-block:: Java

    class Termination extends TransactionEvent stereotype postExecution
    {
    	before PostExecutionContractState (1..1);
    	after PostExecutionContractState (1..1);
    }

    data rule Termination_Contract_before <"The contractState before a termination event is 'open'.">
    	when Termination exists
    	then Termination -> before -> contract -> contractState = ContractStateEnum.open

    data rule Termination_Contract_after <"The contractState after a termination event is 'close'.">
    	when Termination exists
    		and Termination -> action <> ActionEnum.cancel
    	then Termination -> after -> contract -> contractState = ContractStateEnum.close

    data rule Termination_close <"When an termination event is cancelled, the contract state is 'open'.">
    	when Termination exists
    		and Termination -> action = ActionEnum.cancel
    	then Termination -> after -> contract -> contractState = ContractStateEnum.open

  .. code-block:: Java

    class PartialTermination extends TransactionEvent  stereotype postExecution
    {
    	before PostExecutionContractState (1..1);
    	after PostExecutionContractState (1..1);
    	change Quantity (1..1);
    }
  .. code-block:: Java

    class Aggregation extends TransactionEvent stereotype postExecution
    {
    	before PostExecutionState (2..*);
    	after PostExecutionState (1..1);
    }

  The ``PostExecutionContractState`` class associated with those events is composed of a single contract (as the contract cardinality is handled at the event level) and optional fee(s).

  .. code-block:: Java

    class PostExecutionContractState stereotype postExecution
    {
    	contract Contract (1..1);
    	fee Payment (0..*);
    }

  Referring to the list of events specified as part of the `ISDA CDM Design Definition Document <https://www.isda.org/a/gVKDE/CDM-FINAL.pdf>`__, this modelling approach applies to the following events:

    - Portfolio compression
    - Termination
    - Partial termination


* **Post-execution events applicable to contractual and listed products**, such as the **intermediation event**, differ by the fact that the attributes ``before`` and ``after`` are of type ``PostExecutionState`` in order to provide for the ability to **specify the resulting state as a contract, a listed product or a package transaction** (with the package transaction having a further product qualification tree, as detailed in the below Product section).

  .. code-block:: Java

    class Intermediation extends TransactionEvent stereotype postExecution
    {
    	before PostExecutionState (1..1);
    	after PostExecutionState (1..*);
    }

  .. code-block:: Java

    class PostExecutionState stereotype postExecution
    {
    	listedProduct ListedProduct (0..1);
    	contract Contract (0..1);
    		[synonym FpML value trade]
    	packageTransaction PackageTransaction (0..1);
    		[synonym FpML value tradePackage]
    	fee Payment (0..*);
    }

    choice rule PostExecutionState_choice
    	for PostExecutionState required choice between
    	listedProduct and contract and packageTransaction

  This modeling approach applies to the following events:

    - Clearing
    - Allocation
    - Option exercise (to take into consideration the listed options)
    - Intermediation
    - Aggregation

* **Pre-execution and execution events** differ from post-execution events by the fact that they are characterised by 'workflow' rather than 'state' considerations.  As a result, the ``before`` attribute refers to a pre-execution event, while the ``after`` attribute only applies to the execution event.

  Taking the example of the ``Order`` event below, the ``before`` state associated with it will correspond to (possibly) another ``Order`` event (e.g. in the case where that event is a correction) and its ``after`` state can only be qualified by the fact that this order is outstanding. A number of FIX attributes associated with this event have been integrated as part of this Rosetta representation.

  .. code-block:: Java

    class Order extends TransactionEvent stereotype preExecution // To be renamed Order once the other is deprecated
    	[synonym FIX value NewOrderSingle componentID 14]
    	[regulatoryReference ESMA_MiFIR regulation "RTS 22" article "4" provision "Specifies the order details (i.e. attributes) which shall be transmitted by an investment firm in relation with an order. A list of 10 attributes is specified: identification code of the financial instrument, acquisition/disposal indication, price and quantity, identification of the client and its decision maker, short sale indicator, identification of the decision maker or algorithm at the investment firm, identification of investment firm and the branch where the decision maker is located, indication of whether the order is meant to reduce risk in the case where the product is a commodity derivative."]
    {
    	before PreExecutionFinancialTransaction (0..1);
    	product PreExecuted (1..1) <"The contractual product, listed product or package transaction which is subject the transaction; corresponds to the Instrument block in the FIX protocol.">;
    	side SideEnum (1..1) <"The side of the order, i.e. buy or sell.">;
    		[synonym FIX value Side tag 54]
    	quantity Quantity (0..1) <"The quantity associated with the order.">;
    		[synonym ISO_20022 value Qty]
    	cashOrderQuantity number (0..1) <"Specifies the approximate order quantity desired in total monetary units vs. as tradeable units (e.g. number of shares). The broker or fund manager (for CIV orders) would be responsible for converting and calculating a tradeable unit (e.g. share) quantity (FIX OrderQty (38) attribute, part of the Quantity class in the canonical model) based upon this amount to be used for the actual order and subsequent messages.">;
    		[synonym FIX value CashOrderQty tag 152]
    	orderType OrderTypeEnum (1..1) <"The type of order (e.g. limit, market), as specified by an enumeration.">;
    		[synonym FIX value OrdType tag 40]
    	solicitedFlag boolean (0..1) <"Indicates whether or not the order was solicited.">;
    		[synonym FIX value SolicitedFlag tag 377]
    	stopPrice number (0..1) <"The top price, per unit of quantity.">;
    		[synonym FIX value StopPx tag 99]
    	timeInForce TimeInForceEnum (0..1) <"Specifies how long the order remains in effect. According to FIX, absence of this field is interpreted as DAY, while it is not applicable to Collective Investment Vehicles (CIV) Orders.">;
    		[synonym FIX value TimeInForce tag 59]
    	expireDate date (0..1) <"Date of order expiration (last day the order can trade), always expressed in terms of the local market date. The time at which the order expires is determined by the local market’s business practices.">;
    		[synonym FIX value ExpireDate tag 432]
    	expireTime time (0..1) <"Time of order expiration (always expressed in UTC) The meaning of expiration is specific to the context where the field is used. For orders, this is the expiration time of a Good Til Date TimeInForce.">;
    		[synonym FIX value ExpireTime tag 126]
    }

The ``product`` attribute associated with those pre-execution events is of type ``PreExecuted``, which differs from the post-execution class ``PostExecutionState`` by the fact that it references the ``ContractualProduct`` class rather than the ``Contract`` class.  Those two classes differ by the fact that the ``ContractualProduct`` only includes the economic terms which are specified at the pre-execution stage.  This corresponds to the FpML difference between the pre-trade and confirmation views of the *Trade*.  The distinction between those two modelling constructs is further detailed in the below product section.

  .. code-block:: Java

    class PreExecuted stereotype preExecution one of <"The set of products applicable to pre-execution activity.">
    {
    	listedProduct ListedProduct (0..1);
    	contractualProduct ContractualProduct (0..1);
    		[synonym FpML value trade set contractualProduct when RequestForQuote or Quote or Order exists]
    	packageTransaction PackageTransaction (0..1);
    		[synonym FpML value tradePackage]
    }

  .. code-block:: Java

    class Execution extends TransactionEvent stereotype execution // To be renamed Execution once the other is deprecated
    {
    	before PreExecutionFinancialTransaction (0..1);
    	after PostExecutionState (1..1);
    	side SideEnum (0..1) <"The side of the execution, e.g. buy or sell. It is not applicable in the case where the execution involves a contractual product, as the side is represented as part of it.">;
    		[synonym FIX value Side tag 54]
    	quantity Quantity (0..1) <"The quantity associated with the execution. It is not applicable in the case where the execution involves a contractual product, as the quantity is represented as part of it.">;
    		[synonym ISO_20022 value Qty]
    	price Price (0..1) <"The price. As part of the Rosetta model, it is not applicable if the product is a contractual instrument, as the price is then represented through those contractual terms.">;
    		[synonym ISO_20022 value Pric]
    	executionType ExecutionTypeEnum (1..1) <"FIX definition: Describes the specific ExecutionRpt (i.e. Pending Cancel) while OrdStatus (39) will always identify the current order status (i.e. Partially Filled).">;
    		[synonym FIX value ExecType tag 150]
    	solicitedFlag boolean (0..1) <"Indicates whether or not the execution was solicited.">;
    		[synonym FIX value Side tag 377]
    	executionDateTime dateTime (0..1);
    		[synonym FpML value executionDateTime]
    }

  This modeling approach applies to the following events:

    - Request for quote
    - Quote
    - Order
    - Execution

Product Model
-------------

Rosetta product representation is articulated around a distinction between **contractual products** and **listed products**, as those differ fundamentally in terms of:

*  **Access to the economic terms**, abstracted through a product identifier in the case of listed products, spelled out through the contract terms (and, possibly, also referenced as part of the master agreement) in the case of contractual products;
*  **Fungibility**, handled at the contract level in the case of contractual products, at the product identifier level in the case of fungible products;
*  **Data representation from pre-execution through post-execution**, with contractual products still represented at the transaction/trade level, although with a varying level of information, while listed products evolve from a transaction to a position representation once the post-execution stage is reached.

Once **secured funding** and **loan** products are introduced as part of the model, they will be positioned alongside those two broad categories, as they have both fungible and contractual product characteristics.

As detailed in the prior section, those products representations are accessed by the transaction events through three classes:

*  The ``PreExecuted`` class, which provides a choice between a listed product, a contractual product and a package transaction;
*  The ``PostExecutionState`` class, which provides a choice between a listed product, a contract and a package transaction;
*  The ``PostExecutionContractState`` class, which provides access to a contract.

Contractual products
~~~~~~~~~~~~~~~~~~~~

Contractual products are bilateral contracts between two parties, which terms are specified at trade inception and apply throughout the life of the contract. Contractual products are fungible only under specific terms (e.g. existence of a close-out netting agreement between the parties).

As detailed as part of the above Event section, Rosetta provides two contractual product representations: the ``ContractualProduct`` class is used for **pre-execution purposes**, and the ``Contract`` class for **post-execution purposes**. In essence, those respectively correspond to the FpML pre-trade and confirmation views of the *Trade* complex type.

This Rosetta terminology is meant to reflect the fact that a ‘financial product’ is transacted pre-execution, while a ‘contract’ only exists post-execution. In this respect, the FpML *trade* term is deemed ambiguous, and its use as part of the standard is largely due to an exclusive focus on post-execution activity in the initial stages of its development. Later adjustments in this respect would have been made difficult as a result of backward compatibility considerations. This adjustment is made easy as part of Rosetta, thanks to the synonym approach to establish a resilient relationship with other data representations.

Pre-execution: the contractual product
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

As just indicated, the ``ContractualProduct`` class is conceptually similar to the pre-trade view of the FpML *Trade* complex type. It provides a choice between the respective contractual product representations (i.e. ``Swap``, ``Fra``, ``CreditDefaultSwap``, etc.), which themselves inherit from the abstract class ``ContractualHeader`` (Rosetta doesn’t support downcasting, hence the positioning of the respective product variations as attributes of the ``ContractualProduct`` class, rather than through an inheritance paradigm).

Because the ``ContractualProduct`` is meant to be used in a pre-execution context, the ``ContractualHeader`` abstract class only has a very limited set of attributes: ``productTaxonomy`` and ``priceMultiplier``.

 .. code-block:: Java

   class ContractualProduct stereotype productReferenceData, contractualProduct one of <"This Rosetta class corresponds to the FpML Product substitution group.">
    {
    	bulletPayment BulletPayment (0..1) <"A bullet payment product.">;
    		[synonym FpML value bulletPayment]
    	bondOption BondOption (0..1) <"A bond option product.">;
    		[synonym FpML value bondOption]
    	capFloor CapFloor (0..1) <"A cap, floor or cap/floor product.">;
    		[synonym FpML value capFloor]
    	creditDefaultSwap CreditDefaultSwap (0..1) <"A credit default swap product.">;
    		[synonym FpML value creditDefaultSwap]
    	creditDefaultSwapOption CreditDefaultSwapOption (0..1) <"A credit default swap option product.">;
    		[synonym FpML value creditDefaultSwapOption]
    	fra Fra (0..1) <"A forward rate agreement product.">;
    		[synonym FpML value fra]
    	swap Swap (0..1) <"A swap product.">;
    		[synonym FpML value swap]
    	swaption Swaption (0..1) <"A swaption product.">;
    		[synonym FpML value swaption]
    }

Post-execution: the contract
^^^^^^^^^^^^^^^^^^^^^^^^^^^^
The Rosetta ``Contract`` class incorporates all the elements that are part of the FpML *Trade* confirmation view, with the exception of a few elements which usage needs to be confirmed with FpML experts: *tradeSummary*, *originatingPackage*, *allocations* and *approvals*.

The Rosetta ``Contract`` class includes a ``contractState`` attribute whose purpose is to specify the state of a contract (i.e. ``open`` or ``close``) as a result of an event, i.e. the state transition outcome as it relates to the contract state.

 .. code-block:: Java

   class Contract stereotype productReferenceData, contractualProduct <"A class to specify a contract object, which can be invoked either within the context of a financial transaction, or independently from it. It corresponds to the FpML Trade, although restricted to execution and post-execution contexts. Attributes also applicable to pre-execution contexts have been positioned as part of the ContractualHeader abstract class.">
  	[synonym FpML value Trade]
    {
    	contractIdentifier PartyTradeIdentifier (1..*) <"The contract reference identifier(s) allocated by the parties involved in the contract.">;
    		[synonym FpML value partyTradeIdentifier pathExpression "^", "tradeHeader"]
    	tradeDate date (1..1) <"The trade date. This is the date the trade was originally executed. In the case of a novation, the novated part of the trade should be reported (by both the remaining party and the transferee) using a trade date corresponding to the date the novation was agreed. The remaining part of a trade should be reported (by both the transferor and the remaining party) using a trade date corresponding to the original execution date.">;
    		[synonym FpML value tradeDate pathExpression "tradeHeader"]
    		[synonym ISO_20022 value TradDt]
    	clearedDate date (0..1) anchor <"If the trade was cleared (novated) through a central counterparty clearing service, this represents the date the trade was cleared (transferred to the central counterparty).">;
    		[synonym FpML value clearedDate]
    	contractualProduct ContractualProduct (1..1) <"The product information that is associated with the contract. The corresponding FpML construct is the product abstract element and the associated substitution group.">;
    	otherPartyPayment Payment (0..*) <"Other fees or additional payments associated with the contract, e.g. broker commissions, where one or more of the parties involved are not principal parties involved in the contract.">;
    		[synonym FpML value otherPartyPayment]
    	brokerParty Party (0..*) reference <"The party (or parties) that brokered the contract. In FpML, this element is specified as a reference to a party.">;
    		[synonym FpML value brokerPartyReference]
    	calculationAgent CalculationAgentModel (1..1) <"The ISDA Calculation Agent and the associated business center information. This information is represented in FpML via the CalculationAgent.model.">;
    	determiningParty Party (0..2) reference <"The ISDA Determination Party that is specified in the related confirmation as Determination Party.">;
    		[synonym FpML value determiningParty]
    	barrierDeterminationAgent Party (0..1) reference <"The party specified in the related confirmation as Barrier Determination Agent.">;
    		[synonym FpML value barrierDeterminationAgent]
    	hedgingParty Party (0..2) reference <"The ISDA Hedging Party that is specified in the related confirmation as Hedging, or if no Hedging Party is specified, either party to the contract.">;
    		[synonym FpML value hedgingParty]
    	collateral Collateral (0..1) <"The collateral terms associated with the contract.">;
    		[synonym FpML value collateral]
    	documentation Documentation (0..1) <"Defines the definitions that govern the document and should include the year and type of definitions referenced, along with any relevant documentation (such as master agreement) and the date it was signed.">;
    		[synonym FpML value documentation]
    	governingLaw GoverningLawEnum (0..1) <"Identification of the law governing the transaction.">;
    		[synonym FpML value governingLaw]
    	contractState ContractStateEnum (0..1) <"The state qualification of a contractual product, i.e. whether open or close. This attribute is not present as part of the FpML standard.">;
    }

Taking the example of the **swap** product to further illustrate the Rosetta modelling approach for contractual products, the key modelling considerations are as follows:

* Each of the contractual products extend the ``ContractualHeader`` abstract class;
* The Rosetta model follows quite strictly the FpML standard as it relates to the data representation, and extends it through validation logic.
* While the Rosetta model has been manually crafted at this time, once the modelling approach and the associated syntax is firmed up, the plan is to automate the relationship between the FpML and Rosetta data representation to (i) model the further asset classes, and (ii) support the future model versioning.

 .. code-block:: Java

   class Swap extends ContractualHeader stereotype contractualProduct <"A class defining swap streams and additional payments between the principal parties involved in the swap.">
   	[synonym FpML value Swap]
   {
   	swapStream InterestRateStream (1..*) <"The swap streams.">;
   		[synonym FpML value swapStream]
   	earlyTerminationProvision EarlyTerminationProvision (0..1) <"Parameters specifying provisions relating to the optional and mandatory early termination of a swap transaction.">;
   		[synonym FpML value earlyTerminationProvision]
   	cancelableProvision CancelableProvision (0..1) <"A provision that allows the specification of an embedded option within a swap giving the buyer of the option the right to terminate the swap, in whole or in part, on the early termination date.">;
   		[synonym FpML value cancelableProvision]
   	extendibleProvision ExtendibleProvision (0..1) <"A provision that allows the specification of an embedded option with a swap giving the buyer of the option the right to extend the swap, in whole or in part, to the extended termination date.">;
   		[synonym FpML value extendibleProvision]
   	additionalPayment Payment (0..*) <"Additional payments between the principal parties.">;
   		[synonym FpML value additionalPayment]
   		[regulatoryReference CFTC_DFA guideline "ISDA Price Notation and Additional Price Notation Approach" section "Rates" provision "This is the Additional Price Notation (and, when a 2nd fee exists, the Price Notation 3) of an IRS when corresponding to a swap fee."]
   	additionalTerms SwapAdditionalTerms (0..1) <"Contains any additional terms to the swap contract.">;
   		[synonym FpML value additionalTerms]
   }


Listed products
~~~~~~~~~~~~~~~

Listed products have some (or all) of their economic terms abstracted through a **product identifier** and publicly disseminated by a central venue. As a result, fungibility applies as a function of this product identifier.

The Rosetta model for listed products is articuled along the same lines as contractual products:

* The ``ListedProduct`` class provides a **choice between the respective listed product representations**;

 .. code-block:: Java

   class ListedProduct stereotype productReferenceData, listedProduct one of <"Product which terms are abstracted through a product identifier and are then publicly available through a central venue.">
    {
     bond Bond (0..1);
     convertibleBond ConvertibleBond (0..1);
     mortgage Mortgage (0..1);
     listedInterestRateDerivative ListedInterestRateDerivative (0..1);
    }

* A **two-levels class inheritance structure** has been specified to provide for a scalable implementation:

  - All listed products inherit from a ``ListedHeader`` abstract class which contains a ``productTaxonomy``, ``productIdentifier`` and a ``description`` attribute;

  .. code-block:: Java

    abstract class ListedHeader stereotype productReferenceData, listedProduct <"An abstract class to holds the attributes that are common across listed products.">
    {
    	id string (0..1);
    		[synonym FpML value id]
    	productTaxonomy ProductTaxonomy (1..*) <"The product taxonomy value(s) associated with a product.">;
    	productIdentifier ProductIdentifier (1..*) <"There can be several identifiers associated with a given product.">;
    	description string (1..1) <"The product name.">;
    		[synonym FpML value description]
    }

  - Leveraging the FpML approach for underlyer components, a ``FixedIncomeSecurity`` and an ``EquityAsset`` abstract class then provide the commmon attributes for those respective type of instruments.

  .. code-block:: Java

    class FixedIncomeSecurity extends ListedHeader stereotype productReferenceData, listedProduct <"A fixed income security. In FpML, it corresponds to the FixedIncomeSecurityContent.model.">
    {
    	issuer Party (0..1) reference <"FpML implements this element as an href into the party information. Rosetta restricts the type of party that can issue a product to a legal entity. FpML provides the ability to specify the issuer name, but this is deemed insufficient in the context of Rosetta at a time when the LEI is available and of paramount importance to identify entities.">;
    		[synonym FpML value issuerPartyReference]
    	seniority CreditSeniorityEnum (0..1) <"The repayment precedence of a debt instrument, as specified by a set of enumerated values.  FpML specifies that creditSeniorityTradingScheme (specified in Rosetta through the CreditSeniorityTradingEnum) overrides creditSeniorityScheme (specified in Rosetta through the CreditSeniorityEnum) when the underlyer defines the reference obligation used in a single name credit default swap trade.">;
    		[synonym FpML value seniority]
    	couponType CouponTypeEnum (0..1) <"Specifies if the bond has a variable coupon, step-up/down coupon or a zero-coupon.">;
    		[synonym FpML value couponType]
    	couponRate number (0..1) <"Specifies the coupon rate (expressed in percentage) of a fixed income security or convertible bond.">;
    		[synonym FpML value couponRate]
    	maturity date (0..1) <"The date when the principal amount of a security becomes due and payable.">;
    		[synonym FpML value maturity]
    		[synonym FIX value maturityDate]
    	issueDate date (0..1) <"The date on which the instrument was issued.">;
    		[synonym FIX value issueDate]
    }

  - At the moment, listed derivatives products such as the **listed interest rate derivatives** have been positioned alongside those in order to take into consideration the further applicable attributes. This design will need to be confirmed as a function of the ISIN implementation that supports standardised listed derivatives.

  .. code-block:: Java

    class ListedInterestRateDerivative extends ListedHeader stereotype productReferenceData, listedProduct <"The terms applicable to interest rate derivatives which are required to infer a price but are not abstracted through a product identifier.">
    {
    	effectiveDate date (1..1) <"The effective date, meaning the date on which the payoff terms start to be computed.">;
    	terminationDate date (1..1) <"The termination date.">;
    		[synonym FIX value MaturityDate tag 541]
    	spread number (0..2) <"The spread applicable to the floating interest rate reference.  There can be up to two float rates, as in the case of a basis swap.">;
    		[synonym FIX value Spread tag 218]
    		[synonym CFTC_Part43 value PN1 projection Price_Derivatives]
    		[synonym CFTC_Part43 value PN2 projection Price_Derivatives]
    	fixedRate number (0..2) <"The fixed rate. There can be up to two fixed rates, as in the case of a fix-fix swap.">;
    		[synonym CFTC_Part43 value PN1 projection Price_Derivatives]
    		[synonym CFTC_Part43 value PN2 projection Price_Derivatives]
    	fee Money (0..2) <"The ISDA specification for the Price Notation / Additional Price Notation specifies that there can be up to two fees for interest rate derivatives.">;
    		[synonym CFTC_Part43 value PN3 projection Price_Derivatives]
    		[synonym CFTC_Part43 value APN projection Price_Derivatives]
    }


Reference Data Model
--------------------

Rosetta scope as it relates to the reference data modelling components is driven by the need to provide all relevant information to support the product and event components of the model in the pre-execution, execution and post-execution scenarios, including the associated regulatory reporting one.

Below are insights into the following components of this reference data model:

* Entity reference data
* Regulatory eligibility


Entity reference Data
~~~~~~~~~~~~~~~~~~~~~

The ``stereotype entityReferenceData`` is associated with the classes that support that segment of the Rosetta model.

The ``Party`` class is the cornertone of the entity reference data model. As a result of the fact that Rosetta doesn't support downcasting, the ``legalEntity`` and ``naturalPerson`` features are positioned as Party attributes, rather than extend this class.

The regulatory qualification that is specified as part of the ``regulatoryQualification_MiFID_II`` and ``regulatoryQualification_CFTC_DFA`` attributes is detailed as part of the below Regulatory Eligibility section.

 .. code-block:: Java

  class Party stereotype entityReferenceData <"The party class, which is extended through the NaturalPerson and LegalEntity classes.">
  	[synonym FpML value Party]
  	[synonym FpML value relatedParty]
  	[synonym FIX value Parties componentID 1032]
  	[synonym FIX value RootParties componentID 1031]
  {
  	id string (0..1);
  		[synonym FpML value id]
  	partyId PartyIdentifier (1..*) <"The set of identifiers associated with a party.">;
  	account Account (0..*) <"The account that might be specified by the party in relation to a transaction.">;
  	regulatoryQualification_MiFID_II RegulatoryQualification_MiFID_II (0..1) <"The MiFID II regulatory qualifications associated to the party.">;
  	regulatoryQualification_CFTC_DFA RegulatoryQualification_CFTC_DFA (0..1) <"The CFTC DFA regulatory qualifications associated to the party.">;
  	partyRole PartyRoleEnum (0..*) <"The role associated with the party in the context of a transaction, e.g. agent, custodian, exchange, ...">;
  		[synonym FIX value PartyRole tag 452]
  		[synonym FIX value NestedPartyRole tag 538]
  		[synonym FIX value Nested2PartyRole tag 759]
  		[synonym FIX value Nested3PartyRole tag 951]
  		[synonym FIX value Nested4PartyRole tag 1417]
  		[synonym FIX value InstrumentPartyRole tag 1051]
  		[synonym FIX value DerivativeInstrumentPartyRole tag 1295]
  		[synonym FIX value TargetPartyRole tag 1464]
  		[synonym FpML value role]
  	reportingRole ReportingRoleEnum (0..1) <"Identifies the role of this party in reporting this trade (e.g. originator, counterparty).">;
  		[synonym FpML value reportingRole]
  	algorithm Algorithm (0..*) <"Provides information about the algorithm(s) that might be involved in the transaction by the party.  MiFID requires this information.">;
  	decisionMaker boolean (0..1) <"The party that makes the investment decision when the transacting party is not acting in such capacity. The MiFID/MiFIR regulation identifies two such scenarios: (i) when the investment decision is made under a power of representation, or (ii) when the transacting party is a natural person.">;
  		[regulatoryReference ESMA_MiFIR regulation "RTS 22" article "7(2)" provision "Where the client is not the person taking the investment decision in relation to that transaction, the transaction report shall identify the person taking such decision on behalf of the client as specified in fields 12 to 15 for the buyer and in fields 21 to 24 for the seller in Table 2 of Annex I"]
  		[regulatoryReference ESMA_MiFIR regulation "RTS 22" annex "I table 2 #12-15" provision "Specifies the buyer/seller decision maker code (and, in the case where it is a natural person, its details)."]
  	brokerCapacity BrokerCapacityEnum (0..1) <"designates the capacity of the broker involved in the transaction, when applicable.">;
  		[synonym FIX value LastCapacity tag 29]
  	legalEntity LegalEntity (0..1);
  	naturalPerson NaturalPerson (0..1);
  	executionVenue ExecutionVenue (0..1);
  }

  choice rule Party_choice <"A party is either a legal entity or a natural person.">
  	for Party optional choice between
  	legalEntity and naturalPerson


Regulatory eligibility
~~~~~~~~~~~~~~~~~~~~~~

The current scope consideration as it relates to regulatory eligibility is for Rosetta to (i) **provide the product and entity reference data input** to compute the regulatory eligibility assessment, and (ii) **capture the outcome of such eligibility assessment**. The actual computation of such regulatory eligibility assessment is however deemed as out of scope.

This section focuses on the latter component of this regulatory eligibility model, as the former is implictly addressed as part of the above product and entity reference data sections.

Taking into consideration the regulatory provisions that make a distinction between the **transaction eligibility** and **entity eligibility** considerations (such as, but not limited to, the CFTC Dodd Frank Act provisions), Rosetta provides the ability to capture the outcome of a regulatory eligibility assessment at the transaction level and at an entity level.

Transaction eligibility outcome
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

As detailed as part of the above event section, the ``TransactionEvent`` abstract class includes ``party`` and ``eligibilityAssessment`` attributes.

 .. code-block:: Java

  abstract class TransactionEvent extends Event stereotype preExecution, execution, postExecution <"Transaction events are characterised by the fact that they involve parties and, as an implication, regulatory eligibility assessments.">
  {
  	party Party (1..*) reference <"The parties to the transaction event.">;
  		[synonym FpML value partyMessageInformation]
  	eligibilityAssessment TransactionEligibility (1..1) <"The eligibility assessment is expected to be associated with every transaction as a result of an eligibility rule engine.">;
  }

The **transaction eligibility** assessment is modelled as a ``boolean`` attribute that is associated with each of the regulatory regimes, of which two have presently been specified.

 .. code-block:: Java

   class TransactionEligibility stereotype regulatoryEligibility <"The qualification of whether the transaction is subject to the regulatory regime.">
   {
   	isEligibleToMiFID_II boolean (1..1) <"Outcome of the MiFID II transaction eligibility assessment.">;
   	isEligibleToCFTC_DFA boolean (1..1) <"Outcome of the CFTC Dodd-Frank transaction eligibility assessment.">;
   }

Entity eligibility outcome
^^^^^^^^^^^^^^^^^^^^^^^^^^

As detailed as part of the Entity Eligibility section, the ``Party`` class includes two regulatory assessment attributes: ``regulatoryQualification_MiFID_II`` and ``regulatoryQualification_CFTC_DFA``. Once confirmed, those are meant to be applied to further regulatory regimes.

Those classes inherit from the ``abstract class RegulatoryQualification``, which purpose is to qualify the entity eligibility assessment consistently across the various regulatory regimes.

The classes that inherit from that abstract class are meant to specify the entity attributes which qualification depends of regulatory considerations. A typical example of such is the financial entity qualification.

 .. code-block:: Java

   abstract class RegulatoryQualification stereotype regulatoryEligibility
   {
   	isEligibleTo boolean (1..1) <"The qualification of whether the party is subject to the applicable regulatory regime.">;
   }

   class RegulatoryQualification_CFTC_DFA extends RegulatoryQualification stereotype regulatoryEligibility <"The CFTC Dodd-Frank Act regulatory entity level qualification attributes and entity eligibility assertion.">
   {
   	isFinancialEntity boolean (0..1) <"The qualification of whether the entity qualifies as a financial entity according to the CFTC DFA regulation.">;
   		[regulatoryReference CFTC_DFA regulation "17 CFR Part 1" article "240.3a67–6" provision "(...) The term financial entity means: (1) A swap dealer; (2) A major swap participant; (3) A commodity pool as defined in section 1a(10) of the Commodity Exchange Act (7 U.S.C. 1a(10)); (4) A private fund as defined in section 202(a) of the Investment Advisers Act of 1940 (15 U.S.C. 80b–2(a)); (5) An employee benefit plan as defined in paragraphs (3) and (32) of section 3 of the Employee Retirement Income Security Act of 1974 (29 U.S.C. 1002); and (6) A person predominantly engaged in activities that are in the business of banking or financial in nature, as defined in section 4(k) of the Bank Holding Company Act of 1956 (12 U.S.C. 1843k)."]
   	registeredOrganisation RegisteredOrganisationEnum_CFTC (0..1) <"The qualification of the type of organisation as specified by the CFTC CFA regulatory regime, e.g. Swap Dealer. This corresponds to the FpML organizationTypeScheme in FpML, although specified by regulatory regime.">;
   		[synonym FpML value organizationType]
   	isEligibleToCFTC_DFA boolean (0..1) <"Outcome of the CFTC Dodd-Frank entity eligibility assessment. This attribute is optional, as it may not be specified as part of some of the Rosetta use cases.">;
   }

   class RegulatoryQualification_MiFID_II extends RegulatoryQualification stereotype regulatoryEligibility <"The MiFID regulatory qualification attributes, which are of two types: entity-related attributes which qualification is specific to this regulatory regime (e.g. investment firm) and entity level eligibility assertion.">
   {
   	isInvestmentFirm boolean (1..1) <"The qualification of whether the entity qualifies as an investment firm under the local regulation. This qualification being regulation-specified, it is modelled as an attribute of the MiFID regulatory context.">;
   		[synonym ISO_20022 value InvstmtPtyInd]
   		[regulatoryReference ESMA_MiFID_II regulation "Directive 2014/65/EU" article "4.1(1)" provision "'investment firm’ means any legal person whose regular occupation or business is the provision of one or more investment services to third parties and/or the performance of one or more investment activities on a professional basis."]
   		[regulatoryReference ESMA_MiFIR regulation "RTS 22" annex "I table 2 #5" provision "Technical standards for the reporting of transactions must include the boolean indication as to whether the executing entity is an investment firm as defined by Article 4(1) of Directive 2014/65/EU."]
   		[regulatoryReference ESMA_MiFIR specification "ISO 20022 - Part 2" section "20.1.11.2.3" provision "InvestmentPartyIndicator <InvstmtPtyInd> - Definition: Indicates whether the reporting party is defined as an investment firm under the local regulation or not."]
   		[regulatoryReference ESMA_MiFIR specification "2016-ITMG-66 - Annex 1 Validation Rules" field "5" provision "Only 'true' or 'false' values allowed to indicate whether the entity identified in field 4 is an investment firm covered by Directive 2014/65/EU."]
   		[regulatoryReference ESMA_MiFIR specification "2016-ITMG-66 - Annex 1 Validation Rules" field "5" provision "The Investment Firm covered by Directive 2014/65/EU field is mandatory for all new transaction reports."]
   	registeredOrganisation RegisteredOrganisationEnum_MiFID (0..1) <"The qualification of the type of organisation as specified by the MiFID regulatory regime, e.g. Systematic Internaliser. This corresponds to the FpML organizationTypeScheme in FpML, although specified by regulatory regime.">;
   		[synonym FpML value organizationType]
   	isEligibleToCFTC_DFA boolean (0..1) <"Outcome of the MiFID II entity eligibility assessment. This attribute is optional, as it may not be specified as part of some of the Rosetta use cases.">;
   }
