# *BusinessEvent and TradeState Creation - Missing activityDate in closedState when creating a Termination*

_Background_

Issue link [#3969](https://github.com/finos/common-domain-model/issues/3969)

CDM does not populate the activityDate in the closedState of a TradeState when using the Create_TerminationInstruction function, thus always creating a TradeState with validation errors
[TradeState → state → closedState → activityDate]

Activity date cannot be added automatically from Create_TradeState as the activityDate exist in the Create_BusinessEvent and is not passed down to Create_TradeState

This change creates a provision for the activityDate by setting the effectiveDate of the QuantityChange when setting zero amounts in the Create_TerminationInstruction

Impact: the new parameter added to Create_TerminationInstruction has to be supported by all other functions calling it"

_What is being released?_

[#3969 (comment)](https://github.com/finos/common-domain-model/pull/4363)

Primary Change:
Add input parameter in Create_TerminationInstruction
effectiveDate AdjustableOrRelativeDate (0..1) <""Date of Termination"">

Assign activityDate in closed state of a Terminated TradeState
Assumption: All quantityChange instructions in the business event will be the same

Impact Changes:

functions using Create_TerminationInstruction need to supply the additional parameter effectiveDate

func Create_RollPrimitiveInstruction
use effectiveRollDate as the effectivDate for termination

func Create_OnDemandRateChangePrimitiveInstruction
use effectiveDate for termination

func Create_CancellationPrimitiveInstruction
use cancellationDate as the effectivDate for termination

func Create_RepricePrimitiveInstruction
use effectiveRepriceDate as the effectivDate for termination

func Create_AdjustmentPrimitiveInstruction
use effectiveRepriceDate as the effectivDate for termination

func Create_ShapingInstruction
use empty as the effectivDate for termination
no suitable date value to repurpose, issue will still exist

func Create_PartialDeliveryPrimitiveInstruction
use empty as the effectivDate for termination
no suitable date value to repurpose, issue will still exist

PS: This solution needs to be further tested and discusses

_Review Directions_

Changes can be reviewed in PR: [#4439](https://github.com/finos/common-domain-model/pull/4439)
