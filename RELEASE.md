# *Legal Agreement Model - Collateral Agreement Elections*

_What is being released?_

Under Credit Support Agreement Elections, mapping exercises identified a number of validation errors due to the model initially focussing on IM documentation, during mapping a VM agreement these were spotted. This release resolves some of the fields which are mandatory for IM only and makes them optional. The elections covered in this release are as follows: 

* generalSimmElections
* substitutedRegime
* conditionsPrecedent
* custodyArrangements

_Review Directions_

In the CDM portal use the textual browser to review the following:

* Review enhancements to type `CreditSupportAgreementElections`
*	Locate generalSimmElections, substitutedRegime, conditionsPrecedent, custodyArrangements
* Check cardinality changes for each from (1..1) to (0..1)

# *Legal Agreement Model - Eligible Collateral Schedule*

_What is being released?_

Eligible Collateral Criteria allows specification of a Listing Type in order to define constraints based on the index, sector, or exchange an asset is listed on.  The cardinality on these attributes has been changed so that multiple values for each can be specified.


_Review Directions_

In the CDM portal use the textual browser  to review the following:

* Review enhancements to type `ListingType`


