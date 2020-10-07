# *CDM Model: Remaining model in org.isda.cdm namespace classification*

_What is being released_

This refactor is the sixth and final incremental change that will further transform the org.isda.cdm file into a hierarchical namespace.
This refactor includes the reorganisation of some model objects to existing namespaces while other being deleted (as they are not being used in CDM).

Types to be moved to existing namespaces:

* Types: CalculationAgentModel moved to __cdm.product.template__ namespace. 
* Types: PackageInformation & ExerciseEvent moved to __cdm.event.common__ namespace.
* Enums: StandardSettlementStyleEnum moved to _cdm.product.common_.
* Funcs: NewEquitySwapProduct, NewSingleNameEquityPayout & NewFloatingPayout moved to __cdm.event.common__ namespace.

Objects to be deleted:

* Types: CommoditySet, BondOptionStrike, CalculationAmount & DeterminationMethod.
* Enums: OriginatingEventEnum, PaymentStatusEnum & PackageTypeEnum.
* Funcs: EquityAmountPayer & ResolvePrice. 

_Review Directions_

In Rosetta Core (https://ui.rosetta-technology.io/), review the File or Namespace structure in the Editor Textual View. In the CDM Portal,
navigate to the Downloads tile, then download artefacts in Java, DAML, Typescript or Scala distribution format and review the reorganised source folder.