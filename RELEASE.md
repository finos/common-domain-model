# *Exchange Traded Positions Model*

_Background_

The CDM model does not currently support exchange traded positions within its framework. To that end, a collaborative effort has been made to model them within the framework of the CDM. This initiative has been a collective endeavor, drawing upon the expertise of SMEs. The primary objective has been to establish a representation of Exchange Traded Positions that aligns with the broader CDM. A working model has been reached which gathers consensus among member firms, and entails the creation of a root type `CounterpartyPositionState`, and a new type `CounterpartyPositionBusinessEvent` within the already existing `WorkflowStep` to document the state transition of a counterparty position in CDM.

_What is being released?_

- Support for exchange traded positions to CDM Core.

_Data types_

- Added new `CounterpartyPositionState` root type.
- `counterpartyPosition` attribute of type `CounterpartyPosition` added to `CounterpartyPositionState` type.
- `state` attribute of type `State` added to `CounterpartyPositionState` type.
- `observationHistory` attribute of type `ObservationEvent` added to `CounterpartyPositionState` type.
- `valuationHistory` attribute of type `Valuation` added to `CounterpartyPositionState` type.
- Added new `CounterpartyPositionBusinessEvent` type.
- `intent` attribute of type `PositionEventIntentEnum` added to `CounterpartyPositionBusinessEvent` type.
- `corporateActionIntent` attribute of type `CorporateActionTypeEnum` added to `CounterpartyPositionBusinessEvent` type.
- `eventDate` attribute of type `date` added to `CounterpartyPositionBusinessEvent` type.
- `effectiveDate` attribute of type `date` added to `CounterpartyPositionBusinessEvent` type.
- `packageInformation` attribute of type `IdentifiedList` added to `CounterpartyPositionBusinessEvent` type.
- `after` attribute of type `CounterpartyPositionState` added to `CounterpartyPositionBusinessEvent` type.
- Added new `PositionIdentifier` type.
- `identifierType` attribute of type `PositionIdentifierTypeEnum` added to `PositionIdentifier` type.
- `valuationTiming` attribute of type `PriceTimingEnum` added to `Valuation` type.
- `priceComponent` attribute of type `Price` added to `Valuation` type.
- `counterpartyPositionBusinessEvent` attribute of type `CounterpartyPositionBusinessEvent` added to `WorkflowStep` type.
- Added new `ContractBase` type.
- `contractDetails` attribute of type `ContractDetails` added to `ContractBase` type.
- `executionDetails` attribute of type `ExecutionDetails` added to `ContractBase` type.
- `collateral` attribute of type `Collateral` added to `ContractBase` type.
- Added new `CounterpartyPosition` type.
- `positionIdentifier` attribute of type `PositionIdentifier` added to `CounterpartyPosition` type.
- `openDateTime` attribute of type `dateTime` added to `CounterpartyPosition` type.
- `tradeReference` attribute of type `TradeState` added to `CounterpartyPosition` type.
- `party` attribute of type `Party` added to `CounterpartyPosition` type.
- `partyRole` attribute of type `PartyRole` added to `CounterpartyPosition` type.
- `positionBase` attribute of type `TradableProduct` added to `CounterpartyPosition` type.
- Added condition `CounterpartyPositionBusinessEventOrBusinessEventChoice` to `WorkflowStep` type.
- Added `[metadata key]` to `Collateral` type.

_Enumerations_

- Added new `PositionEventIntentEnum` enumeration.
- Added new `PriceTimingEnum` enumeration.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes listed above.