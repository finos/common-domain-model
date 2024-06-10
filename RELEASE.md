# *Updates to the Python version of CDM*

_Background_

The current implementation of the Python version of CDM can be broken when Python restricted keywords are used in the CDM models. 

_What is being released?_

- A new Python generator that "mungs" Python keywords when encountered.
- Support for two additional DSL operators (to-string, to-enum)

Depends on the completion of the [Code Generator PR #306](https://github.com/REGnosys/rosetta-code-generators/pull/306)

