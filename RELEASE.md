# Reference Data — Rune-DSL FpML Coding Scheme Validation Upgrade

_Background_

The Common Domain Model relied on a custom Java-based implementation for externalized FpML coding scheme validation. 
This release upgrades the validation framework by moving the core logic directly into native Rune DSL. It removes the dependency on the Java runtime for cross-platform consistency.

_What is being released?_

Key improvements include:
- The `ValidateFpMLCodingSchemeDomain` function is now implemented directly in the model.
- Decommissioned the legacy `ValidateFpMLCodingSchemeImpl` and its `CdmRuntimeModule` binding.
- Decommissioned Java-specific validation unit tests.

_Review Directions_

Maintainers and users can verify the upgrade by running the following tests:
- `rosetta-source/src/test/java/cdm/base/staticdata/codelist/FpMLCodingSchemeTests.java` (cdm model unit tests)
- `examples/src/test/java/org/isda/cdm/codelist/CodeValidationTest.java` (custom application example)