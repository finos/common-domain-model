# Event Model - Contribution of ISLA - CDM for Securities Lending*

_What is being released?_

This release contributes modelling components proposed by ISLA to extend the CDM event model in term of Securities Lending products. The release extends existing CDM event model concepts to support allocation, re-allocation, initial Settlement, and part and full return, of a Securities Lending contract.  In addition a new product and event model is introduced for the billing function.  Visualisation examples are included for all events covered as part of the contribution in a new folder `Security Lending`.

_Details_

_Allocation and Re-Allocation_

The `Create_Allocation` and `Create_SplitPrimitive` functions have been extended to be fully formed functions that are able to support multiple allocations.  A new `Create_Reallocation` function has been introduced to allow specification of a reallocation event through the `ReallocationInstruction` data type consisting of: the original block trade, existing splits to be decreased, existing splits to be increased, and details of any new splits produced by the reallocation event. 

Updates to Existing Data Types and Functions

-

New Data Types and Functions

-

Visualisation

- See examples `Allocation` and `Reallocation` 

_New Settlement, Part and Full Return_

The `Create_Transfer` function has been extended to support creation of cash and security transfers representing the settlement of a security lending transaction.  A new `Create_Return` function has been introduced to allow specification of the part or full return of a Security Lending Transaction through the `ReturnInstruction` data type consisting of: the trade being partially or fully returned, the quantity being returned, and the return date.

Updates to Existing Data Types and Functions

-

New Data Types and Functions

-

Visualisation
The workflow of the new, partial, and full return, settlement events are demonstrated using the CDM workflow event model reflecting the instruction and settlement of each event.
- See examples `New Settlement Workflow`, `Part Return Settlement Workflow` and `Full Return Settlement Workflow`.

_Billing_

The process of invoicing fees associated with a Security Lending transaction has been modelled with the addition of the `SecurityLendingInvoice` data type, to represent the information needing to be passed from one party to the other.  The `Create_SecurityLendingInvoice` function has been introduced to allow specification of the information required to populate an invoice, including calculation of the billing amounts, through the `BillingInstruction` data type consisting of: the sending and receiving party, the billing start and end date, a `BillingRecordInstruction`, and a `BillingSummaryInstruction`.

New Data Types and Functions

- SecurityLendingInvoice
- Create_SecurityLendingInvoice
- BillingInstruction
- BillingRecordInstruction
- BillingSummaryInstruction


Visualisation
- See example `Billing`.

_Review directions_

In the CDM Portal, select the Textual Browser and search for any of the changes specified above.
In Rosetta, select Visualisation and search for any of the examples defined above.
