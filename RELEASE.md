# *ICMA Contribution - CDM for Repo and Bonds*

_Background_

This release covers two phases of the ICMA CDM for Repo and Bonds Initiative. Phase 1 covered CDM design of a classic repurchase agreements with a fixed term and fixed rate, and events for purchase and repurchase. Phase 2 extended the CDM product model and event model to cover open term and floating rate repurchase agreements, and the associated lifecycle events.

_What is being released?_

This release includes the following:

* Additions to AssetClass and ProductTaxonomy to add Money Market and repurchase agreement taxonomy requirements.
* Add ICMA as a documentation publisher.
* Add OnVenue as a ExecutionTypeEnum.
* Replaced SecurityFinancePayout and SecurityFinanceLeg with AssetPayout and AssetLeg.
* Additional provisions for cancellation (early termination of a repo contract).
* Additional provisions for pairoff.
* Added the functions to create primitive instructions and qualifications for the following lifecycle events:

  * Roll
  * Early Termination
  * On-Demand Rate Change
  * On-Demand Interest Payment
  * Shaping
  * PairOff

_Review Directions_

In the CDM Portal, select the Textual Browser, navigate to types and event functions mentioned above and inspect their structure definitions and associated data conditions.

In addition for the new events, there are 2 ways to review how they work:

1. In Rosetta, select the Visualisation tab and select the Repo and Bond section, where a set of pre-canned event inputs and their output can be inspected.
2. In Rosetta, select the Functions tab and then select the `Create_BusinessEvent` function, where an arbitrary event instruction object can be passed. To build that instruction input, each new event is associated to an instruction generator function (e.g. `Create_RollPrimitiveInstruction` for the Roll event) that can be invoked in the Functions tab as well.
