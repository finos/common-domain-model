# *ICMA Contribution - CDM for Repo and Bonds*

_Background_

This release extends the product model and event model to cover repurchase agreements and the associated lifecycle events.

_What is being released?_

This release includes the following:

* Additions to AssetClass and ProductTaxonomy to add Money Market and repurchase agreement taxonomy requirements.
* Add ICMA as a documentation publisher.
* Add OnVenue as a ExecutionTypeEnum.
* Replaced SecurityFinancePayout and SecurityFinanceLeg with AssetPayout and AssetLeg.
* Additional provisions for cancellation (early termination of a repo contract).
* Additional provisions for pairoff.
* Added the following functions to create primitive instructions:

  * Roll
  * Early Termination
  * On-Demand Rate Change
  * On-Demand Interest Payment
  * Substitution
  * Shaping
  * PairOff
  * Partial Delivery
  * Reprice

_Review Directions_

In the CDM Portal, select the Textual Browser, navigate to types mentioned above and inspect their structure definitions and associated data conditions.

For the new events, select the Functions tab and upload a test file that matches the input parameters of the primitive instruction generator function. Use the output of the function to upload to Create_BusinessEvent.
