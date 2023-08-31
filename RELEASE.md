# _Product Model - FpML Synonym Mappings for Commodity Spread_

_Background_

Currently, the CDM attribute `priceType` is incorrectly being populated with `INTEREST_RATE` for Commodity spreads. This is inadequate since the spread is an asset price.

_What is being released?_

This release updates and extends the FpML mapping coverage for the product model to map the correct price. 

- Mappings added to populate CDM attribute `priceType` with `ASSET_PRICE` for commodity spreads.

_Review directions_

- In the CDM Portal, select the Textual Browser and inspect the change listed above.

# *CDM Model - Reference Index Information*

_Background_

A gap has been identified in the CDM regarding reference index information. Currently, there is no CDM object which can capture the name of the underlying index. The existing `IndexReferenceInformation` type has been identified that can support this, however it contains several attributes that are specific to a credit index.

_What is being released?_

This release simplifies the existing CDM type `IndexReferenceInformation` to capture only the generic index name & id information and separates all other credit-specific reference information into an extended type `CreditIndexReferenceInformation`. The slimmed-down `IndexReferenceInformation` is encapsulated into `FloatingRateOption`.

The release includes the following changes:

- Type `IndexReferenceInformation` is splitted into a generic `IndexReferenceInformation` and a credit-specific `CreditIndexReferenceInformation`
  - Kept only `indexName` and `indexId` attributes under `IndexReferenceInformation`
  - Created a new `CreditIndexReferenceInformation` type that extends `IndexReferenceInformation`
  - Moved all other specific credit reference attributes to `CreditIndexReferenceInformation`
- Added `indexReferenceInformation` attribute of type `IndexReferenceInformation` under type `FloatingRateOption`
- Updated `indexReferenceInformation` attribute in `GeneralTerms` to use the new `CreditIndexReferenceInformation` type, resulting in no overall impact to `GeneralTerms` attributes.

_Review Directions_

In CDM, In textual view, review the updates listed above in:
	
 - static data > asset > common > type
 - cdm > observable > asset > type
 - cdm > product > asset > type
