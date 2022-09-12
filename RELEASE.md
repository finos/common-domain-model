# *DSL Update: Improved validation of only-element*

_Background_

This release of the Rosetta DSL includes improved validation of `only-element` expressions. This change notified us on current faulty uses of `only-element` in the model.

_What is being released?_

Faulty `only-element` uses where resolved, in particular when `only-element` operator is used on a singular expression. Rosetta will not accept this anymore.

_Review Directions_
 
In the CDM Portal, select Textual Browser and review usages of the `only-element` operator.
