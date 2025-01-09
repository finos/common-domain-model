# Infrastructure - Dependency Update

What is being released?

This release updates the rune dependencies.

Version updates include:

- DSL 9.25.0: improve type errors and cardinality errors. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.25.0
- DSL 9.24.0: add a feature to override attributes in extended types. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.24.0
- DSL 9.22.0: handle null for min and max operations. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.22.0
- FpML Coding Scheme 11.25.1: support for latest version (v2.20).
- Updates the rune dependencies to version 11.24.2. This update includes support for visualising the Choice Type elements in the Rosetta User Interface.
- DSL 9.20.0: support for passing metadata to functions and highlighting fixes. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.20.0
- DSL 9.19.0: support for switch operation on choice types. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.19.0
- DSL 9.18.0: new syntax features. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.18.0
- DSL 9.18.1: memory improvements. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.18.1
- DSL 9.16.2: support for ingestion tabulators. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.16.2
- DSL 9.16.1: support for ingestion tabulators. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.16.1
- ingest-test-framework 11.17.1: Add support for address/location references to be used on nested model types.
- DSL 9.15.0: patch for supporting tabulation of types with circular dependencies. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.15.0
- DSL 9.15.1: patch for missing generated Java files. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.15.1
- DSL 9.15.2: patch for missing Java meta classes. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.15.2
- DSL 9.14.1: Support for defining metadata annotations on choice options. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.14.1
- DSL 9.14.0: Support for accessing meta features after a deep feature call. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.14.0
- DSL 9.12.0: this release fixes an issue where the only exists operator behaved unexpectedly when subtyping was involved. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.12.0.
- DSL 9.12.1: this patch fixes null pointers in the Java runtime of the only exists operator. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.12.1.
- DSL 9.12.2: this patch fixes a code generation bug in the Java generator. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.12.2.
- DSL 9.12.3: this patch fixes an issue where the code generator could freeze Rosetta. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.12.3.
- DSL 9.12.4: this patch fixed an issue with only exists on multi-cardinality inputs. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.12.4.
- Jackson 2.17.1: this release updates the library used to serialise/deserialise JSON.
- ingest-test-framework 11.10.3: Translate bug fix for long XML files
- DSL 9.11.2: Fix syntax validation issue. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.11.2
- rosetta-bundle 11.6.0: Dependencies migrated to Maven Central
- rosetta-bundle 11.6.2: FpML coding scheme infrastructure update to support configurable coding scheme matching for Prod and Dev versions
- rosetta-bundle 11.7.0: Java compilation performance improvements
- DSL 9.8.5: Java compilation performance improvements. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.8.5
- rosetta-bundle 10.17.1:
    - FpML coding scheme updated to version 2.19. FloatingRateIndexEnum updated to match coding scheme.
    - Bug fix for zonedDateTime serialisation issue #2895.
- rosetta-bundle 10.16.0: FpML Coding schema updated.
- rosetta-dsl 9.8.0: this release features three new operations - to-date, to-date-time and to-zoned-date-time - to convert a string into a date, dateTime or zonedDateTime respectively. It also adds support to convert these three types into a string using the to-string operation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.8.0.
- rosetta-bundle 10.15.7: Translate bug fix to handle enum name clashes.
- rosetta-dsl 9.7.0: DSL validation and performance enhancements. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.7.0.
- rosetta-bundle 10.13.4: FpML Coding schema updated.
- rosetta-dsl 9.6.1: DSL bug fix for handing null values. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.6.1.
- rosetta-dsl 9.5.0: Adds support for tabulating projection data. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.5.0.
- rosetta-dsl 9.6.0: DSL build and compile performance improvements. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.6.0.
- rosetta-bundle 10.12.0: Adds JSON schema code generator.
- rosetta-bundle 10.9.3: this release adds mapping support for XSD substitution groups, which fixes the issue related to the mapping of FpML oilPhysicalLeg xml elements.
- rosetta-bundle 10.0.0: Ingestion performance improvements related to the loading of xml schema files
- rosetta-dsl 9.2.0: this release moves deployment of DSL artifacts to Maven Central. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.2.0.
- rosetta-dsl 9.3.0: this release contains syntax highlighting improvements. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.3.0.
- rosetta-dsl 9.1.2: this release fixes DSL issues #670 and #653. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.1.2.
- rosetta-dsl 9.1.3: this release fixes an issue related to the generated Java process method. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.1.3.

# *Event & Product Model Qualification and Cardinality Fixes*

_Background_

Following a recent DSL update (see [DSL release notes](https://github.com/finos/rune-dsl/releases/tag/9.25.0)) which adds additional logical checks related to cardinality, some errors were found in the model.
These errors stem from an ambiguity of how to handle multiple elements in certain situations.

In general, the solution is to follow one of two approaches:
- Do we expect only a single item to be present? Then use the `only-element` operator.
- Should we support multiple elements? Then use the `extract` operation to handle all of them, and reduce them
  to a single result according to the context, e.g., using the `all = True` operation.

_What is being released?_

This release includes fixes for all cardinality-related errors detected by the DSL, which are listed below.
It also includes a related fix to the `Qualify_CashAndSecurityTransfer` function, which is described in more detail below the list.

1. The function `SecurityFinanceCashSettlementAmount` contained a multiplication of which one operand - `securityQuantity` -
   was of multi cardinality. In practice, due to filtering, it should always be a single element, so this was fixed with `only-element`.
2. The function `ResolveSecurityFinanceBillingAmount` had a similar problem as in (1).
3. The function `Qualify_Repurchase` was performing an `only exists` operation on multiple `primitiveInstruction`s at
   once. Since a former check verified there is only one, this was fixed with `only-element`.
4. The function `Qualify_Shaping` had a similar problem as in (3).
5. The function `Qualify_PartialDelivery` was comparing two multi cardinality quantities. In practice, due to filtering,
   both should always be a single element, so this was fixed with `only-element`.
6. The function `Qualify_OnDemandPayment` had a similar problem as in (3).
7. The condition `SettlementPayout` of the type `Trade` was performing an `only exists` operation on multiple `payout`s
   at once. This was fixed by calling the existing function `SettlementPayoutOnlyExists` instead, which handles multiple
   payouts.
8. The function `Qualify_CashTransfer` was performing an `only exists` operation on multiple `primitiveInstruction`s at
   once. To resolve the ambiguity, the check is now performed on all `primitiveInstruction`s separately using `extract`.
9. The function `Qualify_OpenOfferClearedTrade` was performing an `only exists` operation on two `primitiveInstruction`s at
   once. The check has been replaced with two equivalent `exists` operations, one for each of the two `primitiveInstruction`s.
10. The function `Qualify_Renegotiation` had a similar problem as in (8).
11. The function `Qualify_SecuritySettlement` was performing an `only exists` operation on an unsupported input, which
    always resulted in `False`. This was fixed by replacing it with an `exists` check instead.
12. The function `Qualify_ValuationUpdate` was performing an `only exists` operation on multiple `primitiveInstruction`s at
    once. Given that the specification requires only a single component to be present, this was fixed with `only-element`.
13. The function `CheckAgencyRating` was performing a `contains` operation on two operands of single cardinality.
    The operation has been replaced with an equality check `=` instead.
14. The function `CheckAssetType` had a similar problem as in (13).
15. The function `Qualify_EquityOption_PriceReturnBasicPerformance_SingleName` was performing an `only exists` operation on multiple `payout`s
    at once. Since a former check verified there is only one, this was fixed with `only-element`.
16. The function `Qualify_EquityOption_PriceReturnBasicPerformance_Index` had a similar problem as in (15).
17. The function `Qualify_EquityOption_PriceReturnBasicPerformance_Basket` had a similar problem as in (15).
18. The function `Qualify_ForeignExchange_VanillaOption` had a similar problem as in (15).
19. The condition `Basket` of the type `SettlementPayout` was performing an `only exists` operation on multiple `basketConstituent`s at
    once. To resolve the ambiguity, the check is now performed on all `basketConstituent`s separately using `extract`.

Due to the bug fix in the function `Qualify_SecuritySettlement`, another bug in the function `Qualify_CashAndSecurityTransfer`
came to light. According to its specification, a business event should only be qualified as a `CashAndSecurityTransfer`
only if the cash and security move in the same direction - however, this was never actually checked. The check has been implemented
and three expectation files have been updated accordingly.

# Initial Margin Model - Enhancement and Optimization of the Standardized Schedule Method
_Background_

Following the initial contribution of the Standardized Schedule Method for calculating Initial Margin (IM) within the Common Domain Model (CDM) and after receiving feedback from the working group, further work has been carried out to enhance the model by introducing new functionalities.

_What is being released?_

In this second contribution, improvements have been made to the model, categorized into three main areas: the creation of conditions, code optimization, and cosmetic changes.

Key components of this release include:

New conditions have been added to validate the outputs of functions, ensuring they make sense from a business perspective.
Code optimization has been implemented to reduce redundancy by avoiding repetitive use of qualifying functions within data extraction functions, resulting in improved efficiency.
The name of one function has been updated, and some definitions have been expanded for better user understanding.
_Conditions_

Added new PositiveNotional post-condition:
Ensure the notional is greater than 0.
Added new ValidCurrency post-condition:
Ensure the currency is a valid ISO 3-Letter Currency Code.
Added new PositiveDuration post-condition:
Ensure the duration is greater than 0.
Added new PositiveGrossInitialMargin post-condition:
Ensure the gross initial margin is greater than 0.
Added new NonNegativeNetInitialMargin post-condition:
Ensure net initial margin is non-negative.
Added new TotalGIMAddition post-condition:
Ensure that only a single currency exists.
Added new NGRAddition post-condition:
Ensure that only a single currency exists.
_Types_

Modification to the StandardizedSchedule type
The following conditions have been added: PositiveNotional , ValidCurrency , and PositiveDuration .
Modification to the StandardizedScheduleTradeInfo type
The attributes grossInitialMargin and markToMarketValue, which were previously of type Quantity, are now of type Money. Additionally, the conditions PositiveGrossInitialMargin and SameCurrency have been included.
Modification to the StandardizedScheduleInitialMargin type
The condition NonNegativeNetInitialMargin has been added.
_Functions_

Modification to the BuildStandardizedSchedule function
Aliases for productClass and assetClass have been introduced to serve as temporary variable assignments.
Modification to the StandardizedScheduleNotional function
The qualifying functions have been substituted with the newly created aliases.
Modification to the StandardizedScheduleNotionalCurrency function
The qualifying functions have been substituted with the newly created aliases.
Modification to the StandardizedScheduleDuration function
The qualifying functions have been substituted with the newly created aliases.
_Rename_

GetStandardizedScheduleMarginRate is now used instead of GetIMRequirement.
_Backward Incompatible_

The following changes are backward incompatible:

All the function condition additions specified in the Conditions section of these release notes.
All the type modifications specified in the Types section of these release notes.
The changes can also be reviewed in PR: #3305.

# Product Model - Commodity Payout Underlier
_Background_

The Asset Refactoring initiative (see #2805) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional financial products and markets. A proposal has been agreed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes an adjustment following three planned major tranches of work in CDM 6 to implement the refactored model.

_What is being released?_

In the original Asset Refactoring scope, the underlier on CommodityPayout was changed from being type Product to type Commodity.

This has proven to be too restrictive in DRR, where Commodity Payout can operate on a basket or index. Therefore, the data type of the underlier has been updated to Underlier with the added benefit of making it consistent with the other payouts.

To ensure that the underlier is indeed commodity-related, conditions have been added to force the underlier attribute to reference a commodity-related underlier. The CommodityUnderlier condition uses a switch statement to evaluate whether the underlier is an Observable or Product, and to assess it accordingly. A new function ObservableIsCommodity is used to standardise this and handles the different choice types of observables and the potential recursive nature of baskets.

_Backward-incompatible changes_

The change to the data type of the underlier attribute is not backward-compatible.

# CDM Model - Collateral Criteria AND/OR Logic
_Background_

This release enhances the modelling of Eligible Collateral Criteria to enable the use of complex AND, OR and NOT logic in the combination of terms within a criteria.

Eligible Collateral is currently modelled using the data type EligibleCollateralSpecification which can contain many EligibleCollateralCriteria, which are themselves constructed from CollateralTreatment, IssuerCriteria and AssetCriteria.
The attributes isIncluded (true/false) and qualifier (all/any) can be used to model some simple cases of and / or logic in the construction of certain parts of the criteria (eg AgencyRatingCriteria).

Members of the CDM Collateral Working Group have requested that the functionality is extended to enable more complex combinations of AND and OR logic across multiple terms. This has been implemented by combining all the criteria terms in a single new data type CollateralCriteria. This is a choice data type that includes all the criteria terms that previously appeared in AssetCriteria and IssuerCriteria. As each term can only occur once, in its simplest form, only one criteria can be specified. If more than one criteria is required, it is necessary to specify whether the terms are all required or only that any of them are required. The two new data types AllCriteria and AnyCriteria also appear on CollateralCriteria to enable this. If all terms are required, ie AND logic, then the terms should be linked in a parent AllCriteria. If any of the terms are required, ie OR logic, then the terms should be linked in a parent AnyCriteria. These two terms can be used iteratively to create complex logic between terms. Additionally, the data type NegativeCriteria can also be used in the logic to apply a NOT function to a single term.

_What is being released?_

This release implements AND, OR and NOT logic between the Collateral terms.

There is no longer a separate data type for each of asset and issuer criteria; they have been combined in a single new data type called CollateralCriteria.

The new choice data type CollateralCriteria replaces the removed AssetCriteria and IssuerCriteria data types as the combined type.
The attributes issuer and asset on CollateralCriteriaBase have now been replaced with the single one collateralCriteria which is the specific criteria that applies. It can be created using AND, OR and NOT logic, and both asset and issuer characteristics.
The conditions on CollateralCriteriaBase have been updated and now use the new CriteriaMatchesAssetType function.
The data type ConcentrationLimit has been refactored to reduce the cardinality of concentrationLimitCriteria to 1 and the condition is updated accordingly.
The condition on ConcentrationLimitCriteria has been updated to reflect the combined CollateralCriteria.
Three new logic data types have been introduced to support the AND, OR and NOT logic of terms and are used in CollateralCriteria:
AllCriteria
AnyCriteria
NegativeCriteria,
The following new data types have been introduced and are used in CollateralCriteria:
IssuerCountryOfOrigin
AssetCountryOfOrigin
IssuerName
IssuerAgencyRating
SovereignAgencyRating
AssetAgencyRating
AssetMaturity
ListingExchange
ListingSector
DomesticCurrencyIssued.
Changes to remove the old model:

The data types AssetCriteria and IssuerCriteria have been removed.
The qualifier attribute has been removed from AgencyRatingCriteria as it is now redundant.
The data type ListingType has been removed.
In addition, the following functions have also been updated to reflect the new modelling:

CheckEligibilityByDetails which now references a new function CheckCriteria which takes a single criteria and evaluates it against the criteria. This function handles the recursive use of AND and OR logic.
CheckAssetCountryOfOrigin has been made more generic as the country of origin can apply to both an Issuer and an Asset; it has been renamed to CheckCountryOfOrigin.
CheckMaturity has been updated to reflect the new modelling of AssetMaturity.
CriteriaMatchesAssetType has been implemented to enable the enforcement of conditions on AssetType to be evaluated correctly.
Previously, the function Create_EligibleCollateralSpecificationFromInstruction enabled some limited automation of the creation of multiple Criteria with slightly differing logic. As this function no longer aligns to the new design, it has been deleted. The following new functions have been added in its place:

CloneEligibleCollateralWithChangedTreatment: Creates a new Eligible Collateral Specification based on an input specification but with one changed criteria with a changed treatment.
CreateAndCriteria: Combines multiple CollateralCriteria together using AND logic.
CreateOrCriteria: Combines multiple CollateralCriteria together using OR logic.
Some changes have been made to the Java supporting code behind this functionality; see the changes in the following files:

rosetta-source/src/test/java/org/isda/cdm/functions/FunctionInputCreationTest.java
rosetta-source/src/main/java/org/finos/cdm/CdmRuntimeModule.java
rosetta-source/src/main/java/cdm/product/collateral/functions/MergeEligibleCollateralCriteriaImpl.java.
Finally, the CDM documentation has been updated to reflect all the above changes.

_Backward incompatible changes_

Most of these changes are not backward compatible as they remove or significantly alter data types and attributes.

The expectations have been regenerated. In addition, the test files used for the Check Eligibility and Merge Critera functions have also been updated.

# Product Model - Cashflow Generation for Settlement Payout
_Background_

This release contains changes required to enable the generation of cashflows from a simple settlement payout. This change allows FX transactions, which are now represented using a settlement payout, still to be viewed as two legs for downstream reporting purposes in DRR.

Further _Background_ is detailed in Issue: #3269

_What is being released?_

This release includes core modifications to the Cashflow type, so that it can be generated by a settlement payout and used to represent the two legs of an FX transaction. Because a settlement payout can take any sort of asset as underlyer, the generated cashflows can in fact represent the transfers of any asset and not just cash as in an FX transaction.

Changes are therefore brought to the Cashflow type to accomodate any type of asset. Because that type becomes closely aligned functionally to the Transfer type, the two types have been harmonised to extend a common ancestor: AssetFlowBase, which defines the what (asset), how much (quantity > 0) and when (settlement date) of an asset transfer. The "AssetFlowBase" name is generalising the term "cash" in "cashflow" to any "asset", whereas the Cashflow type retains its original name to minimise downstream impact.

A new function: Create_CashflowFromSettlementPayout, takes a single SettlementPayout and returns the two Cashflow implied by this payout. The two cashflow legs are called assetLeg and priceLeg, respectively. The function works generically regardless of the type of asset used as underlyer in the SettlementPayout and assetLeg. The priceLeg always consists of cash, whose amount and currency are implied by the price and quantity parameters of the settlement payout.

Finally, the Cashflow choice is being removed from Payout as it is no longer used there.

Accordingly, this release includes the following changes:

Types and attributes

New type:

AssetFlowBase, in product.common.settlement, that provides the common ancestor to harmonise Cashflow and Transfer
Its attributes are aligned onto the former TransferBase ones and are all mandatory:
asset as Asset
quantity as NonNegativeQuantity
settlementDate as AdjustableOrAdjustedOrRelativeDate
Modification to existing types:

Both Cashflow (previously extending PayoutBase) and Transfer (previously extending TransferBase) extend AssetFlowBase
Transfer is unchanged as a result of this change, i.e. it has the same attributes as before
Cashflow no longer has the following irrelevant attributes previously inherited from PayoutBase:
principalPayment
settlementTerms, replaced by settlementDate only
priceQuantity, replaced by asset and quantity only
Deletions:

TransferBase has been deleted
Cashflow is no longer a choice of Payout
Validation rules

EconomicTerms: Rules that rely on the presence of Cashflow for certain CDS transactions are no longer relevant. In cases where the fee leg includes a stand-alone cashflow payment, it is already represented by a Transfer in the transaction event.
FpML_cd_26_28
FpML_cd_27
Functions

New function:

Create_CashflowFromSettlementPayout
Modification to existing function:

Create_OnDemandInterestPaymentPrimitiveInstruction has been modified to include a transfer instead of a termsChange primitive (the latter previously featured an additional Cashflow in the payout).
Deletions:

Create_CashflowTermsChangeInstruction: no longer used
Create_Cashflow: no longer used
_Backward incompatible changes_

Most changes in this release are backward-incompatible, with some types and functions being deleted or their attributes modified.

The most consequential change is to the Cashflow type and the fact that it has been removed from the Payout choice. However, no sample illustrates this change because practically this component was no longer being used as a payout.

Transfer remains unchanged.

# CDM Model - Equity Products
_Background_

This release contains modifications required to accomodate Equity and Exotic Products under individual Asset Classes.

_What is being released?_

This release creates the following modifications:

Qualification functions

Added a new qualification function for Equity Exotic Options: Qualify_Equity_OtherOption, that uses nonStandardisedTerms attribute to identify when an option is Exotic.
For existing Equity Option qualifications functions, nonStandardisedTerms is negatively tested to prevent redundant qualification.
Validation conditions

InterestRatePayout:
FpML conditions FpML_ird_9 and FpML_ird_29 are relaxed when compoundingMethod is None (instead of just when absent).
ExerciseTerms:
Attribute expirationTime relaxed to be optional (previously mandatory).
Attribute expirationTimeType tightened to be mandatory (previously optional).
Addition of validation condition ExpirationTimeChoice to establish the correlation between expirationTime and expirationTimeType: expirationTimeType must be set to SpecificTime when expirationTime is specified (and conversely).
Backward incompatible changes

The ExerciseTerms validation change is backward incompatible and all affected samples have been updated to ensure that expirationTimeType is populated as SpecificTime when the expirationTime attribute is populated.

See for example: fpml-5-13 > fx-ex09-euro-opt

# Product Model - Asset Refactoring of FloatingRateIndex and InterestRateIndex
_Background_

The Asset Refactoring initiative (see #2805) is seeking to improve the # Product Model to address some long-standing issues and to ensure the continued extensibility to additional financial products and markets. A proposal has been agreed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes a minor adjustment following three planned major tranches of work in CDM 6 to implement the refactored model.

_What is being released?_

The names of the two terms FloatingRateIndex and InterestRateIndex have been flipped to make the latter, InterestRateIndex to be the higher level term such that an interest rate index can be either a floating rate index or an inflation rate index.

Rationale:

Consistent with the name InterestRatePayout, which operates on both floating rates and inflation rates.
Consistent with the name FloatingRateIndexEnum.
Consistent with how "floating rate option" or "FRO" is understood in other places in the model.
_Backward-incompatible changes_

This release contains changes that are not backward-compatible:

Rename the data type FloatingRateIndex to be called InterestRateIndex.
Update InterestRateIndex to be a choice data type with two attributes: FloatingRateIndex and InflationIndex.
Update the attribute rateOption on the data type FloatingRateBase to be of type InterestRateIndex as the base class is used for both floating and inflation indices.
In addition, the name swap has been implemented in the following types:
PriceQuantity
IndexTransitionInstruction
and the following functions:
FindMatchingIndexTransitionInstruction
Qualify_IndexTransition
UpdateIndexTransitionPriceAndRateOption
InterestRateObservableCondition
EvaluateCalculatedRate
IndexValueObservation
IndexValueObservationMultiple
GetFloatingRateProcessingType
Qualify_Transaction_OIS
and the following mappings:
cdm.mapping.fpml.confirmation.tradestate:synonym
cdm.mapping.ore:synonym
The following two functions have been moved from the cdm.observable.asset.fro namespace to the cdm.observable.asset.func namespace as they no longer act on a fro ie floating rate index:
IndexValueObservation
IndexValueObservationMultiple.

# Product Model - Security Finance trade types
_Background_

The Asset Refactoring initiative (see #2805) is seeking to improve the # Product Model to address some long-standing issues and to ensure the continued extensibility to additional financial products and markets. A proposal has been agreed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes two minor adjustments following three planned major tranches of work in CDM 6 to implement the refactored model.

_What is being released?_

An optional attribute exists on the AssetPayout data type to uniquely identify the different types of securities financing trade types, such as a repurchase transaction or a buy/sell-back. The name of this attribute has been updated to have a broader potential scope and not limit just to repos. Specifically, the attribute repoType has been renamed to tradeType and its data type has been renamed from RepoTypeEnum to AssetPayoutTradeTypeEnum. The values of the enumerator have not been changed.

The FINOS CDM documentation for the securities lending use case has been corrected; it was not correctly showing the remodelled use of AssetPayout that was implemented as a result of the Asset Refactor Taskforce.

_Backward-incompatible changes_

This release contains changes that are not backward-compatible:

Rename the repoType to tradeType on AssetPayout.
Rename RepoTypeEnum to AssetPayoutTradeTypeEnum.
The two product qualification functions have been updated to use the new names:
Qualify_RepurchaseAgreement
Qualify_buySellBack.

# CDM Model - TaxonomySourceEnum
_Background_
A DRR issue has been identified where reporting the Underlying CO values was not supported for MAS. To address this, we proposed replicating the reporting logic used for BaseProduct and SubProduct in EMIR. In CDM, this involves adding "MAS" as a value to the TaxonomySourceEnum, since the TaxonomySource in CDM determines the jurisdiction based on the commodityClassificationScheme being used. So the "MAS" value will be added in the TaxonomySourceEnum.

_What is being released?_

Updated TaxonomySourceEnum in cdm.base.staticdata.asset.common:enum
Enumerations

Updated TaxonomySourceEnum by adding MAS to support Monetary Authority of Singapore (MAS) as a taxonomy source.

# Product Model - Settlement Payout Price
_What is being released?_

This release updates the FpML synonyms to map the price to the SettlementPayout->priceQuantity->priceSchedule attribute for FX samples.

Backwards incompatible changes

This release removes attributes SettlementPayoutt->fixedPrice and OptionPayout->fixedPrice as they are duplicates of the existing attributes SettlementPayout->priceQuantity->priceSchedule and OptionPayout->priceQuantity->priceSchedule.


# CDM Model - CapacityUnit Enum
_Background_
In has been seen that in the ExternalUnitOfMeasure1Code from the 2Q2024 ISO External CodeSets v1, the unity Joule is supported in the Enum. However, in CDM this is not the case, as it does not appear anywhere in the CapacityUnitEnum. Therefore, the Joule unit of measure will be added to the CapacityUnitEnum for completeness and to align with 2Q2024 ISO External CodeSets v1, for versions 5 and 6 of CDM.

_What is being released?_

Updated CapacityUnitEnum in cdm.base.math
Enumerations

Updated CapacityUnitEnum by adding 'J' to support Joule unit
The changes can be reviewed in PR: #3198

# Product Model - Asset Refactoring in AssetCriteria
_Background_

The Asset Refactoring initiative (see #2805) is seeking to improve the # Product Model to address some long-standing issues and to ensure the continued extensibility to additional financial products and markets. A proposal has been agreed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes some additional functionality (following three planned major tranches of work in CDM 6 to implement the refactored model).

_What is being released?_

AssetCriteria:

The attribute assetIdentifier has been refactored to model an actual asset, specified using the Asset choice data type, rather than just an identifier. The attribute name has also been updated to specificAssets to make it clear that it is a list of specific assets, all of whom are eligible to be pledged as collateral. The condition on the data type has been updated too.
ListingType:

The cardinality of the three attributes in the data type ListingType (which is used in AssetCriteria) has been changed to none-to-many (rather than none or one); the attributes are exchange, sector, index. Without this, it would be only possible to select one of the values.

_Backward-incompatible changes_

This release contains changes that are not backward-compatible:

All references to the attribute assetIdentifier on AssetCriteria need to be updated as referenced; the new attribute is asset.

# Mapping Update - Related party role mapper
_Background_

The Party Role mapping issue involved the incorrect transfer of FpML's relatedParty structure into CDM, particularly in cases where multiple relatedParty elements exist within the same partyTradeInformation block. The mapping process was only capturing the first relatedParty role found, which led to incorrect associations between party references and roles. Furthermore, if the role of the first relatedParty was not found in PartyRoleEnum, another role was incorrectly assigned, causing mismatches and inaccuracies in the data mapping.

_What is being released?_

We are introducing a new RelatedPartyRoleMappingProcessor that addresses the limitations of the previous implementation. This processor evaluates all relatedParty elements within a partyTradeInformation block instead of just mapping the first one. It ensures that each relatedParty is independently assessed, verifies its role against the PartyRoleEnum list, and assigns the correct role and reference accordingly. Additionally, if a role is not found in PartyRoleEnum, the processor omits that reference rather than assigning an incorrect role to the relatedParty.

# Mapping Update - InterestRateForwardDebtPriceMappingProcessor updated to handle 'Percentage' quoteUnits
_Background_

The price of bond forwards is captured as a monetary value whereas it should be a decimal/percentage. Even if the value in FpML was 'Percentage', the CDM representation value did not accurately represent this, causing misinterpretations.

_What is being released?_

An update to the InterestRateForwardDebtPriceMappingProcessor code to fix the described issue. This change, would correct the interpretation by dividing the current monetary value by 100, when the quoteUnits corresponds to the XML value 'Percentage'.
The bond-fwd-generic-ex01.xml and bond-fwd-generic-ex02.xml samples have been updated as the files were using the value 'Percent' but the correct value according to the enum should be 'Percentage'

# Product Model - Asset Refactoring: Payout as a Choice
_Background_

The Asset Refactoring initiative (see #2805) is seeking to improve the # Product Model to address some long-standing issues and to ensure the continued extensibility to additional financial products and markets. A proposal has been agreed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes some additional functionality (following three planned major tranches of work in CDM 6 to implement the refactored model).

_What is being released?_

Payout:

The Payout data type has been refactored as a Choice. Choice data types work slightly different from the regular one-of condition because they force each of the members of the choice to have a single cardinality. Therefore, the use of Payout, for example on EconomicTerms and ResetInstruction, now have multiple cardinality.
Product Qualification:

Some minor changes have been made to the product qualification functions to ensure that the functionality and logic is unaffected by this change.
Documentation updates:

The CDM documentation on the FINOS website has been updated.

_Backward-incompatible changes_

This release contains changes that are not backward-compatible:

All references to a payout need to be updated as references to a payout are now treated as capitalised Data Types rather than lower case Attributes. For example, a previous reference might have read: payout -> interestRatePayout -> floatingAmount must now be written as: payout -> InterestRatePayout -> floatingAmount.
Logic or mapping that expects certain cardinality may need to be reviewed; see the explanation above.

# Product Model - Refactor ETD Product Qualification
_Background_

In an earlier Asset Refactoring release, the modelling of Exchange Traded Derivatives was enhanced
by introducing a new item ListedDerivative as an option of one of the values in the Asset choice
data type. However, the product qualification functions were still expecting these products to be
modelled using the Security choice within Asset. This has been corrected.
The enumerator type SecurityTypeEnum has been renamed to InstrumentTypeEnum and the value of
ListedDerivative has been removed from the list. This broadens the potential use of this
enumeration for additional assets.

The attribute instrumentType, using the InstrumentTypeEnum data type, has been added to InstrumentBase
so that this basic type determination is on all types of instrument. The corresponding attribute,
securityType has been removed from Security to avoid duplication.

The attribute securityType on the data type AssetType has been changed to use the renamed data
type, ie InstrumentTypeEnum.

Occurrences of logic to test the type of a security or instrument
have also been updated to use this new name InstrumentTypeEnum, including a number of references
to this enumerator in the product qualification logic. Where product qualification was only
looking for the type of a security, it has also been broadened to instrument.

# Product Model - Underlier in Corporate Action
_Background_

In an earlier Asset Refactoring release, an unintending defect was introduced on the CorporateAction data type.
This release corrects that.

_What is being released?_

The data type of the underlier attribute within CorporateAction has been undated to be Underlier rather than
Instrument. Instrument is too restrictive as a broader range of assets can be the subject of corporate actions
and this is best represented by the Underlier data type.

# Product Model - Add Price to Payouts
_Background_

This is an additional enhancement following the Asset Refactoring initiative by improving the consistency
of the modelling of prices on payouts.

_What is being released?_

The new attribute fixedPrice of type PriceSchedule has been added to the OptionPayout and to the
SettlementPayout. Both additionally have a metadata address link, pointing to the PriceQuantity
in the TradeLot.

# CDM Model - RoundToSignificantFigures Function
_Background_

In an earlier release, function cdm.base.math.RoundToSignificantFigures was added, however it was missing configuration.

_What is being released?_

Added missing Guice configuration for Java function implementation for cdm.base.math.RoundToSignificantFigures.

# CDM Model - RoundToPrecision Function
_Background_

This release contains an enhancement for the RoundToPrecision function, as described in issue #2915.

_What is being released?_

This release updates the existing function cdm.base.math.RoundToPrecision to add a new boolean flag which specifies whether to remove trailing zeros.

func RoundToPrecision: <"Round a rate to the supplied precision, using the supplied rounding direction">
inputs:
value number (1..1) <"The original (unrounded) number.">
precision int (1..1) <"The number of decimal digits of precision.">
roundingMode RoundingDirectionEnum (1..1) <"The method of rounding (up/down/nearest).">
removeTrailingZeros boolean (1..1) <"Flag to specify whether to remove trailing zeros.">
output:
roundedValue number (1..1) <"The value to the desired precision">
The following examples show the function behaviour:

RoundToPrecision(1023.123456789, 5, RoundingDirectionEnum -> NEAREST, false) = 1023.12346
RoundToPrecision(1023.123456789, 5, RoundingDirectionEnum -> NEAREST, true) = 1023.12346
RoundToPrecision(1023.12000, 5, RoundingDirectionEnum -> NEAREST, false) = 1023.12000
RoundToPrecision(1023.12000, 5, RoundingDirectionEnum -> NEAREST, true) = 1023.12
RoundToPrecision(1023, 5, RoundingDirectionEnum -> NEAREST, false) = 1023.00000
RoundToPrecision(1023, 5, RoundingDirectionEnum -> NEAREST, true) = 1023
RoundToPrecision(999999999, 4, RoundingDirectionEnum -> NEAREST, false) = 999999999.0000
RoundToPrecision(999999999, 4, RoundingDirectionEnum -> NEAREST, true) = 999999999

# CDM Model - RoundToSignificantFigures Function
_Background_

This release contains a new function for RoundToSignificantFigures function, as described in issue #3154.

_What is being released?_

This release creates the new function cdm.base.math.RoundToSignificantFigures to round to the significant number of decimal places.

func RoundToSignificantFigures: <"Round a number to the supplied significant figures, using the supplied rounding direction.">
inputs:
value number (1..1) <"The original (unrounded) number.">
significantFigures int (1..1) <"The number of significant figures.">
roundingMode RoundingDirectionEnum (1..1) <"The method of rounding (up/down/nearest).">
output:
roundedValue number (1..1) <"The value to the desired number of significant figures.">

    condition NonZeroSignificantFigures: <"The number of significant figures should be greater than zero.">
        significantFigures > 0
The following examples show the function behaviour:

RoundToSignificantFigures(1023.123456789, 5, RoundingDirectionEnum -> NEAREST) = 1023.1
RoundToSignificantFigures(1023.123456789, 5, RoundingDirectionEnum -> UP) = 1023.2
RoundToSignificantFigures(1023.123456789, 5, RoundingDirectionEnum -> DOWN) = 1023.1
RoundToSignificantFigures(1023.123456789, 1, RoundingDirectionEnum -> NEAREST) = 1000
RoundToSignificantFigures(1023.1, 7, RoundingDirectionEnum -> NEAREST) = 1023.1
This is a new function, so there are no compatibility issues.

# Product Model - Asset Refactoring: Product, SettlementPayout, Underliers
WORK IN PROGRESS

_Background_

The Asset Refactoring initiative (see #2805) is seeking to improve the # Product Model to address some long-standing issues and to ensure the continued extensibility to additional financial products and markets. A proposal is being developed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes the third tranche of changes (of three planned tranches in CDM 6) to implement the refactored model. It introduces some significant refactoring of the Product structure in to the model.

_What is being released?_

Product Refactoring:

This release completes the refactoring of the major financial product data types, that is Asset, Observable, and Index, and combines their use in a new structure for financial products.
The new data type NonTransferableProduct has replaced the former ContractualProduct and is the main "product" data type used on a trade; it appears as the attribute product on the data type TradableProduct.
On a Trade, all financial products should be composed into the EconomicTerms of a NonTransferableProduct.
Separate PR but included here for completeness The data type payout is now a choice construct with the consequence that references to specific instances of a specific payout should refer to the capitalised data type name rather than an attribute, for example economicTerms -> payout -> performancePayout becomes economicTerms -> payout -> PerformancePayout. This has large impact in terms of the number of changes in this PR.
Underliers:

An Underlier represents the financial product that will be physically or cash settled.
Whereas all underliers were previously defined to use data type product, this has now been improved so that they can also be an Observable when the case warrants it.
Underlier is modelled as a choice data type, that is, it can either be an Observable or a Product.
A Product is also a choice type, either a TransferableProduct (a type of financial product which can be held or transferred, represented as an Asset with the addition of specific EconomicTerms), or a NonTransferableProduct (a product that can be traded, as part of a TradableProduct, but cannot be transferred to others).
Product and Trade Hierarchy:

The Trade data type now extends TradableProduct; this means that the latter is "hidden" in many uses in the CDM, eg in the graphical view, and one level within the hierarchy is removed when generating JSON. This change has resulted to updates to 100s of occurences to path accesses within the model (particularly in the Event and Product functions). For example, the previous access path tradeState -> trade -> tradableProduct -> tradeLot has become tradeState -> trade -> tradeLot.
The financial product hierarchy has also changed so that the previous path trade -> tradableProduct -> product -> contractualProduct -> economicTerms -> payout -> InterestRatePayout has become trade -> product -> economicTerms -> payout -> InterestRatePayout.
Product details:

Security
Removed the attributes economicTerms and ProductTaxonomy because these are not appropriate for an Asset data type which is standardised. if economicTerms are required, these can be added by wrapping security into a TransferableProduct. The taxonomy of assets is explicitly defined in the type.
Index
Index is now an Observable and the replaces the existing data type rateOption.
The path observable -> rateOption has become observable -> Index -> FloatingRateIndex.
Data type CreditIndex now extends IndexBase.
ProductQualification:

Additional functions have been created to ease with the qualification process: UnderlierQualification. ObservableQualification.
Extensive refactoring has been made to the qualification functions to reflect the refactoring, albeit with no changes to the actual logic.
The qualification for options has been tidied up to be more readable and maintainable in the new model, without change to the functionality.
The introduction of SettlementPayout has been included in the qualification logic.
The qualification of Foreign Exchange transactions has been updated.
The securities financing qualification functions have been enhanced to differentiate repos and lending; the new functions are: Qualify_RepurchaseAgreement, Qualify_BuySellBack, and Qualify_SecurityLending.
A new enumerator data type has been added to support repo qualification: RepoTypeEnum. The corresponding attribute repoType was added to AssetPayout.
Payouts:

A new payout type has been created: SettlementPayout
The existing ForwardPayout has been collapsed into SettlementPayout; the latter should be used whereever the former was previously.
Separate PR but included here for completeness The Payout data type has been refactored to be a choice and payouts now have multiple cardinality on EconomicTerms. The conditions that validated the business logic on payouts has been moved to EconomicTerms.
Event Model:

Create_Exercise has additional logic to support an option underlier that coan be an Asset, a TransferableProduct or a NonTransferableProduct.
Create_Execution now acts upon a more narrowly defined NonTransferableProduct rather than a generic product.
Additional functions have been created to support events using the new # Product Model: Create_NonTransferableProduct, Create_TransferableProductFromAsset, Create_TransferableProductFromIndex, CheckTransferableProduct, CheckTradeNotTransferableProduct, CreateTradableProduct.
The function NewEquitySwapProduct now creates a NonTransferableProduct not a generic product.
Event processing has been refactored to handle the new modeling of TradableProduct.
The unused data types Affirmation and Confirmation have been removed.
Observable:

The attribute Observable has been removed from ObservationTerms where it created duplication.
Collateral:

AssetIdentifier replaces ProductIdentifier on AssetCriteria.
The function CheckEligibilityForProduct now uses TransferableProduct not Product.
Namespace re-alignments:

The following data objects have been moved to a more appropriate namespace:
enum PutCallEnum to cdm.base.staticdata.asset
choice Index to cdm.observable.asset
type IndexBase to cdm.observable.asset
type PriceQuantity to cdm.observable.asset
The function InterestRateObservableCondition has been moved to the cdm.observable.asset function namespace.
Deprecated data types which have been removed:

IndexReferenceInformation: replaced with Index.
Documentation updates:

Significant improvements have been made to the pages on # Product Model, Event Model and Process Model.
As well as incorporating the direct changes that result from the refactoring, the pages have been restructured to improve the hierarchy, sections have been resequenced where this improves understanding, and "tips" have been added to highlight important definitions.
The use case sections on Collateral, Securities Lending and Repos have been updated to reflect the revised modeling.

_Backward-incompatible changes_

This release contains changes that are not backward-compatible:

# Legal Documentation Model - New ContractualDefinitionsEnum value to support the 2022 ISDA Verified Carbon Credit Transactions Definitions
_Background_

The International Swaps and Derivatives Association, Inc. published in 2022 the ISDA Verified Carbon Credit (VCC) Transactions Definitions (see https://www.isda.org/book/2022-isda-verified-carbon-credit-transaction-definitions/).

This release includes the reference to the ISDA VCC Transaction Definitions by including a new value to the existing ContractualDefinitionsEnum.

_What is being released?_

New ContractualDefinitionsEnum value:

Add a new value ISDA2022VerifiedCarbonCredit, in line with the existing values.
The annotation of the value is: ISDA 2022 Verified Carbon Credit Transactions Definitions.
Correct a typo in the annotation of the ISDA2023DigitalAssetDerivatives value, with a year of 2023 instead of 2021 as the value indicates.

_Backward-incompatible changes_

The change is backward compatible.

# Implementation of the Standardized Schedule Method for Initial Margin Calculation in CDM
# Standardised Schedule - Key Features
_Background_

The financial crisis that began in 2007 highlighted significant weaknesses in the resilience of banks and market participants to financial and economic shocks. To address these vulnerabilities and curb excessive risk-taking, the Basel Committee on Banking Supervision (BCBS) published the D499 document in April 2020, detailing new margin requirements aimed at reducing systemic risk and promoting central clearing. These requirements mandate that Initial Margin (IM) be calculated using either a quantitative model or a standardized method, ensuring uniformity and comprehensive coverage of counterparty risk.

_What is being released?_

The implementation of the Standardized Schedule Method for calculating Initial Margin (IM) within the Common Domain Model (CDM) is being released. This release introduces a structured approach to computing IM, providing a conservative and straightforward alternative for market participants who either lack the resources to develop quantitative models or prefer not to use third-party services.

Key components of this release include:

New enumerations have been created to cover the asset typologies and subtypes of financial products included in this methodology.
Functions have been developed to extract the duration and notional of financial products in accordance with specification D499.
After extracting the necessary data, additional functions have been developed to compute the initial margin by integrating the extracted data with the model formulas.
A package of examples of various covered products has been assembled to test the functionality of the implemented functions.
Data types

Added new StandardizedSchedule type.
Added new GrossInitialMarginAndMarkToMarketValue type.
Added new CreditEvent type.
Enumerations

Added new StandardizedScheduleAssetClassEnum enumeration.
Added new StandardizedScheduleProductClassEnum enumeration.
Functions

Added new AdjustableDateResolution function.
A fall back for unadjustedDate when adjustedDate is only available.
Added new AdjustableOrAdjustedOrRelativeDateResolution function.
A fall back for unadjustedDate when adjustedDate is only available.
Added new AuxiliarEffectiveDate function.
Extracts the effective date of specific products such as interest rate swaps and swaptions.
Added new AuxiliarTerminationDate function.
Extracts the termination date of specific products such as interest rate swaps and swaptions.
Added new BuildStandardizedSchedule function.
Takes a trade and uses qualification to extract the relevant information to populate the grid that will be used to calculate the gross initial margin.
Added new DateDifferenceYears function.
Computes the difference in years between two dates. All years are supposed to have 365 days.
Added new EconomicTermsForProduct function.
Extracts the economic terms from a product.
Added new FXFarLeg function.
Extracts the far leg of an FX swap (deliverable or not) based on two criteria: the forward payout with the latest value date or the forward payout with the latest settlement date.
Added new GetGrossInitialMarginFromStandardizedSchedule function.
Takes the grid information from an specific trade and calculates the gross initial margin.
Added new GetIMRequirement function.
Computes the IM requirement, which is required in the calculation of the gross initial margin. It depends exclusively on the asset class of the trade and, in some cases, on the duration as well.
Added new GetNetInitialMarginFromBaseCurrencyExposure function.
Computes the net initial margin, taking the gross initial margin result and the mark to market value."
Added new IsCreditNthToDefault function.
Identifies a product as a CR basket Nth to default.
Added new IsFXDeliverableOption function.
Identifies a product as an FX deliverable option.
Added new IsFXNonDeliverableOption function.
Identifies a product as an FX non-deliverable option.
Added new IsIRSwaptionStraddle function.
Identifies a product as an IR swaption straddle.
Added new IsIRSwapWithCallableBermudanRightToEnterExitSwaps function.
Identifies a product as an IR swap with bermudan/callable right to enter/exit swaps.
Added new ProductForTrade function.
Extracts the product from a trade.
Added new StandardizedScheduleAssetClass function.
Identifies the asset class of a trade, according to the standardized schedule classification.
Added new StandardizedScheduleCommodityForwardNotionalAmount function.
Extracts the notional amount of a CO forward. Floating price forwards not supported.
Added new StandardizedScheduleCommoditySwapFixedFloatNotionalAmount function.
Extracts the notional amount of a CO fixed float swap.
Added new StandardizedScheduleDuration function.
Extracts the duration of a trade, according to the product class-depending extraction method defined in the ISDA industry survey.
Added new StandardizedScheduleEquityForwardNotionalAmount function.
Extracts the notional amount of an EQ forward.
Added new StandardizedScheduleFXNDFNotional function.
Extracts the notional amount and currency of an FX non-deliverable forward.
Added new StandardizedScheduleFXNDONotional function.
Extracts the notional amount and currency of an FX non-deliverable option.
Added new StandardizedScheduleFXSwapNotional function.
Extracts the notional amount and currency of an FX swap.
Added new StandardizedScheduleFXVarianceNotionalAmount function.
Extracts the notional amount of an FX variance swap.
Added new StandardizedScheduleMonetaryNotionalCurrencyFromResolvablePQ function.
Extracts the notional currency for all products that have it populated in the resolvable priceQuantity.
Added new StandardizedScheduleMonetaryNotionalFromResolvablePQ function.
Extracts the notional amount for all products that have it populated in the resolvable priceQuantity.
Added new StandardizedScheduleNotional function.
Extracts the notional amount of a trade, according to the product class-depending extraction method defined in the ISDA industry survey.
Added new StandardizedScheduleNotionalCurrency function.
Extracts the notional currency of a trade, according to the product class-depending extraction method defined in the ISDA industry survey.
Added new StandardizedScheduleOptionNotionalAmount function.
Extracts the notional amount of a CO or EQ option.
Added new StandardizedScheduleProductClass function.
Identifies the product class of a trade, according to the standardized schedule classification.
Added new StandardizedScheduleVarianceSwapNotionalAmount function.
Extracts the notional amount of an EQ variance swap.
Added new UnderlierForProduct function.
Extracts the underlier product.

# Product Model - Asset Refactoring: Basket, Index, Observable, Foreign Exchange
_Background_

The Asset Refactoring initiative (see #2805) is seeking to improve the # Product Model to address some long-standing issues and to ensure the continued extensibility to additional financial products and markets. A proposal is being developed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes the second tranche of changes (of three planned tranches in CDM 6) to implement the refactored model. It introduces some new data types and makes changes to others; more significant refactoring ofthe Product structure will be introduced in the third release.

_What is being released?_

The word "update" below is to refer to a change to the modelling between this release and the previous release of Asset Refactoring changes (in CDM 6.0-dev.58).

Updates to AssetBase data type:

The identifier attribute has been made mandatory.
The attributes isExchangeListed, exchange and relatedExchange have been moved from InstrumentBase to AssetBase.
The type of the exchange and relatedExchange attributes has been updated from string to LegalEntity.
Updates to Cash data type:

New conditions have been added to ensure that taxonomy and exchange are not used for this asset type.
Changes to Commodity data type:

Now extends from AssetBase not ProductBase.
Accordingly, productTaxonomy has been replaced by taxonomy and the conditions updated.
Changes to Index and IndexBase data types:

The attributes exchange and relatedExchange have been removed from IndexBase as they will now be picked up from AssetBase.
Index has been refactored from a one-of condition to a choice data type.
The data type FloatingIndex has been renamed FloatingRateIndex.
The documentation in the model on the index-related data types has been improved.
The qualification logic has been updated to reflect the new modelling of Index as an underlier on the relevant functions.
Updates to ListedDerivative data type:

Updates have been made to the attribute names and types introduced in the previous release to improve modelling and composition.
The condition on VarianceReturnTerms has been updated to reflect the new position of ListedDerivative in the # Product Model.
Changes to Security data type:

Now extends from InstrumentBase not ProductBase.
Temporary changes made to add economicTerms and productTaxonomy pending further refactoring in the third phase.
Support for FX Observables:

The data type ForeignExchangeRate has been created and added as a choice to Index; it also extends IndexBase.
This new data type contains the same attributes as the existing FXRateObservable which has been deprecated.
The ForeignExchange data type has been deprecated and the deprecated ExchangeRate and CrossRate datas type have both been deleted.
Refactoring of Observable:

The following data types have been added to Observable as new attributes: Asset, Basket, Index.
The following data types have been removed from Observable: Commodity (now available as an Asset); QuotedCurrencyPair (replaced by the the FX observable data type inside Index).
The unused attribute optionReferenceType and its corresponding enumerator OptionReferenceTypeEnum have been removed from the model.
Observable now has a one-of condition (and will be updated to choice in the third phase).
The two attributes pricingTime and pricingTimeType on ObservationTerms have been renamed observationTime and observationTimeType respectively.
Changes to BasketConstituent:

BasketConstituent now extends from Observable, not Product.
Moved from the product namespace to the observable namespace.
The qualification logic has been updated to reflect the new modelling of BasketConstituent as an underlier on the relevant functions.
Updates to TransferableProduct:

The inheritance on this data type has been updated so that it now extends Asset but the attributes are unchanged.
Updates to Payouts:

The documentation in the model on the payout-related data types has been improved.
The SettlementCommitment data type and attribute has been renamed SettlementPayout.

_Backward-incompatible changes_

This release contains changes that are not backward-compatible:

Changes to the following data types are particularly impactful and have required updates to the mapping synonyms and samples:
Commodity
Index
QuotedCurrencyPair
FXRateObservable.
A full description of the _Backward-incompatible changes_, and how persisted objects should be remapped, will be included in the release notes for the last tranche of the asset refactoring.

# Product Model - Asset Refactoring: Asset, Index, Identifier
_Background_

The Asset Refactoring initiative (see #2805) is seeking to improve the # Product Model to address some long-standing issues and to ensure the continued extensibility to additional financial products and markets. A proposal is being developed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes the first tranche of changes to implement the refactored model (the first of three planned tranches in CDM 6). It introduces some new data types and makes minor changes to others without significant impact to the Product structure itself.

_What is being released?_

New Asset data type:

Introduce the new data type Asset which is defined as "something that can be owned and transferred in the financial markets". The data type is implemented using the new Rune DSL feature choice that is available in Release 9.10.
Introduce the additional Asset data sub-type called Instrument, also using choice, defined as "a type of Asset that is issued by one party to one or more others".
Create the new base class InstrumentBase to model common attributes across all Instrument data types.
Introduce the new enumerator AssetIdTypeEnum, to define certain identifier sources unique to Assets, as an extension of ProductIdTypeEnum.
Change the inheritance from ProductBase to InstrumentBase for Loan, ListedDerivative.
Add a reference on Observable to an Asset using an AssetIdentifier.
Changes to Transfer:

As Asset is defined as something that can be transferred, the modelling of Transfer has been refactored to act upon Asset rather than Observable with a change to TransferBase.
This also results in changes to the Qualify_SecurityTransfer function.

# Product Model:

Introduce a new data type on Payout: SettlementCommitment which models the settlement of an Asset for cash.
Introduce TransferableProduct as a type of Product which can be used in a SettlementCommitment for a basic cash settled trade of either an Asset with or without the addition of specific EconomicTerms.
Define the new SettlementCommitment data type to model this new kind of Payout.

_Backward-incompatible changes_

This release contains changes that are not backward-compatible:

The change in the inheritance for Loan and ListedDerivative impacts the use of identifiers in these data types.
The refactoring of Transfer to act upon an Asset rather than Observable impacts the use of the related functions.
Samples and mappings for both have been updated accordingly.

A full description of the _Backward-incompatible changes_, and how persisted objects should be remapped, will be included in the release notes for the last tranche of the asset refactoring.

# CDM Model - Modification to product condition
_What is being released?_

Modified condition for FpML_cd_30

Addition of clause to allow adjustedDate check
Modified condition for Floating Rate Option.

Addition of clause to allow posting of supplementary attribute IndexReferenceInformation

# Product Model - Portfolio Return Terms
_Background_

Purpose is to release a Dev version for Portfolio Return Terms, in which the components with [deprecated] annotation in current Prod version have been removed.
As an indication, this release also contains a renaming of existing attributes in ValuationDates.

_What is being released?_

removed [deprecated] attributes below from type PriceReturnTerms :
valuationPriceInitial
and valuationPriceFinal
and finalValuationPrice
added attributes below to type PerformancePayout : that is core release to Dev of the same components previously released in Prod today :
creation of new type PorfolioReturnTerms which extends ReturnTerms
with existing types PayerReceiver
also with existing type PriceSchedule to PerformancePayout used under three attribute names, and NonNegativeQuantitySchedule used for one, respectively : initialValuationPrice, interimValuationPrice, finalValuationPrice and quantity
added the above new type to PerformancePayout
added existing type PriceSchedule to PerformancePayout used under three attribute names : initialValuationPrice, interimValuationPrice and finalValuationPrice (that is to replace [deprecated] ones removed from ReturnTerms)
updated type Basket with below changes :
removed temporary PorfolioBasketConsituent of type BasketConstituent
removed as well [deprecated] basketConstituent of type Product
added instead new attribute basketConstituent of type BasketConstituent
renamed attributes below in ValuationDates :
initialValuationDate (instead of valuationDatesInitial)
interimValuationDate (instead of valuationDatesInterim)
finalValuationDate (instead of valuationDatesFinal)

_Backward compatibility_

As an information, this release contains two sets of changes with no backward compabiltity :

removing [deprecated] components above mentioned
renaming of attribtues in ValuationDates above mentioned
The changes can be reviewed in PR: #2974

# Reference Data - Update ISOCurrencyCodeEnum
_What is being released?_

Updated ISOCurrencyCodeEnum based on updated scheme ISO Standard 4217.

Version updates include:

Removed values: MWK, PEN, RON, SZL, TRY
Added value: ZWG
Backward Incompatible Changes

As this release removes multiple enum values, it will not be backwards compatible.

CDM Distribution - Python Code Generation
_What is being released?_

This release updates the bundle dependency to version 11.10.0 to include the new version of the Python generator which includes the following changes:

added support for model name clashes with Python keywords, soft keywords, and items whose names begin with "_"
added support for DSL operators to-string and to-enum
resolves the defect exposed by PR #2766
includes an update to the Python runtime library (2.1.0) used to encapsulate the Pydantic support
# Product Model - FpML Mapping Update
_Background_

This release adds FpML mapping fixes and improvements that have been previously implemented in other models such as Digital Regulatory Reporting (DRR).

_What is being released?_

FpML synonyms to map EventInstruction attributes intent, eventDate and effectiveDate
FpML synonyms to map EconomicTerms attribute nonStandardisedTerms
FpML synonyms to map WorkflowState attribute workflowStatus
FpML synonyms and mapper to map commodity schedule xml elements calculationPeriodsSchedule and calculationPeriods into PriceSchedule->datedValue

# Product Model: Quantity Change For Existing Trade Lot
_Background_

This release adds support for price and/or quantity changes on an existing TradeLot, as described in issue #2923.

_What is being released?_

The Create_QuantityChange function has been updated to allow the price and/or quantity to be updated on an existing TradeLot. If the QuantityChangeInstruction->lotIdentifier matches the trade's TradeLot->lotIdentifier, then the price and/or quantity with matching units are updated based on the direction (i.e. Increase, Decrease, Replace) specified in the instructions.

The existing functionality is unchanged for an increase, i.e., if the QuantityChangeInstruction->lotIdentifier does not match the trade's TradeLot->lotIdentifier, then a new TradeLot is created.

# # Eligible Collateral Representation - New Attributes
_Background_

Through the Collateral Working Group, members have requested two enhancements to the modelling of collateral eligibility to enhance the CDM's capability to support
additional use cases:

To support the scenario where a legacy collateral schedule has shared criteria for IM and VM, with selected terms applicable to only one single margin type
To prioritise between Collateral Criteria where agency ratings are specified for both assets and issuers
_What is being released?_

A new enumerator added to denote the different options available to identify how to prioritise the Agency Rating in a particular Issuer or Asset Criteria over
the Agency Rating in another Criteria: RatingPriorityResolutionEnum.
Two new attributes added to CollateralCriteriaBase to increase the specificity of the definition of the criteria in which collateral is eligible:
restrictTo to denote whether the criteria applies to only IM or VM, using the existing enumerator CollateralMarginTypeEnum
ratingPriorityResolution to denote whether the Issuer Criteria or Asset Criteria have precedence where there are multiple Agency Ratings defined,
using the new RatingPriorityResolutionEnum enumerator
Both new attributes are optional, with singular cardinality, so this is a backward-compatible change.

# Product Model - Remove AssetPool and deprecated data types
_Background_

The Asset Refactoring initiative (see #2805) is seeking to improve the # Product Model to address some long-standing issues and to ensure the continued extensibility to additional financial products and markets. A proposal is being developed - through a cross-industry Task Force - to implement this remodelling in the CDM. Prior to that, this preparatory PR proposes to remove the AssetPool data type, which has been found to be both unused and incorrect, and to remove some additional data types in the # Product Model which were previously deprecated. This approach should provide a cleaner implementation path for the remodelling which will be put forward in a subsequent PR.

_What is being released?_

Remove the AssetPool data type which was previously introduced from FpML but has been found to be incorrect and unusable.

Remove the following deprecated data types used in the # Product Model:

Bond
ConvertibleBond
Equity
IdentifiedProduct
ObservationSource
SecurityPayout.
Remove the following deprecated data types that are related to the deprecated SecurityPayout:

SecurityLeg
InitialMargin
InitialMarginCalculation
SecurityValuation
SecurityValuationModel
BondValudationModel
BondPriceAndYieldModel
CleanOrDirtyPrice
CleanPrice
RelativePrice
BondEquityModel
BondChoiceModel
UnitContractValuationModel.
Remove the reference to SecurityPayout from Payout.

Remove the reference to AssetPool from Product.

Remove functions which act upon SecurityPayout.

Remove mapping synonyms from FpML for AssetPool and SecurityPayout.

Update the CDM documentation to ensure it remains aligned with the implementation. This includes some changes to the hierarchy of the documentation to improve readability when displayed using Docusaurus.

Further details on the rationale for the change and the impact on the model can be found in Issue #2966.

Backward Incompatible Changes

As this release removes multiple attributes and product types, it will not be backwards compatible.

Sample Impact

The existing Fixed Rate Repo sample was using the SecurityPayout construct. It is believed that this was based on some mapping of FpML files which did not represent
real business cases. Futhermore, according to ICMA, FpML is not widely used for repo transactions. Therefore this erroneous sample has been removed from the
FINOS CDM distribution.

There is no impact to samples from removing AssetPool or any of the other changes listed above.

# Product Model - New Data Types
_Background_

The Asset Refactoring initiative (see #2805) is seeking to improve the # Product Model to address some long-standing issues and to ensure the continued extensibility to additional financial products and markets. A proposal is being developed - through a cross-industry Task Force - to implement this remodelling in the CDM. Prior to that, this preparatory PR introduces some new data types in the development environment so that they can be seen by participants.

_What is being released?_

A new enumerator added to support a new approach to identifiers for assets: AssetIdTypeEnum.
New data types added to start the build out of the concept of "Asset" in the # Product Model:
AssetBase as the base data type to specify common attributes for all Assets
AssetIdentifier a new data type to uniquely identify an asset, including using the AssetIdTypeEnum
Cash a new data type to represent an asset that is a monetary holding in a currency
DigitalAsset a new data type to represent an asset that exist only in digital form, eg Bitcoin or Ethereum
ListedDerivative a new data type to represent an asset that is a securitised derivative on another asset, such as a exchange traded future
A new func namespace created cdm.base.staticdata.asset.common, containing three new functions to aid the use of the new Cash asset: AssetIdentifierByType, GetCashCurrency, SetCashCurrency.
None of the existing functionality or modelling is impacted as these changes are standalone at this time. The proposed Asset data type will be defined and introduced later.


# Product Model - FpML Mapping Update
_Background_

The FpML mapping needs further coverage for Forward Rate Agreements (FRAs).

_What is being released?_

FpML mapper updated to map FRA payment frequency from the FpML index tenor on to the fixed leg instead of the floating leg

# Product Model - FpML Mapping Update
_Background_

The FpML mapping needs further coverage for Forward Rate Agreement (FRA) and Commodity products.

_What is being released?_

FpML synonyms and mapper updated to map FRA fixed leg payment frequency from the FpML index tenor
FpML synonyms added to map Commodity delivery date parameter deliveryNearby

# Product Model - Principal Amount Conditions
_Background_

The condition that requires the amount (or its present value) to be populated on a principal payment is too strict. There are cases where this amount is not known in advance because the quantity of the payout may evolve, e.g. in scenarios such as "Mark-to-Market" Cross-Currency Swaps or resettable Equity Swaps.

_What is being released?_

This release relaxes the condition on PrincipalPayment so that the amount (or its present value) are optional. Instead, to preserve sufficient data quality constraints on the model, new conditions are implemented at the Payout level to enforce the presence of those attributes only in the relevant scenarios.

Data types:

PrincipalPayment:
Relaxed the PrincipalAmount constraint to be optional choice instead of required choice between the principalAmount and presentValuePrincipalAmount attributes.
PrincipalPaymentSchedule:
Added a new InitialPrincipalAmountExists condition that requires initialPrincipalPayment, when it exists, to have either its principalAmount or presentValuePrincipalAmount attributes populated.
PayoutBase:
Added a new FinalPrincipalAmountExists condition that requires finalPrincipalPayment, when it exists and the quantity is not resettable, to have either its principalAmount or presentValuePrincipalAmount attributes populated.

# Product Model - Trigger type refactoring
_Background_

Currently, the values for representing specific elements such as barriers are done through the level and levelPercentage within the Trigger type . The interest in having them represented as Price types is strong since it is a richer type than a number.

_What is being released?_

A refactoring has been made to the Trigger type element, modifying the level and levelPercentage to a unified CDM element level of type PriceSchedule.

Data types:

Updated Trigger type with a new unified level element of PriceSchedule type.
The condition Choice1 has been updated to be consistent with the proposal.
Modification of the synonym mappings for Trigger type.

# Product Model - Modification of AmericanExercise Condition in ExerciseTerms
_Background_

The conditions under ExerciseTerms in the refactored OptionPayout are not sufficiently restrictive for American options. More specifically, the AmericanExercise condition should not only control that the expirationDate is present (which it already does through the CommencementAndExpirationDate condition), but that it is also present only once (not currently implemented in the model). This release aims at modifying the AmericanExercise condition to correct this.

_What is being released?_

The AmericanExercise condition under the ExerciseTerms type has been modified to constrain the expirationDate field to only one occurrence.
Backward Incompatible Changes

It should be noted that this proposal contains a backwards incompatible change, given that a condition has been made stricter, but should not impact any of the actual implementations.

# CDM Model - RoundToPrecision Function
_Background_

This release contains a bug fix for RoundToPrecision function, as described in issue #2915.

_What is being released?_

This release updates the existing function cdm.base.math.RoundToPrecision to round to the correct number of decimal places.

func RoundToPrecision: <"Round a rate to the supplied precision, using the supplied rounding direction">
inputs:
value number (1..1) <"The original (unrounded) number.">
precision int (1..1) <"The number of decimal digits of precision.">
roundingMode RoundingDirectionEnum (1..1) <"The method of rounding (up/down/nearest).">
output:
roundedValue number (1..1) <"The value to the desired precision">
The following examples show the function behaviour:

RoundToPrecision(1023.123456789, 5, RoundingDirectionEnum -> NEAREST) = 1023.12346
RoundToPrecision(1023.123456789, 5, RoundingDirectionEnum -> UP) = 1023.12346
RoundToPrecision(1023.123456789, 5, RoundingDirectionEnum -> DOWN) = 1023.12345
RoundToPrecision(1023.123456789, 0, RoundingDirectionEnum -> NEAREST) = 1023
RoundToPrecision(1023.1, 7, RoundingDirectionEnum -> NEAREST) = 1023.1000000

# Python Generator v2
_What is being released?_
This release uses the new version of the Python generator (v2) which includes the following changes:

Migration to Pydantic 2.x
More comprehensive support for Rosetta's operators
Resolves the defect exposed by PR 2766
Includes an update to the Python Rosetta runtime library used to encapsulate the Pydantic support (now version 2.0.0)

# Product Model - Qualification of Total Return Swaps (TRS) with a Debt Underlier
_Background_

Following ESMA Guidelines, Total Return Swaps with a debt instrument as their underlier (bond, loan, etc) must report field 2.11 - Asset Class as 'CRDT', while TRS on an equity index or a basket of equities should report Asset Class as 'EQUI'. Currently in the CDM, a Total Return Swap with a debt underlier is not classified correctly, and thus is being reported incorrectly as well. This release aims at fixing the Qualify_AssetClass_Credit function such that Total Return Swaps on a bond or a loan report AssetClass as 'CRDT'.

_What is being released?_

The function Qualify_AssetClass_Credit is increasing its coverage to include Total Return Swaps with an underlier of a loan or a securityType of debt.
Functions

Updated Qualify_AssetClass_Credit function to support Total Return Swap products, defined as having an interestRatePayout and a performancePayout. The function checks the performancePayout that underlier -> loan is present or that underlier -> security -> securityType = Debt.

# Product Model - Qualification of Foreign Exchange NDS
_Background_

Currently, Foreign Exchange Non-Deliverable Swaps are not supported in the Common Domain Model. This release adds qualification support for this kind of product.

_What is being released?_

Added the function Qualify_ForeignExchange_NDS that qualifies as true if a product has two forward payouts with an FX underlier and the cashSettlementTerms populated.

# Addition of new enumeration to AvailableInventory
_Background_

The AvailableInventory type supports two major uses cases:

Where a lender wants to distribute the details of the securities that they have available to lend, and
Where a borrower wants to locate specific securities that they want to borrow.
For the second use case, the SecurityLocate type was created, which extends AvailableInventory but has no additional attributes within it.

When using these two types, there is currently no way to differentiate between when the user is intending to implement use case 1 (i.e. use the AvailableInventory type) or use case 2 (i.e. use the SecurityLocate type).

As an example, the following valid JSON could represent a lender saying they have 10000 shares available of security GB00000000012, or a borrower requesting 10000 shares of the security.

{
"availableInventoryRecord": [
{
"identifier": {
"identifier": "00001"
},
"security": {
"securityType": "Equity",
"productIdentifier": {
"identifier": "GB00000000012",
"source": "ISIN"
}
},
"availableQuantity": {
"value": 10000
}
}
]
}
_What is being released?_

A new AvailableInventoryTypeEnum enumeration has been added with two options:

AvailableToLend - for where a party wants to expose the securities that they have available (i.e. use case 1)
RequestToBorrow - for where a party wants to request specific securities from another party (i.e. use case 2)
The new enumeration has been added to the AvailableInventory type and named availableInventoryType. It has been set with (1..1) cardinality, making it mandatory.

The availableQuantity attribute within AvailableInventoryRecord has also been renamed to just quantity to make it more generic and thus applicable to more use cases.

# Product Model - Qualification of AssetClass
_Background_

Issue #2863 was identified with the recent change to the qualification of AssetClass in PR #2840.

_What is being released?_

This release fixes the following functions to ensure an else clause is specified in all nested if statements.

Qualify_AssetClass_InterestRate
Qualify_AssetClass_Credit
Qualify_AssetClass_ForeignExchange
Qualify_AssetClass_Equity
Qualify_AssetClass_Commodity

# Product Model - Synonym mappings for BusinessCenterEnum
_Background_

The version 2-17 of the FpML coding schemes was recently published. This new version included some changes that are already present in the corresponding enumerations of the CDM model, but the synonym mappings from FpML to CDM have not been updated to cover the latest changes.
This release introduces support for the synonym mappings, to cover the changes in v2-17 of FpML coding schemes for BusinessCenterEnum.

_What is being released?_

Added mapping coverage for all missing values of BusinessCenterEnum.

# Product Model - Bond Option and Forward Qualification
_Background_

A previous release introduced a regression in the qualification of Bond Forwards and Bond Options, that were no longer qualified due to an update in the Qualify_AssetClass_InterestRate function. This release addresses this issue.

_What is being released?_

Reintroduced the capability to have a security with securityType = SecurityTypeEnum -> Debt as underlier of the option or forward in the Qualify_AssetClass_InterestRate function.

# Product Model - FpML Mapping - Commodity Swaps
_Background_

Commodity swaps with a physical leg and a fixed/floating leg are incorrectly mapped from FpML. This is described in more detail in issue #2837.

_What is being released?_

This release fixes the mapping for Commodity Swaps from FpML as listed below.

Commodity swap samples with a physical leg and a fixed leg are now only mapped to these two payouts: ForwardPayout and FixedPricePayout
Commodity swap samples with a physical leg and a floating leg no longer have a settlement type defaulted to cash

# Product Model - Updates to Qualification of AssetClass
_Background_

Some products are currently not covered by the "ISDA Taxonomy V2 Level 1 - ASSETCLASS" functions.
In particular, a gap in index underliers has been identified and is resolved by this update. For further information please see issue #2762.

_What is being released?_

This release modifies functions Qualify_AssetClass_* along the below three axes:

Added inclusive checks on underlier -> index -> productTaxonomy -> primaryAssetClass
Aligned security criteria to index criteria: switch from checking security->securityType to checking security->productTaxonomy -> primaryAssetClass
Added inclusive checks on forwardUnderlier under some functions where only the optionUnderlier was previously considered
Modified functions:

Qualify_AssetClass_InterestRate
Qualify_AssetClass_Credit
Qualify_AssetClass_ForeignExchange
Qualify_AssetClass_Equity
Qualify_AssetClass_Commodity

# Product Model - FpML Mappings - Contractual Party
_Background_

Currently, ContractualParty is not being mapped in the LegalAgreement element. The contractualParty is a (2..2) mandatory element and should be represented accordingly to avoid validation failures. For more information see issue #2788 .

_What is being released?_

Updated DocumentationHelper which allows mappers to extract the value from TradableProduct->counterparty and map it to LegalAgreement->contractualParty.

# Product Model - FpML Mapping - Commodity Forwards
_What is being released?_

This release extends the FpML mapping coverage for Commodity Forwards.

FpML commoditySwap legs coalPhysicalLeg, electricityPhysicalLeg, environmentalPhysicalLeg, gasPhysicalLeg, oilPhysicalLeg have been mapped into the model as a ForwardPayout leg
The payerPartyReference and receiverPartyReference have been mapped to ForwardPayout->payerReceiver
The commodity->commodityClassification reference has been mapped to ForwardPayout->underlier->commodity->productTaxonomy

# Eligible Collateral Representation - CreditNotationMismatchResolutionEnum update
_Background_

The existing enum CreditNotationMismatchResolutionEnum contains enumeration values for specifying credit notation in the case where several are listed. The values include "highest", "lowest" and other levels of credit notation as well as sourced from a defined rating agency. There is currently no option for a credit notation where bespoke language represents the label characteristics of the rating.

_What is being released?_

Added enum value: Other to cover for the case where credit notation is based on bespoke language.

# Product Model - Commodity Physical Options
_Background_

This release provides qualification support for Commodity Physical Option products, which were not supported until now. These kind of products will be represented similarly to cash settled options: an optionPayout with a strike that can be fixed or floating, a commodity underlier and the delivery information specified in the attribute delivery. The only difference will be that settlementType will be Physical.

_What is being released?_

Added two qualifying functions: Qualify_Commodity_Option_Cash and Qualify_Commodity_Option_Physical, which use the function Qualify_Commodity_Option and check the value in settlementType.

Updated the qualifying functions Qualify_Commodity_Swap_FixedFloat and Qualify_Commodity_Swap_Basis so they do not check that the underlier is a commodity. That is redundant information since it is already been checked in Qualify_AssetClass_Commodity.


# Product Model - OptionPayout Refactoring
_Background_

In order to reduce redundancy and overcomplexity in the CDM, a refactoring of the OptionPayout structure is required. The information contained in the fields inside the optionStyle (americanExercise, europeanExercise, and bermudaExercise) can be unified under a new type ExerciseTerms. This will reduce the redundancy of having the same information repeated under the three styles and improve the simplicity of the model. To distinguish whether the option is American, European, or Bermuda, a new style enumeration is added to the model inside ExerciseTerms, with the values American, European, and Bermuda. Additionally, the strike attribute, previously under exerciseTerms, is moved outside and located directly under OptionPayout, given that it does not convey any information about the exercise terms of an option.

_What is being released?_

The OptionStyle type is removed from the model, along with the AmericanExercise, EuropeanExercise, and BermudaExercise types that are encapsulated in it.
A new ExerciseTerms structure is added to OptionPayout, containing all distinct fields found previously under the three exercise styles.
A new style enumeration is added under ExerciseTerms to distinguish the style of the option. This enumeration is made optional to account for the exercise terms of a CancelableProvision, ExtendibleProvision, or OptionalEarlyTermination, where the style is derived from the structure itself.
The CancelableProvision, ExtendibleProvision, and OptionalEarlyTermination structures have been modified to use the new ExerciseTerms type instead of the old AmericanExercise, EuropeanExercise, and BermudaExercise types.
Synonym mappings have been modified to reflect these changes.
Data types

Removed OptionExercise type.
Removed OptionStyle type.
Removed AmericanExercise, EuropeanExercise, and BermudaExercise types.
Added new OptionExerciseStyleEnum enumeration with values American, European and Bermuda.
Added new ExerciseTerms type, containing:
all of the distinct fields present before in AmericanExercise, EuropeanExercise, and BermudaExercise types,
a style attribute of type OptionExerciseStyleEnum,
and the exerciseProcedure attribute of type ExerciseProcedure that was previously contained in OptionExercise.
Switched exerciseTerms attribute in OptionPayout type to use the new ExerciseTerms type instead of the removed OptionExercise type.
Moved the strike attribute previously contained in OptionExercise type to OptionPayout type.
Removed americanExercise, europeanExercise, and bermudaExercise attributes from CancelableProvision, ExtendibleProvision, and OptionalEarlyTermination types
Replaced by a single exerciseTerms attribute of type ExerciseTerms instead.
Enumerations

Added new OptionExerciseStyleEnum enumeration, with values American, European, and Bermuda.
Backward Incompatible Changes

The type OptionExercise is removed from the model and replaced by the ExerciseTerms type. This new type is used instead for the exerciseTerms attribute in OptionPayout.
The OptionStyle type is removed from the model along with the three option exercise types contained inside it: AmericanExercise, EuropeanExercise, BermudaExercise.
Instead in ExerciseTerms, it is replaced by the distinct group of attributes required to represent any type of option style, plus the exerciseProcedure attribute previously in OptionExercise.
The style enumeration is incorporated into the ExerciseTerms type to differentiate between American, European, and Bermuda styles.
Finally, the strike, previously under OptionStyle, has been moved outside of ExerciseTerms and is located directly under OptionPayout.
Sample Impact

There are many samples impacted by this change, namely all the samples utilizing the OptionPayout structure. The impact on the three following samples (one European, one American, one Bermuda) is shown to visualize how the refactoring of the OptionPayout affects the structure of the CDM trades:

eqd ex04 european call index long form: the OptionStyle has been removed in favor of the the style = "European", and the relevant fields previously under europeanExercise (expirationDate and expirationTimeType). Additionally, the strike is moved from inside exerciseTerms to outside.
From:

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
To this:

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
eqd ex01 american call stock long form: the OptionStyle has been removed in favor of the the style = "American", and the relevant fields previously under americanExercise (commencementDate, expirationDate, latestExerciseTime, expirationTimeType, and multipleExercise). Additionally, the strike is moved from inside exerciseTerms to outside.
From:

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
To this:

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
...

# Product Model - Qualifying functions: Enhanced support for ETDs
_Background_

In CDM there is no support for Exchange-traded derivatives (ETDs) in some qualifying functions. This contribution enhances the support for ETD in Qualify_AssetClass_InterestRate and Qualify_AssetClass_Commodity when an optionUnderlier exists in the sample at "security" level.

_What is being released?_

Added support for optionUnderlier when is ETD for Qualify_AssetClass_InterestRate.
Added support for optionUnderlier when is ETD for Com Opt in Qualify_AssetClass_Commodity.

# Legal Agreements - Master Agreement Type enumeration - ISDAIIFM_TMA code
_Background_

In CDM there are not any available codes for Islamic Master Agreements. This contribution adds a new ISDAIIFM-TMA code to the MasterAgreementTypeEnum to cover the Islamic Derivative Master Agreements in CDM.

_What is being released?_

Added a new ISDAIIFM_TMA code to the MasterAgreementTypeEnum.
Mapping coverage for this new ISDAIIFM_TMA code.

# Mapping - Settlement Type Mapping Fix
_Background_

It had been observed that some of the ingested FpML samples in the Rosetta Platform were not correctly translating the settlement type of the trade. Specifically, the field settlementTerms->settlementType in CDM was not being populated, despite the incoming FpML samples containing information about the settlement type. This release aims at correcting this mismatch between FpML and CDM. In turn, this will improve the reporting capabilities of the DRR field DeliveryType, given that several samples which were previously not reporting this field will now contain the necessary information to do so.

_What is being released?_

Updates in the synonym mappings have been incorporated to populate the settlementType field in CDM whenever FpML contains the fields cashSettlement, amount->cashSettlement, tradeHeader->productSummary->settlementType, or genericProduct->settlementType.
Translate

Added [hint "productSummary"] to the tradeLot in the TradableProduct synonym.
Added [value "creditDefaultSwap" , "creditDefaultSwap" path "creditDefaultSwapOption" , "creditDefaultSwapOption", "tradeHeader"] to the tradeLot in the TradableProduct synonym.
Added [hint "settlementType"] to the settlementTerms in the PriceQuantity synonym.
Added [set to SettlementTypeEnum -> Cash when "cashSettlement" exists] to the settlementType in the SettlementBase synonym.
Added [set to SettlementTypeEnum -> Cash when "settlementType" = "Cash"] to the settlementType in the SettlementBase synonym.
Added [set to SettlementTypeEnum -> Cash when "amount->cashSettlement" = True] to the settlementType in the SettlementBase synonym.
Added [set to SettlementTypeEnum -> Physical when "settlementType" = "Physical"] to the settlementType in the SettlementBase synonym.

# Mapping - Commodity Classification Coverage
_Background_

Following the addition of commodity classification structures in the Rosetta Platform, there is now a need to incorporate mappings from the corresponding FpML fields to the new CDM fields related to the classification of commodities.

Specifically, to cover the CDM mappings to:

underlier->commodity->productTaxonomy->value->classification->value and
underlier->commodity->productTaxonomy->value->classification->ordinal
_What is being released?_

Synonym mappings that map:

commodityClassification->code to classification->value.
commodityClassification->code->commodityClassificationScheme to classification->ordinal.
The ordinal is mapped to values 1, 2, or 3 as follows:

If commodityClassificationScheme = http://www.fpml.org/coding-scheme/esma-emir-refit-layer-1-commodity-classification, then ordinal = 1
If commodityClassificationScheme = http://www.fpml.org/coding-scheme/esma-emir-refit-layer-2-commodity-classification, then ordinal = 2
If commodityClassificationScheme = http://www.fpml.org/coding-scheme/esma-emir-refit-layer-3-commodity-classification, then ordinal = 3
Mappings

[value "floatingLeg", "oilPhysicalLeg"] is added to priceQuantity in the TradeLot synonym.
[hint "commodityClassification"] is added to commodity in the Observable synonym.
[value "code"] is added to value in the TaxonomyClassification synonym.
[hint "commodityClassification"] is added to the observable in the PriceQuantity synonym.
[set to 1 when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/esma-emir-refit-layer-1-commodity-classification"] is added to ordinal in the TaxonomyClassification synonym.
[set to 2 when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/esma-emir-refit-layer-2-commodity-classification"] is added to ordinal in the TaxonomyClassification synonym.
[set to 3 when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/esma-emir-refit-layer-3-commodity-classification"] is added to ordinal in the TaxonomyClassification synonym.
[set to 1 when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/isda-layer-1-commodity-classification"] is added to ordinal in the TaxonomyClassification synonym.
[set to 2 when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/isda-layer-2-commodity-classification"] is added to ordinal in the TaxonomyClassification synonym.
[set to 3 when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/isda-layer-3-commodity-classification"] is added to ordinal in the TaxonomyClassification synonym.
[set to TaxonomySourceEnum -> ISDA when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/isda-layer-1-commodity-classification"] is added to source in the ProductTaxonomy synonym.
[set to TaxonomySourceEnum -> EMIR when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/esma-emir-refit-layer-1-commodity-classification"] is added to source in the ProductTaxonomy synonym.
[hint "primaryAssetClass"], [hint "secondaryAssetClass"], [hint "fra"], [hint "creditDefaultSwapOption"], [hint "bondOption"], [hint "commoditySwaption"], [hint "genericProduct"], [hint "productType"], and [value "commodityClassification"] are added to the productTaxonomy in the ProductBase synonym.

# Product Model - Day Count Fraction: RBA_Bond_Basis
_Background_

The codes RBA_BOND_BASIS_QUARTER, RBA_BOND_BASIS_SEMI_ANNUAL and RBA_BOND_BASIS_ANNUAL in the CDM enum DayCountFractionEnum have been found redundant by definition. The solution to this issue is to merge them into one single code: RBA_BOND_BASIS. This also aligns with the FpML representation.

_What is being released?_

Replaced the codes RBA_BOND_BASIS_QUARTER, RBA_BOND_BASIS_SEMI_ANNUAL and RBA_BOND_BASIS_ANNUAL with the code RBA_BOND_BASIS in the CDM enum DayCountFractionEnum.
Mapping added to populate the new code with the FpML code RBA.
Backward incompatible changes

This release contains backward-incomplatible changes. Anywhere the codes RBA_BOND_BASIS_QUARTER, RBA_BOND_BASIS_SEMI_ANNUAL or RBA_BOND_BASIS_ANNUAL are found, this code must be replaced by the new one RBA_BOND_BASIS.

# Product Model - Commodity Forwards
_Background_

This release provides qualification support for Commodity Forward products, which were not supported until now. The representation of commodity forwards pictures two possible kinds of forwards: fixed price forwards consisting in the combination of a fixedPricePayout and a forwardPayout; and floating price forwards consisting in the combination of a commodityPayout and a forwardPayout. The forwardPayout represents the physical delivery of the commodity while the other payout represents its pricing and the payment of a monetary amount in exchange.

_What is being released?_

Added the qualifying function Qualify_Commodity_Forward.
Updated the qualifying function Qualify_AssetClass_Commodity in order to consider commodity forwards.

# CDM Model - Date Time Functions
_What is being released?_

This release implements the existing ToDateTime function, and adds ToTime function.

ToDateTime - converts a date to a zonedDateTime, defaulting the time to "00:00:00" and the timezone to "Z" (UTC)
ToTime - created a time from inputs of hours, minutes and seconds

# Event Model - PartyRoleEnum including PTRRServiceProvider role
_Background_

In order to report under EMIR, a party needs to be identified as a portfolio compression or a portfolio rebalancing service provider. These roles can be unified in a more generic role: PTRR Service Provider. The current CompressionServiceProvider code will be replaced by PTRR Service Provider.

_What is being released?_

_Backward-incompatible changes_

CDM enum PartyRoleEnum has been modified in the following way: code CompressionServiceProvider has been eliminated and replaced by a more generic code PTRRServiceProvider.
Synonym mappings have been added to reflect this change.

# Product Model - FpML Mappings - Bond Forwards
_What is being released?_

This release fixes FpML mapping issues related to bond forward samples.

# Updates to Zero Coupon Swaps Qualification Functions
What is being released

_Background_:

The Qualification functions in CDM classify financial products.
Qualifying functions Qualify_InterestRate_IRSwap_FixedFloat_ZeroCoupon and Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon don't need the reference to Qualify_Transaction_ZeroCoupon_KnownAmount since it is redundant. The first two functions use Qualify_Transaction_ZeroCoupon and that means that the reference to the KnownAmount function is not needed.
Goal:

Improve the existing qualifying functions Qualify_InterestRate_IRSwap_FixedFloat_ZeroCoupon and Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon.
Qualification functions

Updated the Qualify_InterestRate_IRSwap_FixedFloat_ZeroCoupon and Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon functions:

Removed the OR statement checking the applicability of Qualify_Transaction_ZeroCoupon_KnownAmount in both functions.
Corrected one of the InterestRatePayout type conditions:

Updated the RateSpecification condition to check the presence of priceQuantity instead of principalPayment.

# Mapping Updates - KnownAmountSchedule Mappings
_Background_

The PriceQuantity>priceSchedule component is not represented in the InterestRatePayout when we have a sample with knownAmountSchedule. This release updates the mappings so that knownAmountSchedule is mapped to the priceSchedule component.

_What is being released?_

Updated mappings for PriceSchedule to FpML knownAmountSchedule.

# FpML 5.13 Working Draft 3 Mapping Updates - Post Trade Risk Reduction Mapping Update
_Background_

In FpML 5.13 Working Draft 3, support was introduced for PTRR-related items including PTRR originating events
and PTRR party roles. The present relase adds mappings for these items into CDM and FpML samples containing
the new items for ingestion.

_What is being released?_

Updated PTRR-related mappings.
Added sample to fpml-5-13/events/
msg-ex69-execution-advice-commodity-swap-classification-new-trade-esma-emir-refit
msg-ex70-execution-advice-commodity-swap-classification-termination-esma-emir-refit

# FpML Coding Schemes 2.16 Mapping Updates - Floating Rate Index Mappings
_Background_

FloatingRateIndexEnum is automatically updated in CDM from FpML releases, but corresponding mappings and related functions are not. The present release updates mappings and functions to FpML floatingRateIndexScheme version 3.8 published as part of FpML Coding Schemes version 2.16.

_What is being released?_

Updated mappings for FloatingRateIndexEnum to FpML floatingRateIndexScheme version 3.8.
Updated function Qualify_Transaction_OIS FpML floatingRateIndexScheme version 3.8.

# Eligible Collateral Schedule Model - CheckEligibilityResult cardinality fix
_Background_

The CheckEligibilityResult data type holds the data returned by the
CheckEligbilityByDetails function which is used to see whether certain
assets or issuers are eligible to be posted as collateral for a given
collateral eligibility schedule.

_What is being released?_

This release updates the CheckEligibilityResult data type. Specifically,
the cardinality on two attributes has been corrected such that:

matchingEligibleCriteria can be empty if there is no match (i.e. the
collateral is not eligible)
eligibilityQuery must be present as this is a copy of the query input
provided to the function.

# Product Model - ISO Country Code Enum Update
_Background_

Rosetta has a feature for its enumerations that enables, if a certain enumeration is directly related to an FpML or ISO coding scheme, to label that enumeration with the corresponding coding scheme canonical URI, so every time that coding scheme is updated, the enumeration will be automatically updated.

_What is being released?_

_Backward-incompatible changes_

This release updates ISOCurrencyCodeEnum to keep it in sync with the latest ISO 4217 coding scheme.

Removed enum value SLL <"Leone">, should be mapped to SLE instead.

# Product Model - Qualification - Bond Forwards
_Background_

Qualification function Qualify_AssetClass_InterestRate does not qualify bond forwards correctly. The function alias that should extract the forward payout is instead extracting the option payout. This is described in issue #2601.

_What is being released?_

Function cdm.product.qualification.Qualify_AssetClass_InterestRate has been updated to resolve the issue and cater for forward payouts
Bond forward FpML samples, and corresponding FpML synonym mappings, have been added

# Event Model - Trade Lot Identifier added to Execution Instruction
_Background_

In order for quantityChange instructions to impact an existing tradeLot, the executionInstruction requires a tradeLot identifer to be present.

_What is being released?_

Added lotIdentifier attribute (optional) to ExecutionInstruction
In Create_Execution function, the lotIdentifier attribute is used when creating the execution's TradeLot object#

# Eligible Collateral Schedule Model - Determination of the Party Roles
_Background_

Subject matter experts for collateral trade management have identified that a self-contained representation of eligible collateral criteria requires associating each involved party with the various roles specififed. A typical example is the identification of which party is Payer or Receiver of collateral.

_What is being released?_

A new PartyRole attribute has been added to the EligibleCollateralSpecification data type.

# Product Model - Natural Person and NaturalPersonRole circular reference
_Background_

An issue regarding a circular reference inside the NaturalPerson type was recently found in the model.
NaturalPerson and NaturalPersonRole are located at the same level inside Party, to follow the same structure that Party and PartyRole have inside the Trade type. The circular reference issue appears because the NaturalPerson type also contains a NaturalPersonRole, which references back to the containing type of NaturalPerson, causing a circular reference in the model.

_What is being released?_

This release fixes this issue by removing the NaturalPersonRole inside the NaturalPerson type.

_Backward-incompatible changes_

Removed the personRole attribute of type NaturalPersonRole from NaturalPerson.

# Event Model - Valuation Update
_Background_

This release introduces new types and functions to support the native representation and qualification of a valuation update. The valuation update can add to or replace the existing valuation history.

_What is being released?_

The following features have been added:

Representation of the primitive instruction for a valuation event.
Application of the valuation primitive instruction to change the trade state.
Qualification of a valuation update event.
Data types and attributes

Added new ValuationInstruction type with the following attributes:
valuation attribute of type Valuation
replace attribute of type boolean
valuation attribute of type ValuationInstruction added to PrimitiveInstruction.
Functions

Added new Create_Valuation function.
Updated Create_TradeState function to include a valuation update step using Create_Valuation.
Qualification

Added new Qualify_ValuationUpdate function.

# Product Model - Qualification Functions for Zero-coupon Swaps
_Background_

The qualification function for a zero-coupon swap is too restrictive in CDM, since it requires that all the payout legs should feature one unique payment at Term. Normally a zero coupon only points out that at least one leg has a unique payment made at term.

This release fixes this, along with some inaccurate provisions of qualifying functions regarding zero-coupon swaps. In addition, a new Qualify_Transaction_ZeroCoupon_KnownAmount function was added to facilitate the classification of Zero Coupon swaps with a Known Amount.

This release also removes the use of conditional synonym mappings from FpML to CDM of the PrincipalPayments element for Zero-Coupon Swaps with Known Amount cases. The Qualify_SubProduct_FixedFloat qualification function has also been updated to not require the use of this PrincipalPayments element.

_What is being released?_

Minor changes to existing Zero-Coupon Swap qualification functions
Addition of a new Zero-Coupon Swap Known Amount qualification function
Removed the conditional synonym mappings from FpML to CDM for the PrincipalPayments element in scenarios with Zero-Coupon Swaps with Known Amount.
Qualification

Updated Qualify_Transaction_ZeroCoupon function to accurately qualify Zero-Coupon swaps.
Updated Qualify_SubProduct_FixedFloat function to include Zero-Coupon swaps with Known Amount.
Updated Qualify_InterestRate_IRSwap_FixedFloat_ZeroCoupon function to include Zero-Coupon swaps with Known Amount.
Updated Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon function to include Zero-Coupon swaps with Known Amount.
Created new Qualify_Transaction_ZeroCoupon_KnownAmount function.
Updated the annotations of Qualify_InterestRate_IRSwap_FixedFloat_ZeroCoupon, Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon and Qualify_InterestRate_InflationSwap_Basis_ZeroCoupon functions to accurately describe the qualified products.
Translate

Deprecated use of synonym mapping from FpML to CDM for the PrincipalPayments element for Zero-Coupon Swaps with Known Amount cases.
