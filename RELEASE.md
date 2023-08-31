# *CDM Model - Reference Index Information*

_Background_

A gap has been identified in the CDM regarding reference index information. Currently, there is no CDM object which can capture the name of the underlying index. 

_What is being released?_

This release modifies existing CDM type `IndexReferenceInformation` to capture generic 'index name & id' and separates specific credit reference information
The release includes the following changes:

- Type `IndexReferenceInformation` is splitted into generic `IndexReferenceInformation` and credit specific `CreditIndexReferenceInformation`
- Added indexName and indexId attributes under `IndexReferenceInformation`
- Moved other specific credit reference attributes to `CreditIndexReferenceInformation`
- Added `indexReferenceInformation` attribute under type FloatingRateOption

_Review Directions_

In CDM, In textual view, review the updates listed above in:
	
 - static data > asset > common > type
 - cdm > observable > asset > type
 - cdm > product > asset > type
