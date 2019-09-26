_What is being released_

*Proposed model changes as part of an on-going effort to augment the product coverage with securities*

- Added sample implementations of existing functions `Allocate` and `NewAllocationPrimitive` which focuses on the securities use case.
- On `Execution`, mark the attribute `Party` as a reference.  This is allows the Execution.party to be specified as a value (e.g. for a standalone `Execution` instance) or as a reference (e.g. when the `Execution` is part of an `Event`, where the `Execution.party` can be a reference to `Event.party`).
- On `Position`, add attribute `cashBalance` to allow the settlement amounts to be aggregated.
- On `Lineage`, add attribute `transferReference` so any previous transfers can be conveniently tracked.

_Review direction_

- In the CDM Textual Browser:
  - Review classes `Execution`, `Position` and `Lineage`.
  - Review functions `Allocate` and `NewAllocationPrimitive`.
- In the downloaded CDM Distribution, review sample function implementations `AllocateImpl.java` and `NewAllocationPrimitiveImpl.java` and corresponding tests `AllocateTest.java`.
