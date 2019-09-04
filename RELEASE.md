
_What is being released_

*Rosetta Syntax Upgrade*

As part of on-going improvements, the Rosetta syntax has been upgraded to consolidate language features. This work aims to simplify the syntax needed when adding to, and editing the CDM. This will become increasingly important as the number of CDM contributors increase and as CDM contributions starting coming from a variety of sources.

This is the second release in a series of that will cover the scope described in the Rosetta Syntax Upgrade Wiki[1].

In this release, the `func` syntax and supporting infrastructure were upgraded to support all remaining use-cases of `spec`, `function` and `calculation`. This consolidation of syntax simplifies the writing of Functions in the model and forces syntactic consistency across the model.

*Other Changes*

Fixed bug related `Date` serialization which affected reg reporting functionality in Rosetta Core, and caused failures in the  testing infrastructure.

_Review directions_

From the CDM Portal, make use of the Textual Browser to inspect syntax changes in the model. Download the CDM Distribution Pack to inspect the change to generated Java code artefacts.

[1] https://github.com/REGnosys/rosetta-dsl/wiki/Rosetta-Syntax-Upgrade)
