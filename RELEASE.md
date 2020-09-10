# *Model Optimisation: Extract Party References from Product*

_What is being released_

This change is part of the ongoing model refactoring to externalise the definition of the parties involved in a transaction from the definition of the transacted product.

Update function `Create_ClearedTrade` to correctly assign the counterparties on the resulting beta and gamma trades.

_Review Direction_

In the CDM Portal, use the Textual Browser to review the function `Create_ClearedTrade`, and use the Instance Viewer to review the Clearing Accepted and Clearing Rejected examples.

In Rosetta Core, review the function `Create_ClearedTrade` in file `model-cdm-functions`, and on the Visualisation panel review the Clearing Accepted and Clearing Rejected examples.
