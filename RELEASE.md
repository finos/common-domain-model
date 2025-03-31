# _Product Qualification - Amendment in filter conditions for Commodity Products_

_Background_

An issue was identified in the product qualification framework following updates introduced in [Issue #3476](https://github.com/finos/common-domain-model/issues/3476), whereby it was possible for products with non-standardised terms to be double-qualified. The patch that was added earlier missed one `Commodity Option` qualifier function. This patch addresses the issue. 

_What is being released?_

This release addresses this issue, amending the following functions that qualify non-exotic products to add a negative `nonStandardisedTerms` check:

* Qualify_Commodity_option

_Review Directions_

In the Rosetta platform, select the Textual Browser and inspect the change identified above, navigating to file cdm > product > qualification > func.

Details of the Issue and the resolution are available here: [#3579](https://github.com/finos/common-domain-model/issues/3579)

Changes can be reviewed in PR: [#3596](https://github.com/finos/common-domain-model/pull/3596)

# _Infrastructure - Dependency Update_

_Background_

Rosetta has a feature for its enumerations that enables, if a certain enumeration is directly related to an FpML or ISO coding scheme, to label that enumeration with the corresponding coding scheme canonical URI, so every time that coding scheme is updated, the enumeration will be automatically updated.

_What is being released?_

This release updates `ISOCurrencyCodeEnum` to keep it in sync with the latest ISO 4217 coding scheme.
- The following enum value have been added:
    - `XCG <"Caribbean Guilder">`

_Review directions_

In Rosetta, select the Textual Browser and inspect the changes identified:

Changes can be reviewed in PR: [#3596](https://github.com/finos/common-domain-model/pull/3596)
