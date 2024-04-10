# _Product Model - Bond Option and Forward Qualification_

_Background_

A previous release caused a regression in the qualification of Bond Forwards and Bond Options, that were not qualified anymore due to an update to the `Qualify_AssetClass_InterestRate` function. This release fixes that.

_What is being released?_

- Reintroduced to `Qualify_AssetClass_InterestRate` the possibility to have a security with `securityType = SecurityTypeEnum -> Debt` as underlier of the option or forward.

_Review directions_

In Rosetta, select the Textual View and inspect each of the changes identified above

The changes can be reviewed in PR: [#2851](https://github.com/finos/common-domain-model/pull/2851)