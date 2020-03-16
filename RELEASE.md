# *CDM Model: Equity Stock Split Function*

_What is being released_

Function added to create a split stock business event based on a `SplitStockInstruction`.  The `SplitStockInstruction.adjustmentRatio` is applied to the number of securities and cash price resulting in the a `QuantityChangePrimitive` and `TermsChangePrimitive` respectively.

_Review Directions_

In the Textual Browser, review the following:

- Business event func `SplitStock`.
- Type `StockSplitInstruction`.
- Qualification func `StockSplit`.