<<<<<<< HEAD
# _Product Model - Bond Option and Forward Qualification_

_Background_

A previous release introduced a regression in the qualification of Bond Forwards and Bond Options, that were no longer qualified due to an update in the `Qualify_AssetClass_InterestRate` function. This release addresses this issue.

_What is being released?_

- Reintroduced the capability to have a security with `securityType = SecurityTypeEnum -> Debt` as underlier of the option or forward in the `Qualify_AssetClass_InterestRate` function.

_Review directions_

In Rosetta, select the Textual View and inspect the change identified above

The changes can be reviewed in PR: [#2851](https://github.com/finos/common-domain-model/pull/2851)
=======
# _Product Model - Synonym mappings for FpML coding schemes v2-17_

_Background_

The version 2-17 of the FpML coding schemes was recently published. This new version included some changes that are already present in the corresponding enumerations of the CDM model, but the synonym mappings from FpML to CDM have not been updated to cover the latest changes.
This release introduces support for the synonym mappings corresponding to this FpML coding schemes update.

_What is being released?_

Synonym mappings to cover the changes in v2-17 of FpML coding schemes.

- Added mapping coverage for all missing values of `BusinessCenterEnum`.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

PR: [#2813](https://github.com/finos/common-domain-model/pull/2813)
>>>>>>> 328cbae88acd38f599905fd51c8b4e59da8d542d
