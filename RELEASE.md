# *Product Model - Enhancement for Enumerated Values for Capacity Units*

_What is being released?_

This release updates the existing `CapacityUnitEnum`. This follows a gap analysis completed on the additional capacity codes being used in the industry for OTC products, including commodities, and ISO 20022 codes. There are two types of proposed changes:

- Distinction between US and GB codes to improve clarity of the units being used.
- Addition of new codes that were not supported by the CapacityUnitEnum

The following units have been removed:

- CWT
- GAL
- T
- ST

The following new units have been added:

- UST - "Denotes a US Ton as a standard unit" replaces "ST"
- GBT - "Denotes GB Ton as a standard unit" replaces "T"

- USCWT -  "Denotes US Hundredweight unit as a standard unit" replaces CWT"
- GBCWT - "Denotes a GB Hundredweight as a standard unit"

- USGAL - "Denotes a US Gallon as a standard unit" replaces GAL
- GBGAL -  "Denotes a GB Gallon unit as standard unit"

Weight and volume units:

- BDFT - "Denotes Board Feet as a standard unit"
- CBM - "Denotes Cubic Meters as a standard unit"
- MMBBL - "Denotes a Million Barrels as a standard unit"
- G - "Denotes a Gram as a standard unit"

Environmental units:

- CRT - "Denotes Climate Reserve Tonnes as a standard unit"
- ENVCRD - "Denotes Environmental Credit as a standard unit"
- ENVOFST - "Denotes a Environmental Offset as a standard unit"

Energy units

- KWDC -  "Denotes a Kilowatt Day Capacity as a standard unit"
- KWHC - "Denotes a Kilowatt Hours Capacity as a standard unit"
- KWMC - "Denotes a Kilowatt Month Capacity as a standard unit"
- KWMINC - "Denotes a Kilowatt Minute Capacity as a standard unit"
- KWYC - "Denotes a Kilowatt Year Capacity as a standard unit"
- MWDC - "Denotes a Megawatt Day Capacity as a standard unit"
- MWHC - "Denotes a Megawatt HoursCapacity as a standard unit"
- MWMC - "Denotes a Megawatt Month Capacity as a standard unit"
- MWINC - "Denotes a Megawatt Minute Capacity as a standard unit"
- MWYC - "Denotes a Megawatt Year Capacity as a standard unit"

_Review Directions_

In the CDM Portal Textual Browser search for `CapacityUnitEnum` to see the updated list of values.
