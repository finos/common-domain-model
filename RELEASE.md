# *Collateral Model - Check Eligibility Function*

_Background_

As part of the [FINOS BMO Hackathon](https://www.finos.org/hosted-events/2023-05-03-finos-hackathon-bmo-nyc) event in May 2023, a demonstation of a [use-case](https://github.com/finos/community/discussions/251) was created that showed the CDM can be used to check collateral eligibility against multiple jurisdictions’ minimum collateral requirements and specific eligible collateral schedules.

The demonstation was successful has been codified into the CDM and the `CheckEligibilityByDetails` function.

_Model Changes_

- Added new function/types:
    - `CheckEligibilityByDetails` - Applys an `EligibilityQuery` to check against a set of given `EligibleCollateralSpecification` to determine which collateral meets the eligibility and can be used/posted for delivery.

_Review directions_

- Review the changes in the model
- Inspect the Pull Request: https://github.com/finos/common-domain-model/pull/2439

# *Product Model/ Collateral – ISO Country Code Enum and connection to Asset/Issuer Criteria*

_What is being released?_

A new enumeration list has been added to the Common Domain Model named `ISOCountryCodeEnum`.
The following changes have been made in the CDM to connect to this (including replacing the string with the enum list in the following occurrences):

1.	Data type `IssuerCriteria` attribute `issuerCountryOfOrigin`, string and metadata scheme removed, `ISOCountryCodeEnum` added.
2.	Data type `AssetCriteria` attribute `assetCountryOfOrigin`, string and metadata scheme removed, `ISOCountryCodeEnum` added.
   
In addition, the following change has been made for connecting to ISO Currency codes: 

1.	Data type `AssetCriteria` attribute `denominatedCurrency`, string and metadata scheme removed, `CurrencyCodeEnum` added.


_Review Directions_

In the CDM Portal, select the Textual Browser and inspect the changes mentioned above. 

Changes can be review in PR: https://github.com/finos/common-domain-model/pull/2477

