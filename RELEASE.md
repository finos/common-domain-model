# *CDM Model: Event namespace classification*

_What is being released_

This refactor is the fifth incremental change that will further transform the org.isda.cdm file into a hierarchical namespace.

This fifth refactor includes the changes for the __cdm.event.*__ set of namespaces.

The namespaces contain components used across the CDM for 
* Business event concepts: primitives, contract state and associated state transition function specifications,
* and Workflow concepts (orthogonal to business event): time stamp, credit limit, trade warehouse info and associated function specifications.

_Review Direction_

In Rosetta Core (https://ui.rosetta-technology.io/), review the File or Namespace structure in the Editor Textual View. In the CDM Portal, 
navigate to the Downloads tile, then download artefacts in Java, DAML, Typescript or Scala distribution format and review the reorganised source folder with new cdm.event.* files.

# *CDM Model: Regulation namespace classification*

_What is being released_

This refactor is the fifth incremental change that will further transform the org.isda.cdm file into a hierarchical namespace.

This fifth refactor includes the changes for the __cdm.regulation.*__ set of namespaces.

The namespaces contain components used across the CDM for 
* Regulatory reporting concepts: regulatory rules, report definitions, reporting formats,
* and ISO standard concepts.

_Review Direction_

In Rosetta Core (https://ui.rosetta-technology.io/), review the File or Namespace structure in the Editor Textual View. In the CDM Portal, 
navigate to the Downloads tile, then download artefacts in Java, DAML, Typescript or Scala distribution format and review the reorganised source folder with new cdm.regulation.* files.

# *CDM Model: Position namespace classification*

_What is being released_

This refactor is the fifth incremental change that will further transform the org.isda.cdm file into a hierarchical namespace.

This fifth refactor includes the changes for the __cdm.position.*__ set of namespaces.

The namespaces contain components used across the CDM for Position concepts: portfolio and portfolio aggregation

_Review Direction_

In Rosetta Core (https://ui.rosetta-technology.io/), review the File or Namespace structure in the Editor Textual View. In the CDM Portal, 
navigate to the Downloads tile, then download artefacts in Java, DAML, Typescript or Scala distribution format and review the reorganised source folder with new cdm.position.* files.

# *CDM Model: Deprecated types, enums & funcs*

_What is being released_

Following objects to be deprecated.

Types:
* CommoditySet
* BondOptionStrike
* CalculationAmount
* CalculationAgentModel
* PackageInformation
* DeterminationMethod
* ExerciseEvent
* TradeDate

Enums: 
* StandardSettlementStyleEnum
* OriginatingEventEnum
* PaymentStatusEnum
* NoThresholdEnum
* PackageTypeEnum

Functions:
* EquityAmountPayer
* ResolvePrice 