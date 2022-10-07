# _Business Events - Credit Events_ #

_Background_

The present release adds CDM model support for credit events and its related functions. Credit events are represented as `primitiveInstructions` of a new `observation` type, which can be expanded in the future to include other observation-related events such as corporate actions. The added functions also allow for the addition of the `creditEvent` as a part of the `observationHistory` of a trade state. Thus, with an `instruction` containing a `creditEvent`, a trade and the corresponding dates will be able to generate a `businessEvent`, the `after` trade state of which contains the trade plus an `observationHistory` with the specified `creditEvent`. The representation of credit events currently does not include information about the impact of the event on a particular trade but only the generic information published by the Determinations Committee, which can be associated to a trade as described above without modifying it.

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