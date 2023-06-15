---
title: Mapping (Synonym)
---

In order to facilitate the translation of existing industry messages
(based on open standards or proprietary ones) into CDM, the CDM is
mapped to a set of those alternative data representations using the
Rosetta DSL *synonym* feature, as described in the [Mapping Component
Section](https://docs.rosetta-technology.io/rosetta/rosetta-dsl/rosetta-modelling-component#mapping-component).

The following set of synonym sources are currently in place for the CDM:

-   **FpML standard** (synonym source: `FpML_5_10`): synonyms to the
    version 5.10 of the FpML standard
-   **FIX standard** (synonym source: `FIX_5_0_SP2`): synonyms to the
    version 5.0 SP2 of the FIX protocol
-   **ISO 20022 standard** (synonym source: `ISO_20022`): synonyms to
    the ISO 20022 reporting standard, with no version reference at
    present
-   **Workflow event** (synonym source: `Workflow_Event`): synonyms to
    the *event.xsd* schema used internally in Rosetta to ingest sample
    lifecycle events
-   **DTCC** (synonym sources: `DTCC_11_0` and `DTCC_9_0`): synonyms to
    the *OTC_Matching_11-0.xsd* schema used for trade matching
    confirmations, and to the *OTC_Matching_9-0.xsd* schema used for
    payment notifications, both including the imported FpML schema
    version 4.9.
-   **CME** (synonym sources: `CME_ClearedConfirm_1_17` and
    `CME_SubmissionIRS_1_0`): synonyms to the *cme-conf-ext-1-17.xsd*
    schema (including the imported FpML schema version 5.0) used for
    clearing confirmation, and to the *bloombergTradeFixml* schema
    (including the imported FpML schema version 4.6) used for clearing
    submission
-   **AcadiaSoft** (synonym source: `AcadiaSoft_AM_1_0`): synonyms to
    version 1.0 of AcadiaSoft Agreement Manager
-   **ISDA Create** (synonym source: `ISDA_Create_1_0`): synonyms to
    version 1.0 of the ISDA Create tool for Initial Margin negotiation
-   **ORE** (synonym source: `ORE_1_0_39`): synonyms to version 1.0.39
    of the ORE XML Model

Those synonym sources are listed as part of a configuration file in the
CDM using a special `synonym source` enumeration, so that the synonym
source value can be controlled when editing synonyms.
