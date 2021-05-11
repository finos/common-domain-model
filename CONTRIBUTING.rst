How to Contribute
=================

This purpose of this page is to provide guidance to community contributors to the CDM.

Before you start modelling
--------------------------

Please make sure you have gone through your pre-modelling checklist:

- Review the CDM Components and Principles
- Review the `Rosetta Starter Guide <https://docs.rosetta-technology.io/core/0-welcome-to-rosetta.html>`_
- Get approval of conceptual design from stakeholders (for large model change)

For large model changes, or changes to core data types, it is recommended that the contributor reviews the CDM governance documentation and follows these steps:

- **Define Use Case**: Identify and document one or more use cases with details (e.g., a sample trade).
- **Draft Conceptual Design** (High Level): Draft a conceptual view showing the set of data types, their definitions (and/or sample attributes but not the whole set of attributes), their relationships to each other, and, if applicable, a workflow.
- **Design approval**: Obtain approval of high-level conceptual design from stakeholders

  - CDM Team (ISDA)
  - Sub-working group if applicable
  - ARC

Editing the model using Rosetta
-------------------------------

Modelling checklist:

- Use latest available CDM version
- No syntax warnings or errors
- Model compiles, with no static compilation errors
- Additional test samples provided (if use-case is not covered by existing samples)
- All translate regression tests expectations for mapping, validation and qualification maintained or improved
- All model components have descriptions
- Documentation adjusted (if necessary)
- Release note provided

Use latest available CDM version
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Once ready to start modelling, the contributor can log into Rosetta and start using the Rosetta Design application to create a workspace and edit the model, referring to the Rosetta Design Guide.

No syntax warning or error
^^^^^^^^^^^^^^^^^^^^^^^^^^
Model changes require use of the Rosetta DSL syntax, for further information refer to the Rosetta DSL Documentation and Rosetta Syntax Validation Guide.  All syntax warnings and errors must be resolved before contributing the changes.

Model compilation
^^^^^^^^^^^^^^^^^

Model changes can also cause static compilation errors when changes conflict with static function or mapper implementations (Rosetta Static Compilation Error Guide, TODO).  Rosetta support can help resolve these issues before the changes are contributed.  If significant work is required to resolve these errors, then the contributor should contact ISDA to discuss allocating resources to assist.

Testing
^^^^^^^

The CDM has adopted a test-driven approach to development by mapping model components to existing sample data (e.g., FpML documents or other existing standards).  Mappings are specified in the DSL as synonyms, refer to the DSL Mapping Guide for details. The model is regression tested using the mappings to translate the sample data and then comparing against the expected number of mapped data points, validation and qualification results. Model changes can be tested against the sample data using the Translate tab in Rosetta (Rosetta Translate Guide). When editing existing model components, the corresponding synonyms should be updated to maintain or improve existing levels. When adding new model components, new sample data and corresponding synonym mappings should also be provided so the new use-case can be added to the set of regression tests.

All model components have descriptions
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

All model components (e.g. types, attributes, conditions, functions etc.) should be specified with descriptions in accordance with the CDM Style Guide (TODO migrate to the documentation website).

Documentation
^^^^^^^^^^^^^

The CDM documentation must be kept up-to-date with the model in production. Where applicable, the contributor should provide accompanying documentation (in text format) that can be added to the CDM documentation site for their proposed changes. When a model change impacts those snippets, or if new relevant snippets should be added to support the documentation, those snippets should be provided together with the documentation update.

Release note
^^^^^^^^^^^^

In addition to the model changes, a release note should be provided that concisely describes the high-level conceptual design, model changes and how to review (TODO release notes style guide).

Contributing model changes using Rosetta
----------------------------------------

Contribution checklist:

- Use Rosetta to contribute model changes to GitHub, specifying meaningful title and description
- Notify ISDA of the submitted contribution via Slack or email, including:

  - Any notes on expected mapping, validation or qualification changes
  - Additional data samples, if necessary
  - Release notes and, if necessary, any additional materials or documentation

Once the model changes have been completed, use Rosetta to submit changes to GitHub for review (Rosetta Contribute Workspace Guide), specifying a meaningful title and description.  Contributed changes are submitted as a Pull Request on GitHub, and will invoke build process to compile and run all CDM unit tests, and regression tests.  Note that is it not yet possible to use Rosetta to contribute updated test expectations, documentation, release notes, or new sample data so these must be provided to ISDA via Slack or email. 

Reviewing the model changes
---------------------------

Review checklist:

- Review GitHub pull request to assert that:

  - Model changes fulfil the proposed design and use-case requirements.
  - Synonyms have been updated and output json looks correct.  Note that it is not possible to verify mapping, validation and qualification expectations have been maintained by looking at the GitHub pull request and CDM build only.  Refer to downstream projects section for more details.
  - Contributed model version is not stale and does not conflict with any recent changes.
  - Changes are in accordance with the CDM governance guidelines.

- CDM build process completed with no errors or test failures.
- Review additional samples provided (if use-case is not covered by existing samples).
- All model components have descriptions.
- Release note provided.
- Additional documentation provided (if necessary).
- Review feedback sent to the contributor as required via Slack, email or in meetings.

Following the initial model review, a number of technical tasks may be required before the review can be finally approved, merged and released:

- **Stale CDM version**: Contribution is based on an old CDM version and model changes conflict with more recent changes. If the conflicting change is available in Rosetta, the contributor should be asked to update their contribution to the latest version and resubmit.  If the conflicting change is not yet available in Rosetta, then contact REGnosys.
- **Failed unit tests**: Java unit tests in the CDM project may fail due to problems in the contributed changes, alternatively it may be that the test expectations need to be updated.  The reviewer should determine the test failure cause and notify either the contributor or REGnosys.
- **Documentation code snippets**: To avoid stale documentation, the CDM build process verifies that any code snippets in the documentation exist in the CDM model.  The reviewer should update the documentation code snippets by editing documentation.rst file on GitHub.
- **Additional documentation**: If the contributor provided additional documentation, the reviewer should update the CDM documentation by editing documentation.rst file on GitHub.
- **Code generation**: Model changes may cause code generator failures (e.g., Java, C#, Scala, Kotlin etc.). In the unlikely event of code generation failures, contact REGnosys.
- **Downstream dependencies**: The CDM has a number of dependent projects, and it is possible that model changes may cause these downstream projects to fail.  Contact REGnosys to test and update if necessary:

  - **Translate**: The regression tests in this project compare the contributed model against the expected number of mapping, validation and qualification results. Due to the contributed model changes, it is likely that there will be expectation mismatches that cause this build to fail.
  - **CDM Portal**: compile and test.
  - **CDM Java Examples**: compile and test.

Once technical tasks have been completed and the CDM and all downstream builds are successful then the change can be merged on to master.

Releasing model changes (Rosetta Deploy)
----------------------------------------

Once the contributed model change has been merged to master, a new release can be built, tested and deployed.

Release checklist:

- Update CDM version, which has a semantic version format e.g., MAJOR.MINOR.REVISION where:

  - MAJOR – incremented for major CDM milestones.
  - MINOR – incremented for significant or non-backwards compatible changes.
  - REVISION – incremented for minor changes or bug fixes.

- Build release candidate, and test.
- Build documentation website release candidate, and test.
- Deploy release candidates and notify channels
