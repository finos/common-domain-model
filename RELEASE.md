# _Ingestion - FpML model to CDM model Ingest_

_Background_

As outlined in the [#4031](https://github.com/finos/common-domain-model/issues/4031), the ingest functions for FpML Confirmation to CDM can be contributed to the CDM 6 version.

_What is being released?_

This release contains functions to ingest data from the FpML Confirmation model into CDM. This PR includes:

- Ingest functions for mapping FpML Confirmation to CDM
- Regression tests (test packs and expected results)
- `FpML as Rune` model as an upstream dependency

_Ingest functions_

Ingest functions have been written to map the attributes from a `FpML as Rune` model type to a CDM type.  For example, the function `cdm.ingest.fpml.confirmation.message.Ingest_FpmlConfirmationToTradeState` maps from the `FpML as Rune` type `fpml.confirmation.Document` to the CDM type `cdm.event.common.TradeState`.

```
func Ingest_FpmlConfirmationToTradeState:
    [ingest XML]
    inputs:
        fpmlDocument fpml.Document (0..1)
    output:
        tradeState TradeState (0..1)
```

These functions can be found in multiple namespaces within `cdm.ingest.fpml`.

_Regression tests - test packs and expectations_

The mapping functions have been validated using the same ingestion test packs as the existing Synonym ingestion, and provide an equivalent mapping coverage.

The test packs and expectations for the FpML Confirmation Ingest functions can be found in the folder `ingest`.

_`FpML as Rune` model_

The FpML Confirmation schemas (.xsd) has been imported into a `FpML as Rune` model, where each schema type has a corresponding Rune data type.  e.g. FpML `<dataDocument>` has been imported into the model as type `fpml.confirmation.DataDocument`.

This model is hosted as a public Github repository, see [FpML as Rune](https://github.com/rosetta-models/rune-fpml).  `FpML as Rune` is an upstream dependency of CDM.

_Mapping Diagnostics_

The release includes ingestion mapping diagnostics that provide comparison between synonym-based and function-based ingestion.

- Mapping coverage diagnostics can be found [here](https://github.com/finos/common-domain-model/tree/6.x.x-ing.x/tests/src/test/resources/ingest/diagnostics)
- Mapping diffs files can be found [here](https://github.com/finos/common-domain-model/tree/6.x.x-ing.x/tests/src/test/resources/ingest/expected-output)

_Backward compatibility_

This release contains a new set of Rune namespaces containing ingest functions, so there are no backwards compatibility concerns.

_Review directions_

Changes can be reviewed in PR: [#4166](https://github.com/finos/common-domain-model/pull/4166)
