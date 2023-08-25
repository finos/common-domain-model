# *Product Model - FpML synonym mappings for Price Components*

_Background_

FpML mapping regressions were identified following recent model improvements related to price components (see [#2183](https://github.com/finos/common-domain-model/issues/2183)) released in version [5.0.0-dev.13](https://github.com/finos/common-domain-model/releases/tag/5.0.0-dev.13). 

_What is being released?_

This release resolves the mapping regressions and updates FpML mapping synonyms for FX and Cap / Floor products.

_Review directions_

In the CDM Portal, select Ingestion and review the following samples:

fpml-5-10 / incomplete-products / fx-derivatives
- fx-ex13-fx-dbl-barrier-option
- fx-ex29-fx-swap-with-multiple-identifiers

fpml-5-10 / products / fx
- fx-ex03-fx-fwd
- fx-ex05-fx-fwd-w-ssi
- fx-ex07-non-deliverable-forward
- fx-ex12-fx-barrier-option
- fx-ex28-non-deliverable-w-disruption

fpml-5-10 / products / rates
- ird-ex22-cap-spread
- ird-ex23-floor-spread
- ird-ex24-collar
- ird-ex27-inverse-floater
- ird-ex35-inverse-floater-inverse-vs-floating

# *Party Model - Triparty Agent Enumeration*

_What is being released?_

Minor enhancement to add `TripartyAgentEnum` to `PartyRoleEnum`.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect changes mentioned above.

# *Event Model - Trade Date Time*

_Background_

`Trade` type does not include date, time and business center that may be required for contractual purposes.

_What is being released?_

This enhancement adds optional attribute `tradeTime` to types `Trade` and `ExecutionInstruction`.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect changes mentioned above.
