.. include:: links.rst

Development Guidelines
======================

.. _governance:

Governance
----------

The CDM is an open source project with a governance framework that regulates any contribution to its on-going development. A three-tiered committee structure pilots the development of the CDM:

#. The *CDM Executive Committee* is accountable for setting the strategy, promoting adoption of the standard, and overseeing the activity of the Working Groups and the Architecture & Review Committee. Members are senior executives appointed by the ISDA Board considering their strategic influence in the decision making at their firm and active contribution to the development of the CDM.

#. The *CDM Architecture and Review Committee* is responsible for specifying the technical and modelling guidelines and reviewing and approving proposals for new modelling components introduced by the CDM Working Groups. Members include subject matter experts, senior technologists, as well as practitioners in business process, legal documentation, and technical modelling.

#. The *CDM Working Groups* are assembled per subject matter or use cases to actively develop and implement concretely targeted elements of the CDM. Participants include ISDA members and non-members from the CDM user community who commit time and effort for the development and implementation of the CDM as a global standard.

Committee members or any user of CDM within the community can propose amendments in accordance with the following process:

.. figure:: images/CDM-Development-Process.png

A proposal can be defined at a conceptual level or a logical level (i.e. in code). In each case, the proposal must be developed in line with the CDM :ref:`design-principles` and :ref:`agile-development-approach` and submitted to ISDA staff and the Architecture & Review Committee for approval. In some instances, the proposal may not be immediately approved but may be assigned to an existing or new Working Group for the purpose of reviewing, revising or extending the proposal.

Once approved, the amendment will be scheduled to be merged with the CDM's main code branch by the CDM Maintainers.

.. _design-principles:

Design Principles
-----------------

The purpose of this section is to detail the CDM design principles that any contribution to the CDM development must adhere to. The CDM supports the market objectives of standardisation via a set of design principles that include the following concepts:

* **Normalisation** through abstraction of common components, e.g. *price* or *quantity*
* **Composability** where objects are composed and qualified from the bottom up
* **Mapping** to existing industry messaging formats, e.g. *FpML*
* **Embedded logic** to represent industry processes, e.g. data validation or state-transition logic
* **Modularisation** into logical layers, using *namespace* organisation

Normalisation
^^^^^^^^^^^^^

**To achieve standardisation across products and asset classes, the CDM identifies logical components that fulfil the same function** and normalises them, even when those components may be named and treated differently in the context of their respective markets. By helping to remove inefficiencies that siloed IT environments can create (e.g. different systems dealing with cash, listed, financing and derivative trades make it harder to manage aggregated positions), such design reaffirms the goal of creating an inter-operable ecosystem for the processing of transactions across asset classes.

An example of this approach is the normalisation of the concepts of *quantity*, *price* and *party* in the representation of financial transactions. The CDM identifies that, regardless of the asset class or product type, a financial transaction always involves two counterparties *trading* (i.e. buying or selling) a certain financial product in a specific quantity and at a specific price. Both quantity and price are themselves a type of *measure*, i.e. an amount expressed in a specific unit which could be a currency, a number of shares or barrels, etc. An exchange rate between currencies, or an interest rate, also fit that description and are represented as prices.

This approach means that a single logical concept such as *quantity* represents concepts that may be named and captured differently across markets: e.g. *notional* or *principal* amount etc. This in turn allows to normalise processes that depend on this concept: for instance, how to perform an allocation (essentially a split of the quantity of a transaction into several sub-transactions) or an unwind, instead of specialised IT systems handling it differently for each asset class.

**It is imperative that any request to add new model components or extend existing ones is analysed against existing components** to find patterns that should be factored into common components and avoid specialising the model according to each use case. For instance, in the model for *averaging* options (often used for commodity products, whereby multiple price observations are averaged through time to be compared to the option's strike price), the components are built and named such that they can be re-used across asset classes.

Composability
^^^^^^^^^^^^^

**To ensure re-usability across different markets, the CDM is designed as a composable model** whereby financial objects can be constructed bottom-up based on building-block components. A composable and modular approach allows for a streamlined model to address a broad scope of operational processes consistently across firms' front-to-back flows and across asset classes. The main groups of composable components are:

* **Financial products**: e.g. the same *option* component is re-used to describe option payouts across any asset class, rather than having specialised *Swaption*, *Equity Option* or *FX option* etc. components.
* **Business events** that occur throughout the transaction lifecycle are described by composing more fundamental building blocks called *primitive events*: e.g. a *partial novation* is described by combining a *quantity change* primitive event (describing the partial unwind of the transaction being novated away) and a *contract formation* primitive event (describing the new contract with the novation party).
* **Legal agreements** that document the legal obligations that parties enter into when transacting in financial products are constructed using *election* components associated to functional logic that is re-usable across different types of agreement: e.g. the same logic defining the calculation of margin requirements can be re-used across both initial and variation margin agreements.

In this paradigm, the type of object defined by the CDM, whether a financial product, business event or legal agreement, is not declared upfront: instead, the type is inferred through some business logic applied onto its constituents, which may be context-specific based on a given taxonomy (e.g. a product classification).

**The benefit of this approach is that consistency of object classification is achieved through how those objects are populated**, rather than depending on each market participant's implementation to use the same naming convention. This approach also avoids the model relying on specific taxonomies, labels or identifiers to function and provides the flexibility to maintain multiple values from different taxonomies and identifier sets as data in the model related to the same transaction. This has a number of useful application, not least for regulatory purposes.

Mapping
^^^^^^^

**To facilitate adoption by market participants, the CDM is made compatible with existing industry messaging formats.** This means that the CDM does not need to be implemented "wholesale" as a replacement to existing messaging systems or databases but can coexist alongside existing systems, with a translation layer. In fact, the CDM is designed to provide only a logical model but does not prescribe any physical data format, neither for storage nor transport. This means that translation to those physical data formats is built-in, and the CDM is best thought of as a logical layer supporting inter-operability between them.

.. note:: Although the CDM features a *serialisation* mechanism (currently in JSON), this format is only provided for the convenience of representing physical CDM objects and is not designed as a storage mechanism.

The need for such inter-operability is illustrated by a typical trade flow, as it exists in derivatives: a trade may be executed using the pre-trade FIX protocol (with an FpML payload representing the product), confirmed electronically using FpML as the contract representation, and reported to a Trade Repository under the ISO 20022 format. What the CDM provides is a consistent logical layer that allows to articulate the different components of that front-to-back flow.

In practice, mapping to existing formats is supported by *synonym* mappings, which are a compact description in the CDM of how data attributes in one format map to model components. In turn, those synonym mappings can support an *ingestion* process that consumes physical data messages and converts them into CDM objects.

**The CDM recognises certain formats as de-facto standards that are widely used to exchange information between market participants.** Their synonym mappings are included and rigorously tested in each CDM release, allowing firms that already use such standards to bootstrap their CDM implementation. Besides, because most standard messaging formats are typically extended and customised by each market participants (e.g. FpML or FIX), the CDM allows the synonym representation for those standards to be similarly inherited and extended to cover each firm's specific customisation.

Embedded logic
^^^^^^^^^^^^^^

**The CDM is designed to lay the foundation for the standardisation, automation and inter-operability of industry processes**. Industry processes represent events and actions that occur through the transaction’s lifecycle, from negotiating a legal agreement to allocating a block-trade, calculating settlement amounts or exchanging margin requirements.

While ISDA defines the protocols for industry processes in its documentation library, differences in the implementation minutia may cause operational friction between market participants. Even the protocols that have a native digital representation have written specifications which require further manual coding in order to result in a complete executable solution: e.g. the validation rules in FpML, the Recommended Practices/Guidelines in FIX or CRIF for SIMM and FRTB, which are only available in the form of PDF documents.

Traditional implementation of a technical standard distributed in prose comes with the risk of misinterpretation and error. The process is duplicated across each firm adopting the standard, ultimately adding up to high implementation costs across the industry.

**By contrast, the CDM provides a fully specified processing model that translates the technical standards supporting industry processes** into a machine-readable and machine-executable format. Systematically providing the domain model as executable code vastly reduces implementation effort and virtually eliminates the risk of inconsistency. For instance, the CDM is designed to provide a fully functional event model, where the state-transition logic for all potential transaction lifecycle events is being specified and distributed as executable code. Another CDM feature is that each model component is associated with data validation constraints to ensure that data is being validated at the point of creation, and this validation logic is distributed alongside the model itself.

Modularisation
^^^^^^^^^^^^^^

**The set of files that define the CDM data structures and functions are organised into a hierarchy of namespaces**. The first level in the namespace hierarchy corresponds to the layer of the CDM that the components belong to, and those CDM layers are organised from inner- to outer-most as follows:

.. figure:: images/namespaces.png

Namespaces have many benefits:

* Accelerated understanding of the model by allowing users to easily see a high-level view of the model and easily find, select, and study specific areas of interest
* Faster and easier to find data types and functions for referencing/use in new components
* Allowing for partial adoption of areas of interest in the model
* Smaller upgrades representing new versions limited to the name spaces that are impacted

Each of these higher-level namespaces is further divided into lower-level namespaces. The independent components in each namespace are organised according to their core purpose but can be referenced from anywhere in the model to allow all the components to work together for a complete modelling solution. E.g. below is the *product* namespace:

.. figure:: images/product-namespace.png

**When developing new CDM components, the positioning of those components in the namespace hierarchy is critical** as part of the design (or potentially the re-organising of the hierarchy following the new development), to ensure the CDM remains well organised.

.. _agile-development-approach:

Agile Development Approach
--------------------------

The on-going development of the CDM adheres to a methodology inspired by the *Agile* software development framework. This methodology is based on two high-level principles:

#. Focus on business value from the user's perspective, encapsulated in the concept of *user story*
#. Delivery of small, releasable changes that contribute to that business value (sometimes referred to as *shippable increments*) - i.e. no "big bang" changes

Development by the CDM Maintainer team is being planned along a series of 2-week *sprints* aligned onto the CDM Architecture and Review Committee cycle. This development is guided by high-level priorities set on a quarterly basis.

Outside `contributions <#how-to-contribute>`_ that are proposed by industry members, whilst not formally enlisted in the sprint process, usually generate additional tasks (design, review, deployment etc.) for the CDM Maintainer team that will need to be included in a sprint. Those outside contributions still need to comply with the above development principles to ensure a smooth integration with the rest of the development by the CDM Maintainer team.

Focus on business value
^^^^^^^^^^^^^^^^^^^^^^^

Any CDM development work must start from a business case describing the business benefit being sought from the proposed development, as seen from the perspective of the user who will enjoy that benefit. This is know as a *user story* in the agile framework.

What a user story looks like
""""""""""""""""""""""""""""

A good user story comprises three elements which can be summarised into one sentence: who, what and why.

- *Who* defines the user (or more generally the set of users defined by some profiling) that will enjoy that benefit
- *What* defines the feature to be delivered, as a verbal proposition applying to the user - i.e. "the user can do this or that"
- *Why* specifies the benefit, i.e. what makes this feature important for the user

Since a story is from the user's perspective, it must be written in plain language, or at least in language that is intelligible by that user. It must be free of technical jargon that only the developer of that feature may understand, so that it can be communicated to the user.

Further details about the business case (including documents, pictures, sample data etc.) may be attached to a user story to complement that summary, but the sumamry itself should be whole and self-explanatory.

Story vs task
"""""""""""""

By contrast, how a story gets delivered is *not* part of that story:

- *How* describes the set of *tasks* that will need to be executed to deliver the story. This is where the story is being decomposed into units of work written in terms that are actionable by the developers.

Tasks will typically map to steps in the software development lifecycle: analysis, design, build, test, deploy. They must be planned before the story is scheduled for development, as part of the *sprint planning* process. Based on this planning, a set of stories is being prioritised for development in the upcoming sprint. Those stories are communicated to users at the Architecture and Review Committee, but not their underlying tasks or techical details.

Some examples
"""""""""""""

Instead of writing:

 "Commodity Swap Follow Up w/Enum values, mapping, samples"

Which is unclear, assumes some context which not all users may have ("follow-up" from what?) and has no explicit benefit, write:

 "A Commodity user of the CDM can map a set of basic Commodity attributes to represent simple Commodity derivative products."

In which some of the underlying tasks may be:

 - "Map basic Commodity enumerations"
 - "Add Commodity samples to the ingestion test pack"
 - etc.

As a rule, a task is written in the imperative mode as an injunction to the developer, whereas a story should be written as a sentence starting with the user's profile as the subject of a verbal proposition. A story written in the imperative mode is more likely a task and improperly written.

For instance, instead of writing:

 "Release member contribution for DayCountFraction."

Write:

 "A user of interest rate products is able to model products that use the ACT/364 day count convention in the CDM."

Where the "Release" injunction is attached to a deploy-type task.

Delivery of small releasable units
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

What is a releasable unit
"""""""""""""""""""""""""

To maintain on-going momentum in the development of the CDM, delivery is organised around small but releasable units of change. This means that any change must be small enough to be achievable during a single sprint (usually), but large enough to be releasable as a cohesive whole. In particular, a change unit should not be regressive or break existing functionality, even if only temporarily (except when retiring such functionality is the purpose of that change) - in agile terms, it must be *shippable*. This principle applies to on-going development by the CDM Maintainers as well as to outside contributions.

**The CDM development approach aligns the concepts of user story and releasable unit**, therefore stories should be calibrated to be achievable during a single sprint.

 1 contribution = 1 releasable unit = 1 user story

.. note:: A CDM `release <cdm-overview.html#versioning>`_ may contain more than 1 releasable unit. Every unit should still be shippable in isolation, even if they may end-up being shipped as a group.

Epics
"""""

Some larger changes may not be achievable in a single sprint: e.g. if they impact a large number of objects or core features of the model. Such changes are known as *epics* and need to be decomposed into several user stories. Developers or contributors are responsible for ensuring that the changes are being delivered in small, incremental units and must plan accordingly.

Particularly for complex stories, not all of that story's tasks may necessarily be known in advance and therefore guaranteed to fit in one single sprint. A story may demand some prior analysis before it can be decomposed into development tasks. It may also require several design iterations before development can start. Those prior discovery tasks should be fit into a single sprint and the actual development scheduled in a subsequent sprint.

The discovery phase may reveal that the story is not well calibrated and is in fact an epic that should be further decomposed. This is an acceptable scenario which does not contravene the prescribed development approach, as long as development has not yet started. In that case the story should be requalified and several stories be spun-out as a result, before development can start.

How to Contribute
-----------------

The purpose of this section is to provide guidance for submitting, reviewing and releasing changes to the CDM contributed by the wider industry community including market participants, trade associations and technology or service vendors. It describes:

- What a Contributor should do to edit and contribute changes to the CDM
- What a Maintainer should do to review the changes
- How to release a new CDM version once changes have been approved

The steps required to change the CDM are aligned with the software development lifecycle typically applicable to the development of any other software. This development lifecycle is illustrated in the diagram below. Each step is associated to the relevant component of the `Rosetta`_ platform that can be used to support the development of the CDM.

.. figure:: images/SDLC.png

Development of the CDM is also enabled through alternative modelling platforms (for instance `Legend`_). Regardless of the modelling platform used, modelling and contribution to the CDM should go through the contribution check-list below.

.. note:: This documentation is not an endorsement of any modelling platform and associated products and CDM users remain invited to leverage the tools of their choosing. This contributiob guide has been contextualised with references to Rosetta to ease understanding and align with the current process.

Before you start modelling
^^^^^^^^^^^^^^^^^^^^^^^^^^

Before you start modelling, please make sure you have gone through the following pre-modelling checklist:

- Review the :ref:`design-principles` and :ref:`governance`
- Review the `Rosetta Starter Guide`_ or equivalent in your chosen modelling platform.

In addition, for large model changes or changes to core data types, it is recommended that the Contributor reviews the :ref:`agile-development-approach` and follows these steps:

- **Define use case**. Identify and document one or more use cases with details (e.g. a sample trade).
- **Draft conceptual design** (high level). Draft a conceptual view showing the set of data types, their definitions (and/or sample attributes but not the whole set of attributes), their relationships to each other, and, if applicable, a workflow.
- **Design approval**. Obtain approval of high-level conceptual design from CDM stakeholders:

  - CDM Owners (ISDA and other involved Trade Associations, where applicable)
  - CDM Sub-Working Group, if applicable
  - CDM Architecture and Review Committee

- **Quality assurance**. Seek the early appointment of at least one CDM Maintainer who can assist modelling discussions and provide early feedback. CDM Maintainers are appointed by the CDM Owners as and when relevant. Please contact `cdmInfo@isda.org <cdminfo@isda.org>`_.

Editing the model
^^^^^^^^^^^^^^^^^

When editing the CDM, please go through the following modelling checklist:

- CDM version: use latest available version
- Syntax: no syntax warnings or errors
- Compilation: model compiles ok with no "static compilation" errors
- Testing: all translate regression tests expectations for mapping, validation and qualification maintained or improved. Additional test samples may be needed if use-case is not covered by existing samples.
- Namespace: all model components positioned in the correct namespace
- Descriptions: all model components have descriptions

The following sections detail that checklist. When using the Rosetta Design web application to edit the model, the Contributor should also refer to the `Rosetta Design Guide`_.

CDM version
"""""""""""

To the extent possible it is recommended that the Contributor keeps working with a version of the CDM that is as close as possible to the latest to minimise the risk of backward compatibility.

Please refer to the `Source Control Integration Guide`_ for more information.

Syntax
""""""

The model is represented in the Rosetta DSL syntax. All syntax warnings and errors must be resolved to have a valid model before contributing any changes. For further guidance about features of the syntax, please refer to the `Rosetta DSL Documentation`_.

In Rosetta Design, that syntax is automatically checked live as the user edits the model, as described in the `Rosetta Design Content Assist Guide`_.

Compilation
"""""""""""

Normally, once the model is syntactically correctly edited, valid code is being auto-generated and compiled. However, certain model changes can cause compilation errors when changes conflict with static code (e.g. certain mapper implementations).

The Rosetta support team can help resolve these errors before the changes are contributed. In most cases you will be able to contact the team via the `In-App chat`_. If the  support team identifies that significant work may be required to resolve these errors, they will notify the Contributor who should then contact the CDM Maintainer originally appointed for the proposed change and/or CDM Owners. The latter will be able to assist in the resolution of the issues.

For more information about auto-compilation using the Rosetta DSL, please refer to the `Rosetta Auto Compilation Guide`_.

Testing
"""""""

The CDM has adopted a test-driven development approach that maps model components to existing sample data (e.g., FpML documents or other existing standards).  Mappings are specified in the CDM using ``synonym`` which are collected into a Translation Dictionary, and the sample data are collected into a Test Pack. Each new model version is regression-tested using those mappings to translate the sample data in the Test Pack and then comparing against the expected number of mapped data points, validation and qualification results.

When using Rosetta to edit the model, contributors are invited to test their model changes live against the Test Pack using the Rosetta Translate application, referring to the `Rosetta Translate Guide`_. When editing existing model components, the corresponding synonyms should be updated to maintain or improve existing mapping levels. When adding new model components, new sample data and corresponding synonym mappings should also be provided so the new use-case can be added to the set of regression tests.

Please refer to the `Mapping Guide`_ for details about the synonym mapping syntax.

Namespace
"""""""""

All model components should be positioned appropriately in the existing namespace hierarchy. If the proposed contribution includes changes to the namespace hierarchy, those changes should be justified and documented. Any new namespace should have an associated description, and be imported where required.

Please refer to the :ref:`namespace-documentation` section for more details.

Descriptions
""""""""""""

All model components (e.g. types, attributes, conditions, functions etc.) should be specified with descriptions in accordance with the `CDM Documentation Style Guide <#documentation-style-guide>`_.

Contributing model changes
^^^^^^^^^^^^^^^^^^^^^^^^^^

Contribution checklist
""""""""""""""""""""""

Before you start contributing your model changes, please go through the following contribution checklist:

- Specify a meaningful title and description for the contribution
- Notify the CDM Maintainers (via email or Slack) of the submitted contribution
- Include:

  - Any notes on expected mapping, validation or qualification changes (success numbers should not decrease)
  - Additional data samples, if necessary
  - Documentation adjustment, if necessary
  - Release notes
  - Any other additional materials or documentation that may help with the review and approval process

.. note:: A contribution should be a whole `releasable unit <#what-is-a-releasable-unit>`_ and its size calibrated in accordance with the CDM's `agile development approach <#agile-development-approach>`_.

Contributing
""""""""""""

Changes are contributed by submitting a Pull Request for review into the CDM source-control repository. This pull request will invoke a build process to compile and run all CDM unit tests and regression tests.

Given the alignment:

 1 pull request = 1 contribution = 1 releasable unit = 1 user story,

we recommend labelling the pull request with the user story label, i.e. "STORY-XYZ: ..." to facilitate its tracking.

.. note:: All contributions are submitted as candidate changes to be incorporated under `the CDM licence`_.

When using Rosetta to contribute model changes, the contribution interface allows to specify a title and description for the contribution. Those inputs are used to create a Pull Request on a one-off branch in the source-control repository. Please refer to the `Rosetta Workspace Contribution Guide`_ for more information.

.. note:: It is not yet possible to contribute updated test expectations, documentation, release notes or new sample data using Rosetta, so these must be provided to the CDM Maintainers via Slack or email.

Documentation
"""""""""""""

The CDM documentation must be kept up-to-date with the model in production. Where applicable, the Contributor should provide accompanying documentation (in text format) that can be added to the CDM documentation for their proposed changes.

The documentation includes code snippets that directly illustrate explanations about certain model components, and those snippets are validated against the actual model definitions. When a model change impacts those snippets, or if new relevant snippets should be added to support the documentation, those snippets should be provided together with the documentation update.

Release note
""""""""""""

A release note should be provided with the proposed model change that concisely describes the high-level conceptual design, model changes and how to review. Please refer to the :ref:`content-of-release-notes` for further guidance on editing release notes.

Reviewing model changes
^^^^^^^^^^^^^^^^^^^^^^^

Review checklist
""""""""""""""""

Before starting to review a contribution, the CDM Maintainer should go through the following review checklist:

- Review Pull Request to assert that:

  - Model changes fulfil the proposed design and use-case requirements
  - Synonyms have been updated and output (JSON) looks correct
  - Contributed model version is not stale and does not conflict with any recent changes
  - Changes are in accordance with the CDM governance guidelines

.. note:: It is not yet possible to verify that mapping, validation and qualification expectations have been maintained by looking at the output of the Pull Request and CDM build only. Please refer to the :ref:`downstream-dependencies` section for more details.

- CDM build process completed with no errors or test failures
- Review additional samples provided (if use-case is not covered by existing samples)
- All model components positioned in the correct namespace
- All model components have descriptions
- Additional documentation provided, if necessary.
- Release note provided

Any review feedback should be sent to the Contributor as required via Slack, email or in direct meetings.

.. note:: Depending on the size, complexity or impact of a contribution, the CDM Maintainer can recommend for the contribution to be presented with an appropriate level of details with the CDM Architecture and Review Committee for further feedback. The CDM Maintainer will work with the Contributor to orchestrate that additional step. The additional feedback may recommend revisions to the proposed changes. When it is the case the review process will iterate on the revised proposal.

Model maintenance
^^^^^^^^^^^^^^^^^

Before the Pull Request can be merged into the CDM's main branch, some work is usually required by the Maintainer to preserve the integrity of the model source code and of its downstream dependencies.

Post-review technical tasks
"""""""""""""""""""""""""""

A number of technical tasks may need to be performed on the Pull Request once it is approved:

- **Stale CDM version**: Contribution is based on an old CDM version and model changes conflict with more recent changes. If the conflicting change is available in Rosetta, the contributor should be asked to update their contribution to the latest version and resubmit. If the conflicting change is not yet available in Rosetta, this merge will need to be handled by the CDM Maintainer.
- **Failed unit tests**: Java unit tests in the CDM project may fail due to problems in the contributed changes. Alternatively it may be that the test expectations need to be updated. The Maintainer should determine the cause of the test failure and notify either the Contributor or work on adjusting the test expectations.
- **Additional documentation**: If the contributor provided additional documentation, the Maintainer should update the CDM documentation by editing the *documentation.rst* file in GitHub.
- **Documentation code snippets**: To avoid stale documentation, the CDM build process verifies that any code snippets in the documentation exists and is in line with the model itself. The Maintainer should adjust or include any code snippets by editing the *documentation.rst* file on GitHub.
- **Code generation**: Model changes may cause code generator failures (e.g., Java, C#, Scala, Kotlin etc.). In the unlikely event of code generation failures, these will need to be addressed by the Maintainer.

.. _downstream-dependencies:

Downstream dependencies
"""""""""""""""""""""""

The CDM has a number of dependent projects that are required for the model to be successfully distributed. It is possible that model changes may cause these downstream projects to fail. The Maintainer will need to test and, if necessary, update those before the changes can be released.

- **Translate**: The regression tests in this project compare the contributed model against the expected number of mapping, validation and qualification results. Due to the contributed model changes, it is likely that there will be expectation mismatches that cause this build to fail.
- **CDM Portal**: compile and test.
- **CDM Java Examples**: compile and test.

.. note:: In most cases, the post-review technical tasks and downstream dependencies require software engineering expertise in addition to CDM expertise. Additional technical support from the CDM Maintainer team may need to be called upon to address those.

The change can be merged into the main CDM code base only upon:

- approval by CDM Maintainers and/or CDM Architecture and Review Committee,
- successful completion of all the above technical tasks, and
- successful builds of the CDM and all its downstream dependencies.

Releasing model changes
^^^^^^^^^^^^^^^^^^^^^^^

Once the contributed model change has been merged, a new release can be built, tested and deployed. The Maintainer will work with the CDM Owners and the Contributor on a deployment timeline.

The following release checklist should be verified before deploying a new model:

- Update CDM version, which uses the semantic version format (see CDM :ref:`versioning` Documentation )
- Build release candidate, and test
- Build documentation website release candidate, and test
- Deploy release candidate and notify channels if need be
- (Currently done at a later stage) Update the latest CDM version available in Rosetta

.. note:: When the release process is handled through Rosetta Deploy, the Maintainer should contact the Rosetta support team to request that deployment and discuss a timeline for the release.

Documentation Style Guide
-------------------------

The purpose of this section is to provide form and substance recommendations for editors of CDM documentation. "Documentation" in this context means any form of written guidance to CDM users and includes:

- the CDM user documentation
- release notes provided with each CDM release
- any description developed in the logical model itself, which includes:

  - data types and attributes
  - enumerations
  - functions and rules
  - test descriptions
  - any other areas of the logical model where a textual description may be provided

The intended audience for the CDM documentation includes software developers, data modelers, legal experts, business experts, and other subject matter experts who may have expertise in one area related to financial products, but are not experts in every area.

The baseline for the recommendation is standards for spelling, punctuation, and other style rules applicable to British English. The recommendation in this document extends this rule set with a set of guidelines applicable to documentation for logical models. The recommendation does not provide an exhaustive list of all of the standard British English rules, but provides selective examples that are common to documentation in general.

Writing and editing style is subjective and a matter of personal preferences, rather than right or wrong. The following guidelines are intended to ensure that the documentation provides consistent styling, regardless of who writes it, but should not be interpreted as an authoritative source on "good" styling.

Unless otherwise noted, the recommended rules apply to all forms of CDM documentation. When rules are applicable only to certain forms of documentation (for instance, the logical model descriptions or the release notes), they will be specified as such.

Terminology
^^^^^^^^^^^

The CDM
"""""""

The model should be referred to as *the CDM*, without any ownership mention. The only exception is the `Overview of the ISDA CDM <cdm-overview.html>`_ section at the beginning of the user documentation when the CDM is introduced as *the ISDA CDM*.

General data definition components
""""""""""""""""""""""""""""""""""

The two data definition components should be referred to as follows:

#. *Data type*: defines an entity with a description, attributes, and where applicable, conditions.

   #. Not *type*.
   #. Not *class*: although these are appropriate terms for some of the distributions of the CDM into other languages, they are not applicable for all.

#. *Attribute*: defines a member of a data type.

   #. Not *field*.
   #. Not *element*.

Product
"""""""

#. *Financial Product*. The user documentation defines :ref:`financial-product` as the highest level of the hierarchy of the universe of products. The term *Financial Product* should be used consistently throughout the documentation and wherever applicable in the model descriptions when describing the broad set of products.
#. *Contractual Product*. Similarly, :ref:`contractual-product` is defined as a subset of Financial Products and should be used consistently in any documentation.

   #. Not *Derivatives*.
   #. Not *OTC*. Contractual Product is at a higher hierarchy level than OTC Derivatives because it can include other types of products such as Security Financing.

Event
"""""

#. *Business Event*. In the CDM, a :ref:`business-event` represents an event that may occur during the lifecycle of a trade, such as an amendment, a termination, a reset or a payment.

   #. Not *Lifecycle Event*. the term *Business Event* should be consistently used when referring to these data structures in the CDM documentation.

#. *Primitive Event*. In the CDM, a :ref:`primitive-event` represents a building block component used to specify business events in the CDM.

   #. Not *Primitive* (stand-alone). In the CDM documentation, the word *Primitive* always needs to be qualified with *Event*, because the word *Primitive* may be associated to very different meanings, e.g. in computing.

Completeness
^^^^^^^^^^^^

User Documentation
""""""""""""""""""

#. The user documentation should provide an applicable introduction and should have a section for every primary component of the CDM.
#. Each section should provide enough business context and explanations of the model so that the average reader in the target audience understands the purpose of the component and its role in the model.
#. Each section should have at least one example of a data structure

Logical Model
"""""""""""""

#. Every data type, attribute, enumerated value, function, and test should have a description that describes its purpose in the context of the CDM.

Accuracy
^^^^^^^^

#. Descriptions should accurately describe the current state of the model. This seems obvious, but there are many possible ways for introducing misalignments, including: an anticipated change never occurred, or the author incorrectly interpreted the structure, or the data structure changed while the documentation or description was not updated.
#. Subtasks in a design process should include an assessment of documentation and descriptions that will be required to be changed or created, and should include the content.
#. A release checklist should verify that the affected documentation and descriptions are completed accordingly.

Content Guidelines
^^^^^^^^^^^^^^^^^^

General guidelines
""""""""""""""""""

1. Data Definition components (e.g. data types, attributes, enumerations and enumerated values) should be explained in business terms.

   #. The description of objects in the model should begin with the purpose of the object. The purpose should explain what the object is, not "what it is not".
   #. Data type description should begin with a verb that describes what the type does.
   #. The logical model identifies data types without needing a description, therefore, the description should not begin with a phrase like: “A data type that does..."
   #. Attribute description should articulate the use of the type in the context of the attribute.
   #. The description should not be tautological, e.g. PartyRole <defines the party role> is not compliant with these guidelines.
   #. References to a similar attribute in  FpML should not be used as a crutch in place of explaining a data type, attribute, etc.
   #. In most cases, where a reference to FpML is considered useful, it should be placed at the end of a description in the logical model, or in a note at the end of a section in the user documentation.  In the case of the logical model, note that synonyms for FpML are provided, therefore it should not be necessary to reference FpML in every case.

Example of a non-compliant description:

.. code-block:: sourcecode

  <"A data type to represent a financial product. With respect to contractual products, this class specifies the pre-execution product characteristics (the ContractualProduct class). This class is used as underlying for the option exercise representation, which makes use of the contractualProduct attribute to support the swaption use case, with the exercise into a swap. In a complete workflow, the swaption contract itself then needs to be superseded by a swap contract underpinned by the exercised swap as a contractualProduct.">

Instead a compliant description would state:

.. code-block:: sourcecode

  <"Represents a financial product. With respect to a contractual products, this data type specifies the pre-execution product characteristics...”>

Another non-compliant example:

.. code-block:: sourcecode

  <"This class corresponds to the FpML CalculationAgent.model.">

2. The description of data objects or the overall model should be focused on the current state, there should be no reference to the history of the model or a future state.

   #. The history of the model is not relevant in this context. It is sufficient to describe how the model currently works.
   #. Forward-looking statements can create a perception that the product is not finished, and become a distraction to explaining what the product does. Moreover, these future plans may never materialise.
   #. Given these rules, phrases such as “the model currently does….” should be excluded because any documentation must be a description of what the model currently does.

Heading styles and flow in the user documentation
"""""""""""""""""""""""""""""""""""""""""""""""""

1. Heading styles. The user documentation is edited in the *RST (reStructured Text)* mark-up language, which is then rendered into Html in the CDM documentation website using *Sphinx*. For headings to be rendered with the correct structure, they should be annotated according to the following table:

   #. If using a header to identify a section to describe a component, then use headers to describe other components that are at the same level.
   #. Sub-headings should have a name distinct from the higher level heading. e.g. if Legal Agreement is the Heading Level 2, then there should not be a heading at level 3 or 4 with the exact same title.

.. list-table:: Heading Styles
   :widths: 10 25 25 25
   :header-rows: 1

   * - Heading Level
     - Notation (underline in .rst)
     - Relative font size (as seen by users)
     - Section Example
   * - 1
     - `===============`
     - XL font and bold
     - :ref:`common-domain-model`
   * - 2
     - `---------------`
     - L font and bold
     - :ref:`product-model-page`, :ref:`legal-agreements-page`
   * - 3
     - `^^^^^^^^^^^^^^^`
     - M font and bold
     - :ref:`tradable-product`
   * - 4
     - `"""""""""""""""`
     - S font (same as content), but bold
     - :ref:`price-quantity`

The RST editing syntax in which the user documentation is written is a standard web mark-up language, for which a reference guide can be found at: https://sublime-and-sphinx-guide.readthedocs.io

2. Logical organization and order:

   #. The user documentation should walk the user through the model from the top down, beginning at a description of the primary components.
   #. Levels 1 and 2 should include a bullet point list of the sub sections that will be described (bullets formatted with the insertion of an asterisk followed by a space).

3. Define business terms and CDM terminology early in a section so that the broader audience understands the model.
4. Details about a topic should be presented in the section for that topic:

   #. In the case of a model component, the description of the component should always be followed by an example.
   #. Most or all of the explanation should occur before the example, not after, unless the example was needed as context for an explanation.
   #. Transitions from one topic to another should be used to help guide the user through the model.

.. _content-of-release-notes:

Content of Release Notes
""""""""""""""""""""""""

Release notes are text describing the content of any new CDM release and are a critical component of the distribution of that release. Release notes are edited in the *Mark-Down (MD)* syntax, which is then rendered into Html in the various channels where the release is published.

1. release notes should begin with a high-level headline of the part of the model being changed, followed by "–" and a short headline description

   #. For example: "# Legal Agreement Model - Collateral Agreement Elections"

2. They should provide enough detail for a reviewer or other interested parties to be able to find and evaluate the change. For a data model change, for example, the data type and attributes should be named and the before/after states of the model explained, along with a justification in which the issue is summarised.
3.	If the release notes describe mapping rules, there should be explicit information about the examples affected and the change in resulting values for those examples.
4. If the release is documentation, it should specify exactly where the document was changed and why.
5. Special formatting rules related to use of the MD mark-up language:

   #. Headline should begin with a ``#``, as in the above example, so that it appears correctly formatted in Html
   #. ``*`` before and after text (no space) for bold
   #. ``_`` before and after text (no space) for italics
   #. ``–`` (plus a space) for bullets
   #. Backticks ````` before and after model components, e.g. data types, attributes, enums, function names, etc. for special code-style formatting

Example release notes formatted in MD:

.. code-block:: MD

  # *CDM Model: Expanded set of enumerations in RegulatoryRegimeEnum*

  _What is being released_

  Additional regimes have been added to the `RegulatoryRegimeEnum` which is used to express the required regimes for initial margin documentation. The `RegulatoryRegimeEnum` is used as an enumeration for attributes in the `ApplicableRegime` and `SubstitutedRegime` data types within the legal agreements model.

  The new enumerated values are `BrazilMarginRules`, `UnitedKingdomMarginRules`, `SouthAfricaMarginRules`, `SouthKoreaMarginRules`, and `HongKongSFCMarginRules`, all of which have come into force in January 2021.  Each of these enumerated values has a complete description that uses the text provided in the relevant regulatory supplement.

  _Review directions_

  In the CDM Portal select the Textual Browser, search for ‘ApplicableRegime’ and ‘SubstitutedRegime’, click on the ‘RegulatoryRegimeEnum’ next to the ‘regime’ attribute and observe the expanded list of regimes, including the ones noted above.

The MD editing syntax in which release notes are written is a standard web mark-up language, for which a reference guide can be found at: https://www.markdownguide.org/cheat-sheet/

.. note:: The MD syntax provides similar features to the RST syntax (used to edit the user documentation), but the special formatting characters are slightly different between the two. While RST allows richer features that are useful for a full documentation website, MD is preferred for release notes because Slack supports (a subset of) the MD language and can therefore serve as a release publication channel.

Style
^^^^^

Content style
"""""""""""""

1. Content should be correct with regard to grammar, punctuation, and spelling (in British English), including but not limited to the following rules:

   #. Grammatical agreement, e.g. data types need, not data types needs
   #. Punctuation:

      #. etc. requires a period.
      #. Complete sentences should end with a period or colon (there should be no need for a question mark or exclamation point in these artefacts).
      #. Incomplete sentences cannot end with a punctuation.  For example, “Through the ``legalAgreement`` attribute the CDM provides support for implementors to:" is an incomplete sentence and cannot end in a punctuation. This can be fixed by adding a few words, e .g. “Through the ``legalAgreement`` attribute the CDM provides support for implementors to do the following:"
      #. Always use the Oxford Comma (aka the Serial Comma) for clarity when listing more than two items in a sentence, e.g. “data types, attributes, and enumerated values.”  In extreme cases, failure to use this comma could be costly.

2. Other grammatical rules

   #. Agreement of numbers:  For example, if one sentence reads “the following initiatives…” , then it should be followed by more than one.
   #. Sentences should not end with a preposition

      #. Non-compliant example: “..to represent the party that the election terms are being defined for.”
      #. Compliant: “...to represent the party or parties for which the election terms are being defined.”

3. When a name or phrase is defined - continue to use it unless an alias has been defined. For example, one section reviewed had an expression "agreement specification details" but then switched to using "agreement content" without explanation. There is sufficient terminology to absorb, as such there is no need for synonyms or aliases, unless there are commonly used terms, in which case, they should be defined and one term should be used consistently.
4. User Documentation and descriptions should always be in the third person, for example: "the CDM model provides the following...". Never use the first person (including the use of "we").
5. In the user documentation, when there is a need for a long list, use bullets (``*`` or ``-`` followed by space, then text) as opposed to long sentences.
6. To the extent possible, use simple direct sentence structures, e.g. replace "An example of such" with "For example", or replace "Proposals for amendment to the CDM can be created upon the initiative of members of a Committee or by any users of CDM within the community who are not a current Committee member." with "Committee members or any user of CDM within the community can propose amendments to the CDM."
7. Exclude the usage of "mean to", “intends to”, or “looks to”.

   #. For example, "the model looks to use strong data type attributes such as numbers, boolean or enumerations whenever possible."
   #. Either the object works as designed or it does not. This expression might be used in a bug report when describing a function not working as intended but not to describe a production data model.

8.	Explain the CDM objects in an honest and transparent manner, but without criticism of the model. Sentences such as: "...which firms may deem inappropriate and may replace by..." or "the model is incomplete with regards to..." are unnecessary in a documentation. Rather, issues which may be identified in the CDM should be raised and addressed via the CDM governance structure.

Special format for CDM objects
""""""""""""""""""""""""""""""

1. Data types and attributes display rules:

   #. Data types and attributes should be identified in the editor with code quotes, where the text between the quotes will appear in a special block format as illustrated here: ``LegalAgreementBase``.
   #. If the same word or phrase is used in a business context, as part of an explanation, then the words should be spaced and titled normally and the special format is not required: e.g. “Tradable products are represented by...”.

2. Code snippets should be preceded by the string: ``.. code-block:: Language`` (where the Language could be any of Haskell, Java, JSON, etc.), followed by a line spacing before the snippet itself. The entire snippet should be indented with one space, to be identified as part of the code block and formatted appropriately. Indentation can be produced inside the snippet itself using further double space. Meta-data such as data type descriptions or synonyms that appear in the CDM should be excluded from the code snippet, unless the purpose of the snippet is to illustrate those.

Example of how a code snippet should be edited in the documentation:

.. code-block:: MD

 .. code-block:: Haskell

  type Party:
    [metadata key]
    partyId PartyIdentifier (1..*)
    name string (0..1)
      [metadata scheme]
    businessUnit BusinessUnit (0..*)
    person NaturalPerson (0..*)
    account Account (0..1)
    contactInformation ContactInformation (0..1)

And the result will be rendered as:

.. code-block:: Haskell

 type Party:
   [metadata key]
   partyId PartyIdentifier (1..*)
   name string (0..1)
     [metadata scheme]
   businessUnit BusinessUnit (0..*)
   person NaturalPerson (0..*)
   account Account (0..1)
   contactInformation ContactInformation (0..1)

.. note:: Code snippets that appear in the user documentation are being compared against actual CDM components during the CDM build process, and any mismatch will trigger an error in the build. This mechanism ensures that the user documentation is kept in sync with the model in production prior to any release.

Fonts, Text Styles, and Spaces
""""""""""""""""""""""""""""""

#. Bold should be used sparingly:

   #. Only in the beginning of a section when there is a salient point to emphasize, like a tag line - the bold line should be syntactically complete and correct.
   #. In the editor, bold is specified with double asterisks before and after the word or phrase.

#. Italics

   #. Italics should be used when defining an unusual term for the first time rather than using quotes, for example to identify something CDM specific, such as the concept of Primitive Events.
   #. In the editor, italics is specified with a single asterisk ``*`` before and after the word or phrase.

#. Single space should be used in-between sentences, not double space.

Style references for additional guidance
""""""""""""""""""""""""""""""""""""""""

#. `New Hart's Rules`_: An updated version of this erstwhile comprehensive style guide for writers and editors using British English, published by the Oxford University Press. Invaluable as an official reference on proofreading and copy-editing.  Subjects include spelling, hyphenation, punctuation, capitalisation, languages, law, science, lists, and tables. An earlier version coined the phrase Oxford Comma in July 1905.
#. `Eats, Shoots & Leaves: The Zero Tolerance Approach to Punctuation <https://www.lynnetruss.com/books/eats-shoots-leaves/>`_:  A light-hearted book with a serious purpose regarding common problems and correctness for using punctuation in the English language.
