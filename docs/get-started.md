# Get Started
This page will take you through how to contribute to the CDM, the necessary steps to take beforehand, editing and testing. 

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

---

## Editing the model

When editing the CDM, please go through the following modelling
checklist:

-   CDM version: use the latest available [development
    version](versioning.md)
-   Syntax: no syntax warnings or errors
-   Compilation: model compiles ok with no *static compilation* errors
-   Testing: all translate regression tests expectations for mapping,
    validation and qualification maintained or improved. Additional test
    samples may be needed if use-case is not covered by existing
    samples.
-   Namespace: all model components positioned in the correct namespace
-   Descriptions: all model components have descriptions

The following sections detail that checklist. When using the Rosetta
Design web application to edit the model, the Contributor should also
refer to the [Rosetta Design Guide](https://docs.rosetta-technology.io/rosetta/rosetta-products/0-welcome-to-rosetta).

### CDM version

To the extent possible it is recommended that the Contributor keeps
working with a version of the CDM that is as close as possible to the
latest to minimise the risk of backward compatibility.

Please refer to the [Source Control Integration Guide](https://docs.rosetta-technology.io/rosetta/rosetta-products/1-workspace/#source-control-integration) for more
information.

### Syntax

The model is represented in the Rosetta DSL syntax. All syntax warnings
and errors must be resolved to have a valid model before contributing
any changes. For further guidance about features of the syntax, please
refer to the [Rosetta DSL Documentation](https://docs.rosetta-technology.io/rosetta/rosetta-dsl/rosetta-modelling-component).

In Rosetta Design, that syntax is automatically checked live as the user
edits the model, as described in the [Rosetta Design Content Assist
Guide](https://docs.rosetta-technology.io/rosetta/rosetta-products/2-rosetta-design/#rosetta-design-content-assist).

### Compilation

Normally, once the model is syntactically correctly edited, valid code
is being auto-generated and compiled. However, certain model changes can
cause compilation errors when changes conflict with static code (e.g.
certain mapper implementations).

The Rosetta support team can help resolve these errors before the
changes are contributed. In most cases you will be able to contact the
team via the [In-App chat](https://docs.rosetta-technology.io/rosetta/rosetta-products/0-welcome-to-rosetta#in-app-chat). If the support team identifies that
significant work may be required to resolve these errors, they will
notify the Contributor who should then contact the CDM Maintainer
originally appointed for the proposed change and/or CDM Owners. The
latter will be able to assist in the resolution of the issues.

For more information about auto-compilation using the Rosetta DSL,
please refer to the [Rosetta Auto Compilation Guide](https://docs.rosetta-technology.io/rosetta/rosetta-products/2-rosetta-design/#auto-completion-scoping).

### Testing

The CDM has adopted a test-driven development approach that maps model
components to existing sample data (e.g., FpML documents or other
existing standards). Mappings are specified in the CDM using `synonym`
which are collected into a Translation Dictionary, and the sample data
are collected into a Test Pack. Each new model version is
regression-tested using those mappings to translate the sample data in
the Test Pack and then comparing against the expected number of mapped
data points, validation and qualification results.

When using Rosetta to edit the model, contributors are invited to test
their model changes live against the Test Pack using the Rosetta
Translate application, referring to the [Rosetta Translate Guide](https://docs.rosetta-technology.io/rosetta/rosetta-products/3-rosetta-translate/).
When editing existing model components, the corresponding synonyms
should be updated to maintain or improve existing mapping levels. When
adding new model components, new sample data and corresponding synonym
mappings should also be provided so the new use-case can be added to the
set of regression tests.

Please refer to the [Mapping Guide](https://docs.rosetta-technology.io/rosetta/rosetta-dsl/rosetta-modelling-component#mapping-component) for details about the synonym
mapping syntax.

### Namespace

All model components should be positioned appropriately in the existing
namespace hierarchy. If the proposed contribution includes changes to
the namespace hierarchy, those changes should be justified and
documented. Any new namespace should have an associated description, and
be imported where required.

Please refer to the [namespace-documentation](namespace.md) section for more details.

### Descriptions

All model components (e.g. types, attributes, conditions, functions
etc.) should be specified with descriptions in accordance with the [CDM
Documentation Style Guide](#documentation-style-guide).

## Contributing model changes

### Contribution checklist

Before you start contributing your model changes, please go through the
following contribution checklist:

-   Specify a meaningful title and description for the contribution
-   Notify the CDM Maintainers (via email or Slack) of the submitted
    contribution
-   Include:
    -   Any notes on expected mapping, validation or qualification
        changes (success numbers should not decrease)
    -   Additional data samples, if necessary
    -   Documentation adjustment, if necessary
    -   Release notes
    -   Any other additional materials or documentation that may help
        with the review and approval process

---
**Note:**
A contribution should be a whole [releasable
unit](#what-is-a-releasable-unit) and its size calibrated in accordance
with the CDM's [agile development
approach](development-approach.md).

---

### Contributing

Changes are contributed by submitting a Pull Request for review into the
CDM source-control repository. This pull request will invoke a build
process to compile and run all CDM unit tests and regression tests.

Given the alignment:

> 1 pull request = 1 contribution = 1 releasable unit = 1 user story,

we recommend labelling the pull request with the user story label, i.e.
"STORY-XYZ: ..." to facilitate its tracking.

![](/img/operating-model-2.png)

![](/img/operating-model-3.png)

---
**Note:**
All contributions are submitted as candidate changes to be incorporated
under [the CDM licence](https://portal.cdm.rosetta-technology.io/#/terms-isda).

---

When using Rosetta to contribute model changes, the contribution
interface allows to specify a title and description for the
contribution. Those inputs are used to create a Pull Request on a
one-off branch in the source-control repository. Please refer to the
[Rosetta Workspace Contribution Guide](https://docs.rosetta-technology.io/rosetta/rosetta-products/1-workspace/#contribute-workspace-changes) for more information.

---
**Note:**
It is not yet possible to contribute updated test expectations,
documentation, release notes or new sample data using Rosetta, so these
must be provided to the CDM Maintainers via Slack or email.

---

### Documentation

The CDM documentation must be kept up-to-date with the model in
production. Where applicable, the Contributor should provide
accompanying documentation (in text format) that can be added to the CDM
documentation for their proposed changes.

The documentation includes code snippets that directly illustrate
explanations about certain model components, and those snippets are
validated against the actual model definitions. When a model change
impacts those snippets, or if new relevant snippets should be added to
support the documentation, those snippets should be provided together
with the documentation update.

### Release note

A release note should be provided with the proposed model change that
concisely describes the high-level conceptual design, model changes and
how to review. Please refer to the
[content-of-release-notes](#content-of-release-notes) for further
guidance on editing release notes.

## Reviewing model changes

### Review checklist

Before starting to review a contribution, the CDM Maintainer should go
through the following review checklist:

-   Review Pull Request to assert that:
    -   Model changes fulfil the proposed design and use-case
        requirements
    -   Synonyms have been updated and output (JSON) looks correct
    -   Contributed model version is not stale and does not conflict
        with any recent changes
    -   Changes are in accordance with the CDM governance guidelines

---
**Note:**
It is not yet possible to verify that mapping, validation and
qualification expectations have been maintained by looking at the output
of the Pull Request and CDM build only. Please refer to the
[downstream-dependencies](#downstream-dependencies) section for more
details.

---

-   CDM build process completed with no errors or test failures
-   Review additional samples provided (if use-case is not covered by
    existing samples)
-   All model components positioned in the correct namespace
-   All model components have descriptions
-   Additional documentation provided, if necessary.
-   Release note provided

Any review feedback should be sent to the Contributor as required via
Slack, email or in direct meetings.

---
**Note:**
Depending on the size, complexity or impact of a contribution, the CDM
Maintainer can recommend for the contribution to be presented with an
appropriate level of details with the CDM Architecture and Review
Committee for further feedback. The CDM Maintainer will work with the
Contributor to orchestrate that additional step. The additional feedback
may recommend revisions to the proposed changes. When it is the case the
review process will iterate on the revised proposal.

---

## Model maintenance

Before the Pull Request can be merged into the CDM's main branch, some
work is usually required by the Maintainer to preserve the integrity of
the model source code and of its downstream dependencies.

### Post-review technical tasks

A number of technical tasks may need to be performed on the Pull Request
once it is approved:

-   **Stale CDM version**: Contribution is based on an old CDM version
    and model changes conflict with more recent changes. If the
    conflicting change is available in Rosetta, the contributor should
    be asked to update their contribution to the latest version and
    resubmit. If the conflicting change is not yet available in Rosetta,
    this merge will need to be handled by the CDM Maintainer.
-   **Failed unit tests**: Java unit tests in the CDM project may fail
    due to problems in the contributed changes. Alternatively it may be
    that the test expectations need to be updated. The Maintainer should
    determine the cause of the test failure and notify either the
    Contributor or work on adjusting the test expectations.
-   **Additional documentation**: If the contributor provided additional
    documentation, the Maintainer should update the CDM documentation by
    editing the *documentation.rst* file in GitHub.
-   **Documentation code snippets**: To avoid stale documentation, the
    CDM build process verifies that any code snippets in the
    documentation exists and is in line with the model itself. The
    Maintainer should adjust or include any code snippets by editing the
    *documentation.rst* file on GitHub.
-   **Code generation**: Model changes may cause code generator failures
    (e.g., Java, C#, Scala, Kotlin etc.). In the unlikely event of code
    generation failures, these will need to be addressed by the
    Maintainer.

### Downstream dependencies

The CDM has a number of dependent projects that are required for the
model to be successfully distributed. It is possible that model changes
may cause these downstream projects to fail. The Maintainer will need to
test and, if necessary, update those before the changes can be released.

-   **Translate**: The regression tests in this project compare the
    contributed model against the expected number of mapping, validation
    and qualification results. Due to the contributed model changes, it
    is likely that there will be expectation mismatches that cause this
    build to fail.
-   **CDM Homepage**: compile and test.
-   **CDM Java Examples**: compile and test.

---
**Note:**
In most cases, the post-review technical tasks and downstream
dependencies require software engineering expertise in addition to CDM
expertise. Additional technical support from the CDM Maintainer team may
need to be called upon to address those.

---

The change can be merged into the main CDM code base only upon:

-   approval by CDM Maintainers and/or CDM Architecture and Review
    Committee,
-   successful completion of all the above technical tasks, and
-   successful builds of the CDM and all its downstream dependencies.

## Releasing model changes

Once the contributed model change has been merged, a new release can be
built, tested and deployed. The Maintainer will work with the CDM Owners
and the Contributor on a deployment timeline.

The following release checklist should be verified before deploying a
new model:

-   Update the CDM version number, using the [semantic
    versioning](#semantic-versioning) format
-   Build release candidate, and test
-   Build documentation website release candidate, and test
-   Deploy release candidate and notify channels if need be
-   (Currently done at a later stage) Update the latest CDM version
    available in Rosetta

![](/img/CDM–Build-Release-Process.png)

---
**Note:**
When the release process is handled through Rosetta Deploy, the
Maintainer should contact the Rosetta support team to request that
deployment and discuss a timeline for the release.

---

## Documentation Style Guide 

The purpose of this section is to provide form and substance
recommendations for editors of CDM documentation. "Documentation" in
this context means any form of written guidance to CDM users and
includes:

-   the CDM user documentation
-   release notes provided with each CDM release
-   any description developed in the logical model itself, which
    includes:
    -   data types and attributes
    -   enumerations
    -   functions and rules
    -   test descriptions
    -   any other areas of the logical model where a textual description
        may be provided

The intended audience for the CDM documentation includes software
developers, data modelers, legal experts, business experts, and other
subject matter experts who may have expertise in one area related to
financial products, but are not experts in every area.

The baseline for the recommendation is standards for spelling,
punctuation, and other style rules applicable to British English. The
recommendation in this document extends this rule set with a set of
guidelines applicable to documentation for logical models. The
recommendation does not provide an exhaustive list of all of the
standard British English rules, but provides selective examples that are
common to documentation in general.

Writing and editing style is subjective and a matter of personal
preferences, rather than right or wrong. The following guidelines are
intended to ensure that the documentation provides consistent styling,
regardless of who writes it, but should not be interpreted as an
authoritative source on "good" styling.

Unless otherwise noted, the recommended rules apply to all forms of CDM
documentation. When rules are applicable only to certain forms of
documentation (for instance, the logical model descriptions or the
release notes), they will be specified as such.

## Terminology

### The CDM

The model should be referred to as *the CDM*, without any ownership
mention. The only exception is the [Overview of the FINOS
CDM](cdm-overview.md) section at the beginning of the user
documentation when the CDM is introduced as *the FINOS CDM*.

### General data definition components

The two data definition components should be referred to as follows:

1.  *Data type*: defines an entity with a description, attributes, and
    where applicable, conditions.
    1.  Not *type*.
    2.  Not *class*: although these are appropriate terms for some of
        the distributions of the CDM into other languages, they are not
        applicable for all.
2.  *Attribute*: defines a member of a data type.
    1.  Not *field*.
    2.  Not *element*.

### Product

1.  *Financial Product*. The user documentation defines
    [financial-product](/docs/product-model#financial-product) as the highest
    level of the hierarchy of the universe of products. The term
    *Financial Product* should be used consistently throughout the
    documentation and wherever applicable in the model descriptions when
    describing the broad set of products.
2.  *Contractual Product*. Similarly,
    [contractual-product](/docs/product-model#contractual-product) is defined as a
    subset of Financial Products and should be used consistently in any
    documentation.
    1.  Not *Derivatives*.
    2.  Not *OTC*. Contractual Product is at a higher hierarchy level
        than OTC Derivatives because it can include other types of
        products such as Security Financing.

### Event

1.  *Business Event*. In the CDM, a [business-event](/docs/event-model#business-event) represents an event that may occur during the lifecycle
    of a trade, such as an amendment, a termination, a reset or a
    payment.
    1.  Not *Lifecycle Event*. the term *Business Event* should be
        consistently used when referring to these data structures in the
        CDM documentation.
2.  *Primitive Event*. In the CDM, a [primitive-event](/docs/event-model#primitive-event) represents a building block component used to specify
    business events in the CDM.
    1.  Not *Primitive* (stand-alone). In the CDM documentation, the
        word *Primitive* always needs to be qualified with *Event*,
        because the word *Primitive* may be associated to very different
        meanings, e.g. in computing.

## Completeness

### User Documentation

1.  The user documentation should provide an applicable introduction and
    should have a section for every primary component of the CDM.
2.  Each section should provide enough business context and explanations
    of the model so that the average reader in the target audience
    understands the purpose of the component and its role in the model.
3.  Each section should have at least one example of a data structure

### Logical Model

1.  Every data type, attribute, enumerated value, function, and test
    should have a description that describes its purpose in the context
    of the CDM.

## Accuracy

1.  Descriptions should accurately describe the current state of the
    model. This seems obvious, but there are many possible ways for
    introducing misalignments, including: an anticipated change never
    occurred, or the author incorrectly interpreted the structure, or
    the data structure changed while the documentation or description
    was not updated.
2.  Subtasks in a design process should include an assessment of
    documentation and descriptions that will be required to be changed
    or created, and should include the content.
3.  A release checklist should verify that the affected documentation
    and descriptions are completed accordingly.

## Content Guidelines

### General guidelines

1.  Data Definition components (e.g. data types, attributes,
    enumerations and enumerated values) should be explained in business
    terms.
    1.  The description of objects in the model should begin with the
        purpose of the object. The purpose should explain what the
        object is, not "what it is not".
    2.  Data type description should begin with a verb that describes
        what the type does.
    3.  The logical model identifies data types without needing a
        description, therefore, the description should not begin with a
        phrase like: "A data type that does..."
    4.  Attribute description should articulate the use of the type in
        the context of the attribute.
    5.  The description should not be tautological, e.g. PartyRole
        \<defines the party role\> is not compliant with these
        guidelines.
    6.  References to a similar attribute in FpML should not be used as
        a crutch in place of explaining a data type, attribute, etc.
    7.  In most cases, where a reference to FpML is considered useful,
        it should be placed at the end of a description in the logical
        model, or in a note at the end of a section in the user
        documentation. In the case of the logical model, note that
        synonyms for FpML are provided, therefore it should not be
        necessary to reference FpML in every case.

Example of a non-compliant description:

``` sourcecode
<"A data type to represent a financial product. With respect to contractual products, this class specifies the pre-execution product characteristics (the ContractualProduct class). This class is used as underlying for the option exercise representation, which makes use of the contractualProduct attribute to support the swaption use case, with the exercise into a swap. In a complete workflow, the swaption contract itself then needs to be superseded by a swap contract underpinned by the exercised swap as a contractualProduct.">
```

Instead a compliant description would state:

``` sourcecode
<"Represents a financial product. With respect to a contractual products, this data type specifies the pre-execution product characteristics...”>
```

Another non-compliant example:

``` sourcecode
<"This class corresponds to the FpML CalculationAgent.model.">
```

2.  The description of data objects or the overall model should be
    focused on the current state, there should be no reference to the
    history of the model or a future state.
    1.  The history of the model is not relevant in this context. It is
        sufficient to describe how the model currently works.
    2.  Forward-looking statements can create a perception that the
        product is not finished, and become a distraction to explaining
        what the product does. Moreover, these future plans may never
        materialise.
    3.  Given these rules, phrases such as "the model currently
        does...." should be excluded because any documentation must be a
        description of what the model currently does.

### Heading styles and flow in the user documentation

1.  Heading styles. The user documentation is edited in the *RST
    (reStructured Text)* mark-up language, which is then rendered into
    Html in the CDM documentation website using *Sphinx*. For headings
    to be rendered with the correct structure, they should be annotated
    according to the following table:
    1.  If using a header to identify a section to describe a component,
        then use headers to describe other components that are at the
        same level.
    2.  Sub-headings should have a name distinct from the higher level
        heading. e.g. if Legal Agreement is the Heading Level 2, then
        there should not be a heading at level 3 or 4 with the exact
        same title.

| Heading Level |    Notation (underline in .rst)    | Relative font size (as seen by users) |                                          Section Example |
|:--------------|:----------------------------------:|--------------------------------------:|---------------------------------------------------------:|
| 1             |        _[===============]_         |                      XL font and bold |         [common-domain-model](/docs/common-domain-model) |
| 2             | _[\-\-\-\-\-\-\-\-\-\-\-\-\-\--]_  |                       L font and bold |               [product-model-page](/docs/product-model),<br/>[legal-agreements-page](/docs/legal-agreements) |
| 3             | _[\^\^\^\^\^\^\^\^\^\^\^\^\^\^\^]_ |                       M font and bold | [tradable-product](/docs/product-model#tradable-product) |
| 4             | _["""""""""""""""]_ |     S font (same ascontent), but bold |     [price-quantity](/docs/product-model#price-quantity) |
:Heading Styles

The RST editing syntax in which the user documentation is written is a
standard web mark-up language, for which a reference guide can be found
at: <https://sublime-and-sphinx-guide.readthedocs.io>

2.  Logical organization and order:
    1.  The user documentation should walk the user through the model
        from the top down, beginning at a description of the primary
        components.
    2.  Levels 1 and 2 should include a bullet point list of the sub
        sections that will be described (bullets formatted with the
        insertion of an asterisk followed by a space).
3.  Define business terms and CDM terminology early in a section so that
    the broader audience understands the model.
4.  Details about a topic should be presented in the section for that
    topic:
    1.  In the case of a model component, the description of the
        component should always be followed by an example.
    2.  Most or all of the explanation should occur before the example,
        not after, unless the example was needed as context for an
        explanation.
    3.  Transitions from one topic to another should be used to help
        guide the user through the model.

### Content of Release Notes

Release notes are text describing the content of any new CDM release and
are a critical component of the distribution of that release. Release
notes are edited in the *Mark-Down (MD)* syntax, which is then rendered
into Html in the various channels where the release is published.

1.  Release notes should begin with a high-level headline of the part of
    the model being changed, followed by "--" and a short headline
    description
    1.  For example: "# Legal Agreement Model - Collateral Agreement
        Elections"
2.  They should provide enough detail for a reviewer or other interested
    parties to be able to find and evaluate the change. For a data model
    change, for example, the data type and attributes should be named
    and the before/after states of the model explained, along with a
    justification in which the issue is summarised.
3.  They should also embed a link to the Pull Request containing the change, to enable users to inspect those changes in details.
4.  If the release notes describe mapping rules, there should be
    explicit information about the examples affected and the change in
    resulting values for those examples.
5.  If the release is documentation, it should specify exactly where the
    document was changed and why.
6.  Special formatting rules related to use of the MD mark-up language:
    1.  Headline should begin with a `#`, as in the above example, so
        that it appears correctly formatted in Html
    2.  `*` before and after text (no space) for bold
    3.  `_` before and after text (no space) for italics
    4.  `–` (plus a space) for bullets
    5.  Backticks ``\` before and after model components, e.g. data
        types, attributes, enums, function names, etc. for special
        code-style formatting

Example release notes formatted in MD:

``` MD
# *CDM Model: Expanded set of enumerations in RegulatoryRegimeEnum*

_What is being released_

Additional regimes have been added to the `RegulatoryRegimeEnum` which is used to express the required regimes for initial margin documentation. The `RegulatoryRegimeEnum` is used as an enumeration for attributes in the `ApplicableRegime` and `SubstitutedRegime` data types within the legal agreements model.

The new enumerated values are `BrazilMarginRules`, `UnitedKingdomMarginRules`, `SouthAfricaMarginRules`, `SouthKoreaMarginRules`, and `HongKongSFCMarginRules`, all of which have come into force in January 2021.  Each of these enumerated values has a complete description that uses the text provided in the relevant regulatory supplement.

_Review directions_

In the CDM Portal select the Textual Browser, search for ‘ApplicableRegime’ and ‘SubstitutedRegime’, click on the ‘RegulatoryRegimeEnum’ next to the ‘regime’ attribute and observe the expanded list of regimes, including the ones noted above.

Inspect Pull Request: [#1101](https://github.com/finos/common-domain-model/pull/1101)

```

The MD editing syntax in which release notes are written is a standard
web mark-up language, for which a reference guide can be found at:
<https://www.markdownguide.org/cheat-sheet/>

---
**Note:**
The MD syntax provides similar features to the RST syntax (used to edit
the user documentation), but the special formatting characters are
slightly different between the two. While RST allows richer features
that are useful for a full documentation website, MD is preferred for
release notes because Slack supports (a subset of) the MD language and
can therefore serve as a release publication channel.

---

## Style

### Content style

1.  Content should be correct with regard to grammar, punctuation, and
    spelling (in British English), including but not limited to the
    following rules:
    1.  Grammatical agreement, e.g. data types need, not data types
        needs
    2.  Punctuation:
        1.  etc. requires a period.
        2.  Complete sentences should end with a period or colon (there
            should be no need for a question mark or exclamation point
            in these artefacts).
        3.  Incomplete sentences cannot end with a punctuation. For
            example, "Through the `legalAgreement` attribute the CDM
            provides support for implementors to:" is an incomplete
            sentence and cannot end in a punctuation. This can be fixed
            by adding a few words, e .g. "Through the `legalAgreement`
            attribute the CDM provides support for implementors to do
            the following:"
        4.  Always use the Oxford Comma (aka the Serial Comma) for
            clarity when listing more than two items in a sentence, e.g.
            "data types, attributes, and enumerated values." In extreme
            cases, failure to use this comma could be costly.
2.  Other grammatical rules
    1.  Agreement of numbers: For example, if one sentence reads "the
        following initiatives..." , then it should be followed by more
        than one.
    2.  Sentences should not end with a preposition
        1.  Non-compliant example: "..to represent the party that the
            election terms are being defined for."
        2.  Compliant: "...to represent the party or parties for which
            the election terms are being defined."
3.  When a name or phrase is defined - continue to use it unless an
    alias has been defined. For example, one section reviewed had an
    expression "agreement specification details" but then switched to
    using "agreement content" without explanation. There is sufficient
    terminology to absorb, as such there is no need for synonyms or
    aliases, unless there are commonly used terms, in which case, they
    should be defined and one term should be used consistently.
4.  User Documentation and descriptions should always be in the third
    person, for example: "the CDM model provides the following...".
    Never use the first person (including the use of "we").
5.  In the user documentation, when there is a need for a long list, use
    bullets (`*` or `-` followed by space, then text) as opposed to long
    sentences.
6.  To the extent possible, use simple direct sentence structures, e.g.
    replace "An example of such" with "For example", or replace
    "Proposals for amendment to the CDM can be created upon the
    initiative of members of a Committee or by any users of CDM within
    the community who are not a current Committee member." with
    "Committee members or any user of CDM within the community can
    propose amendments to the CDM."
7.  Exclude the usage of "mean to", "intends to", or "looks to".
    1.  For example, "the model looks to use strong data type
        attributes such as numbers, boolean or enumerations whenever
        possible."
    2.  Either the object works as designed or it does not. This
        expression might be used in a bug report when describing a
        function not working as intended but not to describe a
        production data model.
8.  Explain the CDM objects in an honest and transparent manner, but
    without criticism of the model. Sentences such as: "...which firms
    may deem inappropriate and may replace by..." or "the model is
    incomplete with regards to..." are unnecessary in a documentation.
    Rather, issues which may be identified in the CDM should be raised
    and addressed via the CDM governance structure.

### Special format for CDM objects

1.  Data types and attributes display rules:
    1.  Data types and attributes should be identified in the editor
        with code quotes, where the text between the quotes will appear
        in a special block format as illustrated here:
        `LegalAgreementBase`.
    2.  If the same word or phrase is used in a business context, as
        part of an explanation, then the words should be spaced and
        titled normally and the special format is not required: e.g.
        "Tradable products are represented by...".
2.  Code snippets should be preceded by the string:
    `.. code-block:: Language` (where the Language could be any of
    Haskell, Java, JSON, etc.), followed by a line spacing before the
    snippet itself. The entire snippet should be indented with one
    space, to be identified as part of the code block and formatted
    appropriately. Indentation can be produced inside the snippet itself
    using further double space. Meta-data such as data type descriptions
    or synonyms that appear in the CDM should be excluded from the code
    snippet, unless the purpose of the snippet is to illustrate those.

Example of how a code snippet should be edited in the documentation:

``` MD
.. code-block:: Haskell

 type Party:
   [metadata key]
   partyId PartyIdentifier (1..*)
   name string (0..1)
     [metadata scheme]
   businessUnit BusinessUnit (0..*)
   person NaturalPerson (0..*)
   personRole NaturalPersonRole (0..*)
   account Account (0..1)
   contactInformation ContactInformation (0..1)
```

And the result will be rendered as:

``` Haskell
type Party:
  [metadata key]
  partyId PartyIdentifier (1..*)
  name string (0..1)
    [metadata scheme]
  businessUnit BusinessUnit (0..*)
  person NaturalPerson (0..*)
  personRole NaturalPersonRole (0..*)
  account Account (0..1)
  contactInformation ContactInformation (0..1)
```

---
**Note:**
Code snippets that appear in the user documentation are being compared
against actual CDM components during the CDM build process, and any
mismatch will trigger an error in the build. This mechanism ensures that
the user documentation is kept in sync with the model in production
prior to any release.

---

### Fonts, Text Styles, and Spaces

1.  Bold should be used sparingly:
    1.  Only in the beginning of a section when there is a salient point
        to emphasize, like a tag line - the bold line should be
        syntactically complete and correct.
    2.  In the editor, bold is specified with double asterisks before
        and after the word or phrase.
2.  Italics
    1.  Italics should be used when defining an unusual term for the
        first time rather than using quotes, for example to identify
        something CDM specific, such as the concept of Primitive Events.
    2.  In the editor, italics is specified with a single asterisk `*`
        before and after the word or phrase.
3.  Single space should be used in-between sentences, not double space.

### Style references for additional guidance

1.  [New Hart's Rules](https://global.oup.com/academic/product/new-harts-rules-9780199570027): An updated version of this erstwhile
    comprehensive style guide for writers and editors using British
    English, published by the Oxford University Press. Invaluable as an
    official reference on proofreading and copy-editing. Subjects
    include spelling, hyphenation, punctuation, capitalisation,
    languages, law, science, lists, and tables. An earlier version
    coined the phrase Oxford Comma in July 1905.
2.  [Eats, Shoots & Leaves: The Zero Tolerance Approach to
    Punctuation](https://www.lynnetruss.com/books/eats-shoots-leaves/):
    A light-hearted book with a serious purpose regarding common
    problems and correctness for using punctuation in the English
    language.