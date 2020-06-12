.. |trade|  unicode:: U+02122 .. TRADE MARK SIGN

Overview of the ISDA CDM 
========================
.. role:: raw-html(raw)
    :format: html

**Continuous Integration:** |Codefresh build status| :raw-html:`<br />`

What is the ISDA CDM
--------------------

The ISDA Common Domain Model (CDM) is a standardised, machine-readable and machine-executable blueprint for how financial products are traded and managed across the transaction lifecycle. The CDM has been initially developed for the derivatives market and has since been extended to cover cash securities, securities financing and commodities.

A single, digital processing standard for trade events and actions enhances financial markets' operational efficiency in several ways:

* **Enables inter-operability and straight-through processing** across firms, solutions and platforms, reducing the need for reconciliation caused by variations in how each firm records trade lifecycle events.
* **Accelerates financial technology innovation** by providing a common, readily operational foundation for how technologies like distributed ledger, smart contracts, cloud and artificial intelligence can be applied to financial markets.
* **Delivers better regulatory oversight** by promoting transparency and alignment between regulators and market participants and consistency in regulatory reporting, thanks to a shared golden source of trade data.

Further detailed information about the CDM and its application are available in the following resources:

* The `ISDA CDM Fact Sheet <https://www.isda.org/a/z8AEE/ISDA-CDM-Factsheet.pdf>`_, published by ISDA in 2018, gives an overview of what the CDM is and its intended benefits.
* The `CDM Introduction Video <https://vimeo.com/372578450>`_ gives a tour of the main features of the model. It is based on a version of the CDM from October 2019.
* The `CDM Concept Paper <https://www.isda.org/a/gVKDE/CDM-FINAL.pdf>`_, published by ISDA in 2017, specifies the original CDM design principles.
* Additional information and announcements about the CDM can be found in the `Market Infrastructure and Technology Section <https://www.isda.org/category/infrastructure/market-infrastructure-technology/>`_ of the ISDA website.


The CDM Components
------------------

**There are three sets of CDM components**, as laid-out in the below ISDA CDM components diagram:

* The Rosetta DSL
* The ISDA CDM Distribution
* CDM Application

.. figure:: documentation/source/cdm-components-diagram.png

The Rosetta DSL
^^^^^^^^^^^^^^^

The Rosetta DSL corresponds to the infrastructure component of the CDM. The CDM is a domain model written in a Domain-Specific Language (DSL) called *Rosetta*, comprising a syntax (or *grammar*) and code generators.

To enable adoption and implementation of the CDM by the community of industry participants, the Rosetta DSL and its default code generator (Java) are open source under an Apache 2.0 `licence <https://github.com/REGnosys/rosetta-dsl/blob/master/LICENSE>`_. Further code generators are also open source under an Apache 2.0 `license <https://github.com/REGnosys/rosetta-code-generators/blob/master/LICENSE>`_, allowing industry participants to write and share code generators for any other language.

The `Rosetta DSL documentation <https://docs.rosetta-technology.io/dsl/index.html>`_ presents the Rosetta DSL and the mechanism to write and use code generators. The documentation details all the modelling components available in the Rosetta syntax and their purpose, with examples drawn from the CDM.

The ISDA CDM Distribution
^^^^^^^^^^^^^^^^^^^^^^^^^

The ISDA CDM distribution is openly accessible to all industry participants and can be downloaded from the `CDM Portal <https://portal.cdm.rosetta-technology.io>`_. The ISDA CDM is distributed subject to the ISDA CDM open source licence.

Components
""""""""""

The ISDA CDM distribution comprises three main sets of components:

* **Model definition**, which corresponds to the model as expressed in the Rosetta DSL syntax and contained into a set of *.rosetta* files. All model definitions are further detailed in the `Common Domain Model Section <https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html>`_ of the documentation.
* **Executable code distribution**, currently available in Java, Scala, TypeScript and DAML languages and automatically generated from the model definitions using the Rosetta DSL code generators. Once a code generator is implemented for a particular language, the corresponding code generation can be included as part of the CDM build and release process, allowing the CDM to be automatically distributed in that language going forward. Although sometimes considered "code", structured representations of the CDM in standard messaging formats (such as JSON) are not currently provided. They can only support the data model but no executable logic so have been deemed of limited scope and usefulness.
* **Default implementation**, comprising manually-written code (in Java) which, combined with the auto-generated code, provides a complete implementation of the model. This hand-written code is distributed together with the CDM to facilitate adoption by firms, which can directly use the CDM distribution to set-up and test an implementation. Such default implementation can either be used as such or be disabled or extended by industry participants in their implementation. An example of such is the *key* meta-data implementation used for cross-referencing: the default implementation uses the de-facto Java hash function, which firms may deem inappropriate and may replace by an alternative implementation.

.. note:: While the Java executable code distribution is complete, i.e. it represents the entire CDM as defined in Rosetta (plus any associated default implementation), the other distributions only capture parts of the model: for instance, only data types but not functions in the Scala and TypeScript distributions.

Versioning
""""""""""

The CDM is developed, built and released using standard software source-control management. Each new released version is associated to a *release note* that describes the change introduced by that new version. The CDM release history is available in the `Release Section <https://docs.rosetta-technology.io/cdm/releases/all.html>`_ of the CDM documentation.

The CDM is released using the semantic versioning system. The format of a version number is MAJOR.MINOR.PATCH (e.g. ``1.23.456``), where the:

* MAJOR (``1``) version change introduces *backward incompatible* changes and will be used as high level release name (e.g. ``ISDA CDM Version 1``)
* MINOR (``23``) version change may or may not introduce backward incompatible changes, for example supporting a new type of event or feature or upgrading to a new version of the Rosetta DSL.
* PATCH (``456``) version when a backward compatible change is made, for example adding a new class, attribute or updating documentation. An implementor would not have to make any changes to update to this release.

The MAJOR.MINOR.PATCH numbers may increment by more than one unit. This is because release candidates can be created, but not released as further changes are made and then released altogether.

CDM Application
^^^^^^^^^^^^^^^

An eco-system of CDM-based application components from service providers is developing in order to support the adoption of CDM and the implementation of CDM-based production systems by industry participants. These applications may be open source themselves or licensed under commercial terms.

As an early participant in the development of the CDM, REGnosys have developed an offering to assist other market participants in making use of the CDM, in particular:

* The CDM Portal provides a user interface allowing users to navigate through and download the CDM.
* Rosetta Core is a Software Development Kit (SDK or *dev-kit*) for the CDM, corresponding to an *editable* version of the CDM Portal. Rosetta Core consists of an integrated set of tools for adopting, editing and implementing the model, allowing the indutry community to directly contribute code to the CDM.

ISDA doesn't endorse any of those application components.


The CDM Governance
------------------

The CDM governance framework regulates the development of the CDM standard in open source. The CDM governance framework is articulated around three Committees:

#. The *CDM Working Group(s)* are responsible for developing the CDM standard.
#. The *CDM Architecture and Review Committee* is responsible for developing the operating guidelines and for vetting the changes proposed by the Working Groups. The operating guidelines specify the CDM technical and modelling guidelines and the way the CDM changes and extensions proposed by the Working Groups are reviewed and approved by the Architecture & Review Committee.
#. The *CDM Executive Committee* is responsible for setting the strategy, promoting adoption of the standard and overseeing the activity of the Working Groups and the Architecture & Review Committee.

Proposals for amendment to the CDM can be created upon the initiative of members of a Committee or by any users of CDM within the community who are not a current Committee member. In each case, a proposal, which may or may not include code, must be developed in line with the operating guidelines and submitted to ISDA staff and the Architecture & Review Committee for approval. In some cases, a proposal may require a Working Group to be established for the purpose of developing the proposal.


.. |Codefresh build status| image:: https://g.codefresh.io/api/badges/pipeline/regnosysops/REGnosys%2Frosetta-cdm%2Frosetta-cdm?branch=master&key=eyJhbGciOiJIUzI1NiJ9.NWE1N2EyYTlmM2JiOTMwMDAxNDRiODMz.ZDeqVUhB-oMlbZGj4tfEiOg0cy6azXaBvoxoeidyL0g&type=cf-1
   :target: https://g.codefresh.io/pipelines/rosetta-cdm/builds?repoOwner=REGnosys&repoName=rosetta-cdm&serviceName=REGnosys%2Frosetta-cdm&filter=trigger:build~Build;branch:master;pipeline:5a86c209eaf77d0001daacb6~rosetta-cdm
