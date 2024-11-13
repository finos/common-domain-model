
# Design Principles

The purpose of this section is to detail the CDM design principles that
any contribution to the CDM development must adhere to. The CDM supports
the market objectives of standardisation via a set of design principles
that include the following concepts:

-   **Normalisation** through abstraction of common components, e.g.
    *price* or *quantity*
-   **Composability** where objects are composed and qualified from the
    bottom up
-   **Mapping** to existing industry messaging formats, e.g. *FpML*
-   **Embedded logic** to represent industry processes, e.g. data
    validation or state-transition logic
-   **Modularisation** into logical layers, using *namespace*
    organisation

## Normalisation

**To achieve standardisation across products and asset classes, the CDM
identifies logical components that fulfil the same function** and
normalises them, even when those components may be named and treated
differently in the context of their respective markets. By helping to
remove inefficiencies that siloed IT environments can create (e.g.
different systems dealing with cash, listed, financing and derivative
trades make it harder to manage aggregated positions), such design
reaffirms the goal of creating an inter-operable ecosystem for the
processing of transactions across asset classes.

An example of this approach is the normalisation of the concepts of
*quantity*, *price* and *party* in the representation of financial
transactions. The CDM identifies that, regardless of the asset class or
product type, a financial transaction always involves two counterparties
*trading* (i.e. buying or selling) a certain financial product in a
specific quantity and at a specific price. Both quantity and price are
themselves a type of *measure*, i.e. an amount expressed in a specific
unit which could be a currency, a number of shares or barrels, etc. An
exchange rate between currencies, or an interest rate, also fit that
description and are represented as prices.

This approach means that a single logical concept such as *quantity*
represents concepts that may be named and captured differently across
markets: e.g. *notional* or *principal* amount etc. This in turn allows
to normalise processes that depend on this concept: for instance, how to
perform an allocation (essentially a split of the quantity of a
transaction into several sub-transactions) or an unwind, instead of
specialised IT systems handling it differently for each asset class.

**It is imperative that any request to add new model components or
extend existing ones is analysed against existing components** to find
patterns that should be factored into common components and avoid
specialising the model according to each use case. For instance, in the
model for *averaging* options (often used for commodity products,
whereby multiple price observations are averaged through time to be
compared to the option's strike price), the components are built and
named such that they can be re-used across asset classes.

## Composability

**To ensure re-usability across different markets, the CDM is designed
as a composable model** whereby financial objects can be constructed
bottom-up based on building-block components. A composable and modular
approach allows for a streamlined model to address a broad scope of
operational processes consistently across firms' front-to-back flows
and across asset classes. The main groups of composable components are:

-   **Financial products**: e.g. the same *option* component is re-used
    to describe option payouts across any asset class, rather than
    having specialised *Swaption*, *Equity Option* or *FX option* etc.
    components.
-   **Business events** that occur throughout the transaction lifecycle
    are described by composing more fundamental building blocks called
    *primitive events*: e.g. a *partial novation* is described by
    combining a *quantity change* primitive event (describing the
    partial unwind of the transaction being novated away) and a
    *contract formation* primitive event (describing the new contract
    with the novation party).
-   **Legal agreements** that document the legal obligations that
    parties enter into when transacting in financial products are
    constructed using *election* components associated to functional
    logic that is re-usable across different types of agreement: e.g.
    the same logic defining the calculation of margin requirements can
    be re-used across both initial and variation margin agreements.

In this paradigm, the type of object defined by the CDM, whether a
financial product, business event or legal agreement, is not declared
upfront: instead, the type is inferred through some business logic
applied onto its constituents, which may be context-specific based on a
given taxonomy (e.g. a product classification).

**The benefit of this approach is that consistency of object
classification is achieved through how those objects are populated**,
rather than depending on each market participant's implementation to
use the same naming convention. This approach also avoids the model
relying on specific taxonomies, labels or identifiers to function and
provides the flexibility to maintain multiple values from different
taxonomies and identifier sets as data in the model related to the same
transaction. This has a number of useful application, not least for
regulatory purposes.

## Mapping

**To facilitate adoption by market participants, the CDM is made
compatible with existing industry messaging formats.** This means that
the CDM does not need to be implemented "wholesale" as a replacement
to existing messaging systems or databases but can coexist alongside
existing systems, with a translation layer. In fact, the CDM is designed
to provide only a logical model but does not prescribe any physical data
format, neither for storage nor transport. This means that translation
to those physical data formats is built-in, and the CDM is best thought
of as a logical layer supporting inter-operability between them.

---
**Note:**
Although the CDM features a *serialisation* mechanism (currently in
JSON), this format is only provided for the convenience of representing
physical CDM objects and is not designed as a storage mechanism.

---

The need for such inter-operability is illustrated by a typical trade
flow, as it exists in derivatives: a trade may be executed using the
pre-trade FIX protocol (with an FpML payload representing the product),
confirmed electronically using FpML as the contract representation, and
reported to a Trade Repository under the ISO 20022 format. What the CDM
provides is a consistent logical layer that allows to articulate the
different components of that front-to-back flow.

In practice, mapping to existing formats is supported by *synonym*
mappings, which are a compact description in the CDM of how data
attributes in one format map to model components. In turn, those synonym
mappings can support an *ingestion* process that consumes physical data
messages and converts them into CDM objects.

**The CDM recognises certain formats as de-facto standards that are
widely used to exchange information between market participants.** Their
synonym mappings are included and rigorously tested in each CDM release,
allowing firms that already use such standards to bootstrap their CDM
implementation. Besides, because most standard messaging formats are
typically extended and customised by each market participants (e.g. FpML
or FIX), the CDM allows the synonym representation for those standards
to be similarly inherited and extended to cover each firm's specific
customisation.

## Embedded logic

**The CDM is designed to lay the foundation for the standardisation,
automation and inter-operability of industry processes**. Industry
processes represent events and actions that occur through the
transaction's lifecycle, from negotiating a legal agreement to
allocating a block-trade, calculating settlement amounts or exchanging
margin requirements.

While FINOS defines the protocols for industry processes in its
documentation library, differences in the implementation minutia may
cause operational friction between market participants. Even the
protocols that have a native digital representation have written
specifications which require further manual coding in order to result in
a complete executable solution: e.g. the validation rules in FpML, the
Recommended Practices/Guidelines in FIX or CRIF for SIMM and FRTB, which
are only available in the form of PDF documents.

Traditional implementation of a technical standard distributed in prose
comes with the risk of misinterpretation and error. The process is
duplicated across each firm adopting the standard, ultimately adding up
to high implementation costs across the industry.

**By contrast, the CDM provides a fully specified processing model that
translates the technical standards supporting industry processes** into
a machine-readable and machine-executable format. Systematically
providing the domain model as executable code vastly reduces
implementation effort and virtually eliminates the risk of
inconsistency. For instance, the CDM is designed to provide a fully
functional event model, where the state-transition logic for all
potential transaction lifecycle events is being specified and
distributed as executable code. Another CDM feature is that each model
component is associated with data validation constraints to ensure that
data is being validated at the point of creation, and this validation
logic is distributed alongside the model itself.

## Modularisation
**The set of files that define the CDM data structures and functions are
organised into a hierarchy of [namespaces](namespaces.md)**. The first level in the
namespace hierarchy corresponds to the layer of the CDM that the
components belong to, and those CDM layers are organised from inner- to
outer-most as follows:

![](/img/namespaces.png)

Namespaces have many benefits:
-   Accelerated understanding of the model by allowing users to easily
    see a high-level view of the model and easily find, select, and
    study specific areas of interest
-   Faster and easier to find data types and functions for
    referencing/use in new components
-   Allowing for partial adoption of areas of interest in the model
-   Smaller upgrades representing new versions limited to the name
    spaces that are impacted

Each of these higher-level namespaces is further divided into
lower-level namespaces. The independent components in each namespace are
organised according to their core purpose but can be referenced from
anywhere in the model to allow all the components to work together for a
complete modelling solution. E.g. below is the *product* namespace:

![](/img/product-namespace.png)

**When developing new CDM components, the positioning of those
components in the namespace hierarchy is critical** as part of the
design (or potentially the re-organising of the hierarchy following the
new development), to ensure the CDM remains well organised.
