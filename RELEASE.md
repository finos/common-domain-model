# _Infrastructure - Dependency Update_

_Background_

The Rosetta platform has a feature for its enumerations that enables, if a certain enumeration is directly related to an FpML or ISO coding scheme, to label that enumeration with the corresponding coding scheme canonical URI, so every time that coding scheme is updated, the enumeration will be automatically updated.

_What is being released?_

- This release updates `FloatingRateIndexEnum` to keep it in sync with the latest FpML coding scheme.
   * The following enum value has been added:
       * `INR_MIBOR <"INR-MIBOR">`
       * `INR_SORR <"INR-SORR">`
       * `INR_SORR_OIS_Compound <"INR-SORR-OIS Compound">`
       * `PLN_POLSTR <"PLN-POLSTR">`
       * `PLN_POLSTR_OIS_Compound <"PLN-POLSTR-OIS Compound">`


- This release updates the `DSL` dependency.

   Version updates include:
   - `DSL` 9.65.0 The `switch` operation now supports complex types. It also contains various model validation fixes. See DSL release notes: [DSL 9.65.0](https://github.com/finos/rune-dsl/releases/tag/9.65.0)
   - `DSL` 9.65.1 Code generation fix for the `with-meta` operation. See DSL release notes: [DSL 9.65.1](https://github.com/finos/rune-dsl/releases/tag/9.65.1)
   - `DSL` 9.65.2 Various fixes and build optimization. See DSL release notes: [DSL 9.65.2](https://github.com/finos/rune-dsl/releases/tag/9.65.2)
   - `DSL` 9.65.3 Fixes related to `key` metadata. See DSL release notes: [DSL 9.65.3](https://github.com/finos/rune-dsl/releases/tag/9.65.3)
   - `DSL` 9.65.4 Fix metadata template Java type. See DSL release notes: [DSL 9.65.4](https://github.com/finos/rune-dsl/releases/tag/9.65.4)
   - `DSL` 9.65.5 Fix issue where clashing names from other namespaces are not correctly qualified in generated code. See DSL release notes: [DSL 9.65.5](https://github.com/finos/rune-dsl/releases/tag/9.65.5)
   - `DSL` 9.66.0 Fix issue where `then` operation allows incorrect syntax. See DSL release notes: [DSL 9.66.0](https://github.com/finos/rune-dsl/releases/tag/9.66.0)



There are no changes to model or test expectations.

_Review Directions_

The changes can be reviewed in PR: [#4037](https://github.com/finos/common-domain-model/pull/4037)

# _Infrastructure - Security Update_

_What is being released?_

Update to the CVE scanning configuration. The `OSS Index integration` has been disabled in the build pipeline because username and password credentials are not yet configured. Dependency scanning continues to run using OWASP Dependency-Check with suppression rules, ensuring vulnerability checks are still performed.
`disableOssIndex` flag set in CVE scanning workflow configuration

_Review Directions_

The changes can be reviewed in PR: [#4037](https://github.com/finos/common-domain-model/pull/4037)
