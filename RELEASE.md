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
