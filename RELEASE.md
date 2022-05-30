# *Product Model - Party and Person Identification*

_Background_

This release contains a set of changes to `Party`and `Person` identification. The model now supports the enumeration of the nature of an identifier for both `partyId` and `personId`instead of relying on FpML metadata schemes to qualify the source of the identifier.

In addition, this release fixes an issue with a missing mapping for FpML `knownAmountSchedule`.

_What is being released?_

* Change to partyId structure
  - Added new type `PartyIdentifier` allowing a Party to be represented by an identifier and an enumeration
  - `partyId` updated to be of type `PartyIdentifier`
  - Added new `PartyIdentifierTypeEnum`with two values, BIC and LEI
* Change to personId structure
  - Added new type `PersonIdentifier` allowing a Person to be represented by an identifier and an enumeration
  - `personId` updated to be of type `PersonIdentifier`
  - Added values `NPID` and `PLID`, and removed values `BIC` and `LEI` from `PersonIdentifierTypeEnum`
* Updated model to model mappings as a result of the partyId and personId changes
* Fix for `knownAmountSchedule` mapping

_Review Directions_

In the CDM Portal, select the Textual Browser and search and inspect the updated `partyId` and `personId` structures and the associated enumerations `PartyIdentifierTypeEnum` and `PersonIdentifierTypeEnum`.

In the CDM Portal, select Ingestion and review the following sample trade to see `partyId` and `personId` changes:
- fpml 5-10 > products > rates> USD-Vanilla-swap
