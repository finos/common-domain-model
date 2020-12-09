# *DSL Syntax: Disjoint Keyword*

_What is being released_

Added the new `disjoint` keyword which can be used to write an expression that compares two lists to determine if there are any common elements.  If there are no common elements, the lists are disjoint, and the expression will evaluate to true.

Given two lists of floating rate indexes, e.g.

`before -> ... -> floatingRateIndex disjoint after -> ... -> floatingRateIndex`

Then the expression will evaluate to true if every "after" floating rate index is different from every "before" floating rate index.

# *Bug Fix: Data Rule Java Code Generation*

_What is being released_

Fix bug in the Java code generation to allow data rules to be invoked after each function invocation.

_Review Directions_

In the CDM Portal, go to the Downloads page, and download the Java Examples. Review the example in Java class, `com.regnosys.cdm.example.template.Validation`.
