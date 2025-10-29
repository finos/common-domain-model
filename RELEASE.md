## Ingestion Framework - Retire legacy synonym mappings for FpML

*Background*

FpML to CDM mapping functions were recently contributed to CDM 7-dev ([#3836](https://github.com/finos/common-domain-model/issues/3836)). They will replace the legacy synonym mappings which were less intuitive and relied on Java Mappers in scenarios when the Rune syntax was not powerful enough to perform the required transformation.

To avoid duplicate FpML mappings (i.e. synonym and functions), the old FpML-based synonym mappings and associated infrastructure should be removed. For further details see [#4030](https://github.com/finos/common-domain-model/issues/4030).

*What is being released?*

The removal of all FpML-related synonym mappings, related regression tests, and associated test pack samples in the following namespaces: 
- `cdm.mapping.fpml.confirmation.tradestate`
- `cdm.mapping.fpml.confirmation.workflowstep`
- `cdm.mapping.cme`
- `cdm.mapping.dtcc`

*Review Directions*

Changes can be reviewed in PR: [#4114](https://github.com/finos/common-domain-model/pull/4114)
