# *Utility Functions - Vector Math Library*

_What is being released?_

A library of utility functions for working with vectors (ordered collections of numbers) has been added to support modelling functions that require complex mathematical operations.  Typical usage will include averaging and compounding calculations for floating amounts..

The functions are located in the namespace `base-math-func`.

New functions include:

* `ToVector`: Creates a vector from a list of numbers.
* `SelectFromVector`: Selects a single value from a vector (list of numbers), i.e. result = val[index].  
  * Returns null if the specified vector is empty or if the specified index is out of range.  
  * Returns the element of the vector at the specified index.
* `LastInVector`: Returns the last value in a vector.  If the vector is empty, returns null
* `AppendToVector`: Appends a single value to a vector
* `VectorOperation`: Generates a result vector by applying the specified arithmetic operation to each element of the specified left and right vectors in turn.  i.e. result[n] = left[n] [op] right[n], where [op] is the arithmetic operation specified per the `ArithmeticOperationEnum` enum list.  This function can be used to, for example, multiply or add two vectors.
* `VectorScalarOperation`: Generates a result vector by applying the specified arithmetic operation and scalar right value to each element of the specified left vector in turn. i.e. result[n] = left[n] [op] right[n], where [op] is the arithmetic operation specified per the `ArithmeticOperationEnum` enum list.  This function can be used, for example, to multiply a vector by a scalar value, or to add a scalar to a vector.
* `VectorGrowthOperation`: Generates a result vector where the first component is the specified `baseValue` (typically 1) and each following nth component is the product of the previous component and the nth element of a seperate specified vector. For example: result[1] = baseValue * factor[1] and result[n] = result[n-1] * factor[n]. Typically factor[n] = 1.1 can represent a 10% increase, and 0.9 would correspond to a 10% decrease. The resulting vector will have the one more element than the specified list of factors.  This function is typically useful for performing compounding calculations.


A new scalar function has been added to better support floating rate processing:
* `RoundToPrecision`:  Rounds a specified number to a specified precision (in decimal places) using a roundingMode of type `RoundingDirectionEnum`.  This is similar to `RoundToNearest` but takes a precision rather than an amount, and uses a different rounding mode enumeration that supports more values.

# *Utility Functions - Basic Date Math Library*

_What is being released?_

A library of utility functions for working with dates and lists of dates has been added to CDM to support modelling functions that require to ma to perform mathematical operations on date.  Typical usage will  generate list of dates for modular rate calculations in floating amounts.

A simple Java reference implementation accompanies this change. However users can provide their own implementations of these functions using a more robust date math library.

The functions are located in the namespace `base-datetime-func`.

New functions include:

* `CombineBusinessCenters`: Creates a `BusinessCenters` object that includes the union of business centers in the two specified lists.
* `RetrieveBusinessCenterHolidays`: Returns a merged list of holidays for the specified business centers.
* `DayOfWeek`: Returns the day of week corresponding to the specified date
* `AddDays`: Adds the specified number of calendar days to the specified date.  A negative number will generate a date before the specified date.
* `DateDifference`: Returns the difference in days between two specified dates.  A negative number implies the first specified date is after the second.
* `LeapYearDateDifference`: Returns the difference in leap year calendar days between  two specified dates. (That is, the number of dates that happen to fall within a leap year.)  A negative number implies the first specified date is after second.
* `SelectDate`: Selects a date from a list of dates based on index.  If not found, return nothing.
* `LastInDateList`: Returns the last date in a list of dates
* `AppendDateToList`: Adds a date to a list of dates
* `PopOffDateList`:  Removes the last element from a list of dates

The following are also new functions. A simple Java reference implementation is also provided.
* `IsWeekend`: Returns whether the specified date is a weekend.  The implementation currently assumes a 5 day week with Saturday and Sunday as holidays.  A more sophisticated implementation might use the business centers to determine which days are weekends, but most jurisdictions where derivatives are traded follow this convention.
* `IsHoliday`: Returns whether a day is a holiday for the specified business centers
* `IsBusinessDay`: Returns an indicator of whether the specified date is a good business date given the specified business centers.  True => good date, i.e. not a weekend or holiday. False means that it is either a weekend or a holiday
* `AddBusinessDays`: Returns a good business date that has been offset by the given number of business days given the specified business centers.  A negative value implies an earlier date (before the specified originalDate), and a positive value a later date (after the specified date).
* `GenerateDateList`: Creates a list of good business days starting from the startDate and going to the end date, inclusive, omitting any days that are weekends or holidays according to the specified business centers.
