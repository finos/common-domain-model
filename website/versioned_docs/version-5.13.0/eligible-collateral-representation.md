---
title: Eligible Collateral Representation
---

# Introduction

Within collateral documentation, it is common to detail what assets you
will exchange with your counterparties, i.e., what you deem eligible
collateral. Such information is found in bilateral legal document,
custodian triparty agreements and is also used for other purposes where
defining whether an asset is eligible to be used as collateral to
mitigate risk on a defined set (portfolio) of financial instruments
between parties.

Data requirements to represent eligible collateral include common
information such as, asset descriptors e.g. who issues the asset, the
asset type, its maturity profile, any related agency credit risk rating,
and if any collateral haircut is to be applied to the asset's value.

Within legal collateral documents, the definition of eligible collateral
can take several forms; some may want to list assets and the related
eligibility information in table-format using common language, use
textual description of types of eligible assets, or use common
identifiers and taxonomies. However, it is evident for each method
chosen there is no common data standard to express the same information
for all the data attributes used.

The financial crisis and the resulting regulatory framework that emerged
from guidelines outlined under BCBS/IOSCO and Basel III has presented
further requirements that define specific criteria for collateral
eligibility that must be applied to portfolios. Observation of different
regulations under various jurisdictions has presented several challenges
for defining collateral asset economic identity, correct categorisation
and application of specified haircuts and concentration limits. Having
no common standards in place to represent the key data has led to
lengthy negotiation, misinterpretation, lack of interoperability and
downstream operational inefficiency.

# Eligible Collateral in the CDM

The CDM provides a standard digital representation of the data required
to express collateral eligibility for purposes such as representation in
legal agreements that govern transactions and workflows. The benefits of
this digital representation are summarized below:

-   Provides a comprehensive digital representation to support the data
    requirements to universally identify collateral types.
-   Includes ability to identify attributes of collateral that
    contribute to the risk like the type of asset, interest structures,
    economics, embedded options and unique characteristics.
-   Uses data standards to specify eligibility related information such
    as haircuts (regulatory credit quality, FX related or additional
    haircuts), agency or composite credit ratings and asset maturity
    terms
-   Provides functions to apply treatment rules to predefined collateral
    criteria such as include/exclude logic
-   Applies treatment rules for concentration limits caps by percentage
    or value. These can be applied to one or multiple elements of the
    collateral characteristics and defined criteria
-   Attributes are included to identify regulatory rules by defined
    eligibility identification categories published by regulatory bodies
    such as EMIR, CFTC and US Prudential
-   Provides a means of Identifying Schedules and constructing reusable
    collateral profiles
-   Standardises digital data representation components to construct the
    details to identify collateral eligibility not just for regulatory
    purposes but for all needs of eligibility expression within legal
    contracts and documentation
-   Promotes a standard format to represent eligible collateral for
    negotiators to identify and agree details without misinterpretation
-   Provides standards to facilitate Interoperability between platforms
    for digitised eligible collateral information
-   Connects contractual terms of eligibility in documentation to
    supporting processes
-   Standardises data records for audit requirements
-   Provides many opportunities in the collateral ecosystem and benefits
    a data representation of collateral choices that can be imported and
    exported to other systems such as credit, treasury, trade reporting
    and custodian platforms, providing a full workflow solution from
    negotiation, execution through to optimisation and settlement.

# Modelling Approach

## Scope

The model's primary intention is to deliver standards for OTC
Derivatives with a focus on uncleared margin rules. In addition, the
approach is intended to also be used to express collateral eligibility
for other industry workflows such as Securities Lending, Repo and
Exchange Traded Derivatives (ETD). The model foundations, broad range of
attributes and functions has been constructed with this in mind and can
be extended further to operate to wider processes.

The common data requirements have been established through industry
working groups reviewing a wide range of examples in order to identify
collateral for the purpose of constructing eligible collateral
specifications, including representation of additional attributes for
regulatory risk and credit factors. For the purpose of understanding the
principle, these can be divided into the following categories:

-   Issuer Identification
-   Asset Identification
-   Collateral Haircuts
-   Maturity Ranges
-   Concentrations Limits
-   Treatments Functions

The data attributes within the model provides the flexibility to firstly
identify the collateral issuer and asset class, then define its maturity
if relevant, then apply treatment rules for any chosen haircut
percentages, concentration limits and inclusion or exclusion conditions.
The combination of these terms allows a wide range of collateral and
associated data for eligibility to be represented.

## Approach to identifying Collateral Assets

The universe of collateral used throughout the industry for risk
mitigation purposes is vast and the intention is for the CDM to provide
a standard means of identifying as much of this collateral universe as
possible initially and then extend the model further as required via
open-source contributions under the CDM governance structure.

At the outset, there have been no common standards for describing
collateral; the foundational structure in the CDM provides a means to
identify a majority of collateral issuers and covers a wide range of
asset types that are commonly seen in eligible collateral data.

The approach in the CDM is to adopt a similar method to the 'Animal
Kingdom' tree and taxonomy (kingdom → phylum → class → order→ family →
genus → species) -- i.e., that there is one method for describing any of
the core attributes of an "animal" (i.e., type of issuer/type of
asset/type of economic terms) that need to be referenced, but only one
way. Each issuer type, asset type, economic type etc has a unique place
in the universe of collateral but is logically grouped together with
similar types.

An illustrative example for understanding the principle is shown here:

![image](/img/collateral-asset-identifier-tree.png)

The CDM method for representing eligible collateral will be capable of
reference to, and inclusion in, common master and respective collateral
documentation for OTC Derivatives and non- OTC master agreements
(notably Repo and Securities Lending) and potentially for OTC Cleared
and Exchange Traded Derivatives. For this reason, it is important that
the CDM is able to accommodate Regulatory Uncleared Margin Rules
concepts that are relevant but the model should not prescribed by them.

Although the industry will benefit from using a digital standard to
describe collateral, there is also a need to recognise that market
participants may want to identify eligible collateral without fully
describing every feature and instead use industry identifiers, where
available. Thus, the CDM also provides a means that collateral issuers
can be identified using common legal entity identifiers such as an LEI.
Similarly, asset types can be identified using a product ID such as ISIN
or CUSIP or a standard taxonomy source.

## High Level Design Principles

The foundational data structure from the highest level allows firstly to
represent eligibility through specification of *criteria*:

![image](/img/cdm-graphical-ecs.png)

The Asset type is used to specify criteria related to the nature of the
asset, such as its type (cash, debt, equity, or other), its country of
origin or its denominated currency.

The Issuer type is used to specify criteria related to the issuer of the
asset, such the type of issuer (government, corporate, etc), specific
issuer name, or agency rating

Treatment is used to specify the valuation percentage, any concentration
limits and or specific inclusion or exclusion conditions, which
additionally apply to filter whether a piece of collateral is eligible
or not.

The combination of these terms allows a wide variety of eligible
collateral types to be represented and a structure can be used to
identify individual collateral types or a group of collateral assets for
inclusion in specifying eligible collateral schedule details.

# Identifying Eligible Collateral using the CDM Data Structure

A combination of data types can be used to describe the collateral
asset, its origin and its issuer. Data type `EligibleCollateralCriteria`
extends `CollateralCriteriaBase` and contains data types to enable to
define collateral Asset and Issuer characterises

## Asset Criteria

The data type `AssetCriteria` is used to specify the definition of the
collateral asset, this includes the following data attributes:

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

   condition AssetCriteriaChoice:
       optional choice collateralAssetType, collateralTaxonomy, productIdentifier
```

-   `collateralAssetType` Represents a filter based on the asset product
    type.
-   `collateralAssetType` Represents a filter based on the asset product
    type.
-   `assetCountryOfOrigin` Represents a filter based on the issuing
    entity country of origin.
-   `denominatedCurrency` Represents a filter based on the underlying
    asset denominated currency.
-   `agencyRating` Represents an agency rating based on default risk and
    creditors claim in event of default associated with specific
    instrument.
-   `maturityType` Specifies whether the maturity range is the remaining
    or original maturity.
-   `maturityRange` Represents a filter based on the underlying asset
    maturity.
-   `productIdentifier` Represents a filter based on specific instrument
    identifiers (e.g. specific ISINs, CUSIPs etc)
-   `collateralTaxonomy` Specifies the collateral taxonomy, which is
    composed of a taxonomy value and a taxonomy source.
-   `domesticCurrencyIssued` Identifies that the Security must be
    denominated in the domestic currency of the issuer.
-   `ListingType` Specifies the exchange, index or sector specific to
    listing of a security.

Each of the `AssetCriteria` data attributes in the model provides
further granularity to describe the asset, either as basic types or
complex types, for example:

-   `collateralAssetType` can be used to define further by `AssetType`
    such as `securityType`, `debtType`, `equityType`, or `fundType`.
    Each of these can be used to represent data in further granularity
    if required providing more enumeration options. These are covered in
    further examples throughout this guide.
-   `assetCountryOfOrigin` and `denominatedCurrency` are 'string' basic
    types and can be populated by a country name, code or currency
    abbreviations.
-   `domesticCurrencyIssued` is a Boolean data attribute option to
    specify True or False.
-   `AgencyRatingCriteria` and maturityType are explained in more detail
    in further examples throughout this guide.

## Issuer Criteria

The data type `IssuerCriteria` is used to specify the issuer of a
collateral asset, this includes the following data attributes:

``` Haskell
type IssuerCriteria:
  issuerType CollateralIssuerType (0..*)
  issuerCountryOfOrigin ISOCountryCodeEnum (0..*)
  issuerName LegalEntity (0..*)
  issuerAgencyRating AgencyRatingCriteria (0..*)
  sovereignAgencyRating AgencyRatingCriteria (0..*)
  counterpartyOwnIssuePermitted boolean (0..1)
```

-   `issuerType` Represents a filter based on the type of entity issuing
    the asset.
-   `issuerCountryOfOrigin` Represents a filter based on the issuing
    entity country of origin, which is the same as filtering by eligible
    Sovereigns.
-   `issuerName` Specifies the issuing entity name or LEI.
-   `issuerAgencyRating` Represents an agency rating based on default
    risk and creditors claim in event of default associated with asset
    issuer.
-   `sovereignAgencyRating` Represents an agency rating based on default
    risk of country.
-   `counterpartyOwnIssuePermitted` Represents a filter based on whether
    it is permitted for the underlying asset to be issued by the posting
    entity or part of their corporate family.

For each of the `IssuerCriteria` options, the model will provide further
options of granularity; for example `issuerType` will allow you to
define further express data for the detail to be more specific to the
type of issuer for example: `SovereignCentralBank`, `QuasiGovernment`,
`RegionalGovernment` and so on., If necessary, each will offer further
levels of granularity relevant to each issuer type. These will be
covered in more detail and in further examples throughout this guide.

Other attributes of `IssuerCriteria` can be used and added to your
issuer description, if required, and will give various levels of
granularity dependent on their nature and purpose in describing the
issuer. For example `issuerCountryOfOrigin` is a free format 'string'
representation to be populated by a country name, code.

`counterpartyOwnIssuePermitted` is a Boolean data option to specify Y/N.
`issuerName` is used to express a legal entity id as a 'string'.
Whereas, other attributes will have more detailed options such as
`IssuerAgencyRating` These will be covered in more detail and in further
examples throughout this guide.

# Treatment Functions

Treatment rules can be applied to eligible collateral in several ways
using data type `CollateralTreatment` which specifies the treatment
terms for the eligible collateral criteria specified . This includes a
number of options which are listed below:

-   `ValuationTreatment` Specification of the valuation treatment for
    the specified collateral, such as haircuts percentages.
-   `concentrationLimit` Specification of concentration limits
    applicable to the collateral criteria.
-   `isIncluded` A boolean attribute to specify whether collateral
    criteria are inclusion (True) or exclusion (False) criteria**.**

The CDM model is flexible so that these treatment rules can be applied
to the detail of data expression for eligible collateral on an
individual basis or across a group of issuer names or asset types or
combinations of both. Each treatment function will have its own set of
options and the model will provide further options of granularity.

## Valuation Treatments

`CollateralValuationTreatment` will allow for representation of
different types of haircuts, as follows . Please note: data expression
for percentages is a number with a condition to be expressed as a
decimal between 0 and 1.

-   `haircutPercentage` Specifies a haircut percentage to be applied to
    the value of asset and used as a discount factor to the value of the
    collateral asset, expressed as a percentage in decimal terms.
-   `marginPercentage` Specifies a percentage value of transaction
    needing to be posted as collateral expressed as a valuation.
-   `fxHaircutPercentage` Specifies an FX haircut applied to a specific
    asset which is agreed between the parties.
-   `AdditionalHaircutPercentage` Specifies a percentage value of any
    additional haircut to be applied to a collateral asset, the
    percentage value is expressed as the discount haircut to the value
    of the collateral.

## Concentration Limits

`ConcentrationLimit`,is another form of treatment which has a set of
attributes which allow concentration limits to be defined in two
alternative ways using `ConcentrationLimitCriteria`

*Generic method* : If you wish to apply a concentration limit to a set
of pre-defined eligible collateral details in the CDM, you would use
`ConcentrationLimitType`, `ConcentrationLimitTypeEnum` which allows you
to define which existing details to apply the concentration limit to
from an enumeration list including (Asset, Base currency, Issuer,
Primary Exchange, Sector.. etc)

*Specific method* : If you wish to apply a concentration limit to a
specific asset or issuer of asset, you would use the
`ConcentrationLimitCriteria`. This extends `CollateralCriteriaBase` and
allows you be more specific using the granular structures of the
`IssuerCriteria` and `AssetCriteria` to specify the details of the
issuer or asset you want to apply the concentration limit.

In addition, you would need to specify the form of the Concentration
limit being used as a value limit range to apply a cap (upper bound) or
floor (lower bound) to the identified asset, issuer or attributes. There
are two options that allow this to be represented in value or percentage
terms as follows:

``` Haskell
type ConcentrationLimit:
  concentrationLimitCriteria ConcentrationLimitCriteria (0..*)
  valueLimit MoneyRange (0..1)
  percentageLimit NumberRange (0..1)
```

-   `ValueLimit` Specifies the value of collateral limit represented as
    a range
-   `percentageLimit` Specifies the percentage of collateral limit
    represented as a decimal number

There are conditions in the CDM when applying concentration limits that
constrain choices to:

-   one of the concentration limit methods (either a limit type or limit
    criteria must be specified)
-   one concentration limit type (either a value limit or percentage
    limit concentration must be specified)

## Inclusion Rules

The collateral treatment function `isIncluded` can be used as a
treatment term for the eligible collateral criteria specified and
indicate if the collateral is eligible or not. Therefore a Boolean data
attribute is applied using one of the following:

-   (True) Collateral Inclusion
-   (False) Collateral Exclusion

# Additional Granular Information for Eligible Collateral Data Construction

The CDM data structure to express collateral eligibility has been
explored in more detail and it has been demonstrated where the
`EligibleCollateralCriteria` can be broken down into data related to
`IssuerCriteria` and `AssetCriteria` and rules can be applied using data
for CollateralTreatment.

The following section focuses on the more granular details of the
various data attributes available through `IssuerCriteria` and
`AssetCriteria`.

## Collateral Asset and Issuer Types

Under data types for both `IssuerCriteria` and `AssetCriteria` the first
data attributes available to detail collateral are `issuerType` and
`collateralAssetType` these will offer additional data.

**Defining Collateral Issuers:**

`issuerType` allows for multiple expressions of data related to the
issuer using `CollateralIssuerType` containing data attributes as
follows:

`issuerType IssuerTypeEnum` Specifies the origin of entity issuing the
collateral with the following enumerations shown as examples but not
limited to:

-   SupraNational
-   SovereignCentralBank
-   RegionalGovernment
-   Corporate

Some attributes are extended to allow further granularity as shown in
the examples below:

`supraNationalType` Represents types of supranational entity issuing the
asset, such as international organisations and multilateral banks --
with enumerations to define:

-   InternationalOrganisation
-   MultilateralBank

**Defining Collateral Assets:**

`collateralAssetType` allows for multiple expressions of data related to
the collateral asset using `AssetType` which has further data attributes
as follows:

assetType - Represents the type of collateral asset with data attributes
as enumerations to define

-   Security
-   Cash
-   Commodity
-   Other Collateral Products

`securityType` - Represents the type of security with data attributes to
define, as examples:

-   Debt
-   Equity
-   Fund

`debtType` - Represents a filter based on the type of bond which
includes further optional granularity for certain characteristics that
may be required to define specific details related to debt type assets
such but not limited to as follows:

-   DebtClass
    -   Asset Backed
    -   Convertible
    -   RegCap
    -   Structured
-   DebtEconomics
    -   Debt Seniority
        -   *Secured*
        -   *Senior*
        -   *Subordinated*
    -   Debt Interest
        -   *Fixed*
        -   *Floating*
        -   *Inflation Linked*
    -   Debt Principal
        -   *Bullet*
        -   *Callable*
        -   *Puttable*
        -   *Amortising*

A similar structure exists for `equityType` and `fundType` and other
collateral assets types.

As well as defining the details of the asset and issuer of collateral
using the various attributes available in the CDM description tree,
there are other detailed criteria that may be required to define
collateral and for use in expressing eligibility details; the guide will
detail these and indicate the data structure available to define them.

## Agency Ratings Criteria (Used within both Issuer and Asset Criteria)

The use of specifying agency rating criteria for credit purposes can be
useful for many means in legal documentation to drive operational
outcomes such as collateral thresholds and event triggers. When defining
collateral eligibility, the CDM can represent collateral underlying
credit default risk in various ways by using agency rating sources.
These are useful and common for determining eligible collateral between
parties and those defined under regulatory rules for posting certain
margin types.

The model components are specified in the CDM using data type
`AgencyRatingCriteria` : - Represents class to specify multiple credit
notations alongside a conditional 'any' or 'all' qualifier.

For the purpose of use in defining eligible collateral this can be
applied to the following data attributes:

-   `IssuerCriteria` \> `issuerAgencyRating` - Represents an agency
    rating based on default risk and creditors claim in event of default
    associated with asset issuer
-   `IssuerCriteria` \> `sovereignAgencyRating` - Represents an agency
    rating based on default risk of the country of the issuer
-   `AssetCriteria` \> `agencyRating` - Represents an agency rating
    based on default risk and creditors claim in event of default
    associated with specific instrument

Data type `AgencyRatingCriteria` Allows specification of the following
related information to eligible collateral

``` Haskell
type AgencyRatingCriteria:
  qualifier QuantifierEnum (1..1)
  creditNotation CreditNotation (1..*)
  mismatchResolution CreditNotationMismatchResolutionEnum (0..1)
  referenceAgency CreditRatingAgencyEnum (0..1)
  boundary CreditNotationBoundaryEnum (0..1)
```

-   `qualifier` Indicator for whether *all or any* of the agency ratings
    specified apply using the All or Any enumeration contained within
    QuantifierEnum
-   `creditNotation` Indicates the agency rating criteria specified for
    the asset or issuer. This expands to offer further granularity for
    details relating to the credit details

``` Haskell
type CreditNotation:
  agency CreditRatingAgencyEnum (1..1)
  notation string (1..1)
    [metadata scheme]
  scale string (0..1)
    [metadata scheme]
  debt CreditRatingDebt (0..1)
  outlook CreditRatingOutlookEnum (0..1)
  creditWatch CreditRatingCreditWatchEnum (0..1)
```

-   `CreditRatingAgencyEnum` A list of enumerated values to specify the
    rating agency or agencies, (all major rating agencies are supported)
-   `notation` Specifies the credit rating notation. As itvaries among
    credit rating agencies, the CDM does not currently specify each
    specific rating listed by each agency. The data'string' allows the
    free format field to be populated with a rating , such as 'AAA'
-   `scale` Specifies the credit rating scale, with a typical
    distinction between short term, long term. The data 'string' allows
    the free format field to be populated with a scale indicator such as
    'long term', 'short term'.
-   `debt` Specifies the credit rating debt type is for any credit
    notation associated debt related credit attributes if needed. This
    gives the additional flexibility option to identify amongst the
    credit criteria debt characteristics such as (high yield, deposits,
    investments grade) The data type extends to offer two options
    -   `debtType` This attribute is free format 'string' and used
        when only one debt type is specified
    -   `debtTypes` This allows you to specify for than one multiple
        debt type characteristics and has a qualifying conditions to
        specify if you wish to include 'All' or 'Any' of the elements
        listed in scope
-   `outlook` This data attributes allows you to specify the a credit
    rating outlook assessment that is commonly determine by rating
    agencies. It is an indication of the potential direction of a
    long-term credit rating over the intermediate term, which is
    generally up to two years for investment grade and generally up to
    one year for speculative grade. The enumeration list allows you to
    specify if required one of the following outlook terminology
    -   Positive (A rating may be raised)
    -   Negative (A rating may be lowered)
    -   Stable (A rating is not likely to change)
    -   Developing (A rating may be raised, lowered, or affirmed)
-   `creditWatch` Similar to detailing a type of credit outlook, credit
    agencies will also identify individual credit by a means of a
    monitoring (watch) status for an undefined period. This watch status
    can be expressed using the following data terminology under this
    enumeration list.
    -   Positive (A rating may be raised)
    -   Negative (A rating may be lowered)
    -   Developing (A rating may be raised, lowered, or affirmed)
-   `mismatchResolution` If several agency issue ratings are being
    specified that are not necessarily equivalent of each, this data
    attribute allows you to label which one has certain characteristics
    amongst the others, such as lowest or highest etc, the following
    enumerations are available:
    -   Lowest
    -   Highest
    -   Reference Agency
    -   Average
    -   Second Best

``` Haskell
enum CreditNotationMismatchResolutionEnum:
   Lowest
   Highest
   ReferenceAgency
   Average
   SecondBest
```

-   `referenceAgency` This part of the agency rating criteria again
    allows you to specify from the list of enumerated values for the
    rating agency. But in this case it is to identify the rating agency
    if you need to determine one from others if you used the data
    attribute referenceAgency in the
    `CreditNotationMismatchResolutionEnum` as outlined above.
-   `boundary` Indicates the boundary of a credit agency rating i.e
    minimum or maximum.

A condition exists If the mismatch resolution choice is
`ReferenceAgency`, you must ensure that the reference agency is
specified through the `CreditRatingAgencyEnum`

*For example:*

Through `CreditNotation` the following data has been specified:

S&P AAA

Moodys Aaa

Fitch AAA

Then one of these needed to be specified as the dominant rating as an
example (Moodys), you would express `mismatchResolution` \>
`CreditNotationMismatchResolutionEnum` \> **ReferenceAgency**

`referenceAgency` \> `CreditRatingAgencyEnum` \> **Moodys**

## Collateral Taxonomy (Used within Asset Criteria)

It is understood that data used to determine asset types used in
specifying eligible collateral information can often refer to common
structured standard pre-defined taxonomy sources. Although the purpose
of the CDM is to encourage one standard representation of data for asset
types, there are circumstances where assets are organised and labelled
into categories, such as by regulators. In some circumstances, it may be
a requirement to refer to these identifiable sources. In the CDM, these
taxonomy sources can be referenced in a consistent representation.

The CDM allows the definition of, and reference to, certain taxonomy
sources to be used to express details for eligibility. These can be used
as an additional means of expressing asset types outside of the
descriptions tree or alongside it. Under data type `AssetCriteria` there
are data attributes to reference collateral related taxonomy sources as
follows:

Data Type `collateralTaxonomy` will allow for specification of the
collateral taxonomy, which is composed of a taxonomy value and a
taxonomy source.

-   The data attribute `taxonomySource` must be specified and will
    provide the following options through the enumerations list:
    -   CFI (The ISO 10962 Classification of Financial Instruments code)
    -   ISDA (The ISDA product taxonomy)
    -   ICAD (ISDA Collateral Asset Definition Identifier code)
    -   EU EMIR Eligible Collateral Asset Class (European Union Eligible
        Collateral Assets classification categories based on EMIR
        Uncleared Margin Rules)
    -   UK EMIR Eligible Collateral Asset Class (UK EMIR Eligible
        Collateral Assets classification categories based on UK EMIR
        Uncleared Margin Rules)
    -   US CFTC PR Eligible Collateral Asset Class (US Eligible
        Collateral Assets classification categories based on Uncleared
        Margin Rules published by the CFTC and the US Prudential
        Regulators)

The options CFI, ISDA and ICAD would be further expressed with the
flexible data *'string'* representation through data type
`ProductTaxonomy`.

However the regulatory 'Eligible Collateral Asset Class' rules have
individual enumeration lists unique to their asset class categories
identified under each of the respective regulatory bodies. Therefore if
these are selected as taxonomy sources through TaxonomySourceEnum it is
required to specify details from the related unlimited enumeration lists
that exist under data type CollateralTaxonomyValue, these are shown
below:

-   `eu_EMIR_EligibleCollateral`
-   `uk_EMIR_EligibleCollateral`
-   `us_CFTC_PR_EligibleCollateral`
-   `nonEnumeratedTaxonomyValue`

Please note: The regime codes are not mandatory and are based on
reference to the regulatory eligible categories, but do not qualify the
regulations. The CDM only provides a standard data representation so
that institutions can recognise the same information.

Each enumeration has a full description of what regulatory published
rules the list of eligible collateral assets classification
codes/categories are based on. Under each enumeration list there are a
number of categorised eligible asset groups which have been identified
under each set of regulatory rules. Some limited examples of these are
shown below which are contained in the `EU_EMIR_EligibleCollateralEnum`
list:

-   `EU_EMIRTypeA` -Denotes Cash in the form of money credited to an
    account in any currency, or similar claims for the repayment of
    money, such as money market deposits.
-   `EU_EMIRTypeB` - Denotes gold in the form of allocated pure gold
    bullion of recognised good delivery.
-   `EU_EMIRTypeC` -Denotes debt securities issued by Member States'
    central governments or central banks.

The cardinality for these enumeration lists (0..\*) denotes that
multiple values can be provided so several categories can be applied to
a line of data expressed in an eligibility profile.

The final attribute in `CollateralTaxonomyValue`,
`nonEnumeratedTaxonomyValue`, offers additional data expression outside
of the listed taxonomy values, for use when a taxonomy value is not
enumerated in the model.

There are conditions associated to the use of the data attributes within
`CollateralTaxonomyValue` to ensure correct use of the data. These
conditions enforce the specified regulatory enumerated list to match the
taxonomy source. Therefore as an example you can only specify a category
from the EMIR enumerations list if the taxonomy source is
`EU_EMIR_EligibleCollateralAssetClass`

## Maturity Profiles (Used within Asset Criteria)

The expression of collateral life span periods and specific maturity
dates is a common eligibility characteristic and may be needed for
determining other key collateral treatments such as haircut percentages.
The CDM has various approaches for representing assets maturities, they
are data attributes within the data type `AssetCriteria` as follows:

-   `maturityType` - Allows specification of the type of maturity range
    and has the following enumerated values:
    -   Remaining Maturity
    -   Original Maturity
    -   From Issuance
-   `maturityRange` Allows filtering on the underlying asset maturity
    through definition of a lower and upper bound range using data type
    `PeriodRange`. Using `PeriodBound` for both ends of the scale you
    would need to specify the period, for example:
    -   `lowerBound` 1Y , representing one year using the `Period` \>
        `periodMultiplier` **1** and period `PeriodEnum` **Y**
    -   `upper bound` 5Y, representing 5 years using the `Period` \>
        `periodMultiplier` **5** and period `PeriodEnum` **Y**
    -   In addition `PeriodBound` has the inclusive boolean to indicate
        whether the period bound is inclusive, e.g. for a lower bound,
        false would indicate greater than, whereas true would indicate
        greater than or equal to.

A combination of these data attributes combined allows specificity of
the maturity profile of collateral asset types and definition of a range
that would sit alongside the other asset data criteria. Multiple
maturity ranges can be listed for and associated to one asset type,
varied collateral treatment haircuts can then be added to each of the
ranges, this would be a common feature of a collateral eligibility
schedule especially if there is an uncleared margin rules regulatory
requirement.

## Product Identifier (Used within Asset Criteria)

The CDM model as described throughout this guide will allow the user to
define collateral assets through the granular structure of the
`AssetCriteria`, but we must understand that expression of asset details
for eligibility purposes can take other forms across the universe of
collateral, for some processes there is a requirement to use certain
product identifiers. Data type `productIdentifier` can be used to
express specific instrument identifiers such as ISINs, CUSIPs etc. There
is a section within the CDM documentation that covers this area of the
model, this can be found in the following link
[products-with-identifiers-section](/docs/product-model#products-with-identifiers).

## Listing (Used within Asset Criteria)

Additional details may be required to describe asset characteristics
related to a securities financial listing, exchange, sector or specified
indices, if relevant these are used to express eligibility details in
documentation and collateral profiles. The data type listing
`ListingType` contained within `AssetCriteria` can be used to
specification such listing criteria. This expands to three attributes
that can be used individually or together :

-   exchange string (0..1) Represents a filter based on the primary
    stock exchange facilitating the listing of companies, exchange of
    Stocks, Exchange traded Derivatives, Bonds, and other Securities.
-   sector string (0..1) Represents a filter based on an industry sector
    defined under a system for classifying industry types such as
    'Global Industry Classification Standard (GICS)' and 'North American
    Industry Classification System (NAICS) or other related industry
    sector reference data.
-   index Index (0..1) -- Represents a filter based on an index that
    measures a stock market, or a subset of a stock market. The
    \`Index\` data type can be used in the CDM to define an index in
    terms of a \`ProductIdentifier' and an enumeration identifying the
    index constituent type.

# Using The CDM Data Representation to Construct Eligible Collateral Information

This user guide provides an overview of the data available to represent
details for expressing eligibility inclusive of the asset criteria,
issuer criteria and the collateral treatment inclusion rules, valuation
percentages and concentration limits. However, a combination of how the
data is represented and structured will determine specific outcomes.

The data can be specified and organised as a list of attributes, such as
descriptive details of the asset and the issuer, to identify the makeup
of collateral.

This list can be made up of multiple attributes from both the asset or
issuer criteria and be grouped together. Items listed in this way using
the same level in the CDM are defined as an 'and' relationship. However,
opportunities exist in the CDM data structure to extended lists within a
list and add another level to both asset and issuer criteria which will
operate as an 'or' relationship. An example of this would be within data
type `AssetCriteria` there is an option to define a
`denominatedCurrency` (0..\*); this data attribute with an open
cardinality allows for a definition of a list of currencies and
describes where a 'or' relationship exists.

Each list combination identified in this way can then have specific
treatment rules applied to it.

For example, a simple list can be constructed as follows:

AssetCriteria\>

-   collateralAssetType\>assetType: **CASH**
-   denominatedCurrency: **USD**

And then the following treatment applied to the list

Treatment\>

-   isIncluded: **TRUE**
-   haircutPercentage**: 0.005**

The outcome is- USD CASH IS ELIGIBLE AT 99.5% VALUE/ or WITH 0.5%
HAIRCUT

To extend this example further a digital JSON output extract of the same
details is show here:

``` Javascript
{
"criteria": [{
   "asset": [{
         "collateralAssetType": [{
               "assetType": "CASH"
           }],
         "denominatedCurrency": [{
               "value": "USD"
}]
         }],
       "treatment": {
           "haircutPercentage": {
               "haircutPercentage": 0.005
               },
           "isIncluded": true
           }
```
