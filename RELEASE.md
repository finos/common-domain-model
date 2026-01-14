# *Product Model - New Recall Provisions and Recall transfer type*

_Background_

In securities lending an agent lender lends shares out on behalf of their clients who own the shares. Occasionally, the owner of the shares will need the shares back from the firm they have been lent to. This is often required when they have sold the shares, or a share goes special. In this instance the agent lender will initiate a Recall of the shares back from the borrower.

The physical transfer of the shares back to the lender can be performed using a `TransferInstruction`. However, details of the recall criteria need to be available on the trade itself.

_What is being released?_

To support the entry and processing of recalls the following enhancements have been made:

Addition of new `RecallProvision` type, which includes attributes required to define a recall
Update to `TerminationProvision` to add `RecallProvision` and update type conditions accordingly
Addition of new Recall option to `ScheduledTransferEnum` for operational support of processing a recall using a `TransferInstruction`

_Review Directions_

Changes can be reviewed in PR: [#4341](https://github.com/finos/common-domain-model/pull/4341)
