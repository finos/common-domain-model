# Product Model - Update Scheduled Transfer types

_Background_

In a securities lending trade, the lender will deliver the security being lent to the borrower. This is performed on a date agreed by the parties (i.e. it is scheduled) and forms part of the settlement obligations of the trade.

The actual transfer of the shares can be processed using the TransferInstruction primitive instruction. However, under the scheduledTransfer type there is a mandatory transferType attribute where the type of this scheduled transfer must be defined. This enum currently has no option that can be used to define the delivery of the security to the borrower.

_What is being released?_

The changes being contributed are as follows:

Update the cdm.product.common.settlement.enum ScheduledTransferEnum "PrincipalPayment` option to now be named "Principal".
This allows "Principal" to be used for any transfers related to the principal i.e. the cash paid as collateral or the securities being delivered.
The Asset itself within the transfer will describe what is actually being transferred.
Update the Create_CashFlowFromSettlementPayout function in cdm.product.template.func to use "Principal" instead of "PrincipalPayment"
Update the FpML mapping and ingest namespaces to use "Principal" instead of "PrincipalPayment" where appropriate

_Review Directions_

Changes can be reviewed in PR: #4203