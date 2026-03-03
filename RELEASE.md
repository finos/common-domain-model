# *Documentation - CDM Schema Publication*

_Background_

CDM schemas were not previously available via the CDM Documentation, which limited their discoverability and reuse by external consumers and tooling. Issue [#4436](https://github.com/finos/common-domain-model/issues/4436) identified the need to make CDM schemas available to support wider adoption and integration.

This change addresses that gap by enabling the publication of CDM schemas as part of the CDM Documentation.

_What is being released?_

- Introducing publication of CDM schemas so they can be accessed publicly
- Enabling external consumers to reference CDM schemas directly for validation and integration use cases
- Supporting improved discoverability and reuse of CDM artefacts beyond the core repository

These changes are focused on schema availability and do not alter the underlying CDM business semantics.

_Review Directions_

Changes can be reviewed in PR: [#4435](https://github.com/finos/common-domain-model/pull/4435)