# *Initial Margin Model - Functional Externalization*

_Background_

Following the latest contribution of the **Standardized Schedule Method** for calculating Initial Margin (IM) within the **Common Domain Model (CDM)**, the decision has been made to partially externalize its functionality (i.e. contributing back to Core CDM some of the functions for its broader utility).

_What is being released?_

**Namespace changes**

The following functions have been extracted from the namespace `cdm.margin.schedule:func` to new namespaces:

- `UnderlierForProduct` has been renamed `UnderlierForOptionOrForwardProduct` and has been moved to `cdm.product.template:func`.
- `AdjustableOrAdjustedOrRelativeDateResolution`, `AdjustableDateResolution` and `AdjustableDatesResolution` have been moved to `cdm.base.datetime:func`.
- `FXFarLeg` has been moved to `cdm.product.template:func`.
- `DateDifferenceYears` has been moved to `cdm.base.datetime:func`.
- `IsFXNonDeliverableOption` has been renamed `Qualify_ForeignExchange_NDO` and has been moved to `cdm.product.qualification:func`.
- `Create_ExposureFromTrades` has been moved to `cdm.event.common:func`.

**Renamed Functions**

The following functions have been renamed, even though they remain in the original workspace `cdm.margin.schedule:func`:

- `IsIRSwapWithCallableBermudanRightToEnterExitSwaps` has been renamed to `Qualify_InterestRate_SwapWithCallableBermudanRightToEnterExitSwaps`.
- `IsIRSwaptionStraddle` has been renamed to `Qualify_InterestRate_Swaption_Straddle`.
- `IsCreditNthToDefault` has been renamed to `Qualify_Credit_NthToDefault`.
- `IsFXNonDeliverableOption` has been renamed `Qualify_ForeignExchange_NDO`.
- `UnderlierForProduct` has been renamed `UnderlierForOptionOrForwardProduct`.

**Function Modifications**

Also, the function `Qualify_ForeignExchange_VanillaOption` has been slightly modified in order to admit only physically delivered options (as a function for NDO has been created).

_Backward-incompatible changes_

All the functions specified in the **Renamed Functions** and **Function Modifications** sections above are backward-incompatible.

_Review Directions_

The changes can be reviewed in PR: [#3463](https://github.com/finos/common-domain-model/pull/3463)
