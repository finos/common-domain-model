# _Event Model - FpML Synonym Mappings for Ingested Events_

_Background_

This release introduces mapping support for ingested FpML messages representing events, in alignment with the recommendation of the CDM Architecture and Review Committee to capture the event under the intent flag.

_What is being released?_

The release enhances the FpML mapping coverage for  terminating events, extending the mapping of the CDM `intent` flag with the following values of the `EventIntentEnum`: `NOVATION`, `COMPRESSION`, `OPTION_EXERCISE`, `ALLOCATION` and `CREDIT_EVENT`.

Furthermore, it introduces mapping coverage to facilitate the ingestion of FpML messages representing Option Exercises.

_Review directions_

In the CDM Portal, select the Textual Browser, navigate to the file cdm/mapping/fpml/confirmation/workflowstep/synonym, and inspect the changes identified above.

PR: https://github.com/finos/common-domain-model/pull/2376
