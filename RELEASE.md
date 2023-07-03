# *Product Model - Forward Payout*

_What is being released?_

This release updates the `ForwardPayout` to extend `PayoutBase` to make it consistent with all other payouts.

The `settlementTerms` attribute has been removed from `ForwardPayout` as it an attribute of `PayoutBase`.

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

This release does not meaningfully change any existing mapping expectations:

- In the serialised JSON, the attribute ordering has changed due to the `settlementTerms` attribute change, however this has no meaningful impact on the representation of the model. 
