# *Model Optimisation: Exercise Notice*

_What is being released_

The `ExerciseNotice` data structure is insufficient to address all use cases, for example when both parties have the right to exercise.​

To address all use cases, `ExerciseNoticeGiverEnum` has been introduced with values `Buyer`, `Seller`, `Both` and `AsSpecifiedInMasterAgreement`.

In addition, for greater clarity the `ExerciseNotice` attributes have been renamed to `exerciseNoticeGiver` and `exerciseNoticeReceiver`  and the descriptions have been updated.

_Review Directions_

In the CDM Portal, use the Textual Browser to review `ExerciseNotice` and `ExerciseNoticeGiverEnum`.
