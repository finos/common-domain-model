# *Initial Margin Model - Enhancement and Optimization of the Standardized Schedule Method*

_Background_

Following the initial contribution of the **Standardized Schedule Method** for calculating Initial Margin (IM) within the **Common Domain Model (CDM)** and after receiving feedback from the working group, further work has been carried out to enhance the model by introducing new functionalities.

_What is being released?_

In this second contribution, improvements have been made to the model, categorized into three main areas: the creation of conditions, code optimization, and cosmetic changes.

_Key components of this release include:_
- New conditions have been added to validate the outputs of functions, ensuring they make sense from a business perspective.
- Code optimization has been implemented to reduce redundancy by avoiding repetitive use of qualifying functions within data extraction functions, resulting in improved efficiency.
- The name of one function has been updated, and some definitions have been expanded for better user understanding.

_Conditions_
- Added new `PositiveNotional` post-condition:
    - Ensure the notional is greater than 0.
- Added new `ValidCurrency` post-condition:
    - Ensure the currency is a valid ISO 3-Letter Currency Code.
- Added new `PositiveDuration` post-condition:
    - Ensure the duration is greater than 0.
- Added new `PositiveGrossInitialMargin` post-condition:
    - Ensure the gross initial margin is greater than 0.
- Added new `NonNegativeNetInitialMargin` post-condition:
    - Ensure net initial margin is non-negative.
- Added new `TotalGIMAddition` post-condition:
    - Ensure that only a single currency exists.
- Added new `NGRAddition` post-condition:
    - Ensure that only a single currency exists.

_Types_
- Modification to the `StandardizedSchedule` type
    - The following conditions have been added: `PositiveNotional` , `ValidCurrency` , and `PositiveDuration` .
- Modification to the `StandardizedScheduleTradeInfo` type
    - The attributes `grossInitialMargin` and `markToMarketValue`, which were previously of type `Quantity`, are now of type `Money`. Additionally, the conditions `PositiveGrossInitialMargin` and `SameCurrency` have been included.
- Modification to the `StandardizedScheduleInitialMargin` type
    - The condition `NonNegativeNetInitialMargin` has been added.

_Functions_
- Modification to the `BuildStandardizedSchedule` function
    - Aliases for `productClass` and `assetClass` have been introduced to serve as temporary variable assignments.
- Modification to the `StandardizedScheduleNotional` function
    - The qualifying functions have been substituted with the newly created aliases.
- Modification to the `StandardizedScheduleNotionalCurrency` function
    - The qualifying functions have been substituted with the newly created aliases.
- Modification to the `StandardizedScheduleDuration` function
    - The qualifying functions have been substituted with the newly created aliases.

_Rename_
- `GetStandardizedScheduleMarginRate` is now used instead of `GetIMRequirement`.

_Backward Incompatible_

The following changes are backward incompatible:

- All the function condition additions specified in the `Conditions` section of these release notes.
- All the type modifications specified in the `Types` section of these release notes.

The changes can also be reviewed in PR: [#3305](https://github.com/finos/common-domain-model/pull/3305).

# _Product Model_ - Commodity Payout Underlier

_Background_

The Asset Refactoring initiative (see [#2805](https://github.com/finos/common-domain-model/issue/2805)) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional financial products and markets. A proposal has been agreed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes an adjustment following three planned major tranches of work in CDM 6 to implement the refactored model.

_What is being released?_

In the original Asset Refactoring scope, the `underlier` on `CommodityPayout` was changed from being type `Product` to type `Commodity`.

This has proven to be too restrictive in DRR, where Commodity Payout can operate on a basket or index. Therefore, the data type of the underlier has been updated to `Underlier` with the added benefit of making it consistent with the other payouts.

To ensure that the underlier is indeed commodity-related, conditions have been added to force the `underlier` attribute to reference a commodity-related underlier.  The `CommodityUnderlier` condition uses a switch statement to evaluate whether the underlier is an `Observable` or `Product`, and to assess it accordingly.  A new function `ObservableIsCommodity` is used to standardise this and handles the different choice types of observables and the potential recursive nature of baskets.

_Backward-incompatible changes_

The change to the data type of the `underlier` attribute is not backward-compatible.

_Review directions_

The changes can be reviewed in PR: [#3277](https://github.com/finos/common-domain-model/pull/3277) or in Rosetta.

# *Infrastructure - Dependency Update*

_What is being released?_

This release updates the rune dependencies.

Version updates include:
- DSL 9.24.0: add a feature to override attributes in extended types. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.24.0
- DSL 9.25.0: improve type errors and cardinality errors. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.25.0

_Review directions_

The changes can be reviewed in PR: [#3302](https://github.com/finos/common-domain-model/pull/3302)

# *CDM Model - Collateral Criteria AND/OR Logic*

_Background_

This release enhances the modelling of Eligible Collateral Criteria to enable the use of complex AND, OR and NOT logic in the combination of terms within a criteria.

Eligible Collateral is currently modelled using the data type `EligibleCollateralSpecification` which can contain many `EligibleCollateralCriteria`, which are themselves constructed from `CollateralTreatment`, `IssuerCriteria` and `AssetCriteria`.
The attributes `isIncluded` (true/false) and `qualifier` (all/any) can be used to model some simple cases of and / or logic in the construction of certain parts of the criteria (eg `AgencyRatingCriteria`).

Members of the CDM Collateral Working Group have requested that the functionality is extended to enable more complex combinations of AND and OR logic across multiple terms.  This has been implemented by combining all the criteria terms in a single new data type `CollateralCriteria`.  This is a choice data type that includes all the criteria terms that previously appeared in `AssetCriteria` and `IssuerCriteria`.  As each term can only occur once, in its simplest form, only one criteria can be specified.  If more than one criteria is required, it is necessary to specify whether the terms are all required or only that any of them are required.  The two new data types `AllCriteria` and `AnyCriteria` also appear on `CollateralCriteria` to enable this.  If all terms are required, ie AND logic, then the terms should be linked in a parent `AllCriteria`.  If any of the terms are required, ie OR logic, then the terms should be linked in a parent `AnyCriteria`.  These two terms can be used iteratively to create complex logic between terms.  Additionally, the data type `NegativeCriteria` can also be used in the logic to apply a NOT function to a single term.

_What is being released?_

This release implements AND, OR and NOT logic between the Collateral terms.

There is no longer a separate data type for each of asset and issuer criteria; they have been combined in a single new data type called `CollateralCriteria`.
- The new choice data type `CollateralCriteria` replaces the removed `AssetCriteria` and `IssuerCriteria` data types as the combined type.
- The attributes `issuer` and `asset` on `CollateralCriteriaBase` have now been replaced with the single one `collateralCriteria` which is the specific criteria that applies. It can be created using AND, OR and NOT logic, and both asset and issuer characteristics.
- The conditions on `CollateralCriteriaBase` have been updated and now use the new `CriteriaMatchesAssetType` function.
- The data type `ConcentrationLimit` has been refactored to reduce the cardinality of `concentrationLimitCriteria` to 1 and the condition is updated accordingly.
- The condition on `ConcentrationLimitCriteria` has been updated to reflect the combined `CollateralCriteria`.
- Three new logic data types have been introduced to support the AND, OR and NOT logic of terms and are used in `CollateralCriteria`:
    - `AllCriteria`
    - `AnyCriteria`
    - `NegativeCriteria`,
- The following new data types have been introduced and are used in `CollateralCriteria`:
    - `IssuerCountryOfOrigin`
    - `AssetCountryOfOrigin`
    - `IssuerName`
    - `IssuerAgencyRating`
    - `SovereignAgencyRating`
    - `AssetAgencyRating`
    - `AssetMaturity`
    - `ListingExchange`
    - `ListingSector`
    - `DomesticCurrencyIssued`.

Changes to remove the old model:
- The data types `AssetCriteria` and `IssuerCriteria` have been removed.
- The `qualifier` attribute has been removed from `AgencyRatingCriteria` as it is now redundant.
- The data type `ListingType` has been removed.

In addition, the following functions have also been updated to reflect the new modelling:
- `CheckEligibilityByDetails` which now references a new function `CheckCriteria` which takes a single criteria and evaluates it against the criteria. This function handles the recursive use of AND and OR logic.
- `CheckAssetCountryOfOrigin` has been made more generic as the country of origin can apply to both an Issuer and an Asset; it has been renamed to `CheckCountryOfOrigin`.
- `CheckMaturity` has been updated to reflect the new modelling of `AssetMaturity`.
- `CriteriaMatchesAssetType` has been implemented to enable the enforcement of conditions on AssetType to be evaluated correctly.

Previously, the function `Create_EligibleCollateralSpecificationFromInstruction` enabled some limited automation of the creation of multiple Criteria with slightly differing logic. As this function no longer aligns to the new design, it has been deleted.  The following new functions have been added in its place:

- `CloneEligibleCollateralWithChangedTreatment`:  Creates a new Eligible Collateral Specification based on an input specification but with one changed criteria with a changed treatment.
- `CreateAndCriteria`: Combines multiple `CollateralCriteria` together using AND logic.
- `CreateOrCriteria`: Combines multiple `CollateralCriteria` together using OR logic.

Some changes have been made to the Java supporting code behind this functionality; see the changes in the following files:
- rosetta-source/src/test/java/org/isda/cdm/functions/FunctionInputCreationTest.java
- rosetta-source/src/main/java/org/finos/cdm/CdmRuntimeModule.java
- rosetta-source/src/main/java/cdm/product/collateral/functions/MergeEligibleCollateralCriteriaImpl.java.

Finally, the CDM documentation has been updated to reflect all the above changes.

_Backward incompatible changes_

Most of these changes are not backward compatible as they remove or significantly alter data types and attributes.

The expectations have been regenerated.  In addition, the test files used for the Check Eligibility and Merge Critera functions have also been updated.

_Review Directions_

Please inspect the changes identified above for the functions and types in the Textual Viewer Rosetta.

Please inspect the changes to option samples using the Ingestion Panel in Rosetta.

The changes can also be reviewed in PR: [#3296](https://github.com/finos/common-domain-model/pull/3296).
