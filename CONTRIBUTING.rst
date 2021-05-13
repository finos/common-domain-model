How to Contribute
=================

The purpose of this section is to provide guidance for accepting contributions into the CDM by the wider industry community including market participants, trade associations and technology or service vendors. It describes:

- What a Contributor should do to edit and contribute to the CDM
- What a Reviewer should do to review the changes
- How to release a new CDM version once changes have been approved

The CDM is an open source project and any contribution to its on-going development is governed by the `CDM Governance Principles <https://docs.rosetta-technology.io/cdm/readme.html#the-cdm-governance>`_

Before you start modelling
--------------------------

Before you start modelling, please make sure you have gone through the following pre-modelling checklist:

- Review the `CDM Components and Design Principles <https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#>`_
- Review the `Rosetta Starter Guide <https://docs.rosetta-technology.io/core/0-welcome-to-rosetta.html>`_
- Get approval of conceptual design from stakeholders (for large model change)

For large model changes, or changes to core data types, it is recommended that the Contributor reviews the CDM Governance Principles and follows these steps:

- **Define Use Case**: Identify and document one or more use cases with details (e.g., a sample trade).
- **Draft Conceptual Design** (High Level): Draft a conceptual view showing the set of data types, their definitions (and/or sample attributes but not the whole set of attributes), their relationships to each other, and, if applicable, a workflow.
- **Design approval**: Obtain approval of high-level conceptual design from CDM stakeholders

  - CDM Owners (ISDA and other involved Trade Associations, where applicable)
  - CDM Sub-Working Group, if applicable
  - CDM Architecture and Review Committee

Editing the model
-----------------

Modelling checklist
^^^^^^^^^^^^^^^^^^^

Before you start editing the CDM using Rosetta, please go through the following modelling checklist:

- Use latest available CDM version
- No syntax warnings or errors
- Model compiles (with no "static compilation" errors)
- All translate regression tests expectations for mapping, validation and qualification maintained or improved
- Additional test samples used (if use-case is not covered by existing samples)
- All model components have descriptions

Use latest available CDM version
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Once ready to start modelling, the Contributor can log into Rosetta and start using the Rosetta Design application to create a workspace and edit the model, referring to the `Rosetta Design Guide <https://docs.rosetta-technology.io/core/2-rosetta-design.html>`_.

No syntax warning or error
^^^^^^^^^^^^^^^^^^^^^^^^^^

The model is edited using the Rosetta DSL syntax. All syntax warnings and errors must be resolved to have a valid model before contributing any changes. The syntax is automatically checked live in Rosetta Design as the user edits the model, as described in the `Rosetta Design Content Assist Guide <https://docs.rosetta-technology.io/core/2-rosetta-design.html#rosetta-design-content-assist>`_.

For further guidance about features of the syntax, please refer to the `Rosetta DSL Documentation <https://docs.rosetta-technology.io/dsl/documentation.html>`_.

Model compilation
^^^^^^^^^^^^^^^^^

Normally, when the model is syntactically correctly edited, valid code is being auto-generated and compiled in Rosetta. However, certain model changes can cause compilation errors when changes conflict with static code (e.g. certain mapper implementations).

The Rosetta support team can help resolve these issues before the changes are contributed. If the Rosetta support identifies that significant work may be required to resolve these errors, they will notify the Contributor who should then contact the CDM Owners to discuss allocating resources to assist.

For more information about auto-compilation in Rosetta, please refer to the `Rosetta Auto Compilation Guide <https://docs.rosetta-technology.io/core/2-rosetta-design.html#auto-compilation>`_.

Testing
^^^^^^^

The CDM has adopted a test-driven development approach that maps model components to existing sample data (e.g., FpML documents or other existing standards).  Mappings are specified in the CDM using ``synonym`` which are collected into a Translation Dictionary, and the sample data are collected into a Test Pack. Each new model version is regression-tested using those mappings to translate the sample data in the Test Pack and then comparing against the expected number of mapped data points, validation and qualification results.

Contributors are invited to test their model changes live against the Test Pack using the Rosetta Translate application, referring to the `Rosetta Translate Guide <https://docs.rosetta-technology.io/core/3-rosetta-translate.html>`_. When editing existing model components, the corresponding synonyms should be updated to maintain or improve existing levels. When adding new model components, new sample data and corresponding synonym mappings should also be provided so the new use-case can be added to the set of regression tests.

Please refer to the `Mapping Guide <https://docs.rosetta-technology.io/dsl/documentation.html#mapping-component>`_ for details about the synonym mapping syntax.

All model components have descriptions
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

All model components (e.g. types, attributes, conditions, functions etc.) should be specified with descriptions in accordance with the `CDM Style Guide <https://docs.rosetta-technology.io/cdm/documentation/source/documentation-style-guide.html>`.

Contributing model changes
--------------------------

Contribution checklist
^^^^^^^^^^^^^^^^^^^^^^

Before you start contributing your model changes, please go through the following contribution checklist:

- Use Rosetta to contribute model changes to GitHub, specifying a meaningful title and description
- Notify the CDM Reviewers (via email or Slack) of the submitted contribution
- Include:

  - Any notes on expected mapping, validation or qualification changes (success numbers should not decrease)
  - Additional data samples, if necessary
  - Documentation adjustment, if necessary
  - Release notes
  - Any other additional materials or documentation that may help with the review and approval process

Contributing using Rosetta
^^^^^^^^^^^^^^^^^^^^^^^^^^

Once the model changes have been completed, use Rosetta to submit changes for review, referring to the `Rosetta Workspce Contribution Guide <https://docs.rosetta-technology.io/core/1-workspace.html#contribute-workspace-changes>`_ and specifying a meaningful title and description.

The CDM is hosted in GitHub. Any changes contributed through Rosetta are submitted as a "Pull Request" on a one-off CDM branch and will invoke a build process to compile and run all CDM unit tests and regression tests.

.. note:: It is not yet possible to use Rosetta to contribute updated test expectations, documentation, release notes or new sample data, so these must be provided to the CDM Reviewers via Slack or email.

Documentation
^^^^^^^^^^^^^

The CDM documentation must be kept up-to-date with the model in production. Where applicable, the Contributor should provide accompanying documentation (in text format) that can be added to the CDM documentation for their proposed changes.

The documentation includes *.rosetta* code snippets that directly illustrate explanations about certain model components, and those snippets are validated against the actual model definitions. When a model change impacts those snippets, or if new relevant snippets should be added to support the documentation, those snippets should be provided together with the documentation update.

Release note
^^^^^^^^^^^^

A release note should be provided with the proposed model change that concisely describes the high-level conceptual design, model changes and how to review. Please refer to the `Release Note Style Guide <https://docs.rosetta-technology.io/cdm/documentation/source/documentation-style-guide.html#content-of-release-notes>` for further guidance on editing release notes.

Reviewing model changes
-----------------------

Review checklist
^^^^^^^^^^^^^^^^

Before starting to review a contribution, the CDM Reviewer should go through the following review checklist:

- Review the GitHub Pull Request to assert that:

  - Model changes fulfil the proposed design and use-case requirements
  - Synonyms have been updated and output (JSON) looks correct
  - Contributed model version is not stale and does not conflict with any recent changes
  - Changes are in accordance with the CDM governance guidelines
  
.. note:: It is not yet possible to verify that mapping, validation and qualification expectations have been maintained by looking at the output of the GitHub Pull Request and CDM build only. Pleae refer to the downstream projects section for more details.

- CDM build process completed with no errors or test failures
- Review additional samples provided (if use-case is not covered by existing samples)
- All model components have descriptions
- Additional documentation provided, if necessary.
- Release note provided

Any review feedback should be sent to the contributor as required via Slack, email or in direct meetings.

Post-review technical tasks
^^^^^^^^^^^^^^^^^^^^^^^^^^^

Following the initial model review, a number of technical tasks may be required before the changes can be finally approved, merged and released:

- **Stale CDM version**: Contribution is based on an old CDM version and model changes conflict with more recent changes. If the conflicting change is available in Rosetta, the contributor should be asked to update their contribution to the latest version and resubmit. If the conflicting change is not yet available in Rosetta, this will need to be handled by REGnosys.
- **Failed unit tests**: Java unit tests in the CDM project may fail due to problems in the contributed changes. Alternatively it may be that the test expectations need to be updated. The Reviewer should determine the cause of the test failure and notify either the contributor or REGnosys.
- **Additional documentation**: If the contributor provided additional documentation, the Reviewer should update the CDM documentation by editing the *documentation.rst* file in GitHub.
- **Documentation code snippets**: To avoid stale documentation, the CDM build process verifies that any code snippets in the documentation exists and is in line with the model itself. The Reviewer should adjust or include any code snippets by editing the *documentation.rst* file on GitHub.
- **Code generation**: Model changes may cause code generator failures (e.g., Java, C#, Scala, Kotlin etc.). In the unlikely event of code generation failures, contact REGnosys.

Downstream dependencies
^^^^^^^^^^^^^^^^^^^^^^^

The CDM has a number of dependent projects that are required for the CDM to be successfully distributed. It is possible that model changes may cause these downstream projects to fail. The Reviewer will need to contact REGnosys to test and, if necessary, update those before the changes can be released.

- **Translate**: The regression tests in this project compare the contributed model against the expected number of mapping, validation and qualification results. Due to the contributed model changes, it is likely that there will be expectation mismatches that cause this build to fail.
- **CDM Portal**: compile and test.
- **CDM Java Examples**: compile and test.

Once all the above technical tasks have been completed and the CDM and all downstream builds are successful, then the change can be merged into the main CDM code base.

Releasing model changes
-----------------------

Once the contributed model change has been merged, a new release can be built, tested and deployed.

The following release checklist should be verified before deploying a new model:

- Update CDM version, which uses the semantic version format (see `CDM Versioning Documentation <https://docs.rosetta-technology.io/cdm/readme.html#versioning>`_)
- Build release candidate, and test
- Build documentation website release candidate, and test
- Deploy release candidate and notify channels if need be
- (Currently done at a later stage) Update the latest CDM version available in Rosetta

.. note:: The release process is now being handled by the **Rosetta Deploy** solution.
