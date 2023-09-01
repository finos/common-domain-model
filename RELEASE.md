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

# *FpML Mappings - Taxonomy Source*

_Background_

The recently introduced coverage for crypto-based indicator in FpML uses the same element from which CDM sources the taxonomy value. This release updates the taxonomy mapper so that only actual taxonomy values are mapped into the taxonomy structure. This avoids duplication since the crypto-based indicator is placed elsewhere in the model.

_What is being released?_

- Updated `TaxonomySourceMappingProcessor` to ignore values coming from the particular FpML scheme related to the crypto indicator.

_Review directions_

Download one of the full CDM distributions (Java or Python).

In a code processor, inspect the element `TaxonomySourceMappingProcessor` for changes.
