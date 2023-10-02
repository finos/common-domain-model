# *Product Model - ProductBase - ContractDetails*

_Background_

This release addresses a gap in the product model that did not include a reference to the legal agreements that define the product. 
The CDM only included contract details at the trade level which can only be added after execution. Product offerings, especially 
primary issuance, exchange traded products and swap execution facilities offer products to be traded with pre-defined legal agreements

_What is being released?_

The following function updates have been made in the `cdm.base.staticdata.common.type` namespace:

- Added `ContractDeatils` to `ProductBase` to be used to define the legal agreements associated with a product. 


_Review directions_

In the CDM Portal, select the Textual Browser and inspect the changes identified above.

Inspect Pull Request: https://github.com/finos/common-domain-model/pull/2419
