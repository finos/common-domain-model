# *CDM Model: Legal Document Modelling*

_What is being released_

Resolve complex ISDA Create mapping issues for legal documents:

 - Addresses for Transfer/Demands and Notices - Addresses are now mapped for the specified party
 - Related Agreements – `COLLATERAL_TRANSFER_AGREEMENT`, `MASTER_AGREEMENT` and `SECURITY_AGREEMENT` agreement types are now mapped for related agreements
 - Posting Obligations - Additional_language is now correcty mapped to the specified party.
 - Recalculation of Value - Elections are now correcty mapped to the specified party.
 - French Law Addendum – Bespoke language are now associated with correct party on CTA ISDA Bank Custodian 2019.
 - Regime/Security Provider Rights Event/Security Taker Rights Event - Elections are now correcty mapped to the specified party.

_Review direction_

In the Ingestion Panel, try samples in folder isda-create.

 - clearstream-cta-2016-englaw/sample1 - see `partyElection -> address` mapped to correct party.
 - clearstream-sa-2016-luxlaw/sample1 - see `agreementType -> COLLATERAL_TRANSFER_AGREEMENT` mapped for the releated agreement for date 2020-04-03
 - isda-csa-im-2016-jpnlaw/sample1 - see `postingObligations -> partyElection` mapped for partyA and partyB
 - clearstream-cta-2019/sample1 - see `recalculationOfValue -> partyElection` correctly associate partyA and partyB in `disputeResolution`.
 - euroclear-cta-2019/sample1 - see `jurisdictionRelatedTerms -> frenchLawAddendum` correctly associate the `addendumLanguage` with partyB.
 - isda-csa-im-2018-nylaw/sample1 - see `rightsEvents -> securityProviderRightsEvent` correctly associate partyA and partyB for `partyElection`.
