# *CDM Model: Observable namespace classification*

_What is being released_

This refactor is the second incremental change that will further transform the org.isda.cdm file into a hierarchical namespace tree.
 
This second refactor includes the changes for the __cdm.observable.*__ set of namespaces.

The namespaces contain components used across the CDM for market data, holiday calendar date, event (extraordinary event, trigger event, disruption event), and asset (schedule, settlement, price and quantity notation, etc).

_Review Directions_

In Rosetta Core (https://ui.rosetta-technology.io/), review the File or Namespace structure in the Editor Textual View.

In the CDM Portal, navigate to the Downloads tile, then download artefacts in Java, DAML, Typescript or Scala distribution format and review the source to see the new cdm.observable.* files.

(*note*: changes cannot be reviewed in Go, as objects are not downloaded in a file structure)
