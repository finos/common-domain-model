# *Legal Agreement Model - Eligible Collateral Schedule*

_What is being released?_

Under Credit Support Agreement Elections, mapping exercises identified a number of validation errors due to the model initially focussing on IM documentation, during mapping a VM agreement these were spotted. This release resolves some of the fields which are mandatory for IM only and makes them optional. The elections covered in this release are as follows: 

* generalSimmElections
* substitutedRegime
* conditionsPrecedent
* custodyArrangements

_Review Directions_

In the CDM portal use the text viewer to review the following:

* Review enhancements to type CreditSupportAgreementElections 
*	Locate elections generalSimmElections, substitutedRegime, conditionsPrecedent, custodyArrangements
* Check cardinality changes for each from (1..1) to (0..1)


