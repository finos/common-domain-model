# _CDM Core - Multiple Cardinality for the CounterpartyPositionBusinessEvent After state_

_Background_

Following the release of counterparty positions in the Common Domain Model, a modelling update is required. The attribute `after` of type `CounterpartyPositionBusinessEvent`. has been modified to be multiple cardinality `(0..*)`. The rationale for this is  to support the case when multiple `after` position states are generated as a result of a _partial option exercise_ event being applied to the position.


_What is being released?_

- Multiple cardinality for the `after` position state within `CounterpartyPositionBusinessEvent`.

_Data types_

- `after` attribute of type `CounterpartyPositionState` updated to multiple cardinality.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes listed above.