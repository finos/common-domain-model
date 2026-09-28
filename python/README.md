# FINOS Common Domain Model — Python Distribution

[![FINOS - Active](https://cdn.jsdelivr.net/gh/finos/contrib-toolbox@master/images/badge-active.svg)](https://community.finos.org/docs/governance/Software-Projects/stages/active)
[![PyPI](https://img.shields.io/pypi/v/finos-cdm)](https://pypi.org/project/finos-cdm/)
[![Python](https://img.shields.io/pypi/pyversions/finos-cdm)](https://pypi.org/project/finos-cdm/)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

The [FINOS Common Domain Model (CDM)](https://cdm.finos.org/) is a standardised, machine-readable and machine-executable blueprint for how financial products are traded and managed across the transaction lifecycle. This package distributes the CDM as a native Python library, generated automatically from the authoritative Rune model source.

## Installation

```bash
pip install finos-cdm
```

Requires Python 3.11 or later.

## Dependencies

| Package | Version |
|---------|---------|
| [pydantic](https://docs.pydantic.dev/) | `>=2.10.3` |
| [rune.runtime](https://pypi.org/project/rune.runtime/) | `>=2.0.0,<3.0.0` |

`rune.runtime` provides the base classes, validation infrastructure, and serialisation utilities used by all generated CDM types.

## Quick Start

```python
from finos.cdm.event.common.TradeState import TradeState

# All CDM types are Pydantic models — construct with keyword arguments
trade_state = TradeState(...)
```

All CDM classes are Pydantic v2 models, giving you automatic validation, JSON serialisation, and IDE auto-complete out of the box:

```python
import json

# Serialise to JSON
payload = trade_state.rune_serialize()

# Deserialise from JSON
restored = BaseDataClass.rune_deserialize(payload)
```

Deserialisation returns an object of the appropriate type.

## What Is and Is Not Supported

### Supported

- **Full CDM data model** — every CDM type defined in the Rune DSL is generated as a Pydantic v2 class with field-level type checking, cardinality validation, and condition enforcement via `validate_model()`.
- **CDM functions** — functions whose logic is expressed directly in Rune DSL are generated as callable Python functions.
- **Conditions and constraints** — all Rune-defined conditions are generated and evaluated by the runtime when `validate_conditions()` or `validate_model()` is called.
- **Rune-conformant serialisation** — `rune_serialize()` and `rune_deserialize()` produce and consume JSON that includes the `@type`, `@model`, and `@version` envelope required for interoperability with other Rune-based runtimes and tooling.
- **Object references** — cross-object references defined in the CDM model are resolved automatically on deserialisation.

### Not Supported

- **Native Java function implementations** — a subset of CDM functions are implemented natively in Java rather than expressed in Rune DSL. These functions are present in the Python distribution as stubs but will raise `NotImplementedError` at runtime if called without a registered Python replacement. Python native implementations can be registered via `rune_register_native()` from `rune.runtime.native_registry`.
- **Code list data loading** — the `CodeList`, `CodeListIdentification`, and `CodeValue` data types are fully generated, but the `LoadCodeList` function that populates them is a native Java stub (see above). Applications that require runtime code list validation must supply their own implementation.


## How This Package Is Generated

This package is automatically generated from the [CDM Rune DSL source](https://github.com/finos/common-domain-model) using the [Rune Python Generator](https://github.com/finos/rune-python-generator). The generated code is published to PyPI as part of each CDM release. Do not edit the generated source directly — raise a CDM model change via the [contribution process](https://github.com/finos/common-domain-model/blob/master/CONTRIBUTING.md) instead.

## Versioning

This package follows the CDM release version. A CDM release `X.Y.Z` corresponds to `finos-cdm==X.Y.Z` on PyPI. Pre-release builds are published as `X.Y.Z.devN`.

## Documentation

- [CDM Documentation](https://cdm.finos.org/) — full model documentation and design principles
- [CDM GitHub Repository](https://github.com/finos/common-domain-model) — model source, issues, and contributions
- [Rune Python Runtime](https://github.com/finos/rune-python-runtime) — runtime library documentation

## Community and Support

- Mailing list: [cdm+subscribe@lists.finos.org](mailto:cdm+subscribe@lists.finos.org)
- Issues: [github.com/finos/common-domain-model/issues](https://github.com/finos/common-domain-model/issues)
- FINOS Community: [community.finos.org](https://community.finos.org)

## License

Copyright 2021 FINOS and CDM Participants

The CDM model specification is subject to the [Community Specification License 1.0](https://github.com/finos/common-domain-model/blob/master/LICENSE.md).

The Python runtime binding code in this distribution is licensed under the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0).