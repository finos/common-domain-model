# *FpML Mappings - Taxonomy Source*

_Background_

The recently introduced coverage for crypto-based indicator in FpML uses the same element from which CDM sources the taxonomy value. This release updates the taxonomy mapper so that only actual taxonomy values are mapped into the taxonomy structure. This avoids duplication since the crypto-based indicator is placed elsewhere in the model.

_What is being released?_

- Updated `TaxonomySourceMappingProcessor` to ignore values coming from the particular FpML scheme related to the crypto indicator.

_Review directions_

Download one of the full CDM distributions (Java or Python).

In a code processor, inspect the element `TaxonomySourceMappingProcessor` for changes.
