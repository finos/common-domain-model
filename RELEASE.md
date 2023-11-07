# *Product Model/ Collateral – ISO Country Code Enum and connection to Asset/Issuer Criteria*

## _What is being released?_

A new enumeration list has been added to the CDM named `ISOCountryCodeEnum`
The following changes have been made in the CDM to connect to this:

1.	Data type `IssuerCriteria` attribute `issuerCountryOfOrigin`, string and metadata scheme removed `ISOCountryCodeEnum` added.
2.	Data type `AssetCriteria` attribute `assetCountryOfOrigin`, string and metadata scheme removed `ISOCountryCodeEnum` added.
   
In addition, the following change has been made for connecting to ISO Currency codes: 

1.	Data type `AssetCriteria` attribute `denominatedCurrency`, string and metadata scheme removed `CurrencyCodeEnum` added.


## _Review Directions_

In the CDM Portal, select the Textual Browser and inspect the changes mentioned above. 

Changes can be review in PR: https://github.com/finos/common-domain-model/pull/2477

