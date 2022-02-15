# *DSL Syntax - List operations: sum, min, max, join, sort, first and last*

_What is being released?_

This release introduces a number of syntax keywords related to list operations.

*`sum`*

- Returns a summed total from the items of a list of `int` or `number`.

*`min`/`max`*

- Returns the maximum or minimum item from a list based on a comparable (e.g. `string`, `int`, `number`, `date`) item or attribute.

*`join`*

- Returns a `string` concatenated from the items of a list, optionally separated with a delimiter `string`.

*`sort`*

- Returns a list in sorted order based on a comparable (e.g. `string`, `int`, `number`, `date`) item or attribute.

*`first`/`last`*

- Returns the first or last item from a list.

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
