---
title: Namespace
---

The CDM is partitioned into groups of namespaces. A namespace is an
abstract container created to hold a logical grouping of model
artefacts. The approach is designed to make it easier for users to
understand the model structure and adopt selected components. It also
aids the development cycle by insulating groups of components from
unrelated model changes that may occur. The partitioning is visible to
users in Rosetta Core by toggling the Namespace view in the left hand
panel, and in the generated code files.

# Model Artifacts

Model artifacts are organised into a directory hierarchy that is exposed
in the model editor.

# Organising Principles

Namespaces are organised into a hierarchy, with layers going from in to
out. The hierarchy contains an intrinsic inheritance structure where
each layer has access to ("imports") the layer outside, and is designed
to be usable without any of its inner layers. Layers can contain several
namespaces ("siblings"), which can also refer to each other.

Example -- the base namespace

![](/img/cdm-namespace.png)

In the example above the layers of the "base" namespace can be observed.
There are four layers to the namespace. The outer layer "base" contains
one file and three namespaces. The next layer contains three siblings,
"datetime", "math", and "staticdata". A third and fourth layer is
contained within the "staticdata" namespace.

# Hierarchy Structure

The namespace hierarchy in the CDM contains 7 components

-   Base -- contains basic concepts used across the model: date, time,
    maths, static data
-   Event -- contains business event concepts: primitive, contract
    state, and associated state transition function specifications
-   Legal Agreement -- contains generic documentation concepts: legal
    agreement, contract, and credit support specifications
-   Observable -- contains observable concepts: market data, holiday
    calendars, asset class specific specifications
-   Product -- contains generic product concepts: quantity, price,
    economic terms and payout, that are built using template features
-   Regulation -- contains regulation concepts: regulatory bodies,
    corpus, report definitions and field rules
-   Synonym -- contains model to model synonym mappings

Each of these higher-level namespaces is further divided into
lower-level namespaces. The independent components in each namespace are
organised according to their core purpose but can be referenced from
anywhere in the model to allow all the components to work together for a
complete modelling solution.

**When developing new CDM components, the positioning of those
components in the namespace hierarchy is critical** as part of the
design (or potentially the re-organising of the hierarchy following the
new development), to ensure the CDM remains well organised.