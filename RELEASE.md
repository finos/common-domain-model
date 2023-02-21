# *Legal Documentation & Collateral – Eligible Collateral Schedule Builder Function*

_Background_

The current CDM model provides the unambiguous data representation of an Eligible Collateral Schedules, which necessarily requires a relatively verbose definition of complex data relationships.

When using the CDM Object Builder to manually construct an Eligible Collateral Schedule members need a simpler data model to be exposed to allow a quicker and simpler method for creating more complex Eligible Collateral Schedule information.

Existing Eligible Collateral Schedule data type should not be changed as it has been tested and signed off by members as suitable for representing an unambiguous complete schedule.

_What is being released?_

Function `EligibleCollateralScheduleHelper` has been added to provide a quicker way to build an `EligibleCollateralSchedule` through the combination of common and variable schedule characteristics.  The function input `EligibleCollateralScheduleInstruction` contains common and variable `EligibleCollateralCriteria` which are merged by the function to form a complete `EligibleCollateralSchedule`.

_Review Directions_

In the CDM Portal, select the Textual Browser to inspect the types mentioned above and review the changes.

In the CDM Portal, select Downloads page, and download the ISDA CDM as Java Examples project. Open the project and review the Java example `EligibleCollateralScheduleHelperTest`.

In the CDM Portal, select the Instance Viewer, review the visualisation examples in the Eligible Collateral folder.