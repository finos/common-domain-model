# _FpML 5.13 Working Draft 3 Mapping Updates - Post Trade Risk Reduction Mapping Update_

_Background_

In FpML 5.13 Working Draft 3, support was introduced for PTRR-related items including PTRR originating events
and PTRR party roles. The present relase adds mappings for these items into CDM and FpML samples containing
the new items for ingestion.

_What is being released?_

- Updated PTRR-related mappings.
- Added sample to fpml-5-13/events/
  msg-ex69-execution-advice-commodity-swap-classification-new-trade-esma-emir-refit
  msg-ex70-execution-advice-commodity-swap-classification-termination-esma-emir-refit

_Review directions_

In Rosetta, select the Textual Browser and inspect each of the changes identified above.
In Rosetta, select Ingestion and review the following samples:

fpml-5-13/record-keeping/events/
- msg-ex69-execution-advice-commodity-swap-classification-new-trade-esma-emir-refit
- msg-ex70-execution-advice-commodity-swap-classification-termination-esma-emir-refit

- The changes can be reviewed in PR https://github.com/finos/common-domain-model/pull/2638

# _FpML Coding Schemes 2.16 Mapping Updates - Floating Rate Index Mappings_

_Background_

`FloatingRateIndexEnum` is automatically updated in CDM from FpML releases, but corresponding mappings and related functions are not. The present release updates mappings and functions to FpML `floatingRateIndexScheme` version 3.8 published as part of FpML Coding Schemes version 2.16.

_What is being released?_

- Updated mappings for `FloatingRateIndexEnum` to FpML `floatingRateIndexScheme` version 3.8.
- Updated function `Qualify_Transaction_OIS` FpML `floatingRateIndexScheme` version 3.8.

_Review directions_

In Rosetta, select the Textual Browser and inspect each of the changes identified above.

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2633
