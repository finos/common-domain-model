# *Event Model: Direct Principal and Agency Clearing Model *

_What is being released_

The clearing function has been enhanced to support the Direct Agency clearing model. Direct clearing is when the risk party to the trade is facing the CCP, either through its own account (principle) or its clearing member acting as agent (angency).

Notable changes:
 - Clearing function renamed to `Create_CrearedTrade` and is now consistent with the strategic approach.
 - Descriptions added/modified for `ClearingInstruction` and `Create_CrearedTrade`.
 - `party1`/`party2` alias used in clearing function to link parties have been moved to `ClearingInstruction`.
 - `clearerParty1`/`clearerParty2` added to in `ClearingInstruction` to support agency clearing.
 - Constrain added to `Create_CrearedTrade` inputs so that the parties and roles are present in the alpha trade.

_Review direction_

In the CDM Portal, open the Textual Browser and see:

- func `Create_CrearedTrade`
- type `ClearingInstruction`

# *Infrastructure: Namespace hierarchy to be sorted*

_What is being released_

Infrastructure change to show the files in the namespace hierarchy in sorted order.
