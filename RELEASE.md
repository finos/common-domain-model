# *Model Change - Removed unnecessary comments from the logical model*

_What is being released?_

This release provides a cleaner version of the CDM logical model that is viewable in the ISDA CDM Portal.  About 500 unnecessary commments were removed from the model in order to minimize distractions while reading the model.  These comments were preceded by the symbol // or inserted between the symbols /* and * /. Over 100 of these comments began with the phrase TODO or similar, indicating future work to be considered.  All of these comments have been archived and are under review to identify and prioritize work to be done.

The only type of comments that have not been removed are those that provide useful guidance to the users, most typically found in the product qualifications and synonyms where additional explanation is helpful for user interpretation between lines of code.

_Review directions_

In the CDM Portal, select the Textual Browser, search for the symbol //.  The only cases that should be found are examples of guidance comments or the use of // in a URL.  Also, search for /* , there should not be any cases of this notation.

# *Model Change and Technical Change - DSL upgrades for regulatory rules*

_What is being released?_

**Technical Change**
* Removal of redundant ``multiple`` keyword from ``extract`` rule.
* New syntax to allow ``maxBy`` and ``minBy`` to call rules and find ``minimum``/``maximum`` of things that are comparable directly.

**Model Change**
* Rewrite of reporting rule CrossCurrencySwapBuyerSeller as DSL changes show it to be invalid
* Update of all reporting rules currently using ``extract multiple`` syntax

_Review directions_

In the CDM Portal use the Textual Browser to inspect the reporting rules. 

See `reporting rule CrossCurrencySwapBuyerSeller`, which failed the new validation checks meaning it would have been broken in the generated Java code. This rule has been updated to be correct using new syntax.

See `reporting rule TradingDateTime`, which has had the redundant ``multiple`` keyword removed.

# *Technical Change - Bugfix to Java code*

_What is being released?_

The setXXX methods in RosettaModelObjectBuilders now accept null as an argument. Setting a value to null has the effect of clearing out the value for that attribute.
