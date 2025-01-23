---
title: Eligible Collateral Representation
---
# Eligible Collateral Representation

## Introduction

Within collateral documentation, it is common to detail what assets you
will exchange with your counterparties, i.e., what you deem eligible
collateral. Such information is found in bilateral legal documents,
custodian triparty agreements, and is also used for other purposes where
defining whether an asset is eligible to be used as collateral to
mitigate risk on a defined set (portfolio) of financial instruments
between parties.

Data requirements to represent eligible collateral include common
information such as asset descriptors e.g. who issues the asset, the
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
for defining collateral asset economic identity, correct categorisation,
and application of specified haircuts and concentration limits. Having
no common standards in place to represent the key data has led to
lengthy negotiation, misinterpretation, lack of interoperability, and
downstream operational inefficiency.

## Eligible Collateral in the CDM

The CDM provides a standard digital representation of the data required
to express collateral eligibility for purposes such as representation in
legal agreements that govern transactions and workflows. The benefits of
this digital representation are summarized below:

-   Provides a comprehensive digital representation to support the data
    requirements to universally identify collateral types.
-   Includes ability to identify attributes of collateral that
    contribute to the risk like the type of asset, interest structures,
    economics, embedded options and unique characteristics.
-   Provides the capability for these attributes to be combined using AND,
    OR, and NOT logic operators to enable complex representations of the
    interactions between attributes to digitise the rules around eligibility.
-   Uses data standards to specify eligibility related information such
    as haircuts (regulatory credit quality, FX related or additional
    haircuts), agency or composite credit ratings and asset maturity
    terms.
-   Provides functions to apply treatment rules to predefined collateral
    criteria such as include/exclude logic.
-   Applies treatment rules for concentration limit caps by percentage
    or value. These can be applied to one or multiple elements of the
    collateral characteristics and defined criteria.
-   Includes attributes to identify regulatory rules by defined
    eligibility identification categories published by regulatory bodies
    such as EMIR, CFTC and US Prudential.
-   Provides a means of identifying schedules and constructing reusable
    collateral profiles.
-   Standardises digital data representation components to construct the
    details to identify collateral eligibility not just for regulatory
    purposes but for all needs of eligibility expression within legal
    contracts and documentation.
-   Promotes a standard format to represent eligible collateral for
    negotiators to identify and agree details without misinterpretation.
-   Provides standards to facilitate Interoperability between platforms
    for digitised eligible collateral information.
-   Connects contractual terms of eligibility in documentation to
    supporting processes.
-   Standardises data records for audit requirements.
-   Provides many opportunities in the collateral ecosystem and benefits
    a data representation of collateral choices that can be imported and
    exported to other systems such as credit, treasury, trade reporting
    and custodian platforms, providing a full workflow solution from
    negotiation, execution through to optimisation and settlement.

## Modelling Approach

### Scope

The model's primary intention is to deliver standards for OTC
Derivatives with a focus on uncleared margin rules. In addition, the
approach is intended to also be used to express collateral eligibility
for other industry workflows such as Securities Lending, Repo, and
Exchange Traded Derivatives (ETD). The model foundations, broad range of
attributes, and functions have been constructed with this in mind and can
be extended further to operate to wider processes.

The common data requirements have been established through industry
working groups reviewing a wide range of examples in order to identify
collateral for the purpose of constructing eligible collateral
specifications, including representation of additional attributes for
regulatory risk and credit factors. For the purpose of understanding the
principle, these can be divided into the following categories:

-   Asset and Issuer Identification
-   Collateral Haircuts
-   Maturity Ranges
-   Concentrations Limits
-   Treatments Functions

The data attributes within the model provide the flexibility to firstly
identify the collateral issuer and asset class, then define its maturity
if relevant, then apply treatment rules for any chosen haircut
percentages, concentration limits and inclusion or exclusion conditions.
The combination of these terms allows a wide range of collateral and
associated data for eligibility to be represented.

### Approach to identifying Collateral Assets

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
genus → species), that is there is one method for describing any of
the core attributes of an "animal" (i.e. type of issuer/type of
asset/type of economic terms) that need to be referenced, but only one
way. Each issuer type, asset type, economic type etc has a unique place
in the universe of collateral but is logically grouped together with
similar types.

An illustrative example for understanding the principle is shown here:

![](img/collateral-asset-identifier-tree.png)

The CDM method for representing eligible collateral will be capable of
reference to, and inclusion in, common master and respective collateral
documentation for OTC Derivatives and non-OTC master agreements
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

### High Level Design Principles

The highest level and foundational data structure for the representation of
eligibility is the `EligibleCollateralSpecification` which is a *root class*.
An `EligibleCollateralSpecification` typically represents
the schedule of eligible collateral agreed between two parties and is represented
digitally as one or more `EligibleCollateralCriteria` to define the details.

![image](/img/cdm-graphical-ecs-6.png)

`EligibleCollateralSpecification` consists of the following key attributes:

- The `identifier` attribute is used to specify the identifier(s) that uniquely identify eligible collateral or a set of eligible collateral, such as a schedule or equivalant for an identity issuer.

- The `criteria` attribute is used to specify the set of criteria used to define eligible collateral,
  made up of one or more `EligibleCollateralCriteria`.

- The `party` attribute is used to represent the parties to the agreement.

- The `partyRole` attribute is used to specify the role(s) that each of the
  party(s) is playing in the context of the specification, eg Payor or Receiver
  of collateral.

- The `counterparty` attribute defines the parties to the agreement in the form of references
  to the terms "Party1" and "Party2.

`EligibleCollateralCriteria` consists of the following attributes:

- The `collateralCriteria` attribute is used to specify all of the criteria terms; this is discussed
  in more detail in the next section.

- The `treatment` attribute is used to specify the valuation percentage, any concentration
limits and or specific inclusion or exclusion conditions, which
additionally apply to filter whether a piece of collateral is eligible
or not.

- The `appliesTo` attribute is used to specify which of the two counterparties the
  criteria applies to (either one or both counterparties).

- The `restrictTo` attribute can be used to restrict the criteria to only apply to a
  specific type of margin, i.e. IM or VM.
  
- The `ratingPriorityResolution` attribute denotes which Criteria has priority if more
  than one agency rating applies.

The combination of these terms allows a wide variety of eligible
collateral types to be represented and a structure can be used to
identify individual collateral types or a group of collateral assets for
inclusion in specifying eligible collateral schedule details.

## Identifying Eligible Collateral

A combination of data types can be used to describe the collateral
asset, its origin and its issuer. Data type `EligibleCollateralCriteria`
inherits attributes from `CollateralCriteriaBase` which contains data types to
define collateral Asset and Issuer characteristics.

### CollateralCriteria

The data type `CollateralCriteria` is used to specify the definition of the
collateral terms. This data type is implemented as `choice` which means that, in
its simplest form, a `CollateralCriteria` can only
consist of a single collateral term. But terms can also be combined, using 
AND and OR logic, which is covered in the next section.

The terms are modelled as individual attributes on the `CollateralCriteria` 
choice data type:

``` MD
choice CollateralCriteria:  
    CollateralIssuerType            <"Criteria is the type of entity issuing the asset.">
    AssetType                       <"Criteria is the asset type of the collateral.">
    IssuerCountryOfOrigin           <"Criteria is the issuing entity country of origin.">
    AssetCountryOfOrigin            <"Criteria is the collateral asset country of origin.">
    CurrencyCodeEnum                <"Criteria is the denominated currency of the collateral.">
    IssuerName                      <"Criteria is a specific named issuer entity.">
    IssuerAgencyRating              <"Criteria is the agency rating(s) of the issuer.">
    SovereignAgencyRating           <"Criteria is the agency rating(s) of the country of the issuer.">
    AssetAgencyRating               <"Criteria is the agency rating(s) of the collateral asset.">
    AssetMaturity                   <"Criteria is the maturity characteristics of the collateral asset.">
    Asset                           <"Criteria is a specifically identified asset.">
    CollateralTaxonomy              <"Criteria is the taxonomy characteristics of an collateral.">
    ListingExchange                 <"Criteria is that the collateral is listed on a specific exchange.">
    ListingSector                   <"Criteria is the industry sector of the collateral asset.">
    Index                           <"Criteria is that the collateral is a constituent of a specific index.">
    CounterpartyOwnIssuePermitted   <"Criteria includes collateral issued by the counterparty.">
    DomesticCurrencyIssued          <"Criteria is that collateral must be denominated in the domestic currency of the issuer.">
```

### Combining CollateralCriteria using AND and OR logic

The above code snippet only allows a single term to be specified within
the `CollateralCriteria` - i.e. the choice of one attribute.

In reality, it is usually necessary to combine terms together to model
the eligiblity schedule fully.  Furthermore, these combinations often 
need to use complex AND and OR logic between the terms.

For example, a schedule may specify that either of the following would
qualify as eligible collateral:
- Equity assets from US issuers
- Bond instruments from UK issuers.

This can be described logically as:

( `AssetType` = 'Equity' AND `IssuerCountryOfOrigin` = 'USA')
OR
( `AssetType` = 'FixedIncome' AND `IssuerCountryOfOrigin` = 'UK').

Within the CDM, the implementation of this AND and OR logic is achieved
by the addition of further attributes to `CollateralCriteria`:

- `AllCriteria`: Enables two or more Collateral Criteria to be combined using AND logic.
- `AnyCriteria`: Enables two or more Collateral Criteria to be combined using OR logic.
- `NegativeCriteria`: Enables a single Collateral Criteria to be excluded using NOT logic.

For completeness, these additional types are modelled like this:

``` MD
choice CollateralCriteria: 
    AllCriteria                
    AnyCriteria   
    NegativeCriteria 

type AllCriteria:  
    allCriteria CollateralCriteria (2..*)

type AnyCriteria: 
    anyCriteria CollateralCriteria (2..*)

type NegativeCriteria:  
    negativeCriteria CollateralCriteria (1..1)
```

### Treatment Functions

Treatment rules can be applied to eligible collateral in several ways
using data type `CollateralTreatment` which specifies the treatment
terms for the eligible collateral criteria specified . This includes a
number of options which are listed below:

-   `valuationTreatment` Specification of the valuation treatment for
    the specified collateral, such as haircuts percentages.
-   `concentrationLimit` Specification of concentration limits
    applicable to the collateral criteria.
-   `isIncluded` A boolean attribute to specify whether collateral
    criteria are inclusion (True) or exclusion (False) criteria.

The CDM model is flexible so that these treatment rules can be applied
to the detail of data expression for eligible collateral on an
individual basis or across a group of issuer names or asset types or
combinations of both. Each treatment function will have its own set of
options and the model will provide further options of granularity.

#### Valuation Treatments

`CollateralValuationTreatment` will allow for representation of
different types of haircuts, as follows. Please note: data expression
for percentages is a decimal number between 0 and 1.

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

#### Concentration Limits

`ConcentrationLimit` is another form of treatment which has a set of
attributes which allow concentration limits to be defined in two
alternative ways using `ConcentrationLimitCriteria`.

*Generic method* : If you wish to apply a concentration limit to a set
of pre-defined eligible collateral details in the CDM, you would use
`ConcentrationLimitType`, `ConcentrationLimitTypeEnum` which allows you
to define which existing details to apply the concentration limit to
from an enumeration list including (Asset, Base currency, Issuer,
Primary Exchange, Sector, etc).

*Specific method* : If you wish to apply a concentration limit to a
specific asset or issuer of asset, you would use the
`ConcentrationLimitCriteria`. This extends `CollateralCriteriaBase` and
allows you be more specific using the granular structures of `CollateralCriteria` 
to specify the details of the terms you want to apply the concentration limit.

In addition, you would need to specify the form of the concentration
limit being used as a value limit range to apply a cap (upper bound) or
floor (lower bound) to the identified asset, issuer or attributes. There
are two options that allow this to be represented in value or percentage
terms as follows:

``` Haskell
type ConcentrationLimit:
  concentrationLimitCriteria ConcentrationLimitCriteria (0..1)
  valueLimit MoneyRange (0..1)
  percentageLimit NumberRange (0..1)
```

-   `valueLimit` Specifies the value of collateral limit represented as
    a range
-   `percentageLimit` Specifies the percentage of collateral limit
    represented as a decimal number.

There are conditions in the CDM when applying concentration limits that
constrain choices to:

-   one of the concentration limit methods (either a limit type or limit
    criteria must be specified)
-   one concentration limit type (either a value limit or percentage
    limit concentration must be specified).

## Additional Granular Construction

The CDM data structure to express collateral eligibility has been
explored in more detail and it has been demonstrated where the
`EligibleCollateralCriteria` can be broken down into data related to
`CollateralCriteria` and rules can be applied using data
for CollateralTreatment.

The following section focuses on the more granular details of the
various data attributes available through `CollateralCriteria`.

### Collateral Asset and Issuer Types

Under data type `CollateralCriteria`, 
the `CollateralIssuerType` and `AssetType` attributes provide additional
data to detail collateral.

#### Defining Collateral Issuers:

`CollateralIssuerType` allows for multiple expressions of data related to the
issuer containing data attributes as
follows:

- `issuerType` of type `IssuerTypeEnum` specifies the origin of entity issuing the
collateral with the following enumerations shown as examples but not
limited to:

  - SupraNational
  - SovereignCentralBank
  - RegionalGovernment
  - Corporate

Some attributes are extended to allow further granularity as shown in
the examples below:

- `supraNationalType` Represents types of supranational entity issuing the
asset, such as international organisations and multilateral banks --
with enumerations to define:

  - InternationalOrganisation
  - MultilateralBank

#### Defining Collateral Assets:

`AssetType` - Represents the type of collateral asset with data attributes
as enumerations to define:

- Security
- Cash
- Commodity
- Other Collateral Products

- `securityType` - Represents the type of security with data attributes to
define, as examples:

  - Debt
  - Equity
  - Fund

- `debtType` - Represents a filter based on the type of bond which
includes further optional granularity for certain characteristics that
may be required to define specific details related to debt type assets
such but not limited to as follows:

  - DebtClass
    - Asset Backed
    - Convertible
    - RegCap
    - Structured

  - DebtEconomics
    - Debt Seniority
      - *Secured*
      - *Senior*
      - *Subordinated*
    - Debt Interest
      - *Fixed*
      - *Floating*
      - *Inflation Linked*
    - Debt Principal
      - *Bullet*
      - *Callable*
      - *Puttable*
      - *Amortising*

A similar structure exists for `equityType` and `fundType` and other
collateral assets types.

As well as defining the details of the asset and issuer of collateral
using the various attributes available in the CDM description tree,
there are other detailed criteria that may be required to define
collateral and for use in expressing eligibility details; the guide will
detail these and indicate the data structure available to define them.

### Agency Ratings Criteria 

The use of specifying agency rating criteria for credit purposes can be
useful for many means in legal documentation to drive operational
outcomes such as collateral thresholds and event triggers. When defining
collateral eligibility, the CDM can represent collateral underlying
credit default risk in various ways by using agency rating sources.
These are useful and common for determining eligible collateral between
parties and those defined under regulatory rules for posting certain
margin types.

The model components are specified in the CDM using the data types
`IssuerAgencyRating`, `SovereignAgencyRating` and `AssetAgencyRating`.

Each of these attributes on `CollateralCriteria` refer to the data type 
`AgencyRatingCriteria` which allows specification of the following
related information to eligible collateral:

``` Haskell
type AgencyRatingCriteria:
  creditNotation CreditNotation (1..1)
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
-   `notation` Specifies the credit rating notation. As it varies among
    credit rating agencies, the CDM does not currently specify each
    specific rating listed by each agency. The data 'string' allows the
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
    -   `debtTypes` This allows you to specify multiple
        debt type characteristics and has a qualifying condition to
        specify if you wish to include 'All' or 'Any' of the elements
        listed in scope
-   `outlook` This data attribute allows you to specify a credit
    rating outlook assessment that is commonly determined by rating
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
    attribute `referenceAgency` in the
    `CreditNotationMismatchResolutionEnum` as outlined above.
-   `boundary` Indicates the boundary of a credit agency rating i.e.
    minimum or maximum.

A condition exists, such that if the mismatch resolution choice is
`ReferenceAgency`, you must ensure that the reference agency is
specified through the `CreditRatingAgencyEnum`

*For example:*

Through `CreditNotation` the following data has been specified:

S&P AAA

Moodys Aaa

Fitch AAA

Then one of these needed to be specified as the dominant rating as an
example (Moodys), you would express `mismatchResolution` -\>
`CreditNotationMismatchResolutionEnum` -\> **ReferenceAgency**

`referenceAgency` -\> `CreditRatingAgencyEnum` -\> **Moodys**

### Collateral Taxonomy 

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
descriptions tree or alongside it. Under data type `CollateralCriteria` there
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
that exist under data type `CollateralTaxonomyValue`, these are shown
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

-   `EU_EMIRTypeA` - Denotes Cash in the form of money credited to an
    account in any currency, or similar claims for the repayment of
    money, such as money market deposits.
-   `EU_EMIRTypeB` - Denotes gold in the form of allocated pure gold
    bullion of recognised good delivery.
-   `EU_EMIRTypeC` - Denotes debt securities issued by Member States'
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

### Maturity Profiles 

The expression of collateral life span periods and specific maturity
dates is a common eligibility characteristic and may be needed for
determining other key collateral treatments such as haircut percentages.
The CDM has various approaches for representing assets maturities, they
are data attributes within the data type `AssetMaturity` as follows:

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

### Asset Identifier 

The CDM model as described throughout this guide will allow the user to
define collateral assets through the granular structure of the
`CollateralCriteria`, but we must understand that expression of asset details
for eligibility purposes can take other forms across the universe of
collateral, for some processes there is a requirement to use specific identifiers
for particular financial products. The data type `Asset` can be used to
express specific instrument identifiers such as ISINs, CUSIPs etc. There
is a section within the CDM documentation that covers this area of the
model, this can be found in the following link
[products-with-identifiers-section](/docs/product-model#identifiers).

### Listing 

Additional details may be required to describe asset characteristics
related to a security's financial listing, exchange, or sector. If relevant, these are used to express eligibility details in
documentation and collateral profiles. The following attributes on 
`CollateralCriteria` can be used to
specify such listing criteria. 

-   `ListingExchange` Represents a filter based on the primary
    stock exchange facilitating the listing of companies, exchange of
    Stocks, Exchange traded Derivatives, Bonds, and other Securities.
-   `ListingSector`  Represents a filter based on an industry sector
    defined under a system for classifying industry types such as
    'Global Industry Classification Standard (GICS)' and 'North American
    Industry Classification System (NAICS) or other related industry
    sector reference data.

## Combining Data in Eligible Collateral

### Combining Criteria & Treatments

This user guide provides an overview of the data available to represent
details for expressing eligibility inclusive of the collateral criteria
and the collateral treatment inclusion rules, valuation
percentages and concentration limits. However, a combination of how the
data is represented and structured will determine specific outcomes.

The data can be specified and organised as a list of attributes, such as
descriptive details of the asset and the issuer, to identify the makeup
of collateral.

Each list combination identified in this way can then have specific
treatment rules applied to it.

For example, a simple list can be constructed as follows:

CollateralCriteria

-   AssetType -\> assetType: **CASH**
-   denominatedCurrency: **USD**

And then the following treatment applied to the list

Treatment

-   isIncluded: **TRUE**
-   haircutPercentage**: 0.005**

The outcome is- USD CASH IS ELIGIBLE AT 99.5% VALUE/ or WITH 0.5%
HAIRCUT

### Automating Construction

In typical industry practice, eligible collateral schedules are quite complex with a number of interacting terms.
To digitize them in the CDM, they will need to be converted into an `EligibleCollateralSpecification` 
with many `EligibleCollateralCriteria` with potentially recursive instances of `CollateralCriteria`.
The construction of these terms can be quite laborious, so functions have been created in the CDM to provide
a level of automation.

#### EligibleCollateralSpecificationInstruction

The instruction is formed using a special type:

``` Haskell
type EligibleCollateralSpecificationInstruction:
    [rootType]
    common EligibleCollateralCriteria (1..1)
    variable EligibleCollateralCriteria (1..*)
```

This data type is used as the input to the `Create_EligibleCollateralSpecificationFromInstruction` function
and should be populated as follows: 

* `common`: a single `EligibleCollateralCriteria` containing the fully-formed data to be cloned.
* `variable`: one or more `EligibleCollateralCriteria` containing the number of instances, with
new terms, that should be created.

This can be represented symbolically as follows:

* `common` is set to the criteria:
  * A1, B1, C1
* `variable` is set to the criteria:
  * C2, C3
* then the created output would be the original criteria plus two clones:
  * A1, B1, C1
  * A1, B1, C2
  * A1, B1, C3

#### Example

A collateral eligibility schedule agreed between two parties specifies the acceptable 
collateral as being a list of certain types of fixed income bonds, from certain types of issuer, 
in certain countries, and 
denominated in a list of acceptable currencies. 
Across all these bonds, different haircut treatments
must be applied, depending on the maturity of the bond.

The `EligibleCollateralSpecification` required to model this case needs to be constructed
using potentially many `EligibleCollateralCriteria`, which are themselves constructed from `CollateralTreatment` and `CollateralCriteria` to correctly represent the conditions regarding the bond
types, issuer types, and currencies.  For each of the maturity bands, this
`EligibleCollateralCriteria` must have a specific `Treatment` specifying the terms
of the haircut.

To use the `Create_EligibleCollateralSpecificationFromInstruction` function to automate
this, one starts by creating the first `EligibleCollateralCriteria` with the all the
required constituent parts, including the first haircut `Treatment`.

The function is then invoked, using this first `EligibleCollateralCriteria` as the
`common` input and then providing the set of haircut `Treatments`, one for each
maturity band, as the `variable` input.  The function will provide a fully formed
output consisting of an  `EligibleCollateralSpecification` containing
all the required `EligibleCollateralCriteria` with their attached `Treatment`s.

#### MergeEligibleCollateralCriteria

This is the function in the CDM that actually does the work of the construction.
However, unlike most functions in CDM, the implementation is in bespoke Java code, albeit
still within the CDM Distribution.  

## Validating Eligible Collateral

A CDM function has been developed to run eligibility validation checks which can be applied to several use cases.
The function requires two sets of information to be present as CDM data:

- a set of eligible collateral criteria, including asset and issuer details, maturity profiles, haircuts and
  any other required characteristics, all modelled in an `EligibleCollateralSpecification`.
- a defined list of characteristics of the collateral that is to be tested against the eligibility details, defined
  in an `EligibilityQuery`.

### Overview

The function uses the CDM collateral representation already built to test collateral eligibility against multiple minimum
collateral requirements and specific eligible collateral schedules. Essentially it is testing different criteria sets built 
from the `EligibleCollateralSpecification` root type against each other as an example: 

- The margin rules set by a specific regulation state that market participants must post collateral using, at a minimum,
  bonds with a maturity of 5 years or more from a specific set of countries.
  The CDM can represent this information using the `EligibleCollateralSpecification` as a list of
  `EligibleCollateralCriteria`s.
  
- The `EligibilityQuery` is used to check whether assets that a participant has available are allowed to be posted
  as collateral.
  This could be a set of simple questions presented as CDM data:
  * Is an EU bond with 4 years remaining maturity eligible? If so, what are applicable haircuts?
  * Are JGBs with a 3 year remaining maturity eligible? If so, what are applicable haircuts?
  * Is GBP (cash) eligible? If so, what are applicable haircuts?
 
The function `CheckEligibilityByDetails`, when presented with the 2 sets sets of input information, will check to determine
which collateral meets the eligibility and can be used/posted for delivery, and will return the breakdown
`CheckEligibilityResult` as output including information such as haircuts.

### EligibilityQuery

The data type `EligibilityQuery` is used to form the input data to the Eligibility Collateral Validation function.  

``` Haskell
type EligibilityQuery: 
    maturity number (1..1)
    collateralAssetType AssetType (1..1) 
    assetCountryOfOrigin ISOCountryCodeEnum (1..1)
    denominatedCurrency CurrencyCodeEnum (1..1)
    agencyRating AgencyRatingCriteria (1..1) 
    issuerType CollateralIssuerType (1..1) 
    issuerName LegalEntity (1..1) 
```
The data type should be populated with data to describe the collateral that is being validated, as follows:

* `maturity`:  the number of remaining years until maturity, as a number
* `collateralAssetType`:  the type of collateral using the `AssetType` data type and related enumerators
* `assetCountryOfOrigin`:  specified using the ISO Country Code
* `denominatedCurrency`:  specified using the Currency Code enumerator
* `agencyRating`:  the rating assigned to the asset by an agency, using the `AgencyRatingCriteria` data type
* `issuerType`:  the type of entity that issued the asset, using the `CollateralIssuerType` data type
* `issuerName`:  the name or identifier of the issuer of the asset, using the `LegalEntity` data type.

All attributes in the query must be populated and all are of single cardinality.

### CheckEligibilityByDetails

The function `CheckEligibilityByDetails` performs the actual validation based on the description of the available
collateral (specified in the aforementioned `EligibilityQuery`) and an eligibility collateral schedule (defined as
an `EligbilityCollateralSpecification`).  

The required inputs are one single `EligbilityCollateralSpecification` and one single `EligibilityQuery`.

The output is a single eligibility result, using the `CheckEligibilityResult` data type, described next.

In practical terms, the function can be integrated into systems and applications using the CDM code generators
to create executable software in one of many software languages, typically Java.  For testing and demononstration
purposes, the input data can be built manually using the [FINOS CDM Object Builder](https://cdm-object-builder.finos.org/)
and loaded and run using the Function evaluation feature in the [Rosetta Engine](https://ui.rosetta-technology.io/#/login) product.

### CheckEligibilityResult

The output of the function is delivered using the `CheckEligibilityResult` data type which has four attributes:

``` Haskell
type CheckEligibilityResult:
    isEligible boolean (1..1)
    matchingEligibleCriteria EligibleCollateralCriteria (0..*) 
    eligibilityQuery EligibilityQuery (1..1) 
    specification EligibleCollateralSpecification (1..1)
```

* `isEligible`: a simple boolean which is set to true if the asset described in the `EligibilityQuery` input is
  eligible.
* `matchingEligibleCriteria`: if there was a match, this will be the one or more criteria that were supplied in the
  `EligbilityCollateralSpecification` which matched with the query input.
* `eligibilityQuery`: a copy of the input query that was checked against the eligible collateral specification.
* `specification`: a copy of the input `EligbilityCollateralSpecification` that was checked against the query.

### Example Eligibility Check

Let's take an example eligible collateral schedule that accepts government bonds with outstanding
maturity of more than one year.  This can be coded into an `EligibilityCollateralSpecification`, as the
first parameter of the validation function; here illustrated as JSON:

* `EligibilityCollateralSpecification`
``` Javascript
{ "criteria": [ {
      "asset": [ {
            "collateralAssetType": [ {
              "assetType": "SECURITY"
              "securityType": "DEBT"
          } ],
          "maturityRange": {
            "lowerBound": {
              "period": {
                "period": "Y",
                "periodMultiplier": 1
      } } } } ],
      "issuer": [ {
          "issuerType": [ {
              "issuerType": "SOVEREIGN_CENTRAL_BANK"
} ] } ] } ] }
```
We can then run eligibility tests against this, for example:

1. Is US dollar cash accepted as collateral for this schedule? 
2. Are JGBs with a 3-year remaining maturity eligible? If so, what are applicable haircuts?

Showing this as JSON code, the first `EligibilityQuery` would be:

``` Javascript
{   "query": {
	"collateralAssetType": [ {
		"assetType": "Cash"
		} ] ,
	"assetCountryOfOrigin": "US" ,
	"demoninatedCurrency": "USD"
}  }
```
Running this code through the `EligibilityQuery` function will generate a result of `False`
in the `isEligible` attribute.

For the second example, the query can be constructed as follows:

``` Javascript
{   "query": {
	"maturity": 3,
	"collateralAssetType": [ {
		"assetType": "Security",
		"securityType": "Bond"
		} ] ,
	"assetCountryOfOrigin": "JP" ,
	"demoninatedCurrency": "JPY",
	"agencyRating": {
		"qualifier" : "All", 
		"creditNotation" : [ { 
			"agency": "StandardAndPoors",
 			"notation": "AA"
		} ]
	} ,
	"issuerType": "SOVEREIGN_CENTRAL_BANK" ,
	"issuerName": "Government of Japan"
}  }
```
The above will generate a result of `True` in the `isEligible` attribute.  To determine the
applicable haircut, interogate the returned `CheckEligibilityResult` data type, and specifically
`matchingEligibleCriteria` -> `treatment` -> `valuationTreatment` -> `haircutPercentage`.

### CheckEligibilityForProduct

The Function `CheckEligibilityForProduct`, which takes a specific `Product` as the input and validates 
its eligibility, has been defined conceptually but not fully implemented.
