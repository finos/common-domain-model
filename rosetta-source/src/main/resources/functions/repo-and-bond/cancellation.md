### Create Cancellation Business Event

#### New Execution
- Execution is performed on `2021-3-18` between parties `Global Bank Inc (Seller)` and `UK Bank plc (Buyer)`
- `10000000` notional of `GB00B24FF097` are sold with dirty price of `1.0075 GBP`, a repo rate of `0.4%` and haircut of `1%`
- Purchase date is on `2021-3-19` and repurchase date `2021-3-22`

#### Cancellation Before Expiry
- Prior to the original repurchase date on `2021-3-21`, the trade is being cancelled, effectively with its termination date brought forward
- The existing trade is terminated with quantities brought down to zero and a new trade is instantiated
- The termination date (repurchase date) of the new trade is `2021-3-21`, i.e. the day of the cancellation
- Otherwise same notional, same price, same repo rate and same haircut as the original trade
- Repurchase settlement for the new trade to be handled as a separate event
