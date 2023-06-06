# *CDM Distribution - Python Code Generation*

_What is being released?_

This release introduces Python code generation functionality.  The Python package implements a class library mirroring the model's hierarchy and supporting its full capabilities including object construction, validation, and deserialization/serialization of enabled types.  Facilitating development, the package provides as Python docstrings documentation of all Rosetta elements (data types, enums, and validation conditions) described in the model.  It does not support functionality natively implemented in the underlying language.

_Review directions_

In the CDM Portal, go to the Downloads section, and download CDM as Python.

Unzip and review the libs folder.  The library comes in two parts.  Each must be installed via pip.

- a static runtime that provides certain core functionality: rosetta_runtime-1.0.0-py3-none-any.whl.  Install this package first.
- the CDM library: python_cdm-x.x.x-py3-none-any.whl
  
These libraries are compatible with Python 3.10+ and rely upon [Pydantic](https://pydantic.dev).
