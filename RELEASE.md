# *Floating Rate Index Reference Data model - Extended FloatingRateIndexDefinition*



_Background_
- The CDM Floating Rate Index Definition model was developed in the summer of 2021
- Additional versions of the ISDA 2021 Interest Rate Definitions (V2, V3,…) have added more metadata to be included as part of the index definition
- The goal of this change is to update the CDM model to support the latest version of the ISDA Floating Rate Options Metadata spreadsheet

_What is being released?_

FROs Metadata extensions

Extended `FloatingRateIndexDefinition` type with: 

- Changed existing `fro` element from `FloatingRateOption` type to a new `FloatingRateIndexIndentification` type
- Added new `FloatingRateIndexIndentification` type with:
    - New `floatingRateIndex` element of `FloatingRateIndexEnum` type
    - New `currency` element of `ISOCurrencyCodeEnum` type
    - New `froType` element of `string` type
    
- Changed existing `supportedDefinition` element from `ContractualDefinitionsEnum` type to a new `ContractualDefinition` type
- Added new `ContractualDefinition` type with:
    - New choice between:
        - New `contractualDefinitionIdentifier` element of `ContratcualDefinitionIdentifier` type
            - New `contractualDefinitionType` element of `ContractualDefinitionsEnum` type
            - New `contractualDefinitionVersion` element of `string` type
        - Existing `identifier` element of `identifier` type
    - New `publicationDate` element of `date` type
   
- Extended existing `FloatingRateIndexMap` type with:   
    - New choice between:
        - New `contractualDefinitionIdentifier` element of `ContratcualDefinitionIdentifier` type
            - New `contractualDefinitionType` element of `ContractualDefinitionsEnum` type
            - New `contractualDefinitionVersion` element of `string` type
        - Existing `identifier` element of `identifier` type
        
- Changed cardinality of `externalStandard` element within `FloatingRateIndexExternalMap` type. From required (lower bound eq 1) to optional (lower bound eq 0)

- Extended existing `FloatingRateIndexCalculationDefaults` with:
    - Changed cardinality of `fixing` element of `FloatingRateIndexFixingDetails` type. From upper bound eq 1 to unbounded.
    - Removed `fixingTime` and `fixingOffset` elements
    - New `publicationCalendar` element of `BusinessCenterEnum` type
    
- Added new `inLoan` element of `boolean` type

- Added new `history` element of `FroHistory` type
    - Added new `FroHistory` type with:
        - New `startDate` element of `date` type
        - New `firstDefinedIn` element of `ContractualDefinition` type
        - New `updateDate` element of `date` type
        - New `lastUpdateIn` element of `ContractualDefinition` type
        - New `endDate` element of `date` type

- Added new `administrator` element of `Administrator` type
    - Added new `Administrator` type with:
        - New `name` element of `string` type
        - New `website` element of `string` type
        
- Added new `deprecationReason` element of `string` type

- Added new `fpmlDescription` element of `string` type

- Extended existing `FloatingRateIndexFixingDetails` with:
    - Changed cardinality of `fixingTime` element. From unbounded to upper bound eq 1
    - Removed `alternativeFixingTime` element
    - Changed cardinality of `fixingOffset` element. From unbounded to upper bound eq 1
                        
- Extended existing `FloatingRateIndexFixingTime` with:
    - New `fixingTimeDefinition` of `string` type
    - New `fixingReason` of `string` type

- Extended existing `BusinessDayOffset` with:
    - New `fixingTimeDefinition` of `string` type
    - New `fixingReason` of `string` type
       
_Review Directions_

In the CDM Portal, select the Textual Browser and search and inspect the `FloatingRateIndexDefinition` type

