# *Quantity Refactoring: several IR product types migrated*

As presented in the 30-Jul-2019 CDM WG meeting, the essence of the restructuring is to abstract away the quantity from the contractual product definition, such that a contractual product is defined as a "unit" of that product, similar to how non-contractual products (e.g. securities) work. The actual quantity is handled as part of a separate `QuantityNotation` object, while the product uses the `ResolvablePayoutQuantity` type to implement the quantity mechanics in the various `Payout` legs of the product.

_What is being released_

Following previous (initial) release on Equity Swaps, several Interest Rates product types have been migrated to the new quantity mechanism: Swap (through Swap Stream), Swaption, CapFloor, Xccy Swap and Variable Notional. The Bond Option case has yet to be handled.

For Equity Swaps, a bug where the equity notional amount was not captured in the `QuantityNotation` has been fixed.

At the moment the mapping to existing FpML documents is being done "as-is". E.g. where the notional is represented 2x in each leg of a swap , we will have 2 `quantityNotation` attributes with the same value. Whereas in a xccy swap, they have different values but are also associated with different currencies. In "native" CDM objects, this attribute should either be unique, or have an associated data rule to verify that multiple instances do not have conflicting values.

The migration will carry-on gradually to encompass more product types, with credit derivatives as likely next candidate.

_Review direction_

In the Ingestion Panel of the CDM Portal:
- Look at products / rates for the above product type examples, and search for keyword `quantity` in the output, to see where respective elements are now positioned.

# *Tutorials Page*

_What is being released_

To aid users of CDM, a Tutorials page has been created, in which video tutorials will demonstrate the various ways to make use of CDM’s generated code artefacts. Initially, we have created a simple “Getting Started” video that shows how to access the ISDA CDM's generated code artefacts and setup the Maven project correctly in order to download all project dependencies. 

More tutorials will be uploaded in the coming weeks and as always, all feedback is welcome, please share them via the usual channels.

_Review direction_

In the CDM Portal:
- Look for the new Tutorial tile
