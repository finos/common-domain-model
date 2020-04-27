# *Infrastructure: Annotation Syntax*

_What is being released_

Changes to annotations based on the recommendations of the recent design review of CDM functions.  The following annotations have been updated:

`qualification`: 
- Annotation declaration syntax changed to specify the qualifiable type e.g. `BusinessEvent` or `Product`.
- Annotation declaration syntax changed to specify the function name prefix, and set as "Qualify". e.g. `func Qualify_Execution`.
- Add validation to enforce that any function with a `qualification` annotation has the correct function name prefix, input type and output type.

`creation`:
- Rename annotation from `funcType` to `creation`.
- Set annotation prefix to "Create". e.g. `func Create_ExecutionPrimitive`.
- Add validation to enforce that any function with a `creation` annotation has the correct function name prefix, and output type.

_Review Directions_

In the Textual Browser, review annotations `qualification` and `creation`.
