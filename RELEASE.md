# CSA type PostingObligations - Update securityProvider within CSA PostingObligations with party identity

_Background_

It has been raised that improvements can be made to capture party details more consistently within the attribute securityProvider under the data type PostingObligations, which is a CSA election structure. Currently the attributes offers a ‘string’ option

_What is being released?_

Replacing the securityProvider attribute option of string with the CounterpartyRoleEnum, this already exists in the model and offers the clarity for identifying party1 or party2 , it is also recommended to change the cardinality to (1..2) will allow for both parties to be identified as well as individually.

_Review Directions_

Changes can be reviewed in PR: #4140