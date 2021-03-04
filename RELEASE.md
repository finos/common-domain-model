# *Technical Change - Bugfix to Typescript code generator*

_What is being released?_

Bugfix. The Typescript code generator has been updated to generate a missing interface, which is needed to make use of the generated Typescript code.

_Review directions_

In the CDM Portal, use the Downloads icon and subsequently the Typescript icon to download and access the generated Typescript files.

# *Technical Change - Better expose CDM data validation issues to users*

_What is being released?_

Java code, which is used by the generated CDM Java code, has been updated to better expose validation issues. Technical CDM adopters will now more easily see data validation issues as they execute CDM data validation logic in a Java execution environment.

_Review directions_

In the CDM Portal, navigate to the Downloads icon and then to the Java icon to download all Java code artefacts. Users can make use of the CDM Demo icon (also within Downloads) to see how to invoke CDM data validation logic in Java.

# *CDM Model: Expanded set of enumerations in RegulatoryRegimeEnum*
_What is being released?_ 

Additional regimes have been added to the `RegulatoryRegimeEnum` which is used to express the required regimes for initial margin documentation. The `RegulatoryRegimeEnum` is used as an enumeration for attributes in the `ApplicableRegime` and `SubstitutedRegime` data types within the legal agreements model.  

The new enumerated values are `BrazilMarginRules`, `UnitedKingdomMarginRules`, `SouthAfricaMarginRules`, `SouthKoreaMarginRules`, and `HongKongSFCMarginRules`, all of which have come into force in January 2021.  Each of these enumerated values has a complete description that uses the text provided in the relevant regulatory supplement.

_Review directions_

In the CDM Portal select the Textual Browser, search for ‘ApplicableRegime’ and ‘SubstitutedRegime’, click on the ‘RegulatoryRegimeEnum’ next to the ‘regime’ attribute and observe the expanded list of regimes, including the ones noted above.
