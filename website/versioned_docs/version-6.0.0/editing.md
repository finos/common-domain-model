---
title: Editing the Model
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
Documentation Style Guide](documentation-style-guide.md).

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
unit](development-approach.md#what-is-a-releasable-unit) and its size calibrated in accordance
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
through the [review checklist](maintenance-and-release.md).



---
**Note:**
It is not yet possible to verify that mapping, validation and
qualification expectations have been maintained by looking at the output
of the Pull Request and CDM build only. Please refer to the
[downstream dependencies](maintenance-and-release.md) section for more details.

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
