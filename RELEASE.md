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
