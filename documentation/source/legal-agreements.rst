.. include:: links.rst

.. _legal-agreements-page:

Legal Agreements
----------------

The Use of *Agreements* in Financial Markets
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Financial transactions consist primarily of agreements between parties to make future payments or deliveries to each other. To ensure performance, those agreements typically take the form of legally enforceable contracts, which the parties record in writing to minimize potential future disagreements.

It is common practice in some markets for different aspects of these agreements to be recorded in different documents, most commonly dividing those terms that exist at the trading relationship level (e.g. credit risk monitoring and collateral) from those at the transaction level (the economic and risk terms of individual transactions). Relationship agreements and individual transaction level documents are often called “master agreements” and “confirmations” respectively, and multiple confirmations may be linked to a single master agreement.

Both the relationship and transaction level documents may be further divided into those parts that are standard for the relevant market, which may exist in a pre-defined base form published by a trade association or similar body, and those that are more bespoke and agreed by the specific parties. The standard published forms may anticipate that the parties will choose from pre-defined elections in a published form, or create their own bespoke amendments.

The ISDA Master Agreement is an internationally recognised document which is used to provide certain legal and credit protection for parties who enter into OTC derivatives. Parties that execute agreements for OTC derivatives are expected to have bi-lateral Master Agreements with each other that cover an agreed range of transactions. Accordingly in the CDM each transaction can be associated with a single master agreement, and a single master agreement can be associated with multiple transactions.

In addition to the Master Agreement are sets of credit support documentation which parties may enter into as part of Master Agreement to contain the terms on which they will exchange collateral for their OTC derivatives. Collateral provides protection to a party against the risk that its counterparty defaults and fails to pay the amount that it owes on default. The risk of loss in this scenario is for the current cost of replacing the defaulted transactions (for which margin is called “variation margin”) and the risk of further loss before the default can be closed out (called “initial margin” or “independent amount”).

There are several different types of ISDA credit support document, reflecting variation and initial margin, regulatory requirements and terms for legal relationships under different legal jurisdictions. The key components of the suite of credit support documents are summarized below:

* **Credit Support Annexes (CSAs)** exist in New York, English, Irish, French, and Japanese law forms.  They define the terms for the provision of collateral by the parties in derivatives transactions, and in some cases they are specialized for initial margin or variation margin.
* **Credit Support Deed CSD (CSD)** is very similar to a CSA, except that it is used to create specific types of legal rights over the collateral under English and Irish law, which requires a specific type of legal agreement (a deed).
* **The Collateral Transfer Agreement and Security Agreement (CTA and SA)** together define a collateral arrangement where initial margin is posted to a custodian account for use in complying with initial margin requirements. The CTA/SA offers additional flexibility by allowing parties to apply one governing law to the mechanical aspects of the collateral relationship (the CTA) and a different governing law to the grant and enforcement of security over the custodian account (the SA).

In the CDM and in this user documentation, *legal agreement* refers to the written terms of a relationship-level agreement, and *contract* refers to the written terms defining an executed financial transaction.

Legal Agreements in the CDM
^^^^^^^^^^^^^^^^^^^^^^^^^^^

The CDM provides a digital representation of the legal agreements that govern transactions and workflows. The benefits of this digital representation are summarized below:

* **Supporting marketplace initiatives to streamline and standardise legal agreements** with a comprehensive digital representation of such agreements.
* **Providing a comprehensive representation of the financial workflows** by complementing the trade and lifecycle event model and formally tying legal data to the business outcome and performance of legal clauses. (e.g. in collateral management where lifecycle processes require reference to parameters found in the associated legal agreements, such as the Credit Support Annex).
* **Supporting the direct implementation of functional processes** by providing a normalised representation of legal agreements as structured data, as opposed to the unstructured data contained of a full legal text that needs to be interpreted first before any implementation (e.g. for a calculation of an amount specified in a legal definition).

The scope of the CDM legal agreement model includes all of the types of ISDA credit support documents. The legal agreement model is explained below, including examples and references to these types of documents.

The topics covered in this section are listed below:

* Modelling Approach
* Legal Agreement Data Structure
* Linking Legal Agreements to contracts


Modelling Approach
^^^^^^^^^^^^^^^^^^

Scope
"""""

The legal agreement model in the CDM comprises the following features:

* **Composable and normalised model representation** of the ISDA agreements. The terms of an ISDA agreement can be defined by identification of the published base document, and the elections or amendments made to that base in a specific legal agreement. There are distinct versions of the published agreements for jurisdiction and year of publication, but the set of elections and amendments to those base agreements often belong to a common universe. Therefore, the CDM defines each of these terms in a single location, and allows for the representation of a specific legal agreement by combining terms where appropriate. The following legal agreements are supported in the CDM:

  **Initial Margin Agreements**

  * ISDA 2016 Phase One Credit Support Annex (“CSA”) (Security Interest – New York Law)
  * ISDA 2016 Phase One Credit Support Deed (“CSD”) (Security Interest – English Law)
  * ISDA 2016 Phase One CSA (Loan – Japanese Law)
  * ISDA 2016 ISDA-Clearstream Collateral Transfer Agreement (“CTA”) (New York law and Multi Regime English Law) and Security Agreement
  * ISDA 2016 ISDA-Euroclear CTA (New York law and Multi Regime English Law) and Security Agreement
  * ISDA 2018 CSA (Security Interest – New York Law)
  * ISDA 2018 CSD (Security Interest – English Law)
  * ISDA 2019 Bank Custodian CTA and Security Agreement (English Law, New York Law)
  * ISDA 2019 ISDA-Clearstream CTA and Security Agreement (Luxembourg Law – Security-provider or Security-taker name)
  * ISDA 2019 ISDA-Euroclear CTA and Security Agreement


  **Variation Margin Agreements**

  * ISDA 2016 CSA for Variation Margin ("VM") (Security Interest - New York Law)
  * ISDA 2016 CSA for VM (Title Transfer – English Law)
  * ISDA 2016 CSA for VM (Loan – Japanese Law)
  * ISDA 2016 CSA for VM (Title Transfer – Irish Law)
  * ISDA 2016 CSA for VM (Title Transfer – French Law)


  **Master Agreement Schedule**

  * ISDA 2002 Master Agreement Schedule (Partial agreement representation)


* **Composable and normalised model representation** of the eligible collateral schedule for initial and variation margin into a directly machine readable format.

* **Linking of legal agreement into a trade object** through the CDM referencing mechanism.

* **Mapping to ISDA Create derivative documentation negotiation platform** : Synonyms identified as belonging to ``ISDA_Create_1_0`` have been defined to establish mappings that support automated transformation of ISDA Create documents into objects that are compliant with the CDM.

  * The mapping between the two models through the use of Synonyms validated that all the necessary permutations of elections and data associated with the supported agreements have been replicated in the CDM
  * Ingestion of JSON sample files generated from ISDA Create for samples of executed documents has been implemented in the CDM Portal to demonstrate this capability between ISDA Create and the CDM.
  * More details on Synonyms are provided in the Mapping (Synonym) section of this document.

.. note:: The CDM supports the ISDA CSA for Variation Margin, but this document is not yet represented in ISDA Create - the CDM representation of this document is tested with alternative external sample data.


Design Principles
"""""""""""""""""

The key modelling principles that have been adopted to represent legal agreements are described below:

* **Distinction between the agreement identification features and the agreement content features**

  * The agreement identification features: agreement name, publisher (of the base agreement being used), identification, etc. are represented by the ``LegalAgreementBase`` type.
  * The agreement content features: elections and amendments to the published agreement, related agreements and umbrella agreement terms are represented by the ``AgreementTerms``.

* **Composite and extendable model**.

  * The Legal Agreement model follows the CDM design principles of composability and reusability to develop an extendable model that can support multiple document types.
  * For instance, the ``LegalAgreementBase`` data type uses components that are also used as part of the CDM trade and lifecycle event components: e.g. ``Party``, ``Identifier``, ``date``.

* **Normalisation of the data representation**

  * Strong data type attributes such as numbers, Boolean, or enumerations are used where possible to create a series of normalised elections within terms used in ISDA documentation and create a data representation of the legal agreement that is machine readable and executable. This approach allows CDM users to define normalised elections into a corresponding legal agreement template to support functional processes.
  * In practice the use of elections expressed in a ``string`` format has been restricted, as the ``string`` format is generally unsuitable for the support of standardised functional processes.

The components of the legal agreement model specified in the CDM are detailed in the section below.

Legal Agreement Data Structure
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
The ``LegalAgreement`` data type represents the highest-level data type for defining a legal agreement in the CDM.  This data type extends the ``LegalAgreementBase``, which contains information to uniquely identify an agreement. There are three non-inherited components to ``LegalAgreement``, as shown in the code snippet below:.

.. code-block:: Haskell

  type LegalAgreement extends LegalAgreementBase:
    [metadata key]
     [rootType]
    agreementTerms AgreementTerms (0..1)
    relatedAgreements LegalAgreement (0..*)
    umbrellaAgreement UmbrellaAgreement (0..1)

The ``LegalAgreementBase``, ``UmbrellaAgreement``, and ``AgreementTerms`` are defined in the following sections.

Agreement Identification
""""""""""""""""""""""""
The CDM provides support for implementors to uniquely identify a legal agreement solely through the specification of the agreement identification features, as represented in the ``LegalAgreementBase`` abstract data type, which is illustrated below:

.. code-block:: Haskell

 type LegalAgreementBase:
   agreementDate date (0..1)
   effectiveDate date (0..1)
   identifier Identifier (0..*)
   legalAgreementIdentification LegalAgreementIdentification (1..1)
   contractualParty Party (2..2)
    [metadata reference]
   otherParty PartyRole (0..*)

As indicated by the cardinality for the attributes in this data type, all legal agreements must contain an agreement date, two contractual parties, and information indicating the published form of market standard agreement being used (including the name and publisher of the legal agreement being specified in the ``agreementIdentification`` attribute).  Provision is made for further information to be captured, for example an agreement identifier, which is an optional attribute.

Related Agreement
"""""""""""""""""

Related agreements attribute is used to specify any higher-level agreement(s) that may govern the agreement, either as a reference to such agreements when specified as part of the CDM, or through identification of some of the key terms of those agreements.

.. note:: The ``LegalAgreementType`` attribute is used to map related agreement terms that are embedded as part of a transaction message converted from another model structure, such as FpML.  For example, this attribute may reference an ISDA Master Agreement, which is not modelled or mapped in the CDM ``LegalAgreement`` data type.

Umbrella Agreement
""""""""""""""""""

``UmbrellaAgreement`` is a data type used to specify the applicability of Umbrella Agreement terms, relevant specific language, and underlying entities associated with the umbrella agreement.

The below snippet represents the ``UmbrellaAgreement`` data type.

.. code-block:: Haskell

 type UmbrellaAgreement:
   isApplicable boolean (1..1)
   language string (0..1)
   parties UmbrellaAgreementEntity (0..*)

Agreement Content
"""""""""""""""""

``AgreementTerms`` is used to specify the content of a legal agreement in the CDM. There are two components to agreement terms, as shown in the code snippet below:

.. code-block:: Haskell

 type AgreementTerms:
   agreement Agreement (1..1)
   clauseLibrary boolean (0..1)
   counterparty Counterparty (2..2)

The following sections describe each of these components.

Agreement
"""""""""

``Agreement`` is a data type used to specify the individual elections contained within the legal agreement. It contains a set of encapsulated data types, each containing the elections used to define a specific group of agreements.

.. code-block:: Haskell

 type Agreement:
   creditSupportAgreementElections CreditSupportAgreementElections (0..1)
   collateralTransferAgreementElections CollateralTransferAgreementElections (0..1)
   securityAgreementElections SecurityAgreementElections (0..1)
   masterAgreementSchedule MasterAgreementSchedule (0..1)
   condition: one-of

Counterparty
""""""""""""

Each counterparty to the agreement is assigned an enumerated value of either ``Party1`` or ``Party2`` through the association of a ``CounterpartyRoleEnum`` with the corresponding ``Party``.  The ``CounterpartyRoleEnum`` value is then used to specify elections throughout the rest of the document.

.. code-block:: Haskell

 enum CounterpartyRoleEnum:
   Party1
   Party2

.. code-block:: Haskell

 type Counterparty:
   role CounterpartyRoleEnum (1..1)
   partyReference Party (1..1)
    [metadata reference]

The modelling approach for elective provisions is explained in further detail in the corresponding section below.

Elective Provisions
^^^^^^^^^^^^^^^^^^^

This section describes the modelling approach and data structure for election provisions, which are the detailed terms of agreement in each legal document.  The section concludes with relevant examples to illustrate the approach and structure.

Modelling Approach
""""""""""""""""""

In many cases the pre-printed clauses in legal agreement templates for OTC Derivatives offer pre-defined elections that the parties can select. In these cases, the clauses are explicitly identified in the agreement templates, including the potential values for each election (e.g. an election from a list of options or a specific type of information such as an amount, date or city). The design of the elective provisions in the CDM to represent these instances is a direct reflection of the choices in the clause and uses boolean attributes or enumeration lists to achieve the necessary outcome.

However, in some cases, the agreement template may identify a clause but not all the applicable values, e.g. when a single version of a clause term is provided with a space for parties to agree on a term that is not defined in the template. In order to support these instances, the CDM uses string attributes to capture the clause in a free text format.

Election Structure
""""""""""""""""""

For ease of reference, the structure of the elections contained within each agreement data type in the CDM are modelled to reflect the structure of the legal agreements that they represent. Each data type contains a set of elections or election families which can be used to represent the clauses contained within the corresponding legal agreement, regardless of vintage or governing law.

This approach allows the representation of elections in the CDM to focus on their intended business outcome in order to better support the standardisation of related business processes.

For example, ``CreditSupportAgreementElections`` , which is one of the four agreement types, contains all the elections that may be applicable to a credit support agreement and can be used to define any of the Initial Margin or Variation Margin Credit Support Agreements supported by the CDM:

* ISDA 2016 Phase One Credit Support Annex (“CSA”) for Initial Margin ("IM") (Security Interest – New York Law)
* ISDA 2016 Phase One Credit Support Deed (“CSD”) for IM (Security Interest – English Law)
* ISDA 2016 Phase One CSA for IM (Loan – Japanese Law)
* ISDA 2018 CSA for IM (Security Interest – New York Law)
* ISDA 2018 CSD for IM (Security Interest – English Law)
* ISDA 2016 CSA for Variation Margin ("VM") (Security Interest - New York Law)
* ISDA 2016 CSA for VM (Title Transfer – English Law)
* ISDA 2016 CSA for VM (Loan – Japanese Law)
* ISDA 2016 CSA for VM (Title Transfer – Irish Law)
* ISDA 2016 CSA for VM (Title Transfer – French Law)

The ``CreditSupportAgreementElections`` data type therefore contains a super-set of the elections that may apply to any of the above document types.  Common elections used in different document types are represented using common components in this data type.

.. code-block:: Haskell

 type CreditSupportAgreementElections:
   regime Regime (1..1)
   oneWayProvisions OneWayProvisions (1..1)
   generalSimmElections GeneralSimmElections (0..1)
   identifiedCrossCurrencySwap boolean (0..1)
   sensitivityMethodologies SensitivityMethodologies (1..1)
   fxHaircutCurrency FxHaircutCurrency (0..1)
   postingObligations PostingObligations (1..1)
   substitutedRegime SubstitutedRegime (0..*)
   baseAndEligibleCurrency BaseAndEligibleCurrency (1..1)
   additionalObligations string (0..1)
   coveredTransactions CoveredTransactions (1..1)
   creditSupportObligations CreditSupportObligations (1..1)
   exchangeDate string (0..1)
   calculationAndTiming CalculationAndTiming (1..1)
   conditionsPrecedent ConditionsPrecedent (0..1)
   substitution Substitution (1..1)
   disputeResolution DisputeResolution (1..1)
   holdingAndUsingPostedCollateral HoldingAndUsingPostedCollateral (1..1)
   rightsEvents RightsEvents (1..1)
   custodyArrangements CustodyArrangements (0..1)
   distributionAndInterestPayment DistributionAndInterestPayment (0..1)
   creditSupportOffsets boolean (1..1)
   additionalRepresentations AdditionalRepresentations (1..1)
   otherEligibleAndPostedSupport OtherEligibleAndPostedSupport (1..1)
   demandsAndNotices ContactElection (0..1)
   addressesForTransfer ContactElection (0..1)
   otherAgreements OtherAgreements (0..1)
   terminationCurrencyAmendment TerminationCurrencyAmendment (1..1)
   minimumTransferAmountAmendment MinimumTransferAmountAmendment (1..1)
   interpretationTerms string (0..1)
   processAgent ProcessAgent (0..1)
   appropriatedCollateralValuation AppropriatedCollateralValuation (0..1)
   jurisdictionRelatedTerms JurisdictionRelatedTerms (0..1)
   additionalAmendments string (0..1)
   additionalBespokeTerms string (0..1)
   trustSchemeAddendum boolean (1..1)

.. note:: Validation exists in the model to ensure that the set of elections specified within the ``Agreement`` are consistent with the agreement identified as part of ``LegalAgreementBase``.  The below snippet represents a sample of a validation condition:

.. code-block:: Haskell

 condition AgreementVerification:
   if agreementTerms -> agreement -> securityAgreementElections exists
   then legalAgreementIdentification -> agreementName -> agreementType = LegalAgreementTypeEnum->SecurityAgreement

The validation in this case requires that if the ``securityAgreementElections`` attribute is populated, then the value in ``LegalAgreementNameEnum`` must be ``SecurityAgreement`` .

Selected examples from two of the agreement data types are explained in the following sections to illustrate the overall approach.

Elective Provisions Example 1: Posting Obligations
"""""""""""""""""""""""""""""""""""""""""""""""""""
``postingObligations`` is one of the required attributes in ``CreditSupportAgreementElections`` .  It defines the security provider party to which a set of posting obligations applies and the applicable collateral posting obligations as indicated in the data structure shown below:

.. code-block:: Haskell

 type PostingObligations:
   securityProvider string (1..1)
   partyElection PostingObligationsElection (1..2)

The ``partyElection`` attribute, which is of the type partyElection ``PostingObligationsElection`` defines the party that the collateral posting obligations apply to and defines the collateral that is eligible, as shown below:

.. code-block:: Haskell

 type PostingObligationsElection:
   party CounterpartyRoleEnum (1..1)
   asPermitted boolean (1..1)
   eligibleCollateral EligibleCollateralSchedule (0..*)
   excludedCollateral string (0..1)
   additionalLanguage string (0..1)

.. note:: In order to provide compatibility with ISDA Create the ``party`` attribute in CDM is represented as a string.  Implementors should populate this field with ``PartyA`` , ``PartyB`` , or ``PartyAPartyB`` as appropriate to represent the party that the election terms are being defined for.

The development of a digital data standard for representation of eligible collateral schedules is a crucial component required to drive digital negotiation, straight through processing, and digitisation of collateral management. The standard representation provided within the CDM allows institutions involved in the collateral workflow cycle to exchange eligible collateral information accurately and efficiently in digital form.  The ``EligibleCollateral`` data type is a root type with one attribute, as shown below:

.. code-block:: Haskell

 type EligibleCollateralSchedule:
    [rootType]
    [metadata key]
    scheduleIdentifier Identifier (0..*)
    criteria EligibleCollateralCriteria (1..*)

The ``EligibleCollateralCriteria`` data type contains the following key components to allow the digital representation of the detailed criteria reflected in the legal agreement:

#. **Collateral Issuer Criteria** specifies criteria that the issuer of an asset (if any) must meet when defining collateral eligibility for that asset.
#. **Collateral Product Criteria** specifies criteria that the product must meet when defining collateral eligibility.
#. **Collateral Treatment** specifies criteria for the treatment of collateral assets, including whether the asset is identified as eligible or ineligible, and treatment when posted.

The following code snippets represent these three components of the eligible collateral model. These components are assembled under the ``EligibleCollateralCriteria`` data type, which is contained within the ``postingObligationElection`` component of the credit support agreement elections described above.

.. code-block:: Haskell

 type EligibleCollateralCriteria extends CollateralCriteriaBase:
    treatment CollateralTreatment (1..1)

.. code-block:: Haskell

 type CollateralCriteriaBase:
    issuer IssuerCriteria (0..*)
    asset AssetCriteria (0..*)

.. code-block:: Haskell

 type IssuerCriteria:
   issuerType CollateralIssuerType (0..*)
   issuerCountryOfOrigin string (0..*)
     [metadata scheme]
   issuerName LegalEntity (0..*)
   issuerAgencyRating AgencyRatingCriteria (0..*)
   sovereignAgencyRating AgencyRatingCriteria (0..*)
   counterpartyOwnIssuePermitted boolean (0..1)

.. code-block:: Haskell

 type AssetCriteria:
   collateralAssetType AssetType (0..*)
   assetCountryOfOrigin string (0..*)
     [metadata scheme]
   denominatedCurrency string (0..*)
     [metadata scheme]
   agencyRating AgencyRatingCriteria (0..*)
   maturityType MaturityTypeEnum (0..1)
   maturityRange PeriodRange (0..1)
   productIdentifier ProductIdentifier (0..*)
   collateralTaxonomy CollateralTaxonomy (0..*)
   domesticCurrencyIssued boolean (0..1)
   listing ListingType (0..1)

.. code-block:: Haskell

 type CollateralTreatment:
   valuationTreatment CollateralValuationTreatment (0..1)
   concentrationLimit ConcentrationLimit (0..*)
   isIncluded boolean (1..1)

Elective Provisions Example 2: Security Agreement Elections
"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

The ``SecurityAgreementElections`` data type is another one of the four agreement types.  Given the structure of this type, the CDM model supports nine distinct Security Agreements. Election structures across any of these agreements can be represented through the following data type:

.. code-block:: Haskell

 type SecurityAgreementElections:
   pledgedAccount Account (0..1)
   enforcementEvent EnforcementEvent (0..1)
   deliveryInLieuRight boolean (0..1)
   fullDischarge boolean (0..1)
   appropriatedCollateralValuation AppropriatedCollateralValuation (0..1)
   processAgent ProcessAgent (0..1)
   jurisdictionRelatedTerms JurisdictionRelatedTerms (0..1)
   additionalAmendments string (0..1)
   additionalBespokeTerms string (0..1)
   executionTerms ExecutionTerms (0..1)

Depending on the agreement being specified, a different combination of attributes would be used when specifying the agreement. The cardinality of each attribute allows the appropriate combination to be provided dependent on the agreement.

An equivalent approach is followed for ``CreditSupportAgreementElections`` and ``CollateralTransferAgreementElections``.

Elective Provisions Example 3: Credit Support Obligations
"""""""""""""""""""""""""""""""""""""""""""""""""""""""""

The ``creditSupportObligations`` attribute is contained within two of the agreement types: ``CreditSupportAgreementElections`` and ``CollateralTransferAgreementElections``.  In both cases, the data type is ``CreditSupportObligations``, which is used to represent a key set of terms that are fundamental to collateral calculations within these document families. The ``CollateralTransferAgreementElections`` data type is shown below, in which the ``creditSupportObligations`` is the tenth attribute:

.. code-block:: Haskell

 type CollateralTransferAgreementElections:
   regime Regime (1..1)
   oneWayProvisions OneWayProvisions (1..1)
   generalSimmElections GeneralSimmElections (1..1)
   identifiedCrossCurrencySwap boolean (0..1)
   sensitivityMethodologies SensitivityMethodologies (1..1)
   fxHaircutCurrency FxHaircutCurrency (0..1)
   postingObligations PostingObligations (1..1)
   substitutedRegime SubstitutedRegime (1..*)
   baseAndEligibleCurrency BaseAndEligibleCurrency (1..1)
   creditSupportObligations CreditSupportObligations (1..1)
   calculationAndTiming CalculationAndTiming (1..1)
   conditionsPrecedent ConditionsPrecedent (1..1)
   substitution Substitution (0..1)
   disputeResolution DisputeResolution (1..1)
   rightsEvents RightsEvents (0..1)
   custodyArrangements CustodyArrangements (1..1)
   additionalRepresentations AdditionalRepresentations (1..1)
   demandsAndNotices ContactElection (0..1)
   addressesForTransfer ContactElection (0..1)
   otherCsa string (0..1)
   terminationCurrencyAmendment TerminationCurrencyAmendment (1..1)
   minimumTransferAmountAmendment MinimumTransferAmountAmendment (0..1)
   interpretationTerms string (0..1)
   processAgent ProcessAgent (0..1)
   jurisdictionRelatedTerms JurisdictionRelatedTerms (0..1)
   additionalAmendments string (0..1)
   additionalBespokeTerms string (0..1)

This set of elections in ``CreditSupportObligations`` is modelled to directly reflect the equivalent paragraph in the ISDA documentation, for example Paragraph 13 (c) of the ISDA 2018 CSA (Security Interest – New York Law).  The cardinality constraint requires ``threshold`` and ``minimumTransferAmount`` to be specified, as it is an elective provision in all the Credit Support Agreements supported in CDM.  Other clauses such as ``marginApproach`` are not elective provisions in all supported agreements so the cardinality indicates optionality.

.. code-block:: Haskell

 type CreditSupportObligations:
   deliveryAmount string (0..1)
   returnAmount string (0..1)
   marginApproach MarginApproach (0..1)
   otherEligibleSupport string (0..1)
   threshold Threshold (1..1)
   minimumTransferAmount MinimumTransferAmount (1..1)
   rounding CollateralRounding (0..1)
   bespokeTransferTiming BespokeTransferTiming (0..1)
   creditSupportObligationsVariationMargin CreditSupportObligationsVariationMargin (0..1)

Each attribute is modelled based on the corresponding clause in the relevant legal agreement templates.  Therefore, each provides the necessary components to reflect the election structure. For example the attribute ``rounding`` is of data type ``CollateralRounding`` which allows the specification of rounding terms for the Delivery Amount and the Return Amount, as shown below:

.. code-block:: Haskell

 type CollateralRounding:
   deliveryAmount number (1..1)
   returnAmount number (1..1)

.. note:: The credit support obligations election data type, `CreditSupportObligationsInitialMargin`, is suffixed with ``InitialMargin``, because the initial set of credit support agreement documents that have been digitised in the CDM are Initial Margin CSAs.

Linking Legal Agreements to Contracts
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
Financial transactions defined in CDM can be referenced in the ``ContractTradeDetails`` data type.  This represents the transaction confirmation that is the legally binding agreement between two parties for an execution of a specified tradable product.  The ``documentation`` attribute uses the ``RelatedAgreement`` data type, which can be populated with the details for a relevant agreement that has been defined in the CDM.  For OTC derivatives, this attribute will contain a reference to the ISDA Master Agreement that governs any derivative transaction between the parties.

Similarly, the ``ContractFormation`` business event that creates the legally binding agreement between the parties can reference a ``LegalAgreement`` governing the transaction.

.. note:: The functions to create such business events are further detailed in the :ref:`lifecycle-event-process` of the documentation.
