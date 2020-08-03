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

- The CME file is named `synonym-cdm-cme.rosetta` under namespace `cdm.synonyms.cme`. 
- The FpML file is named `synonym-cdm-fpml.rosetta` under namespace `cdm.synonyms.fpml`.


# *Infrastructure: DAML Upgrade*

_What is being released_

Upgrade DAML version to use version 1.3.0 for DAML compilation.

_Review direction_

Download the DAML artifact from the CDM Portal Downloads page.


# *Infrastructure: Go Code Generation*

_What is being released_

Add a GO implementation of the CDM to the available downloads.

Notable changes and updates introduced:

- New tile added to Downloads page offering the option to download GO.

_Review direction_

Download the GO artifact from the CDM Portal Downloads page.
