# *CDM Model: Legal Document Modelling*

_What is being released_

Resolve complex ISDA Create mapping issues for legal documents:

- Convert ISDA Create currency descriptions into ISO Country Code (iso4217)
- `Access Conditions` - fix mapping of Additional Termination Events
- `Amendment to Termination Currency` - fix mapping of three party election structure, and agreement date.
- `SIMM Calculation Currency` - fix mapping of elections.
- `Collateral Management Agreement` - fix mapping of elections.
- `Custodian Event End Date` - fix mapping issues and add `CustodianEventEndDate.safekeepingPeriodExpiry` to accommodate Clearstream CTA documents.
- Remove `LegalDocumentBase.partyInformation` as it duplicates `LegalDocumentBase.contractualParty` and `LegalDocumentBase.otherParty`.

_Review Direction_

In the Ingestion Panel, try samples in folder `isda-create`.
