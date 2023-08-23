# _Enhanced mappings for submittedForClearing timestamp_

_Background_

The mappings of the FpML `submittedForClearing` in DRR were pointing to the CDM `clearingSubmissionDateTime` in the `EventTimestampQualificationEnum`. The mappings of the FpML `submittedForClearing` should be pointing to the `clearingReceiptDateTime`. This contribution fixes this.

_What is being released?_

_Translate_

- Updated mapping coverage of `submittedForClearing` element for FpML and CME.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.
In the CDM Portal, select Ingestion and review the following samples:

cme-cleared-confirm-1-17/CME_ClearedConfirm_1_17

- Basis-ex01-LIBOR-vs-SOFR
- FRA-ex02
- IRS-ex02-Fixed-Float
- IRS-ex09-OIS
