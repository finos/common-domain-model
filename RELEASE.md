# *Product Model - Party and Person Identification*

_Background_

This release contains a set of changes to `Party`and `Person`identification. The model supports now the type of identifier for both, `partyId` and `personId`instead of relying on FpML metadata schemes to qualify the source of the identifier.

In addition, this release fixes an issue with a missing mapping for FpML `knownAmountSchedule`.

_What is being released?_

* Change to partyId structure
* Change to personId structure
* Updates to mappings as a result of the partyId and personId changes
* Fix for `knownAmountSchedule` mapping

_Types_

base-staticdata-party-enum
* Renamed `PartyIdSourceEnum`to `PersonIdentifierTypeEnum`
* Added values NPID and PLID to `PersonIdentifierTypeEnum`
* Added new `PartyIdentifierTypeEnum`with two values, BIC and LEI

base-staticdata-party-type
* Added new type `PersonIdentifier`
* Added new type `PartyIdentifier`
* `personId` is of type `PersonIdentifier` instead of `string`
* `partyId` is of type `PartyIdentifier` instead of  `string`


_Translate_

synonym-cdm-dtcc
* Updated `Party` mapping
* Added `PartyIdentifier` mapping

synonym-cdm-event
* Removed `PartyIdSourceEnum` mapping

synonym-cdm-fpml
* Added the path `knownAmountSchedule` to the `PriceQuantity` type

synonym-cdm-ore
* Added `PartyIdentifier` mapping

_Review Directions_

In the CDM Portal, select the Textual Browser and search and inspect the updated `partyId` and `personId` structures.

Please also inspect the associated enumerations `PartyIdentifierTypeEnum` and `PersonIdentifierTypeEnum` that have been added to the model.

