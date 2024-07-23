---
title: Overview of the FINOS CDM
---

**Continuous Integration:** 

[![FINOS - Incubating](https://cdn.jsdelivr.net/gh/finos/contrib-toolbox@master/images/badge-incubating.svg)](https://community.finos.org/docs/governance/Software-Projects/stages/incubating)

[![Codefresh build status]( https://g.codefresh.io/api/badges/pipeline/regnosysops/FINOS%2Fcommon-domain-model?type=cf-1)]( https://g.codefresh.io/public/accounts/regnosysops/pipelines/new/63ecb79bde06416b39d81e70)

# What is the FINOS CDM

The FINOS Common Domain Model (CDM) is a standardised, machine-readable
and machine-executable blueprint for how financial products are traded
and managed across the transaction lifecycle. It is represented as a
[domain model](https://olegchursin.medium.com/a-brief-introduction-to-domain-modeling-862a30b38353) and distributed in open source.

For an overview of the Common Domain Model, a comparative analysis with FpML, an exploration of the CDM's historical development and events, as well as insights into its further applications, including its role in the ecosystem and support for smart contract technology, we recommend reviewing the [CDM Overview deck](/pdfs/CDM-Overview.pdf). 

ISDA’s Digital Regulatory Reporting (DRR) solution leverages the open-source Common Domain Model (CDM) to convert industry-agreed interpretations of new or amended regulatory reporting rules into clear, machine-executable code. This approach enhances implementation efficiency and reduces costs. Institutions contributing to the development of the ISDA DRR include (but are not limited to) those listed on [ISDA’s Digital Regulatory Reporting Page](https://www.isda.org/isda-digital-regulatory-reporting/).

ISDA has also launched the Get Started with the CDM for Collateral Guide! You can now access a wealth of resources and tutorials on CDM for collateral management on the [CDM-Collateral Initiatives page](https://www.isda.org/2023/02/16/isda-collateral-initiatives/).

If you’re interested in the Common Domain Model (CDM), please complete the [CDM Interest Form](https://www.finos.org/common-domain-model) to join our mailing list. By signing up, you’ll receive updates on CDM initiatives, meeting agendas, and other working group activities.

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
at <cdm@lists.finos.org>.

## Design Principles

To support these objectives, the CDM is built according to a set of
design principles that include the following concepts:

-   **Normalisation** through abstraction of common components
-   **Composability** where objects are composed and qualified from the
    bottom up
-   **Mapping** to existing industry messaging formats
-   **Embedded logic** to represent industry processes
-   **Modularisation** into logical layers

These design principles are further detailed in the
[design-principles](#design-principles) section of the CDM
documentation.

## Governance

The CDM [governance framework](contribution#governance) regulates
the development of the CDM in open source.

## Scope

The CDM covers OTC derivatives, cash securities, securities financing, commodities, and can expand to include other Capital Markets products and Asset Classes.

## CDM 2024 Roadmap
Below is the full roadmap as ratified by the Steering WG in Jan 2024, subject to change as priorities evolve.
![](/img/cdm-roadmap-2024-2.png)
![](/img/cdm-roadmap-2024-1.png)

- The latest CDM roadmap of expected contributions sponsored by the presently participating trade associations: ICMA, ISDA and ISLA can be found under [Roadmap.md](https://github.com/finos/common-domain-model/blob/master/ROADMAP.md) . 
- Releases can be tracked on the [CDM Releases](https://github.com/finos/common-domain-model/releases/tag/5.0.0-dev.7) page.

# CDM Components

**The CDM is made of three sets of components**, as laid-out in the FINOS
CDM components diagram below:

-   The FINOS CDM Distribution (in *blue*)
-   The Rosetta DSL (in *grey*)
-   CDM Applications (in *green*)

![](/img/cdm-components-diagram.png)

## FINOS CDM Distribution

The FINOS CDM distribution is openly accessible to all industry
participants, subject to the FINOS CDM open source licence. This
distribution is fully downloadable.

The FINOS CDM distribution comprises three main sets of components:

-   **Model definition**, which corresponds to the model as expressed in
    the Rosetta DSL and contained into a set of *.rosetta* files
    organised as *namespaces*. The primary dimensions of the model are
    listed below and further described in the
    [common-domain-model](/docs/common-domain-model) of the
    documentation.
    -   Product
    -   Event
    -   Legal Agreement
    -   Process
    -   Reference Data
    -   Mapping (Synonym)
-   **Executable code distribution**, automatically generated from the
    model definitions expressed in the Rosetta DSL using [available code
    generators](https://docs.rosetta-technology.io/rosetta/rosetta-dsl/rosetta-code-generators/#what-code-generators-are-available). Once a code generator is implemented for a particular
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

## Rosetta DSL

The CDM is written in a Domain-Specific Language (DSL) called the
*Rosetta DSL*, that comprises a [language](https://github.com/REGnosys/rosetta-dsl) (i.e. syntax, semantics and
rules) and [code generators](https://github.com/REGnosys/rosetta-code-generators).

The language includes one default code generator into [java](https://www.oracle.com/java/). To
facilitate adoption and implementation of the CDM by the community of
industry participants, the Rosetta DSL is available in open source under
an Apache 2.0 license. This allows industry participants to write and
share code generators into any other languages.

---
**Note:**
All the language components, their syntax and purpose are detailed in
the [Rosetta DSL Documentation](https://docs.rosetta-technology.io/rosetta/rosetta-dsl/rosetta-modelling-component). The documentation also describes the
mechanism to write and use code generators.

---


## CDM Applications

An ecosystem of CDM-based application components from software providers
exists in order to support the adoption of CDM and the implementation of
CDM-based production systems by industry participants. These
applications may be open source software or licensed under commercial
terms. In particular:

-   Rosetta is a Software Development Kit (SDK or *dev-kit*) for the
    Rosetta DSL, that provides the cumminity with a free and easy way to
    contribute code to the CDM. Please refer to the [Rosetta
    Documentation](https://docs.rosetta-technology.io/rosetta/rosetta-products/) for more details.

---
**Note:**
The CDM Portal and Rosetta have been developed by technology firm
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
[CDM Java Distribution Guidelines](/docs/cdm-guidelines).
