FpML 4.6 - Readme file
===========================================

The "xml" directory contains the following:

fpml-XXX-4-6.xsd : schema files, as follows:
    * fpml-main-4-6.xsd - Root definitions.
    * fpml-doc-4-6.xsd - Trade and contract definitions and definitions relating to validation.
    * fpml-shared-4-6.xsd - Shared definitions used widely throughout the specification. These include items such as base types, shared financial structures, etc.
    * fpml-enum-4-6.xsd - Shared enumeration definitions. These definitions list the values that enumerated types may take.
    * fpml-asset-4-6.xsd - Underlyer definitions plus some types used by them (e.g. ones relating to commissions or dividend payouts).
    * fpml-msg-4-6.xsd - Definitions related to messaging and workflow.
    * fpml-ird-4-6.xsd - Interest rate derivative product definitions.
    * fpml-fx-4-6.xsd - Foreign exchange product definitions.
    * fpml-cd-4-6.xsd - Credit derivative product definitions.
    * fpml-com-4-6.xsd - Commodity product definitions.
    * fpml-eqd-4-6.xsd - Equity Option and Equity Forward Product Definitions.
    * fpml-return-swaps-4-6.xsd - Return Swaps Product Definitions.
    * fpml-correlation-swaps-4-6.xsd - Correlation Swaps Product Definitions.
    * fpml-dividend-swaps-4-6.xsd - Dividend Swaps Product Definitions.
    * fpml-variance-swaps-4-6.xsd - Variance Swaps Product Definitions.
    * fpml-eq-shared-4-6.xsd - Definitions shared by types with Equity Underlyers.
    * fpml-bond-option-4-6.xsd - Bond and Convertible Bond Options Product Definitions.
    * fpml-option-shared-4-6.xsd - Shared option definitions used for defining the common features of options.
    * fpml-loan-4-6.xsd - Loan Notices and Product Definitions.
    * fpml-valuation-4-6.xsd - Valuation result sets and related definitions.
    * fpml-reporting-4-6.xsd - Messages used for requesting and providing valuation reporting results.
    * fpml-mktenv-4-6.xsd - Definitions of market environment data structures such as yield curves, volatility matrices, and the like.
    * fpml-riskdef-4-6.xsd - Definitions of valuation and sensitivity results. They include detailed definitions of sensitivity calculations and are intended to be used by sophisticated users.
    * fpml-allocation-4-6.xsd - Allocation Components
    * fpml-confirmation-4-6.xsd - Confirmation components
    * fpml-contract-notification-4-6.xsd - Contract notification components
    * fpml-credit-event-notification-4-6.xsd - Credit event notification components
    * fpml-matching-status-4-6.xsd - Matching Status Components
    * fpml-pretrade-4-6.xsd - Pre-trade messaging components
    * fpml-tradeexec-4-6.xsd - Trade execution messaging components
    * fpml-trade-notification-4-6.xsd - Trade notification Components
    * fpml-posttrade-confirmation-4-6.xsd - Post-trade confirmation messaging components
    * fpml-posttrade-execution-4-6.xsd - Post-trade execution messaging components
    * fpml-posttrade-negotiation-4-6.xsd - Post-trade negotiation messaging components
    * fpml-posttrade-4-6.xsd - Post-trade messaging components
    * fpml-reconciliation-4-6.xsd - Cash flow matching and Portfolio Reconciliation messaging components



Plus, the xml directory contains subdirectories for each subset of the 
FpML standard, namely:

commodity-derivatives
credit-derivatives
correlation-swaps
equity-options
equit-forwards
bond-options
equity-swaps
variance-swaps
fx-derivatives
interest-rate-derivatives
inflation-swaps
dividend-swaps
loan
msg - messaging; which includes examples for:
 allocations
 amendments
 cash flow matching
 confirmations
 increases
 novations
 party-roles
 terminations
 portfolio reconciliation
 contract notifications
valuation


Each subdirectory contains a number of example files.  These files are named as follows:
YY_exNN_long_description.xml, 
where
YY is the subset identifier and
NN is an integer number.

The examples have a schemaLocation attribute that references their parent 
directory.  In other words, the FpML schema must be present in the parent 
directory of the example file for the example file to validate using the 
schemaLocation attribute.

The schemaLocation attribute previously referenced the examples' own directory, 
and extra copies of the schema files were placed in each example directory.
This simplified validation of the examples and helped certain tools to work
properly, but caused some users confusion.

The examples have been validated using Xerces J v 2.4.1.