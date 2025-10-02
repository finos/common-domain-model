# *Broker Equity Option - Exercise Procedure Mapping fix*

_Background_

For broker equity options, in the switchover from synonyms to functions, the exercise terms was mapped but the exercise procedure was left empty, meaning for all equity broker option products, this was left blank.

_What is being released?_

Additional mapping have been added under the 'ingest' namespace to:

- 'common' function renaming 'fpmlAutomaticExerciseIsApplicable' to 'fpmlAutomaticExercise' to make the code easier to read
- 'brokerequityoption' where 'exerciseProcedure' has been mapped under 'exerciseTerms', as this was previously set to 'empty'

_Review Directions_

Changes can be reviewed in PR: [#4070](https://github.com/finos/common-domain-model/pull/4070)
