# *Observable - Adding `EquityIndexEnum` emum list*

_Background_

Members of the CDM Collateral working group requested an enumeration list of common equity indexes as a list to choose from when defining equity assets in an eligible collateral schedule.

_What is being released?_

The Equity Index enum is defined in a new `staticdata.asset.equity.enum` namespace. The enum is added as an attribute under `EquityIndex` which extends `IndexBase`.

A condition validates that the asset class is Equity. Another condition checks whether an enum value is used, and if so, restricts setting a name.

_Review Directions_

Changes can be reviewed in PR: [#4013](https://github.com/finos/common-domain-model/pull/4013)
