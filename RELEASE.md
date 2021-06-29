# *DSL Syntax - Only Exists for multiple attributes*

_What is being released?_

The `only exists` DSL feature has been enhanced to allow a list of attributes to be evaluated in a similar way to a single attribute.

The syntax for a single attribute remains the same.  The statement below will evaluate to true if `interestRatePayout` exists and all other attributes of `Payout` are absent.

- `economicTerms -> payout -> interestRatePayout only exists`

The new syntax allows for a list of attributes to be specified.  The statement below will evaluate to true only if both `interestRatePayout` and `equityPayout` exist and all other attributes of `Payout` are absent.

- `(economicTerms -> payout -> interestRatePayout, economicTerms -> payout -> equityPayout) only exists`

This change allows many of the qualification functions to be simplified.

_Review Directions_

In the CDM Portal, use the Textual Browser to review the usages of `only exists` in the model, including the following examples:

- Product qualification functions - `Qualify_InterestRate_IRSwap_FixedFloat`, `Qualify_Commodity_Swap_FixedFloat`, `Qualify_CreditDefaultSwap_SingleName`.
