# *Trade Model - Identification of the nature of a Trade Identifier*

_What is being released?_

This release contains an updated `TradeIdentifier` data and a new enumeration `TradeIdentifierTypeEnum` in order to enumerate the nature of a Trade Identifier.  Previously this has only been possible through the inclusion of a `metadata scheme`.

- The enumeration list `TradeIdentifierTypeEnum` contains two values: `UniqueTransactionIdentifier` and `UniqueSwapIdentifier`

_Review Directions_

In CDM Portal Ingestion tab review sample trade:

fpml-5-10 > products > rates > cdm-xccy-swap-after-usi-uti
