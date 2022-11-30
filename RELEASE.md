# *Infrastructure - Qualification*

_What is being released?_

This release contains a bug fix related to qualification.  If multiple qualification matches are found, no qualifier will now be set, and an error will be generated.

_Review Directions_

This bug fix does not affect any examples in CDM as none were qualifying with multiple matches.  

The fixed can be found in com.regnosys.rosetta.common.postprocess.qualify.QualificationResult.java.
