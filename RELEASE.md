# *CDM Distribution - C# Data Validation*

_What is being released?_

This release introduces support for data validation in the C# distribution, enabling validation based on both cardinality and conditional statements.

*Cardinality*

The C# code generators now generate a validation rule for each attribute cardinality constraint, so if the cardinality of the attribute does not match the requirement an error will be associated with that attribute by the validation process.

*Condition Statement*

Data type condition definitions comprises a boolean expression that applies to the type attributes.  The C# code generators now support the following language features:

- conditional statements: `if`, `then`, `else` 
- boolean operators: `and`, `or`
- list statements: `exists`, `is absent`, `contains`, `count`
- comparison operators: `=`, `<>`, `<`, `<=`, `>=`, `>`

*Choice*

Choice rules define a choice constraint between the set of attributes of a type.  The C# code generators now support the `one-of` condition.

_Review directions_

In the CDM Portal, go to the Downloads section, and download either C# 9 (.NET 5.0) or C# 8 (.NET Standard 2.1).
