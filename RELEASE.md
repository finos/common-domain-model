# *DSL - CDM Distribution - Java Code Generation For Functions with Data Validations*

_What is being released?_

This release improves the Java code generation for functions related to the handling `condition` and `post-condition` failures.  The default implemetation will will throw an exception. Implementers can reconfigure or extend this outcome based on their specific requirements, for example additional logging, error reporting etc.   

_Review Directions_

In the CDM Portal, download the Java distribution and inspect the generated function classes, and the default handler class `org.isda.cdm.function.DefaultConditionValidator.java`.
