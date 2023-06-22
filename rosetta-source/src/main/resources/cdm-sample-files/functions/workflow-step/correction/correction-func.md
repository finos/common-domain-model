## Basic use case for New/Correct

A trade is executed and recorded however there is an error on the record. The trade did happen so the record should be corrected.

> 12-Dec-1994; 18:11 UTC
    Phone trade for a plain vanilla Fix/Float swap with quantity of 50,000,000

1 ) **_New Step_** created at 12-Dec-1994; 18:12 UTC by Party A
 - action = NEW
 - businessEvent includes price, quantity and trade details
 - workflow meta.globalKey=NEW123 (illustrative only)

> 12-Dec-1994; 19:13 UTC
    Booking error of an incorrect quantity of 99,999 is observed

2 ) **_Correct Step_** created at 12-Dec-1994; 19:13 UTC by Party A
 - action = CORRECT
 - businessEvent includes correct trade details with quantity of 50,000,000
 - previousWorkflowStep = NEW123 (illustrative only)

> The lineage is established to indicate that the original new trade is CORRECTED as of 19:13 UTC
