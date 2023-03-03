# Product Model - FpML synonym mappings for party information

_Background_

This release updates and extends the FpML mapping coverage for party representation.

_What is being released?_

* Mappings added to populate CDM attribute `AssignedIdentifier -> identifier` with FpML elements `messageId`, `collateralPortfolio` and `eventId`; the latter being referenced with FpML `eventIdScheme`

* Mappings added to populate CDM attributes `NaturalPerson -> firstName` and `NaturalPerson -> lastName` with FpML element `personId`, when that element is formatted as two strings (first and last name) separated by a dot

_Review directions_

* In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.
