# *Product Model - FpML Mapping of Interest Rate Known Amount Schedule*

_Background_

This release extends the product mapping coverage for the FpML Intererst Rates Known Amount Schedule structure.

_What is being released?_

* Updating the cardinality of `rateSpecification` to optional instead of required and adding a condition that `rateSpecification` or `principalPayment` must be present.
* Updating the mapping for the FpML element `knownAmountSchedule` to `PayoutBase`’s `PriceQuantity`.
* Updating the qualification function for fixed float interest rate swap to cover the use case of single final payment.

_Review Directions_

In the CDM Portal, select the Textual Browser, navigate to types mentioned above and inspect their structure definitions and associated data conditions.
