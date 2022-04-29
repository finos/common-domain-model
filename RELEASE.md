# Extensions to date types Collateral, Collateral Balance and Collateral Portfolio 

_What is being Released_

Extensions have been made to enable the user more options for collateral related information to be referenced in the CDM. This connects trade data, collateral portfolios and balances to legal agreement data for varied use cases including DRR . The following extensions in the CDM model will enable this: 
1.	New attributes added to data type `Collateral` for `portfolioidentifier` and `collateralPortfolio` this allows user to identify collateral portfolios related to trades and also list collateral components and balances
2.	New attribute `payerReceiver` added to data type `CollateralBalance` to allow representation of both the Payer Receiver (party1 or party2) and the Collateral direction (posted or received)
3.	New attribute `collateralAgreement` added to data type `CollateralPortfolio` this will extend to options to retrieve data from related collateral agreements needed for various use cases
4.	The data type ‘CollateralPortfolio` has been made a [root Type] to allow for independent use in the CDM model

_Review Directions_

In the CDM Portal, select the Textual Browser and search and inspect the additions laid out above across the following data types: 

1.	`Collateral`
2.	`CollateralBalance`
3.	`CollateralPortfolio`


