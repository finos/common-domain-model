# *Period Amount Calculations - Clean up*

_What is being released?_

The FixedAmount and FloatingAmount functions have been updated to leverage the FixedAmountCalculation and FloatingAmountCalculation functions released in 2.120.1.

The DayCountFraction function has been retired; it is replaced by YearFraction (from release 2.120.1), which is less dependent on InterestRatePayout structures and so is more generally usable.

The CalculationPeriods function has been added; it returns all calculation periods for an InterestRatePayout.
The FixedAmountCalculation function has been enhanced to return detailed intermediate results.

A few bugs in the floating amount calculations have been corrected, including:

- Handling of the notional was incorrect in a couple of functions 
- Handling of daily caps and floors on daily calculated (average and compound) was incorrect

_Background_

An extensive set of enhancements was made in release 2.120.1 to floating and fixed amount calculation logic.  These allowed the logic to retrieve more information from the InterestRatePayout structure, and to do new types of calculations.  There were some existing uses of the old FixedAmount and FloatingAmount logic that were not updated at that time to use the new calculations.  With this release, we retire most of that legacy code, by making small enhancements to the new floating and fixed amount calculation code.

Minor logic errors have been corrected and some minor enhanced functionality has been added to make using the above easier.  There has also been some minor redesign to some functions to improve processing efficiency, based on implementation experience.

_Details_

- The `FixedAmount` function has been modified to call the `FixedAmountCalculation` function.  As part of this, the function signature for `FixedAmount` has changed slightly
- The `FixedAmountCalculation` function has been modified to:
  - Allow the notional to be passed in, instead of being looked up from the InterestRatePayout.  (If omitted it looks up the notional from the InterestRatePayout)
  - Change/correct the notional representation to use notional->amount instead of quantity->multiplier and to put the currency in notional->unitOfAmount->currency instead of notional->multiplierUnit->currency
  - Return a structure holding intermediate results to explain the calculation
- The `FloatingAmount` function has been modified to call the `FloatingAmountCalculation` function.  As part of this, the function signature for `FloatingAmount` has changed slightly
- The `FloatingAmountCalculation` function has been modified to:
  - Allow the notional to be passed in, instead of being looked up from the InterestRatePayout.  (If omitted it looks up the notional from the InterestRatePayout)
  - Allow the interest rate to be passed in, instead of being calculated.  (If omitted, the floating rate is calculated).
  - Change/correct the notional representation to use notional->amount instead of quantity->multiplier and to put the currency in notional->unitOfAmount->currency instead of notional->multiplierUnit->currency
  - Refactor it into several pieces to allow intermediate calculations to be reused more efficiently
- The `LookupNotionalAmount` function has been changed as above to use amount and unitOfAmount.
- The `DayCountFraction` function/implementation has been deleted.  Use the new YearFraction function instead.
- The `GenerateObservationDatesAndWeights` function has been refactored to split it into several smaller functions to allow intermediate results to be reused without being recomputed, to improve efficiency
- The `CalculationPeriods` function has been added to product-common-schedule-func.rosetta and a Java implementation has been provided

# *Event Model - Composable Event - Updates for Execution, Contract Formation and Quantity Change Visualisation Samples*

_What is being released_

This release follows the recent work to compose business events with a list of instructions applied as input to the common business event creation function `Create_BusinessEvent`. The visualisation examples for the following business events have been refactored and augmented with the details of the input instructions: execution, contract formation, and quantity change.

_Review Directions_

In the CDM Portal, select the Instance Viewer, review the visualisation examples in the Execution Business Event, Contract Formation Business Event and Quantity Change Business Event folders and inspect how the representation of the input instructions has been embedded in each business event.
