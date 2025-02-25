# *Initial Margin Model - Functional externalization*

_Background_

Following the latest contribution of the **Standardized Schedule Method** for calculating Initial Margin (IM) within the **Common Domain Model (CDM)**, decision has been made to partially externalize its functionality (i.e. contributing back to Core CDM some of the functions due to its broader utility).

_What is being released?_

_Namespace changes_

The following functions have been moved from the namespace `cdm.margin.schedule:func` to new namespaces:

- `UnderlierForProduct` has been renamed `UnderlierForOptionOrForwardProduct` and has been moved to cdm.product.template:func.
- `AdjustableOrAdjustedOrRelativeDateResolution`, `AdjustableDateResolution` and `AdjustableDatesResolution` have been moved to `cdm.base.datetime:func`.
- `FXFarLeg` has been moved to `cdm.product.template:func`.
- `DateDifferenceYears` has been moved to `cdm.base.datetime:func`.
- `IsFXNonDeliverableOption` has been renamed `Qualify_ForeignExchange_NDO` and has been moved to `cdm.product.qualification:func`.
- `Create_ExposureFromTrades` has been moved to `cdm.event.common:func`.

_Renamings_

The following functions have been renamed, even though they remain in the original workspace `cdm.margin.schedule:func`:

- `IsIRSwapWithCallableBermudanRightToEnterExitSwaps` has been renamed to `Qualify_InterestRate_SwapWithCallableBermudanRightToEnterExitSwaps`.
- `IsIRSwaptionStraddle` has been renamed to `Qualify_InterestRate_Swaption_Straddle`.
- `IsCreditNthToDefault` has been renamed to `Qualify_Credit_NthToDefault`.
- `IsFXNonDeliverableOption` has been renamed `Qualify_ForeignExchange_NDO`.
- `UnderlierForProduct` has been renamed `UnderlierForOptionOrForwardProduct`.

_Function modifications_

Also, the function `Qualify_ForeignExchange_VanillaOption` has been slightly modified in order to admit only physically delivered options (as the function for NDO has been created).

_Backward Incompatible_

The following changes are backward incompatible:

- All the function renamings specified in the _Renamings_ section and the modification specified in the Function modifications section.

_Review directions_

The changes can be reviewed in PR: [#3463](https://github.com/finos/common-domain-model/pull/3463)
