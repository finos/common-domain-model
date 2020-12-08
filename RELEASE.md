# *DSL Syntax: Disjoint Keyword*

_What is being released_

Added the new `disjoint` keyword used to create an expression that is true if no element on the left side expression is equal to any element on the right side expression, e.g.

`before -> ... -> floatingRateIndex disjoint after -> ... -> floatingRateIndex`

The expression will only evaluate to true if every "after" floating rate index is different from every "before" floating rate index.

# *Bug Fix: Data Rule Java Code Generation*

_What is being released_

Fix bug in the Java code generation to allow data rules to be invoked after each function invocation.

_Review Directions_

In the CDM Portal, go to the Downloads page, and download the Java Examples. Review the example in Java class, `com.regnosys.cdm.example.template.Validation`.
