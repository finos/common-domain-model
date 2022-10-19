# _Event Model - Representation of Credit Events and Corporate Actions_

_Background_

The representation of credit events and corporate actions and the corresponding functional elements have been introduced in the model. Both types of events are represented as components of an `ObservationEvent`. The later can be recorded in the new `observationHistory` of a `TradeState` by using the new primitive instruction `ObservationInstruction`.

The representation of credit events and corporate actions does not include information about the impact of the event on a particular trade. The focus of this change is  only to document the generic information published by the Determinations Committee on the occurence of a credit event or the basic information describing the ocurrence of a corporate action events.

_What is being released?_

- Model coverage for the determination of credit events.
- Model coverage for the ocurrence of corporate actions.
- Qualification functions for the determination of a credit event and corporate actions.
- Creation functions for credit events and corporate actions.

_Data types_

- Added new `ObservationInstruction` type.
- Added new `ObservationEvent` type.
- Added new `CreditEvent` type.
- Added new `CorporateAction` type.
- `observationHistory` attribute of type `ObservableEvent` added to `TradeState`.
- `observation` attribute of type `ObservationInstruction` added to `PrimitiveInstruction`.
- Type of `excludedReferenceEntity` in `IndexReference information` changed to `ReferenceInformation`.

_Enumerations_

- Added new `CreditEventTypeEnum` enumeration.
- Updated `EventIntentEnum` to support credit events.
- Updated `CorporateActionTypeEnum` with more values and documentation.
- Updated `FeeTypeEnum` enumeration to support credit events and corporate actions.


_Functions_

- Added new `Create_Observation` function.
- Updated `Create_TradeState`function to support `observation`.
- Updated `Create_PrimitiveInstruction` function to support `observation`.
- Updated function `Create_StockSplit` to work with the introduced changes.

_Qualification_

- Added new `Qualify_CreditEventDetermined` function.
- Added new `Qualify_CorporateActionDetermined` function.

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above. 

In the CDM Portal, select the Instance Viewer, and review examples:

* Credit Event Business Event > Credit Event
* Credit Event Business Event > Credit Event With Observation History
* Corporate Actions Business Event > Corporate Actions
* Corporate Actions Business Event > Corporate Actions With Observation History
