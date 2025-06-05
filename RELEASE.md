# _Event Model - TradeState Quantity Decrease update_

_Background_

The `QuantityDecreasedToZero` function currently assumes that a decrease has occurred when the quantity is reduced to zero by checking that:

1. The quantity in the after TradeState is zero.
2. The quantity in the before TradeState is zero or greater.

This leads to incorrectly flagging as decreases the cases where both the `before` and `after` quantities are zero, which should not be considered decreases.

_What is being released?_

This contribution modifies the logic in the `QuantityDecreasedToZero` function so that a decrease is only considered when the before quantity is greater than zero, not equal to zero.

_Review Directions_

The changes can be reviewed in PR: [#3759](https://github.com/finos/common-domain-model/pull/3759)
