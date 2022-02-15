# *DSL Syntax - List operations: sum, min, max, join, sort, first and last*

_What is being released?_

This release introduces a number of syntax keywords related to list operations.

*`sum`*

- Returns a summed total from the items of a list of `int` or `number`.

_Example_

```
 func SumNumbers: <"Returns the sum of the given list of numbers.">
    inputs:
        numbers number (0..*)
    output:
        total number (1..1)

    set total:
        numbers sum
```

*`min`/`max`*

- Returns the maximum or minimum item from a list based on a comparable (e.g. `string`, `int`, `number`, `date`) item or attribute.

_Example_

```
 func FindVehicleWithMaxPower: <"Returns the vehicle with the highest power engine.">
    inputs:
        vehicles Vehicle (0..*)
    output:
        vehicleWithMaxPower Vehicle (1..1)

    set vehicleWithMaxPower:
        vehicles 
            max [ item -> specification -> engine -> power ]
```

*`join`*

- Returns a `string` concatenated from the items of a list, optionally separated with a delimiter `string`.

_Example_

```
 func JoinStrings: <"Concatenates the list of strings, separating each element with a comma (",") delimiter.">
    inputs:
        strings string (0..*)
    output:
        result string (1..1)

    set result:
        strings 
            join [ "," ]
```

*`sort`*

- Returns a list in sorted order based on a comparable (e.g. `string`, `int`, `number`, `date`) item or attribute.

_Example_

```
 func SortDatesChronologically: <"Sorts the list of dates chronologically.">
    inputs:
        dates date (0..*)
    output:
        sortedDates date (0..*)

    set sortedDates:
        dates sort
```

*`first`/`last`*

- Returns the first or last item from a list.

_Example_

```
 func GetFirst: <"Get the first date.">
    inputs:
        dates date (0..*)
    output:
        firstDate date (0..1)

    set sortedDates:
        dates first
```

_Review Directions_

In the CDM Portal, select the Textual Browser, and search for syntax keywords.

# *Product Model - CreditDefaultPayout Index Factor and Seniority*

_What is being released_

Data type `IndexReferenceInformation` has been extended to include `indexFactor` and `seniority`.

- `indexFactor` - represents the index version factor
- `seniority` - Defines the seniority of debt instruments comprising the index.  The enumerated values in the CDM have been populated with linkage to the FpML scheme Credit Seniority Scheme (http://www.fpml.org/coding-scheme/credit-seniority-2-3.xml)
- FpML mappings have been updated to reflect the above

_Review Directions_

In the CDM Portal, select Textual Browser, and review the data type and attributes above.
