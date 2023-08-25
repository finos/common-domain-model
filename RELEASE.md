# *CDM Model - Addition of new type to capture Reference Index Information*

_Background_

A gap has been identified in the CDM regarding reference index information. Currently, there is no CDM object which can capture the name of the underlying index. 

_What is being released?_

This release adds a generic type `IndexReferenceInformation` which can be used to supply and capture index name and id. New CDM type to capture `IndexReferenceInformation` 

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
