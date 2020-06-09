# *CDM Model: Legal Document Modelling*

_What is being released_

Resolve complex `ISDA Create` mapping issues:

- `CalculationDateLocation` - resolve incorrect association of mapped items.
- `Custodian` - build mapper to associate Custodian Name and Account details for Stock vs Cash.
- `Threshold` / `MinimumTransferAmount` - update existing mapper to correctly deal with zero amount.
- `CustodyArrangements` - resolve incorrect association of mapped items.

_Review Direction_

In the Ingestion Panel, try samples in folder `isda-create`.

# *User Documentation: Event Model*

_What is being released_

Following recent overhaul of the Rosetta DSL documentation, the CDM documentation has been updated, focusing on:

- Event Model section
- Lifecycle Event Process section of the Process Model

An overview of the design principle of the CDM event model has been introduced, including an explanatory diagram for the different levels in the model and their relationships:

* Trade State
* Primitive Event
* Business Event
* Worksflow Step

All snippet examples have been updated to reflect their current status in the CDM.

The Process Model section has been expanded to include Primitive Event creation functions, and the Event Qualification section has also been updated, both reflecting consistent use of the `func` syntax in the Rosetta DSL.

_Review Direction_

In the CDM Documentation, review the CDM Model tab, in particular the sections:

- [Event Model section](https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#event-model)
- [Lifecycle Event Process section](https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#lifecycle-event-process)

# *CDM Model: Eligible Collateral Modelling*

_What is being released_

Eligible collateral model changes to support concentration limits and wrong way risk.

_Review Direction_

In the Textual Browser, review types `CollateralTreatment` and `ConcentrationLimitCriteria`.
