# _Natural Person and NaturalPersonRole circular reference_

_Background_

An issue regarding a circular reference inside the `NaturalPerson` type was recently found in the model.
`NaturalPerson` contains a list of `NaturalPersonRole`, which has a reference back to the containing type `NaturalPerson`, causing a circular reference in the model.
This release fixes this issue.

_What is being released?_

- Minor changes to `NaturalPerson` type.

_Data types_

- `personRole` attribute of type `NaturalPersonRole` removed from `NaturalPerson`.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

