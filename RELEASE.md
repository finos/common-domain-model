# *Reporting - CFTC*

## _Background_

This release will fix the regulatory rule `ReportingTimestamp` to output the correct ISO8601 date time format.

## _What is being released_

* The `Now` function used in CFTC reporting has now been updated to generate the correct timestamp format.

## _Review Directions_

- In Rosetta open up a DRR workspace and navigate to the `Reports` tab
- Select Report `CFTC / Part45` and Dataset `CFTC Event Scenarios`
- Scroll to column `97 Reporting timestamp` and notice the new format
