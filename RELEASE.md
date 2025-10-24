## FPML - Add support for FpML Record-keeping schema

*Background*

This change increases the version of the upstream Rune FpML dependency to version `1.0.1`. This will allow use to support FpML Record-keeping messages when working with the Ingest 2.0 mapping function which was released as part of [#3866](https://github.com/finos/common-domain-model/issues/3836).  

*What is being released?*

The following namespaces have been updated:

- All Rosetta files mapping files with the naming convention `ingest-fpml-confirmation[...].rosetta` now contain updated imports to the consolidated FpML Confirmation and Record Keeping types.
- Update the following pipline config files with consolidated to level type and updated serialisation config file names:
  - `pipeline-translate-fpml-confirmation-to-trade-state.json`
  - `pipeline-translate-fpml-confirmation-to-workflow-step.json`
- Updated the following static Java classes with new consolidated namespaces:
  - `CreateAssetKeyImpl.java`
  - `CreateKeyForQuotedCurrencyPairImpl.java`
  - `CreateKeyImpl.java`

*Review Directions*

Changes can be reviewed in PR: [#4117](https://github.com/finos/common-domain-model/pull/4117)