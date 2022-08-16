# *Event Model - FpML Synonym Mappings for amendment events*

_Background_

The FpML mapping for business events was previously adjusted to map FpML event messages to a `WorkflowStep` instruction, i.e., a `WorkflowStep` containing a proposed `BusinessEvent`. Doing so enabled the use of the `WorkflowStep` instruction with the function `Create_AcceptedWorkflowStepFromInstruction` to create the corresponding fully-specified `WorkflowStep`.

_What is being released?_

This release extends the FPML synonym mappings to address amendment events.

_Review Directions_
 
In the CDM Portal, select Ingestion and review the samples below, which have been mapped to `WorkflowStep` instructions:

fpml-5-10/incomplete-processes/msg-ex59-execution-advice-trade-amendment-F02-00.xml
fpml-5-10/incomplete-processes/msg-ex60-execution-advice-trade-amendment-correction-F02-10.xml

In the CDM Portal, select Instance Viewer, and review the above samples in the `FpML Processes` folder, which create fully-specified `Workflowstep` events from the ingested instructions.

# *Product Model - FpML Synonym Mapping for the Product Identifier of Bond Options*

_What is being released?_

FpML mappings have been enhanced to support mapping of `productIdentifier` for Bond and Convertible Bond Options.

_Review Directions_
 
In the CDM Portal, select Ingestion and review the samples below, which now contain a product identifier:

fpml-5-10/products/rates/bond-option-uti.xml
fpml-5-10/products/rates/cb-option-usi.xml

# *Product Model - FpML Synonym Mapping for FX Volatility Swaps*

_What is being released?_

FpML mappings have been enhanced to resolve issues with the FpML mappings for FX Volatilty Swaps.  `volatilityStrike` and currency of `vegaNotional` are now both mapped

_Review Directions_
 
In the CDM Portal, select Ingestion and review the sample below, which now contain a product identifier:

fpml-5-10/incomplete-products/fx-derivatives/fx-ex31-volatility-swap.xml
