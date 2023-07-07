# *Infrastructure - Dependency Updates*

_What is being released?_

This release updates the rosetta-dsl dependency:

- Version upgrade includes:
  - 8.2.0: new operations have been added to convert strings into enum values (`to-enum`), numbers (`to-number`/`to-int`), and time values (`to-time`). Additionally, converting enum values or values of a basic type into a string can be performed via the new operation `to-string`. No changes are required in the CDM. See release notes for examples and details: https://github.com/REGnosys/rosetta-dsl/releases/tag/8.2.0

# *Product Model - Forward Payout*

This release updates the `ForwardPayout` to extend `PayoutBase` to make it consistent with all other payouts.

The `settlementTerms` attribute has been removed from `ForwardPayout` as it is already an attribute of `PayoutBase`.

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

This release does not have any functional impact on mapping expectations:

- In serialised JSON CDM samples, the attribute ordering has changed due to the repositioning of the `settlementTerms` component, however this has no functional impact on the model.
- For FpML FX samples, the number of validation failures has increased by 1 because `PayoutBase->payerReceiver` is mandatory but is not populated for the existing samples.  The mapping of FX samples will be reviewed in a future release.

# *Infrastructure - Build Pipeline Bug Fix*

This release fixes the automated build and release pipeline to ensure unit test failures are correctly reported.  For further information see GitHub issue https://github.com/finos/common-domain-model/issues/2252.
