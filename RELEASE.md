# *Quantity Refactoring: Credit Products Migrated*

As presented in the 30-Jul-2019 CDM WG meeting, the essence of the restructuring is to abstract away the quantity from the contractual product definition, such that a contractual product is defined as a "unit" of that product, similar to how non-contractual products (e.g. securities) work. The actual quantity is handled as part of a separate `QuantityNotation` object, while the product uses the `ResolvablePayoutQuantity` type to implement the quantity mechanics in the various `Payout` legs of the product.

_What is being released_

Credit products (CDS, CD Index etc.) have been migrated to the new quantity representation.

As part of this release, the quantity referencing mechanism has been augmented with an additional attribute, to distinguish between different assets which quantities are being specified. So `ResolvablePayoutQuantity` now uses both `Tag` and `Asset` as identifiers to build its quantity reference. This is applicable in the XC Swap case, where the currency will be used as the `Asset` identifier.

_Review direction_

In the Ingestion Panel of the CDM Portal:
- Look at Products / Credit and search for keyword `quantity` in the output, to see where respective elements are now positioned.
