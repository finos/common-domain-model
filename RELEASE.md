# *CDM - A CDM user can access a contract's closed status component*

_Background_

The ISDA Foundations project is a model extension built on top of the CDM that contains legal IP (contained in legal documentation references) only available to ISDA members. Additions or updates to the ISDA Foundations project can cause it to go out of sync with the CDM.

The issue [#3348](https://github.com/finos/common-domain-model/issues/3348) proposes to migrate the ISDA Foundations project to the CDM, without the ISDA legal documentation IP

Preparation must be first be done in the CDM to synchronise it with the ISDA Foundations project and simplify the migration of any ISDA Foundations components.

_What is being released?_

As part of the preparation for the migration of any ISDA Foundations components to the CDM, this release allows CDM users to access a contract’s closed status component in the event namespace.

1. Moved `ClosedStateEnum` to `event.common.enum` 
2. Moved `ClosedState` to `event.common.type`


_Backward-incompatible changes_

None.

_Review Directions_

The change can be reviewed in PR: [#3378](https://github.com/finos/common-domain-model/pull/3378).
