# *Product Model - FpML Mapping Update*

_Background_

This release adds FpML mapping fixes and improvements that have been previously implemented in other models such as Digital Regulatory Reporting (DRR).

_What is being released?_

- FpML synonyms to map `EventInstruction` attributes `intent`, `eventDate` and `effectiveDate`
- FpML synonyms to map `EconomicTerms` attribute `nonStandardisedTerms`
- FpML synonyms to map `WorkflowState` attribute `workflowStatus`
- FpML synonyms and mapper to map commodity schedule xml elements `calculationPeriodsSchedule` and `calculationPeriods` into `PriceSchedule->datedValue`

_Review Directions_

In Rosetta, select the Translate tab and review the following samples:

- fpml-5-10 > processes > msg-cleared-alpha-trade-CFTC-SEC-and-canada.xml
- fpml-5-10 > processes > msg-ex52-execution-advice-trade-partial-novation-C02-00.xml
- fpml-5-10 > incomplete-processes > msg-ex60-execution-advice-trade-amendment-correction-F02-10.xml
- fpml-5-13 > products > commodity-derivatives > com-mockup-ex1-strikePricePerUnitSchedule.xml
- fpml-5-13 > products > commodity-derivatives > com-mockup-ex2-strikePricePerUnitSchedule.xml

Changes can be reviewed in PR [#2976](https://github.com/finos/common-domain-model/pull/2976)
