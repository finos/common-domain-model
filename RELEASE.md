# *Maintenance*

_What is being released?_

This release is purely a syntactical change and does not contain no functional changes to the model.

Deprecated usages of the type name in conditions have been removed.
For example, the condition `ReferenceAgency` on the type `MultipleCreditNotations` has been rewritten from
```
if MultipleCreditNotations -> mismatchResolution = CreditNotationMismatchResolutionEnum -> ReferenceAgency
then MultipleCreditNotations -> referenceAgency exists
```
to
```
if mismatchResolution = CreditNotationMismatchResolutionEnum -> ReferenceAgency
then referenceAgency exists
```

This will ease the upgrade to future DSL versions.

_Review Directions_

Three conditions have been rewritten to remove the deprecated syntax:
- Type `ResolvablePriceQuantity`, condition `QuantityMultiplier`.
- Type `AgencyRatingCriteria`, condition `ReferenceAgency`.
- Type `MultipleCreditNotations`, condition `ReferenceAgency`.
