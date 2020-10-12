# *CDM Function: CalculationPeriod Function Overlapping Period bugfix*

_What is being fixed_

The fix for the CalculationPeriod function bug. The bug caused the wrong period to be picked for a given date, for the overlapping periods, due to endDate of one period being the startDate of the next.
