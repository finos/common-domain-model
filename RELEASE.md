# *Documentation: DSL Documentation Overhaul*

_What is being released_

Complete overhaul of Rosetta DSL documentation, with the syntax now stabilised following a complete set of changes introduced recently.

The changes include:

* Change from ``class`` to ``type``
* Unification of validation components under ``condition`` attached to type
* All functions unified under ``func``, including object qualification logic
* New ``annotation`` concept introduced, to unify all metadata
* Syntax made more consistent across all these components

The examples illustrating the syntax in the documentation have been fully updated.

_Review Direction_

In the CDM Documentation, review the Rosetta Modelling Components section at: https://docs.rosetta-technology.io/dsl/documentation.html

# *Product Model: Add FpML Equity Samples*

_What is being released_

Add FpML Equity samples and set up ingestion regression tests.
  
_Review Directions_

In the Ingestion Panel, try the following samples folder `products > equity`:

- `eqs-ex04-zero-strike-long-form.xml`
- `eqs-ex05-single-stock-plus-fee-long-form.xml`
- `eqs-ex06-single-index-long-form.xml`
- `eqs-ex09-compounding-swap.xml`
- `eqs-ex11-on-european-index-underlyer-short-form.xml`
- `eqs-ex12-on-european-index-underlyer-short-form.xml`
- `eqs-ex13-pan-asia-interdealer-share-swap-short-form.xml`
- `eqs-ex14-european-interdealer-share-swap-short-form.xml`
- `eqs-ex15-forward-starting-pre-european-interdealer-share-swap-short-form.xml`
- `eqs-ex16-forward-starting-post-european-interdealer-share-swap-short-form.xml`
- `eqs-ex17-cfd.xml`
- `eqs-ex18-pan-asia-interdealer-index-swap-short-form.xml`
- `eqs-ex19-european-interdealer-fair-value-share-swap-short-form.xml`
- `trs-ex02-single-equity.xml`
- `trs-ex03-single-stock-execution-swap-with-fixing-and-dividend-payment-dates.xml`
- `trs-ex04-index-ios.xml`
