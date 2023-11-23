# _Natural Person and NaturalPersonRole circular reference_

_Background_

An issue regarding a circular reference inside the `NaturalPerson` type was recently found in the model.
`NaturalPerson` and `NaturalPersonRole` are located at the same level inside `Party`, to follow the same structure that `Party` and `PartyRole` have inside the `Trade` type. The circular reference issue appears because the `NaturalPerson` type also contains a `NaturalPersonRole`, which references back to the containing type of `NaturalPerson`, causing a circular reference in the model.
This release fixes this issue by removing the `NaturalPersonRole` inside the `NaturalPerson` type.

_What is being released?_

_Data types_

* `personRole` attribute of type `NaturalPersonRole` removed from `NaturalPerson`.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.
Inspect Pull Request: https://github.com/finos/common-domain-model/pull/2546