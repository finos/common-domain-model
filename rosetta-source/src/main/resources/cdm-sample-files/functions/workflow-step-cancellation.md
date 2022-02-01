## Basic use case for New/Cancel

A trade is thought to have been agreed and has been executed and recorded. However this is an error. The trade did not happen so the the record should be cancelled. Later on, another new trade is indeed agreed and executed correctly.

> 12-Dec-1994; 18:11 UTC
    Phone trade for a plain vanilla Fix/Float swap with quantity of 99,999

1 ) **_New Step_** created at 12-Dec-1994; 18:12 UTC by Party A
 - action = NEW
 - businessEvent includes price, quantity and trade details
 - workflow meta.globalKey=NEW123 (illustrative only)

> 12-Dec-1994; 18:50 UTC
    Internal reconcilers observe a trade matching error.

2 ) **_Cancel Step_** created at 12-Dec-1994; 18:55 UTC by Party A
- action = CANCEL
- previousWorkflowStep = NEW123 (illustrative only)

> The lineage is established to indicate that the original new trade is CANCELLED as of 18:55 UTC

> 12-Dec-1994; 19:12 UTC
    Phone trade for a plain vanilla Fix/Float swap with quantity of 50,000,000
    
3 ) **_New Step_** created at 12-Dec-1994; 19:13 UTC by Party A
 - action = NEW
 - businessEvent includes includes price, quantity and trade details
 - workflow meta.globalKey=NEW124 (illustrative only)
