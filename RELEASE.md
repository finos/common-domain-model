# *User Documentation - CDM Eligible Collateral Representation*

_What is being released_

CDM Eligible Collateral Representation User Documentation - STORY-720: Publish Eligible Collateral User Documentation on CDM Documentation site

_Review Directions_

Please review the CDM Eligible Collateral Representation user documentation which has now been converted to format to be published on CDM Documentation site. For reference the document has already had a peer review and has previously been published as a PDF to ISDA members.

# *DSL Syntax - Reduce keyword to merge or aggregate a list*

_What is being released_

This release introduces the `reduce` keyword to merge or aggregate the items of a list into a single item based on an expression.  The `reduce` keyword can be used to implement simple operations such as summing a list of numbers, joining a list of strings, or finding a maximum or minimum value in a list, and well as operations on complex objects.

Upcoming releases will contain additional syntax to more concisely specify simple operations `sum`, `join`, `min` and `max`.

_Syntax_

```reduce <parameter1>, <parameter1> [ <expression to merge or aggregate any two list items, parameter1 and parameter2> ]```

_Example_

In the example below, the input list of numbers are combined by addition.

```
 func SumNumbers: 
    inputs:
        numbers number (0..*)
    output:
        total number (1..1)

    set total:
        numbers 
            reduce a, b [ a + b ]
```

_Example_

In the example below, the input list of strings are concatenated together along with a delimiter.  Given a list "a", "b", "c", and a delimiter ",", the resulting string would be "a,b,c".

```
 func JoinStrings: 
    inputs:
        strings string (0..*)
        delimiter string (1..1)
    output:
        result string (1..1)

    set result:
        strings 
            reduce a, b [ a + delimiter + b ]
```

_Example_

In the example below, the input list of numbers are aggregated to find the maximum number.  Given two list items, `n1` and `n2`; keep the greater item and discard the lesser item. 

```
 func FindMaxNumber: 
    inputs:
        numbers number (0..*)
    output:
        max number (1..1)

    set max:
        numbers 
            reduce n1, n2 [ if n1 > n2 then n1 else n2 ]
```

_Example_

In the example below, the input list of `Vehicle` are aggregated to find the vehicle with the most powerful engine.

```
 func FindVehicleWithMaxPower: 
    inputs:
        vehicles Vehicle (0..*)
    output:
        vehicleWithMaxPower Vehicle (1..1)

    set vehicleWithMaxPower:
        vehicles 
            reduce v1, v2 [ if v1 -> specification -> engine -> power > v2 -> specification -> engine -> power then v1 else v2 ]
```

_Review Directions_

In the CDM Portal, select the Textual Browser, and search for usages such as `Sum`.
