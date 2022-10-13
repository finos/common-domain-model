# _Business Events - Credit Events_ #

_Background_

This release introduces the representation of credit events and how they are documented in the lifecycle of a trade. The focus is about the generic information published by the Determinations Committee on  the occurence of a credit event that can be associated to a trade. The observation of a `creditEvent` is instructed  with a new `ObservationInstruction`. This results in the details of the event being instanciated  as part of the `observationHistory` of the trade state as an `ObservationEvent`. This change does not include a functional model on how to compute and represent the effect of such business event on the trade state.

_What is being released?_

- Model coverage for the determination of credit events.
- Qualification functions for the determination of a credit event.
- Creation functions for credit events.

_Data types_

- Added new `ObservableEvent` type.
- Added new `CreditEvent` type.
- Added new `ObservationInstruction` type.
- Added new `ObservationEvent` type.
- `observation` attribute of type `ObservationEvent` added to `PrimitiveInstruction`.
- `observationHistory` attribute of type `ObservableEvent` added to `TradeState`.
- Type of `excludedReferenceEntity` in `IndexReference information` changed to `ExcludedReferenceEntity`.

_Enumerations_

- Added new `CreditEventTypeEnum` enumeraion.
- Updated `FeeTypeEnum` enumeration to support credit events.

_Functions_

- Added new `Create_Observation` function.
- Added new `Qualify_CreditEventDetermined` function.
- Updated `Create_TradeState`function to support `observation`.
- Updated `Create_PrimitiveInstruction` function to support `observation`.
- Updated function `Create_StockSplit` to work with the introduced changes.

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.
In the CDM Portal, select the Visualisation tab and select Credit Event from the menu in the left.
In the CDM Portal, select the Functions tab and select the function `Create_BusinessEvent`.
Upload one of the files:

../credit_event_examples/example_1/business_event_creation_input.json
../credit_event_examples/example_2/business_event_creation_input.json
