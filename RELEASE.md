# *Event Model - Create_Transfer Can Set the positionState on Securities-Lending Trades*

_Background_

For securities lending, knowing whether a trade has "Settled" is imperative, as this controls whether specific lifecycle events can be performed upon it.

_What is being released?_

- Adding logic to the Create_Transfer function to update the `positionState` to `Settled` if the payout obligations have been fulfilled.
- New functions to determine whether the payout obligations have been fulfilled, i.e., do the transferred security and cash amounts match the quantities on the payout.

_Review directions_

The changes can be reviewed in PR: [#4378](https://github.com/finos/common-domain-model/pull/4378)
