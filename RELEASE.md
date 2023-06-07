# *CDM Distribution - Python Code Generation*

_What is being released?_

This release introduces Python code generation functionality.  

The Python distribution includes:
- a class library mirroring the model namespace hierarchy
- support for object construction, validation, and deserialization/serialization
- includes docstrings documentation for data types, enums, and conditions

_Review directions_

In the CDM Portal, go to the Downloads section, and download CDM as Python.

Unzip and review the libs folder.  The library comes in two parts, each must be installed via pip.

- a static runtime that provides certain core functionality: `rosetta_runtime-1.0.0-py3-none-any.whl`.  Install this package first.
- the CDM library: `python_cdm-x.x.x-py3-none-any.whl`
  
These libraries are compatible with Python 3.10+ and rely upon [Pydantic](https://pydantic.dev).
