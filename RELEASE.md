# *DSL - Java Code Generation For Functions*

_What is being released?_

This release contains improvements to the Java code generation for functions related to the handling `condition` and `post-condition` failures.  The default behaviour is to throw an exception, however handling can be configured to allow for any implementor-specific requirements, e.g., additional logging, error reporting etc.   

_Review Directions_

In the CDM Portal, download the Java distribution and inspect the generated function classes, and the default handler class `org.isda.cdm.function.DefaultConditionValidator.java`.
