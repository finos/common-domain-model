### Create On Demand Interest Payment Business Event

#### New Execution
- Execution is performed on `2021-3-18` between parties `Global Bank Inc (Seller)` and `UK Bank plc (Buyer)`
- `10000000` notional of `GB00B24FF097` are sold with dirty price of `1.0075 GBP`, a repo rate of `0.4%` and haircut of `1%`
- Purchase date is on `2021-3-19` and repurchase date `2021-3-22`

#### On Demand Interest Payment Before Expiry
- Prior to the original repurchase date on `2021-3-21`, an interim interest payment is being requested
- A cashflow is added to the payout using the corresponding amount, currency, direction and settlement date of the requested payment
- The event does not yet incorporate any modification to the final repurcahse price to subtract any interest already paid
