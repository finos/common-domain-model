# *CDM Model: Enhancement to Create_ExecutionPrimitive and Create_Execution*

_What is being released_

Added three input and output attributes to the `Create_ExecutionPrimitive`, `Create_Execution`, and `Create_ClearedTrade` functions.  All three attributes are required in the `ExecutionPrimitive` but were not previously in the functions: `executionType`, `tradeDate`, and `identifier`.

_Review Directions_

In the Rosetta Core, search for the functions noted above.
