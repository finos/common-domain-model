# *Product Model - Adding `EquityIndexEnum` emum list*

_Background_

When defining equity assets in an eligible collateral schedule it would be beneficial to have an enumeration list of common equity indices. This was agree on the Collateral & Contribution Review Working Groups.

_What is being released?_

The Equity Index enum is defined in a new `staticdata.asset.equity.enum` namespace. The enum is added as an attribute under `EquityIndex` which extends `IndexBase`.

A condition restricts the `EquityIndex` type from having an enum value and a name.

_Review Directions_

Changes can be reviewed in PR: [#4013](https://github.com/finos/common-domain-model/pull/4013)
