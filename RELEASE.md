# _Event Model - CME and FpML synonym mappings for submitted For Clearing timestamp_

_Background_

The synonym mappings to the CME and FpML messagge element `submittedForClearing` in CDM were incorrectly pointing to the CDM enum value `clearingSubmissionDateTime` in the `EventTimestampQualificationEnum` data type. These mappings should be pointing to the `clearingReceiptDateTime` enum value. This contribution addresses this issue.

_What is being released?_

- Updated  FpML and CME synonym mappings for `submittedForClearing` message element to point to `clearingReceiptDateTime`.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes described above.
In the CDM Portal, select the Ingestion panel and review the following samples:

- cme-cleared-confirm-1-17/CME_ClearedConfirm_1_17
- Basis-ex01-LIBOR-vs-SOFR
- FRA-ex02
- IRS-ex02-Fixed-Float
- IRS-ex09-OIS
