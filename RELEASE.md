# Period Amount Calculations -  Cleanup  #

_What is being released?_

The FixedAmount and FloatingAmount functions have been updated to leverage the FixedAmountCalculation and FloatingAmountCalculation functions releaseed in 2.120.1.

The DayCountFraction function has been retired; it is replaced by YearFraction (from releasee 2.120.1), which is less dependent on InterestRatePayout structures and so is more generally usable. 

_Background_

An extensive set of enhancements was made in release 2.120.1 to floating and fixed amount calculation logic.  These allowed the logic to retrieve more information from the InterestRatePayout structure, and to do new types of calculations.  There were some existing uses of the old FixedAmount and FloatingAmount logic that were not updated at that time to use the new calculations.  With this release, we retire most of that legacy code, by making small enhancements to the new floating and fixed amount calculation code


_Details_

- The `FixedAmount` function has been modified to call the `FixedAmountCalculation` function.  As part of this, the function signature for `FixedAmount` has changed slightly
- The `FixedAmountCalculation` function has been modified to:
  - Allow the notional to be passed in, instead of being looked up from the InterestRatePayout.  (If omitted it looks up the notional from the InterestRatePayout)
  - Change/correct the notional representation to use notional->amount instead of quantity->mulitplier and to put the currency in notional->unitOfAmount->currency instead of notional->multiplierUnit->currency
- The `FloatingAmount` function has been modified to call the `FloatingAmountCalculation` function.  As part of this, the function signature for `FloatingAmount` has changed slightly
- The `FloatingAmountCalculation` function has been modified to:
  - Allow the notional to be passed in, instead of being looked up from the InterestRatePayout.  (If omitted it looks up the notional from the InterestRatePayout)
  - Allow the interest rate to be passed in, instead of being calculated.  (If omitted, the floating rate is calculated).
  - Change/correct the notional representation to use notional->amount instead of quantity->mulitplier and to put the currency in notional->unitOfAmount->currency instead of notional->multiplierUnit->currency
- The `LookupNotionalAmount` function has been changed as above to use amount and unitOfAmount.
- The `DayCountFraction` function/implementation has been deleted.  Use the new YearFraction function instead.
