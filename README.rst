.. |trade|  unicode:: U+02122 .. TRADE MARK SIGN

Overview of the ISDA CDM
========================

What is the ISDA CDM
--------------------

The ISDA Common Domain Model (CDM) is an initiative that ISDA has spearheaded to produce a common, robust, digital blueprint for how derivatives are traded and managed across their lifecycle.

ISDA anticipates that establishing such digital data and processing standards will lead to the following benefits:

* **Reduce the need for reconciliation** to address mismatches caused by variations in how each firm records trade lifecycle events,
* **Enable consistency** in regulatory compliance and reporting,
* **Accelerate automation and efficiency** in the derivatives market,
* **Provide a common foundation** for new technologies like distributed ledger, cloud and smart contracts to facilitate data consistency,
* **Facilitate interoperability** across firms and platforms.

A high-level `presentation <https://www.isda.org/a/z8AEE/ISDA-CDM-Factsheet.pdf>`_ of the ISDA CDM and additional information is available on the ISDA website (`www.isda.org <http://www.isda.org/>`_), particularly with the referred `Short Video <https://www.isda.org/2017/11/30/what-is-the-isda-cdm/>`_. It is based on the design principles specified as part of ISDA’s October 2017 `CDM concept paper <https://www.isda.org/a/gVKDE/CDM-FINAL.pdf>`_, which the *ISDA CDM Design Working Group* is tasked with implementing.

The ISDA CDM is openly accessible to all industry participants through the `CDM Portal <https://portal.cdm.rosetta-technology.io>`_.

The CDM Components
------------------

**There are three sets of CDM components** that are laid-out in the below ISDA CDM Components Diagram:

.. figure:: documentation/source/cdm-components-diagram.png

Rosetta
^^^^^^^

Rosetta corresponds to the CDM infrastructure component. As a domain model, the CDM is written in a Domain-Specific Language (DSL) called the *Rosetta DSL*, comprising a syntax (or *grammar*) and default code generators (currently Java).

To enable adoption and implementation of the CDM by the community, the Rosetta DSL is open source under an Apache 2.0 licence and hosted in a dedicated `Rosetta DSL repository <https://github.com/REGnosys/rosetta-dsl#the-rosetta-dsl>`_. A further `Code Generators repository <https://github.com/REGnosys/rosetta-code-generators>`_ is also open source under an Apache 2.0 license, allowing industry participants to write code generators for any other language.

The `Rosetta DSL documentation <https://docs.rosetta-technology.io/dsl/index.html>`_ presents the Rosetta DSL and the mechanism to write and use code generators. The documentation details all the modelling artefacts available in the Rosetta syntax and their purpose, with examples drawn from the CDM.

The ISDA CDM Distribution
^^^^^^^^^^^^^^^^^^^^^^^^^

The ISDA CDM Distribution is made available to participants through download from the CDM Portal and is subject to the ISDA CDM licence. The most crucial components of this ISDA CDM Distribution are:

* **Model Definition**, which corresponds to the actual model as expressed in the Rosetta syntax and which components are further detailed as part of the CDM Modelling Artefacts section of this documentation.
* **Model Code Projection**, currently available as Java and JSON.  As the Rosetta syntax represents not just data components but also logic, the JSON representation has quite a limited scope and usefulness but is being used in practice by downstream consumers of the CDM.
* **Default Apps**. While the two above components represent the essence of the model and are meant to be used as such by implementors, the Default Apps correspond to default implementations which can either be used as such, or be disabled or extended by those participants.  An example of such would be the ``key`` implementation, which uses the de-facto Java hash code function, but which might be deemed as inappropriate by some participants and hence be replaced by some alternative implementation.

CDM Applications
^^^^^^^^^^^^^^^^

It is expected that a rich eco-system of licensed application components from service providers that are based upon the CDM  will develop over time. REGnosys have taken the initiative to develop an offering of solutions to assist market participants in making use of the CDM. In particular, the CDM Portal provides UI components allowing those participants to browse through the CDM.

ISDA doesn't endorse any of those application components.
