# *Extension to ExecutionInstruction for Collateral and Create_ExecutionPrimative*

_Background_

This release is for the extension to enable the user with the option to include a collateral amount at the point of execution of a trade in the CDM.  

_What is being released?_

1.	New attribute `collateral` added to data type `ExecutionInstruction` this allows user to identify collateral amount related to a trade  
2.	New set added to func  `Create_ExecutionPrimative` to allow representation of a collateral amount to the function that creates a trade execution


_Review Directions_

In the CDM Portal, select the Textual Browser and search and inspect the additions laid out above across the following data types: 

1.	Data type `ExecutionInstruction`
2.	Function `Create_ExecutionPrimative`
