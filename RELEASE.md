# Legal Agreement - CSA type PostingObligations - Update securityProvider

_Background_

It has been raised that improvements can be made to capture party details more consistently within the attribute securityProvider under the data type PostingObligations, which is a CSA election structure. Currently the attribute offers a `string` option.

_What is being released?_

Replacing the securityProvider attribute option of string with the CounterpartyRoleEnum will offer the clarity for identifying party1 or party2. It is also recommended to change the cardinality to (1..2). This allows for both parties to be identified as well as individually.

_Review Directions_

Changes can be reviewed in PR: [#4230](https://github.com/finos/common-domain-model/pull/4230)
