# *Digital Regulatory Reporting: FpML Event Ingestion*

_What is being released_

As part of the Digital Regulatory Reporting workstream, the FpML record-keeping schema, and a corresponding ingestion environment, has been added to demonstrate CDM integration with FpML reporting.
 
The FpML record-keeping schema is intended to be used for reporting the primary economic terms of derivative transactions to swaps data repositories.  FpML messages, such as `nonpublicExecutionReport`, can now be ingested into a CDM event, e.g. `WorkflowStep`, which can then be processed by CDM regulatory reporting logic.

_Review Directions_

In the CDM Portal Ingestion section, review new FpML record-keeping samples:

- record-keeping > record-ex01-vanilla-swap
- record-keeping > record-ex02-vanilla-swap-datadoc
- record-keeping > record-ex100-new-trade

In the Rosetta Core, use the text editor to review the new synonyms in file `synonym-cdm-fpml-recordkeeping.rosetta`.
