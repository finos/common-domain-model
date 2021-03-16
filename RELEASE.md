# *DSL Syntax - Syntax upgrades for regulatory rules*

_What is being released?_

* A new syntax to allow ``maxBy`` and ``minBy`` to call rules and find ``minimum``/``maximum`` of things that are comparable directly. User should follow the following patterns to use these operators: ``maxBy rule`` [rulename] and ``minBy rule`` [rulename]

# *CDM Model - Simplification of ``extract`` regulatory rule*

_What is being released?_

An update for all reporting rules currently using ``extract multiple`` syntax. The use of the `` multiple`` operator is not necessary.

**Model Change**
* Removal of redundant ``multiple`` keyword from ``extract`` rule.

_Review directions_

In the CDM Portal use the Textual Browser to inspect the reporting rules. 

See `reporting rule TradingDateTime`, which has had the redundant ``multiple`` keyword removed.
