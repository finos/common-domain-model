## Basic use case for New/Cancel

A trade is executed and recorded however the record was entered incorrectly. The trade record should be amended to reflect the correct details of the trade that has happened.

> 12-Dec-1994; 18:11 UTC
    Phone trade for a plain vanilla Fix/Float swap with quantity of 50,000,000

1 ) **_New Step_** created at 12-Dec-1994; 18:12 UTC by Party A
 - action = NEW
 - businessEvent includes price, quantity and trade details
 - workflow meta.globalKey=NEW123 (illustrative only)

> 12-Dec-1994; 18:50 UTC
    Internal reconcilers observe an incorrect quantity of 99,999

2 ) **_Cancel Step_** created at 12-Dec-1994; 18:55 UTC by Party A
- action = CANCEL
- previousWorkflowStep = NEW123 (illustrative only)

> The lineage is established to indicate that the original new trade is CANCELLED as of 18:55 UTC

3 ) **_New Step_** created at 12-Dec-1994; 19:13 UTC by Party A
 - action = NEW
 - businessEvent includes correct trade details with quantity of 50,000,000
