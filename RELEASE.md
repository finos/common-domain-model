# *Event Model - Function Update*

_What is being released?_

This release fixes the `Create_ContractFormation` function for the use case where the instructions include a `LegalAgreement`.  By adding a Java implementation for the `Create_RelatedAgreementsWithPartyReference`, the provided `LegalAgreement` is now added to contract formation output `Trade->contractDetails->documentation` attribute.

_Review Directions_

In the CDM Portal, select the Visualization panel and review contents of the folder labeled Form Contract Business Event, and compare Fixed/Floating Single Currency Interest Rate Swap Master Agreement with Fixed/Floating Single Currency Interest Rate Swap No Legal Agreement.
