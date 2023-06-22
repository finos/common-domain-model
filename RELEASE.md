# *CDM Version 4.0*

CDM 4.0, a production release, is the culmination of CDM development releases from the first half of 2023 since CDM was made an open-source project at FINOS. This release sees the contribution of the Repo model and separation of legal content in CDM which was completed in development in preparation for open sourcing now released in production. Notable other additions to the CDM project are a new Python Distribution of CDM and associated code generator, enhancements to the product qualifications and eligible collateral specification model. There are also several technical changes since version 3.0 related to bug fixed, dependencies and synonym mappings.

Below are some of the high-level contributions that CDM 4.0 includes, more detail and additional contribution release notes can be found in the releases section of the CDM documentation:
 - CDM Distribution - Python Code Generation
 - CDM Distribution - Java artifacts published to Maven Central
 - Infrastructure - Dependency Updates   
 - Product Model - Currency Enums (automated generation of ISO 4217 enum lists)
 - Product Model – Qualification (composable derivatives product qualification based on ISDA taxonomy v2)
 - Product Model - Inflation Swaps - calculationMethod and calculationStyle  (added to CDM)
 - Change of name of a root data type EligibleCollateralSchedule to a new name EligibleCollateralSpecification to indicate more general utility of model
 - Legal Documentation & Collateral – Eligible Collateral Schedule Builder Function 
 - Workflow Model - Representation of approvals for a Workflowstep  (added to CDM)

----------

Below is the version Minesh created.

# *CDM 4.0*

## Product Model

### Model Changes

  - Currency Enums
    - Added process to automate the generation of ISO 4217 Currencies. The updated currency enum values can be found in ISOCurrencyCodeEnum
  - Inflation Swaps - calculationMethod and calculationStyle
    - Introduced concepts for calculationMethod  and calculationStyle  to InflationRateSpecification and added enum InflationCalculationMethodEnum 
  - Bond Reference - Coupon Rate
    - This release adds the field couponRate to the BondReference type under InterestRatePayout.
  - Business centers - Enhancement for business day calendar location
    - The definition of the enumerated list BusinessCenterEnum was previously conflating 2 separate concepts: the business centre calendar defined per geo-location and the business centre calendar defined by reference to a commodity exchange or as specified in the ISDA definitions.
  - Observation Dates
    - This release revises the representation of a custom Observation schedule to improve representation in the model.
  - Orphan Types clean-up
    - This release relocates and deletes some unused types in the model and adjusts the corresponding FpML synonym mappings.
  - Party Identifier Type
    - This release updates the enumeration PartyIdentifierTypeEnum and adds the corresponding FpML synonym mappings.
  - Performance Payout - Valuation dates
    - This release completes the representation of the valuation dates for a performance payout with the specification of the initial dates, that was previously missing.
  - Qualification Functions - Bug Fixes
    - This release fixes a defect related to the following function Qualify_SubProduct_FixedFloat.
  - Package Price and Spread
    - Coverage for package price was introduced in FpML 5.13. This release allows for the mapping of package prices and spreads, and refactors BusinessEvent and EventInstruction to improve consistency.
  - Equity Swap - Valuation Dates Initial
    - This release updates and extends valuation dates mapping coverage for Equity Swap products.
  - Day Count Fractions
    - This release updates the enumeration list DayCountFractionEnum and associated functions to reference the ISDA 2021 Definitions.
  - Simple Payment
    - Data type SimplePayment has been removed from the CDM as it replicates content contained in Transfer. Attribute initialFee of data type CancelableProvision has been updated to be of type Transfer.

### Qualification

  - ISDA taxonomy v2
    - This release completes the coverage of the first level of composable product qualification based on ISDA taxonomy v2.
  - Implementation examples for Qualification
    - Examples of Qualification Logic have been included in the Java distribution to illustrate usage and inform implementation.
   
### Collateral

  - Eligible Collateral Schedule – Data Type Name Change
    - The change of name of a root data type EligibleCollateralSchedule to a new name EligibleCollateralSpecification and where else this is referenced in the CDM. 
  - Core CDM Contribution to FINOS - Repositioning of Collateral & Removal of ISDA Legal Documentation components in Preparation for Open Sourcing
    - As part of the CDM transition to the Finance Open Source Foundation (FINOS), a new "Core CDM" has been constructed and will be transferred to FINOS.

### FpML
  - FpML 5.13 Synonym Source
    - Infrastructure changes required to work with FpML 5.13 Synonym Source.
  - FpML Scheme - Enumeration Update
    - FpML version 5.13 contains scheme updates. The CDM Enumerations have been re-synced with the FpML source in order to contain the latest information.
  - FpML Scheme - Enumeration Referencing
    - InflationRateIndexEnum is now linked to the FpML Coding Scheme through use of the docReference functionality. The contents of the enumeration list will now be automatically kept in line with the latest FpML scheme information.
  - Enum and FpML coding scheme update
    - Rosetta has a feature for its enumerations that enables, if a certain enumeration is directly related to an FpML coding scheme, to label that enumeration with the corresponding coding scheme canonical URI, so every time that coding scheme is updated, the enumeration will be automatically updated (and so will its mappings). That has been done for CreditEventTypeEnum and InformationProviderEnum. For CapacityUnitEnum, since it is not uniquely related to an FpML coding scheme, the changes have been done manually.
  - Simplify FpML Synonym Hierarchy
    - This release will simplify and rationalise the FpML synonym hierarchy and versions. This will make it easier for contributors working on synonyms to map FpML documents into CDM objects.
  - Synonym mappings of underlier for Equity Option Baskets
    - An issue was identified with the underlier mapping from FpML to CDM for Equity Option Basket products. The mapping from CDM was not generating an underlier and its corresponding baskets for these products and therefore some samples were not accurately qualified.
  - Synonym mappings for CreditSeniorityEnum
    - This release updates and extends the FpML mapping coverage for the product model.
  - Synonym mappings for Commodity products
    - This release updates and extends the FpML mapping coverage for commodity products.
  - Synonym mappings for price and quantity
    - This release updates and extends the FpML mapping coverage for the product model.
  - Synonym mappings for party information
    - This release updates and extends the FpML mapping coverage for party representation.
  - Synonym mappings for credit products
    - This release updates and extends the FpML mapping coverage for the product model.
  - Synonym mappings for credit products
    - This release updates and extends the FpML mapping coverage for the product model.
  - Synonym mappings - Bond Reference for Interest Rate Payout
    - Synonym mapping has been added to populate the bondReference attribute in data type InterestRatePayout so that the reference to a bond underlier and the applicability of the Precedent bond condition. The latter denotes that the contract is only valid if the bond is issued and that if there is any dispute over the terms of the fixed stream then the bond terms will be used.
  - Synonym Mappings for Credit Default Swaps
    - This release updates and extends the FpML mapping coverage for Credit Default Swap products.


	

## Event Model

  - Event Timestamp Qualification
    - The release contains the addition of an enum value clearingReceiptDateTime to enum EventTimestampQualificationEnum for the purpose of qualifying an event time stamp indicating when a cleared trade was received by the clearing body.


## Workflow Model

  - Representation of approvals for a Workflowstep
    - As part of the work done by the ISLA Trading Working Group some enhancements have been made to the WorkflowStep event model and associated functions.
      
## ICMA Contribution

  - Replaced SecurityFinancePayout with AssetPayout and Removal of Additional Deprecated Components
    - As part of ICMA's previous contribution to the CDM for Repo and Bonds (reference release 4.0.0-dev 19), several model components that have been ear-marked as deprecated need to be removed. Those components are now superseded by new components from that contribution.
  - CDM for Repo and Bonds
    - This release covers two phases of the ICMA CDM for Repo and Bonds Initiative. Phase 1 covered CDM design of a classic repurchase agreements with a fixed term and fixed rate, and events for purchase and repurchase. Phase 2 extended the CDM product model and event model to cover open term and floating rate repurchase agreements, and the associated lifecycle events.


## Infrastructure

### Rosetta DSL Updates

  - `7.10.0`: Java code-generation update to allow the default condition implementation to be overridden (via Google Guice) by implementors, similar to how function implementations can be overridden.
  - `7.9.3`: Made keyword then mandatory to avoid ambiguity. Improved consistency of using square brackets.
  - `7.8.0`: Logging implementations removed from classpath to allow Java users of the CDM to select their own logging implementation.
  - `7.7.3`: Validation checks concerning type format (e.g., the number of characters in a string, the number of digits of a number) will now be supported by the DSL.
  - `7.5.4`: Fixed DSL issue related to typeAlias.
  - `7.5.0`: Support for ISO data types .
  - `7.5.2`: Import model from xsd updated to support different documentation tags.
  - `7.5.3`: Additional changes make ISO data types backwards compatible.
  - `7.4.0`: Rosetta models generated from an xsd.
  - `7.3.1`: Fix Java code-gen bug related to extracting date from zonedDateTime record type.
  - `7.3.0`: Add support for external rule reference.
  - `7.2.1`: Code-gen generated Java that does not contain keyword clashes.
  - `7.1.1`: Bug fixes, a simplified dependency structure, and security updates.
    
### Security Update

  - Upgrade per Common Vulnerabilities and Exposures (CVE) Standards
    - A part of the CDM Contribution to FINOS, all third party dependencies have been verified and updated when necessary according the FINOS requirements for Common   -   -   -   - Vulnerabilities and Exposures
    - Third party software libraries updated to comply with the “Common Vulnerabilities and Exposures” standard (CVE, https://www.cve.org/).

### Code Distribution

 - Python Code Generation
   - This release introduces Python code generation functionality.


