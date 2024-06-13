# *CDM Distribution - Python Code Generation*

_What is being released?_

This release updates the `bundle` dependency to version `11.10.0` to include the new version of the Python generator which includes the following changes:

- added support for model name clashes with Python keywords, soft keywords, and items whose names begin with "_"
- added support for DSL operators `to-string` and `to-enum`
- resolves the defect exposed by PR [#2766](https://github.com/finos/common-domain-model/pull/2766)
- includes an update to the Python runtime library (2.1.0) used to encapsulate the Pydantic support 

_Review directions_

Download the latest Python distribution from the [Maven Central](https://central.sonatype.com/artifact/org.finos.cdm/cdm-python)

The changes can be reviewed in PR: [#2984](https://github.com/finos/common-domain-model/pull/2984)
