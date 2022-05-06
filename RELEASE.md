# *Reporting - CFTC*

## _Background_

This release will fix reporting timestamps to use the correct ISO8601 date time format.

## _What is being released_

* Updated to the `Now` function used in CFTC reporting to generate the correct format.

## _Review Directions_

- In Rosetta open up a DRR workspace and select Report `CFTC / Part45` and Dataset `CFTC Event Scenarios`
- Scroll to column `97 Reporting timestamp` and notice the new format