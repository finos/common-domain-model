# *DSL Update: better validation of only-element*

_Background_

The new release of the Rosetta DSL includes improved validation of `only-element` expressions. This change notified us on current faulty uses of `only-element` in the model.

_What is being released?_

Faulty `only-element` uses where resolved.

_Review Directions_
 
Try to apply the `only-element` operator on a singular expression. Rosetta will not accept this anymore.
