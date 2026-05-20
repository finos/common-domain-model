### Create On Demand Rate Change Business Event

#### New Execution

- Execution is performed on `2021-3-18` between parties `Global Bank Inc (Seller)` and `UK Bank plc (Buyer)`
- `10000000` notional of `GB00B24FF097` are sold with dirty price of `1.0075 GBP`, a repo rate of `0.4%` and haircut of `1%`
- Purchase date is on `2021-3-19` and repurchase date `2021-3-22`

#### Rate Change Before Expiry

- On the day after the purchase date on `2021-3-20`, a new interest rate of `0.5%` is being negotiated between the parties
- The existing trade is terminated with quantities brought down to zero and a new trade is instantiated
- The new effective date (purchase date) is `2021-3-20` and the termination date (repurchase date) is unchanged as `2021-3-22`
- No settlement (cash or securities) is taking place at that point, all settlements will occur on the repurchase date
