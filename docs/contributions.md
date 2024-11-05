# Contributions 
This page will take you through how to contribute to the CDM, the necessary steps to take beforehand, editing and testing. 

## CDM development guidelines 

The CDM Development Guidelines are defined by the Steering Working Group. The full set of CDM development guidelines can be found [here](https://cdm.docs.rosetta-technology.io/source/contribution.html).

## Version Management

The CDM is developed, built and released using standard software
source-control management. Each new released version is announced to
users via a *release note* that describes the change introduced by that
new version. The CDM release history is available in the [Release
Section](#) of the CDM documentation.

![](/img/CDM–Semantic-Versioning-Refresher.png)

Information on semantic versioning, backwards compatibility and version availability can be found [here.](versioning.md)


## Agile Development Approach

The on-going development of the CDM adheres to a methodology inspired by
the *Agile* software development framework. This methodology is based on
two high-level principles:

1.  Focus on business value from the user's perspective, encapsulated
    in the concept of *user story*
2.  Delivery of small, releasable changes that contribute to that
    business value (sometimes referred to as *shippable increments*) -
    i.e. no "big bang" changes


 To learn more, visit our [development approach](development-approach.md) page. 

# How to Contribute

The purpose of this section is to provide guidance for submitting,
reviewing and releasing changes to the CDM contributed by the wider
industry community including market participants, trade associations and
technology or service vendors. It describes:

-   What a Contributor should do to edit and contribute changes to the
    CDM
-   What a Maintainer should do to review the changes
-   How to release a new CDM version once changes have been approved

# Modelling Platforms
Development of the CDM is supported through various modelling platforms,
including [Rosetta](https://rosetta-technology.io) and [Legend](https://legend.finos.org/studio). Regardless of the modelling
platform used, modelling and contribution to the CDM should go through
the contribution check-list below.

---

The steps required to change the CDM are aligned with the software
development lifecycle typically applicable to the development of any
other software. This development lifecycle is illustrated in the diagram
below. Each step is associated to the relevant component of the Rosetta
platform that can be used to support the development of the CDM.

![](/img/SDLC.png)

---
**Note:**
This documentation is not an endorsement of any modelling platform and
associated products and CDM users remain invited to leverage the tools
of their choosing. This contribution guide has been contextualised with
references to Rosetta to ease understanding and align with the current
process.

---

## Before you start modelling

Before you start modelling, please make sure you have gone through the
following pre-modelling checklist:

-   Review the [design principles](design-principles.md) and
    [governance](/docs/cdm-overview#governance)
-   Review the [Rosetta Starter Guide](https://docs.rosetta-technology.io/rosetta/rosetta-products/0-welcome-to-rosetta) or equivalent in your chosen
    modelling platform.

In addition, for large model changes or changes to core data types, it
is recommended that the Contributor reviews the
[agile-development-approach](development-approach.md) and follows
these steps:

-   **Define use case**. Identify and document one or more use cases
    with details (e.g. a sample trade).
-   **Draft conceptual design** (high level). Draft a conceptual view
    showing the set of data types, their definitions (and/or sample
    attributes but not the whole set of attributes), their relationships
    to each other, and, if applicable, a workflow.
-   **Design approval**. Obtain approval of high-level conceptual design
    from CDM stakeholders:
    -   CDM Owners (FINOS and other involved Trade Associations, where
        applicable)
    -   CDM Sub-Working Group, if applicable
    -   CDM Architecture and Review Committee
-   **Quality assurance**. Seek the early appointment of at least one
    CDM Maintainer who can assist modelling discussions and provide
    early feedback. CDM Maintainers are appointed by the CDM Owners as
    and when relevant. Please contact
    [cdm@lists.finos.org](mailto:cdm@lists.finos.org).

---
**Note:**
Unless explicitly instructed by a CDM Maintainer, a Contributor can only
ever develop changes to a development (i.e. pre-release) version of the
CDM.


