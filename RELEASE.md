# _Visualisations - Update securities lending Allocation example_
_Background_

Rosetta provides examples of how to perform lifecycle events. The example showing the use of the Allocation process for securities lending trades has been updated.

_What is being released?_
T
he existing Allocation visualisation has been updated as follows:

- The `after` trades are now assigned to the agent lender and the fund, rather than the borrower and the fund
- The `after` trades have been given Unique Transaction Identifiers
- The original Trade Execution that the allocation is being performed upon is no longer closed. This has to remain open as this is the agreement between the borrower and the lender.

**Compatibility**

There are no compatibility issues with the CDM as these files are separate to the core model.

_Review Directions_

The changes can be reviewed in PR: [#4464](https://github.com/finos/common-domain-model/pull/4464)
