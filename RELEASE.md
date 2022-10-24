# _Product Model - Day Count Fractions 2021 ISDA Definitions_

_Background_



_What is being released?_

This release adds to the model the day count fractions as defined in the 2021 ISDA Definitions. The model already contained day count fractions, as defined in the 2006 ISDA Definitions or the 2000 ISDA Definitions, hence this release aims at including the relevant text from the new interest rate derivatives definitions booklet, adding new day count fractions only present in the 2021 ISDA Definitions and reviewing that the day count fraction calculations to see if any needed to be added in the model.

- The 2021 ISDA Definitions text for the following DayCountFractionEnum were added to the existing ones:
ACT/360, ACT/365L, ACT/365.FIXED, ACT/ACT.ICMA, ACT/ACT.ISDA, 30E/360, 30E/360.ISDA, 30/360, RBA Bond Basis (quarter), RBA Bond Basis (semi-annual), RBA Bond Basis (annual).
- The following DayCountFractionEnum was created as such day count fraction is newly added in the 2021 ISDA Definitions and not present in previous booklets:
CAL/252
- The functions for the day count fractions listed under i. above were amended to include the relevant text from the 2021 ISDA Definitions and the calculations already present in the model were checked to ensure alignment with calculations from the 2021 ISDA Definitions. The 2021 ISDA Definitions did not change the calculation method of the day count fractions that were carried over from the 2006 ISDA Definitions, hence no changes were required to the calculation functions.
- The function for CAL/252 was added to the model since this day count fraction did not exist in previous ISDA Definitions.

_Review Directions_

In the CDM Portal, select the Textual Browser and navigate to the above enumerations and functions.