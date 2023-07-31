# *Infrastructure - Dependency Update*

_What is being released?_

This release updates the rosetta-dsl dependency:

- rosetta-dsl:
  - 8.3.5: Bug fix related to filtering of items that are null. This change has no impact on the CDM model or test expectations.  For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/8.3.5
- rosetta-bundle:
  - 7.5.9: Latest FpML scheme (2.14) published 2023-07-28.  This change updates the model for enum `FloatingRateIndexEnum` with new values `MXN_TIIE_ON`, `MXN_TIIE_ON_OIS_Compound`, `ZAR_ZARONIA` and `ZAR_ZARONIA_OIS_Compound` as per the FpML scheme 2.14.
