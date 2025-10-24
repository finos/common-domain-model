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

# Ingest - Continued Mapping for Equity Products

_Background_

Ingest functions for FpML Confirmation to CDM are available in the CDM 7-dev version, where they are available for beta testing by the CDM community.
There are still gaps in the original synonym mapping and ingest functional mapping which will be addressed in this PR.

_What is being released?_

Fixing mapping issues found within `equityswaptransactionsupplement`:

- `quantitySchedule`
- `unit` and `perUnit` in `SpreadSchedule`
- `firstOrSecondPeriod` in `dividendReturnTerms`
- `unit` and `perUnit` in `price`
- `adjustment` in `trade`

Fixing mapping issues found within `brokerequityoption`:

- Renaming `fpmlAutomaticExerciseIsApplicable` to `fpmlAutomaticExercise` in `common` namespace
- Mapping `exerciseProcedure` in `exerciseTerms`

_Review Directions_

Changes can be reviewed in PR: [#4068](https://github.com/finos/common-domain-model/pull/4068)
