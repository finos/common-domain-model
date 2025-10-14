# Pre-trade - Addition of `[rootType] `to AvailableInventory and SecurityLocate

_Background_

The `AvailableInventory` and `SecurityLocate` types are top level types used for modelling the securities lending pre-trade processes of where a lender distributes details of the securities they have available and where a borrower can request specific securities respectively.

Adding the `[rootType]` annotation to these two types sets them as top level types and also makes them accessible through the Object Builder, making it easier for modellers to build JSON examples of their structure.

_What is being released?_

- Addition of `[rootType]` annotation to `AvailableInventory`
- Addition of `[rootType]` annotation to `SecurityLocate`

_Review Directions_

Changes can be reviewed in PR: [#4105](https://github.com/finos/common-domain-model/pull/4105)
