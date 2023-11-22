# _Product Model - PTRR - Portfolio Rebalancing_

_Background_

In the technical specification for the EMIR jurisdiction, there are some reportable fields regarding PTRR (Post-Trade Risk Reduction) events. This release introduces support in the Common Domain Model to allow the complete reporting for one of these PTRR events, specifically for the Portfolio Rebalancing, by adding its corresponding value to the `EventIntentEnum` and a specific qualification function for this event.

_What is being released?_

_Enumerations_

- Added new `PortfolioRebalancing` value to the `EventIntentEnum` enumeration.

_Qualification_

- Added new `Qualify_PortfolioRebalancing` function.
- Updated the `Qualify_Execution` function to check for the absence of intent.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above. The changes can be reviewed in PR [2538](https://github.com/finos/common-domain-model/pull/2538).


# *Event / Product Model - FpML 5.13 Schema*

_What is being released?_

This release updates the FpML schema used for FpML to CDM ingestion. The version has been updated from `5.13 Second Working Draft` to `5.13 Third Working Draft`.  For further details, visit https://www.fpml.org/spec/fpml-5-13-3-wd-3/.

_Review Directions_

This change has no impact on the model or test expectations. The changes can be reviewed in PR [2542](https://github.com/finos/common-domain-model/pull/2542).