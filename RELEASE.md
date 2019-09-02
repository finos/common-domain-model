
# *Initial quantity refactoring: Equity Swap*

_What is being released_

As part of an on-going effort to restructure the handling of "quantity" in the CDM, this initial release implements the new structure in the model and applies it for Equity Swap products. Other product types will be migrated to this new structure over time (likely starting with Interest Rate Swap products).

As was presented in the 30-Jul-2019 CDM WG meeting, the essence of the restructuring is to abstract away the quantity from the contractual product definition, such that a contractual product is defined as a "unit" of that product, similar to how non-contractual products (e.g. securities) work.

To this end, 2 new main types are being introduced: `QuantityNotation` and `ResolvablePayoutQuantity`, that both leverage the existing `Quantity` type:

1) `QuantityNotation` specifies the actual transacted quantity of a given product. It is meant to be specified alongside but separate from the `ContractualProduct`, as an additional attribute in an `Execution` or a `Contract`. The reason for the "notation" qualifier is that this object _characterises_ the type of quantity being specified with a tag, taken from an enumeration value called `QuantityNotationEnum`. The types of quantities that can be specified include: `Notional`, `NumberOfSecurities` and the list is expected to grow over time as more product types get migrated. Multiple `quantityNotation` attributes can be specified for the same product: e.g. for Equity Swaps, both the number of securities and the notional can be specified (and a validation logic will need to be implemented to verify consistency as notional = no. securities x initial price).

2) `ResolvablePayoutQuantity`, as its name suggests, is a resolvable quantity concept that applies to each of the underlying `Payout` legs through the `payoutQuantity` attribute. The principle is that a resolvable quantity can always be resolved into one number, based on: (i) the `quantityNotationTag` attribute that must be specified alongside the `contractualProduct` description, and (ii) a given date, where a schedule is applicable.

For instance: an `EquityPayout` leg may specify `Notional` as the tag for its `payoutQuantity`, in which case the notional will have to be fetched from the corresponding `Notional` quantity notation, which must be provided upon execution, to resolve the product. In addition to the base case where quantity is specified either directly as a number or indirectly through this quantity notation mechanism, the use-cases for `ResolvablePayoutQuantity` are:

  - Quantity based on some pre-defined schedule: e.g. amortising notional
  - Quantity based on some pre-defined events: e.g. resetting cross-currency notional
  - Quantity set as reference to another quantity: e.g. equity notional as no. securities x price

In practice, the `Contract` object has a `contractualQuantity` attribute containing the (potentially multiple) `quantityNotation` attributes, plus other information such as whether there is a pre-agreed mechanism to amend such quantity in future. `synonym`s have been implemented for the `QuantityNotation` and `ResolvablePayoutQuantity` classes for the Equity Swaps use-case only. Other product types have been left untouched. For technical reasons a (new but temporary placeholder) type called `ExecutionQuantity` is used for the `contractualQuantity` attribute, until such time when it will be renamed as, and supersede, the existing `ContractualQuantity` type which will be progressively "emptied-out".

_Review direction_

In the CDM Textual Browser:

- Review the `Contract` type with the additional `contractualQuantity` attribute and its underlying `quantityNotation` attribute.
- Review the `InterestRatePayout` and `EquityPayout` types, that implement the new `PayoutBase` containing the `payoutQuantity` attribute.

In the CDM Ingestion panel:

- Test the products/equity use-cases, and see the structure of the newly ingested CDM. The actual quantity numbers (number of securities and notional) have been extracted away and replaced by tags in the product description. The actual numbers now sit under the `contractualQuantity` attribute that is part of the `Contract` object.
