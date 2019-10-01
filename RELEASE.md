
Re-releasing 2.5.2 with updated release notes.

*Rosetta Syntax Upgrade*

_What is being released_

As part of on-going improvements, the Rosetta syntax has been upgraded to consolidate language features. This work aims to simplify the syntax needed when adding to, and editing the CDM. This will become increasingly important as the number of CDM contributors increase and as CDM contributions starting coming from a variety of sources.

This is the third release in a series of that will cover the scope described in the Rosetta Syntax Upgrade Wiki[1].

In this release, usability features were added to support contributing to the CDM. Content Assist features make suggestions as you edit the CDM. Validation features check for correct use of types and cardinality of model elements when making model references, which is especially important when specifying Functions.

Generated Java code has also been consolidated such that all Functions generate Java code in the same structure. This makes  reuse of the internal meta-model and will make external code generators easier to create and make the generated Java code easier for users to understand.

_Review Directions_

For the usability features, look out for the announcement of the soon to be released Rosetta Core Community Edition application that will allow users to edit and contribute directly to the CDM.

For the generated Java artefacts: in the CDM Distribution Pack (accessible via the Downloads UI of the Portal), inspect the generated Java code

[1] https://github.com/REGnosys/rosetta-dsl/wiki/Rosetta-Syntax-Upgrade)

*Proposed model changes as part of an on-going effort to augment the product coverage with securities*

_What is being released_

- Added sample implementation of existing function `Settle` which focuses on the securities use case.

_Review Directions_

- In the downloaded CDM Distribution review sample function implementation `SettleImpl.java` and `SettleTest.java`.
