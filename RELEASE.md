
# _Collateral Model - Concentration Limit Update_

_Background_

A concentration limit is a type of collateral treatment defining concentration limits that may be applicable to eligible collateral criteria.

`ConcentrationLimitCriteria` is an attribute within `ConcentrationLimit` used to specify the criteria to which the limits apply. Within `ConcentrationLimitCriteria`, there is a condition which forces the user to select only one `ConcentrationLimitCriteria`.

However, because `ConcentrationLimitCriteria` extends `CollateralCriteriaBase` (where `collateralCriteria` is already a mandatory field), the condition always sets `collateralCriteria` as the `ConcentrationLimitTypeChoice`. Therefore, it is not possible to set `concentrationLimitType` or `averageTradingVolume`.

_What is being released?_

The update in this release allows users to set different types of concentration limits by relaxing cardinality on `collateralCritera` in `CollateralCriteriaBase` to (0..1). An `override` is added to the `collateralCriteria` attribute in `EligibleCollateralCriteria` to enforce the mandatory addition of a `collateralCriteria` to an eligible collateral schedule.

_Review Directions_

Changes can be reviewed in PR: [3690](https://github.com/finos/common-domain-model/pull/3690)

# _Reference Data - Update ISOCurrencyCodeEnum_

_What is being released?_

Updated `ISOCurrencyCodeEnum` based on updated scheme ISO Standard 4217.

Version updates include:
- added value: `XAD`


_Review directions_

Changes can be reviewed in PR: [#3699](https://github.com/finos/common-domain-model/pull/3699)
