# *Legal Documentation - Valuation/Calculation Agent*

_Background_

D2LT and ISDA are working to enhance the legal documentation aspect of CDM. D2LT has reviewed the Initial Margin / Variation Margin (IM/VM) and Legacy Credit Support documentation and is updating the model to accurately represent the clauses. This includes reducing duplication in the model where possible.

_What is being released?_

Calculation Agent: 
- Changed the party type from `CounterpartyRoleEnum` to `CalculationValuationAgentPartyEnum`.
- Updated type and attribute descriptions.

Valuation Agent:
- Removed duplicate `CollateralValuationAgent`
- Renamed `LegacyValuationDate`/`LegacyValuationTime`/`LegacyValuationDate` to `ValuationDate`/`ValuationTime`/`ValuationLocation`.
- Updated type and attribute descriptions.

`CalculationValuationAgentPartyEnum` and `ValuationCalculationDateLocation`: 
- Standardised types and enums used under both Valuation Agent and Calculation Agent.


_Review Directions_

Changes can be reviewed in PR: [#4080](https://github.com/finos/common-domain-model/pull/4080)
