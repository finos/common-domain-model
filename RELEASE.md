# Product Model - New PrincipalReturn transfer type

_Background_

When a securities loan ends, the borrower will need to return the securities to the lender, and the lender will need to return the collateral that they received against that loan to the borrower.

Similarly, during the term of the loan, the borrower may decide that they wish to return a proportion of the shares that they have been lent.

The transfer of these assets between parties needs to be performed using the `TransferInstruction`.

_What is being released?_

A new option PrincipalReturn is being added to the `ScheduledTransferEnum`. This will allow the `TransferInstruction` to differentiate
between:

- Settlements, where the securities and collateral are initially transferred between the parties
- Recalls, where the borrower is mandated by the lender to return some shares
- Returns, where the borrower is deciding that they want to return some or all of the shares

_Review Directions_

Changes can be reviewed in PR: [#4349](https://github.com/finos/common-domain-model/pull/4349)