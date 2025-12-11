# _Reference Data – Externalized FpML Coding Scheme Validation_

_Background_

The Common Domain Model (CDM) historically integrates FpML coding schemes by compiling them into static enumerations via `docReference` annotations in Rune DSL. While this approach simplifies code generation, it tightly couples reference data updates with full CDM releases. This dependency increases maintenance overhead for CDM contributors and complicates adoption for end users, who must upgrade the entire CDM stack—even for minor reference data changes.

Some FpML code lists change frequently (historical records show 140+ updates over the past decade), this coupling slows adoption and risks misalignment with regulatory, legal, and market standards.

The enhancement decouples CDM releases from FpML updates by enabling validation against externalized code list resources. This ensures flexibility for implementers, while maintaining data integrity and alignment with industry-maintained references.

Further background is available in Issue [#3512](https://github.com/finos/common-domain-model/issues/3512).
A detailed contribution document is available [here](https://github.com/user-attachments/files/21262091/CDM.Reference.Data.Documentation.v5.md).

_What is being released?_

This release introduces support for externalized code list validation through the new `FpMLCodingScheme` type alias and a supporting validation function `ValidateFpMLCodingSchemeImpl`.

Key technical improvements include:

- **Extensible validation**: validation logic is now independent from CDM release cycles. It is driven by type alias condition and can be extended to user-defined code lists.
- **Automated transformation**: FpML XML code lists are converted to CDM-ready JSON resources via a Maven-based process.
- **Runtime availability**: generated JSON resources are bundled into project resources, eliminating the need for recompilation.

Detailed changes include:

1. **New Namespace**: `cdm.base.staticdata.codelist`

    - Introduced types: `CodeList`, `CodeListIdentification`, `CodeValue`.
    - Added type aliases:

        * `FpMLCodingScheme` (validation wrapper).
        * `BusinessCenter` (extension of `FpMLCodingScheme`, replacing `BusinessCenterEnum`).
    - New functions:

        * `LoadCodeList` (load external JSON resources into a `CodeList`).
        * `ValidateFpMLCodingSchemeDomain` (run validation against external resources).

2. **Java Runtime**

    - Default validation implementation: `ValidateFpMLCodingSchemeImpl` (bound in `CdmRuntimeModule`).

3. **Testing Enhancements**

    - Standard validation scenarios: `FpMLCodingSchemeTests`.
    - Custom list validation: `CodeValidationTest`.
    - JSON resource loading: `LoadCodeListTest`.

4. **Migration Updates**

    - All references to `BusinessCenterEnum` migrated to the new model and validation approach.

5. **Build Process Enhancements**

    - Maven execution added for:

        * XML-to-JSON code list transformation.
        * Relocating generated resources to `src/main/resources/org/isda/codelist` for consistent runtime access.

_Review Directions_

Full implementation details and review discussion can be found in PR [#4227](https://github.com/finos/common-domain-model/pull/4227).
