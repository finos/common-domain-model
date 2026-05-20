### Create Shaping Business Event

#### New Execution
- Execution is performed on `2021-3-18` between parties `Global Bank Inc (Seller)` and `UK Bank plc (Buyer)`
- `10000000` notional of `GB00B24FF097` are sold with dirty price of `1.0075 GBP`, a repo rate of `0.4%` and haircut of `1%`
- Purchase date is on `2021-3-19` and repurchase date `2021-3-22`

#### Shaping Event
- On the day of the execution on `2021-3-18`, the trade is being split ('shaped') into multiple smaller trades to reflect a maximum asset notional limit of `4000000`
- The existing trade is terminated with quantities brought down to zero
- 3 new trades are instantiated with sizes of `4000000`, `4000000` and `2000000`, respectively.
- All new trades are associated to a common package identifier recorded in the trades' execution details that binds them together
- Otherwise same notional, same price, same repo rate and same haircut as the original trade
