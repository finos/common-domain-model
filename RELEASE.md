# *CDM - A CDM user can access an extended Agreement model*

_Background_

The ISDA Foundations project is a model extension built on top of the CDM that contains legal IP (contained in legal documentation references) only available to ISDA members. Additions or updates to the ISDA Foundations project can cause it to go out of sync with the CDM.

The issue [#3348](https://github.com/finos/common-domain-model/issues/3348) proposes to migrate the ISDA Foundations project to the CDM, without the ISDA legal documentation IP

Preparation must be first be done in the CDM to synchronise it with the ISDA Foundations project and simplify the migration of any ISDA Foundations components.

_What is being released?_

As part of the preparation for the migration of any ISDA Foundations components to the CDM, this release creates an extended Agreement model in the CDM. This includes adding some (already sanitised) ISDA Foundations components into CDM, and moving other components around.

1. Added `BrokerConfirmationTypeEnum` in `legaldocumentation.contract.enum` 
2. Added `BrokerConfirmation` and `IssuerTradeId` to `legaldocumentation.contract.type`
3. Added `brokerConfirmationType` attribute to `AgreementName` 
4. Moved all empty types related to “additional terms” (used in `TransactionAdditionalTerms`) to a sub-namespace: `legaldocumentation.master.additionalterms`


_Backward-incompatible changes_

None.

_Review Directions_

The change can be reviewed in PR: [#3352](https://github.com/finos/common-domain-model/issues/3352).
