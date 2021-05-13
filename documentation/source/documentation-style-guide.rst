CDM Documentation Style Guide
=============================

The purpose of this section is to provide recommendations for CDM documentation style and substance. "Documentation" in this context means any form of written guidance to CDM users and includes:

- the CDM user documentation
- release notes provided with each CDM release
- any description developed in the logical model itself, which includes:

  - data types and attributes
  - enumerations
  - functions and rules
  - test descriptions
  - any other areas of the logical model where a textual description may be provided

The baseline for the recommendation is standards for spelling, punctuation, and other style rules applicable to British English, such as those found in `New Hart’s Rules <https://global.oup.com/academic/product/new-harts-rules-9780199570027>`_. The recommendation in this document extends this rule set with a set of guidelines applicable to documentation for logical models. The recommendation does not provide an exhaustive list of all of the standard British English rules, but provides selective examples that are common to documentation in general.

The intended audience for the CDM documentation includes software developers, data modelers, legal experts, business experts, and other subject matter experts who may have expertise in one area related to financial products, but are not experts in every area.

Unless otherwise noted, the recommended rules apply to all forms of CDM documentation. When rules are applicable only to certain forms of Documentation (for instance, the logical model descriptions or the release notes), they will be specified as such.

Terminology
-----------

The CDM
^^^^^^^

The model should be referred to as *the CDM*, without any ownership mention. The only exception is the `Overview of the ISDA CDM <https://docs.rosetta-technology.io/cdm/readme.html>`_ section at the beginning of the user documentation when the CDM is introduced as *the ISDA CDM*.

General data definition components
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The two data definition components should be referred to as follows:

#. Data type: defines an entity with a description, attributes, and where applicable, conditions.

   #. Not type.
   #. Not class: although these are appropriate terms for some of the distributions of the CDM into other languages, they are not applicable for all.

#. Attribute: defines a member of a data type.

   #. Not field.
   #. Not element.

Specific components
^^^^^^^^^^^^^^^^^^^

Product
"""""""

#. Financial Product. The user documentation defines `Financial Product <https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#financial-product>`_ as the highest level of the hierarchy of the universe of products. The term *Financial Product* should be used consistently throughout the documentation and wherever applicable in the model descriptions when describing the broad set of products.
#. Contractual Product. Similarly, `Contractual Product <https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#contractual-product>`_ is defined as a subset of Financial Products and should be used consistently in any documentation.

   #. Not Derivatives.
   #. Not OTC. Contractual Product is at a higher hierarchy level than OTC Derivatives because it can include other types of products such as Security Financing.

Event
"""""

#. Business Event. In the CDM, a `Business Event <https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#business-event>`_ represents an event that may occur during the lifecycle of a trade, such as an amendment, a termination, a reset or a payment.

   #. Not Lifecycle Event. the term *Business Event* should be consistently used when referring to these data structures in the CDM documentation.
  
#. Primitive Event. In the CDM, a `Primitive Event <https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#primitive-event>`_ represents a building block component used to specify business events in the CDM.

   #. Not Primitive. In the CDM documentation, the word *Primitive* always needs to be qualified with *Event*, because the word *Primitive* (stand-alone) may be associated to very different meanings, e.g. in computing.

Content
-------

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
