# _Cardinality change for after state within CounterpartyPositionBusinessEvent in CDM Core_

_Background_

Following the release of counterparty positions in the CDM model, a small modelling update is required regarding the cardinality of the `after` field contained within `counterpartyPositionBusinessEvent`. It was initially contributed as single cardinality `(0..1)`, and we raise the point that it should be modified to multiple cardinality `(0..*)`. This is done to support the case when multiple `after` position states are generated as a result of a _partial option exercise_ event being applied to the position. Additionally, it mirrors the way the `after` trade states within `businessEvent` currently operate for trades.


_What is being released?_

- Multiple cardinality for the `after` position state within `CounterpartyPositionBusinessEvent`.

_Data types_

- `after` attribute of type `CounterpartyPositionState` updated to multiple cardinality.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes listed above.