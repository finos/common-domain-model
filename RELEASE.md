# _Event Model - CounterpartyPositionBusinessEvent - Cardinality of the After state_

_Background_

Following the release of counterparty position in the Common Domain Model, a modelling update is required. The cardinality of the attribute `after` of type `CounterpartyPositionBusinessEvent` has been relaxed to unbounded list `(0..*)`. This change will support the cases when multiple `after` position states are generated as a result of a _partial option exercise_ event being applied to the position.


_What is being released?_

- The cardinality of the `after` position state within `CounterpartyPositionBusinessEvent`is relaxed to unbounded list.

_Data types_

- `after` attribute of type `CounterpartyPositionState` updated to multiple cardinality.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes listed above.

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2539

# *Event / Product Model - FpML 5.13 Schema*

_What is being released?_

This release updates the FpML schema used for FpML to CDM ingestion. The version has been updated from `5.13 Second Working Draft` to `5.13 Third Working Draft`.  For further details, visit https://www.fpml.org/spec/fpml-5-13-3-wd-3/.

_Review Directions_

This change has no impact on the model or test expectations. The changes can be reviewed in PR [2542](https://github.com/finos/common-domain-model/pull/2542).