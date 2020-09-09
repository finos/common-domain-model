# *Model Optimisation: Extract Party References from Product*

_What is being released_

This change is part of the ongoing model refactoring to externalise the definition of the parties involved in a transaction from the definition of the transacted product.

Update function `Create_ClearedTrade` to correctly populate the counterparties on the resulting beta and gamma trades.

_Review Direction_

In Rosetta Core (https://ui.rosetta-technology.io/), review the Clearing Accepted and Clearing Rejected samples on the Visualisation panel.
