# *User Documentation: Consistency of Examples vs Model*

_What is being released_

A technical change has been introduced in the CDM build and release infrastructure, that verifies the consistency of any code snippet included in the user documentation vs the model, and alerts in case of any discrepancy. Going forward, this will facilitate maintenance of the user documentation as the model evolves, by forcing to update the documentation in synchrony with any model change.

As part of this infrastructure change, a number of code snippets in the user documentation have been updated to be synchronised with the model.

In particular, following removal of the _inception_ primitive (superseded by the _execution_ and _contract formation_ primitives) as part of a recent release, the user documentation has been updated to reflect that model change.

_Review Direction_

In the CDM Portal, review the CDM Documentation, in particular the section:
- [Primitive Event Section](https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#primitive-event)
