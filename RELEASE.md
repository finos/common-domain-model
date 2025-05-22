# _Infrastructure - Upgrade Java Version_

_What is being released?_

This release upgrades the project to compile and run using `Java 21`, taking advantage of the latest improvements in the Java platform, including enhanced performance and security updates.

_Backwards Compatability_

While the project compiles using `Java 21`, the distributed artifacts remain compatible with `Java 11`. This ensures backward compatibility with `Java 11` (and later) runtime environments.  

- CDM contributors need `Java 21` installed locally to build or contribute to the project
- CDM implementors do not need to upgrade as the runtime compatibility remains at `Java 11`

_Review directions_

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/3688

# _Reference Data - Update ISOCurrencyCodeEnum_

_What is being released?_

Updated `ISOCurrencyCodeEnum` based on updated scheme ISO Standard 4217.

Version updates include:
- added value: `XAD`


_Review directions_

The changes can be reviewed in PR: [#3699](https://github.com/finos/common-domain-model/pull/3699)
