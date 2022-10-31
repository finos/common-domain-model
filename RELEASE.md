# *Product Model - Transfer Type Enumeration*

_What is being released?_

* The Enumeration list `TransferTypeEnum` has been renamed `ScheduledTransferTypeEnum` to identify more clearly cashflows associated with lifecycle events.
* The Enumeration value `BrokerageCommission` has been repositioned from the relabeled `ScheduledTransferTypeEnum` to `FeeTypeEnum`.

_Review Directions_

In the CDM Portal, select the Textual Browser, navigate to the model components mentioned above.

# *Product Model - FpML Mapping of Interest Rate Known Amount Schedule*

_Background_

This release extends the product mapping coverage for the FpML Intererst Rates Known Amount Schedule structure.

_What is being released?_

* Relax the cardinality of `rateSpecification` to optional instead of required and add a condition that `rateSpecification` or `principalPayment` must be present.
* Update the mapping for the FpML element `knownAmountSchedule` to `PayoutBase`’s `PriceQuantity`.
* Refine the qualification function for fixed float interest rate swap to cover the use case of single final payment.

_Review Directions_

In the CDM Portal, select the Textual Browser, navigate to types mentioned above and inspect their structural definitions and associated data conditions.
