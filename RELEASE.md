# *CDM Model - Reference Index Information*

_Background_

A gap has been identified in the CDM in not capturing the index involved in a transaction. Currently there is no object in the Common Domain Model for capturing the name of the underlying index. This release updates the CDM by adding a generic type `IndexReferenceInformation` which can be used to supply and capture an index name and id.

_What is being released?_

Added CDM type to capture `IndexReferenceInformation` 

_Review Directions_

In CDM, In textual view, 
	open static data > asset > common > type and review:
 
	- Addition of `IndexReferenceInformation` as type
	- Addition of indexName and indexId attributes under `IndexReferenceInformation`
	
	open cdm > observable > asset > type and review:
	- Addition of indexInformation attribute under FloatingRateOption

	open cdm > product > asset > type and review:
	- renaming of IndexReferenceInformation to CreditIndexReferenceInformation
	- removal of indexName and indexId attributes from IndexReferenceInformation
