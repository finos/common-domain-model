# _Product Model - Trigger type refactoring_

_Background_

Currently, the values for representing specific elements such as barriers are done through the `level` and `levelPercentage` within the `Trigger` type . The interest in having them represented as `Price` types is strong since it is a richer type than a number.

_What is being released?_

A refactoring of the `Trigger` type element, modifying `level` and `levelPercentage` to a unified CDM element `level` of type `PriceSchedule`.

_Data types:_

- Updated `Trigger` type with a new unified `level` element of `PriceSchedule` type.
- The condition `Choice1` has been updated to be consistent with the proposal.
- Modification of the synonym mappings for `Trigger` type.

_Review directions_

In the Rosetta Platform, select the Textual View and inspect each of the changes identified above.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2927
