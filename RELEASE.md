# *DSL Syntax - Syntax upgrades for regulatory rules*

_What is being released?_

* Additional validation rules have been added to identify incorrect syntax when using the minBy or maxBy operator.

* A simplification of the syntax for extracting information from a CDM object. The word `multiple` is no longer required after `extract`.

# *CDM Model - Simplification of regulatory rules*

_What is being released?_

An update for all reporting rules currently using ``extract multiple`` syntax. The use of the `` multiple`` operator is no longer necessary.
An update of regulatory rule ``CrossCurrencySwapBuyerSeller`` which had invalid syntax.
An update of regulatory rule ``CDSPrice`` to use syntax which is more human readable.

**Model Change**
* Removal of redundant ``multiple`` keyword from ``extract`` rule.
* Update of regulatory rule ``CrossCurrencySwapBuyerSeller``
* Update of regulatory rule ``CDSPrice``

_Review directions_

In Rosetta Core use the Textual View to inspect the reporting rules. 

See `reporting rule TradingDateTime`, which has had the redundant ``multiple`` keyword removed.
See `reporting rule CrossCurrencySwapBuyerSeller`, which has had the invalid syntax removed.
See `reporting rule CDSPrice`, which has been rewritten.
