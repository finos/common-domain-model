# *Rosetta Syntax Upgrade - Data Types*

What is being released

As part of on-going improvements, the Rosetta syntax has been upgraded to consolidate language features. This work aims to simplify the syntax needed when contributing to the CDM. This will become increasingly important as the number of CDM contributors increase and as CDM contributions starting coming from a variety of sources.

This is the fourth release in a series of that will cover the scope described in the Rosetta Syntax Upgrade Wiki[1].

In this release, the representation of data types were upgraded to bring greater readability of data types in the model and greater consistency of syntax between data types and functions.

The new syntax focuses on the essence of a data type definition and moves related extra details into annotations, such as the metadata attributes scheme and reference. These annotations can then be flexibly hidden from view for easier consumption of the model in textual or graphical form (to come in future releases). Technical symbols such as semi-colons and curly-braces were removed to bring further consistency between data types and functions. 

Validation logic concerning a data type is now represented directly on the type to more closely associate these model facets. Data Rules, Choice Rules and One Of  (rules) are now all represented within the ‘condition’ block on the data type. This eliminates the need to explicitly associate validation rules to data types, which also removes the need for the inconsistent syntax that was in use previous between the Data and Choice Rules. 

The new syntax that represents data types is characterised by the `type` keyword, moving away from the `class` keyword, which can draw erroneous and confusing comparisons to Java classes. The Rosetta Type is different in that Rosetta Types represent data structures and data validation only - no further functionality is allowed on the type.

Review direction

To see the new syntax, make use of the Textual Browser in the CDM Portal. See especially the Interest Rate Payout object (line 8360) which gives a good representation of the changes. 

The `key` keyword has been moved into an annotation under the `type`’s name as it is not critical to the data definition itself. The semi-colons at the end of each attribute has also been eliminated, along with the curly-braces. All validation logic associates with the Interest Rate Payout are now defined directly on the Interest Rate Payout type. 

[1] https://github.com/REGnosys/rosetta-dsl/wiki/Rosetta-Syntax-Upgrade)
