# *Infrastructure - Serialise dates to ISO-8601 format*

_What is being released_

This release changes the Java to JSON serialisation format for dates to ISO-8601 format, so that it represents a date starting with the year, followed by the month, and day.  For example, "2022-01-31" represents the 31st January 2022.

Previously, the serialised format contained separate fields for day, month and year.

```
"unadjustedDate" : {
    "day": 31,
    "month": 1,
    "year": 2022
}
```

Now, the serialised format is an ISO-8601 date string.

```
"unadjustedDate" : "2022-01-31"
```

_Review Directions_

In the CDM Portal, select Ingestion and review any sample.
