### Create Roll Business Event

#### New Execution
- Execution is performed on `2021-3-18` between parties `Global Bank Inc (Seller)` and `UK Bank plc (Buyer)`
- `10000000` notional of `GB00B24FF097` are sold with dirty price of `1.0075 GBP`, a repo rate of `0.4%` and haircut of `1%`
- Purchase date is on `2021-3-19` and repurchase date `2021-3-22`

#### Repo Roll at Expiry
- On the repurchase date on `2021-3-22`, the same trade is being rolled into a new trade
- The existing trade is terminated with quantities brought down to zero and a new trade is instantiated
- The new effective date (purchase date) is `2021-3-22` and new termination date (repurchase date) is `2021-3-25`
- Otherwise same notional, same price, same repo rate and same haircut as the original trade
