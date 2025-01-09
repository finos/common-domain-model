
# CDM Version 6.0
CDM 6.0, a production release, is the culmination of Common Domain Model (CDM) development releases from the second and third quarter of 2024 since CDM was made an open-source project at FINOS. There are also several technical changes since version 5.0 related to bug fixes, dependencies and synonym mappings.

**What is being released**
Below are some of the high-level contributions that CDM 6.0 includes, more detail and additional contribution release notes can be found in the releases section of the CDM documentation:

- CDM Distribution - Python Code Generation
- Infrastructure
  - Dependency updates

- Event & Product Model
  - Qualification and Cardinality Fixes
- Initial Margin Model
  - Enhancement and Optimization of the Standardized Schedule Method
- Product Model
  - Commodity Payout Underlier
  - Cashflow Generation for Settlement Payout 
  - Refactor ETD Product Qualification
  - Underlier in Corporate Action
  - Add Price to Payouts
  - Asset Refactoring: Product, SettlementPayout, Underliers
  - Quantity Change For Existing Trade Lot
  - Trigger type refactoring
  - Modification of AmericanExercise Condition in ExerciseTerms
  - Qualification of 
    - Total Return Swaps (TRS) with a Debt Underlier
    - Foreign Exchange NDS
    - AssetClass
  - Synonym mappings for BusinessCenterEnum
  - Bond Option and Forward Qualification 
  - FpML Mapping Update
  - FpML Mapping
    - Commodity Swaps
    - Contractual Party
    - Commodity Forwards
    - Bond Forwards
  - Commodity Physical Options
  - Qualifying functions: Enhanced support for ETDs
  - Commodity Forwards
  - ISO Country Code Enum Update
  - Qualification - Bond Forwards
- CDM Model
  - Collateral Criteria AND/OR Logic
  - TaxonomySourceEnum
  - CapacityUnit Enum
  - Modification to product condition

- Mapping Update
  - Related party role mapper
  - InterestRateForwardDebtPriceMappingProcessor updated to handle 'Percentage' quoteUnits
  - Settlement Type Mapping Fix
  - Commodity Classification Coverage
- Legal Documentation Model - 
  - New `ContractualDefinitionsEnum` value to support the 2022 ISDA Verified Carbon Credit Transactions Definitions
- Implementation of the Standardized Schedule Method for Initial Margin Calculation in CDM
- Legal Agreements - Master Agreement Type enumeration - ISDAIIFM_TMA code
- Eligible Collateral Representation
  - New Attributes
  - CreditNotationMismatchResolutionEnum update
- Eligible Collateral Schedule Model - 
  - Determination of the Party Roles
  - CheckEligibilityResult cardinality fix

- Addition of new enumeration to AvailableInventory
# CDM Model - Date Time Functions

- Event Model 
  - PartyRoleEnum including PTRRServiceProvider role
  - Trade Lot Identifier added to Execution Instruction
  - Valuation Update

- FpML 5.13 Working Draft 3 Mapping Updates
  - Post Trade Risk Reduction Mapping Update
- FpML Coding Schemes 2.16 Mapping Updates 
  - Floating Rate Index Mappings
  - 
# CDM Model - RoundToPrecision Function
This release updates the existing function `cdm.base.math.RoundToPrecision` to add a new boolean flag which specifies whether to remove trailing zeros.

```
func RoundToPrecision: <"Round a rate to the supplied precision, using the supplied rounding direction">
    inputs:
        value number (1..1) <"The original (unrounded) number.">
        precision int (1..1) <"The number of decimal digits of precision.">
        roundingMode RoundingDirectionEnum (1..1) <"The method of rounding (up/down/nearest).">
        removeTrailingZeros boolean (1..1) <"Flag to specify whether to remove trailing zeros.">
    output:
        roundedValue number (1..1) <"The value to the desired precision">
```
-
The following examples show the function behaviour:

- `RoundToPrecision(1023.123456789, 5, RoundingDirectionEnum -> NEAREST, false)` = 1023.12346
- `RoundToPrecision(1023.123456789, 5, RoundingDirectionEnum -> NEAREST, true)` = 1023.12346
- `RoundToPrecision(1023.12000, 5, RoundingDirectionEnum -> NEAREST, false)` = 1023.12000
- `RoundToPrecision(1023.12000, 5, RoundingDirectionEnum -> NEAREST, true)` = 1023.12
- `RoundToPrecision(1023, 5, RoundingDirectionEnum -> NEAREST, false)` = 1023.00000
- `RoundToPrecision(1023, 5, RoundingDirectionEnum -> NEAREST, true)` = 1023
- `RoundToPrecision(999999999, 4, RoundingDirectionEnum -> NEAREST, false)` = 999999999.0000
- `RoundToPrecision(999999999, 4, RoundingDirectionEnum -> NEAREST, true)` = 999999999

# CDM Model - RoundToSignificantFigures Function
This release creates the new function `cdm.base.math.RoundToSignificantFigures` to round to the significant number of decimal places.

```
func RoundToSignificantFigures: <"Round a number to the supplied significant figures, using the supplied rounding direction.">
    inputs:
        value number (1..1) <"The original (unrounded) number.">
        significantFigures int (1..1) <"The number of significant figures.">
        roundingMode RoundingDirectionEnum (1..1) <"The method of rounding (up/down/nearest).">
    output:
        roundedValue number (1..1) <"The value to the desired number of significant figures.">
        
    condition NonZeroSignificantFigures: <"The number of significant figures should be greater than zero.">
        significantFigures > 0
```

The following examples show the function behaviour:
- `RoundToSignificantFigures(1023.123456789, 5, RoundingDirectionEnum -> NEAREST)` = 1023.1
- `RoundToSignificantFigures(1023.123456789, 5, RoundingDirectionEnum -> UP)` = 1023.2
- `RoundToSignificantFigures(1023.123456789, 5, RoundingDirectionEnum -> DOWN)` = 1023.1
- `RoundToSignificantFigures(1023.123456789, 1, RoundingDirectionEnum -> NEAREST)` = 1000
- `RoundToSignificantFigures(1023.1, 7, RoundingDirectionEnum -> NEAREST)` = 1023.1


# Product Model - New Data Types
_What is being released?_

- A new enumerator added to support a new approach to identifiers for assets: `AssetIdTypeEnum`.
- New data types added to start the build out of the concept of "Asset" in the product model:
    - `AssetBase` as the base data type to specify common attributes for all Assets
    - `AssetIdentifier` a new data type to uniquely identify an asset, including using the `AssetIdTypeEnum`
    - `Cash` a new data type to represent an asset that is a monetary holding in a currency
    - `DigitalAsset` a new data type to represent an asset that exist only in digital form, eg Bitcoin or Ethereum
    - `ListedDerivative` a new data type to represent an asset that is a securitised derivative on another asset, such as a exchange traded future
- A new `func` namespace created `cdm.base.staticdata.asset.common`, containing three new functions to aid the use of the new `Cash` asset:  `AssetIdentifierByType`, `GetCashCurrency`, `SetCashCurrency`.


**Backward incompatible changes**

# *CDM Model - Equity Products*

_Background_

This release contains modifications required to accomodate Equity and Exotic Products under individual Asset Classes.

_What is being released?_

This release creates the following modifications:

**Qualification functions**
- Added a new qualification function for Equity Exotic Options: `Qualify_Equity_OtherOption`, that uses `nonStandardisedTerms` attribute to identify when an option is Exotic.
- For existing Equity Option qualifications functions, `nonStandardisedTerms` is negatively tested to prevent redundant qualification.

**Validation conditions**
- `InterestRatePayout`:
    - FpML conditions `FpML_ird_9` and `FpML_ird_29` are relaxed when `compoundingMethod` is `None` (instead of just when absent).
- `ExerciseTerms`:
    - Attribute `expirationTime` relaxed to be optional (previously mandatory).
    - Attribute `expirationTimeType` tightened to be mandatory (previously optional).
    - Addition of validation condition `ExpirationTimeChoice` to establish the correlation between `expirationTime` and `expirationTimeType`: `expirationTimeType` must be set to `SpecificTime` when `expirationTime` is specified (and conversely).

_Backward incompatible changes_

The `ExerciseTerms` validation change is backward incompatible and all affected samples have been updated to ensure that `expirationTimeType` is populated as `SpecificTime` when the `expirationTime` attribute is populated.

See for example: [`fpml-5-13 > fx-ex09-euro-opt`](https://github.com/finos/common-domain-model/blob/master/rosetta-source/src/main/resources/cdm-sample-files/fpml-5-13/products/fx-derivatives/fx-ex09-euro-opt.xml)

PR: [#3278](https://github.com/finos/common-domain-model/pull/3278).

# _Product Model_ - Asset Refactoring of FloatingRateIndex and InterestRateIndex

_Background_

The Asset Refactoring initiative (see [#2805](https://github.com/finos/common-domain-model/issue/2805)) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional financial products and markets. A proposal has been agreed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes a minor adjustment following three planned major tranches of work in CDM 6 to implement the refactored model.

_What is being released?_

The names of the two terms `FloatingRateIndex` and `InterestRateIndex` have been flipped to make the latter, `InterestRateIndex` to be the higher level term such that an interest rate index can be either a floating rate index or an inflation rate index.

Rationale:
- Consistent with the name `InterestRatePayout`, which operates on both floating rates and inflation rates.
- Consistent with the name `FloatingRateIndexEnum`.
- Consistent with how "floating rate option" or "FRO" is understood in other places in the model.

_Backward-incompatible changes_

This release contains changes that are not backward-compatible:
- Rename the data type `FloatingRateIndex` to be called `InterestRateIndex`.
- Update `InterestRateIndex` to be a choice data type with two attributes: `FloatingRateIndex` and `InflationIndex`.
- Update the attribute `rateOption` on the data type `FloatingRateBase` to be of type `InterestRateIndex` as the base class is used for both floating and inflation indices.
- In addition, the name swap has been implemented in the following types:
    - `PriceQuantity`
    - `IndexTransitionInstruction`
- and the following functions:
    - `FindMatchingIndexTransitionInstruction`
    - `Qualify_IndexTransition`
    - `UpdateIndexTransitionPriceAndRateOption`
    - `InterestRateObservableCondition`
    - `EvaluateCalculatedRate`
    - `IndexValueObservation`
    - `IndexValueObservationMultiple`
    - `GetFloatingRateProcessingType`
    - `Qualify_Transaction_OIS`
- and the following mappings:
    - `cdm.mapping.fpml.confirmation.tradestate:synonym`
    - `cdm.mapping.ore:synonym`
- The following two functions have been moved from the `cdm.observable.asset.fro` namespace to the `cdm.observable.asset.func` namespace as they no longer act on a `fro` ie floating rate index:
    - `IndexValueObservation`
    - `IndexValueObservationMultiple`.

PR: https://github.com/finos/common-domain-model/pull/3267

# _Product Model_ - Security Finance trade types

_Background_

The Asset Refactoring initiative (see [#2805](https://github.com/finos/common-domain-model/issue/2805)) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional financial products and markets. A proposal has been agreed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes two minor adjustments following three planned major tranches of work in CDM 6 to implement the refactored model.

_What is being released?_

An optional attribute exists on the `AssetPayout` data type to uniquely identify the different types of securities financing trade types, such as a repurchase transaction or a buy/sell-back.  The name of this attribute has been updated to have a broader potential scope and not limit just to repos.  Specifically, the attribute `repoType` has been renamed to `tradeType` and its data type has been renamed from `RepoTypeEnum` to `AssetPayoutTradeTypeEnum`.  The values of the enumerator have not been changed.

The FINOS CDM documentation for the securities lending use case has been corrected; it was not correctly showing the remodelled use of AssetPayout that was implemented as a result of the Asset Refactor Taskforce.

_Backward-incompatible changes_

This release contains changes that are not backward-compatible:
- Rename the `repoType` to `tradeType` on `AssetPayout`.
- Rename `RepoTypeEnum` to `AssetPayoutTradeTypeEnum`.
- The two product qualification functions have been updated to use the new names:
    - `Qualify_RepurchaseAgreement`
    - `Qualify_buySellBack`.

PR: [#3270](https://github.com/finos/common-domain-model/pull/3270)

# _Product Model - Settlement Payout Price_

_What is being released?_

This release updates the FpML synonyms to map the price to the `SettlementPayout->priceQuantity->priceSchedule` attribute for FX samples.

_Backwards incompatible changes_

This release removes attributes `SettlementPayoutt->fixedPrice` and `OptionPayout->fixedPrice` as they are duplicates of the existing attributes `SettlementPayout->priceQuantity->priceSchedule` and `OptionPayout->priceQuantity->priceSchedule`.

_Review directions_

In Rosetta, select the Textual Browser and inspect FpML mapping changes in namespace `cdm.mapping.fpml.confirmation.tradestate`.

In Rosetta, select the Ingest tab and review the following FpML samples:

- fx-ex01-fx-spot.xml
- fx-ex02-spot-cross-w-side-rates.xml
- fx-ex03-fx-fwd.xml
- fx-ex05-fx-fwd-w-ssi.xml
- fx-ex07-non-deliverable-forward.xml
- fx-ex08-fx-swap.xml
- fx-ex26-fxswap-multiple-USIs.xml
- fx-ex28-non-deliverable-w-disruption.xml
- fx-ex29-fx-swap-with-multiple-identifiers.xml

In Rosetta, select the Visualisation tab and review the `Repo And Bond > Bond Execution` example:

PR: [#3250](https://github.com/finos/common-domain-model/pull/3250)


# _Product Model - Asset Refactoring in AssetCriteria_

_Background_

The Asset Refactoring initiative (see https://github.com/finos/common-domain-model/issues/2805) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional financial products and markets.  A proposal has been agreed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes some additional functionality (following three planned major tranches of work in CDM 6 to implement the refactored model).

_What is being released?_

AssetCriteria:
- The attribute `assetIdentifier` has been refactored to model an actual asset, specified using the `Asset` choice data type, rather than just an identifier. The attribute name has also been updated to `specificAssets` to make it clear that it is a list of specific assets, all of whom are eligible to be pledged as collateral.  The condition on the data type has been updated too.

ListingType:
- The cardinality of the three attributes in the data type `ListingType` (which is used in `AssetCriteria`) has been changed to none-to-many (rather than none or one); the attributes are `exchange`, `sector`, `index`. Without this, it would be only possible to select one of the values.

_Backward-incompatible changes_

This release contains changes that are not backward-compatible:
- All references to the attribute `assetIdentifier` on `AssetCriteria` need to be updated as referenced; the new attribute is `asset`.

- PR: [#3228](https://github.com/finos/common-domain-model/pull/3228)

# _Product Model - Asset Refactoring: Payout as a Choice_

_Background_

The Asset Refactoring initiative (see https://github.com/finos/common-domain-model/issues/2805) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional  financial products and markets.  A proposal has been agreed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes some additional functionality (following three planned major tranches of work in CDM 6 to implement the refactored model).

_What is being released?_

Payout:
- The `Payout` data type has been refactored as a `Choice`.  `Choice` data types work slightly different from the regular `one-of` condition because they force each of the members of the choice to have a single cardinality.  Therefore, the use of `Payout`, for example on `EconomicTerms` and `ResetInstruction`, now have multiple cardinality.

Product Qualification:
- Some minor changes have been made to the product qualification functions to ensure that the functionality and logic is unaffected by this change.

Documentation updates:
- The CDM documentation on the FINOS website has been updated.


_Backward-incompatible changes_

This release contains changes that are not backward-compatible:
- All references to a payout need to be updated as references to a payout are now treated as capitalised Data Types rather than lower case Attributes.  For example, a previous reference might have read:  `payout -> interestRatePayout -> floatingAmount` must now be written as:  `payout -> InterestRatePayout -> floatingAmount`.
- Logic or mapping that expects certain cardinality may need to be reviewed; see the explanation above.

PR: [#3178](https://github.com/finos/common-domain-model/pull/3178)
# _Product Model - Asset Refactoring: Basket, Index, Observable, Foreign Exchange_

_Background_

The Asset Refactoring initiative (see https://github.com/finos/common-domain-model/issues/2805) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional  financial products and markets.  A proposal is being developed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes the second tranche of changes (of three planned tranches in CDM 6) to implement the refactored model. It introduces some new data types and makes changes to others; more significant refactoring ofthe Product structure will be introduced in the third release.

_What is being released?_

The word "update" below is to refer to a change to the modelling between this release and the previous release of Asset Refactoring changes (in CDM 6.0-dev.58).

Updates to `AssetBase` data type:
- The `identifier` attribute has been made mandatory.
- The attributes `isExchangeListed`, `exchange` and `relatedExchange` have been moved from `InstrumentBase` to `AssetBase`.
- The type of the `exchange` and `relatedExchange` attributes has been updated from `string` to `LegalEntity`.

Updates to `Cash` data type:
- New conditions have been added to ensure that `taxonomy` and `exchange` are not used for this asset type.

Changes to `Commodity` data type:
- Now extends from `AssetBase` not `ProductBase`.
- Accordingly, `productTaxonomy` has been replaced by `taxonomy` and the conditions updated.

Changes to `Index` and `IndexBase` data types:
- The attributes `exchange` and `relatedExchange` have been removed from `IndexBase` as they will now be picked up from `AssetBase`.
- `Index` has been refactored from a `one-of` condition to a choice data type.
- The data type `FloatingIndex` has been renamed `FloatingRateIndex`.
- The documentation in the model on the index-related data types has been improved.
- The qualification logic has been updated to reflect the new modelling of `Index` as an underlier on the relevant functions.

Updates to `ListedDerivative` data type:
- Updates have been made to the attribute names and types introduced in the previous release to improve modelling and composition.
- The condition on `VarianceReturnTerms` has been updated to reflect the new position of `ListedDerivative` in the product model.

Changes to `Security` data type:
- Now extends from `InstrumentBase` not `ProductBase`.
- Temporary changes made to add `economicTerms` and `productTaxonomy` pending further refactoring in the third phase.

Support for FX Observables:
- The data type `ForeignExchangeRate` has been created and added as a choice to `Index`; it also extends `IndexBase`.
- This new data type contains the same attributes as the existing `FXRateObservable` which has been deprecated.
- The `ForeignExchange` data type has been deprecated and the deprecated `ExchangeRate` and `CrossRate` datas type have both been deleted.

Refactoring of `Observable`:
- The following data types have been added to `Observable` as new attributes: `Asset`, `Basket`, `Index`.
- The following data types have been removed from `Observable`:  `Commodity` (now available as an `Asset`); `QuotedCurrencyPair` (replaced by the the FX observable data type inside `Index`).
- The unused attribute `optionReferenceType` and its corresponding enumerator `OptionReferenceTypeEnum` have been removed from the model.
- `Observable` now has a `one-of` condition (and will be updated to `choice` in the third phase).
- The two attributes `pricingTime` and `pricingTimeType` on `ObservationTerms` have been renamed `observationTime` and `observationTimeType` respectively.

Changes to `BasketConstituent`:
- `BasketConstituent` now extends from `Observable`, not `Product`.
- Moved from the product namespace to the observable namespace.
- The qualification logic has been updated to reflect the new modelling of `BasketConstituent` as an underlier on the relevant functions.

Updates to `TransferableProduct`:
- The inheritance on this data type has been updated so that it now extends `Asset` but the attributes are unchanged.

Updates to Payouts:
- The documentation in the model on the payout-related data types has been improved.
- The `SettlementCommitment` data type and attribute has been renamed `SettlementPayout`.

_Backward-incompatible changes_

This release contains changes that are not backward-compatible:
- Changes to the following data types are particularly impactful and have required updates to the mapping synonyms and samples:
    - Commodity
    - Index
    - QuotedCurrencyPair
    - FXRateObservable.

A full description of the backward-incompatible changes, and how persisted objects should be remapped, will be included in the release notes for the last tranche of the asset refactoring.

PR: [#3044](https://github.com/finos/common-domain-model/pull/3044)

# _Product Model - Asset Refactoring: Asset, Index, Identifier_

_Background_

The Asset Refactoring initiative (see https://github.com/finos/common-domain-model/issues/2805) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional  financial products and markets.  A proposal is being developed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes the first tranche of changes to implement the refactored model (the first of three planned tranches in CDM 6).  It introduces some new data types and makes minor changes to others without significant impact to the Product structure itself.

_What is being released?_

New `Asset` data type:
- Introduce the new data type `Asset` which is defined as "something that can be owned and transferred in the financial markets". The data type is implemented using the new Rune DSL feature `choice` that is available in [Release 9.10](https://github.com/finos/rune-dsl/releases/tag/9.10.0).
- Introduce the additional Asset data sub-type called `Instrument`, also using `choice`, defined as "a type of Asset that is issued by one party to one or more others".
- Create the new base class `InstrumentBase` to model common attributes across all `Instrument` data types.
- Introduce the new enumerator `AssetIdTypeEnum`, to define certain identifier sources unique to Assets, as an extension of `ProductIdTypeEnum`.
- Change the inheritance from `ProductBase` to `InstrumentBase` for `Loan`, `ListedDerivative`.
- Add a reference on `Observable` to an `Asset` using an `AssetIdentifier`.

Changes to `Transfer`:
- As `Asset` is defined as something that can be transferred, the modelling of `Transfer` has been refactored to act upon `Asset` rather than `Observable` with a change to `TransferBase`.
- This also results in changes to the `Qualify_SecurityTransfer` function.

Product Model:
- Introduce a new data type on `Payout`: `SettlementCommitment` which models the settlement of an `Asset` for cash.
- Introduce `TransferableProduct` as a type of Product which can be used in a `SettlementCommitment` for a basic cash settled trade of either an `Asset` with or without the addition of specific `EconomicTerms`.
- Define the new `SettlementCommitment` data type to model this new kind of `Payout`.


_Backward-incompatible changes_

This release contains changes that are not backward-compatible:
- The change in the inheritance for `Loan` and `ListedDerivative` impacts the use of identifiers in these data types.
- The refactoring of `Transfer` to act upon an `Asset` rather than `Observable` impacts the use of the related functions.

Samples and mappings for both have been updated accordingly.

A full description of the backward-incompatible changes, and how persisted objects should be remapped, will be included in the release notes for the last tranche of the asset refactoring.

PR: [#3022](https://github.com/finos/common-domain-model/pull/3022)

# Product Model - Portfolio Return Terms

_What is being released?_

- removed [deprecated] attributes below from type **PriceReturnTerms** :
    - **valuationPriceInitial**
    - and **valuationPriceFinal**
    - and **finalValuationPrice**
- added attributes below to type **PerformancePayout** : that is core release to Dev of the same components previously released in Prod today :
    - creation of new type **PorfolioReturnTerms** which extends **ReturnTerms**
        - with existing types **PayerReceiver**
        - also with existing type **PriceSchedule** to **PerformancePayout** used under three attribute names, and **NonNegativeQuantitySchedule** used for one, respectively : **initialValuationPrice**, **interimValuationPrice**, **finalValuationPrice** and **quantity**
    - added the above new type to **PerformancePayout**
    - added existing type **PriceSchedule** to **PerformancePayout** used under three attribute names : **initialValuationPrice**, **interimValuationPrice** and **finalValuationPrice** (that is to replace [**deprecated**] ones removed from **ReturnTerms**)
    - updated type Basket with below changes :
        - removed temporary **PorfolioBasketConsituent** of type **BasketConstituent**
        - removed as well [deprecated] **basketConstituent** of type **Product**
        - added instead new attribute **basketConstituent** of type **BasketConstituent**
    - renamed attributes below in **ValuationDates** :
        - **initialValuationDate** (instead of **valuationDatesInitial**)
        - **interimValuationDate** (instead of **valuationDatesInterim**)
        - **finalValuationDate** (instead of **valuationDatesFinal**)

_Backward compatibility_

As an information, this release contains two sets of changes with no backward compabiltity :

- removing [deprecated] components mentioned above
- renaming of attributes in ValuationDates mentioned above

PR #2974

# Reference Data - Update ISOCurrencyCodeEnum
Updated `ISOCurrencyCodeEnum` based on updated scheme ISO Standard 4217.

Version updates include:
- added removed values: `MWK`, `PEN`, `RON`, `SZL`, `TRY`

_Backward Incompatible Changes_

As this release removes multiple enum values, it will not be backwards compatible.

PR #3017

# *Product Model - Remove AssetPool and deprecated data types*

_Background_

The Asset Refactoring initiative (see https://github.com/finos/common-domain-model/issues/2805) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional  financial products and markets.  A proposal is being developed - through a cross-industry Task Force - to implement this remodelling in the CDM.  Prior to that, this preparatory PR proposes to remove the `AssetPool` data type, which has been found to be both unused and incorrect, and to remove some additional data types in the Product Model which were previously deprecated.  This approach should provide a cleaner implementation path for the remodelling which will be put forward in a subsequent PR.

_What is being released?_

- Remove the `AssetPool` data type which was previously introduced from FpML but has been found to be incorrect and unusable.

- Remove the following deprecated data types used in the Product Model:
    - `Bond`
    - `ConvertibleBond`
    - `Equity`
    - `IdentifiedProduct`
    - `ObservationSource`
    - `SecurityPayout`.
- Remove the following deprecated data types that are related to the deprecated `SecurityPayout`:
    - `SecurityLeg`
    - `InitialMargin`
    - `InitialMarginCalculation`
    - `SecurityValuation`
    - `SecurityValuationModel`
    - `BondValudationModel`
    - `BondPriceAndYieldModel`
    - `CleanOrDirtyPrice`
    - `CleanPrice`
    - `RelativePrice`
    - `BondEquityModel`
    - `BondChoiceModel`
    - `UnitContractValuationModel`.
- Remove the reference to `SecurityPayout` from `Payout`.
- Remove the reference to `AssetPool` from `Product`.
- Remove functions which act upon `SecurityPayout`.
- Remove mapping synonyms from FpML for `AssetPool` and `SecurityPayout`.
- Update the CDM documentation to ensure it remains aligned with the implementation. This includes some changes to the hierarchy of the documentation to improve readability when displayed using Docusaurus.

Further details on the rationale for the change and the impact on the model can be found in Issue #[2966](https://github.com/finos/common-domain-model/issues/2966).

_Backward Incompatible Changes_

As this release removes multiple attributes and product types, it will not be backwards compatible.

_Sample Impact_

The existing [Fixed Rate Repo sample](https://github.com/finos/common-domain-model/tree/master/rosetta-source/src/main/resources/cdm-sample-files/functions/business-event/execution) was using the `SecurityPayout` construct.  It is believed that this was based on some mapping of FpML files which did not represent
real business cases.  Futhermore, according to ICMA, FpML is not widely used for repo transactions.  Therefore this erroneous sample has been removed from the
FINOS CDM distribution.

There is no impact to samples from removing `AssetPool` or any of the other changes listed above.
PR #2964.

# _Product Model - OptionPayout Refactoring_

_Background_

In order to reduce redundancy and overcomplexity in the CDM, a refactoring of the `OptionPayout` structure is required. The information contained in the fields inside the `optionStyle` (`americanExercise`, `europeanExercise`, and `bermudaExercise`) can be unified under a new type `ExerciseTerms`. This will reduce the redundancy of having the same information repeated under the three styles and improve the simplicity of the model. To distinguish whether the option is American, European, or Bermuda, a new `style` enumeration is added to the model inside `ExerciseTerms`, with the values `American`, `European`, and `Bermuda`. Additionally, the `strike` attribute, previously under `exerciseTerms`, is moved outside and located directly under `OptionPayout`, given that it does not convey any information about the exercise terms of an option.

_What is being released?_

- The `OptionStyle` type is removed from the model, along with the `AmericanExercise`, `EuropeanExercise`, and `BermudaExercise` types that are encapsulated in it.
- A new `ExerciseTerms` structure is added to `OptionPayout`, containing all _distinct_ fields found previously under the three exercise styles.
- A new `style` enumeration is added under `ExerciseTerms` to distinguish the style of the option. This enumeration is made **optional** to account for the exercise terms of a `CancelableProvision`, `ExtendibleProvision`, or `OptionalEarlyTermination`, where the style is derived from the structure itself.
- The `CancelableProvision`, `ExtendibleProvision`, and `OptionalEarlyTermination` structures have been modified to use the new `ExerciseTerms` type instead of the old `AmericanExercise`, `EuropeanExercise`, and `BermudaExercise` types.
- **Synonym mappings** have been modified to reflect these changes.

_Data types_

- Removed `OptionExercise` type.
- Removed `OptionStyle` type.
- Removed `AmericanExercise`, `EuropeanExercise`, and `BermudaExercise` types.
- Added new `OptionExerciseStyleEnum` enumeration with values `American`, `European` and `Bermuda`.
- Added new `ExerciseTerms` type, containing:
    - all of the distinct fields present before in `AmericanExercise`, `EuropeanExercise`, and `BermudaExercise` types,
    - a `style` attribute of type `OptionExerciseStyleEnum`,
    - and the `exerciseProcedure` attribute of type `ExerciseProcedure` that was previously contained in `OptionExercise`.
- Switched `exerciseTerms` attribute in `OptionPayout` type to use the new `ExerciseTerms` type instead of the removed `OptionExercise` type.
- Moved the `strike` attribute previously contained in `OptionExercise` type to `OptionPayout` type.
- Removed `americanExercise`, `europeanExercise`, and `bermudaExercise` attributes from `CancelableProvision`, `ExtendibleProvision`, and `OptionalEarlyTermination` types
- Replaced by a single `exerciseTerms` attribute of type `ExerciseTerms` instead.

_Enumerations_

- Added new `OptionExerciseStyleEnum` enumeration, with values `American`, `European`, and `Bermuda`.

_Backward Incompatible Changes_

- The type `OptionExercise` is removed from the model and replaced by the `ExerciseTerms` type. This new type is used instead for the `exerciseTerms` attribute in `OptionPayout`.
- The `OptionStyle` type is removed from the model along with the three option exercise types contained inside it: `AmericanExercise`, `EuropeanExercise`, `BermudaExercise`.
- Instead in `ExerciseTerms`, it is replaced by the distinct group of attributes required to represent any type of option style, plus the `exerciseProcedure` attribute previously in `OptionExercise`.
- The `style` enumeration is incorporated into the `ExerciseTerms` type to differentiate between American, European, and Bermuda styles.
- Finally, the `strike`, previously under `OptionStyle`, has been moved outside of `ExerciseTerms` and is located directly under `OptionPayout`.

_Sample Impact_

There are many samples impacted by this change, namely all the samples utilizing the `OptionPayout` structure. The impact on the three following samples (one European, one American, one Bermuda) is shown to visualize how the refactoring of the `OptionPayout` affects the structure of the CDM trades:

- `eqd ex04 european call index long form`: the `OptionStyle` has been removed in favor of the the `style` = "European", and the relevant fields previously under `europeanExercise` (`expirationDate` and `expirationTimeType`). Additionally, the `strike` is moved from inside `exerciseTerms` to outside.

From:
```
"optionPayout": [
    {
        ...
        "exerciseTerms": {
            "optionStyle": {
                "europeanExercise": {
                    "expirationDate": [
                        {
                            "adjustableDate": {
                                "unadjustedDate": "2004-12-19",
                                "dateAdjustments": {
                                    "businessDayConvention": "NONE"
                                }
                            }
                        }
                    ],
                    "expirationTimeType": "OSP"
                }
            },
            "strike": {
                "strikePrice": {
                    "value": 8700,
                    "unit": {
                        "currency": {"value": "CHF"}
                    },
                    "perUnitOf": {"financialUnit": "IndexUnit"},
                    "priceType": "AssetPrice"
                }
            },
            "exerciseProcedure": {
                "automaticExercise": {"isApplicable": true}
            }
        },
        ...
    }
]
```

To this:
```
"optionPayout": [
    {
        ...
        "exerciseTerms": {
            "style": "European",
            "expirationDate": [
                {
                    "adjustableDate": {
                        "unadjustedDate": "2004-12-19",
                        "dateAdjustments": {
                            "businessDayConvention": "NONE"
                        }
                    }
                }
            ],
            "expirationTimeType": "OSP",
            "exerciseProcedure": {
                "automaticExercise": {"isApplicable": true}
            }
        },
        "strike": {
            "strikePrice": {
                "value": 8700,
                "unit": {
                    "currency": {"value": "CHF"}
                },
                "perUnitOf": {"financialUnit": "IndexUnit"},
                "priceType": "AssetPrice"
            }
        }
    }
]
```

- `eqd ex01 american call stock long form`: the `OptionStyle` has been removed in favor of the the `style` = "American", and the relevant fields previously under `americanExercise` (`commencementDate`, `expirationDate`, `latestExerciseTime`, `expirationTimeType`, and `multipleExercise`). Additionally, the `strike` is moved from inside `exerciseTerms` to outside.

From:
```
"optionPayout": [
    {
        ...
        "exerciseTerms": {
            "optionStyle": {
                "americanExercise": {
                    "commencementDate": {
                        "adjustableDate": {
                            "unadjustedDate": "2001-07-13",
                            "dateAdjustments": {
                                "businessDayConvention": "NONE"
                            }
                        }
                    },
                    "expirationDate": {
                        "adjustableDate": {
                            "unadjustedDate": "2005-09-27",
                            "dateAdjustments": {
                                "businessDayConvention": "NONE"
                            }
                        }
                    },
                    "latestExerciseTime": {
                        "hourMinuteTime": "17:15:00",
                        "businessCenter": {"value": "GBLO"}
                    },
                    "expirationTimeType": "Close",
                    "multipleExercise": {
                        "integralMultipleAmount": 1,
                        "minimumNumberOfOptions": 1,
                        "maximumNumberOfOptions": 150000
                    }
                }
            },
            "strike": {
                "strikePrice": {
                    "value": 32.00,
                    "unit": {
                        "currency": {"value": "EUR"}
                    },
                    "perUnitOf": {"financialUnit": "Share"},
                    "priceType": "AssetPrice"
                }
            },
            "exerciseProcedure": {
                "automaticExercise": {"isApplicable": true}
            }
        }
        ...
    }
]
```

To this:
```
"optionPayout": [
    {
        ...
        "exerciseTerms": {
            "style": "American",
            "commencementDate": {
                "adjustableDate": {
                    "unadjustedDate": "2001-07-13",
                    "dateAdjustments": {
                        "businessDayConvention": "NONE"
                    }
                }
            },
            "expirationDate": [
                {
                    "adjustableDate": {
                        "unadjustedDate": "2005-09-27",
                        "dateAdjustments": {
                            "businessDayConvention": "NONE"
                        }
                    }
                }
            ],
            "latestExerciseTime": {
                "hourMinuteTime": "17:15:00",
                "businessCenter": {"value": "GBLO"}
            },
            "expirationTimeType": "Close",
            "multipleExercise": {
                "integralMultipleAmount": 1,
                "minimumNumberOfOptions": 1,
                "maximumNumberOfOptions": 150000
            },
            "exerciseProcedure": {
                "automaticExercise": {"isApplicable": true}
            }
        },
        "strike": {
            "strikePrice": {
                "value": 32.00,
                "unit": {
                    "currency": {"value": "EUR"}
                },
                "perUnitOf": {"financialUnit": "Share"},
                "priceType": "AssetPrice"
            }
        }
    }
]
```

- `ird ex14 berm swaption`: the `OptionStyle` has been removed in favor of the the `style` = "Bermuda", and the relevant fields previously under `bermudaExercise` (`bermudaExerciseDates`, `relevantUnderlyingDate`, `earliestExerciseTime`, and `expirationTime`).

From:
```
"optionPayout": [
    {        
        ...    
        "exerciseTerms": {
            "optionStyle": {
                "bermudaExercise": {
                    "bermudaExerciseDates": {
                        "adjustableDates": {
                            "unadjustedDate": [
                                "2000-12-28",
                                "2001-04-28",
                                "2001-08-28"
                            ],
                            "dateAdjustments": {
                                "businessDayConvention": "FOLLOWING",
                                "businessCenters": {
                                    "businessCenter": [
                                        {"value": "EUTA"},
                                        {"value": "GBLO"}
                                    ]
                                }
                            }
                        }
                    },
                    "relevantUnderlyingDate": {
                        "relativeDates": {
                            "periodMultiplier": 2,
                            "period": "D",
                            "dayType": "Business",
                            "businessDayConvention": "NONE",
                            "businessCenters": {
                                "businessCenter": [
                                    {"value": "EUTA"},
                                    {"value": "GBLO"}
                                ]
                            },
                            "dateRelativeTo": {
                                "globalReference": "32e3a858",
                                "externalReference": "bermudaExercise0"
                            }
                        }
                    },
                    "earliestExerciseTime": {
                        "hourMinuteTime": "09:00:00",
                        "businessCenter": {"value": "BEBR"}
                    },
                    "expirationTime": {
                        "hourMinuteTime": "11:00:00",
                        "businessCenter": {"value": "BEBR"}
                    },
                    "meta": {
                        "globalKey": "32e3a858",
                        "externalKey": "bermudaExercise0"
                    }
                }
            },
            "exerciseProcedure": {
                "manualExercise": {
                    "exerciseNotice": {
                        "exerciseNoticeGiver": "Seller",
                        "businessCenter": {"value": "GBLO"}
                    }
                },
                "followUpConfirmation": true
            }
        }
        ...        
    }
]
```

To this:
```
"optionPayout": [
    {
        ...
        "exerciseTerms": {
            "style": "Bermuda",
            "exerciseDates": {
                "adjustableDates": {
                    "unadjustedDate": [
                        "2000-12-28",
                        "2001-04-28",
                        "2001-08-28"
                    ],
                    "dateAdjustments": {
                        "businessDayConvention": "FOLLOWING",
                        "businessCenters": {
                            "businessCenter": [
                                {"value": "EUTA"},
                                {"value": "GBLO"}
                            ]
                        }
                    }
                }
            },
            "relevantUnderlyingDate": {
                "relativeDates": {
                    "periodMultiplier": 2,
                    "period": "D",
                    "dayType": "Business",
                    "businessDayConvention": "NONE",
                    "businessCenters": {
                        "businessCenter": [
                            {"value": "EUTA"},
                            {"value": "GBLO"}
                        ]
                    },
                    "dateRelativeTo": {
                        "globalReference": "5c12e2ce",
                        "externalReference": "bermudaExercise0"
                    }
                }
            },
            "earliestExerciseTime": {
                "hourMinuteTime": "09:00:00",
                "businessCenter": {"value": "BEBR"}
            },
            "expirationTime": {
                "hourMinuteTime": "11:00:00",
                "businessCenter": {"value": "BEBR"}
            },
            "exerciseProcedure": {
                "manualExercise": {
                    "exerciseNotice": {
                        "exerciseNoticeGiver": "Seller",
                        "businessCenter": {"value": "GBLO"}
                    }
                },
                "followUpConfirmation": true
            },
            "meta": {
                "globalKey": "5c12e2ce",
                "externalKey": "bermudaExercise0"
            }
        }
    }
]
```

PR: #2716

# _Product Model - Day Count Fraction: RBA_Bond_Basis_

_Background_

The codes `RBA_BOND_BASIS_QUARTER`, `RBA_BOND_BASIS_SEMI_ANNUAL` and `RBA_BOND_BASIS_ANNUAL` in the CDM enum `DayCountFractionEnum` have been found redundant by definition. The solution to this issue is to merge them into one single code: `RBA_BOND_BASIS`. This also aligns with the FpML representation.

_What is being released?_

- Replaced the codes `RBA_BOND_BASIS_QUARTER`, `RBA_BOND_BASIS_SEMI_ANNUAL` and `RBA_BOND_BASIS_ANNUAL` with the code `RBA_BOND_BASIS` in the CDM enum `DayCountFractionEnum`.
- Mapping added to populate the new code with the FpML code `RBA`.

_Backward incompatible changes_

This release contains backward-incomplatible changes. Anywhere the codes `RBA_BOND_BASIS_QUARTER`, `RBA_BOND_BASIS_SEMI_ANNUAL` or `RBA_BOND_BASIS_ANNUAL` are found, this code must be replaced by the new one `RBA_BOND_BASIS`.

PR: #2727

# _Product model - Natural Person and NaturalPersonRole circular reference_

_Background_

An issue regarding a circular reference inside the `NaturalPerson` type was recently found in the model.
`NaturalPerson` and `NaturalPersonRole` are located at the same level inside `Party`, to follow the same structure that `Party` and `PartyRole` have inside the `Trade` type. The circular reference issue appears because the `NaturalPerson` type also contains a `NaturalPersonRole`, which references back to the containing type of `NaturalPerson`, causing a circular reference in the model.

_What is being released?_

This release fixes this issue by removing the `NaturalPersonRole` inside the `NaturalPerson` type.

_Backward-Incompatible Changes_

- Removed the `personRole` attribute of type `NaturalPersonRole` from `NaturalPerson`.

PR: #2546