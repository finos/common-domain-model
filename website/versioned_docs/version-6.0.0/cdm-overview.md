---
title: Overview of the FINOS CDM
---

**Continuous Integration:** 

[![FINOS - Incubating](https://cdn.jsdelivr.net/gh/finos/contrib-toolbox@master/images/badge-incubating.svg)](https://community.finos.org/docs/governance/Software-Projects/stages/incubating)

[![Codefresh build status]( https://g.codefresh.io/api/badges/pipeline/regnosysops/FINOS%2Fcommon-domain-model?type=cf-1)]( https://g.codefresh.io/public/accounts/regnosysops/pipelines/new/63ecb79bde06416b39d81e70)

# What is the FINOS CDM?

The FINOS Common Domain Model (CDM) is a standardised, machine-readable
and machine-executable blueprint for how financial products are traded
and managed across the transaction lifecycle. It is represented as a
[domain model](https://olegchursin.medium.com/a-brief-introduction-to-domain-modeling-862a30b38353) and distributed in open source.

For an overview of the Common Domain Model, a comparative analysis with FpML, an exploration of the CDM's historical development and events, as well as insights into its further applications, including its role in the ecosystem and support for smart contract technology, we recommend reviewing the [CDM Overview deck](/pdfs/CDM-General-Overview.pdf). 

For more detailed insight into specific CDM use cases, the following additional overview decks are available:

- [CDM Collateral Use Case Overview](/pdfs/CDM-Collateral-Use-Case-Overview-2026.pdf) - explores how the CDM supports collateral management through standardised eligible collateral schedules, margin call workflows, collateral validation, and digital documentation across OTC, repo, securities lending, and cleared markets.
- [CDM Smart Contracts Overview](/pdfs/CDM-Smart-Contracts-Overview-2026.pdf) -  outlines how the CDM enables standardized, machine-executable smart contract workflows for post-trade events such as interest rate resets, supporting automation, auditability, interoperability, and future tokenized workflows.
- [CDM Tokenization Use Case Overview](/pdfs/CDM-Tokenization-Use-Case-Overview-2026.pdf) -  explains how the CDM supports tokenized assets and digital securities through both asset-level and settlement-level tokenisation models, helping firms represent cryptoassets, security tokens, and tokenized securities consistently across legal and operational frameworks.

These materials provide practical examples of how the CDM can be implemented across financial markets and support industry-wide standardisation, automation, and interoperability.

ISDA’s Digital Regulatory Reporting (DRR) solution leverages the open-source Common Domain Model (CDM) to convert industry-agreed interpretations of new or amended regulatory reporting rules into clear, machine-executable code. This approach enhances implementation efficiency and reduces costs. Institutions contributing to the development of the ISDA DRR include (but are not limited to) those listed on [ISDA’s Digital Regulatory Reporting Page](https://www.isda.org/isda-digital-regulatory-reporting/).

If you’re interested in the Common Domain Model (CDM), please complete the [CDM Interest Form](https://www.finos.org/common-domain-model) to join our mailing list. By signing up, you’ll receive updates on CDM initiatives, meeting agendas, and other working group activities.

## CDM Training

-    **Introduction to the CDM (LFEL1016)**: A 90-minute Express Learning course covering the fundamentals of the Common Domain Model. Including how it standardizes data for financial products, trades, and lifecycle events to improve interoperability across technologies. [Enroll here](https://training.linuxfoundation.org/training/introduction-to-the-common-domain-model-cdm-lfel1016/).
-    **CDM Collateral Start-up Guide**: A set of short tutorials launched by ISDA, designed for business users to explore the CDM in the context of collateral management. This hands-on resource walks through features, tools, and implementation approaches. [Learn more](https://www.isda.org/2023/02/16/isda-collateral-initiatives/)

## Purpose

A single, digital processing standard for trade events and actions
enhances financial markets' operational efficiency in several ways:

-   **Enables inter-operability and straight-through processing** across
    firms, solutions and platforms, reducing the need for reconciliation
    caused by variations in how each firm records trade lifecycle
    events.
-   **Accelerates financial technology innovation** by providing a
    common, readily operational foundation for how technologies like
    distributed ledger, smart contracts, cloud computing, and artificial
    intelligence can be applied to financial markets.
-   **Delivers better regulatory oversight**, promotes transparency and
    alignment between regulators and market participants and enables
    consistency in regulatory reporting, by providing a standard
    representation of trade data and supporting machine executable
    reporting requirements.

For further information about the CDM and its applications, please
consult the [CDM section](https://www.finos.org/common-domain-model) of the FINOS website or contact FINOS directly
at [cdm@lists.finos.org](mailto:cdm@lists.finos.org).

## Model Overview 
The key models defined in the CDM are listed below. Click each one to learn more. 
- [Product model](product-model.md) -  describes a thing (e.g. contract or instrument) that is used to transfer financial risk between two parties.
- [Event model](event-model.md) - provides data structures to represent the lifecycle events of financial transactions.
- [Process model](process-model.md) - lays the foundation for the standardisation, automation and inter-operability of industry processes.
- [Reference data model](reference-data-model.md) – describes underlying business entities such as parties, legal entities, floating rate indexes, and so on that needed to model the in-scope products, events, legal agreements and function components.
- [Use cases](use-cases.md) – business process areas in the financial ecosystem that the CDM supports.

## Design Principles

To support the objectives described above, the CDM is built according to a set of
[design principles](design-principles.md) that include the following concepts:

-   **Normalisation** through abstraction of common components
-   **Composability** where objects are composed and qualified from the
    bottom up
-   **Mapping** to existing industry messaging formats
-   **Embedded logic** to represent industry processes
-   **Modularisation** into logical layers

These design principles are further detailed in the
[design principles](design-principles.md) section of the CDM
documentation.

## Governance

The CDM [governance framework](https://github.com/finos/common-domain-model/blob/master/GOVERNANCE.md) regulates
the development of the CDM in open source.

## Scope

The CDM covers OTC derivatives, cash securities, securities financing, commodities, and can expand to include other Capital Markets products and Asset Classes.

## CDM 2025 Roadmap

- The latest CDM roadmap of expected contributions sponsored by the presently participating trade associations: ICMA, ISDA and ISLA can be found under [Roadmap](https://github.com/finos/common-domain-model/blob/master/ROADMAP.md) . 
- Releases can be tracked on the [CDM Releases](https://github.com/finos/common-domain-model/releases/) page.

# CDM Components

**The CDM is made of three sets of components**, as laid-out in the FINOS
CDM components diagram below:

-   The FINOS CDM Distribution (in the centre, in *purple*)
-   The Rune DSL (on the left, in *grey*)
-   CDM Applications (on the right, in *blue*)

![](/img/cdm-components-diagram.png)

## FINOS CDM Distribution

The FINOS CDM distribution is openly accessible to all industry
participants, subject to the FINOS CDM open source licence. This
distribution is fully downloadable.

The FINOS CDM distribution comprises three main sets of components:

-   **Model definition**, which corresponds to the model as expressed in
    the Rune DSL and contained into a set of *.rosetta* files
    organised as [*namespaces*](namespace.md). The primary dimensions of the model are
    listed below and further described in the
    [above](cdm-overview#model-overview).
    -   Product
    -   Event
    -   Legal Agreement
    -   Process
    -   Reference Data
    -   Mapping (Synonym)
-   **Executable code distribution**, automatically generated from the
    model definitions expressed in the Rune DSL using [available code
    generators](https://docs.rosetta-technology.io/rosetta/rune-dsl/rosetta-code-generators/#what-code-generators-are-available). Once a code generator is implemented for a particular
    language, the corresponding code generation is included as part of
    the CDM build and release process, allowing the CDM to be
    automatically distributed in that language going forward.
-   **Default implementation**, comprising manually-written code (in
    Java) which, combined with the auto-generated code, provides a
    complete implementation of the model. This hand-written code is
    distributed together with the CDM to facilitate adoption by firms,
    which can directly use the CDM distribution to set-up and test an
    implementation. The default implementation can be used in its
    original state or be disabled or extended by industry participants
    in their implementations. For example, the default implementation
    uses the de-facto Java hash function to support cross-referencing in
    the CDM, but firms may elect to use an alternative implementation.

---
**Note:**
Only the Java executable code distribution is complete: i.e. it
represents the entire CDM as defined in Rosetta (plus any associated
default implementation). Other distributions may only capture parts of
the model: for instance, the Scala and TypeScript distributions include
the complete data model and function specifications, but not the
functions' executable code.

---

## Rune DSL

The CDM is written in a Domain-Specific Language (DSL) called the
*Rune DSL*, that comprises a [language](https://github.com/finos/rune-dsl) (i.e. syntax, semantics and
rules) and [code generators](https://github.com/REGnosys/rosetta-code-generators).

The language includes one default code generator into [java](https://www.oracle.com/java/). To
facilitate adoption and implementation of the CDM by the community of
industry participants, the Rune DSL is available in open source under
an Apache 2.0 license. This allows industry participants to write and
share code generators into any other languages.

---
**Note:**
All the language components, their syntax and purpose are detailed in
the [Rune DSL Documentation](https://docs.rosetta-technology.io/rosetta/rune-dsl/rune-modelling-component/). The documentation also describes the
mechanism to write and use code generators.

---


## CDM Applications

An ecosystem of CDM-based application components from software providers
exists in order to support the adoption of CDM and the implementation of
CDM-based production systems by industry participants. These
applications may be open source software or licensed under commercial
terms. In particular:

-   Rosetta is a Software Development Kit (SDK or *dev-kit*) for the
    Rune DSL, that provides the community with a free and easy way to
    contribute code to the CDM. Please refer to the [Rosetta
    Documentation](https://docs.rosetta-technology.io/rosetta/rosetta-products/) for more details.

---
**Note:**
Rosetta has been developed by technology firm
[REGnosys](https://regnosys.com). FINOS encourages the adoption of CDM by software providers
but does not endorse any CDM application component.

---

# Using the CDM (Java)

The Java distribution of the CDM is designed to be built and used with
Maven.

It depends on some open source java artifacts which are freely available
from an artifact repository. Maven can be configured to use this
repository using the repository settings contained in the `settings.xml`
file in the CDM jar.

For more details, please follow the
[CDM Java Distribution Guidelines](/docs/cdm-java-distribution).
