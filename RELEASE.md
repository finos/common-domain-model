# *Product Model - FpML Mapping Enhancements for Equity Index Products*

_Background_

The recent price and quantity schedule refactoring left some inconsistency in the treatment of the quantity of equity index vs single-name options.

In the single-name case the quantity being referenced in the option payout is a number of shares, whereas in the index case the quantity was a monetary amount (unless that monetary amount was itself absent, in which case the number of index units was referenced). This means that any post-trade processing of an option contract would have to be forked between those cases, including for reporting.

_What is being released?_

The index treatment has been aligned onto the single-name one and the quantity now reflects the number of index units in all cases.

Since there was no equity index option featuring both a monetary amount and a number of index units available in the FpML test pack, a new one has been synthesized based on an existing record-keeping sample and used to adjust the mapping.

_Review Directions_

In the CDM Portal, select Ingestion and review the new sample to illustrate the new behaviour:

- fpml-5-10 > products > equity > `equity-option-price-return-index-ex03-european-call`

and compare for consistency with an existing sample:

- fpml-5-10 > products > equity > `eqd-ex04-european-call-index-long-form`
