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

---

## Editing the model

Please refer to the [Editing](editing.md) section which covers editing the model in more detail.

---   
## Contributing
Changes are contributed by submitting a Pull Request for review into the
CDM source-control repository. This pull request will invoke a build
process to compile and run all CDM unit tests and regression tests.

Given the alignment:

> 1 pull request = 1 contribution = 1 releasable unit = 1 user story,

we recommend labelling the pull request with the user story label, i.e.
"STORY-XYZ: ..." to facilitate its tracking.

![](/img/operating-model-2.png)

![](/img/operating-model-3.png)

**Note:**
All contributions are submitted as candidate changes to be incorporated
under [the CDM licence](https://portal.cdm.rosetta-technology.io/#/terms-isda).

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

---
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

Please visit the [Documentation Style Guide page](documentation-style-guide.md) for reccomendations for editors of CDM documentation. 
