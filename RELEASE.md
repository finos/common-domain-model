# *Base Libraries - Vector Math Library*

_What is being released?_

A small library of functions for working with vectors (ordered collections of numbers) has been added to CDM to support Rosetta functions needing to perform complex mathematical operations.  Anticipated uses include averaging and compounding calculations for floating amounts, but the functions are designed to be general use.

The functions are located in `base-math-func`.

New functions include:

* `ToVector`: Creates a vector from a list of numbers.
* `SelectFromVector`: Selects a single value from a vector (list of numbers), i.e. result = val[index].  
  * Returns null if the supplied vector is empty or if the supplied index is out of range.  
  * Returns the element of the vector at the supplied index.
* `LastInVector`: Returns the last value in a vector.  If the vector is empty, returns null
* `AppendToVector`: Appends a single value to a vector
* `VectorOperation`: Generates a result vector by applying the supplied arithmetic operation to each element of the supplied left and right vectors in turn.  i.e. result[n] = left[n] [op] right[n], where [op] is the arithmetic operation defined by arithmeticOp.  This function can be used to, for example, multiply or add two vectors.
* `VectorScalarOperation`: Generates a result vector by applying the supplied arithmetic operation and scalar right value to each element of the supplied left vector in turn. i.e. result[n] = left[n] [op] right, where [op] is the arithmetic operation defined by arithmeticOp.  This function can be used to, for example, multiply a vector by a scalar value, or add a scalar to a vector.
* `VectorGrowthOperation`: Generates a result vector by starting with the supplied base value (typically 1), and then multiplying it in turn by each growth factor, which is typically a number just above 1.  For instance, a growth factor of 1.1 represents a 10% increase, and 0.9 a 10% decrease.  The results will show the successive results of applying the successive growth factors, with the first value of the list being the supplied `baseValue`, and final value of the results list being the product of all the supplied values.  i.e. result[1] = baseValue * factor[1], result[n] = result[n-1] * factor[n].  The resulting list will have the one more element than the supplied list of factors.  This function is useful for performing compounding calculations.


A new scalar functions has been added to better support floating rate processing:
* `RoundToPrecision`:  Rounds a supplied number to a specified precision (in decimal places) using a roundingMode of type `RoundingDirectionEnum`.  This is similar to `RoundToNearest` but takes a precision rather than an amount, and uses a different rounding mode enumeration that supports more values.

# *Base Libraries - Basic Date Math Library*

_What is being released?_

A small library of functions for working with dates and lists of dates has been added to CDM to support Rosetta 
functions needing to perform date mathematics.  Anticipated uses include date list generation for modular rate 
calculations for floating amounts, but the functions are designed to be general use.

There is a basic Java language implementation that can be used, or users can provide their own implementations
of these functions using a more robust date math library.

The functions are located in `base-datetime-func`.

New functions include:

* `CombineBusinessCenters`: Creates a `BusinessCenters` object that includes the union of business centers in the two supplied lists.
* `RetrieveBusinessCenterHolidays`: Returns a merged list of holidays for the supplied business centers.
* `DayOfWeek`: Returns the day of week corresponding to the supplied date
* `AddDays`: Adds the specified number of calendar days to the supplied date.  A negative number will generate a date before the supplied date.
* `DateDifference`: Subtracts the two supplied dates to return the number of calendar days between them.  A negative number implies first is after second.
* `LeapYearDateDifference`: Subtracts the two supplied dates to return the number of leap year calendar days between them. (That is, the number of dates that happen to fall within a leap year.)  A negative number implies firstDate is after secondDate.
* `SelectDate`: Select a date from a list of dates based on index.  If not found, return nothing.
* `LastInDateList`: Return the last date in a list of dates
* `AppendDateToList`: Add a date to a list of dates
* `PopOffDateList`:  Remove last element from a list of dates

The following are implemented in Rosetta based on the above primitives.
* `IsWeekend`: Returns whether the supplied date is a weekend.  This implementation currently assumes a 5 day week with Saturday and Sunday as holidays.  A more sophisticated implementation might use the business centers to determine which days are weekends, but most jurisdictions where derivatives are traded follow this convention.
* `IsHoliday`: Returns whether a day is a holiday for the specified business centers
* `IsBusinessDay`: Returns an indicator of whether the supplied date is a good business date given the supplied business centers.  True => good date, i.e. not a weekend or holiday. False means that it is either a weekend or a holiday
* `AddBusinessDays`: Returns a good business date that has been offset by the given number of business days given the supplied business centers.  A negative value implies an earlier date (before the supplied originalDate), and a positive value a later date (after the supplied date).
* `GenerateDateList`: Creates a list of good business days starting from the startDate and going to the end date, inclusive, omitting any days that are weekends or holidays according to the supplied business centers.
