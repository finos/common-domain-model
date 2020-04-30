# *Infrastructure: Annotations*

_What is being released_

Changes to annotations based on the recommendations of the recent design review of CDM functions.

- Remove `[partialKey]` - the annotation was used on the `EconomicTerms` type, and was intended to provide a hash/checksum of the object data, excluding any metadata, that could be used for comparing economic equality between objects.  However, the current implementation could cause both false positive and false negative results, and, following the recent quantity and price model refactor does not account for these values. 

_Review Directions_

In the Textual Browswer, review the `EconomicTerms` type.
