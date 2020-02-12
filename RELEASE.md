# *CDM Model: AllocationBreakdown and DividendDateReferenceEnum*

_What is being released_

 * Added new enum options for `DividendDateReferenceEnum`:
   * `CumulativeEquityExDivBeforeReset`
   * `CumulativeEquityPaidBeforeReset`
   * `CumulativeEquityPaidInclReset`
   * `CumulativeLiborPaid` renamed as `CumulativeInterestPaid`, including for ExDiv type.
   * `CumulativeInterestPaidInclReset`
   * `CumulativeInterestPaidBeforeReset`
   * `UnwindOrEquityExDiv`
   * `UnwindOrEquityPaid`
   * `UnwindOrInterestExDiv`
   * `UnwindOrInterestPaid`
   * `UnwindExDiv`
   * `UnwindPaid`
 
 * Updated `AllocationBreakdown` to have a quantity of type `QuantityNotation` wherease previously it was `NonNegativeQuantity`

 * Added additional details to `AllocationBreakdown` including account, collateral and allocationTradeId in order to ensure that AllocationBreakdown is more consistent with its FpML specification - https://www.fpml.org/spec/fpml-5-10-5-rec-1/html/confirmation/schemaDocumentation/schemas/fpml-doc-5-10_xsd/complexTypes/Allocations/allocation.html

_Review Directions_

In the Textual Browser, review the following:
 * `AllocationBreakdown`
 * `DividendDateReferenceEnum`

# *Model Optimisation: Clearing Instructions*

_What is being released_

* The `ClearingInstruction` now has a reference to the alpha contract that is being cleared. 
* Folded `WorkflowStepInstruction` and `BusinessEventInstruction` into new type `Instruction` as they served the same purpose.

_Review Directions_

In the Textual Browser, review the following:
 * `ClearingInstruction`
 * `Instruction`
 
 # *Model Optimisation: Primitive Event lineage*

_What is being released_

Lineage in the CDM has been rationalised for `PrimitiveEvent`. Each event has been updated to have a `reference` to the `before` state allowing the events to be chained together.

The following states are now annotated with `key` allowing them to be used as references.
 * `ContractState`
 * `ExecutionState`
 * `Trade`
 
 The following states are now annotated with `metadata reference` allowing them to be used as references.
 * `ExecutionPrimitive`
 * `ContractFormationPrimitive`
 * `AllocationPrimitive`
 * `ExercisePrimitive`
 * `InceptionPrimitive`
 * `QuantityChangePrimitive`
 * `ResetPrimitive`
 * `TermsChangePrimitive`

_Review Directions_

In the Textual Browser, review the following the above types.

# *Repo Model: Open Repo Handling*

_What is being released_

Following initial workshop with ICMA focusing on the open repo use-case, the CDM has been updated to handle the `Call` feature of open repos, using the same callable/extendible provision component as for Swaps.

The `CallableProvision` and `ExtendibleProvision` have been factored into a common `OptionProvision` component, existing FpML `synonyms` have been updated and the corresponding `Call` component in FpML for open repo has been mapped.

_Review Directions_

In the Textual / Graphical Browser, see: `EconomicTerms` > `OptionProvision`.

In the Ingestion Panel, see Product > Repo > "repo-ex02-repo-open-fixed-rate"
