# *Ingestion Framework - Remove reference to hard coded choice functions*

_Background_

This change is required to remove references to the hard coded choice functions such as ToDocumentChoice. These functions are implemented in static Java and can instead be implemented with the switch operator in the Rune syntax.

_What is being released?_

Everywhere in the ingestion functions where we use a choice function, the function call has been replaced with a switch operator which is now capable of switching over types that extend a base type.
The following rosetta files are affected:

- rosetta-source/src/main/rosetta/ingest-fpml-confirmation-common-func.rosetta
- rosetta-source/src/main/rosetta/ingest-fpml-confirmation-datetime-func.rosetta
- rosetta-source/src/main/rosetta/ingest-fpml-confirmation-legal-func.rosetta
- rosetta-source/src/main/rosetta/ingest-fpml-confirmation-message-func.rosetta
- rosetta-source/src/main/rosetta/ingest-fpml-confirmation-party-func.rosetta
- rosetta-source/src/main/rosetta/ingest-fpml-confirmation-payment-func.rosetta
- rosetta-source/src/main/rosetta/ingest-fpml-confirmation-pricequantity-func.rosetta
- rosetta-source/src/main/rosetta/ingest-fpml-confirmation-product-commodityswap-func.rosetta
- rosetta-source/src/main/rosetta/ingest-fpml-confirmation-product-creditdefaultswap-func.rosetta
- rosetta-source/src/main/rosetta/ingest-fpml-confirmation-product-equityswaptransactionsupplement-func.rosetta
- rosetta-source/src/main/rosetta/ingest-fpml-confirmation-product-returnswap-func.rosetta
- rosetta-source/src/main/rosetta/ingest-fpml-confirmation-product-swap-func.rosetta
- rosetta-source/src/main/rosetta/ingest-fpml-confirmation-tradestate-func.rosetta

_Review Directions_


Changes can be reviewed in PR: [#4156](https://github.com/finos/common-domain-model/pull/4156)
