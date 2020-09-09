# *CDM Model: Product namespace classification*

_What is being released_

This refactor is the third incremental change that will further transform the org.isda.cdm file into a hierarchical namespace tree.

This third refactor includes the changes for the __cdm.product.*__ set of namespaces.

The namespaces contain components used across the CDM for 
* Generic product concepts: economic terms and payout,
* Common product settlement concepts:cash vs physical, non-deliverable, money and cashflow, delivery vs payment,
* Common product schedule concepts: calculation period, reset, fixing and payment dates, stub, notional schedule, roll convention, 
* Product concepts applicable to specific asset classes,
* and Template feature concepts to define payouts.

_Review Direction_

In Rosetta Core (https://ui.rosetta-technology.io/), review the File or Namespace structure in the Editor Textual View. In the CDM Portal, 
navigate to the Downloads tile, then download artefacts in Java, DAML, Typescript or Scala distribution format and review the source to see the new cdm.product.* files.

# *Extension of EMIR Regulatory Rules*

_What is being released_

In preparation for modelling of the functional expression of EMIR Article 9 RTS and ITS, the provisions for an initial set of rules have been added to the CDM.

_Review Direction_

In Rosetta Core (https://ui.rosetta-technology.io/), review reporting rules containing `regulatoryReference ESMA EMIR ITS_9` in the model-reg-reporting file.

# Change in rosetta path conditional mapping*

_What is being released_

An enhancement to the semantics of the rosetta path conditional logic required a minor change to the synonyms for `effectiveDate` in `calculationPeriodDates`.

_Review Directions_

In Rosetta Core (https://ui.rosetta-technology.io/), navigate to the synonym-cdm-fpml file and review the synonyms for `effectiveDate` in `calculationPeriodDates`.