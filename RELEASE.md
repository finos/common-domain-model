_What is being released_

- Added new type called BusinessEvent to capture just the collection of Primitive Events.
- Changed the cardinality of BusinessEvent.primative from 1..1 to 1..*. 
- Changed the cardinality of all attributes of PrimitiveEvent from 0..* to 0..1 and introduced a one-of constraint.
- Moved all primitive types to their own file for ease of navigating the model files.
- Renamed primitives to have consistent naming convention (e.g. Inception changed to InceptionPrimitive)
- Updated all qualifications to work with the primitives cardinality change.

_Review direction_

View the BusinessEvent type and PrimitiveEvent type in the CDM Portal to see the changes.
