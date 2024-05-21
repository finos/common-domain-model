# _Product Model - Refactoring of level and levelPercentage within Trigger CDM_

_Background_
Currently, `level` and `levelPercentage` represent the Trigger values for specific elements such as barriers. The interest in having them represented as Price types is strong since it is a richer type than a number.

_What is being released?_

A refactoring of 'level` and `levelPercentage` within Trigger CDM to a unified CDM element `level` of type PriceSchedule.

_Data types:_

- Updated `Trigger` type with a new unified level element of PriceSchedule type.
- The condition `Choice1` has been updated to be consistent with the proposal.
- Modification of the synonym mappings for Trigger type.

_Review directions_

In the Rosetta Platform, select the Textual View and inspect each of the changes identified above.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2878
