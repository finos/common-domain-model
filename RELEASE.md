# *CDM Model: Legal Document Modelling*

_What is being released_

Variation Margin model added and initial changes for Master Agreement Schedule model.

The following documents or specific clauses are now supported:

Variation Margin Agreements
- ISDA 2016 CSA for Variation Margin ("VM") (Security Interest - New York Law)
- ISDA 2016 CSA for VM (Title Transfer – English Law)
- ISDA 2016 CSA for VM (Loan – Japanese Law)
- ISDA 2016 CSA for VM (Title Transfer – Irish Law)
- ISDA 2016 CSA for VM (Title Transfer – French Law)

Master Agreement Schedule
- ISDA 2002 Master Agreement Schedule (Automatic Early Termination Clause only)

Other related changes:

- Model changes for Eligible Collateral and Margin Agreement Schedule.
- `ISDA Create` synonym mapping fixes.
- Updated documentation samples in the Legal Agreement section.

_Review direction_

- Review type `CreditSupportAgreementElections`, `MasterAgreementSchedule` and `EligibileCollateral`.
- Review `ISDA Create` ingestion samples in the CDM Portal Ingestion Page in folder `isda-create`.


# *CDM Model: Synonym Externalisation*

_What is being released_

As part of the ongoing namespace changes, synonyms defined inline in the CDM Model are being moved to their respective namespaces. The following have now been externalised:
 - `FpML_5_10`
 - `CME_SubmissionIRS_1_0`
 - `CME_ClearedConfirm_1_17`

_Review direction_

The extracted synonyms can be viewed in Rosetta Core:
- The FpML file is named `synonym-cdm-fpml.rosetta` under namespace `cdm.synonyms.fpml`.
- The CME file is named `synonym-cdm-cme.rosetta` under namespace `cdm.synonyms.cme`. 

The extracted synonyms can also be viewed in the CDM Portal Textual Browser by searching for `synonym source FpML_5_10`, `synonym source CME_ClearedConfirm_1_17` and `synonym source CME_SubmissionIRS_1_0`.    


# *Infrastructure: DAML Upgrade*

_What is being released_

Upgrade DAML version to use version 1.3.0 for DAML compilation.

_Review direction_

Download and review the DAML CDM distributed artifact from the CDM Portal Download page.


# *Infrastructure: Go Code Generation*

_What is being released_

Add a Go implementation of the CDM to the available downloads.

Notable changes and updates introduced:

- The generated Go source code contains all CDM model types and enums, and all function specifications.
- As Go language compilation is platform-specific, the CDM distributed artifact contains the source code only.
- The Go CDM distributed artifact is now available on the CDM Portal Downloads page.

_Review direction_

Download and review the Go CDM distributed artifact from the CDM Portal Download page.
