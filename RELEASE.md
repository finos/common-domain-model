# *Legal Documentation - Valuation/Calculation Agent*

_Background_

D2LT and ISDA are working to enhance the legal documentation aspect of CDM. D2LT has reviewed the IM/VM and Legacy Credit Support documentation and is updating the model to accurately represent the clauses. This includes the elimination of duplications in the model.

_What is being released?_

1. Calculation Agent: Changed Enum to include the party making the demand.
2. Valuation Agent: Removed duplication (Collateral Valuation Agent). Renamed 'Legacy Valuation date/time/location' to 'Valuation date/time/location'.
3. CalculationValuationAgentPartyEnum and ValuationCalculationDateLocation: Standardised Types and enums used under both Valuation Agent and Calculation Agent.
4. General update to some descriptions and definitions.

_Review Directions_

Changes can be reviewed in PR: [#4080](https://github.com/finos/common-domain-model/pull/4080)
