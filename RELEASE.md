# *Product Model - Forward Payout*

This release updates the `ForwardPayout` to extend `PayoutBase` to make it consistent with all other payouts.

The `settlementTerms` attribute has been removed from `ForwardPayout` as it is already an attribute of `PayoutBase`.

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

This release does not have any functional impact on mapping expectations:

- In serialised JSON CDM samples, the attribute ordering has changed due to the repositioning of the `settlementTerms` component, however this has no functional impact on the model.
- For FpML FX samples, the number of validation failures has increased by 1 because `PayoutBase->payerReceiver` is mandatory but is not populated for the existing samples.  The mapping of FX samples will be reviewed in a future release.
