# *Syntax Upgrade - Consistency across Synonym syntax*

_What is being released_

As part of on-going improvements, the Rosetta syntax has been upgraded to consolidate language features. This work aims to simplify the syntax needed when contributing to the CDM. This will become increasingly important as the number of CDM contributors increase and as CDM contributions starting coming from a variety of sources.

This is the fifth release in a series of that will cover the scope described in the Rosetta Syntax Upgrade Wiki[1].

In this release, the Synonym syntax used on `type`s and `enum`s have been consolidated to bring consistency. All Synonym syntax now follow a single set of grammar rules and are represented by a single meta-model (Ecore) for each of code generation. An enumeration of changes below:

1. Synonym Values, Synonym Path Values should both be inside double-quotes `" ... "`
2. All model paths are represented with the arrow syntax `->`. This applies to: 
    1. CDM model attribute paths i.e. `Event -> party -> assignedIdentifier`; 
    2. Enumeration values i.e. `PartyRoleEnum -> Seller`;
    3. Synonym path values i.e. `[synonym DTCC_11_0 value "OTC_RM" path "Header", "OTC_Matching" path "Body" set when "Header->OTC_RM->Manifest->TradeMsg->TransType" = "Trade”]`

For readability, auto-formatting feature was added to the new Syntax such that all CDM files and future contributions will have a consistent style in terms of white space i.e. new-lines, spaces and indentation.

This release also includes the following, related Bug Fix.  Support was added to the `as-key` keyword such that it can operate on attributes with multiple cardinality. CDM will be updated to make use of this bug fix in the following releases. 

_Review direction_

Make use of the Textual Browser Tile to inspect the new model syntax.  

[1] https://github.com/REGnosys/rosetta-dsl/wiki/Rosetta-Syntax-Upgrade 
