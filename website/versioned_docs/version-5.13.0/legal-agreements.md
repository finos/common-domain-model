---
title: Legal Agreements
---

# The Use of *Agreements* in Financial Markets

Financial transactions consist primarily of agreements between parties
to make future payments or deliveries to each other. To ensure
performance, those agreements typically take the form of legally
enforceable contracts, which the parties record in writing to minimize
potential future disagreements.

It is common practice in some markets for different aspects of these
agreements to be recorded in different documents, most commonly dividing
those terms that exist at the trading relationship level (e.g. credit
risk monitoring and collateral) from those at the transaction level (the
economic and risk terms of individual transactions). Relationship
agreements and individual transaction level documents are often called
"master agreements" and "confirmations" respectively, and multiple
confirmations may be linked to a single master agreement.

Both the relationship and transaction level documents may be further
divided into those parts that are standard for the relevant market,
which may exist in a pre-defined base form published by a trade
association or similar body, and those that are more bespoke and agreed
by the specific parties. The standard published forms may anticipate
that the parties will choose from pre-defined elections in a published
form, or create their own bespoke amendments.

The ISDA Master Agreement is an internationally recognised document
which is used to provide certain legal and credit protection for parties
who enter into OTC derivatives. Parties that execute agreements for OTC
derivatives are expected to have bi-lateral Master Agreements with each
other that cover an agreed range of transactions. Accordingly in the CDM
each transaction can be associated with a single master agreement, and a
single master agreement can be associated with multiple transactions.

In addition to the Master Agreement are sets of credit support
documentation which parties may enter into as part of Master Agreement
to contain the terms on which they will exchange collateral for their
OTC derivatives. Collateral provides protection to a party against the
risk that its counterparty defaults and fails to pay the amount that it
owes on default. The risk of loss in this scenario is for the current
cost of replacing the defaulted transactions (for which margin is called
"variation margin") and the risk of further loss before the default can
be closed out (called "initial margin" or "independent amount").

There are several different types of ISDA credit support document,
reflecting variation and initial margin, regulatory requirements and
terms for legal relationships under different legal jurisdictions. The
key components of the suite of credit support documents are summarized
below:

-   **Credit Support Annexes (CSAs)** exist in New York, English, Irish,
    French, and Japanese law forms. They define the terms for the
    provision of collateral by the parties in derivatives transactions,
    and in some cases they are specialized for initial margin or
    variation margin.
-   **Credit Support Deed CSD (CSD)** is very similar to a CSA, except
    that it is used to create specific types of legal rights over the
    collateral under English and Irish law, which requires a specific
    type of legal agreement (a deed).
-   **The Collateral Transfer Agreement and Security Agreement (CTA and
    SA)** together define a collateral arrangement where initial margin
    is posted to a custodian account for use in complying with initial
    margin requirements. The CTA/SA offers additional flexibility by
    allowing parties to apply one governing law to the mechanical
    aspects of the collateral relationship (the CTA) and a different
    governing law to the grant and enforcement of security over the
    custodian account (the SA).

In the CDM and in this user documentation, *legal agreement* refers to
the written terms of a relationship-level agreement, and *contract*
refers to the written terms defining an executed financial transaction.

# Legal Agreements in the CDM

The CDM provides a digital representation of the legal agreements that
govern transactions and workflows. The benefits of this digital
representation are summarized below:

-   **Supporting marketplace initiatives to streamline and standardise
    legal agreements** with a comprehensive digital representation of
    such agreements.
-   **Providing a comprehensive representation of the financial
    workflows** by complementing the trade and lifecycle event model and
    formally tying legal data to the business outcome and performance of
    legal clauses. (e.g. in collateral management where lifecycle
    processes require reference to parameters found in the associated
    legal agreements, such as the Credit Support Annex).
-   **Supporting the direct implementation of functional processes** by
    providing a normalised representation of legal agreements as
    structured data, as opposed to the unstructured data contained of a
    full legal text that needs to be interpreted first before any
    implementation (e.g. for a calculation of an amount specified in a
    legal definition).

The scope of the CDM legal agreement model includes all of the types of
ISDA credit support documents. The legal agreement model is explained
below, including examples and references to these types of documents.

The topics covered in this section are listed below:

-   Modelling Approach
-   Legal Agreement Data Structure
-   Linking Legal Agreements to contracts

# Modelling Approach

## Scope

The legal agreement model in the CDM comprises the following features:

-   **Composable and normalised model representation** of the ISDA
    agreements. The terms of an ISDA agreement can be defined by
    identification of the published base document, and the elections or
    amendments made to that base in a specific legal agreement. There
    are distinct versions of the published agreements for jurisdiction
    and year of publication, but the set of elections and amendments to
    those base agreements often belong to a common universe. Therefore,
    the CDM defines each of these terms in a single location, and allows
    for the representation of a specific legal agreement by combining
    terms where appropriate. The following legal agreements are
    supported in the CDM:

    **Initial Margin Agreements**

    -   ISDA 2016 Phase One Credit Support Annex ("CSA") (Security
        Interest -- New York Law)
    -   ISDA 2016 Phase One Credit Support Deed ("CSD") (Security
        Interest -- English Law)
    -   ISDA 2016 Phase One CSA (Loan -- Japanese Law)
    -   ISDA 2016 ISDA-Clearstream Collateral Transfer Agreement ("CTA")
        (New York law and Multi Regime English Law) and Security
        Agreement
    -   ISDA 2016 ISDA-Euroclear CTA (New York law and Multi Regime
        English Law) and Security Agreement
    -   ISDA 2018 CSA (Security Interest -- New York Law)
    -   ISDA 2018 CSD (Security Interest -- English Law)
    -   ISDA 2019 Bank Custodian CTA and Security Agreement (English
        Law, New York Law)
    -   ISDA 2019 ISDA-Clearstream CTA and Security Agreement
        (Luxembourg Law -- Security-provider or Security-taker name)
    -   ISDA 2019 ISDA-Euroclear CTA and Security Agreement

    **Variation Margin Agreements**

    -   ISDA 2016 CSA for Variation Margin ("VM") (Security Interest -
        New York Law)
    -   ISDA 2016 CSA for VM (Title Transfer -- English Law)
    -   ISDA 2016 CSA for VM (Loan -- Japanese Law)
    -   ISDA 2016 CSA for VM (Title Transfer -- Irish Law)
    -   ISDA 2016 CSA for VM (Title Transfer -- French Law)

    **Master Agreement Schedule**

    -   ISDA 2002 Master Agreement Schedule (Partial agreement
        representation)

-   **Composable and normalised model representation** of the eligible
    collateral schedule for initial and variation margin into a directly
    machine readable format.

-   **Linking of legal agreement into a trade object** through the CDM
    referencing mechanism.

-   **Mapping to ISDA Create derivative documentation negotiation
    platform** : Synonyms identified as belonging to `ISDA_Create_1_0`
    have been defined to establish mappings that support automated
    transformation of ISDA Create documents into objects that are
    compliant with the CDM.

    -   The mapping between the two models through the use of Synonyms
        validated that all the necessary permutations of elections and
        data associated with the supported agreements have been
        replicated in the CDM
    -   Ingestion of JSON sample files generated from ISDA Create for
        samples of executed documents has been implemented in the CDM
        Portal to demonstrate this capability between ISDA Create and
        the CDM.
    -   More details on Synonyms are provided in the Mapping (Synonym)
        section of this document.

---
**Note:**
The CDM supports the ISDA CSA for Variation Margin, but this document is
not yet represented in ISDA Create - the CDM representation of this
document is tested with alternative external sample data.

---

## Design Principles

The key modelling principles that have been adopted to represent legal
agreements are described below:

-   **Distinction between the agreement identification features and the
    agreement content features**
    -   The agreement identification features: agreement name, publisher
        (of the base agreement being used), identification, etc. are
        represented by the `LegalAgreementBase` type.
    -   The agreement content features: elections and amendments to the
        published agreement, related agreements and umbrella agreement
        terms are represented by the `AgreementTerms`.
-   **Composite and extendable model**.
    -   The Legal Agreement model follows the CDM design principles of
        composability and reusability to develop an extendable model
        that can support multiple document types.
    -   For instance, the `LegalAgreementBase` data type uses components
        that are also used as part of the CDM trade and lifecycle event
        components: e.g. `Party`, `Identifier`, `date`.
-   **Normalisation of the data representation**
    -   Strong data type attributes such as numbers, Boolean, or
        enumerations are used where possible to create a series of
        normalised elections within terms used in ISDA documentation and
        create a data representation of the legal agreement that is
        machine readable and executable. This approach allows CDM users
        to define normalised elections into a corresponding legal
        agreement template to support functional processes.
    -   In practice the use of elections expressed in a `string` format
        has been restricted, as the `string` format is generally
        unsuitable for the support of standardised functional processes.

The components of the legal agreement model specified in the CDM are
detailed in the section below.

# Legal Agreement Data Structure

The `LegalAgreement` data type represents the highest-level data type
for defining a legal agreement in the CDM. This data type extends the
`LegalAgreementBase`, which contains information to uniquely identify an
agreement. There are three non-inherited components to `LegalAgreement`,
as shown in the code snippet below:.

``` Haskell
type LegalAgreement extends LegalAgreementBase:
  [metadata key]
   [rootType]
  agreementTerms AgreementTerms (0..1)
  relatedAgreements LegalAgreement (0..*)
  umbrellaAgreement UmbrellaAgreement (0..1)
```

The `LegalAgreementBase`, `UmbrellaAgreement`, and `AgreementTerms` are
defined in the following sections.

## Agreement Identification

The CDM provides support for implementors to uniquely identify a legal
agreement solely through the specification of the agreement
identification features, as represented in the `LegalAgreementBase`
abstract data type, which is illustrated below:

``` Haskell
type LegalAgreementBase:
  agreementDate date (0..1)
  effectiveDate date (0..1)
  identifier Identifier (0..*)
  legalAgreementIdentification LegalAgreementIdentification (1..1)
  contractualParty Party (2..2)
   [metadata reference]
  otherParty PartyRole (0..*)
```

As indicated by the cardinality for the attributes in this data type,
all legal agreements must contain an agreement date, two contractual
parties, and information indicating the published form of market
standard agreement being used (including the name and publisher of the
legal agreement being specified in the `agreementIdentification`
attribute). Provision is made for further information to be captured,
for example an agreement identifier, which is an optional attribute.

## Related Agreement

Related agreements attribute is used to specify any higher-level
agreement(s) that may govern the agreement, either as a reference to
such agreements when specified as part of the CDM, or through
identification of some of the key terms of those agreements.

---
**Note:**
The `LegalAgreementType` attribute is used to map related agreement
terms that are embedded as part of a transaction message converted from
another model structure, such as FpML. For example, this attribute may
reference an ISDA Master Agreement, which is not modelled or mapped in
the CDM `LegalAgreement` data type.

---

## Umbrella Agreement

`UmbrellaAgreement` is a data type used to specify the applicability of
Umbrella Agreement terms, relevant specific language, and underlying
entities associated with the umbrella agreement.

The below snippet represents the `UmbrellaAgreement` data type.

``` Haskell
type UmbrellaAgreement:
  isApplicable boolean (1..1)
  language string (0..1)
  parties UmbrellaAgreementEntity (0..*)
```

## Agreement Content

`AgreementTerms` is used to specify the content of a legal agreement in
the CDM. There are two components to agreement terms, as shown in the
code snippet below:

``` Haskell
type AgreementTerms:
  agreement Agreement (1..1)
  clauseLibrary boolean (0..1)
  counterparty Counterparty (2..2)
```

The following sections describe each of these components.

## Agreement

`Agreement` is a data type used to specify the individual elections
contained within the legal agreement. It contains a set of encapsulated
data types, each containing the elections used to define a specific
group of agreements.

``` Haskell
type Agreement:
  creditSupportAgreementElections CreditSupportAgreementElections (0..1)
  collateralTransferAgreementElections CollateralTransferAgreementElections (0..1)
  securityAgreementElections SecurityAgreementElections (0..1)
  masterAgreementSchedule MasterAgreementSchedule (0..1)
  transactionAdditionalTerms TransactionAdditionalTerms (0..1)
  condition: one-of
```

## Counterparty

Each counterparty to the agreement is assigned an enumerated value of
either `Party1` or `Party2` through the association of a
`CounterpartyRoleEnum` with the corresponding `Party`. The
`CounterpartyRoleEnum` value is then used to specify elections
throughout the rest of the document.

``` Haskell
enum CounterpartyRoleEnum:
  Party1
  Party2
```

``` Haskell
type Counterparty:
  role CounterpartyRoleEnum (1..1)
  partyReference Party (1..1)
   [metadata reference]
```

The modelling approach for elective provisions is explained in further
detail in the corresponding section below.

# Elective Provisions

This section describes the modelling approach and data structure for
election provisions, which are the detailed terms of agreement in each
legal document. The section concludes with relevant examples to
illustrate the approach and structure.

## Modelling Approach

In many cases the pre-printed clauses in legal agreement templates for
OTC Derivatives offer pre-defined elections that the parties can select.
In these cases, the clauses are explicitly identified in the agreement
templates, including the potential values for each election (e.g. an
election from a list of options or a specific type of information such
as an amount, date or city). The design of the elective provisions in
the CDM to represent these instances is a direct reflection of the
choices in the clause and uses boolean attributes or enumeration lists
to achieve the necessary outcome.

However, in some cases, the agreement template may identify a clause but
not all the applicable values, e.g. when a single version of a clause
term is provided with a space for parties to agree on a term that is not
defined in the template. In order to support these instances, the CDM
uses string attributes to capture the clause in a free text format.

## Election Structure

For ease of reference, the structure of the elections contained within
each agreement data type in the CDM are modelled to reflect the
structure of the legal agreements that they represent. Each data type
contains a set of elections or election families which can be used to
represent the clauses contained within the corresponding legal
agreement, regardless of vintage or governing law.

This approach allows the representation of elections in the CDM to focus
on their intended business outcome in order to better support the
standardisation of related business processes.

For example, `CreditSupportAgreementElections` , which is one of the
four agreement types, contains all the elections that may be applicable
to a credit support agreement and can be used to define any of the
Initial Margin or Variation Margin Credit Support Agreements supported
by the CDM:

-   ISDA 2016 Phase One Credit Support Annex ("CSA") for Initial Margin
    ("IM") (Security Interest -- New York Law)
-   ISDA 2016 Phase One Credit Support Deed ("CSD") for IM (Security
    Interest -- English Law)
-   ISDA 2016 Phase One CSA for IM (Loan -- Japanese Law)
-   ISDA 2018 CSA for IM (Security Interest -- New York Law)
-   ISDA 2018 CSD for IM (Security Interest -- English Law)
-   ISDA 2016 CSA for Variation Margin ("VM") (Security Interest - New
    York Law)
-   ISDA 2016 CSA for VM (Title Transfer -- English Law)
-   ISDA 2016 CSA for VM (Loan -- Japanese Law)
-   ISDA 2016 CSA for VM (Title Transfer -- Irish Law)
-   ISDA 2016 CSA for VM (Title Transfer -- French Law)

``` Haskell
condition AgreementVerification:
  if agreementTerms -> agreement -> securityAgreementElections exists
  then legalAgreementIdentification -> agreementName -> agreementType = LegalAgreementTypeEnum->SecurityAgreement
```

The validation in this case requires that if the
`securityAgreementElections` attribute is populated, then the value in
`LegalAgreementNameEnum` must be `SecurityAgreement` .

Selected examples from two of the agreement data types are explained in
the following sections to illustrate the overall approach.

The development of a digital data standard for representation of
eligible collateral schedules is a crucial component required to drive
digital negotiation, straight through processing, and digitisation of
collateral management. The standard representation provided within the
CDM allows institutions involved in the collateral workflow cycle to
exchange eligible collateral information accurately and efficiently in
digital form. The `EligibleCollateral` data type is a root type with one
attribute, as shown below:

``` Haskell
type EligibleCollateralSpecification:
   [rootType]
   [metadata key]
   identifier Identifier (0..*)
   party Party (0..2)
   counterparty Counterparty (0..2)
   criteria EligibleCollateralCriteria (1..*)
```

The `EligibleCollateralCriteria` data type contains the following key
components to allow the digital representation of the detailed criteria
reflected in the legal agreement:

1.  **Collateral Issuer Criteria** specifies criteria that the issuer of
    an asset (if any) must meet when defining collateral eligibility for
    that asset.
2.  **Collateral Product Criteria** specifies criteria that the product
    must meet when defining collateral eligibility.
3.  **Collateral Treatment** specifies criteria for the treatment of
    collateral assets, including whether the asset is identified as
    eligible or ineligible, and treatment when posted.

The following code snippets represent these three components of the
eligible collateral model. These components are assembled under the
`EligibleCollateralCriteria` data type, which is contained within the
`postingObligationElection` component of the credit support agreement
elections described above.

``` Haskell
type EligibleCollateralCriteria extends CollateralCriteriaBase:
   treatment CollateralTreatment (1..1)
```

``` Haskell
type CollateralCriteriaBase:
   issuer IssuerCriteria (0..*)
   asset AssetCriteria (0..*)
   appliesTo CounterpartyRoleEnum (0..2)
```

``` Haskell
type IssuerCriteria:
  issuerType CollateralIssuerType (0..*)
  issuerCountryOfOrigin ISOCountryCodeEnum (0..*)
  issuerName LegalEntity (0..*)
  issuerAgencyRating AgencyRatingCriteria (0..*)
  sovereignAgencyRating AgencyRatingCriteria (0..*)
  counterpartyOwnIssuePermitted boolean (0..1)
```

``` Haskell
type AssetCriteria:
  collateralAssetType AssetType (0..*)
  assetCountryOfOrigin ISOCountryCodeEnum (0..*)
  denominatedCurrency CurrencyCodeEnum (0..*)
  agencyRating AgencyRatingCriteria (0..*)
  maturityType MaturityTypeEnum (0..1)
  maturityRange PeriodRange (0..1)
  productIdentifier ProductIdentifier (0..*)
  collateralTaxonomy CollateralTaxonomy (0..*)
  domesticCurrencyIssued boolean (0..1)
  listing ListingType (0..1)
```

``` Haskell
type CollateralTreatment:
  valuationTreatment CollateralValuationTreatment (0..1)
  concentrationLimit ConcentrationLimit (0..*)
  isIncluded boolean (1..1)
```
