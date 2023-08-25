# *CDM Model - Reference Index Information*

_Background_

A gap has been identified in the CDM regarding reference index information. Currently, there is no CDM object which can capture the name of the underlying index. 

_What is being released?_

This release adds a generic type `IndexReferenceInformation` which can be used to supply and capture index name and id, and includes the following updates:

- Added type `IndexReferenceInformation`
- Added indexName and indexId attributes under `IndexReferenceInformation`
- Added indexInformation attribute under FloatingRateOption
- Renamed IndexReferenceInformation to CreditIndexReferenceInformation
- Removed indexName and indexId attributes from IndexReferenceInformation

_Review Directions_

In CDM, In textual view, review the updates listed above in:
	
 - static data > asset > common > type
 - cdm > observable > asset > type
 - cdm > product > asset > type
