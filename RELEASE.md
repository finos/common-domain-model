# *Floating Rate Index Reference Data model - Extended FloatingRateIndexDefinition*

_What is being released_

_Background:_
- The CDM Floating Rate Index Definition model was developed in the summer of 2021
- Additional versions of the ISDA 2021 Interest Rate Definitions (V2, V3,…) have added more metadata to be included as part of the index definition

_Goal:_
- Update the CDM model to support the latest version of the ISDA Floating Rate Options Metadata spreadsheet

_FROs Metadata extensions_

Updated `FloatingRateIndexStyleEnum` type with: 

- Changed existing value from `Other displayName "Other FRO"` to  `Other displayName "Other"`
- Added new value `SpecifiedFormula displayName "Specified Formula"`

Updated `FloatingRateIndexCalculationMethodEnum` type with:

- Changed existing `Average  displayName "Overnight Average"` to `Average displayName "Overnight Averaging"`
- Added new value `Compounded displayName "Compounded Index"`
- Added new value `AllInCompounded displayName "All-In Compounded Index"`
    
Changed `FloatingRateIndexIndentification` type with:

- Fixed typo from `FloatingRateIndexI(n)dentification` to `FloatingRateIndexIdentification`

       
_Review Directions_

In the CDM Portal, select the Textual Browser and search and inspect the `FloatingRateIndexDefinition` type
