# _Ingestion - FpML Confirmation model to CDM model Ingest_

_Background_

As outlined in the [Implementation Update Q2 2025](https://github.com/finos/common-domain-model/issues/3364#issuecomment-2957178892), the ingest functions for FpML Confirmation to CDM can be contributed to the CDM 7-dev branch. This enables early testing by the CDM community.

This milestone provides an opportunity to gather feedback, validate the implementation, and ensure alignment with community needs.

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

_Backward compatibility_

This release contains a new set of Rune namespaces containing ingest functions, so there are no backwards compatibility concerns.

_Review directions_

Changes can be reviewed in PR: [3996](https://github.com/finos/common-domain-model/pull/3996)

# *Infrastructure - Security Update*

_What is being released?_

Third-party software libraries updated to comply with the “Common Vulnerabilities and Exposures” standard (CVE, https://www.cve.org/).

- `npm/axios` upgraded from version 0.25.0 to 0.30.1, see [GHSA-jr5f-v2jv-69x6](https://github.com/advisories/ghsa-jr5f-v2jv-69x6) for further details
- `npm/image-size` upgraded from version 1.2.0 to 1.2.1, see [GHSA-m5qc-5hw7-8vg7](https://github.com/advisories/GHSA-m5qc-5hw7-8vg7) for further details
- `npm/trim` upgraded from version 0.0.1 to 0.0.3, see [CVE-2020-7753](https://cve.mitre.org/cgi-bin/cvename.cgi?name=CVE-2020-7753) for further details

_Review Directions_

Changes can be reviewed in PR: [#3998](https://github.com/finos/common-domain-model/pull/3998)
