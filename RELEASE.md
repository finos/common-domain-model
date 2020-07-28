# *CDM Model: Legal Document Modelling*

_What is being released_

Variation Margin model added and initial changes for Master Agreement Schedule model.

The following documents are now supported:

- Variation Margin Agreements
- ISDA 2016 CSA for Variation Margin ("VM") (Security Interest - New York Law)
- ISDA 2016 CSA for VM (Title Transfer – English Law)
- ISDA 2016 CSA for VM (Loan – Japanese Law)
- ISDA 2016 CSA for VM (Title Transfer – Irish Law)
- ISDA 2016 CSA for VM (Title Transfer – French Law)
- Master Agreement Schedule
- ISDA 2002 Master Agreement Schedule (Automatic Early Termination Clause only)

Other related changes:

- Model changes for Eligible Collateral and Margin Agreement Schedule.
- `ISDA Create` synonym mapping fixes.
- Updated documentation samples in the Legal Agreement section.

_Review direction_

- Review type `CreditSupportAgreementElections`, `MasterAgreementSchedule` and `EligibileCollateral`.
- Review `ISDA Create` ingestion samples in the CDM Portal Ingestion Page in folder `isda-create`.