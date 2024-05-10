# *Product Model - Modification of AmericanExercise Condition in ExerciseTerms*

_Background_

The conditions under `ExerciseTerms` in the refactored `OptionPayout` are not sufficiently restrictive for American options. More specifically, the `AmericanExercise` condition should not only control that the `expirationDate` is present (which it already does through the `CommencementAndExpirationDate` condition), but that it is also **present only once** (not currently implemented in the model). This release aims at modifying the `AmericanExercise` condition to correct this.

_What is being released?_

- The `AmericanExercise` condition under the `ExerciseTerms` type has been modified to constrain the `expirationDate` field to only one occurrence.

_Backward Incompatible Changes_

It should be noted that this proposal contains a backwards incompatible change, given that a condition has been made stricter, but should not impact any of the actual implementations.

_Review Directions_

In Rosetta, select the Textual Browser and inspect the change identified above.

Changes can be reviewed in PR [#2914](https://github.com/finos/common-domain-model/pull/2914)
