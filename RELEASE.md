# _Product Model - Bond Option and Forward Qualification_

_Background_

A previous release introduced a regression in the qualification of Bond Forwards and Bond Options, that were no longer qualified due to an update in the `Qualify_AssetClass_InterestRate` function. This release addresses this issue.

_What is being released?_

- Reintroduced the capability to have a security with `securityType = SecurityTypeEnum -> Debt` as underlier of the option or forward in the `Qualify_AssetClass_InterestRate` function.

_Review directions_

In Rosetta, select the Textual View and inspect the change identified above

The changes can be reviewed in PR: [#2851](https://github.com/finos/common-domain-model/pull/2851)
