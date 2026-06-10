### Create Pair Off Business Event

#### New Execution

- Execution is performed on `2021-3-18` between parties `Global Bank Inc (Seller)` and `UK Bank plc (Buyer)`
- `10000000` notional of `GB00B24FF097` are sold with dirty price of `1.0075 GBP`, a repo rate of `0.4%` and haircut of `1%`
- Purchase date is on `2021-3-19` and repurchase date `2021-3-22`
- That same identical trade is executed twice

#### Pair Off After Eecution

- On the day of the execution on `2021-3-18`, the two identical trades are being paired-off
- A package object (i.e. a list of identifiers) is created out of the 2 underlying trade identifiers and assigned an identifier: "package"
- New executions are recreated, with this package object added to the execution details of the original trades 
