.. |trade|  unicode:: U+02122 .. TRADE MARK SIGN

Overview of the ISDA CDM 
========================
.. role:: raw-html(raw)
    :format: html

**Continuous Integration:** |Codefresh build status| :raw-html:`<br />`

What is the ISDA CDM
--------------------

The ISDA Common Domain Model (CDM) is a standardised, machine-readable and machine-executable blueprint for how financial products are traded and managed across the transaction lifecycle. The CDM has been initially developed for the derivatives market and has since been extended to cover cash securities, securities financing and commodities.

Having a single, digital processing standard for trade events and actions will enhance financial markets' operational efficiency by:

* **Enabling inter-operability and straight-through processing** across firms, solutions and platforms, reducing the need for reconciliation caused by variations in how each firm records trade lifecycle events,
* **Accelerating financial technology innovation** by providing a common, readily operational foundation for how technologies like distributed ledger, smart contracts, cloud and artificial intelligence can be applied to financial markets,
* **Delivering better regulatory oversight** by promoting transparency and alignment between regulators and market participants and consistency in regulatory reporting, through a shared golden source of trade data.

Further detailed information about the CDM and its application can be found in the following resources:

* A high-level `Fact Sheet <https://www.isda.org/a/z8AEE/ISDA-CDM-Factsheet.pdf>`_ of the ISDA CDM and additional information is available on the ISDA website (`www.isda.org <http://www.isda.org/>`_),
* particularly with the referred `Short Video <https://www.isda.org/2017/11/30/what-is-the-isda-cdm/>`_,
* It is based on the design principles specified as part of ISDA’s October 2017 `CDM concept paper <https://www.isda.org/a/gVKDE/CDM-FINAL.pdf>`_,
* The ISDA CDM is openly accessible to all industry participants through the `CDM Portal <https://portal.cdm.rosetta-technology.io>`_.

which the *ISDA CDM Design Working Group* is tasked with implementing.


The CDM Components
------------------

**There are three sets of CDM components** that are laid-out in the below ISDA CDM Components Diagram:

* The Rosetta DSL
* The ISDA CDM Distribution
* CDM Applications

.. figure:: documentation/source/cdm-components-diagram.png

Rosetta DSL
^^^^^^^^^^^

The Rosetta DSL corresponds to the infrastructure component of ther CDM. As a domain model, the CDM is written in a Domain-Specific Language (DSL) called *Rosetta*, comprising a syntax (or *grammar*) and code generators.

To enable adoption and implementation of the CDM by the community of industry participants, the Rosetta DSL and its default code generator (Java) is open source under an Apache 2.0 licence and hosted in a dedicated `Rosetta DSL repository <https://github.com/REGnosys/rosetta-dsl>`_. A further `Code Generators repository <https://github.com/REGnosys/rosetta-code-generators>`_ is also open source under an Apache 2.0 license, allowing industry participants to write code generators for any other language.

The `Rosetta DSL documentation <https://docs.rosetta-technology.io/dsl/index.html>`_ presents the Rosetta DSL and the mechanism to write and use code generators. The documentation details all the modelling components available in the Rosetta syntax and their purpose, with examples drawn from the CDM.

The ISDA CDM Distribution
^^^^^^^^^^^^^^^^^^^^^^^^^

The ISDA CDM is distributed to industry participants subject to the ISDA CDM open source licence. The distribution can be downloaded from the CDM Portal.

Components
""""""""""

The ISDA CDM distribution comprises three main sets of components:

* **Model Definition**, which corresponds to the model as expressed in the Rosetta DSL syntax and contained into a set of *.rosetta* files. All model definitions are further detailed in the `Common Doamin Model Section`_ section of the documentation.
* **Code Distribution**, currently available as Java and JSON.  As the Rosetta syntax represents not just data components but also logic, the JSON representation has quite a limited scope and usefulness but is being used in practice by downstream consumers of the CDM.
* **Default Implementation**. While the two above components represent the essence of the model and are meant to be used as such by implementors, the Default Apps correspond to default implementations which can either be used as such, or be disabled or extended by those participants.  An example of such would be the ``key`` implementation, which uses the de-facto Java hash code function, but which might be deemed as inappropriate by some participants and hence be replaced by some alternative implementation.

Versioning
""""""""""

The CDM is released using the semantic versioning system. 

The format of a version number is MAJOR.MINOR.PATCH (e.g. ``1.23.456``), where the:

* MAJOR (``1``) version change introduces *backward incompatible* changes and will be used as high level release name (e.g. ``ISDA CDM Version 1``)
* MINOR (``23``) version change may or may not introduce backward incompatible changes, for example supporting a new type of event or feature or upgrading to a new version of the Rosetta DSL.
* PATCH (``456``) version when a backward compatible change is made, for example adding a new class, attribute or updating documentation. An implementor would not have to make any changes to update to this release.

The MAJOR.MINOR.PATCH numbers may increment by more than one unit. This is because release candidates can be created, but not released as further changes are made and then released altogether.

CDM Applications
^^^^^^^^^^^^^^^^

It is expected that a rich eco-system of licensed application components from service providers that are based upon the CDM  will develop over time. REGnosys have taken the initiative to develop an offering of solutions to assist market participants in making use of the CDM. In particular, the CDM Portal provides UI components allowing those participants to browse through the CDM.

ISDA doesn't endorse any of those application components.


.. |Codefresh build status| image:: https://g.codefresh.io/api/badges/pipeline/regnosysops/REGnosys%2Frosetta-cdm%2Frosetta-cdm?branch=master&key=eyJhbGciOiJIUzI1NiJ9.NWE1N2EyYTlmM2JiOTMwMDAxNDRiODMz.ZDeqVUhB-oMlbZGj4tfEiOg0cy6azXaBvoxoeidyL0g&type=cf-1
   :target: https://g.codefresh.io/pipelines/rosetta-cdm/builds?repoOwner=REGnosys&repoName=rosetta-cdm&serviceName=REGnosys%2Frosetta-cdm&filter=trigger:build~Build;branch:master;pipeline:5a86c209eaf77d0001daacb6~rosetta-cdm


