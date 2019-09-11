_What is being released_

*Proposed model changes as part of an on-going effort to augment the product coverage with securities*

1) Add `executionReference` attribute to `Lineage` class.
2) Add post-condition to `Settle` function to assert that the `executionReference` has been set.

_Review direction_

In the CDM Textual Browser:
- Review class `Lineage` and function `Settle`.
