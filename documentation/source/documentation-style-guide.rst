CDM Documentation Style Guide
=============================

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
-----------

The CDM
^^^^^^^

The model should be referred to as *the CDM*, without any ownership mention. The only exception is the `Overview of the ISDA CDM <https://docs.rosetta-technology.io/cdm/readme.html>`_ section at the beginning of the user documentation when the CDM is introduced as *the ISDA CDM*.

General data definition components
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The two data definition components should be referred to as follows:

#. *Data type*: defines an entity with a description, attributes, and where applicable, conditions.

   #. Not *type*.
   #. Not *class*: although these are appropriate terms for some of the distributions of the CDM into other languages, they are not applicable for all.

#. *Attribute*: defines a member of a data type.

   #. Not *field*.
   #. Not *element*.

Specific components
^^^^^^^^^^^^^^^^^^^

Product
"""""""

#. *Financial Product*. The user documentation defines `Financial Product <https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#financial-product>`_ as the highest level of the hierarchy of the universe of products. The term *Financial Product* should be used consistently throughout the documentation and wherever applicable in the model descriptions when describing the broad set of products.
#. *Contractual Product*. Similarly, `Contractual Product <https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#contractual-product>`_ is defined as a subset of Financial Products and should be used consistently in any documentation.

   #. Not *Derivatives*.
   #. Not *OTC*. Contractual Product is at a higher hierarchy level than OTC Derivatives because it can include other types of products such as Security Financing.

Event
"""""

#. *Business Event*. In the CDM, a `Business Event <https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#business-event>`_ represents an event that may occur during the lifecycle of a trade, such as an amendment, a termination, a reset or a payment.

   #. Not *Lifecycle Event*. the term *Business Event* should be consistently used when referring to these data structures in the CDM documentation.
  
#. *Primitive Event*. In the CDM, a `Primitive Event <https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#primitive-event>`_ represents a building block component used to specify business events in the CDM.

   #. Not *Primitive* (stand-alone). In the CDM documentation, the word *Primitive* always needs to be qualified with *Event*, because the word *Primitive* may be associated to very different meanings, e.g. in computing.

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
     - `The Common Domain Model <https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#>`_
   * - 2
     - `---------------`
     - L font and bold
     - `Product Model <https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#product-model>`_, `Legal Agreements <https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#legal-agreements>`_
   * - 3
     - `^^^^^^^^^^^^^^^`
     - M font and bold
     - `Tradable Product <https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#tradableproduct>`_
   * - 4
     - `"""""""""""""""`
     - S font (same as content), but bold
     - `Price Quantity <https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#pricequantity>`_

The RST editing syntax in which the user documentation is written is a standard web mark-up language, for which a reference guide can be found at: https://sublime-and-sphinx-guide.readthedocs.io

2. Logical organization and order:

   #. The user documentation should walk the user through the model from the top down, beginning at a description of the primary components.
   #. Levels 1 and 2 should include a bullet point list of the sub sections that will be described (bullets formatted with the insertion of an asterisk followed by a space).

3. Define business terms and CDM terminology early in a section so that the broader audience understands the model.
4. Details about a topic should be presented in the section for that topic:

   #. In the case of a model component, the description of the component should always be followed by an example.  
   #. Most or all of the explanation should occur before the example, not after, unless the example was needed as context for an explanation.
   #. Transitions from one topic to another should be used to help guide the user through the model.

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
-----

Content style
^^^^^^^^^^^^^

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
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

1. Data types and attributes display rules:

   #. Data types and attributes should be identified in the editor with code quotes, where the text between the quotes will appear in a special block format as illustrated here: ``LegalAgreementBase``.
   #. If the same word or phrase is used in a business context, as part of an explanation, then the words should be spaced and titled normally and the special format is not required: e.g. “Tradable products are represented by...”.

2. Code snippets should be preceded by the string: ``.. code-block:: Language`` (where the Language could be any of Haskell, Java, JSON, etc.), followed by a line spacing before the snippet itself. The entire snippet should be indented with one space, to be identified as part of the code block and formatted appropriately. Indentation can be produced inside the snippet itself using further double space. Meta-data such as data type descriptions or synonyms that appear in the CDM should be excluded from the code snippet, unless the purpose of the snippet is to illustrate those.

Example of how a code snippet should be edited in the documentation:

.. code-block:: MD

 .. code-block:: Haskell
 
  type EventEffect:
    effectedTrade TradeState (0..*)
      [metadata reference]
    trade TradeState (0..*)
      [metadata reference]
    productIdentifier ProductIdentifier (0..*)
      [metadata reference]
    transfer TransferPrimitive (0..*)
      [metadata reference]
    
And the result will be rendered as:

.. code-block:: Haskell

 type EventEffect:
   effectedTrade TradeState (0..*)
     [metadata reference]
   trade TradeState (0..*)
     [metadata reference]
   productIdentifier ProductIdentifier (0..*)
     [metadata reference]
   transfer TransferPrimitive (0..*)
     [metadata reference]

.. note:: Code snippets that appear in the user documentation are being compared against actual CDM components during the CDM build process, and any mismatch will trigger an error in the build. This mechanism ensures that the user documentation is kept in sync with the model in production prior to any release.

Fonts, Text Styles, and Spaces
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

#. Bold should be used sparingly:
   
   #. Only in the beginning of a section when there is a salient point to emphasize, like a tag line - the bold line should be syntactically complete and correct.
   #. In the editor, bold is specified with double asterisks before and after the word or phrase.

#. Italics
   
   #. Italics should be used when defining an unusual term for the first time rather than using quotes, for example to identify something CDM specific, such as the concept of Primitive Events.
   #. In the editor, italics is specified with a single asterisk ``*`` before and after the word or phrase.
   
#. Single space should be used in-between sentences, not double space.

Style references for additional guidance
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

#. `New Hart’s Rules <https://global.oup.com/academic/product/new-harts-rules-9780199570027>`_: An updated version of this erstwhile comprehensive style guide for writers and editors using British English, published by the Oxford University Press. Invaluable as an official reference on proofreading and copy-editing.  Subjects include spelling, hyphenation, punctuation, capitalisation, languages, law, science, lists, and tables. An earlier version coined the phrase Oxford Comma in July 1905.
#. `Eats, Shoots & Leaves: The Zero Tolerance Approach to Punctuation <https://www.lynnetruss.com/books/eats-shoots-leaves/>`_:  A light-hearted book with a serious purpose regarding common problems and correctness for using punctuation in the English language.
