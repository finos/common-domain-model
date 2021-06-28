The Common Domain Model
=======================

**There are seven modelling dimensions** to the CDM:

#. Product
#. Event
#. Legal Agreement
#. Process
#. Reference Data
#. Mapping (Synonym)
#. Namespace

The first section defines the general design principles of the CDM. The following sections define each of the CDM modelling dimensions. Selected examples of model definitions are used as illustrations to help explain each dimension and include, where applicable, data samples to help demonstrate the structure. All the Rosetta DSL modelling components that are used to express the CDM are described in the `Rosetta DSL Documentation`_

The complete model definition, including descriptions and other details can be viewed in the `Textual Browser <https://portal.cdm.rosetta-technology.io/#/text-browser>`_ on the ISDA CDM Portal.

Design Principles
-----------------

The CDM supports the market objectives of standardisation via a set of design principles that include the following concepts:

* Normalisation through the abstration of common components, e.g. *price* or *quantity*
* Construction by composition and qualification, i.e. "bottom-up" approach
* Embedded processing logic, e.g. data validation, state-transition logic
* Organisation into logical layers using namespaces
* Mapping to existing industry messaging formats

Normalisation through abstraction of common components
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

To achieve standardisation across products and asset classes, the CDM is designed to identify logical components that fulfil the same function and to normalise them, even when those components may be named and treated differently in the context of their respective markets. By helping to remove the inefficiencies that exist in the industry's siloed IT environments (e.g. different systems dealing with cash, listed, financing and derivatives), such design reaffirms the goal of creating an inter-operable ecosystem for the processing of transactions across asset classes.

An example of this contribution is the normalisation of the concepts of *quantity*, *price* and *party* in the representation of financial transactions. The CDM identifies that, regardless of the asset class or product type, a financial transaction always involves two counterparties *trading* (i.e. buying or selling) a certain financial product in a specific quantity and at a specific price. Both quantity and price are themselves a type of *measure*, i.e. an amount expressed in a specific unit which could be a currency, a number of shares or barrels, etc. An exchange rate between currencies, or an interest rate, also fit that specification and can be represented as prices.

This approach means that a single logical concept such as *quantity* represents concepts that may be named and captured differently across markets: e.g. *notional*, *principal*, *amount* etc. This in turn allows to normalise processes that depend on this concept: for instance, how to perform an allocation (essentially a split of the quantity of a transaction into several sub-transactions) or an unwind, which would usually be handled differently by specialised IT systems for each asset class.

To maintain such normalisation feature and avoid specialising the model according to each use case, it is imperative that any request to add new model components or extend existing ones is analysed against existing components, to find common patterns that should be factored into common components. For instance, when developing the model for *averaging* options that are often used for commodity products (i.e. where multiple price observations are averaged through time, to be compared to the option's strike price), the components should be developed (and named) such that they could be re-used across asset classes.

Composability
^^^^^^^^^^^^^
