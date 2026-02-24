# *BusinessEvent and TradeState Creation - Missing activityDate in closedState when creating a Termination #3969*

_Background_

Issue link [#3969](https://github.com/finos/common-domain-model/issues/3969)

CDM does not populate the activityDate in the closedState of a TradeState when using the Create_TerminationInstruction function, thus always creating a TradeState with validation errors
[TradeState → state → closedState → activityDate]

Activity date cannot be added automatically from Create_TradeState as the activityDate exist in the Create_BusinessEvent and is not passed down to Create_TradeState

This change creates a provision for the activityDate by setting the effectiveDate of the QuantityChange when setting zero amounts in the Create_TerminationInstruction

Impact: the new parameter added to Create_TerminationInstruction has to be supported by all other functions calling it

_What is being released?_

#3969 [comment](https://github.com/finos/common-domain-model/issues/3969#issuecomment-3772786891)

Primary Change:
Add input parameter in Create_TerminationInstruction
effectiveDate AdjustableOrRelativeDate (0..1) <"Date of Termination">

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

Changes can be reviewed in PR: [#4443](https://github.com/finos/common-domain-model/pull/4443)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` and `bundle` dependency:

Version updates include:
- `DSL` `9.75.3` Performance improvements and bug fix. See DSL release notes: [9.75.3](https://github.com/finos/rune-dsl/releases/tag/9.75.3)
- `bundle` `11.108.0` Performance improvements and bug fix.

_Review Directions_

The changes can be reviewed in PR: [#4409](https://github.com/finos/common-domain-model/pull/4409)

# *Ingestion Framework for FpML - Principal Payment Schedule*

_Background_

An issue was identified related to the FpML mapping of `PrincipalPaymentSchedule` for single final payments. For further information, see [#4076](https://github.com/finos/common-domain-model/issues/4076).

_What is being released?_

Synonym Ingest mappings related to `PrincipalPaymentSchedule` have been updated to set `principalPaymentSchedule->finalPrincipalPayment` when `principalPayment->finalPayment` is true.

_Review Directions_

Changes can be reviewed in PR: [#4402](https://github.com/finos/common-domain-model/pull/4402)
