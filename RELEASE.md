# *CDM Model: Event & Regulation namespace classification*

_What is being released_

This refactor is the fifth incremental change that will further transform the org.isda.cdm file into a hierarchical namespace.

This fifth refactor includes the changes for the __cdm.event.*__ & __cdm.regulation.*__ set of namespaces.

The namespaces contain components used across the CDM for 
* Business event concepts: primitives, contract state and associated state transition function specifications
* Workflow concepts (orthogonal to business event): time stamp, credit limit, trade warehouse info and associated function specifications
* Position concepts: portfolio and portfolio aggregation
* ISO standard concepts.

_Review Directions_

In Rosetta Core (https://ui.rosetta-technology.io/), review the File or Namespace structure in the Editor Textual View. In the CDM Portal, 
navigate to the Downloads tile, then download artefacts in Java, DAML, Typescript or Scala distribution format and review the reorganised source folder with new cdm.event.* files.

# *CDM Model: Deprecated types & functions*

_What is being released_

Following objects to be marked deprecated.

Types:
* CommoditySet
* BondOptionStrike
* CalculationAmount
* CalculationAgentModel
* PackageInformation
* DeterminationMethod
* ExerciseEvent
* TradeDate

Functions:
* EquityAmountPayer
* ResolvePrice

# *User Documentation: Regulatory Reporting Syntax*

_What is being released_

A new section has been added to the Rosetta DSL documentation that explains the regulatory reporting components and the syntax that can be used to write reporting logic.

This update will allow external contributors to write further regulatory reporting components in the CDM, in order to scale-up the effort of digitising regulatory rules and associated best practices.

_Review Directions_

In the CDM Portal, navigate to the ISDA CDM Documentation tile and find the "Reporting Component" under the Rosetta DSL > Rosetta Modelling Components section.

Alternatively, documentation section is directly accessible at: https://docs.rosetta-technology.io/dsl/documentation.html#reporting-component.
 